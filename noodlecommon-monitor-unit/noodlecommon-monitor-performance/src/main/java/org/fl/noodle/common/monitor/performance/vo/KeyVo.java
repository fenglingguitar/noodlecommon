package org.fl.noodle.common.monitor.performance.vo;

public class KeyVo {
	
	private String themeName;
	private String monitorType;
	private String selfModuleType;
	private String selfModuleId;
	private String moduleType;
	private String moduleId;
		
	public String getThemeName() {
		return themeName;
	}
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
	
	public String getSelfModuleType() {
		return selfModuleType;
	}
	public void setSelfModuleType(String selfModuleType) {
		this.selfModuleType = selfModuleType;
	}
	
	public String getSelfModuleId() {
		return selfModuleId;
	}
	public void setSelfModuleId(String selfModuleId) {
		this.selfModuleId = selfModuleId;
	}
	
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	public String toKeyString () {
		return new StringBuilder()
					.append("KEY-")
					.append(themeName)
					.append("-")
					.append(monitorType)
					.append("-")
					.append(selfModuleType)
					.append("-")
					.append(selfModuleId)
					.append("-")
					.append(moduleType)
					.append("-")
					.append(moduleId)
					.toString();
	}
}
