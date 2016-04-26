package com.dessert.sys.common.tool;

import com.dessert.sys.common.bean.User;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ding-Admin on 2016/4/26.
 */
public class UserTool {


    public static User getUserCache(HttpServletRequest request){
        String ticket = CookieHelper.getInstance().getUserTicket(request);
        if (!StringUtils.isEmpty(ticket)) {
            Object object = SysRedisTool.getObject(ticket);
            if (object != null && (object instanceof User)) {
                return (User) object;
            }
        }
        return null;
    }


}
