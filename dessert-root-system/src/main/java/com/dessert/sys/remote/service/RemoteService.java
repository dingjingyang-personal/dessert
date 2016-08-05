package com.dessert.sys.remote.service;

import java.util.Map;
/**
 * 远程调用
 * @author
 * 功能描述:
 */
public interface RemoteService {
	  /**
	   * 功能描述: 调用远程方法
	   * author:liwm
	   * @param benId
	   * @param methodName
	   * @param params
	   * @return
	   */
      Map<String, Object> invokeRemoteMethod(String benId, String methodName, Map<String, Object> params);
      
      
}
