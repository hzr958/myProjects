var dynamicShare;
var showMenuTimeout;
DynamicShare = function() {
	this.initShareMaint = DynamicShare.initShareMaint;
	this.selectChange = DynamicShare.selectChange;
	this.showMenuShow = DynamicShare.showMenuShow;
	this.showMenuHide = DynamicShare.showMenuHide;
	this.showMenuChoose = DynamicShare.showMenuChoose;
	this.showShareTxt = DynamicShare.showShareTxt;
	this.initSharePubTxt = DynamicShare.initSharePubTxt;
};

/**
 * 初始化.
 */
DynamicShare.initShareMaint = function() {
	// 隐藏、首选
	var shareType = parent.shareConfig.shareType;
	var shareTypeFirst = parent.shareConfig.shareTypeFirst;
	var shareTypeArray = shareType.split(",");
	for ( var i = 0; i < shareTypeArray.length; i++) {
		$("#shareTypeDiv" + shareTypeArray[i]).show();
	}
//	if (parent.shareContentConfig.mineDynContent != null
//			&& typeof (parent.shareContentConfig.mineDynContent) != "undefined") {
//		$("#mineDynContent").html(parent.shareContentConfig.mineDynContent);
//	}
	//指定联系人.
	if (parent.shareContentConfig.friendShareContent != null
			&& typeof (parent.shareContentConfig.friendShareContent) != "undefined") {
		$("#friendShareContent").html(
				parent.shareContentConfig.friendShareContent);
	}
	//指定联系人发送消息.
	if (parent.shareContentConfig.friendSendTitle != null
			&& typeof (parent.shareContentConfig.friendSendTitle) != "undefined") {
		$("#friendSendTitle").val(parent.shareContentConfig.friendSendTitle);
	}
	if (parent.shareContentConfig.friendSendContent != null
			&& typeof (parent.shareContentConfig.friendSendContent) != "undefined") {
		$("#friendSendContent").html(
				parent.shareContentConfig.friendSendContent);
	}
	//发送群组邀请消息.
	if (parent.shareContentConfig.InviteSendTitle != null
			&& typeof (parent.shareContentConfig.InviteSendTitle) != "undefined") {
		$("#InviteSendTitle").val(parent.shareContentConfig.InviteSendTitle);
	}
	if (parent.shareContentConfig.InviteSendContent != null
			&& typeof (parent.shareContentConfig.InviteSendContent) != "undefined") {
		$("#InviteSendContent").html(
				parent.shareContentConfig.InviteSendContent);
	}
	//分享简历.
	if (parent.shareContentConfig.shareCVToFriendContent != null
			&& typeof (parent.shareContentConfig.shareCVToFriendContent) != "undefined") {
		$("#shareCVToFriendContent").html(
				parent.shareContentConfig.shareCVToFriendContent);
	}
	//指定群组.
	if (parent.shareContentConfig.groupShareContent != null
			&& typeof (parent.shareContentConfig.groupShareContent) != "undefined") {
		$("#groupShareContent").html(
				parent.shareContentConfig.groupShareContent);
	}
	
	if(parent.shareContentConfig.mineDynContent!=null&& typeof (parent.shareContentConfig.mineDynContent) != "undefined"){
		$("#mineDynContent").html(parent.shareContentConfig.mineDynContent);
	}
	
	if(parent.shareContentConfig.dynShareContent!=null&& typeof (parent.shareContentConfig.dynShareContent) != "undefined"){
		$("#dyn—mineDynContent").html(parent.shareContentConfig.dynShareContent);
	}
	
	if (parent.shareContentConfig.cvName != null && typeof (parent.shareContentConfig.cvName) != "undefined") {
		$("#shareTypeDiv7_cvName").html(parent.shareContentConfig.cvName);
		$("#shareTypeDiv9_cvName").html(parent.shareContentConfig.cvName);
	}

	if(shareTypeFirst=='8'){
		DynamicShare.initInviteSendInfo();
	}
	$("#shareTypeDetailDiv" + shareTypeFirst).show();
	$("#shareTypeRadio" + shareTypeFirst).attr("checked", "checked");
	$("#selectItem").val("shareTypeDetailDiv" + shareTypeFirst);
	// 分享成果和文献
	if (parent.shareConfig.resType == 1 || parent.shareConfig.resType == 2 || parent.shareConfig.resType == 4) {
		this.initSharePubTxt();
	}
};
/**
 * 初始化发送群组邀请信息的标签内容(dyn_share_send_invite_content.jsp页面中span标签的内容).
 */
