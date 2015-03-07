package org.fl.noodle.common.net.http;

public interface HttpConnect {
	
	public void connect() throws Exception;
	public void close();
	
	public <T> T get(String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception;
	public <T> T post(String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception;
	public <T> T posts(String[] requestParamNames, Object[] requestParamObjects, Class<T> responseClazz) throws Exception;
	
	public String getString(String requestParamName, String requestParamString) throws Exception;
	public String postString(String requestParamName, String requestParamString) throws Exception;
}
