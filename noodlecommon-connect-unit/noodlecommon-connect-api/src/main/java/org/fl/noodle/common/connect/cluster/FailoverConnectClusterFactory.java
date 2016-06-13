package org.fl.noodle.common.connect.cluster;

public class FailoverConnectClusterFactory extends AbstractConnectClusterFactory {

	@Override
	public ConnectCluster createConnectCluster(Class<?> serviceInterface) {
		return new FailoverConnectCluster(serviceInterface, connectDistinguish, methodInterceptorList);
	}
}
