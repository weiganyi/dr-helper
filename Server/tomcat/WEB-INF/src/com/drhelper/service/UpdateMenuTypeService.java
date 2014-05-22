package com.drhelper.service;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.com.MenuTypeList;
import com.drhelper.db.DBManager;
import com.drhelper.entity.MenuType;

public class UpdateMenuTypeService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		MenuTypeList respMenuTypeList = null;
		String respBody = null;
		
		respMenuTypeList = new MenuTypeList();
		
		//get the meny type list
		DBManager db = new DBManager();
		ArrayList<MenuType> menuTypeList = db.getMenuTypeList();
		if (menuTypeList == null) {
			respMenuTypeList.setResult(false);
			respBody = JSON.toJSONString(respMenuTypeList);
			return respBody;
		}
		
		//create the resp object
		respMenuTypeList.setList(menuTypeList);
		respMenuTypeList.setResult(true);
		
		//serialize the object
		respBody = JSON.toJSONString(respMenuTypeList);
		return respBody;
	}
}
