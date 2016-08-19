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
@RequestMapping("/syserrorlog")
public class Sys_Error_LogController {
    @Autowired
    private SysLogService sysLogService ;


    @SystemOperatingLog(module = "系统管理-日志管理",methods = "异常日志-查询异常日志")
    @RequestMapping("/findErrorLogs")
    public String findErrorLogs(){
        return "system/log/errorlogmain";
    }


    @RequestMapping("findErrorLogJson.htm")
    public void findErrorLogJson(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page errorogLPage = sysLogService.findErrorLogPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(errorogLPage), response);
    }
}
