package org.fl.noodle.common.trace;

import org.junit.Test;

public class TraceObjectInterceptorTest {

	@Test
	public void test() {
		TraceObjectInterceptor traceInvocationHandler = new TraceObjectInterceptor(new TestClass());
		((TestClass)traceInvocationHandler.getProxy()).test();
	}
	
	private static class TestClass {
		
		public TestClass() {
		}
		
		public void test() {
			System.out.println("TraceInvocationHandlerTest");
		}
	}
}
