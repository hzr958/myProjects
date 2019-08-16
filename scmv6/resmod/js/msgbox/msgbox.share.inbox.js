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
	des3ResRecId = rid;
}

/**
 * 加载成果列表.
 * 
 * @return
 */
function ajaxInitPubList(sid, shareType, obj) {
	var mailId = $(obj).parent().attr("mailId");
	var recvId = $(obj).parent().attr("recvId");
	$(".Menubox").hide();
	$("#con_one_1").html("<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		url : snsctx + "/commend/publication/ajaxShareInPubList",
		type : "POST",
		data : {
			"sid" : encodeURIComponent(sid),
			"articleType" : shareType,
			"mailId" : mailId,
			"recvId" : recvId
		},
		success : function(data) {
		   if (data.ajaxSessionTimeOut) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   }else {
		   	if (data == "" || data == null) {
		   		if ($("#theme_title").length > 0) {//详情中
								var titleId = $(obj).parent().attr("id");
								if (titleId == "nextShareTitle") {//点的是下一封
									shareCancelLinkForInbox($(obj).parent().attr("recvId"), 2);
								}
								else {//点的是上一封
									shareCancelLinkForInbox($(obj).parent().attr("recvId"), 1);
								}
							}
							else {
								$.smate.scmtips.show("warn", msgboxTip.canceledSharing, locale == "zh_CN" ? 310 : 330);
								//改变样式
								$("#share_tr" + recvId).removeClass("m_tr01");
								$("#share_p" + recvId).removeClass("p_share_title");
								$("#share_p" + recvId).addClass("p_share_title2");
								$("#share_img" + recvId).attr("src", resmod + "/images_v5/mail02.gif");
							}
						}
						else {
							$("#con_one_1").html(data);
							$("body,html").animate({
								scrollTop: 0
							}, 500);
						}
						
						MsgBoxUtil.ajaxMarkMessageStatus(recvId, "share", "read", null, 1);
				}
		},
		error : function() {
		}
	});
}

/**
 * 加载文件列表.
 * 
 * @return
 */
function ajaxInitFileList(sid, obj) {
	var mailId = $(obj).parent().attr("mailId");
	var recvId = $(obj).parent().attr("recvId");
	$(".Menubox").hide();
	$("#con_one_1").html("<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		url : snsctx + "/commend/receivefilelist",
		type : "POST",
		data : {
			"sid" : encodeURIComponent(sid),
			"mailId" : mailId,
			"recvId" : recvId
		},
		cache : false,
		success : function(data) {
		   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   }
		   else {
		   	if (data == "" || data == null) {
		   		if ($("#theme_title").length > 0) {//详情中
								var titleId = $(obj).parent().attr("id");
								if (titleId == "nextShareTitle") {//点的是下一封
									shareCancelLinkForInbox($(obj).parent().attr("recvId"), 2);
								}
								else {//点的是上一封
									shareCancelLinkForInbox($(obj).parent().attr("recvId"), 1);
								}
							}
							else {
								$.smate.scmtips.show("warn", msgboxTip.canceledSharing, locale == "zh_CN" ? 310 : 330);
								//改变样式
								$("#share_tr" + recvId).removeClass("m_tr01");
								$("#share_p" + recvId).removeClass("p_share_title");
								$("#share_p" + recvId).addClass("p_share_title2");
								$("#share_img" + recvId).attr("src", resmod + "/images_v5/mail02.gif");
							}
						}
						else {
							$("#con_one_1").html(data);
							$("body,html").animate({
								scrollTop: 0
							}, 500);
						}
						
						MsgBoxUtil.ajaxMarkMessageStatus(recvId, "share", "read", null, 1);
					}
		},
		error : function() {
		}
	});
}

