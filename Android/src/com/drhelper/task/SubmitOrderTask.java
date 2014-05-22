package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.OrderActivity;
import com.drhelper.bean.com.OrderInfo;
import com.drhelper.entity.Order;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class SubmitOrderTask extends AsyncTask<Order, Integer, Integer> {
	private static final String SUBMIT_ORDER_TASK_TAG = "SubmitOrderTask";

	private Activity act;

	public static final int SUBMIT_ORDER_TASK_SUCCESS = 0;
	public static final int SUBMIT_ORDER_TASK_LOCAL_FALIURE = 1;
	public static final int SUBMIT_ORDER_TASK_REMOTE_FALIURE = 2;
	
	public SubmitOrderTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((OrderActivity)act).doSubmitOrderResult(result);
	}
	
	@Override
	protected Integer doInBackground(Order... param) {
		// TODO Auto-generated method stub
		if (param.length != 1) {
			Log.e(SUBMIT_ORDER_TASK_TAG, "SubmitOrderTask.doInBackground(): there isn't one input param");
			return SUBMIT_ORDER_TASK_LOCAL_FALIURE;
		}
		
		OrderInfo orderReq = new OrderInfo();
		if (param[0] != null) {
			orderReq.setOrder(param[0]);
		}
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(orderReq);
			
			//send the http post and recv response
			String specUrl = "submitOrder.do";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				OrderInfo orderResp = JSON.parseObject(respBody, OrderInfo.class);
				if (orderResp != null && orderResp.isResult() == true) {
					return SUBMIT_ORDER_TASK_SUCCESS;
				}else {
					return SUBMIT_ORDER_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(SUBMIT_ORDER_TASK_TAG, "SubmitOrderTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(SUBMIT_ORDER_TASK_TAG, "SubmitOrderTask.doInBackground(): json serialize or http post is failure");
		}
		
		return SUBMIT_ORDER_TASK_LOCAL_FALIURE;
	}
}
