package org.fl.noodle.common.connect.cluster;

import org.fl.noodle.common.connect.constent.ConnectClusterType;

public class FailoverConnectClusterFactory extends AbstractConnectClusterFactory {

	@Override
	public ConnectCluster createConnectCluster(Class<?> serviceInterface) {
		return new FailoverConnectCluster(serviceInterface, connectDistinguish, methodInterceptorList, ConnectClusterType.FAILOVER.getCode());
	}
}
