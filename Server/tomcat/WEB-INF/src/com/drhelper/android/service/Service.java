package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

public abstract class Service {
	public abstract String doAction(HttpSession session, String reqBody);
}
