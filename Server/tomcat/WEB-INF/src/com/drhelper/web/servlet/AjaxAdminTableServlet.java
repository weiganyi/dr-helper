package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.AdminTableObject;
import com.drhelper.web.service.AjaxAdminTableService;
import com.drhelper.web.util.ServletUtil;

public class AjaxAdminTableServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String op;
		String id;
		String table;
		String seat;
		String empty;
		String page;
		
		//get the request params
		session = request.getSession(false);
		op = request.getParameter("op");
		id = request.getParameter("id");
		table = request.getParameter("table");
		seat = request.getParameter("seat");
		empty = request.getParameter("empty");
		page = request.getParameter("page");

		//call the service
		AjaxAdminTableService service = new AjaxAdminTableService();
		AdminTableObject resultObj = service.doAction(
				session, op, id, table, seat, empty, page);
		if (resultObj == null) {
			response.sendError(500);
			System.out.println("AjaxAdminTableServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxAdminTable.jsp";
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
