package com.drhelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import com.drhelper.R;
import com.drhelper.entity.Detail;
import com.drhelper.entity.Order;
import com.drhelper.task.DeleteOrderTask;
import com.drhelper.task.LoadOrderTask;
import com.drhelper.task.SubmitOrderTask;
import com.drhelper.util.DialogBox;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderActivity extends AfterLoginActivity {
	private static final String ORDER_ACTIVITY_TAG = "OrderActivity";
	
	public static final String ORDER_NUM = "order_num";
	
	private String orderNum;

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
	
	public static final String MENU_TAG = "menu";
	public static final String PRICE_TAG = "price";
	public static final String AMOUNT_TAG = "amount";
	public static final String FINISH_TAG = "finish";
	public static final String REMARK_TAG = "remark";
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private String[] from = {MENU_TAG, PRICE_TAG, AMOUNT_TAG, FINISH_TAG, REMARK_TAG};
	private int[] to = {R.id.order_activity_listview_menu_textview, R.id.order_activity_listview_price_textview, 
			R.id.order_activity_listview_menu_type_textview, R.id.order_activity_listview_finish_textview, 
			R.id.order_activity_listview_remark_textview};

	private Order order;
	private int deletePos;
	
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
		if (orderNum == null || orderNum.length() == 0) {
			Log.e(ORDER_ACTIVITY_TAG, "OrderActivity.onCreate(): the order number isn't exist");
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
				doOrderMenu();
			}
		});

		//set listen handler for submit button
		submitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doSubmitOrder();
			}
		});

		//set listen handler for return button
		returnBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doReturnMainActivity();
			}
		});

		//set listen handler for delete button
		deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDeleteOrder();
			}
		});
		
		//load the order data for background
		doLoadOrder();
	}

	private void doLoadOrder() {
		if (startOrderTask == 0) {
			startOrderTask = 1;

			LoadOrderTask task = new LoadOrderTask(OrderActivity.this);
			task.execute(orderNum);
		}else {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doLoadOrderResult(Integer result, Order order) {
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
		
		//save the ListView data
		this.order = order;
		
		//fill the ListView data
		fillListView();

		//set long click listener for ListView
		menuLV.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					deletePos = position-1;
					DialogBox.showConfirmDialog(OrderActivity.this, 
							getString(R.string.order_activity_want_to_delete_menu), 
							"deleteMenu");
				}
				return false;
			}
		});
		
		startOrderTask = 0;
	}
	
	public void deleteMenu() {
		ArrayList<Detail> detail = order.getDetail();
		if (detail != null && detail.isEmpty() != true) {
			//delete the selected menu from detail
			detail.remove(deletePos);
			
			//clear the list
			list.clear();
			
			//refill the ListView data
			fillListView();
		}
	}

	private void fillListView() {
		//fill the value into TextView 
		orderTV.setText(String.valueOf(order.getOrder()));
		tableTV.setText(String.valueOf(order.getTable()));
		userTV.setText(order.getUser());
		timeTV.setText(order.getTime());
		payTV.setText(String.valueOf(order.isPay()));

		//fill the title into the map
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MENU_TAG, getString(R.string.order_activity_listview_menu));
		map.put(PRICE_TAG, getString(R.string.order_activity_listview_price));
		map.put(AMOUNT_TAG, getString(R.string.order_activity_listview_amount));
		map.put(FINISH_TAG, getString(R.string.order_activity_listview_finish));
		map.put(REMARK_TAG, getString(R.string.order_activity_listview_remark));
		//append the map into the list
		list.add(map);
		
		ArrayList<Detail> detail = order.getDetail();
		if (detail != null && detail.isEmpty() != true) {
			Detail menu = null;
			ListIterator<Detail> it = detail.listIterator();

			while(it.hasNext()) {
				menu = it.next();

				//fill data into the map
				map = new HashMap<String, String>();
				map.put(MENU_TAG, menu.getMenu());
				map.put(PRICE_TAG, String.valueOf(menu.getPrice()));
				map.put(AMOUNT_TAG, String.valueOf(menu.getAmount()));
				if (menu.isFinish() == true) {
					map.put(FINISH_TAG, getString(R.string.order_activity_finish_true));
				}else {
					map.put(FINISH_TAG, getString(R.string.order_activity_finish_false));
				}
				map.put(REMARK_TAG, menu.getRemark());
				//append the map into the list
				list.add(map);
			}
		}		

		//construct the adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.order_activity_listview, 
				from, to);
		//bind the adapter
		menuLV.setAdapter(adapter);	
	}
	
	private void doOrderMenu() {
		//launch to MenuActivity
		Intent intent = new Intent(OrderActivity.this, MenuActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		
		String menu;
		String price;
		String amount;
		String remark;
		HashMap<String, String> map;
		
		if (resCode == RESULT_OK && data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				menu = bundle.getString(MENU_TAG);
				price = bundle.getString(PRICE_TAG);
				amount = bundle.getString(AMOUNT_TAG);
				remark = bundle.getString(REMARK_TAG);
				
				//fill data into the order 
				ListIterator<Detail> iterator = order.getDetail().listIterator();
				Detail detail;
				
				detail = new Detail();
				detail.setMenu(menu);
				detail.setPrice(Integer.valueOf(price));
				detail.setAmount(Integer.valueOf(amount));
				detail.setFinish(false);
				detail.setRemark(remark);
				iterator.add(detail);
				
				//fill data into the map
				map = new HashMap<String, String>();
				map.put(MENU_TAG, menu);
				map.put(PRICE_TAG, price);
				map.put(AMOUNT_TAG, amount);
				map.put(FINISH_TAG, getString(R.string.order_activity_finish_false));
				map.put(REMARK_TAG, remark);
				//append the map into the list
				list.add(map);
				
				//construct the adapter
				SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.order_activity_listview, 
						from, to);
				//bind the adapter
				menuLV.setAdapter(adapter);	
			}
		}
	}
	
	private void doSubmitOrder() {
		if (startOrderTask == 0) {
			startOrderTask = 1;

			SubmitOrderTask task = new SubmitOrderTask(OrderActivity.this);
			task.execute(order);
		}else {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doSubmitOrderResult(Integer result) {
		if (result == LoadOrderTask.LOAD_ORDER_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startOrderTask = 0;
			return;
		}else if (result == LoadOrderTask.LOAD_ORDER_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.order_activity_submit_order_remote_failure), null);
			startOrderTask = 0;
			return;
		}
		
		startOrderTask = 0;

		//launch to MainActivity
		doReturnMainActivity();
	}

	private void doReturnMainActivity() {
		//launch to MainActivity
		Intent intent = new Intent(OrderActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	private void doDeleteOrder() {
		if (startOrderTask == 0) {
			startOrderTask = 1;

			DeleteOrderTask task = new DeleteOrderTask(OrderActivity.this);
			task.execute(order);
		}else {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doDeleteOrderResult(Integer result) {
		if (result == LoadOrderTask.LOAD_ORDER_TASK_LOCAL_FALIURE) {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.activity_asynctask_failure), null);
			startOrderTask = 0;
			return;
		}else if (result == LoadOrderTask.LOAD_ORDER_TASK_REMOTE_FALIURE) {
			DialogBox.showAlertDialog(OrderActivity.this, 
					this.getString(R.string.order_activity_delete_order_remote_failure), null);
			startOrderTask = 0;
			return;
		}
		
		startOrderTask = 0;
		
		//launch to MainActivity
		doReturnMainActivity();
	}
}
