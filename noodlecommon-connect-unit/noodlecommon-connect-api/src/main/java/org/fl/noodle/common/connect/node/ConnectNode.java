package org.fl.noodle.common.connect.node;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public interface ConnectNode {

	public List<ConnectAgent> getHealthyConnectAgentList();
	public boolean isContainsConnectAgent(ConnectAgent connectAgent);
	public void addConnectAgent(ConnectAgent connectAgent);
	public void removeConnectAgent(ConnectAgent connectAgent);
	public List<ConnectAgent> getAllConnectAgentList();

	public void addChildConnectNode(long key, ConnectNode connectNode);
	public ConnectNode getChildConnectNode(long key);
	public boolean isContainsChildConnectNode(long key);
	public void removeChildConnectNode(long key);
	public ConcurrentMap<Long, ConnectNode> getChildConnectNodeMap();

	public String getNodeName();
}
