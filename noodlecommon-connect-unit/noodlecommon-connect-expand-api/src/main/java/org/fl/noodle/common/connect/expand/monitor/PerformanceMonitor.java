package org.fl.noodle.common.connect.expand.monitor;

public interface PerformanceMonitor {
	
	public void before(String themeName, String monitorType, String moduleType, long moduleId);
	public void after(String moduleName, String themeName, String monitorType, String moduleType, long moduleId, long threshold, boolean result);
}
