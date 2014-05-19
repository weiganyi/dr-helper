package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.OrderActivity;
import com.drhelper.entity.Order;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class DeleteOrderTask extends AsyncTask<Order, Integer, Integer> {
	private static final String DELETE_ORDER_TASK_TAG = "DeleteOrderTask";

	private Activity act;

	public static final int DELETE_ORDER_TASK_SUCCESS = 0;
	public static final int DELETE_ORDER_TASK_LOCAL_FALIURE = 1;
	public static final int DELETE_ORDER_TASK_REMOTE_FALIURE = 2;
	
	public DeleteOrderTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((OrderActivity)act).doDeleteOrderResult(result);
	}
	
	@Override
	protected Integer doInBackground(Order... param) {
		// TODO Auto-generated method stub
		if (param.length != 1) {
			Log.e(DELETE_ORDER_TASK_TAG, "DeleteOrderTask.doInBackground(): there isn't one input param");
			return DELETE_ORDER_TASK_LOCAL_FALIURE;
		}
		
		Order orderReq = param[0];
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(orderReq);
			
			//send the http post and recv response
			String specUrl = "deleteOrder.do";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				Order orderResp = JSON.parseObject(respBody, Order.class);
				if (orderResp != null && 
						orderResp.getOrder() == orderReq.getOrder() && 
						orderResp.getTable() == orderReq.getTable()) {
					return DELETE_ORDER_TASK_SUCCESS;
				}else {
					return DELETE_ORDER_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(DELETE_ORDER_TASK_TAG, "DeleteOrderTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(DELETE_ORDER_TASK_TAG, "DeleteOrderTask.doInBackground(): json serialize or http post is failure");
		}
		
		return DELETE_ORDER_TASK_LOCAL_FALIURE;
	}
}
