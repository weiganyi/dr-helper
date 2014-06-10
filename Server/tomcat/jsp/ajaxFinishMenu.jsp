<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<center>
	<table id="page_content_table">
		<tr>
			<th>${thOrder}</th>
			<th>${thTable}</th>
			<th>${thWaiter}</th>
			<th>${thTime}</th>
			<th>${thMenu}</th>
			<th>${thAmount}</th>
			<th>${thRemark}</th>
			<th>${thCancel}</th>
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
			<td><input type="button" value="${tdCancel}" onclick='onBtnCancelClick("order=${menu.order}&menu=${menu.menu}&page=${currPage}");'/></td>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onFinishMenuPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
		<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
		<a href="#" onclick='onFinishMenuPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onFinishMenuPageClick("page=${totalPage}");'>>></a>
</div>
