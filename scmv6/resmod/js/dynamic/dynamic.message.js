var awardLockFlag = "";
var lastDynId = -1;//最后加载的动态ID
var Share1 = Share1 ? Share1
		: {
			reminder : locale == "zh_CN" ? "提示" : "Reminder",
			loginCfmTip : locale == "zh_CN" ? "系统超时或未登录，你要登录吗？"
					: "You are not logged in or session time out, please log in again.",
	
		};

DynMessageUtil = function(pNode, des3PsnId, des3GroupId, doFlag) {
	this.pNode = pNode;
	this.queryType = 0;
	this.des3PsnId = des3PsnId;
	this.des3GroupId = des3GroupId;
	this.doFlag = doFlag;
	this.fetchSize = 10;
	this.firstResult = 0;
	this.maxSize = 100;
	this.lastDynId = "";
	this.ajaxLoadDyn = DynMessageUtil.ajaxLoadDyn;
	this.ajaxLoadGroupDyn=DynMessageUtil.ajaxLoadGroupDyn;
	this.ajaxLoadAllDyn = DynMessageUtil.ajaxLoadAllDyn;
	this.ajaxLoadMineDyn = DynMessageUtil.ajaxLoadMineDyn;
	this.ajaxLoadNewDyn = DynMessageUtil.ajaxLoadNewDyn;
	this.ajaxLoadFriendDyn = DynMessageUtil.ajaxLoadFriendDyn;
	this.ajaxLoadGroupAllDyn = DynMessageUtil.ajaxLoadGroupAllDyn;
	this.ajaxLoadGroupNewDyn = DynMessageUtil.ajaxLoadGroupNewDyn;
	this.ajaxLoadPsnDyn = DynMessageUtil.ajaxLoadPsnDyn;
	this.ajaxLoadMoreDyn = DynMessageUtil.ajaxLoadMoreDyn;
	this.ajaxLoadMoreGroupDyn=DynMessageUtil.ajaxLoadMoreGroupDyn;
	this.ajaxAward = DynMessageUtil.ajaxAward;
	this.ajaxCancelAward = DynMessageUtil.ajaxCancelAward;
	this.showReplyBox = DynMessageUtil.showReplyBox;
	this.textCommentBoxClick = DynMessageUtil.textCommentBoxClick;
	this.replyWordCount = DynMessageUtil.replyWordCount;
	this.ajaxExtendReply = DynMessageUtil.ajaxExtendReply;
	this.ajaxReply = DynMessageUtil.ajaxReply;
	this.showSharePage = DynMessageUtil.showSharePage;
	this.refreshShareCount = DynMessageUtil.refreshShareCount;
	this.ajaxLoadExtend = DynMessageUtil.ajaxLoadExtend;
	this.showDynOperation = DynMessageUtil.showDynOperation;
	this.hideDynOperation = DynMessageUtil.hideDynOperation;
	this.extendDynOperation = DynMessageUtil.extendDynOperation;
	this.shrinkDynOperation = DynMessageUtil.shrinkDynOperation;
	this.ajaxDeleteDyn = DynMessageUtil.ajaxDeleteDyn;
	this.ajaxShieldDynByPsn = DynMessageUtil.ajaxShieldDynByPsn;
	this.ajaxShieldDynByType = DynMessageUtil.ajaxShieldDynByType;
	this.ajaxHandlerFriend=DynMessageUtil.ajaxHandlerFriend;
	this.ajaxHandlerGroup=DynMessageUtil.ajaxHandlerGroup;
	this.ajaxJoinGroup=DynMessageUtil.ajaxJoinGroup;
	this.impMyPub=DynMessageUtil.impMyPub;
	this.impMyRef=DynMessageUtil.impMyRef;
	this.impMyPubFromPdwh=DynMessageUtil.impMyPubFromPdwh;
	this.impMyRefFromPdwh=DynMessageUtil.impMyRefFromPdwh;
	this.ajaxDynamicEndorse=DynMessageUtil.ajaxDynamicEndorse;
	this.showCollectDiv=DynMessageUtil.showCollectDiv;
	this.emptySenderTip=DynMessageUtil.emptySenderTip;
	this.ajaxCheckResIsDel=DynMessageUtil.ajaxCheckResIsDel;
	this.replyToPsn=DynMessageUtil.replyToPsn;
	this.deleteReply = DynMessageUtil.deleteReply;
	this.showDelMsg=DynMessageUtil.showDelMsg;
	this.ajaxDownloadRes=DynMessageUtil.ajaxDownloadRes;
	this.ajaxIsShowPicInTB=DynMessageUtil.ajaxIsShowPicInTB;
};

/**
 * 加载个人动态.
 */
DynMessageUtil.ajaxLoadDyn = function() {
	var outter = this;
	var postData = {
		"queryType" : outter.queryType,
		"fetchSize" : outter.fetchSize,
		"firstResult" : outter.firstResult,
		"lastDynId" : lastDynId 
	};

	if (outter.des3PsnId != null) {
		postData["des3PsnId"] = outter.des3PsnId;
	}
	
	//(拆分个人动态和群组动态_MJG_SCM-5913).
//	if (outter.des3GroupId != null) {
//		postData["des3GroupId"] = outter.des3GroupId;
//		postData["groupNode"] = outter.groupNode;
//	}

	$("#loadDynamic_div").show();
	$("#moreDynamic_div").hide();

	$.ajax({
		url : ctxpath + "/dynamic/ajaxGetDynList",
		type : "post",
		dataType : "json",
		data : postData,
		success : function(data) {
			var dynamicObj = null;
			var dynamicHtml = null;
			//存储最后加载的动态ID,用于加载其他时排序
			//存放tmpId=29的相关信息 ,dynId
			var dynIdForEndorse = new Array();
			for ( var i = 0, length = data.length; i < length
					&& i < outter.fetchSize; i++) {
				dynamicObj = data[i];
				if (dynamicObj.dynContent != "") {
					lastDynId = dynamicObj.dynId; //更新最后加载动态ID
					dynamicHtml = $(dynamicObj.dynContent);
					if (outter.doFlag == 0) {// 不能操作
						dynamicHtml.find(".comments_box").remove();

						dynamicHtml.find(".t_detail").find("a").each(
								function() {
									$(this).attr("onclick", "");
									$(this).css({
										"color" : "#888888"
									});
								});
					}
					outter.pNode.append(dynamicHtml);
					//SCM-3498 by zk 2013/08/16
					if(dynamicObj.tmpId==29){
						dynIdForEndorse.push(dynamicObj.dynId);
					}
					// 添加dynId
					outter.lastDynId = "li_" + dynamicObj.dynId;
					dynamicHtml.attr("id", outter.lastDynId);
				}
			}

			if (dynamicHtml != null) {
				// 最后一个处理下划线
				dynamicHtml.css({
					"border-bottom" : 0,
					"padding-bottom" : 0,
					"margin-bottom" : "20px"
				});
			}

			// 是否有更多
			if (data.length > outter.fetchSize) {
				// 下限（maxSize）
				if ((outter.firstResult + outter.fetchSize) > outter.maxSize) {
					$("#noMoreDynamic_div").show();
				} else {
					$("#moreDynamic_div").show();
				}
			} else {
				$("#noMoreDynamic_div").show();
			}

			// 隐藏加载提示.
			$("#loadDynamic_div").hide();

			// 重新设定开始值.
			if (data.length > outter.fetchSize) {
				outter.firstResult += outter.fetchSize;
			} else {
				outter.firstResult += data.length;
			}
			
			//科研之友的推荐动态不算入计算中，如果有推荐，需要-1
			if(data.length>0){
				if(data[0].dynType==999){
					outter.firstResult = outter.firstResult -1;
				}
			}
			// 还原回原来的加载数.
			outter.fetchSize = 10;

			// 弹出窗口
			$(".gallery_link").each(function() {
				var hrefStr = $(this).attr("href");
				$(this).attr("class","gallery_link_over");
				if(hrefStr!='#'){
					$(this).attr("href", hrefStr + "?temp=" + Math.random());
					$(this).thickbox();
				}
			});
			
			//处理添加联系人
			DynMessageUtil.ajaxHandlerFriend();
			
			//处理加入群组(拆分个人动态和群组动态_MJG_SCM-5913).
			DynMessageUtil.ajaxHandlerGroup();
			// 处理删除图标. 
			if (outter.des3PsnId != null) {
				$(".drop_down").each(function() {
					$(this).parent().attr("onmouseover", "");
					$(this).parent().attr("onmouseout", "");
					$(this).remove();
				});
			}
			//处理群组动态删除标签操作权限(拆分个人动态和群组动态_MJG_SCM-5913).
//			DynMessageUtil.ajaxGroupDynAuthority();

			// 分享下拉模式
			$(".share_pull").sharePullMode({
				showSharePage:function(_this){
					DynMessageUtil.showSharePage(_this);
				},
				showShareSites:function(){
					showShareSites();
				}
			});
			//全文请求
			 $(".fulltext_iris").fullTextRequest();
		   //SCM-3498 by zk 2013/08/16
			if(dynIdForEndorse.length>0){
				DynMessageUtil.ajaxDynamicEndorse(dynIdForEndorse);
			}
			
			replyOperatShowOrHide();
			 
			$.Thickbox.closeWin();
		},
		error : function() {

		}
	});
};

