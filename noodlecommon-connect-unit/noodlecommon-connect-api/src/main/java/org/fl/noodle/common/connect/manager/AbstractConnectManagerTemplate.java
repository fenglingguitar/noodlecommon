package org.fl.noodle.common.connect.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.manager.AbstractConnectManager;
import org.fl.noodle.common.connect.node.ConnectNodeImpl;

public abstract class AbstractConnectManagerTemplate extends AbstractConnectManager {
	
	//private final static Logger logger = LoggerFactory.getLogger(AbstractConnectManagerTemplate.class);
	
	protected Map<String, List<Object>> connectAndNodeInfoMap = null;
	protected Map<String, Object> clusterInfoMap = null;
	protected Map<String, Object> routeInfoMap = null;
	
	protected List<String> addNodeList = null;
	protected List<Object> addConnectList = null;
	protected Map<String, List<Object>> addConnectMappingMap = null;
	protected List<String> addClusterList = null;
	protected List<String> addRouteList = null;
	
	protected List<String> reduceNodeList = null;
	protected List<ConnectAgent> reduceConnectList = null;
	protected Map<String, List<ConnectAgent>> reduceConnectMappingMap = null;
	protected List<String> reduceClusterList = null;
	protected List<String> reduceRouteList = null;
	
	@Override
	protected void destroyConnectAgent() {
		connectNodeMap.clear();
		Set<Long> connectAgentKeySet = connectAgentMap.keySet();
		for (long key : connectAgentKeySet) {
			ConnectAgent connectAgent = connectAgentMap.get(key);
			connectAgent.close();
		}
		connectAgentMap.clear();
	}
	
	@Override
	public void runUpdateAddComponent() {	
		cleanComponent();
		queryInfo();
		addComponent();
	}
	
	@Override
	public void runUpdateReduceComponent() {
		cleanComponent();
		queryInfo();
		reduceComponent();
	}
	
	protected void cleanComponent() {
		connectAndNodeInfoMap = null;
		clusterInfoMap = null;
		routeInfoMap = null;
	}
	
	protected void addComponent() {
		if (connectAndNodeInfoMap != null) {
			getAddNode();
			addNode();
			getAddConnect();
			addConnect();
			getAddConnectMapping();
			addConnectMapping();
		}
		
		if (clusterInfoMap != null) {
			getAddCluster();
			addCluster();
		}
		
		if (routeInfoMap != null) {
			getAddRoute();
			addRoute();
		}
	}
	
	protected void reduceComponent() {
		if (connectAndNodeInfoMap != null) {
			getReduceConnectMapping();
			reduceConnectMapping();
			getReduceConnect();
			reduceConnect();
			getReduceNode();
			reduceNode();
		}
		
		if (clusterInfoMap != null) {
			getReduceCluster();
			reduceCluster();
		}
		
		if (routeInfoMap != null) {
			getReduceRoute();
			reduceRoute(); 
		}
	}
	
	protected void getAddNode() {
		addNodeList = new ArrayList<String>();
		for (String name : connectAndNodeInfoMap.keySet()) {
			if (!connectNodeMap.containsKey(name)) {
				addNodeList.add(name);
			}
		}
	}
	
	protected void addNode() {
		for (String name : addNodeList) {
			connectNodeMap.put(name, new ConnectNodeImpl(name));
		}
	}
	
	protected void getAddConnect() {
		addConnectList = new ArrayList<Object>();
		Map<Long, Object> objectMap = new HashMap<Long, Object>();
		for (List<Object> objectListIt : connectAndNodeInfoMap.values()) {
			for (Object objectIt : objectListIt) {
				if (!objectMap.containsKey(getId(objectIt))) {
					objectMap.put(getId(objectIt), objectIt);
				}
			}
		}
		for (Object objectIt : objectMap.values()) {
			if (!connectAgentMap.containsKey(getId(objectIt))) {
				addConnectList.add(objectIt);
			} else {
				ConnectAgent connectAgent = connectAgentMap.get(getId(objectIt));
				if (!isSameConnect(connectAgent, objectIt)) {
					addConnectList.add(objectIt);
				}
			}
		}
	}
	
