package com.dessert.system.service.home.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface HomeService {
	/**
	 * 功能描述: 登陆
	 * author:liwm
	 * @param params
	 * @return
	 */
    boolean login(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 功能描述: 登出
     * author:liwm
     * @param request
     * @param response
     * @return
     */
    boolean loginOut(HttpServletRequest request, HttpServletResponse response);


	/**
	 * 注册
	 * @param params
	 * @param request
	 * @param response
     * @return
     */
	boolean signUp(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 设置登录用户信息
	 * @param request
	 * @param response
     * @return
     */
	boolean setLoginUserInfo(HttpServletRequest request, HttpServletResponse response);
}
