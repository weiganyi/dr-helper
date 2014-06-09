package com.drhelper.web.bean;

public class FinishMenuObject {
	private FinishMenu[] finishMenu;
	private int startPage;
	private int endPage;
	private int currPage;
	private int totalPage;

	public FinishMenu[] getFinishMenu() {
		return finishMenu;
	}
	public void setFinishMenu(FinishMenu[] finishMenu) {
		this.finishMenu = finishMenu;
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
