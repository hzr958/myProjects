var msgboxSend;

MsgboxSend = function(title, content, forward) {
	this.title = title;
	this.content = content;
	this.forward = forward;
	this.init = MsgboxSend.init;
	this.handlerFileUpload = MsgboxSend.handlerFileUpload;
	this.cancelFileUpload = MsgboxSend.cancelFileUpload;
	this.ajaxFileUpload = MsgboxSend.ajaxFileUpload;
	this.submitAttachFile = MsgboxSend.submitAttachFile;
	this.deleteAttachFile = MsgboxSend.deleteAttachFile;
	this.checkLess = MsgboxSend.checkLess;
	this.sumitMessage = MsgboxSend.sumitMessage;
	this.validateMessage = MsgboxSend.validateMessage;
	this.showFriendTbox = MsgboxSend.showFriendTbox;
	this.showGroupMemberTbox = MsgboxSend.showGroupMemberTbox;
	this.wrapperData = MsgboxSend.wrapperData;
};

/**
 * 初始化.
 */
MsgboxSend.init = function() {
	if (parent.msgboxConfig != null && parent.msgboxConfig) {
		if (typeof parent.msgboxConfig.friendOrGroupMember != 'undefined') {// 判断是否选择为联系人或者是选择群组成员
																			// 默认是选择联系人
			if (parent.msgboxConfig.friendOrGroupMember == 1) {
				$("#selectFriendBtn").thickbox( {
					snsctx : snsctx,
					resmod : resmod,
					parameters : {
						"divId" : "receiverDiv"
					},
					type : "showfriend"
				});
				$("#groupMemberBtn").hide();
				$("#groupMemberTip").hide();
				// 下拉提示初始化
				autowordArr = $("#receiverDiv,#receiverDivOnly").autoword( {
					"split_char" : [],
					"enter" : false,
					"words_max":50,
					"onlyText":false,
					"select" : $.auto["friend_name"],
					"blur" : function() {
						$.autoword["receiverDiv"].clear_input();
					}
				});
			} else if (parent.msgboxConfig.friendOrGroupMember == 2) {
				var des3GroupId = parent.msgboxConfig.des3GroupId;
				var groupNodeId = parent.msgboxConfig.groupNodeId;
				$("#selectGroupMemberBtn").thickbox( {
					snsctx : snsctx,
					resmod : resmod,
					parameters : {
						"divId" : "receiverDiv",
						"des3GroupId" : des3GroupId,
						"groupNodeId" : groupNodeId
					},
					type : "groupPopGroupMember"
				});
				// 下拉提示初始化
				autowordArr = $("#receiverDiv,#receiverDivOnly").autoword( {
					"split_char" : [],
					"enter" : false,
					"words_max":50,
					"onlyText":false,
					"select" : $.auto["groupMember_name"],
					"blur" : function() {
						$.autoword["receiverDiv"].clear_input();
					}
				});
				$("#receiverDiv").width(locale == 'zh_CN' ? 473 : 419);
				$("#friendBtn").hide();
				$("#friendTip").hide();
			}
		} else {
			$("#selectFriendBtn").thickbox( {
				snsctx : snsctx,
				resmod : resmod,
				parameters : {
					"divId" : "receiverDiv"
				},
				type : "showfriend"
			});
			autowordArr = $("#receiverDiv,#receiverDivOnly").autoword( {
				"split_char" : [],
				"enter" : false,
				"words_max":50,
				"onlyText":false,
				"select" : $.auto["friend_name"],
				"blur" : function() {
					$.autoword["receiverDiv"].clear_input();
				}
			});
			$("#groupMemberBtn").hide();
			$("#groupMemberTip").hide();
		}

		var title = parent.msgboxConfig.sendTitle;
		if (title != undefined && title.length > 150) {
			title = title.substring(0, 150);
		}
		$("#smsTitle").val(title);
		$("#smsContent").html(parent.msgboxConfig.sendContent);
		var smsContentLength = common.textLengthCount($("#smsContent").text());
		if (smsContentLength > 2000) {
			$("#smsContent_countLabel").css( {
				"color" : "red",
				"font-weight" : "bold"
			});
		}
		$("#smsContent_countLabel").text(smsContentLength);
		var sendPsnArray = parent.msgboxConfig.sendPsn;
		if (parent.msgboxConfig.selectFriendable == 1) {
			$("#autowordDiv").val("receiverDiv");
			for ( var i = 0; i < sendPsnArray.length; i++) {
				autowordArr["receiverDiv"].put(sendPsnArray[i].psnId,
						sendPsnArray[i].psnName);
			}
		} else {
			$("#tr-receiverR").show();//把这个提前show出来时应为插件要计算隐藏的高低
			$("#autowordDiv").val("receiverDivOnly");
			for ( var i = 0; i < sendPsnArray.length; i++) {
				autowordArr["receiverDivOnly"].put(sendPsnArray[i].psnId,
						sendPsnArray[i].psnName);
			}
			$("#tr-receiverWR").hide();
		}

		if(parent.msgboxConfig.msgType==50){
			$("#choose_file_tr").remove();
		}
		
		if (parent.msgboxConfig.hasAttach == 0) {
			$("#p-attach").hide();
		} else {// 附件
			if (parent.$("#attachFileBlock").length > 0) {
				$("#attachFileList").html(parent.$("#attachFileBlock").html());
				$("#attachFileList").find("a.box-delete").each(function() {
					$(this).show();
				});
				$("#attachFileList").parent().show();
			}
		}
	}

};

