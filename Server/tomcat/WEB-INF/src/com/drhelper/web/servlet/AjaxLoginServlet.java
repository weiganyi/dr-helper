package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.LoginObject;
import com.drhelper.web.service.AjaxLoginService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxLoginServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String user;
		String passwd;
		
		//if session not exist, create one
		session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}

		//get the request params
		user = request.getParameter("loginUser");
		passwd = request.getParameter("loginPasswd");
		
		//call the service
		AjaxLoginService service = new AjaxLoginService();
		LoginObject resultObj = service.doAction(session, user, passwd);
		if (resultObj == null) {
			response.sendError(500);
			System.out.println("AjaxLoginServlet.doGet(): Service return fail");
			return;
		}

		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
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
