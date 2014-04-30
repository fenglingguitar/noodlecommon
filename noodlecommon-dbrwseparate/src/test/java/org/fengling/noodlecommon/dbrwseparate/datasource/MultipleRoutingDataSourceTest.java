package org.fengling.noodlecommon.dbrwseparate.datasource;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fengling/noodlecommon/dbrwseparate/datasource/noodlecommon-datasource.xml"
})

public class MultipleRoutingDataSourceTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private MultipleRoutingDataSource dataSource;

	@Test
	public void test() throws Exception {
		
		DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
		Connection connection_master = dataSource.getConnection();
		assertNotNull(connection_master);
		DataSourceContextHolder.setDataSourceType(DataSourceType.SALVE_1);
		Connection connection_salve_1 = dataSource.getConnection();
		assertNotNull(connection_salve_1);
		DataSourceContextHolder.setDataSourceType(DataSourceType.SALVE_2);
		Connection connection_salve_2 = dataSource.getConnection();
		assertNotNull(connection_salve_2);
	}

}
