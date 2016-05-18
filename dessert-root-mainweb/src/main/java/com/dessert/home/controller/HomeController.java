package com.dessert.home.controller;

import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.tool.CookieHelper;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.UserTool;
import com.dessert.sys.common.tool.ValidateUtils;
import com.dessert.system.service.home.service.HomeService;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


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

        String loginname = SysToolHelper.getMapValue(params, "loginname");
        String email = SysToolHelper.getMapValue(params, "email");

        if (userMap == null || userMap.isEmpty()) {
            SysToolHelper.outputByResponse("true", response);
        } else {
            String userLoginName = SysToolHelper.getMapValue(userMap, "loginname");
            String userEmail = SysToolHelper.getMapValue(userMap, "email");
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
    public void signUp(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        boolean isnull = SysToolHelper.isExists(params, "loginname", "email", "userpwd");
        if (!isnull) {
            SysToolHelper.outputByResponse("2", response);
            return;
        }

        String email = SysToolHelper.getMapValue(params, "email");
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
    public void login(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        String loginNameOrEmail = SysToolHelper.getMapValue(params, "loginnameoremail");
        if (ValidateUtils.Email(loginNameOrEmail)) {
            params.put("email", loginNameOrEmail);
        } else {
            params.put("loginname", loginNameOrEmail);
        }

        SysToolHelper.outputByResponse(homeService
                .login(params, request, response) ? "1" : SysToolHelper.getMapValue(params, "error", "2"), response);
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

        if(homeService.setLoginUserInfo(request,response)){
            return "/home/index";
        }
        return "redirect:showLoginPage.htm";
    }

}
