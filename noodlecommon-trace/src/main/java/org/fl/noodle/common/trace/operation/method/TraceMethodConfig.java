package org.fl.noodle.common.trace.operation.method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceMethodConfig {
	
	public static final boolean IsEnterLog = true;
	public static final boolean IsErrorLog = true;
	public static final boolean IsReturnLog = false;
	public static final boolean IsPerformanceLog = true;
	public static final boolean IsParam = true;
	
	boolean isEnterLog() default IsEnterLog;
	boolean isErrorLog() default IsErrorLog;
	boolean isReturnLog() default IsReturnLog;
	boolean isPerformanceLog() default IsPerformanceLog;
	boolean isParam() default IsParam;
}
