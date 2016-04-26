package com.dessert.sys.remote.service.impl;

import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.remote.service.RemoteHelperService;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class RemoteHelperServiceImpl implements RemoteHelperService {

	@SuppressWarnings("unchecked")
	@Override
	public void parseRemoteMethod(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> params = SysToolHelper.getRequestParams(request);
		if (!SysToolHelper.isExists(params, "beanId", "methodName")) {
			SysToolHelper.outputByResponse("params is invalid", response);
			return;
		}
		Object object = SysToolHelper.getBean(String.valueOf(params.get("beanId")));
		if (object == null) {
			SysToolHelper.outputByResponse("bean is not exist", response);
			return;
		}
		String error = null;
		try {
			Method method = object.getClass().getMethod(String.valueOf(params.get("methodName")), Map.class);
			if (method != null) {
				Map<String, Object> temp;
				Object pObject = params.get("params");
				if (pObject != null) {
					temp = JSONObject.fromObject(pObject);
				} else {
					temp = new HashMap<String, Object>();
				}
				Object object2=method.invoke(object, temp);
				String content=null;
				try {
					content = URLEncoder.encode(JSONObject.fromObject(object2).toString(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				SysToolHelper.outputByResponse(content,response);
				return;
			}
		} catch (NoSuchMethodException e) {
			error = String.valueOf(e);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (SecurityException e) {
			error = String.valueOf(e);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (RuntimeException e) {
			error = String.valueOf(e);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (IllegalAccessException e) {
			error = String.valueOf(e);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (InvocationTargetException e) {
			error = String.valueOf(e);
			SysToolHelper.error("parseRemoteMethod", error);
		}
		SysToolHelper.outputByResponse("method not exist", response);
	}

}
