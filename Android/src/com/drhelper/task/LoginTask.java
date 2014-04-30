package com.drhelper.task;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.LoginActivity;
import com.drhelper.bean.Login;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Integer, Integer> {
	private Activity act;
	private static final String LOGINTASK_TAG = "LoginTask";
	
	private static final int LOGINTASK_SUCCESS = 1;
	private static final int LOGINTASK_FALIURE = 0;
	
	public LoginTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		if (result == LOGINTASK_SUCCESS) {
			((LoginActivity)act).doLoginResult();
		}
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		if (param.length != 2) {
			Log.e(LOGINTASK_TAG, "LoginTask.doInBackground(): the number of input param isn't two");
			return LOGINTASK_FALIURE;
		}
		
		Login loginReq = new Login();
		loginReq.setUserName(param[0]);
		loginReq.setUserPasswd(param[1]);

		loginReq.setResult(LOGINTASK_SUCCESS);
		
		try	{
			//serialize by fastjson
			String reqBody = JSON.toJSONString(loginReq);
			
			//send the http post and recv response
			String specUrl = "login";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody.length() != 0) {
				//unserialize from response string
				Login loginResp = JSON.parseObject(respBody, Login.class);
				if (loginResp.getUserName().equals(loginReq.getUserName()) && 
					loginResp.getUserPasswd().equals(loginReq.getUserPasswd())) {
					int result = loginResp.getResult();
					return result;
				}
			}
		}catch(Exception e) {
			Log.e(LOGINTASK_TAG, "LoginTask.doInBackground(): json serialize failure");
		}
		
		return LOGINTASK_FALIURE;
	}
}
