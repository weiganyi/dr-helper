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
public class IndexServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		//if session not exist, create one
		session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}else {
			if (session.getAttribute("auth") != null && 
					session.getAttribute("auth").equals("") != true) {
				//set the attribute into the request
				request.setAttribute("auth", session.getAttribute("auth"));
			}
		}

		//call the service
		IndexService service = new IndexService();
		String webName = service.getWebName();
		if (webName == null) {
			response.setStatus(400);
			System.out.println("IndexServlet.doGet(): Service return fail");
			return;
		}

		//set the attribute into the request
		request.setAttribute("webName", webName);
		
		//dispatch the request
		String JspFileBaseName = "index.jsp";
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
