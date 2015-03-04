package org.fl.noodle.common.connect.performance;

import java.util.concurrent.atomic.AtomicLong;

public class ConnectPerformanceNode {
	
	private AtomicLong totalTime = new AtomicLong(0);
	private AtomicLong totalCount = new AtomicLong(0);
	private AtomicLong overtimeCount = new AtomicLong(0);
	private volatile long avgTime = 0;
	
	public void addTotalTime(long time) {
		totalTime.addAndGet(time);
	}
	public long getTotalTime() {
		return totalTime.get();
	}
	
	public void addTotalCount() {
		totalCount.incrementAndGet();
	}
	public long getTotalCount() {
		return totalCount.get();
	}
	
	public void addOvertimeCount() {
		overtimeCount.incrementAndGet();
	}
	public long getOvertimeCount() {
		return overtimeCount.get();
	}
	
	public long getAvgTime() {
		return avgTime;
	}
	
	public void calculate() {	
		
		long totalTimeTemp = totalTime.get();
		long totalCountTemp = totalCount.get();
		if (totalCountTemp > 0) {
			avgTime = totalTimeTemp / totalCountTemp;
		} else {
			avgTime = 0;
		}
		
		totalTime.set(totalTime.get() / 2);
		totalCount.set(totalCount.get() / 2);
		overtimeCount.set(overtimeCount.get() / 2);
	}
}
