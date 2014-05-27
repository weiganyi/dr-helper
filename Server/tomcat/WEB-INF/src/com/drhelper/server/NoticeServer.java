package com.drhelper.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.drhelper.bean.NoticeEvent;
import com.drhelper.bean.UserSocketChannel;
import com.drhelper.bean.com.NoticeHeartBeat;
import com.drhelper.bean.com.NoticeLogin;
import com.drhelper.bean.com.NoticeLogout;
import com.drhelper.bean.com.NoticePush;
import com.drhelper.bean.com.NoticeSubscribe;
import com.drhelper.db.DBManager;
import com.drhelper.entity.User;
import com.drhelper.util.LogicException;
import com.drhelper.util.TypeConvert;

public class NoticeServer implements Runnable {
	public static final int listenPort = 30000;
	
	private Selector selector;
	
	private LinkedList<UserSocketChannel> loginChanList;
	private LinkedList<UserSocketChannel> emptyTableChanList;
	private LinkedList<UserSocketChannel> finishMenuChanList;
	private LinkedList<UserSocketChannel> waitRespChanList;
	
	private Timer timer;
	
	public static final String heartBeatEvent = "heartbeat";
	public static final String emptyTableEvent = "emptytable";
	public static final String finishMenuEvent = "finishmenu";
	
	DatagramChannel eventChannel = null;

	public NoticeServer() {
		loginChanList = new LinkedList<UserSocketChannel>();
		emptyTableChanList = new LinkedList<UserSocketChannel>();
		finishMenuChanList = new LinkedList<UserSocketChannel>();
		waitRespChanList = new LinkedList<UserSocketChannel>();
	}
	
