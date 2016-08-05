package com.dessert.sys.common.interceptor;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SystemLoggerInterceptor {

    private Logger logger = LoggerFactory.getLogger(SystemLoggerInterceptor.class);


    /**
     * 异常抛出执行方法
     *
     * @param joinPoint
     * @param e
     */
    public void afterThrowingException(JoinPoint joinPoint, Exception e ) {
        logger.error(getException(e));
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            System.out.println("|____异常相关参数_____>"+joinPoint.getArgs()[i]);
        }
    }

    /**
     * 获取异常
     * @param e
     * @return
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
