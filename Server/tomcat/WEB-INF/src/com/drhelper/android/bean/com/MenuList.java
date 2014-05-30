package com.drhelper.android.bean.com;

import java.util.ArrayList;

import com.drhelper.common.entity.Menu;

public class MenuList {
	private ArrayList<Menu> list;
	private boolean result;

	public ArrayList<Menu> getList() {
		return list;
	}
	public void setList(ArrayList<Menu> list) {
		this.list = list;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
}
