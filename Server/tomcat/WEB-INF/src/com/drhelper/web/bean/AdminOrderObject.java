package com.drhelper.web.bean;

public class AdminOrderObject {
	private AdminOrder[] adminOrder;
	private int startPage;
	private int endPage;
	private int currPage;
	private int totalPage;
	private int orderNum;
	private int startOrder;
	private int endOrder;
	private int tableNum;

	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getStartOrder() {
		return startOrder;
	}
	public void setStartOrder(int startOrder) {
		this.startOrder = startOrder;
	}
	public int getEndOrder() {
		return endOrder;
	}
	public void setEndOrder(int endOrder) {
		this.endOrder = endOrder;
	}
	public int getTableNum() {
		return tableNum;
	}
	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}
	public AdminOrder[] getAdminOrder() {
		return adminOrder;
	}
	public void setAdminOrder(AdminOrder[] adminOrder) {
		this.adminOrder = adminOrder;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
