var ValidatePay = ValidatePay ? ValidatePay : {};

// 订单预处理，显示微信支付二维码
ValidatePay.showWechatOrderInfo = function() {
	var reqData = ValidatePay.checkReqData();
	if (reqData != null) {
		$
				.ajax({
					url : "/application/pay/ajaxpre",
					type : "post",
					dataType : "json",
					data : reqData,
					success : function(data) {
						if (data.result == "success"
								&& !SieBaseUtils.checkIsNull(data.codeUrl)
								&& !SieBaseUtils.checkIsNull(data.orderNum)) {
							$("#orderNum").val(data.orderNum);
							$("#wechat_order_create_time").text(data.curDate); // ROL-6835
							$("#wechat_order_NO").text(data.orderNum);
							$("#wechat_order_background_cover").attr("style",
									"display:block;");
							$("#wechat_pay_success").attr("style",
									"display:none;");
							$("#wechat_order_info").attr("style",
									"display:flex;");
							var payele = $("#wechat_order_container")[0];
							payele.style.left = (window.innerWidth - payele.offsetWidth)
									/ 2 + "px";
							payele.style.top = (window.innerHeight - payele.offsetHeight)
									/ 2 + "px";
							// 生成支付二维码
							var options = {
								render : "canvas",
								ecLevel : 'H',// 识别度
								fill : '#000',// 二维码颜色
								background : '#ffffff',// 背景颜色
								quiet : 2,// 边距
								size : 215,
								text : data.codeUrl,// 二维码内容
								label : 'jQuery.qrcode',
								fontname : 'Ubuntu',
								fontcolor : '#ff9818',
							};
							$('#wechat_pay_qrcode').empty().qrcode(options);
							if (intervalId != null
									&& typeof (intervalId) != "undefined") {
								clearInterval(intervalId);
							}
							// 每4秒校验一次订单支付状态
							intervalId = setInterval(function() {
								ValidatePay.checkOrderStatus();
							}, 5000);
						} else {
							scmpublictoast("网络异常，请稍后再试", 2000);
						}
						$("#pay_submit_btn").attr("onclick",
								"ValidatePay.payNow();");
					},
					error : function() {
						scmpublictoast("网络异常，请稍后再试", 2000);
						$("#pay_submit_btn").attr("onclick",
								"ValidatePay.payNow();");
					}
				});
	} else {
		$("#pay_submit_btn").attr("onclick", "ValidatePay.payNow();");
	}
}

var checkOrderReq;
// 校验订单支付状态
ValidatePay.checkOrderStatus = function() {
	checkOrderReq = $.ajax({
		url : "/application/pay/ajaxcheck",
		type : "post",
		dataType : "json",
		data : {
			"orderNum" : $.trim($("#orderNum").val())
		},
		success : function(data) {
			if (data.result == "paied") {
				if (intervalId != null && typeof (intervalId) != "undefined") {
					clearInterval(intervalId);
				}
				if ($("#wechat_order_background_cover").is(":visible")) {
					$("#wechat_pay_success").attr("style", "");
					$("#wechat_order_info").attr("style", "display:none;");
				}
				if ($("#back_to_validate_time").text() == "5") {
					ValidatePay.backToValidateTime(5);
				}
			}
		},
		error : function() {
			scmpublictoast("网络异常，请稍后再试", 2000);
		}
	});
}

ValidatePay.goAliPay = function() {
	if (SieBaseUtils.checkIsNull($("#pay_grade").val())) {
		scmpublictoast("请先选择付费套餐类型", 2000);
		$("#pay_submit_btn").attr("onclick", "ValidatePay.payNow();");
		return false;
	}
	if (intervalId != null && typeof (intervalId) != "undefined") {
		clearInterval(intervalId);
	}
	intervalId = setInterval(function() {
		ValidatePay.checkOrderStatus();
	}, 5000);
	$("#ali_pay_form").submit();
}