/**
 * 加载群组动态.
 */
DynMessageUtil.ajaxLoadGroupDyn = function() {
	var outter = this;
	var postData = {
		"queryType" : outter.queryType,
		"fetchSize" : outter.fetchSize,
		"firstResult" : outter.firstResult
	};

	if (outter.des3PsnId != null) {
		postData["des3PsnId"] = outter.des3PsnId;
	}
	if (outter.des3GroupId != null) {
		postData["des3GroupId"] = outter.des3GroupId;
		postData["groupNode"] = outter.groupNode;
	}

	$("#loadDynamic_div").show();
	$("#moreDynamic_div").hide();

	$.ajax({
		url : ctxpath + "/dynamic/ajaxGetGroupDynList",
		type : "post",
		dataType : "json",
		data : postData,
		success : function(data) {
			var dynamicObj = null;
			var dynamicHtml = null;
			if(data==null){
				$("#noMoreDynamic_div").show();
				// 隐藏加载提示.
				$("#loadDynamic_div").hide();	
				$.Thickbox.closeWin();
				return;
			}
			
			//存放tmpId=29的相关信息 ,dynId
			var dynIdForEndorse = new Array();
			for ( var i = 0, length = data.length; i < length
					&& i < outter.fetchSize; i++) {
				dynamicObj = data[i];
				if (dynamicObj.dynContent != "") {
					dynamicHtml = $(dynamicObj.dynContent);
					if (outter.doFlag == 0) {// 不能操作
						dynamicHtml.find(".comments_box").remove();

						dynamicHtml.find(".t_detail").find("a").each(
								function() {
									$(this).attr("onclick", "");
									$(this).css({
										"color" : "#888888"
									});
								});
					}
					outter.pNode.append(dynamicHtml);
					//SCM-3498 by zk 2013/08/16
					if(dynamicObj.tmpId==29){
						dynIdForEndorse.push(dynamicObj.groupDynId);
					}
					// 添加dynId
					outter.lastDynId = "li_" + dynamicObj.groupDynId;
					dynamicHtml.attr("id", outter.lastDynId);
				}
			}

			if (dynamicHtml != null) {
				// 最后一个处理下划线
				dynamicHtml.css({
					"border-bottom" : 0,
					"padding-bottom" : 0,
					"margin-bottom" : "20px"
				});
			}

			// 是否有更多
			if (data.length > outter.fetchSize) {
				// 下限（maxSize）
				if ((outter.firstResult + outter.fetchSize) > outter.maxSize) {
					$("#noMoreDynamic_div").show();
				} else {
					$("#moreDynamic_div").show();
				}
			} else {
				$("#noMoreDynamic_div").show();
			}

			// 隐藏加载提示.
			$("#loadDynamic_div").hide();

			// 重新设定开始值.
			if (data.length > outter.fetchSize) {
				outter.firstResult += outter.fetchSize;
			} else {
				outter.firstResult += data.length;
			}
			
			//科研之友的推荐动态不算入计算中，如果有推荐，需要-1
			if(data.length>0){
				if(data[0].dynType==999){
					outter.firstResult = outter.firstResult -1;
				}
			}
			// 还原回原来的加载数.
			outter.fetchSize = 10;

			// 弹出窗口
			$(".gallery_link").each(function() {
				var hrefStr = $(this).attr("href");
				$(this).attr("class","gallery_link_over");
				if(hrefStr!='#'){
					$(this).attr("href", hrefStr + "?temp=" + Math.random());
					$(this).thickbox();
				}
			});
			
			//处理添加联系人
			DynMessageUtil.ajaxHandlerFriend();
			
			//处理加入群组
			DynMessageUtil.ajaxHandlerGroup();
			// 处理删除图标. 
			if (outter.des3PsnId != null) {
				$(".drop_down").each(function() {
					$(this).parent().attr("onmouseover", "");
					$(this).parent().attr("onmouseout", "");
					$(this).remove();
				});
			}
			//处理群组动态删除标签操作权限.
			DynMessageUtil.ajaxGroupDynAuthority();

			// 分享下拉模式
			$(".share_pull").sharePullMode({
				showSharePage:function(_this){
					DynMessageUtil.showSharePage(_this);
				},
				showShareSites:function(){
					showShareSites();
				}
			});
			//全文请求
			 $(".fulltext_iris").fullTextRequest();
		   //SCM-3498 by zk 2013/08/16
			if(dynIdForEndorse.length>0){
				DynMessageUtil.ajaxDynamicEndorse(dynIdForEndorse);
			}
			
			replyOperatShowOrHide();
			 
			$.Thickbox.closeWin();
		},
		error : function(data) {
		}
	});
};

/**
 * 加载所有动态.
 */
DynMessageUtil.ajaxLoadAllDyn = function(loadFlag) {
	//初始化最后加载动态ID
	if(lastDynId!='undefined'){
		lastDynId = -1;
	}
	var outter = this;
	outter.queryType = 0;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadDyn();
};

/**
 * 加载与我相关的动态.
 */
DynMessageUtil.ajaxLoadMineDyn = function(loadFlag) {
	//初始化最后加载动态ID
	if(lastDynId!='undefined'){
		lastDynId = -1;
	}
	var outter = this;
	outter.queryType = 1;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadDyn();
};

/**
 * 加载个人新鲜事.
 */
DynMessageUtil.ajaxLoadNewDyn = function(loadFlag) {
	//初始化最后加载动态ID
	if(lastDynId!='undefined'){
		lastDynId = -1;
	}
	var outter = this;
	outter.queryType = 2;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadDyn();
};

/**
 * 加载联系人动态.
 */
