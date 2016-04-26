package com.dessert.sys.timer.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.rhc.system.service.merchantPointRecord.service.MerPointRecordService;

public class TestQuartz {
	
	@Autowired(required = false)
	private MerPointRecordService merPointRecordService;
	
	public void run(){
		System.out.println(">>>>>><<<访问型测试,如出现,则成功!"+Thread.currentThread().getName());
	}
	
	public void pointRecord(){
		merPointRecordService.eliminatePoint();
	}
}
