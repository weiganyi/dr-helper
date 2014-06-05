<%-- this jsp without the script --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<c:import url="lang/ch.jsp" />

<center>
	<table id="order_menu_table">
		<tr>
			<th><c:out value="${thFetch}" /></th>
			<th><c:out value="${thOrder}" /></th>
			<th><c:out value="${thTable}" /></th>
			<th><c:out value="${thWaiter}" /></th>
			<th><c:out value="${thTime}" /></th>
			<th><c:out value="${thMenu}" /></th>
			<th><c:out value="${thAccount}" /></th>
			<th><c:out value="${thRemark}" /></th>
			<th><c:out value="${thFinish}" /></th>
		</tr>
		<c:forEach var="menu" items="${orderMenu}">
		<tr>
			<c:if test="${menu.fetch == false}">
			<td><input type="button" value="<c:out value="${thFetch}" />" onclick="onFetchButtonClick(this);"></td>
			</c:if>
			<c:if test="${menu.fetch == true}">
			<td><input type="button" value="${menu.chef}" onclick="onFetchButtonClick(this);"></td>
			</c:if>
			<td>${menu.order}</td>
			<td>${menu.table}</td>
			<td>${menu.waiter}</td>
			<td>${menu.time}</td>
			<td>${menu.menu}</td>
			<td>${menu.amount}</td>
			<td>${menu.remark}</td>
			<c:if test="${menu.fetch == false}">
			<td></td>
			</c:if>
			<c:if test="${menu.fetch == true}">
			<td><input type="button" value="<c:out value="${thFinish}" />" onclick="onFinishButtonClick(this);"></td>
			</c:if>
		</tr>
		</c:forEach>
	</table>
</center>
