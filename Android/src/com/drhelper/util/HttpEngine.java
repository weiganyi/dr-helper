package com.drhelper.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpEngine {
	private static String httpSrvBaseUrl = "http://172.16.3.146/DrHelperServer/";
	private static final String HTTPENGINE_TAG = "HttpEngine";
	
	public static String getHttpSrvBaseUrl() {
		return httpSrvBaseUrl;
	}
	
	public static void setHttpSrvBaseUrl(String httpSrvBaseUrl)	{
		HttpEngine.httpSrvBaseUrl = httpSrvBaseUrl;
	}
	
	public static String doPost(String url, String reqBody) {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		try	{
			StringEntity entity = new StringEntity(reqBody);
			request.setEntity(entity);
		}catch(Exception e)	{
			Log.e(HTTPENGINE_TAG, "HttpEngine.doPost(): construct entity failure");
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
			Log.e(HTTPENGINE_TAG, "HttpEngine.doPost(): send request and recv respose failure");
		}
		
		return null;
	}
}
