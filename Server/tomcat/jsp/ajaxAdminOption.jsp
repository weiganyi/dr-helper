<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<center>
	<table id="page_content_table">
		<tr>
			<td>${tdWebName}</td>
			<td><input type="text" id="web_name_input" value="${webName}"/></td>
		</tr>
		<tr>
			<td>${tdItemPerPage}</td>
			<td><input type="text" id="item_per_page_input" value="${itemPerPage}"/></td>
		</tr>
	</table>
	<div>
		<input type="button" value="${adminOptionCommit}" onclick='onBtnOptionCommitClick("${adminOptionNull}");'/>
	</div>
</center>
