package com.drhelper.common.db;

import java.util.ArrayList;

import com.drhelper.android.util.LogicException;
import com.drhelper.common.entity.Menu;
import com.drhelper.common.entity.MenuType;
import com.drhelper.common.entity.Option;
import com.drhelper.common.entity.Order;
import com.drhelper.common.entity.Table;
import com.drhelper.common.entity.User;

public class DBManager {
	private MysqlDB mysqldb = null;
	private MongoDB mongodb = null;
	
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
		}
		return orderNum;
	}
	
	public Order getOrderObjByOrder(int orderNum) {
		Order order = null;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderObjByOrder(): open mongodb failure");
			return order;
		}
		
		//get the empty table list
		order = mongodb.getOrderObjByOrder(orderNum);

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
	
	public int getOrderByTable(int tableNum) {
		int orderNum = 0;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderByTable(): open mongodb failure");
			return orderNum;
		}
		
		//get the order number
		orderNum = mongodb.getOrderByTable(tableNum);
		
		//release the connect to sql
		clear();
		return orderNum;
	}
	
	public int getOrderByOrder(int orderNum) {
		int newOrderNum = 0;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderByOrder(): open mongodb failure");
			return orderNum;
		}
		
		//get the order number
		newOrderNum = mongodb.getOrderByOrder(orderNum);
		
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
		}
		return result;
	}
	
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
		}
		return orderNum;
	}
	
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
		}
		return orderNum;
	}
	
	public Table getEmptyTable() {
		Table table = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getEmptyTable(): open mysqldb failure");
			return table;
		}
		
		//get the empty table
		table = mysqldb.getEmptyTable();

		//release the connect to sql
		clear();
		return table;
	}
	
	public int getFinishMenu(String user) {
		int num = 0;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getFinishMenu(): open mongodb failure");
			return num;
		}
		
		//get the finish menu
		num = mongodb.getFinishMenu(user);

		//release the connect to sql
		clear();
		return num;
	}
	
	public String getOptionString(String name) {
		String value = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOptionString(): open mysqldb failure");
			return value;
		}
		
		//get the string option record
		value = mysqldb.getOptionString(name);

		//release the connect to sql
		clear();
		return value;
	}
	
	public int getOptionInt(String name) {
		int value = 0;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOptionInt(): open mysqldb failure");
			return value;
		}
		
		//get the int option record
		value = mysqldb.getOptionInt(name);

		//release the connect to sql
		clear();
		return value;
	}
	
	public boolean updateOrderMenuFetch(int orderNum, String menu, String user) {
		boolean result = false;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.updateOrderMenuFetch(): open mongodb failure");
			return result;
		}
		
		//update the chef status into the order
		result = mongodb.updateOrderMenuFetch(orderNum, menu, user);

		//release the connect to sql
		clear();
		return result;
	}

	public boolean updateOrderMenuFinish(int orderNum, String menu) {
		boolean result = false;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.updateOrderMenuFinish(): open mongodb failure");
			return result;
		}
		
		//update the finish status into the order
		result = mongodb.updateOrderMenuFinish(orderNum, menu);

		//release the connect to sql
		clear();
		return result;
	}

	public ArrayList<Order> getOrderListNotPay() {
		ArrayList<Order> orderList = null;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderListNotPay(): open mongodb failure");
			return orderList;
		}
		
		//get the order not pay
		orderList = mongodb.getOrderListNotPay();

		//release the connect to sql
		clear();
		return orderList;
	}
	
	public boolean getOrderIsPay(int orderNum) {
		boolean result = false;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderIsPay(): open mongodb failure");
			return result;
		}
		
		//get the pay status
		result = mongodb.getOrderIsPay(orderNum);

		//release the connect to sql
		clear();
		return result;
	}

	public boolean updateAdminOrderPay(int orderNum, String user) {
		boolean result = false;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.updateAdminOrderPay(): open mongodb failure");
			return result;
		}
		
		//update the pay status into the order
		result = mongodb.updateAdminOrderPay(orderNum, user);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean deleteAdminOrderItem(int orderNum, 
			int startOrderNum, 
			int endOrderNum, 
			int tableNum) {
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.deleteAdminOrderItem(): open mongodb failure");
			return result;
		}
		
		//delete a order
		result = mongodb.deleteAdminOrderItem(orderNum, startOrderNum, endOrderNum, tableNum);

		//release the connect to sql
		clear();
		return result;
	}

	public ArrayList<Order> getOrderList(int orderNum, 
			int startOrderNum, 
			int endOrderNum, 
			int tableNum) {
		ArrayList<Order> orderList = null;
		boolean result;
		
		//create the connect to mongodb
		mongodb = new MongoDB();
		result = mongodb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOrderListByTable(): open mongodb failure");
			return orderList;
		}
		
		//get the order list
		orderList = mongodb.getOrderList(orderNum, startOrderNum, endOrderNum, tableNum);

		//release the connect to sql
		clear();
		return orderList;
	}
	
	public boolean commitAdminUserItem(int idNum, 
			String name, 
			String passwd, 
			String auth) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.commitAdminUserItem(): open mysqldb failure");
			return result;
		}
		
		//add or modify a user
		result = mysqldb.commitAdminUserItem(idNum, name, passwd, auth);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean deleteAdminUserItem(int idNum) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.deleteAdminUserItem(): open mysqldb failure");
			return result;
		}
		
		//delete a user
		result = mysqldb.deleteAdminUserItem(idNum);

		//release the connect to sql
		clear();
		return result;
	}

	public ArrayList<User> getUserList() {
		ArrayList<User> userList = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getUserList(): open mysqldb failure");
			return userList;
		}
		
		//get the user list
		userList = mysqldb.getUserList();

		//release the connect to sql
		clear();
		return userList;
	}
	
	public boolean commitAdminTableItem(int idNum, 
			int tableNum, 
			int seatNum, 
			int emptyNum) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.commitAdminTableItem(): open mysqldb failure");
			return result;
		}
		
		//add or modify a table
		result = mysqldb.commitAdminTableItem(idNum, tableNum, seatNum, emptyNum);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean deleteAdminTableItem(int idNum) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.deleteAdminTableItem(): open mysqldb failure");
			return result;
		}
		
		//delete a user
		result = mysqldb.deleteAdminTableItem(idNum);

		//release the connect to sql
		clear();
		return result;
	}

	public ArrayList<Table> getTableList() {
		ArrayList<Table> tableList = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getTableList(): open mysqldb failure");
			return tableList;
		}
		
		//get the table list
		tableList = mysqldb.getTableList();

		//release the connect to sql
		clear();
		return tableList;
	}
	
	public boolean commitAdminMenuTypeItem(int idNum, String name) {
		boolean result = false;
		
		//filter out the uncaterogies
		if (idNum == 1) {
			return result;
		}
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.commitAdminMenuTypeItem(): open mysqldb failure");
			return result;
		}
		
		//add or modify a menu type
		result = mysqldb.commitAdminMenuTypeItem(idNum, name);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean deleteAdminMenuTypeItem(int idNum) {
		boolean result = false;
		
		//filter out the uncaterogies
		if (idNum == 1) {
			return result;
		}

		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.deleteAdminMenuTypeItem(): open mysqldb failure");
			return result;
		}
		
		//delete a menu type
		result = mysqldb.deleteAdminMenuTypeItem(idNum);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean commitAdminMenuItem(int idNum, 
			String name, 
			int priceNum, 
			int typeNum) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.commitAdminMenuItem(): open mysqldb failure");
			return result;
		}
		
		//add or modify a menu
		result = mysqldb.commitAdminMenuItem(idNum, name, priceNum, typeNum);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean deleteAdminMenuItem(int idNum) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.deleteAdminMenuItem(): open mysqldb failure");
			return result;
		}
		
		//delete a menu
		result = mysqldb.deleteAdminMenuItem(idNum);

		//release the connect to sql
		clear();
		return result;
	}
	
	public boolean commitAdminOptionItem(String name, String item) {
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.commitAdminOptionItem(): open mysqldb failure");
			return result;
		}
		
		//add or modify a option
		result = mysqldb.commitAdminOptionItem(name, item);

		//release the connect to sql
		clear();
		return result;
	}
	
	public ArrayList<Option> getOptionList() {
		ArrayList<Option> optionList = null;
		boolean result;
		
		//create the connect to mysql
		mysqldb = new MysqlDB();
		result = mysqldb.openConnect();
		if (!result) {
			System.out.println("DBManager.getOptionList(): open mysqldb failure");
			return optionList;
		}
		
		//get the option list
		optionList = mysqldb.getOptionList();

		//release the connect to sql
		clear();
		return optionList;
	}
}
