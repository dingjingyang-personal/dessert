package com.dessert.sys.common.tool;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * @author zhang.hua
 * 功能描述：
 * 创建时间：2015年2月27日 上午10:10:55
 */
public interface FileUpload {
	
	/**
	 * 文件上传
	 * @param request  请求
	 * @param directory  文件目录
	 * @param fileName   文件名
	 * @param fileField  文件字段
	 * @return
	 */
	public String uploadFile(HttpServletRequest request, String directory, String fileName, String fileField);
	
	/**
	 * 文件上传
	 * @param directory  目录
	 * @param filename  文件名
	 */
	public void deleteFile(String directory, String filename);

	boolean uploadFile(InputStream inStream, String directory, String fileName);
	
	public String readFile(String directory, String filename);
	

}
