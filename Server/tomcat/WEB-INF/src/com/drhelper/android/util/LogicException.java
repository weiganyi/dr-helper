package com.drhelper.android.util;

public class LogicException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
