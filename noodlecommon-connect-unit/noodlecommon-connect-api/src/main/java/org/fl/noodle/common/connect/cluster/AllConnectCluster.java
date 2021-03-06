package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.fl.noodle.common.connect.agent.ConnectAgent;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.exception.ConnectNoAliveException;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

public class AllConnectCluster extends AbstractConnectCluster {
	
	private final static Logger logger = LoggerFactory.getLogger(AllConnectCluster.class);
	
	public AllConnectCluster(Class<?> serviceInterface, ConnectDistinguish connectDistinguish, List<MethodInterceptor> methodInterceptorList, String type) {
		super(serviceInterface, connectDistinguish, methodInterceptorList, type);
	}

	@Override
	public Object doInvoke(Method method, Object[] args) throws Throwable {
		
		ConnectNode connectNode = getConnectNode(args);
		
		if (connectNode.getHealthyConnectAgentList().isEmpty()) {
			getConnectManager().runUpdate();
			throw new ConnectNoAliveException("all connect agent is no alive");
		}
		
		Object result = null;
		Throwable resultThrowable = null;
		
		for (ConnectAgent connectAgent : connectNode.getHealthyConnectAgentList()) {
			try {
				result = AopUtils.invokeJoinpointUsingReflection(connectAgent.getProxy(), method, args);
			} catch (Throwable e) {
				logger.error("doInvoke -> method.invoke -> Exception:{}", e.getMessage());
				resultThrowable = e;
			}
		}
		
		if (resultThrowable != null) {
			throw resultThrowable;
		}
		
		return result;
	}
}