//针对已取消共享上一封下一封链接.
function shareCancelLinkForInbox(des3ShareId,flag) {
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxShareInboxInitLink",
		dataType : "json",
		data : {
			"recvId" : encodeURIComponent(des3ShareId),
			"type" : "share"
		},
		success : function(json) {
			   if (json.ajaxSessionTimeOut) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
			   			document.location.reload();
			   		}
			   	});
			   }else {
			   	if (json.action == "success") {
			   		if (flag == 1) {//点的是上一封
									if (json.prev == "true") {
										$("#prevShareTitle").html(json.prevTitle);
										shareLinkTitleForBox("prevShareTitle");
										$("#prevShareTitle").attr("recvId", json.prevRecvId);
										$("#prevShareTitle").attr("mailId", json.prevMailId);
										$("#prevId").attr("href", "javascript:showPrevInbox()");
										showPrevInbox();
									}
									else {
										$("#prevId").attr("href", "javascript:prevTips();");
										prevTips();
									}
								}
								else {//点的是下一封
									if (json.next == "true") {
										$("#nextShareTitle").html(json.nextTitle);
										shareLinkTitleForBox("nextShareTitle");
										$("#nextShareTitle").attr("recvId", json.nextRecvId);
										$("#nextShareTitle").attr("mailId", json.nextMailId);
										$("#nextId").attr("href", "javascript:showNextInbox()");
										showNextInbox();
									}
									else {
										$("#nextId").unbind();
										$("#nextId").attr("href", "javascript:nextTips();");
										nextTips();
									}
								}
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

// 初始化上一封下一封链接.
function shareInitLinkForInbox(des3ShareId) {
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxShareInboxInitLink",
		dataType : "json",
		data : {
			"recvId" : encodeURIComponent(des3ShareId),
			"type" : "share"
		},
		success : function(json) {
			   if (json.ajaxSessionTimeOut) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
			   			document.location.reload();
			   		}
			   	});
			   }else {
			   	if (json.action == "success") {
			   		if (json.prev == "true") {
			   		
			   			$("#prevShareTitle").html(json.prevTitle);
			   			shareLinkTitleForBox("prevShareTitle");
			   			$("#prevShareTitle").attr("recvId", json.prevRecvId);
			   			$("#prevShareTitle").attr("mailId", json.prevMailId);
			   			$("#prevId").attr("href", "javascript:showPrevInbox()");
			   		}
			   		else {
			   			$("#prevId").attr("href", "javascript:prevTips();");
			   		}
			   		
			   		if (json.next == "true") {
			   			$("#nextShareTitle").html(json.nextTitle);
			   			shareLinkTitleForBox("nextShareTitle");
			   			$("#nextShareTitle").attr("recvId", json.nextRecvId);
			   			$("#nextShareTitle").attr("mailId", json.nextMailId);
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
 * 未读分享数.
 * 
 * @return
 */
function shareInitInboxUnReadCount() {
	setTimeout(function() {
		var shareUnReadCount = $.trim($("#displayShareCount").text());
		if (shareUnReadCount != "") {
			$("#label-share-unread").text(shareUnReadCount);
			var totalCount=parseInt($("#msg_totalCount_share").val());
			$("#label-share-read").text(totalCount-parseInt(shareUnReadCount));
		}
	}, 500);
}

/**
 * 收件箱-批量删除系统消息.
 * 
 * @return
 */
function shareDeleteInboxBatch() {
	var ids = MsgBoxUtil.collectIds("shareCheckbox");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids, "share", shareDeleteInboxCallBack);
}

/**
 * 收件箱-单个删除.
 * 
 * @return
 */
function shareDeleteInboxOne() {
	var ids = $(this).attr("mailId");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids, "share", shareDeleteInboxCallBack);
}

/**
 * 收件箱-删除回调函数.
 * 
 * @param ids
 * @return
 */
function shareDeleteInboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	shareAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxShareInbox");
}

/**
 * 已读未读
 * 
 * @param status
 * @return
 */
function readShareInbox(status) {
	$("#hidden-msgboxStatus").val(status);
	shareAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxShareInbox");
}

/**
 * 删除分享文件.
 * 
 * @return
 */
function shareDeleteFileBatch(rid) {
	var ids = "";
	var indexArray = new Array();
	var i = 0;
	$("input[name='shareFileCheckbox']:checked").each(function() {
		if (ids == "") {
			ids = $(this).val();
		} else {
			ids += "," + $(this).val();
		}

		indexArray[i] = $(this).attr("index");
		i++;
	});

	shareDeleteFile(rid, ids, indexArray);
}

/**
 * 删除分享文件.
 * 
 * @param index
 * @param rid
 * @return
 */
function shareDeleteFileOne(index, rid) {
	var ids = $("#checkbox" + index).val();
	var indexArray = new Array(1);
	indexArray[0] = index;
	shareDeleteFile(rid, ids, indexArray);
}

