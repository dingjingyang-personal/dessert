package com.dessert.system.service.home.service;

import com.dessert.sys.exception.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
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
	boolean signUp(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 设置登录用户信息
	 * @param request
	 * @param response
     * @return
     */
	boolean setLoginUserInfo(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 处理激活
	 * @param params
     */
	void processActivate(Map<String, Object> params) throws ServiceException, ParseException;

	/**
	 * 注册发送邮件
	 */
	void processregister(Map<String, Object> userMap) throws Exception;
}
