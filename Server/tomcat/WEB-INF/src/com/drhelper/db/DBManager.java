package com.drhelper.db;

import java.util.ArrayList;

import com.drhelper.entity.User;
import com.drhelper.entity.Table;

public class DBManager {
	MysqlDB db = null;

	public User getUser(String userName, String userPasswd) {
		User user = null;
		
		//create the connect to mysql
		db = new MysqlDB();
		boolean result = db.openConnect();
		if (!result) {
			return user;
		}
		
		//check the user and passwd
		user = db.getUser(userName, userPasswd);

		return user;
	}
	
	public ArrayList<Table> getEmptyTableList() {
		ArrayList<Table> tableList = null;
		
		//create the connect to mysql
		db = new MysqlDB();
		boolean result = db.openConnect();
		if (!result) {
			return tableList;
		}
		
		//get the empty table list
		tableList = db.getEmptyTableList();

		return tableList;
	}
	
	public void clear() {
		if (db != null) {
			//close the connect
			db.closeConnect();
		}
	}
}
