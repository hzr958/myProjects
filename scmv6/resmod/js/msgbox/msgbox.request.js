/**
 * 选项卡切换.
 * 
 * @param name
 * @param cursel
 * @param n
 * @return
 */
function requestSetTab(name, cursel, n) {
	$("#msgbox_search").val(msgboxTip.searchMsg);
	$("#msgbox_search").css("color","#999");
	if (cursel == "1") {
		$("#hidden-inOrOut").val(0);
	} else {
		$("#hidden-inOrOut").val(1);
	}
	for (var i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

function requestAjaxHtml(replaceId, url) {
	var searchKey = $.trim($("#msgbox_search").val());
	if (searchKey == msgboxTip.searchKey) {
		searchKey = "";
	}
	var status = $("#hidden-msgboxStatus").val();

	$("#" + replaceId).html("<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		type : "post",
		url : url,
		data : {
			"searchKey" : searchKey,
			"status" : status
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
		   }
		},
		error : function(e) {
		}
	});
}

function requestInitMainContent(msgboxFlag) {
	$("#hidden-inOrOut").val(msgboxFlag);
	if(msgboxFlag==0){
		requestAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxRequestInbox");
	}else{
		requestSetTab('one', 2, 2);
		requestAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxRequestOutbox");
	}
}

/**
 * 未读请求数.
 * 
 * @return
 */
function requestInitInboxUnReadCount() {
	var requestUnReadCount = $.trim($("#displayRequestCount").text());
	if (requestUnReadCount != "") {
		$("#label-request-unread").text(requestUnReadCount);
		var totalCount=parseInt($("#msg_totalCount_request").val());
		$("#label-request-read").text(totalCount-parseInt(requestUnReadCount));
	}
}

/**
 * 已处理、未处理请求.
 * 
 * @return
 */
function readRequestInbox(status) {
	$("#hidden-msgboxStatus").val(status);
	requestAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxRequestInbox");
}

/**
 * 收件箱-批量删除.
 * 
 * @return
 */
function requestDeleteInboxBatch() {
	var ids = MsgBoxUtil.collectIds("requestCheckbox");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids, "invite", requestDelInboxCallBack);
}

/**
 * 发件箱-批量删除.
 * 
 * @return
 */
function requestDeleteOutboxBatch() {
	var ids = MsgBoxUtil.collectIds("requestCheckbox");
	MsgBoxUtil.ajaxDeleteOutboxMessage(ids, "invite", requestDelOutboxCallBack);
}

/**
 * 收件箱-删除回调函数.
 * 
 * @return
 */
function requestDelInboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	requestAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxRequestInbox");
}

/**
 * 发件箱-删除回调函数.
 * 
 * @return
 */
function requestDelOutboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	requestAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxRequestOutbox");
}

/**
 * 处理请求.
 * 
 * @param msgId
 * @param id
 * @param type
 * @param inviteType
 * @param contentId
 * @return
 */
