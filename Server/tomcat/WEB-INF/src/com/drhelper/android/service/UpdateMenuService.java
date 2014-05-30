package com.drhelper.android.service;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.MenuList;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Menu;
import com.drhelper.common.service.Service;

public class UpdateMenuService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		MenuList respMenuList = null;
		String respBody = null;
		
		respMenuList = new MenuList();
		
		//get the meny type list
		DBManager db = new DBManager();
		ArrayList<Menu> menuList = db.getMenuList();
		if (menuList == null) {
			respMenuList.setResult(false);
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
