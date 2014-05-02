package com.drhelper.util;

import java.lang.reflect.Method;

import com.drhelper.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class DialogBox {
	private final static String DIALOG_BOX_TAG = "DialogBox";
	
	public static void showAlertDialog(final Activity act, String msg, final String result) {
		if (act == null || msg == null) {
			Log.e(DIALOG_BOX_TAG, "DialogBox.showAlertDialog(): input param is null");
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		
		builder.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton(R.string.dialogbox_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (result != null && result.length() != 0) {
						try {
							Class<? extends Activity> cls = act.getClass();
							Class<?>[] param = new Class<?>[]{};

							//get the callback method
							Method meth = cls.getMethod(result, param);
							if (meth != null) {
								Object argList[] = null;

								//call the callback function
								meth.invoke(act, argList);
							}
						} catch(Exception e) {
							Log.e(DIALOG_BOX_TAG, "DialogBox.showAlertDialog(): reflect get some exception");
						}
					}
				}
			});

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void showConfirmDialog(final Activity act, String msg, final String result) {
		if (act == null || msg == null) {
			Log.e(DIALOG_BOX_TAG, "DialogBox.showConfirmDialog(): input param is null");
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		
		builder.setMessage(msg)
			.setCancelable(true)
			.setPositiveButton(R.string.dialogbox_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (result != null && result.length() != 0) {
						try {
							Class<? extends Activity> cls = act.getClass();
							Class<?>[] param = new Class<?>[]{};

							//get the callback method
							Method meth = cls.getMethod(result, param);
							if (meth != null) {
								Object argList[] = null;

								//call the callback function
								meth.invoke(act, argList);
							}
						} catch(Exception e) {
							Log.e(DIALOG_BOX_TAG, "DialogBox.showConfirmDialog(): reflect get some exception");
						}
					}
				}
			});

		builder.setNegativeButton(R.string.dialogbox_cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});

		AlertDialog alert = builder.create();
		alert.show();
	}
}
