package com.wmsapi.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.wmsapi.dao.DAORecvAPI;
import com.wmsapi.dto.DTOGetAvailQty;
import com.wmsapi.service.SVCGetAvailQty;
import com.wmsapi.service.SVCRecvCancel;
import com.wmsapi.service.SVCRecvChInfo;
import com.wmsapi.service.SVCRecvItem;
import com.wmsapi.service.SVCRecvOutbound;
import com.wmsapi.service.SVCSendUpdInventory;

public class RecvAPIServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("recvApi start!!");
		try {
			String resp = "";
			String[] svcResult = new String[4]; 
			request.setCharacterEncoding("UTF-8");
			
			logger.debug("method:"+request.getMethod());
			logger.debug("contents Type:"+request.getContentType());
			
			String msg = "";
			String callId = "";
			if(request.getParameter("msg")!=null) {
				msg = request.getParameter("msg");
			}
			
			if(msg.length() < 1) {
				logger.debug(resp);
				resp = createRespMsg("ERR_05", "Post Param msg가 존재하지 않습니다.", "0", "0", "");
			} else {
				logger.debug(request.getParameter("msg"));
				System.out.println(request.getParameter("msg"));
				
				JSONObject recvJson = JSONObject.fromObject(request.getParameter("msg"));
				logger.debug("RecvAPIServelt--51");
				JSONObject recvHeader = recvJson.getJSONObject("header");
				JSONArray recvBody = null;
				JSONObject recvObjBody = null;
				
				boolean hasError = false; 
				callId = recvHeader.getString("callId");
				String userId = recvHeader.getString("bizUserId");
				String userPw = recvHeader.getString("bizUserPw");
				if(!isValidUserId(userId, userPw)) { // API 유효계 ID 확인
					resp = "ERR_001";
					hasError = true;
				}
				
				if("getAvailQty".equals(callId) 
						|| "recvCancel".equals(callId) 
						|| "recvChangeInfo".equals(callId)) {
					recvObjBody = recvJson.getJSONObject("body");
				} else {
					recvBody = recvJson.getJSONArray("body");
				}
				
				logger.debug("respString1: "+resp);
				ArrayList<DTOGetAvailQty> list = new ArrayList<DTOGetAvailQty>();
				if(!hasError) {
					if(callId.equals("recvOutbound")) {
						SVCRecvOutbound svcRo = new SVCRecvOutbound();
						svcResult = svcRo.callWmsProc(recvBody);
					} else if(callId.equals("recvItem")) {
						SVCRecvItem svcRi = new SVCRecvItem();
						svcResult = svcRi.callWmsProc(recvBody);
					} else if(callId.equals("recvCancel")) {						
						SVCRecvCancel svcRc = new SVCRecvCancel();
						svcResult = svcRc.callProcCancel(recvObjBody);
					} else if(callId.equals("getAvailQty")) {
						String itemState = "";
						if(recvObjBody.getString("itemState") != null) {
							itemState = recvObjBody.getString("itemState");
						}
						
						if(itemState.length() < 1) {
							DTOGetAvailQty dtoA = new DTOGetAvailQty();
							dtoA.setItemState("A");
							list.add(dtoA);
							
							DTOGetAvailQty dtoC = new DTOGetAvailQty();
							dtoC.setItemState("C");
							list.add(dtoC);
						} else {
							DTOGetAvailQty dto = new DTOGetAvailQty();
							dto.setItemState(itemState);
							list.add(dto);
						}
						SVCGetAvailQty svcGa = new SVCGetAvailQty();
						resp = svcGa.callWmsProc(recvObjBody, list);
					} else if(callId.equals("recvChangeInfo")){
						System.out.println("recvChangeInfo start!");
						SVCRecvChInfo svcChInfo = new SVCRecvChInfo();
						svcResult = svcChInfo.updateAcperInfo(callId, recvObjBody);
					} else if(callId.equals("sendUpdInventory")) {
						logger.info("sendUpdInventory received");
						SVCSendUpdInventory svcSendUpdInven = new SVCSendUpdInventory();
						String result = svcSendUpdInven.callSendUpdInventory(recvJson);
						
						svcResult = new String[4];
						svcResult[0] = "SUCC";
						svcResult[1] = result;
						svcResult[2] = "-";
						svcResult[3] = "-";
						
					} else {
						resp = "ERR_003";
					}
				}
				
				logger.debug("resp:"+resp);
				if("getAvailQty".equals(callId)) {
					resp = createRespMsgByAvailQty(resp, list);
				} else if("recvCancel".equals(callId)) { 
					resp = createRespMsgByCancel(svcResult[0], svcResult[1]);
				} else {
					resp = createRespMsg(svcResult[0], svcResult[1], svcResult[2], svcResult[3], request.getParameter("msg"));
				}
			}
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-www-form-urlencoded");
			logger.debug(resp);
			response.getWriter().print(resp);
		} catch (IOException e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		doPost(request, response);
	}
	
	
	public boolean isValidUserId(String userId, String userPw) {
		DAORecvAPI dao = DAORecvAPI.getInstance();
		boolean result = dao.isVaildId(userId, userPw);
		
		return result;
	}
	
	public String createRespMsg(String respCd, String respMsg, String totCnt, String updCnt, String recv) {
		JSONObject respJson = new JSONObject();
		JSONObject respHeader = new JSONObject();
		JSONObject respBody = new JSONObject();
		
		if("SUCC".equals(respCd)) {
			respMsg = "The process is complete.";
			respHeader.put("result", "success");
		} else if("ERR_001".equals(respCd)) {
			respMsg = "The userId is not validate.";
			respHeader.put("result", "fail");
		} else if("ERR_002".equals(respCd)) {
			respMsg = "Decryption error.";
			respHeader.put("result", "fail");
		} else if("ERR_003".equals(respCd)) {
			respMsg = "The callId is not validate.";
			respHeader.put("result", "fail");
		} else if("ERR_004".equals(respCd)) {
			respMsg = "Unexpected Exception is occurred.";
			respHeader.put("result", "fail");
		} else {
			respHeader.put("result", "fail");
		}
		
		respHeader.put("code", respCd);
		respHeader.put("message", respMsg);
		respHeader.put("recvData", recv);
		
		respBody.put("totCnt", totCnt);
		respBody.put("updCnt", updCnt);	
		respJson.put("header", respHeader);
		respJson.put("body", respBody);
		
		return respJson.toString();
	}
	
	public String createRespMsgByAvailQty(String respCd, ArrayList<DTOGetAvailQty> list) {
		JSONObject respJson = new JSONObject();
		JSONObject respHeader = new JSONObject();
		JSONArray respBody = new JSONArray();
		
		String respMsg = "";
		if("SUCC".equals(respCd)) {
			respMsg = "The process is complete.";
			respHeader.put("result", "success");
		} else if("ERR_001".equals(respCd)) {
			respMsg = "The userId is not validate.";
			respHeader.put("result", "fail");
		} else if("ERR_002".equals(respCd)) {
			respMsg = "Decryption error.";
			respHeader.put("result", "fail");
		} else if("ERR_003".equals(respCd)) {
			respMsg = "The callId is not validate.";
			respHeader.put("result", "fail");
		} else if("ERR_004".equals(respCd)) {
			respMsg = "Unexpected Exception is occurred.";
			respHeader.put("result", "fail");
		} else if("ERR_004".equals(respCd)) {
			respMsg = "Unexpected Exception is occurred.";
			respHeader.put("result", "fail");
		} else {
		}
		
		respHeader.put("code", respCd);
		respHeader.put("message", respMsg);
		
		for(int i=0; i<list.size(); i++) {
			JSONObject respItem = new JSONObject();
			DTOGetAvailQty dto = list.get(i);
			
			respItem.put("centerCd",dto.getCenterCd());
			respItem.put("brandCd",dto.getBrandCd());
			respItem.put("itemCd",dto.getItemCd());
			respItem.put("itemState",dto.getItemState());
			respItem.put("currQty",dto.getCurrQty());
			respItem.put("availQty",dto.getAvailQty());
			
			respBody.add(respItem);
		}
		respJson.put("header", respHeader);
		respJson.put("body", respBody);
		
		return respJson.toString();
	}
	
	public String createRespMsgByCancel(String respCd, String respMsg) {
		JSONObject respJson = new JSONObject();
		JSONObject respHeader = new JSONObject();
		JSONObject respBody = new JSONObject();
		int totCnt = 0;
		int updCnt = 0;
		if("SUCC".equals(respCd)) {
			respMsg = "The process is complete.";
			respHeader.put("result", "success");
			totCnt = 1;
			updCnt = 1;
		} else if("ERR_001".equals(respCd)) {
			respMsg = "The userId is not validate.";
			respHeader.put("result", "fail");
		} else if("ERR_002".equals(respCd)) {
			respMsg = "Decryption error.";
			respHeader.put("result", "fail");
		} else if("ERR_003".equals(respCd)) {
			respMsg = "The callId is not validate.";
			respHeader.put("result", "fail");
		} else if("ERR_004".equals(respCd)) {
			respMsg = "Unexpected Exception is occurred.";
			respHeader.put("result", "fail");
		} else {
			respMsg = "Duplicated BrandNo Error";
			respHeader.put("result", "fail");
		}
		
		respHeader.put("code", respCd);
		respHeader.put("message", respMsg);
		respBody.put("totCnt", totCnt);
		respBody.put("updCnt", updCnt);
		respJson.put("header", respHeader);
		respJson.put("body", respBody);
		
		return respJson.toString();
	}
}
