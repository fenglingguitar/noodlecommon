package org.fl.noodle.common.connect.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.framework.ReflectiveMethodInvocation;

public class NoodleReflectiveMethodInvocation extends ReflectiveMethodInvocation {
	public NoodleReflectiveMethodInvocation(Object proxy, Object target,
			Method method, Object[] arguments, Class<?> targetClass,
			List<Object> interceptorsAndDynamicMethodMatchers) {
		super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
	}
}
