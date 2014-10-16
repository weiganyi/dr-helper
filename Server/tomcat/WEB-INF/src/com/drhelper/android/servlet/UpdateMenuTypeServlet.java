package com.drhelper.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.android.service.UpdateMenuTypeService;

public class UpdateMenuTypeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
			System.out.println("UpdateMenuTypeServlet.doGet(): session isn't exist");
			return;
		}

		//call the service
		UpdateMenuTypeService service = new UpdateMenuTypeService();
		String respBody = service.doAction(session, null);
		if (respBody == null) {
			response.setStatus(400);
			System.out.println("UpdateMenuTypeServlet.doGet(): response body is null");
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
