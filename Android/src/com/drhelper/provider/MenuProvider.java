package com.drhelper.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MenuProvider extends ContentProvider {
	private static final String MENU_PROVIDER_TAG = "MenuProvider";

	//database
	public static final String DB_NAME = "DrHelper.db";
	public static final int DB_VERSION = 2;
	
	//table
	public static final String MENU_TYPE_TABLE_NAME = "dr_menu_type"; 
	public static final String MENU_TABLE_NAME = "dr_menu"; 
	
	//field
	public static final String MENU_TYPE_ID = "menu_type_id";
	public static final String MENU_TYPE_NAME = "menu_type_name";
	
	public static final String MENU_ID = "menu_id";
	public static final String MENU_NAME = "menu_name";
	public static final String MENU_PRICE = "menu_price";
	
	//uri id
	public static final int MENU_TYPE = 1;
	public static final int MENU = 2;

	public static final String AUTHORITY = "com.drhelper.provider.MenuProvider";

	private DBHelper dbHelper;
	
	private UriMatcher uriMatcher;
	
	private HashMap<String ,String> menuTypeProjectionMap;
	private HashMap<String ,String> menuProjectionMap;
	
	public class DBHelper extends SQLiteOpenHelper {
		
		private static final String DB_HELPER_TAG = "DBHelper";
		
		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql;
			
			try {
				//create the dr_menu_type table
				sql = "create table " + MENU_TYPE_TABLE_NAME + "("
					    + MENU_TYPE_ID + " integer not null primary key autoincrement,"
					    + MENU_TYPE_NAME + " text not null);";
				db.execSQL(sql);
				
				//create the dr_menu_type table
				sql = "create table " + MENU_TABLE_NAME + "("
					    + MENU_ID + " integer not null primary key autoincrement,"
					    + MENU_NAME + " text not null,"
					    + MENU_PRICE + " integer not null,"
					    + MENU_TYPE_ID + " integer not null);";
				db.execSQL(sql);
				
				//test data
				db.execSQL("insert into dr_menu_type values (0, \"÷˜ ≥\");");
				db.execSQL("insert into dr_menu_type values (1, \"≥¥≤À\");");
				db.execSQL("insert into dr_menu values (0, \"œ„«€œ„∏…»‚Àø\", 12, 1);");
				db.execSQL("insert into dr_menu values (1, \"π¨±£º¶∂°\", 15, 1);");
				db.execSQL("insert into dr_menu values (2, \"¬¯Õ∑\", 5, 0);");
				db.execSQL("insert into dr_menu values (3, \"∆§µ∞ ›»‚÷‡\", 8, 0);");
			}catch (SQLException e) {
				Log.e(DB_HELPER_TAG, "DBHelper.onCreate(): "+e.getMessage());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql;

			try {
				sql = "drop table if exist " + MENU_TYPE_TABLE_NAME;
				db.execSQL(sql);
				sql = "drop table if exist " + MENU_TABLE_NAME;
				db.execSQL(sql);
			}catch (SQLException e) {
				Log.e(DB_HELPER_TAG, "DBHelper.onUpgrade(): "+e.getMessage());
			}

			onCreate(db);
		}
	}

	@Override
	public boolean onCreate() {
		//create the DBHelper
		dbHelper = new DBHelper(getContext());
		
		//add the url into the uriMatcher
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, MenuProvider.MENU_TYPE_TABLE_NAME, MENU_TYPE);
		uriMatcher.addURI(AUTHORITY, MenuProvider.MENU_TABLE_NAME, MENU);
		
		//add fileds into the map
		menuTypeProjectionMap = new HashMap<String ,String>();
		menuTypeProjectionMap.put(MenuProvider.MENU_TYPE_ID, MenuProvider.MENU_TYPE_ID);
		menuTypeProjectionMap.put(MenuProvider.MENU_TYPE_NAME, MenuProvider.MENU_TYPE_NAME);
		
		menuProjectionMap = new HashMap<String ,String>();
		menuProjectionMap.put(MenuProvider.MENU_ID, MenuProvider.MENU_ID);
		menuProjectionMap.put(MenuProvider.MENU_NAME, MenuProvider.MENU_NAME);
		menuProjectionMap.put(MenuProvider.MENU_PRICE, MenuProvider.MENU_PRICE);
		menuProjectionMap.put(MenuProvider.MENU_TYPE_ID, MenuProvider.MENU_TYPE_ID);
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String orderBy;
		Cursor c = null;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		try {
			switch (uriMatcher.match(uri)) {
			case MENU_TYPE:
				qb.setTables(MenuProvider.MENU_TYPE_TABLE_NAME);
				qb.setProjectionMap(menuTypeProjectionMap);
				break;
			
			case MENU:
				qb.setTables(MenuProvider.MENU_TABLE_NAME);
				qb.setProjectionMap(menuProjectionMap);
				break;
			
			default:
				Log.e(MENU_PROVIDER_TAG, "MenuProvider.query(): there is a incorrect url");
				return null;
			}
			
			orderBy = MenuProvider.MENU_TYPE_ID + " ASC";
			c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		}catch (SQLException e) {
			Log.e(MENU_PROVIDER_TAG, "MenuProvider.query(): "+e.getMessage());
		}

		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try {
			switch (uriMatcher.match(uri)) {
			case MENU_TYPE:
				db.insert(MenuProvider.MENU_TYPE_TABLE_NAME, null, values);
				break;
			
			case MENU:
				db.insert(MenuProvider.MENU_TABLE_NAME, null, values);
				break;
			
			default:
				Log.e(MENU_PROVIDER_TAG, "MenuProvider.insert(): there is a incorrect url");
			}
		}catch (SQLException e) {
			Log.e(MENU_PROVIDER_TAG, "MenuProvider.insert(): "+e.getMessage());
		}
		
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			switch (uriMatcher.match(uri)) {
			case MENU_TYPE:
				db.delete(MenuProvider.MENU_TYPE_TABLE_NAME, null, null);
				break;
			
			case MENU:
				db.delete(MenuProvider.MENU_TABLE_NAME, null, null);
				break;
			
			default:
				Log.e(MENU_PROVIDER_TAG, "MenuProvider.delete(): there is a incorrect url");
			}
		}catch (SQLException e) {
			Log.e(MENU_PROVIDER_TAG, "MenuProvider.delete(): "+e.getMessage());
		}
		
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
