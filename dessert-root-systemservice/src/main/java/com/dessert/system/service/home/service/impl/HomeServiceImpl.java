package com.dessert.system.service.home.service.impl;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.enm.DateStyle;
import com.dessert.sys.common.tool.*;
import com.dessert.sys.exception.service.ServiceException;
import com.dessert.system.service.home.service.HomeService;
import com.dessert.system.service.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dessert.sys.common.tool.SysToolHelper.encryptPwd;

/**
 * 登陆、首页类
 *
 * @author
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

        Map<String, Object> userMap = userService.findUserMap(params);

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
        user.setUsername(SysToolHelper.getMapValue(userMap, "username"));
        user.setLoginname(SysToolHelper.getMapValue(userMap, "loginname"));
        user.setStatus(Integer.parseInt(SysToolHelper.getMapValue(userMap, "status")));

        Map<String, Object> tempMap = userMap;

        UserTool.setUserCache(request, response, user);
        CookieHelper.getInstance().setCookie(response, SysConstants.COOKIE_USERNO, user.getUserno(), SysSettings.COOKIE_DOMAIN, SysConstants.WEEK_SECONDS);
        return true;
    }


    @Override
    public boolean loginOut(HttpServletRequest request, HttpServletResponse response) {
        UserTool.removeUserCache(request, response);
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();
        }
        return true;
    }

    @Override
    public boolean signUp(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!SysToolHelper.isExists(params, "loginname", "userpwd", "ip", "mac")) {
            return false;
        }
        String userid = SysToolHelper.readSeqBySeqKeyAndOwner("USER", "USER", true);
        params.put("userid", DateUtil.getDateForFormat(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_SSS.getValue()) + userid);


        String userpwd = MD5Util.encode2hex(SysToolHelper.getMapValue(params, "userpwd"));
//        String userpwd = SysToolHelper.encryptPwd(SysToolHelper.getMapValue(params, "userpwd"));
        params.put("userpwd", userpwd);
        params.put("status", "1");
        params.put("sex", "1");
        params.put("username", "未定义");
        params.put("state", "0");

        Date activationdate = DateUtil.addDay(new Date(), 2);
        String acticode = MD5Util.encode2hex(SysToolHelper.getMapValue(params, "email"));

        params.put("activationdate", activationdate);
        params.put("acticode", acticode);

        processregister(params);


        return daoClient.update("com.dessert.user.adduser", params) > 0 ;
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


    /**
     * 注册发送邮件
     */

    @Override
    public void processregister(Map<String, Object> userMap) throws Exception {

        String email = SysToolHelper.getMapValue(userMap, "email");
        String loginname = SysToolHelper.getMapValue(userMap, "loginname");
        StringBuilder stburl = new StringBuilder();
        stburl.append("http://localhost:8080/dessert-root-mainweb/home/processActivate.htm?email=");
        stburl.append(email);
        stburl.append("&acticode=");
        stburl.append(SysToolHelper.getMapValue(userMap, "acticode"));


        ///邮件的内容
        StringBuilder stb = new StringBuilder();

        stb.append("<p> <a target=\"_blank\" href=\"mailto:");
        stb.append(email);
        stb.append("\">");
        stb.append(loginname);
        stb.append("</a> 您好, 欢迎来到小站. </p> \n" +
                "<p> 请点击链接验证邮箱: <a target=\"_blank\" href=\"");
        stb.append(stburl);
        stb.append("\">");
        stb.append(stburl);
        stb.append("</a> </p> ");


        //发送邮件
        SendEmail.send(email, stb.toString());

    }

    /**
     * 处理激活
     *
     * @throws ParseException
     */
    ///传递激活码和email过来
    @Override
    public void processActivate(Map<String, Object> params) throws ServiceException, ParseException {

        Map<String,Object> exceptionParams = new HashMap<String, Object>();

        //数据访问层，通过email获取用户信息
        Map<String, Object> userMap = userService.findUserMap(params);
        //验证用户是否存在
        if (userMap != null && !userMap.isEmpty()) {
            //验证用户激活状态
            if ((SysToolHelper.getMapValue(userMap, "state")).equals("0")) {
                ///没激活
                Date currentTime = new Date();//获取当前时间
                //验证链接是否过期
                if (currentTime.before(DateUtil.StringToDate(SysToolHelper.getMapValue(userMap, "activationdate")))) {
                    //验证激活码是否正确
                    if (SysToolHelper.getMapValue(params, "acticode").equals(SysToolHelper.getMapValue(userMap, "acticode"))) {

                        //激活成功， //并更新用户的激活状态，为已激活
                        Map<String, Object> activUserMap = new HashMap<String, Object>();
                        activUserMap.put("userid", SysToolHelper.getMapValue(userMap, "userid"));
                        activUserMap.put("state", 1);

                        daoClient.update("com.dessert.user.updateuser", activUserMap);
                    } else {
                        exceptionParams.put("activateStatus","104");
                        throw new ServiceException("激活码不正确",exceptionParams);
                    }
                } else {
                    exceptionParams.put("activateStatus","103");
                    throw new ServiceException("激活码已过期！请重新注册！",exceptionParams);
                }
            } else {
                exceptionParams.put("activateStatus","102");
                throw new ServiceException("邮箱已激活，请登录！",exceptionParams);
            }
        } else {
            exceptionParams.put("activateStatus","101");
            throw new ServiceException("连接已失效,请重新注册！",exceptionParams);
        }

    }

}
