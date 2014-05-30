<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><%= request.getAttribute("webName") %></title>
	<link type="text/css" rel="stylesheet" href="../res/drhelper.css"/>
</head>
<body>
	<%-- include the header --%>
    <%@ include file="header.jsp" %>

	<%-- include the sidebar --%>
    <%@ include file="sidebar.jsp" %>

	<%-- include the body --%>
    <%@ include file="body.jsp" %>

	<%-- include the foot --%>
    <%@ include file="foot.jsp" %>
</body>
</html>
