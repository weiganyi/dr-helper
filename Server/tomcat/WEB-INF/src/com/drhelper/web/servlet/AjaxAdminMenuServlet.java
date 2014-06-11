package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.AdminMenuObject;
import com.drhelper.web.service.AjaxAdminMenuService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxAdminMenuServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String op;
		String id;
		String name;
		String price;
		String type;
		String page;
		
		//get the request params
		session = request.getSession(false);
		op = request.getParameter("op");
		id = request.getParameter("id");
		name = request.getParameter("name");
		price = request.getParameter("price");
		type = request.getParameter("type");
		page = request.getParameter("page");

		//call the service
		AjaxAdminMenuService service = new AjaxAdminMenuService();
		AdminMenuObject resultObj = service.doAction(
				session, op, id, name, price, type, page);
		if (resultObj == null) {
			response.sendError(500);
			System.out.println("AjaxAdminMenuServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxAdminMenu.jsp";
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
