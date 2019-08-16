/**
 * 选项卡切换.
 * 
 * @param name
 * @param cursel
 * @param n
 * @return
 */
function shareSetTab(name, cursel, n) {
	$("#msgbox_search").val(msgboxTip.searchMsg);
	$("#msgbox_search").css("color","#999");
	if (cursel == "1") {
		$("#hidden-inOrOut").val(0);
	} else {
		$("#hidden-inOrOut").val(1);
	}
	for ( var i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

/**
 * 切换内容.
 * 
 * @param replaceId
 * @param url
 * @return
 */
function shareAjaxHtml(replaceId, url) {
	var searchKey = $.trim($("#msgbox_search").val());
	if (searchKey == msgboxTip.searchMsg) {
		searchKey = "";
	}
	var status = $("#hidden-msgboxStatus").val();
	var backPageNo = $("#hidden-pageNo").val();
	var backPageSize = $("#hidden-pageSize").val();
	if (backPageSize == 0) {
		backPageSize = 10;
	}
	$("#" + replaceId).html(
			"<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		type : "post",
		url : url,
		data : {
			"searchKey" : searchKey,
			"status" : status,
			"page.pageNo" : backPageNo,
			"page.pageSize" : backPageSize
		},
		dataType : "html",
		cached : false,
		success : function(data) {
		   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   }
		   else {
		   	$("div[id*=con_one_]").each(function(){
		   		$(this).html("");
		   	});
		   	$("#" + replaceId).html(data);
		   	$("#hidden-pageNo").val(0);
		   	$("#hidden-pageSize").val(0);
		   	MsgBoxUtil.refreshMsgCount();
		   }
		},
		error : function(e) {
		}
	});
}

/**
 * 初始进入.
 * 
 * @param msgboxFlag
 * @return
 */
function shareInitMainContent(msgboxFlag) {
	var url = snsctx + "/msgbox/";
	var replaceId = "";
	if (msgboxFlag == 0) {// 收件箱
		url = url + "ajaxShareInbox";
		replaceId = "con_one_1";
	} else if (msgboxFlag == 1) {// 发件箱
		url = url + "ajaxShareOutbox";
		replaceId = "con_one_2";
	} else if (msgboxFlag == 2 || msgboxFlag == 3) {// 成果详情2:收；3：发
		url = url + "ajaxSharePubView";
		replaceId = "share-right-wrap";
	} else if (msgboxFlag == 4 || msgboxFlag == 5) {// 文件详情：4：收；5：发
		url = url + "ajaxShareFileView";
		replaceId = "share-right-wrap";
	}
	shareAjaxHtml(replaceId, url);
}

/**
 * 显示title.
 * 
 * @return
 */
function shareShowTitle() {
	$('.shareMsgTitle').each(function() {
		//如果发件人ID为-1，则表示该发件人已不存在，取消a标签的绑定事件_MJG_SCM-3733.
		var mailbox_sendId=$(this).attr("alt");
		var title = '';
		if (locale == 'zh_CN') {
			if(mailbox_sendId=='-1'){
				title = $(this).find('#share_Title_CN').find('a').html();
			}else{
				title = $(this).find('#share_Title_CN').html();
			}
		} else {
			if(mailbox_sendId=='-1'){
				title = $(this).find('#share_Title_EN').find('a').html();
			}else{
				title = $(this).find('#share_Title_EN').html();
			}
		}
		var target_label=$(this).parent().find(".shareShowTitle");
		if (title != null && $.trim(title) != '') {
			target_label.html(title);
		} else {
			target_label.html($(this).html());
		}
		$(this).remove();
	});
}

/**
 * 处理分享老数据的标题.
 */
function dealOldShareInboxMsgTitle() {
	$('.oldShareMsgTitle').each(function() {
		var viewTitle = "";
		if(locale == 'zh_CN') {
			viewTitle = $(this).find("#share_Title_CN > a").html();
		} else {
			viewTitle = $(this).find("#share_Title_EN > a").html();
		}
		var des3ResRecId = $(this).find("#receiveId").val();
		$(this).parent().html('<p><a class="Blue" style="cursor:pointer" onclick="shareInboxMsg(\'' + des3ResRecId + '\')">' + viewTitle + "</a></p>");
	});
}

