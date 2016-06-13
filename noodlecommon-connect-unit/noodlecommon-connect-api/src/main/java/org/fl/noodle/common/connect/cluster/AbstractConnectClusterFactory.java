package org.fl.noodle.common.connect.cluster;

import java.util.List;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;

public abstract class AbstractConnectClusterFactory implements ConnectClusterFactory {

	protected ConnectDistinguish connectDistinguish;
	
	protected List<Object> methodInterceptorList;

	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}

	public void setMethodInterceptorList(List<Object> methodInterceptorList) {
		this.methodInterceptorList = methodInterceptorList;
	}	
}
