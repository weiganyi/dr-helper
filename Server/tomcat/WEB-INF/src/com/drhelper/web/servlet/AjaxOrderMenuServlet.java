package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.OrderMenuObject;
import com.drhelper.web.service.AjaxOrderMenuService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxOrderMenuServlet extends HttpServlet {
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
		AjaxOrderMenuService service = new AjaxOrderMenuService();
		OrderMenuObject resultObj = service.doAction(session, op, order, menu, page);
		if (resultObj == null) {
			response.sendError(500);
			System.out.println("AjaxOrderMenuServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxOrderMenu.jsp";
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
