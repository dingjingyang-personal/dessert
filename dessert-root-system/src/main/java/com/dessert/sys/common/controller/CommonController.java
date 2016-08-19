package com.dessert.sys.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理系统公用跳转页面
 */
@Controller
@RequestMapping("/sys")
public class CommonController {



    /**
     * 403错误页
     * @param request
     * @return
     */
    @RequestMapping("/403.htm")
    public String show403ErrorPage(HttpServletRequest request) {

        return "/common/error/403";
    }

    /**
     * 404错误页
     * @param request
     * @return
     */
    @RequestMapping("/404.htm")
    public String show404ErrorPage(HttpServletRequest request) {
        return "/common/error/404";
    }

    /**
     * 没有授权时跳转到的页面
     * @param request
     * @return
     */
    @RequestMapping("/showInvalidMenuPage.htm")
    public String showInvalidMenuPage(HttpServletRequest request) {
        return "/common/error/invalidMenuPage";
    }

    /**
     * 405错误页
     * @param request
     * @return
     */
    @RequestMapping("/405.htm")
    public String show405ErrorPage(HttpServletRequest request) {

        return "/common/error/405";
    }

    /**
     * 500错误页
     * @param request
     * @return
     */
    @RequestMapping("/500.htm")
    public String show500ErrorPage(HttpServletRequest request) {

        return "/common/error/500";
    }

    /**
     * 超时
     * @param request
     * @return
     */
    @RequestMapping("/timeout.htm")
    public String showTimeoutErrorPage(HttpServletRequest request) {

        return "/common/error/timeout";
    }

}
