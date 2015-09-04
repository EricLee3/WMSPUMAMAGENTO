package com.wmsapi.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class PropManager {
	private static PropManager propMgr;
	
	private PropManager() {
	}
	
	public static PropManager getInstance() {
		if(propMgr==null) {
			propMgr = new PropManager();
		}
		return propMgr;
	}
	
	public Properties getProp(String propName) {
		FileInputStream fis = null;
		Properties prop = null;
		try {
//			fis = new FileInputStream("D:/workspace_WMS_ACT/WMSAPI/props/"+propName+".properties");
			fis = new FileInputStream("/export/home/wmsapi/props/"+propName+".properties");
			prop = new Properties();
			prop.load(fis);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return prop;
	}
}
