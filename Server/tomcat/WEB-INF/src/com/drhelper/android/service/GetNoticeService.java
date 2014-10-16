package com.drhelper.android.service;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.drhelper.android.bean.com.NoticeDetail;
import com.drhelper.android.listener.NoticeServerListener;
import com.drhelper.common.db.DBManager;
import com.drhelper.common.entity.Table;

public class GetNoticeService extends Service {
	public String doAction(HttpSession session, String reqBody) {
		NoticeDetail reqNoticeDetail = null;
		NoticeDetail respNoticeDetail = null;
		String respBody = null;
		
		//parse the body
		try{
			reqNoticeDetail = JSON.parseObject(reqBody, NoticeDetail.class);
		}catch (Exception e) {
			System.out.println("GetNoticeService.doAction(): json parse body failure: " + e.getMessage());
			return respBody;
		}

		//get the event
		String event = reqNoticeDetail.getEvent();
		
		respNoticeDetail = new NoticeDetail();
		
		if (event != null) {
			DBManager db= new DBManager();

			if (event.equals(NoticeServerListener.worker.emptyTableEvent)) {
				//get the empty table
				Table table = db.getEmptyTable();
				if (table == null) {
					respNoticeDetail.setEmptyTable(false);
				}else {
					respNoticeDetail.setEmptyTable(true);
				}
				
				respNoticeDetail.setEvent(event);
			}
			
			if (event.equals(NoticeServerListener.worker.finishMenuEvent)) {
				//get the finish menu
				String user = (String) session.getAttribute("id");
				int num = db.getFinishMenu(user);
				if (num == 0) {
					respNoticeDetail.setFinishMenu(false);
				}else {
					respNoticeDetail.setFinishMenu(true);
					respNoticeDetail.setTable(num);
				}
				
				respNoticeDetail.setEvent(event);
			}
		}
		
		//serialize the object
		respBody = JSON.toJSONString(respNoticeDetail);
		return respBody;
	}
}
