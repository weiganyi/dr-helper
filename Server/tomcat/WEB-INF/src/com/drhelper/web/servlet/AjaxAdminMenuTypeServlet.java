package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.AdminMenuTypeObject;
import com.drhelper.web.service.AjaxAdminMenuTypeService;
import com.drhelper.web.util.ServletUtil;

public class AjaxAdminMenuTypeServlet extends HttpServlet {
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
		String name;
		String page;
		
		//get the request params
		session = request.getSession(false);
		op = request.getParameter("op");
		id = request.getParameter("id");
		name = request.getParameter("name");
		page = request.getParameter("page");

		//call the service
		AjaxAdminMenuTypeService service = new AjaxAdminMenuTypeService();
		AdminMenuTypeObject resultObj = service.doAction(
				session, op, id, name, page);
		if (resultObj == null) {
			response.sendError(500);
			System.out.println("AjaxAdminMenuTypeServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxAdminMenuType.jsp";
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