DynMessageUtil.ajaxLoadFriendDyn = function(loadFlag) {
	//初始化最后加载动态ID
	if(lastDynId!='undefined'){
		lastDynId = -1;
	}
	var outter = this;
	outter.queryType = 3;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadDyn();
};

/**
 * 加载群组所有动态.
 */
DynMessageUtil.ajaxLoadGroupAllDyn = function(loadFlag) {
	var outter = this;
	outter.queryType = 4;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadGroupDyn();
};

/**
 * 加载群组新鲜事动态.
 */
DynMessageUtil.ajaxLoadGroupNewDyn = function(loadFlag) {
	var outter = this;
	outter.queryType = 5;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadGroupDyn();
};

/**
 * 加载相关人的动态.
 */
DynMessageUtil.ajaxLoadPsnDyn = function(loadFlag) {
	var outter = this;
	outter.queryType = 6;
	if (loadFlag) {
		if (outter.firstResult != 0) {
			outter.fetchSize = outter.firstResult + 1;
		}
	}
	outter.firstResult = 0;
	outter.pNode.html("");
	$("#noMoreDynamic_div").hide();
	$("#moreDynamic_div").hide();
	outter.ajaxLoadDyn();
};

/**
 * 加载更多动态.
 */
DynMessageUtil.ajaxLoadMoreDyn = function() {
	var outter = this;
	$("#" + outter.lastDynId).css({
		"border-bottom" : "1px solid #e9e9e9",
		"padding-bottom" : "20px",
		"margin-bottom" : "20px"
	});
	outter.ajaxLoadDyn();
};

/**
 * 加载更多群组动态.
 */
DynMessageUtil.ajaxLoadMoreGroupDyn = function() {
	var outter = this;
	$("#" + outter.lastDynId).css({
		"border-bottom" : "1px solid #e9e9e9",
		"padding-bottom" : "20px",
		"margin-bottom" : "20px"
	});
	outter.ajaxLoadGroupDyn();
};

/**
 * 赞
 */
DynMessageUtil.ajaxAward = function(type,des3ObjId,objectId) {
	common.like.submit(type,des3ObjId,function(data){
		//赞列表HTML
		var likeHTML = common.like.generatelikepsnhtml(data["likeList_"+des3ObjId]);
		//赞数量
		var likeNum = data["listNum_"+des3ObjId];
		if(data["isZan_"+des3ObjId]==1){
			//当前用户已赞
			$(".award_link_"+objectId).attr("style","cursor:pointer;display:none;");
			$(".cancel_award_link_"+objectId).attr("style","cursor:pointer;");
		}else{
			//当前用户未赞
			$(".award_link_"+objectId).attr("style","cursor:pointer;");
			$(".cancel_award_link_"+objectId).attr("style","cursor:pointer;display:none;");
		}
		$(".like_detail_"+objectId+" > .user-list").html(likeHTML);
		$(".award_num_"+objectId).text(likeNum);
		if(likeNum>0){
			$(".like_detail_"+objectId).attr("style","");
		}else{
			$(".like_detail_"+objectId).attr("style","display:none;");
		}
		});
	
};

/**
 * 取消赞.
 */
DynMessageUtil.ajaxCancelAward = function(type,des3ObjId,objectId) {
	common.like.remove(type,des3ObjId,function(data){
			if(data.existslist=="1"){
				//赞列表HTML
				var likeHTML = common.like.generatelikepsnhtml(data["likeList_"+des3ObjId]);
				//赞数量
				var likeNum = data["listNum_"+des3ObjId];
				if(data["isZan_"+des3ObjId]==1){
					//当前用户已赞
					$(".award_link_"+objectId).attr("style","cursor:pointer;display:none;");
					$(".cancel_award_link_"+objectId).attr("style","cursor:pointer;");
				}else{
					//当前用户未赞
					$(".award_link_"+objectId).attr("style","cursor:pointer;");
					$(".cancel_award_link_"+objectId).attr("style","cursor:pointer;display:none;");
				}
				$(".like_detail_"+objectId+" > .user-list").html(likeHTML);
				$(".award_num_"+objectId).text(likeNum);
				if(likeNum>0){
					$(".like_detail_"+objectId).attr("style","");
				}else{
					$(".like_detail_"+objectId).attr("style","display:none;");
				}
			}else{
				//隐藏赞列表
				$(".like_detail_"+objectId).attr("style","display:none;");
				//当前用户未赞
				$(".award_link_"+objectId).attr("style","cursor:pointer;");
				$(".cancel_award_link_"+objectId).attr("style","cursor:pointer;display:none;");
			}
	
		});

};

/**
 * 打开评论输入框.
 */
DynMessageUtil.showReplyBox = function(resId,dynId, e) {
	if(document.getElementsByName("isoutside").length>0){
		jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
			if (r) {
				document.location.href="/inspg/login?forwardUrl="+window.location.href;
			}
		});
		return;
	}
	var outter = this;
	$(".reply_content_div").each(function() {
		$(this).hide();
	});
	$(".reply_enter_div").each(function() {
		$(this).show();
	});
	$("#input_" + dynId + "_" + resId).hide();
	$("#textarea_" + dynId + "_" + resId).show();
	$("#textarea_box_" + dynId + "_" + resId).html('').focus();
	
	if (e && e.stopPropagation) {// 非IE
		e.stopPropagation();
	} else {// IE
		window.event.cancelBubble = true;
	}

	$("#showMenuList").hide();
	$("#selDymic").hide();
	$(".share_site_list").each(function(){
		$(this).hide();
	});
};

DynMessageUtil.textCommentBoxClick = function(e) {
	if (e && e.stopPropagation) {// 非IE
		e.stopPropagation();
	} else {// IE
		window.event.cancelBubble = true;
	}
};
/**
 * 评论字数计算.
 */
DynMessageUtil.textLengthCount = function(text){
	if($.trim(text).length==0){
		return 0;
	}else{
		var inputLength = 0;
		text = $.trim(text);
		for(var i=0;i<text.length;i++){
			var cchar = text.charAt(i);
			if(common.isChinaStr(cchar)){
				inputLength += 1;
			}else{
				inputLength += 0.5;
			}
		}
		inputLength = Math.round(inputLength);
		return inputLength;
	}
}
/**
 * 评论字数计算.
 */
DynMessageUtil.replyWordCount = function(obj, resId, dynId, length) {
	//if(Sys.ie == '8.0'){// SCM-4611 IE8 光标定位问题
	if(client.browser.name == "IE" && client.browser.version == "8.0"){
		$(obj).html($(obj).html());
	}
	var content = $.trim($(obj).text());
	var inputLength = DynMessageUtil.textLengthCount(content);
	$("#count_label_" + dynId + "_" + resId).text(inputLength);
	if (inputLength > length) {
		$("#count_label_" + dynId + "_" + resId).css({
			"color" : "red",
			"font-weight" : "bold"
		});
	} else {
		$("#count_label_" + dynId + "_" + resId).css({
			"color" : "#999",
			"font-weight" : "normal"
		});
	}
};

/**
 * 展开剩余的评论.
 */
DynMessageUtil.ajaxExtendReply = function(obj, dynId, replyId,queryType) {
	var beginRecord = $(obj).attr("beginRecord");
	var removeLast = $(obj).attr('removeLast') != undefined ? $(obj).attr('removeLast') : 1;
	$.ajax({
		url : ctxpath + "/dynamic/ajaxExtendReply",
		type : "post",
		data : {
			"replyId" : replyId,
			"firstResult" : beginRecord,
			"queryType":queryType,
			"dynId":dynId,
			'removeLast' : removeLast
		},
		dataType : "json",
		success : function(data) {
			$(data.replyContent).insertBefore($(obj));
			if (data.hasNext == 1) {
				$(obj).attr("beginRecord", parseInt(beginRecord) + 10);
				var otherCount = parseInt($("#reply_num_" + dynId + "_" + replyId).text());
				$("#reply_num_" + dynId + "_" + replyId)
						.text((otherCount - 10));
			} else {
				$(obj).remove();
			}
			replyOperatShowOrHide();
		},
		error : function(e) {
		}
	});
};

