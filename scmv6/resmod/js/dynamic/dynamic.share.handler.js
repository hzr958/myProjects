var dynamicShareHandler = dynamicShareHandler ? dynamicShareHandler : {};

dynamicShareHandler.submitShare = function(obj) {
		var shareType = $("input[name='shareTypeRadio']:checked").val();
		if (shareType == 5) {
			if(locale=='en_US'){
				$.scmtips.show("warn", "you would not choose a social website you want to share");
			}else{
				$.scmtips.show("warn", "您还没有选择想要分享到的站点");
			}
			
		//parent.$.Thickbox.closeWin();
		}
		else {
			setTimeout(function(){
				$(obj).disabled();
				$("#loadTip").show();
				if (shareType == 1) {// 发布到我的动态
					dynamicShareHandler.sendMineDynMessage();
				}
				else 
					if (shareType == 2) {// 分享给我的联系人
						dynamicShareHandler.sendShareForFriend();
					}
					else 
						if (shareType == 3) {// 发送分享短信给联系人
							dynamicShareHandler.sendMessage();
						}
						else 
							if (shareType == 4) {// 分享到指定群组
								dynamicShareHandler.sendShareForGroup();
							}
							else 
								if (shareType == 6) {// 动态转发
									dynamicShareHandler.forwardDynMessage(obj);
								}
								else 
									if (shareType == 7) {// 分享简历给联系人
										dynamicShareHandler.sendShareCVToFriend();
									}
									else 
										if (shareType == 9) {// 分享简历到我的动态.
											dynamicShareHandler.sendResumeDynMessage();
										}
			}, 100);
		}
};

/**
 * 发布到我的动态.
 */
