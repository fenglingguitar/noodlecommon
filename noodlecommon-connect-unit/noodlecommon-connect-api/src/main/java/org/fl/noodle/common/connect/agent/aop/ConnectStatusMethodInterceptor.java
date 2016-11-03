package org.fl.noodle.common.connect.agent.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.aop.ConnectThreadLocalStorage;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.exception.ConnectRefusedException;
import org.fl.noodle.common.connect.exception.ConnectResetException;
import org.fl.noodle.common.connect.exception.ConnectTimeoutException;
import org.fl.noodle.common.connect.exception.ConnectUnableException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectStatusMethodInterceptor implements MethodInterceptor {
	
	private ConnectDistinguish connectDistinguish;
	
	private final static Logger logger = LoggerFactory.getLogger(ConnectStatusMethodInterceptor.class);
	
	public ConnectStatusMethodInterceptor() {
		
	}
	
	public ConnectStatusMethodInterceptor(ConnectDistinguish connectDistinguish) {
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
			logger.error("invoke -> connectDistinguish.getConnectManager return null");
			throw new ConnectInvokeException("no this connect manager");
		}
		
		ConnectAgent connectAgent = (ConnectAgent)ConnectThreadLocalStorage.get(ConnectThreadLocalStorage.StorageType.AGENT.getCode());
		if (connectAgent == null) {
			logger.error("invoke -> ConnectThreadLocalStorage.get agent return null");
			throw new ConnectInvokeException("no this connect agent");
		}
		
		if (connectAgent.getConnectStatus().get() == false) {
			logger.error("invoke -> connect status is false -> {}", this);
			throw new ConnectUnableException("connect disable for the net http connect agent");
		}
		
		try {
			return invocation.proceed();
		} catch (Throwable e) {
			if (e instanceof ConnectRefusedException
					|| e instanceof ConnectResetException
						|| e instanceof ConnectTimeoutException) {
				if (connectAgent.getInvalidCount().incrementAndGet() >= connectAgent.getInvalidLimitNum()) {					
					connectAgent.getConnectStatus().set(false);
					logger.debug("invoke -> set connect status to false -> {}, invalidLimitNum:{}, invalidCount:{}", this, connectAgent.getInvalidLimitNum(), connectAgent.getInvalidCount().get());
				}
			} 
			throw e;
		}
	}
	
	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}
}
