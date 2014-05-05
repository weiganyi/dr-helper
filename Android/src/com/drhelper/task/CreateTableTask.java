package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.CreateTableActivity;
import com.drhelper.bean.TableOrder;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CreateTableTask extends AsyncTask<String, Integer, Integer> {
	private static final String CREATE_TABLE_TASK_TAG = "CreateTableTask";

	private Activity act;
	private int orderNum;
	private int tableNum;
	
	public static final int CREATE_TABLE_TASK_SUCCESS = 0;
	public static final int CREATE_TABLE_TASK_LOCAL_FALIURE = 1;
	public static final int CREATE_TABLE_TASK_REMOTE_FALIURE = 2;
	
	public CreateTableTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((CreateTableActivity)act).doCreateTableResult(result, orderNum, tableNum);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		if (param.length != 1) {
			Log.e(CREATE_TABLE_TASK_TAG, "CreateTableTask.doInBackground(): there isn't one input param");
			return CREATE_TABLE_TASK_LOCAL_FALIURE;
		}
		
		TableOrder tableOrderReq = new TableOrder();
		tableOrderReq.setTableNum(Integer.valueOf(param[0]));
		
		tableOrderReq.setOrderNum(2);
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(tableOrderReq);
			
			//send the http post and recv response
			String specUrl = "createTable";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				TableOrder tableOrderResp = JSON.parseObject(respBody, TableOrder.class);
				if (tableOrderResp.getTableNum() == tableOrderReq.getTableNum()) {
					//get the order num and table num from tableResp
					orderNum = tableOrderResp.getOrderNum();
					tableNum = tableOrderResp.getTableNum();
						
					return CREATE_TABLE_TASK_SUCCESS;
				}else {
					return CREATE_TABLE_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(CREATE_TABLE_TASK_TAG, "CreateTableTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CREATE_TABLE_TASK_TAG, "CheckTableTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CREATE_TABLE_TASK_LOCAL_FALIURE;
	}
}
