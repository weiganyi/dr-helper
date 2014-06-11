<%-- this jsp with the script --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- include the language array --%>
<%@ include file="lang/ch.jsp" %>

<div id="login_div">
	<span><%= loginUser %>:  </span>
	<input type="text" id="login_user_input"/>
	<span><%= loginPasswd %>: </span>
	<input type="password" id="login_passwd_input"/>
	<input type="button" value="<%= loginSubmit %>" onclick='onBtnLoginClick("<%= loginSubmitNull %>");'/>
</div>
<div id="title_div">
	<%= request.getAttribute("webName") %>
</div>
<div id="menu_div">
</div>
