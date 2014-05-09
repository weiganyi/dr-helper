package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.service.NoticeService;
import com.drhelper.util.DialogBox;
import com.drhelper.util.PrefsManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("WorldWriteableFiles")
public class BeforeLoginActivity extends Activity {
	private static final String BEFORE_LOGIN_ACTIVITY_TAG = "BeforeLoginActivity";

	private ExitReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//register the exit receiver
		receiver = new ExitReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.drhelper.activity.intent.action.EXIT");
		registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		//unregister the exit receiver
		unregisterReceiver(receiver);
		
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.exit_menu) {
			doLogout();
		}else if (item.getItemId() == R.id.prefs_menu) {
			launchPrefsActivity();
		}
		return true;
	}
	
	private void doLogout() {
		DialogBox.showConfirmDialog(BeforeLoginActivity.this, 
				this.getString(R.string.before_login_activity_want_to_logout), "doLogoutResult");
	}
	
	public void doLogoutResult() {
		//get the prefs manager
		SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_WRITEABLE);
		if (prefs != null) {
			//clear the user
			Editor editor = prefs.edit();
			editor.putString("user_name", "");
			editor.commit();
		}else {
			Log.e(BEFORE_LOGIN_ACTIVITY_TAG, "BeforeLoginActivity.doLogoutResult(): login_user prefs isn't exist");
		}

		//send the service exit broadcast
		if (PrefsManager.isNotice_service_start() == true) {
			String action = "com.drhelper.service.intent.action.EXIT";
			Intent intent = new Intent(action);
			sendBroadcast(intent);
			PrefsManager.setNotice_service_start(false);
		}

		//send the activity exit broadcast
		String action = "com.drhelper.activity.intent.action.EXIT";
		Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	private void launchPrefsActivity() {
		//launch to PrefsActivity
		Intent intent = new Intent(BeforeLoginActivity.this, PrefsActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		
		//save the shared prefs
		saveSharedPref();
		
		//start or stop the NoticeService base on prefs
		if (PrefsManager.isNotice_service_start() == true) {
			if (PrefsManager.isEmpty_table_notice() == false && 
					PrefsManager.isFinish_menu_notice() == false) {
				String action = "com.drhelper.service.intent.action.EXIT";
				Intent intent = new Intent(action);
				sendBroadcast(intent);
				PrefsManager.setNotice_service_start(false);
			}
		}else {
			if (PrefsManager.isEmpty_table_notice() == true || 
					PrefsManager.isFinish_menu_notice() == true) {
				Intent intent1 = new Intent(BeforeLoginActivity.this, NoticeService.class);
				startService(intent1);
				PrefsManager.setNotice_service_start(true);
			}
		}
	}

	protected void saveSharedPref() {
		//get the saved prefs item
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		String server_address = prefs.getString("server_address", 
				getString(R.string.prefs_activity_server_address_default_value));
		if (server_address != null && server_address.length() == 0) {
			DialogBox.showAlertDialog(BeforeLoginActivity.this, 
					this.getString(R.string.before_login_activity_server_address_is_null), null);
		}else {
			PrefsManager.setServer_address(server_address);
		}

		boolean empty_table_notice = prefs.getBoolean("empty_table_notice", false);
		PrefsManager.setEmpty_table_notice(empty_table_notice);

		boolean finish_menu_notice = prefs.getBoolean("finish_menu_notice", false);
		PrefsManager.setFinish_menu_notice(finish_menu_notice);
	}
	
	private class ExitReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
		    // TODO Auto-generated method stub
			if (intent.getAction().equals("com.drhelper.activity.intent.action.EXIT")) {
				//exit this activity
				finish();
			}
		}
	}
}
