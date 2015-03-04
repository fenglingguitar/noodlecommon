package org.fl.noodle.common.connect.performance;

public class ConnectPerformanceInfo {
	
	private volatile IsDowngrade isDowngrade = IsDowngrade.NO;
	private volatile DowngradeType downgradeType = DowngradeType.AVGTIME;
	private volatile ReturnType returnType = ReturnType.T_EXCEPTION;
	private volatile long avgTimeLimitThreshold = 200;
	private volatile long overtimeThreshold = 200;
	private volatile long overtimeLimitThreshold = 10;
	private volatile boolean isMonitor = true;
	
	public IsDowngrade getIsDowngrade() {
		return isDowngrade;
	}
	public void setIsDowngrade(IsDowngrade isDowngrade) {
		this.isDowngrade = isDowngrade;
	}
	
	public DowngradeType getDowngradeType() {
		return downgradeType;
	}
	public void setDowngradeType(DowngradeType downgradeType) {
		this.downgradeType = downgradeType;
	}
	
	public ReturnType getReturnType() {
		return returnType;
	}
	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
	}

	public long getAvgTimeLimitThreshold() {
		return avgTimeLimitThreshold;
	}
	public void setAvgTimeLimitThreshold(long avgTimeLimitThreshold) {
		this.avgTimeLimitThreshold = avgTimeLimitThreshold;
	}
	
	public long getOvertimeThreshold() {
		return overtimeThreshold;
	}
	public void setOvertimeThreshold(long overtimeThreshold) {
		this.overtimeThreshold = overtimeThreshold;
	}
	
	public long getOvertimeLimitThreshold() {
		return overtimeLimitThreshold;
	}
	public void setOvertimeLimitThreshold(long overtimeLimitThreshold) {
		this.overtimeLimitThreshold = overtimeLimitThreshold;
	}
	
	public boolean getIsMonitor() {
		return isMonitor;
	}
	
	public void setIsMonitor(boolean isMonitor) {
		this.isMonitor = isMonitor;
	}

	public static enum IsDowngrade {
		
		YES(1), NO(2);
		
		private int code;

		private IsDowngrade(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}  
		
		public static IsDowngrade valueOf(int code) {
			switch (code) {
				case 1: return IsDowngrade.YES;
				case 2: return IsDowngrade.NO;
				default: return null;
			} 
		}
	}
	
	public static enum DowngradeType {
		
		AVGTIME(1), OVERTIME(2);
		
		private int code;

		private DowngradeType(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}  
		
		public static DowngradeType valueOf(int code) {
			switch (code) {
				case 1: return DowngradeType.AVGTIME;
				case 2: return DowngradeType.OVERTIME;
				default: return null;
			} 
		}
	}
	
	public static enum ReturnType {
		
		T_EXCEPTION(1), R_NULL(2);
		
		private int code;

		private ReturnType(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}  
		
		public static ReturnType valueOf(int code) {
			switch (code) {
				case 1: return ReturnType.T_EXCEPTION;
				case 2: return ReturnType.R_NULL;
				default: return null;
			} 
		}
	}
}
