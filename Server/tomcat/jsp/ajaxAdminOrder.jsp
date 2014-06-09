<%-- this jsp without the script --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<c:import url="lang/ch.jsp" />

<div id="search_order_div">
	<div>
		<span><c:out value="${adminOrderFromOrder}" />:  </span>
		<input type="text" id="order_num_input" value='<c:out value="${orderNum}" />'</input>
		<span><c:out value="${adminOrderOR}" />:  </span>
		<input type="text" id="start_order_input" value='<c:out value="${startOrder}" />'></input>
		<span> - </span>
		<input type="text" id="end_order_input" value='<c:out value="${endOrder}" />'></input>
	</div>
	<div>
		<span><c:out value="${adminOrderFromTable}" />:  </span>
		<input type="text" id="table_num_input" value='<c:out value="${tableNum}" />'></input>
	</div>
	<div>
		<input type="button" id="order_search_input" value='<c:out value="${adminOrderSearch}" />' onclick='onBtnOrderSearchClick();'></input>
		<input type="button" id="order_clear_input" value='<c:out value="${adminOrderClear}" />' onclick='onBtnOrderClearClick();'></input>
	</div>
</div>

<center>
	<table id="menu_table">
		<tr>
			<th><c:out value="${thOrder}" /></th>
			<th><c:out value="${thTable}" /></th>
			<th><c:out value="${thWaiter}" /></th>
			<th><c:out value="${thTime}" /></th>
			<th><c:out value="${thAdmin}" /></th>
			<th><c:out value="${thPay}" /></th>
			<th><c:out value="${thMenu}" /></th>
			<th><c:out value="${thPrice}" /></th>
			<th><c:out value="${thAmount}" /></th>
			<th><c:out value="${thChef}" /></th>
			<th><c:out value="${thFinish}" /></th>
			<th><c:out value="${thRemark}" /></th>
			<th><c:out value="${thPay}" /></th>
		</tr>
		<c:forEach var="item" items="${adminOrder}">
			<tr>
			<c:if test="${item.detailNum != 0}">
				<c:if test="${item.newOrder == true}">
					<td rowspan="${item.detailNum}">${item.order}</td>
					<td rowspan="${item.detailNum}">${item.table}</td>
					<td rowspan="${item.detailNum}">${item.waiter}</td>
					<td rowspan="${item.detailNum}">${item.time}</td>
					<td rowspan="${item.detailNum}">${item.admin}</td>
					<td rowspan="${item.detailNum}">${item.pay}</td>
				</c:if>
				<c:if test="${item.newOrder == false}">
					<td rowspan="${item.detailNum}"></td>
					<td rowspan="${item.detailNum}"></td>
					<td rowspan="${item.detailNum}"></td>
					<td rowspan="${item.detailNum}"></td>
					<td rowspan="${item.detailNum}"></td>
					<td rowspan="${item.detailNum}"></td>
				</c:if>
			</c:if>
					<td>${item.menu}</td>
					<td>${item.price}</td>
					<td>${item.amount}</td>
					<td>${item.chef}</td>
					<td>${item.finish}</td>
					<td>${item.remark}</td>
			<c:if test="${item.detailNum != 0}"> 
				<c:if test="${item.newOrder == true}">  
					<td rowspan="${item.detailNum}">
						<input type="button" value="<c:out value="${tdPay}" />" onclick='onBtnPayClick("payOrder=${item.order}&page=<c:out value="${currPage}" />");'>
					</td>
				</c:if>
				<c:if test="${item.newOrder == false}">
					<td rowspan="${item.detailNum}"></td>
				</c:if>
			</c:if>
			</tr>
		</c:forEach>
	</table>
</center>

<div id="page_link_div">
	<a href="#" onclick='onAdminOrderPageClick("page=1");'><<</a>
	<c:forEach var="idx" begin="${startPage}" end="${endPage}">
		<c:if test="${idx == currPage}">
			<span><c:out value="${idx}" /></span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onAdminOrderPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onAdminOrderPageClick("page=${totalPage}");'>>></a>
</div>
