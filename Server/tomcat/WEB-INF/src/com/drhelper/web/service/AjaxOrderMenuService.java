package com.drhelper.web.service;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.drhelper.android.listener.NoticeServerListener;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Detail;
import com.drhelper.common.entity.Order;
import com.drhelper.web.bean.OrderMenu;
import com.drhelper.web.bean.OrderMenuObject;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxOrderMenuService implements Service<HttpSession, String, OrderMenuObject> {
	public OrderMenuObject doAction(HttpSession session, String... param) {
		OrderMenuObject resultObj = null;
		String op = null;
		String order = null;
		String menu = null;
		String page = null;
		
		if (checkAuth(session) != true) {
			return resultObj;
		}
		
		//parse the params
		if (param[0] != null) {
			op = param[0];
		}
		if (param[1] != null) {
			order = param[1];
		}
		if (param[2] != null) {
			menu = param[2];
		}
		if (param[3] != null) {
			page = param[3];
		}

		if (op != null) {
			if (op.equals("fetch") == true) { 
				updateOrderMenuFetch(session, order, menu);
			}else if (op.equals("finish") == true) {
				updateOrderMenuFinish(session, order, menu);
			}
		}

		resultObj = getOrderMenuObject(session, page);
	
		return resultObj;
	}
	
	private boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxOrderMenuService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the chef
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("chef") != true) {
			System.out.println("AjaxOrderMenuService.checkAuth(): auth isn't chef");
			return false;
		}
		
		return true;
	}
	
	private boolean updateOrderMenuFetch(HttpSession session, String order, String menu) {
		boolean result = false;
		String user;
		int orderNum = 0;
		
		//convert String to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}else {
			return result;
		}

		//get the user
		user = (String) session.getAttribute("id");
		
		//update the chef status into the order
		DBManager db = new DBManager();
		result = db.updateOrderMenuFetch(orderNum, menu, user);

		return result;
	}

	private boolean updateOrderMenuFinish(HttpSession session, String order, String menu) {
		boolean result = false;
		int orderNum = 0;

		//convert String to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}else {
			return result;
		}

		//update the finish status into the order
		DBManager db = new DBManager();
		result = db.updateOrderMenuFinish(orderNum, menu);

		//publish finish menu notice
		NoticeServerListener.worker.publishFinishMenuNotice(orderNum);

		return result;
	}

	private OrderMenuObject getOrderMenuObject(HttpSession session, String page) {
		String user;
		ArrayList<OrderMenu> orderMenuList = null;
		int itemNum = 0;
		int currPage = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}

		//get the user
		user = (String) session.getAttribute("id");
		
		//get the order not pay
		DBManager db = new DBManager();
		ArrayList<Order> orderList = db.getOrderListNotPay();
		if (orderList == null) {
			return null;
		}
		
		//get the item_per_page
		int itemPerPage = db.getOptionInt("item_per_page");
		
		//calculate the start and end
		int start = (currPage-1)*itemPerPage;
		int end = start+itemPerPage;
		
		//construct the orderMenuList
		orderMenuList = new ArrayList<OrderMenu>();
		for (Order obj : orderList) {
			int orderNum = obj.getOrder();
			int tableNum = obj.getTable();
			String waiter = obj.getWaiter();
			String time = obj.getTime();
			
			ArrayList<Detail> detailList = obj.getDetail();
			for (Detail detail : detailList) {
				if (detail.isFinish() == false &&
						detail.getChef() != null && 
						(detail.getChef().equals("") == true || 
						detail.getChef().equals(user) == true)) {
					if (itemNum >= start && itemNum < end) {
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
					}
					
					itemNum++;
				}
			}
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		//construct the OrderMenuObject
		OrderMenu[] orderMenuArray = new OrderMenu[orderMenuList.size()];
		orderMenuList.toArray(orderMenuArray);

		OrderMenuObject obj = new OrderMenuObject();
		obj.setOrderMenu(orderMenuArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		
		return obj;
	}
}
