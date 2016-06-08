package org.fl.noodle.common.connect.filter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.fl.noodle.common.connect.aop.NoodleReflectiveMethodInvocation;
import org.fl.noodle.common.connect.exception.ConnectInvokeException;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;

public class ConnectFilter implements AopProxy, InvocationHandler, InitializingBean {
	
	private List<Object> methodInterceptorList = new ArrayList<Object>();
	
	private AopProxy aopProxy;

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (aopProxy == null || aopProxy.getProxy() == null) {
			throw new ConnectInvokeException("aopProxy is null");
		}
		
		Object retVal = null;
		
		if (methodInterceptorList.isEmpty()) {
			retVal = AopUtils.invokeJoinpointUsingReflection(aopProxy.getProxy(), method, args);
		}
		else {
			retVal = new NoodleReflectiveMethodInvocation(proxy, aopProxy.getProxy(), method, args, aopProxy.getProxy().getClass(), methodInterceptorList).proceed();
		}
		
		return retVal;
	}

	public void setMethodInterceptorList(List<Object> methodInterceptorList) {
		this.methodInterceptorList = methodInterceptorList;
	}
	
	public void setAopProxy(AopProxy aopProxy) {
		this.aopProxy = aopProxy;
	}

	@Override
	public Object getProxy() {
		return null;
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return null;
	}
}
