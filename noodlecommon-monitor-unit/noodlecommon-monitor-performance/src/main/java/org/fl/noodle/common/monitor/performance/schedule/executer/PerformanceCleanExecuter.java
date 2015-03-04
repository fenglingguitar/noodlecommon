package org.fl.noodle.common.monitor.performance.schedule.executer;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.monitor.executer.AbstractExecuter;
import org.fl.noodle.common.monitor.performance.persistence.PerformancePersistence;

public class PerformanceCleanExecuter extends AbstractExecuter {

	private final static Logger logger = LoggerFactory.getLogger(PerformanceCleanExecuter.class);

	private PerformancePersistence performancePersistence;
	
	private long timeSpan = 1000 * 60 * 3;

	@Override
	public void execute() {
		
		Set<String> keysSet = null;
		
		try {
			keysSet = performancePersistence.getKeys();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("execute -> performancePersistence.getKeys -> timeSpan:{} -> Exception:{}", timeSpan, e.getMessage());
			}
		}
		
		if (keysSet != null) {
			long nowTime = System.currentTimeMillis();
			
			for (String key : keysSet) {
				try {
					performancePersistence.deletes(key, 0, nowTime - timeSpan);
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error("execute -> performancePersistence.deletes -> key{}, timeSpan:{} -> Exception:{}", key, timeSpan, e.getMessage());
					}
				}
			}
		}
	}
	
	public static class ThreadPerformanceInfo {
		public long startTime;
	}

	public void setPerformancePersistence(PerformancePersistence performancePersistence) {
		this.performancePersistence = performancePersistence;
	}
	
	public void setTimeSpan(long timeSpan) {
		this.timeSpan = 1000 * 60 * 60 * timeSpan;
	}
}
