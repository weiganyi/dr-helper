package com.drhelper.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB implements DataBase {
	String mongodb_host;
	String mongodb_port;
	String mongodb_db;
	String mongodb_username;
	String mongodb_password;
	
	Mongo m = null;
	DB db = null;
	
	@Override
	public boolean openConnect() {
		//get the config
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("DBConfig.properties"));
			
			mongodb_host = prop.getProperty("mongodb_host");
			mongodb_port = prop.getProperty("mongodb_port");
			mongodb_db = prop.getProperty("mongodb_db");
			mongodb_username = prop.getProperty("mongodb_username");
			mongodb_password = prop.getProperty("mongodb_password");
		} catch (IOException e1) {
			System.out.println("MongoDB.openConnect(): properties catch IOException");
			return false;
		}
		
		//create the monogo client
		try {
			m = new Mongo(mongodb_host, Integer.valueOf(mongodb_port));
		} catch (UnknownHostException e) {
			System.out.println("MongoDB.openConnect(): create the mongo catch UnknownHostException");
			return false;
		} catch (MongoException e) {
			System.out.println("MongoDB.openConnect(): create the mongo catch MongoException");
			return false;
		}
		
		//create the connnect
		db = m.getDB(mongodb_db);
		
		//do the auth
		boolean auth = db.authenticate(mongodb_username, mongodb_password.toCharArray());
		if (auth) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean closeConnect() {
		// TODO Auto-generated method stub
		if (m != null) {
			m.close();
		}
		return true;
	}

}
