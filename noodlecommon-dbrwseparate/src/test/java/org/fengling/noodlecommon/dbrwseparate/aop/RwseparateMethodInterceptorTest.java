package org.fengling.noodlecommon.dbrwseparate.aop;

import org.fengling.noodlecommon.dbrwseparate.aop.service.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fengling/noodlecommon/dbrwseparate/aop/noodlecommon-aop-rwseparate.xml"
})
public class RwseparateMethodInterceptorTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	TestService testService;
	
	@Test
	public void test() throws Exception {
		testService.queryData();
		testService.insertData("好吧");
	}
	
	@Test
	public void testRepeat() {
		
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
