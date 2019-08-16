<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
<script type="text/javascript">

var selectedKeywords = [];

$(function(){
    $(".keyword_content_div").each(function(){
        if($(this).text() != ""){
            selectedKeywords.push($.trim($(this).text()));
        }
    });
    document.getElementsByClassName("new-edit_keword-tip")[0].style.height =  window.innerHeight - 160 + "px";
    document.getElementsByClassName("new-edit_keword-body")[0].style.height =  window.innerHeight - 160 + "px";
    var keywords = $(".keyword_content_div");
    $("#itme_sum").text(keywords.length);
});

//添加关键词
function addPsnKeywords(){
	//中断提示关键词请求
	if(searchPromptKeywordReq){
        searchPromptKeywordReq.abort();
    }
	//隐藏提示关键词
	hidePrompts();
	var keyword = $.trim($("#keyword_input").val());
	if(keyword){
		if(selectedKeywords.length >= 10){
		  newMobileTip("最多保存10个关键词");
	    }else{
		    //查重
		    var isRepeat = false;
		    for(var i=0; i<selectedKeywords.length; i++){
		        if(keyword.toLowerCase() == selectedKeywords[i].toLowerCase()){
		            isRepeat = true;
		            break;
		        }
		    }
		    if(isRepeat){
		      newMobileTip("关键词不能重复");
		    }else{
				var templateDiv = $(".keyword_template").prop("outerHTML") ;
				templateDiv = templateDiv.replace("$keywords", keyword).replace("keyword_template", "keyword_content_div").replace("$keyword_content_class", "keyword_content");
				$("#current_keywords").append(templateDiv);
				$(".keyword_content_div:hidden").show();
				selectedKeywords.push(keyword);
			    $("#save_keyword_btn").css("border","#288aed");
			    $("#save_keyword_btn").css("background","#288aed");
		    }
	    }
	}else if(keyword==""){
		//scmpublictoast("请输入关键词",1000);
	}
	$("#keyword_input").val("");
    $("#addKeyBottom").css("border","#ccc");
    $("#addKeyBottom").css("background","#ccc");
    var keywords = $(".keyword_content_div");
    $("#itme_sum").text(keywords.length);
}

//去重
Array.prototype.unique = function(){
    var res = [];
    var json = {};
    for(var i = 0; i < this.length; i++){
        var $currentItem = $.trim(this[i]).toLowerCase();
	     if(!json[$currentItem]){
			res.push($.trim(this[i]));
			json[$currentItem] = 1;
	     }
    }
    return res;
}

//保存关键词
function ajaxSavePsnKeywords(){
	BaseUtils.doHitMore($("#save_keyword_btn"), 1000);
	var url = window.location.href;
	var reqUrl = null;
	if(url.indexOf("reqUrl=")>0){
		reqUrl = url.substring(url.indexOf("reqUrl=")+7);
	}
	if(selectedKeywords.length > 0){
	    //去掉重复的
	    $.ajax({
	        url: "/psnweb/mobile/keywords/ajaxsave",
	        type: "post",
	        data: {
	            "psnKeyStr": selectedKeywords.unique().join("@@")
	        },
	        dataType: "json",
	        success: function(data){
	            if(data && !data.ajaxSessionTimeOut){
	                if(data.result == "error"){
	                  newMobileTip("网络错误，请稍后重试");
	                }else if(data.result == "sumLimit"){
	                  newMobileTip("最多保存10个关键词");
	                }else{
                        //不是个人主页的编辑关键词，跳转首页
                        if($("#isHomepageEdit").val() == 0){
                            if(reqUrl){
                                document.location.href = decodeURIComponent(reqUrl);
                            }else{
                        	    //判断当前环境
                        	    var ua = navigator.userAgent.toLowerCase();
                        	    if(ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)){//判断是否为iso系统 
                        	        if(ua.match(/MicroMessenger/i)){
                        	        	document.location.href = "/dynweb/mobile/dynshow";
                        	        }else{
                                        document.location.href = "/dynweb/mobile/dynshow";
                                        // 为啥这样做、？？
                                        //document.location.href = "/psnweb/mobile/registersuccess";
                        	        } 
                        	    }else{
                                    document.location.href = "/dynweb/mobile/dynshow";
                        	    }
                              
                            }
                        }else{
                          document.location.href = "/psnweb/mobile/homepage";
                        }
	                }
	            }
	        },
	        error: function(data){
	          newMobileTip("网络错误，请稍后重试");
	        }
	    });
	}else{
	    //scmpublictoast("请至少填写一个关键词",1000);
	}
    
	
}


