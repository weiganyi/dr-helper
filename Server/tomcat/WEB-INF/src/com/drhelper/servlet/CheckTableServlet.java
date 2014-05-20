package com.drhelper.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drhelper.service.CheckTableService;

@SuppressWarnings("serial")
public class CheckTableServlet extends HttpServlet {
	private PrintWriter out;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//do some prepare
		response.setContentType("text/html");
		out = response.getWriter();

		//call the service
		CheckTableService service = new CheckTableService();
		String respBody = service.doAction(null);
		if (respBody == null) {
			response.setStatus(400);
			System.out.println("CheckTableServlet.doGet(): response body is null");
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
