package org.fl.noodle.common.monitor.performance.schedule.executer;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/monitor/performance/schedule/executer/callcenter-monitor-performance-clean-schedule-executer.xml"
})

public class PerformanceCleanExecuterTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	PerformanceCleanExecuter performanceCleanExecuter;
	
	@Test
	public void testExecute() throws Exception {
		
		performanceCleanExecuter.execute();
		
		Thread.sleep(1000);
	}
}
