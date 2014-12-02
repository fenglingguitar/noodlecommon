package org.fl.noodle.common.dbseparate.loadbalancer;

import java.util.List;

import org.fl.noodle.common.dbseparate.datasource.DataSourceType;

public interface LoadBalancerManager {
	
	public DataSourceType getAliveDataSource();
	public DataSourceType getOtherAliveDataSource(List<DataSourceType> dataSourceTypeList);
	boolean checkIsAliveDataSource(DataSourceType dataSourceType);
}
