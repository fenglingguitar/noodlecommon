package org.fl.noodle.common.monitor.performance.net;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/monitor/performance/net/callcenter-monitor-performance-net-server.xml",
		"classpath:org/fl/noodle/common/monitor/performance/net/callcenter-monitor-performance-net-client.xml"
})
public class UdpTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	UdpClient udpClient;
	
	@Autowired
	UdpServer udpServer;
	
	@Test
	public final void test() throws Exception {
		udpClient.send("Hello");
		System.out.println("Send: " + "Hello");
		System.out.println("Recv: " + udpServer.get());
	}
}
