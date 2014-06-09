package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.LogoutObject;
import com.drhelper.web.service.AjaxLogoutService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxLogoutServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		//get the request params
		session = request.getSession(false);

		//call the service
		AjaxLogoutService service = new AjaxLogoutService();
		LogoutObject resultObj = service.doAction(session);
		if (resultObj == null) {
			response.setStatus(400);
			System.out.println("AjaxLogoutServlet.doGet(): Service return fail");
			return;
		}

		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
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
