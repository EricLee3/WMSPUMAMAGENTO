package com.wmsapi.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.wmsapi.dao.DAOSendAPI;
import com.wmsapi.dto.DTOSendExpressTrace;
import com.wmsapi.utils.PropManager;

public class TaskSendExpressDaily extends TimerTask {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
	public void run() {
		logger.info("----TaskExpressTrace Start");
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("sendExpressTrace");
		DAOSendAPI dao = DAOSendAPI.getInstance();
		try {
			URL url = null;
			String[] brandCdList = prop.getProperty("sendExpressTrace.list").split(",");
			for(int i=0; i<brandCdList.length; i++) {
				ArrayList<DTOSendExpressTrace> list = dao.callSendExpressTrace(brandCdList[i], "10");
				url = new URL(prop.getProperty("sendExpressTrace."+brandCdList[i]+".url")); 
				if(list.size() < 1) {
					logger.info("해당건의 배송추적데이터가 없습니다.");
					continue;
				}
				
				JSONObject sendData = createSendExTraceData(list);
				String result = sendByPhp(url, sendData.toString());
//					if("Y".equals(result)) {
				dao.updateExtraceSucc(brandCdList[i], "60");
//					}
				logger.debug("Send Result: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.toString());
			
			loggerErr.error(e.getMessage());
			loggerErr.error(e.toString());
		}
		logger.info("----TaskExpressTrace End");
	}
	
	private JSONObject createSendExTraceData(ArrayList<DTOSendExpressTrace> list) throws Exception {
		JSONObject sendObj = new JSONObject();
		JSONObject sendHeader = new JSONObject();
		JSONArray sendBody = new JSONArray();
		
		sendHeader.put("bizUserId", list.get(0).getBizUserId());
		sendHeader.put("bizUserPw", list.get(0).getBizUserPw());
		sendHeader.put("callId", list.get(0).getCallId());
		sendHeader.put("encType", list.get(0).getEncType());
		
		for(int i=0; i<list.size(); i++) {
			DTOSendExpressTrace dto = list.get(i);
			JSONObject row = new JSONObject();
			row.put("brandNo", dto.getBrandNo());
			row.put("expressNo", dto.getExpressNo());
			row.put("dvState", dto.getDvState());
			if(dto.getNoDvReason() == null) {
				row.put("noDvReason", "");
			} else {
				row.put("noDvReason", dto.getNoDvReason());
			}
			row.put("regDatetime", dto.getRegDatetime());
			
			sendBody.add(row);
		}
		
		sendObj.put("header", sendHeader);
		sendObj.put("body", sendBody);
		
		logger.debug(" create sendObj : "+sendObj.toString());
	
		return sendObj;
	}
	
	private String sendByPhp(URL url, String sendData) {
		StringBuilder sb = new StringBuilder();
		OutputStream os = null;
		BufferedReader in = null;
		String respString = "";
		try {
			String type= "application/x-www-form-urlencoded";
			String encodedData = "msg="+URLEncoder.encode(sendData, "UTF-8");
			
//			url = new URL("http://test.puma.co.kr/api/index.php");
	        HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
	        urlConn.setDoOutput(true);
	        urlConn.setRequestMethod("POST");
	        urlConn.setRequestProperty("Content-Type", type);
	        urlConn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
	
	        os = urlConn.getOutputStream();
	        os.write(encodedData.getBytes());
	        
	        in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
	        while ((respString = in.readLine()) != null) {
	        	logger.info("response: "+respString);
	        	
	        	sb.append(respString);
	        }
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error(e.toString());
			
			loggerErr.error(e.getMessage());
			loggerErr.error(e.toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e.toString());
			
			loggerErr.error(e.getMessage());
			loggerErr.error(e.toString());
		} finally {
			try {
				if(os!=null) {
					os.close();
				}
				if(in!=null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error(e.toString());
				
				loggerErr.error(e.getMessage());
				loggerErr.error(e.toString());
			}			
		}
		return sb.toString();
	}
}
