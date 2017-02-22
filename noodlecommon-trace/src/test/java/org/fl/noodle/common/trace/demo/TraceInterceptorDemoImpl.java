package org.fl.noodle.common.trace.demo;

import java.util.ArrayList;
import java.util.List;

import org.fl.noodle.common.trace.operation.method.TraceBusinessException;
import org.fl.noodle.common.trace.operation.method.TraceMethodAuthor;
import org.fl.noodle.common.trace.operation.method.TraceMethodConfig;

public class TraceInterceptorDemoImpl implements TraceInterceptorDemo {

	@TraceMethodAuthor(authorID = "chuanzhiz", authorName = "朱传志", lastEditTime = "20160823")
	public String testTraceMethodAuthor(int a, String b) throws Exception {
		return "testTraceMethodAuthor return";
	}
	
	@TraceMethodAuthor(authorID = "chuanzhiz", authorName = "朱传志", lastEditTime = "20160823")
	@TraceMethodConfig(isEnterLog = true, isErrorLog = true, isReturnLog = true, isPerformanceLog = true, isParam = true)
	public String testTraceMethodConfig(int a, String b) throws Exception {
		return "testTraceMethodConfig return";
	}

	private Integer getNull() {
		return null;
	}
	
	@TraceMethodAuthor(authorID = "chuanzhiz", authorName = "朱传志", lastEditTime = "20160823")
	@TraceMethodConfig(isEnterLog = true, isErrorLog = true, isReturnLog = true, isPerformanceLog = true)
	public void throwNullPointerException(int a, String b) throws Exception {
		getNull().toString();
	}
	
	@TraceMethodAuthor(authorID = "chuanzhiz", authorName = "朱传志", lastEditTime = "20160823")
	@TraceMethodConfig(isEnterLog = true, isErrorLog = true, isReturnLog = true, isPerformanceLog = true)
	public void throwArrayIndexOutOfBoundsException(int a, String b) throws Exception {
		List<String> list = new ArrayList<String>();
		list.get(10);
	}
	
	@TraceMethodAuthor(authorID = "chuanzhiz", authorName = "朱传志", lastEditTime = "20160823")
	@TraceMethodConfig(isEnterLog = true, isErrorLog = true, isReturnLog = true, isPerformanceLog = true)
	public void throwBusinessException(int a, String b) throws Exception {
		throw new TraceBusinessException("testBusinessException");
	}
}
