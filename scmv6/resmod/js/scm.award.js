/**
 * SCM系统的赞操作
 * 
 * @author Scy
 */

var Award = Award ? Award
		: {
			reminder : locale == "zh_CN" ? "提示" : "Reminder",
			loginCfmTip : locale == "zh_CN" ? "系统超时或未登录，你要登录吗？"
					: "You are not logged in or session time out, please log in again",
			optSuccess : locale == "zh_CN" ? "操作成功" : "Operated successfully",
			optFailed : locale == "zh_CN" ? "操作失败" : "Operated failed",
			personalList : locale == "zh_CN" ? "赞人员列表"
					: "Praise personnel list",
			noAward : locale == "zh_CN" ? "对不起，暂时没有人赞过" : "Sorry,Nobody like"
		};

Award.awardLockFlag = "";
// 初始化赞操作
Award.iniAward = function(resType) {
	var jsonParam = "{\"resType\":" + resType + ",\"resDetails\":"
			+ Award.collectResData(resType) + "}";
	var resDataJsonObj = JSON.parse(Award.collectResData(resType))[0];
	var resNode;
	if(typeof resDataJsonObj != "undefined"){
		resNode = resDataJsonObj.resNode;
	}
	$.ajax( {
		type : "post",
		url : ctxpath + "/dynamic/ajaxIniAward",
		data : {
			"jsonParam" : encodeURIComponent(jsonParam)
		},
		dataType : "json",
		success : function(data) {
			if (data.result == "success") {
				var rtnJson = data.rtnJson;
				for ( var i = 0; i < rtnJson.length; i++) {
					var jsonObj = rtnJson[i];
					var awardTimes = $("#awardcount1_" + resType + "_" + jsonObj.resId).attr("count");
					//兼容旧版
				   if(typeof (awardTimes)=='undefined'){
						$("#awardcount_" + resType + "_" + jsonObj.resId).text(
								jsonObj.awardTimes);
						$("#awardLink_" + resType + "_" + jsonObj.resId).attr(
								"awardId", jsonObj.awardId);
						if(jsonObj.awardTimes > 0){
							$("#awardLink_" + resType + "_" + jsonObj.resId).show();
							$("#awardspan_" + resType + "_" + jsonObj.resId).show();
						}
						if (jsonObj.hasAward == 1) {
							$("#cancelaward_" + resType + "_" + jsonObj.resId)
									.show();
							$(".cancelaward_" + resType + "_" + resNode + "_" + jsonObj.resId).show();
						} else {
							$("#award_" + resType + "_" + jsonObj.resId).show();
							$(".award_" + resType + "_" + resNode + "_" + jsonObj.resId).show();
						}
						
						$(".award_psn_list" + "_" + resType + "_" + resNode + "_" + jsonObj.resId).each(function(){
							$(this).html(jsonObj.awardPsnContent);
						});
						$(".award_num" + "_" + resType + "_" + resNode + "_" + jsonObj.resId).each(function() {
							if(jsonObj.awardTimes > 0){
								$(this).show();
								$(this).find(".award_num").text(jsonObj.awardTimes);
							}else{
								$(this).find(".award_num").text(0);
							}
							$(".award_detail_thickbox" + "_" + resType + "_" + resNode + "_" + jsonObj.resId).each(function(){
								$(this).attr("awardId",jsonObj.awardId);
							});
						});
						$(".award_detail" + "_" + resType + "_" + resNode + "_" + jsonObj.resId).each(function() {
							if(jsonObj.awardTimes > 0){
								$(this).show();
							}else{
								$(this).hide();
							}
						});
				   }else{
						if (jsonObj.hasAward == 1) {
							$("#cancelaward_" + resType + "_" + jsonObj.resId)
									.show();
							$("#awardcount2_" + resType + "_" + jsonObj.resId).text(
									"("+jsonObj.awardTimes+")");
							$("#awardcount2_" + resType + "_" + jsonObj.resId).attr("count",jsonObj.awardTimes);
						} else {
							$("#awardcount1_" + resType + "_" + jsonObj.resId).attr("count",jsonObj.awardTimes);
							$("#award_" + resType + "_" + jsonObj.resId).show();
							if(jsonObj.awardTimes>0){
							$("#awardcount1_" + resType + "_" + jsonObj.resId).text(
									'('+jsonObj.awardTimes+')');
							}
						}
				   }
				}
			}
		},
		error : function(e) {
		}
	});
};

