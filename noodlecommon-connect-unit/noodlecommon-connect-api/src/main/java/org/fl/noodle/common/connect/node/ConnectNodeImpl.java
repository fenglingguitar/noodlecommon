package org.fl.noodle.common.connect.node;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectNodeImpl implements ConnectNode {
	
	private final static Logger logger = LoggerFactory.getLogger(ConnectNodeImpl.class);
	
	protected String nodeName; 
	
	protected CopyOnWriteArrayList<ConnectAgent> connectAgentList = new CopyOnWriteArrayList<ConnectAgent>();
	
	protected ConcurrentMap<Long, ConnectNode> childConnectNodeMap = new ConcurrentHashMap<Long, ConnectNode>();

	public ConnectNodeImpl(String nodeName) {
		this.nodeName = nodeName;
	}
	
	@Override
	public List<ConnectAgent> getConnectAgentList() {
		for (ConnectAgent connectAgent : connectAgentList) {
			if (!connectAgent.isHealthyConnect()) {
				connectAgentList.remove(connectAgent);
			}
		}
		return connectAgentList;
	}
	
	@Override
	public void updateConnectAgentList(List<ConnectAgent> connectAgentListNew) {
		
		connectAgentList.addAllAbsent(connectAgentListNew);
		for (ConnectAgent connectAgent : connectAgentList) {
			if (!connectAgentListNew.contains(connectAgent)) {
				connectAgentList.remove(connectAgent);
				if (logger.isDebugEnabled()) {
					logger.debug("updateConnectAgentList -> remove connect -> {}, {}", this, connectAgent);
				}
			}
		}
	}
	
	@Override
	public boolean isContainsConnectAgent(ConnectAgent connectAgent) {
		return connectAgentList.contains(connectAgent);
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
