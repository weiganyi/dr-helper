package com.drhelper.db;

import java.util.ArrayList;

import com.drhelper.entity.User;
import com.drhelper.entity.Table;
import com.drhelper.util.LogicException;

public class DBManager {
	MysqlDB mysqldb = null;
	MongoDB mongodb = null;
	
	public void clear() {
		if (mysqldb != null) {
			//close the connect
			mysqldb.closeConnect();
		}

		if (mongodb != null) {
			//close the connect
			mongodb.closeConnect();
		}
	}

	public User getUser(String userName, String userPasswd) {
		User user = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getUser(): open mysqldb failure");
			return user;
		}
		
		//check the user and passwd
		user = mysqldb.getUser(userName, userPasswd);

		//release the connect to sql
		clear();
		return user;
	}
	
	public ArrayList<Table> getEmptyTableList() {
		ArrayList<Table> tableList = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getEmptyTableList(): open mysqldb failure");
			return tableList;
		}
		
		//get the empty table list
		tableList = mysqldb.getEmptyTableList();

		//release the connect to sql
		clear();
		return tableList;
	}

	@SuppressWarnings("finally")
	public int createTable(String user, int tableNum) {
		int orderNum = 0;
		boolean result = true;
		
		try {
			//create the connect to mysql
			mysqldb = new MysqlDB();
			result = mysqldb.openConnect();
			if (!result) {
				throw new LogicException("DBManager.createTable(): open mysqldb failure");
			}
			
			//update a table to non-empty
			result = mysqldb.updateTable(tableNum);
			if (!result) {
				throw new LogicException("DBManager.createTable(): update mysqldb failure");
			}
			
			//create the connect to mysql
			mongodb = new MongoDB();
			result = mongodb.openConnect();
			if (!result) {
				//if fail, do rollback
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.createTable(): open mongodb failure");
			}
			
			//create a order
			orderNum = mongodb.createOrder(user, tableNum);
			if (orderNum == 0) {
				//if fail, do rollback
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.createTable(): insert mongodb failure");
			}

			//if succ, do commit
			mysqldb.commit();

		} catch (LogicException e) {
			System.out.println(e.getLog());
		} finally {		
			//release the connect to sql
			clear();
			return orderNum;
		}
	}
}
