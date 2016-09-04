package com.yc.utils;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.jsp.PageContext;

import com.jspsmart.upload.*;

public class UploadUtil {
	private String PATH = "uploads";
	private String ALLOWED = "gif,jpg,bmp,doc,png";
	private String DENIED = "exe,bat,jsp,html,js";
	private int TOTALMAXSIZE = 20 * 1024 * 1024;
	private int SINGLESIZE = 2 * 1024 * 1024;

	public UploadUtil(String pATH, String aLLOWED, String dENIED,
			int tOTALMAXSIZE, int sINGLESIZE) {
		super();
		PATH = pATH;
		ALLOWED = aLLOWED;
		DENIED = dENIED;
		TOTALMAXSIZE = tOTALMAXSIZE;
		SINGLESIZE = sINGLESIZE;
	}

	public UploadUtil() {
	}

	public Map<String,String> uploadFile(PageContext pContext) throws Exception {
		SmartUpload su = new SmartUpload();
		File f = null;
		su.initialize(pContext);
		su.setAllowedFilesList(ALLOWED);
		su.setDeniedFilesList(   DENIED );
		su.setCharset("gbk");
		su.setTotalMaxFileSize(TOTALMAXSIZE);
		su.setMaxFileSize(SINGLESIZE);
		su.upload();
		Request req = su.getRequest();   
		Map<String,String> params = new HashMap<String, String>();
		Enumeration enu=req.getParameterNames();
		while(   enu.hasMoreElements() ){
			String key=enu.nextElement().toString();
			String value=req.getParameter(key);
			params.put(key, value);
		}
		Files fs=su.getFiles();
		if(  fs!=null ){
			for( int i=0;i<fs.getCount();i++){
				File file=fs.getFile(i);
				if(  !file.isMissing() ){
					String name=file.getFileName();
					String fieldName=file.getFieldName();   
					String extName=file.getFileExt();
					String path=file.getFilePathName();
					String mimetype=file.getTypeMIME();     //     content-type="text/html"
					String newFileName = genFileSavePath(file);
					file.saveAs(newFileName);
					params.put(fieldName, newFileName);
				}
			}
		}
		return params;
	}

	private String genFileSavePath(File f) {
		String fname = new Date().getTime() + ""
				+ new Random().nextInt(1000);
		String fn = f.getFileExt();
		fname = PATH + "/" + fname + "." + fn;
		return fname;
	}

}
