/**
 * 基准库成果检索.
 * 
 * @author xys
 * 
 */
var PdwhPubSearch = PdwhPubSearch ? PdwhPubSearch : {};
var ctxpath='/scmwebsns';
var searchTypeTip="检索论文、专利、专家...";
var searchType="检索人员";
var searchType2="检索论文";
var searchType3="检索专利";
var searchTypeTipEn="Search publications patents...";
var searchTypeEn="Search user";
var searchTypeEn2="Search publication";
var searchTypeEn3="Search patent";
/*var friendShareContentPub="You may find this publication(s) interesting.";
var friendShareContentRef="You may find this reference(s) interesting.";*/

PdwhPubSearch.type = 0;// 1-论文；2-专利；

PdwhPubSearch.init = function(type){
	PdwhPubSearch.type = type;
};
//微信分享
function getQrImg(url){
	if(navigator.userAgent.indexOf("MSIE")>0){
		$("#share-qr-img").qrcode({render: "table",width: 175,height:175,text:url });
	}else{
		$("#share-qr-img").qrcode({render: "canvas",width: 175,height:175,text:url });
	}
}

/**
 * 加载基准库成果列表.
 */
PdwhPubSearch.ajaxlist = function(){
	if(PdwhPubSearch.type == 0)
		return;
	
	if(PdwhPubSearch.type == 1){
		
	}else if(PdwhPubSearch.type == 2){
		
	}
};

/**
 * 加载基准库成果左侧菜单.
 */
PdwhPubSearch.ajaxleftmenu = function(){
	if(PdwhPubSearch.type == 0)
		return;
	
	if(PdwhPubSearch.type == 1){//论文
		
	}else if(PdwhPubSearch.type == 2){//专利
		
	}
};
/**
 * 初始化加载列表和菜单
 */
PdwhPubSearch.ajaxsearch = function(url,data,select){
	$.ajax({
		url:url,
		type : "post",
		dataType : "html",
		data :data,
		success:function(data){
			$("#content").find(select).html(data);
			if(select==".cont_r"){
				//PdwhPubSearch.ajaxrgetfulltext();
			//分享下拉模式
			$(".share_tile").sharePullMode({
				showSharePage:function(_this){
					viewSharePub(_this);
				},
				'snsctx' : snsctx,
				'styleVersion':1,
				'isShowSmate':0,
			});}
		},
		error:function(data){
		}
	});
}
//显示全文图片和下载统计数
PdwhPubSearch.ajaxrgetfulltext= function (){
	var length=$("#pubListTal").find("input[name='des3pubId']").length;
	if(length==0){
		return;
	}
	var fullTextPubs="[";
	$("#pubListTal").find("input[name='des3pubId']").each(function(i, dom){
		//pdwpPubList.push({"des3PubId":$(this).val(),"dbId":$(this).next().val()});
		if(i==length-1){
			fullTextPubs+="\""+$(this).val()+"\"";
		}else{
			fullTextPubs+="\""+$(this).val()+"\",";
		}
	});
	fullTextPubs += "]";
	$.ajax({
		url : "/pubweb/mobile/ajaxrgetfulltext",
		type : "post",
		dataType : "html",
		data : {"fullTextPubs" : fullTextPubs},
		success : function(data){
			if(data!=null&&typeof data !== 'undefined' && data !== ''){
				var objdata = eval(data);
				$(".cont_r").find("img[name='fullImg']").each(function(i){
					 var _dowCount = $(this);
					 $.each(objdata,function(n,value) {   
							var pubId = value['des3PubId'];
							var fullTextImg = value['fullTextImg'];
		                    if(_dowCount.attr("des3PubId") == pubId && typeof(fullTextImg) != 'undefined'){
		                    	_dowCount.attr("src",fullTextImg); 
		                    }    
				      });  
				});				
				$(".dev_down-count").each(function(i){
					 var _dowCount = $(this);
					 $.each(objdata,function(n,value) {   
							var downloadCount = value['downloadCount'];
							var pubId = value['des3PubId'];
		                    if(_dowCount.attr("des3PubId") == pubId && typeof(downloadCount) != 'undefined' && downloadCount!=0){
		               	        var html = '<div>'+downloadCount+'</div><div class="">'+PdwhPubsearch.dowload+'</div>'
		               	        _dowCount.html(html); 
		                    } 
				      });  
	
				});
			}
		} 
	});
}
/**
 * 加载全文图片
 */
