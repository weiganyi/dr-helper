package com.drhelper.service;

import javax.servlet.http.HttpSession;

public class LogoutService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		//clear the session
		session.invalidate();
		
		return null;
	}
}
