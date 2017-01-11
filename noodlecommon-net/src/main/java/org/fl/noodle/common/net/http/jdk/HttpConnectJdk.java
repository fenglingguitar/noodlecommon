package org.fl.noodle.common.net.http.jdk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.fl.noodle.common.net.http.AbstractHttpConnect;
import org.fl.noodle.common.util.json.JsonTranslator;

public class HttpConnectJdk extends AbstractHttpConnect {

	public HttpConnectJdk(String url, int connectTimeout, int readTimeout) {
		super(url, connectTimeout, readTimeout);
	}
	
	public HttpConnectJdk(String url, int connectTimeout, int readTimeout, String encoding) {
		super(url, connectTimeout, readTimeout, encoding);
	}
	
	@Override
	public void connect() throws Exception {
		URL httpUrl = new URL(this.url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setUseCaches(false);  
		httpURLConnection.setConnectTimeout(connectTimeout);
		httpURLConnection.setReadTimeout(readTimeout);             
		httpURLConnection.setRequestProperty("Accept-Charset", encoding);
		httpURLConnection.setRequestProperty("Connection", "close");
		httpURLConnection.setRequestProperty("Keep-Alive", "close");
		httpURLConnection.connect();
	}
	
	@Override
	public void close() {
	}
	
	public <T> T requestTo(Method requestMethod, String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception {
		
		StringBuilder stringBuilder = new StringBuilder().append(requestParamName).append("=").append(URLEncoder.encode(JsonTranslator.toString(requestParamObject), encoding));

		return JsonTranslator.fromString(requestExecute(requestMethod, stringBuilder.toString(), readTimeout), responseClazz);
	}
	
	public <T> T requestTo(Method requestMethod, String[] requestParamNames, Object[] requestParamObjects, Class<T> responseClazz) throws Exception {
				
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i=0; i<requestParamNames.length; i++) {
			stringBuilder.append(requestParamNames[i]).append("=").append(URLEncoder.encode(JsonTranslator.toString(requestParamObjects[i]), encoding));
			if(i != requestParamNames.length - 1) stringBuilder.append("&");
		}

		return JsonTranslator.fromString(requestExecute(requestMethod, stringBuilder.toString(), readTimeout), responseClazz);
	}
	
	public String requestTo(Method requestMethod, String requestParamName, String requestParamString, int readTimeout) throws Exception {
		
		StringBuilder stringBuilder = new StringBuilder().append(requestParamName).append("=").append(requestParamString);
		
		return requestExecute(requestMethod, stringBuilder.toString(), readTimeout);
	}
	
	private String requestExecute(Method requestMethod, String request, int readTimeout) throws Exception {
		
		URL httpUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod(requestMethod.getName());
		httpURLConnection.setUseCaches(false);  
		httpURLConnection.setConnectTimeout(connectTimeout);
		httpURLConnection.setReadTimeout(readTimeout);             
		httpURLConnection.setRequestProperty("Accept-Charset", encoding);
		httpURLConnection.setRequestProperty("Connection", "keepalive");
		httpURLConnection.setRequestProperty("Keep-Alive", "30");
		httpURLConnection.connect();
		
		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
		printWriter.print(request);
		printWriter.flush();
		printWriter.close();
        
		String line;
		StringBuilder response = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		while ((line = bufferedReader.readLine()) != null) {
			response.append(line);
		}
		
		return response.toString();
	}
}
