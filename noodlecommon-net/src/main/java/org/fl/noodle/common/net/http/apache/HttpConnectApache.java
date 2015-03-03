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


public class HttpConnectApache extends AbstractHttpConnect {

	public HttpConnectApache(String url, int connectTimeout, int readTimeout) {
		super(url, connectTimeout, readTimeout);
	}
	
	public String requestTo(Method requestMethod, String requestParamName, String jsonStr) throws Exception {
		
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(readTimeout).build();

		HttpUriRequest httpUriRequest = null;
		if (requestMethod == Method.GET) {
			String requestUrl = new StringBuilder().append(url).append("?").append(requestParamName).append("=").append(URLEncoder.encode(jsonStr, "utf-8")).toString();
			HttpGet httpGet = new HttpGet(requestUrl);
			httpGet.setConfig(requestConfig);
			httpUriRequest = httpGet;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(1); 
			nameValuePairList.add(new BasicNameValuePair(requestParamName, jsonStr));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
			httpUriRequest = httpPost;
		}
		
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
