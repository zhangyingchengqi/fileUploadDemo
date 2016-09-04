package com.yc.uploadClient.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 要上传的文件的属性的封装类
 */
public class FileProperty {
	private byte[] data;   //要上传的数据，小数据量上传
	private InputStream inputStream;  //要上传的数据的输入流,用于大文件上传真
	private File file;    //要上传的文件对象
	
	private String fileName;  //要上传的文件名
	private String parameterName;   //要上传的文件的请求参数名称
	private String contenttype="application/octet-stream";
	/**
	 * 此构造方法用于上传一些较小的数据.
	 * @param filename
	 * @param data : 只适合存储较少的数据
	 * @param paramterName
	 * @param contenttype
	 */
	public FileProperty(String filename,byte[] data,String paramterName,String contenttype){
		this.data=data;
		this.fileName=filename;
		this.parameterName=paramterName;
		if(contenttype!=null){
			this.contenttype=contenttype;
		}
	}
	/**
	 * 可以传较大的文件
	 * @param filename  文件名
	 * @param file    要上传的文件
	 * @param parameterName   文件的参数名字
	 * @param contenttype     文件类型
	 */
	public FileProperty(String filename,File file,String parameterName,String contenttype){
		this.fileName=filename;
		this.parameterName=parameterName;
		this.file=file;
		try {
			this.inputStream=new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(contenttype!=null){
			this.contenttype=contenttype;
		}
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getContenttype() {
		return contenttype;
	}
	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	
	
	
}