DynamicShare.initInviteSendInfo=function(){
	var groupDesc=parent.shareGroupInfo.groupDescription;
	if(groupDesc==null||groupDesc==''){
		groupDesc=noDesc;//noDesc 在dyn_share_send_invite_content.jsp页面中初始化.
	}
	$("#groupName").html(parent.shareGroupInfo.groupName);
	$("#groupDescription").html(groupDesc);
	$("#sumMembers").html(parent.shareGroupInfo.sumMembers);
	$("#sumPubs").html(parent.shareGroupInfo.sumPubs);
	$("#sumRefs").html(parent.shareGroupInfo.sumRefs);
	$("#sumPrjs").html(parent.shareGroupInfo.sumPrjs);
	$("#sumFiles").html(parent.shareGroupInfo.sumFiles);
};
/**
 * 选中change.
 */
DynamicShare.selectChange = function() {
	var obj = this;
	var selectItem = $("#selectItem").val();
	$("#" + selectItem).hide();
	$(obj).attr("checked", "checked");
	$("#selectItem").val("shareTypeDetailDiv" + $(obj).val());
	var selectItem = $(obj).parent().parent().find("div.shareTypeDetailDiv");
	$(selectItem).show();
	
	//将成果分享中的输入框设置焦点。scm-4391的类似问题
	var t=$("#friendShareContent").html();
	$("#friendShareContent").html("").focus().html(t);
	var t=$("#mineDynContent").html();
	$("#mineDynContent").html("").focus().html(t);
	var t=$("#groupShareContent").html();
	$("#groupShareContent").html("").focus().html(t);
	var t=$("#resumeMineDynContent").html();
	$("#resumeMineDynContent").html("").focus().html(t);
	var t=$("#shareCVToFriendContent").html();
	$("#shareCVToFriendContent").html("").focus().html(t);
	var t=$("#friendSendContent").html();
	$("#friendSendContent").html("").focus().html(t);
};

DynamicShare.showMenuShow = function(obj,showId,e) {
	if (showMenuTimeout != null && typeof (showMenuTimeout) != "undefined") {
		clearTimeout(showMenuTimeout);
	} 
	$("#"+showId).fadeIn();
    if (e && e.stopPropagation) {//非IE  
        e.stopPropagation();  
    }  
    else {//IE  
        window.event.cancelBubble = true;  
    } 
    $(document).bind("click",documentClick);
};

function documentClick(){
	$("#showMenuList1").hide();
	$("#showMenuList6").hide();
}

DynamicShare.showMenuHide = function(hideId) {
	showMenuTimeout = setTimeout(function() {
		$("#"+hideId).fadeOut();
	}, 100);
};

DynamicShare.showMenuChoose = function(obj, flag,inputRight,labelId) {
	$(obj).parent().parent().find("i.img_choose").attr("class", "img_nochoose");
	$(obj).find("i").attr("class", "img_choose");
	$("#"+labelId).text($(obj).text());
	$("#"+inputRight).val(flag);
	if(flag==1){
		$("#"+labelId+"_icon").attr("class","icon_people");
	}else{
		$("#"+labelId+"_icon").attr("class","icon_people2");
	}
	$("#showMenuList").hide();
};

DynamicShare.initSharePubTxt = function() {
	var post_data = {'resIds' : parent.shareConfig.resIds, 'resType' : parent.shareConfig.resType};
	if ('undefined' != typeof parent.shareConfig.dbid) {
		post_data['dbid'] = parent.shareConfig.dbid;
	}
	$.ajax({
		url : ctxpath + "/dynamic/ajaxGetShareTxt",
		type : 'post',
		dataType : "json",
		data : post_data,
		success : function(data){
			if (data.result == "success" && data.shareTxt != undefined && data.shareTxt != '') {
				var pubCon = eval("[" + data.shareTxt + "]");
				if (pubCon[0].showTxt != '') {
					$('#shareTxt').html(pubCon[0].showTxt).show();
				}
				if (pubCon[0].resCount == 1) {
					var split = "zh_CN" == locale ? "，" : ", ";
					parent.irisShareConfig = {
							'url' : pubCon[0].resLink,
							'pic' : pubCon[0].pic,
							'category' : parent.shareConfig.resType,
							'authorNames' : pubCon[0].authorNames,
							'title' : pubCon[0].title,
							'source' : pubCon[0].resOther,
							'language' : pubCon[0].language
					};
				}
			}
		},
		error : function(data) {
			
		}
	});
};