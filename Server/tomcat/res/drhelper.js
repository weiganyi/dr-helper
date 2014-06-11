/* index.jsp */
function fnHeaderFill(data, code, request) {
	if (code == "success") {
		var header = $(header_div);
		header.html(data);
		
		var content = $(content_div);
		content.html("");
	}
}

function fnContentFill(data, code, request) {
	var content = $(content_div);
	content.html(data);

	if (code == "success") {
		content.html(data);
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

function onMenuOrderMenuClick() {
	//start ajax to get the order menu list
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
	//start ajax to get the order menu list
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
	//start ajax to get the order list
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
	//start ajax to get the user list
	var ajaxUrl = "ajaxAdminUser.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuAdminTableClick() {
	//start ajax to get the table list
	var ajaxUrl = "ajaxAdminTable.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuAdminMenuTypeClick() {
	//start ajax to get the menu type list
	var ajaxUrl = "ajaxAdminMenuType.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuAdminMenuClick() {
	//start ajax to get the menu list
	var ajaxUrl = "ajaxAdminMenu.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

function onMenuAdminOptionClick() {
	//start ajax to get the option list
	var ajaxUrl = "ajaxAdminOption.do";
	jQuery.ajax({
		type : "GET",
		url : ajaxUrl,
		data : null,
		dataType : "html",
		success : fnContentFill
	});
}

/* ajaxOrderMenu.jsp */
function fnBtnOrderMenu(param) {
	//start ajax to access the order menu
	var ajaxUrl = "ajaxOrderMenu.do";
	var ajaxData = param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : param,
		dataType : "html",
		success : fnContentFill
	});

}

function onBtnFetchClick(param) {
	//start ajax to update the order menu
	var ajaxData = "op=fetch" + "&" + param;
	fnBtnOrderMenu(ajaxData)
}

function onBtnFinishClick(param) {
	//start ajax to update the order menu
	var ajaxData = "op=finish" + "&" + param;
	fnBtnOrderMenu(ajaxData)
}

function onOrderMenuPageClick(page) {
	//start ajax to get the order menu
	var ajaxData = page;
	fnBtnOrderMenu(ajaxData)
}

/* ajaxFinishMenu.jsp */
function fnBtnFinishMenu(param) {
	//start ajax to access the finish menu
	var ajaxUrl = "ajaxFinishMenu.do";
	var ajaxData = param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : param,
		dataType : "html",
		success : fnContentFill
	});

}

function onBtnCancelClick(param) {
	//start ajax to update the finish menu
	var ajaxData = "op=cancel" + "&" + param;
	fnBtnFinishMenu(ajaxData)
}

function onFinishMenuPageClick(page) {
	//start ajax to get the finish menu
	var ajaxData = page;
	fnBtnFinishMenu(ajaxData)
}

/* ajaxAdminOrder.jsp */
function fnBtnAdminOrder(param) {
	var order = $(order_num_input).val();
	var startOrder = $(start_order_input).val();
	var endOrder = $(end_order_input).val();
	var table = $(table_num_input).val();

	//start ajax to access the order list
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
	fnBtnAdminOrder(null);
}

function onBtnOrderClearClick() {
	$(order_num_input).val("");
	$(start_order_input).val("");
	$(end_order_input).val("");
	$(table_num_input).val("");

	$(page_content_table).html("");
	$(page_link_div).html("");
}

function onBtnOrderDeleteClick(log) {
	var order = $(order_num_input).val();
	var startOrder = $(start_order_input).val();
	var endOrder = $(end_order_input).val();
	var table = $(table_num_input).val();

	//check the input
	if (order == "" && startOrder == "" && endOrder == "" && table == "") {
		alert(log);
		return;
	}
	
	var param = "op=delete";
	fnBtnAdminOrder(param);
}

function onBtnPayClick(param) {
	//start ajax to update admin order
	var ajaxData = "op=pay" + "&" + param;
	fnBtnAdminOrder(ajaxData);
}

