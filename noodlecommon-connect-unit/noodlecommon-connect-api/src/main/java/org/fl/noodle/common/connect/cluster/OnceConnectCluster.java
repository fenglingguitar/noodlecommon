package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.Method;
import java.util.LinkedList;

import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.exception.ConnectNoAliveException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.route.ConnectRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

public class OnceConnectCluster extends AbstractConnectCluster {
	
	private final static Logger logger = LoggerFactory.getLogger(OnceConnectCluster.class);
	
	public OnceConnectCluster(Class<?> serviceInterface, ConnectDistinguish connectDistinguish) {
		super(serviceInterface, connectDistinguish);
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
				
		ConnectAgent connectAgent = null;		

		connectAgent = connectRoute.selectConnect(connectNode.getConnectAgentList(), new LinkedList<ConnectAgent>(), connectDistinguish.getMethodKay(method, args));
		if (connectAgent != null) {
			try {
				return AopUtils.invokeJoinpointUsingReflection(connectAgent.getProxy(), method, args);
			} catch (Throwable e) {
				logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
				throw e;
			}
		} else {
			connectManager.runUpdate();
			throw new ConnectNoAliveException("all connect agent is no alive");
		}
	}
}
