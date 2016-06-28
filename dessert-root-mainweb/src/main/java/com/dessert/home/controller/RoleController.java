package com.dessert.home.controller;

import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.home.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/22.
 */
@RequestMapping("system/role")
@Controller
public class RoleController {


    @Autowired
    private RoleService roleService;


    /**
     * 查询角色
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("findRoles.htm")
    public String findRoles(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);

        List<Map<String, Object>> roleList = roleService.findRoles(params);
        request.setAttribute("roleList", roleList);
        request.setAttribute("params", params);
        return "role/roleMain";
    }

    /**
     * 准备添加或修改页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        Map<String,Object> roleMap = roleService.findRole(params);
        if(!SysToolHelper.isEmptyMap(roleMap)){
            request.setAttribute("roleMap",roleMap);
        }
        return "role/addOrUpdateRolePage";
    }


    /**
     * 添加角色
     *
     * @param request
     * @param response
     */
    @RequestMapping("addRole.htm")
    public void addRole(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.addRole(params) ? "1" : "2", response);

    }

    /**
     * 更新角色
     * @param request
     * @param response
     */
    @RequestMapping("updateRole.htm")
    public void updateRole(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.updateRole(params) ? "1" : "2", response);
    }

    /**
     * 删除角色
     * @param request
     * @param response
     */
    @RequestMapping("deleteRole.htm")
    public void deleteRole(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.deleteRole(params)?"1":"2",response);
    }
}

