package com.drhelper.util;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public class CookieManager {
	private static String cookie = null;
	
	public static void saveCookie(HttpResponse response) {
		//get the Set-Cookie header
		Header[] headers = response.getHeaders("Set-Cookie");
		if (headers.length != 0) {
			Header header = headers[0];
			cookie = header.getValue();
		}
	}

	public static void addCookie(HttpRequest request) {
		if (cookie != null && cookie.equals("") != true) {
			request.addHeader("cookie", cookie);
		}
	}
}