/**
 * 初始化详情标题.
 * 
 * @param des3SmsId
 * @return
 */
function shareInitTitleForBox() {
	// 初始化theme
	var currentTitleObj = $("#currentShareTitle");
	var title = '';
	if (locale == 'zh_CN') {
		title = $(currentTitleObj).find('#share_Title_CN').text();
	} else {
		title = $(currentTitleObj).find('#share_Title_EN').text();
	}
	if (title != null && $.trim(title) != '') {
		$("#theme_title").text(title);
		$("#theme_title").attr("title", title);
	} else {
		$("#theme_title").text($(currentTitleObj).text());
		$("#theme_title").attr("title", $(currentTitleObj).text());
	}
	$(currentTitleObj).remove();
}

/**
 * 初始化上一封下一封标题.
 * 
 * @param id
 * @return
 */
function shareLinkTitleForBox(id) {
	// 初始化theme
	var currentTitleObj = $("#" + id);
	var title = '';
	if (locale == 'zh_CN') {
		title = $(currentTitleObj).find('#share_Title_CN').html();
	} else {
		title = $(currentTitleObj).find('#share_Title_EN').html();
	}
	if (title != null && $.trim(title) != "") {
		$(currentTitleObj).html(title);

	} else {
		$(currentTitleObj).html($(currentTitleObj).html());
	}
}
/**
 * 没有上一封提示.
 * 
 * @return
 */
function prevTips() {
	$.smate.scmtips.show("warn", msgboxTip.firstMsg);
}

/**
 * 没有下一封提示.
 * 
 * @return
 */
function nextTips() {
	$.smate.scmtips.show("warn", msgboxTip.lastMsg);
}

function showNextInbox() {
	$("#nextShareTitle").find("a").click();
}

function showPrevInbox() {
	$("#prevShareTitle").find("a").click();
}

/**
 * 下载文件.
 * 
 * @param Id
 * @param nodeId
 * @return
 */
function openFile(Id, nodeId) {
	window.open(snsctx + "/file/download?des3Id=" + Id + "&nodeId=" + nodeId
			+ "&type=0&flag=1");
}

/**
 * 人员信息为空.
 */
function emptySenderTip(){
	$.smate.scmtips.show("success", msgboxTip.emptySender);
}

function shareOutboxMsg(des3MailId) {
	if ($("#pageNo").length > 0) {
		$("#hidden-pageNo").val($("#pageNo").val());
		$("#hidden-pageSize").val($("#pageSize").val());
	}
	$(".Menubox").hide();
	$("#con_one_2").html(
			"<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxShareOutboxView",
		data : {
			"mailId" : encodeURIComponent(des3MailId),
			"des3ResSendId" : des3ResSendId
		},
		dataType : "text",
		cached : false,
		success : function(data) {
			   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
				   		document.location.reload();
				   		}
				   	});
			   } else {
			   	$("#con_one_2").html(data);
			   		//全文请求
			   	$(".notPrintLinkSpan").fullTextRequest();
					$("a.thickbox,input.thickbox").thickbox( {
						resmod : resmod
					});
			   }
			   des3ResSendId = "";
		},
		error : function(e) {
			 des3ResSendId = "";
		}
	});
};

function shareInboxMsg(des3RecvId) {
	if ($("#pageNo").length > 0) {
		$("#hidden-pageNo").val($("#pageNo").val());
		$("#hidden-pageSize").val($("#pageSize").val());
	}
	$(".Menubox").hide();
	$("#con_one_1").html(
			"<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxShareInboxView",
		data : {
			"recvId" : encodeURIComponent(des3RecvId),
			"des3ResRecId" : des3ResRecId
		},
		dataType : "text",
		cached : false,
		success : function(data) {
			   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
				   		document.location.reload();
				   		}
				   	});
			   } else {
			   	$("#con_one_1").html(data);
			   		//全文请求
				   $(".notPrintLinkSpan").fullTextRequest();
			   }
			   des3ResRecId = "";
		},
		error : function(e) {
			des3ResRecId = "";
		}
	});
};

