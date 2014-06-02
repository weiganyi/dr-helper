package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.OneTableOneOrder;
import com.drhelper.android.util.LogicException;
import com.drhelper.common.db.DBManager;

public class CheckOrderService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		OneTableOneOrder reqOrder = null;
		OneTableOneOrder respOrder = null;
		String respBody = null;
		
		//parse the body
		try{
			reqOrder = JSON.parseObject(reqBody, OneTableOneOrder.class);
		}catch (Exception e) {
			System.out.println("CheckOrderService.doAction(): json parse body failure");
			return respBody;
		}

		//check the input param
		int tableNum = reqOrder.getTableNum();
		int orderNum = reqOrder.getOrderNum();
		if (tableNum == 0 && orderNum == 0) {
			System.out.println("CheckOrderService.doAction(): tableNum and orderNum is null");
			return respBody;
		}
		
		respOrder = new OneTableOneOrder();
		
		DBManager db = new DBManager();
		try {
			//check if order is exist
			if (tableNum != 0) {
				orderNum = db.getOrderFromTable(tableNum);
				if (orderNum == 0) {
					throw new LogicException();
				}
			}else if (orderNum != 0) {
				orderNum = db.getOrderFromOrder(orderNum);
				if (orderNum == 0) {
					throw new LogicException();
				}
			}

			respOrder.setResult(true);
		}catch (LogicException e) {
			respOrder.setResult(false);
		}finally {
			//create the resp object
			respOrder.setOrderNum(orderNum);
			respOrder.setTableNum(tableNum);
		}
		
		//serialize the object
		respBody = JSON.toJSONString(respOrder);
		return respBody;
	}
}
