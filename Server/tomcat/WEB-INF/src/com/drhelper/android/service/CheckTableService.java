package com.drhelper.android.service;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.EmptyTable;
import com.drhelper.android.bean.com.EmptyTableList;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Table;

public class CheckTableService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		EmptyTableList respEmptyTableList = null;
		String respBody = null;
		
		respEmptyTableList = new EmptyTableList();
		
		//get the empty table list
		DBManager db = new DBManager();
		ArrayList<Table> tableList = db.getEmptyTableList();
		if (tableList == null) {
			respEmptyTableList.setResult(false);
			respBody = JSON.toJSONString(respEmptyTableList);
			return respBody;
		}
		
		respEmptyTableList.setList(new ArrayList<EmptyTable>());
		ArrayList<EmptyTable> list = respEmptyTableList.getList();
		
		//create the resp object
		Iterator<Table> iterator = tableList.listIterator();
		Table table = null;
		EmptyTable emptyTable = null;
		while (iterator.hasNext()) {
			table = iterator.next();
			
			emptyTable = new EmptyTable();
			emptyTable.setTableNum(table.getTable_num());
			emptyTable.setTableSeatNum(table.getTable_seat_num());
			list.add(emptyTable);
		}
		respEmptyTableList.setResult(true);
		
		//test notice push
		/*
		if (NoticeServerListener.worker != null) {
			NoticeServerListener.worker.publishEvent(
					NoticeServer.emptyTableEvent, 
					(String)session.getAttribute("id"));
		}
		*/
		
		//serialize the object
		respBody = JSON.toJSONString(respEmptyTableList);
		return respBody;
	}
}