/**
 * 评论.
 */
DynMessageUtil.ajaxReply = function(resType,psnId, parentId,resId, dynId,des3ObjectId) {
	var replyBtnObj = $("#textarea_" + dynId + "_" + resId).find(".replyBtn");
	replyBtnObj.attr("disabled","disabled");
	replyBtnObj.attr("style","cursor: pointer; color: rgb(204, 204, 204);");
	var replyObj = $("#textarea_box_" + dynId + "_" + resId);
	var replyToPsnTip = replyObj.find(".replyToPsnTip").attr("replyTip");
	
	var replyContent = $.trim($(replyObj).text());
	if (replyContent == "" || replyContent == replyToPsnTip) {
		replyBtnObj.removeAttr("disabled");
		replyBtnObj.attr("style","cursor: pointer;");
		$.smate.scmtips.show("warn", dynamicMsg.replyConReq);
		$("#input_" + dynId + "_" + resId).hide();
		$("#textarea_" + dynId + "_" + resId).show();
		$("#textarea_box_" + dynId + "_" + resId).focus();
		common.moveCursorToEnd($("#textarea_box_" + dynId + "_" + resId));
		return false;
	} 
	replyObj.find(".replyToPsnTip").remove();
	replyContent = $.trim($(replyObj).text());
	if (DynMessageUtil.textLengthCount(replyContent) > 250) {
		$(replyBtnObj).enabled();
		$.smate.scmtips.show("warn", dynamicMsg.lengthWarn);
		$("#input_" + dynId + "_" + resId).hide();
		$("#textarea_" + dynId + "_" + resId).show();
		$("#textarea_box_" + dynId + "_" + resId).focus();
		return false;
	}
	var outter = this;
	replyContent = replyContent.replace("'", "&apos;");
	replyContent = replyContent.replace("\"", "&quot;");

//发表评论
common.comment.submit(resType,parentId,replyContent,function(data){
	common.comment.dynload(resType,des3ObjectId,parentId,resId,dynId);
});


};

/**
 * 弹出分享页面.
 */
DynMessageUtil.showSharePage = function(obj, resType, resNode, resId, dynId) {
	resType = typeof resType == "undefined" ? $(obj).attr("resType") : resType;
	resNode = typeof resNode == "undefined" ? $(obj).attr("resNode") : resNode;
	resId = typeof resId == "undefined" ? $(obj).attr("resId") : resId;
	dynId = typeof dynId == "undefined" ? $(obj).attr("dynId") : dynId;
	
	if(resType!=1&&resType!=2&&resType!=4){
		//增加"des3GroupId"参数以兼容对群组资源的检查_MJG_SCM-3617.
		var outter = this;
		var jsonParam = {
				"resType" : Number(resType),
				"resNode" : Number(resNode),
				"resId" : Number(resId),
				"des3GroupId":outter.des3GroupId
		};
		
		var isDel = DynMessageUtil.ajaxCheckResIsDel(jsonParam, this);
		if(isDel){
			return;
		}
	}
	
	var shareUrl = null;
	var shareImg = null;
	shareConfig = {
		"resType" : resType,
		"resIds" : resId,
		"des3Ids":resId,
		"shareType" : '6',
		"shareTypeFirst" : '6',
		"jsonParam" : {
			"resType" : resType,
			"resNode" : resNode,
			"resId" : resId,
			"dynId" : dynId
		}
	};
	
	//添加分享记录的时候，需要用到
	var resDetails = new Array();
	resDetails.push({ 'resId' :resId, 'resNode' :resNode});
	shareConfig['jsonParam']['resDetails'] = resDetails;
	
	if (resType == 1 || resType == 2 || resType == 4) {
		shareConfig['shareType'] = "2,4,6";
		if (resType == 4) {
			shareConfig['shareType'] = "4,6";
			shareConfig['shareTypeFirst'] = '6';
		}else{
			shareConfig['shareTypeFirst'] = '2';
		}
		shareConfig['actionName'] = 'ext';
		var resDetails = new Array();
		resDetails.push({ 'resId' : resId, 'resNode' : resNode});
		shareConfig['jsonParam']['resDetails'] = resDetails;
		shareUrl = $("#shareTheme_" + resType + "_" + resId).attr("href");
		shareImg = $("#img_currentpsn_" + resId).attr("src");
		$('#hidden-share').attr('alt', ctxpath + '/dynamic/ajaxShareMaint?TB_iframe=true&height=580&width=720');
	} else {
		$('#hidden-share').attr('alt', ctxpath + '/dynamic/ajaxShareMaint?TB_iframe=true&height=250&width=720');
	}

	shareContentConfig = {
		"dynShareContent" : dynamicMsg.sayAWord,
		 "friendShareContent": resType == 2 ? dynamicMsg.friendShareRefContent : dynamicMsg.friendSharePubContent
	};

	$("#hidden-share").click();
};

/**
 * 更新分享次数.
 */
DynMessageUtil.refreshShareCount = function() {
	$(
			".shareCountLabel_" + shareConfig.jsonParam.resType + "_"
					+ shareConfig.jsonParam.resId).each(function() {
		var shareCount = parseInt($(this).text()) + 1;
		$(this).text(shareCount);
	});
	$.Thickbox.closeWin();
};

/**
 * 加载扩展.
 */
DynMessageUtil.ajaxLoadExtend = function(obj,dynId, resType, permission) {
	var outter = this;
	var jsonparam={};
	jsonparam["dynId"]=Number(dynId);
	jsonparam["resType"]=Number(resType);
	jsonparam["permission"]= Number(permission);
	jsonparam["dynDate"] =$.trim($(obj).attr("dynDate"));
	jsonparam["currentDes3PsnId"]=$(obj).attr("currentDes3PsnId");
	jsonparam["currentPsnAvatar"]=$(obj).attr("currentPsnAvatar"); 
	jsonparam["currentPsnName"]=$(obj).attr("currentPsnName");
	jsonparam["isMine"]=Number($(obj).attr("isMine"));
	jsonparam["isObjectOwner"]=Number($(obj).attr("isObjectOwner"));
	jsonparam["queryType"]=outter.queryType;

	$.ajax({
		url : ctxpath + "/dynamic/ajaxLoadExtend",
		type : "post",
		data : {
			"jsonParam" : JSON.stringify(jsonparam)
		},
		dataType : "json",
		success : function(data) {
			$("#clear_more_" + dynId).remove();
			$(data.extendContent).insertBefore($("#more_div_" + dynId));
			$("#more_div_" + dynId).remove();

			// 弹出窗口
			$(".gallery_link").thickbox();
			
			// 分享下拉模式
			$(".share_pull").sharePullMode({
				showSharePage:function(_this){
					DynMessageUtil.showSharePage(_this);
				},
				showShareSites:function(){
					showShareSites();
				}
			});
			
			//全文请求
			$(".fulltext_iris").fullTextRequest();
		},
		error : function(e) {
		}
	});
};

/**
 * 显示删除.
 */
DynMessageUtil.showDynOperation = function(obj) {
	$(obj).find(".link_delete").show();
};

