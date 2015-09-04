package com.wmsapi.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.wmsapi.dao.DAORecvAPI;
import com.wmsapi.dto.DTORecvItem;
import com.wmsapi.utils.PropManager;
import com.wmsapi.utils.StringUtils;
import com.wmsapi.utils.ValidateUtils;

public class SVCRecvItem {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	public String[] callWmsProc(JSONArray body) {
		String[] svcResult = new String[4];
		DAORecvAPI dao = DAORecvAPI.getInstance();
		
		ArrayList<DTORecvItem> list = new ArrayList<DTORecvItem>();
		for(int i=0; i<body.size(); i++) {
			DTORecvItem dtoItem = new DTORecvItem();
			JSONObject jsonObj = body.getJSONObject(i);
			dtoItem.setBrandCd(jsonObj.getString("brandCd"));
			dtoItem.setItemCd(jsonObj.getString("itemCd"));
			dtoItem.setItemNm(jsonObj.getString("itemNm"));
			dtoItem.setItemBarCd(jsonObj.getString("itemBarCd"));
			dtoItem.setItemColor(jsonObj.getString("itemColor"));
			dtoItem.setItemSize(jsonObj.getString("itemSize"));
			dtoItem.setFactCd(jsonObj.getString("factCd"));
			dtoItem.setOpenDate(jsonObj.getString("openDate"));
			dtoItem.setItemBrandCd(jsonObj.getString("itemBrandCd"));
			dtoItem.setItemBrandNm(jsonObj.getString("itemBrandNm"));
			dtoItem.setSupplyPrice(jsonObj.getString("supplyPrice"));
			dtoItem.setSalePrice(jsonObj.getString("salePrice"));
			dtoItem.setItemStyle(jsonObj.getString("itemStyle"));
			dtoItem.setYears(jsonObj.getString("years"));
			dtoItem.setSeasons(jsonObj.getString("seasons"));
			dtoItem.setMansDiv(jsonObj.getString("mansDiv"));
			dtoItem.setCountryNm(jsonObj.getString("countryNm"));
			dtoItem.setMaterial1(jsonObj.getString("material1"));
			dtoItem.setMaterial2(jsonObj.getString("material2"));
			dtoItem.setDepartCd(jsonObj.getString("departCd"));
			dtoItem.setLineCd(jsonObj.getString("lineCd"));
			dtoItem.setClassCd(jsonObj.getString("classCd"));
			dtoItem.setFreeVal1(jsonObj.getString("freeVal1"));
			dtoItem.setFreeVal2(jsonObj.getString("freeVal2"));
			dtoItem.setFreeVal4(jsonObj.getString("freeVal4"));
			
			list.add(dtoItem);
		}
		
		svcResult = dao.callProcItem(list);
		return svcResult;
	}
	
	public String[] createFile(JSONArray body) {
		logger.info("createFile start!");
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
			JSONObject jsonObj = body.getJSONObject(i);
			String[] row = new String[jsonObj.size()];
			row[0] = jsonObj.getString("itemCd");
			row[1] = jsonObj.getString("brandCd");
			row[2] = jsonObj.getString("itemNm");
			row[3] = jsonObj.getString("itemBarCd");
			row[4] = jsonObj.getString("itemColor");
			row[5] = jsonObj.getString("itemSize");
			row[6] = jsonObj.getString("factCd");
			row[7] = jsonObj.getString("openDate");
			row[8] = jsonObj.getString("itemBrandCd");
			row[9] = jsonObj.getString("itemBrandNm");
			row[10] = jsonObj.getString("supplyPrice");
			row[11] = jsonObj.getString("salePrice");
			row[12] = jsonObj.getString("itemStyle");
			row[13] = jsonObj.getString("years");
			row[14] = jsonObj.getString("seasons");
			row[15] = jsonObj.getString("mansDiv");
			row[16] = jsonObj.getString("countryNm");
			row[17] = jsonObj.getString("material1");
			row[18] = jsonObj.getString("material2");
			row[19] = jsonObj.getString("departCd");
			row[20] = jsonObj.getString("lineCd");
			row[21] = jsonObj.getString("classCd");
			row[22] = jsonObj.getString("freeVal1");
			row[23] = jsonObj.getString("freeVal2");
			row[24] = jsonObj.getString("freeVal4");
			recvData.add(row);
			String[] validateResult = ValidateUtils.validateCheck("recvItem", row);
			if(!"SUCC".equals(validateResult[0])) {
				logger.info("NOT SUCC");
				isSucc=false;
				if(errorMsg.length() > 0) {
					errorMsg += ",";
				}
				errorMsg += "[brandCd: "+row[1]+", itemCd: "+row[0]+" is not validated. Error msg: "+validateResult[1]+"]";
				loggerErr.error(errorMsg);
			} else {
				isSucc=true;
				sb.append(StringUtils.replaceNull(jsonObj.getString("brandCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemNm"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemBarCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemColor"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemSize"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("factCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("openDate"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemBrandCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemBrandNm"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("supplyPrice"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("salePrice"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("itemStyle"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("years"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("seasons"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("mansDiv"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("countryNm"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("material1"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("material2"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("departCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("lineCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("classCd"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("freeVal1"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("freeVal2"))).append(",")
				.append(StringUtils.replaceNull(jsonObj.getString("freeVal4")));
				sb.append("$row$");
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
		
		BufferedWriter writer = null;
		try {
			if(sb.toString().length() > 0) {
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
					fileName = today+"_item_0";
				} else {
					while(fileName.length() < 1) {
						if((today+"_item_"+seq).equals(fileNames[i].replaceAll(".txt", ""))) {
							seq++;
						}
						
						if(i<(fileNames.length-1)) {
							i++;
						} else {
							fileName = (today+"_item_"+seq);
							break;
						}
					}
				}
				
				writer = new BufferedWriter(new FileWriter(prop.getProperty("filePath")+fileName+".txt"));
				writer.write(sb.toString());
				logger.info("recvItem file created!");
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
