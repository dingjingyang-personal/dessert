package com.dessert.sys.timer.controller;

import com.dessert.sys.timer.service.AlarmClockURlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/alarmclockurl")
public class AlarmClockURLController {

	@Autowired
	private AlarmClockURlService alarmClockURlService;

	@RequestMapping("/invokeMethod.htm")
	public void invokeMethod(HttpServletRequest request, HttpServletResponse response) {
		alarmClockURlService.parseRemoteMethod(request, response);
	}

}
