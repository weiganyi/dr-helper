package com.drhelper.web.service;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.web.bean.LogoutObject;

public class AjaxLogoutService implements Service<HttpSession, String, LogoutObject>{
	public LogoutObject doAction(HttpSession session, String... param) {
		LogoutObject resultObj = new LogoutObject();
		String webName = null;
		boolean result = false;

		result = doLogout(session);
		if (result != true) {
			return resultObj;
		}
		
		webName = getWebName();
		
		resultObj.setWebName(webName);
		return resultObj;
	}

	private boolean doLogout(HttpSession session) {
		if (session == null) {
			System.out.println("AjaxLogoutService.doLogout(): session isn't exist");
			return false;
		}else {
			//clear the session
			session.invalidate();
			return true;
		}
		
	}
	
	private String getWebName() {
		//get web_name
		DBManager db = new DBManager();
		String webName = db.getOptionString("web_name");

		return webName;
	}
}