PdwhPubSearch.ajaxloadful = function(){
	
	var length=$("#pubListTal").find("input[name='des3pubId']").length;
	if(length==0){
		return;
	}
	var temp="[";
	$("#pubListTal").find("input[name='des3pubId']").each(function(i, dom){
		//pdwpPubList.push({"des3PubId":$(this).val(),"dbId":$(this).next().val()});
		if(i==length-1){
			temp+="{\"des3PdwhPubId\":\""+$(this).val()+"\",\"dbId\":\""+$(this).next().val()+"\"}";
		}else{
			temp+="{\"des3PdwhPubId\":\""+$(this).val()+"\",\"dbId\":\""+$(this).next().val()+"\"},";
		}
	});
	temp+="]";
	$.ajax({
		url:"/pubweb/search/ajaxinitfulltext",
		type : "post",
		dataType : "json",
		data :{
			"searchPub":temp
		},
		success:function(data){
			if(data!=null&&typeof data !== 'undefined'){
				$("#pubListTal").find("img[name='fullImg']").each(function(i){
					if(data[i]['fulltextFileId'] && data[i]['fulltextFileId'] != ""){
						var pdwhPubId = data[i]['pdwhPubId'];
						var snsDes3PubId = data[i]['snsDes3PubId'];//基准库对应的个人库成果
						var des3PdwhPubId = data[i]['des3PdwhPubId'];
						var dbId = data[i]['dbId'];
						var fileId = data[i]['fulltextFileId'];
						$("#grade_" + pdwhPubId).attr("href","javascript:savePdwhPaper(this, '"+des3PdwhPubId+"')"); 
						//已在后台获取
//						if(data[i]['fulltextPicPath'] && data[i]['fulltextPicPath'] != ""){ 
//							$(this).attr("src", data[i]['fulltextPicPath']);
//						}
					}
				});
			}
		},
		error:function(data){
		}
	});
};
//全文下载
PdwhPubSearch.fullTextDownload = function(des3PdwhPubId){
	if(des3PdwhPubId && des3PdwhPubId != ""){
		//SCM-14409 全文下载采用新的下载方式
		$.ajax({
			url: '/fileweb/download/ajaxgeturl',
			type: 'POST',
			dataType: 'json',
			data: {"des3Id": des3PdwhPubId, "type": 3},
			success: function(result){
			    BaseUtils.ajaxTimeOut(result,function(){
			        if(result && result.status == "success"){
			            window.location.href = result.data.url;
			        }
			    });
			}
		});
		return;
	}
}
/**
 * 控制年份的翻页显示
 */
PdwhPubSearch.moveFundingYear = function(go){
	var years = $("#years").find("div[name='year']");
	if(years.length<6){
		$("#menuListsc").hide();
	}
//	var count = Math.floor(years.length/ 5);
	var count = Math.ceil(years.length/ 5);
	if(go==""){//初始化第一页
		var showcount = years.length<5?years.length:5;
		for(var i=0;i<showcount;i++){
			years.eq(i).show();
		}
	}else if(go=="left"){//点击上一页
		var current =parseInt($("#currentyears").val());
		if(current>1){
			years.hide();
			for(var i=(current-2)*5;i<(current-1)*5;i++){
				years.eq(i).show();
			}
			$("#currentyears").val(current-1);
		}
	}else if(go=="right"){//点击下一页
		var current =parseInt($("#currentyears").val());
		if(current<count){//小于最大页数
			years.hide();
			for(var i=current*5;i<(current+1)*5;i++){
				years.eq(i).show();
			}
			$("#currentyears").val(current+1);
		}
	}
	var current =parseInt($("#currentyears").val());
	//按钮置灰
	if(current==1){
		//置灰上页按钮
		$("#yearlastpage").css("color","#e2e2e2");
	}else{
		$("#yearlastpage").css("color","#999");
	}
//	if(current==count+1){
	if(current==count){
		//置灰下页按钮
		$("#yearnextpage").css("color","#e2e2e2");
	}else{
		$("#yearnextpage").css("color","#999");
	}
};

