package org.fl.noodle.common.connect.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.fl.noodle.common.util.thread.ExecutorThreadFactory;

public class ConnectManagerPool {
	
	private ConcurrentMap<String, ConnectManager> connectManagerMap = new ConcurrentHashMap<String, ConnectManager>();
	private List<ConnectManager> connectManagerList = new ArrayList<ConnectManager>();
	
	protected ExecutorService executorService = Executors.newSingleThreadExecutor(new ExecutorThreadFactory(this.getClass().getName()));
	
	protected volatile boolean stopSign = false;
	
	private long suspendTime = 60000;
	
	public ConnectManager getConnectManager(String managerName) {
		return connectManagerMap.get(managerName);
	}
	
	public void start() {
		initManager();
		startUpdate();
	}
	
	public void destroy() {
		stopUpdate();
		destroyManager();
	}
	
	private void initManager() {
		Collections.sort(connectManagerList, new PriorityComparator());
		for (ConnectManager connectManager : connectManagerList) {
			connectManager.setConnectManagerPool(this);
			connectManager.start();
			connectManagerMap.put(connectManager.getManagerName(), connectManager);
		}
	}
	
	private void destroyManager() {
		ListIterator<ConnectManager> listIterator = connectManagerList.listIterator(connectManagerList.size());
		while (listIterator.hasPrevious()) {
			listIterator.previous().destroy();
		}
		connectManagerList.clear();
		connectManagerMap.clear();
	}
	
	private void startUpdate() {
		runUpdateNow();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				while (!stopSign) {
					try {
						suspendUpdate();
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
					updateConnectAgent();
				}
			}
		});
	}
	
	private void stopUpdate() {
		stopSign = true;
		executorService.shutdownNow();
		try {
			if(!executorService.awaitTermination(60000, TimeUnit.MILLISECONDS)) {
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected synchronized void suspendUpdate() throws InterruptedException {
		wait(suspendTime);
	}
	
	public synchronized void runUpdate() {
		notifyAll();
	}
	
	public synchronized void runUpdateNow() {
		updateConnectAgent();
	}
	
	protected synchronized void updateConnectAgent() {
		for (ConnectManager connectManager : connectManagerList) {
			connectManager.runUpdateAddComponent();
		}
		ListIterator<ConnectManager> listIterator = connectManagerList.listIterator(connectManagerList.size());
		while (listIterator.hasPrevious()) {
			listIterator.previous().runUpdateReduceComponent();
		}
	}
	
	private static class PriorityComparator implements Comparator<ConnectManager> {

		@Override
		public int compare(ConnectManager o1, ConnectManager o2) {
			if (o1.getUpdateLevel() > o2.getUpdateLevel()) {
				return 1;
			} else if (o1.getUpdateLevel() < o2.getUpdateLevel()) {
				return -1;
			} else {
				return 0;
			}
		}		
	}

	public void setConnectManagerList(List<ConnectManager> connectManagerList) {
		this.connectManagerList = connectManagerList;
	}

	public void setSuspendTime(long suspendTime) {
		this.suspendTime = suspendTime;
	}
}
