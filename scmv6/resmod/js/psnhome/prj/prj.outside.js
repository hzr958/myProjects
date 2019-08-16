var project = project?project:{};
//改变url

//项目主页
project.ajaxOutSideShow =function(){
	project.changeUrl("prj");
	$.ajax({
		url:'/prjweb/outside/ajaxshow',
		type : 'post',
		dataType:'html',
		success : function(data) {
			$(".dev_psnhome_back").remove();
			if($(".container__horiz").length>0){
				$(".container__horiz").replaceWith(data);	
			}else{
				$(".dev_select_tab").replaceWith(data);	//container__horiz带有样式，影响力用dev_select_tab这个
			}
		}
	})
}
//项目列表
project.ajaxOutSidePrjList = function(des3CurrentId){
	$prjlist=window.Mainlist({
		name:"prjlist",
		listurl: "/prjweb/outside/ajaxprjlist",
		listdata: {'des3CurrentId':des3CurrentId},
		listcallback: function(){},
		statsurl:"/prjweb/show/ajaxprjlistcallback",
	})
}
//机构列表
project.ajaxOutSideRecommendList =function (des3CurrentId){
	$.ajax({
		url: "/prjweb/show/ajaxagencyname",
		type: "post",
		data:{'des3CurrentId':des3CurrentId},
		dataType: "json",
		success: function(data){
					if(data != null){
						var agencyNamesHtml = "";
						for(var i=0; i<data.length; i++){
							agencyNamesHtml += '<li class=\"filter-value__item js_filtervalue\" filter-value=\"' +data[i].agencyName+ '\"><div class=\"input-custom-style\"> <input type=\"checkbox\"> <i class=\"material-icons custom-style\"></i> </div> <div class=\"filter-value__option js_filteroption\" title=\"'+data[i].agencyName+'\">' + data[i].agencyName +'</div>  <div class=\"filter-value__stats js_filterstats\">(0)</div> <i class=\"material-icons filter-value__cancel js_filtercancel\">close</i> </li>';
						}
						agencyNamesHtml += '<li class=\"filter-value__item js_filtervalue\" filter-value=\"' +'other_agency_name'+ '\"><div class=\"input-custom-style\"> <input type=\"checkbox\"> <i class=\"material-icons custom-style\"></i> </div> <div class=\"filter-value__option js_filteroption\" title=\"'+homepage.otherAgency+'\">' + homepage.otherAgency +'</div>  <div class=\"filter-value__stats js_filterstats\">(0)</div> <i class=\"material-icons filter-value__cancel js_filtercancel\">close</i> </li>';
						$("#agencyNames").html(agencyNamesHtml);
					}
					/*project.ajaxPrjList(des3CurrentId);*/
					project.ajaxOutSidePrjList(des3CurrentId); 
			
		},
		error: function(){
			
		}
	});
}
//项目数和项目金额数
project.outsidePrjNumberAmount= function(des3CurrentId){
	$.ajax({
		url:'/prjweb/remind/ajaxamount',
		type:'post',
		data:{'des3CurrentId':des3CurrentId},
		dataType : "json",
		success : function(data) {
			if(data!=null){
				var numbers=data.amounts;
				if(numbers!=0 && data.locale=='zh'){
					numbers=numbers/10000;
				}else if(numbers!=0 && data.locale=='en'){
					numbers=numbers/1000;
				}
				$("#prjNumbers").text(data.prj);
				$("#prjTotalAmounts").text(numbers +homepage.unit);
			}
	    }	
		
	})
}

//站外项目合作者列表
project.cooperator = function() {
	$.ajax({
		url : '/psnweb/outside/ajaxprjcooperator',
		type : 'post',
		dataType : 'html',
		data : {"des3CurrentId":$("#outsideDes3PsnId").val()},
		success : function(data) {
			$("#prjcooperation").append(data);
		}
	});
};
//查看全部项目合作者
project.outsideCooperatorAll = function() {
  $('#dev_pubcooperator_isall').val("all");
  showDialog("dev_lookall_pubcooperator_back");
  window.Mainlist({
    name : "pubcooperator",
    listurl : "/psnweb/outside/ajaxprjcooperatorAll",
    listdata : {
      "des3CurrentId" : $("#outsideDes3PsnId").val(),
      "isAll" : "1"
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
};
project.changeUrl = function(targetModule) {
	var json = {};
	var oldUrl = window.location.href;
	var index = oldUrl.lastIndexOf("module");
	var newUrl = window.location.href;
	if (targetModule != undefined && targetModule != "") {
		if (index < 0) {
			if(oldUrl.lastIndexOf("?")>0){
				newUrl = oldUrl + "&module=" + targetModule;
			}else{
				newUrl = oldUrl + "?module=" + targetModule;
			}
		} else {
			newUrl = oldUrl.substring(0, index) + "module=" + targetModule;
		}
	}
	window.history.replaceState(json, "", newUrl);
}

/**
 * 项目标题进入详情页面 -- 已废弃，改用a标签href打开
 * @author 	houchuanjie
 * @date	2017-7-11
 */
project.enterDetail = function (des3Id){
	window.open('/prjweb/project/detailsshow?des3PrjId='+des3Id+'');
}
//项目全文下载
project.download =function(fdesId){
	
	location.href='/scmwebsns/archiveFiles/downLoadNoVer.action?fdesId='+fdesId+'&nodeId=1';
}