package org.fl.noodle.common.util.net.http;

public interface HttpConnect {
	
	public <T> T get(String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception;
	public <T> T post(String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception;
}
