package com.dessert.sys.common.tool;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.constants.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ding-Admin on 2016/4/26.
 */
public class UserTool {


    /**
     * 判断是否登录
     * @return
     */
    public static boolean isUserLoginForShiro(){
        Subject currentUser = SecurityUtils.getSubject();
        if(currentUser.isAuthenticated()){
            return true;
        }
        return false;
    }


    /**
     * 从redis获取登录用户
     *
     * @param request
     * @return
     */
    public static User getUserForRedis(HttpServletRequest request) {
        String ticket = CookieHelper.getInstance().getUserTicket(request);
        if (!StringUtils.isEmpty(ticket)) {
            try {
                Object object = SysRedisTool.getObject(ticket);
                if (object != null && (object instanceof User)) {
                    return (User) object;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从shiro中获取USER
     * @return
     */
    public static User getUserForShiro() {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        return user;
    }

    /**
     * 将USER对象存入redis
     *
     * @param request
     * @param response
     * @param user
     */
    public static void setUserForRedis(HttpServletRequest request, HttpServletResponse response, User user) {
        if (user == null) {
            return;
        }
        String uuid = SysToolHelper.getUuid();

        String ticket = CookieHelper.getInstance().getUserTicket(request);
        if (StringUtils.isEmpty(ticket)) {
            ticket = SysToolHelper.combineString(user.getUserno(), uuid);
        }
        CookieHelper.getInstance().setUserTicket(response, ticket);
        SysRedisTool.setCacheData(ticket, user);
    }

    /**
     * 从redis清除登录用户
     *
     * @param request
     * @param response
     */
    public static void removeUserForRedis(HttpServletRequest request, HttpServletResponse response) {
        SysToolHelper.removeValueInSession(request, SysConstants.EMPLOYEE_KEY);
        String ticketName = CookieHelper.getInstance().getUserTicket(request);
        if (!StringUtils.isEmpty(ticketName)) {
            CookieHelper.getInstance().clearCookie(request, response, SysConstants.TICKET_NAME);
            SysRedisTool.delete(ticketName);
        }
    }


}
