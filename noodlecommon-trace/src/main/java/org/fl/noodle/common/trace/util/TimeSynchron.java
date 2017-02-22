package org.fl.noodle.common.trace.util;

import org.fl.noodle.common.net.http.HttpConnect;
import org.fl.noodle.common.net.http.jdk.HttpConnectJdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class TimeSynchron implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(TimeSynchron.class);
	
	private String timeSvrUrl;
	private long interval = 10000;
	
	private static String timeSvrUrlStc;
	private static long timeDiff = 0;
	private static Thread timeHeartbeatThread;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (timeSvrUrl != null && timeSvrUrl.startsWith("http://") && timeHeartbeatThread == null) {
			timeSvrUrlStc = timeSvrUrl;
			timeHeartbeatThread = new Thread(new TimeHeartbeat());
			timeHeartbeatThread.setDaemon(true);
			timeHeartbeatThread.setName("TimeHeartbeatThread");
			timeHeartbeatThread.start();
		}
	}
	
	public static long currentTimeMillis() {
		return System.currentTimeMillis() + timeDiff;
	}
	
	class TimeHeartbeat implements Runnable {

		@Override
		public void run() {
			
			while (true) {
				
				if (timeSvrUrlStc != null) {
					HttpConnect httpConnect = new HttpConnectJdk(timeSvrUrlStc, 100, 100);
					String timeStr = null;
					try {
						timeStr = httpConnect.getString("", "");
						timeDiff = Long.valueOf(timeStr) - System.currentTimeMillis();
					} catch (Exception e) {
						logger.warn("TimeHeartbeat -> run -> httpConnect.getString -> timeSvrUrlStc:{}, Exception:{}", timeSvrUrlStc, e);
					}
				}
				
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					logger.error("TimeHeartbeat -> run -> Thread.sleep -> interval:{}, Exception:{}", interval, e);
				}
			}
		}		
	}
	
	public void setTimeSvrUrl(String timeSvrUrl) {
		this.timeSvrUrl = timeSvrUrl;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}
}
