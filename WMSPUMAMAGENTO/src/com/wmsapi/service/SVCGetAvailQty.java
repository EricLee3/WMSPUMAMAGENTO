package com.wmsapi.service;

import java.util.ArrayList;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.wmsapi.dao.DAORecvAPI;
import com.wmsapi.dto.DTOGetAvailQty;
import com.wmsapi.utils.PropManager;

public class SVCGetAvailQty {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
	public String callWmsProc(JSONObject body, ArrayList<DTOGetAvailQty> list) {
		
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("getAvailQty");
		String respString = "";
		try {
			
			DAORecvAPI dao = DAORecvAPI.getInstance();
			for(int i=0; i<list.size(); i++) {
				DTOGetAvailQty dto = list.get(i);
				dto.setCenterCd(prop.getProperty("getAvailQty."+body.getString("centerCd")));
				dto.setBrandCd(body.getString("brandCd"));
				dto.setItemCd(body.getString("itemCd"));
			}
			
			respString = dao.callProcGetAvailQty(list);
			logger.info(respString);
		} catch (Exception e) {
			e.printStackTrace();
			respString = "ERR_004";
			loggerErr.error(respString);
		}
		
		return respString;
	}
}
