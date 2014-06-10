<%-- this jsp without the script --%>

<%-- include the jstl --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- include the language array --%>
<jsp:include page="lang/ch.jsp" />

<div id="page_op_div">
	<div>
		<span>${adminOrderByOrder}:  </span>
		<input type="text" id="order_num_input" value="${orderNum}"/>
		<span>${adminOrderOR}:  </span>
		<input type="text" id="start_order_input" value="${startOrder}"/>
		<span> - </span>
		<input type="text" id="end_order_input" value="${endOrder}"/>
	</div>
	<div>
		<span>${adminOrderByTable}:  </span>
		<input type="text" id="table_num_input" value="${tableNum}"/>
	</div>
	<div>
		<input type="button" id="order_search_input" value="${adminOrderSearch}" onclick="onBtnOrderSearchClick();"/>
		<input type="button" id="order_clear_input" value="${adminOrderClear}" onclick="onBtnOrderClearClick();"/>
	</div>
</div>

<center>
	<table id="page_content_table">
		<tr>
			<th>${thOrder}</th>
			<th>${thTable}</th>
			<th>${thWaiter}</th>
			<th>${thTime}</th>
			<th>${thAdmin}</th>
			<th>${thPay}</th>
			<th>${thMenu}</th>
			<th>${thPrice}</th>
			<th>${thAmount}</th>
			<th>${thChef}</th>
			<th>${thFinish}</th>
			<th>${thRemark}</th>
			<th>${thPay}</th>
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
					<c:if test="${item.pay == true}">
					<td rowspan="${item.detailNum}">${tdYes}</td>
					</c:if>
					<c:if test="${item.pay == false}">
					<td rowspan="${item.detailNum}">${tdNo}</td>
					</c:if>
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
			<c:if test="${item.finish == true}">
			<td>${tdYes}</td>
			</c:if>
			<c:if test="${item.finish == false}">
			<td>${tdNo}</td>
			</c:if>
			<td>${item.remark}</td>
			<c:if test="${item.detailNum != 0}"> 
				<c:if test="${item.newOrder == true}">  
				<td rowspan="${item.detailNum}">
					<input type="button" value="${tdPay}" onclick='onBtnPayClick("payOrder=${item.order}&page=${currPage}");'/>
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
			<span>${idx} </span>
		</c:if>
		<c:if test="${idx != currPage}">
			<a href="#" onclick='onAdminOrderPageClick("page=${idx}");'>${idx} </a>
		</c:if>
	</c:forEach>
	<a href="#" onclick='onAdminOrderPageClick("page=${totalPage}");'>>></a>
</div>
