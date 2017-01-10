package org.fl.noodle.common.connect.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.agent.ConnectAgentFactory;
import org.fl.noodle.common.connect.cluster.ConnectCluster;
import org.fl.noodle.common.connect.cluster.ConnectClusterFactory;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.performance.ConnectPerformanceInfo;
import org.fl.noodle.common.connect.route.ConnectRoute;
import org.fl.noodle.common.connect.route.ConnectRouteFactory;
import org.fl.noodle.common.connect.serialize.ConnectSerializeFactory;
import org.fl.noodle.common.util.thread.ExecutorThreadFactory;

public abstract class AbstractConnectManager implements ConnectManager {
	
	//private final static Logger logger = LoggerFactory.getLogger(AbstractConnectManager.class);
	
	private long calculateAvgTimeInterval = 5000;
	
	protected Map<String, ConnectAgentFactory> connectAgentFactoryMap;
	protected Map<String, ConnectClusterFactory> connectClusterFactoryMap;
	protected Map<String, ConnectRouteFactory> connectRouteFactoryMap;
	protected Map<String, ConnectSerializeFactory> connectSerializeFactoryMap;
	
	protected ConcurrentMap<String, ConnectNode> connectNodeMap = new ConcurrentHashMap<String, ConnectNode>();
	protected ConcurrentMap<Long, ConnectAgent> connectAgentMap = new ConcurrentHashMap<Long, ConnectAgent>();
	protected ConcurrentMap<String, ConnectCluster> connectClusterMap = new ConcurrentHashMap<String, ConnectCluster>();
	protected ConcurrentMap<String, ConnectRoute> connectRouteMap = new ConcurrentHashMap<String, ConnectRoute>();
	
	protected ConcurrentMap<String, ConnectPerformanceInfo> connectPerformanceInfoMap = new ConcurrentHashMap<String, ConnectPerformanceInfo>();

	protected ExecutorService executorService = Executors.newSingleThreadExecutor(new ExecutorThreadFactory(this.getClass().getName()));
	
	private long updateLevel;
	
	protected ConnectManagerPool connectManagerPool;
	
	@Override
	public void start() {
		(new Timer(true)).schedule(new TimerTask() {
			public void run() {
				for (Entry<Long, ConnectAgent> entry : connectAgentMap.entrySet()) {
					entry.getValue().calculate();
				}
			}
		}, calculateAvgTimeInterval, calculateAvgTimeInterval);
	}
	
	@Override
	public void destroy() {
		destroyConnectAgent();	
	}
	
	@Override
	public void runUpdate() {
		connectManagerPool.runUpdate();
	}
	
	@Override
	public void runUpdateNow() {
		connectManagerPool.runUpdateNow();
	}
	
	@Override
	public ConnectNode getConnectNode(String nodeName) {
		return connectNodeMap.get(nodeName);
	}

	@Override
	public ConnectAgent getConnectAgent(long connectId) {
		return connectAgentMap.get(connectId);
	}
	
	@Override
	public ConnectCluster getConnectCluster(String clusterName) {
		return connectClusterMap.get(clusterName);
	}
	
	@Override
	public ConnectRoute getConnectRoute(String routeName) {
		return connectRouteMap.get(routeName);
	}
	
	@Override
	public ConnectPerformanceInfo getConnectPerformanceInfo(String methodKey) {
		return connectPerformanceInfoMap.get(methodKey);
	}
	
	protected abstract void destroyConnectAgent();
	
	public void setCalculateAvgTimeInterval(long calculateAvgTimeInterval) {
		this.calculateAvgTimeInterval = calculateAvgTimeInterval;
	}

	public void setConnectAgentFactoryMap(Map<String, ConnectAgentFactory> connectAgentFactoryMap) {
		this.connectAgentFactoryMap = connectAgentFactoryMap;
	}

	public void setConnectClusterFactoryMap(Map<String, ConnectClusterFactory> connectClusterFactoryMap) {
		this.connectClusterFactoryMap = connectClusterFactoryMap;
	}

	public void setConnectRouteFactoryMap(
			Map<String, ConnectRouteFactory> connectRouteFactoryMap) {
		this.connectRouteFactoryMap = connectRouteFactoryMap;
	}

	public void setConnectSerializeFactoryMap(Map<String, ConnectSerializeFactory> connectSerializeFactoryMap) {
		this.connectSerializeFactoryMap = connectSerializeFactoryMap;
	}

	@Override
	public long getUpdateLevel() {
		return updateLevel;
	}

	public void setUpdateLevel(long updateLevel) {
		this.updateLevel = updateLevel;
	}
	
	public void setConnectManagerPool(ConnectManagerPool connectManagerPool) {
		this.connectManagerPool = connectManagerPool;
	}
}
