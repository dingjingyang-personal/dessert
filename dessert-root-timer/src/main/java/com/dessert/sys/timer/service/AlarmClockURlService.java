package com.dessert.sys.timer.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AlarmClockURlService {

	void parseRemoteMethod(HttpServletRequest request, HttpServletResponse response);

}
