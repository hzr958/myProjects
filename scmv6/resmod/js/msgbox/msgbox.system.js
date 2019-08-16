/**
 * 未读分享数.
 * 
 * @return
 */
function systemInitBoxUnReadCount() {
	setTimeout(function() {
		var systemUnReadCount = $.trim($("#displaySystemCount").text());
		if (systemUnReadCount != "") {
			$("#label-system-unread").text(systemUnReadCount);
		}
	}, 500);
}

/**
 * 批量删除系统消息.
 * @return
 */
function systemDeleteBatch(){
	var ids=MsgBoxUtil.collectIds("systemCheckbox");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids,"msgNoticeStyle",deleteMessageCallBack);
}

/**
 * 单个删除.
 * @return
 */
function systemDeleteOne(){
	var ids=$(this).attr("mailId");
	MsgBoxUtil.ajaxDeleteInboxMessage(ids,"msgNoticeStyle",deleteMessageCallBack);
}

/**
 * 删除回掉函数.
 * @param ids
 * @return
 */
function deleteMessageCallBack(ids){
	$.smate.scmtips.show("success", msgboxTip.optSuccess);
	setTimeout(function(){
		$("#mainForm").submit();
	},500);
}

/**
 * 将状态更新为已读.
 * @return
 */
function systemRead(){
	var resIds=MsgBoxUtil.collectIdsAll("systemCheckbox");
	$.ajax({
		type:"post",
		url:snsctx+"/msgbox/ajaxUpdateMessageStatus",
		data:{
		  "resIds":resIds,
		  "type":"msgNoticeStyle",
		  "statusType":"read"
	    },
	    dataType:"json",
	    success:function(data){
	    }
	});
}