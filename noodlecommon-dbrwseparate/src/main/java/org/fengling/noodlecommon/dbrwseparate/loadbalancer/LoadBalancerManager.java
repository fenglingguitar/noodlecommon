package org.fengling.noodlecommon.dbrwseparate.loadbalancer;

import java.util.List;

import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;

public interface LoadBalancerManager {
	
	public DataSourceType getAliveDataSource();
	public DataSourceType getOtherAliveDataSource(List<DataSourceType> DataSourceTypeList);
}
