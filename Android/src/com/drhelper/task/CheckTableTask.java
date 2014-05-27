package com.drhelper.task;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.CheckTableActivity;
import com.drhelper.bean.com.EmptyTable;
import com.drhelper.bean.com.EmptyTableList;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CheckTableTask extends AsyncTask<String, Integer, Integer> {
	private static final String CHECK_TABLE_TASK_TAG = "CheckTableTask";

	private Activity act;
	private EmptyTableList emptyTableListResp = null;
	
	public static final int CHECK_TABLE_TASK_SUCCESS = 0;
	public static final int CHECK_TABLE_TASK_LOCAL_FALIURE = 1;
	public static final int CHECK_TABLE_TASK_REMOTE_FALIURE = 2;
	
	public CheckTableTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		List<EmptyTable> emptyTableList = null;
		
		if (emptyTableListResp != null) {
			emptyTableList = emptyTableListResp.getList();
		}
		
		((CheckTableActivity)act).doCheckTableResult(result, emptyTableList);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		try	{
			//send the http post and recv response
			String specUrl = "checkTable.do";
			String respBody = HttpEngine.doPost(specUrl, null);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				emptyTableListResp = JSON.parseObject(respBody, EmptyTableList.class);
				if (emptyTableListResp != null && emptyTableListResp.isResult() == true) {
					return CHECK_TABLE_TASK_SUCCESS;
				}else {
					return CHECK_TABLE_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(CHECK_TABLE_TASK_TAG, "CheckTableTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(CHECK_TABLE_TASK_TAG, "CheckTableTask.doInBackground(): json serialize or http post is failure");
		}
		
		return CHECK_TABLE_TASK_LOCAL_FALIURE;
	}
}
