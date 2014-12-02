package org.fl.noodle.common.dbseparate.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipleRoutingDataSource extends AbstractRoutingDataSource {
	
	protected Object determineCurrentLookupKey() {
		return DataSourceSwitch.getDataSourceType();
	}
}
