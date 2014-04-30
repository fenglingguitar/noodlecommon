package org.fengling.noodlecommon.dbrwseparate.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipleRoutingDataSource extends AbstractRoutingDataSource {
	
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSourceType();
	}
}
