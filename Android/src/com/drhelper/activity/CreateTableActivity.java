package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.task.CreateTableTask;
import com.drhelper.util.DialogBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTableActivity extends AfterLoginActivity {
	private Button tableBtn;
	private EditText tableText;
	private String tableNum;
	
	private int startCreateTableTask = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.create_table_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.create_table_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		tableBtn = (Button)findViewById(R.id.create_table_activity_button);
		tableText = (EditText)findViewById(R.id.create_table_activity_table_num_edittext);
		
		//set listen handler for create button
		tableBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tableNum = tableText.getText().toString();
				
				if (checkInput(tableNum)) {
					doCreateTable(tableNum);
				}
			}
		});
	}
	
	private boolean checkInput(String tableNum) {
		if (tableNum == null || tableNum.equals("")){
			DialogBox.showAlertDialog(CreateTableActivity.this, 
					this.getString(R.string.create_table_activity_table_num_is_null), null);
			return false;
		}
		
		return true;
	}
	
	private void doCreateTable(String tableNum) {
		if (startCreateTableTask == 0) {
			startCreateTableTask = 1;

			CreateTableTask task = new CreateTableTask(CreateTableActivity.this);
			task.execute(tableNum);
		}else {
			DialogBox.showAlertDialog(CreateTableActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doCreateTableResult(Integer result, int orderNum) {
		if (result == CreateTableTask.CREATE_TABLE_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(CreateTableActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startCreateTableTask = 0;
			return;
		}else if (result == CreateTableTask.CREATE_TABLE_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(CreateTableActivity.this, 
					this.getString(R.string.create_table_activity_remote_failure), null);
			startCreateTableTask = 0;
			return;
		}
		
		//launch to OrderActivity
		Intent intent = new Intent(CreateTableActivity.this, OrderActivity.class);
		//append the order num as extras data
		Bundle bundle = new Bundle();
		bundle.putString(OrderActivity.ORDER_NUM, String.valueOf(orderNum));
		intent.putExtras(bundle);
		startActivity(intent);
		
		startCreateTableTask = 0;
	}
}
