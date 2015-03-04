package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.connect.expand.monitor.PerformanceMonitor;

public class AllConnectCluster extends AbstractConnectCluster {
	
	private final static Logger logger = LoggerFactory.getLogger(AllConnectCluster.class);
	
	public AllConnectCluster(Class<?> serviceInterface, ConnectDistinguish connectDistinguish, PerformanceMonitor performanceMonitor) {
		super(serviceInterface, connectDistinguish, performanceMonitor);
	}

	@Override
	public Object doInvoke(Method method, Object[] args) throws Throwable {
		
		ConnectManager connectManager = connectDistinguish.getConnectManager();
		if (connectManager == null) {
			throw new ConnectInvokeException("no this connect manager");
		}
		
		ConnectNode connectNode = connectManager.getConnectNode(connectDistinguish.getNodeName(args));
		if (connectNode == null) {
			throw new ConnectInvokeException("no this connect node");
		}
		
		Object result = null;
		Throwable resultThrowable = null;	

		for (ConnectAgent connectAgent : connectNode.getConnectAgentList()) {
			try {
				result = method.invoke(connectAgent.getProxy(), args);
			} catch (IllegalAccessException e) {
				if (logger.isErrorEnabled()) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
				}
				resultThrowable = e;
			} catch (IllegalArgumentException e) {
				if (logger.isErrorEnabled()) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
				}
				resultThrowable = e;
			} catch (InvocationTargetException e) {
				if (logger.isErrorEnabled()) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getTargetException().getMessage());
				}
				resultThrowable = e.getTargetException();
			}
		}
		
		if (resultThrowable != null) {
			throw resultThrowable;
		}
		
		return result;
	}
}
