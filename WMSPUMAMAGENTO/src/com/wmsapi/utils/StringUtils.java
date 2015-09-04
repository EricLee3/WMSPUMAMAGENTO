package com.wmsapi.utils;

public class StringUtils {
	
	public static String replaceNull(String value) {
		String rtnString = value; 
		if(value == null || value.length() < 1) {
			rtnString = null;
		}
		return rtnString;
	}
	
	public static String replaceNullStr(String value) {
		String str = value;
		if("null".equals(value.toLowerCase())) {
			str = "";
		}
		return str;
	}
}
