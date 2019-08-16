<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title>论文检索</title>
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.alerts.css" media="screen" />
<link href="${resscmsns }/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/common.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="styleSheet" type="text/css">
  <script src="${resscmsns }/js_v5/js2016/scrollBar.js" type="text/javascript"></script>
  <script type='text/javascript' src='${resmod }/js/dialog.js'></script>
  <script type="text/javascript" src="/resmod/js/weixin/jquery.qrcode.min.js"></script>
  <script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.alerts.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
  <script type="text/javascript" src="${resscmsns }/js_v5/plugin/jquery.sharePullMode.js"></script>
  <script type="text/javascript" src="${resscmsns }/js_v5/js2016/divselect.js"></script>
  <script type="text/javascript" src="${ressns }/js/fulltext/jquery.fulltext.request.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/PdwhPubSearch_${locale }.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/PdwhPubSearch.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/pub_search_${locale }.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/pub_search.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/search.message_${locale }.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
  <script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
  <script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
  <script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
  <script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
  <script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
  <script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
  <script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
  <script type="text/javascript">
 var pubCount = '<spring:message code='pub.menu.search.resultEnd' />'
 var pubCounts = '<spring:message code='pub.menu.search.resultEnds' />'
$(function(){
	if(window.locale == 'en_US'){
        noresultHTML =  '<div class="no_effort"><h2>Sorry, no results matching all your search terms were found.</h2>'
             +'<div class="no_effort_tip">'
             +'<span>Suggestions：</span><p>Make sure that all words are spelled correctly.</p>'
             +'<p>Try synonyms or more general keywords.</p>'
             +'<p>Search in other categories or clear filters.</p>'
             +'</div></div>'
    }
	//SCM-10339
	$(".header-t").css("position" ,"relative");
	var searchString =$("#searchString").val();
	if(searchString!==null&&searchString!=""){
		var data = {
		    "searchString":searchString,
	      	"suggestPsnName":$("#suggestStrPsn").val(),
	      	"suggestPsnId":$("#suggestStrPid").val(),
	      	"suggestInsName":$("#suggestStrIn").val(),
	      	"suggestType":$("#suggestStrType").val(),
	      	"suggestKw":$("#suggestStrKw").val(),
	      	"suggestInsId":$("#suggestStrInId").val()};
	    page.showPubList(data);
		//PdwhPubSearch.ajaxsearch("/pub/search/ajaxpaperleftmenu",data,".cont_l");
	}
    if(document.getElementsByClassName("header__nav")){
        document.getElementById("num1").style.display="flex";
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
    }else{
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num1"));
        document.getElementById("num2").style.display="flex";
    }
    
    if(document.getElementById("search_some_one")){
        document.getElementById("search_some_one").onfocus = function(){
            this.closest(".searchbox__main").style.borderColor = "#2882d8";
        }
        document.getElementById("search_some_one").onblur = function(){
            this.closest(".searchbox__main").style.borderColor = "#ccc";
        }
    }
});
 
 var noresultHTML =
 '<div class="no_effort"><h2>很抱歉，未找到与检索条件相关结果</h2>'
 +'<div class="no_effort_tip">'
 +'<span>温馨提示：</span><p>检查输入条件是否正确.</p>'
 +'<p>尝试同义词或更通用关键词.</p>'
 +'<p>更换检索类别或过滤条件.</p>'
 +'</div></div>';
 
