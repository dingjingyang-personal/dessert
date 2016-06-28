package com.dessert.home.controller;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.tool.*;
import com.dessert.sys.exception.service.ServiceException;
import com.dessert.system.service.home.service.HomeService;
import com.dessert.system.service.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dessert.sys.common.tool.SysToolHelper.getMapValue;


@RequestMapping("/home")
@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;


    /**
     * 注册页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/showSignUpPage.htm")
    public String showSignUpPage(HttpServletRequest request, HttpServletResponse response) {

        return "/home/signuppage";
    }


    /**
     * 校验登录名或邮箱
     *
     * @param request
     * @param response
     */
    @RequestMapping("/validateLoginNameOrEmail.htm")
    public void validateLoginName(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> userMap = userService.getUserMap(params);

        String loginname = getMapValue(params, "loginname");
        String email = getMapValue(params, "email");

        if (userMap == null || userMap.isEmpty()) {
            SysToolHelper.outputByResponse("true", response);
        } else {
            String userLoginName = getMapValue(userMap, "loginname");
            String userEmail = getMapValue(userMap, "email");
            if (loginname.equals(userLoginName) || email.equals(userEmail)) {
                SysToolHelper.outputByResponse("false", response);
            } else {
                SysToolHelper.outputByResponse("true", response);
            }
        }
    }


    /**
     * 注册
     *
     * @param request
     * @param response
     */
    @RequestMapping("signup")
    public void signUp(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        boolean isnull = SysToolHelper.isExists(params, "loginname", "email", "userpwd");
        if (!isnull) {
            SysToolHelper.outputByResponse("2", response);
            return;
        }

        String email = getMapValue(params, "email");
//        String sex = SysToolHelper.getMapValue(params, "sex");

        boolean isemail = ValidateUtils.Email(email);
//        boolean issex = ValidateUtils.Integer(sex);

        if (!(isemail)) {
            SysToolHelper.outputByResponse("2", response);
            return;
        }

        String ip = SysToolHelper.getIp(request);
        params.put("ip", ip);
        try {
            params.put("mac", SysToolHelper.getMACAddress(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }


        SysToolHelper.outputByResponse(homeService.signUp(params, request, response) ? "1" : "2", response);


    }


    /**
     * 处理邮件激活
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("processActivate")
    public String processActivate(HttpServletRequest request, HttpServletResponse response) {


        Map<String, Object> params = SysToolHelper.getRequestParams(request);

        Map<String, Object> message = new HashMap<String, Object>();
        Map<String, Object> messageparams = new HashMap<String, Object>();
        message.put("message", "邮箱已激活,请登录");
        try {
            homeService.processActivate(params);
        } catch (ServiceException e) {
            e.printStackTrace();
            messageparams = e.getExceptionParams();
            message.put("message", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (messageparams.isEmpty()) {
            messageparams.put("activateStatus", "100");
        }

        request.setAttribute("message", message);
        request.setAttribute("messageparams", messageparams);

        return "/home/mailactivate";

    }


    /**
     * 重新发送注册邮件
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("resendmail")
    public void resendmail(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> userMap = userService.getUserMap(params);
        if (userMap == null && userMap.isEmpty()) {
            SysToolHelper.outputByResponse("2", response);
        } else {
            userMap.put("activationdate", DateUtil.addDay(new Date(), 2));
            userService.updateUser(userMap);
            homeService.processregister(userMap);
            SysToolHelper.outputByResponse("1", response);
        }

    }

    /**
     * 功能描述: 登陆页
     *
     * @param request
     * @return
     */
    @RequestMapping("/showLoginPage.htm")
    public String showLoginPage(HttpServletRequest request) {
        if (UserTool.getUserCache(request) != null) {
            return "redirect:showIndex.htm";
        }
        request.setAttribute("username", CookieHelper.getInstance().getCookieValue(request, SysConstants.COOKIE_USERNO));
        return "/home/login";
    }

    /**
     * 功能描述: 登陆
     *
     * @param request
     * @param response
     */
    @RequestMapping("/login.htm")
    public void login(HttpServletRequest request, HttpServletResponse response,Model model) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);

        String msg ;

        String loginNameOrEmail = getMapValue(params, "loginnameoremail");
        String password = SysToolHelper.getMapValue(params,"userpwd");

        UsernamePasswordToken token = new UsernamePasswordToken(loginNameOrEmail, password);
//        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                Session session = SecurityUtils.getSubject().getSession();
                User user = (User) session.getAttribute("userSession");
                if(user!=null){
                    UserTool.setUserCache(request, response, user);
                    CookieHelper.getInstance().setCookie(response, SysConstants.COOKIE_USERNO, user.getUserno(), SysSettings.COOKIE_DOMAIN,SysConstants.WEEK_SECONDS);
                }
                SysToolHelper.outputByResponse("1",response);
                return;
            } else {
                SysToolHelper.outputByResponse("2",response);
                return;
            }
        } catch (IncorrectCredentialsException e) {
            msg = "登录密码错误";
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败次数过多";
        } catch (LockedAccountException e) {
            msg = "帐号已被锁定";
        } catch (DisabledAccountException e) {
            msg = "帐号已被禁用";
        } catch (ExpiredCredentialsException e) {
            msg = "帐号已过期";
        } catch (UnknownAccountException e) {
            msg = "帐号不存在";
        } catch (UnauthorizedException e) {
            msg = "您没有得到相应的授权";
        }




        SysToolHelper.outputByResponse(msg,response);
    }

    /**
     * 功能描述: 登出
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loginOut.htm")
    public String loginOut(HttpServletRequest request, HttpServletResponse response) {
        homeService.loginOut(request, response);
        return "redirect:showLoginPage.htm";
    }

    /**
     * 功能描述: 首页
     *
     * @param request
     * @param response
     */
    @RequestMapping("/showIndex.htm")
    public String showIndex(HttpServletRequest request, HttpServletResponse response) {

        if (homeService.setLoginUserInfo(request,response)) {
            return "/home/index";
        }
        return "redirect:showLoginPage.htm";
    }

    @RequestMapping("/showIndexdemo.htm")
    public String showIndexdemo(HttpServletRequest request, HttpServletResponse response) {

        return "home/demo";
    }

}