/**
 * 隐藏删除.
 */
DynMessageUtil.hideDynOperation = function(obj) {
	var ld =$(obj).find(".link_delete");
	 ld.hide();
	 setTimeout(function(){
		 if($(ld).css("display")=='none'){
			 $(obj).find(".div_item_delete").hide();
		 }
     	},150
	 );
};


/**
 * 展开删除.
 */
DynMessageUtil.extendDynOperation = function(obj) {
	$(obj).parent().find(".div_item_delete").show();
};

/**
 * 缩起删除.
 */
DynMessageUtil.shrinkDynOperation = function(obj) {

	//SCM-4941动态右边的操作下拉弹出层不好用_zk_2014_03_24
	var ths = $(obj).parent().find(".div_item_delete");
	$(ths).hover(function(){
//		$(ths).show();
	},function(){
		$(ths).hide();
	})
//	$(obj).parent().find(".div_item_delete").hide();
};

/**
 * 删除此动态.
 */
DynMessageUtil.ajaxDeleteDyn = function(des3DynId,dynId) {
	var outter = this;
	jConfirm(dynamicMsg.deleteTip, dynamicMsg.prompt, function(r) {
		if (r) {
			$.ajax({
				url : "/inspg/inspgdynamic/ajaxdeldyn",
				type : "post",
				data : {
					"des3DynId" : des3DynId,
					"des3InspgId":$("#des3InspgId").val()
				},
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						$("#li_"+dynId).remove();
					}
				},
				error : function(e) {
				}
			});
		}
	});
};

/**
 * 屏蔽此人动态.
 */
DynMessageUtil.ajaxShieldDynByPsn = function(dynId) {
	var outter = this;
	jConfirm(dynamicMsg.shieldPsnTip, dynamicMsg.prompt, function(r) {
		if (r) {
			$.ajax({
				url : ctxpath + "/dynamic/ajaxShieldByPsn",
				type : "post",
				data : {
					"dynId" : Number(dynId)
				},
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						switch (outter.queryType) {
						case 0:
							outter.ajaxLoadAllDyn();
							break;
						case 1:
							outter.ajaxLoadMineDyn();
							break;
						case 2:
							outter.ajaxLoadNewDyn();
							break;
						case 3:
						   outter.ajaxLoadFriendDyn();
						   break;
						}
					}
				},
				error : function(e) {
				}
			});
		}
	});
};

/**
 * 屏蔽此类动态.
 */
DynMessageUtil.ajaxShieldDynByType = function(attId, dynId) {
	var outter = this;
	jConfirm(dynamicMsg.shieldTypeTip, dynamicMsg.prompt, function(r) {
		if (r) {
			$.ajax({
				url : ctxpath + "/dynamic/ajaxShieldByType",
				type : "post",
				data : {
					"dynId" : Number(dynId),
					"attId" : attId
				},
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						switch (outter.queryType) {
						case 0:
							outter.ajaxLoadAllDyn();
							break;
						case 1:
							outter.ajaxLoadMineDyn();
							break;
						case 2:
							outter.ajaxLoadNewDyn();
							break;
						case 3:
						   outter.ajaxLoadFriendDyn();
						   break;
						}
					}
				},
				error : function(e) {
				}
			});
		}
	});
};

/**
 * 加为联系人.
 */
DynMessageUtil.ajaxHandlerFriend=function(){
    $(".addFriendClass").each(function(){
		var _this=this;
		$.ajax({
		   url : ctxpath + "/friend/isFriend",
			type : "post",
			data : {
				"psnId" : $(_this).attr("psnhm")
			},
			dataType : "json",
			success: function(data){
				var timeOut = data['ajaxSessionTimeOut'];
				if(timeOut == 'yes' || data.isFriend == 0){
					$(_this).show();
					$(_this).thickbox({
						ctxpath: ctxpath,
			         respath: respath,
			         type: "addRequests",
			         parameters: {
			            "d3d": $(_this).attr("des3Id")
			            }
			        });
		      } else {
		    	  $(_this).remove();
		        }
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
            }
		});
    });
};
/**
 * 人员信息为空.
 */
DynMessageUtil.emptySenderTip=function(){
	$.smate.scmtips.show("success", dynamicMsg.emptySender);
}
/**
 * 加入群组相关.
 */
DynMessageUtil.ajaxHandlerGroup=function(){
	$(".apply_joingroup").each(function(){
		var _this=this;
		$.ajax({
			url:ctxpath+"/group/ajaxGetGroupMPJ",
			type:"post",
			data:{
				"groupId":$(_this).attr("groupId"),
				"groupNode":$(_this).attr("groupNode")
			},
			dataType:"json",
			success:function(data){
				var dynId=$(_this).attr("dynId");
				$("#groupMember_"+dynId).text(data.groupMember);
				if(data.groupPubNum && Number(data.groupPubNum) > 0){
					$("#groupPubNum_"+dynId).text(data.groupPubNum);
				}else{
					$("#groupPubNum_"+dynId).parent().remove();
				}
				if(data.isMember){
					$(_this).remove();
				}else{
					$(_this).show();
				}
			},
			error:function(e){
				
			}
		});
	});
}; 
/**
 * 处理群组动态的删除标签处理权限_MJG_SCM-3615..
 */
DynMessageUtil.ajaxGroupDynAuthority=function(){
	$(".drop_down").each(function() {
		//当前登录用户是否动态产生者.
		var isMine=$(this).find(".group_params").attr("isMine");
		//当前登录用户是否群组管理员.
		var isGroupManager=$(this).find(".group_params").attr("isGroupManager");
		//当前用户不是动态生成者且不是群组管理员，则取消其删除权限.
		if ((typeof isMine != 'undefined' && isMine == '0')&&(typeof isGroupManager != 'undefined' && isGroupManager == '0')) {
			$(this).parent().attr("onmouseover", "");
			$(this).parent().attr("onmouseout", "");
			$(this).remove();
		}
	});
};
// 请求加入群组.
DynMessageUtil.ajaxJoinGroup=function(obj,groupId,groupNodeId){
	var jsonParam = {
			"resType":8,
			"resNode":groupNodeId,
			"resId":groupId
	}
	var isDel = this.ajaxCheckResIsDel(jsonParam, this);
	if (!isDel) {
		var groupPsnList = [];
		groupPsnList.push({groupId:groupId,groupNodeId:groupNodeId});
		var groupIdNodeIdStr = JSON.stringify(groupPsnList);
		$.ajax( {
			url : ctxpath+"/group/ajaxGroupNode",
			type : 'post',
			dataType:'json',
			data : {"groupIdNodeIdStr":groupIdNodeIdStr},
			success : function(data) {
				common.ajaxSessionTimeoutHandler(data);
				$.smate.scmtips.show(data.result, data.msg);
				if(data.result == 'success'){
					$(obj).remove();
				}else{
					return;
				}
			},
			error : function(data){
				$.smate.scmtips.show('error',dynamicMsg.optFailed);
			}
		});
	}
};


