package com.drhelper.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.drhelper.R;
import com.drhelper.activity.CheckTableActivity;
import com.drhelper.activity.MainActivity;
import com.drhelper.bean.com.NoticeDetail;
import com.drhelper.bean.com.NoticeHeartBeat;
import com.drhelper.bean.com.NoticeLogin;
import com.drhelper.bean.com.NoticeLogout;
import com.drhelper.bean.com.NoticePush;
import com.drhelper.bean.com.NoticeSubscribe;
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

@SuppressLint("WorldReadableFiles")
public class NoticeService extends Service {
	private static final String NOTICE_SERVICE_TAG = "NoticeService";
	
	private static final int PORT = 30000;
	
	private ExitReceiver exitReceiver;
	private SubReceiver subReceiver;

	private NotificationManager noticeMgr;
	private int noticeNum = 0;
	
	private Thread threadId = null;
	private Socket socket = null;
	
	private String userName;
	private String userPasswd;
	
	private OutputStream out = null;
	private InputStream in = null;
	
	private static final String heartBeatEvent = "heartbeat";
	
	private boolean is_empty_table = false;
	private boolean is_finish_menu = false;
	
	private ServiceWorker worker;
	
	private String logoutUserName = null;
	
	private ReentrantLock lock;
	
	private Timer timer;
	private HeartBeatTask task;
	
	private static final String connectLostEvent = "connlost";
	
