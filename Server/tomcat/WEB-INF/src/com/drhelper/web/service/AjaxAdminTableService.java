package com.drhelper.web.service;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Table;
import com.drhelper.web.bean.AdminTableObject;
import com.drhelper.web.bean.PageInfo;
import com.drhelper.web.util.ServiceUtil;

public class AjaxAdminTableService implements Service<HttpSession, String, AdminTableObject>{
	public AdminTableObject doAction(HttpSession session, String... param) {
		AdminTableObject resultObj = null;
		String op = null;
		String id = null;
		String table = null;
		String seat = null;
		String empty = null;
		String page = null;
		
		if (checkAuth(session) != true) {
			return resultObj;
		}
		
		//parse the params
		if (param[0] != null) {
			op = param[0];
		}
		if (param[1] != null) {
			id = param[1];
		}
		if (param[2] != null) {
			table = param[2];
		}
		if (param[3] != null) {
			seat = param[3];
		}
		if (param[4] != null) {
			empty = param[4];
		}
		if (param[5] != null) {
			page = param[5];
		}

		if (op != null) {
			if (op.equals("commit") == true) { 
				commitAdminTableItem(session, id, table, seat, empty);
			}else if (op.equals("delete") == true) { 
				deleteAdminTableItem(session, id);
			}
		}

		resultObj = getAdminTableObject(page);
	
		return resultObj;
	}
	
	public boolean checkAuth(HttpSession session) {
		String auth;

		//if session not exist, it may be a fault
		if (session == null) {
			System.out.println("AjaxAdminTableService.checkAuth(): session isn't exist");
			return false;
		}
		
		//check the admin
		auth = (String) session.getAttribute("auth");
		if (auth == null || auth.equals("admin") != true) {
			System.out.println("AjaxAdminTableService.checkAuth(): auth isn't admin");
			return false;
		}
		
		return true;
	}

	public boolean commitAdminTableItem(HttpSession session, 
			String id, 
			String table, 
			String seat, 
			String empty) {
		boolean result = false;
		int idNum = 0;
		int tableNum = 0;
		int seatNum = 0;
		int emptyNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}
		//convert String to Integer
		if (table != null && table.equals("") != true) {
			tableNum = Integer.valueOf(table);
		}
		//convert String to Integer
		if (seat != null && seat.equals("") != true) {
			seatNum = Integer.valueOf(seat);
		}
		//convert String to Integer
		if (empty != null && empty.equals("") != true) {
			emptyNum = Integer.valueOf(empty);
		}

		//add or modify a user
		DBManager db = new DBManager();
		result = db.commitAdminTableItem(idNum, tableNum, seatNum, emptyNum);

		return result;
	}

	public boolean deleteAdminTableItem(HttpSession session, String id) {
		boolean result = false;
		int idNum = 0;

		//convert String to Integer
		if (id != null && id.equals("") != true) {
			idNum = Integer.valueOf(id);
		}else {
			return false;
		}

		//delete a table
		DBManager db = new DBManager();
		result = db.deleteAdminTableItem(idNum);

		return result;
	}

	public AdminTableObject getAdminTableObject(String page) {
		ArrayList<Table> tableList = null;
		ArrayList<Table> table2List = null;
		int itemNum = 0;
		int currPage = 0;
		
		//save the page number
		if (page != null && page.equals("") != true) {
			currPage = Integer.valueOf(page);
		}else {
			currPage = 1;
		}

		//get the table list
		DBManager db = new DBManager();
		tableList = db.getTableList();
		if (tableList == null) {
			return null;
		}
		
		//get the item_per_page
		int itemPerPage = db.getOptionInt("item_per_page");
		
		//calculate the start and end
		int start = (currPage-1)*itemPerPage;
		int end = start+itemPerPage;
		
		//construct the user2List
		table2List = new ArrayList<Table>();
		int size = tableList.size();
		ListIterator<Table> it = tableList.listIterator(size);
		while (it.hasPrevious()) {
			Table obj = it.previous();

			if (itemNum >= start && itemNum < end) {
				table2List.add(obj);
			}

			itemNum++;
		}
		
		//calculate the start page and end page
		PageInfo pgInfo = ServiceUtil.makePageInfo(currPage, itemNum, itemPerPage);

		Table[] tableArray = new Table[table2List.size()];
		table2List.toArray(tableArray);

		AdminTableObject obj = new AdminTableObject();
		obj.setTable(tableArray);
		obj.setCurrPage(currPage);
		obj.setStartPage(pgInfo.getStartPage());
		obj.setEndPage(pgInfo.getEndPage());
		obj.setTotalPage(pgInfo.getPageNum());
		
		return obj;
	}
}
