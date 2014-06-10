<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

String footer="版权所有	韦干翼 	@2014";
%>

<%-- ajaxOrderMenu.jsp --%>
<c:set var="thOrder" value="订单号" scope="request" />
<c:set var="thTable" value="桌号" scope="request" />
<c:set var="thWaiter" value="服务员" scope="request" />
<c:set var="thTime" value="下单时间" scope="request" />
<c:set var="thMenu" value="菜名" scope="request" />
<c:set var="thAmount" value="数量" scope="request" />
<c:set var="thRemark" value="备注" scope="request" />
<c:set var="thFinish" value="完成" scope="request" />

<c:set var="tdFetch" value="领取" scope="request" />
<c:set var="tdFinish" value="完成" scope="request" />

<%-- ajaxFinishMenu.jsp --%>
<c:set var="thCancel" value="取消完成" scope="request" />

<c:set var="tdCancel" value="取消完成" scope="request" />

<%-- ajaxAdminOrder.jsp --%>
<c:set var="thAdmin" value="管理员" scope="request" />
<c:set var="thPay" value="付款" scope="request" />
<c:set var="thPrice" value="价格" scope="request" />
<c:set var="thChef" value="厨师" scope="request" />

<c:set var="tdPay" value="付款" scope="request" />

<c:set var="adminOrderByOrder" value="按订单号" scope="request" />
<c:set var="adminOrderOR" value="或" scope="request" />
<c:set var="adminOrderByTable" value="按桌号" scope="request" />
<c:set var="adminOrderSearch" value="查询" scope="request" />
<c:set var="adminOrderClear" value="清空" scope="request" />

<c:set var="tdYes" value="是" scope="request" />
<c:set var="tdNo" value="否" scope="request" />

<%-- ajaxAdminUser.jsp --%>
<c:set var="adminUserName" value="用户名" scope="request" />
<c:set var="adminUserPasswd" value="密码" scope="request" />
<c:set var="adminUserAuth" value="权限" scope="request" />
<c:set var="adminUserCommit" value="提交" scope="request" />
<c:set var="adminUserDelete" value="删除" scope="request" />
<c:set var="adminUserClear" value="清空" scope="request" />
<c:set var="adminUserNull" value="用户名或密码为空，请重新输入" scope="request" />

<c:set var="adminUserWaiter" value="服务员" scope="request" />
<c:set var="adminUserChef" value="厨师" scope="request" />
<c:set var="adminUserAdmin" value="管理员" scope="request" />

<c:set var="thUserName" value="用户名" scope="request" />
<c:set var="thUserPasswd" value="密码" scope="request" />
<c:set var="thUserAuth" value="权限" scope="request" />
<c:set var="thEdit" value="修改" scope="request" />

<c:set var="tdEdit" value="修改" scope="request" />

