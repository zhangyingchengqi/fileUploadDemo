package com.yc.uploadClient.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yc.uploadClient.bean.FileProperty;

public class HttpUtil {
	
	
	/**
	 * descript: 用于文件上传的帮助类
	 * 直接通过http协议提交数据到服务器。 实现类似于下面web页面提交数据的功能 <form method="post"
	 * action="http://xxxx.xx:8080/pic/pic.do" enctype="multipart/form-data">
	 * <input type="hidden" name="op" value="add" /> 图片: <input name="pic"
	 * type="file" id="pic" /> 描述:<textarea name="desc" id="desc"></textarea>
	 * <input type="submit" name="Submit" value="提交" /> </form>
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static boolean uploadFile(String path, Map<String, String> params,
			List<FileProperty> files) throws UnknownHostException, IOException {
		Random r=new Random();
		// 属性数据的分隔线
		final String BOUNDARY = "---------------------------"+ r.nextInt(9999999);
		final String ENDLINE = "--" + BOUNDARY + "--\r\n";
		long fileInEntityDataLength = 0; // 存http协议实体部分文件数据的总长度
		// 下面开始计算http协议实体部分的总长度
		// 1.先计算文件部分包括文件属性和文件内容的长度
		for (FileProperty uploadFile : files) {
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFileName() + "\"\r\n");
			sb.append("Content-Type: " + uploadFile.getContenttype()
					+ "\r\n\r\n");
			sb.append("\r\n");
			// 以上形成了文件的属性值部分的参数长度
			fileInEntityDataLength += sb.toString().length();
			// 再加上文件内容的总长度
			if (uploadFile.getInputStream() != null) { // 如果用的是文件流的话，就计算流文件长度
				fileInEntityDataLength += uploadFile.getFile().length();
			} else { // 否则计算二进制文件长度
				fileInEntityDataLength += uploadFile.getData().length;
			}
		}
		// 2.再计算文本属性部分的长度
		StringBuilder sb2 = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb2.append("--");
			sb2.append(BOUNDARY);
			sb2.append("\r\n");
			sb2.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n");
			sb2.append(entry.getValue());
			sb2.append("\r\n");
		}
		// 计算传输给服务器的实体数据的总长度
		// 实体数据总长度=文本数据总长+文件内容总长+结束语长度
		long datalength = sb2.toString().getBytes().length + fileInEntityDataLength	+ ENDLINE.getBytes().length;
		
		// 构建URL对象，用于联接网络
		URL url = new URL(path);
		// 判断端口号
		int port = url.getPort() == -1 ? 80 : url.getPort();
		// 构建socket,用于与服务器的联接
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		// 输出流
		OutputStream outputStream = socket.getOutputStream();
		
		// 下面完成http请求头的拼接和发送
		StringBuffer protocal = new StringBuffer( "POST " + url.getPath() + " HTTP/1.1\r\n");
		protocal.append( "Accept: image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n");
		protocal.append( "Accept-Language: zh-CN\r\n" );
		
		//以下的两行最重要
		protocal.append(  "Content-Type: multipart/form-data; boundary="	+ BOUNDARY + "\r\n" );
		protocal.append(  "Content-Length: " + datalength + "\r\n" );
		
		protocal.append( "Connection: Keep-Alive\r\n" );
		protocal.append(  "Host: " + url.getHost() + ":" + port + "\r\n" );
		protocal.append( "\r\n" );
		System.out.println("打印一下http头信息:"+   protocal.toString());
		//将请求的命令行和请求头信息发出
		outputStream.write(   protocal.toString().getBytes());
		// 再将文件中的所有的文本类型的普通参数数据都发送出去
		outputStream.write(sb2.toString().getBytes());
		
		// 将所有文件类型的实体数据发送出去
		for (FileProperty fp : files) {
			StringBuilder fsb = new StringBuilder();
			fsb.append("--");
			fsb.append(BOUNDARY);
			fsb.append("\r\n");
			fsb.append(
					"Content-Disposition: form-data; name=\""
							+ fp.getParameterName() + "\"; filename=\""
							+ fp.getFileName() + "\"").append("\r\n");
			fsb.append("Content-Type: " + fp.getContenttype() + "\r\n\r\n");
			outputStream.write(fsb.toString().getBytes());
			// 发送文件真正的数据
			if (fp.getInputStream() != null) { // 是大文件的话，用流输出
				byte[] buffer = new byte[2048];
				int length = 0;
				while ((length = fp.getInputStream().read(buffer, 0, 2048)) != -1) {
					outputStream.write(buffer, 0, length);
				}
				fp.getInputStream().close(); // 关闭输入流
			} else { // 是小文件的话，用byte[] data输出
				outputStream.write(fp.getData(), 0, fp.getData().length);
			}
			outputStream.write("\r\n".getBytes());
		}
		// 最后是发送数据的结束标志，表示数据发送完毕
		outputStream.write(ENDLINE.getBytes());
		outputStream.flush();
		
		// 读取服务器端的回应
		BufferedReader read = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		//System.out.println(read.readLine());
		if (read.readLine().indexOf("200") == -1) {
			return false;
		}
		outputStream.close();
		read.close();
		socket.close();
		return true;
	}

}