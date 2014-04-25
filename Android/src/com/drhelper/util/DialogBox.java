package com.drhelper.util;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogBox {
	public static void showAlertDialog(Activity act, String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		
		builder.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});

		AlertDialog alert = builder.create();
		alert.show();
	}
}
