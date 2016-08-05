package com.dessert.sys.common.tool;

/**
 * Created by admin-ding on 2016/8/2.
 */
public class StringUtil {
    public static boolean isNullOrEmpty(Object obj) {
        return obj==null||String.valueOf(obj).length()==0;
    }
}
