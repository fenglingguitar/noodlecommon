package org.fl.noodle.common.test.press;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class PressToolClientSpringProxyFactory extends AbstractPressToolClient implements FactoryBean<Object>, MethodInterceptor, InitializingBean {

	private String interfaceName;
	private Object serviceOriginal;
	
	private Class<?> serviceInterface;
	private Object serviceProxy;	

	@Override
	public void afterPropertiesSet() throws Exception {
		serviceInterface = Class.forName(interfaceName);
		this.serviceProxy = (new ProxyFactory(serviceInterface, this)).getProxy();
	}
	
	@Override
	public Object getObject() throws Exception {
		return serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return requestInvoke(serviceOriginal, invocation.getMethod(), invocation.getArguments());
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public void setServiceOriginal(Object serviceOriginal) {
		this.serviceOriginal = serviceOriginal;
	}
}