//提示关键词
var searchPromptKeywordReq = null;
function searchPromptKeywords(){
	if(searchPromptKeywordReq){
		searchPromptKeywordReq.abort();
	}
	var keyword = $("#keyword_input").val();
    if(keyword && $.trim(keyword)){
    	$("#addKeyBottom").css("border","#288aed");
        $("#addKeyBottom").css("background","#288aed");
    	keyword = $.trim(keyword);
    	searchPromptKeywordReq = 
    		$.ajax({
    		url: "/groupweb/mygrp/ajaxautoconstkeydiscs",
            type: "post",
            data: {
                "searchKey": keyword,
                "keywordsSize": 5
            },
            dataType: "json",
            success: function(data){
            	$(".keywords_prompt_div").remove();
            	if(data && !data.ajaxSessionTimeOut){
            		for(var i = 0; i<data.length; i++){
            			var code= data[i].code;
            			var key = data[i].name;
            			var keyId = data[i].keyId;
		            	var templateDiv = $(".keywords_prompt_template").prop("outerHTML") ;
		                templateDiv = templateDiv.replace("$keyword_tips_content", key).replace("keywords_prompt_template", "keywords_prompt_div");
		                $("#keywords_prompt_div_list").append(templateDiv);
            		}
	                $("#current_selected_kd").hide();
	                $("#keywords_prompt_div_list").css("height","auto");
	                $("#keywords_prompt_div_list").show();
	                $(".keywords_prompt_div:hidden").show();
	                //有可能点击了添加按钮后，提示的关键词才显示出来
	                var keyword = $("#keyword_input").val();
	                if(!keyword || !$.trim(keyword)){
	                	hidePrompts();
	                }
            	}else{
            		hidePrompts();
            	}
            },
            error: function(data){
            	hidePrompts();
            }
    	});
    }else{
        $("#addKeyBottom").css("border","#ccc");
        $("#addKeyBottom").css("background","#ccc");
    	searchPromptKeywordReq.abort();
    	hidePrompts();
    }
}

//隐藏提示的关键词
function hidePrompts(){
	$("#keywords_prompt_div_list").hide();
    $(".keyword_content_div:hidden").show();
    $("#current_selected_kd").show();
}

//选中提示的关键词
function selectPromptKeyword(obj){
	$("#keywords_prompt_div_list").hide();
	var $this = $(obj);
	$("#keyword_input").val($this.find(".prompt_keyword_content").text());
	addPsnKeywords();
	$("#current_selected_kd").show();
}

//删除关键词
function deleteKeywords(obj){
    var $this = $(obj);
    $this.parent(".keyword_content_div").remove();
	var keyword = $this.siblings(".keyword_content:last").text();
	//删除选中的关键词
	var newSelectedKeywords = [];
	for(var i=0; i<selectedKeywords.length; i++){
        if(keyword.toLowerCase() != selectedKeywords[i].toLowerCase()){
            newSelectedKeywords.push(selectedKeywords[i]);
        }
    }
	selectedKeywords = newSelectedKeywords;
	if(selectedKeywords.length <= 0){
        $("#save_keyword_btn").css("border","#ccc");
        $("#save_keyword_btn").css("background","#ccc");
	}
  var keywords = $(".keyword_content_div");
  $("#itme_sum").text(keywords.length);
}

function doNoting(type){
	if("skip" == type){
		window.location.href = "/dynweb/mobile/dynshow";
	}else{
		window.history.go(-1);
	}
}

function changeSaveBtnStat(){
    if($(".keyword_content_div").length > 0){
        $("#save_keyword_btn").removeClass("opt_button");
        $("#save_keyword_btn").removeAttr("disabled");
    }else{
        $("#save_keyword_btn").addClass("opt_button");
        $("#save_keyword_btn").attr("disabled", "disabled");
    }
}

