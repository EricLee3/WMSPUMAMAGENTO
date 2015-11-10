package com.wmsapi.servlet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.json.simple.ItemList;

public class TestAPIServelt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	
	public void doPost(HttpServletRequest resqust, HttpServletResponse respose) {
		
//		JSONObject recvJson =createE30Test();
//		JSONObject recvJson = createGetAvail();
		JSONObject recvJson = createOutboundTest();
//		JSONObject recvJson = createItemTest();
//		JSONObject recvJson = createCancelTest();
//		JSONObject recvJson = createChangeInfoTest();
//		JSONObject recvJson = createSendUpdInven();
		logger.debug("test start");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("msg", recvJson.toString()));
//		System.out.println("testApi respose:"+callAPI("http://localhost:8080/WMSAPI/recvApi", params));
//		logger.debug("test 1");
//		logger.debug("testApi respose:"+callAPI("https://wmsapi.isecommerce.co.kr:3133/recvApi", params));
		logger.debug("testApi respose:"+callAPI("http://wmsapi.itlinetest.com:3133/recvApi", params));
//		logger.debug("test 2");
//		logger.debug("testApi respose:"+callAPI("http://localhost:8080/WMSAPI/recvApi", params));
		
//		try {
//			respose.sendRedirect("http://localhost:8080/WMSAPI?msg="+recvJson.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void doGet(HttpServletRequest resqust, HttpServletResponse respose) {
		doPost(resqust, respose);
	}
	
	/*
	 * POST 방식으로 API 콜하는 메소드 
	 */
	private String callAPI(String url, List<NameValuePair> params ) {
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
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject createOutboundTest() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONArray recvBody = new JSONArray();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "recvOutbound");
		recvHeader.put("encType", "utf8");
		
		JSONArray recvOrder = new JSONArray();
		
//		String[] itemCdList = { "569480020120","187459050100"};
		
