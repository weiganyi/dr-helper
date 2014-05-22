package com.drhelper.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.com.TwoTableOneOrder;
import com.drhelper.db.DBManager;

public class UnionTableService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		TwoTableOneOrder reqTable = null;
		TwoTableOneOrder respTable = null;
		String respBody = null;
		
		//parse the body
		try{
			reqTable = JSON.parseObject(reqBody, TwoTableOneOrder.class);
		}catch (Exception e) {
			System.out.println("UnionTableService.doAction(): json parse body failure");
			return respBody;
		}

		//check the input param
		int tableNum1 = reqTable.getTable1();
		int tableNum2 = reqTable.getTable2();
		if (tableNum1 == 0 || tableNum2 == 0) {
			System.out.println("UnionTableService.doAction(): tableNum is null");
			return respBody;
		}
		
		respTable = new TwoTableOneOrder();
		
		//create a order and return the number
		DBManager db = new DBManager();
		int orderNum = db.unionTable(tableNum1, tableNum2);
		if (orderNum == 0) {
			respTable.setResult(false);
			respBody = JSON.toJSONString(respTable);
			return respBody;
		}
		
		//create the resp object
		respTable.setOrder(orderNum);
		respTable.setTable1(tableNum1);
		respTable.setTable2(tableNum2);
		respTable.setResult(true);
		
		//serialize the object
		respBody = JSON.toJSONString(respTable);
		return respBody;
	}
}
