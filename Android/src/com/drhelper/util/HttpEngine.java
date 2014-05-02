package com.drhelper.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public class HttpEngine {
	private static final String HTTP_ENGINE_TAG = "HttpEngine";

	private static String httpSrvBaseUrl = "http://172.16.3.146/DrHelperServer/";
	
	public static String getHttpSrvBaseUrl() {
		return httpSrvBaseUrl;
	}
	
	public static void setHttpSrvBaseUrl(String httpSrvBaseUrl)	{
		if (httpSrvBaseUrl != null) {
			HttpEngine.httpSrvBaseUrl = httpSrvBaseUrl;
		}else {
			Log.e(HTTP_ENGINE_TAG, "HttpEngine.setHttpSrvBaseUrl(): input param httpSrvBaseUrl is null");
		}
	}
	
	public static String doPost(String url, String reqBody) {
		HttpClient client = new DefaultHttpClient();

		if (url == null || url.length() == 0) {
			Log.e(HTTP_ENGINE_TAG, "HttpEngine.doPost(): input param url is null");
			return null;
		}
		
		HttpPost request = new HttpPost(url);

		if (reqBody != null && reqBody.length() != 0) {
			try	{
				StringEntity entity = new StringEntity(reqBody);
				request.setEntity(entity);
			}catch(Exception e)	{
				Log.e(HTTP_ENGINE_TAG, "HttpEngine.doPost(): construct entity failure");
			}
		}
		
		try {
			/*
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String respBody = EntityUtils.toString(response.getEntity());
				return respBody;
			}*/
			return reqBody;
		}catch(Exception e) {
			Log.e(HTTP_ENGINE_TAG, "HttpEngine.doPost(): send http request or recv respose failure");
		}
		
		return null;
	}
}
