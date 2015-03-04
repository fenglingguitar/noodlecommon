package org.fl.noodle.common.connect.agent;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;

import org.fl.noodle.common.connect.expand.monitor.PerformanceMonitor;

public abstract class AbstractConnectAgentFactory implements ConnectAgentFactory {

	protected int connectTimeout = 3000;
	protected int readTimeout = 3000;
	protected String encoding = "utf-8";
	protected int invalidLimitNum = 3;
	
	protected ConnectDistinguish connectDistinguish;
	
	protected PerformanceMonitor performanceMonitor;

	@Override
	public ConnectAgent createConnectAgent(long connectId, String ip, int port, String url) {
		return createConnectAgent(connectId, ip, port, url, this.connectTimeout, this.readTimeout);
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setInvalidLimitNum(int invalidLimitNum) {
		this.invalidLimitNum = invalidLimitNum;
	}

	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}
	
	public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
		this.performanceMonitor = performanceMonitor;
	}
}