/**
 * 上传附件前处理.
 */
MsgboxSend.handlerFileUpload = function(clickId, isModify) {
	var attachListObj = $("#attachFileList").find("#attachFileDiv");
	if (attachListObj.length >= 5) {
		$.smate.scmtips.show("warn", msgboxTip.addAttachmentWarnTip);
		return false;
	} else {
		$("#" + clickId).click();
		if (typeof isModify != undefined && isModify) {
			parent.$.Thickbox.modifyHeight(1, 150, 500);
			var old = $('#sendMsg_thickbox').height();
			$('#sendMsg_thickbox').height(old + 150);
		}
		$("#attachFileList").find("#attachFileDiv").each(function() {
			if ($(this).attr("isMine") == "1") {
				checkAttachFile.push({
					"fileId" : $(this).find(".input_fileId").val(),
					"attachId" : $(this).find(".input_attachFileId").val(),
					"des3AttachId" : $(this).find(".input_des3attachFileId").val(),
					"fileName" : $(this).find(".attachName").find("a").text(),
					"url" : $(this).find(".attachName").find("a").attr("href")
				});
			}
		});
	}
};

/**
 * 确认附件处理(我的文件).
 */
MsgboxSend.submitAttachFile = function(obj) {// 确认发送附件
	var attachCKB = $("input[name='attachCKB']:checked");
	if (parent.checkAttachFile.length == 0) {
		$.smate.scmtips.show("warn", msgboxTip.addAttachmentReq);
		return false;
	}
	$(obj).attr("disabled", "disabled");
	// 清空父页面附件中我的文件
	parent.$("#attachFileList").find("#attachFileDiv").each(function() {
		if ($(this).attr("isMine") == "1") {
			$(this).remove();
		}
	});

	for (var index in parent.checkAttachFile) {
		// 重新添加我的文件
		var item = parent.checkAttachFile[index];
		var attachObj = parent.$("#attach_temp").clone();
		attachObj.attr("id", "attachFileDiv");
		attachObj.attr("isMine", 1);
		attachObj.find(".attachName").find("a").addClass("Blue").attr("href", item.url).attr("title", item.fileName).text(item.fileName);
		attachObj.find(".input_attachFileId").val(item.attachId);
		attachObj.find(".input_des3attachFileId").val(item.des3AttachId);
		attachObj.find(".input_fileId").val(item.fileId);
		parent.$("#attachFileList").append(attachObj);
		attachObj.show();
		var attachFileTrObj = parent.$("#attachFileTr");
		if (attachFileTrObj.length > 0) {
			attachFileTrObj.show();
		}
	}

	parent.$.Thickbox.closeWin();

};

/**
 * 删除发送附件.
 */
MsgboxSend.deleteAttachFile = function(obj) {
	jConfirm(msgboxTip.cfmRemoveAttachment, msgboxTip.dialoagTitle,
			function(r) {
				if (r) {
					$(obj).parent().remove();
				}
				//弹出删除上传文件的提示框后给"#smsContent"设置焦点
				var t=$("#smsContent").html();
				$("#smsContent").html("").focus().html(t);
			});
};

