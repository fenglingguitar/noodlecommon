package org.fl.noodle.common.dbseparate.aop;

import org.fl.noodle.common.dbseparate.aop.service.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/dbseparate/aop/noodlecommon-aop-changeback.xml"
})
public class ChangeBackMethodInterceptorTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	TestService testService;
	
	@Test
	public void test() throws Exception {
		testService.queryData();
		testService.insertData("好吧");
	}
	
	@Test
	public void testRepeat() throws Exception {
		
		for (int i=0; i<20; i++) {	
			try {
				testService.queryData();
			} catch (Exception e) {
				System.out.println("queryData -> Exception: " + e);
			}
			try {
				testService.insertData("好吧");				
			} catch (Exception e) {
				System.out.println("insertData -> Exception: " + e);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}
}
