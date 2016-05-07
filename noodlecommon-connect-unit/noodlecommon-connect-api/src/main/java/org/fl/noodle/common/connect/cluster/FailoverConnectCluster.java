package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.exception.ConnectNoAliveException;
import org.fl.noodle.common.connect.exception.ConnectResetException;
import org.fl.noodle.common.connect.exception.ConnectTimeoutException;
import org.fl.noodle.common.connect.exception.ConnectUnableException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.route.ConnectRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.connect.expand.monitor.PerformanceMonitor;

public class FailoverConnectCluster extends AbstractConnectCluster {
	
	private final static Logger logger = LoggerFactory.getLogger(FailoverConnectCluster.class);
	
	public FailoverConnectCluster(Class<?> serviceInterface, ConnectDistinguish connectDistinguish, PerformanceMonitor performanceMonitor) {
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
				
		ConnectRoute connectRoute = connectManager.getConnectRoute(connectDistinguish.getRouteName(args));
		if (connectRoute == null) {
			throw new ConnectInvokeException("no this connect route");
		}
		
		List<ConnectAgent> connectAgentListSelected = new LinkedList<ConnectAgent>();
				
		ConnectAgent connectAgent = null;		
		do {
			connectAgent = connectRoute.selectConnect(connectNode.getConnectAgentList(), connectAgentListSelected, connectDistinguish.getMethodKay(method, args));
			if (connectAgent != null) {
				connectAgentListSelected.add(connectAgent);
				try {
					return method.invoke(connectAgent.getProxy(), args);
				} catch (IllegalAccessException e) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
					throw e;
				} catch (IllegalArgumentException e) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
					throw e;
				} catch (InvocationTargetException e) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getTargetException().getMessage());
					if (e.getTargetException() instanceof ConnectUnableException
							|| e.getTargetException() instanceof ConnectResetException
								|| e.getTargetException() instanceof ConnectTimeoutException) {
						continue;
					} else {
						throw e.getTargetException();
					}
				}
			} else {
				connectManager.runUpdate();
				throw new ConnectNoAliveException("all connect agent is no alive");
			}
		} while (connectAgent != null);
		
		return null;
	}
}
