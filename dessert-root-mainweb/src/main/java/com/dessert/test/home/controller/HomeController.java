package com.dessert.test.home.controller;

import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.tool.CookieHelper;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.ValidateUtils;
import com.dessert.test.system.service.home.service.HomeService;
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
     * 注册
     *
     * @param request
     * @param response
     */
    @RequestMapping("signup")
    public void signUp(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        boolean isnull = SysToolHelper.isExists(params, "loginname", "username", "email", "userpwd", "sex");
        if (!isnull) {
            SysToolHelper.outputByResponse("2", response);
            return;
        }

        String userpwd = SysToolHelper.getMapValue(params,"userpwd");
        String email = SysToolHelper.getMapValue(params,"email");
        String sex = SysToolHelper.getMapValue(params,"sex");

        boolean isemail = ValidateUtils.Email(email);
        boolean issex = ValidateUtils.Integer(sex);

        if(!(isemail&&issex)){
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

        params.put("status","1");
        params.put("userpwd",SysToolHelper.encryptPwd(userpwd));
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
        if (SysToolHelper.getEmployeeCache(request) != null) {
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

        return "redirect:showLoginPage.htm";
    }

}
