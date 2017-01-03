package org.fl.noodle.common.connect.cluster;

import org.fl.noodle.common.connect.constent.ConnectClusterType;

public class AllConnectClusterFactory extends AbstractConnectClusterFactory {

	@Override
	public ConnectCluster createConnectCluster(Class<?> serviceInterface) {
		return new AllConnectCluster(serviceInterface, connectDistinguish, methodInterceptorList, ConnectClusterType.ALL.getCode());
	}
}
