package com.drhelper.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.com.OrderInfo;
import com.drhelper.db.DBManager;
import com.drhelper.entity.Order;

public class LoadOrderService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		OrderInfo reqOrder = null;
		OrderInfo respOrder = null;
		String respBody = null;

		//parse the body
		try{
			reqOrder = JSON.parseObject(reqBody, OrderInfo.class);
		}catch (Exception e) {
			System.out.println("LoadOrderService.login(): json parse body failure");
			return respBody;
		}

		//check the input param
		int orderNum = reqOrder.getOrder().getOrder();
		if (orderNum == 0) {
			System.out.println("LoadOrderService.login(): orderNum is null");
			return respBody;
		}
		
		respOrder = new OrderInfo();
		
		//fetch the order
		DBManager db = new DBManager();
		Order order = db.getOrder(orderNum);
		if (order == null) {
			respBody = JSON.toJSONString(respOrder);
			return respBody;
		}
		
		//create the resp object
		respOrder.setOrder(order);
		respOrder.setResult(true);
		
		//serialize the object
		respBody = JSON.toJSONString(respOrder);
		return respBody;
	}
}
