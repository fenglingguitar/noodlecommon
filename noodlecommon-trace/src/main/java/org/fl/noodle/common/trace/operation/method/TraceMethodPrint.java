package org.fl.noodle.common.trace.operation.method;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.trace.TraceInterceptor;
import org.fl.noodle.common.trace.operation.TraceOperation;
import org.fl.noodle.common.trace.util.Postman;
import org.fl.noodle.common.util.json.JsonTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;

public class TraceMethodPrint implements TraceOperation {

	private static final Logger logger = LoggerFactory.getLogger("trace.method");
	
	public static final String TRACE_AUTHOR = "trace_author";

	@Override
	public void before(MethodInvocation invocation) {
		try {
			Method targetClassMethod = TraceInterceptor.getTargetClass(invocation).getMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
			if (targetClassMethod.getAnnotation(TraceMethodConfig.class) == null
					|| (targetClassMethod.getAnnotation(TraceMethodConfig.class) != null
						&& targetClassMethod.getAnnotation(TraceMethodConfig.class).isEnterLog())) {
				if (targetClassMethod.getAnnotation(TraceMethodAuthor.class) == null) {
					if (targetClassMethod.getAnnotation(TraceMethodConfig.class) != null
							&& !targetClassMethod.getAnnotation(TraceMethodConfig.class).isParam()) {
						logger.info("MethodEnter -> Method:{}, ParentMethod:{}, TraceKey:{}",
								TraceInterceptor.getInvoke(),
								TraceInterceptor.getParentInvoke(),
								TraceInterceptor.getTraceKey()
								);
					} else {
						logger.info("MethodEnter -> Method:{}, Param:{}, ParentMethod:{}, TraceKey:{}",
								TraceInterceptor.getInvoke(),
								getParam(invocation.getArguments()),
								TraceInterceptor.getParentInvoke(),
								TraceInterceptor.getTraceKey()
								);
					}
				} else {
					setTraceAuthor(targetClassMethod.getAnnotation(TraceMethodAuthor.class));
					if (targetClassMethod.getAnnotation(TraceMethodConfig.class) != null
							&& !targetClassMethod.getAnnotation(TraceMethodConfig.class).isParam()) {
						logger.info("MethodEnter -> Method:{}, ParentMethod:{}, TraceKey:{}, AuthorID:{}, AuthorName:{}, LastEditTime:{}",
								TraceInterceptor.getInvoke(),
								TraceInterceptor.getParentInvoke(),
								TraceInterceptor.getTraceKey(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorID(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorName(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).lastEditTime()
								);
					} else {
						logger.info("MethodEnter -> Method:{}, Param:{}, ParentMethod:{}, TraceKey:{}, AuthorID:{}, AuthorName:{}, LastEditTime:{}",
								TraceInterceptor.getInvoke(),
								getParam(invocation.getArguments()),
								TraceInterceptor.getParentInvoke(),
								TraceInterceptor.getTraceKey(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorID(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorName(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).lastEditTime()
								);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void after(MethodInvocation invocation, boolean isError, Object returnValue) {
		try {
			Method targetClassMethod = TraceInterceptor.getTargetClass(invocation).getMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
			if (targetClassMethod.getAnnotation(TraceMethodConfig.class) != null
						&& targetClassMethod.getAnnotation(TraceMethodConfig.class).isReturnLog()
						&& !isError) {
				if (targetClassMethod.getAnnotation(TraceMethodAuthor.class) == null) {
					logger.info("MethodReturn -> Method:{}, Return:{}, ParentMethod:{}, TraceKey:{}",
							TraceInterceptor.getInvoke(),
							returnValue != null ? JsonTranslator.toString(returnValue) : "",
							TraceInterceptor.getParentInvoke(),
							TraceInterceptor.getTraceKey()
							);
				} else {
					setTraceAuthor(null);
					logger.info("MethodReturn -> Method:{}, Return:{}, ParentMethod:{}, TraceKey:{}, AuthorID:{}, AuthorName:{}, LastEditTime:{}",
							TraceInterceptor.getInvoke(),
							returnValue != null ? JsonTranslator.toString(returnValue) : "",
							TraceInterceptor.getParentInvoke(),
							TraceInterceptor.getTraceKey(),
							targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorID(),
							targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorName(),
							targetClassMethod.getAnnotation(TraceMethodAuthor.class).lastEditTime()
							);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(MethodInvocation invocation, Throwable e) {
		try {
			Method targetClassMethod = TraceInterceptor.getTargetClass(invocation).getMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
			if (targetClassMethod.getAnnotation(TraceMethodConfig.class) == null
					|| (targetClassMethod.getAnnotation(TraceMethodConfig.class) != null
						&& targetClassMethod.getAnnotation(TraceMethodConfig.class).isErrorLog())) {
				if (e instanceof TraceBusinessException) {
					if (targetClassMethod.getAnnotation(TraceMethodAuthor.class) == null) {
						logger.warn("MethodWarn -> Method:{}, ExcType:{}, ExcMsg:{}, JvmStack:{}, InvokeStack:{}, Param:{}, TraceKey:{}", 
								TraceInterceptor.getInvoke(), 
								e.getClass().getSimpleName(), 
								getMessage(e), 
								getStackTrace(e), 
								TraceInterceptor.getTraceStackToString(), 
								getParam(invocation.getArguments()), 
								TraceInterceptor.getTraceKey()
								);
					} else {
						logger.warn("MethodWarn -> Method:{}, ExcType:{}, ExcMsg:{}, JvmStack:{}, InvokeStack:{}, Param:{}, TraceKey:{}, AuthorID:{}, AuthorName:{}, LastEditTime:{}", 
								TraceInterceptor.getInvoke(), 
								e.getClass().getSimpleName(), 
								getMessage(e), 
								getStackTrace(e), 
								TraceInterceptor.getTraceStackToString(), 
								getParam(invocation.getArguments()), 
								TraceInterceptor.getTraceKey(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorID(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorName(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).lastEditTime()
								);
					}
				} else {
					if (targetClassMethod.getAnnotation(TraceMethodAuthor.class) == null) {
						logger.error("MethodError -> Method:{}, ExcType:{}, ExcMsg:{}, JvmStack:{}, InvokeStack:{}, Param:{}, TraceKey:{}", 
								TraceInterceptor.getInvoke(), 
								e.getClass().getSimpleName(), 
								getMessage(e), 
								getStackTrace(e), 
								TraceInterceptor.getTraceStackToString(), 
								getParam(invocation.getArguments()), 
								TraceInterceptor.getTraceKey()
								);
					} else {
						logger.error("MethodError -> Method:{}, ExcType:{}, ExcMsg:{}, JvmStack:{}, InvokeStack:{}, Param:{}, TraceKey:{}, AuthorID:{}, AuthorName:{}, LastEditTime:{}", 
								TraceInterceptor.getInvoke(), 
								e.getClass().getSimpleName(), 
								getMessage(e), 
								getStackTrace(e), 
								TraceInterceptor.getTraceStackToString(), 
								getParam(invocation.getArguments()), 
								TraceInterceptor.getTraceKey(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorID(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).authorName(),
								targetClassMethod.getAnnotation(TraceMethodAuthor.class).lastEditTime()
								);
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
	}
	
	private static void setTraceAuthor(TraceMethodAuthor traceMethodAuthor) {
		Postman.putParam(TRACE_AUTHOR, traceMethodAuthor);
	}
	
	public static TraceMethodAuthor getTraceAuthor() {
		return (TraceMethodAuthor) Postman.getParam(TRACE_AUTHOR);
	}
	
	public static String getMessage(Throwable e) {
		Throwable throwable = getCause(e);
		if (throwable.getMessage() != null) {
			return throwable.getMessage().replaceAll(",\\s+", ",");
		}
		return throwable.toString().replaceAll(",\\s+", ",");
	}
	
	public static String getStackTrace(Throwable e) {
		
		StackTraceElement[] stackTraceElement = getCause(e).getStackTrace();
		if (stackTraceElement == null || stackTraceElement.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(stackTraceElement[0].toString());
		
		if (stackTraceElement[0].getClassName().startsWith("com.tujia")) {
			sb.append("]");
			return sb.toString();
		}
		
		for (StackTraceElement stackTraceElementIt : stackTraceElement) {
			if (stackTraceElementIt.getClassName().startsWith("com.tujia")) {
				sb.append("; ");
				sb.append(stackTraceElementIt.toString());
				break;
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	public static Throwable getCause(Throwable e) {
		Throwable throwable = e;
        while (throwable.getCause() != null) {
        	throwable = throwable.getCause();
        }
        return throwable;
	}
	
	public static String getParam(Object[] arguments) {
		
		if (arguments == null || arguments.length == 0) {
			return "[]";
		}
		
		List<Object> objectList = new ArrayList<Object>(arguments.length);
		for (Object object : arguments) {
		    if (object != null) {
		        if (object instanceof Serializable) {
		            objectList.add(object);
		        } else {
		            objectList.add(object.getClass().getSimpleName());
		        }
		    }
		}

		try {
			return JSON.toJSONString(objectList, new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					if (value == null || !(value instanceof Serializable)) {
						return false;
					}
					return true;
				}
			});
		} catch (Exception e) {
			logger.warn("CatchError -> doInvoke -> getParam -> JsonTranslator.toString -> Method:{}, Exception:{}", TraceInterceptor.getInvoke(), e.getMessage());
		}
		
		return "[]";
	}
}