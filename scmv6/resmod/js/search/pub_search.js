var Pubsearch = Pubsearch ? Pubsearch : {}
var recommendReq;
var collectReq;
//推荐显示左边查询条件
Pubsearch.ajaxLeftshow = function (){
	$.ajax({
		url : "/pubweb/pubconditions/ajaxLeftshow",
		type : "post",
		dataType : "html",
		data : "",
		success : function(data){
			$("#left_condition").html(data);
			Pubsearch.ajaxLondPubList();
		}, 
	});
}
//推荐左边选择方法
Pubsearch.addCondition = function(obj,typeclass){
	if(typeclass == "type_area"){
/*		if(!$("#area_ul").find("li").hasClass(".item_list-align_slected")){
			$("#searchArea").val("");
		}*/
		if($("#area_ul >.item_list-align_slected").size() == 0){
			$("#searchArea").val("");
		}
		if($("#searchArea").val() == ""){
			$("#searchArea").val($(obj).attr("value"));
		}else{
			var areaList = $("#searchArea").val().split(',');
			var index = $.inArray($(obj).attr("value"), areaList);
			if(index < 0){
				$("#searchArea").val($("#searchArea").val()+','+$(obj).attr("value"));
			}
		}
	}
	if(typeclass == "type_key"){
		if($("#key_ul >.item_list-align_slected").size() == 0){
			$("#searchPsnKey").val("");
		}
		Pubsearch.searchPsnKey = Pubsearch.addListElement(Pubsearch.searchPsnKey,$(obj).attr("value"));
	}
	if(typeclass == "type_time"){
		$("."+typeclass).closest(".item_list-align").each(function(){
		    $(this).removeClass("item_list-align_slected");
		});
		$("."+typeclass).each(function(){
		    $(this).attr('onclick','Pubsearch.addCondition(this,\''+typeclass+'\')');
		});
		$("#searchPubYear").val($(obj).attr("value"));
	}
	if(typeclass == "type_pub"){
		if($("#searchPubType").val() == ""){
			$("#searchPubType").val($(obj).attr("value"));
		}else{
			var pubTypeList = $("#searchPubType").val().split(',');
			var index = $.inArray($(obj).attr("value"), pubTypeList);
			if(index < 0){
				$("#searchPubType").val($("#searchPubType").val()+','+$(obj).attr("value"));
			}
		}
	}
	$(obj).closest(".item_list-align").addClass("item_list-align_slected");
	$(obj).attr('onclick','Pubsearch.delCondition(this,\''+typeclass+'\')');
    $(obj).next().remove();
	Pubsearch.ajaxLondPubList();
}
//推荐左边取消选择方法
Pubsearch.delCondition = function(obj,typeclass){
	if(typeclass == "type_area"){
		var deleteHtml = '<i class="material-icons filter-value__cancel delete_area" onclick="deleteScienArea(this)" >close</i>'
		var areaStr = $("#searchArea").val();
		var areaList = areaStr.split(',');
		if($.inArray($(obj).attr("value"), areaList)>=0){
			areaList.splice($.inArray($(obj).attr("value"),areaList),1);
			$("#searchArea").val(areaList.join(','));
		}
		$(obj).attr('onclick','Pubsearch.addCondition(this,"type_area")');
		$(obj).closest(".item_list-align").append(deleteHtml);
		Pubsearch.addHover($(obj).closest(".item_list-align"),deleteHtml);
	}
	if(typeclass == "type_key"){
		var deleteHtml = '<i class="material-icons filter-value__cancel delete_area" onclick="deleteKeyWord(this)">close</i>'
		$('input[name="searchPsnKey"]').each(function(){
			if($(obj).attr("value") == $(this).val()){
				$(this).remove();
			}
		});
		Pubsearch.searchPsnKey = Pubsearch.deleteListElement(Pubsearch.searchPsnKey,$(obj).attr("value"));
/*		var psnKeyStr = $("#searchPsnKey").val();
		var psnKeyList = psnKeyStr.split(',');
		if($.inArray($(obj).attr("value"), psnKeyList)>=0){
			psnKeyList.splice($.inArray($(obj).attr("value"),psnKeyList),1);
			$("#searchPsnKey").val(psnKeyList.join(','));
		}*/
		$(obj).attr('onclick','Pubsearch.addCondition(this,"type_key")');
		$(obj).closest(".item_list-align").append(deleteHtml);
		Pubsearch.addHover($(obj).closest(".item_list-align"),deleteHtml);
	}
	if(typeclass == "type_time"){
		$("#searchPubYear").val("");
		$(obj).attr('onclick','Pubsearch.addCondition(this,"type_time")');
	}
	if(typeclass == "type_pub"){
		var pubTypeStr = $("#searchPubType").val();
		var pubTypeList = pubTypeStr.split(',');
		if($.inArray($(obj).attr("value"), pubTypeList)>=0){
			pubTypeList.splice($.inArray($(obj).attr("value"),pubTypeList),1);
			$("#searchPubType").val(pubTypeList.join(','));
		}
		$(obj).attr('onclick','Pubsearch.addCondition(this,"type_pub")');
	}
	$(obj).closest(".item_list-align").removeClass("item_list-align_slected");
	/*$(obj).closest(".item_list-align").remove(".filter-value__cancel");*/
	/*$(obj).querySelector(".filter-value__cancel").style.opacity="1";*/
	Pubsearch.ajaxLondPubList();
}