ValidatePay.closeWeChatOrder = function() {
	$("#wechat_order_background_cover").hide();
}

ValidatePay.backToValidateList = function() {
	window.location.href = "/application/validate/toadd";
}

ValidatePay.changeNeedInvoices = function() {
	if ($("#need_Invoices").is(':checked')) {
		$("#need_Invoices").val(1);
		$("#validate_pay_invoices_info_div").show();
		$("#invoices_type").val("2");
		changeInvoicesType('normal', $("#normal_invoices_div_title"));
	} else {
		$("#need_Invoices").val(0);
		$("#validate_pay_invoices_info_div").hide();
		$("#invoices_type").val("");
		$(".invoices_input").removeClass("new-invoice_payitem-right_error");
		$(".invoices_input_msg").hide();
	}
}

ValidatePay.backToValidateTime = function(time) {
	setTimeout(function() {
		if (time > 0) {
			ValidatePay.backToValidateTime(--time);
			$("#back_to_validate_time").text(time);
		} else {
			window.location.href = "/application/validate/toadd";
		}
	}, 1000);
}

ValidatePay.changeSelectedGradeInfo = function(payGrade) {
	var currentDate = new Date();
	var currentYear = parseInt(currentDate.getFullYear());
	var currentMonth = parseInt(currentDate.getMonth()) + 1;
	var currentDay = currentDate.getDate();
	var gradeAEndYear = parseInt(currentDate.getFullYear()) + 1;
	var gradeBEndYear = parseInt(currentDate.getFullYear()) + 2;
	var gradeCEndYear = currentYear;
	if (payGrade == "A") {
		$("#vip_end_date").text(
				gradeAEndYear + "-" + currentMonth + "-" + currentDay);
		$("#wechat_order_price").text("998");
		$("#wechat_order_name").text("科研之友-升级VIP账号12个月");
		$("#wechat_order_desc").text("科研之友-升级VIP账号12个月");
	} else if (payGrade == "B") {
		$("#vip_end_date").text(
				gradeBEndYear + "-" + currentMonth + "-" + currentDay);
		$("#wechat_order_price").text("1800");
		$("#wechat_order_name").text("科研之友-升级VIP账号24个月");
		$("#wechat_order_desc").text("科研之友-升级VIP账号24个月");
	} else {
		if (++currentMonth > 12) {
			currentMonth = 1;
			++gradeCEndYear;
		}
		$("#vip_end_date").text(
				gradeCEndYear + "-" + currentMonth + "-" + currentDay);
		$("#wechat_order_price").text("98");
		$("#wechat_order_name").text("科研之友-升级VIP账号1个月");
		$("#wechat_order_desc").text("科研之友-升级VIP账号1个月");
	}
}

ValidatePay.payNow = function() {

	$.ajax({
		url : "/application/validate/ajaxcheckupload",
		type : "post",
		dataType : "json",
		data : "",
		async : false,
		success : function(data) {
			if (data.ajaxSessionTimeOut == "yes") {
				ScmLoginBox.showLoginToast();
			} else { // 没有超时则进入正常的业务判断
				$("#pay_submit_btn").removeAttr("onclick");
				var tradeType = $("#selected_trade_type").val();
				var reqData = ValidatePay.checkReqData();
				if (reqData != null) {
					if (tradeType == "wechat") {
						ValidatePay.showWechatOrderInfo();
					} else {
						ValidatePay.goAliPay();
					}
				} else {
					$("#pay_submit_btn").attr("onclick",
							"ValidatePay.payNow();");
				}
			}
		},
		error : function(data) {
			scmpublictoast('系统出现异常，请稍后再试！', 1000);
		}
	});
}

ValidatePay.changeTradeType = function(tradeType, obj) {
	$(".validate_trade_type").removeClass(
			"new-paymentpage_Accountappy-item_border");
	$(obj).addClass("new-paymentpage_Accountappy-item_border");
	if (tradeType == "wechat") {
		$("#selected_trade_type").val("wechat");
	} else {
		$("#selected_trade_type").val("alipay");
	}
}

