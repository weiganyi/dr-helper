package com.drhelper.util;

import java.lang.reflect.Method;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class DialogBox {
	private final static String DIALOGBOX_TAG = "DialogBox";
	
	public static void showAlertDialog(final Activity act, String msg, final String cb) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		
		builder.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (cb.length() != 0) {
						try {
							Class<? extends Activity> cls = act.getClass();
							Class<?> param[] = null;
							String test = cb;

							//get the callback method
							Method meth = cls.getMethod(cb, param);
							if (meth != null) {
								Object argList[] = null;

								//call the callback function
								meth.invoke(act, argList);
							}
						} catch(Exception e) {
							Log.e(DIALOGBOX_TAG, "DialogBox.showAlertDialog(): get some exception");
						}
					}
				}
			});

		AlertDialog alert = builder.create();
		alert.show();
	}
}
