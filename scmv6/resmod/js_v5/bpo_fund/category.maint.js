
var categoryMaint = categoryMaint ? categoryMaint : {};

categoryMaint.setTab = function (name, cursel, n) {
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
categoryMaint.checkAll = function (obj) {
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
categoryMaint.check = function (obj) {
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

categoryMaint.search = function(flag){
	//分页参数
	var order = $("#order").val()==undefined?"":$("#order").val();
	var orderBy = $("#orderBy").val()==undefined?"":$("#orderBy").val();
	var pageSize = $("#pageSize").val()==undefined?10:$("#pageSize").val();
	var pageNo = $("#pageNo").val()==undefined?1:$("#pageNo").val();

	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	$("#searchResult").html("");
	$("#loading_tip").show();
	var params = {"searchKey":searchKey,"page.order":order,"page.orderBy":orderBy,"page.pageSize":pageSize,"page.pageNo":pageNo};
	$.ajax({
		url:ctxpath+"/fund/ajaxCategoryMaintList",
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
				if(flag!=2)
				categoryMaint.latedLeftMenu();
			}
		}
	});
}
	
categoryMaint.searchByLeft = function(obj){
	var typeId = $(obj).attr("typeId");
	var parentId = $(obj).attr("parentId");
	var agencyTypeId="";
	var agencyRegionId="";
	var agencyId="";
	var language="";
	var newMonth="";
	if(typeId=="" && parentId==""){
		$("#searchKey").val("");
		$('#searchKey').watermark({
			tipCon : search_tipcon
		});
		categoryMaint.search(0);
		return;
	}
	if(parentId==""){
			typeId = parseInt(typeId);
			switch (typeId) {
			case 170:// 截止日期
				newMonth=3;	
				break;
			case 180:// 语言类别
				break;
			default:
				agencyTypeId=typeId;
				break;
			}	
	}else{
		parentId = parseInt(parentId);
		switch (parentId) {
		case 170:// 截止日期
			newMonth=typeId;
			break;
		case 180:// 语言类别
			language=typeId;
			break;
		case 120:// 国家级
			agencyId=typeId;
			break;
		default:
			agencyRegionId=typeId;
			break;
		}
	}
	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	//分页参数
	var order = $("#order").val()==undefined?"":$("#order").val();
	var orderBy = $("#orderBy").val()==undefined?"":$("#orderBy").val();
	var pageSize = $("#pageSize").val()==undefined?10:$("#pageSize").val();
	var pageNo = $("#pageNo").val()==undefined?1:$("#pageNo").val();
	$("#searchResult").html("");
	$("#loading_tip").show();
	var params = {"searchKey":searchKey,"agencyId":agencyId,"agencyTypeId":agencyTypeId,"agencyRegionId":agencyRegionId,"language":language,"newMonth":newMonth,"page.order":order,"page.orderBy":orderBy,"page.pageSize":pageSize,"page.pageNo":pageNo};
	$.ajax({
		url:ctxpath+"/fund/ajaxCategoryMaintList",
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
categoryMaint.editThickBox = function (id){
  $("#thickbox_category").attr("title","编辑资助类别");
  $("#thickbox_category").attr("alt",ctxpath+"/fund/ajaxCategoryEdit?id="+id+"&TB_iframe=true&height=630&width=980");
  $("#thickbox_category").click();  
}
categoryMaint.editCategory=function(obj){
  var fundId=$(obj).attr("fundId");
  categoryMaint.editThickBox(fundId);
}
categoryMaint.latedLeftMenu = function(){
	var searchKey = $.trim($("#searchKey").val());
	searchKey = search_tipcon==searchKey?"":searchKey;
	$.ajax({
		url:ctxpath+"/fund/ajaxCategoryLeft",
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

categoryMaint.add=function(){
	$("#thickbox_category").attr("title","新增资助类别");
	$("#thickbox_category").attr("alt",ctxpath+"/fund/ajaxCategoryAdd?TB_iframe=true&height=630&width=980");
	$("#thickbox_category").click();
}

categoryMaint.edit=function(){
	var ids = $(".fundcheck:checked");
	if($("#checkAll").checked ||ids.size()>1){
		$.scmtips.show('warn',"一次只能修改一个资助类别，您已选择多个，请重新选择", null, 2000);
		return;
	}else if(ids.size()==0){
		$.scmtips.show('warn',"请选择资助类别", null, 2000);
		return;
	}else{
		var id = $('input:checkbox[name="fundcheck"]:checked').val();
		$("#thickbox_category").attr("title","编辑资助类别");
		$("#thickbox_category").attr("alt",ctxpath+"/fund/ajaxCategoryEdit?id="+id+"&TB_iframe=true&height=630&width=980");
		$("#thickbox_category").click();
	}
}

categoryMaint.remove=function(){
	var ids = $(".fundcheck:checked");
	if(ids.size()==0){
		$.scmtips.show('warn',"您至少需要选择一个资助类别", null, 2000);
		return;
	}
	var categoryIds = "";
	$(".fundcheck").each(function(){
		if($(this).attr('checked')){
			categoryIds = categoryIds + "," + $(this).val();
		}
	});
	if(categoryIds != ""){
		categoryIds = categoryIds.substring(1, categoryIds.length);
	}
	if(categoryIds==undefined){
		$.scmtips.show('warn',select_agency, null, 2000);
		return;
	}
	jConfirm("您确定要删除选择的资助类别吗？", "提示", function(sure){
	     if(sure){
	    	 var categoryList=new Array;
	    		$(":checkbox[name='fundcheck']").each(function() {
	    			if($(this).attr("checked")){
	    				var param={"agencyId":$(this).attr("agencyId"),"categoryId":$(this).attr("categoryId")};
	    				categoryList.push(param);
	    			}
	    		});
	    		categoryList=JSON.stringify(categoryList);
	    		$.ajax({
	    			url:ctxpath+"/fund/ajaxCategoryDel",
	    			type:"post",
	    			data:{"categoryIds":categoryIds},
	    			timeout:10000,
	    			success:function(data){
	    				if(data.ajaxSessionTimeOut=="yes"){
	    					jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r){
	    						if (r) {
	    							document.location.href=ctxpath+"/fund/category/maint";
	    						}
	    				   });
	    				}else{
	    					$.scmtips.show('success',"操作成功!", null, 2000);
		    				setTimeout(function(){
		    					categoryMaint.search(0);
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



