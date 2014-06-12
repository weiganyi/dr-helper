<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<div id="page_op_div">
	<div>
		<input type="hidden" id="table_id_input"/>
		<span>${adminTableNum}:  </span>
		<input type="text" id="table_num_input"/>
		<span>${adminTableSeatNum}:  </span>
		<input type="text" id="table_seat_num_input"/>
		<span>${adminTableEmpty}:  </span>
		<select id="table_empty_select">
			<option value="1" selected="selected">${tdYes}</option>
			<option value="0">${tdNo}</option>
		</select>
	</div>
	<div>
		<input type="button" value="${adminTableCommit}" onclick='onBtnTableCommitClick("${adminTableNull}");'/>
		<input type="button" value="${adminTableClear}" onclick="onBtnTableClearClick();"/>
		<input type="button" value="${adminTableDelete}" onclick='onBtnTableDeleteClick("${adminTableNull}");'/>
	</div>
</div>

<center>
	<table id="page_content_table">
		<tr>
			<th>${thTableNum}</th>
			<th>${thTableSeatNum}</th>
			<th>${thTableEmpty}</th>
			<th>${thEdit}</th>
		</tr>
		<c:forEach var="item" items="${table}">
		<tr>
			<td>${item.table_num}</td>
			<td>${item.table_seat_num}</td>
			<c:if test="${item.table_empty eq '1'}">
			<td>${tdYes}</td>
			</c:if>
			<c:if test="${item.table_empty eq '0'}">
			<td>${tdNo}</td>
			</c:if>
			<td><a href="#" onclick='onAdminTableEditClick("${item.table_id}", "${item.table_num}", "${item.table_seat_num}", "${item.table_empty}");'>${tdEdit}</a></td>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onAdminTablePageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onAdminTablePageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onAdminTablePageClick("page=${totalPage}");'>>></a>
</div>
