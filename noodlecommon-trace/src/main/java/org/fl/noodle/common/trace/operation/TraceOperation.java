package org.fl.noodle.common.trace.operation;

import org.aopalliance.intercept.MethodInvocation;

public interface TraceOperation {
	public void before(MethodInvocation invocation);
	public void after(MethodInvocation invocation, boolean isError, Object returnValue);
	public void error(MethodInvocation invocation, Throwable e);
}