package com.wmsapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.wmsapi.utils.PropManager;

public class SVCSendUpdInventory {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
//	public void callSendUpdInventory(String centerCd, String brandCd) {
//	
//		PropManager propMgr = PropManager.getInstance();
//		Properties prop = propMgr.getProp("sendUpdInventory");
//		DAOSendAPI dao = DAOSendAPI.getInstance();
//		try {
//			URL url = new URL(prop.getProperty("sendUpdInventory."+brandCd+".url")); 
//			ArrayList<DTOSendInventory> list = dao.callSendUpdInventory(centerCd, brandCd);
//			if(list.size() < 1) {
//				logger.info("해당 물류센터에 재고가 없습니다.");
//				return;
//			}
//			JSONObject sendData = createInvenData(list);
//			System.out.println(sendData.toString());
//			logger.info(sendData.toString());
//			
//			String result = sendByPhp(url, sendData.toString());
//			logger.debug("Send updInventory Result: "+ result);	
//			
//			dao.updateUpdInvenState(centerCd, brandCd);
//		} catch (Exception e) {
//			loggerErr.error(e.getMessage());
//			loggerErr.error(e.toString());
//		}
//	}
	
	public String callSendUpdInventory(JSONObject msg) {
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("sendUpdInventory");
		String result = "";
		try {
			URL url = new URL(prop.getProperty("sendUpdInventory.6101.url"));
			result = sendByPhp(url, msg.toString());
			logger.debug("Send updInventory Result: "+ result);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
//	private JSONObject createInvenData(ArrayList<DTOSendInventory> list) throws Exception {
//		
//		JSONObject sendObj = new JSONObject();
//		JSONObject sendHeader = new JSONObject();
//		JSONArray sendBody = new JSONArray();
//		
//		sendHeader.put("bizUserId", list.get(0).getBizUserId());
//		sendHeader.put("bizUserPw", list.get(0).getBizUserPw());
//		sendHeader.put("callId", list.get(0).getCallId());
//		sendHeader.put("encType", list.get(0).getEncType());
//		
//		for(int i=0; i<list.size(); i++) {
//			DTOSendInventory dto = list.get(i);
//			JSONObject row = new JSONObject();
//			row.put("centerCd", dto.getCenterCd());
//			row.put("brandCd", dto.getBrandCd());
//			row.put("itemCd", dto.getItemCd());
//			row.put("itemState", dto.getItemState());
//			row.put("currQty", dto.getCurrQty());
//			row.put("availQty", dto.getAvailQty());
//			
//			sendBody.add(row);
//		}
//		
//		sendObj.put("header", sendHeader);
//		sendObj.put("body", sendBody);
//		
//		logger.debug(" create sendObj : "+sendObj.toString());
//		return sendObj;
//	}
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
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(os!=null) {
					os.close();
				}
				if(in!=null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return sb.toString();
	}
}
