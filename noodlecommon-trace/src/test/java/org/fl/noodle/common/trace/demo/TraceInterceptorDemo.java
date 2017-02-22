package org.fl.noodle.common.trace.demo;

public interface TraceInterceptorDemo {
	
	public String testTraceMethodAuthor(int a, String b) throws Exception;
	public String testTraceMethodConfig(int a, String b) throws Exception;
	
	public void throwNullPointerException(int a, String b) throws Exception;
	public void throwArrayIndexOutOfBoundsException(int a, String b) throws Exception;
	public void throwBusinessException(int a, String b) throws Exception;
}
