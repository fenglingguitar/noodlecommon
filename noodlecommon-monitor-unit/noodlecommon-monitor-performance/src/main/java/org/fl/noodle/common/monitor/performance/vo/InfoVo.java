package org.fl.noodle.common.monitor.performance.vo;

public class InfoVo {
	
	private long totalCount;
	private long overtimeCount;
	private long threshold;
	private long averageTime;
	private long successCount;
	
	private long timestamp;

	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getOvertimeCount() {
		return overtimeCount;
	}
	public void setOvertimeCount(long overtimeCount) {
		this.overtimeCount = overtimeCount;
	}

	public long getThreshold() {
		return threshold;
	}
	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

	public long getAverageTime() {
		return averageTime;
	}
	public void setAverageTime(long averageTime) {
		this.averageTime = averageTime;
	}
	
	public long getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(long successCount) {
		this.successCount = successCount;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
