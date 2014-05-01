package com.drhelper.activity;

import com.drhelper.R;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class OrderActivity extends AfterLoginActivity {
	private static final String ORDERACTIVITY_TAG = "OrderActivity";
	
	public static final String ORDER_NUM = "order_num";
	public static final String TABLE_NUM = "table_num";
	
	private String orderNum;
	private String tableNum;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.order_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		
		//get the extras data from intent
		Bundle bundle = getIntent().getExtras();
		orderNum = bundle.getString(ORDER_NUM);
		tableNum = bundle.getString(TABLE_NUM);
		if (orderNum == null || orderNum.length() == 0 || 
				tableNum == null || tableNum.length() == 0) {
			Log.e(ORDERACTIVITY_TAG, "OrderActivity.onCreate(): the order number or table number isn't exist");
			return;
		}
		
		
	}

}
