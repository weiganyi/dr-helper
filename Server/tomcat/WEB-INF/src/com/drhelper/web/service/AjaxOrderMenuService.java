package com.drhelper.web.service;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Detail;
import com.drhelper.common.entity.Order;
import com.drhelper.web.bean.OrderMenu;

public class AjaxOrderMenuService{
	public boolean updateOrderMenuFetch(HttpSession session, String order, String menu) {
		boolean result = false;
		int orderNum = 0;
		
		//get the chef
		String user = (String) session.getAttribute("id");
		String auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("chef") != true) {
			return result;
		}

		//convert String to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}else {
			return result;
		}

		//update the chef status into the order
		DBManager db = new DBManager();
		result = db.updateOrderMenuFetch(orderNum, menu, user);

		return result;
	}

	public boolean updateOrderMenuFinish(HttpSession session, String order, String menu) {
		boolean result = false;
		int orderNum = 0;
		
		//get the chef
		String auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("chef") != true) {
			return result;
		}

		//convert String to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}else {
			return result;
		}

		//update the finish status into the order
		DBManager db = new DBManager();
		result = db.updateOrderMenuFinish(orderNum, menu);

		return result;
	}

	public OrderMenu[] getOrderMenuArray(HttpSession session) {
		OrderMenu[] orderMenuArray = null;
		ArrayList<OrderMenu> orderMenuList = null;
		int num = 0;
		
		//get the chef
		String user = (String) session.getAttribute("id");
		String auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("chef") != true) {
			return null;
		}

		//get the order not pay
		DBManager db = new DBManager();
		ArrayList<Order> orderList = db.getOrderNotPay();
		if (orderList == null) {
			return null;
		}
		
		//get max_detail_num
		int maxNum = db.getOptionInt("max_detail_num");
		
		//construct the orderMenuList
		orderMenuList = new ArrayList<OrderMenu>();
		for (Order order : orderList) {
			int orderNum = order.getOrder();
			int tableNum = order.getTable();
			String waiter = order.getWaiter();
			String time = order.getTime();
			
			ArrayList<Detail> detailList = order.getDetail();
			for (Detail detail : detailList) {
				if (detail.isFinish() == false &&
						(detail.getChef().equals("") == true || 
						detail.getChef().equals(user) == true)) {
					OrderMenu orderMenu = new OrderMenu();
					
					orderMenu.setOrder(orderNum);
					orderMenu.setTable(tableNum);
					orderMenu.setWaiter(waiter);
					orderMenu.setTime(time);
					orderMenu.setMenu(detail.getMenu());
					orderMenu.setAmount(detail.getAmount());
					orderMenu.setRemark(detail.getRemark());
					if (detail.getChef().equals("") == true) {
						orderMenu.setFetch(false);
					}else if (detail.getChef().equals(user) == true) {
						orderMenu.setFetch(true);
						orderMenu.setChef(detail.getChef());
					}
					
					orderMenuList.add(orderMenu);
					
					num++;
					if (num >= maxNum) {
						orderMenuArray = new OrderMenu[orderMenuList.size()];
						orderMenuList.toArray(orderMenuArray);
						return orderMenuArray;
					}
				}
			}
		}
		
		orderMenuArray = new OrderMenu[orderMenuList.size()];
		orderMenuList.toArray(orderMenuArray);
		return orderMenuArray;
	}
}
