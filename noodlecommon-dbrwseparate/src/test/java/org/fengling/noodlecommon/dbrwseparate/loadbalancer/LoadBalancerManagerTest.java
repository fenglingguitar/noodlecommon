package org.fengling.noodlecommon.dbrwseparate.loadbalancer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fengling/noodlecommon/dbrwseparate/loadbalancer/noodlecommon-loadbalancer.xml"
})
public class LoadBalancerManagerTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	LoadBalancerManager loadBalancerManager;
	
	@Test
	public void test() throws Exception {

		int totalCount = 20;
		int masterCount = 0;
		
		for (int i=1; i<=totalCount; i++) {
			System.out.println("Test " + i);
			DataSourceModel dataSourceModel = loadBalancerManager.getAliveDataSource();
			assertNotNull(dataSourceModel);
			if (dataSourceModel.getDataSourceType().equals("master")) {
				masterCount++;
			}
			System.out.println("DataSource Type: " + dataSourceModel.getDataSourceType());
			Connection Connection = null;
			try {
				Connection = dataSourceModel.getDataSource().getConnection();
			} catch (SQLException e) {
			}
			assertNotNull(Connection);
			Thread.sleep(1000);
		}
		
		System.out.println("Master Rate: " + (Double.valueOf(masterCount) / Double.valueOf(totalCount) * 100));
		
		DataSourceModel dataSourceModel = loadBalancerManager.getAliveDataSource();
		assertNotNull(dataSourceModel);
		List<DataSourceModel> dataSourceModelList = new ArrayList<DataSourceModel>();
		dataSourceModelList.add(dataSourceModel);
		DataSourceModel otherDataSourceModel = loadBalancerManager.getOtherAliveDataSource(dataSourceModelList);
		assertNotNull(otherDataSourceModel);
		assertNotSame(dataSourceModel, otherDataSourceModel);
	}
}
