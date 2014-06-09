<%-- this jsp without the script --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<c:import url="lang/ch.jsp" />

<center>
	<table id="menu_table">
		<tr>
			<th><c:out value="${thOrder}" /></th>
			<th><c:out value="${thTable}" /></th>
			<th><c:out value="${thWaiter}" /></th>
			<th><c:out value="${thTime}" /></th>
			<th><c:out value="${thMenu}" /></th>
			<th><c:out value="${thAmount}" /></th>
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
			<td><input type="button" value="<c:out value="${tdCancel}" />" onclick='onBtnCancelClick("order=${menu.order}&menu=${menu.menu}&page=<c:out value="${currPage}" />");'></td>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onFinishMenuPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span><c:out value="${idx}" /></span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onFinishMenuPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onFinishMenuPageClick("page=${totalPage}");'>>></a>
</div>
