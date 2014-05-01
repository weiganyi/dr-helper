package com.drhelper.task;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.CheckTableActivity;
import com.drhelper.bean.EmptyTable;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CheckTableTask extends AsyncTask<String, Integer, Integer> {
	private static final String CHECKTABLETASK_TAG = "CheckTableTask";

	private Activity act;
	
	private static final int CHECKTABLETASK_SUCCESS = 1;
	private static final int CHECKTABLETASK_FALIURE = 0;
	
	private List<EmptyTable> emptyTableList = null;
	
	public CheckTableTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		if (result == CHECKTABLETASK_SUCCESS) {
			((CheckTableActivity)act).doCheckTableResult(emptyTableList);
		}else {
			Log.e(CHECKTABLETASK_TAG, "CheckTableTask.onPostExecute(): the check table result is failure");
		}
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		try	{
			//send the http post and recv response
			String specUrl = "checkTable";
			String respBody = HttpEngine.doPost(specUrl, null);
			respBody = "[{\"tableNum\":\"1\", \"tableSeatNum\":\"1\"}, {\"tableNum\":\"2\", \"tableSeatNum\":\"2\"}, {\"tableNum\":\"3\", \"tableSeatNum\":\"3\"}]";
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				emptyTableList = JSON.parseArray(respBody, EmptyTable.class);
				if (emptyTableList.isEmpty() != true) {
					return CHECKTABLETASK_SUCCESS;
				}
			}else {
				Log.e(CHECKTABLETASK_TAG, "CheckTableTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CHECKTABLETASK_TAG, "CheckTableTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CHECKTABLETASK_FALIURE;
	}
}
