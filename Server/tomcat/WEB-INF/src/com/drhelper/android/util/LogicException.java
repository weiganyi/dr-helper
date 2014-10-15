package com.drhelper.android.util;

@SuppressWarnings("serial")
public class LogicException extends Exception {
	private String log;
	
	public LogicException() {
		
	}
	
	public LogicException(String log) {
		this.log = log;
	}
	
	public String getLog() {
		return this.log;
	}
}
