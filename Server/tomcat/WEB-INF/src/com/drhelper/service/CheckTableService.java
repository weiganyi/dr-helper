package com.drhelper.service;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.com.EmptyTable;
import com.drhelper.db.DBManager;
import com.drhelper.entity.Table;

public class CheckTableService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		ArrayList<EmptyTable> respEmptyTableList = null;
		String respBody = null;
		
		respEmptyTableList = new ArrayList<EmptyTable>();
		
		//get the empty table list
		DBManager db = new DBManager();
		ArrayList<Table> tableList = db.getEmptyTableList();
		if (tableList == null) {
			respBody = JSON.toJSONString(respEmptyTableList);
			return respBody;
		}
		
		//create the resp object
		Iterator<Table> iterator = tableList.listIterator();
		Table table = null;
		EmptyTable emptyTable = null;
		while (iterator.hasNext()) {
			table = iterator.next();
			
			emptyTable = new EmptyTable();
			emptyTable.setTableNum(table.getTable_num());
			emptyTable.setTableSeatNum(table.getTable_seat_num());
			respEmptyTableList.add(emptyTable);
		}
		
		//serialize the object
		respBody = JSON.toJSONString(respEmptyTableList);
		return respBody;
	}
}
