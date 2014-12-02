package org.fl.noodle.common.dynamicsql;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/dynamicsql/noodlecommon-dynamicsql.xml"
})
public class DynamicSqlTemplateTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void testQueryPage() {
	}

	@Test
	public void testQueryList() {
	}

	@Test
	public void testQueryPageSql() {
	}

	@Test
	public void testQueryListSql() {
	}

	@Test
	public void testInsert() {
	}

	@Test
	public void testUpdate() {
	}

	@Test
	public void testUpdateInclude() {
	}
	
	@Test
	public void testUpdateExcept() {
	}

	@Test
	public void testUpdateNonull() {
	}

	@Test
	public void testUpdateNonullNoById() {
	}

	@Test
	public void testUpdateSql() {
	}

	@Test
	public void testDelete() {
	}

	@Test
	public void testDeleteNoById() {
	}

	@Test
	public void testLoad() {
	}

	@Test
	public void testInsertOrUpdate() {
	}
}
