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

String footer="Copyright@2014 Powered by Weiganyi";
%>

<%-- ajaxOrderMenu.jsp --%>
<%
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
String thAmount="数量";
request.setAttribute("thAmount", thAmount);
String thRemark="备注";
request.setAttribute("thRemark", thRemark);
String thFinish="完成";
request.setAttribute("thFinish", thFinish);

String tdFetch="领取";
request.setAttribute("tdFetch", tdFetch);
String tdFinish="完成";
request.setAttribute("tdFinish", tdFinish);
%>

<%-- ajaxFinishMenu.jsp --%>
<%
String thCancel="取消完成";
request.setAttribute("thCancel", thCancel);
String tdCancel="取消完成";
request.setAttribute("tdCancel", tdCancel);
%>

<%-- ajaxAdminOrder.jsp --%>
<%
String thAdmin="管理员";
request.setAttribute("thAdmin", thAdmin);
String thPay="付款";
request.setAttribute("thPay", thPay);
String thPrice="价格";
request.setAttribute("thPrice", thPrice);
String thChef="厨师";
request.setAttribute("thChef", thChef);

String tdPay="付款";
request.setAttribute("tdPay", tdPay);

String adminOrderFromOrder="按订单号";
request.setAttribute("adminOrderFromOrder", adminOrderFromOrder);
String adminOrderOR="或";
request.setAttribute("adminOrderOR", adminOrderOR);
String adminOrderFromTable="按桌号";
request.setAttribute("adminOrderFromTable", adminOrderFromTable);
String adminOrderSearch="查询";
request.setAttribute("adminOrderSearch", adminOrderSearch);
String adminOrderClear="清空";
request.setAttribute("adminOrderClear", adminOrderClear);
%>
