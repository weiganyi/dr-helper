package com.drhelper.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSON;
import com.drhelper.R;
import com.drhelper.activity.CheckTableActivity;
import com.drhelper.activity.MainActivity;
import com.drhelper.bean.com.NoticeDetail;
import com.drhelper.bean.com.NoticeInfo;
import com.drhelper.bean.com.NoticeLogin;
import com.drhelper.util.HttpEngine;
import com.drhelper.util.PrefsManager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class NoticeService extends Service {
	private static final String NOTICE_SERVICE_TAG = "NoticeService";
	
	private static final int PORT = 30000;
	
	private ExitReceiver receiver;

	private NotificationManager noticeMgr;
	private int noticeNum = 0;
	
	private Thread threadId = null;
	private Socket socket = null;
	
	private String userName;
	private String userPasswd;
	
	private BufferedReader br = null;;
	private OutputStream os = null;
	private InputStream is = null;
	
	@Override
	public void onCreate() {
		//register the exit receiver
		receiver = new ExitReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.drhelper.service.intent.action.EXIT");
		registerReceiver(receiver, filter);
		
		//get the notice manager
		noticeMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		//start the notice recv thread
		createNoticeThread();
		
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void onDestroy() {
		//stop the notice recv thread
		destoryNoticeThread();
		
		//unregister the exit receiver
		unregisterReceiver(receiver);
		
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("WorldReadableFiles")
	private void createNoticeThread() {
		//create a thread to detect notice from background
		ServiceWorker worker = new ServiceWorker();
		if (worker != null) {
			threadId = new Thread(worker, "NoticeService");
			if (threadId != null) {
				threadId.start();
			}
		}
	}
	
	private void destoryNoticeThread() {
		//cancel all notice
		noticeMgr.cancelAll();
		
		//close the socket;
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.DestoryNoticeThread(): close the socket failure");
			}
		}
		
		//stop the thread
		if (threadId != null) {
			threadId.interrupt();
			threadId = null;
		}
	}
	
	private class ServiceWorker implements Runnable {
		@Override
		public void run() {
			//create a socket
			String url = PrefsManager.getServer_address();
			try {
				socket = new Socket(url, PORT);
			} catch (UnknownHostException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): create the socket parse host failure ");
				return;
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): create the socket failure");
				return;
			}

			//construct InputStream and OutputStream
			try {
				os = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): getOutputStream connect to remote failure");
				return;
			}

			try {
				is = socket.getInputStream();
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): getInputStream connect to remote failure");
				return;
			}
			br = new BufferedReader(new InputStreamReader(is));

			//do login
			SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_READABLE);
			if (prefs == null) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): getSharedPreferences failure");
				return;
			}
			//get the name and passwd from shared prefs
			userName = prefs.getString("user_name", "");
			userPasswd = prefs.getString("user_passwd", "");
			if (userName.equals("") || userPasswd.equals("")) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): userName or userPasswd is null");
				return;
			}

			NoticeLogin loginReq = new NoticeLogin();
			loginReq.setUserName(userName);
			loginReq.setUserPasswd(userPasswd);

			//serialize by fastjson
			String request = JSON.toJSONString(loginReq);

			//send the request and recv response
			writeToServer(os, request);
			String content = readFromServer(br);
			//String content = "{\"userName\":\"2\", \"userPasswd\":\"2\", \"result\":true}";
			if (content == null) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): login return null");
				return;
			}
			NoticeLogin loginResp = JSON.parseObject(content, NoticeLogin.class);
			if (loginResp.isResult() != true || 
					loginResp.getUserName().equals(userName) == false) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): login result is failure");
				return;
			}
			
			//get notice info from server
			while ((content = readFromServer(br)) != null) {
			content = "{\"notice\":true}";
				NoticeInfo ni = JSON.parseObject(content, NoticeInfo.class);
				if (ni.isNotice() == true) {
					//if has notice, then get the notice detail from server
					try	{
						//send the http post and recv response
						String specUrl = "getNotice";
						String respBody = HttpEngine.doPost(specUrl, null);
						respBody = "{\"emptyTable\":true, \"finishMenu\":true, \"table\":3}";
						if (respBody == null || respBody.length() == 0) {
							Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): respBody is null");
							continue;
						}
						//unserialize from response string
						NoticeDetail noticeDetailResp = JSON.parseObject(respBody, NoticeDetail.class);

						//send the empty table notice
						if (noticeDetailResp.isEmptyTable() == true) {
							int emptyTableIcon = R.drawable.empty_table;
							String emptyTableInfo = getString(R.string.notice_service_empty_table);
							long emptyTableWhen = System.currentTimeMillis();
							Notification emptyTableNotice = 
									new Notification(emptyTableIcon, emptyTableInfo, emptyTableWhen);
								
							//emptyTableNotice.flags = Notification.FLAG_NO_CLEAR;
								
							Intent intent1 = new Intent(NoticeService.this, CheckTableActivity.class);
							PendingIntent emptyTableIntent = 
									PendingIntent.getActivity(NoticeService.this, 0, intent1, 0);
							emptyTableNotice.setLatestEventInfo(NoticeService.this, 
									null, emptyTableInfo, emptyTableIntent);
								
							noticeMgr.notify(noticeNum++, emptyTableNotice);
						}

						//send the finish menu notice
						if (noticeDetailResp.isFinishMenu() == true) {
							int finishMenuTable = noticeDetailResp.getTable();
							int finishMenuIcon = R.drawable.finish_menu;
							String finishMenuInfo = String.valueOf(finishMenuTable) + 
									getString(R.string.notice_service_finish_menu);
							long finishMenuWhen = System.currentTimeMillis();
							Notification finishMenuNotice = 
									new Notification(finishMenuIcon, finishMenuInfo, finishMenuWhen);
								
							//finishMenuNotice.flags = Notification.FLAG_NO_CLEAR;
								
							Intent intent2 = new Intent(NoticeService.this, MainActivity.class);
							PendingIntent finishMenuIntent = 
									PendingIntent.getActivity(NoticeService.this, 0, intent2, 0);
							finishMenuNotice.setLatestEventInfo(NoticeService.this, 
									null, finishMenuInfo, finishMenuIntent);

							noticeMgr.notify(noticeNum++, finishMenuNotice);
						}
					}catch(Exception e) {
						Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): json serialize or http post is failure");
					}
				}
			}
		}
	}
	
	public boolean writeToServer(OutputStream os, String request) {
		if (os != null) {
			try {
				os.write(request.getBytes("utf-8"));
				return true;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.writeToServer(): message encode failure");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.writeToServer(): write data failure");
			}
		}
		return false;
	}
	
	public String readFromServer(BufferedReader br) {
		if (br != null) {
			try {
				return br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.readFromServer(): read data failure");
			}
		}
		return null;
	}

	private class ExitReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
		    // TODO Auto-generated method stub
			if (intent.getAction().equals("com.drhelper.service.intent.action.EXIT")) {
				//exit this service
				stopSelf();
			}
		}
	}
}
