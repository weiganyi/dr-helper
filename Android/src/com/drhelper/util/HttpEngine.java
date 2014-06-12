package com.drhelper.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpEngine {
	private static final String HTTP_ENGINE_TAG = "HttpEngine";
	
	public static String doPost(String url, String reqBody) {
		HttpClient client = new DefaultHttpClient();

		if (url == null || url.length() == 0) {
			Log.e(HTTP_ENGINE_TAG, "HttpEngine.doPost(): input param url is null");
			return null;
		}
		
		String fullUrl = "http://" + PrefsManager.getServer_address() + "/" + url;
		HttpPost request = new HttpPost(fullUrl);
		
		if (reqBody != null && reqBody.length() != 0) {
			try	{
				StringEntity entity = new StringEntity(reqBody, "UTF-8");
				request.setEntity(entity);
			}catch(Exception e)	{
				Log.e(HTTP_ENGINE_TAG, "HttpEngine.doPost(): construct entity failure");
			}
		}
		
		//add the cookie
		CookieManager.addCookie(request);
		
		try {
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				//save the cookie
				CookieManager.saveCookie(response);
				
				String respBody = EntityUtils.toString(response.getEntity());
				return respBody;
			}
		}catch(Exception e) {
			Log.e(HTTP_ENGINE_TAG, "HttpEngine.doPost(): send http request or recv respose failure");
		}
		
		return null;
	}
}
