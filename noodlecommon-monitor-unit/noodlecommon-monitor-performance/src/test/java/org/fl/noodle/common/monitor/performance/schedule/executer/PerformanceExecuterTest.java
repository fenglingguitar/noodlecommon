package org.fl.noodle.common.monitor.performance.schedule.executer;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/monitor/performance/receiver/noodlecommon-monitor-performance-receiver.xml",
		"classpath:org/fl/noodle/common/monitor/performance/schedule/executer/noodlecommon-monitor-performance-schedule-executer.xml"
})

public class PerformanceExecuterTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	PerformanceExecuter performanceExecuter;
	
	@Test
	public void testExecute() throws Exception {
		
		performanceExecuter.before("testService", "testMonitor", "testModule", "1");
		Thread.sleep(Math.round(Math.random() * 10));
		performanceExecuter.after("testService", "testService", "testMonitor", "testModule", "1", 200, true);

		performanceExecuter.before("testService", "testMonitor", "testModule", "2");
		Thread.sleep(Math.round(Math.random() * 500));
		performanceExecuter.after("testService", "testService", "testMonitor", "testModule", "2", 200, false);
		
		performanceExecuter.execute();
		
		Thread.sleep(1000);
	}
}
