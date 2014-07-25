package org.fengling.noodlecommon.dbrwseparate.loadbalancer;

import java.util.List;

public interface LoadBalancerManager {
	
	public DataSourceModel getAliveDataSource();
	public DataSourceModel getOtherAliveDataSource(List<DataSourceModel> DataSourceModelList);
}
