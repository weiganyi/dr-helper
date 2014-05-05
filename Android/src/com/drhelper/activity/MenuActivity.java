package com.drhelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import com.drhelper.R;
import com.drhelper.bean.MenuItem;
import com.drhelper.bean.MenuList;
import com.drhelper.provider.MenuProvider;
import com.drhelper.util.DialogBox;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MenuActivity extends AfterLoginActivity {

	private ListView menuLV;
	
	private static final String TYPE_TAG = "type";
	private static final String MENU_TAG = "menu";
	private static final String PRICE_TAG = "price";
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private String[] from = {TYPE_TAG, MENU_TAG, PRICE_TAG};
	private int[] to = {R.id.menu_activity_listview_menu_type_textview, 
			R.id.menu_activity_listview_menu_textview, 
			R.id.menu_activity_listview_price_textview};
	
	private MenuList menuList = new MenuList();
	private int selectPos;

	private String menu;
	private String price;
	private String amount;
	private String remark;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.menu_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.menu_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);
		
		//get widget handler
		menuLV = (ListView)findViewById(R.id.menu_activity_listview);
		
		//set click listener for ListView
		menuLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position > 0) {
					selectPos = position-1;

					MenuItem item = menuList.getItem().get(selectPos);
					if (item.isMenu() == true && item.getMenu().length() != 0) {
						//store the data
						menu = item.getMenu();
						price = String.valueOf(item.getPrice());
						
						//launch to OrderMenuActivity
						Intent intent = new Intent(MenuActivity.this, OrderMenuActivity.class);
						//append the menu as extras data
						Bundle bundle = new Bundle();
						bundle.putString(MenuActivity.MENU_TAG, menu);
						intent.putExtras(bundle);
						startActivityForResult(intent, 0);
					}
				}
			}
		});

		//load the menu data
		doLoadMenu();
	}
	
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		
		if (resCode == RESULT_OK && data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				amount = bundle.getString(OrderActivity.AMOUNT_TAG);
				remark = bundle.getString(OrderActivity.REMARK_TAG);
				
				//construct a Intent
				Intent intent = new Intent();
				//append the fields as extras data
				Bundle bundle2 = new Bundle();
				bundle2.putString(OrderActivity.MENU_TAG, menu);
				bundle2.putString(OrderActivity.PRICE_TAG, price);
				bundle2.putString(OrderActivity.AMOUNT_TAG, amount);
				bundle2.putString(OrderActivity.REMARK_TAG, remark);
				intent.putExtras(bundle2);
				setResult(RESULT_OK, intent);
				
				finish();
			}
		}
	}
	
	@SuppressLint("UseSparseArrays")
	private void doLoadMenu() {
		//query the table of menu type
		String menuTypeContent = "content://" + MenuProvider.AUTHORITY + "/" + MenuProvider.MENU_TYPE_TABLE_NAME;
		Uri menuTypeUri = Uri.parse(menuTypeContent);
		String[] menuTypeProjection = {MenuProvider.MENU_TYPE_ID, MenuProvider.MENU_TYPE_NAME};
		ContentResolver menuTypeCR = getContentResolver();
		Cursor menuTypeCursor = menuTypeCR.query(menuTypeUri, menuTypeProjection, null, null, null);
		
		//parse the menu type
		String menuTypeId;
		String menuTypeName;
		HashMap<String, String> menuTypeMap = new HashMap<String, String>();

		for (menuTypeCursor.moveToFirst(); 
				!menuTypeCursor.isAfterLast(); 
				menuTypeCursor.moveToNext()) {
			menuTypeId = menuTypeCursor.getString(0);
			menuTypeName = menuTypeCursor.getString(1);

			if (menuTypeId.length() != 0 && 
					menuTypeName.length() != 0) {
				menuTypeMap.put(menuTypeId, menuTypeName);
			}
		}
		
		//fill the title into the map
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TYPE_TAG, getString(R.string.menu_activity_listview_menu_type));
		map.put(MENU_TAG, getString(R.string.menu_activity_listview_menu));
		map.put(PRICE_TAG, getString(R.string.menu_activity_listview_price));
		//append the map into the list
		list.add(map);

		//query the table of menu
		String menuContent = "content://" + MenuProvider.AUTHORITY + "/" + MenuProvider.MENU_TABLE_NAME;
		Uri menuUri = Uri.parse(menuContent);
		String[] menuProjection = {MenuProvider.MENU_ID, MenuProvider.MENU_NAME, 
				MenuProvider.MENU_PRICE, MenuProvider.MENU_TYPE_ID};
		ContentResolver menuCR = getContentResolver();
		Cursor menuCursor = menuCR.query(menuUri, menuProjection, null, null, null);
		
		//parse and fill the menu
		String menuId;
		String menuName;
		String menuPrice;
		String lastMenuTypeId = new String();
		menuList.setItem(new ArrayList<MenuItem>());
		ListIterator<MenuItem> iterator = menuList.getItem().listIterator();
		MenuItem item;

		for (menuCursor.moveToFirst(); !menuCursor.isAfterLast(); menuCursor.moveToNext()) {
			menuId = menuCursor.getString(0);
			menuName = menuCursor.getString(1);
			menuPrice = menuCursor.getString(2);
			menuTypeId = menuCursor.getString(3);

			if (menuId.length() != 0 && menuName.length() != 0
					&& menuPrice.length() != 0 && menuTypeId.length() != 0) {
				//a new menu type start
				if (menuTypeId.equals(lastMenuTypeId) == false) {
					//check if exist this menu type 
					if (menuTypeMap.containsKey(menuTypeId) == false) {
						String msg = menuTypeId + 
								getString(R.string.menu_activity_menu_type_is_incorrect);
						DialogBox.showAlertDialog(MenuActivity.this, msg, null);
						finish();
					}

					//fill the menu type into the map
					map = new HashMap<String, String>();
					menuTypeName = menuTypeMap.get(menuTypeId);
					map.put(TYPE_TAG, menuTypeName);
					map.put(MENU_TAG, new String());
					map.put(PRICE_TAG, new String());
					//append the map into the list
					list.add(map);
					
					//fill the menu type into the menu list
					item = new MenuItem();
					item.setType(menuTypeName);
					item.setMenu(false);
					iterator.add(item);
					
					//record the new menu type
					lastMenuTypeId = menuTypeId;
				}

				//fill the menu into the map
				map = new HashMap<String, String>();
				map.put(TYPE_TAG, new String());
				map.put(MENU_TAG, menuName);
				map.put(PRICE_TAG, menuPrice);
				//append the map into the list
				list.add(map);

				//fill the menu into the menu list
				item = new MenuItem();
				item.setMenu(menuName);
				item.setPrice(Integer.valueOf(menuPrice));
				item.setMenu(true);
				iterator.add(item);
			}
		}
		
		//construct the adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.menu_activity_listview, 
				from, to);
		//bind the adapter
		menuLV.setAdapter(adapter);
	}
}
