package com.dessert.sys.common.test;

import com.dessert.sys.common.enm.DateStyle;
import com.dessert.sys.common.tool.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ding-Admin on 2016/5/10.
 */
public class DateDemo {

    public static void main(String[] args){
        System.out.println(DateUtil.getDate(new Date()));


        System.out.println(DateUtil.getDateForFormat(new Date(),  DateStyle.YYYY_MM_DD_HH_MM_SS_SSS.getValue()));

        SimpleDateFormat df = new SimpleDateFormat();
        System.out.println(df.format(new Date()));
    }
}
