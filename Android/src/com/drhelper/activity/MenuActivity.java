package com.drhelper.activity;

import com.drhelper.R;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MenuActivity extends AfterLoginActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.main_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);
	}

}
