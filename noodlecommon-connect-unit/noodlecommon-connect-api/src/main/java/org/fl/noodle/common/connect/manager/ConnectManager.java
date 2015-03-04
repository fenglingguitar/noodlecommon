package org.fl.noodle.common.connect.manager;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.cluster.ConnectCluster;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.performance.ConnectPerformanceInfo;
import org.fl.noodle.common.connect.route.ConnectRoute;

public interface ConnectManager {
	
	public void runUpdate();
	public void runUpdateNow();
	
	public ConnectNode getConnectNode(String nodeName);
	public ConnectAgent getConnectAgent(long connectId);
	public ConnectCluster getConnectCluster(String clusterName);
	public ConnectRoute getConnectRoute(String routeName);
	public ConnectPerformanceInfo getConnectPerformanceInfo(String methodKey);
}
