package com.drhelper.web.bean;

public class FinishMenu {
	private int order;
	private int table;
	private String waiter;
	private String time;
	private String menu;
	private int amount;
	private String remark;

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
	public String getWaiter() {
		return waiter;
	}
	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
