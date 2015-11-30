package org.fl.noodle.common.util.net;

import org.junit.Test;

public class NetAddressUtilTest {

	@Test
	public void testGetLocalIp() {
		System.out.println("local ip: " + NetAddressUtil.getLocalIp());
	}
	
	@Test
	public void testGetLocalHostName() {
		System.out.println("local host name: " + NetAddressUtil.getLocalHostName());
	}
}
