package com.wmsapi.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.wmsapi.dao.DAORecvAPI;
import com.wmsapi.dto.DTORecvOutbound;
import com.wmsapi.utils.PropManager;
import com.wmsapi.utils.StringUtils;
import com.wmsapi.utils.ValidateUtils;

public class SVCRecvOutbound {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	public String[] callWmsProc(JSONArray body) {
		String[] svcResult = new String[4];
		String[] respString = new String[2];
		DAORecvAPI dao = DAORecvAPI.getInstance();
		DTORecvOutbound dtoOutbound = null;
		String errorMsg = "";
		
		int uptCnt = 0;
		int totCnt = 0;
		
		for(int i=0; i<body.size(); i++) {
			JSONArray order = body.getJSONArray(i);
			ArrayList<DTORecvOutbound> list = new ArrayList<DTORecvOutbound>();
			for(int j=0; j<order.size(); j++) {
				JSONObject jsonObj = order.getJSONObject(j);
				dtoOutbound = new DTORecvOutbound();
				
				logger.info("jsonObj: "+jsonObj.toString());
				dtoOutbound.setCenterCd(jsonObj.getString("centerCd"));
				dtoOutbound.setBrandCd(jsonObj.getString("brandCd"));
				dtoOutbound.setOrderDate(jsonObj.getString("orderDate"));
				dtoOutbound.setInoutCd(jsonObj.getString("inoutCd"));
				dtoOutbound.setDeliveryCd(jsonObj.getString("deliveryCd"));
				dtoOutbound.setRealDeliveryCd(jsonObj.getString("realDeliveryCd"));
				dtoOutbound.setAcperNm(jsonObj.getString("acperNm"));
				dtoOutbound.setAcperCd(jsonObj.getString("acperCd"));
				dtoOutbound.setAcperTel(jsonObj.getString("acperTel"));
				dtoOutbound.setAcperHp(jsonObj.getString("acperHp"));
				dtoOutbound.setAcperZipCd1(jsonObj.getString("acperZipCd1"));
				dtoOutbound.setAcperZipCd2(jsonObj.getString("acperZipCd2"));
				dtoOutbound.setAcperBasic(jsonObj.getString("acperBasic"));
				dtoOutbound.setAcperDetail(jsonObj.getString("acperDetail"));
				dtoOutbound.setOrdNm(jsonObj.getString("ordNm"));
				dtoOutbound.setOrdCd(jsonObj.getString("ordCd"));
				dtoOutbound.setOrdTel(jsonObj.getString("ordTel"));
				dtoOutbound.setOrdHp(jsonObj.getString("ordHp"));
				dtoOutbound.setOrdZipCd1(jsonObj.getString("ordZipCd1"));
				dtoOutbound.setOrdZipCd2(jsonObj.getString("ordZipCd2"));
				dtoOutbound.setOrdBasic(jsonObj.getString("ordBasic"));
				dtoOutbound.setOrdDetail(jsonObj.getString("ordDetail"));
				dtoOutbound.setDeliveryMsg(jsonObj.getString("deliveryMsg"));
				dtoOutbound.setItemCd(jsonObj.getString("itemCd"));
				dtoOutbound.setItemState(jsonObj.getString("itemState"));
				dtoOutbound.setInputQty(jsonObj.getString("inputQty"));
				dtoOutbound.setOrderQty(jsonObj.getString("orderQty"));
				dtoOutbound.setBrandDate(jsonObj.getString("brandDate"));
				dtoOutbound.setBrandNo(jsonObj.getString("brandNo"));
				dtoOutbound.setBrandLineNo(jsonObj.getString("brandLineNo"));
				dtoOutbound.setIndentDate(jsonObj.getString("indentDate"));
				dtoOutbound.setIndentNo(jsonObj.getString("indentNo"));
				dtoOutbound.setFirstIndent(jsonObj.getString("firstIndentNo"));
				dtoOutbound.setSerialNo(jsonObj.getString("serialNo"));
				dtoOutbound.setFreeVal1(jsonObj.getString("freeVal1"));
				dtoOutbound.setReturnDiv(jsonObj.getString("returnDiv"));
				dtoOutbound.setFreeVal2(jsonObj.getString("freeVal2"));
				dtoOutbound.setFreeVal3D(jsonObj.getString("freeVal3D"));
				dtoOutbound.setFreeVal3(jsonObj.getString("freeVal3"));
				dtoOutbound.setFreeVal6(jsonObj.getString("freeVal6"));
				dtoOutbound.setFreeVal8(jsonObj.getString("freeVal8"));
				dtoOutbound.setDomesticGubun(jsonObj.getString("domesticGubun"));
				dtoOutbound.setDestCountry(jsonObj.getString("destCountry"));
				dtoOutbound.setDestCity(jsonObj.getString("destCity"));
				dtoOutbound.setDestState(jsonObj.getString("destState"));
				dtoOutbound.setItemGroup(jsonObj.getString("itemGroup"));
				dtoOutbound.setHscode(jsonObj.getString("hscode"));
				dtoOutbound.setCurrency(jsonObj.getString("currency"));
				dtoOutbound.setSupplyPrice(jsonObj.getString("supplyPrice"));
				dtoOutbound.setSupplyAmt(jsonObj.getString("supplyAmt"));
				dtoOutbound.setTotalAmt(jsonObj.getString("totalAmt"));
				dtoOutbound.setOutWeight(jsonObj.getString("outWeight"));
				dtoOutbound.setPickupYN(jsonObj.getString("pickupYN"));
				dtoOutbound.setFreeVal4D(jsonObj.getString("freeVal4D"));
				dtoOutbound.setFreeVal1D(jsonObj.getString("freeVal1D"));
				dtoOutbound.setFreeVal4(jsonObj.getString("freeVal4"));
				list.add(dtoOutbound);
			}
			
			respString = dao.callProcOutbound(list);
			if("S".equals(respString[0])) {
				uptCnt++;
			} else {
				if(errorMsg.length() > 0) {
					errorMsg += ",";
				}
				errorMsg += errorMsg += "[centerCd: "+dtoOutbound.getCenterCd()+", brandCd: "+dtoOutbound.getBrandCd()
							+", orderDate: "+dtoOutbound.getOrderDate()+", brandNo: "+dtoOutbound.getBrandNo()
							+" is not validated. Error msg: "+respString[1]+"]";
			}
			totCnt++;
		}
		
