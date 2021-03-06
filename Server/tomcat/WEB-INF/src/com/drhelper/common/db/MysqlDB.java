package com.drhelper.common.db;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.drhelper.common.entity.Menu;
import com.drhelper.common.entity.MenuType;
import com.drhelper.common.entity.Option;
import com.drhelper.common.entity.Table;
import com.drhelper.common.entity.User;

public class MysqlDB implements DataBase {
	private String mysql_driver;
	private String mysql_url;
	private String mysql_username;
	private String mysql_password;
	
	private Connection conn;
	
	@Override
	public boolean openConnect() {
		//get the config
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("DBConfig.properties"));
			
			mysql_driver = prop.getProperty("mysql_driver");
			mysql_url = prop.getProperty("mysql_url");
			mysql_username = prop.getProperty("mysql_username");
			mysql_password = prop.getProperty("mysql_password");
		} catch (IOException e) {
			System.out.println("MysqlDB.openConnect(): properties catch IOException: " + e.getMessage());
			return false;
		}

		//load the jdbc driver
		try {
			Class.forName(mysql_driver);
		} catch (ClassNotFoundException e) {
			System.out.println("MysqlDB.openConnect(): load the driver catch ClassNotFoundException: " + e.getMessage());
			return false;
		}
		
		//connect to the mysql
		try {
			conn = DriverManager.getConnection(mysql_url, mysql_username, mysql_password);
		} catch (SQLException e) {
			System.out.println("MysqlDB.openConnect(): connect to the mysql catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public boolean closeConnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("MysqlDB.closeConnect(): close the connect catch SQLException: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public User getUser(String userName, String userPasswd) {
		User user = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_user where user_name=? and user_passwd=?";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//fill the param
			pstmt.setString(1, userName);
			pstmt.setString(2, userPasswd);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			if (rs.next()) {
				int user_id = rs.getInt(1);
				String user_name = rs.getString(2);
				String user_passwd = rs.getString(3);
				String user_auth = rs.getString(4);
				
				user = new User();
				user.setUser_id(user_id);
				user.setUser_name(user_name);
				user.setUser_passwd(user_passwd);
				user.setUser_auth(user_auth);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getUser(): sql query catch SQLException: " + e.getMessage());
			return user;
		}
		
		return user;
	}
	
	public ArrayList<Table> getEmptyTableList() {
		ArrayList<Table> tableList = null;;
		Table table = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_table where table_empty=1";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			tableList = new ArrayList<Table>();
			
			//get the result
			while (rs.next()) {
				int table_id = rs.getInt(1);
				int table_num = rs.getInt(2);
				int table_seat_num = rs.getInt(3);
				int table_empty = rs.getInt(4);
				
				table = new Table();
				table.setTable_id(table_id);
				table.setTable_num(table_num);
				table.setTable_seat_num(table_seat_num);
				table.setTable_empty(table_empty);
				
				tableList.add(table);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getEmptyTableList(): sql query catch SQLException: " + e.getMessage());
			return tableList;
		}
		
		return tableList;
	}
	
	public boolean updateTable(int tableNum, boolean toEmpty) {
		PreparedStatement pstmt;
		int new_table_empty;
		String sql1 = "select table_empty from dr_table where table_num=?";
		String sql2 = "update dr_table set table_empty=? where table_num=?";

		if (toEmpty) {
			new_table_empty = 1;
		}else {
			new_table_empty = 0;
		}
		
		try {
			//set autocommit mode
			conn.setAutoCommit(false);

			//prepare the statement
			pstmt = conn.prepareStatement(sql1);
			
			//fill the param
			pstmt.setInt(1, tableNum);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			if (rs.next()) {
				int old_table_empty = rs.getInt(1);
				if (old_table_empty != new_table_empty) {
					//prepare the statement
					pstmt = conn.prepareStatement(sql2);
					
					//fill the param
					pstmt.setInt(1, new_table_empty);
					pstmt.setInt(2, tableNum);
					
					//execute the update
					pstmt.executeUpdate();
				}else {
					throw new SQLException();
				}
			}else {
				throw new SQLException();
			}

		} catch (SQLException e) {
			System.out.println("MysqlDB.updateTable(): sql update catch SQLException: " + e.getMessage());
			
			try {
				//if fail, do rollback
				conn.rollback();
			} catch (SQLException e2) {
				System.out.println("MysqlDB.updateTable(): sql rollback catch SQLException: " + e2.getMessage());
			}

			return false;
		}
		
		return true;
	}
	
	public boolean rollBack() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			System.out.println("MysqlDB.rollBack(): sql rollback catch SQLException: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean commit() {
		//commit the sql operation
		try {
			conn.commit();
		} catch (SQLException e) {
			System.out.println("MysqlDB.commit(): sql commit catch SQLException: " + e.getMessage());
		}
		return true;
	}
	
	public ArrayList<MenuType> getMenuTypeList() {
		ArrayList<MenuType> menuTypeList = null;;
		MenuType menuType = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_menu_type";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			menuTypeList = new ArrayList<MenuType>();
			
			//get the result
			while (rs.next()) {
				int menu_type_id = rs.getInt(1);
				String menu_type_name = rs.getString(2);
				
				menuType = new MenuType();
				menuType.setMenu_type_id(menu_type_id);
				menuType.setMenu_type_name(menu_type_name);
				
				menuTypeList.add(menuType);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getMenuTypeList(): sql query catch SQLException: " + e.getMessage());
			return menuTypeList;
		}
		
		return menuTypeList;
	}
	
	public ArrayList<Menu> getMenuList() {
		ArrayList<Menu> menuList = null;;
		Menu menu = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_menu";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			menuList = new ArrayList<Menu>();
			
			//get the result
			while (rs.next()) {
				int menu_id = rs.getInt(1);
				String menu_name = rs.getString(2);
				int menu_price = rs.getInt(3);
				int menu_type_id = rs.getInt(4);
				
				menu = new Menu();
				menu.setMenu_id(menu_id);
				menu.setMenu_name(menu_name);
				menu.setMenu_price(menu_price);
				menu.setMenu_type_id(menu_type_id);
				
				menuList.add(menu);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getMenuList(): sql query catch SQLException: " + e.getMessage());
			return menuList;
		}
		
		return menuList;
	}
	
	public boolean changeTable(int tableNum1, int tableNum2) {
		PreparedStatement pstmt;
		ResultSet rs;
		int table_empty1 = 0;
		int table_empty2 = 0;
		String sql1 = "select table_empty from dr_table where table_num=?";
		String sql2 = "update dr_table set table_empty=? where table_num=?";
		
		try {
			//set autocommit mode
			conn.setAutoCommit(false);

			//prepare the statement
			pstmt = conn.prepareStatement(sql1);
			
			//fill the param1
			pstmt.setInt(1, tableNum1);
			
			//execute the query1
			rs = pstmt.executeQuery();
			
			//get the result1
			if (rs.next()) {
				table_empty1 = rs.getInt(1);
			}else {
				throw new SQLException();
			}

			//fill the param2
			pstmt.setInt(1, tableNum2);
			
			//execute the query2
			rs = pstmt.executeQuery();
			
			//get the result2
			if (rs.next()) {
				table_empty2 = rs.getInt(1);
			}else {
				throw new SQLException();
			}

			if (table_empty1 != 0 || table_empty2 == 0) {
				throw new SQLException();
			}
			
			// prepare the statement
			pstmt = conn.prepareStatement(sql2);

			// fill the param1
			pstmt.setInt(1, 1);
			pstmt.setInt(2, tableNum1);

			// execute the update
			pstmt.executeUpdate();

			// fill the param2
			pstmt.setInt(1, 0);
			pstmt.setInt(2, tableNum2);

			// execute the update2
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("MysqlDB.changeTable(): sql update catch SQLException: " + e.getMessage());
			
			try {
				//if fail, do rollback
				conn.rollback();
			} catch (SQLException e2) {
				System.out.println("MysqlDB.changeTable(): sql rollback catch SQLException: " + e2.getMessage());
			}

			return false;
		}
		
		return true;
	}
	
	public boolean unionTable(int tableNum1, int tableNum2) {
		CallableStatement cstmt;
		String proc = "{call union_table(?, ?)}";
		
		try {
			//prepare the procedure call
			cstmt = conn.prepareCall(proc);
			
			//fill the param1
			cstmt.setInt(1, tableNum1);
			cstmt.setInt(2, tableNum2);
			
			//execute the procedure call
			cstmt.execute();
		} catch (SQLException e) {
			System.out.println("MysqlDB.unionTable(): call procedure call catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public Table getEmptyTable() {
		Table table = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_table where table_empty=1";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			while (rs.next()) {
				int table_id = rs.getInt(1);
				int table_num = rs.getInt(2);
				int table_seat_num = rs.getInt(3);
				int table_empty = rs.getInt(4);
				
				table = new Table();
				table.setTable_id(table_id);
				table.setTable_num(table_num);
				table.setTable_seat_num(table_seat_num);
				table.setTable_empty(table_empty);
				
				return table;
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getEmptyTable(): sql query catch SQLException: " + e.getMessage());
		}

		return table;
	}
	
	public String getOptionString(String name) {
		String value = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_option where option_name=?";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//fill the param1
			pstmt.setString(1, name);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			while (rs.next()) {
				//3 is the option_value
				value = rs.getString(3);
				
				return value;
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getOptionString(): sql query catch SQLException: " + e.getMessage());
		}

		return value;
	}
	
	public int getOptionInt(String name) {
		String value = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_option where option_name=?";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//fill the param1
			pstmt.setString(1, name);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			while (rs.next()) {
				//3 is the option_value
				value = rs.getString(3);
				
				return Integer.valueOf(value);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getOptionString(): sql query catch SQLException: " + e.getMessage());
		}

		return 0;
	}
	
	public boolean commitAdminUserItem(int idNum, 
			String name, 
			String passwd, 
			String auth) {
		PreparedStatement pstmt;
		String sql1 = null;
		String sql2 = null;
		String sql3 = null;

		if (idNum != 0) {
			//this is a update
			sql1 = "update dr_user set user_name=? where user_id=?";
			sql2 = "update dr_user set user_passwd=? where user_id=?";
			sql3 = "update dr_user set user_auth=? where user_id=?";
		}else {
			//this is a insert
			sql1 = "insert into dr_user values(0, ?, ?, ?)";
		}
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			if (idNum != 0) {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);
				//fill the param
				pstmt.setString(1, name);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();

				//prepare the statement
				pstmt = conn.prepareStatement(sql2);
				//fill the param
				pstmt.setString(1, passwd);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();

				//prepare the statement
				pstmt = conn.prepareStatement(sql3);
				//fill the param
				pstmt.setString(1, auth);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();
			}else {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);

				//fill the param
				pstmt.setString(1, name);
				pstmt.setString(2, passwd);
				pstmt.setString(3, auth);

				//execute the update
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.commitAdminUserItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public boolean deleteAdminUserItem(int idNum) {
		PreparedStatement pstmt;
		String sql = "delete from dr_user where user_id=?";
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			// prepare the statement
			pstmt = conn.prepareStatement(sql);

			// fill the param
			pstmt.setInt(1, idNum);

			// execute the update
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("MysqlDB.deleteAdminUserItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<User> getUserList() {
		ArrayList<User> userList = new ArrayList<User>();
		PreparedStatement pstmt;
		String sql = "select * from dr_user";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			while (rs.next()) {
				int user_id = rs.getInt(1);
				String user_name = rs.getString(2);
				String user_passwd = rs.getString(3);
				String user_auth = rs.getString(4);
				
				User user = new User();
				user.setUser_id(user_id);
				user.setUser_name(user_name);
				user.setUser_passwd(user_passwd);
				user.setUser_auth(user_auth);
				
				userList.add(user);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getUserList(): sql query catch SQLException: " + e.getMessage());
			return userList;
		}
		
		return userList;
	}
	
	public boolean commitAdminTableItem(int idNum, 
			int tableNum, 
			int seatNum, 
			int emptyNum) {
		PreparedStatement pstmt;
		String sql1 = null;
		String sql2 = null;
		String sql3 = null;

		if (idNum != 0) {
			//this is a update
			sql1 = "update dr_table set table_num=? where table_id=?";
			sql2 = "update dr_table set table_seat_num=? where table_id=?";
			sql3 = "update dr_table set table_empty=? where table_id=?";
		}else {
			//this is a insert
			sql1 = "insert into dr_table values(0, ?, ?, ?)";
		}
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			if (idNum != 0) {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);
				//fill the param
				pstmt.setInt(1, tableNum);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();

				//prepare the statement
				pstmt = conn.prepareStatement(sql2);
				//fill the param
				pstmt.setInt(1, seatNum);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();

				//prepare the statement
				pstmt = conn.prepareStatement(sql3);
				//fill the param
				pstmt.setInt(1, emptyNum);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();
			}else {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);

				//fill the param
				pstmt.setInt(1, tableNum);
				pstmt.setInt(2, seatNum);
				pstmt.setInt(3, emptyNum);

				//execute the update
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.commitAdminTableItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public boolean deleteAdminTableItem(int idNum) {
		PreparedStatement pstmt;
		String sql = "delete from dr_table where table_id=?";
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			// prepare the statement
			pstmt = conn.prepareStatement(sql);

			// fill the param
			pstmt.setInt(1, idNum);

			// execute the update
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("MysqlDB.deleteAdminTableItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Table> getTableList() {
		ArrayList<Table> tableList = new ArrayList<Table>();
		PreparedStatement pstmt;
		String sql = "select * from dr_table";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			//get the result
			while (rs.next()) {
				int table_id = rs.getInt(1);
				int table_num = rs.getInt(2);
				int table_seat_num = rs.getInt(3);
				int table_empty = rs.getInt(4);
				
				Table table = new Table();
				table.setTable_id(table_id);
				table.setTable_num(table_num);
				table.setTable_seat_num(table_seat_num);
				table.setTable_empty(table_empty);
				
				tableList.add(table);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getTableList(): sql query catch SQLException: " + e.getMessage());
			return tableList;
		}
		
		return tableList;
	}
	
	public boolean commitAdminMenuTypeItem(int idNum, String name) {
		PreparedStatement pstmt;
		String sql1 = null;

		if (idNum != 0) {
			//this is a update
			sql1 = "update dr_menu_type set menu_type_name=? where menu_type_id=?";
		}else {
			//this is a insert
			sql1 = "insert into dr_menu_type values(0, ?)";
		}
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			if (idNum != 0) {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);

				//fill the param
				pstmt.setString(1, name);
				pstmt.setInt(2, idNum);

				//execute the update
				pstmt.executeUpdate();
			}else {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);

				//fill the param
				pstmt.setString(1, name);

				//execute the update
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.commitAdminMenuTypeItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public boolean deleteAdminMenuTypeItem(int idNum) {
		PreparedStatement pstmt;
		String sql = "delete from dr_menu_type where menu_type_id=?";
		String sql2 = "update dr_menu set menu_type_id=1 where menu_type_id=?";
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			// prepare the statement
			pstmt = conn.prepareStatement(sql);

			// fill the param
			pstmt.setInt(1, idNum);

			// execute the update
			pstmt.executeUpdate();

			//prepare the statement
			pstmt = conn.prepareStatement(sql2);

			//fill the param
			pstmt.setInt(1, idNum);

			//execute the update
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("MysqlDB.deleteAdminMenuTypeItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public boolean commitAdminMenuItem(int idNum, 
			String name, 
			int priceNum, 
			int typeNum) {
		PreparedStatement pstmt;
		String sql1 = null;
		String sql2 = null;
		String sql3 = null;

		if (idNum != 0) {
			//this is a update
			sql1 = "update dr_menu set menu_name=? where menu_id=?";
			sql2 = "update dr_menu set menu_price=? where menu_id=?";
			sql3 = "update dr_menu set menu_type_id=? where menu_id=?";
		}else {
			//this is a insert
			sql1 = "insert into dr_menu values(0, ?, ?, ?)";
		}
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			if (idNum != 0) {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);
				//fill the param
				pstmt.setString(1, name);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();

				//prepare the statement
				pstmt = conn.prepareStatement(sql2);
				//fill the param
				pstmt.setInt(1, priceNum);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();

				//prepare the statement
				pstmt = conn.prepareStatement(sql3);
				//fill the param
				pstmt.setInt(1, typeNum);
				pstmt.setInt(2, idNum);
				//execute the update
				pstmt.executeUpdate();
			}else {
				//prepare the statement
				pstmt = conn.prepareStatement(sql1);

				//fill the param
				pstmt.setString(1, name);
				pstmt.setInt(2, priceNum);
				pstmt.setInt(3, typeNum);

				//execute the update
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.commitAdminMenuItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public boolean deleteAdminMenuItem(int idNum) {
		PreparedStatement pstmt;
		String sql = "delete from dr_menu where menu_id=?";
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			// prepare the statement
			pstmt = conn.prepareStatement(sql);

			// fill the param
			pstmt.setInt(1, idNum);

			// execute the update
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("MysqlDB.deleteAdminMenuItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public boolean commitAdminOptionItem(String name, String item) {
		PreparedStatement pstmt;
		String sql1 = "update dr_option set option_value=? where option_name=?";
		String sql2 = "update dr_option set option_value=? where option_name=?";
		
		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			// prepare the statement
			pstmt = conn.prepareStatement(sql1);
			// fill the param
			pstmt.setString(1, name);
			pstmt.setString(2, "web_name");
			// execute the update
			pstmt.executeUpdate();

			// prepare the statement
			pstmt = conn.prepareStatement(sql2);
			// fill the param
			pstmt.setString(1, item);
			pstmt.setString(2, "item_per_page");
			// execute the update
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("MysqlDB.commitAdminOptionItem(): sql update catch SQLException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Option> getOptionList() {
		ArrayList<Option> optionList = null;;
		Option option = null;
		PreparedStatement pstmt;
		String sql = "select * from dr_option";

		try {
			//set autocommit mode
			conn.setAutoCommit(true);

			//prepare the statement
			pstmt = conn.prepareStatement(sql);
			
			//execute the query
			ResultSet rs = pstmt.executeQuery();
			
			optionList = new ArrayList<Option>();
			
			//get the result
			while (rs.next()) {
				int option_id = rs.getInt(1);
				String option_name = rs.getString(2);
				String option_value = rs.getString(3);
				
				option = new Option();
				option.setOption_id(option_id);
				option.setOption_name(option_name);
				option.setOption_value(option_value);
				
				optionList.add(option);
			}
		} catch (SQLException e) {
			System.out.println("MysqlDB.getOptionList(): sql query catch SQLException: " + e.getMessage());
			return optionList;
		}
		
		return optionList;
	}
}
