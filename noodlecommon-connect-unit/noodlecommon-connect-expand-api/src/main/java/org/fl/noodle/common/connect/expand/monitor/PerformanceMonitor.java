package org.fl.noodle.common.connect.expand.monitor;

public interface PerformanceMonitor {
	
	public void before(String themeName, String monitorType, String moduleType, String moduleId);
	public void after(String moduleName, String themeName, String monitorType, String moduleType, String moduleId, long threshold, boolean result);
}
