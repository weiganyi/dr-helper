package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.NoticeDetail;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Table;

public class GetNoticeService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		NoticeDetail respNoticeDetail = null;
		String respBody = null;
		
		respNoticeDetail = new NoticeDetail();
		
		//get the empty table
		DBManager db = new DBManager();
		Table table = db.getEmptyTable();
		if (table == null) {
			respNoticeDetail.setEmptyTable(false);
		}else {
			respNoticeDetail.setEmptyTable(true);
		}
		
		//get the finish menu
		db = new DBManager();
		String user = (String) session.getAttribute("id");
		int num = db.getFinishMenu(user);
		if (num == 0) {
			respNoticeDetail.setFinishMenu(false);
		}else {
			respNoticeDetail.setFinishMenu(true);
			respNoticeDetail.setTable(num);
		}
		
		//serialize the object
		respBody = JSON.toJSONString(respNoticeDetail);
		return respBody;
	}
}
