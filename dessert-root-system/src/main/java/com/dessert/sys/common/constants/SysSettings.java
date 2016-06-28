/** 
 * 
* 
* Administrator
* TODO
* Date:2014年9月12日下午4:46:58 
* Copyright (c) 2014年9月12日
* 
 */  

package com.dessert.sys.common.constants;

import org.apache.commons.lang.StringUtils;



public class SysSettings {
	public static final String FTP_IP;
	public static final int FTP_PORT;
	public static final String FTP_USERNAME;
	public static final String FTP_PWD;
	public static final Integer TERMINAL_KEY=10;
	public static final String REMOTE_INVOKE_URL;


	//cookie路径
	public static final String COOKIE_PATH;
	public static final String COOKIE_DOMAIN;

	//图片引用根路径
	public static final String IMGSERVERE_PATH;
	

	
	public static final String FILE_SEPARATOR ;
	public static final long CACHE_TIMEOUT_SECONDS;

	static{
		PropertiesConfig config=new PropertiesConfig("config/setting");
		FTP_IP=config.getPropByKey("ftp.ip");
		FTP_PORT=config.getIntPropByKey("ftp.port", 21);
		FTP_USERNAME=config.getPropByKey("ftp.username");
		FTP_PWD=config.getPropByKey("ftp.password");
		IMGSERVERE_PATH=config.getPropByKey("imageserver.baseurl");
		FILE_SEPARATOR="/";
		String url=config.getPropByKey("remote.invokeurl");
		if(StringUtils.isEmpty(url)){
			REMOTE_INVOKE_URL="http://127.0.0.1:8080/dessert-root-mainweb/remote/invokeMethod.htm";
		}else {
			REMOTE_INVOKE_URL=url;
		}
		COOKIE_PATH=config.getPropByKey("cookie.path");
		COOKIE_DOMAIN=config.getPropByKey("cookie.domain");
		CACHE_TIMEOUT_SECONDS=config.getIntPropByKey("redis.cache_timeout_seconds", 3600*24);
	}
}
