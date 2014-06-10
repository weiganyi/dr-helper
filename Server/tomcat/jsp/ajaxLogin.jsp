<%-- this jsp with the script --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- include the language array --%>
<%@ include file="lang/ch.jsp" %>

<% if (request.getAttribute("loginResult") == null || 
		request.getAttribute("loginResult").equals(Boolean.valueOf(false))) { %>
	<div id="login_div">
		<span><%= loginUser %>:  </span>
		<input type="text" id="login_user_input" value="<%= request.getAttribute("loginUser") %>"/>
		<div id="login_fail_notice"><%= loginFailNotice %></div>
		<span><%= loginPasswd %>: </span>
		<input type="password" id="login_passwd_input" value="<%= request.getAttribute("loginPasswd") %>"/>
		<input type="button" id="login_submit_input" value="<%= loginSubmit %>" onclick='onBtnLoginClick("<%= loginSubmitNull %>");'/>
	</div>
	<div id="title_div">
		<%= request.getAttribute("webName") %>
	</div>
	<div id="menu_div">
	</div>
<% }else { %>
	<div id="login_div">
		<input type="button" id="logout_submit_input" value="<%= logoutSubmit %>" onclick="onBtnLogoutClick();"/>
	</div>
	<div id="title_div">
		<%= request.getAttribute("webName") %>
	</div>
	<div id="menu_div">
	<% String auth = (String)request.getAttribute("auth");
		if(auth.equals("chef") == true) { %>
		<a href="#" class="menu_item_a" onclick="onMenuOrderMenuClick();"><%= menuOrderMenu %></a>
		<a href="#" class="menu_item_a" onclick="onMenuFinishMenuClick();"><%= menuFinishMenu %></a>
	<% }else if(auth.equals("admin") == true) { %>
		<a href="#" class="menu_item_a" onclick="onMenuAdminOrderClick();"><%= menuAdminOrder %></a>
		<a href="#" class="menu_item_a" onclick="onMenuAdminUserClick();"><%= menuAdminUser %></a>
		<a href="#" class="menu_item_a" onclick="onMenuAdminTableClick();"><%= menuAdminTable %></a>
		<a href="#" class="menu_item_a" onclick="onMenuAdminMenuClick();"><%= menuAdminMenu %></a>
		<a href="#" class="menu_item_a" onclick="onMenuAdminOptionClick();"><%= menuAdminOption %></a>
	<% } %>
	</div>
<% } %>
