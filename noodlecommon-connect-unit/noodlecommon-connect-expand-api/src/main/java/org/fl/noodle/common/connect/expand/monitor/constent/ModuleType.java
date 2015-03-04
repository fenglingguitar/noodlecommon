package org.fl.noodle.common.connect.expand.monitor.constent;

public enum ModuleType {
	
	DEFAULT("DEFAULT"), CLIENT("CLIENT"), SERVER("SERVER");
	
	private String code;

	private ModuleType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}  
}
