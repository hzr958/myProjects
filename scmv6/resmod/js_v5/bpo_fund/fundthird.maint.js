
var fundThirdMaint = fundThirdMaint ? fundThirdMaint : {};

fundThirdMaint.setTab = function (name, cursel, n) {
	for (i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

/**
 * 全选
 * 
 * @param obj
 * @return
 */
fundThirdMaint.checkAll = function (obj) {
	$(":checkbox[name='fundcheck']").each(function() {
		$(this).attr("checked", obj.checked);
	});
};
/**
 * 成果单选
 * 
 * @param obj
 * @return
 */
fundThirdMaint.check = function (obj) {
	var flag = true;
	if (obj.checked) {
		var cks = $(":checkbox[name='fundcheck']");
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
	$(":checkbox[name='checkAll']").attr("checked", flag);
};

fundThirdMaint.search = function(flag){
	//分页参数
	var order = $("#order").val()==undefined?"":$("#order").val();
	var orderBy = $("#orderBy").val()==undefined?"":$("#orderBy").val();
	var pageSize = $("#pageSize").val()==undefined?10:$("#pageSize").val();
	var pageNo = $("#pageNo").val()==undefined?1:$("#pageNo").val();

	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	$("#searchFundResult").html("");
	$("#loading_tip").show();
	var params = {"searchKey":searchKey,"page.order":order,"page.orderBy":orderBy,"page.pageSize":pageSize,"page.pageNo":pageNo};
	$.ajax({
		url:ctxpath+"/fund/ajaxFundThirdMaintList",
		type:"post",
		data:params,
		timeout:10000,
		success:function(data){
			if(data.ajaxSessionTimeOut=="yes"){
				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
					if (r) {
						document.location.href=ctxpath+"/fund/third/maint";
					}
			   });
			}else{
				$("#searchFundResult").html(data);
				$("#loading_tip").hide();
				if(flag!=2)
				fundThirdMaint.latedLeftMenu();
			}
		}
	});
}
	
fundThirdMaint.searchByLeft = function(obj){
	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	
	var order = $("#order").val()==undefined?"":$("#order").val();
	var orderBy = $("#orderBy").val()==undefined?"":$("#orderBy").val();
	var pageSize = $("#pageSize").val()==undefined?10:$("#pageSize").val();
	var pageNo = $("#pageNo").val()==undefined?1:$("#pageNo").val();
	$("#searchFundResult").html("");
	$("#loading_tip").show();
	var params = {"searchKey":searchKey,"page.order":order,"page.orderBy":orderBy,"page.pageSize":pageSize,"page.pageNo":pageNo};
	$.ajax({
		url:ctxpath+"/fund/ajaxFundThirdMaintList",
		type:"post",
		data:params,
		timeout:10000,
		success:function(data){
			if(data.ajaxSessionTimeOut=="yes"){
				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
					if (r) {
						document.location.href="${ctx}/fund/third/maint";
					}
			   });
			}else{
				$("#searchFundResult").html(data);
				$("#loading_tip").hide();
			}
		}
	});
}

fundThirdMaint.latedLeftMenu = function(){
	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	$.ajax({
		url:ctxpath+"/fund/ajaxFundThirdLeft",
		type:"post",
		data:{"searchKey":searchKey},
		dataType:"html",
		timeout:10000,
		success:function(data){
		 if(data){
			$("#left-wrap").html(data);	
		  }
		}
	});
}

fundThirdMaint.viewFundThird=function(id){
  $("#thickbox_fundThird").attr("title","查看基金机会详情");
  $("#thickbox_fundThird").attr("alt",ctxpath+"/fund/ajaxFundDetails?id="+id+"&TB_iframe=true&height=630&width=980");
  $("#thickbox_fundThird").click();  
}


fundThirdMaint.checkFund=function(){
  var ids = $(".fundcheck:checked");
  if($("#checkAll").checked ||ids.size()>1){
    $.scmtips.show('warn',"一次只能审核一个基金机会，您已选择多个，请重新选择", null, 2000);
    return;
  }else if(ids.size()==0){
    $.scmtips.show('warn',"请选择基金机会", null, 2000);
    return;
  }else{
    var fundIds = "";
    $(".fundcheck").each(function(){
      if($(this).attr('checked')){
        fundIds = fundIds + "," + $(this).val();
      }
    });
    if(fundIds != ""){
      fundIds = fundIds.substring(1, fundIds.length);
    }
    fundThirdMaint.enterCheck(fundIds);
  }
}
fundThirdMaint.enterCheck=function(fundIds){
  $("#thickbox_fundThird").attr("title","审核资助类别");
  $("#thickbox_fundThird").attr("alt",ctxpath+"/fund/ajaxFundCheck?ids="+fundIds+"&TB_iframe=true&height=630&width=980");
  $("#thickbox_fundThird").click();
}

fundThirdMaint.rejectFund=function(){
  var ids = $(".fundcheck:checked");
  if(ids.size()==0){
    $.scmtips.show('warn',"您至少需要选择一个基金机会", null, 2000);
    return;
  }else{
    var fundIds = "";
    $(".fundcheck").each(function(){
      if($(this).attr('checked')){
        fundIds = fundIds + "," + $(this).val();
      }
    });
    if(fundIds != ""){
      fundIds = fundIds.substring(1, fundIds.length);
    }
    fundThirdMaint.rejectOpt(fundIds,"list");
  }
}
  
fundThirdMaint.rejectOpt=function(fundIds,type){
  var tip="您确定要删除选择的基金机会吗？";
  if(type != "list"){
    tip="您确定要删除该基金机会吗？";
  }
  jConfirm(tip, "提示", function(sure){
    if(sure){
       $.ajax({
         url:ctxpath+"/fund/ajaxFundDel",
         type:"post",
         data:{"ids":fundIds},
         timeout:10000,
         success:function(data){
           if(data.ajaxSessionTimeOut=="yes"){
             jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
               if (r) {
                 document.location.href=ctxpath+"/fund/third/maint";
               }
              });
           }else{
             $.scmtips.show('success',"操作成功!", null, 2000);
             setTimeout(function(){
               parent.$.Thickbox.closeWin();
               parent.loadOtherPage(parent.$("#pageNo").val());
             },1000);
           }
         },error:function(){
           $.scmtips.show('error',"操作失败", null, 2000);
           setTimeout(function(){
             parent.$.Thickbox.closeWin();
           },1000);
         }
       });
    }
});
}
 