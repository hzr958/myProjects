var workRemindTimeout;
var MsgBoxUtil = {
	"initUnReadCount" : function() {
		$.ajax( {
			url : snsctx + '/msgbox/ajaxGetMsgBoxCount',
			type : "post",
			dataType : "json",
			data : {},
			async:true,
			success : function(data) {
			   if (data.ajaxSessionTimeOut) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
			   			document.location.reload();
			   		}
			   	});
			   } else {
			   	//左侧
							$("#displaySmsCount").text(data.insideMsgTotal);
							$("#displayRequestCount").text(data.inviteMsgTotal);
							$("#displayFTRequestCount").text(data.ftrequestMsgTotal);
							$("#displayShareCount").text(data.shareMsgTotal);
							$("#displaySystemCount").text(data.noticeMsgTotal);
							
							$("#msg_totalCount_sms").val(data.insideMsgAll);
							$("#msg_totalCount_request").val(data.inviteMsgAll);
							$("#msg_totalCount_ftrequest").val(data.ftrequestMsgAll);
							$("#msg_totalCount_share").val(data.shareMsgAll);
							$("#msg_totalCount_notice").val(data.noticeMsgAll);
							var totalCount = 0;
							
							//列表处
							if ($("#label-sms-unread").length > 0) {
								$("#label-sms-unread").text(data.insideMsgTotal);
								totalCount = parseInt(data.insideMsgAll);
								$("#label-sms-read").text(totalCount - parseInt(data.insideMsgTotal));
							}
							
							if ($("#label-request-unread").length > 0) {
								$("#label-request-unread").text(data.inviteMsgTotal);
								totalCount = parseInt(data.inviteMsgAll);
								$("#label-request-read").text(totalCount - parseInt(data.inviteMsgTotal));
							}
							
							if ($("#label-ftrequest-unread").length > 0) {
								$("#label-ftrequest-unread").text(data.ftrequestMsgTotal);
								totalCount = parseInt(data.inviteMsgAll);
								$("#label-ftrequest-read").text(totalCount - parseInt(data.inviteMsgTotal));
							}
							
							if ($("#label-share-unread").length > 0) {
								$("#label-share-unread").text(data.shareMsgTotal);
								totalCount = parseInt(data.shareMsgAll);
								$("#label-share-read").text(totalCount - parseInt(data.shareMsgTotal));
							}
							
							//				if ($("#label-system-unread").length > 0) {
							//					$("#label-system-unread").text(data.noticeMsgTotal);
							//				}
							
							//左侧
							if (data.insideMsgTotal != "0") {
								// 短信
								$("#smsCount").text("(" + data.insideMsgTotal + ")");
								$("#smsCount").css("color", "#ee4313");
								
							}
							if (data.inviteMsgTotal != "0") {
								// 请求
								$("#requestCount").text("(" + data.inviteMsgTotal + ")");
								$("#requestCount").css("color", "#ee4313");
								
							}
							if (data.ftrequestMsgTotal != "0") {
								// 全文请求
								$("#ftrequestCount").text("(" + data.ftrequestMsgTotal + ")");
								$("#ftrequestCount").css("color", "#ee4313");
								
							}
							if (data.shareMsgTotal != "0") {
								// 分享
								$("#shareCount").text("(" + data.shareMsgTotal + ")");
								$("#shareCount").css("color", "#ee4313");
								
							}
							if (data.noticeMsgTotal != "0") {
								// 通知
								$("#systemCount").text("(" + data.noticeMsgTotal + ")");
								$("#systemCount").css("color", "#ee4313");
								
							}
						}
			},
	error : function(e) {
		alert(networkException);
	}
		});
	},
	"showMsgTip" : function() {
		$.ajax( {
			type : "post",
			url : snsctx + "/msgbox/ajaxGetMsgTip",
			data : {},
			dataType : "html",
			cache : false,
			success : function(data) {
			   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
			   			document.location.reload();
			   		}
			   	});
			   } else {
			   	$("#msgTip_div").html(data);
					//增加了信息总数赋值_MJG_SCM-5269.
					if (parseInt($("#msg_total_count").val()) > 0) {
						var  msgHtml = '<span id="msg_tip_totalTop"   class="login_msg_count">'+$("#msg_total_count").val()+'</span>'   ;
					    $("#msg_tip_count").append(msgHtml);
						$("#msg_tip_totalTop").html($("#msg_total_count").val());
						$("#main_msg_total_label1").html($("#msg_total_count").val());
						$("#main_msg_total_label").html("("+$("#msg_total_count").val()+")");
					}else{
						//SCM-9746
						$("#main_msg_total_label1").hide();
						//$("#main_msg_total_label1").html(0);
					}
			   	if (!msgBoxIsClose) {
			   		if (parseInt($("#msg_tip_totalTop").text()) > 0) {
							//消息中心提示增加设置参数值_MJG_SCM_20140605.
			   			msgBoxIsClose = 0;
			   			MsgBoxUtil.setCookie("msgBoxIsClose", 0, 10800000); // 180分钟超时
									//$("#work-reminds").show();
									MsgBoxUtil.closeMsgTip();
								}
								else {
									MsgBoxUtil.closeMsgTip();
								}
							}
							else {
								$("#msg_tip_box_close").remove();
							}
						}
			},
			error : function(e) {
			}
		});
	},
	"closeMsgTip" : function() {
		msgBoxIsClose = 1;
		MsgBoxUtil.setCookie("msgBoxIsClose", 1, 10800000); // 180分钟超时
		$("#msg_tip_box_close").remove();
		$("#work-reminds").hide();
	},
	"workRemindsShow" : function() {
		if (workRemindTimeout != null
				&& typeof (workRemindTimeout) != "undefined") {
			clearTimeout(workRemindTimeout);
		}
		$("#quit-nav").hide();
		$("#work-reminds").fadeIn();
	},
	"workRemindsHide" : function() {
		if (msgBoxIsClose == 1) {
			workRemindTimeout = setTimeout(function() {
				$("#work-reminds").fadeOut();
			}, 100);
		}
	},
	"selectAll" : function(checkObjName) {
		$("input[name='" + checkObjName + "']").each(function() {
			$(this).attr("checked", "checked");
		});
	},
	"unSelectAll" : function(checkObjName) {
		$("input[name='" + checkObjName + "']").each(function() {
			$(this).attr("checked", false);
		});
	},
	"selectAllCheckbox" : function(obj, checkboxName) {
		var choose = obj.checked;
		$(":checkbox[name='" + checkboxName + "']").each(function() {
			if($(this).attr("disabled")!='disabled'){
				$(this).attr("checked", choose);
			}
		});
	},
	"collectIds" : function(checkObjName) {
		var ids = "";
		$("input[name='" + checkObjName + "']:checked").each(function() {
			if (ids == "") {
				ids = $(this).val();
			} else {
				ids += "," + $(this).val();
			}
		});

		return ids;
	},
	"collectIdsAll":function(checkObjName){
		var ids = "";
		$("input[name='" + checkObjName + "']").each(function() {
			if (ids == "") {
				ids = $(this).val();
			} else {
				ids += "," + $(this).val();
			}
		});

		return ids;
	},
	"ajaxDeleteInboxMessage" : function(ids, type, callback) {
		if (ids == "") {
			$.smate.scmtips.show("warn", msgboxTip.selectItemReq,
					locale == "zh_CN" ? 300 : 340);
			return false;
		}
		jConfirm(msgboxTip.cfmDelete, msgboxTip.dialoagTitle, function(r) {
			if (r) {

				$.ajax( {
					url : snsctx + "/msgbox/ajaxDelInboxMessage",
					type : "post",
					dataType : "json",
					timeout : 30000,
					data : {
						"type" : type,
						"resIds" : ids
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
					   		callback(ids);
					   		MsgBoxUtil.refreshMsgCount();
					   	}
					   }
					},
					error : function(e) {
						$.smate.scmtips.show("error", msgboxTip.deleteFailed);
					}
				});
			}
		});

	},
	"ajaxDeleteOutboxMessage" : function(ids, type, callback) {
		if (ids == "") {
			$.smate.scmtips.show("warn", msgboxTip.selectItemReq,
					locale == "zh_CN" ? 300 : 340);
			return false;
		}
		jConfirm(msgboxTip.cfmDelete, msgboxTip.dialoagTitle, function(r) {
			if (r) {

				$.ajax( {
					url : snsctx + "/msgbox/ajaxDelOutboxMessage",
					type : "post",
					dataType : "json",
					timeout : 30000,
					data : {
						"type" : type,
						"resIds" : ids
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
					   		callback(ids);
					   	}
					   }
					},
					error : function(e) {
						$.smate.scmtips.show("error", msgboxTip.deleteFailed);
					}
				});
			}
		});
	},
	"ajaxMarkMessageStatus" : function(ids, type, statusType, callback,noTip) {
		if (ids == "") {
			$.smate.scmtips.show("warn", msgboxTip.markItemReq,
					locale == "zh_CN" ? 300 : 340);
			return false;
		}

		$.ajax( {
			url : snsctx + '/msgbox/ajaxUpdateMessageStatus',
			type : "post",
			dataType : "json",
			timeout : 30000,
			data : {
				"type" : type,
				"resIds" : ids,
				"statusType" : statusType
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
			   		if (noTip == null && typeof(noTip) == "undefined") {
			   			$.smate.scmtips.show("success", msgboxTip.markSuccess);
			   		}
			   		if (callback != null && typeof(callback) != "undefined") {
			   			callback(ids);
			   		}
			   		MsgBoxUtil.refreshMsgCount();
			   		var idList = ids.split(',');
			   		for (var id in idList) {
			   			$("#ckItem" + idList[id]).attr("checked", false);
			   		}
			   		$("#selectAll_checkbox").attr("checked", false);
			   	}
			   	else 
			   		if (data.result == "failure") {
			   			if (noTip == null && typeof(noTip) == "undefined") {
			   				$.smate.scmtips.show("error", msgboxTip.markFailed);
			   			}
			   		}
			   }
			},
			error : function(e) {
				$.smate.scmtips.show("error", msgboxTip.networdException);
			}
		});
	},
	"searchMsg" : function() {
		var searchText = $.trim($("#msgbox_search").val());
		if (searchText == "") {
			$.smate.scmtips.show("warn", msgboxTip.searchConReq);
			return false;
		}

		var msgboxFlag = $("#hidden-inOrOut").val();
		var leftMenuId = $("#hidden-leftMenuId").val();
		$(".Menubox").show();
		if (leftMenuId == "msgbox-sms") {// 短信
			if (msgboxFlag == 0) {// 收件箱
				smsAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxSmsInbox");
			} else {// 发件箱
				smsAjaxHtml("con_one_3", snsctx + "/msgbox/ajaxSmsOutbox");
			}
		} else if (leftMenuId == "msgbox-request") {// 请求
			if (msgboxFlag == 0) {// 收件箱
				requestAjaxHtml("con_one_1", snsctx
						+ "/msgbox/ajaxRequestInbox");
			} else {// 发件箱
				requestAjaxHtml("con_one_2", snsctx
						+ "/msgbox/ajaxRequestOutbox");
			}
		} else if (leftMenuId == "msgbox-ftrequest") {// 请求
			if (msgboxFlag == 0) {// 收件箱
				requestAjaxHtml("con_one_1", snsctx
						+ "/msgbox/ajaxFTRequestInbox");
			} else {// 发件箱
				requestAjaxHtml("con_one_2", snsctx
						+ "/msgbox/ajaxFTRequestOutbox");
			}
		} else if (leftMenuId == "msgbox-share") {// 分享
			if (msgboxFlag == 0) {// 收件箱
				shareAjaxHtml("con_one_1", snsctx + "/msgbox/ajaxShareInbox");
			} else {// 发件箱
				shareAjaxHtml("con_one_2", snsctx + "/msgbox/ajaxShareOutbox");
			}
		} else if (leftMenuId == "msgbox-system") {// 系统消息
			$("#msgbox_search").val(searchText);
			$("#mainForm").submit();
		}
	},
	"backList" : function(url, msgboxFlag) {
		var pageNo = $("#hidden-pageNo").val();
		var pageSize = $("#hidden-pageSize").val();
		var status = $('#hidden-msgboxStatus').val();
		$("#backListForm").remove();
		$(
				"<form id=\"backListForm\"  name=\"backListForm\"  method=\"post\">"
						+ "<input name=\"backPageNo\" type=\"hidden\" value=\""
						+ pageNo
						+ "\"/>"
						+ "<input name=\"backPageSize\" type=\"hidden\" value=\""
						+ pageSize + "\"/>"
						+ "<input name=\"msgboxFlag\" type=\"hidden\" value=\""
						+ msgboxFlag + "\"/>"
						+ "<input name=\"status\" type=\"hidden\" value=\""
						+ status + "\"/>" + "</form>").insertAfter("body");
		$("#backListForm").attr("action", url);
		$("#backListForm").submit();
	},
	"refreshMsgCount":function(){
		$.ajax({
			type:"post",
			url:snsctx+"/msgbox/ajaxGetMsgBoxCount",
			data:{},
			async:true,
			dataType:"json",
			success:function(data){
			   if (data.ajaxSessionTimeOut) {
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
			   			document.location.reload();
			   		}
			   	});
			   } else {
			   	//更新左侧菜单
							$("#displaySmsCount").text(data.insideMsgTotal);
							$("#displayRequestCount").text(data.inviteMsgTotal);
							$("#displayFTRequestCount").text(data.ftrequestMsgTotal);
							$("#displayShareCount").text(data.shareMsgTotal);
							$("#displaySystemCount").text(data.noticeMsgTotal);
							
							//更新提示
							$("#msg_tip_sms").text(data.insideMsgTotal);
							$("#msg_tip_request").text(data.inviteMsgTotal);
							$("#msg_tip_ftrequest").text(data.ftrequestMsgTotal);
							$("#msg_tip_share").text(data.shareMsgTotal);
							$("#msg_tip_system").text(data.noticeMsgTotal);
							
							$("#msg_totalCount_sms").val(data.insideMsgAll);
							$("#msg_totalCount_request").val(data.inviteMsgAll);
							$("#msg_totalCount_ftrequest").val(data.ftrequestMsgAll);
							$("#msg_totalCount_share").val(data.shareMsgAll);
							$("#msg_totalCount_notice").val(data.noticeMsgAll);
							var totalCount = 0;
							//列表处
							if ($("#label-sms-unread").length > 0) {
								$("#label-sms-unread").text(data.insideMsgTotal);
							}
							
							if ($("#label-request-unread").length > 0) {
								$("#label-request-unread").text(data.inviteMsgTotal);
							}
							
							if ($("#label-ftrequest-unread").length > 0) {
								$("#label-ftrequest-unread").text(data.ftrequestMsgTotal);
							}
							
							if ($("#label-share-unread").length > 0) {
								$("#label-share-unread").text(data.shareMsgTotal);
							}
							
							if (data.insideMsgTotal != "0") {
								// 短信
								$("#smsCount").text("(" + data.insideMsgTotal + ")");
								$("#smsCount").css("color", "#ee4313");
								
								$("#msg_tip_sms").parent().parent().show();
							}
							else {
								$("#smsCount").text("");
								$("#msg_tip_sms").parent().parent().hide();
							}
							
							if (data.inviteMsgTotal != "0") {
								// 请求
								$("#requestCount").text("(" + data.inviteMsgTotal + ")");
								$("#requestCount").css("color", "#ee4313");
								
								$("#msg_tip_request").parent().parent().show();
							}
							else {
								$("#requestCount").text("");
								$("#msg_tip_request").parent().parent().hide();
							}
							
							if (data.ftrequestMsgTotal != "0") {
								//全文 请求
								$("#ftrequestCount").text("(" + data.ftrequestMsgTotal + ")");
								$("#ftrequestCount").css("color", "#ee4313");
								
								$("#msg_tip_ftrequest").parent().parent().show();
							}
							else {
								$("#ftrequestCount").text("");
								$("#msg_tip_ftrequest").parent().parent().hide();
							}
							
							if (data.shareMsgTotal != "0") {
								// 分享
								$("#shareCount").text("(" + data.shareMsgTotal + ")");
								$("#shareCount").css("color", "#ee4313");
								
								$("#msg_tip_share").parent().parent().show();
							}
							else {
								$("#shareCount").text("");
								$("#msg_tip_share").parent().parent().hide();
							}
							
							if (data.noticeMsgTotal != "0") {
								// 通知
								$("#systemCount").text("(" + data.noticeMsgTotal + ")");
								$("#systemCount").css("color", "#ee4313");
								
								$("#msg_tip_system").parent().parent().show();
							}
							else {
								$("#systemCount").text("");
								$("#msg_tip_system").parent().parent().hide();
							}
							
							var total = parseInt(data.insideMsgTotal) + parseInt(data.inviteMsgTotal) + parseInt(data.ftrequestMsgTotal) + parseInt(data.shareMsgTotal) + parseInt(data.noticeMsgTotal);
							$("#msg_tip_total").text(total);
							$("#msg_tip_totalTop").text(total);
							$("#remind_count_span").text(total);
							if (total == 0) {
								MsgBoxUtil.closeMsgTip();
							}
							else {
								$("#msg_tip_box").show();
							}
						}
			},error:function(e){
			}
		});
	},
	"managerTaskTip":function(){//管理员角色登陆的任务提醒.
		$.ajax({
			url: snsctx+'/taskNotice/ajaxIndexMaint', 
	        type: 'POST', 
			dataType:'json',
			async:true,
			success: function(data){
//				$(".Work-reminds").show();
				$("#msg_tip_box_spinner").hide();
				var kpiPubNum = data.kpiPubNum;
				var mgPubNum = data.mgPubNum;
				var confirmNum = data.confirmNum;
				var approvePsnNum = data.approvePsnNum;
				var noUnitPsnNum = data.noUnitPsnNum;
				var totalNum=data.totalNum;
				if(totalNum>0){
					$("#msg_tip_totalTop").html(totalNum);
					$("#main_msg_total_label").html("("+totalNum+")");
				}else{
					$("#msg_tip_box").hide();
				}
				var flag = false;
				if(kpiPubNum  > 0   ){
					var span = $("#task_skin_kpiPubNum_notice");
					span.find("#msg_tip_sms").html(kpiPubNum);
					span.show();
					flag = true;
				}
				if(mgPubNum  > 0   ){
					var span = $("#task_skin_mgPubNum_notice");
					span.find("#msg_tip_sms").html(mgPubNum);
					span.show();
					flag = true;
				}
				if(confirmNum > 0  ){
					var span = $("#task_skin_confirmNum_notice");
					span.find("#msg_tip_sms").html(confirmNum);
					span.show();
					flag = true;
					if($("#pub_cm_num").size() > 0){
						$("#pub_cm_num_span").html(confirmNum);
						$("#pub_cm_num").show();
					}
				}
				if(approvePsnNum > 0  ){
					var span = $("#task_skin_approvePsnNum_notice");
					span.find("#msg_tip_sms").html(approvePsnNum);
					span.show();
					flag = true;
				}
				if(noUnitPsnNum > 0  ){
					var span = $("#task_skin_noUnitPsnNum_notice");
					span.find("#msg_tip_sms").html(noUnitPsnNum);
					span.show();
					flag = true;
				}
				if(!flag){
					$("#task_no_notice").show();
				}
				$("#work-reminds").hide();
				//$("#work-reminds").animate({opacity:1, height:'show'},300);
			}
		});
	},
	"setCookie" : function(c_name, value, expiredays) {  //设置cookie函数
	     var exdate = new Date();
	     exdate.setDate(exdate.getDate() + expiredays);
	     document.cookie = c_name + "=" + escape(value) + 
	     ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString()) + ";path=/";//+ ';domain=' + Cookie_Domain + '; path=/';
	 },
	 "getCookie" : function(name) { //取cookies函数
		 var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		 if(arr != null) 
			 return unescape(arr[2]); 
		 return null;
	 },
	 "delCookie" : function(name){ //删除cookie
	     var exp = new Date();
	     exp.setTime(exp.getTime() - 1);
	     var cval = MsgBoxUtil.getCookie(name);
	     if(cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/";
	 },
	 "checkSelectBox" : function(obj,checkboxName,selectAllObj) { //单选
		 var flag = true;
			if (obj.checked) {
				var cks = $(":checkbox[name='"+checkboxName+"']");
				for ( var i = 0; i < cks.length; i++) {
					// 没有全部选择
					if (!cks[i].checked) {
						flag = false;
						break;
					}
				}
			} else {
				flag = false;
			}
			$("#"+selectAllObj).attr("checked", flag);
	 },
	 "updateRcmdCount" : function(count, type) {
		 if (type == 1) {
			 var rcmdCount = parseInt($('#msg_tip_rcmdPubCount').text());
			 if (rcmdCount > count) {
				 $('#msg_tip_rcmdPubCount').text(rcmdCount - count);
			 } else {
				 $('#msg_tip_rcmdPubCount').parent().remove();
				 $('#msg_tip_rcmdCount').html($('#msg_tip_rcmdFulltextCount').parent());
			 }
		 } else {
			 var rcmdCount = parseInt($('#msg_tip_rcmdFulltextCount').text());
			 if (rcmdCount > count) {
				 $('#msg_tip_rcmdFulltextCount').text(rcmdCount - count);
			 } else {
				 $('#msg_tip_rcmdFulltextCount').parent().remove();
				 $('#msg_tip_rcmdCount').html($('#msg_tip_rcmdPubCount').parent());
			 }
		 }
	 }
};