function requestHandler(obj,msgId, id, type, inviteType, contentId) {
	$(obj).disabled();
	var param;
	var inviteId = $("#content" + contentId).find("#inviteId").val();
	var groupId = $("#content" + contentId).find("#groupId").val();
	var groupNodeId = $("#content" + contentId).find("#groupNodeId").val();

	if (inviteType == 0) {
		param = {
			"actionKey" : type,
			"recvId" : id
		};
	}
	if (inviteType == 1) {
		param = {
			"actionKey" : type,
			"recvId" : id,
			"inviteType" : inviteType,
			"groupNodeId" : groupNodeId,
			"inviteId" : inviteId
		};
	}
	if (inviteType == 2) {
		param = {
			"actionKey" : type,
			"recvId" : id,
			"inviteType" : inviteType,
			"groupNodeId" : groupNodeId,
			"inviteId" : inviteId,
			"groupId" : groupId
		};
	}

	$.ajax( {
		url : snsctx + "/msgbox/ajaxHandlerRequest",
		type : "post",
		dataType : "json",
		timeout : 30000,
		data : param,
		success : function(json) {
		   if (json.ajaxSessionTimeOut) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}else{
					   $(obj).enabled();
				    }
		        });
		   } else {
		   	if (json.action == "success") {
		   		$.smate.scmtips.show("success", msgboxTip.optSuccess);
		   		if (type == 'accept') {
		   			viewTip(msgId, inviteType, 1);
		   		} else {
		   			viewTip(msgId, inviteType, 2);
		   			}
		   	} else if (json.action == "isuser") {
		   		$.smate.scmtips.show("warn", yourUser);
		   		viewTip(msgId, inviteType, 0);
		   	} else if (json.action == "presence") {
		   		$.smate.scmtips.show("warn", yourFriend);
		   		viewTip(msgId, inviteType, 0);
		   	} else if (json.action == "failure") {
		   		$.smate.scmtips.show("warn", actionFaild);
		   		viewTip(msgId, inviteType, 3);
		   	} else if (json.action == "exist") {
		   		$.smate.scmtips.show("warn", yourGroupMember);
		   		viewTip(msgId, inviteType, 5);
		   	} else if (json.action == "resolved") { // 群组邀请已处理
					$.smate.scmtips.show("warn", groupInviteResolved);
					viewTip(msgId, inviteType, 6);
				} else if (json.action == "delete" || json.action == "empty") {
					$.smate.scmtips.show("warn", execException);
					viewTip(msgId, inviteType, 3);
				} else if (json.action == "senderIsDel") {
					$.smate.scmtips.show("warn", inviteIsDel);
					viewTip(msgId, inviteType, 8);
				} else if (json.action == "inviteIsDel") {
					$.smate.scmtips.show("warn", inviteIsDel);
					viewTip(msgId, inviteType, 8);
				}
			   $('#invitation_tr' + msgId).removeClass('m_tr01');
				MsgBoxUtil.refreshMsgCount();
				$(obj).enabled();
		    }
	},
	error : function(e) {
		$(obj).enabled();
	}
	});
}

/**
 * 批处理请求.
 * 
 * @param type
 * @return
 */
function requestHandlerBatchBtn(_this,type) {
	$(_this).disabled();
	var obj = {
		"ids" : "",
		"groupNodeIds" : "",
		"inviteIds" : "",
		"groupIds" : "",
		"inviteTypes" : ""
	};

	requestHandlercollectData(obj,type);

	if (obj.ids.length == 0) {
		$.smate.scmtips.show("warn", msgboxTip.selectInvitationReq, locale == 'zh_CN' ? 300 : 370);
		$(_this).enabled();
		return false;
	}

	jConfirm(msgboxTip.cfmHandle, msgboxTip.dialoagTitle, function(r) {
		if (r) {
			requestHandlerBatch(_this,obj.ids, obj.inviteTypes, obj.groupNodeIds,
					obj.inviteIds, obj.groupIds,type);
		}else{
			$(_this).enabled();
		}
	});
}

function requestHandlercollectData(obj,type) {
	var contendId, ids = "", groupNodeIds = "", inviteIds = "", groupIds = "", inviteTypes = "";
	$("input[name='requestCheckbox']:checked").each(
			function() {
				var value = $(this).val();
				var button = $("#"+type+"Link" + value);
				if (button.length > 0) {
					contentId = button.attr("name");
					ids += value + ",";
					inviteIds += $('#content' + contentId).find('#inviteId')
					.val()
					+ ",";
					groupIds += $('#content' + contentId).find('#groupId')
					.val()
					+ ",";
					groupNodeIds += $('#content' + contentId).find(
					'#groupNodeId').val()
					+ ",";
					inviteTypes += $('#hidden' + contentId).val() + ",";
				}
	});
	obj.ids = ids.substring(0, ids.length - 1);
	obj.inviteIds = inviteIds.substring(0, inviteIds.length - 1);
	obj.groupIds = groupIds.substring(0, groupIds.length - 1);
	obj.groupNodeIds = groupNodeIds.substring(0, groupNodeIds.length - 1);
	obj.inviteTypes = inviteTypes.substring(0, inviteTypes.length - 1);
};

