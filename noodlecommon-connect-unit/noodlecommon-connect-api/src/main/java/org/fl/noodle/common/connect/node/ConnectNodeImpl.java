package org.fl.noodle.common.connect.node;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public class ConnectNodeImpl implements ConnectNode {
	
	//private final static Logger logger = LoggerFactory.getLogger(ConnectNodeImpl.class);
	
	protected String nodeName; 
	
	protected CopyOnWriteArrayList<ConnectAgent> connectAgentList = new CopyOnWriteArrayList<ConnectAgent>();
	
	protected ConcurrentMap<Long, ConnectNode> childConnectNodeMap = new ConcurrentHashMap<Long, ConnectNode>();

	public ConnectNodeImpl(String nodeName) {
		this.nodeName = nodeName;
	}
	
	@Override
	public List<ConnectAgent> getHealthyConnectAgentList() {
		for (ConnectAgent connectAgent : connectAgentList) {
			if (!connectAgent.isHealthyConnect()) {
				connectAgentList.remove(connectAgent);
			}
		}
		return connectAgentList;
	}
	
	@Override
	public boolean isContainsConnectAgent(ConnectAgent connectAgent) {
		return connectAgentList.contains(connectAgent);
	}
	

	@Override
	public void addConnectAgent(ConnectAgent connectAgent) {
		connectAgentList.add(connectAgent);
	}

	@Override
	public void removeConnectAgent(ConnectAgent connectAgent) {
		connectAgentList.remove(connectAgent);
	}
	
	@Override
	public List<ConnectAgent> getAllConnectAgentList() {
		return connectAgentList;
	}
	
	@Override
	public void addChildConnectNode(long key, ConnectNode connectNode) {
		childConnectNodeMap.put(key, connectNode);
	}
	
	@Override
	public ConnectNode getChildConnectNode(long key) {
		return childConnectNodeMap.get(key);
	}
	
	@Override
	public boolean isContainsChildConnectNode(long key) {
		return childConnectNodeMap.containsKey(key);
	}
	
	@Override
	public void removeChildConnectNode(long key) {
		childConnectNodeMap.remove(key);
	}
	
	@Override
	public ConcurrentMap<Long, ConnectNode> getChildConnectNodeMap() {
		return childConnectNodeMap;
	}
	
	@Override
	public String getNodeName() {
		return this.nodeName;
	}
	
	public String toString() {
		return new StringBuilder()
			.append("nodeName:").append(nodeName)
			.toString();
	}
}
