package org.fengling.noodlecommon.dbrwseparate.aop;

import static org.junit.Assert.*;

import java.util.List;

import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fengling/noodlecommon/dbrwseparate/aop/noodlecommon-aop.xml"
})
public class TestServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private TestService testService;
	
	@Test
	public void test() throws Exception {
		testService.queryData();
		testService.insertData("好吧");
	}
	
	public static interface TestService {
		public List<String> queryData() throws Exception;
		public void insertData(String name) throws Exception;
	}
	
	public static class TestServiceImpl implements TestService {

		@Override
		public List<String> queryData() {
			assertNotNull(DataSourceSwitch.getDataSourceType());
			System.out.println("DataSource: " + DataSourceSwitch.getDataSourceType());
			return null;
		}

		@Override
		public void insertData(String name) {
			assertNotNull(DataSourceSwitch.getDataSourceType());
			assertTrue(DataSourceSwitch.getDataSourceType().equals(DataSourceType.MASTER));
			System.out.println("DataSource: " + DataSourceSwitch.getDataSourceType());
		}
	}
}
