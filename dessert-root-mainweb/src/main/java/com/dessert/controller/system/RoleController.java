package com.dessert.controller.system;

import com.dessert.sys.common.annotation.SystemOperatingLog;
import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.resources.service.ResourcesService;
import com.dessert.system.service.role.service.RoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("system/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourcesService resourcesService;


    /**
     * 查询角色页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("findRoles.htm")
    public String findRoles(HttpServletRequest request, HttpServletResponse response) {
        return "system/role/roleManageMain";
    }


    /**
     * 返回页面数据
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "角色管理-查询角色数据")
    @RequestMapping("findRolesJson.htm")
    public void findRolesJson(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page rolePage = roleService.findRolesPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(rolePage), response);
    }

    /**
     * 准备添加或修改页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> roleMap = roleService.findRole(params);
        if (!SysToolHelper.isEmptyMap(roleMap)) {
            request.setAttribute("roleMap", roleMap);
        }
        return "system/role/addOrUpdateRolePage";
    }


    /**
     * 添加角色
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "角色管理-添加角色")
    @RequestMapping("addRole.htm")
    public void addRole(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.addRole(params) ? "1" : "2", response);

    }

    /**
     * 更新角色
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "角色管理-更新角色")
    @RequestMapping("updateRole.htm")
    public void updateRole(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.updateRole(params) ? "1" : "2", response);
    }

    /**
     * 删除角色
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "角色管理-删除角色")
    @RequestMapping("deleteRole.htm")
    public void deleteRole(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.deleteRole(params) ? "1" : "删除失败，请稍后重试!", response);
    }

    /**
     * 分配权限页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("assignPermissionsPage.htm")
    public String assignPermissionsPage(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        //获取角色拥有的权限
        List<Map<String, Object>> resourcesByRoleList = roleService.findResourcessByRole(params);
        //获取所有权限
        List<Map<String, Object>> resourcesList = resourcesService.selectResourcesByrole(new HashMap<String, Object>());

        List<Map<String, Object>> resourcesByRoleListClon = Lists.newArrayList(resourcesByRoleList);

        //所有权限的map集合
        Map<String, Map<String, Object>> resourcesListMap = SysToolHelper.listsToMap(resourcesList, "menuid");
        //角色所拥有权限的map集合
        Map<String, Map<String, Object>> resourcesByRoleListClonMap = SysToolHelper.listsToMap(resourcesByRoleListClon, "menuid");

        Map<String, String> resourceIdMap = Maps.newHashMap();

        for (Map<String, Object> resourcesMap : resourcesByRoleList) {
            resourceIdMap.put(SysToolHelper.getMapValue(resourcesMap, "menuid"), "");
            if (SysToolHelper.getMapValue(resourcesMap, "action") != null || !SysToolHelper.getMapValue(resourcesMap, "action").equals("")) {
                String parentId = SysToolHelper.getMapValue(resourcesMap, "parentid");
                boolean isParent = true;
                while (isParent) {
                    if (!parentId.equals("0")) {
                        Map<String, Object> parnetMap = resourcesListMap.get(parentId);
                        if(!resourcesByRoleListClonMap.containsKey(SysToolHelper.getMapValue(parnetMap,"menuid"))){
                            resourcesByRoleListClonMap.put(SysToolHelper.getMapValue(parnetMap,"menuid"), parnetMap);
                            resourcesByRoleListClon.add(parnetMap);
                        }
                        parentId = SysToolHelper.getMapValue(parnetMap, "parentid");
                    } else {
                        isParent = false;
                    }
                }
            }
        }

        for (Map<String, Object> temp : resourcesList) {
            if (resourceIdMap.containsKey(SysToolHelper.getMapValue(temp, "menuid"))) {
                temp.put("chkDisabled", "true");
                temp.put("checked", "true");
            }
        }

        String resourcesByRoleListStr = SysToolHelper.getJsonOfCollection(resourcesByRoleListClon);
        String noAssigListStr = SysToolHelper.getJsonOfCollection(resourcesList);

        request.setAttribute("resourcesByRoleListStr", resourcesByRoleListStr);
        request.setAttribute("noAssigListStr", noAssigListStr);

        return "system/role/assignPermissions";
    }


    /**
     * 授权
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "角色管理-授权角色")
    @RequestMapping("addOrDeletePermissions.htm")
    public void addOrDeletePermissions(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(roleService.addOrDeletePermissions(params) ? "success" : "fail", response);
    }


}

