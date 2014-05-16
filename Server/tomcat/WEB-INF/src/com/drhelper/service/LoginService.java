package com.drhelper.service;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.Login;
import com.drhelper.db.MysqlDB;
import com.drhelper.entity.User;

public class LoginService {
	public String doLogin(String reqBody) {
		String respBody = null;
		Login reqLogin = null;
		Login respLogin = null;
		
		//parse the body
		try{
			reqLogin = JSON.parseObject(reqBody, Login.class);
		}catch (Exception e) {
			System.out.println("LoginService.login(): json parse body failure");
			return respBody;
		}

		//check the input param
		String userName = reqLogin.getUserName();
		String userPasswd = reqLogin.getUserPasswd();
		if (userName.length() == 0 || userPasswd.length() == 0) {
			System.out.println("LoginService.login(): userName or userPasswd is null");
			return respBody;
		}
		
		respLogin = new Login();
		
		//create the connect to mysql
		MysqlDB db = new MysqlDB();
		boolean result = db.openConnect();
		if (!result) {
			respBody = JSON.toJSONString(respLogin);
			return respBody;
		}
		
		//check the user and passwd
		User user = db.getUser(userName, userPasswd);
		if (user == null) {
			respBody = JSON.toJSONString(respLogin);
			return respBody;
		}
		
		//create the resp object
		respLogin.setUserName(user.getUser_name());
		respLogin.setUserPasswd(user.getUser_passwd());
		
		//close the connect
		db.closeConnect();
		
		return respBody;
	}
}
