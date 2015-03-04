package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.performance.ConnectPerformanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.connect.expand.monitor.PerformanceMonitor;
import org.fl.noodle.common.connect.expand.monitor.constent.ModuleType;
import org.fl.noodle.common.connect.expand.monitor.constent.MonitorType;

public abstract class AbstractConnectCluster implements ConnectCluster, InvocationHandler {

	private final static Logger logger = LoggerFactory.getLogger(AbstractConnectCluster.class);
	
	private Object serviceProxy;
	
	protected ConnectDistinguish connectDistinguish;
	
	protected PerformanceMonitor performanceMonitor;
	
	public AbstractConnectCluster (Class<?> serviceInterface, ConnectDistinguish connectDistinguish, PerformanceMonitor performanceMonitor) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		serviceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, this);
		this.connectDistinguish = connectDistinguish;
		this.performanceMonitor = performanceMonitor;
	}
	
	@Override
	public Object getProxy() {
		return serviceProxy;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		ConnectManager connectManager = connectDistinguish.getConnectManager();
		if (connectManager == null) {
			if (logger.isErrorEnabled()) {
				logger.error("invoke -> connectDistinguish.getConnectManager return null -> {}", this);
			}
			throw new ConnectInvokeException("no this connect manager");
		}
		
		ConnectPerformanceInfo connectPerformanceInfo = connectManager.getConnectPerformanceInfo(connectDistinguish.getMethodKay(method, args));
		
		long threshold = 200;
		
		if (connectPerformanceInfo != null) {
			threshold = connectPerformanceInfo.getOvertimeLimitThreshold();
		}
		
		try {
			if (performanceMonitor != null && connectPerformanceInfo != null && connectPerformanceInfo.getIsMonitor()) performanceMonitor.before(connectDistinguish.getMethodKay(method, args), MonitorType.CONNECT.getCode(), ModuleType.DEFAULT.getCode(), 0);
			Object object = doInvoke(method, args);
			if (performanceMonitor != null && connectPerformanceInfo != null && connectPerformanceInfo.getIsMonitor()) performanceMonitor.after(connectDistinguish.getModuleName(args), connectDistinguish.getMethodKay(method, args), MonitorType.CONNECT.getCode(), ModuleType.DEFAULT.getCode(), 0, threshold, true);
			return object;
		} catch (Throwable e) {
			if (performanceMonitor != null && connectPerformanceInfo != null && connectPerformanceInfo.getIsMonitor()) performanceMonitor.after(connectDistinguish.getModuleName(args), connectDistinguish.getMethodKay(method, args), MonitorType.CONNECT.getCode(), ModuleType.DEFAULT.getCode(), 0, threshold, true);
			throw e;
		}
	}
	
	protected abstract Object doInvoke(Method method, Object[] args) throws Throwable;
}
