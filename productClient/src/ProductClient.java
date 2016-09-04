import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.yc.uploadClient.Utils.HttpConstants;
import com.yc.uploadClient.Utils.HttpUtil;
import com.yc.uploadClient.bean.FileProperty;


public class ProductClient {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text a;
	private Text b;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ProductClient window = new ProductClient();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(677, 462);
		shell.setText("添加产品");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(90, 36, 61, 17);
		lblNewLabel.setText("产品名:");
		
		text = new Text(shell, SWT.BORDER);
		text.setText("香蕉");
		text.setBounds(170, 36, 73, 23);
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(76, 90, 61, 17);
		lblNewLabel_1.setText("价格:");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setText("40");
		text_1.setBounds(170, 90, 73, 23);
		
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(76, 152, 61, 17);
		lblNewLabel_2.setText("图片一:");
		
		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(76, 215, 61, 17);
		lblNewLabel_3.setText("图片二:");
		
		a = new Text(shell, SWT.BORDER);
		a.setText("C:\\Users\\Administrator\\Pictures\\1.png");
		a.setBounds(170, 152, 363, 23);
		
		b = new Text(shell, SWT.BORDER);
		b.setText("C:\\Users\\Administrator\\Pictures\\2.png");
		b.setBounds(170, 215, 363, 23);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell,SWT.OPEN);   
				dialog.setFilterPath(   System.getProperty("user.home"));//设置初始路径   
				String fileName = dialog.open();//返回的全路径(路径+文件名)  
				if(  fileName==null ||  "".equals(fileName)){
					return;
				}
				a.setText(    fileName);
			}
		});
		btnNewButton.setBounds(550, 152, 80, 27);
		btnNewButton.setText("选择");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell,SWT.OPEN);   
				dialog.setFilterPath(   System.getProperty("user.home"));//设置初始路径   
				String fileName = dialog.open();//返回的全路径(路径+文件名)  
				if(  fileName==null ||  "".equals(fileName)){
					return;
				}
				b.setText(    fileName);
			}
		});
		btnNewButton_1.setBounds(550, 215, 80, 27);
		btnNewButton_1.setText("选择");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String pname=text.getText().trim();
				final String price=text_1.getText().trim();
				final String pic1=a.getText().trim();
				final String pic2=b.getText().trim();
				new Thread(new Runnable(){
					@Override
					public void run() {
						Map<String,String> params=new HashMap<String,String>();
						params.put("pname", pname);
						params.put("price", price);
						List<FileProperty> fps=new ArrayList<FileProperty>();
						fps.add(new FileProperty(  pic1.substring(pic1.lastIndexOf("\\")+1), new File( pic1), "image1","image/png"   ) );
						fps.add(new FileProperty( pic2.substring(pic2.lastIndexOf("\\")+1), new File( pic2), "image2","image/png" ) );
						try {
							boolean flag=HttpUtil.uploadFile(HttpConstants.BASEPATH+"productServlet.action", params, fps);
							if(  flag){
								Display.getDefault().asyncExec(new Runnable(){
									@Override
									public void run() {
										MessageDialog.openInformation(shell, "上传成功", "成功");
									}
								});
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						} 
					}
				}).start();
			}
		});
		btnNewButton_2.setBounds(224, 309, 80, 27);
		btnNewButton_2.setText("添加");

	}

}
