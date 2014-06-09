package com.drhelper.web.bean;

public class OrderMenuObject {
	private OrderMenu[] orderMenu;
	private int startPage;
	private int endPage;
	private int currPage;
	private int totalPage;

	public OrderMenu[] getOrderMenu() {
		return orderMenu;
	}
	public void setOrderMenu(OrderMenu[] orderMenu) {
		this.orderMenu = orderMenu;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
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
