package org.fl.noodle.common.connect.agent;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.fl.noodle.common.connect.agent.AbstractConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.serialize.ConnectSerialize;

public abstract class AbstractNetConnectAgent extends AbstractConnectAgent {

	protected ConnectSerialize connectSerialize;
	
	public AbstractNetConnectAgent(long connectId, String ip, int port, String url, String type, int connectTimeout, int readTimeout, String encoding, int invalidLimitNum, ConnectDistinguish connectDistinguish, List<MethodInterceptor> methodInterceptorList) {
		super(connectId, ip, port, url, type, connectTimeout, readTimeout, encoding, invalidLimitNum, connectDistinguish, methodInterceptorList);
	}
	
	public void setConnectSerialize(ConnectSerialize connectSerialize) {
		this.connectSerialize = connectSerialize;
	}
}
