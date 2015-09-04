package com.wmsapi.service;

import java.util.Properties;

import net.sf.json.JSONObject;

import com.wmsapi.dao.DAORecvAPI;
import com.wmsapi.dto.DTORecvChInfo;
import com.wmsapi.utils.PropManager;

public class SVCRecvChInfo {
	
	public String[] updateAcperInfo(String propName, JSONObject body) {
		String[] svcResult = new String[4];
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp(propName);
		
		DTORecvChInfo dtoChInfo = new DTORecvChInfo();
		dtoChInfo.setAcperNm(body.getString("acperNm"));
		dtoChInfo.setAcperTel(body.getString("acperTel"));
		dtoChInfo.setAcperZipCd1(body.getString("acperZipCd1"));
		dtoChInfo.setAcperZipCd2(body.getString("acperZipCd2"));
		dtoChInfo.setAcperBasic(body.getString("acperBasic"));
		dtoChInfo.setAcperDetail(body.getString("acperDetail"));
		dtoChInfo.setAcperHp(body.getString("acperHp"));
		dtoChInfo.setCenterCd(prop.getProperty(propName+"."+body.getString("centerCd")));
		dtoChInfo.setBrandCd(body.getString("brandCd"));
		dtoChInfo.setOrderDate(body.getString("orderDate"));
		dtoChInfo.setBrandNo(body.getString("brandNo"));
		
		DAORecvAPI dao = DAORecvAPI.getInstance();
		svcResult = dao.updateChInfo(dtoChInfo);
		return svcResult;
	}
}
