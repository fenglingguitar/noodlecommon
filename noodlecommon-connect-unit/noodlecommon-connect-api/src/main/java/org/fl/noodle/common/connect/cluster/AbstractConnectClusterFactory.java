package org.fl.noodle.common.connect.cluster;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;

public abstract class AbstractConnectClusterFactory implements ConnectClusterFactory {

	protected ConnectDistinguish connectDistinguish;
	
	protected List<MethodInterceptor> methodInterceptorList;

	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}

	public void setMethodInterceptorList(List<MethodInterceptor> methodInterceptorList) {
		this.methodInterceptorList = methodInterceptorList;
	}	
}
