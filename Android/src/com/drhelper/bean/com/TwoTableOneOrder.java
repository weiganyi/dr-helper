package com.drhelper.bean.com;

public class TwoTableOneOrder {
	private int table1;
	private int table2;
	private int order;
	private boolean result = false;

	public int getTable1() {
		return table1;
	}
	public void setTable1(int table1) {
		this.table1 = table1;
	}
	public int getTable2() {
		return table2;
	}
	public void setTable2(int table2) {
		this.table2 = table2;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
}
