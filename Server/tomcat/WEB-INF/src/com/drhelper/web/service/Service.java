package com.drhelper.web.service;

public interface Service<A, B, C> {
	public C doAction(A session, B... param);
}
