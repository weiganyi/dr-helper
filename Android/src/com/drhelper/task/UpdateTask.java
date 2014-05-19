package com.drhelper.task;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.UpdateActivity;
import com.drhelper.entity.Menu;
import com.drhelper.entity.MenuType;
import com.drhelper.util.HttpEngine;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateTask extends AsyncTask<String, Integer, Integer> {
	private static final String UPDATE_TASK_TAG = "UpdateTask";

	private Activity act;

	public static final int UPDATE_TASK_SUCCESS = 0;
	public static final int UPDATE_TASK_LOCAL_FALIURE = 1;
	public static final int UPDATE_TASK_REMOTE_FALIURE = 2;
	
	private List<MenuType> menuTypeListResp = null;
	private List<Menu> menuListResp = null;
	
	public UpdateTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		((UpdateActivity)act).doUpdateResult(result, menuTypeListResp, menuListResp);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		// TODO Auto-generated method stub
		String specUrl;
		String respBody;
		
		try	{
			//send the http post and recv response
			specUrl = "updateMenuType.do";
			respBody = HttpEngine.doPost(specUrl, null);
			respBody = "[{\"menu_type_id\":0, \"menu_type_name\":\"÷˜ ≥\"}, {\"menu_type_id\":1, \"menu_type_name\":\"≥¥≤À\"}, {\"menu_type_id\":2, \"menu_type_name\":\"Ãµ„\"}]";
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				menuTypeListResp = JSON.parseArray(respBody, MenuType.class);
				if (menuTypeListResp.isEmpty() != true) {

					//send the http post and recv response
					specUrl = "updateMenu";
					respBody = HttpEngine.doPost(specUrl, null);
					respBody = "[{\"menu_id\":0, \"menu_name\":\"œ„«€œ„∏…»‚Àø\", \"menu_price\":12, \"menu_type_id\":1}, {\"menu_id\":1, \"menu_name\":\"π¨±£º¶∂°\", \"menu_price\":15, \"menu_type_id\":1}, {\"menu_id\":2, \"menu_name\":\"¬¯Õ∑\", \"menu_price\":5, \"menu_type_id\":0}, {\"menu_id\":3, \"menu_name\":\"∆§µ∞ ›»‚÷‡\", \"menu_price\":8, \"menu_type_id\":0}, {\"menu_id\":4, \"menu_name\":\"À´∆§ƒÃ\", \"menu_price\":6, \"menu_type_id\":2}]";
					if (respBody != null && respBody.length() != 0) {
						//unserialize from response string
						menuListResp = JSON.parseArray(respBody, Menu.class);
						if (menuListResp != null && 
								menuListResp.isEmpty() != true) {
							return UPDATE_TASK_SUCCESS;
						}else {
							return UPDATE_TASK_REMOTE_FALIURE;
						}
					}else {
						Log.e(UPDATE_TASK_TAG, "UpdateTask.doInBackground(): menu respBody is null");
					}
				}else {
					return UPDATE_TASK_REMOTE_FALIURE;
				}
			}else {
				Log.e(UPDATE_TASK_TAG, "UpdateTask.doInBackground(): menu type respBody is null");
			}
		}catch(Exception e) {
			Log.e(UPDATE_TASK_TAG, "UpdateTask.doInBackground(): json serialize or http post is failure");
		}
		
		return UPDATE_TASK_LOCAL_FALIURE;
	}
}
