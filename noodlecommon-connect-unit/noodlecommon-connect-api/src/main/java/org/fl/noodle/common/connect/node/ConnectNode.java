package org.fl.noodle.common.connect.node;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public interface ConnectNode {

	public List<ConnectAgent> getConnectAgentList();
	public void updateConnectAgentList(List<ConnectAgent> connectAgentList);
	public boolean isContainsConnectAgent(ConnectAgent connectAgent);

	public void addChildConnectNode(long key, ConnectNode connectNode);
	public ConnectNode getChildConnectNode(long key);
	public ConcurrentMap<Long, ConnectNode> getChildConnectNodeMap();

	public String getNodeName();
}
