package com.dessert.sys.timer.service.impl;

import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.timer.service.AlarmClocklogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


public class AlarmClocklogServiceImpl implements AlarmClocklogService {
	
	@Autowired
	private DaoClient daoClient;

	@Override
	public boolean writelog(Map<String, Object> params) {
		params.put("logid", SysToolHelper.getUuid());
		boolean boo = daoClient.update("com.rhc.alarmclock.addclocklog", params)>0;
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>LOg---------"+boo);
		return boo;
	}

}