	@Override
	public void onCreate() {
		//register the exit receiver
		exitReceiver = new ExitReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.drhelper.service.intent.action.EXIT");
		registerReceiver(exitReceiver, filter);

		//register the subscribe receiver
		subReceiver = new SubReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("com.drhelper.service.intent.action.SUBSCRIBE");
		registerReceiver(subReceiver, filter2);

		//get the notice manager
		noticeMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		//create a thread to detect notice from background
		worker = new ServiceWorker();
		if (worker != null) {
			threadId = new Thread(worker, "NoticeService");
			if (threadId != null) {
				threadId.start();
			}
		}
		
		//create a lock
		lock = new ReentrantLock();
		
		//start a timer to do heartbeat
		timer = new Timer();
		task = new HeartBeatTask();
		timer.schedule(task, 10000, 10000);	//after 10s to start, loop 10s timeout

		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		is_empty_table = intent.getExtras().getBoolean("empty_table");
		is_finish_menu = intent.getExtras().getBoolean("finish_menu");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		//stop the timer
		task.cancel();
		timer.purge();
		
		//stop the notice recv thread
		//after do it, the thread will detect the interrupt flag, then exit
		if (threadId != null) {
			threadId.interrupt();
		}

		//do logout
		if (logoutUserName != null && logoutUserName.equals("") != true) {
			worker.doLogout();
		}
		
		//unregister the receiver
		unregisterReceiver(exitReceiver);
		unregisterReceiver(subReceiver);
		
		//close the socket;
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.destoryNoticeThread(): close the socket failure");
			}
		}

		//cancel all notice
		noticeMgr.cancelAll();

		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private class ServiceWorker implements Runnable {
		@Override
		public void run() {
			boolean result = false;
			String content = null;

			// do login first
			result = doLogin();
			if (!result) {
				doExitService();
				return;
			}

			//check if thread had been interrupted
			while (!Thread.interrupted()) {
				try {
					lock.lock();
					
					// get message from server
					if ((content = readFromServer(in)) == null) {
						lock.unlock();
						continue;
					}

					//check if is connectLostEvent
					if (content.equals(connectLostEvent) == true) {
						//because it means the remote connect had lost, exit this worker and service
						lock.unlock();
						break;
					}
					
					// check if is NoticeHeartBeat response
					NoticeHeartBeat nhb = JSON.parseObject(content, NoticeHeartBeat.class);
					if (nhb != null && 
							nhb.getMsg() != null &&
							nhb.getMsg().equals(heartBeatEvent) == true && 
							nhb.isResult() == true) {
						lock.unlock();
						continue;
					}

					// check if is NoticePush
					NoticePush np = JSON.parseObject(content, NoticePush.class);
					if (np != null && np.getEvent() != null) {
						doNoticeDetailPull(np.getEvent());
					}
					
					lock.unlock();
				}catch (Exception e) {
					Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.run(): catch Exception");
					lock.unlock();
				}
			}
			
			doExitService();
			System.out.println("ServiceWorker.run(): thread exit");
			return;
		}
		
		private void doNoticeDetailPull(String event) {
			// if has notice, then get the notice detail from server
			NoticeDetail noticeDetailReq = new NoticeDetail();
			noticeDetailReq.setEvent(event);
			
			//serialize by fastjson
			String reqBody = JSON.toJSONString(noticeDetailReq);
			
			// send the http post and recv response
			String specUrl = "getNotice.do";
			String respBody = HttpEngine.doPost(specUrl, reqBody);
			if (respBody == null || respBody.length() == 0) {
				Log.e(NOTICE_SERVICE_TAG,
						"NoticeService.ServiceWorker.doNoticeDetailPull(): respBody is null");
				return;
			}
			// unserialize from response string
			NoticeDetail noticeDetailResp = JSON.parseObject(
					respBody, NoticeDetail.class);

			// send the empty table notice
			if (noticeDetailResp.isEmptyTable() == true) {
				int emptyTableIcon = R.drawable.empty_table;
				String emptyTableInfo = getString(R.string.notice_service_empty_table);
				long emptyTableWhen = System.currentTimeMillis();
				Notification emptyTableNotice = new Notification(
						emptyTableIcon, emptyTableInfo,
						emptyTableWhen);

				// emptyTableNotice.flags = Notification.FLAG_NO_CLEAR;

				Intent intent1 = new Intent(NoticeService.this,
						CheckTableActivity.class);
				PendingIntent emptyTableIntent = PendingIntent
						.getActivity(NoticeService.this, 0,
								intent1, 0);
				emptyTableNotice.setLatestEventInfo(
						NoticeService.this, null, emptyTableInfo,
						emptyTableIntent);

				noticeMgr.notify(noticeNum++, emptyTableNotice);
			}

			// send the finish menu notice
			if (noticeDetailResp.isFinishMenu() == true) {
				int finishMenuTable = noticeDetailResp.getTable();
				int finishMenuIcon = R.drawable.finish_menu;
				String finishMenuInfo = String
						.valueOf(finishMenuTable)
						+ getString(R.string.notice_service_finish_menu);
				long finishMenuWhen = System.currentTimeMillis();
				Notification finishMenuNotice = new Notification(
						finishMenuIcon, finishMenuInfo,
						finishMenuWhen);

				// finishMenuNotice.flags = Notification.FLAG_NO_CLEAR;

				Intent intent2 = new Intent(NoticeService.this,
						MainActivity.class);
				PendingIntent finishMenuIntent = PendingIntent
						.getActivity(NoticeService.this, 0,
								intent2, 0);
				finishMenuNotice.setLatestEventInfo(
						NoticeService.this, null, finishMenuInfo,
						finishMenuIntent);

				noticeMgr.notify(noticeNum++, finishMenuNotice);
			}
		}
		
		private boolean doExitService() {
			String action = null;
			Intent intent = null;
			String userName = null;

			SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_WRITEABLE);
			if (prefs != null) {
				//get the user
				userName = prefs.getString("user_name", "");
			}
			action = "com.drhelper.service.intent.action.EXIT";
			intent = new Intent(action);
			intent.putExtra("logout_user", userName);
			sendBroadcast(intent);
			PrefsManager.setNotice_service_start(false);
			
			return true;
		}
		
		private boolean doLogin() {
			//create a socket
			String url = PrefsManager.getServer_address();
			try {
				socket = new Socket(url, PORT);
				//set the noblock timeout to 1s 
				socket.setSoTimeout(1000);
			} catch (UnknownHostException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): create the socket parse host failure ");
				return false;
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): create the socket failure");
				return false;
			}

			//construct InputStream and OutputStream
			try {
				out = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): getOutputStream connect to remote failure");
				return false;
			}

			try {
				in = socket.getInputStream();
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): getInputStream connect to remote failure");
				return false;
			}

			//do login
			SharedPreferences prefs = getSharedPreferences("login_user", MODE_WORLD_READABLE);
			if (prefs == null) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): getSharedPreferences failure");
				return false;
			}
			//get the name and passwd from shared prefs
			userName = prefs.getString("user_name", "");
			userPasswd = prefs.getString("user_passwd", "");
			if (userName.equals("") || userPasswd.equals("")) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): userName or userPasswd is null");
				return false;
			}

			//create NoticeLogin object
			NoticeLogin loginReq = new NoticeLogin();
			loginReq.setLoginUserName(userName);
			loginReq.setLoginUserPasswd(userPasswd);

			//serialize by fastjson
			String request = JSON.toJSONString(loginReq);

			//send the request and recv response
			writeToServer(out, request);
			String content = readFromServer(in);
			if (content == null) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): login return null");
				return false;
			}
			NoticeLogin loginResp = JSON.parseObject(content, NoticeLogin.class);
			if (loginResp.isResult() != true || 
					loginResp.getLoginUserName().equals(userName) == false) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogin(): login result is failure");
				return false;
			}
			
			//subscribe the notice
			if (is_empty_table || is_finish_menu) {
				doSubscribe(is_empty_table, is_finish_menu);
			}
			return true;
		}
		
		public boolean doLogout() {
			//create NoticeLogout object
			NoticeLogout logoutReq = new NoticeLogout();
			logoutReq.setLogoutUserName(logoutUserName);
			
			//serialize by fastjson
			String request = JSON.toJSONString(logoutReq);
			
			//send the request and recv response
			writeToServer(out, request);
			String content = readFromServer(in);
			if (content == null) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogout(): logout return null");
				return false;
			}
			/*
			NoticeLogout logoutResp = JSON.parseObject(content, NoticeLogout.class);
			if (logoutResp.isResult() != true || 
					logoutResp.getLogoutUserName().equals(logoutUserName) == false) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doLogout(): logout result is failure");
				return false;
			}
			*/
			
			return true;
		}
		
		public boolean doSubscribe(boolean emptyTable, boolean finishMenu) {
			//create NoticeLogout object
			NoticeSubscribe subReq = new NoticeSubscribe();
			subReq.setEmptyTable(emptyTable);
			subReq.setFinishMenu(finishMenu);

			//serialize by fastjson
			String request = JSON.toJSONString(subReq);

			//send the request and recv response
			lock.lock();
			writeToServer(out, request);
			String content = readFromServer(in);
			lock.unlock();
			if (content == null) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doSubscribe(): subscribe return null");
				return false;
			}
			NoticeSubscribe subResp = JSON.parseObject(content, NoticeSubscribe.class);
			if (subResp.isResult() != true) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.ServiceWorker.doSubscribe(): subscribe result is failure");
				return false;
			}
			
			return true;
		}
	}
	
	public boolean writeToServer(OutputStream out, String request) {
		if (out != null) {
			try {
				out.write(request.getBytes("utf-8"));
				return true;
			} catch (UnsupportedEncodingException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.writeToServer(): message encode failure");
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.writeToServer(): write data catch IOException");
			}
		}
		return false;
	}
	
	public String readFromServer(InputStream in) {
		if (in != null) {
			byte[] receiveBuf = new byte[128];
			int recvMsgSize = 0;
			String content = null;
			
			try {
				recvMsgSize = in.read(receiveBuf);
				if (recvMsgSize != -1) {
					if (recvMsgSize != 0) {
						content = new String(receiveBuf, 0, recvMsgSize);
						return content;
					}
				}else {
					//if remote disconnect, come here
					content = new String(connectLostEvent);
					return content;
				}
			} catch (IOException e) {
				Log.e(NOTICE_SERVICE_TAG, "NoticeService.readFromServer(): read data catch IOException");
			}
		}

		return null;
	}
	
	private class ExitReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.drhelper.service.intent.action.EXIT")) {
				if (intent.getExtras() != null) {
					logoutUserName = intent.getExtras().getString("logout_user");
				}
				//exit this service
				stopSelf();
			}
		}
	}
	
	private class SubReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.drhelper.service.intent.action.SUBSCRIBE")) {
				is_empty_table = intent.getExtras().getBoolean("empty_table");
				is_finish_menu = intent.getExtras().getBoolean("finish_menu");
				
				//do subscribe
				worker.doSubscribe(is_empty_table, is_finish_menu);
			}
		}
	}

	class HeartBeatTask extends TimerTask {
		@Override
		public void run() {
			//create the NoticeHeartBeat object
			NoticeHeartBeat hbReq = new NoticeHeartBeat();
			hbReq.setMsg(heartBeatEvent);
			
			//serialize by fastjson
			String request = JSON.toJSONString(hbReq);

			//send the request
			lock.lock();
			writeToServer(out, request);
			lock.unlock();
			return;
		}
	}
}