//收藏到文献关联表
function savePdwhPaper(obj, des3PubId){
	$.ajax({
		url:"/pubweb/ajaxsavePaper",
		type:"post",
		dataType:"json",
		data:{"des3PubId":des3PubId, "pubDb":"PDWH","psnId":"12314234"},
		success:function(data){
			if(data && data.result){
				if(data.result == "success"){
					$.smate.scmtips.show('success',searchMsg.opt_success,null,1000);
				}else if(data.result == "exist"){
					$.smate.scmtips.show('warn',"已收藏该成果",null,1000);
				}else if(data.result == "isDel"){
					$.smate.scmtips.show('warn',"成果不存在",null,1000);
				}
				$(obj).removeClass("grade_disable");
			} else {
				var timeout = data.ajaxSessionTimeOut;
				 if (typeof timeout != 'undefined' && timeout == 'yes') {
					 jConfirm(searchMsg.sessionTimeout, searchMsg.prompt, function(r) {
						if (r) {
		   					var url=window.location.href;
		   						window.location.href="/pubweb/getoauth?url="+encodeURIComponent(url); 
						}
					});
				 } 
			}
		$.Thickbox.closeWin();
		},
		error:function(){
			$.Thickbox.closeWin();
			$(obj).removeClass("grade_disable");
		}
	});
}
//导入到我的文献库
function impMyRef(pubId,dbid,fileId){
	$("#grade_"+pubId).addClass("grade_disable");
	var params=[];
	params.push({"pubId":pubId,"dbid":dbid,"fileId":fileId});
	var postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"2"};
		$.ajax({
//			url:ctxpath+"/reference/import/pdwh",
			url:ctxpath+"/reference/ajaximport/pdwh",
			type:"post",
			dataType:"json",
			data:postData,
			timeout:10000,
			success:function(data){
				if(data && data.result){
					$.smate.scmtips.show('success',searchMsg.opt_success,null,1000);
					$("#grade_"+pubId).removeClass("grade_disable");
				} else {
					var timeout = data.ajaxSessionTimeOut;
					 if (typeof timeout != 'undefined' && timeout == 'yes') {
						 jConfirm(searchMsg.sessionTimeout, searchMsg.prompt, function(r) {
							if (r) {
			   					var url=window.location.href;
			   					//http://dev.scholarmate.com/pubweb/getoauth?url=http%3A%2F%2Fdev.scholarmate.com%2Fpubweb%2Fsearch%2Fpdwhpaper%3FsearchString%3Dd
			   					//window.location.href="/scmwebsns/index?service="+url; 
			   						window.location.href="/pubweb/getoauth?url="+encodeURIComponent(url); 
							}
							else{
								    //取消，不需要刷新当前页面
				   					//document.location.reload();
							}
						});
					 } 
				}
			$.Thickbox.closeWin();
			},
			error:function(){
				$.Thickbox.closeWin();
				$("#grade_"+pubId).removeClass("grade_disable");
			}
		});	
};

//分享
function viewSharePub(_obj,pubId,des3PubId){
	  $("#viewSharePubBtn,#hidden-shareLink").thickbox({
			respath : "/resscmwebsns"
	  });
	  pubId = typeof pubId == "undefined" ? $(_obj).attr("resId") : pubId;
	  des3PubId = typeof des3PubId == "undefined" ? $(_obj).attr("des3ResId") : des3PubId;
	  //获取分享url
	  bdUrl=window.location.href;
	  var resTitle=getViewTitle();
	  if($.browser.msie){
		  if(resTitle.length>40){
			  resTitle = resTitle.substring(0,40)+"...";
			  }
	  }
	  bdShareConfig={
		'bdDes':'科研成果分享',//'请参考自定义分享摘要'
		'bdText':'分享科研成果\"'+resTitle+'\"',//'请参考自定义分享内容'
		'bdPopTitle':'分享科研成果\"'+resTitle+'\"',//'请参考自定义pop窗口标题'
		'bdComment':'您好，欢迎关注科研成果\"'+resTitle+'\"',	//'请参考自定义分享评论'
		'bdPic':'http://t2.qlogo.cn/mbloghead/07680f695683b1a35d72/0',	//'请参考自定义分享出去的图片'
		'searchPic':'0',
		'bdUrl':bdUrl
	  };

	  var resDetails=[];
	  resDetails.push({"resId":pubId,"resNode":$("#nodeId").val()});

	  shareConfig={
		"resIds":pubId,
		"des3Ids":des3PubId,
		"resType":$("#articleType").val(),
		"shareType":"1,2,4",
		"shareTypeFirst":"1",
		"actionName":"ext",
		"jsonParam":{"resType":$("#articleType").val(),"resDetails":resDetails}
	  };

	  shareContentConfig={			
	     "mineDynContent":"",
		 "friendShareContent":$("#articleName").val() == "reference" ? shareMsg.reference : shareMsg.publication
	  };
	  
	  $("#viewSharePubBtn").click();
};