/**
 * 最少5个附件检查.
 */
MsgboxSend.checkLess = function(obj) {
	// 获取父页面已有的附件个数
	var currentLength = 0;
	parent.$("#attachFileList").find("#attachFileDiv").each(function() {
		var isMine = $(this).attr("isMine");
		if (isMine == null || typeof (isMine) == "undefined") {
			currentLength++;
		}
	});
	if (parent.checkAttachFile.length + currentLength >= 5) {
		$(obj).attr("checked", false);
		$.smate.scmtips.show("warn", msgboxTip.addAttachmentWarnTip,
				locale == "zh_CN" ? 300 : 330);
	} else {
		var tempIndex = -1;
		for (var index in parent.checkAttachFile) {
			var item = parent.checkAttachFile[index];
			if (item.fileId == $(obj).attr("fileId")) {
				tempIndex = index;
				break;
			}
		}
		if ($(obj).attr("checked")) {
			if (tempIndex == -1) {
				parent.checkAttachFile.push({
					"fileId" : $(obj).attr("fileId"),
					"attachId" : $(obj).attr("attachId"),
					"des3AttachId" : $(obj).attr("des3AttachId"),
					"fileName" : $(obj).attr("fileName"),
					"url" : $(obj).parent().parent().find("td.tdLink").find("a").attr("href")
				});
			}
		} else {
			if (tempIndex > -1) {
				if (tempIndex == 0) {
					parent.checkAttachFile.shift();
				} else {
					parent.checkAttachFile = parent.checkAttachFile.slice(0, tempIndex).concat(
							parent.checkAttachFile.slice(parseInt(tempIndex) + 1, parent.checkAttachFile.length));
				}
			}
		}
	}
};

/**
 * 发送短信. flag标识是否关闭弹出层
 */
MsgboxSend.sumitMessage = function(flag) {
	$('#sendMsgBtn').disabled();
	var outter = this;
	if (!this.validateMessage(flag)) {
		$('#sendMsgBtn').enabled();
		return false;
	}
	
	var smsContent = $("#smsContent").html();

	if (smsContent != "" && smsContent != undefined) {
		smsContent = smsContent.replace(new RegExp("disabled", "gm"), "");
	}

	var psnArray = autowordArr[$("#autowordDiv").val()].vals();
	var receivers = "";
	for ( var i = 0; i < psnArray.length; i++) {
		if (receivers == "")
			receivers = psnArray[i];
		else
			receivers = receivers + "," + psnArray[i];
	}

	//修正msgType和insideType参数值的设置问题_MJG_2013-04-03_SCM-1854.
	var msgType = 14;//14-默认不发送通知邮件.
	if((typeof messageMsgType != undefined)&&(typeof messageMsgType != 'undefined')){
		msgType=messageMsgType;
	}
	if (flag||flag=='1') {
		if (parent.msgboxConfig.msgType != null
				&& typeof (parent.msgboxConfig.msgType) != "undefined") {
			msgType = parent.msgboxConfig.msgType;
		}
	}
	
	var json = {
		"receivers" : receivers,
		"title" : $("#" + this.title).val(),
		"content" : smsContent,
		"insideType" : "12",
		"msgType" : msgType
	};
	
	var groupId = $("#groupId").val();
	if (groupId != null && groupId != '' && groupId != 'undefined') {
		json["groupId"] = groupId;
	}
	
	var gId = $("#gId").val();
	if (gId != null && gId != '' && gId != 'undefined') {
		json["gId"] = Number(gId);
	}
	
	var groupName = $("#groupName").val();
	if (groupName != null && groupName != 'undefined') {
		json["groupName"] = groupName;
	}
	
	var fileList = new Array();
	$("#attachFileList").find("div#attachFileDiv").each(function() {
		var aObj = $(this).find("div > a");
		var downloadUrl = aObj.attr("href");
		var fileName = aObj.text();
		fileList.push({
			fileId : $(this).find(".input_fileId").val(),
			attachFileId : $(this).find(".input_attachFileId").val(),
			des3AttachFileId : $(this).find(".input_des3attachFileId").val(),
			fileName : fileName,
			downloadUrl : downloadUrl
		});
	});
	if (fileList.length > 0) {
		json["jsonFile"] = JSON.stringify(fileList);
	}

	var isSharePubUrl = $("#sharePubUrl").attr("id") == "sharePubUrl";
	var isShareFileUrl = $("#shareFileUrl").attr("id") == "shareFileUrl";

	if (isSharePubUrl) {
		json["isSharePubUrl"] = isSharePubUrl;
		json["sharePubUrl"] = $("#sharePubUrl").html();
	} else if (isShareFileUrl) {
		json["isShareFileUrl"] = isShareFileUrl;
		json["shareFileUrl"] = $("#shareFileUrl").html();
	}

	$.ajax( {
		url : snsctx + "/msgbox/ajaxSendMessage",
		type : "post",
		dataType : "json",
		timeout : 30000,
		data : json,
		error : function(e) {
			$.smate.scmtips.show("error", msgboxTip.sendMsgFailed);
		},
		success : function(data) {
			if (data && data.action == "success") {
				$.smate.scmtips.show("success", msgboxTip.sendMsgSuccess);
				//添加访问记录
				addRecentRecord(receivers);
				if (flag) {
					setTimeout(function() {
						parent.$.Thickbox.closeWin();
					}, 1000);
				} else {
					$("#" + outter.title).val("");
					$("#" + outter.content).html("");
					$("#attachFileList").html("");
					$("#" + outter.content + "_countLabel").text(0);
					autowordArr[$("#autowordDiv").val()].clear();
				}
				$('#sendMsgBtn').enabled();
			}
		}
	});

};

