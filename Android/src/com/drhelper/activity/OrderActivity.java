package com.drhelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import com.drhelper.R;
import com.drhelper.bean.Menu;
import com.drhelper.bean.Order;
import com.drhelper.task.LoadOrderTask;
import com.drhelper.util.DialogBox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderActivity extends AfterLoginActivity {
	private static final String ORDERACTIVITY_TAG = "OrderActivity";
	
	public static final String ORDER_NUM = "order_num";
	public static final String TABLE_NUM = "table_num";
	
	private String orderNum;
	private String tableNum;

	private TextView orderTV;
	private TextView tableTV;
	private TextView userTV;
	private TextView timeTV;
	private TextView payTV;
	private Button orderBtn;
	private Button submitBtn;
	private Button returnBtn;
	private Button deleteBtn;
	private ListView menuLV;
	
	private int startOrderTask = 0;
	
	private static final String MENU = "menu";
	private static final String PRICE = "price";
	private static final String AMOUNT = "amount";
	private static final String FINISH = "finish";
	private static final String REMARK = "remark";
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private String[] from = {MENU, PRICE, AMOUNT, FINISH, REMARK};
	private int[] to = {R.id.order_activity_listview_menu_textview, R.id.order_activity_listview_price_textview, 
			R.id.order_activity_listview_amount_textview, R.id.order_activity_listview_finish_textview, 
			R.id.order_activity_listview_remark_textview};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.order_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.order_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);
		
		//get the extras data from intent
		Bundle bundle = getIntent().getExtras();
		orderNum = bundle.getString(ORDER_NUM);
		tableNum = bundle.getString(TABLE_NUM);
		if (orderNum == null || orderNum.length() == 0 || 
				tableNum == null || tableNum.length() == 0) {
			Log.e(ORDERACTIVITY_TAG, "OrderActivity.onCreate(): the order number or table number isn't exist");
			return;
		}
		
		//get widget handler
		orderTV = (TextView)findViewById(R.id.order_activity_order_value_textview);
		tableTV = (TextView)findViewById(R.id.order_activity_table_value_textview);
		userTV = (TextView)findViewById(R.id.order_activity_user_value_textview);
		timeTV = (TextView)findViewById(R.id.order_activity_time_value_textview);
		payTV = (TextView)findViewById(R.id.order_activity_pay_value_textview);
		orderBtn = (Button)findViewById(R.id.order_activity_order_menu_button);
		submitBtn = (Button)findViewById(R.id.order_activity_submit_order_button);
		returnBtn = (Button)findViewById(R.id.order_activity_return_main_button);
		deleteBtn = (Button)findViewById(R.id.order_activity_delete_order_button);
		menuLV = (ListView)findViewById(R.id.order_activity_menulist_listview);

		//set listen handler for order button
		orderBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doOrderMenu();
			}
		});

		//set listen handler for submit button
		submitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doSubmitOrder();
			}
		});

		//set listen handler for return button
		returnBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doReturnMainActivity();
			}
		});

		//set listen handler for delete button
		deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doDeleteOrder();
			}
		});
		
		//load the order data for background
		doLoad();
	}

	private void doLoad() {
		if (startOrderTask == 0) {
			startOrderTask = 1;

			LoadOrderTask task = new LoadOrderTask(OrderActivity.this);
			task.execute(orderNum, tableNum);
		}else {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doLoadResult(Integer result, Order order) {
		if (result == LoadOrderTask.LOAD_ORDER_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startOrderTask = 0;
			return;
		}else if (result == LoadOrderTask.LOAD_ORDER_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.order_activity_load_order_remote_failure), null);
			startOrderTask = 0;
			return;
		}
		
		//fill the value into TextView 
		orderTV.setText(String.valueOf(order.getOrder()));
		tableTV.setText(String.valueOf(order.getTable()));
		userTV.setText(order.getUser());
		timeTV.setText(order.getTime());
		payTV.setText(String.valueOf(order.isPay()));
		
		Menu menu = null;
		HashMap<String, String> map = null;
		ListIterator<Menu> it = order.getMenuList().listIterator();

		//fill the title into the map
		map = new HashMap<String, String>();
		map.put(MENU, getString(R.string.order_activity_listview_menu));
		map.put(PRICE, getString(R.string.order_activity_listview_price));
		map.put(AMOUNT, getString(R.string.order_activity_listview_amount));
		map.put(FINISH, getString(R.string.order_activity_listview_finish));
		map.put(REMARK, getString(R.string.order_activity_listview_remark));
		//append the map into the list
		list.add(map);

		while(it.hasNext()) {
			menu = it.next();

			//fill data into the map
			map = new HashMap<String, String>();
			map.put(MENU, menu.getMenu());
			map.put(PRICE, String.valueOf(menu.getPrice()));
			map.put(AMOUNT, String.valueOf(menu.getAmount()));
			map.put(FINISH, String.valueOf(menu.isFinish()));
			map.put(REMARK, menu.getRemark());
			//append the map into the list
			list.add(map);
		}

		//construct the adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.order_activity_listview, 
				from, to);
		//bind the adapter
		menuLV.setAdapter(adapter);
		
		startOrderTask = 0;
	}
	
	private void doOrderMenu() {
		
	}
	
	private void doSubmitOrder() {
		
	}
	
	private void doReturnMainActivity() {
		//launch to MainActivity
		Intent intent = new Intent(OrderActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	private void doDeleteOrder() {
		
	}
}
