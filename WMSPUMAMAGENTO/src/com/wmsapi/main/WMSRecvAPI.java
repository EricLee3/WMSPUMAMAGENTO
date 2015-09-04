package com.wmsapi.main;

public class WMSRecvAPI {
//	private static Logger logger  = Logger.getLogger("process.log");
//	private static Logger loggerErr = Logger.getLogger("process.err");
//	
//	public static void main(String args[]) {
//		if(args[0] == null) {
//			System.out.println("매개변수 미입력 에러!");
//			logger.info("매개변수 미입력 에러!");
//			return;
//		}
//		
//		WMSRecvAPI recvApi = new WMSRecvAPI();
//		if("recvOutbound".equals(args[0])) {
//			recvApi.callProcOutbound();
//		} else if("recvItem".equals(args[0])) {
//			recvApi.callProcItem();
//		}
////		} else if("recvCancel".equals(args[0])) {
////			recvApi.callProcCancel();
////		}
//	}
//	
//	private void callProcOutbound() {
//		PropManager propMgr = PropManager.getInstance();
//		Properties prop = propMgr.getProp("fileInfo");
//		BufferedReader bufferedReader = null;
//		StringBuilder sb = new StringBuilder();
//		try {
//			String[] files = new File(prop.getProperty("filePath")).list();
//			for(int i=0; i<files.length; i++) {
//				if(!files[i].contains("outbound")) {
//					continue;
//				}
//				
//				bufferedReader = new BufferedReader(new FileReader(prop.getProperty("filePath")+"/"+files[i]));
//				String line = "";
//				while((line = bufferedReader.readLine())!=null) {
//					sb.append(line);
//				}
//				
//				System.out.println(sb.toString());
//				logger.debug(sb.toString());
//				
//				String[] brandList = sb.toString().split("\\$otherBrandNo\\$");
//				System.out.println("brandList.length:" + brandList.length);
//				logger.debug("brandList.length:" + brandList.length);
//				
//				for(int j=0; j<brandList.length; j++) {
//					ArrayList<DTORecvOutbound> outboundList = new ArrayList<DTORecvOutbound>();
//					DAORecvAPI daoRecv = DAORecvAPI.getInstance();
//					String[] rows = brandList[j].split("\\$row\\$");
//					System.out.println("rows.length: "+rows.length);
//					logger.debug("rows.length: "+rows.length);
//					for(int k=0; k<rows.length; k++) {
//						System.out.println("rows: "+ rows[k]);
//						String[] value =  rows[k].split(",");
//						DTORecvOutbound dtoOutbound = new DTORecvOutbound();
//						dtoOutbound.setCenterCd(StringUtils.replaceNullStr(value[0]));
//						dtoOutbound.setBrandCd(StringUtils.replaceNullStr(value[1]));
//						dtoOutbound.setOrderDate(StringUtils.replaceNullStr(value[2]));
//						dtoOutbound.setInoutCd(StringUtils.replaceNullStr(value[3]));
//						dtoOutbound.setDeliveryCd(StringUtils.replaceNullStr(value[4]));
//						dtoOutbound.setRealDeliveryCd(StringUtils.replaceNullStr(value[5]));
//						dtoOutbound.setAcperNm(StringUtils.replaceNullStr(value[6]));
//						dtoOutbound.setAcperCd(StringUtils.replaceNullStr(value[7]));
//						dtoOutbound.setAcperTel(StringUtils.replaceNullStr(value[8]));
//						dtoOutbound.setAcperHp(StringUtils.replaceNullStr(value[9]));						
//						dtoOutbound.setAcperZipCd1(StringUtils.replaceNullStr(value[10]));
//						dtoOutbound.setAcperZipCd2(StringUtils.replaceNullStr(value[11]));
//						dtoOutbound.setAcperBasic(StringUtils.replaceNullStr(value[12]));
//						dtoOutbound.setAcperDetail(StringUtils.replaceNullStr(value[13]));
//						dtoOutbound.setOrdNm(StringUtils.replaceNullStr(value[14]));
//						dtoOutbound.setOrdCd(StringUtils.replaceNullStr(value[15]));
//						dtoOutbound.setOrdTel(StringUtils.replaceNullStr(value[16]));
//						dtoOutbound.setOrdHp(StringUtils.replaceNullStr(value[17]));
//						dtoOutbound.setOrdZipCd1(StringUtils.replaceNullStr(value[18]));
//						dtoOutbound.setOrdZipCd2(StringUtils.replaceNullStr(value[19]));
//						dtoOutbound.setOrdBasic(StringUtils.replaceNullStr(value[20]));
//						dtoOutbound.setOrdDetail(StringUtils.replaceNullStr(value[21]));
//						dtoOutbound.setDeliveryMsg(StringUtils.replaceNullStr(value[22]));
//						dtoOutbound.setItemCd(StringUtils.replaceNullStr(value[23]));
//						dtoOutbound.setItemState(StringUtils.replaceNullStr(value[24]));
//						dtoOutbound.setInputQty(StringUtils.replaceNullStr(value[25]));
//						dtoOutbound.setOrderQty(StringUtils.replaceNullStr(value[26]));
//						dtoOutbound.setBrandDate(StringUtils.replaceNullStr(value[27]));
//						dtoOutbound.setBrandNo(StringUtils.replaceNullStr(value[28]));
//						dtoOutbound.setBrandLineNo(StringUtils.replaceNullStr(value[29]));
//						dtoOutbound.setIndentDate(StringUtils.replaceNullStr(value[30]));
//						dtoOutbound.setIndentNo(StringUtils.replaceNullStr(value[31]));
//						dtoOutbound.setFirstIndent(StringUtils.replaceNullStr(value[32]));
//						dtoOutbound.setSerialNo(StringUtils.replaceNullStr(value[33]));
//						dtoOutbound.setFreeVal1(StringUtils.replaceNullStr(value[34]));
//						dtoOutbound.setReturnDiv(StringUtils.replaceNullStr(value[35]));
//						dtoOutbound.setFreeVal2(StringUtils.replaceNullStr(value[36]));
//						dtoOutbound.setFreeVal4(StringUtils.replaceNullStr(value[37]));
//						dtoOutbound.setFreeVal3D(StringUtils.replaceNullStr(value[38]));
//						dtoOutbound.setFreeVal3(StringUtils.replaceNullStr(value[39]));
//						dtoOutbound.setFreeVal6(StringUtils.replaceNullStr(value[40]));
//						dtoOutbound.setFreeVal8(StringUtils.replaceNullStr(value[41]));
//						dtoOutbound.setDomesticGubun(StringUtils.replaceNullStr(value[42]));
//						dtoOutbound.setDestCountry(StringUtils.replaceNullStr(value[43]));
//						dtoOutbound.setDestCity(StringUtils.replaceNullStr(value[44]));
//						dtoOutbound.setDestState(StringUtils.replaceNullStr(value[45]));
//						dtoOutbound.setItemGroup(StringUtils.replaceNullStr(value[46]));
//						dtoOutbound.setHscode(StringUtils.replaceNullStr(value[47]));
//						dtoOutbound.setCurrency(StringUtils.replaceNullStr(value[48]));
//						dtoOutbound.setSupplyPrice(StringUtils.replaceNullStr(value[49]));
//						dtoOutbound.setSupplyAmt(StringUtils.replaceNullStr(value[50]));
//						dtoOutbound.setTotalAmt(StringUtils.replaceNullStr(value[51]));
//						dtoOutbound.setOutWeight(StringUtils.replaceNullStr(value[52]));
//						dtoOutbound.setPickupYN(StringUtils.replaceNullStr(value[53]));
//						dtoOutbound.setFreeVal4D(StringUtils.replaceNullStr(value[54]));
//						dtoOutbound.setFreeVal1D(StringUtils.replaceNullStr(value[55]));
//						
//						outboundList.add(dtoOutbound);
//					}
//					String result = daoRecv.callProcOutbound(outboundList);
//					if("S".equals(result)) {
//						System.out.println("백업!");
//						backupFile(files[i]);
//					} else {
//						System.out.println("에러!");
//						writeErrorFile(rows, rows[0].split(",")[2]+"_"+rows[0].split(",")[0]+rows[0].split(",")[1]
//				               +rows[0].split(",")[28]);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			loggerErr.error(e.getMessage());
//		}
//	}
//	
//	private void callProcItem() {
//		PropManager propMgr = PropManager.getInstance();
//		Properties prop = propMgr.getProp("fileInfo");
//		BufferedReader bufferedReader = null;
//		StringBuilder sb = new StringBuilder();
//		try {
//			String[] files = new File(prop.getProperty("filePath")).list();
//			for(int i=0; i<files.length; i++) {
//				if(!files[i].contains("item")) {
//					continue;
//				}
//				
//				bufferedReader = new BufferedReader(new FileReader(prop.getProperty("filePath")+"/"+files[i]));
//				String line = "";
//				while((line = bufferedReader.readLine())!=null) {
//					sb.append(line);
//				}
//				
//				System.out.println(sb.toString());
//				logger.debug(sb.toString());
//				
//				String[] brandList = sb.toString().split("\\$otherBrandNo\\$");
//				System.out.println("brandList.length:" + brandList.length);
//				logger.debug("brandList.length:" + brandList.length);
//				
//				for(int j=0; j<brandList.length; j++) {
//					ArrayList<DTORecvItem> itemList = new ArrayList<DTORecvItem>();
//					DAORecvAPI daoRecv = DAORecvAPI.getInstance();
//					String[] rows = brandList[j].split("\\$row\\$");
//					System.out.println("rows.length: "+rows.length);
//					logger.debug("rows.length: "+rows.length);
//					for(int k=0; k<rows.length; k++) {
//						System.out.println("rows: "+ rows[k]);
//						String[] value =  rows[k].split(",");
//						DTORecvItem dtoItem = new DTORecvItem();
//						dtoItem.setBrandCd(StringUtils.replaceNullStr(value[0]));
//						dtoItem.setItemCd(StringUtils.replaceNullStr(value[1]));
//						dtoItem.setItemNm(StringUtils.replaceNullStr(value[2]));
//						dtoItem.setItemBarCd(StringUtils.replaceNullStr(value[3]));
//						dtoItem.setItemColor(StringUtils.replaceNullStr(value[4]));
//						dtoItem.setItemSize(StringUtils.replaceNullStr(value[5]));
//						dtoItem.setFactCd(StringUtils.replaceNullStr(value[6]));
//						dtoItem.setOpenDate(StringUtils.replaceNullStr(value[7]));
//						dtoItem.setItemBrandCd(StringUtils.replaceNullStr(value[8]));
//						dtoItem.setItemBrandNm(StringUtils.replaceNullStr(value[9]));
//						dtoItem.setSupplyPrice(StringUtils.replaceNullStr(value[10]));
//						dtoItem.setSalePrice(StringUtils.replaceNullStr(value[11]));
//						dtoItem.setItemStyle(StringUtils.replaceNullStr(value[12]));
//						dtoItem.setYears(StringUtils.replaceNullStr(value[13]));
//						dtoItem.setSeasons(StringUtils.replaceNullStr(value[14]));
//						dtoItem.setMansDiv(StringUtils.replaceNullStr(value[15]));
//						dtoItem.setCountryNm(StringUtils.replaceNullStr(value[16]));
//						dtoItem.setMaterial1(StringUtils.replaceNullStr(value[17]));
//						dtoItem.setMaterial2(StringUtils.replaceNullStr(value[18]));
//						dtoItem.setDepartCd(StringUtils.replaceNullStr(value[19]));
//						dtoItem.setLineCd(StringUtils.replaceNullStr(value[20]));
//						dtoItem.setClassCd(StringUtils.replaceNullStr(value[21]));
//						dtoItem.setFreeVal1(StringUtils.replaceNullStr(value[22]));
//						dtoItem.setFreeVal2(StringUtils.replaceNullStr(value[23]));
//						dtoItem.setFreeVal4(StringUtils.replaceNullStr(value[24]));
//						
//						itemList.add(dtoItem);
//					}
//					String result = daoRecv.callProcItem(itemList);
//					if("S".equals(result)) {
//						System.out.println("백업!");
//						backupFile(files[i]);
//					} else {
//						System.out.println("에러!");
//						writeErrorFile(rows, rows[0].split(",")[0]+"_"+rows[0].split(",")[1]);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			loggerErr.error(e.getMessage());
//		}
//	}
//	
////	private void callProcCancel() {
////		PropManager propMgr = PropManager.getInstance();
////		Properties prop = propMgr.getProp("fileInfo");
////		BufferedReader bufferedReader = null;
////		StringBuilder sb = new StringBuilder();
////		try {
////			String[] files = new File(prop.getProperty("filePath")).list();
////			
////			for(int i=0; i<files.length; i++) {
////				if(!files[i].contains("cancel")) {
////					continue;
////				}
////				
////				bufferedReader = new BufferedReader(new FileReader(prop.getProperty("filePath")+"/"+files[i]));
////				String line = "";
////				while((line = bufferedReader.readLine())!=null) {
////					sb.append(line);
////				}
////				
////				String[] brandList = sb.toString().split("*otherBrandNo*");
////				for(int j=0; j<brandList.length; j++) {
////					ArrayList<DTORecvCancel> cancelList = new ArrayList<DTORecvCancel>();
////					DAORecvAPI daoRecv = DAORecvAPI.getInstance();
////					String[] rows = brandList[j].split("*row*");
////					for(int k=0; k<rows.length; k++) {
////						String[] value =  rows[k].split(",");
////						DTORecvCancel dtoCancel = new DTORecvCancel();
////						dtoCancel.setBrandCd(value[0]);
////						dtoCancel.setCenterCd(value[1]);
////						dtoCancel.setBrandDate(value[2]);
////						dtoCancel.setBrandNo(value[3]);
////						dtoCancel.setCancelDiv(value[4]);
////						
////						cancelList.add(dtoCancel);
////					}
////					String result = daoRecv.callProcCancel(cancelList);
////					if("S".equals(result)) {
////						backupFile(files[i]);
////					}
////				}
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
//	
//	private void backupFile(String fileName) {
//		FileInputStream fis = null;
//		FileOutputStream fos = null;
//		File file = null;
//		
//		byte[] buffer = new byte[1024];
//		PropManager propMng = PropManager.getInstance();
//		Properties prop = propMng.getProp("fileInfo");
//		
//		
//		try {
//			file = new File(prop.getProperty("filePath")+fileName);
//			fis = new FileInputStream(file);
//			fos = new FileOutputStream(prop.getProperty("backupPath")+fileName);
//			int line=-1;
//			while((line=fis.read(buffer))>0) {
//				fos.write(buffer, 0 , line);
//			}
//			logger.info("backup success ["+fileName+"]");
//			
//			file.delete();
//			logger.info("original file deleted ["+fileName+"]");
//			
//		} catch (Exception e) {
//			loggerErr.error(e.getMessage());
//		} finally {
//			try {
//				if(fis!=null) {
//					fis.close();
//				}
//				if(fos!=null) {
//					fos.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				loggerErr.error(e.getMessage());
//			}
//		}
//	}
//	
//	private void writeErrorFile(String[] rows, String fileName) {
//		logger.info("writeErrorFile start !");
//		StringBuilder sb = new StringBuilder();
//		BufferedWriter writer = null;
//		PropManager propMng = PropManager.getInstance();
//		Properties prop = propMng.getProp("fileInfo");
//		
//		try {
//			for(int i=0; i<rows.length; i++) {
//				if(i>0) {
//					sb.append("$row$");
//				}
//				sb.append(rows[i]);
//				
//				if(i>=(rows.length-1)) {
//					sb.append("$otherBrandNo$");
//				}
//			}
//			
//			writer = new BufferedWriter(new FileWriter(prop.getProperty("errorPath")+fileName+"_error.txt"));
//			writer.write(sb.toString());
//			logger.info("errorFile created!");
//		} catch (IOException e) {
//			e.printStackTrace();
//			loggerErr.error(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			loggerErr.error(e.getMessage());
//		} finally {
//			try {
//				if(writer!=null) {
//					writer.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(e.getMessage());
//			}
//		}
//		logger.info("writeErrorFile end !");
//	}
}