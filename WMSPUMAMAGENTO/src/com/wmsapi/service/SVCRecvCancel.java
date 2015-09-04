package com.wmsapi.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.wmsapi.dao.DAORecvAPI;
import com.wmsapi.dto.DTORecvCancel;
import com.wmsapi.utils.PropManager;

public class SVCRecvCancel {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	public String[] callProcCancel(JSONObject body) {
		String[] resp = new String[4];
		DAORecvAPI dao = DAORecvAPI.getInstance();
		DTORecvCancel dtoCancel = new DTORecvCancel();
		
		dtoCancel.setBrandCd(body.getString("brandCd"));
		dtoCancel.setCenterCd(body.getString("centerCd"));
		dtoCancel.setBrandDate(body.getString("brandDate"));
		dtoCancel.setBrandNo(body.getString("brandNo"));
		dtoCancel.setCancelDiv(body.getString("cancelDiv").replaceAll("0", ""));
		
		resp = dao.callProcCancel(dtoCancel);
		return resp;
	}
	
//	public String createFile(JSONArray body) {
//		String respString = "";
//		StringBuilder sb = new StringBuilder();
//		PropManager propMgr = PropManager.getInstance();
//		Properties prop = propMgr.getProp("fileInfo");
//		
//		for(int i=0; i<body.size(); i++) {
//			JSONArray order = body.getJSONArray(i);
//			for(int j=0; j<order.size(); j++) {
//				JSONObject jsonObj = order.getJSONObject(j);
//				sb.append(jsonObj.getString("centerCd")).append(",")
//				.append(jsonObj.getString("brandCd")).append(",")
//				.append(jsonObj.getString("brandDate")).append(",")
//				.append(jsonObj.getString("brandNo")).append(",")
//				.append(jsonObj.getString("cancelDiv"));
//				sb.append("$row$");
//			}
//			sb.append("$otherBrandNo$");
//		}
	
	public String createFile(JSONObject body) {
		String respString = "";
		StringBuilder sb = new StringBuilder();
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("fileInfo");

		sb.append(body.getString("centerCd")).append(",")
		.append(body.getString("brandCd")).append(",")
		.append(body.getString("brandDate")).append(",")
		.append(body.getString("brandNo")).append(",")
		.append(body.getString("cancelDiv"));
		sb.append("$row$");
				
		BufferedWriter writer = null;
		try {
			Calendar cal = Calendar.getInstance();
			String month = "";
			String day = "";
			if(cal.get(Calendar.MONTH)+1 < 10) {
				month = "0"+(cal.get(Calendar.MONTH)+1);
			} else {
				month = String.valueOf(cal.get(Calendar.MONTH)+1);
			}
			
			if(cal.get(Calendar.DAY_OF_MONTH) < 10) {
				day = "0"+(cal.get(Calendar.DAY_OF_MONTH));
			} else {
				day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			}		
			String today = String.valueOf(cal.get(Calendar.YEAR))+month+day;

			File f = new File(prop.getProperty("filePath"));
			String[] fileNames = f.list();
			String fileName = "";
			int seq=0;
			int i=0;
			
			Arrays.sort(fileNames);
			if(fileNames.length<1) {
				fileName = today+"_cancel_0";
			} else {
				while(fileName.length() < 1) {
					if((today+"_cancel_"+seq).equals(fileNames[i].replaceAll(".txt", ""))) {
						seq++;
					}
					
					if(i<(fileNames.length-1)) {
						i++;
					} else {
						fileName = (today+"_cancel_"+seq);
						break;
					}
				}
			}
			
			writer = new BufferedWriter(new FileWriter(prop.getProperty("filePath")+fileName+".txt"));
			writer.write(sb.toString());
			logger.info("파일생성 "+fileName+".txt");
			respString = "SUCC"; 
		} catch (IOException e) {
			loggerErr.error(e.getMessage());
			respString = "ERR_004";
		} finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (Exception e) {
					loggerErr.error(e.getMessage());
					respString = "ERR_004";
				}
			}
		}
		
		return respString;
	}
}