/**
 * 批处理请求.
 * 
 * @param ids
 * @param inviteTypes
 * @param groupNodeIds
 * @param inviteIds
 * @param groupIds
 * @param contentIds
 * @return
 */
function requestHandlerBatch(_this,ids, inviteTypes, groupNodeIds, inviteIds,
		groupIds, type) {
	var param = {
		"recvId" : ids,
		"inviteTypes" : inviteTypes,
		"groupNodeIds" : groupNodeIds,
		"inviteIds" : inviteIds,
		"groupIds" : groupIds,
		"actionKey":type
	};
	$.ajax( {
		url : snsctx + "/msgbox/ajaxHandlerRequestBatch",
		type : "post",
		dataType : "json",
		timeout : 30000,
		data : param,
		success : function(json) {
		   if (json.ajaxSessionTimeOut) {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}else{
					   $(_this).enabled();
				    }
		     	});
		   } else {
		   	if ("success" == json.action[0].result) {
		   		$.smate.scmtips.show("success", msgboxTip.optSuccess);
		   		for (var i = 0; i < json.data.length; i++) {
		   			viewTip(json.data[i].msgId, json.data[i].inviteType, json.data[i].optStatus);
		   		}
		   	}
		   	else 
		   		if ("failure" == json.action[0].result) {
		   			$.smate.scmtips.show("error", msgboxTip.optFailed);
		   		}
		   	//更新未读数
						MsgBoxUtil.refreshMsgCount();
						MsgBoxUtil.unSelectAll("requestCheckbox");
						$(_this).enabled();
					}
		},
		error : function(e) {
			$.smate.scmtips.show("error", msgboxTip.optFailed);
			$(_this).enabled();
		}
	});
}

/**
 * 处理请求后提示信息.
 * 
 * @param msgId
 * @param inviteType
 * @param optStatus
 * @return
 */
function viewTip(msgId, inviteType, optStatus) {
	var divTipObj = $("#handlerResult" + msgId);
	divTipObj.addClass("greenys");
	if (inviteType == 0) {
      if (optStatus == 1||optStatus==0) {
			divTipObj.html(fAcceptReq);
			divTipObj.show();
		} else if (optStatus == 2) {
			divTipObj.html(fRefuseReq);
			divTipObj.show();
		} else if (optStatus == 3) {
			divTipObj.html(execException);
			divTipObj.show();
		} else if (optStatus == 7) {
			divTipObj.html(fIgnoreReq);
			divTipObj.show();
		} else if (optStatus == 8) {
			divTipObj.html(inviteIsDel);
			divTipObj.show();
		}
	} else if (inviteType == 1) {
		if (optStatus == 1 || optStatus==5) {
			divTipObj.html(gAcceptReq);
			divTipObj.show();
		} else if (optStatus == 2) {
			divTipObj.html(gRefuseReq);
			divTipObj.show();
		} else if (optStatus == 3) {
			divTipObj.html(execException);
			divTipObj.show();
		} else if (optStatus == 7) {
			divTipObj.html(gIgnoreReq);
			divTipObj.show();
		}
	} else if (inviteType == 2) {
		if (optStatus == 1) {
			divTipObj.html(gAccept);
			divTipObj.show();
		} else if (optStatus == 2) {
			divTipObj.html(gRefuse);
			divTipObj.show();
		} else if (optStatus == 3) {
			divTipObj.html(execException);
			divTipObj.show();
		} else if (optStatus == 6) {
			divTipObj.html(gIgnoreApply);
			divTipObj.show();
		} else if (optStatus == 7) {
			divTipObj.html(gIgnoreReq);
			divTipObj.show();
		} else if (optStatus == 8) {
			divTipObj.html(inviteIsDel);
			divTipObj.show();
		}
	}
}

/**
 * 人员信息为空.
 */
function emptySenderTip(){
	$.smate.scmtips.show("success", msgboxTip.emptySender);
}