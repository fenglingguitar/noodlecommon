package org.fl.noodle.common.connect.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectManagerPool {
	
	private ConcurrentMap<String, ConnectManager> connectManagerMap = new ConcurrentHashMap<String, ConnectManager>();
	private List<ConnectManager> connectManagerList = new ArrayList<ConnectManager>();
	
	public ConnectManager getConnectManager(String managerName) {
		return connectManagerMap.get(managerName);
	}
	
	public void startConnectManager() {
		Collections.sort(connectManagerList, new PriorityComparator());
		for (ConnectManager connectManager : connectManagerList) {
			connectManager.start();
			connectManagerMap.put(connectManager.getManagerName(), connectManager);
		}
	}
	
	public void destroyConnectManager() {
		ListIterator<ConnectManager> listIterator = connectManagerList.listIterator(connectManagerList.size());
		while (listIterator.hasPrevious()) {
			listIterator.previous().destroy();
		}
		connectManagerList.clear();
		connectManagerMap.clear();
	}
	
	private static class PriorityComparator implements Comparator<ConnectManager> {

		@Override
		public int compare(ConnectManager o1, ConnectManager o2) {
			if (o1.getBootPriority() > o2.getBootPriority()) {
				return 1;
			} else if (o1.getBootPriority() < o2.getBootPriority()) {
				return -1;
			} else {
				return 0;
			}
		}		
	}

	public void setConnectManagerList(List<ConnectManager> connectManagerList) {
		this.connectManagerList = connectManagerList;
	}
}
