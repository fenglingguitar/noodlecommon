package org.fengling.noodlecommon.dbrwseparate.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.fengling.noodlecommon.dbrwseparate.loadbalancer.LoadBalancerManager;

public class ChangebackMethodInterceptor implements MethodInterceptor {

	private final Log logger = LogFactory.getLog(ChangebackMethodInterceptor.class);
	
	private LoadBalancerManager loadBalancerManager;
	
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
	
	public void setLoadBalancerManager(LoadBalancerManager loadBalancerManager) {
		this.loadBalancerManager = loadBalancerManager;
	}
}
