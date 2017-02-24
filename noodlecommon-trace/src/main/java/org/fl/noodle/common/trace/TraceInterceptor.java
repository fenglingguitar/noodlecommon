package org.fl.noodle.common.trace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.trace.operation.TraceOperation;
import org.fl.noodle.common.trace.operation.method.TraceException;
import org.fl.noodle.common.trace.util.Postman;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.DirectFieldAccessor;

public class TraceInterceptor implements MethodInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(TraceInterceptor.class);
	
	public static final String TRACE_KAY = "trace_key";
	public static final String TRACE_STACK = "trace_stack";
	public static final String TRACE_KEY_STACK = "trace_key_stack";
	
	private List<String> projectPrdfixs;
	
	private List<TraceOperation> traceOperationList;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		if (getTraceStack().isEmpty()) {
			Postman.putParam(TRACE_KAY, UUID.randomUUID().toString().replaceAll("-", ""));
		}
		
		return doInvoke(invocation);
	}
	
	private Object doInvoke(MethodInvocation invocation) throws Throwable {
		boolean isError = false;
		Object returnValue = null;
		try {
			if (isTrace(invocation)) {
				getTraceStack().push(getInvokeName(invocation));
				getTraceKeyStack().push(UUID.randomUUID().toString().replaceAll("-", ""));
				executeOparetionBefore(invocation);
			}
			returnValue = invocation.proceed();
			return returnValue;
		} catch (Throwable e) {
			isError = true;
			if (isTrace(invocation) && !(e instanceof TraceException)) {
				executeOparetionError(invocation, e);
				throw new TraceException(e);
			}
			throw e;
		} finally {
			if (isTrace(invocation)) {
				executeOparetionAfter(invocation, isError, returnValue);
				if(getTraceStack() != null && !getTraceStack().isEmpty()) getTraceStack().pop();
				if(getTraceKeyStack() != null && !getTraceKeyStack().isEmpty())getTraceKeyStack().pop();
			}
		}
	}
	
	private boolean isTrace(MethodInvocation invocation) {
		
		if (projectPrdfixs == null) {
			return true;
		}
		
		String className = TraceInterceptor.getTargetClass(invocation).getName();
		for (String projectPrdfix : projectPrdfixs) {
			if (className.startsWith(projectPrdfix)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void executeOparetionBefore(MethodInvocation invocation) {
		if (traceOperationList != null) {
			for (TraceOperation traceOperationIt : traceOperationList) {
				traceOperationIt.before(invocation);
			}
		}
	}
	
	private void executeOparetionAfter(MethodInvocation invocation, boolean isError, Object returnValue) {
		if (traceOperationList != null) {
			ListIterator<TraceOperation> ListIterator = traceOperationList.listIterator(traceOperationList.size());
			while (ListIterator.hasPrevious()) {
				ListIterator.previous().after(invocation, isError, returnValue);
			}
		}
	}
	
	private void executeOparetionError(MethodInvocation invocation, Throwable e) {
		if (traceOperationList != null) {
			for (TraceOperation traceOperationIt : traceOperationList) {
				traceOperationIt.error(invocation, e);
			}
		}
	}
	
	public static String getInvokeName(MethodInvocation invocation) {
		return getTargetClass(invocation).getSimpleName() + "." + invocation.getMethod().getName();
	}
	
	public static Class<? extends Object> getTargetClass(MethodInvocation invocation) {
		
		Object object = invocation.getThis();
		
		if (object == null) {
			return invocation.getMethod().getDeclaringClass();
		}
		
		if (AopUtils.isAopProxy(object) && !AopUtils.isJdkDynamicProxy(object)) {
			return AopUtils.getTargetClass(object);
		}
		
		while (Proxy.isProxyClass(object.getClass())) {
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(object);
			if (!invocationHandler.getClass().getName().equals("org.springframework.aop.framework.JdkDynamicAopProxy")) {
				return object.getClass();
			}
			AdvisedSupport advised = (AdvisedSupport) new DirectFieldAccessor(invocationHandler).getPropertyValue("advised");  
	        try {
	        	object = advised.getTargetSource().getTarget();
			} catch (Exception e) {
				logger.warn("TraceInterceptor -> getTargetClass -> getTarget -> Exception:{}", e);
				break;
			}
	        if (object == null) {
	        	return invocation.getMethod().getDeclaringClass();
	        }
		}
		
		return object.getClass();
	}
	
	public static LinkedList<String> getTraceStack() {
		return getTraceStackGeneral(TRACE_STACK);
	}
	
	public static LinkedList<String> getTraceKeyStack() {
		return getTraceStackGeneral(TRACE_KEY_STACK);
	}
	
	@SuppressWarnings("unchecked")
	public static LinkedList<String> getTraceStackGeneral(String stackName) {
		Object object = Postman.getParam(stackName);
		if (object != null && !(object instanceof LinkedList) && object instanceof List) {
			Postman.putParam(stackName, new LinkedList<String>((List<String>)object));
		}
		if (!Postman.exitParam(stackName)) {
			Postman.putParam(stackName, new LinkedList<String>());
		}
		return (LinkedList<String>) Postman.getParam(stackName);
	}
	
	public static String getTraceStackToString() {
		
		if (getTraceStack().isEmpty()) {
			return "[]";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String name : getTraceStack()) {
			sb.append(name).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	public static String getTraceKey() {
		return Postman.getParam(TRACE_KAY) != null ? (String)Postman.getParam(TRACE_KAY) : "";
	}
	public static void setTraceKey(String value) {
		Postman.putParam(TRACE_KAY, value);
	}
	
	public static String getInvoke() {
		if (getTraceStack().size() == 0) {
			return "";
		}
		return getTraceStack().peek();
	}
	public static String getParentInvoke() {
		if (getTraceStack().size() <= 1) {
			return "Root";
		}	
		return getTraceStack().get(1);
	}
	
	public static void setInvoke(String invoke) {
		getTraceStack().push(invoke);
	}
	public static void popInvoke() {
		if (getTraceStack().size() > 0) {
			getTraceStack().pop();
		}
	}
	
	public static String getStackKey() {
		if (getTraceKeyStack().size() == 0) {
			return "";
		}
		return getTraceKeyStack().peek();
	}
	public static String getParentStackKey() {
		if (getTraceKeyStack().size() <= 1) {
			return "Root";
		}	
		return getTraceKeyStack().get(1);
	}
	
	public static void setStackKey(String key) {
		getTraceKeyStack().push(key);
	}
	public static void popStackKey() {
		if (getTraceKeyStack().size() > 0) {
			getTraceKeyStack().pop();
		}
	}

	public void setProjectPrdfixs(List<String> projectPrdfixs) {
		this.projectPrdfixs = projectPrdfixs;
	}
	
	public void setTraceOperationList(List<TraceOperation> traceOperationList) {
		this.traceOperationList = traceOperationList;
	}
}
