package com.drhelper.android.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.android.service.LoginService;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	private PrintWriter out;
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//do some prepare
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out = response.getWriter();
		
		//if session not exist, create one
		session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}
		
		//read the request body
		InputStreamReader input = new InputStreamReader(request.getInputStream());
		BufferedReader br = new BufferedReader(input);
		String line = null;
		String reqBody = br.readLine();
		while((line = br.readLine()) != null) {
			reqBody = reqBody+line;
		}
		if (reqBody.length() == 0) {
			response.setStatus(400);
			System.out.println("LoginServlet.doGet(): request body is null");
			return;
		}

		//call the service
		LoginService service = new LoginService();
		String respBody = service.doAction(session, reqBody);
		if (respBody == null) {
			response.setStatus(400);
			System.out.println("LoginServlet.doGet(): response body is null");
			return;
		}
		
		//output the result
		out.println(respBody);
		out.flush();
		out.close();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
