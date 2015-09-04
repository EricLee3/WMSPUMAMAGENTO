package com.wmsapi.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.internal.OracleCallableStatement;

import org.apache.log4j.Logger;

import com.wmsapi.dto.DTOGetAvailQty;
import com.wmsapi.dto.DTORecvCancel;
import com.wmsapi.dto.DTORecvChInfo;
import com.wmsapi.dto.DTORecvItem;
import com.wmsapi.dto.DTORecvOutbound;
import com.wmsapi.utils.ConnManager;
import com.wmsapi.utils.PropManager;

public class DAORecvAPI {

	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
	private static DAORecvAPI daoRecvApi = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private OracleCallableStatement ocstmt = null;
	private CallableStatement cs = null;
	private ResultSet rs = null;
	
	private DAORecvAPI() {}
	public static DAORecvAPI getInstance() {
		if(daoRecvApi == null) {
			daoRecvApi = new DAORecvAPI();
		}
		return daoRecvApi;
	}
	
	public boolean isVaildId(String userId, String userPw) {
		boolean isValid = false;
		ConnManager connMgr = ConnManager.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT NVL(MAX('Y'), 'N') FROM CMAPIUSER WHERE USER_ID = ? AND USER_PW = ? ");
		logger.debug(sb.toString());
		try {
			conn = connMgr.getConnection();
			pstmt = conn.prepareStatement(sb.toString());
			logger.debug("userId: "+userId);
			logger.debug("userPw: "+userPw);
			
			logger.debug("userId: "+userId);
			logger.debug("userPw: "+userPw);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			rs = pstmt.executeQuery();
			rs.next();
			String value = rs.getString(1);
			if("Y".equals(value)) {
				isValid = true;
			} else {
				isValid = false;
			}
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			loggerErr.error(e.getMessage());
		}  finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(cs!=null) {
					cs.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return isValid;
	}
	
	public String[] callProcOutbound(ArrayList<DTORecvOutbound> list) {
		String[] svcResult = new String[2];
		StringBuilder sb = new StringBuilder();
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("wmsdb");
		int procCnt = Integer.parseInt(prop.getProperty("proc.outbound.paramCnt"));
		ConnManager connMgr = ConnManager.getInstance();
		try {
			conn = connMgr.getConnection();
			sb.append(" call IF_RECV_OUTORDER_PLAN")
			  .append("(?,?,?,?");
			
			for(int i=0; i<procCnt; i++) {
				sb.append(",?");
			}
			sb.append(")");
			
			logger.debug(procCnt);
			logger.debug(sb.toString());
			
//			cs = conn.prepareCall("{ ? = "+sb.toString()+" }");
			cs = conn.prepareCall(sb.toString());
			for(int i=0; i<list.size(); i++) {

				DTORecvOutbound dto = list.get(i);
				logger.debug("dao: "+ dto.getItemCd());
						
				cs.registerOutParameter(1, OracleTypes.VARCHAR);
				cs.registerOutParameter(2, OracleTypes.VARCHAR);
				if(i>=(list.size()-1)) {
					cs.setString(3, "Y"); // LAST_YN
//					System.out.println("row"+i+", col3: Y");
				} else {
					cs.setString(3, "N"); // LAST_YN
				}
				cs.setString(4, dto.getBrandCd());
				cs.setString(5, dto.getCenterCd());
				cs.setString(6, dto.getOrderDate());
				cs.setString(7, dto.getInoutCd());
				cs.setString(8, dto.getDeliveryCd());
				cs.setString(9, dto.getRealDeliveryCd());
				if(dto.getAcperNm()!=null){
					cs.setString(10, dto.getAcperNm().substring(0, dto.getAcperNm().length()-1)+"*");
				} else {
					cs.setString(10, dto.getAcperNm());
				}
				cs.setString(11, dto.getAcperCd());
				cs.setString(12, dto.getAcperTel());
				cs.setString(13, dto.getAcperHp());
				cs.setString(14, dto.getAcperZipCd1());
				cs.setString(15, dto.getAcperZipCd2());
				cs.setString(16, dto.getAcperBasic());
				cs.setString(17, dto.getAcperDetail());
				
				if(dto.getOrdNm()!=null){
					cs.setString(18, dto.getOrdNm().substring(0, dto.getAcperNm().length()-1)+"*");
				} else {
					cs.setString(18, dto.getOrdNm());
				}
				cs.setString(19, dto.getOrdCd());
				cs.setString(20, dto.getOrdTel());
				cs.setString(21, dto.getOrdHp());
				cs.setString(22, dto.getOrdZipCd1());
				cs.setString(23, dto.getOrdZipCd2());
				cs.setString(24, dto.getOrdBasic());
				cs.setString(25, dto.getOrdDetail());
				cs.setString(26, dto.getDeliveryMsg());
				cs.setString(27, dto.getItemCd());
				cs.setString(28, dto.getItemState());
				cs.setString(29, dto.getInputQty());
				cs.setString(30, dto.getOrderQty());
				cs.setString(31, dto.getBrandDate());
				cs.setString(32, dto.getBrandNo());
				cs.setString(33, dto.getBrandLineNo());
				cs.setString(34, dto.getIndentDate());
				cs.setString(35, dto.getIndentNo());
				cs.setString(36, dto.getFirstIndentNo());
				cs.setString(37, dto.getSerialNo());
				cs.setString(38, dto.getFreeVal1());
				cs.setString(39, dto.getReturnDiv());
				cs.setString(40, dto.getFreeVal2());
				cs.setString(41, dto.getFreeVal4());
				cs.setString(42, dto.getFreeVal3D());
				if(dto.getBrandCd().equals("6101")) {
					cs.setString(43, "Z999");
				} else {
					cs.setString(43, dto.getFreeVal3());
				}
				cs.setString(44, dto.getFreeVal6());
				cs.setString(45, dto.getFreeVal8());
				cs.setString(46, dto.getDomesticGubun());
				cs.setString(47, dto.getDestCountry());
				cs.setString(48, dto.getDestCity());
				cs.setString(49, dto.getDestState());
				cs.setString(50, dto.getItemGroup());
				cs.setString(51, dto.getHscode());
				cs.setString(52, dto.getCurrency());
				cs.setString(53, dto.getSupplyPrice());
				cs.setString(54, dto.getSupplyAmt());
				cs.setString(55, dto.getTotalAmt());
				cs.setString(56, dto.getOutWeight());
				cs.setString(57, dto.getPickupYN());
				cs.setString(58, dto.getFreeVal4D());			
				cs.setString(59, dto.getFreeVal1D());
				for(int j=60; j<=(procCnt+4); j++) {
					cs.setString(j, null);
				}
				cs.execute();
				
				ocstmt = (OracleCallableStatement)cs;
	            String errCd = (String)ocstmt.getString(1);
	            String errCts = (String)ocstmt.getString(2);
	            
	            logger.debug("errCd: "+errCd);
	            logger.debug("errCts: "+errCts);
	            
	            svcResult[0] = errCd;
	            svcResult[1] = errCts;
			}
		} catch (SQLException e) {
			svcResult[0] = "ERR_004";
            svcResult[1] = "";
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			svcResult[0] = "ERR_004";
            svcResult[1] = "";
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(cs!=null) {
					cs.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				loggerErr.error(e.getMessage());
			}
		}
		return svcResult;
	}
	public String[] callProcItem(ArrayList<DTORecvItem> list) {
		String[] svcResult = new String[4];
		StringBuilder sb = new StringBuilder();
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp("wmsdb");
		int procCnt = Integer.parseInt(prop.getProperty("proc.item.paramCnt"));
		ConnManager connMgr = ConnManager.getInstance();
		
		String errorMsg="";
		int uptCnt=0;
		int totCnt=0;
		
		try {
			sb.append(" call IF_RECV_ITEM")
			  .append("(?,?,?,?");
			
			for(int i=0; i<procCnt; i++) {
				sb.append(",?");
			}
			sb.append(")");
			
			conn = connMgr.getConnection();
			cs = conn.prepareCall(sb.toString());
			for(int i=0; i<list.size(); i++) {
				DTORecvItem dto = list.get(i);
				cs.registerOutParameter(1, OracleTypes.VARCHAR);
				cs.registerOutParameter(2, OracleTypes.VARCHAR);
				if(i>=(list.size()-1)) {
					cs.setString(3, "Y"); // LAST_YN
//					System.out.println("row"+i+", col3: Y");
				} else {
					cs.setString(3, "N"); // LAST_YN
				}
				cs.setString(4, dto.getBrandCd());
				cs.setString(5, dto.getItemCd());
				cs.setString(6, dto.getItemNm());
				cs.setString(7, dto.getItemBarCd());
				cs.setString(8, dto.getItemColor());
				cs.setString(9, dto.getItemSize());
				cs.setString(10, dto.getFactCd());
				cs.setString(11, dto.getOpenDate());
				cs.setString(12, dto.getItemBrandCd());
				cs.setString(13, dto.getItemBrandNm());
				cs.setString(14, dto.getSupplyPrice());
				cs.setString(15, dto.getSalePrice());
				cs.setString(16, dto.getItemStyle());
				cs.setString(17, dto.getYears());
				cs.setString(18, dto.getSeasons());
				cs.setString(19, dto.getMansDiv());
				cs.setString(20, dto.getCountryNm());
				cs.setString(21, dto.getMaterial1());
				cs.setString(22, dto.getMaterial2());
				cs.setString(23, dto.getDepartCd());
				cs.setString(24, dto.getLineCd());
				cs.setString(25, dto.getClassCd());
				cs.setString(26, dto.getFreeVal1());
				cs.setString(27, dto.getFreeVal2());
				cs.setString(28, dto.getFreeVal4());
				for(int j=29; j<=(procCnt+4); j++) {
					cs.setString(j, null);
				}
				cs.execute();
				
				ocstmt = (OracleCallableStatement)cs;
	            String errCd = (String)ocstmt.getString(1);
	            String errCts = (String)ocstmt.getString(2);
	            
	            logger.debug("errCd: "+errCd);
	            logger.debug("errCts: "+errCts);
	            
	            if("S".equals(errCd)) {
	            	uptCnt++;
	            } else {
	            	if(errorMsg.length() > 0) {
	            		errorMsg += ",";
	            	}
	            	errorMsg += errorMsg += errorMsg += "[brandCd: "+dto.getBrandCd()+", itemCd: "+dto.getItemCd()+"=> Error msg: "+errCts+"]";
	            }
	            totCnt++;
			}
			
			if(errorMsg.length()<1) {
				svcResult[0] = "SUCC";
				svcResult[1] = "";
			} else {
				svcResult[0] = "ERR_006";
				svcResult[1] = errorMsg;
			}
            svcResult[2] = String.valueOf(totCnt);
            svcResult[3] = String.valueOf(uptCnt);
            
		} catch (SQLException e) {
			svcResult[0] = "ERR_004";
			svcResult[1] = "";
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			svcResult[0] = "ERR_004";
			svcResult[1] = "";
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(cs!=null) {
					cs.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				svcResult[0] = "ERR_004";
				svcResult[1] = "";
				loggerErr.error(e.getMessage());
			}
		}
		return svcResult;
	}
	public String[] callProcCancel(DTORecvCancel dto) {
		String[] resp = new String[4];
		StringBuilder sb = new StringBuilder();
		ConnManager connMgr = ConnManager.getInstance();
		try {
			sb.append(" call IF_RECV_CANCEL")
			  .append("(?,?,?,?,?,?,?,?)");
			
			conn = connMgr.getConnection();
			cs = conn.prepareCall(sb.toString());
			
			cs.registerOutParameter(1, OracleTypes.VARCHAR);
			cs.registerOutParameter(2, OracleTypes.VARCHAR);
			cs.setString(3, "Y"); // LAST_YN			
			cs.setString(4, dto.getBrandCd());
			cs.setString(5, dto.getCenterCd());
			cs.setString(6, dto.getBrandDate());
			cs.setString(7, dto.getBrandNo());
			cs.setString(8, dto.getCancelDiv());
			cs.execute();
			
			ocstmt = (OracleCallableStatement)cs;
            String errCd = (String)ocstmt.getString(1);
            String errCts = (String)ocstmt.getString(2);
            
            logger.debug("errCd: "+errCd);
            logger.debug("errCts: "+errCts);
            
            if("S".equals(errCd)) {
            	errCd = "SUCC";
            }
            resp[0] = errCd;
            resp[1] = errCts;
            resp[2] = "1";
            resp[3] = "1";
		} catch (SQLException e) {
			loggerErr.error(e.getMessage());
			resp[0] = "ERR_004";
            resp[1] = "";
            resp[2] = "0";
            resp[3] = "0";
		} catch (Exception e) {
			resp[0] = "ERR_004";
            resp[1] = "";
            resp[2] = "0";
            resp[3] = "0";
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(cs!=null) {
					cs.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				resp[0] = "ERR_004";
	            resp[1] = "";
	            resp[2] = "0";
	            resp[3] = "0";
				loggerErr.error(e.getMessage());
			}
		}
		return resp;
	}
	
	public String callProcGetAvailQty(ArrayList<DTOGetAvailQty> list) {
		String respString = "";
		StringBuilder sb = new StringBuilder();
		ConnManager connMgr = ConnManager.getInstance();
		try {
			conn = connMgr.getConnection();
			String itemLot = "";
			sb.append(" SELECT GET_ITEM_LOT(? , ?) ITEM_LOT FROM DUAL ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, list.get(0).getBrandCd());
			pstmt.setString(2, list.get(0).getItemCd());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				itemLot = rs.getString("ITEM_LOT");
			}
			
			for(int i=0; i<list.size(); i++) {
				cs = conn.prepareCall("{ ? = call CC_GET_QTY_PSTOCK.RS_MASTER(?, ?, ?, ?, ?, ?)}");
				cs.registerOutParameter(1,OracleTypes.CURSOR);
				cs.setString(2, list.get(i).getCenterCd());
				cs.setString(3, list.get(i).getBrandCd());
				cs.setString(4, list.get(i).getItemCd());
				cs.setString(5, list.get(i).getItemState());
				cs.setString(6, itemLot);
				cs.setString(7, "N");
				cs.execute();
			
				rs = (ResultSet)cs.getObject(1);
				while(rs.next()) {
					list.get(i).setCurrQty(rs.getString("STOCK_QTY"));
					list.get(i).setAvailQty(rs.getString("PSTOCK_QTY"));
				}
				
				logger.debug("centerCd:"+list.get(i).getCenterCd());
				logger.debug("brandCd:"+list.get(i).getBrandCd());
				logger.debug("itemCd:"+list.get(i).getItemCd());
				logger.debug("itemState:"+list.get(i).getItemState());
				logger.debug("availQty:"+list.get(i).getAvailQty());
				logger.debug("currQty:"+list.get(i).getCurrQty());
			}
			
			respString = "SUCC";
		} catch (SQLException e) {
			respString = "ERR_004";
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			respString = "ERR_004";
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(cs!=null) {
					cs.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				respString = "ERR_004";
				loggerErr.error(e.getMessage());
			}
		}
		
		return respString; 
	}
	
	public String[] updateChInfo(DTORecvChInfo dtoChInfo) { 
		String[] svcResult = new String[4];
		StringBuilder sb = new StringBuilder();
		ConnManager connMgr = ConnManager.getInstance();
		int result = 0;
		try {
			conn = connMgr.getConnection();
			conn.setAutoCommit(false);
			
			sb.append(" UPDATE LO010NM ")
			  .append(" SET ACPER_NM = ? ")
			  .append(" , ACPER_TEL = ? ")
			  .append(" , ACPER_HP = ? ")
			  .append(" , ACPER_ZIP_CD1 = ? ")
			  .append(" , ACPER_ZIP_CD2 = ? ")
			  .append(" , ACPER_BASIC = ? ")
			  .append(" , ACPER_DETAIL = ? ")
			  .append(" WHERE (CENTER_CD , BRAND_CD , ORDER_DATE, ORDER_NO) IN ( ")
			  .append(" SELECT CENTER_CD, BRAND_CD, ORDER_DATE, ORDER_NO ")
			  .append(" FROM LO010ND WHERE CENTER_CD = ? ")
			  .append(" AND BRAND_CD = ? ")
			  .append(" AND ORDER_DATE = TO_DATE('").append(dtoChInfo.getOrderDate()).append("','YYYYMMDD')")
			  .append(" AND BRAND_NO = ? AND ROWNUM = 1) ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dtoChInfo.getAcperNm());
			pstmt.setString(2, dtoChInfo.getAcperTel());
			pstmt.setString(3, dtoChInfo.getAcperHp());
			pstmt.setString(4, dtoChInfo.getAcperZipCd1());
			pstmt.setString(5, dtoChInfo.getAcperZipCd2());
			pstmt.setString(6, dtoChInfo.getAcperBasic());
			pstmt.setString(7, dtoChInfo.getAcperDetail());
			pstmt.setString(8, dtoChInfo.getCenterCd());
			pstmt.setString(9, dtoChInfo.getBrandCd());
			pstmt.setString(10, dtoChInfo.getBrandNo());
			result = pstmt.executeUpdate();
			
			if(result < 1) {
				logger.info("[centerCd = "+ dtoChInfo.getCenterCd()
						   +" brandCd = "+dtoChInfo.getBrandCd()
						   +" orderDate = "+dtoChInfo.getOrderDate()
						   +" brandNo = "+dtoChInfo.getBrandNo()
						   +"] 해당 출고지시건이 없습니다.");
				conn.rollback();
				svcResult[0] = "ERR_006";
				svcResult[1] = "[centerCd = "+ dtoChInfo.getCenterCd()
							   +" brandCd = "+dtoChInfo.getBrandCd()
							   +" orderDate = "+dtoChInfo.getOrderDate()
							   +" brandNo = "+dtoChInfo.getBrandNo()
							   +"] 해당 출고지시건이 없습니다.";
				svcResult[2] = "0";
				svcResult[3] = "0";
				return svcResult;
			}
			pstmt.clearParameters();
			
			sb.delete(0, sb.length());
			sb.append(" UPDATE LO020NM ")
			  .append(" SET ACPER_NM = ? , ACPER_TEL = ? , ACPER_HP = ? ,  ACPER_ZIP_CD1 = ? ")
			  .append(" , ACPER_ZIP_CD2 = ? , ACPER_BASIC = ? , ACPER_DETAIL = ? ")
			  .append(" WHERE ( CENTER_CD, BRAND_CD, OUTBOUND_DATE, OUTBOUND_NO ) ") 
			  .append(" IN ( SELECT CENTER_CD, BRAND_CD, OUTBOUND_DATE, OUTBOUND_NO ")
              .append(" FROM LO020ND ")
              .append(" WHERE (CENTER_CD, BRAND_CD, OUTBOUND_DATE) IN ( ")
			  .append(" SELECT CENTER_CD, BRAND_CD, OUTBOUND_DATE ")
              .append(" FROM LO020NM ")
              .append(" WHERE CENTER_CD = ? ") 
              .append(" AND BRAND_CD = ? ")
              .append(" AND ORDER_DATE = TO_DATE('").append(dtoChInfo.getOrderDate()).append("' ,'YYYYMMDD') ")
              .append(" ) ")
              .append(" AND BRAND_NO = ? AND ROWNUM = 1 ) ");
            System.out.println(sb.toString());
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dtoChInfo.getAcperNm());
			pstmt.setString(2, dtoChInfo.getAcperTel());
			pstmt.setString(3, dtoChInfo.getAcperHp());
			pstmt.setString(4, dtoChInfo.getAcperZipCd1());
			pstmt.setString(5, dtoChInfo.getAcperZipCd2());
			pstmt.setString(6, dtoChInfo.getAcperBasic());
			pstmt.setString(7, dtoChInfo.getAcperDetail());
			pstmt.setString(8, dtoChInfo.getCenterCd());
			pstmt.setString(9, dtoChInfo.getBrandCd());
			pstmt.setString(10, dtoChInfo.getBrandNo());
			result = pstmt.executeUpdate();
			if(result < 1) {
				logger.info("[centerCd = "+ dtoChInfo.getCenterCd()
						   +" brandCd = "+dtoChInfo.getBrandCd()
						   +" orderDate = "+dtoChInfo.getOrderDate()
						   +" brandNo = "+dtoChInfo.getBrandNo()
						   +"] 해당 출고지시건이 없습니다.");
				conn.rollback();
				
				svcResult[0] = "ERR_006";
				svcResult[1] = "[centerCd = "+ dtoChInfo.getCenterCd()
							   +" brandCd = "+dtoChInfo.getBrandCd()
							   +" orderDate = "+dtoChInfo.getOrderDate()
							   +" brandNo = "+dtoChInfo.getBrandNo()
							   +"] 해당 출고지시건이 없습니다.";
				svcResult[2] = "0";
				svcResult[3] = "0";
				return svcResult;
			}
			
			svcResult[0] = "SUCC";
			svcResult[1] = "";
			svcResult[2] = "1";
			svcResult[3] = "1";
			
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception e1) {
				loggerErr.error(e1.getMessage());
			}
			svcResult[0] = "ERR_004";
			svcResult[1] = "";
			svcResult[2] = "0";
			svcResult[3] = "0";
			loggerErr.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception e1) {
				loggerErr.error(e1.getMessage());
			}
			svcResult[0] = "ERR_004";
			svcResult[1] = "";
			svcResult[2] = "0";
			svcResult[3] = "0";
			loggerErr.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null){ 
					pstmt.close();
				}
				if(cs!=null) {
					cs.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (Exception e1) {
					loggerErr.error(e1.getMessage());
				}
				svcResult[0] = "ERR_004";
				svcResult[1] = "";
				svcResult[2] = "0";
				svcResult[3] = "0";
				loggerErr.error(e.getMessage());
			}
		}
		return svcResult;
	}
}
