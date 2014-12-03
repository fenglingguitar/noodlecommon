package org.fl.noodle.common.demo;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.fl.noodle.common.demo.tools.DemoTools;
import org.fl.noodle.common.demo.vo.DemoVo;
import org.fl.noodle.common.util.net.http.HttpConnect;
import org.fl.noodle.common.util.net.http.jdk.HttpConnectJdk;
import org.junit.Test;

public class DemoControllerApiTest {

	@Test
	public void testQueryMapListJava() throws Exception {
		
		HttpConnect HttpConnect = new HttpConnectJdk("http://localhost:8080/noodlecommon/demo/querymaplist-java", 3000, 3000);
		
		int caseByteLength = 256;
		byte[] caseByteArray = new byte[caseByteLength];
		for (int i=0; i<caseByteLength; i++) {
			caseByteArray[i] = (byte)0x41;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, List<DemoVo>> demoVoResult = HttpConnect.post("input", DemoTools.getVo(1), Map.class);
		
		assertNotNull(demoVoResult);
		assertNotNull(demoVoResult.get("input"));
		assertNotNull(demoVoResult.get("output"));
		assertNotNull(demoVoResult.get("output").get(0));
		assertNotNull(demoVoResult.get("output").get(0).getId());
		
		System.out.println(demoVoResult.get("output").get(0));
	}
}
