/**
 * 移动端-消息提示js事件
 */
var MsgPrompt = MsgPrompt ? MsgPrompt : {};

//获取联系人的数量
MsgPrompt.friendNumbers = function(desPsnId){
	$.ajax({
		url: "/psnweb/mobile/ajaxgetreqfriendnumber",
		dataType:"json",
		type: "post",
		data :{
			"desPsnId":desPsnId
		},
		success: function(data){
			if(Number(data.friendNumbers)>0){
				$(".menu_link").addClass("hasNew");
			}
		},
		error: function(){
			
		}
	});
}

//移动端-消息菜单-红点提示
MsgPrompt.showTips = function() {
	var dataJson = {'des3PsnId': $("#currentDes3PsnId").val()};
	$.ajax({
		url : "/pubweb/wechat/ajaxmsgtips",
		dataType : "json",
		data : dataJson,
		type : "post",
		success : function(data) {
			//待确认成果数 和 待确认全文数
			if (Number(data.pubConfirmCount) > 0 || Number(data.pubFulltextCount) > 0) {
				//显示红点
				$(".menu_msg").addClass("hasNew");
			} else {
				MsgPrompt.getUnreadMsg(dataJson);
			}
		}
	});
};

//查询未读消息
MsgPrompt.getUnreadMsg = function(dataJson) {
	$.ajax({
		url : "/dynweb/mobile/ajaxcountmsg",
		dataType : "json",
		data : dataJson,
		type : "post",
		success : function(data) {
			if (data.unReadMsg > 0) {
				$(".menu_msg").addClass("hasNew");
			}
		}
	});
};


//查询未读消息总数
MsgPrompt.getUnreadMsgCount = function(dataJson) {
	$.ajax({
		url : "/dynweb/showmsg/ajaxcountunreadmsg",
		dataType : "json",
		data : dataJson,
		type : "post",
		success : function(data) {
			var unreadMsgCount = 0;
			var full_text_request_msg_count=parseInt(data["11"]);
			if(full_text_request_msg_count>0){
				unreadMsgCount += full_text_request_msg_count;
			}		
			var chat_msg_count=parseInt(data["7"]);
			if(chat_msg_count>0){
				unreadMsgCount += chat_msg_count;
			}
			var pub_req_count=parseInt(data["2"]);
			if(pub_req_count>0){
				unreadMsgCount += pub_req_count;
			}
			var fulltext_req_count=parseInt(data["3"]);
			if(fulltext_req_count>0){
				unreadMsgCount += fulltext_req_count;
			}
			/*var other_msg_count=parseInt(data["other"]);
			if(other_msg_count>0){
				unreadMsgCount += other_msg_count;
			}*/
			if(unreadMsgCount > 0){
				$("#mobile_msg_unreadcount").addClass("new-mobilepage_footer-item_num");
				$("#mobile_msg_unreadcount").html(unreadMsgCount > 99 ? '99+' : unreadMsgCount);
			}else{
				$("#mobile_msg_unreadcount").removeClass("new-mobilepage_footer-item_num");
				$("#mobile_msg_unreadcount").html("");
			}
			setTimeout(function(){
				//$("div[scm_id='load_state_ico']").remove();
				$("div[list-main='mobile_msg_center_list']").find(".preloader").remove();
			},1);
		}
	});
};


//获取联系人请求数
MsgPrompt.getfriendReqCount = function(){
	$.ajax( {
		url : '/dynweb/showmsg/ajaxmenumsgprompt',
		type : 'post',
		dataType:'json',
		success : function(data) {
			if(data.result=="success"){
				if(Number(data['pCount']) > 0){
					$("#mobile_frdreq_undeal").addClass("new-mobilepage_footer-item_num");
					$("#mobile_frdreq_undeal").html(Number(data['pCount']) > 99 ? '99+' : Number(data['pCount']));
				}else{
					$("#mobile_frdreq_undeal").removeClass("new-mobilepage_footer-item_num");
					$("#mobile_frdreq_undeal").html("");
				}
			}
		},
		error: function (){
		}
	});
};