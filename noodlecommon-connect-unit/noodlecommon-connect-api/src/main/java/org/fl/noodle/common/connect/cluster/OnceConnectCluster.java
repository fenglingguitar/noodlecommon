package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.exception.ConnectNoAliveException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.route.ConnectRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.connect.expand.monitor.PerformanceMonitor;

public class OnceConnectCluster extends AbstractConnectCluster {
	
	private final static Logger logger = LoggerFactory.getLogger(OnceConnectCluster.class);
	
	public OnceConnectCluster(Class<?> serviceInterface, ConnectDistinguish connectDistinguish, PerformanceMonitor performanceMonitor) {
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
				throw e.getTargetException();
			}
		} else {
			connectManager.runUpdate();
			throw new ConnectNoAliveException("all connect agent is no alive");
		}
	}
}
