package com.dessert.sys.common.controller;

import com.dessert.sys.common.tool.SysToolHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理系统公用跳转页面
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/sys")
public class CommonController {
    /**
     * 〈获取uuid值〉
     *
     * @param response
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/getUuid.htm")
    public void getUuid(HttpServletResponse response) {

        SysToolHelper.outputByResponse(SysToolHelper.getUuid(), response);
    }

    @RequestMapping("/attackHeart.htm")
    public void attackHeart(HttpServletResponse response) {
        SysToolHelper.outputByResponse("1", response);
    }

    /**
     * 〈403错误页〉 〈功能详细描述〉
     *
     * @param request
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/403.htm")
    public String show403ErrorPage(HttpServletRequest request) {

        return "/common/error/403";
    }

    /**
     * 〈404错误页〉 〈功能详细描述〉
     *
     * @param request
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/404.htm")
    public String show404ErrorPage(HttpServletRequest request) {
        String uri = (String) request.getAttribute("javax.servlet.forward.request_uri");
        request.setAttribute("toUri", uri);
        return "/common/error/404";
    }

    /**
     * 〈404错误页〉 〈功能详细描述〉
     *
     * @param request
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/showInvalidMenuPage.htm")
    public String showInvalidMenuPage(HttpServletRequest request) {
        String uri = (String) request.getAttribute("javax.servlet.forward.request_uri");
        request.setAttribute("toUri", uri);
        return "/common/error/invalidMenuPage";
    }

    /**
     * 〈405错误页〉 〈功能详细描述〉
     *
     * @param request
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/405.htm")
    public String show405ErrorPage(HttpServletRequest request) {

        return "/common/error/405";
    }

    /**
     * 〈500错误页〉 〈功能详细描述〉
     *
     * @param request
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/500.htm")
    public String show500ErrorPage(HttpServletRequest request) {

        return "/common/error/500";
    }

    /**
     * 〈超时〉 〈功能详细描述〉
     *
     * @param request
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @RequestMapping("/timeout.htm")
    public String showTimeoutErrorPage(HttpServletRequest request) {

        return "/common/error/timeout";
    }


    @RequestMapping("/getValidatecode.htm")
    public void showValidatecode(HttpServletRequest request, HttpServletResponse response) {
        //ImageUtils.createValidateCode(request, response);
    }
}
