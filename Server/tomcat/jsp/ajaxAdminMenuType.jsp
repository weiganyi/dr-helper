<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<div id="page_op_div">
	<div>
		<input type="hidden" id="menu_type_id_input"/>
		<span>${adminMenuTypeName}:  </span>
		<input type="text" id="menu_type_name_input"/>
	</div>
	<div>
		<input type="button" value="${adminMenuTypeCommit}" onclick='onBtnMenuTypeCommitClick("${adminMenuTypeNull}");'/>
		<input type="button" value="${adminMenuTypeClear}" onclick="onBtnMenuTypeClearClick();"/>
		<input type="button" value="${adminMenuTypeDelete}" onclick='onBtnMenuTypeDeleteClick("${adminMenuTypeNull}");'/>
	</div>
</div>

<center>
	<table id="page_content_table">
		<tr>
			<th>${thMenuTypeName}</th>
			<th>${thEdit}</th>
		</tr>
		<c:forEach var="item" items="${menuType}">
		<tr>
			<td>${item.menu_type_name}</td>
			<td><a href="#" onclick='onAdminMenuTypeEditClick("${item.menu_type_id}", "${item.menu_type_name}");'>${tdEdit}</a></td>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onAdminMenuTypePageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onAdminMenuTypePageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onAdminMenuTypePageClick("page=${totalPage}");'>>></a>
</div>