var page = {};
page.showPubList = function(data){
	window.Mainlist({
        name: "pdwhPaperlist",
        listurl: "/pub/search/ajaxpdwhpaperlist",
        listdata: data,
        method: "scroll",
        noresultHTML:noresultHTML,
        listcallback: function(xhr) { 
            var count = $("#pdwh_paper_list").attr("total-count");
            count = !count ? 0 : count;
            if(count==0){
                $("#pubTotalCountMsg").html(count+pubCount);
            }else{
                 $("#pubTotalCountMsg").html(count+pubCounts);              
            }
            //PdwhPubSearch.ajaxrgetfulltext();
            //分享下拉模式
            if($(".share_tile").length>0){
                $(".share_tile").sharePullMode({
                    showSharePage:function(_this){
                        viewSharePub(_this);
                    },
                    'snsctx' : snsctx,
                    'styleVersion':1,
                    'isShowSmate':0,
                });             
            }
            
        }
    }); 
};
//再次点击返回
page.backSearch = function(str){
	var searchString = $.trim($("#searchString").val());
	if(str=="year"){
		$("#searchYear").val("");
	}else if(str=="type"){
		$("#searchType").val("");
	}else if(str=="language"){
		$("#searchLanguages").val("");
	}
	var language = $("#searchLanguages").val();
	var searchType = $("#searchType").val();
	var searchYear = $("#searchYear").val();
	var orderBy = $("#orderBy").val();
	var data = {
			"searchString":searchString,
			"searchPubYear":searchYear,
			"searchPubTypeId":searchType,
			"orderBy":orderBy,
			"language":language,
    		"suggestPsnName":$("#suggestStrPsn").val(),
    		"suggestPsnId":$("#suggestStrPid").val(),
    		"suggestInsName":$("#suggestStrIn").val(),
    		"suggestType":$("#suggestStrType").val(),
    		"suggestKw":$("#suggestStrKw").val(),
    		"suggestInsId":$("#suggestStrInId").val()
	};
	page.showPubList(data);
	//PdwhPubSearch.ajaxsearch("/pub/search/ajaxpaperleftmenu",data,".cont_l");
}
//点击年份查询
page.searchByYear = function(year,obj){
	$("#searchYear").val(year);
	var searchString = $.trim($("#searchString").val());
	var language = $("#searchLanguages").val();
	var searchType = $("#searchType").val();
	var orderBy = $("#orderBy").val();
	var data = {
			"searchString":searchString,
			"searchPubYear":year,
			"searchPubTypeId":searchType,
			"orderBy":orderBy,
			"language":language,
  			"suggestPsnName":$("#suggestStrPsn").val(),
  			"suggestPsnId":$("#suggestStrPid").val(),
  			"suggestInsName":$("#suggestStrIn").val(),
  			"suggestType":$("#suggestStrType").val(),
  			"suggestKw":$("#suggestStrKw").val(),
  			"suggestInsId":$("#suggestStrInId").val()
	};
	page.showPubList(data);
	//PdwhPubSearch.ajaxsearch("/pub/search/ajaxpaperleftmenu",data,".cont_l");
};
//点击类型查询
page.searchByType = function(searchType  , count){
	if(count == '0'){
		return ;
	}
	$("#searchType").val(searchType);
	var searchString = $.trim($("#searchString").val());
	var year = $("#searchYear").val();
	var language = $("#searchLanguages").val();
	var orderBy = $("#orderBy").val();
	var data = {
			"searchString":searchString,
			"searchPubYear":year,
			"searchPubTypeId":searchType,
			"orderBy":orderBy,
			"language":language,
  			"suggestPsnName":$("#suggestStrPsn").val(),
  			"suggestPsnId":$("#suggestStrPid").val(),
  			"suggestInsName":$("#suggestStrIn").val(),
  			"suggestType":$("#suggestStrType").val(),
  			"suggestKw":$("#suggestStrKw").val(),
  			"suggestInsId":$("#suggestStrInId").val()
	};
	page.showPubList(data);
	//PdwhPubSearch.ajaxsearch("/pub/search/ajaxpaperleftmenu",data,".cont_l");
};
//点击语言查询
page.searchByLanguage = function(language ,count){
	if("0" ==count){
		return ;
	}
	$("#searchLanguages").val(language);
	var searchString = $.trim($("#searchString").val());
	var year = $("#searchYear").val();
	var searchType = $("#searchType").val();
	var orderBy = $("#orderBy").val();
	var data = {
			"searchString":searchString,
			"searchPubYear":year,
			"searchPubTypeId":searchType,
			"orderBy":orderBy,
			"language":language
			
	};
	page.showPubList(data);
	//PdwhPubSearch.ajaxsearch("/pub/search/ajaxpaperleftmenu",data,".cont_l");
};
//点击排序查询
page.searchBySort = function(sort,obj){
/* 	if($(obj).hasClass("checked")){
		return;
	}
	$("span[name='typeSpan']").removeClass("checked").addClass("unchecked");
	$(obj).addClass("checked").removeClass("unchecked");
	 */
    if($(obj).hasClass("option_selected")){
        return;
    }
    
    $(obj).closest(".filter-value__list").find(".option_selected").removeClass("option_selected");
    $(obj).addClass("option_selected");
    $(obj).closest(".sort-container").find(".sort-container_header-title").html($(obj).find(".sort-container_item_name").html());
    
	
	$("#orderBy").val(sort);
	var language = $("#searchLanguages").val();
	var searchString = $.trim($("#searchString").val());
	var year = $("#searchYear").val();
	var searchType = $("#searchType").val();
	var data = {
			"searchString":searchString,
			"searchPubYear":year,
			"searchPubTypeId":searchType,
			"language":language,
			"orderBy":sort,
  			"suggestPsnName":$("#suggestStrPsn").val(),
  			"suggestPsnId":$("#suggestStrPid").val(),
  			"suggestInsName":$("#suggestStrIn").val(),
  			"suggestType":$("#suggestStrType").val(),
  			"suggestKw":$("#suggestStrKw").val(),
  			"suggestInsId":$("#suggestStrInId").val()
	};
	page.showPubList(data);
	//PdwhPubSearch.ajaxsearch("/pub/search/ajaxpaperleftmenu",data,".cont_l");
};

