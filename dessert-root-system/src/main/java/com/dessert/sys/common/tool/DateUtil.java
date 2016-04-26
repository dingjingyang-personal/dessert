package com.dessert.sys.common.tool;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author liwm 功能描述:日期工具
 */
public class DateUtil {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat simpleDateFormat_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Map<String, String> getDataThreePeriod() {
		Map<String, String> temp = new HashMap<String, String>(3);
		Calendar calendar = Calendar.getInstance();
		for (int i = 1; i <= 3; i++) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
			temp.put(String.valueOf(i), simpleDateFormat.format(calendar.getTime()));
		}
		return temp;
	}

	public static Timestamp getDate(String date) {
		return getDate(date, false);
	}

	public static Timestamp getDate(String date, boolean moreDay) {
		try {
			if (moreDay) {
				return new Timestamp(simpleDateFormat_ss.parse(date + " 23:59:59").getTime());
			}
			return new Timestamp(simpleDateFormat.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getDate("2014-10-04"));

	}

	/**
	 * 获取当月第一天
	 * 
	 * @return
	 */
	public String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 计算当月最后一天,返回字符串
	 * 
	 * @return
	 */
	public String getDefaultDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获得今天在本年的第几天
	 * 
	 * @return
	 */
	public static int getDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获得今天在本月的第几天(获得当前日)
	 * 
	 * @return
	 */
	public static int getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得今天在本周的第几天
	 * 
	 * @return
	 */
	public static int getDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获得今天是这个月的第几周
	 * 
	 * @return
	 */
	public static int getWeekOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 获取当年第一天
	* @Title: getFirstDayOfMonth
	* @return String
	* @throws
	 */
	public String getFirstDayOfYear() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.set(Calendar.MONTH, 0);// 设为当年第一个月
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 计算当年最后一天,返回字符串
	    * @Title: getDefaultDayByYear
	    * @return String
	    * @throws
	 */
	public String getLastDayByYear() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.set(Calendar.MONTH, 11);// 设最后一个月
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

}
