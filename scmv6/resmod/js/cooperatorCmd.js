/**
 * 合作者推荐.
 */
var CooperatorCmd = CooperatorCmd ? CooperatorCmd : {};

/** 开始推荐合作者. */

CooperatorCmd.recommendCooperator = function() {
	var title = $.trim($('#title_inp').val());
	var abs = $.trim($('#abs_area').val());
	if ((title == '' || title == title_watermark) && (abs == '' || abs == abs_watermark)) {
		$.scmtips.show('warn', formReq);
		return;
	}
	if (title != title_watermark) {
		$('#title_hidden').val($('#title_inp').val());
	}
	
	if (abs != abs_watermark) {
		$('#abs_hidden').val($('#abs_area').val());
	}
	$('#mainForm').submit();
};

/** 修改标题和摘要. */
CooperatorCmd.editTitleAndAbs = function() {
	$('#title_hidden').val($.trim($('#title_td').text()));
	$('#abs_hidden').val($.trim($('#abs_td').text()));
	//$('#mainForm').attr('action', ctxpath + urlType + "/cooperatorCmd/main?menuId=" + menuId).submit();
	$('#mainForm').attr('action', "/scmmanagement/cooperatorCmd/main").submit();
};

/** 修改标题和摘要. */
CooperatorCmd.editTitleAndAbsForKwAnalysis = function() {
	$('#title_hidden').val($.trim($('#title_td').text()));
	$('#abs_hidden').val($.trim($('#abs_td').text()));
	//$('#mainForm').attr('action', ctxpath + urlType + "/cooperatorCmd/main?menuId=" + menuId).submit();
	$('#mainForm').attr('action', "/scmmanagement/researchkws/main").submit();
};

/** 重新推荐合作者. */

//解决因自动填词插件延时引起的问题 by zk SCM-3293
CooperatorCmd.reCmdCooperatorEx= function(){
	setTimeout(CooperatorCmd.reCmdCooperator,200);
}

CooperatorCmd.reCmdCooperator = function() {
	CooperatorCmd.initKeywordsJson();
	$('#discId_hidden').val(0);
	$('#loadFlag_hidden').val(0);
	var pageSize = $('#pageSize').val();
	$("#con_one_page").html("");
	CooperatorCmd.loadCooperator(1, pageSize);
};

/** 加载合作者. */
CooperatorCmd.loadCooperator = function(p, pageSize) {
	// 获取关键词信息
	if ($("#keywordsJson_hidden").val() == "[]") {
		$.scmtips.show("warn", keywordReq);
		return;
	}

	$("#con_one_tip").show();
   $("#nextHtmlTip").show();
   
	$.ajax({
		type : "post",
		url : "/scmmanagement/cooperatorCmd/ajaxLoadCooperator",
		data : {
			'keywordJson' : $("#keywordsJson_hidden").val(),
			'discId' : $("#discId_hidden").val(),
			'loadFlag' : $('#loadFlag_hidden').val(),
			'page.pageNo' : parseInt(p),
			'page.pageSize' : pageSize
		},
		dataType : "html",
		success : function(data) {
			if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
				jConfirm(noLogin, reminder,
						function(r) {
							if (r) {
								document.location.href = "/scmmanagement/cooperatorCmd/main";
							}
						});
			} else {
				$("#con_one_tip").hide();
				$("#nextHtmlTip").hide();
				$("#con_one_page").html(data);
			}
		},
		error : function(e) {
			$.scmtips.show("error", networkExc);
		}
	});
};

CooperatorCmd.reloadPieChartEx= function(){
	setTimeout(CooperatorCmd.reloadPieChart,200);
}

CooperatorCmd.reloadPieChart = function() {
	CooperatorCmd.initKeywordsJson();
	$('#discId_hidden').val(0);
	$('#loadFlag_hidden').val(0);
	$("#con_one_page").html("");
	CooperatorCmd.loadPieChart();
};

/** 加载关键词分析饼图 */
CooperatorCmd.loadPieChart = function() {
	// 获取关键词信息
	if ($("#keywordsJson_hidden").val() == "[]") {
		$.scmtips.show("warn", keywordReq);
		return;
	}
	$("#con_one_tip").show();
   $("#nextHtmlTip").show();
   
	$.ajax({
		type : "post",
		url : "/scmmanagement/researchkws/ajaxpiechart",
		data : {
			'keywordJson' : $("#keywordsJson_hidden").val(),
			'discId' : $("#discId_hidden").val(),
			'loadFlag' : $('#loadFlag_hidden').val(),
			'page.pageNo' : 1,
			'page.pageSize' : 10
		},
		dataType : "html",
		success : function(data) {
			if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
				jConfirm(noLogin, reminder,
						function(r) {
							if (r) {
								document.location.href = "/scmmanagement/researchkws/main";
							}
						});
			} else {
				$("#con_one_tip").hide();
				$("#nextHtmlTip").hide();
				$("#con_one_page").html(data);
				//piechart("piechart", data);
			}
		},
		error : function(e) {
			$.scmtips.show("error", networkExc);
		}
	});
};

