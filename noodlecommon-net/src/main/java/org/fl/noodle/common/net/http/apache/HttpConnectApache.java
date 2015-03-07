package org.fl.noodle.common.net.http.apache;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.fl.noodle.common.net.http.AbstractHttpConnect;
import org.fl.noodle.common.util.json.JsonTranslator;


public class HttpConnectApache extends AbstractHttpConnect {

	public HttpConnectApache(String url, int connectTimeout, int readTimeout) {
		super(url, connectTimeout, readTimeout);
	}
	
	public HttpConnectApache(String url, int connectTimeout, int readTimeout, String encoding) {
		super(url, connectTimeout, readTimeout, encoding);
	}
	
	@Override
	public void connect() throws Exception {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(readTimeout).build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
	}
	
	@Override
	public void close() {
	}
	
	public <T> T requestTo(Method requestMethod, String requestParamName, Object requestParamObject, Class<T> responseClazz) throws Exception {
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(readTimeout).build();

		HttpUriRequest httpUriRequest = null;
		if (requestMethod == Method.GET) {
			StringBuilder stringBuilder = new StringBuilder().append(url).append("?").append(requestParamName).append("=").append(URLEncoder.encode(JsonTranslator.toString(requestParamObject), encoding));
			String requestUrl = stringBuilder.toString();
			HttpGet httpGet = new HttpGet(requestUrl);
			httpGet.setConfig(requestConfig);
			httpUriRequest = httpGet;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(1); 
			nameValuePairList.add(new BasicNameValuePair(requestParamName, JsonTranslator.toString(requestParamObject)));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, encoding));
			httpUriRequest = httpPost;
		}
		
		return JsonTranslator.fromString(requestExecute(httpUriRequest), responseClazz);
	}
	
	public <T> T requestTo(Method requestMethod, String[] requestParamNames, Object[] requestParamObjects, Class<T> responseClazz) throws Exception {
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(readTimeout).build();

		HttpUriRequest httpUriRequest = null;
		if (requestMethod == Method.GET) {
			StringBuilder stringBuilder = new StringBuilder().append(url).append("?");
			for (int i=0; i<requestParamNames.length; i++) {
				stringBuilder.append(requestParamNames[i]).append("=").append(URLEncoder.encode(JsonTranslator.toString(requestParamObjects[i]), encoding));
				if(i != requestParamNames.length - 1) stringBuilder.append("&");
			}
			String requestUrl = stringBuilder.toString();
			HttpGet httpGet = new HttpGet(requestUrl);
			httpGet.setConfig(requestConfig);
			httpUriRequest = httpGet;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(1); 
			for (int i=0; i<requestParamNames.length; i++) {
				nameValuePairList.add(new BasicNameValuePair(requestParamNames[i], JsonTranslator.toString(requestParamObjects[i])));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, encoding));
			httpUriRequest = httpPost;
		}
		
		return JsonTranslator.fromString(requestExecute(httpUriRequest), responseClazz);
	}
	
	public String requestTo(Method requestMethod, String requestParamName, String requestParamString) throws Exception {
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(readTimeout).build();

		HttpUriRequest httpUriRequest = null;
		if (requestMethod == Method.GET) {
			String requestUrl = new StringBuilder().append(url).append("?").append(requestParamName).append("=").append(requestParamString).toString();
			HttpGet httpGet = new HttpGet(requestUrl);
			httpGet.setConfig(requestConfig);
			httpUriRequest = httpGet;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(1); 
			nameValuePairList.add(new BasicNameValuePair(requestParamName, requestParamString));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, encoding));
			httpUriRequest = httpPost;
		}
		return requestExecute(httpUriRequest);
	}
	
	private String requestExecute(HttpUriRequest httpUriRequest) throws Exception {
		
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		
		try {
			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpUriRequest);
			try {
			    HttpEntity entity = closeableHttpResponse.getEntity();
			    if (entity != null) {
			    	InputStream instream = entity.getContent();
	                try {
	                	String line;
	            		StringBuilder responseStringBuilder = new StringBuilder();
	            		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(instream));
	            		while ((line = bufferedReader.readLine()) != null) {
	            			responseStringBuilder.append(line);
	            		}
	            		return responseStringBuilder.toString();
	                } finally {
	                    instream.close();
	                }
	            }
			} finally {
				closeableHttpResponse.close();
			}
		} finally {
			closeableHttpClient.close();
        }
		
		return null;
	}
}
