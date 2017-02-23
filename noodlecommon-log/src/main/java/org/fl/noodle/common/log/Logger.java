package org.fl.noodle.common.log;

import org.fl.noodle.common.trace.TraceInterceptor;
import org.fl.noodle.common.trace.operation.method.TraceMethodPrint;
import org.fl.noodle.common.util.json.JsonTranslator;

public class Logger {
	
	private org.slf4j.Logger logger;
	
	private String getFormat(String format) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("TracePrint -> Method:").append(getMethod(4));
		stringBuilder.append(", ").append("Message:").append(format.replaceAll(",\\s+", ","));
		stringBuilder.append(", ").append("ParentMethod:").append(TraceInterceptor.getInvoke());
		stringBuilder.append(", ").append("TraceKey:").append(TraceInterceptor.getTraceKey());
		
		if (TraceMethodPrint.getTraceAuthor() != null) {
			stringBuilder.append(", ").append("AuthorID:").append(TraceMethodPrint.getTraceAuthor().authorID());
			stringBuilder.append(", ").append("AuthorName:").append(TraceMethodPrint.getTraceAuthor().authorName());
			stringBuilder.append(", ").append("LastEditTime:").append(TraceMethodPrint.getTraceAuthor().lastEditTime());
		}
		
		return stringBuilder.toString();
	}
	
	public void printEnter(Object... params) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("TraceEnter -> Method:").append(getMethod(3));
		stringBuilder.append(", ").append("Param:").append(TraceMethodPrint.getParam(params));
		stringBuilder.append(", ").append("ParentMethod:").append(TraceInterceptor.getInvoke());
		stringBuilder.append(", ").append("TraceKey:").append(TraceInterceptor.getTraceKey());
		
		if (TraceMethodPrint.getTraceAuthor() != null) {
			stringBuilder.append(", ").append("AuthorID:").append(TraceMethodPrint.getTraceAuthor().authorID());
			stringBuilder.append(", ").append("AuthorName:").append(TraceMethodPrint.getTraceAuthor().authorName());
			stringBuilder.append(", ").append("LastEditTime:").append(TraceMethodPrint.getTraceAuthor().lastEditTime());
		}
		
		logger.info(stringBuilder.toString());
	}
	
	public void printReturn(Object returnValue) {
		
		String returnStr = "";
		try {
			returnStr = returnValue != null ? JsonTranslator.toString(returnValue) : "";
		} catch (Exception e) {
			logger.error("infoReturn -> Exception:{}", e.getMessage());
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("TraceReturn -> Method:").append(getMethod(3));
		stringBuilder.append(", ").append("Return:").append(returnStr);
		stringBuilder.append(", ").append("ParentMethod:").append(TraceInterceptor.getInvoke());
		stringBuilder.append(", ").append("TraceKey:").append(TraceInterceptor.getTraceKey());
		
		if (TraceMethodPrint.getTraceAuthor() != null) {
			stringBuilder.append(", ").append("AuthorID:").append(TraceMethodPrint.getTraceAuthor().authorID());
			stringBuilder.append(", ").append("AuthorName:").append(TraceMethodPrint.getTraceAuthor().authorName());
			stringBuilder.append(", ").append("LastEditTime:").append(TraceMethodPrint.getTraceAuthor().lastEditTime());
		}
		
		logger.info(stringBuilder.toString());
	}
	
	public void printWarn(Throwable throwable, Object... params) {
		logger.warn(getExceptionInfo("TraceWarn", throwable, params));
	}
	
	public void printError(Throwable throwable, Object... params) {
		logger.error(getExceptionInfo("TraceError", throwable, params));
	}
	
	private String getExceptionInfo(String type, Throwable throwable, Object... params) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(type);
		stringBuilder.append(" -> Method:").append(getMethod(4));
		stringBuilder.append(", ").append("ExcType:").append(throwable.getClass().getSimpleName());
		stringBuilder.append(", ").append("ExcMsg:").append(TraceMethodPrint.getMessage(throwable));
		stringBuilder.append(", ").append("JvmStack:").append(TraceMethodPrint.getStackTrace(throwable));
		stringBuilder.append(", ").append("InvokeStack:").append(TraceInterceptor.getTraceStackToString());
		stringBuilder.append(", ").append("Param:").append(TraceMethodPrint.getParam(params));
		stringBuilder.append(", ").append("TraceKey:").append(TraceInterceptor.getTraceKey());
		
		if (TraceMethodPrint.getTraceAuthor() != null) {
			stringBuilder.append(", ").append("AuthorID:").append(TraceMethodPrint.getTraceAuthor().authorID());
			stringBuilder.append(", ").append("AuthorName:").append(TraceMethodPrint.getTraceAuthor().authorName());
			stringBuilder.append(", ").append("LastEditTime:").append(TraceMethodPrint.getTraceAuthor().lastEditTime());
		}
		
		return stringBuilder.toString();
	}
	
	private String getMethod(int position) {
		
		String method = "";
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (stackTraceElements.length > position) {
			String className = stackTraceElements[position].getClassName();
			int index = stackTraceElements[position].getClassName().lastIndexOf(".");
			if (index >= 0) {
				className = stackTraceElements[position].getClassName().substring(index + 1);
			}
			method = className + "." + stackTraceElements[position].getMethodName();
		}
		
		return method;
	}
	
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}	
	
	public Logger(org.slf4j.Logger inputLogger)	{
		this.logger=inputLogger;
	}

	public String getName()	{
		return logger.getName();
	}
	
	public void trace(String msg) {
		if (logger.isTraceEnabled()) {
			logger.trace(getFormat(msg));
		}
	}
	public void trace(String format, Object arg) {
		if (logger.isTraceEnabled()) {
			logger.trace(getFormat(format), arg);
		}
	}
	public void trace(String format, Object arg1, Object arg2) {
		if (logger.isTraceEnabled()) {
			logger.trace(getFormat(format), arg1,arg2);
		}
	}
	public void trace(String format, Object... arguments) {
		if (logger.isTraceEnabled()) {
			logger.trace(getFormat(format), arguments);
		}
	}
	public void trace(String msg, Throwable t) {
		if (logger.isTraceEnabled()) {
			logger.trace(getFormat(msg),t);
		}
	}
	 
	public void debug(String msg) {
		if (logger.isDebugEnabled()) {
			logger.debug(getFormat(msg));
		}
	}
	public void debug(String format, Object arg) {
		if (logger.isDebugEnabled()) {
			logger.debug(getFormat(format),arg);
		}
	}
	public void debug(String format, Object arg1, Object arg2) {
		if (logger.isDebugEnabled()) {
			logger.debug(getFormat(format), arg1,arg2);
		}
	}
	public void debug(String format, Object... arguments) {
		if (logger.isDebugEnabled()) {
			logger.debug(getFormat(format), arguments);
		}
	}
	public void debug(String msg, Throwable t) {
		if (logger.isDebugEnabled()) {
			logger.debug(getFormat(msg), t);
		}
	}
	 
	public void info(String msg){
		if (logger.isInfoEnabled()) {
			logger.info(getFormat(msg));
		}
	}
	public void info(String format, Object arg) {
		if (logger.isInfoEnabled()) {			
			logger.info(getFormat(format), arg);
		}
	}	
	public void info(String format, Object arg1, Object arg2) {
		if (logger.isInfoEnabled()) {			
			logger.info(getFormat(format), arg1,arg2);
		}
	}
	public void info(String format, Object... arguments) {
		if (logger.isInfoEnabled()) {			
			logger.info(getFormat(format), arguments);
		}
	}
	public void info(String msg, Throwable t) {
		if (logger.isInfoEnabled()) {			
			logger.info(getFormat(msg),t);
		}
	}
	 
	public void warn(String msg) {
		if (logger.isWarnEnabled()) {
			logger.warn(getFormat(msg));
		}
	}
	public void warn(String format, Object arg) {
		if (logger.isWarnEnabled()) {
			logger.warn(getFormat(format), arg);
		}
	}
	public void warn(String format, Object... arguments) {
		if (logger.isWarnEnabled()) {
			logger.warn(getFormat(format), arguments);
		}
	}
	public void warn(String format, Object arg1, Object arg2) {
		if (logger.isWarnEnabled()) {
			logger.warn(getFormat(format),arg1,arg2);
		}
	}
	public void warn(String msg, Throwable t) {
		if (logger.isWarnEnabled()) {
			logger.warn(getFormat(msg),t);
		}
	}
	
	public void error(String msg) {
		if (logger.isErrorEnabled()) {
			logger.error(getFormat(msg));
		}
	}
	public void error(String format, Object arg) {
		if (logger.isErrorEnabled()) {
			logger.error(getFormat(format), arg);
		}
	}
	public void error(String format, Object arg1, Object arg2) {
		if (logger.isErrorEnabled()) {			
			logger.error(getFormat(format), arg1,arg2);
		}
	}
	public void error(String format, Object... arguments) {
		if (logger.isErrorEnabled()) {			
			logger.error(getFormat(format), arguments);
		}
	}
	public void error(String msg, Throwable t) {
		if (logger.isErrorEnabled()) {
			logger.error(getFormat(msg), t);
		}
	}
}
