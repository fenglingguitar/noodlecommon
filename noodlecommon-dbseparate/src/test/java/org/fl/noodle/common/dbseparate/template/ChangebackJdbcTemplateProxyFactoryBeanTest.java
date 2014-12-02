package org.fl.noodle.common.dbseparate.template;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/dbseparate/template/noodlecommon-jdbctemplate-changeback.xml"
})
public class ChangebackJdbcTemplateProxyFactoryBeanTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	JdbcOperations changebackJdbcTemplate;
	
	@Test
	public void testUpdate() {
		
		assertTrue(changebackJdbcTemplate.update("update dbseparate_test set name=? where id=?", new Object[] {
				"我说",
				1
		}) == 1);
	}
}
