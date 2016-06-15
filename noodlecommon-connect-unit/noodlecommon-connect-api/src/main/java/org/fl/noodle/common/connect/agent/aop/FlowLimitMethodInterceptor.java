package org.fl.noodle.common.connect.agent.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.aop.ConnectThreadLocalStorage;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectDowngradeException;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.performance.ConnectPerformanceInfo;
import org.fl.noodle.common.connect.performance.ConnectPerformanceNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowLimitMethodInterceptor implements MethodInterceptor {
	
	private ConnectDistinguish connectDistinguish;
	
	private final static Logger logger = LoggerFactory.getLogger(FlowLimitMethodInterceptor.class);
	
	public FlowLimitMethodInterceptor() {
		
	}
	
	public FlowLimitMethodInterceptor(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		if (connectDistinguish == null) {
			logger.error("invoke -> connectDistinguish is null");
			throw new ConnectInvokeException("connectDistinguish is null");
		}
		
		ConnectManager connectManager = connectDistinguish.getConnectManager();
		if (connectManager == null) {
			logger.error("invoke -> connectDistinguish.getConnectManager return null -> {}", this);
			throw new ConnectInvokeException("no this connect manager");
		}
		
		ConnectAgent connectAgent = (ConnectAgent)ConnectThreadLocalStorage.get(ConnectThreadLocalStorage.StorageType.AGENT);
		if (connectAgent == null) {
			logger.error("invoke -> ConnectThreadLocalStorage.get agent return null");
			throw new ConnectInvokeException("no this connect agent");
		}
		
		ConnectPerformanceInfo connectPerformanceInfo = connectManager.getConnectPerformanceInfo(connectDistinguish.getMethodKay(invocation.getMethod(), invocation.getArguments()));
		
		ConnectPerformanceNode connectPerformanceNode = connectAgent.getConnectPerformanceNode(connectDistinguish.getMethodKay(invocation.getMethod(), invocation.getArguments()));
		
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
		
		long start = System.currentTimeMillis();
		
		try {
			return invocation.proceed();
		} finally {
			long costTime = System.currentTimeMillis() - start;			
			connectPerformanceNode.addTotalTime(costTime);
			connectPerformanceNode.addTotalCount();
			if (connectPerformanceInfo != null) {				
				if (costTime > connectPerformanceInfo.getOvertimeThreshold()) {
					connectPerformanceNode.addOvertimeCount();
				}
			}
		}
	}
	
	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}
}
