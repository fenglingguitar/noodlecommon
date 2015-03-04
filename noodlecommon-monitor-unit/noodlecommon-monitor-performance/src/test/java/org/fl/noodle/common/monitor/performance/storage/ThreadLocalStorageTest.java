package org.fl.noodle.common.monitor.performance.storage;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ThreadLocalStorageTest {

	@Test
	public final void testGet() throws Exception {
		TestVo testVo = ThreadLocalStorage.get("themeName", "monitorType", "moduleType", 1,  TestVo.class);
		testVo.testStr = "Hello";
		testVo = ThreadLocalStorage.get("themeName", "monitorType", "moduleType", 1, TestVo.class);
		assertNotNull(testVo);
		System.out.println(testVo.testStr);
	} 
	
	static class TestVo {
		public String testStr;
	}
}

