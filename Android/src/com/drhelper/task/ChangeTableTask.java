package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.ChangeTableActivity;
import com.drhelper.bean.TwoTableOneOrder;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class ChangeTableTask extends AsyncTask<String, Integer, Integer> {
	private static final String CHANGE_TABLE_TASK_TAG = "ChangeTableTask";

	private Activity act;
	private String orderNum;
	
	public static final int CHANGE_TABLE_TASK_SUCCESS = 0;
	public static final int CHANGE_TABLE_TASK_LOCAL_FALIURE = 1;
	public static final int CHANGE_TABLE_TASK_REMOTE_FALIURE = 2;
	
	public ChangeTableTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((ChangeTableActivity)act).doChangeTableResult(result, orderNum);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		if (param.length != 2) {
			Log.e(CHANGE_TABLE_TASK_TAG, "ChangeTableTask.doInBackground(): there isn't two input param");
			return CHANGE_TABLE_TASK_LOCAL_FALIURE;
		}
		
		TwoTableOneOrder tableOrderReq = new TwoTableOneOrder();
		if (param[0] != null) {
			tableOrderReq.setTable1(param[0]);
		}else if (param[1] != null) {
			tableOrderReq.setTable2(param[1]);
		}
		
		tableOrderReq.setOrderNum("2");
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(tableOrderReq);
			
			//send the http post and recv response
			String specUrl = "changeTable";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				TwoTableOneOrder tableOrderResp = JSON.parseObject(respBody, TwoTableOneOrder.class);
				if (tableOrderResp.getOrderNum().equals("") != true) {
					//get the order num from tableResp
					orderNum = tableOrderResp.getOrderNum();
					return CHANGE_TABLE_TASK_SUCCESS;
				}else {
					return CHANGE_TABLE_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(CHANGE_TABLE_TASK_TAG, "ChangeTableTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CHANGE_TABLE_TASK_TAG, "ChangeTableTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CHANGE_TABLE_TASK_LOCAL_FALIURE;
	}
}
