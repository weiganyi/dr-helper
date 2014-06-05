<%-- this jsp without the script --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<c:import url="lang/ch.jsp" />

<center>
	<table id="finish_menu_table">
		<tr>
			<th><c:out value="${thOrder}" /></th>
			<th><c:out value="${thTable}" /></th>
			<th><c:out value="${thWaiter}" /></th>
			<th><c:out value="${thTime}" /></th>
			<th><c:out value="${thMenu}" /></th>
			<th><c:out value="${thAccount}" /></th>
			<th><c:out value="${thRemark}" /></th>
			<th><c:out value="${thCancel}" /></th>
		</tr>
		<c:forEach var="menu" items="${finishMenu}">
		<tr>
			<td>${menu.order}</td>
			<td>${menu.table}</td>
			<td>${menu.waiter}</td>
			<td>${menu.time}</td>
			<td>${menu.menu}</td>
			<td>${menu.amount}</td>
			<td>${menu.remark}</td>
			<td><input type="button" value="<c:out value="${thCancel}" />" onclick="onCancelButtonClick(this);"></td>
		</tr>
		</c:forEach>
	</table>
</center>
