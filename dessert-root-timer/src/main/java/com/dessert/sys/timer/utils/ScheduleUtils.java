package com.dessert.sys.timer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhc.sys.alarmclock.exceptions.ScheduleException;
import com.rhc.sys.alarmclock.model.ScheduleJob;
import com.rhc.sys.alarmclock.quartz.JobFactory;
import com.rhc.sys.common.tool.SysToolHelper;

/**
 * 定时任务工具类
    * @ClassName: ScheduleUtils
    * @author Admin
    * @date 2016年1月12日
    *
 */
public class ScheduleUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleUtils.class);

	/**
	 * 计划中的任务
	    * @Title: plannedTasks
	    * @param scheduler void
	    * @throws
	 */
	public static List<ScheduleJob> plannedTasks(Scheduler scheduler) {
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					ScheduleJob job = new ScheduleJob();
					job.setJobName(jobKey.getName());
					job.setJobGroup(jobKey.getGroup());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					job.setStatus(triggerState.name());
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						job.setCronExpression(cronExpression);
					}
					jobList.add(job);
				}
			}
			return jobList;
		} catch (SchedulerException e) {
			LOG.error("获取计划中的任务失败", e);
			throw new ScheduleException("获取计划中的任务失败");
		}
	}

	/**
	 * 运行中的任务
	    * @Title: runTasks
	    * @param scheduler
	    * @return List<ScheduleJob>
	    * @throws
	 */
	public static List<ScheduleJob> runTasks(Scheduler scheduler) {
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				ScheduleJob job = new ScheduleJob();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
			return jobList;
		} catch (SchedulerException e) {
			LOG.error("获取运行中的任务失败", e);
			throw new ScheduleException("获取运行中的任务失败");
		}
	}

	/**
	 * 暂停定时任务
	* @Title: pauseJob
	* @param scheduler
	* @param jobName
	* @param jobGroup void
	* @throws
	 */
	public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			LOG.error("暂停定时任务失败", e);
			throw new ScheduleException("暂停定时任务失败");
		}
	}

	/**
	 * 恢复任务
	    * @Title: resumeJob
	    * @param scheduler
	    * @param jobName
	    * @param jobGroup void
	    * @throws
	 */
	public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			LOG.error("暂停定时任务失败", e);
			throw new ScheduleException("暂停定时任务失败");
		}
	}

	/**
	 * 删除定时任务
	    * @Title: deleteScheduleJob
	    * @param scheduler
	    * @param jobName
	    * @param jobGroup void
	    * @throws
	 */
	public static void deleteScheduleJob(final Scheduler scheduler, String jobName, String jobGroup) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			scheduler.pauseTrigger(triggerKey);// 停止触发器  
			scheduler.unscheduleJob(triggerKey);// 移除触发器  
			scheduler.deleteJob(getJobKey(jobName, jobGroup));
			System.out.println(scheduler.getSchedulerName());
			System.out.println("**********"+scheduler.hashCode());
		} catch (SchedulerException e) {
			LOG.error("删除定时任务失败", e);
			throw new ScheduleException("删除定时任务失败");
		}
	}

	/**
	 * 立即运行一次任务
	    * @Title: runOnce
	    * @param scheduler
	    * @param jobName
	    * @param jobGroup void
	    * @throws
	 */
	public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			LOG.error("运行一次定时任务失败", e);
			throw new ScheduleException("运行一次定时任务失败");
		}
	}

	/**
	 * 创建定时任务
	    * @Title: createScheduleJob
	    * @param scheduler
	    * @param jobName
	    * @param jobGroup
	    * @param cronExpression void
	    * @throws
	 */
	public static void createScheduleJob(final Scheduler scheduler, String jobName, String jobGroup, String cronExpression,
			Map<String, Object> paramsMap) {
		try {

			ScheduleJob schedulejob = new ScheduleJob();

			schedulejob.setScheduleJobId(SysToolHelper.getUuid());
			schedulejob.setJobName(jobName);
			schedulejob.setJobGroup(jobGroup);
			schedulejob.setCronExpression(cronExpression);

			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在，创建一个
			if (null == trigger) {
				JobDetail jobDetail = JobBuilder.newJob(JobFactory.class).withIdentity(jobName, jobGroup)
						.build();
				jobDetail.getJobDataMap().put("scheduleJob", schedulejob);
				jobDetail.getJobDataMap().put("paramsMap", paramsMap);
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
						.build();
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// Trigger已存在，那么更新相应的定时设置
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
			System.out.println(scheduler.getSchedulerName());
			System.out.println("*********"+scheduler.hashCode());

		} catch (SchedulerException e) {
			LOG.error("创建定时任务失败", e);
			throw new ScheduleException("创建定时任务失败");
		}
	}

	/**
	 * 更新任务的时间表达式
	    * @Title: updateScheduleJob
	    * @param scheduler
	    * @param jobName
	    * @param jobGroup
	    * @param cronExpression void
	    * @throws
	 */
	public static void updateScheduleJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression) {

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			LOG.error("更新定时任务失败", e);
			throw new ScheduleException("更新定时任务失败");
		}
	}

	/**
	 * 获取表达式触发器
	    * @Title: getCronTrigger
	    * @param scheduler
	    * @param jobName
	    * @param jobGroup
	    * @return CronTrigger
	    * @throws
	 */
	public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			return (CronTrigger) scheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			LOG.error("获取定时任务CronTrigger出现异常", e);
			throw new ScheduleException("获取定时任务CronTrigger出现异常");
		}
	}

	// -------------------------------------------------------------
	/**
	 * 获取jobKey
	* @Title: getJobKey
	* @param jobName
	* @param jobGroup
	* @return JobKey
	* @throws
	 */
	public static JobKey getJobKey(String jobName, String jobGroup) {

		return JobKey.jobKey(jobName, jobGroup);
	}

	/**
	 * 获取触发器key
	    * @Title: getTriggerKey
	    * @param jobName
	    * @param jobGroup
	    * @return TriggerKey
	    * @throws
	 */
	public static TriggerKey getTriggerKey(String jobName, String jobGroup) {

		return TriggerKey.triggerKey(jobName, jobGroup);
	}

}
