package com.drhelper.activity;

import com.drhelper.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AfterLoginActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//add the self defined title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		String title = getString(R.string.app_name) + " - " + getString(R.string.main_activity_title);
		((TextView)findViewById(R.id.main_title_textview)).setText(title);

		//load the data for gridview
		GridView gridView = (GridView)findViewById(R.id.main_activity_gridview);
		gridView.setAdapter(new ImageAdapter(this));
	}
	
	private class ImageAdapter extends BaseAdapter {

		private Context context;
		//picture id list
		private Integer[] picId = {
			R.drawable.chazhuo, R.drawable.kaizhuo, R.drawable.chadan, 
			R.drawable.zhuantai, R.drawable.bingtai, R.drawable.gengxin
		};
		
		ImageAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return picId.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imgView;

			if (convertView == null) {
				imgView = new ImageView(context);
			}else {
				imgView = (ImageView) convertView;
			}
			
			imgView.setImageResource(picId[position]);
			
			switch (position) {
				case 0:
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//launch to CheckTableActivity
							Intent intent = new Intent(MainActivity.this, CheckTableActivity.class);
							startActivity(intent);
							return;
						}
					});
					break;
				
				case 1:
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//launch to CreateTableActivity
							Intent intent = new Intent(MainActivity.this, CreateTableActivity.class);
							startActivity(intent);
							return;
						}
					});
					break;
				
				case 2:
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//launch to CheckOrderActivity
							Intent intent = new Intent(MainActivity.this, CheckOrderActivity.class);
							startActivity(intent);
							return;
						}
					});
					break;
				
				case 3:
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//launch to ChangeTableActivity
							Intent intent = new Intent(MainActivity.this, ChangeTableActivity.class);
							startActivity(intent);
							return;
						}
					});
					break;
				
				case 4:
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//launch to UnionTableActivity
							Intent intent = new Intent(MainActivity.this, UnionTableActivity.class);
							startActivity(intent);
							return;
						}
					});
					break;
				
				case 5:
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//launch to UpdateActivity
							Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
							startActivity(intent);
							return;
						}
					});
					break;

				default:
					break;
			}

			return imgView;
		}
	}
	
}
