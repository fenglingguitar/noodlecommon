package org.fengling.noodlecommon.util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorThreadFactory implements ThreadFactory {
	
	private List<Thread> threadList = new ArrayList<Thread>();
	private AtomicInteger index = new AtomicInteger(0);
	
	private String name = "ExecutorThreadFactory";
	private boolean daemon = false;
	private int priority = Thread.NORM_PRIORITY;
	
	public ExecutorThreadFactory() {
	}
	
	public ExecutorThreadFactory(String name) {
		this.name = name;
	}

	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setName(name + "-" + index.incrementAndGet());
		thread.setDaemon(daemon);
		thread.setPriority(priority);
		threadList.add(thread);
		return thread;
	}
	
	public void interruptAll() {
		for (Thread thread : threadList) {
			thread.interrupt();
		}
	}

	public List<Thread> getThreadList() {
		return threadList;
	}

	public void setThreadList(List<Thread> threadList) {
		this.threadList = threadList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
