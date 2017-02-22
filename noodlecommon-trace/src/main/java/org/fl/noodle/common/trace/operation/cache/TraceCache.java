package org.fl.noodle.common.trace.operation.cache;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.trace.TraceInterceptor;
import org.fl.noodle.common.trace.operation.TraceOperation;

public class TraceCache implements TraceOperation {

	private static ThreadLocal<Map<String, Object>> threadLocalMap = new ThreadLocal<Map<String, Object>>();
	
	@Override
	public void before(MethodInvocation invocation) {
		if (threadLocalMap.get() == null) {
			threadLocalMap.set(new HashMap<String, Object>());
		}
	}

	@Override
	public void after(MethodInvocation invocation, boolean isError, Object returnValue) {
		if (TraceInterceptor.getTraceStack().size() == 1) {
			if (threadLocalMap.get() != null) {
				threadLocalMap.get().clear();
			}
		}
	}

	@Override
	public void error(MethodInvocation invocation, Throwable e) {
	}

	public static Object get(String key) {
		if (threadLocalMap.get() != null) {
			return threadLocalMap.get().get(key);
		}
		
		return null;
	}
	
	public static Object set(String key, Object object) {
		if (threadLocalMap.get() != null) {
			return threadLocalMap.get().put(key, object);
		}
		return null;
	}
}
