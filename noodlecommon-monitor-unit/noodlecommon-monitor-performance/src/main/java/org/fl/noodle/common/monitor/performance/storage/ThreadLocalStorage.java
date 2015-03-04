package org.fl.noodle.common.monitor.performance.storage;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalStorage {

	private static final ThreadLocal<Map<String, Map<String, Map<String, Map<Long, Object>>>>> mapThreadLocal = 
									new ThreadLocal<Map<String, Map<String, Map<String, Map<Long, Object>>>>>();
	
	public static <T> T get(String themeName, String monitorType, String moduleType, long moduleId, Class<T> clazz) throws Exception {
		
		Map<String, Map<String, Map<String, Map<Long, Object>>>> threadMap = mapThreadLocal.get();
		if (threadMap == null) {
			threadMap = new HashMap<String, Map<String, Map<String, Map<Long, Object>>>>();
			mapThreadLocal.set(threadMap);
		}
		
		Map<String, Map<String, Map<Long, Object>>> themeNameMap = threadMap.get(themeName);
		if (themeNameMap == null) {
			themeNameMap = new HashMap<String, Map<String, Map<Long, Object>>>();
			threadMap.put(themeName, themeNameMap);
		}
		
		Map<String, Map<Long, Object>> monitorTypeMap = themeNameMap.get(monitorType);
		if (monitorTypeMap == null) {
			monitorTypeMap = new HashMap<String, Map<Long, Object>>();
			themeNameMap.put(monitorType, monitorTypeMap);
		}
		
		Map<Long, Object> moduleTypeMap = monitorTypeMap.get(moduleType);
		if (moduleTypeMap == null) {
			moduleTypeMap = new HashMap<Long, Object>();
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
