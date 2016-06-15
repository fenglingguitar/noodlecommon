package org.fl.noodle.common.connect.agent;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;

public abstract class AbstractConnectAgentFactory implements ConnectAgentFactory {

	protected int connectTimeout = 3000;
	protected int readTimeout = 3000;
	protected String encoding = "utf-8";
	protected int invalidLimitNum = 3;
	
	protected ConnectDistinguish connectDistinguish;
	
	protected List<MethodInterceptor> methodInterceptorList;

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

	public void setMethodInterceptorList(List<MethodInterceptor> methodInterceptorList) {
		this.methodInterceptorList = methodInterceptorList;
	}
}
