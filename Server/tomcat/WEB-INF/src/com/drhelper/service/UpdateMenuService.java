package com.drhelper.service;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.com.MenuList;
import com.drhelper.db.DBManager;
import com.drhelper.entity.Menu;

public class UpdateMenuService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		MenuList respMenuList = null;
		String respBody = null;
		
		respMenuList = new MenuList();
		
		//get the meny type list
		DBManager db = new DBManager();
		ArrayList<Menu> menuList = db.getMenuList();
		if (menuList == null) {
			respBody = JSON.toJSONString(respMenuList);
			return respBody;
		}
		
		//create the resp object
		respMenuList.setList(menuList);
		respMenuList.setResult(true);
		
		//serialize the object
		respBody = JSON.toJSONString(respMenuList);
		return respBody;
	}
}
