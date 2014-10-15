package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Detail;
import com.drhelper.common.entity.Order;
import com.drhelper.web.bean.FinishMenu;
import com.drhelper.web.bean.FinishMenuObject;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxFinishMenuService implements Service<HttpSession, String, FinishMenuObject>{
	public FinishMenuObject doAction(HttpSession session, String... param) {
		FinishMenuObject resultObj = null;
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
			if (op.equals("cancel") == true) { 
				updateFinishMenuCancel(session, order, menu);
			}
		}

		resultObj = getFinishMenuObject(session, page);
	
		return resultObj;
	}
	
	private boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxFinishMenuService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the chef
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("chef") != true) {
			System.out.println("AjaxFinishMenuService.checkAuth(): auth isn't chef");
			return false;
		}
		
		return true;
	}
	
	private boolean updateFinishMenuCancel(HttpSession session, String order, String menu) {
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

		return result;
	}

	private FinishMenuObject getFinishMenuObject(HttpSession session, String page) {
		ArrayList<FinishMenu> finishMenuList = null;
		int itemNum = 0;
		int currPage = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}
		
		//get the user
		String user = (String) session.getAttribute("id");

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
		
		//construct the finishMenuList
		finishMenuList = new ArrayList<FinishMenu>();
		int size = orderList.size();
		ListIterator<Order> it = orderList.listIterator(size);
		while (it.hasPrevious()) {
			Order obj = it.previous();
			
			int orderNum = obj.getOrder();
			int tableNum = obj.getTable();
			String waiter = obj.getWaiter();
			String time = obj.getTime();
			
			ArrayList<Detail> detailList = obj.getDetail();
			int size2 = detailList.size();
			ListIterator<Detail> it2 = detailList.listIterator(size2);
			while (it2.hasPrevious()) {
				Detail detail = it2.previous();
				
				if (detail.isFinish() == true && 
						detail.getChef().equals(user) == true) {
					if (itemNum >= start && itemNum < end) {
						FinishMenu finishMenu = new FinishMenu();
						
						finishMenu.setOrder(orderNum);
						finishMenu.setTable(tableNum);
						finishMenu.setWaiter(waiter);
						finishMenu.setTime(time);
						finishMenu.setMenu(detail.getMenu());
						finishMenu.setAmount(detail.getAmount());
						finishMenu.setRemark(detail.getRemark());
						
						finishMenuList.add(finishMenu);
					}
					
					itemNum++;
				}
			}
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		FinishMenu[] finishMenuArray = new FinishMenu[finishMenuList.size()];
		finishMenuList.toArray(finishMenuArray);

		FinishMenuObject obj = new FinishMenuObject();
		obj.setFinishMenu(finishMenuArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		
		return obj;
	}
}