/**
 * 取消共享.
 * 
 * @param des3Id
 * @return
 */
function cancelShare(des3ResSendId) {
	jConfirm(msgboxTip.cfmCancelSharing, msgboxTip.dialoagTitle, function(sure) {
		if (sure) {
			$.ajax( {
				type : "post",
				url : snsctx + "/commend/ajaxCancelShare",
				data : {
					"des3ResSendId" : des3ResSendId
				},
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
				   		$("#cancelShareLink").remove();
				   		$.smate.scmtips.show("success", msgboxTip.optSuccess);
				   	} else {
				   		if (data.ajaxSessionTimeOut) {
						   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
						   		if (r) {
						   			document.location.reload();
						   			}
							   	});
						   } else {
							   $.smate.scmtips.show("success", msgboxTip.optFailed);
						   }
				   		}
				   
				},
				error : function() {
					$.smate.scmtips.show('error', msgboxTip.networdException);
				}
			});
		}
	});
};

/**
 * 删除单个分享资源.
 * @param des3ResId
 */
function deleteShareResOne(des3ResId, index) {
	var des3ResRecId = $("#des3ResRecId").val();
	var params = new Array();
	params.push({
		"des3ResId" : des3ResId,
		"des3ResRecId" : des3ResRecId,
		"index" : index
	})
	deleteShareRes(params);
};

/**
 * 删除选中分享资源.
 */
function deleteShareResBatch() {
	var params = buildSelectResParam();
	deleteShareRes(params);
};

/**
 * 删除分享资源.
 * @param params
 */
function deleteShareRes(params) {
	var shareType = $("#shareType").val();
	if (params.length <= 0) {
		var tip = shareType == 3 ? msgboxTip.selectSharingFile : msgboxTip.selectSharingPub;
		$.smate.scmtips.show("warn", tip, locale == "zh_CN" ? 300 : 380);
		return false;
	}
	var delTip = shareType == 3 ? msgboxTip.cfmDelFile : msgboxTip.cfmDelPub; 
	jConfirm(delTip, msgboxTip.dialoagTitle, function(r) {
		if (r) {
			$.ajax( {
				type : "post",
				url : snsctx + "/commend/deleteShareRes",
				dataType : "json",
				data : {
					"jsonParam" : JSON.stringify(params)
				},
				success : function(data) {
					if (data.result == "success") {
						for (var j = 0; j < params.length; j++) {
							$("#papers_list" + params[j]["index"]).attr("class", "prompt_bj")
							.append("<div class=\"point_add\"><i class=\"added_file\"></i>" + msgboxTip.ignore + "</div>")
							.find(".delete-friend").remove();
				   		$("#resItemCk" + params[j]["index"]).attr("disabled", "disabled")
				   		.attr("checked", false)
				   		.attr("name", "");
				   		var fileName = $("#fileLink" + params[j]["index"]).text();
				   		$("#fileLink" + params[j]["index"]).parent().html(fileName);
				   		}
				   	$.smate.scmtips.show("success", data.msg);
				   } else {
					   if (data.ajaxSessionTimeOut) {
						   jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
						   	if (r) {
						   		document.location.reload();
						   		}
						   	});
						 } else {
							 $.smate.scmtips.show("error", data.msg);
						 }
				   	}
				},
				error : function(e) {
					$.smate.scmtips.show('error', msgboxTip.networdException);
				}
			});
		}
	});
};

/**
 * 导入到我的文件.
 */