function getViewTitle(){
	  var title_text ="";
	  var title_zh=$.trim($("#bdText_zh_CN").val());
    var title_en=$.trim($("#bdText_en_US").val());
	  if("zh_CN"=="en_US"){
		  title_text = title_zh==""?title_en:title_zh;
	  }else{
		  title_text = title_en==""?title_zh:title_en;
	  }
	  return title_text;
};

//引导用户填写准确的检索内容
PdwhPubSearch.getSearchSuggestion=function(){
  //目前只应用到成果检索
  if($("#search_patent").hasClass("cur")||$("#search_person").hasClass("cur")||$("#search_ins").hasClass("cur")){
    return;
  }
  
  var listUL=$("#num1").find(".ac__list");
  $("#search_some_one").on("blur",function(){
    listUL.html("");
    return;
  });
	
  //对应enter，应该在keydown时作出控制
	$("#search_some_one").on("keydown",function(e){
		if($.trim($("#search_some_one").val())==""||e.keyCode==13&&listUL.find(".ac__item").length==0){
			//$("#search_some_one").text("");
			listUL.html("");
			return;
		}
		if((e.keyCode==13||e.keyCode==108)&&listUL.find(".ac__item").length>0){
		  e.preventDefault();
			if(listUL.find(".item_hovered").length>0){
			  listUL.find(".item_hovered").mousedown();
			  listUL.html("");
			}else{
			  $("#search_some_one_form").submit();
			}
			return;
		}else if(e.keyCode==38&&listUL.find(".ac__item").length>0){
		  e.preventDefault();
			if(listUL.find(".item_hovered").length>0){
				const index=listUL.find(".item_hovered").index();
				listUL.find(".item_hovered").removeClass("item_hovered");
				if(index==0){
					listUL.find(".ac__item:last").addClass("item_hovered");
				}else{
					listUL.find(".ac__item").eq(index-1).addClass("item_hovered");
				}
			}else{
				listUL.find(".ac__item:last").addClass("item_hovered");
			}
			return;
		}else if(e.keyCode==40&&listUL.find(".ac__item").length>0){
		  e.preventDefault();
			if(listUL.find(".item_hovered").length>0){
				const index=listUL.find(".item_hovered").index();
				listUL.find(".item_hovered").removeClass("item_hovered");
				if(index==listUL.find(".ac__item").length-1){
					listUL.find(".ac__item:first").addClass("item_hovered");
				}else{
					listUL.find(".ac__item").eq(index+1).addClass("item_hovered");
				}
			}else{
				listUL.find(".ac__item:first").addClass("item_hovered");
			}
			return;
		}else if(e.keyCode==27&&listUL.find(".ac__item").length>0){
		  e.preventDefault();
      //listUL.html("");
		  $("#search_some_one").blur();
      return;
		}
	});
	
	//keyup的时候获取的字符串才完整，onkeydown时，比如是删除按键，会获取到删除之前的值
	//对keydown作出控制的按键，不再触发
	$("#search_some_one").on("keyup focus",function(e){
	  if(e.keyCode==27||e.keyCode==38||e.keyCode==40||e.keyCode==27){
	    e.preventDefault();
	    return;
	  }
	  if($.trim($("#search_some_one").val())==""){
	    listUL.html("");
	    return;
	  }
	  
    $.ajax({
      url : '/psnweb/ac/ajaxpdwhsearchpub',
      type : 'post',
      dataType:'json',
      data:{"searchKey":$.trim($("#search_some_one").val())},
      success : function(data1) {
        //由于有延迟，在返回结果时如果输入为空，则清空下拉框
        if($.trim($("#search_some_one").val())==""||$.trim($("#search_some_one").val())==null||undefined==$.trim($("#search_some_one").val())||"undefined"==$.trim($("#search_some_one").val())){
          listUL.html("");
          return;
        }        
        if (data1 != "null" && data1 != "error" && data1!=null) {
            var list = data1;
            if (list.length > 0) {
                var titleLines = "";
                var lineStr = "";
                list.forEach(function(x) {
                  var displayStr=x.suggestStr;
                  var type = $.trim(x.suggestStrLevel);
                  var avatarUrl = "/resmod/smate-pc/img/more.png";
                  var errorImg="/resmod/smate-pc/img/more.png";
                  var suggestInConType = 0;//默认
                  if(type!=null&&"undefine"!=type&&""!=type&&undefined!=type){
                    suggestInConType = type.substr(0,1);
                  }
                  if(suggestInConType==2){//机构
                    avatarUrl="/resmod/smate-pc/img/acquiescence-mec-avator.png";
                    errorImg="/resmod/smate-pc/img/acquiescence-mec-avator.png";
                    if(x.insLogo!=null&&undefined!=x.insLogo){
                      if(x.insLogo.indexOf("default.png")==-1){//默认机构也使用这边的默认图片
                        avatarUrl=x.insLogo;
                      }
                    }
                    displayStr="<div class=\"new-search_output-item_content\"><div class=\"new-search_output-item_content-up\">"+x.suggestInsName+"</div></div></div></li>";
                  }else if(suggestInConType==1){//人员
                    avatarUrl="/resmod/smate-pc/img/acquiescence-psn-avator.png";
                    errorImg="/resmod/smate-pc/img/acquiescence-psn-avator.png";
                    if(x.psnAvatars!=null&&undefined!=x.psnAvatars){
                      if(x.psnAvatars.indexOf("default.png")==-1){//默认人员头像也使用这边的默认图片
                        avatarUrl=x.psnAvatars;
                      }
                    }
                    displayStr="<div class=\"new-search_output-item_content\"><div class=\"new-search_output-item_content-up\">"+x.suggestPsnName+"</div>";
                    if(x.suggestInsName!=""&&"undefined"!=x.suggestInsName&&undefined!=x.suggestInsName){
                      displayStr = displayStr+"<div class=\"new-search_output-item_content-down\">"+x.suggestInsName+"</div>";
                    }
                    displayStr=displayStr+"</div></div></li>";
                  }
                  //var displayAvatar = "<div class=\"new-search_output-item_avator\"><img src=\"https://dev.scholarmate.com/resmod"+avatarUrl+"\"></div>";
                  var displayAvatar = "<div class=\"new-search_output-item_avator\"><img src=\""+avatarUrl+"\" onerror=\"this.onerror=null;this.src='"+errorImg+"'\"></div>";
                  lineStr = "<li class=\"ac__item\" suggestStr=\""
                          + x.suggestStr
                          +"\"suggestStr_n=\""
                          + x.suggestPsnName
                          +"\"suggestStr_nid=\""
                          + x.suggestPsnId
                          +"\"suggestStr_in=\""
                          + x.suggestInsName
                          +"\"suggestStr_iid=\""
                          + x.suggestInsId
                          +"\"suggestStr_t=\""
                          + x.suggestType
                          + "\" onmousedown=PdwhPubSearch.searchSelectedItem(this)> <div class=\"new-search_output-item\">"
                          + displayAvatar
                          + displayStr;
                  titleLines += lineStr;
                });
                listUL.html(titleLines);
                listUL.show();
                listUL.find(".ac__item").each(
                        function() {
                            $(this).mouseenter(
                                    function() {
                                        var hoveredItems = $(this).parent()
                                                .find(".item_hovered");
                                        if (hoveredItems.length > 0) {
                                            hoveredItems.each(function() {
                                                $(this).removeClass(
                                                        "item_hovered");
                                            });
                                        }
                                        $(this).addClass("item_hovered");
                                    }).mouseleave(function() {
                                $(this).removeClass("item_hovered");
                            })
                        });
            }else{
              listUL.html("");
            }
        }else{
          listUL.html("");
        }   
      }
    });
	});
};

