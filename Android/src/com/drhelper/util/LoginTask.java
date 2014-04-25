package com.drhelper.util;

import com.drhelper.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

public class LoginTask extends AsyncTask<String, Integer, Integer> 
{
	Activity ui_act;
	
	public LoginTask(Activity act)
	{
		ui_act = act;
	}
	
	protected void onPreExecute()
	{
		return;
	}
	
	protected void onProgressUpdate(Integer... progress)
	{
		return;
	}
	
	protected void onPostExecute(Integer result)
	{
		Intent intent = new Intent(ui_act, MainActivity.class);
		ui_act.startActivity(intent);
		return;
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