	@Override
	public void run() {
		try {
			// do some prepare work
			initServer();
		} catch (IOException e) {
			System.out.println("NoticeServer.run(): initServer catch Exception");
			return;
		}

		while (true) {
			try {
				// select any event
				selector.select();

				// iterate to process the event
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					// get the event
					SelectionKey key = it.next();

					// client connect event
					if (key.isAcceptable()) {
						// accept a connect
						ServerSocketChannel srvChan = (ServerSocketChannel) key.channel();
						SocketChannel channel = srvChan.accept();
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
						// client message event
					} else if (key.isReadable()) {
						SelectableChannel channel = key.channel();

						// channel to the client
						if (channel instanceof SocketChannel) {
							SocketChannel socketChannel = (SocketChannel) channel;

							// read the message from client
							ByteBuffer rBuf = ByteBuffer.allocate(256);
							socketChannel.read(rBuf);

							String buffer = TypeConvert.getStringFromByteBuffer(rBuf);
							if (buffer != null && buffer.equals("") != true) {
								// check if is heartbeat response
								NoticeHeartBeat hbResp = JSON.parseObject(buffer, NoticeHeartBeat.class);
								if (hbResp != null &&
										hbResp.getMsg() != null &&
										hbResp.getMsg().equals(heartBeatEvent) == true) {
									doHeartBeatResp(hbResp, socketChannel);
									continue;
								}
								
								// check if is login request
								NoticeLogin loginReq = JSON.parseObject(buffer, NoticeLogin.class);
								if (loginReq != null
										&& loginReq.getLoginUserName() != null
										&& loginReq.getLoginUserPasswd() != null) {
									doLogin(loginReq, socketChannel);
									continue;
								}

								// check if is logout request
								NoticeLogout logoutReq = JSON.parseObject(buffer, NoticeLogout.class);
								if (logoutReq != null
										&& logoutReq.getLogoutUserName() != null) {
									doLogout(logoutReq, socketChannel);
									continue;
								}

								// check if is subscribe request
								NoticeSubscribe subReq = JSON.parseObject(buffer, NoticeSubscribe.class);
								if (subReq != null) {
									doSubscribe(subReq, socketChannel);
									continue;
								}
							}
							// channel to the heartbeat
						} else if (channel instanceof DatagramChannel) {
							DatagramChannel datagramChannel = (DatagramChannel) channel;

							// receive the event
							ByteBuffer rBuf = ByteBuffer.allocate(32);
							datagramChannel.receive(rBuf);

							String buffer = TypeConvert.getStringFromByteBuffer(rBuf);
							if (buffer != null && buffer.equals("") != true) {
								NoticeEvent eventReq = JSON.parseObject(buffer, NoticeEvent.class);

								// check if is heartbeat event
								if (eventReq.getEvent().equals(heartBeatEvent) == true) {
									doHeartBeatReq();
									continue;
								}

								// check if is emptytable event
								if (eventReq.getEvent().equals(emptyTableEvent) == true) {
									doEmptyTable();
									continue;
								}

								// check if is finishmenu event
								if (eventReq.getEvent().equals(finishMenuEvent) == true) {
									doFinishMenu(eventReq.getUserName());
									continue;
								}
							}
						}
					}

					// delete the finished event
					it.remove();
				}
			} catch (IOException e) {
				System.out.println("NoticeServer.run(): while catch Exception");
			}
		}
	}
	
	public void initServer() throws IOException {
		//create a tcp server channel for client
		ServerSocketChannel srvChan = ServerSocketChannel.open();
		srvChan.configureBlocking(false);
		srvChan.socket().bind(new InetSocketAddress(listenPort));
		//create a selector
		selector = Selector.open();
		//register the tcp socket channel into the selector
		srvChan.register(selector, SelectionKey.OP_ACCEPT);
		
		//create a udp server channel for event message
		DatagramChannel dataChan = DatagramChannel.open();
		dataChan.configureBlocking(false);
		dataChan.socket().bind(new InetSocketAddress(listenPort));
		//register the udp socket channel into the selector
		dataChan.register(selector, SelectionKey.OP_READ);
		
		//create a udp client channel for event message
		eventChannel = DatagramChannel.open();
		InetSocketAddress address = new InetSocketAddress(listenPort);
		//bind the local port into the channel
		eventChannel.connect(address);
		
		//start a timer to do heartbeat
		timer = new Timer();
		HeartBeatTask task = new HeartBeatTask();
		timer.schedule(task, 5000, 30000);	//after 5s to start, loop 30s timeout
	}
	
	public void doLogin(NoticeLogin loginReq, SocketChannel channel) {
		String userName = loginReq.getLoginUserName();
		String userPasswd = loginReq.getLoginUserPasswd();
		boolean result = true;
		
		try {
			if (userName == null || userPasswd == null
					|| userName.length() == 0 || userPasswd.length() == 0) {
				System.out.println("NoticeServer.doLogin(): userName or userPasswd is null");
				throw new LogicException();
			}
			
			// check the user and passwd
			DBManager db = new DBManager();
			User user = db.getUser(userName, userPasswd);
			if (user != null) {
				// add the login channel into the loginChanList
				if (findChannel(loginChanList, channel) == null) {
					addChannel(loginChanList, userName, channel);
				}
			}
		} catch (LogicException e) {
			result = false;
		} finally {
			//create the response
			NoticeLogin loginResp = new NoticeLogin();
			loginResp.setLoginUserName(loginReq.getLoginUserName());
			loginResp.setLoginUserPasswd(loginReq.getLoginUserPasswd());
			loginResp.setResult(result);

			//send the response
			String response = JSON.toJSONString(loginResp);
			ByteBuffer sBuf = TypeConvert.getByteBufferFromString(response);
			try {
				channel.write(sBuf);
			} catch (IOException e) {
				System.out.println("NoticeServer.doLogin(): write channel catch IOException");
			}
		}
	}
	
	public void doLogout(NoticeLogout logoutReq, SocketChannel channel) throws IOException {
		String userName = logoutReq.getLogoutUserName();
		boolean result = true;

		try {
			if (userName == null || userName.length() == 0) {
				System.out.println("NoticeServer.doLogout(): userName is null");
				throw new LogicException();
			}
	
			// check the client if had already logined
			if (findChannel(loginChanList, channel) != null) {
				removeChannel(loginChanList, channel);
			}
			if (findChannel(emptyTableChanList, channel) != null) {
				removeChannel(emptyTableChanList, channel);
			}
			if (findChannel(finishMenuChanList, channel) != null) {
				removeChannel(finishMenuChanList, channel);
			}
			if (findChannel(waitRespChanList, channel) != null) {
				removeChannel(waitRespChanList, channel);
			}
		}catch (LogicException e) {
			result = false;
		}finally {
			//create the response
			NoticeLogout logoutResp = new NoticeLogout();
			logoutResp.setLogoutUserName(logoutReq.getLogoutUserName());
			logoutResp.setResult(result);

			//send the response
			String response = JSON.toJSONString(logoutResp);
			ByteBuffer sBuf = TypeConvert.getByteBufferFromString(response);
			try {
				channel.write(sBuf);
			} catch (IOException e) {
				System.out.println("NoticeServer.doLogout(): write channel catch IOException");
			}
			
			//close the socket
			channel.close();
		}
	}
	
	public void doSubscribe(NoticeSubscribe subReq, SocketChannel channel) {
		boolean isEmptyTable = subReq.isEmptyTable();
		boolean isFinishMenu = subReq.isFinishMenu();
		boolean result = true;

		try {
			// check the client if had already logined
			UserSocketChannel lgChan = findChannel(loginChanList, channel);
			if (lgChan != null) {
				String userName = lgChan.getUserName();
				
				//update the empty table list
				UserSocketChannel etChan = findChannel(emptyTableChanList, channel);
				if (isEmptyTable && etChan == null) {
					addChannel(emptyTableChanList, userName, channel);
				}else if (!isEmptyTable && etChan != null) {
					removeChannel(emptyTableChanList, channel);
				}
				
				//update the finish menu list
				UserSocketChannel fmChan = findChannel(finishMenuChanList, channel);
				if (isFinishMenu && fmChan == null) {
					addChannel(finishMenuChanList, userName, channel);
				}else if (!isFinishMenu && fmChan != null) {
					removeChannel(finishMenuChanList, channel);
				}
			}else {
				throw new LogicException();
			}
		}catch (LogicException e) {
			result = false;
		}finally {
			//create the response
			NoticeSubscribe subResp = new NoticeSubscribe();
			subResp.setEmptyTable(isEmptyTable);
			subResp.setFinishMenu(isFinishMenu);
			subResp.setResult(result);

			//send the response
			String response = JSON.toJSONString(subResp);
			ByteBuffer sBuf = TypeConvert.getByteBufferFromString(response);
			try {
				channel.write(sBuf);
			} catch (IOException e) {
				System.out.println("NoticeServer.doSubscribe(): write channel catch IOException");
			}
		}
	}

	public void doHeartBeatReq() {
		UserSocketChannel prevItem = null;
		SocketChannel channel = null;
		String userName = null;
		
		//close all connect in waitRespChanList
		for (UserSocketChannel item : waitRespChanList) {
			//first to process the prev item
			if (prevItem != null) {
				channel = prevItem.getChannel();

				removeChannel(waitRespChanList, channel);
				if (findChannel(loginChanList, channel) != null) {
					removeChannel(loginChanList, channel);
				}
				if (findChannel(emptyTableChanList, channel) != null) {
					removeChannel(emptyTableChanList, channel);
				}
				if (findChannel(finishMenuChanList, channel) != null) {
					removeChannel(finishMenuChanList, channel);
				}

				try {
					channel.close();
				} catch (IOException e) {
					System.out.println("NoticeServer.doHeartBeat(): channel.close catch IOException");
				}

				prevItem = null;
			}

			//heartbeat not response , think this connect had dead
			prevItem = item;
		}

		//then to process the leave item
		if (prevItem != null) {
			channel = prevItem.getChannel();

			removeChannel(waitRespChanList, channel);
			if (findChannel(loginChanList, channel) != null) {
				removeChannel(loginChanList, channel);
			}
			if (findChannel(emptyTableChanList, channel) != null) {
				removeChannel(emptyTableChanList, channel);
			}
			if (findChannel(finishMenuChanList, channel) != null) {
				removeChannel(finishMenuChanList, channel);
			}

			try {
				channel.close();
			} catch (IOException e) {
				System.out.println("NoticeServer.doHeartBeat(): channel.close catch IOException");
			}

			prevItem = null;
		}
		
		//send heartbeat request in loginChanList, move them to waitRespChanList
		for (UserSocketChannel item : loginChanList) {
			try {
				channel = item.getChannel();
				userName = item.getUserName();
				
				NoticeHeartBeat hbReq = new NoticeHeartBeat();
				hbReq.setMsg(heartBeatEvent);
				
				String request = JSON.toJSONString(hbReq);
				ByteBuffer sBuf = TypeConvert.getByteBufferFromString(request);
				
				//send the heartbeat request
				channel.write(sBuf);
				
				//add it into waitRespChanList
				addChannel(waitRespChanList, userName, channel);
			} catch (IOException e) {
				System.out.println("NoticeServer.doHeartBeat(): catch IOException");

				//add it into waitRespChanList
				addChannel(waitRespChanList, userName, channel);
			}
		}
	}
	
	public void doHeartBeatResp(NoticeHeartBeat hbResp, SocketChannel channel) {
		if (hbResp.isResult() == true) {
			if (findChannel(waitRespChanList, channel) != null) {
				removeChannel(waitRespChanList, channel);
			}
		}
	}

	public void doEmptyTable() {
		for (UserSocketChannel item : emptyTableChanList) {
			SocketChannel channel = item.getChannel();
			
			NoticePush hpReq = new NoticePush();
			hpReq.setNotice(true);
			
			String request = JSON.toJSONString(hpReq);
			ByteBuffer sBuf = TypeConvert.getByteBufferFromString(request);
			
			//send the emptytable request
			try {
				channel.write(sBuf);
			} catch (IOException e) {
				System.out.println("NoticeServer.doEmptyTable(): channel.write catch IOException");
			}
		}
	}

	public void doFinishMenu(String userName) {
		for (UserSocketChannel item : finishMenuChanList) {
			String user = item.getUserName();
			
			if (user.equals(userName) == true) {
				SocketChannel channel = item.getChannel();
				
				NoticePush hpReq = new NoticePush();
				hpReq.setNotice(true);
				
				String request = JSON.toJSONString(hpReq);
				ByteBuffer sBuf = TypeConvert.getByteBufferFromString(request);
				
				//send the finishmenu request
				try {
					channel.write(sBuf);
					return;
				} catch (IOException e) {
					System.out.println("NoticeServer.doFinishMenu(): channel.write catch IOException");
				}
			}
		}
	}
	
	public synchronized void publishEvent(String event, String userName) {
		NoticeEvent obj = new NoticeEvent();
		obj.setEvent(event);
		obj.setUserName(userName);
		String buffer = JSON.toJSONString(obj);
		ByteBuffer sBuf = TypeConvert.getByteBufferFromString(buffer);

		//send the event to server
		try {
			InetSocketAddress address = new InetSocketAddress(NoticeServer.listenPort);
			eventChannel.send(sBuf, address);
		} catch (IOException e) {
			System.out.println("NoticeServer.publishEvent(): channel.send catch IOException");
		}
	}
	
	public UserSocketChannel findChannel(LinkedList<UserSocketChannel> list, 
			SocketChannel channel) {
		SocketChannel chan = null;
		UserSocketChannel findItem = null;

		for (UserSocketChannel item : list) {
			chan = item.getChannel();
			if (chan.equals(channel) == true) {
				findItem = item;
				break;
			}
		}

		return findItem;
	}
	
	public UserSocketChannel addChannel(LinkedList<UserSocketChannel> list, 
			String userName, 
			SocketChannel channel) {
		UserSocketChannel item = new UserSocketChannel();
		item.setChannel(channel);
		item.setUserName(userName);
		list.add(item);
		
		return item;
	}
	
	public boolean removeChannel(LinkedList<UserSocketChannel> list, 
			SocketChannel channel) {
		SocketChannel chan = null;
		UserSocketChannel findItem = null;

		for (UserSocketChannel item : list) {
			chan = item.getChannel();
			if (chan.equals(channel) == true) {
				findItem = item;
				break;
			}
		}
		if (findItem != null) {
			list.remove(findItem);
			return true;
		}else {
			return false;
		}
	}
}

class HeartBeatTask extends TimerTask {
	DatagramChannel channel = null;
	SocketAddress address;
	
	public HeartBeatTask() {
		//create a udp channel
		try {
			channel = DatagramChannel.open();
		} catch (IOException e) {
			channel = null;
			System.out.println("HeartBeatTask.HeartBeatTask(): DatagramChannel.open catch IOException");
			return;
		}
		
		address = new InetSocketAddress(NoticeServer.listenPort);
		//bind the local port into the channel
		try {
			channel.connect(address);
		} catch (IOException e) {
			channel = null;
			System.out.println("HeartBeatTask.HeartBeatTask(): channel.connect catch IOException");
			return;
		}
	}
	
	@Override
	public void run() {
		if (channel != null) {
			NoticeEvent obj = new NoticeEvent();
			obj.setEvent(NoticeServer.heartBeatEvent);
			String buffer = JSON.toJSONString(obj);
			ByteBuffer sBuf = TypeConvert.getByteBufferFromString(buffer);

			//send the heartbeat event to server
			try {
				channel.send(sBuf, address);
			} catch (IOException e) {
				System.out.println("HeartBeatTask.run(): channel.send catch IOException");
			}
		}
	}
}
