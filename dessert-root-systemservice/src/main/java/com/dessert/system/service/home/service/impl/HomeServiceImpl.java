package com.dessert.system.service.home.service.impl;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.enm.DateStyle;
import com.dessert.sys.common.tool.CookieHelper;
import com.dessert.sys.common.tool.DateUtil;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.UserTool;
import com.dessert.system.service.home.service.HomeService;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

import static com.dessert.sys.common.tool.SysToolHelper.encryptPwd;

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

        if (!SysToolHelper.isExists(params, "loginname", "userpwd") && !SysToolHelper.isExists(params, "email", "userpwd")) {
            return false;
        }

        Map<String, Object> userMap = userService.getUserMap(params);

        if (SysToolHelper.isEmptyMap(userMap)) {
            params.put("error", "用户不存在");
            return false;
        }
        String userPwd = encryptPwd(SysToolHelper.getMapValue(params, "userpwd"));
        if (!SysToolHelper.equal(userMap, "userpwd", userPwd)) {
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

        UserTool.setUserCache(request, response, user);
        CookieHelper.getInstance().setCookie(response, SysConstants.COOKIE_USERNO, user.getUserno(), SysSettings.COOKIE_DOMAIN, SysConstants.WEEK_SECONDS);
        return true;
    }


    @Override
    public boolean loginOut(HttpServletRequest request, HttpServletResponse response) {
        UserTool.removeUserCache(request, response);
        return true;
    }

    @Override
    public boolean signUp(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        if (!SysToolHelper.isExists(params, "loginname", "userpwd", "ip", "mac")) {
            return false;
        }
        String userid = SysToolHelper.readSeqBySeqKeyAndOwner("USER", "USER", true);
        params.put("userid", DateUtil.getDateForFormat(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_SSS.getValue()) + userid);
        String userpwd = SysToolHelper.encryptPwd(SysToolHelper.getMapValue(params, "userpwd"));
        params.put("userpwd", userpwd);
        params.put("status", "1");
        params.put("sex", "1");
        params.put("username","未定义");


        return daoClient.update("com.dessert.user.adduser", params) > 0 ? true : false;
    }

    @Override
    public boolean setLoginUserInfo(HttpServletRequest request, HttpServletResponse response) {
        User user = UserTool.getUserCache(request);
        if (user == null) {
            return false;
        }
        UserTool.setUserCache(request, response, user);
        return true;
    }

}
