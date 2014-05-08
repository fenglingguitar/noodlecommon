package org.fengling.noodlecommon.dbrwseparate.loadbalancer;

import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;

public class DataSourceModel {
	
	private DataSource dataSource;
	private String dataSourceType;

	private AtomicInteger failureCount = new AtomicInteger(0);
	private AtomicInteger riseCount = new AtomicInteger(0);

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public AtomicInteger getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(AtomicInteger failureCount) {
		this.failureCount = failureCount;
	}

	public AtomicInteger getRiseCount() {
		return riseCount;
	}

	public void setRiseCount(AtomicInteger riseCount) {
		this.riseCount = riseCount;
	}
}
