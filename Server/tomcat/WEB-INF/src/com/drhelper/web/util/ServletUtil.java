package com.drhelper.web.util;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
	public static boolean setRequestAttr(HttpServletRequest request, Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			fields[i].setAccessible(true);

			try {
				request.setAttribute(fields[i].getName(), fields[i].get(obj));
			} catch (IllegalArgumentException e) {
				System.out.println("AjaxOrderMenuServlet.doGet(): catch a IllegalArgumentException: " + e.getMessage());
			} catch (IllegalAccessException e) {
				System.out.println("AjaxOrderMenuServlet.doGet(): catch a IllegalAccessException: " + e.getMessage());
			}
		}
		
		return true;
	}
}