importToMyFile = function(obj, params) {
	$.proceeding.show(msgboxTip.proceeding, "share-right-wrap");
	$.ajax( {
		type : "post",
		url : snsctx + "/commend/confirmToMyFile",
		dataType : "json",
		data : {'jsonParam':JSON.stringify(params)},
		success : function(data) {
			$.proceeding.hide();
			if (data.result == 'success') {
				dealResItemBox(params, msgboxTip.addedFileTip);
				$.smate.scmtips.show("success", data.msg);
			} else {
				if (data.ajaxSessionTimeOut) {
				   jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   	if (r) {
				   		document.location.reload();
				   		}
				   	});
				 } else {
					 $.smate.scmtips.show("error", data.msg);
				 }
			}
		},
		error : function(data) {
			$.proceeding.hide();
			$.smate.scmtips.show('error', msgboxTip.networdException);
		}
	});
};

/**
 * 导入到我的库中.
 */
importToMyLib = function(libType) {
	var _this = this;
	var params = buildSelectResParam();
	if(params.length <= 0) {
		var tip = libType == 3 ? msgboxTip.selectAddSharingFile : msgboxTip.selectAddSharingPub;
		$.smate.scmtips.show("warn", tip, locale == "zh_CN" ? 300 : 380);
		return;
	}
	if (libType == 3) {
		importToMyFile(_this, params);
	} else if(libType == 2) {
		importToMyRef(_this, params);
	} else if(libType == 1) {
		importToMyPub(_this, params);
	}
}

/**
 * 导入到我的文献.
 */
importToMyRef = function(obj, params) {
	$.proceeding.show(msgboxTip.proceeding, "share-right-wrap");
	$.ajax({
		url : snsctx + '/reference/commend/ajaxImport',
		type : "POST",
		dataType : "json",
		data : {
			"jsonParam" : JSON.stringify(params),
			"impArticleType" : 2
		},
		success : function(data){
			$.proceeding.hide();
			if (data.result == 'success') {
				dealResItemBox(params, msgboxTip.addedTip);
				$.smate.scmtips.show('success', data.msg);
			} else if(data.result == 'warn') {
				$.smate.scmtips.show('warn', data.msg);
			} else if (data.ajaxSessionTimeOut) {
				jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   	if (r) {
				   		document.location.reload();
				   		}
				   	});
			} else {
				$.smate.scmtips.show('error', data.msg);
			}
		},
		error : function(){
			$.proceeding.hide();
			$.smate.scmtips.show('error', msgboxTip.networdException);
		}
	});
};

/**
 * 导入到我的成果.
 */
importToMyPub = function(obj, params) {
	$.proceeding.show(msgboxTip.proceeding, "share-right-wrap");
	$.ajax({
		url : snsctx + '/publication/commend/ajaxImport',
		type : "POST",
		dataType : "json",
		data : {
			"jsonParam" : JSON.stringify(params),
			"impArticleType" : 1
		},
		success : function(data){
			$.proceeding.hide();
			if (data.result == 'success') {
				dealResItemBox(params, msgboxTip.addedTip);
				$.smate.scmtips.show('success', data.msg);
			} else if(data.result == 'repeat') {
				getHasDupPub(data.snsPubJson, data.xmlId, data.pdwhPubJson);
			} else if(data.result == 'warn') {
				$.smate.scmtips.show('warn', data.msg);
			} else if(data.ajaxSessionTimeOut) {
				jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   	if (r) {
				   		document.location.reload();
				   		}
				   	});
			} else {
				$.smate.scmtips.show('error', data.msg);
			}
		},
		error : function(){
			$.proceeding.hide();
			$.smate.scmtips.show('error', msgboxTip.networdException);
		}
	});
};

/**
 * 获取有重复成果的选择导入的成果.
 */
getHasDupPub = function(snsImpPubJson, xmlId, pdwhImpPubJson) {
	var des3RecvId = $("#des3RecvId").val();
	$(".Menubox").hide();
	$("#con_one_1").html("<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxGetDupImpPub",
		data : {
			"recvId" : encodeURIComponent(des3RecvId),
			"snsImpPubJson" : snsImpPubJson,
			"xmlId": xmlId,
			"pdwhImpPubJson": pdwhImpPubJson
		},
		dataType : "text",
		cached : false,
		success : function(data) {
			   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
				   		document.location.reload();
				   		}
				   	});
			   } else {
			   	$("#con_one_1").html(data);
			   }
		},
		error : function(e) {
		}
	});
}

