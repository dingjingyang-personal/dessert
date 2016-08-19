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
@RequestMapping("/sysloginlog")
public class Sys_Login_LogController {
    @Autowired
    private SysLogService sysLogService ;


    @SystemOperatingLog(module = "系统管理-日志管理",methods = "登录日志-查询登录日志")
    @RequestMapping("/findLoginLogs")
    public String findLoginLogs(){
        return "system/log/loginlogmain";
    }


    @RequestMapping("findLoginLogJson.htm")
    public void findLoginLogJson(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page errorogLPage = sysLogService.findLoginLogPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(errorogLPage), response);
    }
}
