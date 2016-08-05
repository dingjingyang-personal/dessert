package com.dessert.sys.common.tool;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.constants.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dessert.sys.common.tool.SysToolHelper.combineString;
import static com.dessert.sys.common.tool.SysToolHelper.removeValueInSession;

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
     * 获取登录用户
     *
     * @param request
     * @return
     */
    public static User getUserCache(HttpServletRequest request) {
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
     * 将USER对象存入缓存
     *
     * @param request
     * @param response
     * @param user
     */
    public static void setUserCache(HttpServletRequest request, HttpServletResponse response, User user) {
        if (user == null) {
            return;
        }
        String uuid = SysToolHelper.getUuid();

        String ticket = CookieHelper.getInstance().getUserTicket(request);
        if (StringUtils.isEmpty(ticket)) {
            ticket = combineString(user.getUserno(), uuid);
        }
        CookieHelper.getInstance().setUserTicket(response, ticket);
        SysRedisTool.setCacheData(ticket, user);
    }

    /**
     * 清除登录用户
     *
     * @param request
     * @param response
     */
    public static void removeUserCache(HttpServletRequest request, HttpServletResponse response) {
        removeValueInSession(request, SysConstants.EMPLOYEE_KEY);
        String ticketName = CookieHelper.getInstance().getUserTicket(request);
        if (!StringUtils.isEmpty(ticketName)) {
            CookieHelper.getInstance().clearCookie(request, response, SysConstants.TICKET_NAME);
            SysRedisTool.delete(ticketName);
        }
    }


}
