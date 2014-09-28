package org.fengling.noodlecommon.dbrwseparate.aop;

import org.fengling.noodlecommon.dbrwseparate.aop.service.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fengling/noodlecommon/dbrwseparate/aop/noodlecommon-aop-failover.xml"
})
public class FailoverMethodInterceptorTest extends AbstractJUnit4SpringContextTests {

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
			testService.queryData();
			testService.insertData("好吧");
			Thread.sleep(3000);
		}
	}
}
