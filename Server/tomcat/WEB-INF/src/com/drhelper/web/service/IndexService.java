package com.drhelper.web.service;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.web.bean.IndexObject;

public class IndexService implements Service<HttpSession, String, IndexObject>{
	public IndexObject doAction(HttpSession session, String... param) {
		IndexObject resultObj = new IndexObject();
		String webName = null;

		webName = getWebName();
		
		resultObj.setWebName(webName);
		return resultObj;
	}

	public String getWebName() {
		//get web_name
		DBManager db = new DBManager();
		String webName = db.getOptionString("web_name");

		return webName;
	}
}