Pubsearch.ajaxLondPubList = function() {
	var defultArea = $("#defultArea").val();
	var defultKey = Pubsearch.defultKeyList;
	var defultPubYear = $("#defultPubYear").val();
	var defultPubType = $("#defultPubType").val();
	var searchArea = $("#searchArea").val();
	var searchPsnKey = Pubsearch.searchPsnKey;
	var searchPubYear = $("#searchPubYear").val();
	var searchPubType = $("#searchPubType").val();

	if(searchArea == ""){
		searchArea = defultArea;
		$("#searchArea").val(searchArea);
	}
	if(!searchPsnKey){
		searchPsnKey="";
	}
	window.Mainlist({
		name: "pub_list",
		listurl: "/pubweb/pub/ajaxrecommend",
		listdata: {
			"defultKeyJson" : defultKey,
			"searchArea" : searchArea,
			"searchPsnKey" : searchPsnKey,
			"searchPubYear" : searchPubYear,
			"searchPubType" : searchPubType,
			},
		method: "scroll",
		listcallback: function(xhr) {  
			$(".thickbox").thickbox();
			var fullTextPubs = $(".dev_fulltext-pubs:last").val(); 
			Pubsearch.ajaxrgetfulltext(fullTextPubs);
		}
	});
}

//selectStr：查询的的字符串如searchPsnKey的值，selectName：选择的条件的class如type_area
Pubsearch.ajaxSelectConditions = function(selectStr,selectName){	
	var selectList = selectStr.split(',');
	$("."+selectName).each(function(){
		for(var item in selectList){
		      if($(this).attr("value") == selectList[item]){
		    		$(this).addClass("slectcur");
		    		$(this).attr('onclick','Pubsearch.delCondition(this,"'+selectName+'")');
		      }
		}
	});
}

//基准库成果详情赞操作
Pubsearch.pdwhAward = function(des3PubId,dbid,obj) {
	var isAward = $(obj).attr("isAward");
	var count = Number($(obj).find('.dev_pub_award:first').text().replace(/[\D]/ig,""));
	$.ajax({
		url : "/pubweb/details/ajaxpdwhaward",
		type : "post",
		dataType : "json",
		data : {"des3PubId":des3PubId,"isAward":isAward ,"dbid":dbid},
		success : function(data) {
			BaseUtils.ajaxTimeOut(data, function(){
				if (data && data.result == "success") {
					if (isAward == 1) {//取消赞
						$(obj).attr("isAward",0);
						count -= 1;
						if (count == 0) {
							$(obj).find('.dev_pub_award:first').text(Pubsearch.like);
						} else {
							$(obj).find('.dev_pub_award:first').text(Pubsearch.like+"("+count+")");
						}
						$(obj).find("i").removeClass("icon-praise-award").addClass("icon-praise");
					} else {//赞
						$(obj).attr("isAward",1);
						count += 1;
						$(obj).find('.dev_pub_award:first').text(Pubsearch.unlike+"("+count+")");
						$(obj).find("i").removeClass("icon-praise").addClass("icon-praise-award");

					}
				}
			});
		}
	});
};
//超时处理
Pubsearch.ajaxTimeOut = function(data,myfunction){
	var toConfirm=false;
	if('{"ajaxSessionTimeOut":"yes"}'==data){
		toConfirm = true;
	}
	if(!toConfirm&&data!=null){
		toConfirm=data.ajaxSessionTimeOut;
	}
	if(toConfirm){
		jConfirm(pubi18n.i18n_timeout, pubi18n.i18n_tipTitle, function(r) {
			if (r) {
				document.location.href=window.location.href;
				return 0;
			}
		});
	}else{
		if(typeof myfunction == "function"){
			myfunction();
		}
	}
};
/**
 * 基准库收藏成果 publicationArticleType
 *//*
Pubsearch.importPdwh =function (des3PubId,obj){
	if(obj){
		BaseUtils.doHitMoreAndchangF(obj,1000,"Pubsearch.delCollectedPub('"+des3PubId+"',this);");
	}
	$.ajax({
		url:"/pubweb/ajaxsavePaper",
		type:"post",
		dataType:"json",
		data : {"des3PubId":des3PubId,"pubDb":"PDWH"},
		success:function(data){
			BaseUtils.ajaxTimeOut(data , function(){
				if(data && data.result){
					if(data.result == "success"){
			            $(obj).html('<i class="icon-collect-cur"></i> '+Pubsearch.unsave);
						scmpublictoast(Pubsearch.saveSuccess,1000);
					}else if(data.result == "exist"){
						scmpublictoast(Pubsearch.pubIsSaved,1000);
					}else if(data.result == "isDel"){
						scmpublictoast(Pubsearch.noPub,1000);
					}else{
						scmpublictoast(Pubsearch.saveFail,1000);
					}
				}else{
					scmpublictoast(Pubsearch.saveFail,1000);
				}
				
			});
		
		},
		error:function(){
		}
	});
	
}
//基准库成果-取消收藏

Pubsearch.delCollectedPub = function(pubId,obj){
	if(obj){
		BaseUtils.doHitMoreAndchangF(obj,1000,"Pubsearch.importPdwh('"+pubId+"',this);");
	}
	$.ajax({
		url:"/pubweb/ajaxdelCollectedPub",
		type:'post',
		data:{"des3PubId":pubId,"pubDb":"PDWH"},
		success:function(data){
		    BaseUtils.ajaxTimeOut(data, function(){
		        if(data && data.result == "success"){
		        	$(obj).html('<i class="icon-collect"></i> '+Pubsearch.save);
		            scmpublictoast(Pubsearch.optSuccess,1000);	
		        }else{
		            scmpublictoast(Pubsearch.optFailed,1000);
		        }
		    });
		}
	});
}*/
//引用
Pubsearch.dynCitePub = function(pubId,obj,event){
	var urlStr = "/pubweb/publication/ajaxpdwhpubquote?pubId="+pubId+"&TB_iframe=true&height=350&width=750";
	$(obj).attr("href",urlStr).click();
	Pubsearch.stopNextEvent(event)
}

