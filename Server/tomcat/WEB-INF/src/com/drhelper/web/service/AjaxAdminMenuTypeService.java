package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.MenuType;
import com.drhelper.web.bean.AdminMenuTypeObject;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxAdminMenuTypeService implements Service<HttpSession, String, AdminMenuTypeObject>{
	public AdminMenuTypeObject doAction(HttpSession session, String... param) {
		AdminMenuTypeObject resultObj = null;
		String op = null;
		String id = null;
		String name = null;
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
			page = param[3];
		}

		if (op != null) {
			if (op.equals("commit") == true) { 
				commitAdminMenuTypeItem(session, id, name);
			}else if (op.equals("delete") == true) { 
				deleteAdminMenuTypeItem(session, id);
			}
		}

		resultObj = getAdminMenuTypeObject(page);
	
		return resultObj;
	}
	
	public boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxAdminMenuTypeService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the admin
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("admin") != true) {
			System.out.println("AjaxAdminMenuTypeService.checkAuth(): auth isn't admin");
			return false;
		}
		
		return true;
	}

	public boolean commitAdminMenuTypeItem(HttpSession session, 
			String id, 
			String name) {
		boolean result = false;
		int idNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}

		//add or modify a menu type
		DBManager db = new DBManager();
		result = db.commitAdminMenuTypeItem(idNum, name);

		return result;
	}

	public boolean deleteAdminMenuTypeItem(HttpSession session, String id) {
		boolean result = false;
		int idNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}else {
			return false;
		}

		//delete a menu type
		DBManager db = new DBManager();
		result = db.deleteAdminMenuTypeItem(idNum);

		return result;
	}

	public AdminMenuTypeObject getAdminMenuTypeObject(String page) {
		ArrayList<MenuType> menuTypeList = null;
		ArrayList<MenuType> menuType2List = null;
		int itemNum = 0;
		int currPage = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}

		//get the menu type list
		DBManager db = new DBManager();
		menuTypeList = db.getMenuTypeList();
		if (menuTypeList == null) {
			return null;
		}
		
		//get the item_per_page
		int itemPerPage = db.getOptionInt("item_per_page");
		
		//calculate the start and end
		int start = (currPage-1)*itemPerPage;
		int end = start+itemPerPage;
		
		//construct the menuType2List
		menuType2List = new ArrayList<MenuType>();
		int size = menuTypeList.size();
		ListIterator<MenuType> it = menuTypeList.listIterator(size);
		while (it.hasPrevious()) {
			MenuType obj = it.previous();

			if (itemNum >= start && itemNum < end) {
				menuType2List.add(obj);
			}

			itemNum++;
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		MenuType[] menuTypeArray = new MenuType[menuType2List.size()];
		menuType2List.toArray(menuTypeArray);

		AdminMenuTypeObject obj = new AdminMenuTypeObject();
		obj.setMenuType(menuTypeArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		
		return obj;
	}
}
