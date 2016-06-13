package org.fl.noodle.common.connect.cluster;

public class OnceConnectClusterFactory extends AbstractConnectClusterFactory {

	@Override
	public ConnectCluster createConnectCluster(Class<?> serviceInterface) {
		return new OnceConnectCluster(serviceInterface, connectDistinguish, methodInterceptorList);
	}
}
