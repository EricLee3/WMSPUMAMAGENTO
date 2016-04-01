package com.wmsapi.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.wmsapi.task.TaskInventory;
import com.wmsapi.task.TaskOutbound;
import com.wmsapi.task.TaskSendExpressDaily;
import com.wmsapi.task.TaskSendExpressTrace;

public class WMSSendApi {
	private static Logger logger  = Logger.getLogger("process.log");
	private static Logger loggerErr = Logger.getLogger("process.err");
	public static void main(String args[]) {
		logger.info("start!!");
		Timer t1 = new Timer(false);
		String startDtFoInventory = "2015-09-17 01:00:00";
		String startDtForOutnound = "2015-09-16 20:30:00";
		String startExpressDailyAll ="2015-09-17 04:00:00";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			Date startInvenDt = transFormat.parse(startDtFoInventory);
			Date startOutboundDt = transFormat.parse(startDtForOutnound);
			Date startExpressDt = transFormat.parse(startExpressDailyAll);
			
			if("sendOutbound".equals(args[0])) {
				t1.schedule(new TaskOutbound(), startOutboundDt, 86400000);
			} else if("sendInventory".equals(args[0])) {
				t1.schedule(new TaskInventory(), startInvenDt, 86400000);
			} else if("sendExpressTrace".equals(args[0])) {
				int repTime = (1000*60*60);
				t1.schedule(new TaskSendExpressTrace(), 0, repTime);
			}  else if("sendExpressTraceDaily".equals(args[0])) {
				t1.schedule(new TaskSendExpressDaily(), startExpressDt, 86400000);
			} else {
				logger.info("잘못된 매개변수 에러 =>"+args[0]);
			}
		}catch(ParseException e){
			loggerErr.error(e.getMessage());
		}
		
		
/*		Calendar cal = Calendar.getInstance();	
 * 		if("sendOutbound".equals(args[0])) {
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t1.schedule(new TaskOutbound(), cal.getTime(), 86400000);
		} else if("sendInventory".equals(args[0])) {
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t1.schedule(new TaskInventory(), cal.getTime(), 86400000);
//			t1.schedule(new TaskInventory(), cal.getTime(), 120000);
		} else if("sendExpressTrace".equals(args[0])) {
			int repTime = (1000*60*60);
			t1.schedule(new TaskSendExpressTrace(), 0, repTime);
		}  else if("sendExpressTraceDaily".equals(args[0])) {
			cal.set(Calendar.HOUR_OF_DAY, 4);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t1.schedule(new TaskSendExpressDaily(), cal.getTime(), 86400000);
		} else {
			logger.info("�߸��� �Ű����� ����=>"+args[0]);
		}*/
		logger.info("end!!");
	}
}
