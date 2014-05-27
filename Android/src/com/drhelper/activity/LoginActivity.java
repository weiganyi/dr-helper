package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.service.NoticeService;
import com.drhelper.task.LoginTask;
import com.drhelper.util.DialogBox;
import com.drhelper.util.PrefsManager;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("WorldWriteableFiles")
public class LoginActivity extends BeforeLoginActivity {
	private Button loginBtn;
	private EditText userText, passwdText;
	private String userName, userPasswd;

	private int startLoginTask = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.login_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		userText = (EditText)findViewById(R.id.login_activity_user_edittext);
		passwdText = (EditText)findViewById(R.id.login_activity_passwd_edittext);
		loginBtn = (Button)findViewById(R.id.login_activity_button);

		//load the shared prefs
		super.saveSharedPref();
		
		//set listen handler for login button
		loginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userName = userText.getText().toString();
				userPasswd = passwdText.getText().toString();

				if (checkInput(userName, userPasswd)) {
					doLogin(userName, userPasswd);
				}
			}
		});
	}
	
	private boolean checkInput(String userName, String userPasswd) {
		if (userName == null || userName.equals("")){
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.login_activity_user_name_is_null), null);
			return false;
		}
		
		if (userPasswd == null || userPasswd.equals("")){
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.login_activity_user_passwd_is_null), null);
			return false;
		}
		
		return true;
	}
	
	private void doLogin(String userName, String userPasswd) {
		if (startLoginTask == 0) {
			startLoginTask = 1;

			//start a AsyncTask thread to do something:
			//1. do the json serialization
			//2. construct the request and sending, waiting for the response
			//3. after recv correct response, start a intent to MainActivity
			LoginTask task = new LoginTask(LoginActivity.this);
			task.execute(userName, userPasswd);
		}else {
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doLoginResult(Integer result) {
		if (result == LoginTask.LOGIN_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startLoginTask = 0;
			return;
		}else if (result == LoginTask.LOGIN_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.login_activity_remote_failure), null);
			startLoginTask = 0;
			return;
		}

		//get the prefs manager
		SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_WRITEABLE);
		if (prefs != null) {
			//save the user
			Editor editor = prefs.edit();
			editor.putString("user_name", userName);
			editor.putString("user_passwd", userPasswd);
			editor.commit();
		}else {
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startLoginTask = 0;
			return;
		}
		
		//start the NoticeService
		if (PrefsManager.isNotice_service_start() == false && 
				(PrefsManager.isEmpty_table_notice() == true || 
				PrefsManager.isFinish_menu_notice() == true)) {
			Intent intent1 = new Intent(LoginActivity.this, NoticeService.class);
			intent1.putExtra("empty_table", PrefsManager.isEmpty_table_notice());
			intent1.putExtra("finish_menu", PrefsManager.isFinish_menu_notice());
			startService(intent1);
			PrefsManager.setNotice_service_start(true);
		}
		
		//launch to MainActivity
		Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent2);

		startLoginTask = 0;
	}
}
