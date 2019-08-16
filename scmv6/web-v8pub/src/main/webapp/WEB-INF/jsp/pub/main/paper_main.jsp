<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>论文</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod}/smate-pc/new-mypaper/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link href="/resmod/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod }/js/search/pub_search_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/search/pub_search.js"></script>
<script type="text/javascript" src="${resmod}/js/search/PdwhPubSearch.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/homepage/psn.homepage.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/pub_recommend_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/pub_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript">
$(function(){
  var toPaperType = $("#toPaperType").val();
  var url = "/pub/ajaxpubrecommendmain";//默认论文推荐
  if(toPaperType && toPaperType!=""){
    getPage(toPaperType);
  }else{
    changeModel(url);
   }
  
  var targetlist = document.getElementsByClassName("paper-title_container-item");
  var targetele = document.getElementsByClassName("paper-title_container-underline")[0];
  for(var i = 0; i < targetlist.length; i++){
      targetlist[i].onmousedown = function(){
          targetele.style.left =  this.offsetLeft + "px";
          targetele.style.width = this.offsetWidth + "px";
      }
  }
  
  var headerlist = document.getElementsByClassName("paper-title_container");
  var total = document.getElementsByClassName("header__box")[0].offsetWidth;
  var parentleft = document.getElementsByClassName("header__nav")[0].offsetLeft;
  var subleft  = document.getElementsByClassName("header-nav__item-bottom")[0].offsetWidth;
  for(var i = 0 ; i < headerlist.length; i++){
     //headerlist[i].style.left = total - 380 - parentleft - subleft + "px"; 
    headerlist[i].style.right = 169 + "px"; 
  }
  var setheight = window.innerHeight - 180 - 95;
  var contentlist = document.getElementsByClassName("content-details_container");
  for(var i = 0; i < contentlist.length; i++){
      contentlist[i].style.minHeight = setheight + "px";
  }
  
  if(document.getElementsByClassName("header__nav")){
      document.getElementById("num2").style.display="flex";
      document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num1"));
  }
  if(document.getElementById("search_some_one")){
      document.getElementById("search_some_one").onfocus = function(){
          this.closest(".searchbox__main").style.borderColor = "#2882d8";
      }
      document.getElementById("search_some_one").onblur = function(){
          this.closest(".searchbox__main").style.borderColor = "#ccc";
      }
  }
  
  var sortbtn = document.getElementsByClassName("sort_item")[0];//收藏排序
  var slectlist = document.getElementsByClassName("sort_item-list");
  sortbtn.onmouseover =function(){
    this.closest(".sort_item").querySelector(".sort_item-container").style.display="block";
  }
  sortbtn.onmouseleave  =function(){
    this.closest(".sort_item").querySelector(".sort_item-container").style.display="none";
  } 
  for(var k = 0; k<slectlist.length;k++){
    slectlist[k].onclick = function(){
      this.closest(".sort_item-box").querySelector(".sort_item-title").innerHTML = this.innerHTML; 
    }
  }
  
  $(".header-nav__list>li[data-class!='mine-application']").removeClass("header-nav__item-selected"); 
  $("li[data-class='mine-application']").addClass("header-nav__item-selected"); 
  $(".header-nav__item-bottom").attr("style", "width: 68px; left: 291px;"); 


});
function toPubRecommend(){//切换到论文推荐
  $(".paper-title_container-item").removeClass("paper-title_container-item_selected").removeClass("cur");
  $("#recommend_li").addClass("paper-title_container-item_selected").addClass("cur");
  $(".sort_item").hide();
  var url="/pub/ajaxpubrecommendmain";
  history.replaceState({},0,"?toPaperType=recommend");
  changeModel(url);
}
function toFindPub(){//切换到论文发现
  $(".paper-title_container-item").removeClass("paper-title_container-item_selected").removeClass("cur");
  $("#find_li").addClass("paper-title_container-item_selected").addClass("cur");
  $(".sort_item").hide();
  var url="/pub/ajaxfindpubmain";
  history.replaceState({},0,"?toPaperType=find");
  changeModel(url);
}
function toCollectedPub(){//切换到论文收藏
  $(".paper-title_container-item").removeClass("paper-title_container-item_selected").removeClass("cur");
  $("#collect_li").addClass("paper-title_container-item_selected").addClass("cur");
  $(".sort_item").show();
  var url="/pub/ajaxpapermain";
  history.replaceState({},0,"?toPaperType=collect"); 
  changeModel(url); 
}
function changeModel(url){//切换页面
  $.ajax({
    url:url,
    data:{},
    type:"post",
    dataType:"html",
    success:function(data){
      BaseUtils.ajaxTimeOut(data, function() {
        $("#content").html("");
        $("#content").html(data);
      });
    },
    error:function(data){
      
    }
  });
}
function getPage(toPaperType){//执行页面切换，并执行切换动画
  switch(toPaperType){
    case 'recommend':
      url="/pub/ajaxpubrecommendmain";
      toPubRecommend();
      break;
    case 'find':
      url='/pub/ajaxfindpubmain';
      toFindPub();
      break;
    case 'collect':
      url='/pub/ajaxpapermain';
      toCollectedPub();
      break;
   default:
      url='/pub/ajaxpubrecommendmain';
   	toPubRecommend();
      break;
  }
}
function initShare(des3PubId,obj){
  Pub.pdwhIsExist2(des3PubId,function (){
    Pub.getPdwhPubSareParam(obj); 
    initSharePlugin(obj)
  });
}
</script>
</head>
<body>
  <input type="hidden" id="toPaperType" value="${toPaperType}">
  <div class="result-class01 page_header-style" style="position: fixed; top: 48px; z-index: 20;">
    <div class="result-class__wrap" style="justify-content: flex-end; display: flex; position: relative;">
      <nav class="paper-title_container" style="position: absolute;">
        <ul>
          <li id="recommend_li" class="cur paper-title_container-item  paper-title_container-item_selected" onclick="toPubRecommend();">
             <a href="javascript:void(0);"><spring:message code="pub.main.suggestpaper" /></a>
          </li>
          <li id="find_li" class="paper-title_container-item" onclick="toFindPub();">
             <a href="javascript:void(0);"><spring:message code="pub.main.findpub" /></a>
          </li>                
          <li id="collect_li" class="paper-title_container-item" onclick="toCollectedPub();">
             <a href="javascript:void(0);"><spring:message code="pub.main.mypaper" /></a>
          </li>
        </ul>
        <div class="paper-title_container-underline setting-list_page-item_hidden"></div>
      </nav>
      <div class="sort_item setting-list_page-item_hidden" style="display: none; margin-top: 10px;">
          <i class="material-icons click-sort_btn" style="cursor: pointer; margin: 0px; margin-top: 2px">swap_vert</i>
           <div class="sort_item-box" style="margin-right: 8px; position: relative; width: 120px;">
            <div class="sort_item-title filter-section__title" style="width: 100%; text-align: center;">
              <spring:message code="pub.filter.sortby" />
            </div>
            <c:if test='${locale!="en_US" }'>
              <div class="filter-list sort_item-container" list-filter="paperlist"
                style="width: 130px !important; display: none; left: -23px;top: 24px;">
            </c:if>
            <c:if test='${locale=="en_US" }'>
              <div class="filter-list sort_item-container" list-filter="paperlist"
                style="width: 130px !important; display: none; left: -23px;">
            </c:if>
            <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory">
              <ul class="filter-value__list">
                <li class="filter-value__item" filter-value="collectDate">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort_item-list">
                    <spring:message code="pub.filter.sortby.collectDate" />
                  </div>
                </li>
                <li class="filter-value__item" filter-value="citedTimes">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort_item-list">
                    <spring:message code="pub.filter.sortby.cite" />
                  </div>
                </li>
                <li class="filter-value__item" filter-value="readTimes">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort_item-list">
                    <spring:message code="pub.filter.sortby.read" />
                  </div>
                </li>
                <li class="filter-value__item" filter-value="publishYear">
                  <div class="input-custom-style">
                    <input type="checkbox"> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="filter-value__option js_filteroption sort_item-list">
                    <spring:message code="pub.filter.sortby.publish" />
                  </div>
                </li>
              </ul>
            </div>
           </div>
          </div>
        </div>
    </div>
  </div>
  <div class="dialogs__box" dialog-id="scienceAreaBox" cover-event="hide" style="width: 720px;" id="scienceAreaBox"></div>
  <div id="keywordsModule"></div>
  <div class="clear_h20"></div>
  <div id="content" style="margin-top:35px;display: flex; justify-content: center; width: 1200px;"></div>
  
  <div><%@ include file="/skins_mvc/footer_infor.jsp"%></div> 
  
</body>
</html>