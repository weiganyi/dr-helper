<%-- this jsp without the script --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<c:import url="lang/ch.jsp" />

<center>
	<table id="menu_table">
		<tr>
			<th><c:out value="${thFetch}" /></th>
			<th><c:out value="${thOrder}" /></th>
			<th><c:out value="${thTable}" /></th>
			<th><c:out value="${thWaiter}" /></th>
			<th><c:out value="${thTime}" /></th>
			<th><c:out value="${thMenu}" /></th>
			<th><c:out value="${thAmount}" /></th>
			<th><c:out value="${thRemark}" /></th>
			<th><c:out value="${thFinish}" /></th>
		</tr>
		<c:forEach var="menu" items="${orderMenu}">
		<tr>
			<c:if test="${menu.fetch == false}">
				<td><input type="button" value="<c:out value="${tdFetch}" />" onclick='onBtnFetchClick("order=${menu.order}&menu=${menu.menu}&page=<c:out value="${currPage}" />");'></td>
			</c:if>
			<c:if test="${menu.fetch == true}">
				<td><input type="button" value="${menu.chef}" onclick='onBtnFetchClick("order=${menu.order}&menu=${menu.menu}&page=<c:out value="${currPage}" />");'></td>
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
				<td><input type="button" value="<c:out value="${tdFinish}" />" onclick='onBtnFinishClick("order=${menu.order}&menu=${menu.menu}&page=<c:out value="${currPage}" />");'></td>
			</c:if>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onOrderMenuPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span><c:out value="${idx}" /></span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onOrderMenuPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onOrderMenuPageClick("page=${totalPage}");'>>></a>
</div>
