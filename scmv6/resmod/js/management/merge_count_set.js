/**
 * 账号设置-合并帐号设置页面的JS事件.
 */
var mergeCount = mergeCount ? mergeCount : {};
/**
 * 合并帐号.
 */
mergeCount.addMerge=function(){
	var mergeCount=$("#mergeCount").val();
	var deleteCount=$("#deleteCount").val(); 
	if($.trim(mergeCount)==''){
		$.scmtips.show('warn', "保留帐号不能为空");
		return false;
	}
	if(mergeCount.length>50){
		$.scmtips.show('error', "保留帐号不存在");
		return false;
	}
	if($.trim(deleteCount)==''){
		$.scmtips.show('warn', "删除帐号不能为空");
		return false;
	}
	if(deleteCount.length>50){
		$.scmtips.show('error', "删除帐号不存在");
		return false;
	}
	//修正了提示内容的显示逻辑_MJG_SCM-5217.
	var showCount=mergeCount;
	var index_later=mergeCount.indexOf('@');
	if(index_later>15){
		showCount=mergeCount.substr(0,3)+'**'+mergeCount.substr(index_later-2,mergeCount.length);
	}
	var alert_confirm_info="您确认将"+' '+deleteCount+' '+"帐号合并至 "+' '+showCount+' '+"帐号吗？";
	jConfirm(alert_confirm_info, att_person_util.prompt, function(sure) {
		if (!sure)
			return;
		$.proceeding.show();
		$.ajax( {
				url : '/scmmanagement/setting/ajaxMergeCount',
				type : 'post',
				dataType:'json',
				data : {"mergeCount":mergeCount,"deleteCount":deleteCount},
				success : function(data) {
					if(data.ajaxSessionTimeOut=='yes'){
						// 超时  涉及到定位问题 
						jConfirm(i18n_timeout,i18n_tipTitle, function(r) {
							if(r) {
								//注意获取的是要父级窗口的地址
								var url = parent.location.href;
								url = url.replace('#', '');
								parent.location.href = url;
							}else{
								parent.$.Thickbox.closeWin();
							}
						});
					
					}else{
						$.proceeding.hide();
						if(data.result=='success'){
							$.scmtips.show('success',data.msg);
							$("#mergeCount").attr("value",'');
							$("#deleteCount").attr("value",'');
						}else if (data.result == 'errMergeCount') {
							$.scmtips.show('error', data.msg);
							$.proceeding.hide();
						}else if (data.result == 'errpwd') {
							$.scmtips.show('error', data.msg);
							$.proceeding.hide();
						}
						else {
							$.scmtips.show('error', errormsg);
							$.proceeding.hide();
						
						
						
						}
					}
				}
			});
	});
}

