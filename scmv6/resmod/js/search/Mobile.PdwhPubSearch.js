/**
 * Mobile-基准库成果检索.
 * 
 * @author xys
 * 
 */
var PdwhPubSearch = PdwhPubSearch ? PdwhPubSearch : {};

PdwhPubSearch.type = 0;// 1-论文；2-专利；

PdwhPubSearch.init = function(type){
	PdwhPubSearch.type = type;
};

/**
 * 加载基准库成果列表.
 */
PdwhPubSearch.ajaxlist = function(data, myScroll){
	if(PdwhPubSearch.type == 0)
		return;
	var ajaxurl = "";
	if(PdwhPubSearch.type == 1){
	//论文
		ajaxurl = "/pubweb/pdwhpaper/search/ajaxlist";
	}else if(PdwhPubSearch.type == 2){
	//专利 
		ajaxurl = "/pubweb/pdwhpatent/search/ajaxlist";
	}
	$.ajax({
		url: ajaxurl,
		type: "post",
		data: data,
		dataType: "html",
		success: function(data){
			$("#listdiv").append(data);
			myScroll.refresh();
			PdwhPubSearch.initFullText();
			$("#pageNo").val(parseInt($("#pageNo").val()) + 1);
		},
		error: function(){
			
		}
	});
};


PdwhPubSearch.ajaxlistnew = function(data){
	if(PdwhPubSearch.type == 0)
		return;
	var ajaxurl = "";
	if(PdwhPubSearch.type == 1){
	//论文
		ajaxurl = "/pub/querylist/ajaxpdwhpub";
	}else if(PdwhPubSearch.type == 2){
	//专利 
		ajaxurl = "/pubweb/pdwhpatent/search/ajaxlist";
	}
	$.ajax({
		url: ajaxurl,
		type: "post",
		data: data,
		dataType: "html",
		beforeSend:function(){
			$(".load_preloader").doLoadStateIco({status:1});
		},
		success: function(data){
			$(".load_preloader").find(".preloader").remove();
			$(".load_preloader").before(data);
//			PdwhPubSearch.initFullText();
			$("#pageNo").val(parseInt($("#pageNo").val()) + 1);
		},
		error: function(){
			$(".load_preloader").find(".preloader").remove();
		}
	});
};

PdwhPubSearch.ajaxrefreshlist = function(data, myScroll){
	if(PdwhPubSearch.type == 0)
		return;
	var ajaxurl = "";
	if(PdwhPubSearch.type == 1){
	//论文
		ajaxurl = "/pubweb/pdwhpaper/search/ajaxlist";
	}else if(PdwhPubSearch.type == 2){
	//专利
		ajaxurl = "/pubweb/pdwhpatent/search/ajaxlist";
	}
	$.ajax({
		url: ajaxurl,
		type: "post",
		data: data,
		dataType: "html",
		success: function(data){
			$("#listdiv").html(data);
			myScroll.refresh();
		},
		error: function(){
			
		}
	});
}

/**
 * 加载基准库成果左侧菜单.
 */
PdwhPubSearch.ajaxleftmenu = function(data){
	if(PdwhPubSearch.type == 0)
		return;
	var ajaxUrl = "";
	if(PdwhPubSearch.type == 1){
	//论文
		ajaxUrl = "/pubweb/pdwhpaper/search/ajaxleftmenu";
	}else if(PdwhPubSearch.type == 2){
	//专利
		ajaxUrl = "/pubweb/pdwhpatent/search/ajaxleftmenu";
	}
	$.ajax({
		url: ajaxUrl,
		type: "post",
		data : data,
		dataType: "html",
		success: function(data){
			$("#select").html(data);
			PdwhPubSearch.doFundingYear();
			topInit();
		},
		error: function(){
			
		}
	});
};


PdwhPubSearch.doFundingYear = function() {
	var years = $(".infolink_fundingYear");
	$(".fundingYearIndex").val(1);
	for ( var i = 5; i < years.length; i++) {
		$(years[i]).hide();
	}
};

PdwhPubSearch.fundingYearMove = function(go) {
	var years = $(".infolink_fundingYear");
	var count = Math.floor(years.length / 5);
	if (years.length % 5 > 0) {
		count++;
	}
	var currentFirst = Number($(".fundingYearIndex").val());
	// 由于发表年份为倒序
	if ("left" == go) {
		var leftCanClick = $("#left").attr("canClick");
		if (leftCanClick == "true") {// 前五年可用时
			currentFirst += 1;
		}
		$(".fundingYearIndex").val(currentFirst);
	}
	if ("right" == go) {
		var rightCanClick = $("#right").attr("canClick");
		if (rightCanClick == "true") {// 后五年五年可用时
			currentFirst -= 1;
		}
		$(".fundingYearIndex").val(currentFirst);
	}
	if (currentFirst <= 1) {
		$("#right").attr("style", "cursor: ;color:gray");
		$("#right").attr("canClick", "false");
		$("#left").attr("style", "cursor: pointer;");
		$("#left").attr("canClick", "true");
		currentFirst = 1;
	} else if (currentFirst >= count) {
		$("#right").attr("style", "cursor: pointer;");
		$("#right").attr("canClick", "true");
		$("#left").attr("style", "cursor: ;color:gray");
		$("#left").attr("canClick", "false");
		currentFirst = count;
	} else {
		$("#right").attr("style", "cursor: pointer;");
		$("#right").attr("canClick", "true");
		$("#left").attr("style", "cursor: pointer;");
		$("#left").attr("canClick", "true");
	}
	for ( var i = 0; i < years.length; i++) {
		$(years[i]).hide();
	}
	for ( var i = (currentFirst - 1) * 5; i < ((currentFirst - 1) * 5 + 5)
			&& i < years.length; i++) {
		if ($(years[i]).length > 0) {
			$(years[i]).show();
		}
	}
};

