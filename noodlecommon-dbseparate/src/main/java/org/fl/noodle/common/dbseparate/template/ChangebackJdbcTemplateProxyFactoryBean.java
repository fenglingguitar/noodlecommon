package org.fl.noodle.common.dbseparate.template;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.dbseparate.datasource.DataSourceSwitch;
import org.fl.noodle.common.dbseparate.datasource.DataSourceType;
import org.fl.noodle.common.dbseparate.loadbalancer.LoadBalancerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class ChangebackJdbcTemplateProxyFactoryBean implements
		FactoryBean<Object>, MethodInterceptor, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(ChangebackJdbcTemplateProxyFactoryBean.class);
	
	private Object serviceProxy;
	
	private JdbcTemplate jdbcTemplate;
	
	private LoadBalancerManager loadBalancerManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ProxyFactory ProxyFactory = new ProxyFactory(JdbcOperations.class, this);
		ProxyFactory.setTarget(jdbcTemplate);
		this.serviceProxy = ProxyFactory.getProxy();
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		if (loadBalancerManager.checkIsAliveDataSource(DataSourceType.MASTER)) {
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);
				return invocation.proceed();
			} catch (Throwable e) {
				logger.error("invoke -> master invoke -> Exception:{}", e);
				throw e;
			}
		} else {
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.SALVE_1);
				return invocation.proceed();
			} catch (Throwable e) {
				logger.error("invoke -> salve invoke -> Exception:{}", e);
				throw e;
			}
		}
	}

	@Override
	public Object getObject() throws Exception {
		return this.serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return JdbcOperations.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setLoadBalancerManager(LoadBalancerManager loadBalancerManager) {
		this.loadBalancerManager = loadBalancerManager;
	}
}
