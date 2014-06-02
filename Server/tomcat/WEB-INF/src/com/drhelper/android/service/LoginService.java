package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.Login;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.User;

public class LoginService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		Login reqLogin = null;
		Login respLogin = null;
		String respBody = null;
		
		//parse the body
		try{
			reqLogin = JSON.parseObject(reqBody, Login.class);
		}catch (Exception e) {
			System.out.println("LoginService.doAction(): json parse body failure");
			return respBody;
		}

		//check the input param
		String userName = reqLogin.getUserName();
		String userPasswd = reqLogin.getUserPasswd();
		if (userName.length() == 0 || userPasswd.length() == 0) {
			System.out.println("LoginService.doAction(): userName or userPasswd is null");
			return respBody;
		}
		
		respLogin = new Login();
		
		//check the user and passwd
		DBManager db = new DBManager();
		User user = db.getUser(userName, userPasswd);
		if (user == null) {
			respLogin.setResult(false);
			respBody = JSON.toJSONString(respLogin);
			return respBody;
		}
		
		//create the resp object
		respLogin.setUserName(user.getUser_name());
		respLogin.setUserPasswd(user.getUser_passwd());
		respLogin.setResult(true);
		
		//save the user name
		session.setAttribute("id", user.getUser_name());
		
		//serialize the object
		respBody = JSON.toJSONString(respLogin);
		return respBody;
	}
}
