package com.dessert.controller.system;

import com.dessert.sys.common.annotation.SystemOperatingLog;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.resources.service.ResourcesService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/30.
 */
@Controller
@RequestMapping("system/resources")
public class ResourcesController {

    @Autowired
    private ResourcesService resourcesService;

    /**
     * 查询资源页面
     *
     * @param request
     * @param response
     * @return
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "资源管理-查询资源")
    @RequestMapping("findResources.htm")
    public String findResources(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        List<Map<String, Object>> resourcesList = resourcesService.findResources(params);
        String obj = SysToolHelper.getJsonOfCollection(resourcesList);
        request.setAttribute("resourcesList", obj);
        return "system/resources/resourcesManageMain";
    }


    /**
     * 准备添加或修改菜单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrUpdateResourcesPage.htm")
    public String addOrUpdateResourcesPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> menuMap = resourcesService.findResource(params);

        if (menuMap != null && !menuMap.isEmpty()) {
            String flag = SysToolHelper.getMapValue(params, "flag");
            //标记为A标示新增，只有父菜单信息
            if (flag.equals("A")) {
                //当为新增时,选中的行就是新增菜单的父菜单
                request.setAttribute("supMenuMap", menuMap);
            } else {//标记不为A标示修改，返回当前菜单信息和父菜单信息
                request.setAttribute("MenuMap", menuMap);
                Map<String, Object> parentParams = Maps.newHashMap();
                parentParams.put("menuid", SysToolHelper.getMapValue(menuMap, "parentid"));
                Map<String, Object> parentMap = resourcesService.findResource(parentParams);
                if (parentMap != null && !parentMap.isEmpty()) {
                    request.setAttribute("supMenuMap", parentMap);
                }
            }
        }
        return "system/resources/addOrUpdateResourcesPage";

    }

    /**
     * 添加或修改资源
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "资源管理-添加或修改资源")
    @RequestMapping("addOrUpdateResources.htm")
    public void addOrUpdateResources(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);

        String flag = SysToolHelper.getMapValue(params, "flag");
        boolean result=false;

        //A代表新建菜单
        if("A".equals(flag)){
            result = resourcesService.addResources(params);
        }else {//否则为修改菜单
            result = resourcesService.updateResources(params);
        }
        SysToolHelper.outputByResponse(result, response);
    }


    /**
     * 修改资源序号
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "资源管理-修改资源序号")
    @RequestMapping("updateMenuOrder.htm")
    public void updateMenuOrder(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(resourcesService.updateMenuOrder(params) , response);
    }

    /**
     * 删除资源
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-权限管理",methods = "资源管理-删除资源")
    @RequestMapping("deleteResources.htm")
    public void deleteResources(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(resourcesService.deleteResources(params) ? "1" : "2", response);
    }

}
