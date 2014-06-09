<%-- this jsp with the script --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><%= request.getAttribute("webName") %></title>
	<link type="text/css" rel="stylesheet" href="../res/drhelper.css"/>
	<script type="text/javascript" src="../res/jquery-1.10.1.js"></script>
	<script type="text/javascript" src="../res/drhelper.js"></script>
</head>
<body>
	<%-- include the language array --%>
	<%@ include file="lang/ch.jsp" %>

	<%-- the header --%>
	<div id="header_div">
	<% if (session.getAttribute("id") == null) { %>
	    <div id="login_div">
			<span><%= loginUser %>:  </span>
			<input type="text" id="login_user_input"></input>
			<span><%= loginPasswd %>: </span>
			<input type="password" id="login_passwd_input"></input>
			<input type="button" id="login_submit_input" value="<%= loginSubmit %>" onclick='onBtnLoginClick("<%= loginSubmitNull %>");'></input>
		</div>
		<div id="title_div">
			<%= request.getAttribute("webName") %>
		</div>
		<div id="menu_div">
		</div>
	<% }else { %>
	    <div id="login_div">
			<input type="button" id="logout_submit_input" value="<%= logoutSubmit %>" onclick="onBtnLogoutClick();"></input>
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
	</div>
	<div id="divide_div">
		<hr id="divide_hr"></hr> 
	</div>

	<%-- the content --%>
	<div id="content_div">
	</div>

	<%-- the footer --%>
	<div id="footer_div">
		<%= footer %>
	</div>
</body>
</html>
