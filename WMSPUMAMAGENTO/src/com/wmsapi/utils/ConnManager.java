package com.wmsapi.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import com.wmsapi.utils.PropManager;

public class ConnManager {
	private static ConnManager connMgr; 

	private Connection conn;
	private ConnManager() {		
	}
	
	public static ConnManager getInstance() {
		if(connMgr==null) {
			connMgr = new ConnManager();
		}
		return connMgr;
	}

	public Connection getConnection() throws SQLException {

		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("connInfo");
		
		try {
			Class.forName(prop.getProperty("connInfo.driver"));
			conn = DriverManager.getConnection(prop.getProperty("connInfo.url")
								,prop.getProperty("connInfo.userId")
								,prop.getProperty("connInfo.userPw"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
}