function onAdminOrderPageClick(page) {
	//start ajax to get the admin order
	fnBtnAdminOrder(page);
}

/* ajaxAdminUser.jsp */
function fnBtnAdminUser(param) {
	//start ajax to access the user list
	var ajaxUrl = "ajaxAdminUser.do";
	var ajaxData = param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnContentFill
	});
}

function onBtnUserCommitClick(log) {
	var id = $(user_id_input).val();
	var name = $(user_name_input).val();
	var passwd = $(user_passwd_input).val();
	var auth = $(user_auth_select).val();

	//check the input
	if (name == "" || passwd == "" || auth == "") {
		alert(log);
		return;
	}

	//start ajax to add user
	var ajaxData = "op=commit" + "&" + "id=" + id + "&" + "name=" + name + "&" + "passwd=" + passwd + "&" + "auth=" + auth;
	fnBtnAdminUser(ajaxData);
}

function onBtnUserClearClick() {
	$(user_id_input).val("");
	$(user_name_input).val("");
	$(user_passwd_input).val("");
	$(user_auth_select).val("waiter");

	$(page_content_table).html("");
	$(page_link_div).html("");
}

function onBtnUserDeleteClick(log) {
	var id = $(user_id_input).val();

	//check the input
	if (id == "") {
		alert(log);
		return;
	}

	//start ajax to delete user
	var ajaxData = "op=delete" + "&" + "id=" + id;
	fnBtnAdminUser(ajaxData);
}

function onAdminUserEditClick(user_id, user_name, user_passwd, user_auth) {
	$(user_id_input).val(user_id);
	$(user_name_input).val(user_name);
	$(user_passwd_input).val(user_passwd);
	$(user_auth_select).val(user_auth);
}

function onAdminUserPageClick(page) {
	//start ajax to fetch the user list
	var ajaxData = page;
	fnBtnAdminUser(ajaxData);
}

/* ajaxAdminTable.jsp */
function fnBtnAdminTable(param) {
	//start ajax to access the table list
	var ajaxUrl = "ajaxAdminTable.do";
	var ajaxData = param;
	jQuery.ajax({
		type : "POST",
		url : ajaxUrl,
		data : ajaxData,
		dataType : "html",
		success : fnContentFill
	});
}

function onBtnTableCommitClick(log) {
	var id = $(table_id_input).val();
	var table = $(table_num_input).val();
	var seat = $(table_seat_num_input).val();
	var empty = $(table_empty_select).val();

	//check the input
	if (table == "" || seat == "" || empty == "") {
		alert(log);
		return;
	}

	//start ajax to add table
	var ajaxData = "op=commit" + "&" + "id=" + id + "&" + "table=" + table + "&" + "seat=" + seat + "&" + "empty=" + empty;
	fnBtnAdminTable(ajaxData);
}


function onBtnTableClearClick() {
	$(table_id_input).val("");
	$(table_num_input).val("");
	$(table_seat_num_input).val("");
	$(table_empty_select).val("waiter");

	$(page_content_table).html("");
	$(page_link_div).html("");
}

function onBtnTableDeleteClick(log) {
	var id = $(table_id_input).val();

	//check the input
	if (id == "") {
		alert(log);
		return;
	}

	//start ajax to delete table
	var ajaxData = "op=delete" + "&" + "id=" + id;
	fnBtnAdminTable(ajaxData);
}

function onAdminTableEditClick(table_id, table_num, table_seat_num, table_empty) {
	$(table_id_input).val(table_id);
	$(table_num_input).val(table_num);
	$(table_seat_num_input).val(table_seat_num);
	$(table_empty_select).val(table_empty);
}

function onAdminTablePageClick(page) {
	//start ajax to fetch the table list
	var ajaxData = page;
	fnBtnAdminTable(ajaxData);
}
