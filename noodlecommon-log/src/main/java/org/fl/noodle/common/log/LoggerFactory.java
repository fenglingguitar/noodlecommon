package org.fl.noodle.common.log;

public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		org.slf4j.Logger inputLogger = org.slf4j.LoggerFactory.getLogger(clazz);
		return new Logger(inputLogger);
	}
	
	public static Logger getLogger(String name) {
		org.slf4j.Logger inputLogger = org.slf4j.LoggerFactory.getLogger(name);
		return new Logger(inputLogger);
	}
}
