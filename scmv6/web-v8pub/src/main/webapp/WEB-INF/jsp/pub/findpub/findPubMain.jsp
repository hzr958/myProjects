<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>论文发现</title>
<link href="${resmod}/smate-pc/new-mypaper/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link href="/resmod/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">

<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/findpub/find_pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/findpub/find_pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod }/js/search/pub_search_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/search/pub_search.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<style  type="text/css">
  body{
     overflow: auto!important;
  }
</style>
<script type="text/javascript">
<c:set var="userData.menuId" value="5"></c:set>
$(function(){
  
  FindPub.showLeftConditions();
  if(document.getElementById("search_some_one")){
      document.getElementById("search_some_one").onfocus = function(){
          this.closest(".searchbox__main").style.borderColor = "#2882d8";
      }
      document.getElementById("search_some_one").onblur = function(){
          this.closest(".searchbox__main").style.borderColor = "#ccc";
      }
  } 
  $("#findPubSearchKey").keydown(function(e) { //回车键检索
    e = window.event || e; 
    if (e.keyCode == 13) { 
      FindPub.ajaxLoadePubList();
    }
  });
  $(".sort-container_item-list").click(function(){//排序
    if($(this).closest(".filter-value__list").find(".filter-value__item-selected")){
        $(this).closest(".filter-value__list").find(".filter-value__item-selected").removeClass("filter-value__item-selected")
    }
    $(this).closest("li").addClass("filter-value__item-selected");
    var text = $.trim($(this).text());
    var value=$(this).attr("code");
    $("#findPubOrderBy").find("span").text(text);
    $("#findPubOrderBy").attr("value",value);
    FindPub.ajaxLoadePubList();
    
  });
});

function initLeftCondition(){
  var keylist = document.getElementsByClassName("new-mainpage_left-list_header-onkey");
  var closelist = document.getElementsByClassName("new-mainpage_left-sublist_header");
  var selectlist = document.getElementsByClassName("new-mainpage_left-sublist_body-item");
  var heightlist = document.getElementsByClassName("new-mainpage_scroll-height");
  for(var i = 0; i < heightlist.length; i++){
      heightlist[i].style.height = window.innerHeight - 96 - 30 + "px";
      heightlist[i].style.minHeight = window.innerHeight - 96 - 30 + "px";
  }
  for(var i = 0; i < keylist.length; i++){
      keylist[i].onclick = function(){
          if(this.innerHTML == "expand_less"){
              this.innerHTML = "expand_more";
              if(this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body")){
                  this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body").style.display = "none";
              }
          }else{
              this.innerHTML = "expand_less";
              if(this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body")){
                  this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body").style.display = "block";
              }
          }
      }
  }
  for(var i = 0;i < closelist.length; i++){
      closelist[i].onclick = function(){
          var thisli = this.querySelector(".new-mainpage_left-sublist_header-onkey")
          if(thisli.classList.contains("new-mainpage_left-sublist_header-open")){
            thisli.classList.remove("new-mainpage_left-sublist_header-open");
            thisli.closest(".new-mainpage_left-sublist").querySelector(".new-mainpage_left-sublist_body").style.display = "none";
          }else{
            thisli.classList.add("new-mainpage_left-sublist_header-open");
            thisli.closest(".new-mainpage_left-sublist").querySelector(".new-mainpage_left-sublist_body").style.display = "block";
          }
      }
  }

  for(var i = 0; i < selectlist.length; i++){
     selectlist[i].onclick = function(){ 
         if(this.closest(".new-mainpage_left-list_body").classList.contains("new-mainpage_left-sublist_body-single")){

             if(this.closest(".new-mainpage_left-list_body").querySelector(".new-mainpage_left-sublist_body-item_selected")){
                  if(this.classList.contains("new-mainpage_left-sublist_body-item_selected")){
                      this.classList.remove("new-mainpage_left-sublist_body-item_selected");
                  }else{
                      this.closest(".new-mainpage_left-list_body").querySelector(".new-mainpage_left-sublist_body-item_selected").classList.remove("new-mainpage_left-sublist_body-item_selected");
                      this.classList.add("new-mainpage_left-sublist_body-item_selected");
                  }
             }else{
                 this.classList.add("new-mainpage_left-sublist_body-item_selected");
             }
         }else{
             if(this.classList.contains("new-mainpage_left-sublist_body-item_selected")){
                 this.classList.remove("new-mainpage_left-sublist_body-item_selected");
             }else{
                 this.classList.add("new-mainpage_left-sublist_body-item_selected");
             }
         }
         FindPub.ajaxLoadePubList();
     }
  }
  
  document.getElementsByClassName("new-mainpage_container")[0].style.left = (window.innerWidth- 1200)/2 + "px";
  window.onresize = function(){
      if((window.innerWidth- 1200)>0){
          document.getElementsByClassName("new-mainpage_container")[0].style.left = (window.innerWidth- 1200)/2 + "px";
      }else{
          document.getElementsByClassName("new-mainpage_container")[0].style.left = 0 + "px";
      }
  }
  $("div[name='publishYear']").find(".new-mainpage_left-sublist_body-item").each(function(){
    var title = $(this).find(".new-mainpage_left-sublist_body-detail").text();
    $(this).attr("title",$.trim(title));
  });
};

