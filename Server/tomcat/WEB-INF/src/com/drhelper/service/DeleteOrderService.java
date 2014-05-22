package com.drhelper.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.com.OrderInfo;
import com.drhelper.db.DBManager;
import com.drhelper.entity.Order;

public class DeleteOrderService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		OrderInfo reqOrder = null;
		OrderInfo respOrder = null;
		String respBody = null;

		//parse the body
		try{
			reqOrder = JSON.parseObject(reqBody, OrderInfo.class);
		}catch (Exception e) {
			System.out.println("DeleteOrderService.doAction(): json parse body failure");
			return respBody;
		}

		//check the input param
		Order order = reqOrder.getOrder();
		if (order == null) {
			System.out.println("DeleteOrderService.doAction(): order is null");
			return respBody;
		}
		
		respOrder = new OrderInfo();
		
		//submit the order
		DBManager db = new DBManager();
		boolean result = db.deleteOrder(order);
		
		//create the resp object
		respOrder.setResult(result);

		//serialize the object
		respBody = JSON.toJSONString(respOrder);
		return respBody;
	}
}
