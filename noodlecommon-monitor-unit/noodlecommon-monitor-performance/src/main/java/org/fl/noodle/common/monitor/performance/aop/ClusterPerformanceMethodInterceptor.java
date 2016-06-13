package org.fl.noodle.common.monitor.performance.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.performance.ConnectPerformanceInfo;
import org.fl.noodle.common.monitor.performance.client.PerformanceMonitor;
import org.fl.noodle.common.monitor.performance.constent.ModuleType;
import org.fl.noodle.common.monitor.performance.constent.MonitorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterPerformanceMethodInterceptor implements MethodInterceptor {

	private PerformanceMonitor performanceMonitor;
	
	private ConnectDistinguish connectDistinguish;
	
	private final static Logger logger = LoggerFactory.getLogger(ClusterPerformanceMethodInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		if (performanceMonitor == null) {
			logger.error("invoke -> performanceMonitor is null");
			throw new ConnectInvokeException("performanceMonitor is null");
		}
		
		if (connectDistinguish == null) {
			logger.error("invoke -> connectDistinguish is null");
			throw new ConnectInvokeException("connectDistinguish is null");
		}
		
		ConnectManager connectManager = connectDistinguish.getConnectManager();
		if (connectManager == null) {
			logger.error("invoke -> connectDistinguish.getConnectManager return null");
			throw new ConnectInvokeException("no this connect manager");
		}
		
		ConnectPerformanceInfo connectPerformanceInfo = connectManager.getConnectPerformanceInfo(connectDistinguish.getMethodKay(invocation.getMethod(), invocation.getArguments()));
		
		long threshold = 200;
		
		if (connectPerformanceInfo != null) {
			threshold = connectPerformanceInfo.getOvertimeLimitThreshold();
		}
		
		if (performanceMonitor != null 
				&& connectPerformanceInfo != null 
					&& connectPerformanceInfo.getIsMonitor()) {
			performanceMonitor.before(
					connectDistinguish.getMethodKay(invocation.getMethod(), invocation.getArguments()), 
					MonitorType.CONNECT.getCode(), 
					ModuleType.DEFAULT.getCode(), 
					"0");
		}
		
		boolean isCorrect = true;
		
		try {
			Object result = invocation.proceed();
			if (result == null) {
				isCorrect = false;
			}
			return result;
		} catch (Throwable e) {
			isCorrect = false;
			throw e;
		} finally {
			if (performanceMonitor != null 
					&& connectPerformanceInfo != null 
						&& connectPerformanceInfo.getIsMonitor()) {
				performanceMonitor.after(
						connectDistinguish.getModuleName(invocation.getArguments()), 
						connectDistinguish.getMethodKay(invocation.getMethod(), invocation.getArguments()), 
						MonitorType.CONNECT.getCode(), 
						ModuleType.DEFAULT.getCode(), 
						"0", 
						threshold, 
						isCorrect);
			}
		}
	}

	public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
		this.performanceMonitor = performanceMonitor;
	}
	
	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}
}
