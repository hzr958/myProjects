
var categoryAudit = categoryAudit ? categoryAudit : {};

categoryAudit.search = function(flag){
	//分页参数
	var order = $("#order").val()==undefined?"":$("#order").val();
	var orderBy = $("#orderBy").val()==undefined?"":$("#orderBy").val();
	var pageSize = $("#pageSize").val()==undefined?10:$("#pageSize").val();
	var pageNo = $("#pageNo").val()==undefined?1:$("#pageNo").val();

	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	if(flag==1){
		if(searchKey==""){
			$.scmtips.show('warn',lable_check_input, null, 2000);
			return;
		}
	}
	$("#searchResult").html("");
	$("#loading_tip").show();
	var params = {"searchKey":searchKey,"page.order":order,"page.orderBy":orderBy,"page.pageSize":pageSize,"page.pageNo":pageNo};
	$.ajax({
		url:ctxpath+"/fund/ajaxCategoryAudittList",
		type:"post",
		data:params,
		timeout:10000,
		success:function(data){
			if(data.ajaxSessionTimeOut=="yes"){
				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
					if (r) {
						document.location.href=ctxpath+"/fund/category/maint";
					}
			   });
			}else{
				$("#searchResult").html(data);
				$("#loading_tip").hide();
			}
		}
	});
}

categoryAudit.audit=function(){
	var id = $('input:radio[name="fundcheck"]:checked').val();
	var agencyId = $('input:radio[name="fundcheck"]:checked').attr("agencyId");
	agency_name = $('input:radio[name="fundcheck"]:checked').parent().parent().find(".agency_name").html();
	if(agencyId==undefined){
		$.scmtips.show('warn',"请选择资助类别", null, 2000);
		return;
	}
	$.ajax({
		url:ctxpath+"/fund/ajaxCheckInsFundAgency",
		type:"post",
		data:{"id":agencyId},
		timeout:10000,
		success:function(data){
			if(data.ajaxSessionTimeOut=="yes"){
				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
					if (r) {
						document.location.href=ctxpath+"/fund/category/maint";
					}
			   });
			}else{
				if(data==""){
					jConfirm("该资助类别中的资助机构尚未审核通过，请先审核批准该类别中的资助机构", "提示", function(sure){
					     if(sure){
					    	 location.href=ctxpath+"/fund/agency/audit?id="+agencyId+"&menuId=22401";
					     }else{
					    	$("#thickbox_Category").attr("title","批准/拒绝资助类别");
							$("#thickbox_Category").attr("alt",ctxpath+"/fund/ajaxAuditCategory?id="+id+"&TB_iframe=true&height=600&width=980");
							$("#thickbox_Category").click();
					     }
					});
				}else if(data!="false"){
					$("#thickbox_Category").attr("title","批准/拒绝资助类别");
					$("#thickbox_Category").attr("alt",ctxpath+"/fund/ajaxAuditCategory?id="+id+"&TB_iframe=true&height=600&width=980");
					$("#thickbox_Category").click();
				}
			}
		},error:function(){
			$.scmtips.show('warn',"操作出错", null, 2000);	
		}
	});
}



