package com.drhelper.web.bean;

public class AdminMenu {
	private int menu_id;
	private String menu_name;
	private int menu_price;
	private int menu_type_id;
	private String menu_type_name;

	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public int getMenu_price() {
		return menu_price;
	}
	public void setMenu_price(int menu_price) {
		this.menu_price = menu_price;
	}
	public String getMenu_type_name() {
		return menu_type_name;
	}
	public void setMenu_type_name(String menu_type_name) {
		this.menu_type_name = menu_type_name;
	}
	public int getMenu_type_id() {
		return menu_type_id;
	}
	public void setMenu_type_id(int menu_type_id) {
		this.menu_type_id = menu_type_id;
	}
}
