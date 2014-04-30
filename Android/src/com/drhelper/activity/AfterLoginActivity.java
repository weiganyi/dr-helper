package com.drhelper.activity;

import com.drhelper.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("WorldWriteableFiles")
public class AfterLoginActivity extends BeforeLoginActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.main_menu) {
			launchMainActivity();
		}
		
		super.onOptionsItemSelected(item);
		return true;
	}

	private void launchMainActivity() {
		//launch to MainActivity
		Intent intent = new Intent(AfterLoginActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
