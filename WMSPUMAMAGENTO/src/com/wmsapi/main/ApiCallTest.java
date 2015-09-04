package com.wmsapi.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class ApiCallTest extends TimerTask{
//	private static Logger logger  = Logger.getLogger("process.log");
	
	
	public void run() {
		start();
	}
	
	private void start(){
		
		Calendar cal = Calendar.getInstance();
		System.out.println("start :" + cal.get(Calendar.SECOND));
//		logger.info("start time");
	}
	
	public void callOK_sendInventory(){
		System.out.println("comm in sendInventory");
	}
	
	public void callOK_sendExpressTrace(){
		System.out.println("comm in sendExpressTrace");
	}
	
	public void callOK_sendExpressTraceDaily(){
		System.out.println("comm in  sendExpressTraceDaily");
	}
	
	public static void main(String args[]){
		
		Calendar cal = Calendar.getInstance();
//		cal.set(2015,Calendar.OCTOBER,26);
		cal.set(2015, 8, 26, 17, 10, 0);
		
		
		String from = "2015-08-26 17:30:10";

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date to = transFormat.parse(from);
			System.out.println(to);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		cal.set(Calendar.HOUR_OF_DAY, 15);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
		
		

		
	}
	
}
