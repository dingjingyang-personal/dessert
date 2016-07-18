package com.dessert.home.controller;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/30.
 */
@Controller
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 查询用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("findUsers.htm")
    public String findUsers(HttpServletRequest request , HttpServletResponse response ){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        Page userPage = userService.findUsersPage(params);
        request.setAttribute("userPage",userPage);
        return "system/user/userManageMain";
    }



}
