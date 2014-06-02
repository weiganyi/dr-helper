package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.service.IndexService;

@SuppressWarnings("serial")
public class AjaxLogoutServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		//if session not exist, it may be a fault
		session = request.getSession(false);
		if (session == null) {
			response.setStatus(400);
			System.out.println("AjaxLogoutServlet.doGet(): request params is incorrect");
			return;
		}else {
			//clear the session
			session.invalidate();
		}

		//call the service
		IndexService service = new IndexService();
		String webName = service.getWebName();
		if (webName == null) {
			response.setStatus(400);
			System.out.println("AjaxLogoutServlet.doGet(): response body is null");
			return;
		}

		//set the attribute into the request
		request.setAttribute("webName", webName);
		
		//dispatch the request
		String JspFileBaseName = "ajaxLogout.jsp";
		String JspPath = getServletContext().getInitParameter("jspPath");
		String JspFile = JspPath + JspFileBaseName;
		RequestDispatcher view = request.getRequestDispatcher(JspFile);
		view.forward(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		doGet(request, response);
	}
}
