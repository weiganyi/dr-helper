package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.task.LoginTask;
import com.drhelper.util.DialogBox;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private Button loginBtn;
	private EditText userText, passwdText;

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
				String userName = userText.getText().toString();
				String userPasswd = passwdText.getText().toString();

				if (checkInput(userName, userPasswd)) {
					doLogin(userName, userPasswd);
				}
			}
		});
		
		return;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private boolean checkInput(String userName, String userPasswd) {
		if (userName.equals("")){
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.user_name_is_null));
			return false;
		}
		
		if (userPasswd.equals("")){
			DialogBox.showAlertDialog(LoginActivity.this, 
					this.getString(R.string.user_passwd_is_null));
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
}
