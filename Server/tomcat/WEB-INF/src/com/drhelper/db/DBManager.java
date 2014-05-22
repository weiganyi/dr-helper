package com.drhelper.db;

import java.util.ArrayList;

import com.drhelper.entity.Menu;
import com.drhelper.entity.MenuType;
import com.drhelper.entity.Order;
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
			result = mysqldb.updateTable(tableNum, false);
			if (!result) {
				throw new LogicException("DBManager.createTable(): update mysqldb failure");
			}
			
			//create the connect to mongodb
			mongodb = new MongoDB();
			result = mongodb.openConnect();
			if (!result) {
				//if fail, rollback the mysql
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.createTable(): open mongodb failure");
			}
			
			//create a order
			orderNum = mongodb.createOrder(user, tableNum);
			if (orderNum == 0) {
				//if fail, rollback the mysql
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.createTable(): insert mongodb failure");
			}

			//if succ, commit the mysql
			mysqldb.commit();

		} catch (LogicException e) {
			System.out.println(e.getLog());
		} finally {		
			//release the connect to sql
			clear();
			return orderNum;
		}
	}
	
	public Order getOrder(int orderNum) {
		Order order = null;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrder(): open mongodb failure");
			return order;
		}
		
		//get the empty table list
		order = mongodb.getOrder(orderNum);

		//release the connect to sql
		clear();
		return order;
	}
	
	public ArrayList<MenuType> getMenuTypeList() {
		ArrayList<MenuType> menuTypeList = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getMenuTypeList(): open mysqldb failure");
			return menuTypeList;
		}
		
		//get the menu type list
		menuTypeList = mysqldb.getMenuTypeList();

		//release the connect to sql
		clear();
		return menuTypeList;
	}
	
	public ArrayList<Menu> getMenuList() {
		ArrayList<Menu> menuList = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getMenuList(): open mysqldb failure");
			return menuList;
		}
		
		//get the menu type list
		menuList = mysqldb.getMenuList();

		//release the connect to sql
		clear();
		return menuList;
	}
	
	public int getOrderFromTable(int tableNum) {
		int orderNum = 0;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderFromTable(): open mongodb failure");
			return orderNum;
		}
		
		//get the order number
		orderNum = mongodb.getOrderFromTable(tableNum);
		
		//release the connect to sql
		clear();
		return orderNum;
	}
	
	public int getOrderFromOrder(int orderNum) {
		int newOrderNum = 0;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderFromOrder(): open mongodb failure");
			return orderNum;
		}
		
		//get the order number
		newOrderNum = mongodb.getOrderFromOrder(orderNum);
		
		//release the connect to sql
		clear();
		return newOrderNum;
	}
	
	public boolean submitOrder(Order order) {
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.submitOrder(): open mongodb failure");
			return result;
		}
		
		//submit the order
		result = mongodb.submitOrder(order);
		
		//release the connect to sql
		clear();
		return result;
	}
	
	@SuppressWarnings("finally")
	public boolean deleteOrder(Order order) {
		boolean result = false;

		try {
			//create the connect to mysql
			mysqldb = new MysqlDB();
			result = mysqldb.openConnect();
			if (!result) {
				throw new LogicException("DBManager.deleteOrder(): open mysqldb failure");
			}
			
			//update a table to empty
			result = mysqldb.updateTable(order.getTable(), true);
			if (!result) {
				throw new LogicException("DBManager.deleteOrder(): update mysqldb failure");
			}
			
			//create the connect to mongodb
			mongodb = new MongoDB();
			result = mongodb.openConnect();
			if (!result) {
				//if fail, rollback the mysql
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.deleteOrder(): open mongodb failure");
			}
			
			//delete a order
			result = mongodb.deleteOrder(order);
			if (!result) {
				//if fail, rollback the mysql
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.deleteOrder(): delete mongodb failure");
			}

			//if succ, commit the mysql
			mysqldb.commit();

		} catch (LogicException e) {
			System.out.println(e.getLog());
		} finally {		
			//release the connect to sql
			clear();
			return result;
		}
	}
	
	@SuppressWarnings("finally")
	public int changeTable(int tableNum1, int tableNum2) {
		boolean result = false;
		int orderNum = 0;

		try {
			//create the connect to mysql
			mysqldb = new MysqlDB();
			result = mysqldb.openConnect();
			if (!result) {
				throw new LogicException("DBManager.changeTable(): open mysqldb failure");
			}
			
			//update tableNum1 to empty and update tableNum2 to non-empty
			result = mysqldb.changeTable(tableNum1, tableNum2);
			if (!result) {
				throw new LogicException("DBManager.changeTable(): update mysqldb failure");
			}
			
			//create the connect to mongodb
			mongodb = new MongoDB();
			result = mongodb.openConnect();
			if (!result) {
				//if fail, rollback the mysql
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.changeTable(): open mongodb failure");
			}
			
			//change the table
			orderNum = mongodb.changeTable(tableNum1, tableNum2);
			if (orderNum == 0) {
				//if fail, rollback the mysql
				mysqldb.rollBack();
				
				throw new LogicException("DBManager.changeTable(): change mongodb failure");
			}

			//if succ, commit the mysql
			mysqldb.commit();

		} catch (LogicException e) {
			System.out.println(e.getLog());
		} finally {		
			//release the connect to sql
			clear();
			return orderNum;
		}
	}
	
	@SuppressWarnings("finally")
	public int unionTable(int tableNum1, int tableNum2) {
		boolean result = false;
		int orderNum = 0;

		try {
			// create the connect to mysql
			mysqldb = new MysqlDB();
			result = mysqldb.openConnect();
			if (!result) {
				throw new LogicException("DBManager.unionTable(): open mysqldb failure");
			}

			// update tableNum1 to empty and update tableNum2 to non-empty
			result = mysqldb.unionTable(tableNum1, tableNum2);
			if (!result) {
				throw new LogicException("DBManager.unionTable(): update mysqldb failure");
			}

			// create the connect to mongodb
			mongodb = new MongoDB();
			result = mongodb.openConnect();
			if (!result) {
				throw new LogicException(
						"DBManager.unionTable(): open mongodb failure");
			}

			// create a order
			orderNum = mongodb.unionTable(tableNum1, tableNum2);
			if (orderNum == 0) {
				throw new LogicException("DBManager.unionTable(): change mongodb failure");
			}
		} catch (LogicException e) {
			System.out.println(e.getLog());
		} finally {
			// release the connect to sql
			clear();
			return orderNum;
		}
	}
}
