package com.drhelper.common.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.drhelper.common.entity.Detail;
import com.drhelper.common.entity.Order;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB implements DataBase {
	private String mongodb_host;
	private String mongodb_port;
	private String mongodb_db;
	private String mongodb_username;
	private String mongodb_password;
	
	private Mongo m = null;
	private DB db = null;
	
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
	
	private DBObject findLastOne(DBCollection coll) {
		DBObject dbObj = null;
		
		DBCursor cr = coll.find();
		while(cr.hasNext()) {
			dbObj = cr.next();
		}
		
		return dbObj;
	}

	public int createOrder(String user, int table) {
		int order = 0;
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
		detail = new ArrayList<DBObject>();  

		DBObject doc = new BasicDBObject();
		doc.put("order", order);
		doc.put("table", table);
		doc.put("waiter", user);
		doc.put("time", date);
		doc.put("admin", "");
		doc.put("pay", pay);
		doc.put("detail", detail);
		
		coll.insert(doc);
		
		return order;
	}

	public Order getOrderObjByOrder(int orderNum) {
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

	public int getOrderByTable(int tableNum) {
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

	public int getOrderByOrder(int orderNum) {
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
				node.put("chef", "");
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
		query.put("waiter", user);
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

	public boolean updateOrderMenuFetch(int orderNum, String menu, String user) {
		DBObject dbObj = null;
		DBObject detail = null;
		String menu2 = null;
		String chef = null;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		if (dbObj == null) {
			return false;
		}
		
		//get detail list
		BasicDBList detailList = (BasicDBList)dbObj.get("detail");
		if (detailList == null) {
			return false;
		}
		
		//iterator the detail list to get the menu
		Iterator<Object> it = detailList.iterator();
		while (it.hasNext()) {
			detail = (DBObject)it.next();
			
			menu2 = (String)detail.get("menu");
			if (menu2.equals(menu) == true) {
				chef = (String)detail.get("chef");
				//if chef isn't exist, set it
				if (chef.equals("") == true) {
					detail.put("chef", user);
				//otherwise, clear it
				}else {
					detail.put("chef", "");
				}
			}
		}

		coll.update(query, dbObj);
		return true;
	}

	public boolean updateOrderMenuFinish(int orderNum, String menu) {
		DBObject dbObj = null;
		DBObject detail = null;
		String menu2 = null;
		Boolean finish;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		if (dbObj == null) {
			return false;
		}
		
		//get detail list
		BasicDBList detailList = (BasicDBList)dbObj.get("detail");
		if (detailList == null) {
			return false;
		}
		
		//iterator the detail list to get the menu
		Iterator<Object> it = detailList.iterator();
		while (it.hasNext()) {
			detail = (DBObject)it.next();
			
			menu2 = (String)detail.get("menu");
			if (menu2.equals(menu) == true) {
				finish = (Boolean)detail.get("finish");
				//if finish isn't set, set it
				if (finish == false) {
					detail.put("finish", true);
				//otherwise, clear it
				}else {
					detail.put("finish", false);
				}
			}
		}

		coll.update(query, dbObj);
		return true;
	}

	public ArrayList<Order> getOrderListNotPay() {
		Order order = null;
		DBObject dbObj = null;
		ArrayList<Order> orderList = null;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//get the order
		DBObject query = new BasicDBObject();
		query.put("pay", false);
		DBCursor cr = coll.find(query);
		
		if (cr.hasNext()) {
			orderList = new ArrayList<Order>();
		}else {
			return orderList;
		}
		
		//construct the order list
		while (cr.hasNext()) {
			dbObj = cr.next();

			if (dbObj != null) {
				order = JSON.parseObject(dbObj.toString(), Order.class);
				if (order != null) {
					orderList.add(order);
				}
			}
		}
		
		return orderList;
	}

	public boolean getOrderIsPay(int orderNum) {
		DBObject dbObj = null;
		Boolean pay;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		if (dbObj == null) {
			return false;
		}

		pay = (Boolean)dbObj.get("pay");
		return pay;
	}

	public boolean updateAdminOrderPay(int orderNum, String user) {
		DBObject dbObj = null;
		Boolean pay;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//check if the order is exist 
		DBObject query = new BasicDBObject();
		query.put("order", orderNum);
		dbObj = coll.findOne(query);
		if (dbObj == null) {
			return false;
		}

		pay = (Boolean)dbObj.get("pay");
		//if pay isn't set, set it
		if (pay == false) {
			dbObj.put("pay", true);
			dbObj.put("admin", user);
		//otherwise, clear it
		}else {
			dbObj.put("pay", false);
			dbObj.put("admin", "");
		}

		coll.update(query, dbObj);
		return true;
	}

	public boolean deleteAdminOrderItem(int orderNum, 
			int startOrderNum, 
			int endOrderNum, 
			int tableNum) {
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//delete the order 
		DBObject query = new BasicDBObject();
		if (orderNum != 0) {
			query.put("order", orderNum);
		}else if (startOrderNum != 0 && endOrderNum != 0) {
			query.put("order", new BasicDBObject("$gte", startOrderNum).append("$lte", endOrderNum));
		}else if (startOrderNum != 0) {
			query.put("order", new BasicDBObject("$gte", startOrderNum));
		}else if (endOrderNum != 0) {
			query.put("order", new BasicDBObject("$lte", endOrderNum));
		}else if (tableNum != 0) {
			query.put("table", tableNum);
		}else {
			return false;
		}

		coll.remove(query);
		return true;
	}

	public ArrayList<Order> getOrderList(int orderNum, 
			int startOrderNum, 
			int endOrderNum, 
			int tableNum) {
		Order order = null;
		DBObject dbObj = null;
		ArrayList<Order> orderList = null;
		
		//get the collection
		DBCollection coll = db.getCollection("dr_order");
		
		//get the order
		DBObject query = new BasicDBObject();
		if (orderNum != 0) {
			query.put("order", orderNum);
		}else if (startOrderNum != 0 && endOrderNum != 0) {
			query.put("order", new BasicDBObject("$gte", startOrderNum).append("$lte", endOrderNum));
		}else if (startOrderNum != 0) {
			query.put("order", new BasicDBObject("$gte", startOrderNum));
		}else if (endOrderNum != 0) {
			query.put("order", new BasicDBObject("$lte", endOrderNum));
		}else if (tableNum != 0) {
			query.put("table", tableNum);
		}
		DBCursor cr = coll.find(query);
		
		if (cr.hasNext()) {
			orderList = new ArrayList<Order>();
		}else {
			return orderList;
		}
		
		//construct the order list
		while (cr.hasNext()) {
			dbObj = cr.next();

			if (dbObj != null) {
				order = JSON.parseObject(dbObj.toString(), Order.class);
				if (order != null) {
					orderList.add(order);
				}
			}
		}
		
		return orderList;
	}
}