function shareDeleteFile(rid, ids, indexArray) {
	if (ids == "") {
		$.smate.scmtips.show("warn", msgboxTip.selectSharingFile, locale == "zh_CN" ? 300 : 380);
		return false;
	}

	jConfirm(
			msgboxTip.cfmDelFile,
			msgboxTip.dialoagTitle,
			function(r) {
				if (r) {
					$
							.ajax( {
								type : "post",
								url : snsctx
										+ "/commend/cancelConfirmInboxFiles",
								dataType : "json",
								data : {
									"sid" : rid,
									"fileIds" : ids
								},
								success : function(data) {
									   if (data.ajaxSessionTimeOut) {
									   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
									   		if (r) {
									   			document.location.reload();
									   		}
									   	});
									   } else {
									   	if (data.result == "success") {
									   		for (var j = 0; j < indexArray.length; j++) {
									   			$("#papers_list" + indexArray[j]).attr("class", "prompt_bj");
									   			$("#papers_list" + indexArray[j]).append("<div class=\"point_add\"><i class=\"added_file\"></i>" + msgboxTip.ignore + "</div>");
									   			$("#papers_list" + indexArray[j]).find(".delete-friend").remove();
									   			$("#checkbox" + indexArray[j]).attr("disabled", "disabled");
									   			$("#checkbox" + indexArray[j]).attr("checked", false);
									   			$("#checkbox" + indexArray[j]).attr("name", "");
									   			$("#fileLink" + indexArray[j]).disabled();
									   		}
									   		$.smate.scmtips.show("success", msgboxTip.optSuccess);
									   	}
									   	else {
									   		$.smate.scmtips.show("error", msgboxTip.optFailed);
									   	}
									   }
								},
								error : function(e) {
									$.smate.scmtips.show("error", msgboxTip.optFailed);
								}
							});
				}
			});
}

/**
 * 添加到我的文件.
 * 
 * @param rid
 * @return
 */
function shareFileExportMine(rid) {
	var ids = "";
	var indexArray = new Array();
	var i = 0;
	$("input[name='shareFileCheckbox']:checked").each(function() {
		if (ids == "") {
			ids = $(this).val();
		} else {
			ids += "," + $(this).val();
		}

		indexArray[i] = $(this).attr("index");
		i++;
	});

	if (ids == "") {
		$.smate.scmtips.show("warn", msgboxTip.selectAddSharingFile);
		return false;
	}

	jConfirm(
			msgboxTip.cfmAddToMyFile,
			msgboxTip.dialoagTitle,
			function(r) {
				if (r) {
					$
							.ajax( {
								type : "post",
								url : snsctx + "/commend/confirmInboxFiles",
								dataType : "json",
								data : {
									"sid" : rid,
									"fileIds" : ids
								},
								success : function(data) {
									   if (data.ajaxSessionTimeOut) {
									   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
									   		if (r) {
									   			document.location.reload();
									   		}
									   	});
									   }else {
									   	if (data.result == "success") {
									   		for (var j = 0; j < indexArray.length; j++) {
									   			$("#papers_list" + indexArray[j]).attr("class", "prompt_bj");
									   			$("#papers_list" + indexArray[j]).append("<div class=\"point_add\"><i class=\"added_file\"></i>" + msgboxTip.addedFileTip + "</div>");
									   			$("#papers_list" + indexArray[j]).find(".delete-friend").remove();
									   			$("#checkbox" + indexArray[j]).attr("disabled", "disabled");
									   			$("#checkbox" + indexArray[j]).attr("checked", false);
									   			$("#checkbox" + indexArray[j]).attr("name", "");
									   		}
									   		$.smate.scmtips.show("success", msgboxTip.optSuccess);
									   	}
									   	else {
									   		$.smate.scmtips.show("warn", msgboxTip.optFailed);
									   	}
									   }
								},
								error : function(e) {
									$.smate.scmtips.show("error", msgboxTip.optFailed);
								}
							});
				}
			});
}

/**
 * 删除分享成果-批量.
 * 
 * @param rid
 * @return
 */
function shareDeletePubBatch(rid) {
	var ids = "";
	var indexArray = new Array();
	var i = 0;
	$("input[name='sharePubCheckbox']:checked").each(function() {
		if (ids == "") {
			ids = $(this).val();
		} else {
			ids += "," + $(this).val();
		}

		indexArray[i] = $(this).attr("index");
		i++;
	});

	shareDeletePub(rid, ids, indexArray);
}

/**
 * 删除分享成果.
 * 
 * @param index
 * @param rid
 * @return
 */
function shareDeletePubOne(index, rid) {
	var ids = $("#checkbox" + index).val();
	var indexArray = new Array(1);
	indexArray[0] = index;
	shareDeletePub(rid, ids, indexArray);
}

