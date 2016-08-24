package com.dessert.controller.log;


import com.dessert.sys.common.annotation.SystemOperatingLog;
import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.log.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/system/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService ;

    /**
     * 返回异常日志页面
     * @return
     */
    @RequestMapping("findErrorLogs.htm")
    public String findErrorLogs(){
        return "system/log/errorlogmain";
    }


    /**
     * 查询异常日志数据
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-日志管理",methods = "异常日志-查询异常日志数据")
    @RequestMapping("findErrorLogJson.htm")
    public void findErrorLogJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page errorogLPage = sysLogService.findErrorLogPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(errorogLPage), response);
    }


    /**
     * 返回操作日志页面
     * @return
     */
    @RequestMapping("findOperationLogs.htm")
    public String findOperationLogs(){
        return "system/log/operationlogmain";
    }


    /**
     * 查询操作日志数据
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-日志管理",methods = "异常日志-查询操作日志数据")
    @RequestMapping("findOperationLogJson.htm")
    public void findOperationLogJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page operationogLPage = sysLogService.findOperationLogPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(operationogLPage), response);
    }


    /**
     * 返回登录日志页面
     * @return
     */
    @RequestMapping("findLoginLogs.htm")
    public String findLoginLogs(){
        return "system/log/loginlogmain";
    }


    /**
     * 查询登录日志数据
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-日志管理",methods = "登录日志-查询登录日志数据")
    @RequestMapping("findLoginLogJson.htm")
    public void findLoginLogJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page errorogLPage = sysLogService.findLoginLogPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(errorogLPage), response);
    }
}