function callbackKey(event){
  if(event.keyCode == "13"){
    $("#addKeyBottom").click();
  }
}
</script>
</head>
<body>
  <input type="hidden" id="isHomepageEdit" value="${isHomepageEdit }">
  <div class="new-edit_keword-container">
    <div class="new-edit_keword-header">
      <s:if test="isHomepageEdit == 1">
        <i class="material-icons" onclick="window.history.go(-1);">keyboard_arrow_left</i>
      </s:if>
      <s:else>
        <i class="material-icons"></i>
      </s:else>
      <span class="new-edit_keword-header_title">设置关键词</span> <i class="material-icons"></i>
    </div>
    <div class="new-edit_keword-neck">
      <div class="new-edit_keword-neck_input">
        <input type="text" placeholder="输入新增的关键词" id="keyword_input" onkeydown="callbackKey(event)" oninput="searchPromptKeywords();"
          onpropertychange="searchPromptKeywords();">
      </div>
      <div class="new-edit_keword-neck_btn" id="addKeyBottom" onclick="addPsnKeywords();"
        style="border: #ccc; background: #ccc;">新增</div>
    </div>
    <!-- 已填关键词  begin-->
    <div class="new-edit_keword-body" id="current_selected_kd">
      <div class="new-edit_keword-body_title">当前关键词列表最多输入10个(已输入<span id="itme_sum"></span>个)</div>
      <div class="new-edit_keword-body_item" id="current_keywords">
        <!-- 模板   begin -->
        <div class="new-edit_keword-content_list keyword_template" style="display: none;">
          <span class="new-edit_keword-content_list-content $keyword_content_class">$keywords</span> <i
            class="new-edit_keword-content_list-close" onclick="deleteKeywords(this);"></i>
        </div>
        <!-- 模板   end -->
        <s:iterator value="keywords" var="word">
          <div class="new-edit_keword-content_list keyword_content_div">
            <span class="new-edit_keword-content_list-content keyword_content">${word.keyWords }</span> <i
              class="new-edit_keword-content_list-close" onclick="deleteKeywords(this);"></i>
          </div>
        </s:iterator>
      </div>
    </div>
    <!-- 已填关键词  end-->
    <!-- 提示的关键词  begin-->
    <div class="new-edit_keword-tip" id="keywords_prompt_div_list" style="display: none;width: 91%;left: 19.3438px; bottom: auto;z-index:102;position:fixed;box-shadow:0 1px 5px 0 rgba(0, 0, 0, 0.2), 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.12);">
      <!-- 模板   begin -->
      <div class="new-edit_keword-tip_list keywords_prompt_template" onclick="selectPromptKeyword(this);"
        style="display: none;">
        <!-- <i class="material-icons new-edit_keword-tip_icon-left">query_builder</i> -->
        <div class="new-edit_keword-tip_container">
          <span class="new-edit_keword-tip_container-infor prompt_keyword_content">$keyword_tips_content</span>
        </div>
      </div>
      <!-- 模板   end -->
    </div>
    <!-- 提示的关键词  end-->
    <div class="new-edit_keword-footer">
      <s:if test="isHomepageEdit == 1">
        <div class="new-edit_keword-footer_item new-edit_keword-footer_save" id="save_keyword_btn"
          onclick="ajaxSavePsnKeywords();"
          <c:if test="${keywords== null || fn:length(keywords) == 0}">style="border: #ccc;background: #ccc;"</c:if>>保存</div>
        <div class="new-edit_keword-footer_item" onclick="doNoting('cancel');">取消</div>
      </s:if>
      <s:else>
        <div class="new-add_keword-footer_item new-edit_keword-footer_save" id="save_keyword_btn"
          onclick="ajaxSavePsnKeywords();"
          <c:if test="${keywords== null || fn:length(keywords) == 0}">style="border: #ccc;background: #ccc;"</c:if>>保存</div>
      </s:else>
    </div>
  </div>
  <a id='openApp' href="javascript:void(0);" style="display: none;" target="_blank"></a>
</body>
</html>