page.submit = function(p){
	var language = $("#searchLanguages").val();
	var searchYear = $("#searchYear").val();
	var searchType = $("#searchType").val();
	var orderBy = $("#orderBy").val();
	
	var pageSize =$("#pageSize").val();
	if(!p || !/\d+/g.test(p))
  	  	p = 1;
	var searchString = $.trim($("#searchString").val());
	var data = {"searchString":searchString ,
			    "page.pageSize":pageSize  ,
			    "searchPubYear":searchYear,
				"searchPubTypeId":searchType,
				"orderBy":orderBy,
				"language":language,
			    "page.pageNo":p,
	    		"suggestPsnName":$("#suggestStrPsn").val(),
	    		"suggestPsnId":$("#suggestStrPid").val(),
	    		"suggestInsName":$("#suggestStrIn").val(),
	    		"suggestType":$("#suggestStrType").val(),
	    		"suggestKw":$("#suggestStrKw").val(),
	    		"suggestInsId":$("#suggestStrInId").val()
			     };
	
	page.showPubList(data);
};

page.topage = function(){
	var  toPage = $.trim($("#toPage").val()) ;
	var pageSize =$("#pageSize").val();
	if(!/^\d+$/g.test(toPage))
		toPage = 1;
	
	toPage   =Number(toPage) ;
	var totalPages = Number( $("#totalPages").val()  );
	
	if(toPage > totalPages){
		toPage = totalPages ;
	}else if(toPage<1){
		toPage = 1 ;
	}
	var searchString = $.trim($("#searchString").val());
	var data = {"searchString":searchString ,
  		"suggestPsnName":$("#suggestStrPsn").val(),
  		"suggestPsnId":$("#suggestStrPid").val(),
  		"suggestInsName":$("#suggestStrIn").val(),
  		"suggestType":$("#suggestStrType").val(),
  		"suggestKw":$("#suggestStrKw").val(),
  		"suggestInsId":$("#suggestStrInId").val(),
		        "page.pageSize":pageSize  ,
		        "page.pageNo":toPage     
		     };
	page.showPubList(data);
	
};

function select_search_menu(url){
	$("#search_some_one").val($("#searchString").val());  
	$("#search_some_one_form").attr("action",url);
	$("#search_some_one_form").submit();
}

