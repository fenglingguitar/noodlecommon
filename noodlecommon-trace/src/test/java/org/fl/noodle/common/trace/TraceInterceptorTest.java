package org.fl.noodle.common.trace;

import org.fl.noodle.common.trace.demo.TraceInterceptorDemo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/trace/trace-test-bean.xml"
})
public class TraceInterceptorTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private TraceInterceptorDemo traceInterceptorDemo;

	@Test
	public void testTraceMethodAuthor() {
		try {
			traceInterceptorDemo.testTraceMethodAuthor(3, "testTraceMethodAuthor");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testTraceMethodConfig() {
		try {
			traceInterceptorDemo.testTraceMethodConfig(3, "testTraceMethodConfig");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testNullPointerException() {
		try {
			traceInterceptorDemo.throwNullPointerException(1, "testNullPointerException");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testArrayIndexOutOfBoundsException() {
		try {
			traceInterceptorDemo.throwArrayIndexOutOfBoundsException(2, "testArrayIndexOutOfBoundsException");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testBusinessException() {
		try {
			traceInterceptorDemo.throwBusinessException(3, "testArrayIndexOutOfBoundsException");
		} catch (Exception e) {
		}
	}
}
