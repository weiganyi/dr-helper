package com.drhelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import com.drhelper.R;
import com.drhelper.bean.EmptyTable;
import com.drhelper.task.CheckTableTask;
import com.drhelper.util.DialogBox;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CheckTableActivity extends AfterLoginActivity {
	private Button checkBtn;
	private ListView tableLV;

	private int startCheckTableTask = 0;

	private final String TABLE_NUM = "table_num";
	private final String TABLE_SEAT_NUM = "table_seat_num";
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private String[] from = {TABLE_NUM, TABLE_SEAT_NUM};
	private int[] to = {R.id.check_table_activity_listview_table_num_textview, 
			R.id.check_table_activity_listview_table_seat_num_textview};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.check_table_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.check_table_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		checkBtn = (Button)findViewById(R.id.check_table_activity_button);
		tableLV = (ListView)findViewById(R.id.check_table_activity_listview);
		
		//set listen handler for check button
		checkBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doCheckTable();
			}
		});
	}

	private void doCheckTable() {
		if (startCheckTableTask == 0) {
			startCheckTableTask = 1;

			CheckTableTask task = new CheckTableTask(CheckTableActivity.this);
			task.execute();
		}else {
			DialogBox.showAlertDialog(CheckTableActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doCheckTableResult(Integer result, List<EmptyTable> emptyTableList) {
		if (result == CheckTableTask.CHECK_TABLE_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(CheckTableActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startCheckTableTask = 0;
			return;
		}else if (result == CheckTableTask.CHECK_TABLE_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(CheckTableActivity.this, 
					this.getString(R.string.check_table_activity_remote_failure), null);
			startCheckTableTask = 0;
			return;
		}

		
		EmptyTable emptyTable = null;
		HashMap<String, String> map = null;
		ListIterator<EmptyTable> it = emptyTableList.listIterator();

		//fill the title into the map
		map = new HashMap<String, String>();
		map.put(TABLE_NUM, getString(R.string.check_table_activity_table_num));
		map.put(TABLE_SEAT_NUM,	getString(R.string.check_table_activity_table_seat_num));
		//append the map into the list
		list.add(map);

		while(it.hasNext()) {
			emptyTable = it.next();

			//fill data into the map
			map = new HashMap<String, String>();
			map.put(TABLE_NUM, String.valueOf(emptyTable.getTableNum()));
			map.put(TABLE_SEAT_NUM,	String.valueOf(emptyTable.getTableSeatNum()));
			//append the map into the list
			list.add(map);
		}

		//construct the adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.check_table_activity_listview, 
				from, to);
		//bind the adapter
		tableLV.setAdapter(adapter);
		
		startCheckTableTask = 0;
	}
}
