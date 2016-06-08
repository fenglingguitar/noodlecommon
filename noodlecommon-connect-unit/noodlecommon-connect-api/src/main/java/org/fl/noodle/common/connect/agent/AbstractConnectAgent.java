package org.fl.noodle.common.connect.agent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.fl.noodle.common.connect.aop.ConnectThreadLocalStorage;
import org.fl.noodle.common.connect.aop.NoodleReflectiveMethodInvocation;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectDowngradeException;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.exception.ConnectRefusedException;
import org.fl.noodle.common.connect.exception.ConnectResetException;
import org.fl.noodle.common.connect.exception.ConnectTimeoutException;
import org.fl.noodle.common.connect.exception.ConnectUnableException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.performance.ConnectPerformanceInfo;
import org.fl.noodle.common.connect.performance.ConnectPerformanceNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

public abstract class AbstractConnectAgent implements ConnectAgent, InvocationHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractConnectAgent.class);
	
	protected long connectId;

	protected String ip;
	protected int port;
	protected String url;
	protected String type;
	
	protected int connectTimeout;
	protected int readTimeout;
	
	protected String encoding;
	
	private int invalidLimitNum;
	
	private volatile int weight = 1;

	private AtomicBoolean connectStatus = new AtomicBoolean(false);
	
	private Object serviceProxy;
	
	private AtomicInteger invalidCount = new AtomicInteger(0);
	
	private ConnectDistinguish connectDistinguish;
	
	private ConcurrentMap<String, ConnectPerformanceNode> connectPerformanceNodeMap = new ConcurrentHashMap<String, ConnectPerformanceNode>();
	
	private List<Object> methodInterceptorList = new ArrayList<Object>();
	
	public AbstractConnectAgent(long connectId, String ip, int port, String url, String type, int connectTimeout, int readTimeout, String encoding, int invalidLimitNum, ConnectDistinguish connectDistinguish, List<Object> methodInterceptorList) {
		this.connectId = connectId;
		this.ip = ip;
		this.port = port;
		this.url = url;
		this.type = type;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.encoding = encoding;
		this.invalidLimitNum = invalidLimitNum;
		this.connectDistinguish = connectDistinguish;
		this.methodInterceptorList = methodInterceptorList;
		
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = getServiceInterfaces();
		this.serviceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, this);
	}
	
	@Override
	public void connect() throws Exception {
		
		try {
			connectActual();
		} catch (Exception e) {
			logger.error("connect -> connectActual -> {} -> Exception:{}", this, e.getMessage());
			connectStatus.set(false);
			throw e;
		}
		connectStatus.set(true);
		invalidCount.set(0);

		logger.debug("connect -> connect is ok -> {}, connectStatus:{}, invalidLimitNum:{}, invalidCount:{}", this, connectStatus.get(), invalidLimitNum, invalidCount.get());
	}
	
	@Override
	public void reconnect() throws Exception {
		
		try {
			reconnectActual();
		} catch (Exception e) {
			logger.error("connect -> reconnectActual -> {} -> Exception:{}", this, e.getMessage());
			connectStatus.set(false);
			throw e;
		}
		connectStatus.set(true);
		invalidCount.set(0);

		logger.debug("reconnect -> reconnect is ok -> {}, connectStatus:{}, invalidLimitNum:{}, invalidCount:{}", this, connectStatus.get(), invalidLimitNum, invalidCount.get());
	}
	
	@Override
	public void close() {
		
		connectStatus.set(false);
		closeActual();
		
		logger.debug("close -> close is ok -> {}, connectStatus:{}, invalidLimitNum:{}, invalidCount:{}", this, connectStatus.get(), invalidLimitNum, invalidCount.get());
	}
	
	protected abstract void connectActual() throws Exception;
	protected abstract void reconnectActual() throws Exception;	
	protected abstract void closeActual();
	protected abstract Class<?> getServiceInterfaces();

	@Override
	public boolean isHealthyConnect() {
		return connectStatus.get();
	}
	
	@Override
	public long getConnectId() {
		return connectId;
	}
	
	@Override
	public boolean isSameConnect(String ip, int port, String url, String type) {
		return this.ip.equals(ip) 
				&& this.port == port
				&& (this.url != null ? this.url.equals(url) : true)
				&& this.type.equals(type);
	}
	
	@Override
	public Object getProxy() {
		return serviceProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (connectStatus.get() == false) {
			logger.error("invoke -> connect status is false -> {}", this);
			throw new ConnectUnableException("connect disable for the net http connect agent");
		}
		
		ConnectManager connectManager = connectDistinguish.getConnectManager();
		if (connectManager == null) {
			logger.error("invoke -> connectDistinguish.getConnectManager return null -> {}", this);
			throw new ConnectInvokeException("no this connect manager");
		}
		
		ConnectPerformanceInfo connectPerformanceInfo = connectManager.getConnectPerformanceInfo(connectDistinguish.getMethodKay(method, args));
		
		ConnectPerformanceNode connectPerformanceNode = getConnectPerformanceNode(connectDistinguish.getMethodKay(method, args));
		
		if (connectPerformanceInfo != null) {
			
			if (connectPerformanceInfo.getIsDowngrade() == ConnectPerformanceInfo.IsDowngrade.YES) {
				if (connectPerformanceInfo.getDowngradeType() == ConnectPerformanceInfo.DowngradeType.AVGTIME) {
					if (connectPerformanceNode.getAvgTime() > connectPerformanceInfo.getAvgTimeLimitThreshold()) {
						if (connectPerformanceInfo.getReturnType() == ConnectPerformanceInfo.ReturnType.T_EXCEPTION) {
							throw new ConnectDowngradeException("connect downgrade for the net http connect agent"); 
						} else if (connectPerformanceInfo.getReturnType() == ConnectPerformanceInfo.ReturnType.R_NULL) {
							return null;
						}
					}						
				} else if (connectPerformanceInfo.getDowngradeType() == ConnectPerformanceInfo.DowngradeType.OVERTIME) {
					if (connectPerformanceNode.getOvertimeCount() > connectPerformanceInfo.getOvertimeLimitThreshold()) {
						if (connectPerformanceInfo.getReturnType() == ConnectPerformanceInfo.ReturnType.T_EXCEPTION) {
							throw new ConnectDowngradeException("connect downgrade for the net http connect agent"); 
						} else if (connectPerformanceInfo.getReturnType() == ConnectPerformanceInfo.ReturnType.R_NULL) {
							return null;
						}
					}
				}
			}
		}
				
		try {
			
			ConnectThreadLocalStorage.put(ConnectThreadLocalStorage.StorageType.AGENT, this);
			
			long start = System.currentTimeMillis();
			
			Object retVal = null;
			
			if (methodInterceptorList.isEmpty()) {
				retVal = AopUtils.invokeJoinpointUsingReflection(this, method, args);
			}
			else {
				retVal = new NoodleReflectiveMethodInvocation(proxy, this, method, args, this.getClass(), methodInterceptorList).proceed();
			}

			long costTime = System.currentTimeMillis() - start;			
			connectPerformanceNode.addTotalTime(costTime);
			connectPerformanceNode.addTotalCount();
			if (connectPerformanceInfo != null) {				
				if (costTime > connectPerformanceInfo.getOvertimeThreshold()) {
					connectPerformanceNode.addOvertimeCount();
				}
			}
			
			return retVal;
		} catch (Throwable e) {
			logger.error("invoke -> method.invoke -> {} -> Exception:{}", this, e.getMessage());
			if (e instanceof ConnectRefusedException
					|| e instanceof ConnectResetException
						|| e instanceof ConnectTimeoutException) {
				if (invalidCount.incrementAndGet() >= invalidLimitNum) {					
					connectStatus.set(false);
					logger.debug("invoke -> set connect status to false -> {}, invalidLimitNum:{}, invalidCount:{}", this, invalidLimitNum, invalidCount.get());
				}
			} 
			
			throw e;
		} finally {
			ConnectThreadLocalStorage.remove(ConnectThreadLocalStorage.StorageType.AGENT);
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
					.append("connectId:").append(connectId).append(", ")
					.append("ip:").append(ip).append(", ")
					.append("port:").append(port).append(", ")
					.append("url:").append(url).append(", ")
					.append("type:").append(type).append(", ")
					.append("connectTimeout:").append(connectTimeout).append(", ")
					.append("readTimeout:").append(readTimeout).append(", ")
					.append("encoding:").append(encoding)
					.toString();
	}
	
	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}
	
	@Override
	public void calculate() {
		for (Entry<String, ConnectPerformanceNode> connectPerformanceNodeEntry : connectPerformanceNodeMap.entrySet()) {
			connectPerformanceNodeEntry.getValue().calculate();
		}
	}

	@Override
	public long getAvgTime(String methodKey) {
		return getConnectPerformanceNode(methodKey).getAvgTime();
	}
	
	private ConnectPerformanceNode getConnectPerformanceNode(String methodKay) {
		
		ConnectPerformanceNode connectPerformanceNode = connectPerformanceNodeMap.get(methodKay);
		if (connectPerformanceNode == null) {
			connectPerformanceNode = new ConnectPerformanceNode();
			ConnectPerformanceNode connectPerformanceNodeBack =  connectPerformanceNodeMap.putIfAbsent(methodKay, connectPerformanceNode);
			if (connectPerformanceNodeBack != null) {
				connectPerformanceNode = connectPerformanceNodeBack;
			}
		}
		
		return connectPerformanceNode;
	}
}
