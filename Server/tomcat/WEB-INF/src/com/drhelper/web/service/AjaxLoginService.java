package com.drhelper.web.service;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.User;

public class AjaxLoginService{
	public boolean doLogin(HttpSession session, String userName, String userPasswd) {
		//check the user and passwd
		DBManager db = new DBManager();
		User user = db.getUser(userName, userPasswd);
		if (user == null) {
			return false;
		}
		
		//check the authority of this user
		if (user.getUser_auth().equals("chef") != true &&
				user.getUser_auth().equals("admin") != true) {
			return false;
		}
		
		//save the user name and auth
		session.setAttribute("id", user.getUser_name());
		session.setAttribute("auth", user.getUser_auth());

		return true;
	}
}
