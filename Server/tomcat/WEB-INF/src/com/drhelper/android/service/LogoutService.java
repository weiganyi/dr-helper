package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.drhelper.common.service.Service;

public class LogoutService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		//clear the session
		session.invalidate();
		
		return null;
	}
}