	protected void addConnect() {
		for (Object objectIt : addConnectList) {
			ConnectAgent connectAgent = createConnectAgent(objectIt);
			if (connectAgentMap.containsKey(getId(objectIt))) {
				connectAgentMap.remove(getId(objectIt)).close();
			}
			try {
				connectAgent.connect();
				connectAgentMap.put(getId(objectIt), connectAgent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (ConnectAgent connectAgent : connectAgentMap.values()) {
			if (!connectAgent.isHealthyConnect()) {
				try {
					connectAgent.reconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void getAddConnectMapping() {
		addConnectMappingMap = new HashMap<String, List<Object>>();
		for (String name : connectAndNodeInfoMap.keySet()) {
			for (Object objectIt : connectAndNodeInfoMap.get(name)) {
				boolean isHave = false;
				for (ConnectAgent connectAgentIt : connectNodeMap.get(name).getAllConnectAgentList()) {
					if (getId(objectIt) == connectAgentIt.getConnectId() ) {
						isHave = true;
						break;
					}
				}
				if (!isHave) {
					List<Object> addConnectMappingList = addConnectMappingMap.get(name);
					if (addConnectMappingList == null) {
						addConnectMappingMap.put(name, new ArrayList<Object>());
					}
					addConnectMappingMap.get(name).add(objectIt);
				}
			}
		}
	}
	
	protected void addConnectMapping() {
		for (String name : addConnectMappingMap.keySet()) {
			for (Object objectIt : addConnectMappingMap.get(name)) {
				ConnectAgent connectAgent = connectAgentMap.get(getId(objectIt));
				if (connectAgent != null && connectAgent.isHealthyConnect()) {
					connectNodeMap.get(name).addConnectAgent(connectAgent);
				}
			}
		}
	}
	
	protected void getReduceNode() {
		reduceNodeList = new ArrayList<String>();
		for (String name : connectNodeMap.keySet()) {
			if (!connectAndNodeInfoMap.containsKey(name)) {
				reduceNodeList.add(name);
			}
		}
	}
	
	protected void reduceNode() {
		for (String name : reduceNodeList) {
			connectNodeMap.remove(name);
		}
	}

	protected void getReduceConnect() {
		reduceConnectList = new ArrayList<ConnectAgent>();
		Map<Long, Object> objectMap = new HashMap<Long, Object>();
		for (List<Object> objectListIt : connectAndNodeInfoMap.values()) {
			for (Object objectIt : objectListIt) {
				if (!objectMap.containsKey(getId(objectIt))) {
					objectMap.put(getId(objectIt), objectIt);
				}
			}
		}
		for (ConnectAgent connectAgent : connectAgentMap.values()) {
			if (!objectMap.containsKey(connectAgent.getConnectId())) {
				reduceConnectList.add(connectAgent);
			}
		}
	}
	
	protected void reduceConnect() {
		for (ConnectAgent connectAgent : reduceConnectList) {
			connectAgentMap.remove(connectAgent.getConnectId()).close();
		}
	}
	
	protected void getReduceConnectMapping() {
		reduceConnectMappingMap = new HashMap<String, List<ConnectAgent>>();
		for (String name : connectNodeMap.keySet()) {
			for (ConnectAgent connectAgentIt : connectNodeMap.get(name).getAllConnectAgentList()) {
				boolean isHave = false;
				if (connectAndNodeInfoMap.containsKey(name)) {
					for (Object objectIt : connectAndNodeInfoMap.get(name)) {
						if (connectAgentIt.getConnectId() == getId(objectIt)) {
							isHave = true;
							break;
						}
					}
				}
				if (!isHave) {
					List<ConnectAgent> reduceConnectMappingList = reduceConnectMappingMap.get(name);
					if (reduceConnectMappingList == null) {
						reduceConnectMappingMap.put(name, new ArrayList<ConnectAgent>());
					}
					reduceConnectMappingMap.get(name).add(connectAgentIt);
				}
			}
		}
	}
	
	protected void reduceConnectMapping() {
		for (String name : reduceConnectMappingMap.keySet()) {
			for (ConnectAgent connectAgentIt : reduceConnectMappingMap.get(name)) {
				connectNodeMap.get(name).removeConnectAgent(connectAgentIt);
			}
		}
	}
	
	protected void getAddCluster() {
		addClusterList = new ArrayList<String>();
		for (String name : clusterInfoMap.keySet()) {
			if (!connectClusterMap.containsKey(name) || !connectClusterMap.get(name).getType().equals(clusterInfoMap.get(name))) {
				addClusterList.add(name);
			}
		}
	}
	
	protected void addCluster() {
		for (String name : addClusterList) {
			connectClusterMap.put(name, connectClusterFactoryMap.get(clusterInfoMap.get(name)).createConnectCluster(getConnectAgentClass()));
		}
	}

	protected void getReduceCluster() {
		reduceClusterList = new ArrayList<String>();
		for (String name : connectClusterMap.keySet()) {
			if (!clusterInfoMap.containsKey(name)) {
				reduceClusterList.add(name);
			}
		}
	}
	
	protected void reduceCluster() {
		for (String name : reduceClusterList) {
			connectClusterMap.remove(name);
		}
	}
	
	protected void getAddRoute() {
		addRouteList = new ArrayList<String>();
		for (String name : routeInfoMap.keySet()) {
			if (!connectRouteMap.containsKey(name) || !connectRouteMap.get(name).getType().equals(routeInfoMap.get(name))) {
				addRouteList.add(name);
			}
		}
	}

	protected void addRoute() {
		for (String name : addRouteList) {
			connectRouteMap.put(name, connectRouteFactoryMap.get(routeInfoMap.get(name)).createConnectRoute());
		}
	}
	
	protected void getReduceRoute() {
		reduceRouteList = new ArrayList<String>();
		for (String name : connectRouteMap.keySet()) {
			if (!routeInfoMap.containsKey(name)) {
				reduceRouteList.add(name);
			}
		}
	}

	protected void reduceRoute() {
		for (String name : reduceRouteList) {
			connectRouteMap.remove(name);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void getConnectAndNodeInfoMap(Object object) {
		connectAndNodeInfoMap = (Map<String, List<Object>>) object;
	}
	
	@SuppressWarnings("unchecked")
	protected void getClusterInfoMap(Object object) {
		clusterInfoMap = (Map<String, Object>) object;
	}
	
	@SuppressWarnings("unchecked")
	protected void getRouteInfoMap(Object object) {
		routeInfoMap = (Map<String, Object>) object;
	}
	
	protected Long getId(Object object) {
		try {
			return (Long) object.getClass().getMethod("get" + getIdName()).invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected abstract void queryInfo();
	protected abstract String getIdName();
	protected abstract ConnectAgent createConnectAgent(Object object);
	protected abstract boolean isSameConnect(ConnectAgent connectAgent, Object object);
	protected abstract Class<?> getConnectAgentClass();
}
