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

public class CreateTableActivity extends AfterLoginActivity {
	private Button createBtn;
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
		
		//get widget handler
		createBtn = (Button)findViewById(R.id.create_table_activity_button);
		tableText = (EditText)findViewById(R.id.create_table_activity_table_num_edittext);
		
		//set listen handler for create button
		createBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		}
	}
	
	public void doCreateTableResult() {
		//launch to OrderActivity
		Intent intent = new Intent(CreateTableActivity.this, OrderActivity.class);
		startActivity(intent);
		
		startCreateTableTask = 0;
	}
}
