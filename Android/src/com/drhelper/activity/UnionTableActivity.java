package com.drhelper.activity;

import com.drhelper.R;
import com.drhelper.task.UnionTableTask;
import com.drhelper.util.DialogBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UnionTableActivity extends AfterLoginActivity {
	private Button unionBtn;
	private EditText table1Text;
	private EditText table2Text;
	private String tableNum1;
	private String tableNum2;
	
	private int startUnionTableTask = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.union_table_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.union_table_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		unionBtn = (Button)findViewById(R.id.union_table_activity_button);
		table1Text = (EditText)findViewById(R.id.union_table_activity_table1_edittext);
		table2Text = (EditText)findViewById(R.id.union_table_activity_table2_edittext);
		
		//set listen handler for union button
		unionBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tableNum1 = table1Text.getText().toString();
				tableNum2 = table2Text.getText().toString();
				
				if (checkInputTableNum(tableNum1, tableNum2)) {
					doUnionTable(tableNum1, tableNum2);
				}
			}
		});
	}

	private boolean checkInputTableNum(String tableNum1, String tableNum2) {
		if (tableNum1 == null || tableNum1.equals("") || 
				tableNum2 == null || tableNum2.equals("")){
			DialogBox.showAlertDialog(UnionTableActivity.this, 
					this.getString(R.string.union_table_activity_table_num_is_null), null);
			return false;
		}
		
		return true;
	}

	private void doUnionTable(String tableNum1, String tableNum2) {
		if (startUnionTableTask == 0) {
			startUnionTableTask = 1;

			UnionTableTask task = new UnionTableTask(UnionTableActivity.this);
			task.execute(tableNum1, tableNum2);
		}else {
			DialogBox.showAlertDialog(UnionTableActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doChangeTableResult(Integer result, String orderNum) {
		if (result == UnionTableTask.UNION_TABLE_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(UnionTableActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startUnionTableTask = 0;
			return;
		}else if (result == UnionTableTask.UNION_TABLE_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(UnionTableActivity.this, 
					this.getString(R.string.union_table_activity_remote_failure), null);
			startUnionTableTask = 0;
			return;
		}
		
		//launch to OrderActivity
		Intent intent = new Intent(UnionTableActivity.this, OrderActivity.class);
		//append the order num as extras data
		Bundle bundle = new Bundle();
		bundle.putString(OrderActivity.ORDER_NUM, orderNum);
		intent.putExtras(bundle);
		startActivity(intent);
		
		startUnionTableTask = 0;
	}
}
