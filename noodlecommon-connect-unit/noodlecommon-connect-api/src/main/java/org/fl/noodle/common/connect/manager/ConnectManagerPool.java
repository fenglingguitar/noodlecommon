package org.fl.noodle.common.connect.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectManagerPool {
	
	private static ConcurrentMap<String, ConnectManager> connectManagerMap = new ConcurrentHashMap<String, ConnectManager>();
	
	public static ConnectManager getConnectManager(String managerName) {
		return connectManagerMap.get(managerName);
	}
	
	public static ConnectManager addConnectManager(String managerName, ConnectManager connectManager) {
		return connectManagerMap.put(managerName, connectManager);
	}
}
