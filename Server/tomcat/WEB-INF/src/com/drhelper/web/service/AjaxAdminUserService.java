package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.User;
import com.drhelper.web.bean.AdminUserObject;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxAdminUserService implements Service<HttpSession, String, AdminUserObject>{
	public AdminUserObject doAction(HttpSession session, String... param) {
		AdminUserObject resultObj = null;
		String op = null;
		String id = null;
		String name = null;
		String passwd = null;
		String auth = null;
		String page = null;
		
		if (checkAuth(session) != true) {
			return resultObj;
		}
		
		//parse the params
		if (param[0] != null) {
			op = param[0];
		}
		if (param[1] != null) {
			id = param[1];
		}
		if (param[2] != null) {
			name = param[2];
		}
		if (param[3] != null) {
			passwd = param[3];
		}
		if (param[4] != null) {
			auth = param[4];
		}
		if (param[5] != null) {
			page = param[5];
		}

		if (op != null) {
			if (op.equals("commit") == true) { 
				commitAdminUserItem(session, id, name, passwd, auth);
			}else if (op.equals("delete") == true) { 
				deleteAdminUserItem(session, id);
			}
		}

		resultObj = getAdminUserObject(page);
	
		return resultObj;
	}
	
	public boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxAdminUserService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the admin
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("admin") != true) {
			System.out.println("AjaxAdminUserService.checkAuth(): auth isn't admin");
			return false;
		}
		
		return true;
	}

	public boolean commitAdminUserItem(HttpSession session, 
			String id, 
			String name, 
			String passwd, 
			String auth) {
		boolean result = false;
		int idNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}

		//add or modify a user
		DBManager db = new DBManager();
		result = db.commitAdminUserItem(idNum, name, passwd, auth);

		return result;
	}

	public boolean deleteAdminUserItem(HttpSession session, String id) {
		boolean result = false;
		int idNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}else {
			return false;
		}

		//add or modify a user
		DBManager db = new DBManager();
		result = db.deleteAdminUserItem(idNum);

		return result;
	}

	public AdminUserObject getAdminUserObject(String page) {
		ArrayList<User> userList = null;
		ArrayList<User> user2List = null;
		int itemNum = 0;
		int currPage = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}

		//get the order list
		DBManager db = new DBManager();
		userList = db.getUserList();
		if (userList == null) {
			return null;
		}
		
		//get the item_per_page
		int itemPerPage = db.getOptionInt("item_per_page");
		
		//calculate the start and end
		int start = (currPage-1)*itemPerPage;
		int end = start+itemPerPage;
		
		//construct the user2List
		user2List = new ArrayList<User>();
		int size = userList.size();
		ListIterator<User> it = userList.listIterator(size);
		while (it.hasPrevious()) {
			User obj = it.previous();

			if (itemNum >= start && itemNum < end) {
				user2List.add(obj);
			}

			itemNum++;
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		User[] userArray = new User[user2List.size()];
		user2List.toArray(userArray);

		AdminUserObject obj = new AdminUserObject();
		obj.setUser(userArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		
		return obj;
	}
}
