package com.drhelper.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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
		} catch (IOException e1) {
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
}
