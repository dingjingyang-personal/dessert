package com.dessert.system.controller;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.ValidateUtils;
import com.dessert.system.service.role.service.RoleService;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dessert.sys.common.tool.SysToolHelper.getMapValue;

/**
 * Created by admin-ding on 2016/7/27.
 */

@Controller
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    /**
     * 查询用户
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("findUsers.htm")
    public String findUsers(HttpServletRequest request, HttpServletResponse response) {
        return "system/user/userManageMain";
    }

    /**
     * 返回页面数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("findUsersJson.htm")
    public void findUsersJson(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page userPage = userService.findUsersPage(params);
        String str = SysToolHelper.getJsonOfObject(userPage);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(userPage), response);
    }

    /**
     * 准备添加或修改页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> userMap = userService.findUserMap(params);
        if (!SysToolHelper.isEmptyMap(userMap)) {
            request.setAttribute("userMap", userMap);
        }
        return "system/user/addOrUpdateUserPage";
    }


    /**
     * 添加用户
     *
     * @param request
     * @param response
     */
    @RequestMapping("addUser.htm")
    public void addUser(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        boolean isnull = SysToolHelper.isExists(params, "username", "email", "userpwd");
        if (!isnull) {
            SysToolHelper.outputByResponse("2", response);
            return;
        }

        String email = getMapValue(params, "email");
        boolean isemail = ValidateUtils.Email(email);

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
        SysToolHelper.outputByResponse(userService.addUser(params) ? "1" : "2", response);

    }

    /**
     * 更新用户
     *
     * @param request
     * @param response
     */
    @RequestMapping("updateUser.htm")
    public void updateUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(userService.updateUser(params) ? "1" : "2", response);
    }

    /**
     * 删除用户
     *
     * @param request
     * @param response
     */
    @RequestMapping("deleteUser.htm")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(userService.deleteUser(params) ? "1" : "删除失败，请稍后重试!", response);
    }

    /**
     * 分配角色页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("assigningRolesPage.htm")
    public String assigningRolesPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        List<Map<String, Object>> rolesList = userService.findRoles(params);
        List<Map<String, Object>> allRolesList = roleService.findRoles(new HashMap<String, Object>());

        if (rolesList != null && !rolesList.isEmpty() && allRolesList != null && !allRolesList.isEmpty()) {
            for (Map<String, Object> roleMap : allRolesList) {
                for (Map<String, Object> temp : rolesList) {
                    if (roleMap.get("roleid") .equals(temp.get("roleid")))  {
                        roleMap.put("isAssigning", true);
                        continue;
                    }
                }
            }


        }
        String allRolesListStr = SysToolHelper.getJsonOfCollection(allRolesList);
        request.setAttribute("allRolesListStr", allRolesListStr);

        return "system/user/assigningRoles";
    }


    /**
     * 分配角色
     *
     * @param request
     * @param response
     */
    @RequestMapping("assigningRoles.htm")
    public void assigningRoles(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(userService.addAssigningRoles(params) ? "1" : "2", response);
    }


}
