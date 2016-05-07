package org.fl.noodle.common.dbseparate.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fl.noodle.common.dbseparate.datasource.DataSourceSwitch;
import org.fl.noodle.common.dbseparate.datasource.DataSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailoverMethodInterceptor implements MethodInterceptor {

	private final static Logger logger = LoggerFactory.getLogger(FailoverMethodInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		try {
			DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);
			return invocation.proceed();
		} catch (Throwable me) {
			logger.error("invoke -> master invoke -> DataAccessException:{}", me);
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.SALVE_1);
				return invocation.proceed();
			} catch (Throwable se) {
				logger.error("invoke -> salve invoke -> DataAccessException:{}", se);
				throw se;
			}
		}
	}
}
