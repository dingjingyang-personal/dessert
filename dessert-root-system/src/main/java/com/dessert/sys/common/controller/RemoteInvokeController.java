package com.dessert.sys.common.controller;

import com.dessert.sys.remote.service.RemoteHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/remote")
@Controller
public class RemoteInvokeController {
	@Autowired
	private RemoteHelperService remoteHelperService;
	/**
	 * 功能描述: 调用bean
	 * author:liwm
	 * @param request
	 * @param response
	 */
	@RequestMapping("/invokeMethod.htm")
   public void invokeMethod(HttpServletRequest request,HttpServletResponse response) {
	   remoteHelperService.parseRemoteMethod(request, response);
   }
}
