package com.drhelper.web.bean;

import com.drhelper.common.entity.Table;

public class AdminTableObject {
	private Table[] table;
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
	public Table[] getTable() {
		return table;
	}
	public void setTable(Table[] table) {
		this.table = table;
	}
}
