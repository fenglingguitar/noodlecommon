package org.fl.noodle.common.connect.cluster;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;

import org.fl.noodle.common.connect.expand.monitor.PerformanceMonitor;

public abstract class AbstractConnectClusterFactory implements ConnectClusterFactory {

	protected ConnectDistinguish connectDistinguish;
	
	protected PerformanceMonitor performanceMonitor;

	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}

	public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
		this.performanceMonitor = performanceMonitor;
	}
}