dynamicShareHandler.sendMineDynMessage = function() {
	var actionName = parent.shareConfig.actionName;
	var jsonParam = parent.shareConfig.jsonParam;
	var dynContentText = $.trim($("#mineDynContent").text());
	if (common.textLengthCount(dynContentText) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var dynContentHtml = this.text2UrlReplace(dynContentText); 
	
	jsonParam["shareTitle"] = "分享到他（她）的动态。";
	jsonParam["shareEnTitle"] = "share to him (her) dynamic.";
	jsonParam["sayContent"] = dynContentHtml;
	jsonParam["permission"] = $("#hidden-showRight1").val();

	$.ajax( {
		url : ctxpath + "/dynamic/" + actionName + "/ajaxShareToDyn",
		type : "post",
		dataType : "json",
		data : {
			"jsonParam" : JSON.stringify(jsonParam)
		},
		success : function(data) {
			if (data.result == "success") {
				var resType = parent.shareConfig.resType;
				var resDetails = jsonParam.resDetails;
				if (resDetails != null && typeof (resDetails) != "undefined") {
					for ( var i = 0; i < resDetails.length; i++) {
						
						var shareCountObj = parent.$("#shareCountLabel_"+ resType + "_" + resDetails[i].resId);
						var shareSpanObj= parent.$("#shareCountSpan_" + resType + "_"+ resDetails[i].resId)
						var isExists=$(shareCountObj).attr("count");
						if(typeof (isExists)=="undefined"){
							$(shareCountObj).text(parseInt($(shareCountObj).text()) + 1);
							$(shareSpanObj).show();
							$(shareCountObj).parent().show();
						}else{
							var count=parseInt($(shareCountObj).attr("count"))+1;
							$(shareCountObj).attr("count",count);
							$(shareCountObj).text("("+count+")");
						}
						
					}
				}

				// 关闭弹出层
				$.scmtips.show("success", fileTips.opSuccess);
				setTimeout(function() {
					parent.$.Thickbox.closeWin();
				}, 1000);
			} else if (data.rtn == 'failure') {
				$.scmtips.show("error", fileTips.opFaild);
				$("#dyn_share_btn").enabled();
				$("#loadTip").hide();
			}
		},
		error : function(data) {
			$("#dyn_share_btn").enabled();
			$("#loadTip").hide();
		}
	});
};

/**
 * 简历分享到我的动态.
 */
dynamicShareHandler.sendResumeDynMessage = function() {
	var actionName = parent.shareConfig.actionName;
	var jsonParam = parent.shareConfig.jsonParam;
	var dynContentText = $.trim($("#resumeMineDynContent").text());
	if (common.textLengthCount(dynContentText) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var dynContentHtml = this.text2UrlReplace(dynContentText);

	if ($("#cv_pdfUrl9").attr("checked")) {
		jsonParam["pdfFlag"] = 1;
	} else {
		jsonParam["pdfFlag"] = 0;
	}
	if ($("#cv_wordUrl9").attr("checked")) {
		jsonParam["docFlag"] = 1;
	} else {
		jsonParam["docFlag"] = 0;
	}

	jsonParam["shareTitle"] = "分享到他（她）的动态。";
	jsonParam["shareEnTitle"] = "share to him (her) dynamic.";
	jsonParam["sayContent"] = dynContentHtml;
	jsonParam["permission"] = $("#hidden-showRight9").val();

	$.ajax( {
		url : ctxpath + "/dynamic/" + actionName + "/ajaxShareToDyn",
		type : "post",
		dataType : "json",
		data : {
			"jsonParam" : JSON.stringify(jsonParam)
		},
		success : function(data) {
			if (data.result == "success") {
				$.scmtips.show("success", fileTips.opSuccess);
				setTimeout(function() {
					parent.$.Thickbox.closeWin();
				}, 1000);
			}
		},
		error : function(data) {
			$("#dyn_share_btn").enabled();
			$("#loadTip").hide();
		}
	});
};

/**
 * 分享给联系人.
 */
dynamicShareHandler.sendShareForFriend = function() {
	var sharePsn = autowordArr["receiverDiv"].words();
	if (sharePsn.length == 0) {
		$.scmtips.show("warn", fileTips.selectFriend);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var shareContentText = $.trim($("#friendShareContent").text());
	if (shareContentText == "") {
		$.scmtips.show("warn", fileTips.typeDynContent);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	if (common.textLengthCount(shareContentText) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var deadLine = $("#commend_dead_line").val().replace(/\-/g, "/");
	var currentDate = $("#current_now_date").text().replace(/\-/g, "/");
	
	var nowDate = new Date();
	var nowYear = nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDay = nowDate.getDate();
	var nowDateStr = nowYear + "/" + nowMonth + "/" + nowDay;//本地时间

	if (!dynamicShareHandler.compareDate(currentDate, deadLine)) {
		if(deadLine == nowDateStr && currentDate != nowDateStr){//scm-5874 提示更新本地时间
			$.scmtips.show("warn", fileTips.than + "请更新您的本地日期");
		}else{
			$.scmtips.show("warn", fileTips.than);
		}
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var shareContentHtml = this.text2UrlReplace(shareContentText);

	var receivers = "";
	var receiverNameAll = "";
	for ( var i = 0; i < sharePsn.length; i++) {
		receivers += "," + ((sharePsn[i].val == null || sharePsn[i].val == "") ? sharePsn[i].text : sharePsn[i].val);
		if (i < 5) {
			receiverNameAll += "," + sharePsn[i].text;
		}
	}
	receivers = receivers.replace(",", "");
	receiverNameAll = receiverNameAll.replace(",", "");
	var shareTitleCN = "分享给<label title=\"'" + receiverNameAll + "'\"> ' "
			+ sharePsn[0].text + " ' </label>";
	var shareTitleEN = " share <label title=\"'" + receiverNameAll + "'\"> ' "
			+ sharePsn[0].text + " ' </label>";
	if (sharePsn.length > 1) {
		shareTitleCN += "等" + sharePsn.length + "人。";
		shareTitleEN += " etc" + sharePsn.length + " peoples.";
	}
	parent.sendShareForFriend(receivers,
			shareContentHtml, $("#commend_dead_line").val(), shareTitleCN, shareTitleEN);
	
};

/**
 * 分享简历给联系人.
 */
dynamicShareHandler.sendShareCVToFriend = function() {
	var sharePsn = autowordArr["receiverDiv_CV"].words();
	if (sharePsn.length == 0) {
		$.scmtips.show("warn", fileTips.selectFriend);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var shareContentText = $.trim($("#shareCVToFriendContent").text());

	if (shareContentText == "") {
		$.scmtips.show("warn", fileTips.typeDynContent);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	if (common.textLengthCount(shareContentText) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var shareContentHtml = this.text2UrlReplace(shareContentText);

	var post_data = {
			'supportEmail' : 1,
			'msgType' : 49,
			'title' : fileTips.msgTitle,
			'cvName' : parent.shareContentConfig.cvName,
			'recommendUrl' : parent.shareContentConfig.url
	}
	var shareContent = "<div style=\"margin-bottom:10px\">"
			+ shareContentHtml + "</div>";
	var fileBlock = $("<div id=\"attachFileBlock\" style=\"dispaly:none\"></div>");
	if ($("#cv_pdfUrl").attr("checked")) {
		var resumeFile = $("#resumeFile_temp").clone().attr("id",
				"attachFileDiv").css("display", "block");
		var attachObj = resumeFile.find("div.attachName").find("a");
		attachObj.attr("href", parent.shareContentConfig.pdfUrl);
		attachObj.attr("title", parent.shareContentConfig.cvName + '.pdf');
		attachObj.text(parent.shareContentConfig.cvName + '.pdf');
		fileBlock.append(resumeFile);
		post_data['pdfUrl'] = parent.shareContentConfig.pdfUrl;
	}
	if ($("#cv_wordUrl").attr("checked")) {
		var resumeFile = $("#resumeFile_temp").clone().attr("id",
				"attachFileDiv").css("display", "block");
		var attachObj = resumeFile.find("div.attachName").find("a");
		attachObj.attr("href", parent.shareContentConfig.wordUrl);
		attachObj.attr("title", parent.shareContentConfig.cvName + '.doc');
		attachObj.text(parent.shareContentConfig.cvName + '.doc');
		fileBlock.append(resumeFile);
		post_data['wordUrl'] = parent.shareContentConfig.wordUrl;
	}

   var receivers = "";
	var receiverNameAll = "";
	for ( var i = 0; i < sharePsn.length; i++) {
		receivers += "," + ((sharePsn[i].val == null || sharePsn[i].val == "") ? sharePsn[i].text : sharePsn[i].val);
		if (i < 5) {
			receiverNameAll += "," + sharePsn[i].text;
		}
	}
	post_data['receivers'] = receivers.replace(",", "");
	receiverNameAll = receiverNameAll.replace(",", "");
	
	var shareTitleCN = "分享给<label title=\"'" + receiverNameAll + "'\"> ' "
			+ sharePsn[0].text + " ' </label>";
	var shareTitleEN = " share <label title=\"'" + receiverNameAll + "'\"> ' "
			+ sharePsn[0].text + " ' </label>";
	if (sharePsn.length > 1) {
		shareTitleCN += "等" + sharePsn.length + "人。";
		shareTitleEN += " etc" + sharePsn.length + " peoples.";
	}

	var jsonParam = parent.shareConfig.jsonParam;
	jsonParam["shareTitle"] = shareTitleCN;
	jsonParam["shareEnTitle"] = shareTitleEN;
	post_data['jsonParam'] = JSON.stringify(jsonParam);
	
	post_data['content'] = shareContent;

	$.ajax( {
		url : ctxpath + "/msgbox/ajaxSendMessage",
		type : 'POST',
		dataType : 'json',
		data : post_data,
		success : function(data) {
			if (data && data.action == 'success') {
				$("#loadTip").hide();
				$.scmtips.show('success', fileTips.opSuccess);
				setTimeout(function() {
					parent.$.Thickbox.closeWin();
				}, 1000);
			}
		},
		error : function() {
			$.scmtips.show("error", fileTips.sendMsgFaild);
			$("#dyn_share_btn").enabled();
			$("#loadTip").hide();
		}
	});
};

/**
 * 发送分享消息.
 */
dynamicShareHandler.sendMessage = function() {
	if (!dynamicShareHandler.validateMessage()) {
		return false;
	}
	var smsContent = $("#friendSendContent").text();
	if (smsContent != "" && smsContent != undefined) {
		smsContent = smsContent.replace(new RegExp("disabled", "gm"), "");
	}

	var smsContentHtml = this.text2UrlReplace(smsContent);

	var gId = $("#gId").val();
	if (gId == null || gId == '' || gId == 'undefined') {
		gId = 0;
	}
	var groupName = $("#groupName").val();
	var insideFile = "";
	var groupId = $("#groupId").val();
	if (groupId == null || groupId == '' || groupId == 'undefined')
		groupId = 0;

	var psnArray = autowordArr["send-receiverDiv"].words();
	var receivers = "";
	var receiverNameAll = "";
	for ( var i = 0; i < psnArray.length; i++) {
		receivers += "," + ((psnArray[i].val == null || psnArray[i].val == "") ? psnArray[i].text : psnArray[i].val);
		if (i < 5) {
			receiverNameAll += "," + psnArray[i].text;
		}
	}
	receivers = receivers.replace(",", "");
	receiverNameAll = receiverNameAll.replace(",", "");
	
	var resIds = parent.shareConfig.resIds;

	var shareTitleCN = "分享给<label title=\"'" + receiverNameAll + "'\"> ' "
			+ psnArray[0].text + " ' </label>";
	var shareTitleEN = " share <label title=\"'" + receiverNameAll + "'\"> ' "
			+ psnArray[0].text + " ' </label>";
	if (psnArray.length > 1) {
		shareTitleCN += "等" + psnArray.length + "人。";
		shareTitleEN += " etc" + psnArray.length + " peoples.";
	}

	var jsonParam = parent.shareConfig.jsonParam;
	jsonParam["shareTitle"] = shareTitleCN;
	jsonParam["shareEnTitle"] = shareTitleEN;

	var supportEmail = 0;
	if (typeof(parent.shareConfig.supportEmail) != "undefined") {
		supportEmail = parent.shareConfig.supportEmail;
	}
	var msgType = 14;
	if (typeof(parent.shareConfig.msgType) != "undefined") {
		msgType = parent.shareConfig.msgType;
	}
	var json = {
		"msgType" : msgType,
		"groupName" : groupName,
		"receivers" : receivers,
		"title" : $("#friendSendTitle").val(),
		"content" : smsContentHtml,
		"insideType" : 12,
		"supportEmail" : supportEmail, 
		"groupId" : Number(groupId),
		"gId" : Number(gId),
		"resIds" : resIds,
		"recommendUrl" : parent.shareConfig.recommendUrl,
		"jsonParam" : JSON.stringify(jsonParam)
	};

	if (insideFile) {
		json["insideFile"] = insideFile;
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

	var recommendUrl = $("#recommendUrl");
	if (recommendUrl.length > 0) {
		json["recommendUrl"] = recommendUrl.val();
		json["content"] = json["content"].replace(recommendUrl.val(), "");
	}

	json["content"] = json["content"];
	$.ajax( {
		url : ctxpath + "/msgbox/ajaxSendMessage",
		type : "post",
		dataType : "json",
		timeout : 30000,
		data : json,
		error : function(e) {
			$.scmtips.show("error", fileTips.sendMsgFaild);
			$("#dyn_share_btn").enabled();
			$("#loadTip").hide();
		},
		success : function(data) {
			if (data && data.action == "success") {
				$.scmtips.show("success", fileTips.opSuccess);
				setTimeout(function() {
					parent.$.Thickbox.closeWin();
				}, 1000);

			}
		}
	});

};

/**
 * 分享到群组.
 */
dynamicShareHandler.sendShareForGroup = function() {
	var groupIdStr = "";
	var groupName = "";
	i = 0;
	$("input[name='group_checkbox']:checked").each(
			function() {
				if (groupIdStr == "") {
					groupIdStr = $(this).val();
					if (i < 5) {
						groupName = "“"
								+ $("#group_name" + $(this).val())
										.attr("title") + "”";
					}
				} else {
					groupIdStr += "," + $(this).val();
					if (i < 5) {
						groupName += "、“"
								+ $("#group_name" + $(this).val())
										.attr("title") + "”";
					}
				}
			});

	if (groupIdStr == "") {
		$.scmtips.show("warn", fileTips.selectGroup);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	var dynContentText = $.trim($("#groupShareContent").text());

	if (common.textLengthCount(dynContentText) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}
	var dynContentHtml = this.text2UrlReplace(dynContentText);
	
	var nodeId = parent.shareConfig.nodeId;

	if (nodeId == null || typeof (nodeId) == "undefined") {
		nodeId = "";
	}

	var jsonParam = parent.shareConfig.jsonParam;
	jsonParam["shareTitle"] = "分享到群组" + groupName;
	jsonParam["shareEnTitle"] = " share to group(s) " + groupName;
	jsonParam["groupIds"] = groupIdStr;
	jsonParam["actionName"] = parent.shareConfig.actionName;
	jsonParam["sayContent"] = dynContentHtml;

	if (jsonParam["permission"]) {
		delete jsonParam.permission;
	}

	$.ajax( {
		url : ctxpath + "/dynamic/ajaxShareToGroup",
		type : "post",
		dataType : "json",
		data : {
			"jsonParam" : JSON.stringify(jsonParam)
		},
		success : function(data) {
			if (data.result == "success") {
				var resType = parent.shareConfig.resType;
				var resDetails = jsonParam.resDetails;
				if (resDetails != null && typeof (resDetails) != "undefined") {
					for ( var i = 0; i < resDetails.length; i++) {
						var shareCountObj = parent.$("#shareCountLabel_"+ resType + "_" + resDetails[i].resId);
						var isCount= $(shareCountObj).attr("count");
						if(typeof (isCount) == "undefined"){
							$(shareCountObj).text(parseInt($(shareCountObj).text()) + 1);
							$(shareCountObj).parent().show();
						}else{
							var count=parseInt($(shareCountObj).attr("count"))+1;
							$(shareCountObj).attr("count",count);
							$(shareCountObj).text("("+count+")");
						}
					}
				}

				$("#loadTip").hide();
				$.scmtips.show("success", fileTips.opSuccess);
				setTimeout(function() {
					parent.$.Thickbox.closeWin();
				}, 1000);
			} else if (data.rtn == 'failure') {
				$.scmtips.show("error", fileTips.opFaild);
				$("#dyn_share_btn").enabled();
				$("#loadTip").hide();
			}
		},
		error : function(data) {
			$("#dyn_share_btn").enabled();
			$("#loadTip").hide();
		}
	});

};

/**
 * 动态转发.
 */
dynamicShareHandler.forwardDynMessage = function(obj) {
	var sayContent = $.trim($("#dyn—mineDynContent").text());
	if (sayContent.length == 0
			|| $("#dyn—mineDynContent").hasClass("watermark")) {
		sayContent = "";
	} else if (common.textLengthCount(sayContent) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}


	var sayContentHtml = this.text2UrlReplace(sayContent);

	var jsonParam = parent.shareConfig.jsonParam;
	jsonParam["shareTitle"] = "分享到他（她）的动态。";
	jsonParam["shareEnTitle"] = "share to him (her) dynamic.";
	jsonParam["sayContent"] = sayContentHtml;
	jsonParam["permission"] = $("#hidden-showRight6").val();

	$(obj).disabled();

	$.ajax( {
		url : ctxpath + "/dynamic/ajaxForward",
		type : "post",
		data : {
			"jsonParam" : JSON.stringify(jsonParam)
		},
		dataType : "json",
		success : function(json) {
			if (json.result == "success") {
				$("#loadTip").hide();
				$.scmtips.show("success", fileTips.opSuccess);
				var queryType = parent.dynMsgUtil.queryType;
				switch (queryType) {
				case 0:
					parent.dynMsgUtil.ajaxLoadAllDyn(1);
					break;
				case 1:
					parent.dynMsgUtil.ajaxLoadMineDyn(1);
					break;
				case 2:
					parent.dynMsgUtil.ajaxLoadNewDyn(1);
					break;
				case 4:
					parent.dynMsgUtil.refreshShareCount();
					break;
				case 5:
					parent.dynMsgUtil.refreshShareCount();
					break;
				default:
					parent.$.Thickbox.closeWin();
				}
			}
		},
		error : function(e) {
			$.scmtips.show("error", fileTips.opFaild);
			$(obj).enabled();
			$("#loadTip").hide();
		}

	});

};

dynamicShareHandler.wrapperData = function() {
	var smsContent = $("#friendSendContent").attr("value");
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

dynamicShareHandler.validateMessage = function() {
	if (autowordArr["send-receiverDiv"].words() == 0) {
		$.scmtips.show("warn", fileTips.selectFriend);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	} else if ($("#friendSendTitle").val() == ""
			|| $.trim($("#friendSendTitle").val()).length == 0) {
		$.scmtips.show("warn", fileTips.typeMsgTitle);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	} else if ($.trim($("#friendSendContent").text()) == ""
			|| $.trim($("#friendSendContent").text()).length == 0) {
		$.scmtips.show("warn", fileTips.typeContent);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	} else if (common.textLengthCount($.trim($("#friendSendContent").text())) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	return true;
};
/**
 * 校验发送群组邀请的信息.
 */
dynamicShareHandler.validateGroupInviteMessage = function() {
	if (autowordArr["sendInvite-receiverDiv"].vals() == 0) {
		$.scmtips.show("warn", fileTips.selectFriend);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	} else if ($("#InviteSendTitle").val() == ""
			|| $.trim($("#InviteSendTitle").val()).length == 0) {
		$.scmtips.show("warn", fileTips.typeMsgTitle);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	} else if ($.trim($("#InviteSendContent").text()) == ""
			|| $.trim($("#InviteSendContent").text()).length == 0) {
		$.scmtips.show("warn", fileTips.typeContent);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	} else if (common.textLengthCount($.trim($("#InviteSendContent").text())) > 250) {
		$.scmtips.show("warn", fileTips.lengthWarn);
		$("#dyn_share_btn").enabled();
		$("#loadTip").hide();
		return false;
	}

	return true;
};
/**
 * 时间比较.
 * 
 * @param start
 * @param end
 * @return
 */
dynamicShareHandler.compareDate = function(start, end) {
	var starts = start.split("/");
	var ends = end.split("/");
	if (starts.length != 3 || ends.length != 3) {
		return false;
	}
	var year = parseInt(ends[0]) - parseInt(starts[0]);
	if (year < 0) {
		return false;
	} else if (year > 0) {
		return true;
	}

	var month = year * 12 + (parseInt(ends[1]) - parseInt(starts[1]));
	if (month < 0) {
		return false;
	}
	var day = parseInt(ends[2]) - parseInt(starts[2]);
	if (day < 0) {
		return false;
	}
	return true;
};

/**
 * 替换链接文本为弹出链接.
 * @param {Object} text
 */
dynamicShareHandler.text2UrlReplace = function(text){
	if(typeof text == "undefined" || text==""){
		return text;
	}
	return text.replace(/(<a.*?>.*?<\/a>)|(http(s)?:\/\/(\w+\.)+\w+(:[\d]{1,5})?([\w\?\.\/%&=]*)?)/gi,function(match){

	   if(typeof match == "undefined" || match.indexOf('</a>')!=-1){
	   	return match;
	   }else{
	   	return '<a href="'+match+'" class="Blue" target="_blank">'+match+'</a>';
	   }
	});
};
