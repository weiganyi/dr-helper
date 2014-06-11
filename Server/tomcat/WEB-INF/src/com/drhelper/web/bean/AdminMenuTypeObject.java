package com.drhelper.web.bean;

import com.drhelper.common.entity.MenuType;

public class AdminMenuTypeObject {
	private MenuType[] menuType;
	private int startPage;
	private int endPage;
	private int currPage;
	private int totalPage;

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
	public MenuType[] getMenuType() {
		return menuType;
	}
	public void setMenuType(MenuType[] menuType) {
		this.menuType = menuType;
	}
}
