package org.fl.noodle.common.monitor.performance.schedule;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/monitor/performance/schedule/noodlecommon-monitor-performance-clean-schedule.xml"
})

public class CleanExecuterScheduleTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void test() {
	}
}
