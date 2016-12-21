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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.util.thread.ExecutorThreadFactory;
import org.fl.noodle.common.util.thread.Stopping;

public abstract class AbstractConnectManager implements ConnectManager {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractConnectManager.class);
	
	private long suspendTime = 60000;
	
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
	
	protected volatile boolean stopSign = false;
	private Stopping stopping = new Stopping();
	
	private long bootPriority;
	
	@Override
	public void start() {
		
		stopping.stopInit(1);
		
		runUpdateNow();
		
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				while (!stopSign) {
					suspendUpdate();
					updateConnectAgent();
				}
				destroyConnectAgent();
				stopping.stopDo();
			}
		});
		
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
		stopSign = true;
		do {				
			runUpdate();
		} while (!stopping.stopWait(1000));
		executorService.shutdown();
	}
	
	protected synchronized void suspendUpdate() {
		try {
			wait(suspendTime);
		} catch (InterruptedException e) {
			logger.error("suspendUpdateConnectAgent -> wait -> Exception:{}", e.getMessage());
		}
	}
	
	@Override
	public synchronized void runUpdate() {
		notifyAll();
	}
	
	@Override
	public synchronized void runUpdateNow() {
		updateConnectAgent();
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
	
	protected abstract void updateConnectAgent();
	protected abstract void destroyConnectAgent();

	public void setSuspendTime(long suspendTime) {
		this.suspendTime = suspendTime;
	}
	
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
	public long getBootPriority() {
		return bootPriority;
	}

	public void setBootPriority(long bootPriority) {
		this.bootPriority = bootPriority;
	}
}
