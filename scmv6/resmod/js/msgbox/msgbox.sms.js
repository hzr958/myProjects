/**
 * 选项卡切换.
 * 
 * @param name
 * @param cursel
 * @param n
 * @return
 */
function smsSetTab(name, cursel, n) {
	$("#msgbox_search").val(msgboxTip.searchMsg);
	$("#msgbox_search").css("color", "#999");
	if (cursel == "2") {
		$("#hidden-inOrOut").val(0);
	} else if (cursel == "3") {
		$("#hidden-inOrOut").val(1);
	}
	$("#hidden-msgboxStatus").val(2);
	for ( var i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

/**
 * 内容切换.
 * 
 * @return
 */
function smsAjaxHtml(replaceId, url) {
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
		   }else {
		   	$("div#con_one_2,div#con_one_3").each(function(){
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
 * 短信详情-收件箱.
 * 
 * @param smsId
 * @return
 */
function smsInboxMsg(des3SmsId) {
	if ($("#pageNo").length > 0) {
		$("#hidden-pageNo").val($("#pageNo").val());
		$("#hidden-pageSize").val($("#pageSize").val());
	}
	$(".Menubox").hide();
	$("#con_one_2").html(
			"<div class=\"morenews3\">" + msgboxTip.loading + "</div>");

	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxSmsInboxView",
		data : {
			"recvId" : encodeURIComponent(des3SmsId)
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
		   	MsgBoxUtil.refreshMsgCount();
				$("#hidden-reply,#hidden-forward,#hidden-approve").thickbox({
					resmod:resmod
				});

				$("#attachFileBlock").each(function(){
					$(this).show();
				});

				$(".replyContentBlock").each(function(){
					$(this).show();
				});
				$(".addFriendClass").each(function(){
					$(this).thickbox({
						snsctx:snsctx,
						resmod:resmod,
						parameters:{"d3d" : $(this).attr("des3Id")},
						type:"addRequests"
				   });
			  });
		   }
		},
		error : function(e) {
		}
	});
}

/**
 * 收件箱-状态.
 * 
 * @return
 */
function readSmsInboxMsg(status) {
	$("#hidden-msgboxStatus").val(status);
	smsAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxSmsInbox");
}

/**
 * 短信详情-发件箱.
 * 
 * @param des3SmsId
 * @return
 */
function smsOutboxMsg(des3SmsId) {
	if ($("#pageNo").length > 0) {
		$("#hidden-pageNo").val($("#pageNo").val());
		$("#hidden-pageSize").val($("#pageSize").val());
	}
	$(".Menubox").hide();
	$("#con_one_3").html(
			"<div class=\"morenews3\">" + msgboxTip.loading + "</div>");
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxSmsOutboxView",
		data : {
			"mailId" : encodeURIComponent(des3SmsId)
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
					   	$("#con_one_3").html(data);
							$("a.thickbox,input.thickbox").thickbox( {
								resmod : resmod
							});
							$("input[type=file]").filestyle( {
								image : resmod + "/images_v5/btn_choosefile.gif",
								imageheight : 22,
								imagewidth : 80,
								width : 260
							});
							$('#file-input100').watermark( {
								tipCon : msgboxTip.fileInputTip,
								blurClass : 'watermark'
							});
							$("#attachFileBlock").each(function(){
								$(this).show();
							});
	
							$(".replyContentBlock").each(function(){
								$(this).show();
							});
					   }
		},
		error : function(e) {
		}
	});
}

/**
 * 收件箱初始化上一封下一封.
 * 
 * @param des3SmsId
 * @return
 */
