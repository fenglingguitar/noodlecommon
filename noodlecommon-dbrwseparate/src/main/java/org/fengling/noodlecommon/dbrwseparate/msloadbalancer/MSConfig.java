package org.fengling.noodlecommon.dbrwseparate.msloadbalancer;

public class MSConfig {
	
	private int totalFailureCount = 3;
	private int totalRiseCount = 2;
	private long interTime = 2000;
	private String detectingSql;

	public int getTotalFailureCount() {
		return totalFailureCount;
	}

	public void setTotalFailureCount(int totalFailureCount) {
		this.totalFailureCount = totalFailureCount;
	}

	public int getTotalRiseCount() {
		return totalRiseCount;
	}

	public void setTotalRiseCount(int totalRiseCount) {
		this.totalRiseCount = totalRiseCount;
	}

	public long getInterTime() {
		return interTime;
	}

	public void setInterTime(long interTime) {
		this.interTime = interTime;
	}

	public String getDetectingSql() {
		return detectingSql;
	}

	public void setDetectingSql(String detectingSql) {
		this.detectingSql = detectingSql;
	}
}
