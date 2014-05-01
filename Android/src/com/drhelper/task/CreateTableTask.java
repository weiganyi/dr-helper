package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.CreateTableActivity;
import com.drhelper.bean.Table;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CreateTableTask extends AsyncTask<String, Integer, Integer> {
	private static final String CREATETABLETASK_TAG = "CreateTableTask";

	private Activity act;
	private String orderNum;
	private String tableNum;
	
	public static final int CREATETABLETASK_SUCCESS = 1;
	public static final int CREATETABLETASK_FALIURE = 0;
	
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
			Log.e(CREATETABLETASK_TAG, "CreateTableTask.doInBackground(): there isn't one input param");
			return CREATETABLETASK_FALIURE;
		}
		
		Table tableReq = new Table();
		tableReq.setTableNum(param[0]);
		
		tableReq.setOrderNum("1234");
		tableReq.setResult(CREATETABLETASK_SUCCESS);
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(tableReq);
			
			//send the http post and recv response
			String specUrl = "createTable";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				Table tableResp = JSON.parseObject(respBody, Table.class);
				if (tableResp.getTableNum().equals(tableReq.getTableNum())) {
					int result = tableResp.getResult();
					
					//get the order num and table num from tableResp
					orderNum = tableResp.getOrderNum();
					tableNum = tableResp.getTableNum();
					
					return result;
				}
			}else {
				Log.e(CREATETABLETASK_TAG, "CreateTableTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CREATETABLETASK_TAG, "CheckTableTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CREATETABLETASK_FALIURE;
	}
}