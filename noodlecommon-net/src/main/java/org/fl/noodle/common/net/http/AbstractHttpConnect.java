package org.fl.noodle.common.net.http;

public abstract class AbstractHttpConnect implements HttpConnect {
	
	protected String url;
	protected int connectTimeout;
	protected int readTimeout;
	protected String encoding = "utf-8";
	
	public AbstractHttpConnect(String url, int connectTimeout, int readTimeout) {
		this.url = url;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	public AbstractHttpConnect(String url, int connectTimeout, int readTimeout, String encoding) {
		this.url = url;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.encoding = encoding;
	}
	
	protected abstract <T> T requestTo(Method requestMethod, String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception;
	protected abstract <T> T requestTo(Method requestMethod, String[] requestParamNames, Object[] requestParamObjects, Class<T> responseClazz) throws Exception;
	protected abstract String requestTo(Method requestMethod, String requestParamName, String requestParamString) throws Exception;
	
	@Override
	public <T> T get(String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception {
		return requestTo(Method.GET, requestParamName, requestParamObject, responseClazz);
	}

	@Override
	public <T> T post(String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception {
		return requestTo(Method.POST, requestParamName, requestParamObject, responseClazz);
	}
	
	@Override
	public <T> T posts(String[] requestParamNames, Object[] requestParamObjects, Class<T> responseClazz) throws Exception {
		return requestTo(Method.POST, requestParamNames, requestParamObjects, responseClazz);
	}
	
	@Override
	public String getString(String requestParamName, String requestParamString) throws Exception {
		return requestTo(Method.GET, requestParamName, requestParamString);
	}
	
	@Override
	public String postString(String requestParamName, String requestParamString) throws Exception {
		return requestTo(Method.POST, requestParamName, requestParamString);
	}
	
	protected enum Method {
		
		GET("GET"), POST("POST");
		
		private String name;

		private Method(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}  
	}
}
