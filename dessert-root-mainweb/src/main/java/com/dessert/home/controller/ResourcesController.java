package com.dessert.home.controller;

import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.resources.service.ResourcesService;
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
    private ResourcesService resourcesService ;

    /**
     * 查询资源页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("findResources.htm")
    public String findResources(HttpServletRequest  request , HttpServletResponse response ){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        List<Map<String,Object>> resourcesList= resourcesService.findResources(params);
        request.setAttribute("resourcesList",SysToolHelper.getJsonOfCollection(resourcesList));
        return "system/resources/resourcesManageMain";
    }

    /**
     * 添加资源
     * @param request
     * @param response
     */
    @RequestMapping("addResources.htm")
    public void addResources(HttpServletRequest request ,HttpServletResponse response ){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(resourcesService.addResources(params)?"1":"2",response);
    }


    /**
     * 修改资源
     * @param request
     * @param response
     */
    @RequestMapping("updateResources.htm")
    public void updateResources(HttpServletRequest request ,HttpServletResponse response ){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(resourcesService.updateResources(params)?"1":"2",response);
    }

    /**
     * 删除资源
     * @param request
     * @param response
     */
    @RequestMapping("deleteResources.htm")
    public void deleteResources(HttpServletRequest request ,HttpServletResponse response ){
        Map<String,Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(resourcesService.deleteResources(params)?"1":"2",response);
    }

}
