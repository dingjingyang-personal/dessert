package com.dessert.sys.timer.service.impl;

import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.timer.service.AlarmClockService;
import com.dessert.sys.timer.utils.ScheduleUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AlarmClockServiceImpl implements AlarmClockService {

	@Autowired
	private DaoClient daoClient;

	private static Scheduler scheduler;

	@Autowired
	public void setScheduler(Scheduler schedulerx) {
		if (scheduler != null) {
			return;
		}
		scheduler = schedulerx;
		List<Map<String, Object>> clockList = getClockList();
		if (!(null == clockList || clockList.isEmpty())) {
			for (Map<String, Object> clockMap : clockList) {
				String jobName = (String) clockMap.get("jobname");
				String jobGroup = (String) clockMap.get("jobgroup");
				String cronExpression = (String) clockMap.get("cronexp");

				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("jobid", clockMap.get("clockid").toString());
				paramsMap.put("beanid", clockMap.get("beanid").toString());
				paramsMap.put("beanmethod", clockMap.get("beanmethod").toString());
				paramsMap.put("executeurl", clockMap.get("executeurl").toString());

				if (SysToolHelper.isExists(clockMap, "beanid", "beanmethod")) {
					ScheduleUtils.createScheduleJob(scheduler, jobName, jobGroup, cronExpression, paramsMap);
				}
			}
		}
	}



	@Override
	public Map<String, Object> getClock(Map<String, Object> params) {
		return daoClient.selectMap("com.rhc.alarmclock.getclocks", params);
	}

	@Override
	public boolean addaddClock(Map<String, Object> params) {
		params.put("clockid", SysToolHelper.getUuid());
		boolean boo = daoClient.update("com.rhc.alarmclock.addclock", params) > 0;
		if (boo) {
			String jobName = (String) params.get("jobname");
			String jobGroup = (String) params.get("jobgroup");
			String cronExpression = (String) params.get("cronexp");
			// 创建任务
			params.put("jobid", params.get("clockid"));
			ScheduleUtils.createScheduleJob(scheduler, jobName, jobGroup, cronExpression, params);
		}
		return boo;
	}

	@Override
	public boolean updateClock(Map<String, Object> params) {
		if (!SysToolHelper.isExists(params, "clockid")) {
			return false;
		}

		boolean boo = daoClient.update("com.rhc.alarmclock.updateclock", params) > 0;

		params.put("jobid", params.get("clockid"));

		String jobname = params.get("jobname").toString();
		String jobgroup = params.get("jobgroup").toString();
		String cronexp = params.get("cronexp").toString();

		if (boo) {
			// 更新任务
			ScheduleUtils.updateScheduleJob(scheduler, jobname, jobgroup, cronexp);
		}
		return boo;
	}

	@Override
	public boolean deleteClockById(Map<String, Object> params) {
		if (!SysToolHelper.isExists(params, "clockid")) {
			return false;
		}

		Map<String, Object> clockMap = daoClient.selectMap("com.rhc.alarmclock.getclocks", params);

		daoClient.update("com.rhc.alarmclock.deleteclocklog", params);
		boolean boo = daoClient.update("com.rhc.alarmclock.deleteclock", params) > 0;

		if (boo) {
			// 删除任务
			ScheduleUtils.deleteScheduleJob(scheduler, clockMap.get("jobname").toString(),
					clockMap.get("jobgroup").toString());
		}
		return boo;
	}

	@Override
	public List<Map<String, Object>> getClockList() {
		return daoClient.selectList("com.rhc.alarmclock.getclocks", new HashMap<String, Object>());
	}


}
