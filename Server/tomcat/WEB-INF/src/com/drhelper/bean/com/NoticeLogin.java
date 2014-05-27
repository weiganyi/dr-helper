package com.drhelper.bean.com;

public class NoticeLogin {
	private String loginUserName;
	private String loginUserPasswd;
	private boolean result;

	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public String getLoginUserPasswd() {
		return loginUserPasswd;
	}
	public void setLoginUserPasswd(String loginUserPasswd) {
		this.loginUserPasswd = loginUserPasswd;
	}
}
