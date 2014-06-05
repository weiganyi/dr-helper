package com.drhelper.web.service;

import com.drhelper.common.db.DBManager;

public class IndexService{
	public String getWebName() {
		//get web_name
		DBManager db = new DBManager();
		String webName = db.getOptionString("web_name");

		return webName;
	}
}
