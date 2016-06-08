package org.fl.noodle.common.monitor.performance.constent;

public enum MonitorType {
	
	SERVICE("SERVICE"), CONNECT("CONNECT");
	
	private String code;

	private MonitorType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}  
}
