package com.drhelper.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.service.LogoutService;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//do some prepare
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter();
		
		//check the session
		session = request.getSession(false);
		if (session == null || session.getAttribute("id") == null) {
			response.setStatus(401);
			System.out.println("LogoutServlet.doGet(): session isn't exist");
			return;
		}

		//call the service
		LogoutService service = new LogoutService();
		service.doAction(session, null);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