PdwhPubSearch.ajaxinitfulltext = function(data){
	$.ajax({
		url: "/pubweb/search/ajaxinitfulltext",
		type: "post",
		data: {
			"searchPub": data
		},
		dataType: "json",
		success: function(data){
			for(var i = 0; i < data.length; i++){
				var fulltextPicPath = data[i].fulltextPicPath;
				var fulltextFileId = data[i].des3FulltextFileId;
				var pdwhPubId = data[i].pdwhPubId;
				//var Id = "#"+pdwhPubId;
				if(fulltextFileId != null && fulltextFileId != ""){
					$("img[pubId='"+pdwhPubId+"']").attr("src",fulltextPicPath);
					//$(Id).attr("class", "paper_pic");
					//$(Id).attr("href", "javascript:openFile('"+fulltextFileId+"')");
				}
				//$(Id).parent("div.paper").addClass("hasInit");
				$("img[pubId='"+pdwhPubId+"']").parent("div.paper").addClass("hasInit");
			}
		},
		error: function(data){
			
		}
	});
}

/**
 * 收集信息初始化成果全文
 */
PdwhPubSearch.initFullText = function(){
	var fullTextData = "[";
	var count = 0;
	$("div.paper").not(".hasInit").each(function(){
		count++;
		var des3PdwhPubId = $(this).find("input[name$='des3PdwhPubId']").val() != undefined ? $(this).find("input[name$='des3PdwhPubId']").val() : "";
		var dbId = $(this).find("input[name$='dbId']").val() != undefined ? $(this).find("input[name$='dbId']").val() : "";
		if(count == $("div.paper").not(".hasInit").length){
			fullTextData += "{\"des3PdwhPubId\": \""+des3PdwhPubId+"\", \"dbId\": \"" + dbId+"\"}";
		}else{
			fullTextData += "{\"des3PdwhPubId\": \""+des3PdwhPubId+"\", \"dbId\": \"" + dbId+"\"},";
		}
	});
	
	fullTextData += "]";                     
	PdwhPubSearch.ajaxinitfulltext(fullTextData);
}

PdwhPubSearch.ajaxpaperlist = function() {
	if(parseInt($("#pageNo").val()) < parseInt($("#totalPages").val())){
		var data = {
			"pubYear":$("#pubYear").val(), 
			"searchType": "1", 
			"searchString": $.trim($("#searchString").val()), 
			"pubTypeId":$("#pubTypeId").val(), 
			"orderBy":$("#orderBy").val(), 
			"page.pageNo":parseInt($("#pageNo").val()) + 1,
			"platform": "mobile",
			"language": $("#language").val(),
			"firstLoad": "0"
			}
		PdwhPubSearch.ajaxlistnew(data);
	}
}
PdwhPubSearch.requestFullText =function (des3PubId){
	if(des3PubId){
		$.post("/pubweb/fulltext/req/ajaxadd", {'des3PubId': des3PubId, 'pubType': 'pdwh'}, function(result){
			BaseUtils.ajaxTimeOut(result , function(){
				if(result.status == "success"){
					scmpublictoast("发送全文请求成功", 2000);
				}else{
					scmpublictoast("请求发送失败，系统异常，请稍后再试", 2000);
				}
			});
		},"json");
	}else{
		scmpublictoast("请求参数不正确", 2000);
	}
}
/*
*//**
 * 基准库收藏成果 publicationArticleType
 *//*
PdwhPubSearch.importPdwh =function (des3PubId,obj){
	if(obj){
		BaseUtils.doHitMoreAndchangF(obj,500,"PdwhPubSearch.delCollectedPub('"+des3PubId+"',this);");
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
						$(obj).find("i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
			            $(obj).find("span").text(Pubsearch.unsave);
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

PdwhPubSearch.delCollectedPub = function(pubId,obj){
	if(obj){
		BaseUtils.doHitMoreAndchangF(obj,500,"PdwhPubSearch.importPdwh('"+pubId+"',this);");
	}
	$.ajax({
		url:"/pubweb/ajaxdelCollectedPub",
		type:'post',
		data:{"des3PubId":pubId,"pubDb":"PDWH"},
		success:function(data){
		    BaseUtils.ajaxTimeOut(data, function(){
		        if(data && data.result == "success"){
					$(obj).find("i").removeClass("paper_footer-comment__flag").addClass("paper_footer-comment");
		            $(obj).find("span").text(Pubsearch.save);
		            scmpublictoast(Pubsearch.optSuccess,1000);	
		        }else{
		            scmpublictoast(Pubsearch.optFailed,1000);
		        }
		    });
		}
	});
}*/