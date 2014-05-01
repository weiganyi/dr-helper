package com.drhelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import com.drhelper.R;
import com.drhelper.bean.EmptyTable;
import com.drhelper.task.CheckTableTask;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CheckTableActivity extends AfterLoginActivity {

	private Button checkBtn;
	private ListView tableListView;

	private int startCheckTableTask = 0;

	private final String table_num = "table_num";
	private final String table_seat_num = "table_seat_num";
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private String[] from = {table_num, table_seat_num};
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
		
		//get widget handler
		checkBtn = (Button)findViewById(R.id.check_table_activity_button);
		tableListView = (ListView)findViewById(R.id.check_table_activity_listview);
		
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
		}
	}
	
	public void doCheckTableResult(List<EmptyTable> emptyTableList) {
		HashMap<String, String> map = null;
		ListIterator<EmptyTable> it = emptyTableList.listIterator();
		EmptyTable emptyTable = null;

		//fill the title into the map
		map = new HashMap<String, String>();
		map.put(table_num, getString(R.string.check_table_activity_table_num));
		map.put(table_seat_num,	getString(R.string.check_table_activity_table_seat_num));
		//append the map into the list
		list.add(map);

		while(it.hasNext()) {
			emptyTable = it.next();

			//fill data into the map
			map = new HashMap<String, String>();
			map.put(table_num, emptyTable.getTableNum());
			map.put(table_seat_num,	emptyTable.getTableSeatNum());
			//append the map into the list
			list.add(map);
		}

		//construct the adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.check_table_activity_listview, 
				from, to);
		//bind the adapter
		tableListView.setAdapter(adapter);
		
		startCheckTableTask = 0;
	}
}