//导入到我的成果库
DynMessageUtil.impMyPub=function(nodeId,pubId, obj){
	var params=[];
	params.push({"nodeId":nodeId,"pubId":pubId,"ispubview":"true"});
	var postData={"jsonParams":JSON.stringify(params),"articleType":"1"};
	//增加"des3GroupId"参数以兼容对群组资源的检查_MJG_SCM-3617.
	var outter = this;
	var jsonParam = {
			"resType":1,
			"resNode":nodeId,
			"resId":pubId,
			"des3GroupId":outter.des3GroupId
	};
	
	var collectDiv = $(obj).closest(".collect_box").find("a:first-child");
	var isDel = this.ajaxCheckResIsDel(jsonParam, collectDiv);
	if (!isDel) {
		var showOverId = $(obj).parents("li[id^='li_']").attr("id");
		if(typeof(Loadding_div) != "undefined" && $.isFunction(Loadding_div.show_over)){
			$(obj).parent().parent().parent().fadeOut();
			Loadding_div.show_over(showOverId,showOverId);
		}
		
		$.ajax({
			url:"/pubweb/publication/ajaximporttomypub",
			type:"post",
			data:postData,
			timeout:10000,
			success:function(data){
				if(typeof data!='undefined' && data && data.result=='success'){
					$.smate.scmtips.show('success',data.msg);
					$.Thickbox.closeWin();
				} else {
					var timeout = data.ajaxSessionTimeOut;
					
					 if (typeof timeout != 'undefined' && timeout == 'yes') {
						 jConfirm(dynamicMsg.sessionTimeout, dynamicMsg.prompt, function(r) {
							 if (r) {
								 if(typeof rb!=='undefined'&&rb==true){//站外检索，收藏成果
									 document.location.href = document.location.href.replace("/in/view", "/resume/psnView");
								}else{
								 document.location.reload();
								}
							 }
						 });
					 } else {
						 $("#detail_import_title").attr("alt",ctxpath+"/grouppub/ajaximportmypub/show?TB_iframe=true&height=160&width=700");
						 $("#detail_import_title").click();
					 }
				}
				if(typeof(Loadding_div) != "undefined" && $.isFunction(Loadding_div.show_over)){
					Loadding_div.close(showOverId);
				}
			},
			error:function(){
				$.Thickbox.closeWin();
			}
		});	
	} else {
		$(collectDiv).unbind('mouseenter mouseleave').attr("onmouseover", "").next(".collect_list").hide();
	}
};
//导入到我的文献库
DynMessageUtil.impMyRef=function(obj,nodeId,pubId, obj){
	var outter = this;
	//增加"des3GroupId"参数以兼容对群组资源的检查_MJG_SCM-3617.
	var jsonParam = {
			"resType":1,
			"resNode":nodeId,
			"resId":pubId,
			"des3GroupId":outter.des3GroupId
	};
	
	var collectDiv = $(obj).closest(".collect_box").find("a:first-child");
	var isDel = this.ajaxCheckResIsDel(jsonParam, collectDiv);
	if (!isDel) {
		var ownerId=$(obj).attr("pubOwnerId");
		var params=[];
		params.push({"nodeId":nodeId,"pubId":pubId,"ownerId":ownerId,"ispubview":"true"});
		var postData={"jsonParams":JSON.stringify(params),"articleType":"2"};
		
		var showOverId = $(obj).parents("li[id^='li_']").attr("id");
		if(typeof(Loadding_div) != "undefined" && $.isFunction(Loadding_div.show_over)){
			$(obj).parent().parent().parent().fadeOut();
			Loadding_div.show_over(showOverId,showOverId);
		}
		
		try{
			$.ajax({
				url:ctxpath+"publication/ajaximporttomypub",
				type:"post",
				dataType:"json",
				data:postData,
				timeout:10000,
				success:function(data){
					if(data && data.result == 'success'){
						$.smate.scmtips.show('success',data.msg);
					} else {
						 var timeout = data.ajaxSessionTimeOut;
						 if (typeof timeout != 'undefined' && timeout == 'yes') {
							 jConfirm(dynamicMsg.sessionTimeout, dynamicMsg.prompt, function(r) {
								 if (r) {
									 if(typeof rb!=='undefined'&&rb==true){//站外检索，收藏成果
										 document.location.href = document.location.href.replace("/in/view", "/resume/psnView");
									}else{
									 document.location.reload();
									}
								 }
							 });
						 } 
					}
					$.Thickbox.closeWin();
					
					if(typeof(Loadding_div) != "undefined" && $.isFunction(Loadding_div.show_over)){
						Loadding_div.close(showOverId);
					}
				},
				error:function(){
					$.Thickbox.closeWin();
				}
			});	
		}catch(e){
			$.Thickbox.closeWin();
		}
	} else {
		$(collectDiv).unbind('mouseenter mouseleave').attr("onmouseover", "").next(".collect_list").hide();
	}
};

//导入到我的成果库
DynMessageUtil.impMyPubFromPdwh=function(nodeId,pubId,dbid, obj){
	var outter = this;
	//增加"des3GroupId"参数以兼容对群组资源的检查_MJG_SCM-3617.
	var jsonParam = {
			"resType":22,
			"resNode":nodeId,
			"resId":pubId,
			"dbid":dbid,
			"des3GroupId":outter.des3GroupId
	};
	
	var collectDiv = $(obj).closest(".collect_box").find("a:first-child");
	var isDel = this.ajaxCheckResIsDel(jsonParam, collectDiv);
	if (!isDel) {
		var params=[];
		params.push({"pubId":pubId,"dbid":dbid});
		var postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"1"};
		$.ajax({
			url:ctxpath+"/publication/import/pdwh",
			type:"post",
			data:postData,
			timeout:10000,
			success:function(data){
				if(typeof data!='undefined' && data && data.result){
					$.smate.scmtips.show('success',dynamicMsg.optSuccess);
					$.Thickbox.closeWin();
				} else {
					var timeout = data.ajaxSessionTimeOut;
					 if (typeof timeout != 'undefined' && timeout == 'yes') {
						 jConfirm(dynamicMsg.sessionTimeout, dynamicMsg.prompt, function(r) {
							 if (r) {
								 document.location.reload();
							 }
						 });
					 } else {
						 $("#detail_import_title").attr("alt",ctxpath+"/publication/pdwh/showdup?xmlId="+data.xmlId +"&TB_iframe=true&height=160&width=700");
						 $("#detail_import_title").click();	
					 }
				}
			},
			error:function(){
				$.Thickbox.closeWin();
			}
		});
	} else {
		$(collectDiv).unbind('mouseenter mouseleave').attr("onmouseover", "").next(".collect_list").hide();
	}
};
//导入到我的文献库
DynMessageUtil.impMyRefFromPdwh=function(nodeId,pubId,dbid, obj){
	var outter = this;
	//增加"des3GroupId"参数以兼容对群组资源的检查_MJG_SCM-3617.
	var jsonParam = {
			"resType":22,
			"resNode":nodeId,
			"resId":pubId,
			"dbid":dbid,
			"des3GroupId":outter.des3GroupId
	};
	
	var collectDiv = $(obj).closest(".collect_box").find("a:first-child");
	var isDel = this.ajaxCheckResIsDel(jsonParam, collectDiv);
	if (!isDel) {
		var params=[];
		params.push({"pubId":pubId,"dbid":dbid});
		var postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"2"};
		try{
			$.ajax({
				url:ctxpath+"/reference/import/pdwh",
				type:"post",
				dataType:"json",
				data:postData,
				timeout:10000,
				success:function(data){
					if(data && data.result){
						$.smate.scmtips.show('success',dynamicMsg.optSuccess);
					} else {
						 var timeout = data.ajaxSessionTimeOut;
						 if (typeof timeout != 'undefined' && timeout == 'yes') {
							 jConfirm(dynamicMsg.sessionTimeout, dynamicMsg.prompt, function(r) {
								 if (r) {
									 document.location.reload();
								 }
							 });
						 } 
					}
					$.Thickbox.closeWin();
				},
				error:function(){
					$.Thickbox.closeWin();
				}
			});	
		}catch(e){
			$.Thickbox.closeWin();
		}
	} else {
		$(collectDiv).unbind('mouseenter mouseleave').attr("onmouseover", "").next(".collect_list").hide();
	}
};
/**
 * 显示收藏的div标签框.
 */
