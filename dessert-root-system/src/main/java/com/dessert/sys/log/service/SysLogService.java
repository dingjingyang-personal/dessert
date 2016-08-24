package com.dessert.sys.log.service;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.bean.User;

import java.util.Map;


public interface SysLogService {
	void error(Exception e);

	void error(String errorItem, String errorInfo);

	void error(String errorItem, Exception e);

	void error(String errorItem, String errorInfo, String userId, String userIp, String userid);

	void error(String errorItem, Exception e, String userId, String userIp);
	
	void error(User user,String ip, Exception ex);

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


	/**
	 * 添加操作日志
	 * @param logMap
     */
	boolean addOperatingLog(Map<String, Object> logMap);

	/**
	 * 查询异常日志
	 * @param params
	 * @return
     */
	Page findErrorLogPage(Map<String, Object> params);

	/**
	 * 查询操作日志
	 * @param params
	 * @return
     */
	Page findOperationLogPage(Map<String, Object> params);

	/**查询登录日志
	 *
	 * @param params
	 * @return
     */
	Page findLoginLogPage(Map<String, Object> params);

	/**
	 * 添加登录日志
	 * @param loginMap
	 * @return
     */
	boolean addLoginLog(Map<String, Object> loginMap);
}
