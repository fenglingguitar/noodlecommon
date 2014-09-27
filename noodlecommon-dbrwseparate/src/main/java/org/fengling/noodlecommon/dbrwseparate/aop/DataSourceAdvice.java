package org.fengling.noodlecommon.dbrwseparate.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.fengling.noodlecommon.dbrwseparate.loadbalancer.LoadBalancerManager;
import org.fengling.noodlecommon.dbrwseparate.operation.OperationException;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {
	
	private LoadBalancerManager loadBalancerManager;
	
	private List<String> masterMethodPrefixList;
	
	public void before(Method method, Object[] args, Object target) throws Throwable {
		
		boolean isMaster = false;
		
		if (masterMethodPrefixList != null) {
			for (String prefix : masterMethodPrefixList) {
				if (method.getName().startsWith(prefix)) {
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
				throw new OperationException("None of the available datasource");
	        }
			DataSourceSwitch.setDataSourceType(dataSourceType);
		}
	}

	public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
		DataSourceSwitch.clearDataSourceType();
	}

	public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
		DataSourceSwitch.clearDataSourceType();
	}

	public void setLoadBalancerManager(LoadBalancerManager loadBalancerManager) {
		this.loadBalancerManager = loadBalancerManager;
	}

	public void setMasterMethodPrefixList(List<String> masterMethodPrefixList) {
		this.masterMethodPrefixList = masterMethodPrefixList;
	}
}