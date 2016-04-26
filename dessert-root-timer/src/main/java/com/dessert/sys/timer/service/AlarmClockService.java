package com.dessert.sys.timer.service;

import java.util.List;
import java.util.Map;


public interface AlarmClockService {


	Map<String, Object> getClock(Map<String, Object> params);

	boolean addaddClock(Map<String, Object> params);

	boolean updateClock(Map<String, Object> params);

	boolean deleteClockById(Map<String, Object> params);

	List<Map<String, Object>> getClockList();



}
