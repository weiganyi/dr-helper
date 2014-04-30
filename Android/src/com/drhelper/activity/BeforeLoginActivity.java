package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.util.DialogBox;

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
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("WorldWriteableFiles")
public class BeforeLoginActivity extends Activity {
	private ExitReceiver receiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//register the exit receiver
		receiver = new ExitReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.drhelper.intent.action.EXIT");
		registerReceiver(receiver, filter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_exit) {
			doLogout();
		}else if (item.getItemId() == R.id.menu_prefs) {
			launchPrefsActivity();
		}
		return true;
	}
	
	private void doLogout() {
		DialogBox.showAlertDialog(BeforeLoginActivity.this, 
				this.getString(R.string.before_login_activity_want_to_logout), "doLogoutCallBack");
	}
	
	public void doLogoutCallBack() {
		//get the prefs manager
		SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_WRITEABLE);
		if (prefs != null) {
			//clear the user
			Editor editor = prefs.edit();
			editor.putString("user_name", "");
			editor.commit();
		}

		//send the exit broadcast
		String action = "com.drhelper.intent.action.EXIT";
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
		
		//get the saved prefs item
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String addr = prefs.getString("server_address", 
				getString(R.string.prefs_activity_server_address_default_value));
		//check if the server address is null
		if (addr.length() == 0) {
			DialogBox.showAlertDialog(BeforeLoginActivity.this, 
					this.getString(R.string.before_login_activity_server_address_is_null), null);
		}
	}

	private class ExitReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
		    // TODO Auto-generated method stub
			if (intent.getAction().equals("com.drhelper.intent.action.EXIT")) {
				//exit this activity
				finish();
			}
		}
	}
}
