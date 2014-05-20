package com.drhelper.service;

import javax.servlet.http.HttpSession;

abstract class Service {
	public abstract String doAction(HttpSession session, String reqBody);
}