ValidatePay.checkReqData = function() {
	var selectedGrade = $.trim($("#pay_grade").val());
	var needInvoices = $.trim($("#need_Invoices").val());
	var title = $.trim($("#invoices_title").val());
	var uniformId = $.trim($("#invoices_uniform_id").val());
	var bank = $.trim($("#invoices_bank").val());
	var bankNO = $.trim($("#invoices_bankNO").val());
	var addr = $.trim($("#invoices_addr").val());
	var tel = $.trim($("#invoices_tel").val());
	var invoicesType = $.trim($("#invoices_type").val());
	var tradeType = $("#selected_trade_type").val();
	var checkResult = true;
	if (SieBaseUtils.checkIsNull(selectedGrade)) {
		scmpublictoast("请先选择付费套餐类型", 2000);
		checkResult = false;
	}
	if (SieBaseUtils.checkIsNull(tradeType)) {
		scmpublictoast("请先选择付费方式", 2000);
		checkResult = false;
	}
	if (needInvoices == "1") {
		if (SieBaseUtils.checkIsNull(title)) {
			$("#invoices_title_error_msg").show();
			$("#invoices_title").addClass("new-invoice_payitem-right_error");
			checkResult = false;
		}
		if (SieBaseUtils.checkIsNull(uniformId)) {
			$("#invoices_uniform_id_error_msg").show();
			$("#invoices_uniform_id").addClass(
					"new-invoice_payitem-right_error");
			checkResult = false;
		}
		if (invoicesType == 2) {
			if (SieBaseUtils.checkIsNull(bank)) {
				$("#invoices_bank_error_msg").show();
				$("#invoices_bank").addClass("new-invoice_payitem-right_error");
				checkResult = false;
			}
			if (SieBaseUtils.checkIsNull(bankNO)) {
				$("#invoices_bankNO_error_msg").show();
				$("#invoices_bankNO").addClass(
						"new-invoice_payitem-right_error");
				checkResult = false;
			}
			if (SieBaseUtils.checkIsNull(addr)) {
				$("#invoices_addr_error_msg").show();
				$("#invoices_addr").addClass("new-invoice_payitem-right_error");
				checkResult = false;
			}
			if (SieBaseUtils.checkIsNull(tel)) {
				$("#invoices_tel_error_msg").show();
				$("#invoices_tel").addClass("new-invoice_payitem-right_error");
				checkResult = false;
			}
		}
	}
	var reqData = {
		"tradeType" : tradeType,
		"grade" : selectedGrade,
		"needInvoices" : needInvoices,
		"title" : title,
		"uniformId" : uniformId,
		"bank" : bank,
		"bankNO" : bankNO,
		"addr" : addr,
		"tel" : tel,
		"invoicesType" : invoicesType
	}
	return checkResult ? reqData : null;
}

ValidatePay.checkInvoicesInfo = function(name) {
	var inputId = "#invoices_" + name;
	var inputVal = $(inputId).val();
	var errorMsgId = inputId + "_error_msg";
	if (SieBaseUtils.checkIsNull(inputVal)) {
		$(errorMsgId).show();
		$(inputId).addClass("new-invoice_payitem-right_error");
	} else {
		$(errorMsgId).hide();
		$(inputId).removeClass("new-invoice_payitem-right_error");
	}
}

ValidatePay.back = function() {
	$.ajax({
		url : "/application/validate/ajaxcheckupload",
		type : "post",
		dataType : "json",
		data : "",
		async : false,
		success : function(data) {
			if (data.ajaxSessionTimeOut == "yes") {
				ScmLoginBox.showLoginToast();
			} else { // 没有超时则正常返回
				window.location.href = "/application/validate/maint";
			}
		},
		error : function(data) {
			scmpublictoast('系统出现异常，请稍后再试！', 1000);
		}
	});
}
