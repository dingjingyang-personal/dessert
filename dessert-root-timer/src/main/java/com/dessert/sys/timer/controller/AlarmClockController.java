package com.dessert.sys.timer.controller;

import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.timer.service.AlarmClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/alarmclock")
public class AlarmClockController {

	@Autowired
	private AlarmClockService alarmClockService;


	/**
	 * 准备添加或修改定时器
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addorUpdateClock.htm")
	public String addorUpdateClock(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = SysToolHelper.getRequestParams(request);

		if (params.get("clockid") != null && !params.get("clockid").equals("")) {
			Map<String, Object> clockMap = alarmClockService.getClock(params);
			request.setAttribute("clockMap", clockMap);
		}

		return "/alarmclock/addorUpdateClock";
	}

	/**
	 * 添加定时器
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addClock.htm")
	public void addClock(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = SysToolHelper.getRequestParams(request);

		SysToolHelper.outputByResponse(alarmClockService.addaddClock(params) ? "1" : "添加失败", response);
	}

	/**
	 * 修改定时器
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateClock.htm")
	public void updateClock(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = SysToolHelper.getRequestParams(request);

		SysToolHelper.outputByResponse(alarmClockService.updateClock(params) ? "1" : "修改失败", response);
	}

	/**
	 * 删除定时器
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/deleteClockById.htm")
	public void deleteClockById(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> params = SysToolHelper.getRequestParams(request);

		SysToolHelper.outputByResponse(alarmClockService.deleteClockById(params) ? "1" : "刪除失败", response);
	}



}
