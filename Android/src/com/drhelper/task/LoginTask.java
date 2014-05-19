package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.LoginActivity;
import com.drhelper.bean.Login;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Integer, Integer> {
	private static final String LOGIN_TASK_TAG = "LoginTask";

	private Activity act;
	
	public static final int LOGIN_TASK_SUCCESS = 0;
	public static final int LOGIN_TASK_LOCAL_FALIURE = 1;
	public static final int LOGIN_TASK_REMOTE_FALIURE = 2;
	
	public LoginTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((LoginActivity)act).doLoginResult(result);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		if (param.length != 2) {
			Log.e(LOGIN_TASK_TAG, "LoginTask.doInBackground(): there isn't two input param");
			return LOGIN_TASK_LOCAL_FALIURE;
		}
		
		Login loginReq = new Login();
		loginReq.setUserName(param[0]);
		loginReq.setUserPasswd(param[1]);
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(loginReq);
			
			//send the http post and recv response
			String specUrl = "login.do";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				Login loginResp = JSON.parseObject(respBody, Login.class);
				if (loginResp != null && 
						loginResp.getUserName() != null && 
						loginResp.getUserName().equals(loginReq.getUserName()) && 
						loginResp.getUserPasswd() != null && 
						loginResp.getUserPasswd().equals(loginReq.getUserPasswd())) {
					return LOGIN_TASK_SUCCESS;
				}else {
					return LOGIN_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(LOGIN_TASK_TAG, "LoginTask.doInBackground(): respBody is null");
			}
		}catch(Exception e) {
			Log.e(LOGIN_TASK_TAG, "LoginTask.doInBackground(): json serialize or http post is failure");
		}
		
		return LOGIN_TASK_LOCAL_FALIURE;
	}
}
