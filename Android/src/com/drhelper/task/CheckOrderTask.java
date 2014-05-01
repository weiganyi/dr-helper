package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.CheckOrderActivity;
import com.drhelper.bean.Table;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CheckOrderTask extends AsyncTask<String, Integer, Integer> {
	private static final String CHECKORDERTASK_TAG = "CheckOrderTask";

	private Activity act;
	private String orderNum;
	private String tableNum;
	
	public static final int CHECKORDERTASK_SUCCESS = 1;
	public static final int CHECKORDERTASK_FALIURE = 0;
	
	public CheckOrderTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((CheckOrderActivity)act).doCheckOrderResult(result, orderNum, tableNum);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		if (param.length != 2) {
			Log.e(CHECKORDERTASK_TAG, "CheckOrderTask.doInBackground(): there isn't two input param");
			return CHECKORDERTASK_FALIURE;
		}
		
		Table tableReq = new Table();
		if (param[0] != null) {
			tableReq.setOrderNum(param[0]);
		}else if (param[1] != null) {
			tableReq.setTableNum(param[1]);
		}
		
		tableReq.setOrderNum("1234");
		tableReq.setResult(CHECKORDERTASK_SUCCESS);
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(tableReq);
			
			//send the http post and recv response
			String specUrl = "checkOrder";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				Table tableResp = JSON.parseObject(respBody, Table.class);
				if (tableResp.getOrderNum().equals(tableReq.getOrderNum()) || 
						tableResp.getTableNum().equals(tableReq.getTableNum())) {
					int result = tableResp.getResult();
					
					//get the order num and table num from tableResp
					orderNum = tableResp.getOrderNum();
					tableNum = tableResp.getTableNum();
					
					return result;
				}
			}else {
				Log.e(CHECKORDERTASK_TAG, "CheckOrderTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CHECKORDERTASK_TAG, "CheckOrderTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CHECKORDERTASK_FALIURE;
	}
}