//对onkeydown重复发出多个请求进行限制
var pdwhSearchTimer={};
PdwhPubSearch.delay=function(id, func, waitTime,e, object){
  if(pdwhSearchTimer[id]){
    window.clearTimeout(pdwhSearchTimer[id]);
    delete pdwhSearchTimer[id];
  }
  return pdwhSearchTimer[id] = window.setTimeout(function(){
    (func)(e, object);
    delete pdwhSearchTimer [id];
  }, waitTime);
  
}

PdwhPubSearch.searchSelectedItem=function(itemObject){
  var type=itemObject.getAttribute("suggestStr_t");//1.人员，2.机构，3.关键词，4保持与原来一样正常全文检索
  var psnName = itemObject.getAttribute("suggestStr_n");
  var psnId = itemObject.getAttribute("suggestStr_nid");
  var insName = itemObject.getAttribute("suggestStr_in");
  var insId = itemObject.getAttribute("suggeststr_iid");
  var inputStr = itemObject.getAttribute("suggeststr");
  $("#search_suggest_str_type").val(type);
  $("#search_suggest_str_psn").val(psnName);
  $("#search_suggest_str_pid").val(psnId);
  $("#search_suggest_str_ins").val(insName);
  $("#search_suggest_str_insId").val(insId);
  $("#search_suggest_str_kw").val(inputStr);
  switch(type){
    case "1":
      //区别水印，然后提交检索
      var userInfo=$("#search_some_one").val();
      if(userInfo==searchType || userInfo==searchTypeTip || userInfo==searchType2 || userInfo==searchType3 || userInfo==searchTypeEn || userInfo==searchTypeTipEn || userInfo==searchTypeEn2 || userInfo==searchTypeEn3){
        $("#search_some_one").val("");
      }
      $("#searchString").val(inputStr);
      $("#search_some_one").val(inputStr);
      $("#search_some_one_form").attr("action","");
      $("#search_some_one_form").attr("action","/pub/search/pdwhpaperbyac");
      $("#search_some_one_form").submit();
      break;
    default:
      var userInfo=$("#search_some_one").val();
    if(userInfo==searchType || userInfo==searchTypeTip || userInfo==searchType2 || userInfo==searchType3 || userInfo==searchTypeEn || userInfo==searchTypeTipEn || userInfo==searchTypeEn2 || userInfo==searchTypeEn3){
      $("#search_some_one").val("");
    }
    $("#searchString").val(inputStr);
    $("#search_some_one").val(inputStr); 
    $("#search_some_one_form").attr("action","");
    $("#search_some_one_form").attr("action","/pub/search/pdwhpaperbyac");
    $("#search_some_one_form").submit();
     break;
  }
};

