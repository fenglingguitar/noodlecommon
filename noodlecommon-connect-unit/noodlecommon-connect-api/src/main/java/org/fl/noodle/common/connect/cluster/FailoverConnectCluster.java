package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectNoAliveException;
import org.fl.noodle.common.connect.exception.ConnectResetException;
import org.fl.noodle.common.connect.exception.ConnectTimeoutException;
import org.fl.noodle.common.connect.exception.ConnectUnableException;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.route.ConnectRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

public class FailoverConnectCluster extends AbstractConnectCluster {
	
	private final static Logger logger = LoggerFactory.getLogger(FailoverConnectCluster.class);
	
	public FailoverConnectCluster(Class<?> serviceInterface, ConnectDistinguish connectDistinguish, List<MethodInterceptor> methodInterceptorList, String type) {
		super(serviceInterface, connectDistinguish, methodInterceptorList, type);
	}

	@Override
	public Object doInvoke(Method method, Object[] args) throws Throwable {
		
		ConnectNode connectNode = getConnectNode(args);
		ConnectRoute connectRoute = getConnectRoute(args);
		
		List<ConnectAgent> connectAgentListSelected = new LinkedList<ConnectAgent>();
				
		ConnectAgent connectAgent = null;		
		do {
			connectAgent = connectRoute.selectConnect(connectNode.getHealthyConnectAgentList(), connectAgentListSelected, connectDistinguish.getMethodKay(method, args));
			if (connectAgent != null) {
				connectAgentListSelected.add(connectAgent);
				try {
					return AopUtils.invokeJoinpointUsingReflection(connectAgent.getProxy(), method, args);
				} catch (Throwable e) {
					logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
					if (e instanceof ConnectUnableException
							|| e instanceof ConnectResetException
								|| e instanceof ConnectTimeoutException) {
						continue;
					} else {
						throw e;
					}
				}
			} else {
				getConnectManager().runUpdate();
				throw new ConnectNoAliveException("all connect agent is no alive");
			}
		} while (connectAgent != null);
		
		return null;
	}
}
