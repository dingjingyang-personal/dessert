
package com.dessert.sys.common.tool.impl;

import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.tool.FileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;




/** 
 * 功能描述: <一句话描述> <br/> 
 * 
 * date: 2014年9月12日 上午8:52:04 <br/> 
 * 
 * @author Administrator 
 * @version  
 */

public class FtpClientUtil implements FileUpload {
	private String ip;
	private int port;
	private String userName;
	private String password;
	
	public FtpClientUtil(){
		this.ip= SysSettings.FTP_IP;
		this.port=SysSettings.FTP_PORT;
		this.userName=SysSettings.FTP_USERNAME;
		this.password=SysSettings.FTP_PWD;
	}
	public FtpClientUtil(String ip,int port,String username,String pwd){
		this.ip=ip;
		this.port=port;
		this.userName=username;
		this.password=pwd;
	}
	private String createDirectory(FTPClient ftpClient,String root,String directory) throws IOException {
		if(StringUtils.isEmpty(directory)){
			return root;
		}
		String[] temp;
		if(directory.contains("/")){
			temp=directory.split("/");
		}else {
			temp=directory.split("\\\\");
		}
		StringBuilder builder=new StringBuilder();
		if(!"\\".equals(root)&&!"/".equals(root)){
			builder.append(root);
		}
		for(String path:temp){
			if(StringUtils.isEmpty(path)){
				continue;
			}
			if(builder.length()>0){
				builder.append(SysSettings.FILE_SEPARATOR);
			}
			builder.append(path);
			ftpClient.makeDirectory(builder.toString());
		}
		return builder.toString();
	}
	/* (non-Javadoc)
	 * @see com.rhc.sys.common.intf.FtpClientIntf#uploadFile(java.io.InputStream, java.lang.String)
	 */
	@Override
	public boolean uploadFile(InputStream inStream,String directory, String fileName) {
		FTPClient ftpClient = null;
		OutputStream os=null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(this.ip, this.port);
            ftpClient.login(this.userName, this.password);
            //ftpClient.connect("127.0.0.1", 21);
            //ftpClient.login("gxlwm", "123456");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            String path=createDirectory(ftpClient,ftpClient.printWorkingDirectory(),directory);
            // 切换当前的工作做目录到数据文件的存放路径
            boolean boo = ftpClient.changeWorkingDirectory(path);
            if (!boo) {
                return false;
            }
            return ftpClient.storeFile(fileName, inStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
            if(os!=null){
                try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
                os=null;
            }
            if(inStream!=null){
            	try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
        return false;
	}

	

	/* (non-Javadoc)
	 * @see com.rhc.sys.common.intf.FtpClientIntf#deleteFile(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteFile(String directory, String filefullname) {
		FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(this.ip, this.port);
            ftpClient.login(this.userName, this.password);
            //ftpClient.connect("127.0.0.1", 21);
            //ftpClient.login("gxlwm", "123456");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            String path = ftpClient.printWorkingDirectory() + File.separator + directory;
            // 切换当前的工作做目录到数据文件的存放路径
            boolean boo = ftpClient.changeWorkingDirectory(path);
            if (!boo) {
            	
            }
            FTPFile[] fileList = ftpClient.listFiles();
            String fileNewName =new File(filefullname).getName(); //new String(filename.getBytes(SysConstants.ENCODER_GBK), SysConstants.ENCODER_ISO);
            for (FTPFile file : fileList) {
                if (file.isFile() && file.getName().equals(fileNewName)) {
                    ftpClient.deleteFile(fileNewName);
                    break;
                }
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            if (ftpClient != null) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                	e.printStackTrace();
                }
                ftpClient=null;
            }
        }
	}
	public static void main(String[] args){
		FtpClientUtil ftpClientUtil=new FtpClientUtil();
		try {
			ftpClientUtil.uploadFile(new FileInputStream("d:\\floor4-img1.jpg"), "1205\\1", "we2.jpg");
		   //ftpClientUtil.deleteFile("dd9972/sss", "we2.jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	public static String getFileExt(String fileName) {
		int index=fileName.lastIndexOf(".");
		if(index>=0){
			return fileName.substring(index);
		}
		return "";
	}
	/* (non-Javadoc)
	 * @see com.rhc.sys.common.intf.FtpClientIntf#uploadFile(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String uploadFile(HttpServletRequest request, String directory,
			String fileName, String fileFieldName) {
		if (StringUtils.isEmpty(fileFieldName)||!(request instanceof MultipartHttpServletRequest)) {
			return null;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file =  multipartRequest.getFile(fileFieldName);
		if(file==null||file.getSize()<=0){
			return null;
		}
		String ext =getFileExt(file.getOriginalFilename());
		StringBuilder builder=new StringBuilder();
		String tempfileName=fileName;
		if(StringUtils.isEmpty(tempfileName)){
			tempfileName=UUID.randomUUID().toString().replaceAll("-", "");
		}
		builder.append(tempfileName).append(ext);
		String fn=builder.toString();
		try {
			if(uploadFile(file.getInputStream(), directory,fn)){
				return combineFilename(directory,fn);
			}else {
				throw new RuntimeException("上传文件失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * 功能描述:拼接文件路径
	 * 
	 * @author Administrator 
	 * @param dir
	 * @param filename
	 * @return
	 */
	private String combineFilename(String dir,String filename) {
		StringBuilder builder=new StringBuilder();
		return builder.append(dir.replace("\\", SysSettings.FILE_SEPARATOR))
				.append(SysSettings.FILE_SEPARATOR).append(filename).toString();
	}
	@Override
	public String readFile(String directory, String filename) {

		FTPClient ftpClient = null;
		InputStream ins = null;
  	  	StringBuilder builder = null;
  	  	BufferedReader reader = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(this.ip, this.port);
            ftpClient.login(this.userName, this.password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            String path = ftpClient.printWorkingDirectory() + SysSettings.FILE_SEPARATOR + directory;
            // 切换当前的工作做目录到数据文件的存放路径
            boolean change = ftpClient.changeWorkingDirectory(path);
            if(!change){
            	System.out.println("Directory changes fail");
            }
            
  	  		// 从服务器上读取指定的文件
  	  		ins = ftpClient.retrieveFileStream(filename);
  	  		if(ins == null){
  	  			System.out.println("ins is null");
  	  			return "";
  	  		}
  	  		reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
  	  		String line;
			builder = new StringBuilder(150);
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
			if (ins != null) {
				ins.close();
			}
			ftpClient.getReply();
    
            return builder.toString();
        } catch (IOException e) {
        	e.printStackTrace();
        	return "";
        } finally {
            if (ftpClient != null) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                	e.printStackTrace();
                }
                ftpClient=null;
            }
        }
	
	}
}
