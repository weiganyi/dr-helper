package com.drhelper.android.bean.com;

import com.drhelper.common.entity.Order;

public class OrderInfo {
	private Order order;
	private boolean result = false;

	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
}