//		"187459040180","187459040160","569480020110","030175010140","187459040100","569480020100","030175010120","187459040170"
//		,"569480020120","187459050100","187459040180","187459040160","569480020110","030175010140","187459040100","569480020100","030175010120","187459040170"
		/*
		for(int i=0; i<itemCdList.length; i++) {
			logger.debug("itemCdList: "+itemCdList[i]);
			JSONObject recvRow1 = new JSONObject();
			recvRow1.put("centerCd", "WH001");
			recvRow1.put("brandCd", "1502");
			recvRow1.put("orderDate", "20150416");
			recvRow1.put("inoutCd", "D10");
			recvRow1.put("deliveryCd", "Z999");
			recvRow1.put("realDeliveryCd", "Z999");
			recvRow1.put("acperNm", "이동신");
			recvRow1.put("acperCd", "Z999");
			recvRow1.put("acperTel", "01022459234");
			recvRow1.put("acperHp", "01022459234");
			recvRow1.put("acperZipCd1", "135");
			recvRow1.put("acperZipCd2", "871");
			recvRow1.put("acperBasic", "경기 이천시 신둔면 인후리 2리190-1");
			recvRow1.put("acperDetail", "");
			recvRow1.put("ordNm", "이동신");
			recvRow1.put("ordCd", "Z999");
			recvRow1.put("ordTel", "01022459828");
			recvRow1.put("ordHp", "0102239292");
			recvRow1.put("ordZipCd1", "135");
			recvRow1.put("ordZipCd2", "871");
			recvRow1.put("ordBasic", "경기 이천시 신둔면 인후리 2리190-1");
			recvRow1.put("ordDetail", "");
			recvRow1.put("deliveryMsg", "");
			recvRow1.put("itemCd", itemCdList[i]);
			recvRow1.put("itemState", "A");
			recvRow1.put("inputQty", "1");
			recvRow1.put("orderQty", "1");
			recvRow1.put("brandDate", "20150416");
			recvRow1.put("brandNo", "99999999");
			recvRow1.put("brandLineNo", (i+1));
			recvRow1.put("indentDate", "");
			recvRow1.put("indentNo", "");
			recvRow1.put("firstIndentNo", "");
			recvRow1.put("serialNo", "");
			recvRow1.put("freeVal1", "");
			recvRow1.put("returnDiv", "");
			recvRow1.put("freeVal2", "JOSEPH&STACEY US");
			recvRow1.put("freeVal4", "");
			recvRow1.put("freeVal3D", "");
			recvRow1.put("freeVal3", "JNS02");
			recvRow1.put("freeVal6", "");
			recvRow1.put("freeVal8", "");
			recvRow1.put("domesticGubun", "");
			recvRow1.put("destCountry", "");
			recvRow1.put("destCity", "");
			recvRow1.put("destState", "");
			recvRow1.put("itemGroup", "");
			recvRow1.put("hscode", "");
			recvRow1.put("currency", "");
			recvRow1.put("supplyPrice", "");
			recvRow1.put("supplyAmt", "");
			recvRow1.put("totalAmt", "");
			recvRow1.put("outWeight", "");
			recvRow1.put("pickupYN", "");
			recvRow1.put("freeVal4D", "");
			recvRow1.put("freeVal1D", "");
			recvOrder.add(recvRow1);
		}
		*/
		
		JSONObject recvRow1 = new JSONObject();
		recvRow1.put("centerCd", "WH001");
		recvRow1.put("brandCd", "6101");
		recvRow1.put("orderDate", "20151108");
		recvRow1.put("inoutCd", "D10");
		recvRow1.put("deliveryCd", "Z999");
		recvRow1.put("realDeliveryCd", "Z999");
		recvRow1.put("acperNm", "Evg");
		recvRow1.put("acperCd", "Z999");
		recvRow1.put("acperTel", "010-4848-5671");
		recvRow1.put("acperHp", "010-4848-5671");
		recvRow1.put("acperZipCd1", "449");
		recvRow1.put("acperZipCd2", "815");
		recvRow1.put("acperBasic", "경기 용인시 처인구 포곡읍 전대리 341-10");
		recvRow1.put("acperDetail", "제일기숙사  203호");
		recvRow1.put("ordNm", "김덩인");
		recvRow1.put("ordCd", "Z999");
		recvRow1.put("ordTel", "010-4848-5671");
		recvRow1.put("ordHp", "010-4848-5671");
		recvRow1.put("ordZipCd1", "449");
		recvRow1.put("ordZipCd2", "815");
		recvRow1.put("ordBasic", "경기 용인시 처인구 포곡읍 전대리 341-10");
		recvRow1.put("ordDetail", "제일기숙사  203호");
		recvRow1.put("deliveryMsg", "");
		recvRow1.put("itemCd", "895376010130");
		recvRow1.put("itemState", "A");
		recvRow1.put("inputQty", "1");
		recvRow1.put("orderQty", "1");
		recvRow1.put("brandDate", "20151108");
		recvRow1.put("brandNo", "100032952");
		recvRow1.put("brandLineNo", "1");
		recvRow1.put("indentDate", "");
		recvRow1.put("indentNo", "");
		recvRow1.put("firstIndentNo", "");
		recvRow1.put("serialNo", "");
		recvRow1.put("freeVal1", "");
		recvRow1.put("returnDiv", "");
		recvRow1.put("freeVal2", "");
		recvRow1.put("freeVal4", "");
		recvRow1.put("freeVal3D", "");
		recvRow1.put("freeVal3", "");
		recvRow1.put("freeVal6", "");
		recvRow1.put("freeVal8", "");
		recvRow1.put("domesticGubun", "");
		recvRow1.put("destCountry", "");
		recvRow1.put("destCity", "");
		recvRow1.put("destState", "");
		recvRow1.put("itemGroup", "");
		recvRow1.put("hscode", "");
		recvRow1.put("currency", "");
		recvRow1.put("supplyPrice", "");
		recvRow1.put("supplyAmt", "");
		recvRow1.put("totalAmt", "");
		recvRow1.put("outWeight", "");
		recvRow1.put("pickupYN", "");
		recvRow1.put("freeVal4D", "");
		recvRow1.put("freeVal1D", "");
		recvOrder.add(recvRow1);
				
		JSONObject recvRow2 = new JSONObject();
		recvRow2.put("centerCd", "WH001");
		recvRow2.put("brandCd", "6101");
		recvRow2.put("orderDate", "20151108");
		recvRow2.put("inoutCd", "D10");
		recvRow2.put("deliveryCd", "Z999");
		recvRow2.put("realDeliveryCd", "Z999");
		recvRow2.put("acperNm", "Evg");
		recvRow2.put("acperCd", "Z999");
		recvRow2.put("acperTel", "010-4848-5671");
		recvRow2.put("acperHp", "010-4848-5671");
		recvRow2.put("acperZipCd1", "449");
		recvRow2.put("acperZipCd2", "815");
		recvRow2.put("acperBasic", "경기 용인시 처인구 포곡읍 전대리 341-10");
		recvRow2.put("acperDetail", "제일기숙사   203호");
		recvRow2.put("ordNm", "김덩인");
		recvRow2.put("ordCd", "Z999");
		recvRow2.put("ordTel", "010-4848-5671");
		recvRow2.put("ordHp", "010-4848-5671");
		recvRow2.put("ordZipCd1", "449");
		recvRow2.put("ordZipCd2", "815");
		recvRow2.put("ordBasic", "경기 용인시 처인구 포곡읍 전대리 341-10");
		recvRow2.put("ordDetail", "제일기숙사  203호");
		recvRow2.put("deliveryMsg", "");
		recvRow2.put("itemCd", "569412010130");
		recvRow2.put("itemState", "A");
		recvRow2.put("inputQty", "1");
		recvRow2.put("orderQty", "1");
		recvRow2.put("brandDate", "20151108");
		recvRow2.put("brandNo", "100032952");
		recvRow2.put("brandLineNo", "2");
		recvRow2.put("indentDate", "");
		recvRow2.put("indentNo", "");
		recvRow2.put("firstIndentNo", "");
		recvRow2.put("serialNo", "");
		recvRow2.put("freeVal1", "");
		recvRow2.put("returnDiv", "");
		recvRow2.put("freeVal2", "");
		recvRow2.put("freeVal4", "");
		recvRow2.put("freeVal3D", "");
		recvRow2.put("freeVal3", "");
		recvRow2.put("freeVal6", "");
		recvRow2.put("freeVal8", "");
		recvRow2.put("domesticGubun", "");
		recvRow2.put("destCountry", "");
		recvRow2.put("destCity", "");
		recvRow2.put("destState", "");
		recvRow2.put("itemGroup", "");
		recvRow2.put("hscode", "");
		recvRow2.put("currency", "");
		recvRow2.put("supplyPrice", "");
		recvRow2.put("supplyAmt", "");
		recvRow2.put("totalAmt", "");
		recvRow2.put("outWeight", "");
		recvRow2.put("pickupYN", "");
		recvRow2.put("freeVal4D", "");
		recvRow2.put("freeVal1D", "");
		recvOrder.add(recvRow2);
