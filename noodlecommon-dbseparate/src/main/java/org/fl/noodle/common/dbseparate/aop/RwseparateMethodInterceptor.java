package org.fl.noodle.common.dbseparate.aop;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.dbseparate.datasource.DataSourceSwitch;
import org.fl.noodle.common.dbseparate.datasource.DataSourceType;
import org.fl.noodle.common.dbseparate.loadbalancer.LoadBalancerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RwseparateMethodInterceptor implements MethodInterceptor {

	private final static Logger logger = LoggerFactory.getLogger(RwseparateMethodInterceptor.class);
	
	private LoadBalancerManager loadBalancerManager;
	
	private List<String> masterMethodPrefixList;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		boolean isMaster = false;
		
		if (masterMethodPrefixList != null) {
			for (String prefix : masterMethodPrefixList) {
				if (invocation.getMethod().getName().startsWith(prefix)) {
					isMaster = true;
					break;
				}
			}
		}
		
		if (isMaster) {
			DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);
		} else {
			DataSourceType dataSourceType = loadBalancerManager.getAliveDataSource();
			if (dataSourceType == null) {
				if (logger.isErrorEnabled()) {
					logger.error("getAliveDataSource -> loadBalancerManager.getAliveDataSource return null -> None of the available datasource");
				}
				throw new Exception("None of the available datasource");
	        }
			DataSourceSwitch.setDataSourceType(dataSourceType);
		}
		
		return invocation.proceed();
	}
	
	public void setLoadBalancerManager(LoadBalancerManager loadBalancerManager) {
		this.loadBalancerManager = loadBalancerManager;
	}

	public void setMasterMethodPrefixList(List<String> masterMethodPrefixList) {
		this.masterMethodPrefixList = masterMethodPrefixList;
	}
}