///////////////////////////////////////
var i18Share = "分享";
if ("${locale}" == "en_US") {
    i18Share = "Share";
}
$(function(){
    var orderBy = $("#orderBy").val();
    if(orderBy=="YEAR"){
            //$("#ssort").find("input[value='DEFAULT']").attr("checked","checked");
         $("#yearsort").removeClass("unchecked").addClass("checked");
         $("#defsort").removeClass("checked").addClass("unchecked");
    }
    var movelist = document.getElementsByClassName("fileupload__box-border");
    for(var i = 0; i < movelist.length; i++){
        movelist[i].onmouseenter = function(){
           this.querySelector(".fileupload__box").classList.add("upload_ready");
        }
        movelist[i].onmouseleave = function(){
           this.querySelector(".fileupload__box").classList.remove("upload_ready");
        }
    }
});

function initShare(obj){
  Pub.pdwhIsExist2($(obj).attr("resId"),function(){
    Pubsearch.getPubDetailsSareParam(obj); 
    initSharePlugin(obj);
  });
}

//初始化 分享 插件
function initSharePlugin(obj){
    if ("${locale}" == "en_US") {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
            'language' : 'en_US'
        });
    } else {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
        });
    }
    addFormElementsEvents(); 
    $("div[selector-id='list_sharetype']").find(".nav__item").eq(1).click();
};
//分享到联系人回调
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId){
	$.ajax({
        url : '/pub/opt/ajaxpdwhshare',
        type : 'post',
        dataType : 'json',
        data : {
                  'des3PdwhPubId':resId,
                  'comment':shareContent,
                  'sharePsnGroupIds':receiverGrpId,
                  'platform':"2"
               },
        success : function(data) {
        	var shareNote = $(".dev_pdwhpub_share[resid='"+resId+"']");
        	shareNote.html('<i class="icon-share"></i> '+i18Share+" ("+data.shareTimes+")");
        }
    }); 
}
//分享到群组回调
function shareGrpCallback (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, dyntype, resType){
   $.ajax({
       url : '/dynweb/dynamic/ajaxsharecount',
       type : 'post',
       dataType : 'json',
       data : {'des3ResId':resId,
           'des3DynId':des3DynId,
           'resTypeStr':resType,
            'dynType':dyntype    
              },
       success : function(data) {
           changeShareNum(resId);
       }
   });     
}
//分享到动态回调(只需改变页面分享统计数即可)
function shareCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId,resType,dbId){
	changeShareNum(resId);
};
function changeShareNum(resId){
    var shareNote = $(".dev_pdwhpub_share[resid='"+resId+"']");
    var count = Number(shareNote.text().replace(/[\D]/ig,""))+1;
    shareNote.html('<i class="icon-share"></i> '+i18Share+" ("+count+")");
}
//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        $(obj).find('.new-Standard_Function-bar_item').addClass('new-Standard_Function-bar_selected');
        $(obj).find('.new-Standard_Function-bar_item-title:first').text(Pubsearch.unsave);
    }else{
    	$(obj).find('.new-Standard_Function-bar_item').removeClass('new-Standard_Function-bar_selected');
    	$(obj).find('.new-Standard_Function-bar_item-title:first').text(Pubsearch.save);
    }
}
</script>
</head>
<body style="overflow: hidden;">
  <input id="searchString" type="hidden" value="${searchString }" />
  <input id="searchYear" type="hidden" value="" />
  <input id="searchLanguages" type="hidden" value="" />
  <input id="searchType" type="hidden" value="" />
  <input id="orderBy" type="hidden" value="DEFAULT" />
  <input id="suggestStrType" type="hidden" value="${suggestStrType=='undefined'?'':suggestStrType}" />
 <input id="suggestStrPsn" type="hidden" value="${suggestStrPsn=='undefined'?'':suggestStrPsn}" />
  <input id="suggestStrPid" type="hidden" value="${suggestStrPid=='undefined'?'':suggestStrPid}" />
  <input id="suggestStrIn" type="hidden" value="${suggestStrIn=='undefined'?'':suggestStrIn}" />
  <input id="suggestStrInId" type="hidden" value="${suggestStrInsId=='undefined'?'':suggestStrInsId}" />
  <input id="suggestStrKw" type="hidden" value="${suggestStrKw=='undefined'?'':suggestStrKw}" />
  <div class="result_class">
    <div class="result_class_wrap">
      <ul>
        <li id="search_paper" class="cur" onclick="select_search_menu('/pub/search/pdwhpaper');"><a
          href="javascript:void(0);"><spring:message code='pub.menu.search.paper' /></a></li>
        <li id="search_patent" onclick="select_search_menu('/pub/search/pdwhpatent');"><a
          href="javascript:void(0);"><spring:message code='pub.menu.search.patent' /></a></li>
        <li id="search_person" onclick="select_search_menu('/pub/search/psnsearch');"><a href="javascript:void(0);"><spring:message
              code='pub.menu.search.person' /></a></li>
        <li id="search_ins" onclick="select_search_menu('/prjweb/outside/agency/searchins');"><a href="javascript:void(0);"><spring:message
              code="ins.serach.title" /></a></li>
        <!-- <li><a href="#">基金1</a></li>
        <li><a href="#">期刊</a></li> -->
      </ul>
    </div>
  </div>
  <div id="content" style="display: flex; padding: 0px;">
    <div class="cont_l" style="width: 280px;"></div>
    <div class="cont_r" style="overflow-x: hidden; max-width: 880px;">
      <div class="sorting_box" id="sortingbox" style="display: flex; justify-content: space-between; align-items: center;">
          <span class="f999"> 
              <spring:message code='pub.menu.search.resultPre' /> 
              <span id="pubTotalCountMsg"></span>
          </span> 
          
         <%--  <span class='checked  fr' id="defsort" name='typeSpan' checked='false' onclick="page.searchBySort('DEFAULT',this)">
              <spring:message code='pub.menu.list.relate' />
          </span> 
          <span id="yearsort" class='unchecked  fr' name='typeSpan' checked='false' onclick="page.searchBySort('YEAR',this)">
              <spring:message code='pub.menu.list.pubYear' />
          </span>  --%>
          
          
         <div class="sort-container js_filtersection" style="margin-right: 20px;">
            <div class="sort-container_header" style="width: 130px;">
              <div class="sort-container_header-tip">
                <i class="sort-container_header-flag sort-container_header-up"></i><i class="sort-container_header-down"></i>
              </div>
              <div class="sort-container_header-title filter-section__title" style="width: 105px;">
                  <spring:message code='pub.menu.list.relate' />
              </div>
            </div>
            <div class="sort-container_item" style="width: 128px!important;">
              <div class="filter-list vert-style option_has-stats" list-filter="psnpub">
                <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory" style="margin: 0; padding: 0;">
                  <ul class="filter-value__list">
                    <li class="filter-value__item js_filtervalue sort-container_item-list option_selected" style="padding: 0px;" onclick="page.searchBySort('DEFAULT',this)" >
                      <div class="filter-value__option js_filteroption sort-container_item_name" style="font-size: 14px !important; margin: 0px -10px; padding-left: 34px;">
                           <spring:message code='pub.menu.list.relate' />
                      </div>
                      <div class="filter-value__stats js_filterstats"></div>
                    </li>
                    <li class="filter-value__item js_filtervalue sort-container_item-list" style="padding: 0px;" onclick="page.searchBySort('YEAR',this)">
                      <div class="filter-value__option js_filteroption sort-container_item_name"  style="font-size: 14px !important; margin: 0px -10px; padding-left: 34px;">
                         <spring:message code='pub.menu.list.pubYear' />
                      </div>
                      <div class="filter-value__stats js_filterstats"></div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
  
  
  
  
      </div>
      <!-- 论文列表 -->
      <div class="main-list__list" id='pdwh_paper_list' list-main='pdwhPaperlist' style="min-height: 700px; margin-bottom: 100px;"></div>
      <!-- 论文列表 -->
      <jsp:include page="/common/smate.share.mvc.jsp" />
      <!-- 分享操作 -->
      <div><%@ include file="/skins_mvc/footer_infor.jsp"%></div>
      <!--           </div> -->
    </div>
  </div>
  </div>
</body>
</html>
