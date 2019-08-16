var smate = smate ? smate : {};
smate.msgcount = smate.msgcount ? smate.msgcount : {};
/**
 * SCM-13823 在联系人和群组菜单上增加提醒
 */
smate.msgcount.prompt = function(){
	$.ajax( {
		url : '/dynweb/showmsg/ajaxmenumsgprompt',
		type : 'post',
		dataType:'json',
		success : function(data) {
			if(data.result=="success"){
				var $_lis = $(".header-nav__list").find("span");
				if(Number(data['pCount'])>0){
					$_lis.eq(2).append("<div class='header-nav__item-redball'></div>");
				}
				if(Number(data['gCount'])>0){
					$_lis.eq(3).append("<div class='header-nav__item-redball'></div>");
				}
			}
		},
		error: function (){
		}
	});
}
/**
 * 刷新头部消息统计
 */
smate.msgcount.header = function(){
	//加载消息提示
	$.ajax( {
		url : '/dynweb/showmsg/ajaxcountunreadmsg',
		type : 'post',
		dataType:'json',
		success : function(data) {
			if(data.result=="success"){
				var full_text_request_msg_count=parseInt(data["11"]);
				if(full_text_request_msg_count>0){
					$("#full_text_request_msg_count").addClass("header-main__action_unread").html(full_text_request_msg_count>99?'99+':full_text_request_msg_count);
				}else{
					$("#full_text_request_msg_count").html("").removeClass("header-main__action_unread");
				}			
				var chat_msg_count=parseInt(data["7"]);
				if(chat_msg_count>0){
					$("#chat_msg_count").addClass("header-main__action_unread").html(chat_msg_count>99?'99+':chat_msg_count);
				}else{
					$("#chat_msg_count").html("").removeClass("header-main__action_unread");
				}	
				var other_msg_count=parseInt(data["other"]);
				if(other_msg_count>0){
					$("#other_msg_count").addClass("header-main__action_unread").html(other_msg_count>99?'99+':other_msg_count);
				}else{
					$("#other_msg_count").html("").removeClass("header-main__action_unread");
				}											
			}
		},
		error: function (){
			
		}
	});
}