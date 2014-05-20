package com.drhelper.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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
		} catch (IOException e) {
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
	
	public DBObject findLastOne(DBCollection coll) {
		DBObject dbObj = null;
		
		DBCursor cr = coll.find();
		while(cr.hasNext()) {
			dbObj = cr.next();
		}
		
		return dbObj;
	}

	public int createOrder(String user, int table) {
		int order = 0;
		String time = null;
		boolean pay = false;
		
		//get or create the collection
		DBCollection coll = db.getCollection("dr_order");

		//get the maxnium order
		DBObject dbObj = findLastOne(coll);
		if (dbObj != null) {
			Object obj = dbObj.get("order");
			if (obj != null) {
				order = Integer.valueOf(obj.toString()) + 1;
			}else {
				return order; 
			}
		}else {
			order = 1;
		}
		
		//get the current date
		Date date = new Date();
		time = date.toString();

		DBObject doc = new BasicDBObject();
		doc.put("order", order);
		doc.put("table", table);
		doc.put("user", user);
		doc.put("time", time);
		doc.put("pay", pay);
		
		coll.insert(doc);
		
		return order;
	}
}
