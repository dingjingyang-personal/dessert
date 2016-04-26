package com.dessert.sys.common.interceptor;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SystemLoggerInterceptor {
	private Logger logger=LoggerFactory.getLogger(SystemLoggerInterceptor.class);
   /**
    * 异常抛出执行方法	
    * @param point
    * @param e
    */
   public void afterThrowingException(JoinPoint point,Exception e) {
	   logger.error(getException(e));
   }
   /**
	 * 
	 * 〈获取异常描述〉
	 * 
	 * @param e
	 * @return
	 * @see [相关类/方法]（可选）
	 * @since [产品/模块版本] （可选）
	 */
	private String getException(Exception e) {
		if (e == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		if (e.getStackTrace() != null && e.getStackTrace().length > 0) {
			builder.append("\r\nfrom:").append(e.getStackTrace()[0].toString());
		}
		builder.append("\r\nerror:").append(e.toString());
		return builder.toString();
	}
}