		if(errorMsg.length() < 1) {
			svcResult[0] = "SUCC";
			svcResult[1] = "";
			svcResult[2] = String.valueOf(totCnt);
			svcResult[3] = String.valueOf(uptCnt);
		} else {
			svcResult[0] = "ERROR_006";
			svcResult[1] = errorMsg;
			svcResult[2] = String.valueOf(totCnt);
			svcResult[3] = String.valueOf(uptCnt);
		}
		
		return svcResult;
	}
	
	public String[] createFile(JSONArray body) {
		logger.debug("createFile -- ");
		System.out.println("createFile -- ");
		String[] svcResult = new String[4];
		StringBuilder sb = new StringBuilder();
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("fileInfo");
		String errorMsg = "";
		
		
		boolean isSucc=false;
		int totCnt = 0;
		int updCnt = 0;
		
		ArrayList<String[]> recvData = new ArrayList<String[]>(); 
		for(int i=0; i<body.size(); i++) {
			JSONArray order = body.getJSONArray(i);
			for(int j=0; j<order.size(); j++) {
				JSONObject jsonObj = order.getJSONObject(j);
				String[] row = new String[jsonObj.size()];
				row[0] = jsonObj.getString("centerCd");
				row[1] = jsonObj.getString("brandCd");
				row[2] = jsonObj.getString("orderDate");
				row[3] = jsonObj.getString("inoutCd");
				row[4] = jsonObj.getString("deliveryCd");
				row[5] = jsonObj.getString("realDeliveryCd");
				row[6] = jsonObj.getString("acperNm");
				row[7] = jsonObj.getString("acperCd");
				row[8] = jsonObj.getString("acperTel");
				row[9] = jsonObj.getString("acperHp");
				row[10] = jsonObj.getString("acperZipCd1");
				row[11] = jsonObj.getString("acperZipCd2");
				row[12] = jsonObj.getString("acperBasic");
				row[13] = jsonObj.getString("acperDetail");
				row[14] = jsonObj.getString("ordNm");
				row[15] = jsonObj.getString("ordCd");
				row[16] = jsonObj.getString("ordTel");
				row[17] = jsonObj.getString("ordHp");
				row[18] = jsonObj.getString("ordZipCd1");
				row[19] = jsonObj.getString("ordZipCd2");
				row[20] = jsonObj.getString("ordBasic");
				row[21] = jsonObj.getString("ordDetail");
				row[22] = jsonObj.getString("deliveryMsg");
				row[23] = jsonObj.getString("itemCd");
				row[24] = jsonObj.getString("itemState");
				row[25] = jsonObj.getString("inputQty");
				row[26] = jsonObj.getString("orderQty");
				row[27] = jsonObj.getString("brandDate");
				row[28] = jsonObj.getString("brandNo");
				row[29] = jsonObj.getString("brandLineNo");
				row[30] = jsonObj.getString("indentDate");
				row[31] = jsonObj.getString("indentNo");
				row[32] = jsonObj.getString("firstIndentNo");
				row[33] = jsonObj.getString("serialNo");
				row[34] = jsonObj.getString("freeVal1");
				row[35] = jsonObj.getString("returnDiv");
				row[36] = jsonObj.getString("freeVal2");
				row[37] = jsonObj.getString("freeVal4");
				row[38] = jsonObj.getString("freeVal3D");
				row[39] = jsonObj.getString("freeVal3");
				row[40] = jsonObj.getString("freeVal6");
				row[41] = jsonObj.getString("freeVal8");
				row[42] = jsonObj.getString("domesticGubun");
				row[43] = jsonObj.getString("destCountry");
				row[44] = jsonObj.getString("destCity");
				row[45] = jsonObj.getString("destState");
				row[46] = jsonObj.getString("itemGroup");
				row[47] = jsonObj.getString("hscode");
				row[48] = jsonObj.getString("currency");
				row[49] = jsonObj.getString("supplyPrice");
				row[50] = jsonObj.getString("supplyAmt");
				row[51] = jsonObj.getString("totalAmt");
				row[52] = jsonObj.getString("outWeight");
				row[53] = jsonObj.getString("pickupYN");
				row[54] = jsonObj.getString("freeVal4D");
				row[55] = jsonObj.getString("freeVal1D");
				recvData.add(row);
				System.out.println("validateUtil");
				String[] validateResult = ValidateUtils.validateCheck("recvOutbound", row);
				if(!"SUCC".equals(validateResult[0])) {
					System.out.println("NOT SUCC");
					isSucc=false;
					if(errorMsg.length() > 0) {
						errorMsg += ",";
					}
					errorMsg += "[centerCd: "+row[0]+", brandCd: "+row[1]+", orderDate: "+row[2]
				             +", brandNo: "+row[28]+" is not validated. Error msg: "+validateResult[1]+"]";
					
					System.out.println("errorMsg: "+errorMsg);
					loggerErr.error(errorMsg);
					break;
				} else {
					isSucc=true;
					sb.append(StringUtils.replaceNull(jsonObj.getString("centerCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("brandCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("orderDate"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("inoutCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("deliveryCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("realDeliveryCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperNm"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperTel"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperHp"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperZipCd1"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperZipCd2"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperBasic"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("acperDetail"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordNm"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordTel"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordHp"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordZipCd1"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordZipCd2"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordBasic"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("ordDetail"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("deliveryMsg"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("itemCd"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("itemState"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("inputQty"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("orderQty"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("brandDate"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("brandNo"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("brandLineNo"))).append(",")			
					.append(StringUtils.replaceNull(jsonObj.getString("indentDate"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("indentNo"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("firstIndentNo"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("serialNo"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal1"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("returnDiv"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal2"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal4"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal3D"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal3"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal6"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal8"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("domesticGubun"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("destCountry"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("destCity"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("destState"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("itemGroup"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("hscode"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("currency"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("supplyPrice"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("supplyAmt"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("totalAmt"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("outWeight"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("pickupYN"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal4D"))).append(",")
					.append(StringUtils.replaceNull(jsonObj.getString("freeVal1D")));
					sb.append("$row$");
				}
			}
			
			if(isSucc) {
				updCnt++;
			}
			totCnt++;
			if(sb.length() > 0) {
				System.out.println(sb.toString().substring(sb.toString().length()-5, sb.toString().length()));
				if(sb.toString().substring(sb.toString().length()-5, sb.toString().length()).equals("$row$")) {
					sb.append("$otherBrandNo$");
				}
			}
		}
		
		logger.debug(prop.getProperty("filePath"));
		System.out.println(prop.getProperty("filePath"));
		BufferedWriter writer = null;
		try {
			
			if(sb.toString().length() > 0) {
				Calendar cal = Calendar.getInstance();
				String month = "";
				String day = "";
				logger.debug("a---175");
				if(cal.get(Calendar.MONTH)+1 < 10) {
					month = "0"+(cal.get(Calendar.MONTH)+1);
				} else {
					month = String.valueOf(cal.get(Calendar.MONTH)+1);
				}
				logger.debug("a---181");
				if(cal.get(Calendar.DAY_OF_MONTH) < 10) {
					day = "0"+(cal.get(Calendar.DAY_OF_MONTH));
				} else {
					day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
				}		
				String today = String.valueOf(cal.get(Calendar.YEAR))+month+day;
				logger.debug("a---188");
				File f = new File(prop.getProperty("filePath"));
				String[] fileNames = f.list();
				String fileName = "";
				int seq=0;
				int i=0;
				logger.debug("a---194");
				
				Arrays.sort(fileNames);
				if(fileNames.length<1) {
					fileName = today+"_outbound_0";
				} else {
					while(fileName.length() < 1) {
						if((today+"_outbound_"+seq).equals(fileNames[i].replaceAll(".txt", ""))) {
							seq++;
						}
						
						if(i<(fileNames.length-1)) {
							i++;
						} else {
							fileName = (today+"_outbound_"+seq);
							break;
						}
					}
				}
				logger.debug("write begin");
				writer = new BufferedWriter(new FileWriter(prop.getProperty("filePath")+fileName+".txt"));
				logger.debug("write after0");
				writer.write(sb.toString());
				logger.debug("write after1");
				System.out.println("颇老积己");
				logger.debug("颇老积己");
			}
			if(errorMsg.length() < 1) {
				svcResult[0] = "SUCC";
				svcResult[1] = "";
			} else {
				svcResult[0] = "ERR_006";
				svcResult[1] = errorMsg;
			}
			svcResult[2] = String.valueOf(totCnt);
			svcResult[3] = String.valueOf(updCnt);
		} catch (IOException e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
			svcResult[0] = "ERR_004";
			svcResult[1] = "";
			svcResult[2] = String.valueOf(totCnt);
			svcResult[3] = String.valueOf(0);
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
			svcResult[0] = "ERR_004";
			svcResult[1] = "";
			svcResult[2] = String.valueOf(totCnt);
			svcResult[3] = String.valueOf(0);
		} finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (Exception e) {
					loggerErr.error(e.getMessage());
					svcResult[0] = "ERR_004";
					svcResult[1] = "";
					svcResult[2] = String.valueOf(totCnt);
					svcResult[3] = String.valueOf(0);
				}
			}
		}
		
		return svcResult;
	}
}
