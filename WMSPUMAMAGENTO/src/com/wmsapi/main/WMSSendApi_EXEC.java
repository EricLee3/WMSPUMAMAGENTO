package com.wmsapi.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.wmsapi.task.TaskInventory;
import com.wmsapi.task.TaskOutbound;
import com.wmsapi.task.TaskSendExpressDaily;
import com.wmsapi.task.TaskSendExpressTrace;

public class WMSSendApi_EXEC {
	private static Logger logger  = Logger.getLogger("process.log");
	public static void main(String args[]) {
		logger.info("start!!");
		Timer t1 = new Timer(false);
		Calendar cal = Calendar.getInstance();
		if("sendOutbound".equals(args[0])) {
			t1.schedule(new TaskOutbound(), 1000);
		} else if("sendInventory".equals(args[0])) {
			t1.schedule(new TaskInventory(), 2000);
		} else if("sendExpressTrace".equals(args[0])) {
			t1.schedule(new TaskSendExpressTrace(),1000);
		}  else if("sendExpressTraceDaily".equals(args[0])) {
			t1.schedule(new TaskSendExpressDaily(),1000);
			
		}else {
			logger.info("잘못된 매개변수 에러=>"+args[0]);
		}
		logger.info("end!!");
	}
	
	
	
}