/**
 * 确定导入到我的成果库.
 */
cfmImportToMyPubLib = function() {
	var des3ResRecId =  $('#des3ResRecId').val();
	var snsPubParams=[];
	var pdwhPubParams=[];
	$('.resItem_li').each(function(){
		var dbid = $(this).find('.dbid').val();
		if(dbid == null || dbid == '') {
			snsPubParams.push({
				'pubId' : $(this).find('.pub_id_hidden').val(),
				'nodeId' : $(this).find('.nodeId').val(),
				'dup_action' : $(this).find(":radio").filter(":checked").val(),
				'dup_ids' : $(this).find('.dup_pub_id').val(),
				'des3ResRecId' : des3ResRecId
			});
		} else {
			pdwhPubParams.push({
				'pubId' : $(this).find('.pub_id_hidden').val(),
				'dbid' : $(this).find('.dbid').val(),
				'dup_action' : $(this).find(":radio").filter(":checked").val(),
				'dup_ids' : $(this).find('.dup_pub_id').val(),
				'des3ResRecId' : des3ResRecId
			});
		}
	});
	var postData = {};
	if(snsPubParams.length > 0) {
		postData["snsPubJsonParam"] = JSON.stringify(snsPubParams);
	}
	if (pdwhPubParams.length > 0) {
		postData["xmlId"] = $('#xmlId_hidden').val();
		postData["pdwhPubJsonParam"] = JSON.stringify(pdwhPubParams);
	}
	$.proceeding.show(msgboxTip.proceeding, "share-right-wrap");
	$.ajax({
		url : snsctx + '/publication/commend/ajaxImpToMyLib',
		type : "POST",
		dataType : "json",
		data : postData,
		success : function(data){
			$.proceeding.hide();
			if (data.result == 'success') {
				$.smate.scmtips.show('success', data.msg);
				setTimeout(function(){
					shareInboxMsg($("#des3RecvId").val());
				},1000);
			} else {
				if (data.ajaxSessionTimeOut) {
				   jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   	if (r) {
				   		document.location.reload();
				   		}
				   	});
				 } else {
					 $.smate.scmtips.show("error", data.msg);
				 }
			}
		},
		error : function(){
			$.proceeding.hide();
			$.smate.scmtips.show('error', msgboxTip.networdException);
		}
	});
}

/**
 * 构造选中资源的参数.
 */
buildSelectResParam = function() {
	var des3ResRecId = $("#des3ResRecId").val();
	var params = new Array();
	$(":checkbox[name='resItemCk']:checked").each(function() {
		params.push({
			"des3ResId" : $(this).val(),
			"des3ResRecId" : des3ResRecId,
			"index" : $(this).attr("index")
		})
	});
	return params;
};

function selectResIds(){
	var resIds = "";
	$(":checkbox[name='resItemCk']:checked").each(function() {
		if (resIds == "") {
			resIds = $(this).val();
		} else {
			resIds = resIds + "," + $(this).val();
		}
	});
	return resIds;
};

function dealResItemBox(params, tip) {
	for (var j = 0; j < params.length; j++) {
		$("#papers_list" + params[j]["index"]).attr("class", "prompt_bj")
		.append("<div class=\"point_add\"><i class=\"added_file\"></i>" + tip + "</div>")
		.find(".delete-friend").remove();
		$("#resItemCk" + params[j]["index"]).attr("disabled", "disabled")
		.attr("checked", false)
		.attr("name", "");
	}
}

function openFile(fileId, nodeId, type){
	location.href = snsctx + "/file/download?des3Id=" + fileId + "&nodeId="+ nodeId + "&type=" + type;
}