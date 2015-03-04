package org.fl.noodle.common.monitor.performance.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.fl.noodle.common.monitor.performance.vo.KeyVo;

public class MemoryStorage {

	private static ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Long, Object>>>> storageMap = 
								new ConcurrentHashMap<String, ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Long, Object>>>>();
	
	public static <T> T get(String themeName, String monitorType, String moduleType, long moduleId, Class<T> clazz) throws Exception {
		
		ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Long, Object>>> themeNameMap = storageMap.get(themeName);
		if (themeNameMap == null) {
			themeNameMap = new ConcurrentHashMap<String, ConcurrentMap<String, ConcurrentMap<Long, Object>>>();
			ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Long, Object>>> themeNameMapBack = storageMap.putIfAbsent(themeName, themeNameMap);
			if (themeNameMapBack != null) {
				themeNameMap = themeNameMapBack;
			}
		}
		
		ConcurrentMap<String, ConcurrentMap<Long, Object>> monitorTypeMap = themeNameMap.get(monitorType);
		if (monitorTypeMap == null) {
			monitorTypeMap = new ConcurrentHashMap<String, ConcurrentMap<Long, Object>>();
			ConcurrentMap<String, ConcurrentMap<Long, Object>> monitorTypeMapBack = themeNameMap.putIfAbsent(monitorType, monitorTypeMap);
			if (monitorTypeMapBack != null) {
				monitorTypeMap = monitorTypeMapBack;
			}
		}
		
		ConcurrentMap<Long, Object> moduleTypeMap = monitorTypeMap.get(moduleType);
		if (moduleTypeMap == null) {
			moduleTypeMap = new ConcurrentHashMap<Long, Object>();
			ConcurrentMap<Long, Object> moduleTypeMapBack = monitorTypeMap.putIfAbsent(moduleType, moduleTypeMap);
			if (moduleTypeMapBack != null) {
				moduleTypeMap = moduleTypeMapBack;
			}
		}
		
		@SuppressWarnings("unchecked")
		T object = (T) moduleTypeMap.get(moduleId);
		if (object == null) {
			object = clazz.newInstance();
			@SuppressWarnings("unchecked")
			T objectBack = (T) moduleTypeMap.putIfAbsent(moduleId, object);
			if (objectBack != null) {
				object = objectBack;
			}
		}
		
		return object;
	}
	
	public static List<KeyVo> getKeys() {
		
		List<KeyVo> KeyVoList = new ArrayList<KeyVo>();
		
		for (String themeName : storageMap.keySet()) {
			ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Long, Object>>> monitorTypeMap = storageMap.get(themeName);
			if (monitorTypeMap != null) {
				for (String monitorType : monitorTypeMap.keySet()) {
					ConcurrentMap<String, ConcurrentMap<Long, Object>> moduleTypeMap = monitorTypeMap.get(monitorType);
					if (moduleTypeMap != null) {
						for (String moduleType : moduleTypeMap.keySet()) {
							ConcurrentMap<Long, Object> moduleIdMap = moduleTypeMap.get(moduleType);
							for (Long moduleId : moduleIdMap.keySet()) {
								KeyVo keyVo = new KeyVo();
								keyVo.setThemeName(themeName);
								keyVo.setMonitorType(monitorType);
								keyVo.setModuleType(moduleType);
								keyVo.setModuleId(moduleId);
								KeyVoList.add(keyVo);
							}
						}
					}
				}
			}
		}
	
		return KeyVoList;
	}
}
