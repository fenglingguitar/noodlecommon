package org.fl.noodle.common.trace.operation.method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceMethodAuthor {
	
	public static final String AuthorID = "";
	public static final String AuthorName = "";
	public static final String LastEditTime = "";
	
    String authorID() default AuthorID;
    String authorName() default AuthorName;
    String lastEditTime() default LastEditTime;
}
