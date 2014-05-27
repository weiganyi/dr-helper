package com.drhelper.task;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.drhelper.activity.UpdateActivity;
import com.drhelper.bean.com.MenuList;
import com.drhelper.bean.com.MenuTypeList;
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
	
	private MenuTypeList menuTypeListResp = null;
	private MenuList menuListResp = null;
	
	public UpdateTask(Activity act) {
		//save the Activity that call this AsyncTask
		this.act = act;
	}
	
	protected void onPreExecute() {
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Integer result) {
		List<MenuType> menuTypeList = null;
		List<Menu> menuList = null;
		
		if (menuTypeListResp != null) {
			menuTypeList = menuTypeListResp.getList();
		}
		
		if (menuListResp != null) {
			menuList = menuListResp.getList();
		}

		((UpdateActivity)act).doUpdateResult(result, menuTypeList, menuList);
	}
	
	@Override
	protected Integer doInBackground(String... param) {
		String specUrl;
		String respBody;
		
		try	{
			//send the http post and recv response
			specUrl = "updateMenuType.do";
			respBody = HttpEngine.doPost(specUrl, null);
			if (respBody != null && respBody.length() != 0) {
				//unserialize from response string
				menuTypeListResp = JSON.parseObject(respBody, MenuTypeList.class);
				if (menuTypeListResp != null && 
						menuTypeListResp.isResult() == true) {
					//send the http post and recv response
					specUrl = "updateMenu.do";
					respBody = HttpEngine.doPost(specUrl, null);
					if (respBody != null && respBody.length() != 0) {
						//unserialize from response string
						menuListResp = JSON.parseObject(respBody, MenuList.class);
						if (menuListResp != null && 
								menuListResp.isResult() == true) {
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
