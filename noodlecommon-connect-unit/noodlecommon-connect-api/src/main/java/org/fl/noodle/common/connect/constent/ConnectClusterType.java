package org.fl.noodle.common.connect.constent;

public enum ConnectClusterType {
	
	FAILOVER("FAILOVER"), ONCE("ONCE"), ALL("ALL");
	
	private String code;

	private ConnectClusterType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}  
}
