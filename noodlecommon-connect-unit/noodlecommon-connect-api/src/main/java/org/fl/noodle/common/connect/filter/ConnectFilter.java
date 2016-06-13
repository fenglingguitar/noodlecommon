package org.fl.noodle.common.connect.filter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.fl.noodle.common.connect.aop.NoodleReflectiveMethodInvocation;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

public class ConnectFilter implements AopProxy, InvocationHandler {
	
	private List<Object> methodInterceptorList = new ArrayList<Object>();
	
	private Object target;
	
	private Object proxy;
	
	public ConnectFilter(Class<?> serviceInterface, Object target, List<Object> methodInterceptorList) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, this);
		this.target = target;
		this.methodInterceptorList = methodInterceptorList;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (target == null) {
			throw new ConnectInvokeException("aopProxy is null");
		}
		
		Object retVal = null;
		
		if (methodInterceptorList.isEmpty()) {
			retVal = AopUtils.invokeJoinpointUsingReflection(target, method, args);
		}
		else {
			retVal = new NoodleReflectiveMethodInvocation(proxy, target, method, args, target.getClass(), methodInterceptorList).proceed();
		}
		
		return retVal;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return proxy;
	}
}
