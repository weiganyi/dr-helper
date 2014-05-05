package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.util.DialogBox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OrderMenuActivity extends AfterLoginActivity {
	private static final String ORDER_MENU_ACTIVITY_TAG = "OrderMenuActivity";

	private static final String MENU_TAG = "menu";
	private static final String AMOUNT_TAG = "amount";
	private static final String REMARK_TAG = "remark";
	
	private String menu;
	private String amount;
	private String remark;
	
	private TextView menuTV;
	private EditText amountET;
	private EditText remarkET;
	private Button okBtn;
	private Button cancelBtn;
	
	private static final String DEFAULT_AMOUNT = "1";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.order_menu_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.order_menu_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);
		
		//get the extras data from intent
		Bundle bundle = getIntent().getExtras();
		menu = bundle.getString(MENU_TAG);
		if (menu == null || menu.length() == 0) {
			Log.e(ORDER_MENU_ACTIVITY_TAG, "OrderMenuActivity.onCreate(): the menu isn't exist");
			return;
		}
		
		//get widget handler
		menuTV = (TextView)findViewById(R.id.order_menu_activity_menu_textview);
		amountET = (EditText)findViewById(R.id.order_menu_activity_amount_edittext);
		remarkET = (EditText)findViewById(R.id.order_menu_activity_remark_edittext);
		okBtn = (Button)findViewById(R.id.order_menu_activity_ok_button);
		cancelBtn = (Button)findViewById(R.id.order_menu_activity_cancel_button);
		
		//load data for widget
		menuTV.setText(menu);
		amountET.setText(DEFAULT_AMOUNT);
		
		//set listen handler for ok button
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				amount = amountET.getText().toString();
				remark = remarkET.getText().toString();
				
				if (amount.length() == 0) {
					DialogBox.showAlertDialog(OrderMenuActivity.this, 
							getString(R.string.order_menu_activity_amount_is_null), null);
					return;
				}
				
				//construct a Intent
				Intent intent = new Intent();
				//append the menu as extras data
				Bundle bundle = new Bundle();
				bundle.putString(AMOUNT_TAG, amount);
				bundle.putString(REMARK_TAG, remark);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				
				finish();
			}
		});
		
		//set listen handler for cancel button
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//construct a Intent
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				
				finish();
			}
		});
	}
}
