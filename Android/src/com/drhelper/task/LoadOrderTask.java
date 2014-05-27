package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.OrderActivity;
import com.drhelper.bean.com.OrderInfo;
import com.drhelper.entity.Order;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class LoadOrderTask extends AsyncTask<String, Integer, Integer> {
	private static final String LOAD_ORDER_TASK_TAG = "LoadOrderTask";

	private Activity act;
	private Order order;
	
	public static final int LOAD_ORDER_TASK_SUCCESS = 0;
	public static final int LOAD_ORDER_TASK_LOCAL_FALIURE = 1;
	public static final int LOAD_ORDER_TASK_REMOTE_FALIURE = 2;
	
	public LoadOrderTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((OrderActivity)act).doLoadOrderResult(result, order);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		if (param.length != 1) {
			Log.e(LOAD_ORDER_TASK_TAG, "LoadOrderTask.doInBackground(): there isn't one input param");
			return LOAD_ORDER_TASK_LOCAL_FALIURE;
		}
		
		OrderInfo orderReq = new OrderInfo();
		if (param[0] != null) {
			orderReq.setOrder(new Order());
			Order order = orderReq.getOrder();
			order.setOrder(Integer.parseInt(param[0]));
		}
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(orderReq);
			
			//send the http post and recv response
			String specUrl = "loadOrder.do";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				OrderInfo orderResp = JSON.parseObject(respBody, OrderInfo.class);
				if (orderResp != null && 
						orderResp.isResult() == true && 
								orderResp.getOrder().getOrder() == orderReq.getOrder().getOrder()) {
					//store the order object
					order = orderResp.getOrder();
					return LOAD_ORDER_TASK_SUCCESS;
				}else {
					return LOAD_ORDER_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(LOAD_ORDER_TASK_TAG, "LoadOrderTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(LOAD_ORDER_TASK_TAG, "LoadOrderTask.doInBackground(): json serialize or http post is failure");
		}
		
		return LOAD_ORDER_TASK_LOCAL_FALIURE;
	}
}
