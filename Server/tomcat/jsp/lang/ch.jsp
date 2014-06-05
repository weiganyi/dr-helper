<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- index.jsp --%>
<%
String loginUser="用户名";
String loginPasswd="密码";
String loginSubmit="登录";
String logoutSubmit="注销";
String loginSubmitNull="用户名或密码为空，请重新输入！";
String loginFailNotice="登录失败，请重新登录!";

String menuOrderMenu="已下单";
String menuFinishMenu="已完成";
String menuAdminOrder="订单管理";
String menuAdminUser="用户管理";
String menuAdminTable="餐桌管理";
String menuAdminMenu="菜单管理";
String menuAdminOption="配置";

String thFetch="领取";
request.setAttribute("thFetch", thFetch);
String thOrder="订单号";
request.setAttribute("thOrder", thOrder);
String thTable="桌号";
request.setAttribute("thTable", thTable);
String thWaiter="服务员";
request.setAttribute("thWaiter", thWaiter);
String thTime="下单时间";
request.setAttribute("thTime", thTime);
String thMenu="菜名";
request.setAttribute("thMenu", thMenu);
String thAccount="数量";
request.setAttribute("thAccount", thAccount);
String thRemark="备注";
request.setAttribute("thRemark", thRemark);
String thFinish="完成";
request.setAttribute("thFinish", thFinish);
String thCancel="取消完成";
request.setAttribute("thCancel", thCancel);

String footer="Copyright@2014 Powered by drhelper";
%>
