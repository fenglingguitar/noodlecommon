package org.fl.noodle.common.util.net.http.jdk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.fl.noodle.common.util.net.http.AbstractHttpConnect;


public class HttpConnectJdk extends AbstractHttpConnect {

	public HttpConnectJdk(String url, int connectTimeout, int readTimeout) {
		super(url, connectTimeout, readTimeout);
	}
	
	public String requestTo(Method requestMethod, String requestParamName, String jsonStr) throws Exception {
		
		String requestStr = new StringBuilder().append(requestParamName).append("=").append(URLEncoder.encode(jsonStr, "utf-8")).toString();
		
		URL httpUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod(requestMethod.getName());
		httpURLConnection.setUseCaches(false);  
		httpURLConnection.setConnectTimeout(connectTimeout);
		httpURLConnection.setReadTimeout(readTimeout);             
		httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
		httpURLConnection.setRequestProperty("Connection", "keepalive");
		httpURLConnection.setRequestProperty("Keep-Alive", "30");
		httpURLConnection.connect();
		
		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
		printWriter.print(requestStr);
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