CooperatorCmd.reloadLineChartEx= function(){
	setTimeout(CooperatorCmd.reloadLineChart,200);
}

CooperatorCmd.reloadLineChart = function() {
	CooperatorCmd.initKeywordsJson();
	$('#discId_hidden').val(0);
	$('#loadFlag_hidden').val(0);
	$("#con_one_page").html("");
	CooperatorCmd.loadLineChart();
};

/** 加载关键词分析趋势图 */
CooperatorCmd.loadLineChart = function() {
	// 获取关键词信息
	if ($("#keywordsJson_hidden").val() == "[]") {
		$.scmtips.show("warn", keywordReq);
		return;
	}

	$("#con_one_tip").show();
   $("#nextHtmlTip").show();
   
	$.ajax({
		type : "post",
		url : "/scmmanagement/researchkws/ajaxlinechart",
		data : {
			'keywordJson' : $("#keywordsJson_hidden").val(),
			'discId' : $("#discId_hidden").val(),
			'loadFlag' : $('#loadFlag_hidden').val(),
			'page.pageNo' : 1,
			'page.pageSize' : 10
		},
		dataType : "html",
		success : function(data) {
			if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
				jConfirm(noLogin, reminder,
						function(r) {
							if (r) {
								document.location.href = "/scmmanagement/researchkws/main";
							}
						});
			} else {
				$("#con_one_tip").hide();
				$("#nextHtmlTip").hide();
				$("#con_one_page").html(data);
			}
		},
		error : function(e) {
			$.scmtips.show("error", networkExc);
		}
	});
};


/** 加载研究领域. */
CooperatorCmd.loadDisc = function() {
	var loadFlag = $("#loadFlag_hidden").val();
	if (loadFlag != 2) {
		$("#disc_tip").show();
		$("#disc_div").html("");
		$.ajax({
			type : "post",
			url : ctxpath + urlType + "/cooperatorCmd/ajaxLoadDisc",
			data : {
				"loadFlag" : loadFlag,
				"keywordJson" : $("#keywordsJson_hidden").val()
			},
			dataType : "html",
			success : function(data) {
				$("#disc_tip").hide();
				$("#disc_div").html(data);
				$("#loadFlag_hidden").val(2);
			},
			error : function(e) {
				$.scmtips.show("error", networkExc);
			}
		});
	}
};

/** 切换研究领域加载合作者. */
CooperatorCmd.switchDisLoadCooperator = function(discId) {
	var oldDiscId = $("#discId_hidden").val();
	if (oldDiscId != discId) {
		// 切换选中项
		$("#expert_disclist" + oldDiscId).removeClass();
		// 收起子项
		$("#expert_disclist" + oldDiscId).removeClass();
		if (oldDiscId != 0 && oldDiscId != -1) {
			if ($("#expert_disclist" + oldDiscId).parent().find("dd").length != 0) {// 父级
				$("#expert_disclist" + oldDiscId).parent().find("dl").hide();
				$("#expert_disclist" + oldDiscId).find(".Shear-headbottom")
						.attr("class", "Shear-head");
			} else {// 子级
				$("#expert_disclist" + oldDiscId).parent().parent().hide();
				$("#expert_disclist" + oldDiscId).parent().parent().parent()
						.find(".Shear-headbottom").attr("class", "Shear-head");
			}
		}

		$("#expert_disclist" + discId).attr("class", "leftnav-hover");
		// 展开子项
		if ($("#expert_disclist" + discId).parent().find("dd").length != 0) {// 父级
			$("#expert_disclist" + discId).find(".Shear-head").attr("class",
					"Shear-headbottom");
			$("#expert_disclist" + discId).parent().find("dl").show();
		} else {// 子级
			$("#expert_disclist" + discId).parent().parent().show();
			$("#expert_disclist" + discId).parent().parent().parent().find(
					".Shear-headbottom").attr("class", "Shear-head");
		}

		$("#discId_hidden").val(discId);
		var pageSize = $('#pageSize').val();
		CooperatorCmd.loadCooperator(1, pageSize);
	}
};

