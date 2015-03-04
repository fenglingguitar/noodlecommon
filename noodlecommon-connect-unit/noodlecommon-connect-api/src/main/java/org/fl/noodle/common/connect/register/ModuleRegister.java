package org.fl.noodle.common.connect.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ModuleRegister {
	
	private final String defaultName = "default";
	
	public Map<String, Long> moduleIdMap = new HashMap<String, Long>();
	public String moduleName = "defaultModule";
	
	public Long getModuleId() {
		return moduleIdMap.get(defaultName) != null ? moduleIdMap.get(defaultName) : 0L;
	}
	
	public void setModuleId(Long moduleId) {
		moduleIdMap.put(defaultName, moduleId);
	}
	
	public Long getModuleId(String name) {
		return moduleIdMap.get(name) != null ? moduleIdMap.get(name) : 0L;
	}
	
	public void setModuleId(String name, Long moduleId) {
		moduleIdMap.put(name, moduleId);
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public List<Long> getModuleIdSet() {
		List<Long> valueList = new ArrayList<Long>();
		valueList.addAll(moduleIdMap.values());
		return valueList;
	}
	
	public String toString() {		
		StringBuilder stringBuilder = new StringBuilder().append("moduleIds:[");
		int count = 0;
		for (Entry<String, Long> entry : moduleIdMap.entrySet()) {
			stringBuilder.append("[").append(entry.getKey()).append(",").append(entry.getValue()).append("]");
			if (++count < moduleIdMap.size()) {
				stringBuilder.append(",");
			}
		}
		return stringBuilder.append("]").toString();
	}
}
