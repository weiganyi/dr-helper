package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.FinishMenuObject;
import com.drhelper.web.service.AjaxFinishMenuService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxFinishMenuServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String op;
		String order;
		String menu;
		String page;

		//get the request params
		session = request.getSession(false);
		op = request.getParameter("op");
		order = request.getParameter("order");
		menu = request.getParameter("menu");
		page = request.getParameter("page");
		
		//call the service
		AjaxFinishMenuService service = new AjaxFinishMenuService();
		FinishMenuObject resultObj = service.doAction(session, op, order, menu, page);
		if (resultObj == null) {
			response.setStatus(400);
			System.out.println("AjaxFinishMenuServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxFinishMenu.jsp";
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
