<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<div id="page_op_div">
	<div>
		<input type="hidden" id="menu_id_input"/>
		<span>${adminMenuName}:  </span>
		<input type="text" id="menu_name_input"/>
		<span>${adminMenuPrice}:  </span>
		<input type="text" id="menu_price_input"/>
		<span>${adminMenuTypeName}:  </span>
		<select id="menu_type_select">
			<c:forEach var="item" items="${menuType}">
			<option value="${item.menu_type_id}">${item.menu_type_name}</option>
			</c:forEach>
		</select>
	</div>
	<div>
		<input type="button" value="${adminMenuCommit}" onclick='onBtnMenuCommitClick("${adminMenuNull}");'/>
		<input type="button" value="${adminMenuClear}" onclick="onBtnMenuClearClick();"/>
		<input type="button" value="${adminMenuDelete}" onclick='onBtnMenuDeleteClick("${adminMenuNull}");'/>
	</div>
</div>

<center>
	<table id="page_content_table">
		<tr>
			<th>${thMenuName}</th>
			<th>${thMenuPrice}</th>
			<th>${thMenuTypeName}</th>
			<th>${thEdit}</th>
		</tr>
		<c:forEach var="item" items="${adminMenu}">
		<tr>
			<td>${item.menu_name}</td>
			<td>${item.menu_price}</td>
			<td>${item.menu_type_name}</td>
			<td><a href="#" onclick='onAdminMenuEditClick("${item.menu_id}", "${item.menu_name}", "${item.menu_price}", "${item.menu_type_id}");'>${tdEdit}</a></td>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onAdminMenuPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onAdminMenuPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onAdminMenuPageClick("page=${totalPage}");'>>></a>
</div>
