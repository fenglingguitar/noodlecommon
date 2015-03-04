package org.fl.noodle.common.connect.cluster;

public interface ConnectClusterFactory {

	public ConnectCluster createConnectCluster(Class<?> serviceInterface);
}
