package com.dessert.sys.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理系统公用跳转页面
 */
@Controller
@RequestMapping("/sys")
public class CommonController {



    /**
     * 403错误页
     * @return
     */
    @RequestMapping("/403.html")
    public String show403ErrorPage() {

        return "/common/error/403";
    }

    /**
     * 404错误页
     * @return
     */
    @RequestMapping("/404.html")
    public String show404ErrorPage() {
        return "/common/error/404";
    }



    /**
     * 405错误页
     * @return
     */
    @RequestMapping("/405.html")
    public String show405ErrorPage() {

        return "/common/error/405";
    }

    /**
     * 500错误页
     * @return
     */
    @RequestMapping("/500.html")
    public String show500ErrorPage() {

        return "/common/error/500";
    }

    /**
     * 超时
     * @return
     */
    @RequestMapping("/timeout.html")
    public String showTimeoutErrorPage() {

        return "/common/error/timeout";
    }

    /**
     * 没有授权时跳转到的页面
     * @return
     */
    @RequestMapping("/showInvalidMenuPage.html")
    public String showInvalidMenuPage() {
        return "/common/error/invalidMenuPage";
    }

}
