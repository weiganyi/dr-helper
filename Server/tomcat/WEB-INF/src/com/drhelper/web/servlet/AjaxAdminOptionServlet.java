package com.drhelper.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drhelper.web.bean.AdminOptionObject;
import com.drhelper.web.service.AjaxAdminOptionService;
import com.drhelper.web.util.ServletUtil;

@SuppressWarnings("serial")
public class AjaxAdminOptionServlet extends HttpServlet {
	private HttpSession session;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		String op;
		String name;
		String item;
		
		//get the request params
		session = request.getSession(false);
		op = request.getParameter("op");
		name = request.getParameter("name");
		item = request.getParameter("item");

		//call the service
		AjaxAdminOptionService service = new AjaxAdminOptionService();
		AdminOptionObject resultObj = service.doAction(
				session, op, name, item);
		if (resultObj == null) {
			response.sendError(500);
			System.out.println("AjaxAdminOptionServlet.doGet(): Service return fail");
			return;
		}
		
		//set the attribute into the request
		ServletUtil.setRequestAttr(request, resultObj);
		
		//dispatch the request
		String JspFileBaseName = "ajaxAdminOption.jsp";
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
