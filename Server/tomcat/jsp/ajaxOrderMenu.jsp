<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<center>
	<table id="page_content_table">
		<tr>
			<th>${thFetch}</th>
			<th>${thOrder}</th>
			<th>${thTable}</th>
			<th>${thWaiter}</th>
			<th>${thTime}</th>
			<th>${thMenu}</th>
			<th>${thAmount}</th>
			<th>${thRemark}</th>
			<th>${thFinish}</th>
		</tr>
		<c:forEach var="menu" items="${orderMenu}">
		<tr>
			<c:if test="${menu.fetch == false}">
			<td><input type="button" value="${tdFetch}" onclick='onBtnFetchClick("order=${menu.order}&menu=${menu.menu}&page=${currPage}");'/></td>
			</c:if>
			<c:if test="${menu.fetch == true}">
			<td><input type="button" value="${menu.chef}" onclick='onBtnFetchClick("order=${menu.order}&menu=${menu.menu}&page=${currPage}");'/></td>
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
			<td><input type="button" value="${tdFinish}" onclick='onBtnFinishClick("order=${menu.order}&menu=${menu.menu}&page=${currPage}");'/></td>
			</c:if>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onOrderMenuPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
		<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
		<a href="#" onclick='onOrderMenuPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onOrderMenuPageClick("page=${totalPage}");'>>></a>
</div>
