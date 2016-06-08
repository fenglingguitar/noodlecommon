package org.fl.noodle.common.connect.cluster;

public class AllConnectClusterFactory extends AbstractConnectClusterFactory {

	@Override
	public ConnectCluster createConnectCluster(Class<?> serviceInterface) {
		return new AllConnectCluster(serviceInterface, connectDistinguish);
	}
}