/** 构造keywordsJson. */
CooperatorCmd.initKeywordsJson = function() {
	var keyWordList = autoword.words();
	var keyWordArray = [];
	for ( var i = 0, length = keyWordList.length; i < length; i++) {
		keyWordArray.push({
			"id" : (keyWordList[i].val == null) ? "" : keyWordList[i].val,
			"keyword" : keyWordList[i].text
		});
	}
	$("#keywordsJson_hidden").val(JSON.stringify(keyWordArray));
};

/** 隐藏检索条件. */
CooperatorCmd.hideSearch = function(obj) {
	$(obj).hide();
	$("#search_showlink").show();
	$("#peer_table").hide();
};

/** 显示检索条件. */
CooperatorCmd.showSearch = function(obj) {
	$(obj).hide();
	$("#search_hidelink").show();
	$("#peer_table").show();
};

/** 显示其他页码数据. */
function loadOtherPage(p) {
	var pageSize = $('#pageSize').val();
	CooperatorCmd.loadCooperator(p, pageSize);
};

/** 跳转到哪一页. */
function topage() {
	var pageSize = $('#pageSize').val();
	var toPage = $.trim($("#toPage").val());
	if (!/^\d+$/g.test(toPage))
		toPage = 1;
	CooperatorCmd.loadCooperator(toPage, pageSize);
};

function loadMore() {
	var pageSize = $('#pageSize').val();
	CooperatorCmd.loadCooperator($('#pageNo').val(), pageSize);
};

/** 加为联系人的回调函数. */
function add_friendReq_callBack(result, psnId) {
	if (result) {
		$.scmtips.show('success', sendFriendSuccess, locale != 'zh_CN' ? 360 : 300);
		$("#td_" + psnId).html(
				"<label class='f999'>" + sendFriendLabel + "</label>");
	} else {
		$.scmtips.show('error', sendFriendError);
	}
};
/**
 * 跳转到基金申请页面
 */
CooperatorCmd.reCmdFundEx= function(){
	setTimeout(CooperatorCmd.recFund,200);
};

CooperatorCmd.recFund=function(){
	/*var keyWordList = autoword.words();
	var keyWordArray = [];
	for ( var i = 0, length = keyWordList.length; i < length; i++) {
		keyWordArray.push(keyWordList[i].text);
	}
	var keywordStr=keyWordArray.join("#iris#");
	if (keywordStr == "") {
		$.scmtips.show("warn", keywordReq);
		return;
	}*/
	var keywordStr=autoword.texts().join(",");
	if (keywordStr == "") {
		$.scmtips.show("warn", keywordReq);
		return;
	}
	$("#fund_title").val($.trim($('#title_td').text()));
	$('#fund_digest').val($.trim($('#abs_td').text()));
	$('#fund_keywordStr').val(keywordStr);
	$("#fundForm").submit();
};
/**
 * 跳转到基金申请页面
 */
CooperatorCmd.reCmdJournalEx= function(){
	setTimeout(CooperatorCmd.recJournal,200);
};

CooperatorCmd.recJournal=function(){
	/*var keyWordList = autoword.words();
	var keyWordArray = [];
	for ( var i = 0, length = keyWordList.length; i < length; i++) {
		keyWordArray.push(keyWordList[i].text);
	}
	var keywordStr=keyWordArray.join("#iris#");
	if (keywordStr == "") {
		$.scmtips.show("warn", keywordReq);
		return;
	}*/
	var keywordStr=autoword.texts().join(",");
	if (keywordStr == "") {
		$.scmtips.show("warn", keywordReq);
		return;
	}
	$("#jnl_title").val($.trim($('#title_td').text()));
	$('#jnl_digest').val($.trim($('#abs_td').text()));
	$('#jnl_keywordStr').val(keywordStr);
	$("#jnlForm").submit();
};
/**
 * 跳转到相关文献页面.
 */
CooperatorCmd.reCmdRefEx= function(){
	setTimeout(CooperatorCmd.recRef,200);
};

CooperatorCmd.recRef=function(){
	/*var keyWordList = autoword.words();
	var keyWordArray = [];
	
	for ( var i = 0, length = keyWordList.length; i < length; i++) {
		keyWordArray.push(keyWordList[i].text);
	}
	var keywordStr=keyWordArray.join(",");
	if (keywordStr == "") {
		$.scmtips.show("warn", keywordReq);
		return;
	}*/
	var keywordStr=autoword.texts().join(",");
	if (keywordStr == "") {
		$.scmtips.show("warn", keywordReq);
		return;
	}
	$("#ref_title").val($.trim($('#title_td').text()));
	$('#ref_abs').val($.trim($('#abs_td').text()));
	$("#ref_keywordStr").val(keywordStr);
	$("#refForm").submit();
}