DynMessageUtil.showCollectDiv=function(obj){
	var target_div=$(obj).parent().find('.collect_list');
	$(target_div).show();
	var isOnDiv='F';
	$(target_div).hover(
		  function () {
			  $(this).show();
			  isOnDiv='T';
		  },
		  function () {
			  $(this).stop(true,true).hide();
		  }
	);
	$(obj).hover(
		  function () {
			  $(target_div).fadeIn();
		  },
		  function () {
			  setTimeout(function(){
				  if(isOnDiv=='F'){
						$(target_div).fadeOut();
				  }
			  },1100);
		  }
	);
};

//加载认同信息
DynMessageUtil.ajaxDynamicEndorse = function(dynIds){
	$.ajax({
		url:ctxpath+"/psnView/dynamicEndorseInfo",
		type:"post",
		dataType:"json",
		data:{"dynIds":dynIds.join(',')},
		success:function(data){
			if(data.result=="success"&&data.dynContent!=null){
				var dynContext =  eval("("+data.dynContent+")");
				for(var id in dynContext){
					$("#li_"+dynContext[id].dynId).find(".appraisa-choose").append(dynContext[id].endorse);
				}
			}
		}
	});
} 
/**
 * 显示收藏的div标签框.
 */
DynMessageUtil.showCollectDiv=function(obj){
	var target_div=$(obj).parent().find('.collect_list');
	$(target_div).show();
	var isOnDiv='F';
	$(target_div).hover(
		  function () {
			  $(this).show();
			  isOnDiv='T';
		  },
		  function () {
			  $(this).stop(true,true).hide();
		  }
	);
	$(obj).hover(
		  function () {
			  $(target_div).fadeIn();
		  },
		  function () {
			  setTimeout(function(){
				  if(isOnDiv=='F'){
						$(target_div).fadeOut();
				  }
			  },1100);
		  }
	);
};

DynMessageUtil.ajaxCheckResIsDel = function(jsonParam, obj) {
	return false;
};

function resDelCallback(obj) {
	$.smate.scmtips.show("error", dynamicMsg.resIsDelTip);
	$(obj).removeClass("Blue").addClass("gray999").unbind("click").attr("onclick", "");
};

function documentClickBox() {
	$("#showMenuList").hide();
	$("#selDymic").hide();
	// 点击别处，隐藏回复框
	$(".comment_textarea").each(function() {
		$(this).hide();
		$(this).parent().find("input").show();
	});

};

/**
 * 打开评论回复输入框.
 */
DynMessageUtil.replyToPsn = function(resId, resType, resNode, dynId, des3ReceiverId, e, obj){
	this.showReplyBox(resId, resType, resNode, dynId, e, obj);
	var replyToPsnTip = dynamicMsg.replyToPsnTip+"&nbsp;"+$(obj).attr("receiverPsnName")+("zh_CN" == locale ? "：" : ": ");
    var $replyBoxObj = $("#textarea_box_" + dynId + "_" + resId);
    if($replyBoxObj.is(":hidden")){
    	return;
    }
    //if(Sys.firefox){
    if(client.browser.name == "Firefox"){
    	$replyBoxObj.html('<img  class="replyToPsnTip" style="background: none;border:0 none;"/>&nbsp;');
    }else{
    	$replyBoxObj.html('<button onclick="return false;" contenteditable="false" class="replyToPsnTip" style="background: none;border:0 none;">'+replyToPsnTip+'</button>&nbsp;');
    }
    // 将光标移至结尾(Firefox、Chrome光标没有定位到结尾)
    var replyBoxObj = $replyBoxObj[0]; // jquery对象转换为dom对象
    replyBoxObj.focus();
    if(window.getSelection){
    	var selObj = window.getSelection();            
        var rangeObj = document.createRange();
        rangeObj.setStart(replyBoxObj.lastChild, replyBoxObj.lastChild.length);
        selObj.removeAllRanges();
        selObj.addRange(rangeObj);
    }
};


DynMessageUtil.deleteReply = function(dynId, recordId, resId, resType, resNode, e, obj){
	var outter = this;
	var jsonParam = {
			"resId" : resId,
			"resType" : resType,
			"resNode" : resNode,
			"des3GroupId":outter.des3GroupId
	};
	var isDel = this.ajaxCheckResIsDel(jsonParam, obj);
	if(isDel){
		return;
	}
	var post_data = { "dynId" : dynId, "recordId" : recordId, "resId" : resId, "resType" : resType, "resNode" : resNode };
	$.ajax( {
		url : ctxpath + "/dynamic/ajaxDeleteReply",
		type : 'post',
		dataType : "json",
		data : post_data,
		success : function(data){
			if(data.result=="success"){
				var replyItem = $(".reply_item_" + recordId);
				if (replyItem.prev().length <= 0) {
					var replyMoreDD = $('#reply_more_dd_' + dynId + '_' + resId);
					replyMoreDD.attr('beginrecord', parseInt(replyMoreDD.attr('beginrecord')) - 1);
				}
				if (replyItem.next().length <= 0) {
					$('#reply_more_dd_' + dynId + '_' + resId).attr('removeLast', 0);
				}
				replyItem.remove();
				$('.reply_num_label_' + resType + '_' + resId).each(function() {
					var replyNum = parseInt($(this).text());
					if (replyNum <= 1) {
						$(this).text(0);
						$(this).parent().hide();
					} else {
						$(this).text(replyNum - 1);
					}
				});
			}else{
				$.smate.scmtips.show("error", dynamicMsg.optFailed);
			}
		},
		error : function(){
			$.smate.scmtips.show("error", dynamicMsg.optFailed);
		}
	});
};
/**
 * 提示资源文件已删除_MJG_SCM-3619.
 */
DynMessageUtil.showDelMsg=function(){
	$.smate.scmtips.show("error", dynamicMsg.resIsDelTip);
};

/**
 * 下载资源.
 */
DynMessageUtil.ajaxDownloadRes = function(obj,resType,resNode,resId) {
	var outter = this;
	var jsonParam = {
			"resType" : Number(resType),
			"resNode" : Number(resNode),
			"resId" : Number(resId),
			"des3GroupId":outter.des3GroupId
	};
	$.ajax({
		url: ctxpath + "/dynamic/ajaxDownloadRes",
		type:"post",
		data:{
			"jsonParam":JSON.stringify(jsonParam)
		},
		dataType: "json",
        async : false,
		success:function(data){
			if(data.ajaxSessionTimeOut == "yes"){
				var timeout = data.ajaxSessionTimeOut;
				 if (typeof timeout != 'undefined' && timeout == 'yes') {
					 jConfirm(dynamicMsg.sessionTimeout, dynamicMsg.prompt, function(r) {
						 if (r) {
							 document.location.reload();
						 }
					 });
				 }
			}else{
				if (data.result = "success") {
					if(data.resIsDel == 1) {
						$.smate.scmtips.show("error", dynamicMsg.resIsDelTip);
					}else{
						document.location.href = data.dPath;
					}
				}
			}
		},
		error:function(data) {
		}
	});
};

/**
 * 是否以弹出框的方式显示图片.
 */