Award.collectResData = function(resType) {
	var json = [];
	$(".awardLink_p_" + resType).each(function() {
		json.push( {
			"resId" : $(this).attr("resId"),
			"resNode" : $(this).attr("nodeId")
		});
	});
	return JSON.stringify(json);
};

/**
 * 赞人员列表.
 */
Award.showAwardDetail = function(obj) {
	var awardId = $(obj).attr("awardId");
	if (awardId == 0) {
		$.scmtips.show("warn", Award.noAward);
	} else {
		$("#hidden-awardLink").attr(
				"alt",
				ctxpath + "/dynamic/ajaxShowAwardDetails?awardId=" + awardId
						+ "&TB_iframe=true&height=384&width=672");
		$("#hidden-awardLink").attr("title", Award.personalList);
		$("#hidden-awardLink").click();
	}
};

/**
 * 赞.
 */
Award.ajaxAward = function(resType, resNode, resId) {
	if (Award.awardLockFlag == ""
		|| Award.awardLockFlag != (resType + "_" + resNode + "_" + resId)){
		Award.awardLockFlag = resType + "_" + resNode + "_" + resId;
		var jsonParam = "";
		if ($("#hidden_groupIdABC").length == 0 || $("#hidden_groupIdABC").val()=="") {
			jsonParam = "{\"resType\":" + Number(resType) + ",\"resNode\":"
			+ Number(resNode) + ",\"resId\":" + Number(resId)
			+ ",\"permission\":0}";
		} else {
			jsonParam = "{\"resType\":" + Number(resType) + ",\"resNode\":"
			+ Number(resNode) + ",\"resId\":" + Number(resId)
			+ ",\"permission\":0,\"groupId\":"
			+ Number($("#hidden_groupIdABC").val()) + "}";
		}
		$.ajax( {
			url : snsctx + "/dynamic/ajaxAward",
			type : "post",
			data : {
				"jsonParam" :  encodeURIComponent(jsonParam)
			},
			dataType : "json",
			success : function(data) {
				if (data.result == "success") {
					Award.awardLockFlag = "";
					var awardTimes = $("#awardcount1_" + resType + "_" + resId).attr("count");
					//兼容旧版
					if(typeof (awardTimes)=='undefined'){
						$(".award_detail_thickbox" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							$(this).attr("awardId",data.awardId);
						});
						
						var awardTimes = $("#awardcount_" + resType + "_" + resId)
						.text();
						$("#awardcount_" + resType + "_" + resId).text(
								parseInt(awardTimes) + 1);
						$("#awardLink_" + resType + "_" + resId).attr("awardId",
								data.awardId);
						$("#cancelaward_" + resType + "_" + resId).show();
						$("#award_" + resType + "_" + resId).hide();
						$(".cancelaward_" + resType + "_" + resNode + "_" + resId).show();
						$(".award_" + resType + "_" + resNode + "_" + resId).hide();
						
						$("#awardLink_" + resType + "_" + resId).show();
						$("#awardspan_" + resType + "_" + resId).show();
						
						$(".award_psn_list" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							$(this).html(data.awardPsnContent);
						});
						$(".award_num" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							$(this).show();
							var currentAwardNum = parseInt($(this).find(".award_num").text());
							$(this).find(".award_num").text(currentAwardNum + 1);
						});
						$(".award_detail" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							$(this).show();
						});
					}else{
						awardTimes=parseInt(awardTimes) + 1;
						$("#awardcount2_" + resType + "_" + resId).text("("+awardTimes+")");
						$("#awardcount2_" + resType + "_" + resId).attr("count",awardTimes);
						$("#awardLink_" + resType + "_" + resId).attr("awardId",
								data.awardId);
						$("#cancelaward_" + resType + "_" + resId).show();
						$("#award_" + resType + "_" + resId).hide();
					}
				} else if (data.ajaxSessionTimeOut == "yes") {
					Award.awardLockFlag = "";
					jConfirm(Award.loginCfmTip, Award.reminder, function(result) {
						if (result)
							location.href = ctxpath + "?service="
							+ encodeURIComponent(location.href);
					});
				}
			},
			error : function(e) {
			}
		});
	}
	//阻止冒泡
	$.Event.stopEvent();
};


