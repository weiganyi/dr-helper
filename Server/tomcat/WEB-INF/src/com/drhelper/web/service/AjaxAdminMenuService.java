package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Menu;
import com.drhelper.common.entity.MenuType;
import com.drhelper.web.bean.AdminMenu;
import com.drhelper.web.bean.AdminMenuObject;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxAdminMenuService implements Service<HttpSession, String, AdminMenuObject>{
	public AdminMenuObject doAction(HttpSession session, String... param) {
		AdminMenuObject resultObj = null;
		String op = null;
		String id = null;
		String name = null;
		String price = null;
		String type = null;
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
			price = param[3];
		}
		if (param[4] != null) {
			type = param[4];
		}
		if (param[5] != null) {
			page = param[5];
		}

		if (op != null) {
			if (op.equals("commit") == true) { 
				commitAdminMenuItem(session, id, name, price, type);
			}else if (op.equals("delete") == true) { 
				deleteAdminMenuItem(session, id);
			}
		}

		resultObj = getAdminMenuObject(page);
	
		return resultObj;
	}
	
	public boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxAdminMenuService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the admin
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("admin") != true) {
			System.out.println("AjaxAdminMenuService.checkAuth(): auth isn't admin");
			return false;
		}
		
		return true;
	}

	public boolean commitAdminMenuItem(HttpSession session, 
			String id, 
			String name, 
			String price, 
			String type) {
		boolean result = false;
		int idNum = 0;
		int priceNum = 0;
		int typeNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}
		//convert String to Integer
		if (price != null && price.equals("") != true) {
			priceNum = Integer.valueOf(price);
		}
		//convert String to Integer
		if (type != null && type.equals("") != true) {
			typeNum = Integer.valueOf(type);
		}

		//add or modify a menu
		DBManager db = new DBManager();
		result = db.commitAdminMenuItem(idNum, name, priceNum, typeNum);

		return result;
	}

	public boolean deleteAdminMenuItem(HttpSession session, String id) {
		boolean result = false;
		int idNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}else {
			return false;
		}

		//delete a menu
		DBManager db = new DBManager();
		result = db.deleteAdminMenuItem(idNum);

		return result;
	}

	public AdminMenuObject getAdminMenuObject(String page) {
		ArrayList<Menu> menuList = null;
		ArrayList<MenuType> menuTypeList = null;
		ArrayList<AdminMenu> adminMenuList = null;
		int itemNum = 0;
		int currPage = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}

		//get the menu list
		DBManager db = new DBManager();
		menuList = db.getMenuList();
		if (menuList == null) {
			return null;
		}
		db = new DBManager();
		menuTypeList = db.getMenuTypeList();
		if (menuTypeList == null) {
			return null;
		}
		
		//get the item_per_page
		int itemPerPage = db.getOptionInt("item_per_page");
		
		//calculate the start and end
		int start = (currPage-1)*itemPerPage;
		int end = start+itemPerPage;
		
		//construct the table2List
		adminMenuList = new ArrayList<AdminMenu>();
		int size = menuList.size();
		ListIterator<Menu> it = menuList.listIterator(size);
		while (it.hasPrevious()) {
			Menu obj = it.previous();

			if (itemNum >= start && itemNum < end) {
				AdminMenu adminMenu = new AdminMenu();

				adminMenu.setMenu_id(obj.getMenu_id());
				adminMenu.setMenu_name(obj.getMenu_name());
				adminMenu.setMenu_price(obj.getMenu_price());
				int typeId = obj.getMenu_type_id();

				ListIterator<MenuType> it2 = menuTypeList.listIterator(0);
				boolean found = false;
				MenuType obj2 = null;
				while (it2.hasNext()) {
					obj2 = it2.next();
					
					if (obj2.getMenu_type_id() == typeId) {
						found = true;
						break;
					}
				}
				
				if (found) {
					adminMenu.setMenu_type_id(typeId);
					adminMenu.setMenu_type_name(obj2.getMenu_type_name());
				}else {
					adminMenu.setMenu_type_id(menuTypeList.iterator().next().getMenu_type_id());
					adminMenu.setMenu_type_name(menuTypeList.iterator().next().getMenu_type_name());
				}
				
				adminMenuList.add(adminMenu);
			}

			itemNum++;
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		AdminMenu[] adminMenuArray = new AdminMenu[adminMenuList.size()];
		adminMenuList.toArray(adminMenuArray);

		MenuType[] menuTypeArray = new MenuType[menuTypeList.size()];
		menuTypeList.toArray(menuTypeArray);

		AdminMenuObject obj = new AdminMenuObject();
		obj.setAdminMenu(adminMenuArray);
		obj.setMenuType(menuTypeArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		
		return obj;
	}
}
