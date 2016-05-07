package org.fl.noodle.common.monitor.schedule;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fl.noodle.common.monitor.executer.Executer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuterSchedule {

	private final static Logger logger = LoggerFactory.getLogger(ExecuterSchedule.class);
			
	private List<Executer> executerList;
	
	private int corePoolSize = 1;
	
	private ScheduledExecutorService scheduledExecutorService;
	
	private long delay = 30;
	private long initialDelay = 0;

	public void start() {
		scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);
		initialization();
	}
	
	public void destroy() {
		scheduledExecutorService.shutdown();
	}
	
	private void initialization() {
		
		if (executerList != null) {
			
			for (final Executer executer : executerList) {
				
				long initialDelay = executer.getInitialDelay();
				long delay = executer.getDelay() > 0 ? executer.getDelay() : this.delay;
				if (initialDelay == 0) {
					if (this.initialDelay == 0) {								
						initialDelay = Math.round(Math.random() * delay);
					} else {
						initialDelay = this.initialDelay;
					}
				}
				scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
					@Override
					public void run() {
						try {
							executer.execute();
						} catch (Exception e) {
							logger.error("initialization -> run -> executer.execute -> executer:{}, Exception:{}", executer.getClass(), e.getMessage());
						}
					}
				}, initialDelay, delay, TimeUnit.SECONDS);
				
				logger.info("initialization -> executer:{}, initialDelay:{}, delay:{} -> Executer scheduled", executer.getClass(), initialDelay, delay);
			}
		}
	}
	
	public void setExecuterList(List<Executer> executerList) {
		this.executerList = executerList;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}
}
