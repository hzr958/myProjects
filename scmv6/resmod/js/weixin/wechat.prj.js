var wechat = wechat||{};
wechat.prj = wechat.prj || {};

wechat.prj.ajaxrefreshprjs= function(){
   var data= {"selectStatus":$("#selectStatus").val(),"selectFundingAgencie":$("#selectFundingAgencie").val(),"orderType":$("#orderType").val(),"orderRule":$("#orderRule").val(),"hasPrivatePrj":$("#psnHasPrivatePrj").val(),"flag":1}
   data.refresh =true ;
   wechat.prj.ajaxrequest(data,wechat.prj.refreshprjs);
}

wechat.prj.refreshprjs=function(data){
	$("#listdiv").html();     
	$("#listdiv").html(data);
	$("#nextId").val(0);
}

wechat.prj.ajaxuploadprjs=function(){
	 var nextId=$("#nextId").val();
	 if(nextId==null||nextId==""){
		 var nextIds =document.getElementsByName("nextIdEx");
		 if(nextIds.length>0){
			 nextId=nextIds.length;
		 }else{
			 return;
		 }
	 }
	 var data= {"selectStatus":$("#selectStatus").val(),"selectFundingAgencie":$("#selectFundingAgencie").val(),"orderType":$("#orderType").val(),"orderRule":$("#orderRule").val(),"flag":1,"nextId":$("#nextId").val(),"hasPrivatePrj":$("psnHasPrivatePrj").val()}
	 wechat.prj.ajaxrequest(data,wechat.prj.uploadprjs);
	 $("#des3NextId").val("");
}

wechat.prj.uploadprjs=function(data){
		var hasPrivatePrj = $("#psnHasPrivatePrj").val();
	    var isOthers = $("#other").val();
	    var noMoreRecordTips = "没有更多记录";
	    //判断是否有筛选条件
	    var orderType = $("#orderType").val();
	    var selectStatus = $("#selectStatus").val();
	    var selectFundingAgencie = $("#selectFundingAgencie").val();
	    
	    if(orderType == 0 && selectStatus =='' && selectFundingAgencie == '' && isOthers == "true" && hasPrivatePrj == "true"){
	    	noMoreRecordTips = "由于权限设置, 可能部分数据未显示";
	    }
	    if($("#listdiv").find(".noRecord").length==0){
			if($("#listdiv").find(".paper").length>0 && data.indexOf("response_no-result") > -1){
				$("#load_preloader").before(data);
				$(".response_no-result").text(noMoreRecordTips);
			}else{
				$("#load_preloader").before(data);
				$(".dev_nextIdEx:last").after('<div id="addload" style="width: 100%; height: 120px;"></div>');
			}
		}
	    
		if(data.indexOf("未查到项目记录")==-1){
			$("#listdiv").append(data);
		}
		if(parseInt($("#count").val())==0&&data.indexOf("未查到项目记录")!=-1){
			if(document.getElementsByName("nextIdEx").length<1){
				$("#listdiv").append(data);
			}
			var count=parseInt($("#count").val())+1;
			$("#count").val(count);
		}
		
		//如果只有一页内容，则要显示提示语
		//var totalCount = $("#prjTotalCount").val();
		var totalCount = parseInt($("#prjCount").val());
		var pageSize = parseInt($("#prePageSize").val());
		var pageNum = (totalCount/pageSize) == 0 ? (totalCount/pageSize) : Math.floor(totalCount/pageSize)+1;
		var nextId = $("#nextId").val();
		/*if(totalCount != "" && pageSize != "" && parseInt(totalCount) <= parseInt(pageSize)){*/
		if(totalCount != "" && nextId != "" && pageNum <= nextId){
		   /* if(parseInt(totalCount) != 0){
		        $("#load_preloader").before("<div class='response_no-result'>"+noMoreRecordTips+"</div>");
		    }else{
		        $(".response_no-result").text(noMoreRecordTips);
		    }*/
			if($(".wrap_com div[class='response_no-result']").size()==0){
				$("#notFindPrj").remove();
				$(".wrap_com").append("<div class='response_no-result'>"+noMoreRecordTips+"</div>");
			}
			
		    $("#addload").remove();
		}
		if(parseInt($("#count").val())==0&&data.indexOf("noRecord")!=-1){
			var count=parseInt($("#count").val())+1;
			$("#count").val(count);
		}
		$("#load_preloader").remove();
		
}

wechat.prj.ajaxrequest=function(data,callback){
	var url="";
	if($("#other").val()=='true'){
		if($("#hasLogin").val() == 1){
			url = "/prjweb/wechat/findotherprjs";
		}else{
			url = "/prjweb/outside/ajaxotherprjs";
		}
		data['psnId']=$("#psnId").val();
		if(data.refresh != undefined && data.refresh == true){
			//刷新不要带页码
		}else{
			data['nextId']=$("#nextId").val();
		}
	}else{
		url="/prjweb/wechat/findprjs";
	}
	
	$.ajax({
		url:url,
		type : "post",
		dataType : "html",
		data :data,
		async:false,
		success:function(data){
			$("#notFindPrj").remove();
			callback(data);
		},
		error:function(data){
			alert("请求数据出错!");
		}
	});
}

