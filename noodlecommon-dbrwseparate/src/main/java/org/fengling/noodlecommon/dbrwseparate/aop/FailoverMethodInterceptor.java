package org.fengling.noodlecommon.dbrwseparate.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;

public class FailoverMethodInterceptor implements MethodInterceptor {

	private final Log logger = LogFactory.getLog(FailoverMethodInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		try {
			DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);
			return invocation.proceed();
		} catch (Throwable me) {
			if (logger.isErrorEnabled()) {
				logger.error("invoke -> master invoke -> DataAccessException: " + me);
			}
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.SALVE_1);
				return invocation.proceed();
			} catch (Throwable se) {
				if (logger.isErrorEnabled()) {
					logger.error("invoke -> salve invoke -> DataAccessException: " + se);
				}
				throw se;
			}
		}
	}
}
