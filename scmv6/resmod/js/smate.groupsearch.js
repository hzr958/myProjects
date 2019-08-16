var smate = smate ? smate : {};

smate.groupsearch = smate.groupsearch ? smate.groupsearch : {};
smate.groupsearch.init = function(){
	$("#groupName").bind("focus",function(){
		if($.trim(this.value) == $.trim(this.title)){
			this.value = "";
		}
		$(this).css("color","#000000");
	}).bind("blur",function(){
		if($.trim(this.value) == ""){
			this.value = this.title;
			$(this).css("color","#999999");
		}
	}).css("color","#999999");
	var groupName = $.trim($("#groupName").val());
	var groupTitle = $("#groupName").attr("title");
	if(groupName == ""){
		$("#groupName").val(groupTitle);
	}else if(this.value != $.trim(this.title)){
		$("#groupName").css("color","#000000");
	}
	
	$("input.thickbox").thickbox();
};
smate.groupsearch.doSearch = function(){
    var groupName = $.trim($("#groupName").val());
    if (groupName.length == 0 || groupName == $("#groupName").attr("title")) {
        groupName = "";
    }
    
    $("#pageNo").val(1);
    $("#groupName").val(groupName);
    $("#mainForm").submit();
};
smate.groupsearch.forward = function(code){
    if (code == $("#code").val()) {
        return;
    }
    $("#code").val(code);
    $("#groupName").val("");
    $("#pageNo").val(1);
    $("#mainForm").submit();
};
/**
 * 搜索课程教学群组
 */
smate.groupsearch.course = function(){
};

/**
 * 搜索科研项目群组
 */
smate.groupsearch.project = function(){
};

/**
 * 全选
 * @param cbAll
 */
smate.groupsearch.selectAll = function(cbAll){
    //alert($(cbAll).attr("checked"));
    var checkboxs = $(":checkbox[name=groupId_NodeId]");
    if ($(cbAll).attr("checked")) {
        checkboxs.attr("checked", true);
    } else {
        checkboxs.attr("checked", false);
    }
};


smate.groupsearch.inputGroupCode = function(str){
	$("#inputGroupCode").attr("alt",snsctx+"/group/ajaxJoinGroupByinputCode?groupId="+str+"&TB_iframe=true&height=200&width=450");
	$("#inputGroupCode").click();
};

/**
 * 请求加入群组
 */
smate.groupsearch.toInvite = function(obj,isDirect){
    var checkboxs = $("input:checked[name='groupId_NodeId']");
    if (checkboxs.length == 0) {
        $.smate.scmtips.show('warn', Group.search.inputGroup);
        return;
    }
    smate.groupsearch.goToInvite(obj,isDirect,checkboxs);
    
};

/**
 * 请求加入群组    ------------SCM-6596   用于检索页面请求加入方式改变
 */
smate.groupsearch.toInviteNew = function(obj,isDirect){
	var checkboxs = $(obj).parent().parent().parent().find("input[name='groupId_NodeId']");
	checkboxs.each(function(i, cb){ 
		$(cb).attr("checked","checked");
	});
	smate.groupsearch.goToInvite(obj,isDirect,checkboxs);
};
/**
 * 
 */
smate.groupsearch.goToInvite = function(obj,isDirect,checkboxs){
    var groupPsnList = [];
    var isMemberGroupNameList = [];
    checkboxs.each(function(i, cb){
        var value = $.trim($(cb).val());
        if (/^([\d]+)_([\d]+)_(.*)$/.test(value)) {
            var $1 = RegExp.$1;
            var $2 = RegExp.$2;
            var $3 = RegExp.$3;
            groupPsnList.push({
                groupId: $1,
                groupNodeId: $2,
                groupCode: $3
            });
        }
    });
    $(obj).css("visibility","hidden");
    $.ajax({
        url: snsctx + '/group/ajaxSearchMyGroup',
        type: 'post',
        dataType: 'json',
        success: function(data){
        	if(data.ajaxSessionTimeOut=='yes'){
        		$.smate.scmtips.show("error",'系统超时,请重新登录');
				/*jConfirm(msg_common_timeout, msg_common_reminder, function(r) {
					if (r) {
						top.location.reload(true);
						return;
					}
				});*/
				return;
		    }
            for (var i = 0; i < data.length; i++) {
                for (var j = 0; j < groupPsnList.length; j++) {
                    var jGroup = groupPsnList[j];
                    if (data[i] == jGroup.groupId) {
                        isMemberGroupNameList.push($("#groupName_" + jGroup.groupId).val());
		                    }
		             }
		        }
            if (isMemberGroupNameList.length > 0) {
                $.smate.scmtips.show('warn', isMemberGroupNameList.join(",") + Group.search.audit);
					 $(obj).css("visibility","visible");
                return;
            	}
            
            var groupIdNodeIdStr = JSON.stringify(groupPsnList);
            var groupCode	= groupPsnList[0].groupCode;
            var groupId = groupPsnList[0].groupId;
            
            //判断是否是教学群组并存在验证码 的群组 如果是 弹出输入框 TSZ_2014-03-25_SCM-4943
         if(groupCode!=null&&groupCode!=""&&typeof isDirect =='undefined'){
        	 	//有验证码  已弹出框的方式处理  TSZ_2014-03-25_SCM-4943
        	 smate.groupsearch.inputGroupCode(groupId); 
        	 	$(obj).css("visibility","visible");
        		
          }else{
            $.ajax({
                url: snsctx + '/group/ajaxGroupNode',
                type: 'post',
                dataType: 'json',
                data: {
                    "groupIdNodeIdStr": groupIdNodeIdStr
                },
                success: function(data){
                    $(obj).css("visibility","visible");
                    $.smate.scmtips.show(data.result, data.msg);
                    setTimeout(function(){
                        $('#mainForm').submit();
                    }, 2000);
                },
                error: function(){
                    $(obj).css("visibility","visible");
                }
            });
           }
        },
        error: function(){
            $(obj).css("visibility","visible");
        }
    });
};
