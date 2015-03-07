package org.fl.noodle.common.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoodleResponseBody {
	
	public static final String TYPE	= "json";
	public static final String NAME	= "output";
	
    String type() default TYPE;
    String name() default NAME;
}
