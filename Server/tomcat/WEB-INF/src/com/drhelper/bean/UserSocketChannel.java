package com.drhelper.bean;

import java.nio.channels.SocketChannel;

public class UserSocketChannel {
	private String userName;
	private SocketChannel channel;

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
}
