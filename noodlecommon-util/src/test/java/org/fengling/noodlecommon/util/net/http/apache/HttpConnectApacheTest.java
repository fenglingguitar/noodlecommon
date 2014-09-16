package org.fengling.noodlecommon.util.net.http.apache;

import org.fengling.noodlecommon.util.net.http.HttpConnect;
import org.junit.Test;

public class HttpConnectApacheTest {

	HttpConnect httpConnect = new HttpConnectApache("http://127.0.0.1:80/test", 3000, 3000);
	
	@Test
	public void testGet() throws Exception {
		System.out.println(httpConnect.get("input", "test好的", String.class));
	}

	@Test
	public void testPost() throws Exception {
		System.out.println(httpConnect.post("input", "test好的", String.class));
	}

}
