/**
 * 选项卡切换.
 * 
 * @param name
 * @param cursel
 * @param n
 * @return
 */
function ftRequestSetTab(name, cursel, n) {
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
			"opStatus" : status
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
			}else{
				$("div[id*=con_one_]").each(function() {
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
		requestAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxFTRequestInbox");
	}else{
		requestSetTab('one', 2, 2);
		requestAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxFTRequestOutbox");
	}
}

/**
 * 未读请求数.
 * 
 * @return
 */
function ftRequestInitInboxUnReadCount() {
	var ftRequestUnReadCount = $.trim($("#displayFTRequestCount").text());
	if (ftRequestUnReadCount != "") {
		$("#label-ftrequest-unread").text(ftRequestUnReadCount);
	}
}

/**
 * 已处理、未处理请求.
 * 
 * @return
 */
function ftReadRequestInbox(opStatus) {
	$("#hidden-msgboxStatus").val(opStatus);
	requestAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxFTRequestInbox");
}

/**
 * 收件箱-批量删除.
 * 
 * @return
 */
function ftRequestDeleteInboxBatch() {
	var ids = MsgBoxUtil.collectIds("ftrequestCheckbox");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids, "ftrequest", ftRequestDelInboxCallBack);
}

/**
 * 发件箱-批量删除.
 * 
 * @return
 */
function ftRequestDeleteOutboxBatch() {
	var ids = MsgBoxUtil.collectIds("ftrequestCheckbox");
	MsgBoxUtil.ajaxDeleteOutboxMessage(ids, "ftrequest", ftRequestDelOutboxCallBack);
}

/**
 * 收件箱-删除回调函数.
 * 
 * @return
 */
function ftRequestDelInboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	requestAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxFTRequestInbox");
}

/**
 * 发件箱-删除回调函数.
 * 
 * @return
 */
function ftRequestDelOutboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	requestAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxFTRequestOutbox");
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
function ftRequestHandler(obj,inboxId,opStatus) {
	$("#handle_inbox").val(inboxId);
	$.ajax( {
		url : snsctx + "/msgbox/ajaxHandleFTRequest",
		type : "post",
		dataType : "json",
		timeout : 30000,
		data : {
			"inboxId":inboxId,
			"opStatus":opStatus
		},
		success : function(data) {
	    if(data.ajaxSessionTimeOut){
			  jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r) {
				   if (r) {
						document.location.reload();
				    }
			   });
		}else{
			if (data.result == "success") {
				$.smate.scmtips.show("success", msgboxTip.optSuccess);
				if(opStatus==1){//同意
					$("#tip_td"+inboxId).html("<p class=\"mright20 greenys\" style=\"text-align:right;\">"+msgboxTip.ftRequestApproveTip+"</p>");
				}else if(opStatus==2){//拒绝
					$("#tip_td"+inboxId).html("<p class=\"mright20 greenys\" style=\"text-align:right;\">"+msgboxTip.ftRequestRefuseTip+"</span>");
				}
			   $('#ftrequest_tr' + inboxId).removeClass('m_tr01');
			   MsgBoxUtil.refreshMsgCount();
			} else if(data.result=="none"){
					$('#upload_fulltext_hidden').attr('alt', snsctx
									+ '/folder/showfullltext?pubId=' + $(obj).attr("resId") + '&nodeId=' 
									+ $(obj).attr("resNode") + '&actionType=1&type=1&TB_iframe=true&height=340&width=570').click();
			}
		}
	},
	error : function(e) {
	}
	});
}

/**
 * 上传全文回调函数.
 */
function callbackHandleeFtRequest(fileId,nodeId,fileName) {
	var inboxId=$("#handle_inbox").val();
	$.ajax({
		type:"post",
		url:snsctx+"/msgbox/ajaxCallBackHandleFTRequest",
		data:{
			"inboxId":inboxId,
			"fullTextId":fileId,
			"fullTextNode":nodeId,
			"fullTextName":fileName	
		},
		dataType:"json",
		success:function(data){
			if(data.result=="success"){
			//修改页面状态
			$("#tip_td"+inboxId).html("<span class=\"mright20 greenys\">"+msgboxTip.ftRequestApproveTip+"</span>");
		   $('#ftrequest_tr' + inboxId).removeClass('m_tr01');
		   MsgBoxUtil.refreshMsgCount();
			}
			
		},error:function(e){
			$.smate.scmtips.show("error", msgboxTip.optFailed);
		}
	});
};

/**
 * 人员信息为空.
 */
function emptySenderTip(){
	$.smate.scmtips.show("success", msgboxTip.emptySender);
}