package com.dessert.sys.log.service;

import com.dessert.sys.common.bean.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface SysLogService {
	void error(Exception e);

	void error(String errorItem, String errorInfo);

	void error(String errorItem, Exception e);

	void error(String errorItem, String errorInfo, String userId, String userIp, String userid);

	void error(String errorItem, Exception e, String userId, String userIp);
	
	void error(HttpServletRequest request, Exception ex);

	/**
	 * 
	 * selectLog:查询日志 <br/>
	 * 
	 * @author Administrator
	 * @param params
	 * @return
	 */
	Page<Map<String, Object>> selectLog(Map<String, Object> params);

	/**
	 * 
	 * deleteLog:删除日志 <br/>
	 * 
	 * @author Administrator
	 * @return
	 */
	boolean deleteLog(Map<String, Object> params);
	
	
}
