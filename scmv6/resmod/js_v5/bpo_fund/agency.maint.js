
var agencyMaint = agencyMaint ? agencyMaint : {};

agencyMaint.setTab = function (name, cursel, n) {
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
agencyMaint.checkAll = function (obj) {
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
agencyMaint.check = function (obj) {
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
agencyMaint.editThickBox = function (id){
  $("#thickbox_agency").attr("title","编辑资助机构");
  $("#thickbox_agency").attr("alt",ctxpath+"/fund/ajaxAgencyEdit?id="+id+"&TB_iframe=true&height=366&width=600");
  $("#thickbox_agency").click();
}
agencyMaint.editAgency=function(obj){
  var fundId=$(obj).attr("agencyId");
  agencyMaint.editThickBox(fundId);
}

agencyMaint.search = function(flag){
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
		url:ctxpath+"/fund/ajaxAgencyMaintList",
		type:"post",
		data:params,
		timeout:10000,
		success:function(data){
			if(data.ajaxSessionTimeOut=="yes"){
				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
					if (r) {
						document.location.href=ctxpath+"/fund/agency/maint";
					}
			   });
			}else{
				$("#searchResult").html(data);
				$("#loading_tip").hide();
				if(flag!=2)
				agencyMaint.latedLeftMenu();
			}
		}
	});
}
	
agencyMaint.searchByLeft = function(obj){
	var typeId = $(obj).attr("typeId");
	var parentId = $(obj).attr("parentId");
	var agencyTypeId="";
	var agencyRegionId="";
	var agencyId="";
	if(typeId=="" && parentId==""){
		$("#searchKey").val("");
		$('#searchKey').watermark({
			tipCon : search_tipcon
		});
		agencyMaint.search(0);
		return;
	}
	if(parentId==""){
		agencyTypeId=typeId;	
	}else{
		parentId = parseInt(parentId);
		if(parentId==120){
			agencyId=typeId;
		}else{
			agencyTypeId = parentId;
			agencyRegionId=typeId;
		}
	}
	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	
	var order = $("#order").val()==undefined?"":$("#order").val();
	var orderBy = $("#orderBy").val()==undefined?"":$("#orderBy").val();
	var pageSize = $("#pageSize").val()==undefined?10:$("#pageSize").val();
	var pageNo = $("#pageNo").val()==undefined?1:$("#pageNo").val();
	$("#searchResult").html("");
	$("#loading_tip").show();
	var params = {"searchKey":searchKey,"agencyId":agencyId,"agencyTypeId":agencyTypeId,"agencyRegionId":agencyRegionId,"page.order":order,"page.orderBy":orderBy,"page.pageSize":pageSize,"page.pageNo":pageNo};
	$.ajax({
		url:ctxpath+"/fund/ajaxAgencyMaintList",
		type:"post",
		data:params,
		timeout:10000,
		success:function(data){
			if(data.ajaxSessionTimeOut=="yes"){
				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
					if (r) {
						document.location.href="${ctx}/fund/agency/maint";
					}
			   });
			}else{
				$("#searchResult").html(data);
				$("#loading_tip").hide();
			}
		}
	});
}

agencyMaint.latedLeftMenu = function(){
	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	$.ajax({
		url:ctxpath+"/fund/ajaxAgencyLeft",
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

agencyMaint.add=function(){
	$("#thickbox_agency").attr("title","新增资助机构");
	$("#thickbox_agency").attr("alt",ctxpath+"/fund/ajaxAgencyAdd?TB_iframe=true&height=366&width=600");
	$("#thickbox_agency").click();
}

agencyMaint.edit=function(){
	var ids = $(".fundcheck:checked");
	if($("#checkAll").checked ||ids.size()>1){
		$.scmtips.show('warn',"一次只能修改一个资助机构，您已选择多个，请重新选择", null, 2000);
		return;
	}else if(ids.size()==0){
		$.scmtips.show('warn',"请选择资助机构", null, 2000);
		return;
	}else{
		var id = $('input:checkbox[name="fundcheck"]:checked').val();
		$("#thickbox_agency").attr("title","编辑资助机构");
		$("#thickbox_agency").attr("alt",ctxpath+"/fund/ajaxAgencyEdit?id="+id+"&TB_iframe=true&height=366&width=600");
		$("#thickbox_agency").click();
	}
}

agencyMaint.remove=function(){
	var ids = $(".fundcheck:checked");
	if(ids.size()==0){
		$.scmtips.show('warn',"您至少需要选择一个资助机构", null, 2000);
		return;
	}
	var agencyIds = "";
	$(".fundcheck").each(function(){
		if($(this).attr('checked')){
			agencyIds = agencyIds + "," + $(this).val();
		}
	});
	if(agencyIds != ""){
		agencyIds = agencyIds.substring(1, agencyIds.length);
	}
	if(agencyIds==undefined){
		$.scmtips.show('warn',select_agency, null, 2000);
		return;
	}
	jConfirm("删除该机构将影响所有该机构下资助类别数据，包括已经添加单位基金数据，您确定要删除该资助机构吗？", "提示", function(sure){
	     if(sure){
	    		$.ajax({
	    			url:ctxpath+"/fund/ajaxAgencyDel",
	    			type:"post",
	    			data:{"agencyIds":agencyIds},
	    			timeout:10000,
	    			success:function(data){
	    				if(data.ajaxSessionTimeOut=="yes"){
	    					jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
	    						if (r) {
	    							document.location.href="${ctx}/fund/agency/maint";
	    						}
	    				   });
	    				}else{
	    					$.scmtips.show('success',"操作成功!", null, 2000);
		    				setTimeout(function(){
		    					agencyMaint.search(0);
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



