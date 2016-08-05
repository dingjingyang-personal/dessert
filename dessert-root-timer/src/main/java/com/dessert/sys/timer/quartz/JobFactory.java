package com.dessert.sys.timer.quartz;

import com.dessert.sys.timer.model.ScheduleJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.Map;

import static com.dessert.sys.common.tool.NetUtil.postMap;


public class JobFactory implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {

		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");

		JobDataMap dataMap = context.getMergedJobDataMap();
		Map<String, String> paramsMap =  (Map<String, String>) dataMap.get("paramsMap");
		String executeurl = paramsMap.get("executeurl").toString();


		postMap(executeurl, paramsMap);

		System.out.println("--------------jobName:" + scheduleJob.getJobName() + "***" + Thread.currentThread().getName() + "***" + new Date()
				+ "------------------");
		
	}


}
