package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.service.AjaxLoginService;
import com.drhelper.web.service.IndexService;

@SuppressWarnings("serial")
public class AjaxLoginServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		//if session not exist, create one
		session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}

		//get the request params
		String loginUser = request.getParameter("loginUser");
		String loginPasswd = request.getParameter("loginPasswd");
		if (loginUser == null || loginUser.equals("") == true ||
				loginPasswd == null || loginPasswd.equals("") == true) {
			response.setStatus(400);
			System.out.println("AjaxLoginServlet.doGet(): request params is incorrect");
			return;
		}
		
		//call the service
		AjaxLoginService service = new AjaxLoginService();
		boolean result = service.doLogin(session, loginUser, loginPasswd);

		//call the service
		IndexService service2 = new IndexService();
		String webName = service2.getWebName();
		if (webName == null) {
			response.setStatus(400);
			System.out.println("AjaxLoginServlet.doGet(): response body is null");
			return;
		}

		//set the attribute into the request
		request.setAttribute("webName", webName);
		request.setAttribute("loginResult", Boolean.valueOf(result));
		request.setAttribute("loginUser", loginUser);
		request.setAttribute("loginPasswd", loginPasswd);
		if (session.getAttribute("auth") != null && 
				session.getAttribute("auth").equals("") != true) {
			//set the attribute into the request
			request.setAttribute("auth", session.getAttribute("auth"));
		}
		
		//dispatch the request
		String JspFileBaseName = "ajaxLogin.jsp";
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
