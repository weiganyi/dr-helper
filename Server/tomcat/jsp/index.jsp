<%-- this jsp with the script --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><%= request.getAttribute("webName") %></title>
	<link type="text/css" rel="stylesheet" href="../res/drhelper.css"/>
	<script type="text/javascript" src="../res/jquery-1.10.1.js"></script>
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
			<input type="button" id="login_submit_input" value="<%= loginSubmit %>" onclick="onLoginSubmitClick();"></input>
		</div>
		<div id="title_div">
			<%= request.getAttribute("webName") %>
		</div>
		<div id="menu_div">
		</div>
	<% }else { %>
	    <div id="login_div">
			<input type="button" id="logout_submit_input" value="<%= logoutSubmit %>" onclick="onLogoutSubmitClick();"></input>
		</div>
		<div id="title_div">
			<%= request.getAttribute("webName") %>
		</div>
		<div id="menu_div">
		<% String auth = (String)request.getAttribute("auth");
			if(auth.equals("chef") == true) { %>
			<a href="#" class="menu_item_a" onclick="onOrderMenuClick();"><%= menuOrderMenu %></a>
			<a href="#" class="menu_item_a" onclick="onFinishMenuClick();"><%= menuFinishMenu %></a>
		<% }else if(auth.equals("admin") == true) { %>
			<a href="#" class="menu_item_a" onclick="onAdminOrderClick();"><%= menuAdminOrder %></a>
			<a href="#" class="menu_item_a" onclick="onAdminUserClick();"><%= menuAdminUser %></a>
			<a href="#" class="menu_item_a" onclick="onAdminTableClick();"><%= menuAdminTable %></a>
			<a href="#" class="menu_item_a" onclick="onAdminMenuClick();"><%= menuAdminMenu %></a>
			<a href="#" class="menu_item_a" onclick="onAdminOptionClick();"><%= menuAdminOption %></a>
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
	
	<script type="text/javascript">
		function onLoginSubmitClick() {
			var loginUser = $(login_user_input).val();
			var loginPasswd = $(login_passwd_input).val();
			
			//check the input
			if (loginUser == null || loginPasswd == null) {
				alert("<%= loginSubmitNull %>");
				return;
			}
			
			//start ajax to login
			var ajaxUrl = "ajaxLogin.do";
			var ajaxData = "loginUser=" + loginUser +  "&" + "loginPasswd=" + loginPasswd;
			jQuery.ajax({
				type: "POST",
				url: ajaxUrl,
				data: ajaxData,
				dataType: "html",
				success: fnLoginSuccess
			});
		}
		
		function fnLoginSuccess(data, code, request) {
			if (code == "success") {
				var header = $(header_div);
				header.html(data);
			}
		}

		function onLogoutSubmitClick() {
			//start ajax to logout
			var ajaxUrl = "ajaxLogout.do";
			jQuery.ajax({
				type: "GET",
				url: ajaxUrl,
				data: null,
				dataType: "html",
				success: fnLoginSuccess
			});
		}
		
		function fnOrderMenuClickSuccess(data, code, request) {
			if (code == "success") {
				var content = $(content_div);
				content.html(data);
			}
		}

		function onOrderMenuClick() {
			//start ajax to fetch the order menu list
			var ajaxUrl = "ajaxOrderMenu.do";
			jQuery.ajax({
				type: "GET",
				url: ajaxUrl,
				data: null,
				dataType: "html",
				success: fnOrderMenuClickSuccess
			});
		}

		function onFetchButtonClick(node) {
			var button = $(node);
			if (button) {
				var button_td = button.parent();
				if (button_td) {
					var order_td = button_td.next();
					if (order_td) {
						var order = order_td.text();
					}
					
					var menu_td = button_td.next().next().next().next().next();
					if (menu_td) {
						var menu = menu_td.text();
					}
				}
			}
			
			//start ajax to update the order menu
			var ajaxUrl = "ajaxOrderMenu.do";
			var ajaxData = "op=fetch" + "&" + "order=" + order + "&" + "menu=" + menu;
			jQuery.ajax({
				type: "POST",
				url: ajaxUrl,
				data: ajaxData,
				dataType: "html",
				success: fnOrderMenuClickSuccess
			});
		}

		function onFinishButtonClick(node) {
			var button = $(node);
			if (button) {
				var button_td = button.parent();
				if (button_td) {
					var order_td = button_td.prev().prev().prev().prev().prev().prev().prev();
					if (order_td) {
						var order = order_td.text();
					}
					
					var menu_td = button_td.prev().prev().prev();
					if (menu_td) {
						var menu = menu_td.text();
					}
				}
			}
			
			//start ajax to update the order menu
			var ajaxUrl = "ajaxOrderMenu.do";
			var ajaxData = "op=finish" + "&" + "order=" + order + "&" + "menu=" + menu;
			jQuery.ajax({
				type: "POST",
				url: ajaxUrl,
				data: ajaxData,
				dataType: "html",
				success: fnOrderMenuClickSuccess
			});
		}

		function onFinishMenuClick() {
			//start ajax to fetch the order menu list
			var ajaxUrl = "ajaxFinishMenu.do";
			jQuery.ajax({
				type: "GET",
				url: ajaxUrl,
				data: null,
				dataType: "html",
				success: fnOrderMenuClickSuccess
			});
		}

		function onCancelButtonClick(node) {
			var button = $(node);
			if (button) {
				var button_td = button.parent();
				if (button_td) {
					var order_td = button_td.prev().prev().prev().prev().prev().prev().prev();
					if (order_td) {
						var order = order_td.text();
					}
					
					var menu_td = button_td.prev().prev().prev();
					if (menu_td) {
						var menu = menu_td.text();
					}
				}
			}
			
			//start ajax to update the finish menu
			var ajaxUrl = "ajaxFinishMenu.do";
			var ajaxData = "op=cancel" + "&" + "order=" + order + "&" + "menu=" + menu;
			jQuery.ajax({
				type: "POST",
				url: ajaxUrl,
				data: ajaxData,
				dataType: "html",
				success: fnOrderMenuClickSuccess
			});
		}

		function onAdminOrderClick() {
		}

		function onAdminUserClick() {
		}

		function onAdminTableClick() {
		}

		function onAdminMenuClick() {
		}

		function onAdminOptionClick() {
		}
	</script>
</body>
</html>
