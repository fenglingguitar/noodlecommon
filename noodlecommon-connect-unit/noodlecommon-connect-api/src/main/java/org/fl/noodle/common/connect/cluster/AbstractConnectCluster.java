package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
import org.fl.noodle.common.connect.filter.ConnectFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxy;

public abstract class AbstractConnectCluster implements ConnectCluster, InvocationHandler {

	//private final static Logger logger = LoggerFactory.getLogger(AbstractConnectCluster.class);
	
	private Object serviceProxy;
	
	protected ConnectDistinguish connectDistinguish;
	
	public AbstractConnectCluster (Class<?> serviceInterface, ConnectDistinguish connectDistinguish, List<Object> methodInterceptorList) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		serviceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, this);
		
		if (methodInterceptorList != null) {
			AopProxy aopProxy = new ConnectFilter(serviceInterface, serviceProxy, methodInterceptorList);
			serviceProxy = aopProxy.getProxy();
		}
		
		this.connectDistinguish = connectDistinguish;
	}
	
	@Override
	public Object getProxy() {
		return serviceProxy;
	}
	
	@Override
	public Object getProxy(ClassLoader classLoader) {
		return serviceProxy;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return doInvoke(method, args);
	}
	
	protected abstract Object doInvoke(Method method, Object[] args) throws Throwable;
}