Pubsearch.addDefultArea = function(areaCode){
	if($("#defultArea").val() == ""){
		$("#defultArea").val(areaCode);
	}else{
		var areaList = $("#defultArea").val().split(',');
		var index = $.inArray(areaCode, areaList);
		if(index < 0){
			$("#defultArea").val($("#defultArea").val()+','+areaCode);
		}
	}
}
Pubsearch.deleteDefultArea = function(areaCode){
	var areaStr = $("#defultArea").val();
	var areaList = areaStr.split(',');
	if($.inArray(areaCode, areaList)>=0){
		areaList.splice($.inArray(areaCode,areaList),1);
		$("#defultArea").val(areaList.join(','));
	}
	var searchAreaStr = $("#searchArea").val();
	var searchAreaList = searchAreaStr.split(',');
	if($.inArray(areaCode, searchAreaList)>=0){
		searchAreaList.splice($.inArray(areaCode,searchAreaList),1);
		$("#searchArea").val(searchAreaList.join(','));
	}
}

Pubsearch.addListElement = function(listJson,addStr){
	if(!addStr || addStr == ""){
		return;
	}
	var list = new Array();;
	if(listJson){		
		list=eval(listJson);
		var index = $.inArray(addStr, list);
		if(index < 0){
			list.push(addStr);
		}
	}else{
		list.push(addStr);
	}
	return(JSON.stringify(list));
}
Pubsearch.deleteListElement = function(listJson,addStr){
	if(!addStr || addStr == "" || !listJson){
		return ;
	}
	var list = new Array();;
	list=eval(listJson);
	var index = $.inArray(addStr, list);
	if(index >= 0){
		list.splice(index,1);
	}
	return(JSON.stringify(list));
}

//转义
Pubsearch.encodeHtml = function(s){  
	REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g;
    return (typeof s != "string") ? s :  
        s.replace(REGX_HTML_ENCODE,  
                  function($0){  
                      var c = $0.charCodeAt(0), r = ["&#"];  
                      c = (c == 0x20) ? 0xA0 : c;  
                      r.push(c); r.push(";");  
                      return r.join("");  
                  });  
};  

Pubsearch.addHover = function(hoverlist,text){
    for(var j = 0;j < hoverlist.length; j++ ){
    	hoverlist[j].onmouseover = function(){
    		if(!$(this).hasClass('item_list-align_slected')&&$(this).find(".filter-value__cancel").length <=0){
    		    $(this).append(text);
    		}
    	}
    	hoverlist[j].onmouseleave = function(){
    		$(this).find(".filter-value__cancel").remove();
    	}
    }
}

//成果详情,点击分享，把成果信息加载到分享界面
Pubsearch.getPubDetailsSareParam = function(obj){
	//初始化推荐联系人
	var des3ResId = $(obj).attr("des3ResId");
	if(des3ResId==null){
		des3ResId = $(obj).attr("resId");
	}
	var pubId = $(obj).attr("pubId");
	  var databaseType = $(obj).attr("databaseType");
	  var dbId = $(obj).attr("dbId");
	  var resType = $(obj).attr("resType");
	  var pdwhpubShare = $(obj).attr("pdwhpubShare");
	  $("#share_to_scm_box").attr("dyntype","GRP_SHAREPUB").attr("des3ResId",des3ResId).attr("pubId",pubId).attr("resType",resType).attr("dbId",dbId).attr("pdwhpubShare",pdwhpubShare);
	  var content  = $(obj).prev().val();
	  var $parent =  $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
	  $parent.html("");
	  SmateShare.createFileDiv($parent , content ,des3ResId ) ;
	
};