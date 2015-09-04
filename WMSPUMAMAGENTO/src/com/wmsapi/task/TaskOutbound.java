package com.wmsapi.task;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.wmsapi.dao.DAOSendAPI;
import com.wmsapi.dto.DTOSendOutbound;
import com.wmsapi.utils.PropManager;

public class TaskOutbound extends TimerTask {

	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	public void run() {
		start();
	}
	private void start() {
		logger.info("----TaskOutbound Start");
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("sendOutbound");
		DAOSendAPI dao = DAOSendAPI.getInstance();
		
		try {
			URL url = null;
			String[] brandCdList = prop.getProperty("sendOutbound.list").split(",");
			for(int i=0; i<brandCdList.length; i++) {
				ArrayList<DTOSendOutbound> list = dao.callSendOutbound(brandCdList[i]);
				url = new URL(prop.getProperty("sendOutbound."+brandCdList[i]+".url")); 
				if(list.size() < 1) {
					logger.info("출고확정 건이 없습니다.");
					continue;
				}
				
				JSONObject sendData = createOutboundData(list);
				String result = sendByPhp(url, sendData.toString());
//					if("Y".equals(result)) {
				dao.updateSucc(brandCdList[i]);
//					}
				logger.debug("Send Result: "+ result);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e.toString());
			
			loggerErr.error(e.getMessage());
			loggerErr.error(e.toString());
		}
		logger.info("----TaskOutbound End");
	}
private JSONObject createOutboundData(ArrayList<DTOSendOutbound> list) throws Exception {
		
		JSONObject sendObj = new JSONObject();
		JSONObject sendHeader = new JSONObject();
		JSONArray sendBody = new JSONArray();
		JSONArray sendOrder = null;
		
		sendHeader.put("bizUserId", list.get(0).getBizUserId());
		sendHeader.put("bizUserPw", list.get(0).getBizUserPw());
		sendHeader.put("callId", list.get(0).getCallId());
		sendHeader.put("encType", list.get(0).getEncType());
		
		String currBrandNo = "";
		for(int i=0; i<list.size(); i++) {
			DTOSendOutbound dto = list.get(i);
			JSONObject row = new JSONObject();
			row.put("centerCd", dto.getCenterCd());
			row.put("brandCd", dto.getBrandCd());
			row.put("outboundDate", dto.getOutboundDate());
			row.put("outboundNo", dto.getOutboundNo());
			row.put("inoutCd", dto.getInoutCd());
			row.put("deliveryCd", dto.getDeliveryCd());
			row.put("realDeliveryCd", dto.getRealDeliveryCd());
			row.put("lineNo", dto.getLineNo());
			row.put("itemCd", dto.getItemCd());
			row.put("itemState", dto.getItemState());
			row.put("orderQty", dto.getOrderQty());
			row.put("entryQty", dto.getEntryQty());
			row.put("confirmQty", dto.getConfirmQty());
			row.put("brandDate", dto.getBrandDate());
			row.put("brandNo", dto.getBrandNo());
			row.put("brandLineNo", dto.getBrandLineNo());
			row.put("expressCd", dto.getExpressCd());
			row.put("expressNo", dto.getExpressNo());
			row.put("serialNo", dto.getSerialNo());
			row.put("returnDiv", dto.getReturnDiv());
			row.put("freeVal5D", dto.getFreeVal5D());
			row.put("returnCost", dto.getReturnCost());
			row.put("frecollect", dto.getFrecollect());
			row.put("freeVal4D", dto.getFreeVal4D());
			if(currBrandNo.equals(dto.getBrandNo())) {
				sendOrder.add(row);
				sendBody.remove(sendBody.size()-1);
				sendBody.add(sendOrder);
			} else {
				sendOrder = new JSONArray();
				sendOrder.add(row);
				currBrandNo = dto.getBrandNo();
				sendBody.add(sendOrder);
			}
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
	
//	private String sendByPhp() {
//		try {
//			URL url = new URL( "http://google.com/" );
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
//			    InputStream is = conn.getInputStream();
//			    // do something with the data here
//			}else{
//			    InputStream err = conn.getErrorStream();
//			    // err may have useful information.. but could be null see javadocs for more information
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private String send(String url, List<NameValuePair> params) throws Exception {
		
		System.out.println("send url : "+url);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
//		httpClient.getParams().setParameter("http.socket.timeout", new Integer(1000));
//		httpClient.getParams().setParameter("http.protocol.content-charset", "KSC5601");
		httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		
		HttpPost request = new HttpPost(url);
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 30000);
		HttpResponse response = null;
		String result = "";
		try {
			
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			
			InputStream respIS = entity.getContent();
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1024];
			int line = -1;
			while((line=respIS.read(buffer))!= -1) {
				arrayOutputStream.write(buffer, 0, line);
			}
			arrayOutputStream.flush();
			arrayOutputStream.close();
			
			byte[] respByte = arrayOutputStream.toByteArray();
			result = new String(respByte, 0, respByte.length); 
		} catch(Exception e) {
			logger.error(e.getMessage());
			logger.error(e.toString());
			
			loggerErr.error(e.getMessage());
			loggerErr.error(e.toString());
		}
		return result;
	}
}
