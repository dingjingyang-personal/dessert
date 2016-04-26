package com.dessert.sys.common.tool;

public class StringUtil {
public static boolean isNullOrEmpty(Object obj) {
	return obj==null||String.valueOf(obj).length()==0;
}
}
