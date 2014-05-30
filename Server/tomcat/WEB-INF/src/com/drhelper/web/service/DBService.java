package com.drhelper.web.service;

import com.drhelper.common.db.DBManager;

public class DBService{
	public String getWebName() {
		//get web name
		DBManager db = new DBManager();
		String name = db.getWebName();

		return name;
	}
}
