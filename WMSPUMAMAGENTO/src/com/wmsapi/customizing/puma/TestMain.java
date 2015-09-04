package com.wmsapi.customizing.puma;

import java.util.Calendar;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.wmsapi.task.TaskInventory;
import com.wmsapi.task.TaskOutbound;

public class TestMain {
	private static Logger logger  = Logger.getLogger("process.log");
	public static void main(String args[]) {
		logger.info("start!!");
		Calendar cal = Calendar.getInstance();
		Timer t1 = new Timer(false);
//		t1.schedule(new TaskOutbound(), new Date(cal.getTimeInMillis()),24*60*60*1000);
		cal.set(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		t1.schedule(new TestTask(), cal.getTime(), 86400000);
		logger.info("end!!");
	}

}
