package org.fl.noodle.common.trace.operation.performance;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.trace.TraceInterceptor;
import org.fl.noodle.common.trace.operation.TraceOperation;
import org.fl.noodle.common.trace.operation.method.TraceMethodAuthor;
import org.fl.noodle.common.trace.operation.method.TraceMethodConfig;
import org.fl.noodle.common.trace.util.Postman;
import org.fl.noodle.common.trace.util.TimeSynchron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracePerformancePrint implements TraceOperation {

	private static final Logger logger = LoggerFactory.getLogger("trace.performance");
	
	//public static final String PERFORMANCE_START_TIME = "performance_start_time";
	
	@Override
	public void before(MethodInvocation invocation) {
		Postman.putParam(invocation.getMethod().getName(), TimeSynchron.currentTimeMillis());
	}

	@Override
	public void after(MethodInvocation invocation, boolean isError, Object returnValue) {
		long startTime = (long) Postman.getParam(invocation.getMethod().getName());
		long endTime = TimeSynchron.currentTimeMillis();
		try {
			if (TraceInterceptor.getInvokeName(invocation).equals(TraceInterceptor.getInvoke())) {
				Method targetClassMethod = TraceInterceptor.getTargetClass(invocation).getMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
				if (targetClassMethod.getAnnotation(TraceMethodConfig.class) == null
						|| (targetClassMethod.getAnnotation(TraceMethodConfig.class) != null 
							&& targetClassMethod.getAnnotation(TraceMethodConfig.class).isPerformanceLog())) {
					if (targetClassMethod.getAnnotation(TraceMethodAuthor.class) == null) {
						printTraceLog(
								TraceInterceptor.getInvoke(), 
								TraceInterceptor.getParentInvoke(), 
								startTime, 
								endTime, 
								isError, 
								TraceInterceptor.getStackKey(), 
								TraceInterceptor.getParentStackKey()
								);
					} else {
						printTraceLog(
								TraceInterceptor.getInvoke(), 
								TraceInterceptor.getParentInvoke(), 
								startTime, 
								endTime, 
								isError, 
								TraceInterceptor.getStackKey(), 
								TraceInterceptor.getParentStackKey(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class)
								);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(MethodInvocation invocation, Throwable e) {	
	}
	
	public static void printTraceLog(String invoke, long startTime, long endTime, boolean isError) {
		printTraceLog(
				invoke,
				TraceInterceptor.getInvoke(),
				startTime, 
				endTime, 
				isError, 
				UUID.randomUUID().toString().replaceAll("-", ""), 
				TraceInterceptor.getStackKey());
	}
	
	public static void printTraceLog(String invoke, String parentInvoke, long startTime, long endTime, boolean isError, String stackKey, String parentStackKey) {
		logger.info("Performance -> TraceKey:{}, Invoke:{}, ParentInvoke:{}, StartTime:{}, EndTime:{}, ElapsedTime:{}, IsError:{}, Key:{}, ParentKey:{}", 
				TraceInterceptor.getTraceKey(), 
				invoke, 
				parentInvoke, 
				(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(startTime),
				(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(endTime),
				endTime - startTime, 
				isError,
				stackKey,
				parentStackKey);
	}
	
	public static void printTraceLog(String invoke, String parentInvoke, long startTime, long endTime, boolean isError, String stackKey, String parentStackKey, TraceMethodAuthor traceMethodAuthor) {
		logger.info("Performance -> TraceKey:{}, Invoke:{}, ParentInvoke:{}, StartTime:{}, EndTime:{}, ElapsedTime:{}, IsError:{}, Key:{}, ParentKey:{}, AuthorID:{}", 
				TraceInterceptor.getTraceKey(), 
				invoke, 
				parentInvoke, 
				(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(startTime),
				(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(endTime),
				endTime - startTime, 
				isError,
				stackKey,
				parentStackKey,
				traceMethodAuthor.authorID()
				);
	}
}
