package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Option;
import com.drhelper.web.bean.AdminOptionObject;

public class AjaxAdminOptionService implements Service<HttpSession, String, AdminOptionObject>{
	public AdminOptionObject doAction(HttpSession session, String... param) {
		AdminOptionObject resultObj = null;
		String op = null;
		String name = null;
		String item = null;
		
		if (checkAuth(session) != true) {
			return resultObj;
		}
		
		//parse the params
		if (param[0] != null) {
			op = param[0];
		}
		if (param[1] != null) {
			name = param[1];
		}
		if (param[2] != null) {
			item = param[2];
		}

		if (op != null) {
			if (op.equals("commit") == true) { 
				commitAdminOptionItem(session, name, item);
			}
		}

		resultObj = getAdminOptionObject();
	
		return resultObj;
	}
	
	private boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxAdminOptionService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the admin
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("admin") != true) {
			System.out.println("AjaxAdminOptionService.checkAuth(): auth isn't admin");
			return false;
		}
		
		return true;
	}

	private boolean commitAdminOptionItem(HttpSession session, 
			String name, 
			String item) {
		boolean result = false;

		//add or modify a option
		DBManager db = new DBManager();
		result = db.commitAdminOptionItem(name, item);

		return result;
	}

	private AdminOptionObject getAdminOptionObject() {
		ArrayList<Option> optionList = null;
		String webName = null;
		String itemPerPage = null;

		//get the option list
		DBManager db = new DBManager();
		optionList = db.getOptionList();
		if (optionList == null) {
			return null;
		}
		
		//construct the table2List
		ListIterator<Option> it = optionList.listIterator(0);
		while (it.hasNext()) {
			Option obj = it.next();

			if (obj.getOption_name().equals("web_name") == true) {
				webName = obj.getOption_value();
			}else if (obj.getOption_name().equals("item_per_page") == true) {
				itemPerPage = obj.getOption_value();
			}
		}

		AdminOptionObject obj = new AdminOptionObject();
		obj.setWebName(webName);
		obj.setItemPerPage(itemPerPage);
		
		return obj;
	}
}
