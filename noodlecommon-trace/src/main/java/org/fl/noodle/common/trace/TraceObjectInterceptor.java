package org.fl.noodle.common.trace;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.trace.operation.TraceOperation;
import org.fl.noodle.common.trace.operation.cache.TraceCache;
import org.fl.noodle.common.trace.operation.method.TraceMethodPrint;
import org.fl.noodle.common.trace.operation.performance.TracePerformancePrint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.support.AopUtils;

public class TraceObjectInterceptor implements MethodInterceptor {

	private static List<Object> methodInterceptorListDefault = new ArrayList<Object>();
	
	public static TraceInterceptor traceInterceptorDefault = new TraceInterceptor();
	
	static {
		List<TraceOperation> traceOperationList = new ArrayList<TraceOperation>();
		traceOperationList.add(new TraceMethodPrint());
		traceOperationList.add(new TracePerformancePrint());
		traceOperationList.add(new TraceCache());
		traceInterceptorDefault.setTraceOperationList(traceOperationList);
		methodInterceptorListDefault.add(traceInterceptorDefault);
	}
	
	private List<Object> methodInterceptorList;
	private Object target;
	private Object proxy;
	
	private Set<String> noCareSet = new HashSet<String>();
	
	public TraceObjectInterceptor(Object target) {
		this(target, methodInterceptorListDefault);
	}
	
	public TraceObjectInterceptor(Object target, String[] noCares) {
		this(target, methodInterceptorListDefault);
		if (noCares != null) {
			for (String noCare : noCares) {
				noCareSet.add(noCare);
			}
		}
	}
	
	public TraceObjectInterceptor(Object target, List<Object> methodInterceptorList) {
		this.target = target;
		this.methodInterceptorList = methodInterceptorList;
		createProxy();
	}
	
	private void createProxy() {
		Enhancer enhancer = new Enhancer();  
		enhancer.setSuperclass(target.getClass());  
		enhancer.setCallback(this);  
		proxy = enhancer.create(); 
	}
	
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (noCareSet.contains(method.getName())) {
			return AopUtils.invokeJoinpointUsingReflection(this.target, method, args);
		}
		MethodInvocation invocation = new MyReflectiveMethodInvocation(proxy, target, method, args, target.getClass(), methodInterceptorList);
		return invocation.proceed();
	}
	
	private static class MyReflectiveMethodInvocation extends ReflectiveMethodInvocation {
		protected MyReflectiveMethodInvocation(Object proxy, Object target,
				Method method, Object[] arguments, Class<?> targetClass,
				List<Object> interceptorsAndDynamicMethodMatchers) {
			super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
		}
	}
}
