package org.fl.noodle.common.log;

import org.junit.Test;

public class LoggerTest {

	Logger logger = LoggerFactory.getLogger(LoggerTest.class);
	
	@Test
	public void test() {
		logger.info("test");
		logger.debug("test");
		logger.warn("test");
		logger.error("test");
		logger.trace("test");
		
		logger.printEnter("param");
		logger.printReturn("return");
		logger.printError(new Exception("error"), "param");
		logger.printWarn(new Exception("error"), "param");
	}
}
