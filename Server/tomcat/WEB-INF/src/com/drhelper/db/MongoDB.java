package com.drhelper.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.drhelper.entity.Detail;
import com.drhelper.entity.Order;
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
		ArrayList<DBObject> detail = null;  
		
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
		detail = new ArrayList<DBObject>();  

		DBObject doc = new BasicDBObject();
		doc.put("order", order);
		doc.put("table", table);
		doc.put("user", user);
		doc.put("time", time);
		doc.put("pay", pay);
		doc.put("detail", detail);
		
		coll.insert(doc);
		
		return order;
	}

	public Order getOrder(int orderNum) {
		Order order = null;
		DBObject dbObj = null;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//get the order
		DBObject query = new BasicDBObject();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		
		//serialize the obj to Order.class
		if (dbObj != null) {
			order = JSON.parseObject(dbObj.toString(), Order.class);
		}
		
		return order;
	}

	public int getOrderFromTable(int tableNum) {
		Order order = null;
		DBObject dbObj = null;
		int orderNum = 0;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//get the order
		DBObject query = new BasicDBObject();
		query.put("table", tableNum);
		dbObj = coll.findOne(query);
		
		//serialize the obj to Order.class
		if (dbObj != null) {
			order = JSON.parseObject(dbObj.toString(), Order.class);
		}
		
		if (order != null) {
			orderNum = order.getOrder();
		}
		
		return orderNum;
	}

	public int getOrderFromOrder(int orderNum) {
		Order order = null;
		DBObject dbObj = null;
		int newOrderNum = 0;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");

		//get the order
		DBObject query = new BasicDBObject();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		
		//serialize the obj to Order.class
		if (dbObj != null) {
			order = JSON.parseObject(dbObj.toString(), Order.class);
		}
		
		if (order != null) {
			newOrderNum = order.getOrder();
		}
		
		return newOrderNum;
	}

	public boolean submitOrder(Order order) {
		DBObject dbObj = null;
		boolean result = false;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		int orderNum = order.getOrder();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		
		if (dbObj != null) {
			//if doc exist, update it
			ArrayList<DBObject> detail = new ArrayList<DBObject>();
			
			Iterator<Detail> it = order.getDetail().iterator();
			DBObject node;
			Detail detailItem;
			while (it.hasNext()) {
				detailItem = it.next();
				
				node = new BasicDBObject();
				node.put("menu", detailItem.getMenu());
				node.put("price", detailItem.getPrice());
				node.put("amount", detailItem.getAmount());
				node.put("finish", detailItem.isFinish());
				node.put("remark", detailItem.getRemark());
				detail.add(node);
			}
			
			dbObj.put("detail", detail);
			coll.update(query, dbObj);
			
			result = true;
		}else {
			//if doc not exist, there is a fault
			result = false;
		}
		
		return result;
	}

	public boolean deleteOrder(Order order) {
		DBObject dbObj = null;
		boolean result = false;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		int orderNum = order.getOrder();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		
		if (dbObj != null) {
			//if doc exist, delete it
			coll.remove(query);
			
			result = true;
		}else {
			//if doc not exist, there is a fault
			result = false;
		}
		
		return result;
	}

	public int changeTable(int tableNum1, int tableNum2) {
		DBObject dbObj = null;
		Order order = null;
		int orderNum = 0;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		query.put("table", tableNum1);
		dbObj = coll.findOne(query);
		
		if (dbObj != null) {
			order = JSON.parseObject(dbObj.toString(), Order.class);
			if (order != null) {
				orderNum = order.getOrder();
			}
			
			//if doc exist, update it
			dbObj.put("table", tableNum2);
			coll.update(query, dbObj);
		}
		
		return orderNum;
	}

	public int unionTable(int tableNum1, int tableNum2) {
		int orderNum = 0;
		String proc = "union_table(" + String.valueOf(tableNum1) + ", " + String.valueOf(tableNum2) + ")";
		
		//call the prepare_call
		BasicDBObject obj = db.doEval(proc);
		
		if (obj != null) {
			String orderStr = String.valueOf(obj.toMap().get("retval"));
			orderNum = Double.valueOf(orderStr).intValue();
		}
		
		return orderNum;
	}

	public int getFinishMenu(String user) {
		Order order = null;
		DBObject dbObj = null;
		ArrayList<Detail> detailList = null;
		int num = 0;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//get the order
		DBObject query = new BasicDBObject();
		query.put("user", user);
		DBCursor cr = coll.find(query);
		if (cr.hasNext()) {
			dbObj = cr.next();

			//find the finish menu
			if (dbObj != null) {
				order = JSON.parseObject(dbObj.toString(), Order.class);
				if (order != null) {
					detailList = order.getDetail();
					for (Detail detail : detailList) {
						if (detail.isFinish() == true) {
							num = order.getTable();
							return num;
						}
					}
				}
			}
		}
		
		return num;
	}
}
