package com.drhelper.entity;

import java.util.ArrayList;

public class Order {
	private int order;
	private int table;
	private String user;
	private String time;
	private boolean pay;
	private ArrayList<Detail> detail;
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getTable() {
		return table;
	}
	public void setTable(int table) {
		this.table = table;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isPay() {
		return pay;
	}
	public void setPay(boolean pay) {
		this.pay = pay;
	}
	public ArrayList<Detail> getDetail() {
		return detail;
	}
	public void setDetail(ArrayList<Detail> detail) {
		this.detail = detail;
	}
}
