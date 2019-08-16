var showMenuTimeout;
var DynamicMood = {
	"showMenuShow" : function(e) {
		if (showMenuTimeout != null && typeof (showMenuTimeout) != "undefined") {
			clearTimeout(showMenuTimeout);
		}

		$(".comment_textarea").each(function() {
			$(this).hide();
			$(this).parent().find("input").show();
		});

		$("#selDymic").hide();

		$("#showMenuList").fadeIn();
		if (e && e.stopPropagation) {// 非IE
			e.stopPropagation();
		}

		else {// IE
			window.event.cancelBubble = true;
		}
	},
	"showMenuHide" : function() {
		showMenuTimeout = setTimeout(function() {
			$("#showMenuList").fadeOut();
		}, 100);
	},
	"selDymicShow" : function(e) {
		if (showMenuTimeout != null && typeof (showMenuTimeout) != "undefined") {
			clearTimeout(showMenuTimeout);
		}

		// 点击别处，隐藏回复框
		$(".comment_textarea").each(function() {
			$(this).hide();
			$(this).parent().find("input").show();
		});

		$("#showMenuList").hide();

		$("#selDymic").fadeIn();
		if (e && e.stopPropagation) {// 非IE
			e.stopPropagation();
		} else {// IE
			window.event.cancelBubble = true;
		}

	},
	"selDymicHide" : function() {
		showMenuTimeout = setTimeout(function() {
			$("#selDymic").fadeOut();
		}, 100);
	},
	"showMenuChoose" : function(obj, flag) {
		$(obj).parent().parent().find("i.img_choose").attr("class",
				"img_nochoose");
		$(obj).find("i").attr("class", "img_choose");
		$("#showMenuLabel").text($(obj).text());
		$("#hidden-showRight").val(flag);
		if (flag == 1) {
			$("#icon_right").attr("class", "icon_people");
		} else {
			$("#icon_right").attr("class", "icon_people2");
		}
		$("#showMenuList").hide();
	},
	"showDynamicMenu" : function(obj, id) {
		$(obj).parent().parent().find(".img_choose").attr("class",
				"img_nochoose");
		$("#" + id).attr("class", "img_choose");
		$("#selectDyn").html($("#" + id).parent().find("span").html());
		$("#selDymic").fadeOut();
	},
	"showLinkInput" : function() {
		$("#mood_menu_div").hide();
		var moodLinkTemp = $("#mood_link_temp").clone();
		moodLinkTemp.addClass("add_Item");
		moodLinkTemp.attr("id", "mood_link_add");
		$("#mood_link").append(moodLinkTemp);
		$("#mood_link").show();
		moodLinkTemp.show();
	},
	"delteMoodItem" : function(obj) {
		jConfirm(dynamicMsg.reminderdelete, dynamicMsg.reminder, function(r) {
			if (r) {
				var addItemParentObj = $(obj).parent().parent();
				var addItemObj = addItemParentObj.find("div.add_Item");
				if (addItemObj.length == 1) {
					addItemParentObj.hide();
					$("#mood_menu_div").show();
				} else if (addItemObj.length <= 5) { // 添加文件，少于5个时，需要显示添加按钮
					if (addItemParentObj.attr("id") == "mood_file") {
						addItemParentObj.find("#goOnBtn").show();
					}
				}

				$(obj).parent().remove();
			}
		});
	},
	"moodPublicationConfirm" : function(type) {
		var checkedPub = $("input[name='pubCKB']:checked");
		if (checkedPub.length == 0) {
			$.smate.scmtips.show("warn", type == 1 ? dynamicMsg.selectPubReq
					: dynamicMsg.selectRefReq);
		} else {
			var pubDetailArray = [];
			var pubDetails = [];
			$(checkedPub).each(function() {
				pubDetailArray.push($("#td" + $(this).val()).html());
				pubDetails.push({
					"resId" : $(this).val(),
					"resNode" : $(this).attr("resNode")
				});
			});
			parent.DynamicMood.moodPublicationAdd(pubDetailArray, pubDetails,
					type);
		}
	},
	"pubCheckLess" : function(obj) {
		var pubCKBed = $("input[name='pubCKB']:checked");
		if (pubCKBed.length > 5) {
			$(obj).attr("checked", false);
			$.smate.scmtips.show("warn", dynamicMsg.upTo5);
		}
	},
	"moodPublicationAdd" : function(pubDetailArray, pubDetails, type) {
		$("#mood_menu_div").hide();
		$("#hidden_pubType").val(type);
		for ( var i = 0; i < pubDetailArray.length; i++) {
			var moodPublicationTemp = $("#mood_publication_temp").clone();
			moodPublicationTemp.addClass("add_Item");
			moodPublicationTemp.attr("id", "mood_publication_add");
			moodPublicationTemp.find("input.hidden_pubId").val(
					pubDetails[i].resId);
			moodPublicationTemp.find("input.hidden_nodeId").val(
					pubDetails[i].resNode);
			moodPublicationTemp.find("p.mood_publication_detail").html(
					pubDetailArray[i]);
			$("#mood_publication").append(moodPublicationTemp);
			$("#mood_publication").show();
			moodPublicationTemp.show();
		}
		$.Thickbox.closeWin();
	},
	"submitMoodDynamic" : function() {
		DynamicMood.shareUnable();
		//$('#moodDynSubmitBtn').click(DynamicMood.submitMoodDynamic);
		var content = $.trim($("#share_news").text());

		if (DynMessageUtil.textLengthCount(content) > 250) {
			DynamicMood.shareAble();
			$.smate.scmtips.show("warn", dynamicMsg.lengthWarn);
			$("#share_news").focus();
			return;
		}

		var resType = 0;
		var url = null;
		var resDetails = [];
		var permission = $("#hidden-showRight").val();

		if ($("#mood_file").find("div.add_Item").length > 0) {
			$("#mood_file").find("div.add_Item").each(
					function() {
						resDetails.push({
							//tsz
							/*"resNode" : $(this).find(
									"input.hidden_currentNodeId").val(),*/
							"id" : $(this).find("input.hidden_fileId").val()
						});
					});
			resType = 3;
		} else if ($("#mood_link").find("div.add_Item").length > 0) {
			url = $.trim($("#mood_link").find("div.add_Item input").val());
			if (url != "http://" && url != "") {
				if (!/^(https?|ftp):\/\//.test(url)) {// 缺少协议
					url = "http://" + url;
				}
				url = url.replace(/^(((https?|ftp):\/\/)+)(.*)$/i, "$2$4");// 替换重复的协议
				if (!/^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
						.test(url)) {
					DynamicMood.shareAble();
					$.smate.scmtips.show("warn", dynamicMsg.urlError);
					$("#add_dmsg_url_txt").focus();
					return;
				}
			} else {
				DynamicMood.shareAble();
				$.smate.scmtips.show("warn", dynamicMsg.urlError);
				$("#add_dmsg_url_txt").focus();
				return;
			}
		} else if ($("#mood_publication").find("div.add_Item").length > 0) {
			$("#mood_publication").find("div.add_Item").each(function() {
				resDetails.push({
					//tsz
					/*"resNode" : $(this).find("input.hidden_nodeId").val(),*/
					"resId" : $(this).find("input.hidden_pubId").val()
				});
			});

			resType = $("#hidden_pubType").val();
		} else {
			if (content.length == 0 || $("#share_news").hasClass("watermark")) {
				DynamicMood.shareAble();
				$.smate.scmtips.show("warn", dynamicMsg.typecontent);
				$("#share_news").focus();
				return;
			}
		}

		url = url == null ? "" : url;
		
		/* 与xss拦截冲突
		 * content = content.replace(/&lt;(.*)&gt;.*&lt;\/\1&gt;|&lt;(.*) \/&gt;/,
				"&_lt;$1&_gt;").replace(/</g, "&lt;").replace(/>/g, "&gt;");*/
		
		content = text2UrlReplace(content);

		if ($("#share_news").hasClass("watermark")) {
			content = "";
		}
		
		var des3InspgId=$("#des3InspgId").val();
		var jsonParam = {
			"content" : content,
			"linkUrl" : url
			/*"resType" : resType*/
		};
		var shareType =0;
		if (resDetails.length != 0) {
			jsonParam["detailList"] = resDetails;
			shareType =1; //类型 为文件
		}

		$.ajax({
			url :'/inspg/inspgdynamic/ajaxshare',
			type : 'post',
			dataType : 'json',
			data : {
				"shareJson" : JSON.stringify(jsonParam),
				"des3InspgId":des3InspgId,
				"shareType":shareType
			},
			success : function(data) {
				if (data.ajaxSessionTimeOut == "yes") {
					jConfirm(dynamicMsg.sessionTimeout, dynamicMsg.prompt, function(r) {
						if (r) {
							document.location.reload();
						}
					});
				} else {
					if(data.result="success"){
						$.smate.scmtips.show("success", dynamicMsg.sendSuccess);
						setTimeout(function(){document.location.reload();},1000);
					}else{
						$.smate.scmtips.show("error", data.msg);
						setTimeout(function(){document.location.reload();},1000);
					}
				}
			}
		});
	},
	"cancelMoodDynamic" : function() {
		$("#share_news").val("");
		$("#share_news").blur();

		$("#mood_file").find("div.add_Item").each(function() {
			$(this).remove();
		});
		$("#mood_link").find("div.add_Item").each(function() {
			$(this).remove();
		});
		$("#mood_publication").find("div.add_Item").each(function() {
			$(this).remove();
		});
		$("#mood_file").hide();
		$("#mood_link").hide();
		$("#mood_publication").hide();
		$("#mood_menu_div").show();
	},
	"shareUnable" : function(){
		$('#moodDynSubmitBtn').removeAttr("onclick");
		$('#moodDynSubmitBtn').attr("disabled","disabled");
		$('#moodDynSubmitBtn').attr("style","padding: 4px 18px; color: rgb(204, 204, 204);");
	},
	"shareAble" : function(){
		$('#moodDynSubmitBtn').attr("onclick","DynamicMood.submitMoodDynamic();");
		$('#moodDynSubmitBtn').removeAttr("disabled");
		$('#moodDynSubmitBtn').attr("style","padding: 4px 18px;");
	}
};

