package org.fengling.noodlecommon.dbrwseparate.msloadbalancer;

import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;

public class MSDataSourceModel {
	
	private DataSource dataSource;
	private String dataSourceType;

	private AtomicInteger failureCount = new AtomicInteger(0);// 失败的次数默认为0
	private AtomicInteger riseCount = new AtomicInteger(0);// 连续成功次数默认为0//只是在死链表里起作用

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
