package com.wmsapi.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ValidateUtils {
	
	private static Logger loggerErr = Logger.getLogger("process.err");
	private static String[] isValidCenter(String propName, String[] values) {
		String[] result = {"SUCC", ""};
		ConnManager connMgr = ConnManager.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT NVL(MAX('Y'),'N') IS_VALID FROM EDIINTERFACE ")
			.append(" WHERE BRAND_CD = ? AND IF_CUST_CD = ? AND IF_GRP   = 'CENTER'");
		
		try {
			conn = connMgr.getConnection();
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, values[1]);
			pstmt.setString(2, values[0]);
			
			rs = pstmt.executeQuery();
			rs.next();
			String isValid = rs.getString("IS_VALID");
			if("N".equals(isValid)) {
				result[0] = "ERR_006";
				result[1] = "centerCd Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
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
	
	private static String[] isValidBrandCd(String propName, String[] values) {
		String[] result = {"SUCC", ""};
		ConnManager connMgr = ConnManager.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT NVL(MAX('Y'),'N') IS_VALID FROM CMBRAND ")
			.append(" WHERE BRAND_CD = ?");
		try {
			conn = connMgr.getConnection();
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, values[1]);
			rs = pstmt.executeQuery();
			rs.next();
			String isValid = rs.getString("IS_VALID");
			if("N".equals(isValid)) {
				result[0] = "ERR_006";
				result[1] = "brandCd Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
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
	
	private static String[] isValidDeliveryCd(String propName, String[] values) {
		String[] result = {"SUCC", ""};
		ConnManager connMgr = ConnManager.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT NVL(MAX('Y'),'N') IS_VALID FROM CMDELIVERY ")
			.append(" WHERE BRAND_CD = ? AND DELIVERY_CD = ?");
		try {
			conn = connMgr.getConnection();
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, values[1]);
			pstmt.setString(2, values[4]);
			rs = pstmt.executeQuery();
			rs.next();
			String isValid = rs.getString("IS_VALID");
			if("N".equals(isValid)) {
				result[0] = "ERR_006";
				result[1] = "deliveryCd Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
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
	
	private static String[] lengthCheck(String propName, String[] values) {
		String[] result = {"SUCC", ""};
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp(propName);
		
		try {
			int i=1;
			while(prop.getProperty("col"+i+".length")!=null) {
				if("int".equals(prop.getProperty("col"+i+".length"))) {
					i++;
					continue;
				}
				
				if(getBytes(values[i-1]) >
					Integer.parseInt(prop.getProperty("col"+i+".length"))) {
					
					result[0] = "ERR_006";
					result[1] = prop.getProperty("col"+i+".name")+" Length Error => recvSize: "+getBytes(values[i-1])+", maxSize: "
								+Integer.parseInt(prop.getProperty("col"+i+".length"));
					break;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		}
		return result;
	}
	
	private static String[] duplBrandNoCheck(String propName, String[] values) {
		String[] result = {"SUCC", ""};
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp(propName);
		ConnManager connMgr = ConnManager.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(" SELECT NVL(MAX('Y'),'N') IS_DUPL FROM EDIROUTORDER ")
			  .append(" WHERE CENTER_CD = ? AND BRAND_CD = ? AND BRAND_NO = ? AND ERROR_DIV = '3' ");
			
			conn = connMgr.getConnection();
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, prop.getProperty(propName+"."+values[0]));
			pstmt.setString(2, values[1]);
			pstmt.setString(3, values[28]);
			rs = pstmt.executeQuery();
			rs.next();
			String isDupl = rs.getString("IS_DUPL");
			if("Y".equals(isDupl)){
				result[0] = "ERR_006";
				result[1] = "BrandNo Duplicatioin Error";
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
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
	
	private static String[] nullCheck(String propName, String[] values) {
		String[] result = {"SUCC", ""};
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp(propName);
		
		try {
			int i=1;
			while(prop.getProperty("col"+i+".nullCheck")!=null) {
				if("Y".equals(prop.getProperty("col"+i+".nullCheck"))) {
					String value = "";
					System.out.println("aa");
					if(values[i-1] != null) {
						System.out.println("bb");
						value = values[i-1];
					}
					
					System.out.println("values[i-1]: "+ values[i-1]);
					if(value.length() < 1) {
						System.out.println("dd");
						result[0] = "ERR_006";
						result[1] = prop.getProperty("col"+i+".name")+" is required value";
						break;
					}
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		}
		return result;
	}
	
	public static String[] validateCheck(String propName, String[] values) {
		String[] result = new String[2];
		PropManager propMgr = PropManager.getInstance();
		Properties prop = propMgr.getProp(propName);
		try {
			String checkList = prop.getProperty("validCheck.list");
			
			if(checkList.contains("NULL")) {
				result = nullCheck(propName, values);
				if(!"SUCC".equals(result[0])) {
					return result; 
				}
			}
			
			if(checkList.contains("LENGTH")) {
				result = lengthCheck(propName, values);
				if(!"SUCC".equals(result[0])) {
					return result;
				}
			}
			
			if(checkList.contains("BRAND_CD")) {
				result = isValidBrandCd(propName, values);
				if(!"SUCC".equals(result[0])) {
					return result;
				}
			}
			
			if(checkList.contains("CENTER_CD")) {
				result = isValidCenter(propName, values);
				if(!"SUCC".equals(result[0])) {
					return result;
				}
			}
			
			if(checkList.contains("DELIVERY_CD")) {
				result = isValidDeliveryCd(propName, values);
				if(!"SUCC".equals(result[0])) {
					return result;
				}
			}
						
			if(checkList.contains("DUPL")) {
				result = duplBrandNoCheck(propName, values);
				if(!"SUCC".equals(result[0])) {
					return result; 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerErr.error(e.getMessage());
		}
		return result;
	}
	
	public static boolean numberCheck(String str) {
		char charVal;
		if (str.trim().equals("")) return false;
		for (int i=0; i<str.length(); i++) {
			charVal = str.trim().charAt(i);
			if (charVal<48 || charVal>57) {
				return false;
			}
		}
		return true;
	}
	
	private static int getBytes(String str) {
		int byteSize = 0;
		for(int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			if(((0xAC00 <=c && c <= 0xD7A3) || (0x3131 <= c && c <= 0x318E))) {
				byteSize += 3;
			} else {
				byteSize += 1;
			}
		}
		return byteSize;
	}
}
