/**
 * 分享详情-收件箱.
 * 
 * @param sid
 * @param rid
 * @param obj
 * @return
 */
function showShareList(sid, rid, obj) {
	$("#hidden-pageNo").val($("#pageNo").val());
	$("#hidden-pageSize").val($("#pageSize").val());
	des3ResSendId = sid;
}

/**
 * 加载成果列表-发件箱.
 * 
 * @return
 */
function ajaxInitPubList(sid, shareType, obj) {
	var mailId = $(obj).parent().attr("mailId");
	$(".Menubox").hide();
	$("#con_one_2").html("<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		url : snsctx + "/commend/publication/ajaxShareOutPubList",
		type : "post",
		data : {
			"sid" : encodeURIComponent(sid),
			"articleType" : shareType,
			"mailId" : mailId
		},
		success : function(data) {
		   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   }
		   else {
		   	if (data == '' || data == null) {
		   		$.smate.scmtips.show("error", msgboxTip.sysException);
		   	}
		   	else {
		   		$("#con_one_2").html(data);
		   		$("body,html").animate({
		   			scrollTop: 0
		   		}, 500);
		   	}
		   }
		},
		error : function() {
		}
	});
}

/**
 * 加载文件列表-发件箱.
 */
function ajaxInitFileList(sid, obj) {
	var mailId = $(obj).parent().attr("mailId");
	$(".Menubox").hide();
	$("#con_one_2").html("<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		url : snsctx + "/commend/sendfilelist",
		type : "post",
		data : {
			"sid" : encodeURIComponent(sid),
			"mailId" : mailId
		},
		success : function(data) {
		   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   }
		   else {
		   	$("#con_one_2").html(data);
		   	$("body,html").animate({
		   		scrollTop: 0
		   	}, 500);
		   }
		},
		error : function() {
		}
	});
}

// 初始化上一封下一封链接.
function shareInitLinkForOutbox(des3ShareId) {
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxShareOutboxInitLink",
		dataType : "json",
		data : {
			"mailId" : encodeURIComponent(des3ShareId),
			"type" : "share"
		},
		success : function(json) {
		   if (data.ajaxSessionTimeOut) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   } else {
		   	if (json.action == "success") {
		   		if (json.prev == "true") {
		   			$("#prevShareTitle").html(json.prevTitle);
		   			shareLinkTitleForBox("prevShareTitle");
		   			
		   			$("#prevShareTitle").attr("mailId", json.prevId);
		   			$("#prevId").attr("href", "javascript:showPrevInbox()");
		   		}
		   		else {
		   			$("#prevId").attr("href", "javascript:prevTips();");
		   		}
		   		
		   		if (json.next == "true") {
		   			$("#nextShareTitle").html(json.nextTitle);
		   			shareLinkTitleForBox("nextShareTitle");
		   			$("#nextShareTitle").attr("mailId", json.nextId);
		   			$("#nextId").attr("href", "javascript:showNextInbox()");
		   		}
		   		else {
		   			$("#nextId").unbind();
		   			$("#nextId").attr("href", "javascript:nextTips();");
		   		}
		   		
		   		$("#prevId").enabled();
		   		$("#nextId").enabled();
		   	}
		   	else 
		   		if (json.acton == "failure") {
		   		}
		   }
		},
		error : function(e) {
		}
	});
}

/**
 * 发件箱-批量删除系统消息.
 * 
 * @return
 */
function shareDeleteOutboxBatch() {
	var ids = MsgBoxUtil.collectIds("shareCheckbox");
	MsgBoxUtil.ajaxDeleteOutboxMessage(ids, "share", shareDeleteOutboxCallBack);
}

/**
 * 发件箱-单个删除.
 * 
 * @return
 */
function shareDeleteOutboxOne() {
	var ids = $(this).attr("mailId");
	MsgBoxUtil.ajaxDeleteOutboxMessage(ids, "share", shareDeleteOutboxCallBack);
}

/**
 * 发件箱-删除回掉调数.
 * 
 * @param ids
 * @return
 */
function shareDeleteOutboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	shareAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxShareOutbox");
}

/**
 * 取消共享.
 * 
 * @param des3Id
 * @return
 */
function shareCancelShare(des3Id,psnId) {
	jConfirm(msgboxTip.cfmCancelSharing, msgboxTip.dialoagTitle, function(sure) {
		if (sure) {
			$.ajax( {
				type : "post",
				url : snsctx + "/commend/ajaxCancelShare",
				data : {
					"des3Id" : des3Id,
					"psnId":psnId
				},
				dataType : "json",
				success : function(data) {
				   if (data.ajaxSessionTimeOut) {
				   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   		if (r) {
				   			document.location.reload();
				   		}
				   	});
				   } else {
				   	if (data.result == "success") {
				   		$("#cancelShareLink").remove();
				   		$.smate.scmtips.show("success", msgboxTip.optSuccess);
				   	}
				   }
				},
				error : function() {
					$.smate.scmtips.show("error", msgboxTip.optFailed);
				}
			});
		}
	});
}