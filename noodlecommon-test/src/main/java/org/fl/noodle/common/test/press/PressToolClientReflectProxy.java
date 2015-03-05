package org.fl.noodle.common.test.press;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PressToolClientReflectProxy extends AbstractPressToolClient implements InvocationHandler {
	
	private Object serviceProxy;
	
	public static Object getInstance(Class<?> serviceInterface, Object serviceProxy, long planExecuteNum) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, new PressToolClientReflectProxy(serviceProxy, planExecuteNum));
	}
	
	public static Object getInstance(Class<?> serviceInterface, Object serviceProxy, long planExecuteNum, int threadNum) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, new PressToolClientReflectProxy(serviceProxy, planExecuteNum, threadNum));
	}
	
	public static Object getInstance(Class<?> serviceInterface, Object serviceProxy, long planExecuteNum, int threadNum, long executeInterval, long monitorInterval) {
		Class<?>[] serviceInterfaces = new Class<?>[1];
		serviceInterfaces[0] = serviceInterface;
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceInterfaces, new PressToolClientReflectProxy(serviceProxy, planExecuteNum, threadNum, executeInterval, monitorInterval));
	}
	
	private PressToolClientReflectProxy(Object serviceProxy, long planExecuteNum) {
		super(planExecuteNum);
		this.serviceProxy = serviceProxy;
	}
	
	private PressToolClientReflectProxy(Object serviceProxy, long planExecuteNum, int threadNum) {
		super(planExecuteNum, threadNum);
		this.serviceProxy = serviceProxy;
	}
	
	private PressToolClientReflectProxy(Object serviceProxy, long planExecuteNum, int threadNum, long executeInterval, long monitorInterval) {
		super(planExecuteNum, threadNum, executeInterval, monitorInterval);
		this.serviceProxy = serviceProxy;
	}
	
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		return requestInvoke(serviceProxy, method, args);
	}
}
