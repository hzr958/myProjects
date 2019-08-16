var smate = smate ? smate : {};

smate.groupmain = smate.groupmain ? smate.groupmain : {};

smate.groupmain.prompt="<s:text name='maint.label.prompt'/>";
smate.groupmain.opFaild="<s:text name='maint.action.fail'/>";
smate.groupmain.opSuccess="<s:text name='maint.action.success'/>";
smate.groupmain.deleteGroup="<s:text name='page.group.confirm.deleteGroup'/>";
smate.groupmain.cancelApply="<s:text name='page.group.confirm.cancelApply'/>"; 
smate.groupmain.noAudit="<s:text name='page.group.alert.noAudit'/>";
smate.groupmain.exitGroup="<s:text name='page.group.confirm.exitGroup'/>";

smate.groupmain.doSearch = function(code,ownerType){
	 $("#code").val('');
	 $("#ownerType").val('');
    if(typeof code !=='undefined' && code!=''){
		$("#code").val(code);
		$("#groupName").val('');
	}
	if(typeof ownerType !=='undefined' && ownerType!=''){
		$("#ownerType").val(ownerType);
		$("#groupName").val('');
	}
	 var groupName = $.trim($("#groupName").val());
    if (groupName.length == 0 || groupName == $("#groupName").attr("title")) {
        groupName = "";
    }
    $("#pageNo").val(1);
    $("#groupName").val(groupName);
    $("#mainForm").submit();
};
smate.groupmain.init = function(){
	var code = $("#code").val();
	var ownerType = $("#ownerType").val();
	if(typeof code !=='undefined'&& code!=''){
		$('a').removeClass('leftnav-hover');
		var a = $('#category_'+code);
		$('#category_'+code).addClass('leftnav-hover');
		return;
	}
	if(typeof ownerType !=='undefined' && ownerType!=''){
		$('a').removeClass('leftnav-hover');
		$('#joinType_'+ownerType).addClass('leftnav-hover');
	}
};
/**
 * 给待加入群组的管理员发送信息
 */
smate.groupmain.sendMsg = function(des3GroupId,groupNodeId,groupName){
	$("#req_but").attr(
			'alt',
			snsctx + "/message/inside/showGroupMsgBox?role=admin&des3GroupId="
					+ encodeURIComponent(des3GroupId) + "&groupNodeId="
					+ encodeURIComponent(groupNodeId)+"&groupName="+groupName
					+ "&TB_iframe=true&height=210&width=570");
	$("#req_but").click();
};




/**
 * 取消加入群组
 */
smate.groupmain.cancel = function(des3GroupId, groupNodeId){
    jConfirm(smate.groupmain.cancelApply, smate.groupmain.prompt, function(sure){
        if (sure) {
            $.proceeding.show();
            $.ajax({
                url: snsctx + '/group/ajaxGroupCancel',
                type: 'post',
                dataType: 'json',
                data: {
                    "des3GroupId": des3GroupId,
                    "groupNodeId": groupNodeId
                },
                success: function(data){
                    $.proceeding.hide();
                    $.smate.scmtips.show(data.result, data.msg);
                    setTimeout(function(){
                        window.location.reload();
                    }, 2000);
                },
                error: function(){
                    $.proceeding.hide();
                    $.smate.scmtips.show("error", smate.groupmain.opFaild);
                }
            });
        }
    });
    
};
