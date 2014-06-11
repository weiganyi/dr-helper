<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<div id="page_op_div">
	<div>
		<input type="hidden" id="user_id_input"/>
		<span>${adminUserName}:  </span>
		<input type="text" id="user_name_input"/>
		<span>${adminUserPasswd}:  </span>
		<input type="text" id="user_passwd_input"/>
		<span>${adminUserAuth}:  </span>
		<select id="user_auth_select">
			<option value="waiter" selected="selected">${adminUserWaiter}</option>
			<option value="chef">${adminUserChef}</option>
			<option value="admin">${adminUserAdmin}</option>
		</select>
	</div>
	<div>
		<input type="button" value="${adminUserCommit}" onclick='onBtnUserCommitClick("${adminUserNull}");'/>
		<input type="button" value="${adminUserClear}" onclick="onBtnUserClearClick();"/>
		<input type="button" value="${adminUserDelete}" onclick='onBtnUserDeleteClick("${adminUserNull}");'/>
	</div>
</div>

<center>
	<table id="page_content_table">
		<tr>
			<th>${thUserName}</th>
			<th>${thUserPasswd}</th>
			<th>${thUserAuth}</th>
			<th>${thEdit}</th>
		</tr>
		<c:forEach var="item" items="${user}">
		<tr>
			<td>${item.user_name}</td>
			<td>${item.user_passwd}</td>
			<td>${item.user_auth}</td>
			<td><a href="#" onclick='onAdminUserEditClick("${item.user_id}", "${item.user_name}", "${item.user_passwd}", "${item.user_auth}");'>${tdEdit}</a></td>
		</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onAdminUserPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onAdminUserPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onAdminUserPageClick("page=${totalPage}");'>>></a>
</div>
