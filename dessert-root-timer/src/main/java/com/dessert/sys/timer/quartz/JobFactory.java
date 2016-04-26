package com.dessert.sys.timer.quartz;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rhc.sys.alarmclock.model.ScheduleJob;
import com.rhc.sys.common.tool.SysToolHelper;

public class JobFactory implements Job {

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {

		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");

		JobDataMap dataMap = context.getMergedJobDataMap();
		Map<String, String> paramsMap =  (Map<String, String>) dataMap.get("paramsMap");
		String executeurl = paramsMap.get("executeurl").toString();


		postMap(executeurl, paramsMap);

		System.out.println("--------------jobName:" + scheduleJob.getJobName() + "***" + Thread.currentThread().getName() + "***" + new Date()
				+ "------------------");
		
	}


	private String postMap(String url, Map<String, String> params) {
		UTF8PostMethod post = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
			client.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);
			post = new UTF8PostMethod(url);
			post.addRequestHeader("contentType", "text/html;charset=UTF-8");
			int i = 0;
			NameValuePair[] values = new NameValuePair[params.size()];
			Set<Entry<String, String>> entries = params.entrySet();
			for (Entry<String, String> m : entries) {
				values[i++] = new NameValuePair(m.getKey(), m.getValue());
			}
			post.setRequestBody(values);
			int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				params.put("error", "connect fail");
				return null;
			}
			return post.getResponseBodyAsString();
		} catch (HttpException e) {
			params.put("error", String.valueOf(e));
			SysToolHelper.error("postXml", e);
		} catch (IOException e) {
			params.put("error", String.valueOf(e));
			SysToolHelper.error("postXml", e);
		} catch (RuntimeException re) {
			params.put("error", String.valueOf(re));
			SysToolHelper.error("postXml", re);
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
		return null;
	}
	
	private  class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}
		@Override
		public String getRequestCharSet() {			
			return "UTF-8";
		}
	}

}
