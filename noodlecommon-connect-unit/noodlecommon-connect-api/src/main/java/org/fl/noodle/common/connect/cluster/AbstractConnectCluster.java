package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.fl.noodle.common.connect.manager.ConnectManager;
import org.fl.noodle.common.connect.node.ConnectNode;
import org.fl.noodle.common.connect.route.ConnectRoute;
import org.springframework.aop.framework.ProxyFactory;

public abstract class AbstractConnectCluster implements ConnectCluster, MethodInterceptor {

	//private final static Logger logger = LoggerFactory.getLogger(AbstractConnectCluster.class);
	
	private Object proxy;
	
	protected ConnectDistinguish connectDistinguish;
	
	public AbstractConnectCluster (Class<?> serviceInterface, ConnectDistinguish connectDistinguish, List<MethodInterceptor> methodInterceptorList) {
		
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.addInterface(serviceInterface);
		if (methodInterceptorList != null && methodInterceptorList.size() > 0) {
			for (Object object : methodInterceptorList) {
				proxyFactory.addAdvice((Advice)object);
			}
			
		}
		proxyFactory.addAdvice(this);
		this.proxy = proxyFactory.getProxy();

		this.connectDistinguish = connectDistinguish;
	}
	
	@Override
	public Object getProxy() {
		return proxy;
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return doInvoke(invocation.getMethod(), invocation.getArguments());
	}
	
	protected abstract Object doInvoke(Method method, Object[] args) throws Throwable;
	
	protected ConnectManager getConnectManager() throws ConnectInvokeException {
		ConnectManager connectManager = connectDistinguish.getConnectManager();
		if (connectManager == null) {
			throw new ConnectInvokeException("no this connect manager");
		}
		return connectManager;
	}
	
	protected ConnectNode getConnectNode(Object[] args) throws ConnectInvokeException {
		ConnectNode connectNode = getConnectManager().getConnectNode(connectDistinguish.getNodeName(args));
		if (connectNode == null) {
			throw new ConnectInvokeException("no this connect node");
		}
		return connectNode;
	}
	
	protected ConnectRoute getConnectRoute(Object[] args) throws ConnectInvokeException {
		ConnectRoute connectRoute = getConnectManager().getConnectRoute(connectDistinguish.getRouteName(args));
		if (connectRoute == null) {
			throw new ConnectInvokeException("no this connect route");
		}
		return connectRoute;
	}
}
