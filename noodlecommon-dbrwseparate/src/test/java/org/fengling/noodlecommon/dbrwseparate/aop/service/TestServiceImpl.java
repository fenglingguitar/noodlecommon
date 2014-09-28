package org.fengling.noodlecommon.dbrwseparate.aop.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.sql.DataSource;

import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.springframework.beans.factory.annotation.Autowired;

public class TestServiceImpl implements TestService {

	@Autowired
	DataSource dataSource;
	
	@Override
	public List<String> queryData() throws Exception {
		assertNotNull(DataSourceSwitch.getDataSourceType());
		System.out.println("queryData -> DataSource: " + DataSourceSwitch.getDataSourceType());
		dataSource.getConnection();
		return null;
	}

	@Override
	public void insertData(String name) throws Exception {
		assertNotNull(DataSourceSwitch.getDataSourceType());
		System.out.println("insertData -> DataSource: " + DataSourceSwitch.getDataSourceType());
		dataSource.getConnection();
	}
}
