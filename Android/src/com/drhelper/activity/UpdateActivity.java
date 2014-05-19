package com.drhelper.activity;

import java.util.List;
import java.util.ListIterator;

import com.drhelper.R;
import com.drhelper.entity.Menu;
import com.drhelper.entity.MenuType;
import com.drhelper.provider.MenuProvider;
import com.drhelper.task.UpdateTask;
import com.drhelper.util.DialogBox;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UpdateActivity extends AfterLoginActivity {

	private TextView resultTV;
	private Button updateBtn;
	
	private int startUpdateTask = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		
		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.update_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.update_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//get widget handler
		resultTV = (TextView)findViewById(R.id.update_activity_result_textview);
		updateBtn = (Button)findViewById(R.id.update_activity_button);
		
		//set listen handler for update button
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doUpdate();
			}
		});
	}
	
	private void doUpdate() {
		if (startUpdateTask == 0) {
			startUpdateTask = 1;

			UpdateTask task = new UpdateTask(UpdateActivity.this);
			task.execute();
		}else {
			DialogBox.showAlertDialog(UpdateActivity.this, 
					this.getString(R.string.activity_asynctask_running), null);
		}
	}
	
	public void doUpdateResult(Integer result, 
			List<MenuType> menuTypeListResp, List<Menu> menuListResp) {
		if (result == UpdateTask.UPDATE_TASK_LOCAL_FALIURE || 
				result == UpdateTask.UPDATE_TASK_REMOTE_FALIURE) {
			resultTV.setText(this.getString(R.string.update_activity_result_false));
			startUpdateTask = 0;
			return;
		}
		
		//save the menu type into the sqlite
		if (menuTypeListResp.isEmpty() != true) {
			String menuTypeContent = "content://" + MenuProvider.AUTHORITY + "/" + MenuProvider.MENU_TYPE_TABLE_NAME;
			Uri menuTypeUri = Uri.parse(menuTypeContent);
			ContentResolver menuTypeCR = getContentResolver();
			ContentValues values;
			MenuType menuType;
			
			//clear the table of menu type
			menuTypeCR.delete(menuTypeUri, null, null);
			
			ListIterator<MenuType> iterator = menuTypeListResp.listIterator();
			while (iterator.hasNext()) {
				menuType = iterator.next();
				
				values = new ContentValues();
				values.put(MenuProvider.MENU_TYPE_ID, menuType.getMenu_type_id());
				values.put(MenuProvider.MENU_TYPE_NAME, menuType.getMenu_type_name());
				menuTypeCR.insert(menuTypeUri, values);
			}
		}
		
		//save the menu into the sqlite
		if (menuListResp.isEmpty() != true) {
			String menuContent = "content://" + MenuProvider.AUTHORITY + "/" + MenuProvider.MENU_TABLE_NAME;
			Uri menuUri = Uri.parse(menuContent);
			ContentResolver menuCR = getContentResolver();
			ContentValues values;
			Menu menu;
			
			//clear the table of menu type
			menuCR.delete(menuUri, null, null);
			
			ListIterator<Menu> iterator = menuListResp.listIterator();
			while (iterator.hasNext()) {
				menu = iterator.next();
				
				values = new ContentValues();
				values.put(MenuProvider.MENU_ID, menu.getMenu_id());
				values.put(MenuProvider.MENU_NAME, menu.getMenu_name());
				values.put(MenuProvider.MENU_PRICE, menu.getMenu_price());
				values.put(MenuProvider.MENU_TYPE_ID, menu.getMenu_type_id());
				menuCR.insert(menuUri, values);
			}
		}

		resultTV.setText(this.getString(R.string.update_activity_result_true));
		startUpdateTask = 0;
	}
}