PdwhPubSearch.getInputTips = function(title, pubTypeId, brObject) {
  $.ajax({
      url : "/pubweb/publication/autocomplete/ajaxtips",
      type : "POST",
      dataType : "json",
      data : {
          "titleStr" : title,
          "pubType" : pubTypeId
      },
      success : function(data) {
          $("#login_box_refresh_currentPage").val("false");
          BaseUtils.ajaxTimeOut(
                          data,
                          function() {
                              if (data != null) {
                                  var timeOut = data['ajaxSessionTimeOut'];
                                  if (timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes') {
                                      window.location.reload();
                                  } else {
                                      if (data != "null" && data != "error") {
                                          var list = data;
                                          if (list.length > 0) {
                                              var preLoader = "<div class=\"preloader_ind-linear\" style=\"width: 100%;\"></div>";
                                              var titleLines = "";
                                              var lineStr = "";
                                              list
                                                      .forEach(function(x) {
                                                          lineStr = "<li class=\"ac__item\" style=\"width:688px!important; height:50px; line-height:21px!important;\" code=\""
                                                                  + x.pdwhPubId
                                                                  + "\" title=\""
                                                                  + x.title
                                                                  + "\" onmousedown=ScholarEnter.autoFillPubInfo("
                                                                  + x.pdwhPubId
                                                                  + ")>"
                                                                  + x.title
                                                                  + "<br><label class=\"acitem_author\" style=\"font-size:12px!important; font-weight:bold!important;padding-left:6px;\">"
                                                                  + x.authors + "</label></li>";
                                                          titleLines += lineStr;
                                                      });
                                              titleLines = "<div class=\"ac__list\" style=\"background: rgb(255, 255, 255); border: 1px solid rgb(204, 204, 204); border-image: none; position: absolute; top: 0px;\">"
                                                      + titleLines + "</div>";
                                              brObject.next("#_ac_box_titlelist").html(titleLines);
                                              brObject.next("#_ac_box_titlelist").show();
                                              brObject.next("#_ac_box_titlelist").find(".ac__item").each(
                                                      function() {
                                                          $(this).mouseenter(
                                                                  function() {
                                                                      var hoveredItems = $(this).parent()
                                                                              .find(".item_hovered");
                                                                      if (hoveredItems.length > 0) {
                                                                          hoveredItems.each(function() {
                                                                              $(this).removeClass(
                                                                                      "item_hovered");
                                                                          });
                                                                      }
                                                                      $(this).addClass("item_hovered")
                                                                  }).mouseleave(function() {
                                                              $(this).removeClass("item_hovered");
                                                          })
                                                      });
                                          }
                                      }
                                  }
                              }
                          });
      }
  });
};

 