package com.dessert.sys.remote.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RemoteHelperService {
	/**
	 * 功能描述: 解析远程请求
	 * author:liwm
	 * @param request
	 * @param response
	 */
     void parseRemoteMethod(HttpServletRequest request, HttpServletResponse response);
}
