package org.fl.noodle.common.monitor.performance.storage;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalStorage {

	private static final ThreadLocal<Map<String, Map<String, Map<String, Map<String, Object>>>>> mapThreadLocal = 
									new ThreadLocal<Map<String, Map<String, Map<String, Map<String, Object>>>>>();
	
	public static <T> T get(String themeName, String monitorType, String moduleType, String moduleId, Class<T> clazz) throws Exception {
		
		Map<String, Map<String, Map<String, Map<String, Object>>>> threadMap = mapThreadLocal.get();
		if (threadMap == null) {
			threadMap = new HashMap<String, Map<String, Map<String, Map<String, Object>>>>();
			mapThreadLocal.set(threadMap);
		}
		
		Map<String, Map<String, Map<String, Object>>> themeNameMap = threadMap.get(themeName);
		if (themeNameMap == null) {
			themeNameMap = new HashMap<String, Map<String, Map<String, Object>>>();
			threadMap.put(themeName, themeNameMap);
		}
		
		Map<String, Map<String, Object>> monitorTypeMap = themeNameMap.get(monitorType);
		if (monitorTypeMap == null) {
			monitorTypeMap = new HashMap<String, Map<String, Object>>();
			themeNameMap.put(monitorType, monitorTypeMap);
		}
		
		Map<String, Object> moduleTypeMap = monitorTypeMap.get(moduleType);
		if (moduleTypeMap == null) {
			moduleTypeMap = new HashMap<String, Object>();
			monitorTypeMap.put(moduleType, moduleTypeMap);
		}
		
		@SuppressWarnings("unchecked")
		T object = (T) moduleTypeMap.get(moduleId);
		if (object == null) {
			object = clazz.newInstance();
			moduleTypeMap.put(moduleId, object);
		}
		
		return object;
	}
}
