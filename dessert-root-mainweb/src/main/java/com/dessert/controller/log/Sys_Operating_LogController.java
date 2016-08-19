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
@RequestMapping("/sysoperationlog")
public class Sys_Operating_LogController {
    @Autowired
    private SysLogService sysLogService ;


    @SystemOperatingLog(module = "系统管理-日志管理",methods = "异常日志-查询异常日志")
    @RequestMapping("/findOperationLogs")
    public String findOperationLogs(){
        return "system/log/operationlogmain";
    }


    @RequestMapping("findOperationLogJson.htm")
    public void findOperationLogJson(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page operationogLPage = sysLogService.findOperationLogPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(operationogLPage), response);
    }
}
