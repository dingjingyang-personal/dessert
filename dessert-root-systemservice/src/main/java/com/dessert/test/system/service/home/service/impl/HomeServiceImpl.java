package com.dessert.test.system.service.home.service.impl;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.CookieHelper;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.test.system.service.home.manager.service.UserService;
import com.dessert.test.system.service.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登陆、首页类
 *
 * @author liwm
 *         功能描述:
 */

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserService userService;

    @Autowired
    private DaoClient daoClient;


    @Override
    public boolean login(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        if (!SysToolHelper.isExists(params, "loginname", "userpwd")) {
            return false;
        }

        Map<String, Object> userMap = userService.getUser(params);

        if (SysToolHelper.isEmptyMap(userMap)) {
            params.put("error", "用户名不存在");
            return false;
        }
        String userPwd = SysToolHelper.encryptPwd(SysToolHelper.getMapValue(params, "userpwd"));
        if (!SysToolHelper.equal(userMap, "loginpwd", userPwd)) {
            params.put("error", "密码不正确");
            return false;
        }
        User user = new User();
        user.setUserid(SysToolHelper.getMapValue(userMap, "userid"));
        user.setUserno(SysToolHelper.getMapValue(userMap, "userno"));
        user.setUserName(SysToolHelper.getMapValue(userMap, "username"));
        user.setLoginname(SysToolHelper.getMapValue(userMap, "loginname"));
        user.setStatus(SysToolHelper.getMapValue(userMap, "status"));

        Map<String, Object> tempMap = userMap;

        SysToolHelper.setUserCache(request, response, user);
        CookieHelper.getInstance().setCookie(response, SysConstants.COOKIE_USERNO, user.getUserno(), SysSettings.COOKIE_DOMAIN, SysConstants.WEEK_SECONDS);
        return true;
    }


    @Override
    public boolean loginOut(HttpServletRequest request,
                            HttpServletResponse response) {
        SysToolHelper.removeUserCache(request, response);
        return true;
    }

    @Override
    public boolean signUp(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        if (!SysToolHelper.isExists(params, "username", "loginname", "userpwd", "sex", "status", "ip", "mac")) {
            return false;
        }
        String userid = SysToolHelper.readSeqBySeqKeyAndOwner("USER", "USER", true);
        params.put("userid", userid);
        params.put("userno", "111");


        return daoClient.update("com.dessert.user.com.dessert.adduser", params) > 0 ? true : false;
    }

}
