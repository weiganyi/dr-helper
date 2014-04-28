package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.task.LoginTask;
import com.drhelper.util.DialogBox;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("WorldWriteableFiles")
public class LoginActivity extends Activity {

	private Button loginBtn;
	private EditText userText, passwdText;
	private String userName, userPasswd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//add the selfdefined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.login_title);

		//get widget handler
		userText = (EditText)findViewById(R.id.editText_user);
		passwdText = (EditText)findViewById(R.id.editText_passwd);
		loginBtn = (Button)findViewById(R.id.button_login);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_exit) {
			doLogout();
		}else if (item.getItemId() == R.id.menu_config) {
			
		}
		return true;
	}
	
	private boolean checkInput(String userName, String userPasswd) {
		if (userName.equals("")){
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.user_name_is_null), null);
			return false;
		}
		
		if (userPasswd.equals("")){
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.user_passwd_is_null), null);
			return false;
		}
		
		return true;
	}
	
	private void doLogin(String userName, String userPasswd) {
		//start a AsyncTask thread to do something:
		//1. do the json serialization
		//2. construct the request and sending, waiting for the response
		//3. after recv correct response, start a intent to MainActivity
		LoginTask task = new LoginTask(LoginActivity.this);
		task.execute(userName, userPasswd);
	}
	
	public void saveLoginUser() {
		//get the prefs manager
		SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_WRITEABLE);
		if (prefs != null) {
			//save the user
			Editor editor = prefs.edit();
			editor.putString("user_name", userName);
			editor.commit();
		}
	}
	
	private void doLogout() {
		DialogBox.showAlertDialog(LoginActivity.this, 
				this.getString(R.string.user_want_to_logout), "logoutCallBack");
	}
	
	public void logoutCallBack() {
		//get the prefs manager
		SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_WRITEABLE);
		if (prefs != null) {
			//clear the user
			Editor editor = prefs.edit();
			editor.putString("user_name", "");
			editor.commit();
		}

		//start a intent to the LoginActvity
		Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void jump2MainActivity() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
