package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Detail;
import com.drhelper.common.entity.Order;
import com.drhelper.web.bean.FinishMenu;

public class AjaxFinishMenuService{
	public boolean updateFinishMenuCancel(HttpSession session, String order, String menu) {
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

	public FinishMenu[] getFinishMenuArray(HttpSession session) {
		FinishMenu[] finishMenuArray = null;
		ArrayList<FinishMenu> finishMenuList = null;
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
		
		//construct the finishMenuList
		finishMenuList = new ArrayList<FinishMenu>();
		int size = orderList.size();
		ListIterator<Order> it = orderList.listIterator(size);
		while (it.hasPrevious()) {
			Order order = it.previous();
			
			int orderNum = order.getOrder();
			int tableNum = order.getTable();
			String waiter = order.getWaiter();
			String time = order.getTime();
			
			ArrayList<Detail> detailList = order.getDetail();
			int size2 = detailList.size();
			ListIterator<Detail> it2 = detailList.listIterator(size2);
			while (it2.hasPrevious()) {
				Detail detail = it2.previous();
				
				if (detail.isFinish() == true && 
						detail.getChef().equals(user) == true) {
					FinishMenu finishMenu = new FinishMenu();
					
					finishMenu.setOrder(orderNum);
					finishMenu.setTable(tableNum);
					finishMenu.setWaiter(waiter);
					finishMenu.setTime(time);
					finishMenu.setMenu(detail.getMenu());
					finishMenu.setAmount(detail.getAmount());
					finishMenu.setRemark(detail.getRemark());
					
					finishMenuList.add(finishMenu);
					
					num++;
					if (num >= maxNum) {
						finishMenuArray = new FinishMenu[finishMenuList.size()];
						finishMenuList.toArray(finishMenuArray);
						return finishMenuArray;
					}
				}
			}
		}
		
		finishMenuArray = new FinishMenu[finishMenuList.size()];
		finishMenuList.toArray(finishMenuArray);
		return finishMenuArray;
	}
}
