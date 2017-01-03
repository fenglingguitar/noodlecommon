package org.fl.noodle.common.connect.cluster;

import org.fl.noodle.common.connect.constent.ConnectClusterType;

public class OnceConnectClusterFactory extends AbstractConnectClusterFactory {

	@Override
	public ConnectCluster createConnectCluster(Class<?> serviceInterface) {
		return new OnceConnectCluster(serviceInterface, connectDistinguish, methodInterceptorList, ConnectClusterType.ONCE.getCode());
	}
}