//添加最近访问记录
function addRecentRecord(psnIdList){
	$.ajax({
		url:snsctx+"/friend/ajaxAddRecord",
		type:"post",
		dataType:"json",
		data:{"psnListStr":psnIdList},
		async:false,
		success:function(data){
			if(data!=null && data.ajaxSessionTimeOut=='yes'){
				location.reload(true);
		    }
		},
		error:function(){
		}
	});
};

// 验证数据
MsgboxSend.validateMessage = function(flag) {
	if (autowordArr[$("#autowordDiv").val()].vals() == 0) {
		if (typeof parent.msgboxConfig != 'undefined'
				&& typeof parent.msgboxConfig.friendOrGroupMember != 'undefined'
				&& parent.msgboxConfig.friendOrGroupMember == 2) {
			$.smate.scmtips.show("warn", msgboxTip.selectGroupMemReq);
		} else {
			$.smate.scmtips.show("warn", msgboxTip.selectFriendReq);
		}
		return false;
	}
	if ($("#" + this.title).val() == ""
			|| $.trim($("#" + this.title).val()).length == 0) {
		$.smate.scmtips.show("warn", msgboxTip.titleReq);
		return false;
	}

	var smsContentText = $.trim($("#smsContent").text());
	var inputLength = 0;
	inputLength = common.textLengthCount(smsContentText);

	if (smsContentText == "" || inputLength == 0) {
		$.smate.scmtips.show("warn", msgboxTip.contentReq, locale != 'zh_CN' ? 330
				: 300);
		return false;
	} else if (inputLength > 2000) {
		$.smate.scmtips.show("warn", msgboxTip.lengthWarn);
		return false;
	}

	return true;
};

MsgboxSend.showFriendTbox = function() {
	$('#selectFriendBtn').click();
	parent.$.Thickbox.modifyHeight(1, 150, 500);
	var old = $('#sendMsg_thickbox').height();
	$('#sendMsg_thickbox').height(old + 150);
};

MsgboxSend.showGroupMemberTbox = function() {
	$('#selectGroupMemberBtn').click();
	parent.$.Thickbox.modifyHeight(1, 150, 500);
	var old = $('#sendMsg_thickbox').height();
	$('#sendMsg_thickbox').height(old + 150);
};

MsgboxSend.wrapperData = function() {
	var smsContent = $("#" + this.content).html();
	if (smsContent.length == 0)
		return "";
	var s = "";
	s = smsContent.replace(/</g, "&lt;");
	s = s.replace(/>/g, "&gt;");
	s = s.replace(/ /g, "&nbsp;");
	s = s.replace(/\'/g, "&#39;");
	s = s.replace(/\"/g, "&quot;");
	s = s.replace(/\r\n/g, "<br>");
	return s;
};
