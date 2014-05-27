package com.drhelper.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CompRespFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		System.out.println("CompRespFilter.doFilter(): before do filter");
		
		arg2.doFilter(arg0, arg1);
		
		System.out.println("CompRespFilter.doFilter(): after do filter");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
