package org.fl.noodle.common.monitor.performance.storage;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import org.fl.noodle.common.monitor.performance.vo.KeyVo;

public class MemoryStorageTest {

	@Test
	public final void testGet() throws Exception {
		TestVo testVo = MemoryStorage.get("themeName", "monitorType", "moduleType", 1, TestVo.class);
		testVo.testStr = "Hello";
		testVo = MemoryStorage.get("themeName", "monitorType", "moduleType", 1, TestVo.class);
		assertNotNull(testVo);
		System.out.println(testVo.testStr);
	}

	@Test
	public final void testGetKeys() throws Exception {
		testGet();
		List<KeyVo> keyVoList = MemoryStorage.getKeys();
		for (KeyVo keyVo : keyVoList) {
			System.out.println(keyVo.toKeyString());
		}
	}

	static class TestVo {
		public String testStr;
	}
}
