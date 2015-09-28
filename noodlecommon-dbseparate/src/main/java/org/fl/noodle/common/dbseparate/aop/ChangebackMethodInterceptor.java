package org.fl.noodle.common.dbseparate.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.dbseparate.datasource.DataSourceSwitch;
import org.fl.noodle.common.dbseparate.datasource.DataSourceType;
import org.fl.noodle.common.dbseparate.loadbalancer.LoadBalancerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangebackMethodInterceptor implements MethodInterceptor {

	private final static Logger logger = LoggerFactory.getLogger(ChangebackMethodInterceptor.class);
	
	private LoadBalancerManager loadBalancerManager;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		if (loadBalancerManager.checkIsAliveDataSource(DataSourceType.MASTER)) {
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);
				return invocation.proceed();
			} catch (Throwable e) {
				if (logger.isErrorEnabled()) {
					logger.error("invoke -> master invoke -> Exception:{}", e);
				}
				throw e;
			}
		} else {
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.SALVE_1);
				return invocation.proceed();
			} catch (Throwable e) {
				if (logger.isErrorEnabled()) {
					logger.error("invoke -> salve invoke -> Exception:{}", e);
				}
				throw e;
			}
		}
	}
	
	public void setLoadBalancerManager(LoadBalancerManager loadBalancerManager) {
		this.loadBalancerManager = loadBalancerManager;
	}
}
