package com.drhelper.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drhelper.service.LoginService;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	private PrintWriter out;

	public LoginServlet() {
		super();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void service(ServletRequest request, ServletResponse response) {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//do some prepare
		response.setContentType("text/html");
		out = response.getWriter();
		
		//read the request body
		InputStreamReader input = new InputStreamReader(request.getInputStream());
		BufferedReader br = new BufferedReader(input);
		String line = null;
		String reqBody = null;
		while((line = br.readLine()) != null) {
			reqBody = reqBody+line;
		}
		if (reqBody == null) {
			System.out.println("LoginServlet.doGet(): request body is null");
			return;
		}

		//call the service
		LoginService service = new LoginService();
		String respBody = service.doLogin(reqBody);
		if (respBody == null) {
			System.out.println("LoginServlet.doGet(): response body is null");
			return;
		}
		
		//output the result
		out.print(respBody);
		out.flush();
		out.close();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
