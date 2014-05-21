package com.drhelper.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.service.LoadOrderService;

@SuppressWarnings("serial")
public class LoadOrderServlet extends HttpServlet {
	private PrintWriter out;
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//do some prepare
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out = response.getWriter();
		
		//check the session
		session = request.getSession(false);
		if (session == null || session.getAttribute("id") == null) {
			response.setStatus(401);
			System.out.println("LoadOrderServlet.doGet(): session isn't exist");
			return;
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
			System.out.println("LoadOrderServlet.doGet(): request body is null");
			return;
		}

		//call the service
		LoadOrderService service = new LoadOrderService();
		String respBody = service.doAction(session, reqBody);
		if (respBody == null) {
			response.setStatus(400);
			System.out.println("LoadOrderServlet.doGet(): response body is null");
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
