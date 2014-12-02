package org.fl.noodle.common.dbseparate.loadbalancer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.fl.noodle.common.dbseparate.datasource.DataSourceSwitch;
import org.fl.noodle.common.dbseparate.datasource.DataSourceType;
import org.fl.noodle.common.dbseparate.loadbalancer.LoadBalancerManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/dbseparate/loadbalancer/noodlecommon-loadbalancer.xml"
})
public class LoadBalancerManagerTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	LoadBalancerManager loadBalancerManager;
	
	@Autowired
	DataSource dataSource;
	
	@Test
	public void testGetAliveDataSource() throws Exception {

		int totalCount = 20;
		int masterCount = 0;
		
		for (int i=1; i<=totalCount; i++) {
			System.out.println("Test " + i);
			DataSourceType dataSourceType = loadBalancerManager.getAliveDataSource();
			assertNotNull(dataSourceType);
			if (dataSourceType == DataSourceType.MASTER) {
				masterCount++;
			}
			System.out.println("DataSource Type: " + dataSourceType.typeName());
			
			DataSourceSwitch.setDataSourceType(dataSourceType);
			Connection Connection = null;
			try {
				Connection = dataSource.getConnection();
			} catch (SQLException e) {
			}
			assertNotNull(Connection);
			Thread.sleep(1000);
		}
		
		System.out.println("Master Rate: " + (Double.valueOf(masterCount) / Double.valueOf(totalCount) * 100));
	}
	
	@Test
	public void testGetOtherAliveDataSource() throws Exception {
		
		DataSourceType dataSourceType = loadBalancerManager.getAliveDataSource();
		assertNotNull(dataSourceType);
		List<DataSourceType> dataSourceTypeList = new ArrayList<DataSourceType>();
		dataSourceTypeList.add(dataSourceType);
		DataSourceType otherDataSourceType = loadBalancerManager.getOtherAliveDataSource(dataSourceTypeList);
		assertNotNull(otherDataSourceType);
		assertNotSame(dataSourceType, otherDataSourceType);
	}
	
	@Test
	public void testCheckIsAliveDataSource() throws Exception {
		DataSourceType dataSourceType = loadBalancerManager.getAliveDataSource();
		assertNotNull(dataSourceType);
		assertTrue(loadBalancerManager.checkIsAliveDataSource(dataSourceType));
	}
}
