package com.wmsapi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.wmsapi.dto.DTOSendExpressTrace;
import com.wmsapi.dto.DTOSendInventory;
import com.wmsapi.dto.DTOSendOutbound;
import com.wmsapi.utils.ConnManager;

public class DAOSendAPI {

	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
	private static DAOSendAPI daoSendApi = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private DAOSendAPI() {}
	public static DAOSendAPI getInstance() {
		if(daoSendApi == null) {
			daoSendApi = new DAOSendAPI();
		}
		return daoSendApi;
	} 
	
	public ArrayList<DTOSendOutbound> callSendOutbound(String brandCd) {
		ArrayList<DTOSendOutbound> list = new ArrayList<DTOSendOutbound>();
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT M4.USER_ID bizUserId, USER_PW bizUserPw, 'sendOutbound' callId, 'UTF8' encType ")
		  .append(" , M1.CENTER_CD centerCd, M1.BRAND_CD brandCd, TO_CHAR(M1.REG_DATETIME,'YYYYMMDDHH24MISS') outboundDate, M1.OUTBOUND_NO outboundNo ") 
		  .append(" , M1.INOUT_CD inoutCd, M1.DELIVERY_CD deliveryCd ")
		  .append(" , M1.REAL_DELIVERY_CD realDeliveryCd, M2.LINE_NO lineNo, M2.ITEM_CD itemCd ") 
		  .append(" , M2.ITEM_STATE itemState, M2.ORDER_QTY orderQty, M2.ENTRY_QTY entryQty ")
		  .append(" , M2.CONFIRM_QTY confirmQty, TO_CHAR(M2.BRAND_DATE,'YYYYMMDD') brandDate, M2.BRAND_NO brandNo ")
		  .append(" , M2.BRAND_LINE_NO brandLineNo, M3.EXPRESS_CD expressCd ")
		  .append(" , M3.EXPRESS_NO expressNo, M2.SERIAL_NO serialNo, M2.RETURN_DIV returnDiv, M2.FREE_VAL5 freeVal5D ")
		  .append(" , M1.RETURNCOST returnCost, M1.FRECOLLECT frecollect, M2.FREE_VAL4 freeVal4D  ")
		  .append(" FROM LO020NM  M1, LO020ND M2 , LO020NP M3, CMAPIUSER M4 ") 
		  .append(" WHERE M1.CENTER_CD = M2.CENTER_CD  ")
		  .append(" AND M1.BRAND_CD = M2.BRAND_CD ")
		  .append(" AND M1.OUTBOUND_DATE = M2.OUTBOUND_DATE ") 
		  .append(" AND M1.OUTBOUND_NO = M2.OUTBOUND_NO ")
		  .append(" AND M2.CENTER_CD = M3.CENTER_CD ")
		  .append(" AND M2.BRAND_CD = M3.BRAND_CD  ")
		  .append(" AND M2.OUTBOUND_DATE = M3.OUTBOUND_DATE ") 
		  .append(" AND M2.OUTBOUND_NO = M3.OUTBOUND_NO  ")
		  .append(" AND M2.LINE_NO = M3.LINE_NO ")
		  .append(" and M1.BRAND_CD = M4.BRAND_CD ")
		  .append(" AND M1.CENTER_CD = 'A1' ")
		  .append(" AND M1.OUTBOUND_STATE = '50' ") 
		  .append(" AND (M1.SEND_STATE <> '60' OR M1.SEND_STATE IS NULL) ")
		  .append(" AND M1.BRAND_CD = ? ")
		  .append(" AND M1.DELIVERY_CD = 'Z999' ") 
		  .append(" AND M1.OUTBOUND_DATE <= TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD') ")
		  .append(" AND M1.INOUT_CD = 'D10'")
		  .append(" UNION ALL SELECT M4.USER_ID bizUserId, USER_PW bizUserPw, 'sendOutbound' callId, 'UTF8' encType ")
		  .append(" , M1.CENTER_CD centerCd, M1.BRAND_CD brandCd, TO_CHAR(M1.REG_DATETIME,'YYYYMMDDHH24MISS') outboundDate, M1.OUTBOUND_NO outboundNo ") 
		  .append(" , M1.INOUT_CD inoutCd, M1.DELIVERY_CD deliveryCd ")
		  .append(" , M1.REAL_DELIVERY_CD realDeliveryCd, M2.LINE_NO lineNo, M2.ITEM_CD itemCd ") 
		  .append(" , M2.ITEM_STATE itemState, M2.ORDER_QTY orderQty, M2.ENTRY_QTY entryQty ")
		  .append(" , M2.CONFIRM_QTY confirmQty, TO_CHAR(M2.BRAND_DATE,'YYYYMMDD') brandDate, M2.BRAND_NO brandNo ")
		  .append(" , M2.BRAND_LINE_NO brandLineNo, '09' expressCd ")
		  .append(" , M1.EXPRESS_NO expressNo, M2.SERIAL_NO serialNo, M2.RETURN_DIV returnDiv, M2.FREE_VAL5 freeVal5D ")
		  .append(" , M1.RETURNCOST returnCost, M1.FRECOLLECT frecollect, M2.FREE_VAL4 freeVal4D  ")
		  .append(" FROM LO020NM  M1, LO020ND M2 ,CMAPIUSER M4 ") 
		  .append(" WHERE M1.CENTER_CD = M2.CENTER_CD  ")
		  .append(" AND M1.BRAND_CD = M2.BRAND_CD ")
		  .append(" AND M1.OUTBOUND_DATE = M2.OUTBOUND_DATE ") 
		  .append(" AND M1.OUTBOUND_NO = M2.OUTBOUND_NO ")
		  .append(" AND M1.BRAND_CD = M4.BRAND_CD ")
		  .append(" AND M1.CENTER_CD = 'A1' ")
		  .append(" AND M1.OUTBOUND_STATE = '60' ") 
		  .append(" AND (M1.SEND_STATE <> '60' OR M1.SEND_STATE IS NULL) ")
		  .append(" AND M1.BRAND_CD = ? ")
		  .append(" AND M1.DELIVERY_CD = 'Z999' ") 
		  .append(" AND M1.OUTBOUND_DATE <= TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD') ") 
		  .append(" AND M1.INOUT_CD = 'E30'")
		  .append(" ORDER BY CENTERCD, BRANDCD, OUTBOUNDDATE, OUTBOUNDNO, LINENO ");
		  logger.debug(sb.toString());
		  
		try {
			conn = connMgr.getConnection();
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, brandCd);
			pstmt.setString(2, brandCd);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				DTOSendOutbound dtoSendOutbound = new DTOSendOutbound();
				
				dtoSendOutbound.setBizUserId(rs.getString("bizUserId"));
				dtoSendOutbound.setBizUserPw(rs.getString("bizUserPw"));
				dtoSendOutbound.setCallId(rs.getString("callId"));
				dtoSendOutbound.setEncType(rs.getString("encType"));
				dtoSendOutbound.setCenterCd(rs.getString("centerCd"));
				dtoSendOutbound.setBrandCd(rs.getString("brandCd"));
				dtoSendOutbound.setOutboundDate(rs.getString("outboundDate"));
				dtoSendOutbound.setOutboundNo(rs.getString("outboundNo"));
				dtoSendOutbound.setInoutCd(rs.getString("inoutCd"));
				dtoSendOutbound.setDeliveryCd(rs.getString("deliveryCd"));
				dtoSendOutbound.setRealDeliveryCd(rs.getString("realDeliveryCd"));
				dtoSendOutbound.setLineNo(rs.getString("lineNo"));
				dtoSendOutbound.setItemCd(rs.getString("itemCd"));
				dtoSendOutbound.setItemState(rs.getString("itemState"));
				dtoSendOutbound.setOrderQty(rs.getString("orderQty"));
				dtoSendOutbound.setEntryQty(rs.getString("entryQty"));
				dtoSendOutbound.setConfirmQty(rs.getString("confirmQty"));
				dtoSendOutbound.setBrandDate(rs.getString("brandDate"));
				dtoSendOutbound.setBrandNo(rs.getString("brandNo"));
				dtoSendOutbound.setBrandLineNo(rs.getString("brandLineNo"));
				dtoSendOutbound.setExpressCd(rs.getString("expressCd"));
				dtoSendOutbound.setExpressNo(rs.getString("expressNo"));
				dtoSendOutbound.setSerialNo(rs.getString("serialNo"));
				dtoSendOutbound.setReturnDiv(rs.getString("returnDiv"));
				dtoSendOutbound.setFreeVal5D(rs.getString("freeVal5D"));
				dtoSendOutbound.setReturnCost(rs.getString("returnCost"));
				dtoSendOutbound.setFrecollect(rs.getString("frecollect"));
				dtoSendOutbound.setFreeVal4D(rs.getString("freeVal4D"));
				
				list.add(dtoSendOutbound);
			}
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return list;
	}
	
	public ArrayList<DTOSendInventory> callSendInventory(String centerCd, String brandCd) {
		
		ArrayList<DTOSendInventory> list = new ArrayList<DTOSendInventory>();
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		ResultSet rsInven = null;
		String[] itemState = {"A","C"}; 
		
		try {
			conn = connMgr.getConnection();
			for(int i=0; i<itemState.length; i++) {
				sb.append(" SELECT L4.USER_ID userId, L4.USER_PW userPw, 'sendInventory' callId, 'UTF8' encType, L1.ITEM_CD itemCd, L1.ITEM_STATE itemState, L1.STOCK_QTY currQty,")
				.append(" ( L1.STOCK_QTY -(NVL(L2.OUT_WAIT_QTY,0)+NVL(L3.RTN_OUT_QTY,0))) availQty ")
				.append(" FROM ( ")
				.append(" SELECT A.ITEM_CD, NVL(B.ITEM_STATE, '").append(itemState[i]).append("') ITEM_STATE, NVL(SUM(STOCK_QTY) ,0) AS STOCK_QTY ")
				.append(" FROM CMITEM A, LS010NM B ")
				.append(" WHERE A.BRAND_CD = B.BRAND_CD(+) ")
				.append(" AND A.ITEM_CD = B.ITEM_CD(+) ")
				.append(" AND '").append(itemState[i]).append("' = B.ITEM_STATE(+) ")
				.append(" AND A.BRAND_CD = '").append(brandCd).append("' ")
				.append(" AND   A.ITEM_CD IN  ")
				.append("      (  SELECT B.ITEM_CD ")
				.append("        FROM LI020NM A, LI020ND B ")
				.append("        WHERE     A.BRAND_CD = '").append(brandCd).append("' ")
				.append("         AND A.CENTER_CD = '").append(centerCd).append("' ")
				.append("         AND A.BRAND_CD = B.BRAND_CD ")
				.append("         AND A.CENTER_CD = B.CENTER_CD ")
				.append("         AND A.INBOUND_DATE = B.INBOUND_DATE ")
				.append("         AND A.INBOUND_NO = B.INBOUND_NO ")
				.append("         AND A.INBOUND_STATE = '60' ")
				.append("         GROUP BY B.ITEM_CD) ")
				.append(" GROUP BY A.ITEM_CD, B.ITEM_STATE ")
				.append(" ) L1, ")
				.append(" ( ")
				.append(" SELECT ITEM_CD, ITEM_STATE, NVL(SUM(L1.CONFIRM_QTY), 0) AS OUT_WAIT_QTY ")
				.append(" FROM ( ")
				.append(" SELECT ")
				.append(" ITEM_CD, '").append(itemState[i]).append("' ITEM_STATE, SUM(M2.CONFIRM_QTY)    AS CONFIRM_QTY ")
				.append(" FROM LO020NM M1 ")
				.append(" JOIN LO020ND M2 ON M2.CENTER_CD      = M1.CENTER_CD ")
				.append(" AND M2.BRAND_CD       = M1.BRAND_CD ")
				.append(" AND M2.OUTBOUND_DATE  = M1.OUTBOUND_DATE ")
				.append(" AND M2.OUTBOUND_NO    = M1.OUTBOUND_NO ")
				.append(" JOIN CMCODE  C1 ON C1.CODE_CD        = M1.INOUT_CD ")
				.append(" AND C1.CODE_GRP       = 'LDIV03' ")
				.append(" AND C1.SUB_CD        IN ('D1' ,'D2')        ")        
				.append(" WHERE M1.CENTER_CD       = '").append(centerCd).append("' ")
				.append(" AND M1.BRAND_CD        = '").append(brandCd).append("' ")
				.append(" AND M1.OUTBOUND_DATE   > ADD_MONTHS(TRUNC(SYSDATE, 'DD'), -1) ")
				.append(" AND M2.OUTBOUND_STATE IN ('20' ,'30' ,'40') ")
				.append(" AND M2.ITEM_STATE = '").append(itemState[i]).append("'")
				.append(" GROUP BY ITEM_CD, '").append(itemState[i]).append("' ")
				.append(" UNION ALL ")
				.append(" SELECT ITEM_CD, '").append(itemState[i]).append("' ITEM_STATE, SUM(M2.CONFIRM_QTY)    AS CONFIRM_QTY ")
				.append(" FROM LC010NM        M1 ")
				.append(" JOIN LC010ND   M2 ON M2.CENTER_CD      = M1.CENTER_CD ")
				.append(" AND M2.BRAND_CD       = M1.BRAND_CD ")
				.append(" AND M2.ETC_DATE       = M1.ETC_DATE ")
				.append(" AND M2.ETC_NO         = M1.ETC_NO ")
				.append(" WHERE M1.CENTER_CD        = '").append(centerCd).append("' ")
				.append(" AND M1.BRAND_CD         = '").append(brandCd).append("' ")
				.append(" AND M1.ETC_DATE         > ADD_MONTHS(TRUNC(SYSDATE, 'DD'), -1) ")
				.append(" AND M1.CONFIRM_YN    = 'N' ")
				.append(" AND SUBSTR(M1.INOUT_CD ,1,1)      = 'D' ")
				.append(" AND M2.ITEM_STATE = '").append(itemState[i]).append("'")
				.append(" GROUP BY ITEM_CD, '").append(itemState[i]).append("' ")
				.append(" ) L1 ")
				.append(" GROUP BY ITEM_CD, ITEM_STATE ")
				.append(" ) L2, ")
				.append(" ( ")
				.append(" SELECT ITEM_CD, '").append(itemState[i]).append("' ITEM_STATE, NVL(SUM(M2.CONFIRM_QTY), 0) AS RTN_OUT_QTY ")
				.append(" FROM LI020NM M1 ")
				.append(" JOIN LI020ND M2 ON M2.CENTER_CD      = M1.CENTER_CD ")
				.append(" AND M2.BRAND_CD       = M1.BRAND_CD ")
				.append(" AND M2.INBOUND_DATE   = M1.INBOUND_DATE ")
				.append(" AND M2.INBOUND_NO     = M1.INBOUND_NO ")
				.append(" JOIN CMCODE  C1 ON C1.CODE_CD        = M1.INOUT_CD ")
				.append(" AND C1.CODE_GRP       = 'LDIV03' ")
				.append(" AND C1.SUB_CD        IN ('D3')   ")            
				.append(" WHERE M1.CENTER_CD     = '").append(centerCd).append("' ")
				.append(" AND M1.BRAND_CD      = '").append(brandCd).append("' ")
				.append(" AND M1.INBOUND_DATE  > ADD_MONTHS(TRUNC(SYSDATE, 'DD'), -1) ")
				.append(" AND M2.INBOUND_STATE = '40' ")
				.append(" AND M2.ITEM_STATE = '").append(itemState[i]).append("' ")
				.append(" GROUP BY ITEM_CD, '").append(itemState[i]).append("' ")
				.append(" ) L3, CMAPIUSER L4 ")
				.append(" WHERE L1.ITEM_CD = L2.ITEM_CD(+) ")
				.append(" AND L1.ITEM_STATE = L2.ITEM_STATE(+) ")
				.append(" AND L1.ITEM_CD = L3.ITEM_CD(+) ")
				.append(" AND L1.ITEM_STATE = L3.ITEM_STATE(+) ")
				.append(" AND '").append(brandCd).append("' = L4.BRAND_CD(+) ");
				logger.info(sb.toString());
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				while(rs.next()) {
					DTOSendInventory dto = new DTOSendInventory();
					dto.setBizUserId(rs.getString("userId"));
					dto.setBizUserPw(rs.getString("userPw"));
					dto.setCallId(rs.getString("callId"));
					dto.setEncType(rs.getString("encType"));
					dto.setItemCd(rs.getString("itemCd"));
					dto.setItemState(rs.getString("itemState"));
					dto.setCurrQty(rs.getString("currQty"));
					dto.setAvailQty(rs.getString("availQty"));
					
					list.add(dto);
				}
				sb.delete(0, sb.length());
			}
			
			logger.debug("listSize: "+list.size());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(rsInven!=null) {
					rsInven.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return list;
	} 
	
//public ArrayList<DTOSendInventory> callSendUpdInventory(String centerCd, String brandCd) {
//		
//		ArrayList<DTOSendInventory> list = new ArrayList<DTOSendInventory>();
//		ConnManager connMgr = ConnManager.getInstance();
//		StringBuilder sb = new StringBuilder();
//		ResultSet rsInven = null;
//		
//		try {
//			conn = connMgr.getConnection();
//			sb.append(" UPDATE LO020NM ")
//			  .append(" SET FREE_VAL4 = '10' ")
//			  .append(" WHERE CENTER_CD = '").append(centerCd).append("' ")
//			  .append(" AND BRAND_CD = '").append(brandCd).append("' ")
//			  .append(" AND FREE_VAL4 IS NULL AND OUTBOUND_STATE = DECODE(INOUT_CD , 'D10', '50', '60') ");
//			
//			pstmt = conn.prepareStatement(sb.toString());
//			pstmt.executeUpdate();
//			
//			sb.delete(0, sb.length());
//			sb.append(" UPDATE LI020NM ")
//			  .append(" SET FREE_VAL4 = '10' ")
//			  .append(" WHERE CENTER_CD = '").append(centerCd).append("' ")
//			  .append(" AND BRAND_CD = '").append(brandCd).append("' ")
//			  .append(" AND FREE_VAL4 IS NULL AND INBOUND_STATE = DECODE(INOUT_CD , 'E10', '60', '50')");
//			pstmt = conn.prepareStatement(sb.toString());
//			pstmt.executeUpdate();
//			
//			sb.delete(0, sb.length());
//			sb.append(" UPDATE LC010NM ")
//			  .append(" SET FREE_VAL4 = '10' ")
//			  .append(" WHERE CENTER_CD = '").append(centerCd).append("' ")
//			  .append(" AND BRAND_CD = '").append(brandCd).append("' ")
//			  .append(" AND FREE_VAL4 IS NULL AND CONFIRM_YN = 'Y'");
//			pstmt = conn.prepareStatement(sb.toString());
//			pstmt.executeUpdate();
//					
//			sb.delete(0, sb.length());
//			sb.append(" SELECT L4.USER_ID userId, L4.USER_PW userPw, 'sendUpdInventory' callId, 'utf8' encType, L1.ITEM_CD itemCd, L1.ITEM_STATE itemState, L1.STOCK_QTY currQty, ")
//			.append("  ( L1.STOCK_QTY -(NVL(L2.OUT_WAIT_QTY,0)+NVL(L3.RTN_OUT_QTY,0))) availQty ")
//			.append("  FROM ( ")
//			.append("  SELECT A.ITEM_CD, B.ITEM_STATE, NVL(SUM(STOCK_QTY) ,0) AS STOCK_QTY ")
//			.append("   FROM CMITEM A, LS010NM B ")
//			.append("  WHERE A.BRAND_CD = B.BRAND_CD(+) ")
//			.append("  AND A.ITEM_CD = B.ITEM_CD(+)  ")
//			.append("  AND A.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND (A.ITEM_CD, B.ITEM_STATE) IN ")
//			.append("  ( SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LO020NM M1, LO020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.OUTBOUND_DATE = M2.OUTBOUND_DATE ")
//			.append("  AND M1.OUTBOUND_NO = M2.OUTBOUND_NO ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10'  ")
//			.append("  UNION  ")
//			.append("  SELECT DISTINCT  M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LI020NM M1, LI020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.INBOUND_DATE = M2.INBOUND_DATE ")
//			.append("  AND M1.INBOUND_NO = M2.INBOUND_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ")
//			.append("  UNION ")
//			.append("  SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE  ")
//			.append("  FROM LC010NM M1, LC010ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.ETC_DATE = M2.ETC_DATE ")
//			.append("  AND M1.ETC_NO = M2.ETC_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' )	")
//			.append("  GROUP BY A.ITEM_CD, B.ITEM_STATE ")
//			.append("  ) L1, ")
//			.append("  ( ")
//			.append("  SELECT ITEM_CD, ITEM_STATE, NVL(SUM(L1.CONFIRM_QTY), 0) AS OUT_WAIT_QTY ")
//			.append("  FROM ( ")
//			.append("  SELECT ITEM_CD, M2.ITEM_STATE, SUM(M2.CONFIRM_QTY)    AS CONFIRM_QTY ")
//			.append("  FROM LO020NM M1 ")
//			.append("  JOIN LO020ND M2 ON M2.CENTER_CD      = M1.CENTER_CD ")
//			.append("  AND M2.BRAND_CD       = M1.BRAND_CD ")
//			.append("  AND M2.OUTBOUND_DATE  = M1.OUTBOUND_DATE ")
//			.append("  AND M2.OUTBOUND_NO    = M1.OUTBOUND_NO ")
//			.append("  JOIN CMCODE  C1 ON C1.CODE_CD        = M1.INOUT_CD ")
//			.append("  AND C1.CODE_GRP       = 'LDIV03' ")
//			.append("  AND C1.SUB_CD        IN ('D1' ,'D2')                ")
//			.append("  WHERE M1.CENTER_CD       = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD        = '").append(brandCd).append("' ")
//			.append("  AND M1.OUTBOUND_DATE   > ADD_MONTHS(TRUNC(SYSDATE, 'DD'), -1) ")
//			.append("  AND M2.OUTBOUND_STATE IN ('20' ,'30' ,'40') ")
//			.append("  AND (M2.ITEM_CD, M2.ITEM_STATE) IN  ")
//			.append("  ( SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LO020NM M1, LO020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.OUTBOUND_DATE = M2.OUTBOUND_DATE ")
//			.append("  AND M1.OUTBOUND_NO = M2.OUTBOUND_NO ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10'  ")
//			.append("  UNION  ")
//			.append("  SELECT DISTINCT  M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LI020NM M1, LI020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.INBOUND_DATE = M2.INBOUND_DATE ")
//			.append("  AND M1.INBOUND_NO = M2.INBOUND_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ")
//			.append("  UNION ")
//			.append("  SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE  ")
//			.append("  FROM LC010NM M1, LC010ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.ETC_DATE = M2.ETC_DATE ")
//			.append("  AND M1.ETC_NO = M2.ETC_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ) ")
//			.append("  GROUP BY ITEM_CD, M2.ITEM_STATE	")
//			.append("  UNION ALL ")
//			.append("  SELECT ITEM_CD, M2.ITEM_STATE, SUM(M2.CONFIRM_QTY)    AS CONFIRM_QTY ")
//			.append("  FROM LC010NM        M1 ")
//			.append("  JOIN LC010ND   M2 ON M2.CENTER_CD      = M1.CENTER_CD ")
//			.append("  AND M2.BRAND_CD       = M1.BRAND_CD ")
//			.append("  AND M2.ETC_DATE       = M1.ETC_DATE ")
//			.append("  AND M2.ETC_NO         = M1.ETC_NO ")
//			.append("  WHERE M1.CENTER_CD        = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD         = '").append(brandCd).append("' ")
//			.append("  AND M1.ETC_DATE         > ADD_MONTHS(TRUNC(SYSDATE, 'DD'), -1) ")
//			.append("  AND M1.CONFIRM_YN    = 'N' ")
//			.append("  AND SUBSTR(M1.INOUT_CD ,1,1)      = 'D' ")
//			.append("  AND (M2.ITEM_CD, M2.ITEM_STATE) IN ")
//			.append("  ( SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LO020NM M1, LO020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.OUTBOUND_DATE = M2.OUTBOUND_DATE ")
//			.append("  AND M1.OUTBOUND_NO = M2.OUTBOUND_NO ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10'  ")
//			.append("  UNION  ")
//			.append("  SELECT DISTINCT  M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LI020NM M1, LI020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.INBOUND_DATE = M2.INBOUND_DATE ")
//			.append("  AND M1.INBOUND_NO = M2.INBOUND_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ")
//			.append("  UNION ")
//			.append("  SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE  ")
//			.append("  FROM LC010NM M1, LC010ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.ETC_DATE = M2.ETC_DATE ")
//			.append("  AND M1.ETC_NO = M2.ETC_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ) ")
//			.append("  GROUP BY ITEM_CD, M2.ITEM_STATE	")
//			.append("  ) L1 ")
//			.append("  GROUP BY ITEM_CD, ITEM_STATE ")
//			.append("  ) L2, ")
//			.append("  ( ")
//			.append("  SELECT ITEM_CD, M2.ITEM_STATE, NVL(SUM(M2.CONFIRM_QTY), 0) AS RTN_OUT_QTY ")
//			.append("  FROM LI020NM M1 ")
//			.append("  JOIN LI020ND M2 ON M2.CENTER_CD      = M1.CENTER_CD ")
//			.append("  AND M2.BRAND_CD       = M1.BRAND_CD ")
//			.append("  AND M2.INBOUND_DATE   = M1.INBOUND_DATE ")
//			.append("  AND M2.INBOUND_NO     = M1.INBOUND_NO ")
//			.append("  JOIN CMCODE  C1 ON C1.CODE_CD        = M1.INOUT_CD ")
//			.append("  AND C1.CODE_GRP       = 'LDIV03' ")
//			.append("  AND C1.SUB_CD        IN ('D3')               ")
//			.append("  WHERE M1.CENTER_CD     = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD      = '").append(brandCd).append("' ")
//			.append("  AND M1.INBOUND_DATE  > ADD_MONTHS(TRUNC(SYSDATE, 'DD'), -1) ")
//			.append("  AND M2.INBOUND_STATE = '40' ")
//			.append("  AND (M2.ITEM_CD, M2.ITEM_STATE) IN ")
//			.append("  ( SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LO020NM M1, LO020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.OUTBOUND_DATE = M2.OUTBOUND_DATE ")
//			.append("  AND M1.OUTBOUND_NO = M2.OUTBOUND_NO ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10'  ")
//			.append("  UNION  ")
//			.append("  SELECT DISTINCT  M2.ITEM_CD, M2.ITEM_STATE ")
//			.append("  FROM LI020NM M1, LI020ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.INBOUND_DATE = M2.INBOUND_DATE ")
//			.append("  AND M1.INBOUND_NO = M2.INBOUND_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ")
//			.append("  UNION ")
//			.append("  SELECT DISTINCT M2.ITEM_CD, M2.ITEM_STATE  ")
//			.append("  FROM LC010NM M1, LC010ND M2 ")
//			.append("  WHERE M1.CENTER_CD = M2.CENTER_CD ")
//			.append("  AND M1.BRAND_CD = M2.BRAND_CD ")
//			.append("  AND M1.ETC_DATE = M2.ETC_DATE ")
//			.append("  AND M1.ETC_NO = M2.ETC_NO  ")
//			.append("  AND M1.CENTER_CD = '").append(centerCd).append("' ")
//			.append("  AND M1.BRAND_CD = '").append(brandCd).append("' ")
//			.append("  AND M1.FREE_VAL4 = '10' ) ")
//			.append("  GROUP BY ITEM_CD, ITEM_STATE ")
//			.append("  ) L3, CMAPIUSER L4 ")
//			.append("  WHERE L1.ITEM_CD = L2.ITEM_CD(+) ")
//			.append("  AND L1.ITEM_STATE = L2.ITEM_STATE(+) ")
//			.append("  AND L1.ITEM_CD = L3.ITEM_CD(+) ")
//			.append("  AND L1.ITEM_STATE = L3.ITEM_STATE(+) ")
//			.append("  AND '").append(brandCd).append("' = L4.BRAND_CD(+) ");
//
//			logger.info(sb.toString());
//			pstmt.clearParameters();
//			pstmt = conn.prepareStatement(sb.toString());
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) {
//				DTOSendInventory dto = new DTOSendInventory();
//				dto.setBizUserId(rs.getString("userId"));
//				dto.setBizUserPw(rs.getString("userPw"));
//				dto.setCallId(rs.getString("callId"));
//				dto.setEncType(rs.getString("encType"));
//				dto.setItemCd(rs.getString("itemCd"));
//				dto.setItemState(rs.getString("itemState"));
//				dto.setCurrQty(rs.getString("currQty"));
//				dto.setAvailQty(rs.getString("availQty"));
//				
//				list.add(dto);
//			}
//			sb.delete(0, sb.length());
//			logger.debug("listSize: "+list.size());
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		} finally {
//			try {
//				if(rs!=null) {
//					rs.close();
//				}
//				if(rsInven!=null) {
//					rsInven.close();
//				}
//				if(pstmt!=null){ 
//					pstmt.close();
//				}
//				if(conn!=null) {
//					conn.close();
//				}
//			} catch (Exception e) {
//				loggerErr.error(e.getMessage());
//			}
//		}
//		return list;
//	}
	
//	public int updateUpdInvenState(String centerCd, String brandCd) {
//		int result = -1;
//		ConnManager connMgr = ConnManager.getInstance();
//		StringBuilder sb = new StringBuilder();
//		
//		try {
//			conn = connMgr.getConnection();
//			conn.setAutoCommit(false);
//			sb.append(" UPDATE LO020NM ")
//			  .append(" SET FREE_VAL4 = '60' ")
//			  .append(" WHERE CENTER_CD = ? AND BRAND_CD = ? AND FREE_VAL4 = '10' ");
//			
//			pstmt = conn.prepareStatement(sb.toString());
//			pstmt.setString(1, centerCd);
//			pstmt.setString(2, brandCd);
//			result = pstmt.executeUpdate();
//			if(result<1) {
//				logger.info("updInven LO sendData Update Rollback!");
//				conn.rollback();
//			} else {
//				logger.info("updInven LO sendData Update Commit!");
//				conn.commit();
//			}
//			
//			sb.delete(0, sb.length());
//			sb.append(" UPDATE LI020NM ")
//			  .append(" SET FREE_VAL4 = '60' ")
//			  .append(" WHERE CENTER_CD = ? AND BRAND_CD = ? AND FREE_VAL4 = '10' ");
//			
//			pstmt = conn.prepareStatement(sb.toString());
//			pstmt.setString(1, centerCd);
//			pstmt.setString(2, brandCd);
//			result = pstmt.executeUpdate();
//			if(result<1) {
//				logger.info("updInven LI sendData Update Rollback!");
//				conn.rollback();
//			} else {
//				logger.info("updInven LI sendData Update Commit!");
//				conn.commit();
//			}
//			
//			sb.delete(0, sb.length());
//			sb.append(" UPDATE LC010NM ")
//			  .append(" SET FREE_VAL4 = '60' ")
//			  .append(" WHERE CENTER_CD = ? AND BRAND_CD = ? AND FREE_VAL4 = '10' ");
//			
//			pstmt = conn.prepareStatement(sb.toString());
//			pstmt.setString(1, centerCd);
//			pstmt.setString(2, brandCd);
//			result = pstmt.executeUpdate();
//			if(result<1) {
//				logger.info("updInven LC sendData Update Rollback!");
//				conn.rollback();
//			} else {
//				logger.info("updInven LC sendData Update Commit!");
//				conn.commit();
//			}
//			
//			
//			
//		} catch (SQLException e) {
//			updateError(brandCd);
//			loggerErr.error(e.getMessage());
//		} catch (Exception e) {
//			updateError(brandCd);
//			loggerErr.error(e.getMessage());
//		} finally {
//			try {
//				if(rs!=null) {
//					rs.close();
//				}
//				if(pstmt!=null){ 
//					pstmt.close();
//				}
//				if(conn!=null) {
//					conn.close();
//				}
//			} catch (Exception e) {
//				loggerErr.error(e.getMessage());
//			}
//		}
//		return result;
//	}

	public int updateExtraceSucc(String brandCd, String code) {
		
		int result = -1;
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		String condQuery = ""; 
		if("60".equals(code)) {
			condQuery = " AND EAI_ERR_MSG = '10' ";
		} else {
			condQuery = " AND EAI_ERR_MSG IS NULL ";
		}
		
		sb.append(" UPDATE EDIEXPRESS_CJ_RECEIVE ")
		.append(" SET EAI_ERR_MSG = '").append(code).append("' ")
		.append(" WHERE CUST_USE_NO LIKE '__").append(brandCd).append("%' ")
		.append(" AND (CRG_ST = '91' OR CRG_ST = '84') ").append(condQuery);		
		try {
			conn = connMgr.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sb.toString());
			logger.debug(sb.toString());
			
			result = pstmt.executeUpdate();
			if(result<1) {
				logger.info("sendData Update Rollback!");
				conn.rollback();
			} else {
				logger.info("sendData Update Commit!");
				conn.commit();
			}
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return result;
	}
	public int updateExtraceError(String brandCd) {
		
		int result = -1;
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE EDIEXPRESS_CJ_RECEIVE ")
		.append(" SET EAI_ERR_MSG = 'FAILED' ")
		.append(" WHERE CUST_USE_NO LIKE '__").append(brandCd).append("%' ")
		.append(" AND (CRG_ST = '91' OR CRG_ST = '84') AND EAI_ERR_MSG IS NULL ");		
		try {
			conn = connMgr.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, brandCd);
			result = pstmt.executeUpdate();
			if(result<1) {
				logger.info("sendData Update Rollback!");
				conn.rollback();
			} else {
				logger.info("sendData Update Commit!");
				conn.commit();
			}
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return result;
	}
	public int updateSucc(String brandCd) {
		
		int result = -1;
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE LO020NM ")
		.append(" SET SEND_STATE = '60' ")
		.append(" WHERE BRAND_CD = ? ")
			.append(" AND OUTBOUND_DATE    <= TRUNC(SYSDATE) ")
			.append(" AND OUTBOUND_STATE   = DECODE(INOUT_CD, 'D10', '50', '60') ")
			.append(" AND (SEND_STATE = '00' OR SEND_STATE IS NULL ) ");		
		try {
			conn = connMgr.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, brandCd);
			result = pstmt.executeUpdate();
			if(result<1) {
				logger.info("sendData Update Rollback!");
				conn.rollback();
			} else {
				logger.info("sendData Update Commit!");
				conn.commit();
			}
		} catch (SQLException e) {
			updateError(brandCd);
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			updateError(brandCd);
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return result;
	}
	
	public ArrayList<DTOSendExpressTrace> callSendExpressTrace(String brandCd, String code) {
		
		ArrayList<DTOSendExpressTrace> list = new ArrayList<DTOSendExpressTrace>();
		StringBuilder sb = new StringBuilder();
		
		String codeCond = "";
		String callId = "";
		if("00".equals(code)) {
			codeCond = " AND EAI_ERR_MSG IS NULL ";
			callId = "sendExpressTrace";
		} else {
			codeCond = " AND EAI_ERR_MSG = '10'";
			callId = "sendExpressTraceAll";
		}
		
		sb.append(" SELECT USER_ID bizUserId, USER_PW bizUserPw, '").append(callId)
		.append("' callId, 'UTF8' encType , M3.BRAND_NO brandNo, INVC_NO expressNo, ")  
		.append(" DECODE(CRG_ST, '91', 'S', 'F') dvState, ") 
		.append(" CASE WHEN NO_CLDV_RSN_CD = '01' THEN '고객정보오류' ")   
		.append(" WHEN NO_CLDV_RSN_CD = '02' THEN '고객부재'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '05' THEN '지연도착'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '06' THEN '분류오류'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '08' THEN '통화불가능' ")  
		.append(" WHEN NO_CLDV_RSN_CD = '09' THEN '수취거부'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '11' THEN '천재지변'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '16' THEN '착지변경'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '21' THEN '상품사고(파손/분실)' ")  
		.append(" WHEN NO_CLDV_RSN_CD = '23' THEN '토요휴무'  ")
		.append(" WHEN NO_CLDV_RSN_CD = '24' THEN '지정일배달' ")  
		.append(" WHEN NO_CLDV_RSN_CD = '32' THEN '차량고장/사고' ")  
		.append(" END noDvReason  ")
		.append(" , TO_CHAR(REG_DTIME,'YYYYMMDDHH24MISS') regDatetime   ")
		.append(" FROM EDIEXPRESS_CJ_RECEIVE M1 , CMAPIUSER M2 , ( SELECT DISTINCT L1.CENTER_CD, ") 
		.append(" L1.BRAND_CD, L1.OUTBOUND_DATE, ") 
		.append(" L1.OUTBOUND_NO, L2.BRAND_NO ")
		.append(" FROM LO020NM L1, LO020ND L2  ")
		.append(" WHERE L1.CENTER_CD = L2.CENTER_CD ")
		.append(" AND L1.BRAND_CD = L2.BRAND_CD ")
		.append(" AND L1.OUTBOUND_DATE = L2.OUTBOUND_DATE ")
		.append(" AND L1.OUTBOUND_NO = L2.OUTBOUND_NO ")
		.append(" AND L1.INOUT_CD = 'D10' AND L1.BRAND_CD = '").append(brandCd).append("' ") 
		.append(" UNION ALL ")
		.append(" SELECT DISTINCT L1.CENTER_CD ")
		.append(" , L1.BRAND_CD, L1.ORDER_DATE, ") 
		.append(" L1.ORDER_NO, L2.BRAND_NO ")
		.append(" FROM LO010NM L1, LO010ND L2 ")
		.append(" WHERE L1.CENTER_CD = L2.CENTER_CD ")
		.append(" AND L1.BRAND_CD = L2.BRAND_CD ")
		.append(" AND L1.ORDER_DATE = L2.ORDER_DATE ")
		.append(" AND L1.ORDER_NO = L2.ORDER_NO ")
		.append(" AND L1.BRAND_CD = '").append(brandCd).append("' ")
		.append(" AND L1.INOUT_CD = 'E30' ")
		.append(" ) M3  ")
		.append(" WHERE  SUBSTR(M1.CUST_USE_NO, 1, 2) = M3.CENTER_CD ")
		.append("     AND SUBSTR(M1.CUST_USE_NO, 3, 4) = M3.BRAND_CD ")
		.append("     AND SUBSTR(M1.CUST_USE_NO, 7, 8) = M3.OUTBOUND_DATE ")
		.append("     AND SUBSTR(M1.CUST_USE_NO, 15, 4) = M3.OUTBOUND_NO ")
		.append("     AND CUST_USE_NO LIKE '__").append(brandCd).append("%'  ")
		.append("     AND (CRG_ST = '91' OR CRG_ST = '84') ").append(codeCond);
		
		
		ConnManager connMgr = ConnManager.getInstance();
		try {
			logger.info(sb.toString());
			conn = connMgr.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				DTOSendExpressTrace dto = new DTOSendExpressTrace();
				
				dto.setBizUserId(rs.getString("bizUserId"));
				dto.setBizUserPw(rs.getString("bizUserPw"));
				dto.setCallId(rs.getString("callId"));
				dto.setEncType(rs.getString("encType"));
				dto.setBrandNo(rs.getString("brandNo"));
				dto.setExpressNo(rs.getString("expressNo"));
				dto.setDvState(rs.getString("dvState"));
				dto.setNoDvReason(rs.getString("noDvReason"));
				dto.setRegDatetime(rs.getString("regDatetime"));
				list.add(dto);
			}
			sb.delete(0, sb.length());
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return list;
	}
	
	public int updateError(String brandCd) {
		int result = -1;
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE LO020NM ")
		.append(" SET SEND_STATE = '99' ")
		.append(" WHERE BRAND_CD = ? ")
			.append(" AND OUTBOUND_DATE    <= TRUNC(SYSDATE) ")
			.append(" AND OUTBOUND_STATE   = '50' ")
			.append(" AND (SEND_STATE = '00' OR SEND_STATE IS NULL ) ");		
		try {
			conn = connMgr.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, brandCd);
			result = pstmt.executeUpdate();
			if(result<1) {
				logger.info("sendData Error Update Rollback!");
				conn.rollback();
			} else {
				logger.info("sendData Error Update Commit!");
				conn.commit();
			}
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return result;
	}
}
