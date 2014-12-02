package org.fl.noodle.common.util.net.http;

import org.fl.noodle.common.util.json.JsonTranslator;

public abstract class AbstractHttpConnect implements HttpConnect {
	
	protected String url;
	protected int connectTimeout;
	protected int readTimeout;
	
	public AbstractHttpConnect(String url, int connectTimeout, int readTimeout) {
		this.url = url;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	private <T> T send(Method requestMethod, String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception {
		
		String jsonStr = JsonTranslator.toString(requestParamObject);
		
		String recvJson = requestTo(requestMethod, requestParamName, jsonStr);
		
		return JsonTranslator.fromString(recvJson, responseClazz);
	}
	
	protected abstract String requestTo(Method requestMethod, String requestParamName, String jsonStr) throws Exception;
	
	@Override
	public <T> T get(String name, Object object, Class<T> clazz) throws Exception {
		return send(Method.GET, name, object, clazz);
	}

	@Override
	public <T> T post(String name, Object object, Class<T> clazz) throws Exception {
		return send(Method.POST, name, object, clazz);
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