function initShare(des3PubId,$this){
  Pub.pdwhIsExist2(des3PubId,function(){
    FindPub.getPubDetailsSareParam($this); 
    initSharePlugin($this);
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
          var shareCount = data.shareTimes;
          if(shareCount>999){
            shareCount = "1k+";
          }
            var shareNote = $(".dev_pdwhpub_share[resid='"+resId+"']");
            shareNote.html('<i class="icon-share"></i> '+Pubsearch.share+" ("+shareCount+")");
        }
    }); 
};
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
};
//分享到动态回调(只需改变页面分享统计数即可)
function shareCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId,resType,dbId){
    changeShareNum(resId); 
};
function changeShareNum(resId){
    var shareNote = $(".dev_pdwhpub_share[resid='"+resId+"']");
    var count = Number(shareNote.text().replace(/[\D]/ig,""))+1;
    if(count>999){
      count = "1k+";
    }
    shareNote.html('<i class="icon-share"></i> '+Pubsearch.share+" ("+count+")");
};
//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        var html = "<div class='new-Standard_Function-bar_item new-Standard_Function-bar_selected'>"
               +"<i class='new-Standard_function-icon new-Standard_Save-icon'></i>"
               +"<span class='new-Standard_Function-bar_item-title'>"+Pubsearch.unsave+"</span></div>";
        $(obj).html(html);  
    }else{
        var html = "<div class='new-Standard_Function-bar_item'>"
            +"<i class='new-Standard_function-icon new-Standard_Save-icon'></i>"
            +"<span class='new-Standard_Function-bar_item-title'>"+Pubsearch.save+"</span></div>";
        $(obj).html(html);
    }
};

</script>
</head>
<body>  
<jsp:include page="/common/smate.share.mvc.jsp" />
  <div class="new-mainpage_container" style="margin-top:0px; display: block;">
    <div style="display: flex;">
    <div class="new-mainpage_left-container" id="left_condition" style="width: 235px; border-right: 1px solid #ddd;">
    </div>
    <div class="new-mainpage_right-container" style=" margin-right: -20px; overflow: visible; border: none;">
            <div class="new-mainpage_right-container_search" style="justify-content: space-between;">
                <div style="display: flex; align-items: center;">
                    <div class="new-mainpage_right-container_search-box">
                        <input type="text" id="findPubSearchKey" name="findPubSearchKey" placeholder="<spring:message code='pub.find.search.msg'/>">
                        <i class="searchbox__icon" onclick="FindPub.ajaxLoadePubList();"></i>
                    </div>
                    <div class="new-mainpage_right-container_search-btn" onclick="FindPub.ajaxLoadePubList();"><spring:message code="referencesearch.button.search"/></div>
                </div>
                <div class="sort-container">
                    <div class="sort-container_header" style=" width: 150px;">
                        <div class="sort-container_header-tip">
                            <i class="sort-container_header-flag sort-container_header-up"></i><i class="sort-container_header-down"></i>
                        </div>
                        <div class="sort-container_header-title sort-container_header-limit" id="findPubOrderBy" select_sen="false" value="readCount">
                           <span><spring:message code="pub.find.reads"/></span>                                     
                        </div>
                    </div>
                    <div class="sort-container_item" style="; z-index: 999; width: 150px!important;">
                      <div class="filter-list vert-style option_has-stats">
                        <div class="filter-list__section js_filtersection" style="margin: 0; padding: 0;">
                          <ul class="filter-value__list">
                            <li class="filter-value__item js_filtervalue sort-container_item-list" code="readCount" style="padding: 0px; margin:0px;">
                              <div class="filter-value__option js_filteroption sort-container_item_name" style="font-size: 14px !important;padding-left: 25px;">
                                 <span><spring:message code="pub.find.reads"/></span> 
                              </div>
                            </li>
                            <li class="filter-value__item js_filtervalue sort-container_item-list" code="downLoadCount" style="padding: 0px; margin:0px;">
                              <div class="filter-value__option js_filteroption sort-container_item_name" style="font-size: 14px !important; padding-left: 25px;">
                                 <span><spring:message code="pub.find.downloads"/></span>   
                              </div>
                            </li>
                            <li class="filter-value__item js_filtervalue sort-container_item-list" code="pubCitations" style="padding: 0px; margin:0px;">
                              <div class="filter-value__option js_filteroption sort-container_item_name dev_seniority_unlimit" style="font-size: 14px !important;  padding-left: 25px;">
                                 <span><spring:message code="pub.filter.recentCitations"/> </span> 
                              </div>
                            </li>
                            <li class="filter-value__item js_filtervalue sort-container_item-list" code="pubYear" style="padding: 0px; margin:0px;">
                              <div class="filter-value__option js_filteroption sort-container_item_name dev_seniority_unlimit" style="font-size: 14px !important;  padding-left: 25px;">
                                 <span><spring:message code="pub.filter.recentPublish"/></span>           
                              </div>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                </div>
            </div>
            <div class="new-mainpage_right-container-scroll" style="height:auto;">
              <div class="main-list__list new-mainpage_right-item" list-main="pub_list" style="min-height: 1200px; height: auto;"></div>
              <div class="main-list__footer">
                <div class="pagination__box" list-pagination="pub_list" style="display: flex; justify-content: center; width: 960px;">
                  <!-- 翻页 -->
                </div>
              </div>
            </div>
       </div>
       </div>
        <%-- <div><%@ include file="/skins_mvc/footer_infor.jsp"%></div> --%>
    </div>
</body>
</html>
