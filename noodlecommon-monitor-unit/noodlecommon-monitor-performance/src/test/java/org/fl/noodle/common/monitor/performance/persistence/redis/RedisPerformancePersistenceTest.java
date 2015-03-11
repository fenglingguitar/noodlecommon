package org.fl.noodle.common.monitor.performance.persistence.redis;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.fl.noodle.common.monitor.performance.persistence.PerformancePersistence;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/monitor/performance/persistence/redis/noodlecommon-monitor-performance-persistence-redis.xml"
})

public class RedisPerformancePersistenceTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private PerformancePersistence performancePersistence;

	@Test
	public void testQueryList() throws Exception {
		long startTime = new Date().getTime();
		String keyName = "QueryListKeyName";
		TestVo testVo1 = new TestVo("v1", new Date());
		performancePersistence.insert(keyName, testVo1.getCreateDate().getTime(), testVo1);
		TestVo testVo2 = new TestVo("v2", new Date());
		performancePersistence.insert(keyName, testVo2.getCreateDate().getTime(), testVo2);
		TestVo testVo3 = new TestVo("v3", new Date());
		performancePersistence.insert(keyName, testVo3.getCreateDate().getTime(), testVo3);
		TestVo testVo4 = new TestVo("v4", new Date());
		performancePersistence.insert(keyName, testVo4.getCreateDate().getTime(), testVo4);
		List<TestVo> queryList = performancePersistence.queryList(keyName, startTime, new Date().getTime(), TestVo.class);
		assertTrue(queryList.size() == 4);
		for(TestVo testVo : queryList) {
			System.out.println("name: " + testVo.getName());
		}
	}
	
	@Test
	public void testInsert() throws Exception {
		String keyName = "insertKeyName";
		TestVo testVo1 = new TestVo("v1", new Date());
		performancePersistence.insert(keyName, testVo1.getCreateDate().getTime(), testVo1);
		List<TestVo> queryList = performancePersistence.queryList(keyName, testVo1.getCreateDate().getTime(), new Date().getTime(), TestVo.class);
		assertTrue(queryList.size() == 1);
		for(TestVo testVo : queryList) {
			System.out.println("name: " + testVo.getName());
		}
	}

	@Test
	public void testDeletes() throws Exception {
		long startTime = new Date().getTime();
		String keyName = "deletesKeyName";
		TestVo testVo1 = new TestVo("v1", new Date());
		performancePersistence.insert(keyName, testVo1.getCreateDate().getTime(), testVo1);
		TestVo testVo2 = new TestVo("v2", new Date());
		performancePersistence.insert(keyName, testVo2.getCreateDate().getTime(), testVo2);
		TestVo testVo3 = new TestVo("v3", new Date());
		performancePersistence.insert(keyName, testVo3.getCreateDate().getTime(), testVo3);
		TestVo testVo4 = new TestVo("v4", new Date());
		performancePersistence.insert(keyName, testVo4.getCreateDate().getTime(), testVo4);
		assertTrue(performancePersistence.deletes(keyName, startTime, new Date().getTime()) == 4);
	}
	
	@Test
	public void testGetKeys() throws Exception {
		String keyName = "getKeysKeyName";
		TestVo testVo1 = new TestVo("v1", new Date());
		performancePersistence.insert(keyName, testVo1.getCreateDate().getTime(), testVo1);
		TestVo testVo2 = new TestVo("v2", new Date());
		performancePersistence.insert(keyName, testVo2.getCreateDate().getTime(), testVo2);
		TestVo testVo3 = new TestVo("v3", new Date());
		performancePersistence.insert(keyName, testVo3.getCreateDate().getTime(), testVo3);
		TestVo testVo4 = new TestVo("v4", new Date());
		performancePersistence.insert(keyName, testVo4.getCreateDate().getTime(), testVo4);
		Set<String> keysSet = performancePersistence.getKeys();
		assertTrue(keysSet.size() >= 4);
		for (String key : keysSet) {
			System.out.println("key: " + key);
		}
	}
	
	static class TestVo {
		
		private String name;
		private Date createDate;

		public TestVo() {
		}

		public TestVo(String name, Date createDate) {
			this.name = name;
			this.createDate = createDate;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
	}
}
