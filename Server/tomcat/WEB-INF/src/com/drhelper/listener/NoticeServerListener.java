package com.drhelper.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.drhelper.server.NoticeServer;

public class NoticeServerListener implements ServletContextListener {
	private Thread threadId = null;
	public static NoticeServer worker = null;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("NoticeServerListener.contextInitialized(): start");
		
		worker = new NoticeServer();

		threadId = new Thread(worker, "NoticeServer");
		if (threadId != null) {
			threadId.start();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (threadId != null) {
			threadId.interrupt();
			threadId = null;
		}

		System.out.println("NoticeServerListener.contextDestroyed(): stop");
	}
}
