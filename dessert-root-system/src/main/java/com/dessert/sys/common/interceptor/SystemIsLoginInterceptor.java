package com.dessert.sys.common.interceptor;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.UserTool;
import com.dessert.sys.log.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ding-Admin on 2016/4/26.
 */
public class SystemIsLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SysLogService sysLogService;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if(url.contains("JSESSIONID")){
            int index = url.indexOf(";JSESSIONID");
            String newUrl = url.substring(0,index);
            response.sendRedirect(newUrl);
            return false;
        }

//        if(url.endsWith(".html")){
//            return true;
//        }
//        if(!UserTool.isUserLoginForShiro()){
//            response.sendRedirect(request.getContextPath()+"/sys/timeout.html");
//            return false;
//        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null) {
            User user = UserTool.getUserForShiro();
            String ip = SysToolHelper.getIp(request);
            sysLogService.error(user, ip, ex);
        }
    }


}
