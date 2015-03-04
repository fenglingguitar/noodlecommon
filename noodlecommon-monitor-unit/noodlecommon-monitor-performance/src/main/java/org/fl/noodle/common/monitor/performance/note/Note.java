package org.fl.noodle.common.monitor.performance.note;

import java.util.concurrent.atomic.AtomicLong;

public class Note { 

	private AtomicLong totalCount = new AtomicLong(0);
	private AtomicLong overtimeCount = new AtomicLong(0);
	private AtomicLong totalTime = new AtomicLong(0);
	private AtomicLong successCount = new AtomicLong(0);
	
	private AtomicLong overtimeThreshold = new AtomicLong(0);
	
	private volatile long timestamp;
	private volatile String moduleName;
	
	public void totalCountAdd() {
		totalCount.incrementAndGet();
	}
	
	public long totalCountReset() {
		return totalCount.getAndSet(0);
	}
	
	public void overtimeCountAdd() {
		overtimeCount.incrementAndGet();
	}
	
	public long overtimeCountReset() {
		return overtimeCount.getAndSet(0);
	}
	
	public void totalTimeAdd(long time) {
		totalTime.addAndGet(time);
	}
	
	public long totalTimeReset() {
		return totalTime.getAndSet(0);
	}
	
	public void successCountAdd() {
		successCount.incrementAndGet();
	}
	
	public long successCountReset() {
		return successCount.getAndSet(0);
	}
	
	public void overtimeThresholdSet(long threshold) {
		overtimeThreshold.set(threshold);
	}
	
	public long overtimeThresholdReset() {
		return overtimeThreshold.getAndSet(0);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
} 
