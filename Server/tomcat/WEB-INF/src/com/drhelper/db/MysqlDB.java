package com.drhelper.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.drhelper.entity.Menu;
import com.drhelper.entity.MenuType;
import com.drhelper.entity.Table;
import com.drhelper.entity.User;

public class MysqlDB implements DataBase {
	String mysql_driver;
	String mysql_url;
	String mysql_username;
	String mysql_password;
	
	Connection conn;
	
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
			System.out.println("MysqlDB.openConnect(): properties catch IOException");
			return false;
		}

		//load the jdbc driver
		try {
			Class.forName(mysql_driver);
		} catch (ClassNotFoundException e) {
			System.out.println("MysqlDB.openConnect(): load the driver catch ClassNotFoundException");
			return false;
		}
		
		//connect to the mysql
		try {
			conn = DriverManager.getConnection(mysql_url, mysql_username, mysql_password);
		} catch (SQLException e) {
			System.out.println("MysqlDB.openConnect(): connect to the mysql catch SQLException");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean closeConnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("MysqlDB.closeConnect(): close the connect catch SQLException");
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
			System.out.println("MysqlDB.getUser(): sql query catch SQLException");
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
			System.out.println("MysqlDB.getEmptyTable(): sql query catch SQLException");
			return tableList;
		}
		
		return tableList;
	}
	
	public boolean updateTable(int tableNum) {
		PreparedStatement pstmt;
		String sql1 = "select table_empty from dr_table where table_num=?";
		String sql2 = "update dr_table set table_empty=0 where table_num=?";

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
				int table_empty = rs.getInt(1);
				if (table_empty == 1) {
					//prepare the statement
					pstmt = conn.prepareStatement(sql2);
					
					//fill the param
					pstmt.setInt(1, tableNum);
					
					//execute the query
					pstmt.executeUpdate();
				}else {
					throw new SQLException();
				}
			}else {
				throw new SQLException();
			}

		} catch (SQLException e) {
			System.out.println("MysqlDB.createTable(): sql update catch SQLException");
			
			try {
				//if fail, do rollback
				conn.rollback();
			} catch (SQLException e2) {
				System.out.println("MysqlDB.createTable(): sql rollback catch SQLException");
			}

			return false;
		}
		
		return true;
	}
	
	public boolean rollBack() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			System.out.println("MysqlDB.rollBack(): sql rollback catch SQLException");
			return false;
		}
		return true;
	}
	
	public boolean commit() {
		//commit the sql operation
		try {
			conn.commit();
		} catch (SQLException e) {
			System.out.println("MysqlDB.commit(): sql commit catch SQLException");
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
			System.out.println("MysqlDB.getMenuTypeList(): sql query catch SQLException");
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
			System.out.println("MysqlDB.getMenuList(): sql query catch SQLException");
			return menuList;
		}
		
		return menuList;
	}
}
