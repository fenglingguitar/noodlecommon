package org.fl.noodle.common.connect.constent;

public enum ConnectRouteType {
	
	RANDOM("RANDOM"), WEIGHT("WEIGHT"), RESPONSE("RESPONSE") ;
	
	private String code;

	private ConnectRouteType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}  
}
