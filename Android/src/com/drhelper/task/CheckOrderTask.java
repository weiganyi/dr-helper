package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.CheckOrderActivity;
import com.drhelper.bean.com.OneTableOneOrder;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CheckOrderTask extends AsyncTask<String, Integer, Integer> {
	private static final String CHECK_ORDER_TASK_TAG = "CheckOrderTask";

	private Activity act;
	private int orderNum;
	
	public static final int CHECK_ORDER_TASK_SUCCESS = 0;
	public static final int CHECK_ORDER_TASK_LOCAL_FALIURE = 1;
	public static final int CHECK_ORDER_TASK_REMOTE_FALIURE = 2;
	
	public CheckOrderTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((CheckOrderActivity)act).doCheckOrderResult(result, orderNum);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		if (param.length != 2) {
			Log.e(CHECK_ORDER_TASK_TAG, "CheckOrderTask.doInBackground(): there isn't two input param");
			return CHECK_ORDER_TASK_LOCAL_FALIURE;
		}
		
		OneTableOneOrder tableOrderReq = new OneTableOneOrder();
		if (param[0] != null) {
			tableOrderReq.setOrderNum(Integer.valueOf(param[0]));
		}else if (param[1] != null) {
			tableOrderReq.setTableNum(Integer.valueOf(param[1]));
		}
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(tableOrderReq);
			
			//send the http post and recv response
			String specUrl = "checkOrder.do";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				OneTableOneOrder tableOrderResp = JSON.parseObject(respBody, OneTableOneOrder.class);
				if (tableOrderResp != null && 
						tableOrderResp.isResult() != false && 
						(tableOrderResp.getOrderNum() == tableOrderReq.getOrderNum() || 
						tableOrderResp.getTableNum() == tableOrderReq.getTableNum())) {
					//get the order num from tableResp
					orderNum = tableOrderResp.getOrderNum();
					return CHECK_ORDER_TASK_SUCCESS;
				}else {
					return CHECK_ORDER_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(CHECK_ORDER_TASK_TAG, "CheckOrderTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CHECK_ORDER_TASK_TAG, "CheckOrderTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CHECK_ORDER_TASK_LOCAL_FALIURE;
	}
}
