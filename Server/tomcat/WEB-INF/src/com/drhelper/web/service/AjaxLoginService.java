package com.drhelper.web.service;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.User;
import com.drhelper.web.bean.LoginObject;

public class AjaxLoginService implements Service<HttpSession, String, LoginObject> {
	public LoginObject doAction(HttpSession session, String... param) {
		LoginObject resultObj = new LoginObject();
		String user = null;
		String passwd = null;
		boolean result = false;
		String webName = null;

		//parse the params
		if (param[0] != null) {
			user = param[0];
		}
		if (param[1] != null) {
			passwd = param[1];
		}

		webName = getWebName();
		result = doLogin(session, user, passwd);
		
		resultObj.setWebName(webName);
		resultObj.setLoginResult(result);
		resultObj.setLoginUser(user);
		resultObj.setLoginPasswd(passwd);
		resultObj.setAuth((String)session.getAttribute("auth"));
		return resultObj;
	}

	public boolean doLogin(HttpSession session, String userName, String userPasswd) {
		//check input param
		if (userName == null || userName.equals("") == true ||
				userPasswd == null || userPasswd.equals("") == true) {
			System.out.println("AjaxLoginService.doLogin(): request params is incorrect");
			return false;
		}
		
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

	public String getWebName() {
		//get web_name
		DBManager db = new DBManager();
		String webName = db.getOptionString("web_name");

		return webName;
	}
}
