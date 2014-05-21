package com.drhelper.bean.com;

import java.util.ArrayList;

import com.drhelper.entity.MenuType;

public class MenuTypeList {
	private ArrayList<MenuType> list;
	private boolean result;

	public ArrayList<MenuType> getList() {
		return list;
	}
	public void setList(ArrayList<MenuType> list) {
		this.list = list;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
}
