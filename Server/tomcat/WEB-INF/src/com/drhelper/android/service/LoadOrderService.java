package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.OrderInfo;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Order;

public class LoadOrderService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		OrderInfo reqOrder = null;
		OrderInfo respOrder = null;
		String respBody = null;

		//parse the body
		try{
			reqOrder = JSON.parseObject(reqBody, OrderInfo.class);
		}catch (Exception e) {
			System.out.println("LoadOrderService.doAction(): json parse body failure");
			return respBody;
		}

		//check the input param
		int orderNum = reqOrder.getOrder().getOrder();
		if (orderNum == 0) {
			System.out.println("LoadOrderService.doAction(): orderNum is null");
			return respBody;
		}
		
		respOrder = new OrderInfo();
		
		//fetch the order
		DBManager db = new DBManager();
		Order order = db.getOrder(orderNum);
		if (order == null) {
			respOrder.setResult(false);
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
