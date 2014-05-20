package com.drhelper.service;

import java.util.ArrayList;
import java.util.Iterator;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.EmptyTable;
import com.drhelper.db.DBManager;
import com.drhelper.entity.Table;

public class CheckTableService extends Service {
	public String doAction(String reqBody) {
		ArrayList<EmptyTable> respEmptyTableList = null;
		String respBody = null;
		
		respEmptyTableList = new ArrayList<EmptyTable>();
		
		//get the empty table list
		DBManager db = new DBManager();
		ArrayList<Table> tableList = db.getEmptyTableList();
		db.clear();
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
