package org.fengling.noodlecommon.dbrwseparate.template;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.fengling.noodlecommon.dbrwseparate.loadbalancer.LoadBalancerManager;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class ChangebackJdbcTemplateProxyFactoryBean implements
		FactoryBean<Object>, MethodInterceptor, InitializingBean {

	private final Log logger = LogFactory.getLog(ChangebackJdbcTemplateProxyFactoryBean.class);
	
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
				if (logger.isErrorEnabled()) {
					logger.error("invoke -> master invoke -> Exception: " + e);
				}
				throw e;
			}
		} else {
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.SALVE_1);
				return invocation.proceed();
			} catch (Throwable e) {
				if (logger.isErrorEnabled()) {
					logger.error("invoke -> salve invoke -> Exception: " + e);
				}
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
