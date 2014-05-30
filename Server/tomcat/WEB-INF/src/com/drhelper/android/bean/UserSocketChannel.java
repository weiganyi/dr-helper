package com.drhelper.android.bean;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class UserSocketChannel {
	private String userName;
	private SocketChannel channel;
	private SelectionKey key;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public SocketChannel getChannel() {
		return channel;
	}
	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}
	public SelectionKey getKey() {
		return key;
	}
	public void setKey(SelectionKey key) {
		this.key = key;
	}
}