//		
//		recvBody.add(recvOrder);
		

		JSONObject recvRow3 = new JSONObject();
		recvRow3.put("centerCd", "WH001");
		recvRow3.put("brandCd", "6101");
		recvRow3.put("orderDate", "20151108");
		recvRow3.put("inoutCd", "D10");
		recvRow3.put("deliveryCd", "Z999");
		recvRow3.put("realDeliveryCd", "Z999");
		recvRow3.put("acperNm", "Evg");
		recvRow3.put("acperCd", "Z999");
		recvRow3.put("acperTel", "010-4848-5671");
		recvRow3.put("acperHp", "010-4848-5671");
		recvRow3.put("acperZipCd1", "449");
		recvRow3.put("acperZipCd2", "815");
		recvRow3.put("acperBasic", "경기 용인시 처인구 포곡읍 전대리 341-10");
		recvRow3.put("acperDetail", "제일기숙사  203호");
		recvRow3.put("ordNm", "김덩인");
		recvRow3.put("ordCd", "Z999");
		recvRow3.put("ordTel", "010-4848-5671");
		recvRow3.put("ordHp", "010-4848-5671");
		recvRow3.put("ordZipCd1", "449");
		recvRow3.put("ordZipCd2", "815");
		recvRow3.put("ordBasic", "경기 용인시 처인구 포곡읍 전대리 341-10");
		recvRow3.put("ordDetail", "제일기숙사 203호");
		recvRow3.put("deliveryMsg", "");
		recvRow3.put("itemCd", "352634050180");
		recvRow3.put("itemState", "A");
		recvRow3.put("inputQty", "1");
		recvRow3.put("orderQty", "1");
		recvRow3.put("brandDate", "20151108");
		recvRow3.put("brandNo", "100032952");
		recvRow3.put("brandLineNo", "3");
		recvRow3.put("indentDate", "");
		recvRow3.put("indentNo", "");
		recvRow3.put("firstIndentNo", "");
		recvRow3.put("serialNo", "");
		recvRow3.put("freeVal1", "");
		recvRow3.put("returnDiv", "");
		recvRow3.put("freeVal2", "");
		recvRow3.put("freeVal4", "");
		recvRow3.put("freeVal3D", "");
		recvRow3.put("freeVal3", "");
		recvRow3.put("freeVal6", "");
		recvRow3.put("freeVal8", "");
		recvRow3.put("domesticGubun", "");
		recvRow3.put("destCountry", "");
		recvRow3.put("destCity", "");
		recvRow3.put("destState", "");
		recvRow3.put("itemGroup", "");
		recvRow3.put("hscode", "");
		recvRow3.put("currency", "");
		recvRow3.put("supplyPrice", "");
		recvRow3.put("supplyAmt", "");
		recvRow3.put("totalAmt", "");
		recvRow3.put("outWeight", "");
		recvRow3.put("pickupYN", "");
		recvRow3.put("freeVal4D", "");
		recvRow3.put("freeVal1D", "");

		recvOrder.add(recvRow3);
		recvBody.add(recvOrder);
		
		recvJson.put("header", recvHeader);
		recvJson.put("body", recvBody);
		
		return recvJson;
	}
	
	private JSONObject createItemTest() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONArray recvBody = new JSONArray();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "recvItem");
		recvHeader.put("encType", "utf8");
		
		
		JSONObject recvRow1 = new JSONObject();
		recvRow1.put("brandCd", "6102");
		recvRow1.put("itemCd", "6101TEST");
		recvRow1.put("itemNm", "테스트 상품");
		recvRow1.put("itemBarCd", "6101TESTBARCD");
		recvRow1.put("itemColor", "WINE");
		recvRow1.put("itemSize", "X");
		recvRow1.put("factCd", "GOLF");
		recvRow1.put("openDate", "");
		recvRow1.put("itemBrandCd", "");
		recvRow1.put("itemBrandNm", "");
		recvRow1.put("supplyPrice", "99000");
		recvRow1.put("salePrice", "99000");
		recvRow1.put("itemStyle", "TESTSTYLE");
		recvRow1.put("years", "2015");
		recvRow1.put("seasons", "11");
		recvRow1.put("mansDiv", "Man");
		recvRow1.put("countryNm", "");
		recvRow1.put("material1", "");
		recvRow1.put("material2", "");
		recvRow1.put("departCd", "");
		recvRow1.put("lineCd", "");
		recvRow1.put("classCd", "");
		recvRow1.put("freeVal1", "A");
		recvRow1.put("freeVal2", "");
		recvRow1.put("freeVal4", "");
		recvBody.add(recvRow1);
		
		JSONObject recvRow2 = new JSONObject();
		recvRow2.put("brandCd", "6101");
		recvRow2.put("itemCd", "6101TEST1");
		recvRow2.put("itemNm", "털어버리자");
		recvRow2.put("itemBarCd", "6101TESTBARCD1");
		recvRow2.put("itemColor", "RED");
		recvRow2.put("itemSize", "XL");
		recvRow2.put("factCd", "GOLF");
		recvRow2.put("openDate", "");
		recvRow2.put("itemBrandCd", "");
		recvRow2.put("itemBrandNm", "");
		recvRow2.put("supplyPrice", "99000");
		recvRow2.put("salePrice", "99000");
		recvRow2.put("itemStyle", "TESTSTYLE1");
		recvRow2.put("years", "2015");
		recvRow2.put("seasons", "11");
		recvRow2.put("mansDiv", "Woman");
		recvRow2.put("countryNm", "");
		recvRow2.put("material1", "");
		recvRow2.put("material2", "");
		recvRow2.put("departCd", "");
		recvRow2.put("lineCd", "");
		recvRow2.put("classCd", "");
		recvRow2.put("freeVal1", "A");
		recvRow2.put("freeVal2", "");
		recvRow2.put("freeVal4", "");
		recvBody.add(recvRow2);
		
		recvJson.put("header", recvHeader);
		recvJson.put("body", recvBody);
		
		return recvJson;
	}
	
	private JSONObject createE30Test() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONArray recvBody = new JSONArray();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "recvOutbound");
		recvHeader.put("encType", "utf8");
		
		JSONArray recvOrder = new JSONArray();
		JSONObject recvRow1 = new JSONObject();
		recvRow1.put("centerCd", "WH001");
		recvRow1.put("brandCd", "6101");
		recvRow1.put("orderDate", "20150309");
		recvRow1.put("inoutCd", "E30");
		recvRow1.put("deliveryCd", "Z999");
		recvRow1.put("realDeliveryCd", "Z999");
		recvRow1.put("acperNm", "박정화");
		recvRow1.put("acperCd", "Z999");
		recvRow1.put("acperTel", "01022462042");
		recvRow1.put("acperHp", "01022462042");
		recvRow1.put("acperZipCd1", "135");
		recvRow1.put("acperZipCd2", "871");
		recvRow1.put("acperBasic", "서울특별시 강남구 삼성동 78");
		recvRow1.put("acperDetail", "삼안빌딩 7층");
		recvRow1.put("ordNm", "박정화");
		recvRow1.put("ordCd", "Z999");
		recvRow1.put("ordTel", "01022459828");
		recvRow1.put("ordHp", "0102239292");
		recvRow1.put("ordZipCd1", "135");
		recvRow1.put("ordZipCd2", "871");
		recvRow1.put("ordBasic", "서울특별시 강남구 삼성동 78");
		recvRow1.put("ordDetail", "삼안빌딩 7층");
		recvRow1.put("deliveryMsg", "");
		recvRow1.put("itemCd", "513403010110");
		recvRow1.put("itemState", "A");
		recvRow1.put("inputQty", "2");
		recvRow1.put("orderQty", "2");
		recvRow1.put("brandDate", "20150406");
		recvRow1.put("brandNo", "3456998871");
		recvRow1.put("brandLineNo", "000010");
		recvRow1.put("indentDate", "");
		recvRow1.put("indentNo", "");
		recvRow1.put("firstIndentNo", "");
		recvRow1.put("serialNo", "");
		recvRow1.put("freeVal1", "");
		recvRow1.put("returnDiv", "");
		recvRow1.put("freeVal2", "");
		recvRow1.put("freeVal4", "");
		recvRow1.put("freeVal3D", "");
		recvRow1.put("freeVal3", "");
		recvRow1.put("freeVal6", "");
		recvRow1.put("freeVal8", "");
		recvRow1.put("domesticGubun", "");
		recvRow1.put("destCountry", "");
		recvRow1.put("destCity", "");
		recvRow1.put("destState", "");
		recvRow1.put("itemGroup", "");
		recvRow1.put("hscode", "");
		recvRow1.put("currency", "");
		recvRow1.put("supplyPrice", "");
		recvRow1.put("supplyAmt", "");
		recvRow1.put("totalAmt", "");
		recvRow1.put("outWeight", "");
		recvRow1.put("pickupYN", "");
		recvRow1.put("freeVal4D", "");
		recvRow1.put("freeVal1D", "");
		recvOrder.add(recvRow1);
		
		JSONObject recvRow2 = new JSONObject();
		recvRow2.put("centerCd", "WH001");
		recvRow2.put("brandCd", "6101");
		recvRow2.put("orderDate", "20150309");
		recvRow2.put("inoutCd", "E30");
		recvRow2.put("deliveryCd", "Z999");
		recvRow2.put("realDeliveryCd", "Z999");
		recvRow2.put("acperNm", "박정화");
		recvRow2.put("acperCd", "Z999");
		recvRow2.put("acperTel", "01022462042");
		recvRow2.put("acperHp", "01022462042");
		recvRow2.put("acperZipCd1", "135");
		recvRow2.put("acperZipCd2", "871");
		recvRow2.put("acperBasic", "서울특별시 강남구 삼성동 78");
		recvRow2.put("acperDetail", "삼안빌딩 7층");
		recvRow2.put("ordNm", "박정화");
		recvRow2.put("ordCd", "Z999");
		recvRow2.put("ordTel", "01022459828");
		recvRow2.put("ordHp", "0102239292");
		recvRow2.put("ordZipCd1", "135");
		recvRow2.put("ordZipCd2", "871");
		recvRow2.put("ordBasic", "서울특별시 강남구 삼성동 78");
		recvRow2.put("ordDetail", "삼안빌딩 7층");
		recvRow2.put("deliveryMsg", "");
		recvRow2.put("itemCd", "513403010120");
		recvRow2.put("itemState", "A");
		recvRow2.put("inputQty", "1");
		recvRow2.put("orderQty", "1");
		recvRow2.put("brandDate", "20150406");
		recvRow2.put("brandNo", "3456998871");
		recvRow2.put("brandLineNo", "000020");
		recvRow2.put("indentDate", "");
		recvRow2.put("indentNo", "");
		recvRow2.put("firstIndentNo", "");
		recvRow2.put("serialNo", "");
		recvRow2.put("freeVal1", "");
		recvRow2.put("returnDiv", "");
		recvRow2.put("freeVal2", "");
		recvRow2.put("freeVal4", "");
		recvRow2.put("freeVal3D", "");
		recvRow2.put("freeVal3", "");
		recvRow2.put("freeVal6", "");
		recvRow2.put("freeVal8", "");
		recvRow2.put("domesticGubun", "");
		recvRow2.put("destCountry", "");
		recvRow2.put("destCity", "");
		recvRow2.put("destState", "");
		recvRow2.put("itemGroup", "");
		recvRow2.put("hscode", "");
		recvRow2.put("currency", "");
		recvRow2.put("supplyPrice", "");
		recvRow2.put("supplyAmt", "");
		recvRow2.put("totalAmt", "");
		recvRow2.put("outWeight", "");
		recvRow2.put("pickupYN", "");
		recvRow2.put("freeVal4D", "");
		recvRow2.put("freeVal1D", "");
		recvOrder.add(recvRow2);
		
		recvBody.add(recvOrder);
		
		recvJson.put("header", recvHeader);
		recvJson.put("body", recvBody);
		
		return recvJson;
	}
	
	private JSONObject createGetAvail() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONObject recvBody = new JSONObject();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "getAvailQty");
		recvHeader.put("encType", "utf8");
		
		recvBody.put("centerCd", "WH001");
		recvBody.put("brandCd", "6101");
		recvBody.put("itemCd", "300203370120");
		recvBody.put("itemState", "A");
		
		recvJson.put("header" , recvHeader);
		recvJson.put("body" , recvBody);
		
		return recvJson;
	}
	
	private JSONObject createCancelTest() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONObject recvBody = new JSONObject();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "recvCancel");
		recvHeader.put("encType", "utf8");
		
		recvBody.put("centerCd", "WH001");
		recvBody.put("brandCd", "6101");
		recvBody.put("brandDate", "20150424");
		recvBody.put("brandNo", "100000508");
		recvBody.put("cancelDiv", "2");
		
		recvJson.put("header" , recvHeader);
		recvJson.put("body" , recvBody);
		
		return recvJson;
	}
	
	private JSONObject createChangeInfoTest() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONObject recvBody = new JSONObject();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "recvChangeInfo");
		recvHeader.put("encType", "utf8");
		
		recvBody.put("centerCd", "WH001");
		recvBody.put("brandCd", "6101");
		recvBody.put("orderDate", "20150416");
		recvBody.put("brandNo", "99999999");
		recvBody.put("acperNm", "문채원");
		recvBody.put("acperTel", "0264875478");
		recvBody.put("acperHp", "01022457898");
		recvBody.put("acperBasic", "경상북도 경주시 호법면 매장 118-1");
		recvBody.put("acperDetail", "덕평센터");
		recvBody.put("acperZipCd1", "133");
		recvBody.put("acperZipCd2", "232");
		
		recvJson.put("header" , recvHeader);
		recvJson.put("body" , recvBody);
		
		return recvJson;
	}
	
	private JSONObject createSendUpdInven() {
		JSONObject recvJson = new JSONObject();
		JSONObject recvHeader = new JSONObject();
		JSONObject recvBody = new JSONObject();
		
		recvHeader.put("bizUserId", "pumatest");
		recvHeader.put("bizUserPw", "!pumatest66t");
		recvHeader.put("callId", "sendUpdInventory");
		recvHeader.put("encType", "utf8");
		
		recvBody.put("centerCd", "A1");
		recvBody.put("brandCd", "6101");
		recvJson.put("header" , recvHeader);
		recvJson.put("body" , recvBody);
		
		return recvJson;
	}
}
