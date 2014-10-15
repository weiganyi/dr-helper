package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.android.listener.NoticeServerListener;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Detail;
import com.drhelper.common.entity.Order;
import com.drhelper.web.bean.AdminOrderObject;
import com.drhelper.web.bean.AdminOrder;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxAdminOrderService implements Service<HttpSession, String, AdminOrderObject>{
	public AdminOrderObject doAction(HttpSession session, String... param) {
		AdminOrderObject resultObj = null;
		String op = null;
		String payOrder = null;
		String page = null;
		String order = null;
		String startOrder = null;
		String endOrder = null;
		String table = null;
		
		if (checkAuth(session) != true) {
			return resultObj;
		}
		
		//parse the params
		if (param[0] != null) {
			op = param[0];
		}
		if (param[1] != null) {
			payOrder = param[1];
		}
		if (param[2] != null) {
			page = param[2];
		}
		if (param[3] != null) {
			order = param[3];
		}
		if (param[4] != null) {
			startOrder = param[4];
		}
		if (param[5] != null) {
			endOrder = param[5];
		}
		if (param[6] != null) {
			table = param[6];
		}

		if (op != null) {
			if (op.equals("pay") == true) { 
				updateAdminOrderPay(session, payOrder);
			}else if (op.equals("delete") == true) { 
				deleteAdminOrderItem(session, order, startOrder, endOrder, table);
			}
		}

		resultObj = getAdminOrderObject(page, order, startOrder, endOrder, table);
	
		return resultObj;
	}
	
	private boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxAdminOrderService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the admin
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("admin") != true) {
			System.out.println("AjaxAdminOrderService.checkAuth(): auth isn't admin");
			return false;
		}
		
		return true;
	}

	private boolean updateAdminOrderPay(HttpSession session, String order) {
		boolean result = false;
		int orderNum = 0;
		
		//get the user
		String user = (String) session.getAttribute("id");

		//convert String to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}else {
			return result;
		}

		//update the pay status into the order
		DBManager db = new DBManager();
		result = db.updateAdminOrderPay(orderNum, user);

		//if the order is pay status, it means empty table happen
		boolean pay = db.getOrderIsPay(orderNum);
		if (pay == true) {
			//publish empty table notice
			NoticeServerListener.worker.publishEmptyTableNotice();
		}
		
		return result;
	}

	private boolean deleteAdminOrderItem(HttpSession session, 
			String order, 
			String startOrder, 
			String endOrder, 
			String table) {
		boolean result = false;
		int orderNum = 0;
		int startOrderNum = 0;
		int endOrderNum = 0;
		int tableNum = 0;

		//convert String to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}
		if (startOrder != null && startOrder.equals("") != true) {
			startOrderNum = Integer.valueOf(startOrder);
		}
		if (endOrder != null && endOrder.equals("") != true) {
			endOrderNum = Integer.valueOf(endOrder);
		}
		if (table != null && table.equals("") != true) {
			tableNum = Integer.valueOf(table);
		}

		//delete a order
		DBManager db = new DBManager();
		result = db.deleteAdminOrderItem(orderNum, startOrderNum, endOrderNum, tableNum);

		return result;
	}

	private AdminOrderObject getAdminOrderObject(String page, 
			String order,
			String startOrder,
			String endOrder,
			String table) {
		ArrayList<Order> orderList = null;
		ArrayList<AdminOrder> order2List = null;
		AdminOrder adminOrder = null;
		int itemNum = 0;
		int currPage = 0;
		int orderNum = 0;
		int startOrderNum = 0;
		int endOrderNum = 0;
		int tableNum = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}
		
		//convert String variable to Integer
		if (order != null && order.equals("") != true) {
			orderNum = Integer.valueOf(order);
		}
		if (startOrder != null && startOrder.equals("") != true) {
			startOrderNum = Integer.valueOf(startOrder);
		}
		if (endOrder != null && endOrder.equals("") != true) {
			endOrderNum = Integer.valueOf(endOrder);
		}
		if (table != null && table.equals("") != true) {
			tableNum = Integer.valueOf(table);
		}

		//get the order list
		DBManager db = new DBManager();
		orderList = db.getOrderList(orderNum, startOrderNum, endOrderNum, tableNum);
		if (orderList == null) {
			return null;
		}
		
		//get the item_per_page
		int itemPerPage = db.getOptionInt("item_per_page");
		
		//calculate the start and end
		int start = (currPage-1)*itemPerPage;
		int end = start+itemPerPage;
		
		//construct the order2List
		order2List = new ArrayList<AdminOrder>();
		int size = orderList.size();
		ListIterator<Order> it = orderList.listIterator(size);
		boolean firstDetail = true;
		boolean newOrder = false;
		while (it.hasPrevious()) {
			Order obj = it.previous();
			
			newOrder = true;

			ArrayList<Detail> detailList = obj.getDetail();
			int size2 = detailList.size();
			ListIterator<Detail> it2 = detailList.listIterator(size2);
			int detailNum = 0;
			while (it2.hasPrevious()) {
				Detail detail = it2.previous();
				
				if (itemNum >= start && itemNum < end) {
					adminOrder = new AdminOrder();
					
					if (firstDetail) {
						adminOrder.setDetailNum(size2-detailNum);
						firstDetail = false;
					}
					
					if (newOrder) {
						adminOrder.setDetailNum(size2);
						adminOrder.setOrder(obj.getOrder());
						adminOrder.setTable(obj.getTable());
						adminOrder.setWaiter(obj.getWaiter());
						adminOrder.setTime(obj.getTime());
						adminOrder.setAdmin(obj.getAdmin());
						adminOrder.setPay(obj.isPay());
						adminOrder.setNewOrder(true);
					}else {
						adminOrder.setNewOrder(false);
					}

					adminOrder.setMenu(detail.getMenu());
					adminOrder.setPrice(detail.getPrice());
					adminOrder.setAmount(detail.getAmount());
					adminOrder.setChef(detail.getChef());
					adminOrder.setFinish(detail.isFinish());
					adminOrder.setRemark(detail.getRemark());
					
					order2List.add(adminOrder);
				}

				newOrder = false;
				itemNum++;
				detailNum++;
			}
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		AdminOrder[] adminOrderArray = new AdminOrder[order2List.size()];
		order2List.toArray(adminOrderArray);

		AdminOrderObject obj = new AdminOrderObject();
		obj.setAdminOrder(adminOrderArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		obj.setOrderNum(orderNum);
		obj.setStartOrder(startOrderNum);
		obj.setEndOrder(endOrderNum);
		obj.setTableNum(tableNum);
		
		return obj;
	}
}
