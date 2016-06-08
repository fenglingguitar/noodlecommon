package org.fl.noodle.common.connect.cluster;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public abstract class AbstractConnectCluster implements ConnectCluster, InvocationHandler {

	//private final static Logger logger = LoggerFactory.getLogger(AbstractConnectCluster.class);
	
	private Object serviceProxy;
	
	protected ConnectDistinguish connectDistinguish;
	
	public AbstractConnectCluster (Class<?> serviceInterface, ConnectDistinguish connectDistinguish) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		serviceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, this);
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
