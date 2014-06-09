package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.AdminOrderObject;
import com.drhelper.web.service.AjaxAdminOrderService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxAdminOrderServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String op;
		String payOrder;
		String page;
		String order;
		String startOrder;
		String endOrder;
		String table;
		
		//get the request params
		session = request.getSession(false);
		op = request.getParameter("op");
		payOrder = request.getParameter("payOrder");
		page = request.getParameter("page");
		order = request.getParameter("order");
		startOrder = request.getParameter("startOrder");
		endOrder = request.getParameter("endOrder");
		table= request.getParameter("table");

		//call the service
		AjaxAdminOrderService service = new AjaxAdminOrderService();
		AdminOrderObject resultObj = service.doAction(
				session, op, payOrder, page, order, startOrder, endOrder, table);
		if (resultObj == null) {
			response.setStatus(400);
			System.out.println("AjaxAdminOrderServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxAdminOrder.jsp";
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