function fileAddHandler(data) {
	$("#mood_menu_div").hide();
	var moodFileTemp = $("#mood_file_temp").clone();
	moodFileTemp.addClass("add_Item");
	moodFileTemp.attr("id", "mood_file_add");
	var fileName = data.fileName;
	fileName = common.htmlDecode(fileName);
	var fileDetailObj = moodFileTemp.find("div.mood_file_detail");
	fileDetailObj.html(fileName);
	fileDetailObj.attr("title", fileName);
	moodFileTemp.find("input.hidden_fileId").val(data.fileId);
	moodFileTemp.find("input.hidden_des3FileId").val(data.des3FileId);
	moodFileTemp.find("input.hidden_currentNodeId").val(data.nodeId);
	moodFileTemp.insertBefore($("#goOnBtn"));
	$("#mood_file").show();
	moodFileTemp.show();
	if ($("#mood_file").find("div.add_Item").length >= 5) {
		$("#goOnBtn").hide();
	} else {
		$("#goOnBtn").show();
	}
	$.Thickbox.closeWin();
}

function selDynamic(name, cursel, n, aObj) {// 子菜单样式控制事件.
	for ( var i = 1; i <= n; i++) {
		var con = document.getElementById("con_" + name + "_" + i);
		if (con) {
			con.style.display = (i == cursel) ? "block" : "none";
		}
		var choose = $("#choose_" + i);
		if (choose.length > 0) {
			if (i == cursel) {
				choose.removeClass("img_nochoose");
				choose.addClass("img_choose");
				$("#selectDyn").html(choose.parent().find("span").html());
			} else {
				choose.removeClass("img_choose");
				choose.addClass("img_nochoose");
			}
		}
	}
	$("#selDymic").hide();
}

/**
 * 替换链接文本为弹出链接.
 * @param {Object} text
 */
var text2UrlReplace = function(text){
	if(typeof text == "undefined" || text==""){
		return text;
	}
	return text.replace(/(<a.*?>.*?<\/a>)|(http(s)?:\/\/(\w+\.)+\w+(:[\d]{1,5})?([\w\?\.\/%&=\-]*)?)/gi,function (match){

	   if(typeof match == "undefined" || match.indexOf('</a>')!=-1){
	   	return match;
	   }else{
	   	return '<a href="'+match+'" class="Blue" target="_blank">'+match+'</a>';
	   }
	});
};