DynMessageUtil.ajaxIsShowPicInTB = function(obj,resType,resNode,resId){
	var outter = this;
	var jsonParam = {
			"resType" : Number(resType),
			"resNode" : Number(resNode),
			"resId" : Number(resId),
			"des3GroupId":outter.des3GroupId
	};
	var isDel = this.ajaxCheckResIsDel(jsonParam, obj);
	if(isDel){
		$(obj).removeAttr("href");
		$(obj).unbind();
	}
};

/**
 * 分享给联系人.
 * @author pengweilong
 * @param receivers
 * @param shareContent
 * @param commendDendLine
 * @param shareTitleCN
 * @param shareTitleEN
 * @return
 */
function sendShareForFriend(receivers,shareContent,commendDendLine,shareTitleCN,shareTitleEN){	
	var jsonParam=shareConfig.jsonParam;
	jsonParam["shareTitle"]=shareTitleCN;
	jsonParam["shareEnTitle"]=shareTitleEN;
	var resType =shareConfig.resType;
	var post_data = {
			"receivers" :receivers,
			"des3PubIds":shareConfig.des3Ids,
			"shareDeadline":commendDendLine,
			"articleName":"publication",
			"resType":resType,
			"content":shareContent,
			"jsonParam":JSON.stringify(jsonParam)
	};
	$.ajax( {
		url : ctxpath + "/commend/publication/ajaxCommendPub",
		type : 'post',
		dataType:"json",
		data : post_data,
		success : function(data){
			if(data.result=="success"){
				//更新分享数
				var resIdArray=(shareConfig.resIds).split(",");
				for(var i=0;i<resIdArray.length;i++){
					var shareCountObj=$(".shareCountLabel_"+resType+"_"+resIdArray[i]);
					var shareSpanObj=$(".shareCountSpan_"+resType+"_"+resIdArray[i]);
					//成果分享样式不一样，需做区别对待
					var isCount=$(shareCountObj).attr("count");
					if(typeof (isCount) == "undefined"){
						$(shareCountObj).text(parseInt($(shareCountObj[0]).text())+1);
						$(shareCountObj).parent().show().parent().show();
						$(shareSpanObj).show();
					}else{
						var count=parseInt($(shareCountObj).attr("count"))+1;
						$(shareCountObj).attr("count",count);
						$(shareCountObj).text("("+count+")");
					}
				}
				$.smate.scmtips.show("success", dynamicMsg.optSuccess);
				$.Thickbox.closeWin();
			}else{
				$.smate.scmtips.show("error", dynamicMsg.optFailed);
			}
		},
		error : function(){
			$.smate.scmtips.show("error", dynamicMsg.optFailed);
		}
	});
};

/**
 * 打开或关闭分享站点列表.
 */
function showShareSites(){

	var outter = this;
	
	$(".share_sites_show").each(function(){
		var _this = $(this);
		_this.click(function(e){
			$(".share_site_list").each(function(){
				$(this).hide();
			});
			var resId = _this.parent().find(".share_pull").attr("resId");
			var resType = _this.parent().find(".share_pull").attr("resType");
			var resNode = _this.parent().find(".share_pull").attr("resNode");

			//增加"des3GroupId"参数以兼容对群组资源的检查_MJG_SCM-3617.
			var jsonParam = {
					"resId" : resId,
					"resType" : resType,
					"resNode" : resNode,
					"des3GroupId":outter.des3GroupId
			};
			
			var isDel = DynMessageUtil.ajaxCheckResIsDel(jsonParam, this);
			if(!isDel){
				var _obj = _this.parent().find(".share_site_list");
				// 显示隐藏
				if (_obj.is(":hidden")) {
					_obj.show();
				} else {
					_obj.hide();
					return;
				}
				//点击其他地方关闭
			    if (e && e.stopPropagation) {//非IE  
			        e.stopPropagation();  
			    }  
			    else {//IE  
			        window.event.cancelBubble = true;  
			    } 
			    $(document).click(function(){_obj.hide();});
			}
		});
	});
};

// 评论回复、删除操作显示或隐藏
function replyOperatShowOrHide(){
	$(".f_comment2").find("dd").each(function(){
		$(this).bind({
		"mouseover":function(){
			$(this).find(".reply_operation").show();
		},
		"mouseout":function(){
			$(this).find(".reply_operation").hide();
		}});
	});
}

//Login reminder
function LGtemp_reminder(){
	
	$(".share_sites_show").click(function(resType, resNode, resId){
		var jsonParam = "{\"resType\":" + Number(resType) + ",\"resNode\":"
		+ Number(resNode) + ",\"resId\":" + Number(resId)
		+ ",\"permission\":0}";
		
		$.ajax( {
			url : ctxpath + "/dynamic/ajaxAward",
			type : "post",
			data : {
				"jsonParam" :  encodeURIComponent(jsonParam)
			},
			dataType : "json",
			success : function(data){
				if (data.result != "success" && data.ajaxSessionTimeOut == "yes"){
					
					jConfirm(Share1.loginCfmTip, Share1.reminder, function(result) {
						if (result)
							{
							var host2=location.host; 
							location.href = ctxpath + "?service="
							+ encodeURIComponent("http://"+host2+ctxpath+"/resume/psnView"+location.search);
							}
					});
				}
				
				
			},	
			error : function(e) {
			}
		});
	});
	
}


/**
 * 分享给联系人.
 * @author wangsunan
 * @param receivers
 * @param shareContent
 * @param commendDendLine
 * @param shareTitleCN
 * @param shareTitleEN
 * @return
 */
function sendShareForFriendNew(receivers,shareContent,commendDendLine,shareTitleCN,shareTitleEN,isAddShareTimes){
	var jsonParam=shareConfig.jsonParam;
	jsonParam["shareTitle"]=shareTitleCN;
	jsonParam["shareEnTitle"]=shareTitleEN;
	jsonParam["isAddShareTimes"]=isAddShareTimes;
	var resType =shareConfig.resType;
	var post_data = {
			"receivers" :receivers,
			"des3PubIds":shareConfig.des3Ids,
			"shareDeadline":commendDendLine,
			"articleName":"publication",
			"resType":resType,
			"content":shareContent,
			"jsonParam":JSON.stringify(jsonParam)
	};
	$.ajax( {
		url : ctxpath + "/commend/publication/ajaxCommendPub",
		type : 'post',
		dataType:"json",
		data : post_data,
		success : function(data){
			if(isAddShareTimes){
				if(data.result=="success"){
					//更新分享数
					var resIdArray=(shareConfig.resIds).split(",");
					for(var i=0;i<resIdArray.length;i++){
						var shareCountObj=$(".shareCountLabel_"+resType+"_"+resIdArray[i]);
						var shareSpanObj=$(".shareCountSpan_"+resType+"_"+resIdArray[i]);
						//成果分享样式不一样，需做区别对待
						var isCount=$(shareCountObj).attr("count");
						if(typeof (isCount) == "undefined"){
							$(shareCountObj).text(parseInt($(shareCountObj[0]).text())+1);
							$(shareCountObj).parent().show().parent().show();
							$(shareSpanObj).show();
						}else{
							var count=parseInt($(shareCountObj).attr("count"))+1;
							$(shareCountObj).attr("count",count);
							$(shareCountObj).text("("+count+")");
						}
					}
					$.scmtips.show("success", dynamicMsg.optSuccess);
					$.Thickbox.closeWin();
				}else{
					$.scmtips.show("error", dynamicMsg.optFailed);
				}
			}
		},
		error : function(){
			$.scmtips.show("error", dynamicMsg.optFailed);
		}
	});
}
