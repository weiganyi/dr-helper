package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.OrderMenu;
import com.drhelper.web.service.AjaxOrderMenuService;

@SuppressWarnings("serial")
public class AjaxOrderMenuServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String order;
		String menu;
		
		//if session not exist, it may be a fault
		session = request.getSession(false);
		if (session == null) {
			response.setStatus(400);
			System.out.println("AjaxOrderMenuServlet.doGet(): session isn't exist");
			return;
		}

		//get the request params
		String op = request.getParameter("op");
		
		//call the service
		AjaxOrderMenuService service = new AjaxOrderMenuService();
		if (op != null) {
			order = request.getParameter("order");
			menu = request.getParameter("menu");
			if (op.equals("fetch") == true) { 
				service.updateOrderMenuFetch(session, order, menu);
			}else if (op.equals("finish") == true) {
				service.updateOrderMenuFinish(session, order, menu);
			}
		}
		OrderMenu[] orderMenuArray = service.getOrderMenuArray(session);
		if (orderMenuArray == null) {
			response.setStatus(400);
			System.out.println("AjaxOrderMenuServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		request.setAttribute("orderMenu", orderMenuArray);
		
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
