/* index.jsp */
function fnHeaderFill(data, code, request) {
	if (code == "success") {
		var header = $(header_div);
		header.html(data);
		
		var content = $(content_div);
		content.html("");
	}
}

function onBtnLoginClick(log) {
	var loginUser = $(login_user_input).val();
	var loginPasswd = $(login_passwd_input).val();

	//check the input
	if (loginUser == "" || loginPasswd == "") {
		alert(log);
		return;
	}

	//start ajax to login
	var ajaxUrl = "ajaxLogin.do";
	var ajaxData = "loginUser=" + loginUser + "&" + "loginPasswd="
			+ loginPasswd;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnHeaderFill
	});
}

function onBtnLogoutClick() {
	//start ajax to logout
	var ajaxUrl = "ajaxLogout.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnHeaderFill
	});
}

function fnContentFill(data, code, request) {
	var content = $(content_div);
	content.html("");

	if (code == "success") {
		content.html(data);
	}
}

function onMenuOrderMenuClick() {
	//start ajax to fetch the order menu list
	var ajaxUrl = "ajaxOrderMenu.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuFinishMenuClick() {
	//start ajax to fetch the order menu list
	var ajaxUrl = "ajaxFinishMenu.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuAdminOrderClick() {
	//start ajax to fetch the order list
	var ajaxUrl = "ajaxAdminOrder.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuAdminUserClick() {
}

function onMenuAdminTableClick() {
}

function onMenuAdminMenuClick() {
}

function onMenuAdminOptionClick() {
}

/* ajaxOrderMenu.jsp */
function onBtnFetchClick(param) {
	//start ajax to update the order menu
	var ajaxUrl = "ajaxOrderMenu.do";
	var ajaxData = "op=fetch" + "&" + param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnContentFill
	});
}

function onBtnFinishClick(param) {
	//start ajax to update the order menu
	var ajaxUrl = "ajaxOrderMenu.do";
	var ajaxData = "op=finish" + "&" + param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnContentFill
	});
}

function onOrderMenuPageClick(page) {
	//start ajax to get the order menu
	var ajaxUrl = "ajaxOrderMenu.do" + "?" + page;
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

/* ajaxFinishMenu.jsp */
function onBtnCancelClick(param) {
	//start ajax to update the finish menu
	var ajaxUrl = "ajaxFinishMenu.do";
	var ajaxData = "op=cancel" + "&" + param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnContentFill
	});
}

function onFinishMenuPageClick(page) {
	//start ajax to get the finish menu
	var ajaxUrl = "ajaxFinishMenu.do" + "?" + page;
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

/* ajaxAdminOrder.jsp */
function fnBtnOrderSearch(param) {
	var order = $(order_num_input).val();
	var startOrder = $(start_order_input).val();
	var endOrder = $(end_order_input).val();
	var table = $(table_num_input).val();

	//start ajax to fetch the order list
	var ajaxUrl = "ajaxAdminOrder.do";
	var ajaxData = null;
	var firstParam = true;
	if (order != "") {
		if (firstParam == true) {
			ajaxData = "order=" + order;
			firstParam = false;
		}else {
			ajaxData = ajaxData + "&order=" + order;
		}
	}
	if (startOrder != "") {
		if (firstParam == true) {
			ajaxData = "startOrder=" + startOrder;
			firstParam = false;
		}else {
			ajaxData = ajaxData + "&startOrder=" + startOrder;
		}
	}
	if (endOrder != "") {
		if (firstParam == true) {
			ajaxData = "endOrder=" + endOrder;
			firstParam = false;
		}else {
			ajaxData = ajaxData + "&endOrder=" + endOrder;
		}
	}
	if (table != "") {
		if (firstParam == true) {
			ajaxData = "table=" + table;
			firstParam = false;
		}else {
			ajaxData = ajaxData + "&table=" + table;
		}
	}
	if (param != null) {
		if (firstParam == true) {
			ajaxData = param;
			firstParam = false;
		}else {
			ajaxData = ajaxData + "&" + param;
		}
	}
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnContentFill
	});
}

function onBtnOrderSearchClick() {
	fnBtnOrderSearch(null);
}

function onBtnOrderClearClick() {
	$(order_num_input).val("");
	$(start_order_input).val("");
	$(end_order_input).val("");
	$(table_num_input).val("");

	$(menu_table).html("");
	$(page_link_div).html("");
}

function onBtnPayClick(param) {
	//start ajax to update admin order
	var ajaxData = "op=pay" + "&" + param;
	fnBtnOrderSearch(ajaxData);
}

function onAdminOrderPageClick(page) {
	//start ajax to get the admin order
	fnBtnOrderSearch(page);
}