/**
 * 取消赞.
 */
Award.ajaxCancelAward = function(resType, resNode, resId) {
	if (Award.awardLockFlag == ""
		|| Award.awardLockFlag != (resType + "_" + resNode + "_" + resId)){
		Award.awardLockFlag = resType + "_" + resNode + "_" + resId;
		var jsonParam = "{\"resType\":" + Number(resType) + ",\"resNode\":"
		+ Number(resNode) + ",\"resId\":" + Number(resId)
		+ ",\"permission\":0}";
		$.ajax( {
			url : snsctx + "/dynamic/ajaxCancelAward",
			type : "post",
			data : {
				"jsonParam" : jsonParam
			},
			dataType : "json",
			success : function(data) {
				if (data.result == "success") {
					Award.awardLockFlag = "";
					var awardTimes = $("#awardcount1_" + resType + "_" + resId).attr("count");
					//兼容旧版
					if(typeof (awardTimes)=='undefined'){
						var awardTimes = $("#awardcount_" + resType + "_" + resId)
						.text();
						$("#awardcount_" + resType + "_" + resId).text(
								parseInt(awardTimes) - 1);
						$("#cancelaward_" + resType + "_" + resId).hide();
						$("#award_" + resType + "_" + resId).show();
						$(".cancelaward_" + resType + "_" + resNode + "_" + resId).hide();
						$(".award_" + resType + "_" + resNode + "_" + resId).show();
						if (parseInt(awardTimes) - 1 <= 0) {
							$("#awardLink_" + resType + "_" + resId).hide();
							$("#awardspan_" + resType + "_" + resId).hide();
						}
						
						$(".award_psn_list" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							$(this).html(data.awardPsnContent);
						});
						var currentAwardNum=null;
						$(".award_num" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							currentAwardNum = parseInt($(this).find(".award_num").text())-1;
							$(this).find(".award_num").text(currentAwardNum<0?0:currentAwardNum);
							if(currentAwardNum<=0){
								$(this).hide();
							}
						});
						$(".award_detail" + "_" + resType + "_" + resNode + "_" + resId).each(function(){
							if(currentAwardNum<=0){
								$(this).hide();
							}
						});
					}else{
						var awardTimes = $("#awardcount2_" + resType + "_" + resId).attr("count");
						var count=parseInt(awardTimes) - 1;
						$("#awardcount2_" + resType + "_" + resId).attr("count",count);
						$("#awardcount1_" + resType + "_" + resId).attr("count",count);
						if(count>0){
							$("#awardcount1_" + resType + "_" + resId).text("("+count+")");
						}
						$("#cancelaward_" + resType + "_" + resId).hide();
						$("#award_" + resType + "_" + resId).show();
					}
				} else if (data.ajaxSessionTimeOut == "yes") {
					Award.awardLockFlag = "";
					jConfirm(Award.loginCfmTip, Award.reminder, function(result) {
						if (result)
							location.href = ctxpath + "?service="
							+ encodeURIComponent(location.href);
					});
				}
			},
			error : function(e) {
			}
		});
	}
	$.Event.stopEvent();
};

