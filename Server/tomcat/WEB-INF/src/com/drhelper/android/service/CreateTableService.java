package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.OneTableOneOrder;
import com.drhelper.common.db.DBManager;

public class CreateTableService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		OneTableOneOrder reqTable = null;
		OneTableOneOrder respTable = null;
		String respBody = null;
		
		//parse the body
		try{
			reqTable = JSON.parseObject(reqBody, OneTableOneOrder.class);
		}catch (Exception e) {
			System.out.println("CreateTableService.doAction(): json parse body failure");
			return respBody;
		}

		//check the input param
		int tableNum = reqTable.getTableNum();
		if (tableNum == 0) {
			System.out.println("CreateTableService.doAction(): tableNum is null");
			return respBody;
		}
		
		respTable = new OneTableOneOrder();
		
		//create a order and return the number
		DBManager db = new DBManager();
		String user = (String) session.getAttribute("id");
		int orderNum = db.createTable(user, tableNum);
		if (orderNum == 0) {
			respTable.setResult(false);
			respBody = JSON.toJSONString(respTable);
			return respBody;
		}
		
		//create the resp object
		respTable.setOrderNum(orderNum);
		respTable.setTableNum(tableNum);
		respTable.setResult(true);
		
		//serialize the object
		respBody = JSON.toJSONString(respTable);
		return respBody;
	}
}
