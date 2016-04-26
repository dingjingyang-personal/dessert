package com.dessert.sys.timer.service.impl;

import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.timer.service.AlarmClockURlService;
import com.dessert.sys.timer.service.AlarmClocklogService;
import com.dessert.sys.timer.utils.ScheduleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class AlarmClockURlServiceImpl implements AlarmClockURlService {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleUtils.class);
	@Autowired
	private AlarmClocklogService alarmClocklogService;

	@Override
	public void parseRemoteMethod(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> params = SysToolHelper.getRequestParams(request);
		

		if (!SysToolHelper.isExists(params, "beanid", "beanmethod")) {
			LOG.error("beanid或beanmethod为空!");
			return;
		}

		Object object = SysToolHelper.getBean(String.valueOf(params.get("beanid")));
		if (object == null) {
			LOG.error("bean不存在!");
			params.put("exceptiontxt", "bean不存在!");
			params.put("status", 0);
			alarmClocklogService.writelog(params);
			return;
		}
		String error = null;
		try {
			Method method = object.getClass().getMethod(String.valueOf(params.get("beanmethod")));
			if (method != null) {
				method.invoke(object);
				params.put("status", 1);
				alarmClocklogService.writelog(params);
			}
		} catch (NoSuchMethodException e) {
			LOG.error("方法不存在!");
			error = String.valueOf(e);
			params.put("exceptiontxt", error);
			params.put("status", 0);
			alarmClocklogService.writelog(params);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (SecurityException e) {
			error = String.valueOf(e);
			params.put("exceptiontxt", error);
			params.put("status", 0);
			alarmClocklogService.writelog(params);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (RuntimeException e) {
			error = String.valueOf(e);
			params.put("exceptiontxt", error);
			params.put("status", 0);
			alarmClocklogService.writelog(params);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (IllegalAccessException e) {
			error = String.valueOf(e);
			params.put("exceptiontxt", error);
			params.put("status", 0);
			alarmClocklogService.writelog(params);
			SysToolHelper.error("parseRemoteMethod", error);
		} catch (InvocationTargetException e) {
			error = String.valueOf(e);
			params.put("exceptiontxt", error);
			params.put("status", 0);
			alarmClocklogService.writelog(params);
			SysToolHelper.error("parseRemoteMethod", error);
		}

	}

}
