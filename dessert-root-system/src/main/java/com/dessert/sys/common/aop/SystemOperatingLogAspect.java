package com.dessert.sys.common.aop;

import com.dessert.sys.common.annotation.SystemOperatingLog;
import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.UserTool;
import com.dessert.sys.log.service.SysLogService;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class SystemOperatingLogAspect {

    @Autowired
    private SysLogService sysLogService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemOperatingLogAspect.class);

    //Controller层切点
    //对有应用com.dessert.sys.common.annotation.SystemOperatingLog进行注解的方法进行横切面拦截
    @Pointcut("@annotation(com.dessert.sys.common.annotation.SystemOperatingLog)")
    public void controllerAspect() {

    }


    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param point
     * @return
     */
    @Around("controllerAspect()")
    public Object doController(ProceedingJoinPoint point) {
        Object result = null;
        // 执行方法名
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        Map<String, Object> logMap = new HashMap<String, Object>();
        Map<String, Object> map = null;
        User user = UserTool.getUserForShiro();
        String userName;
        String ip;
        Long runtime;
        //创建一个计时器
        StopWatch watch = new StopWatch();
        try {
            ip = SecurityUtils.getSubject().getSession().getHost();
        } catch (Exception e) {
            ip = "无法获取登录用户Ip";
        }
        try {
            // 登录名
            userName = user.getUsername();
            if (SysToolHelper.isEmpty(userName)) {
                userName = "无法获取登录用户信息！";
            }
        } catch (Exception e) {
            userName = "无法获取登录用户信息！";
        }
        try {
            map = getControllerMethodDescription(point);
            //记时开始
            watch.start();
            result = point.proceed();
            //记时结束
            watch.stop();
            // 获取计时器计时时间
            runtime = watch.getTime();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        try {
            logMap.put("operatinglogid", SysToolHelper.getUuid());
            logMap.put("username", userName);
            logMap.put("module", map.get("module"));
            logMap.put("methods", map.get("methods"));
            logMap.put("description", map.get("description"));
            logMap.put("ip", ip);
            logMap.put("runtime", runtime.toString());
            //持久化到数据库
            sysLogService.addOperatingLog(logMap);
        } catch (Exception e) {
            //记录本地异常日志
            LOGGER.error("====通知异常====");
            LOGGER.error("异常信息:{}", e.getMessage());
        }
        return result;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    map.put("module", method.getAnnotation(SystemOperatingLog.class).module());
                    map.put("methods", method.getAnnotation(SystemOperatingLog.class).methods());
                    String de = method.getAnnotation(SystemOperatingLog.class).description();
                    if (SysToolHelper.isEmpty(de)) de = "执行成功!";
                    map.put("description", de);
                    break;
                }
            }
        }
        return map;
    }
}
