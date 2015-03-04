package org.fl.noodle.common.monitor.performance.vo;

public class KeyVo {
	
	private String themeName;
	private String monitorType;
	private String selfModuleType;
	private long selfModuleId;
	private String moduleType;
	private long moduleId;
		
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
	
	public long getSelfModuleId() {
		return selfModuleId;
	}
	public void setSelfModuleId(long selfModuleId) {
		this.selfModuleId = selfModuleId;
	}
	
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	
	public long getModuleId() {
		return moduleId;
	}
	public void setModuleId(long moduleId) {
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