function shareDeletePub(rid, ids, indexArray) {
	if (ids == "") {
		$.smate.scmtips.show("warn", msgboxTip.selectSharingPub);
		return false;
	}

	jConfirm(
			msgboxTip.cfmDelPub,
			msgboxTip.dialoagTitle,
			function(r) {
				if (r) {
					$
							.ajax( {
								url : snsctx
										+ "/commend/publication/ajaxDelete",
								type : "post",
								dataType : "json",
								data : {
									"pubIds" : ids,
									"sid" : rid
								},
								success : function(data) {
									   if (data.ajaxSessionTimeOut) {
									   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
									   		if (r) {
									   			document.location.reload();
									   		}
									   	});
									   }else {
									   	for (var j = 0; j < indexArray.length; j++) {
									   		$("#papers_list" + indexArray[j]).attr("class", "prompt_bj");
									   		$("#papers_list" + indexArray[j]).append("<div class=\"point_add\"><i class=\"added_file\"></i>" + msgboxTip.ignore + "</div>");
									   		$("#papers_list" + indexArray[j]).find(".delete-friend").remove();
									   		$("#checkbox" + indexArray[j]).attr("disabled", "disabled");
									   		$("#checkbox" + indexArray[j]).attr("checked", false);
									   		$("#checkbox" + indexArray[j]).attr("name", "");
									   		$("#papers_list" + indexArray[j]).find(".notPrintLinkSpan").unbind("click").removeAttr("onclick");
									   		$("#papers_list" + indexArray[j]).find(".notPrintLinkSpan").find("font").removeAttr("color");
									   	}
									   	$.smate.scmtips.show("success", msgboxTip.optSuccess);
									   }
								},
								error : function() {
									$.smate.scmtips.show("error", msgboxTip.optFailed);
								}
							});
				}
			});
}

/**
 * 添加到我的成果/文献.
 * 
 * @param articleType
 * @param rid
 * @return
 */
function sharePubExportMine(articleType, rid) {
	var ids = "";
	var indexArray = new Array();
	var i = 0;
	$("input[name='sharePubCheckbox']:checked").each(function() {
		if (ids == "") {
			ids = $(this).val();
		} else {
			ids += "," + $(this).val();
		}

		indexArray[i] = $(this).attr("index");
		i++;
	});

	if (ids == "") {
		$.smate.scmtips.show("warn", msgboxTip.selectAddSharingPub);
		return false;
	}
	var cfmTipCon = articleType == 1 ? msgboxTip.cfmAddToMyPub : msgboxTip.cfmAddToMyRef;
	jConfirm(cfmTipCon, msgboxTip.dialoagTitle, function(r) {
		if (r) {
			$.ajax( {
				url : snsctx + "/commend/publication/ajaxSelectPubList",
				type : "post",
				data : {
					"sid" : rid,
					"toArticleType" : articleType,
					"pubIds" : ids
				},
				dataType : "html",
				cache : false,
				success : function(data) {
				   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
				   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   		if (r) {
				   			document.location.reload();
				   		}
				   	});
				   }else {
				   	$("#div_sharePubList").append(data);
				   	$("#ul_sharePubList").hide();
				   }
				},
				error : function() {
				}
			});
		}
	});
}

/**
 * 成果/文献-重新选择.
 * 
 * @return
 */
function sharePubReSelect() {
	$("#toArticleType").remove();
	$("#ul_sharePubList2").remove();
	$("input[name='sharePubCheckbox']").each(function() {
		$(this).attr("checked", false);
	});
	$("#ul_sharePubList").show();
}

/**
 * 成果/文献-确定添加.
 * 
 * @return
 */
function sharePubConfirmExportMine() {
	var params = [];
	$(".li_sharePubList2").each(function() {
		params.push( {
			"pubId" : $(this).find(".pub_pubId_class").val(),
			"dupFlag" : $(this).find(".radiobutton").filter(":checked").val(),
			"dupPubId" : $(this).find(".dup_pubId_class").val()
		});

	});
	var jsonParams = JSON.stringify(params);
	$
			.ajax( {
				url : ctx + "/commend/publication/ajaxConfirmPub",
				type : "post",
				dataType : "json",
				data : {
					"jsonParams" : jsonParams,
					"toArticleType" : $("#toArticleType").val()
				},
				success : function(data) {
				   if (data.ajaxSessionTimeOut) {
				   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
				   		if (r) {
				   			document.location.reload();
				   		}
				   	});
				   } else {
				   	var index;
				   	$("input[name='sharePubCheckbox']:checked").each(function(){
				   		index = $(this).attr("index");
				   		$("#papers_list" + index).attr("class", "prompt_bj");
				   		$("#papers_list" + index).append("<div class=\"point_add\"><i class=\"added_file\"></i>" + msgboxTip.addedTip + "</div>");
				   		$("#papers_list" + index).find(".delete-friend").remove();
				   		$(this).attr("checked", false);
				   		$(this).attr("name", "");
				   		$(this).attr("disabled", "disabled");
				   		
				   	});
				   	$("#toArticleType").remove();
				   	$("#ul_sharePubList2").remove();
				   	$("#ul_sharePubList").show();
				   	$.smate.scmtips.show("success", msgboxTip.optSuccess);
				   }
				},
				error : function() {
					$.smate.scmtips.show("error", msgboxTip.optFailed);
				}
			});
}
