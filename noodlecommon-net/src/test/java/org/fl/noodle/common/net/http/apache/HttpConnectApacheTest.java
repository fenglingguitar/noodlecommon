package org.fl.noodle.common.net.http.apache;

import org.fl.noodle.common.net.http.apache.HttpConnectApache;
import org.fl.noodle.common.net.http.HttpConnect;
import org.junit.Test;

public class HttpConnectApacheTest {

	HttpConnect httpConnect = new HttpConnectApache("http://127.0.0.1:8080/noodlecommon/demo/string", 3000, 3000);
	
	@Test
	public void testGet() throws Exception {
		System.out.println(httpConnect.get("input", "test好的", String.class));
	}

	@Test
	public void testPost() throws Exception {
		System.out.println(httpConnect.post("input", "test好的", String.class));
	}
	
	@Test
	public void testPosts() throws Exception {
		System.out.println(httpConnect.posts(new String[] {"input"}, new Object[] {"test好的"}, String.class));
	}

	@Test
	public void testGetString() throws Exception {
		System.out.println(httpConnect.getString("input", "test好的"));
	}

	@Test
	public void testPostString() throws Exception {
		System.out.println(httpConnect.postString("input", "test好的"));
	}
}