function smsInitLinkForInbox(des3SmsId) {
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxSmsInboxInitLink",
		dataType : "json",
		data : {
			"recvId" : encodeURIComponent(des3SmsId),
			"type" : "inside"
		},
		success : function(json) {
		   if (json.ajaxSessionTimeOut == "yes") {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   } else {
		   	if (json.action == "success") {
		   		if (json.prev == true) {
		   			$("#prevId").attr("href", "javascript:smsInboxMsg('" + json.prevId + "')");
		   			$("#prevId").attr("prevId", json.prevId);
		   		}
		   		else {
		   			$("#prevId").attr("href", "javascript:prevTips();");
		   		}
		   		
		   		if (json.next == true) {
		   			$("#nextId").attr("href", "javascript:smsInboxMsg('" + json.nextId + "')");
		   			$("#nextId").attr("nextId", json.nextId);
		   		}
		   		else {
		   			$("#nextId").unbind();
		   			$("#nextId").attr("href", "javascript:nextTips();");
		   		}
		   		
		   		$("#prevId").enabled();
		   		$("#nextId").enabled();
		   		$("#detailLinkDelete").enabled();
		   		
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
 * 发件箱初始化上一封下一封.
 * 
 * @param des3SmsId
 * @return
 */
function smsInitLinkForOutbox(des3SmsId) {
	$.ajax( {
		type : "post",
		url : snsctx + "/msgbox/ajaxSmsOutboxInitLink",
		dataType : "json",
		data : {
			"mailId" : encodeURIComponent(des3SmsId),
			"type" : "inside"
		},
		success : function(json) {
		   if (json.ajaxSessionTimeOut == "yes") {
		   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
		   		if (r) {
		   			document.location.reload();
		   		}
		   	});
		   }else {
		   	if (json.action == "success") {
		   		if (json.prev == true) {
		   			$("#prevId").attr("href", "javascript:smsOutboxMsg('" + json.prevId + "')");
		   			$("#prevId").attr("prevId", json.prevId);
		   		}
		   		else {
		   			$("#prevId").attr("href", "javascript:prevTips();");
		   		}
		   		if (json.next == true) {
		   			$("#nextId").attr("href", "javascript:smsOutboxMsg('" + json.nextId + "')");
		   			$("#nextId").attr("nextId", json.nextId);
		   		}
		   		else {
		   			$("#nextId").unbind();
		   			$("#nextId").attr("href", "javascript:nextTips();");
		   		}
		   		
		   		$("#prevId").enabled();
		   		$("#nextId").enabled();
		   		$("#detailLinkDelete").enabled();
		   		
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

/**
 * 加载.
 * 
 * @param msgboxFlag
 * @return
 */
function smsInitMainContent(msgboxFlag) {
	var url = snsctx + "/msgbox/";
	var replaceId = "";
	if(msgboxFlag==-1){
		smsSetTab('one',1,3);
		return;
	}

	if (msgboxFlag == 0) {// 收件箱
		url = url + "ajaxSmsInbox";
		replaceId = "con_one_2";
	} else if (msgboxFlag == 1) {// 发件箱
		url = url + "ajaxSmsOutbox";
		replaceId = "con_one_3";
	} else if (msgboxFlag == 2) {// 短信详情-收件箱
		url = url + "ajaxSmsInboxView";
		replaceId = "sms-right-wrap";
	} else if (msgboxFlag == 3) {// 短信详情-发件箱
		url = url + "ajaxSmsOutboxView";
		replaceId = "sms-right-wrap";
	} 
	smsAjaxHtml(replaceId, url);
}

/**
 * 未读短信数.
 * 
 * @return
 */
function smsInitInboxUnReadCount() {
	setTimeout(function() {
		var smsUnReadCount = $.trim($("#displaySmsCount").text());
		if (smsUnReadCount != "") {
			var totalCount = parseInt($("#msg_totalCount_sms").val());
			$("#label-sms-unread").text(smsUnReadCount);
			$("#label-sms-read").text(totalCount - parseInt(smsUnReadCount));

		}
	}, 500);
}

/**
 * 收件箱-批量删除消息.
 * 
 * @return
 */
function smsDeleteInboxBatch() {
	var ids = MsgBoxUtil.collectIds("smsCheckbox");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids, "inside", smsDeleteInboxCallBack);
}

/**
 * 收件箱-单个删除.
 * 
 * @return
 */
function smsDeleteInboxOne() {
	var ids = $(this).attr("mailId");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids, "inside", smsDeleteInboxCallBack);
}

/**
 * 收件箱-详情删除.
 * 
 * @param id
 * @return
 */
function smsDeleteInboxDetail(id) {
	MsgBoxUtil.ajaxDeleteInboxMessage(id, "inside",
			smsDeleteInboxDetailCallBack);
}

/**
 * 收件箱-详情删除回调函数.
 * 
 * @return
 */
function smsDeleteInboxDetailCallBack() {
	// 首先判断是否有下一条，否则上一条，在否则回到收件箱列表中去。
	var nextId = $("#nextId").attr("nextId");
	if (nextId == null || typeof (nextId) == "undefined") {
		var prevId = $("#prevId").attr("prevId");
		if (prevId == null || typeof (prevId) == "undefined") {
			document.location.href = snsctx + "/msgbox/smsMain";
		} else {
			smsInboxMsg(prevId);
		}
	} else {
		smsInboxMsg(nextId);
	}
}

/**
 * 收件箱-删除回调函数.
 * 
 * @param ids
 * @return
 */
function smsDeleteInboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	smsAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxSmsInbox");
}

/**
 * 发件箱-批量删除系统消息.
 * 
 * @return
 */
function smsDeleteOutboxBatch() {
	var ids = MsgBoxUtil.collectIds("smsCheckbox");
	MsgBoxUtil.ajaxDeleteOutboxMessage(ids, "inside", smsDeleteOutboxCallBack);
}

/**
 * 发件箱-单个删除.
 * 
 * @return
 */
function smsDeleteOutboxOne() {
	var ids = $(this).attr("mailId");
	MsgBoxUtil.ajaxDeleteOutboxMessage(ids, "inside", smsDeleteOutboxCallBack);
}

/**
 * 发件箱-删除回掉调数.
 * 
 * @param ids
 * @return
 */
function smsDeleteOutboxCallBack(ids) {
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	smsAjaxHtml("con_one_3", snsctx + "/msgbox/ajaxSmsOutbox");
}

/**
 * 收件箱-详情删除.
 * 
 * @param id
 * @return
 */
function smsDeleteOutboxDetail(id) {
	MsgBoxUtil.ajaxDeleteOutboxMessage(id, "inside",
			smsDeleteOutboxDetailCallBack);
}

/**
 * 收件箱-详情删除回调函数.
 * 
 * @return
 */
function smsDeleteOutboxDetailCallBack() {
	// 首先判断是否有下一条，否则上一条，在否则回到收件箱列表中去。
	var nextId = $("#nextId").attr("nextId");
	if (nextId == null || typeof (nextId) == "undefined") {
		var prevId = $("#prevId").attr("prevId");
		if (prevId == null || typeof (prevId) == "undefined") {
			document.location.href = snsctx + "/msgbox/smsMain?msgboxFlag=1";
		} else {
			smsOutboxMsg(prevId);
		}
	} else {
		smsOutboxMsg(nextId);
	}
}

/**
 * 标记为未读.
 * 
 * @return
 */
function smsMarkUnreads() {
	var ids = MsgBoxUtil.collectIds("smsCheckbox");
	MsgBoxUtil.ajaxMarkMessageStatus(ids, "inside", "unread",
			smsMarkUnreadsCallBack);
}

/**
 * 收件箱-详情.
 * 
 * @return
 */
function smsInMarkUnread(id) {
	MsgBoxUtil.ajaxMarkMessageStatus(id, "inside", "unread", null);
}

/**
 * 标记为已读.
 * 
 * @return
 */
function smsMarkReads() {
	var ids = MsgBoxUtil.collectIds("smsCheckbox");
	MsgBoxUtil.ajaxMarkMessageStatus(ids, "inside", "read",
			smsMarkReadsCallBack);
}

/**
 * 标记为未读回调函数.
 * 
 * @param ids
 * @return
 */
function smsMarkUnreadsCallBack(ids) {
	var idArray = ids.split(",");
	var trObj;
	for ( var i = 0; i < idArray.length; i++) {
		trObj = $("#tr" + idArray[i]);
		trObj.attr("class", "m_tr01");
		$("#img" + idArray[i]).attr("src", resmod + "/images_v5/mail01.gif");
		$("#smslist_title" + idArray[i]).attr("class", "Blue b");
	}
}

/**
 * 标记为已读回调函数.
 * 
 * @param ids
 * @return
 */
function smsMarkReadsCallBack(ids) {
	var idArray = ids.split(",");
	var trObj;
	for ( var i = 0; i < idArray.length; i++) {
		trObj = $("#tr" + idArray[i]);
		trObj.removeClass();
		$("#img" + idArray[i]).attr("src", resmod + "/images_v5/mail02.gif");
		$("#smslist_title" + idArray[i]).attr("class", "Blue");
	}
}

/**
 * 上传文件回调函数
 * 
 * @param fileName
 * @param fileId
 * @param des3FileId
 * @param currentNodeId
 * @return
 */
function fileAddHandler(data) {
	var attachObj = $("#localAttach_temp").clone();
	attachObj.attr("id", "attachFileDiv");
	var fileName = data.fileName;
	fileName = common.htmlDecode(fileName);
	attachObj.find(".attachName").attr("title", fileName);
	var attachLinkObj = attachObj.find(".attachName").find("a");
	attachLinkObj.attr("href", $("#smsAttachDomain").val() + data.fileLink);
	attachLinkObj.text(fileName);
	attachObj.find(".input_attachFileId").val(data.fileId);
	attachObj.find(".input_des3attachFileId").val(data.des3FileId);
	$("#attachFileList").append(attachObj);
	attachObj.show();
	var attachFileTrObj = $("#attachFileTr");
	if (attachFileTrObj.length > 0) {
		attachFileTrObj.show();
	}

	$.Thickbox.closeWin();
}

/**
 * 回复短信.
 * 
 * @return
 */
function smsAjaxReply(receiverId, receiverName, id) {
	var replyTitle = "";
	var replyContent = "";
	if(typeof id != 'undefined') {
		$.ajax( {
			type : "post",
			url : snsctx + "/msgbox/ajaxGetSmsInboxContent",
			data : {
				"recvId" : id
			},
			dataType : "json",
			cached : false,
			success : function(data) {
				var timeOut = data['ajaxSessionTimeOut'];
				if (timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes') {
					jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
						if (r) {
							document.location.reload();
						}
					});
				} else {
					$("#attachFileBlock").remove();
					$("#content").append(data.fileList);
					
					replyTitle = msgboxTip.reply + $("#smslist_title" + id).text();
					replyContent = "<br/><br/><hr style=\"height:1px;border:none;border-top:1px solid #e9e9e9\"/>"
						+ msgboxTip.title
						+ $("#smslist_title" + id).text()
						+ "&nbsp;&nbsp;("
						+ $("#receive-time" + id).text()
						+ ")<br/>"
						+ "<div id=\"twice_content\">"
						+ data.content
						+ "</div>";
					msgboxConfig = {
							"sendTitle" : replyTitle,
							"sendContent" : replyContent,
							"selectFriendable" : 1,
							"sendPsn" : [ {
								"psnId" : receiverId,
								"psnName" : receiverName
							} ],
							"hasAttach" : 1,
							"attachList" : [],
							"isForward" : 0
						};

					$("#hidden-reply").click();
				}
			},
			error : function(e) {
			}
		});
	} else {
		replyTitle = msgboxTip.reply + $("#theme_title").text();
		var replyContentObj = $("#smsObjContent").clone();
		// 去除附件
		replyContentObj.find("#attachFileBlock").remove();
		replyContentObj.find(".addFriendClass").remove();
		replyContent = "<br/><br/><hr style=\"height:1px;border:none;border-top:1px solid #e9e9e9\"/>"
			+ msgboxTip.title
			+ $("#theme_title").text()
			+ "&nbsp;&nbsp;("
			+ $("#receive-time").text()
			+ ")<br/>"
			+ "<div id=\"twice_content\">"
			+ replyContentObj.html().replace(
					/<a[^<>]*scmflag="hidden"[\s\S]*?<\/a>/gi, '') + "</div>";
		msgboxConfig = {
				"sendTitle" : replyTitle,
				"sendContent" : replyContent,
				"selectFriendable" : 1,
				"sendPsn" : [ {
					"psnId" : receiverId,
					"psnName" : receiverName
				} ],
				"hasAttach" : 1,
				"attachList" : [],
				"isForward" : 0
			};

		$("#hidden-reply").click();
	}
}

/**
 * 转发短信.
 * 
 * @return
 */
function smsAjaxForward(id) {
	var forwardTitle = "";
	var forwardContent = "";
	if(typeof id != 'undefined') {
		$.ajax( {
			type : "post",
			url : snsctx + "/msgbox/ajaxGetSmsInboxContent",
			data : {
				"recvId" : id
			},
			dataType : "json",
			cached : false,
			success : function(data) {
				var timeOut = data['ajaxSessionTimeOut'];
				if (timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes') {
					jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
						if (r) {
							document.location.reload();
						}
					});
				} else {
					$("#attachFileBlock").remove();
					$("#content").append(data.fileList);
					
					forwardTitle = msgboxTip.forward + $("#smslist_title" + id).text();
					forwardContent = "<br/><br/><hr style=\"height:1px;border:none;border-top:1px solid #e9e9e9\"/>"
						+ msgboxTip.title
						+ $("#smslist_title" + id).text()
						+ "&nbsp;&nbsp;("
						+ $("#receive-time" + id).text()
						+ ")<br/>"
						+ "<div id=\"twice_content\">"
						+ data.content
						+ "</div>";
					msgboxConfig = {
							"sendTitle" : forwardTitle,
							"sendContent" : forwardContent,
							"selectFriendable" : 1,
							"sendPsn" : [],
							"hasAttach" : 1,
							"isForward" : 1
						};
					
					$("#hidden-forward").click();
				}
			},
			error : function(e) {
			}
		});
	} else {
		forwardTitle = msgboxTip.forward + $("#theme_title").text();
		var forwardContentObj = $("#smsObjContent").clone();
		// 去除附件
		forwardContentObj.find("#attachFileBlock").remove();
		forwardContentObj.find(".addFriendClass").remove();
		var forwardContent = "<br/><br/><hr style=\"height:1px;border:none;border-top:1px solid #e9e9e9\"/>"
				+ msgboxTip.title
				+ $("#theme_title").text()
				+ "&nbsp;&nbsp;("
				+ $("#receive-time").text()
				+ ")<br/>"
				+ "<div id=\"twice_content\">"
				+ forwardContentObj.html().replace(
						/<a[^<>]*scmflag="hidden"[\s\S]*?<\/a>/gi, '') + "</div>";

		msgboxConfig = {
			"sendTitle" : forwardTitle,
			"sendContent" : forwardContent,
			"selectFriendable" : 1,
			"sendPsn" : [],
			"hasAttach" : 1,
			"isForward" : 1
		};

		$("#hidden-forward").click();
	}
}

/**
 * 同意分享成果全文.
 * 
 * @return
 */
function smsAjaxApproveAttach() {
	var hrefStr = "";
	$(".pub_attach_link").each(function() {
		$(this).attr("onclick", "");
		hrefStr = $(this).parent().find("input").val();
		$(this).attr("href", hrefStr);
	});

	var forwardTitle = msgboxTip.reply + $("#theme_title").text();
	var forwardContentObj = $("#smsObjContent").clone();
	// 去除附件
	forwardContentObj.find("#attachFileBlock").remove();
	var forwardContent = "<br/><br/><hr style=\"height:1px;border:none;border-top:1px solid #e9e9e9\"/>"
			+ msgboxTip.title
			+ $("#theme_title").text()
			+ "&nbsp;&nbsp;("
			+ $("#receive-time").text()
			+ ")<br/>"
			+ "<div id=\"twice_content\">"
			+ forwardContentObj.html().replace(
					/<a[^<>]*scmflag="hidden"[\s\S]*?<\/a>/gi, '') + "</div>";

	msgboxConfig = {
		"sendTitle" : forwardTitle,
		"sendContent" : forwardContent,
		"selectFriendable" : 1,
		"sendPsn" : [ {
			"psnId" : $("#senderId_hidden").val(),
			"psnName" : $("#senderName_link").text()
		} ],
		"msgType" : 50,
		"hasAttach" : 1,
		"isForward" : 1
	};

	$("#hidden-approve").click();
}

// 提交到成果检索页面
function frmPubSubmit(path) {
	$("#forwardUrl").val(path);
	$("#ownerUrl").val(path);
	$("#frmPublicationSearch").submit();
}
/**
 * 人员信息为空.
 */
function emptySenderTip(){
	$.smate.scmtips.show("success", msgboxTip.emptySender);
}

function attachDownloadHandler(url){
    if(url.indexOf("downLoadWithPermission")!=-1){
        var newUrl=url;
        newUrl=newUrl.replace("downLoadWithPermission","judgePermission");
        $.ajax({
            type:"post",
            url:newUrl,
            data:{},
            dataType:"json",
            success:function(data){
            if(data.result==1){
                window.open(url);
            }else if(data.result==0){
            	$.smate.scmtips.show("warn", msgboxTip.noPermissionTip);
            }
            },
            error:function(e){
            }
        });
    }
}

function add_friendReq_callBack(result,psnId){
	if(result){
		$.smate.scmtips.show('success',msgboxTip.friend_invite_success, locale != 'zh_CN' ? 360 : 300);
		$("#lab"+psnId).addClass("f999");
		$("#lab"+psnId).html(msgboxTip.common_load_label_issend);
	}else{
		$.smate.scmtips.show('error',msgboxTip.friend_invite_fail);
	}
}

function openFile(fileId, nodeId, type){
	location.href = snsctx + "/file/download?des3Id=" + fileId + "&nodeId="+ nodeId + "&type=" + type+"&flag=5";
}
