package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.task.ChangeTableTask;
import com.drhelper.util.DialogBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeTableActivity extends AfterLoginActivity {
	private Button changeBtn;
	private EditText table1Text;
	private EditText table2Text;
	private String tableNum1;
	private String tableNum2;

	private int startChangeTableTask = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_table_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.change_table_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		changeBtn = (Button)findViewById(R.id.change_table_activity_button);
		table1Text = (EditText)findViewById(R.id.change_table_activity_table1_edittext);
		table2Text = (EditText)findViewById(R.id.change_table_activity_table2_edittext);
		
		//set listen handler for change button
		changeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tableNum1 = table1Text.getText().toString();
				tableNum2 = table2Text.getText().toString();
				
				if (checkInputTableNum(tableNum1, tableNum2)) {
					doChangeTable(tableNum1, tableNum2);
				}
			}
		});
	}

	private boolean checkInputTableNum(String tableNum1, String tableNum2) {
		if (tableNum1 == null || tableNum1.equals("") || 
				tableNum2 == null || tableNum2.equals("")){
			DialogBox.showAlertDialog(ChangeTableActivity.this, 
					this.getString(R.string.change_table_activity_table_num_is_null), null);
			return false;
		}
		
		return true;
	}

	private void doChangeTable(String tableNum1, String tableNum2) {
		if (startChangeTableTask == 0) {
			startChangeTableTask = 1;

			ChangeTableTask task = new ChangeTableTask(ChangeTableActivity.this);
			task.execute(tableNum1, tableNum2);
		}else {
			DialogBox.showAlertDialog(ChangeTableActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doChangeTableResult(Integer result, String orderNum) {
		if (result == ChangeTableTask.CHANGE_TABLE_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(ChangeTableActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startChangeTableTask = 0;
			return;
		}else if (result == ChangeTableTask.CHANGE_TABLE_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(ChangeTableActivity.this, 
					this.getString(R.string.change_table_activity_remote_failure), null);
			startChangeTableTask = 0;
			return;
		}
		
		//launch to OrderActivity
		Intent intent = new Intent(ChangeTableActivity.this, OrderActivity.class);
		//append the order num as extras data
		Bundle bundle = new Bundle();
		bundle.putString(OrderActivity.ORDER_NUM, orderNum);
		intent.putExtras(bundle);
		startActivity(intent);
		
		startChangeTableTask = 0;
	}
}
