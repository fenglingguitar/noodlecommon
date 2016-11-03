package org.fl.noodle.common.connect.agent;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.connect.agent.aop.ConnectStatusMethodInterceptor;
import org.fl.noodle.common.connect.agent.aop.FlowLimitMethodInterceptor;
import org.fl.noodle.common.connect.aop.ConnectThreadLocalStorage;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.performance.ConnectPerformanceNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;

public abstract class AbstractConnectAgent implements ConnectAgent, MethodInterceptor {
	
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
	
	private Object proxy;
	
	private AtomicInteger invalidCount = new AtomicInteger(0);
	
	private ConnectDistinguish connectDistinguish;
	
	private ConcurrentMap<String, ConnectPerformanceNode> connectPerformanceNodeMap = new ConcurrentHashMap<String, ConnectPerformanceNode>();
	
	public AbstractConnectAgent(long connectId, String ip, int port, String url, String type, int connectTimeout, int readTimeout, String encoding, int invalidLimitNum, ConnectDistinguish connectDistinguish, List<MethodInterceptor> methodInterceptorList) {
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
		
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.addInterface(getServiceInterfaces());
		proxyFactory.addAdvice(this);
		if (methodInterceptorList != null && methodInterceptorList.size() > 0) {
			for (Object object : methodInterceptorList) {
				proxyFactory.addAdvice((Advice)object);
			}
		}
		proxyFactory.addAdvice(new ConnectStatusMethodInterceptor(this.connectDistinguish));
		proxyFactory.addAdvice(new FlowLimitMethodInterceptor(this.connectDistinguish));
		proxyFactory.setTarget(this);
		this.proxy = proxyFactory.getProxy();
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
		return proxy;
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		ConnectThreadLocalStorage.put(ConnectThreadLocalStorage.StorageType.AGENT.getCode(), this);
		
		try {
			return invocation.proceed();
		} catch (Throwable e) {
			logger.error("invoke -> method.invoke -> {} -> Exception:{}", this, e.getMessage());
			throw e;
		} finally {
			ConnectThreadLocalStorage.remove(ConnectThreadLocalStorage.StorageType.AGENT.getCode());
		}
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
	
	@Override
	public AtomicInteger getInvalidCount() {
		return invalidCount;
	}
	
	@Override
	public int getInvalidLimitNum() {
		return invalidLimitNum;
	}
	
	@Override
	public ConnectPerformanceNode getConnectPerformanceNode(String methodKay) {
		
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
	
	@Override
	public AtomicBoolean getConnectStatus() {
		return connectStatus;
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
}
