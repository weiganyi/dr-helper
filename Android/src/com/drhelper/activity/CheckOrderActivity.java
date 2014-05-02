package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.task.CheckOrderTask;
import com.drhelper.util.DialogBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CheckOrderActivity extends AfterLoginActivity {
	private Button tableBtn;
	private EditText tableText;
	private String tableNum;
	private Button orderBtn;
	private EditText orderText;
	private String orderNum;
	
	private int startCheckOrderTask = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.check_order_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.check_order_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		tableBtn = (Button)findViewById(R.id.check_order_activity_table_num_button);
		tableText = (EditText)findViewById(R.id.check_order_activity_table_num_edittext);
		orderBtn = (Button)findViewById(R.id.check_order_activity_order_num_button);
		orderText = (EditText)findViewById(R.id.check_order_activity_order_num_edittext);
		
		//set listen handler for table button
		tableBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tableNum = tableText.getText().toString();
				
				if (checkInputTableNum(tableNum)) {
					doCheckOrderFromTableNum(tableNum);
				}
			}
		});
		
		//set listen handler for order button
		orderBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderNum = orderText.getText().toString();
				
				if (checkInputOrderNum(orderNum)) {
					doCheckOrderFromOrderNum(orderNum);
				}
			}
		});
	}
	
	private boolean checkInputTableNum(String tableNum) {
		if (tableNum == null || tableNum.equals("")){
			DialogBox.showAlertDialog(CheckOrderActivity.this, 
					this.getString(R.string.check_order_activity_table_num_is_null), null);
			return false;
		}
		
		return true;
	}
	
	private boolean checkInputOrderNum(String orderNum) {
		if (orderNum == null || orderNum.equals("")){
			DialogBox.showAlertDialog(CheckOrderActivity.this, 
					this.getString(R.string.check_order_activity_order_num_is_null), null);
			return false;
		}
		
		return true;
	}
	
	private void doCheckOrderFromTableNum(String tableNum) {
		if (startCheckOrderTask == 0) {
			startCheckOrderTask = 1;

			CheckOrderTask task = new CheckOrderTask(CheckOrderActivity.this);
			task.execute(null, tableNum);
		}else {
			DialogBox.showAlertDialog(CheckOrderActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	private void doCheckOrderFromOrderNum(String orderNum) {
		if (startCheckOrderTask == 0) {
			startCheckOrderTask = 1;

			CheckOrderTask task = new CheckOrderTask(CheckOrderActivity.this);
			task.execute(orderNum, null);
		}else {
			DialogBox.showAlertDialog(CheckOrderActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doCheckOrderResult(Integer result, int orderNum, int tableNum) {
		if (result == CheckOrderTask.CHECK_ORDER_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(CheckOrderActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startCheckOrderTask = 0;
			return;
		}else if (result == CheckOrderTask.CHECK_ORDER_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(CheckOrderActivity.this, 
					this.getString(R.string.check_order_activity_remote_failure), null);
			startCheckOrderTask = 0;
			return;
		}
		
		//launch to OrderActivity
		Intent intent = new Intent(CheckOrderActivity.this, OrderActivity.class);
		//append the order num and table num as extras data
		Bundle bundle = new Bundle();
		bundle.putString(OrderActivity.ORDER_NUM, String.valueOf(orderNum));
		bundle.putString(OrderActivity.TABLE_NUM, String.valueOf(tableNum));
		intent.putExtras(bundle);
		startActivity(intent);
		
		startCheckOrderTask = 0;
	}
}
