<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/mobile_pub_recommend.js"></script>
<script type="text/javascript">

$(function(){
    dealWithConditionsStatus();
});

//选中成果类型
function selectPubType(obj){
    dealSelectStatus(obj, "selector__list-target");
    var pubTypeArr = new Array();
    $(".pub_type .selector__list-target").each(function(){
        pubTypeArr.push($.trim($(this).attr("value")));
    });
    $("#searchPubType").val(pubTypeArr.join(","));
}

//选中成果年份
function selectPubYear(obj){
    dealSelectStatus(obj, "selector__list-target");
    $(obj).siblings().removeClass("selector__list-target");
    $("#searchPubYear").val($(".pub_time .selector__list-target:last").attr("value"));
}

//选中收录情况-----value待改为id形式
function selectPubDB(obj){
    dealSelectStatus(obj, "selector__list-target");
    var pubDBArr = new Array();
    $(".pub_db .selector__list-target").each(function(){
        pubDBArr.push($.trim($(this).attr("value")));
    });
    $("#includeType").val(pubDBArr.join(","));
}
/* function selectLanguage(obj){
  var language = $(obj).attr("value");
  $("#searchLanguage").val(language);
  dealSelectStatus(obj, "selector__list-target");
  $(obj).siblings().removeClass("selector__list-target");
} */
function selectOrderBy(obj){
  var orderBy = $(obj).attr("value");
  $("#orderBy").val(orderBy);
  dealSelectStatus(obj, "selector__list-target");
  $(obj).siblings().removeClass("selector__list-target");
}
//处理选中效果
function dealSelectStatus(obj, className){
    var $this = $(obj);
    if($this.hasClass(className)){
        $this.removeClass(className);
    }else{
        $this.addClass(className);
    }
}

//确定按钮事件
function findPubs(){
    $("#find_pub_search").submit();
}

//处理条件上次选中的状态
function dealWithConditionsStatus(){
    var pubDBIds = $("#includeType").val(),
        searchPubType = $("#searchPubType").val(),
        searchPubYear = $("#searchPubYear").val(),
        //searchLanguage = $("#searchLanguage").val();
        orderBy = $("#orderBy").val();
    if(pubDBIds != null && pubDBIds != ""){
        var dbArr = pubDBIds.split(",");
        for(var i=0; i<dbArr.length; i++){
            var selector = "#";
            if(dbArr[i] == "北大核心"){
                selector += "bdhx_condition";
            }else{
                selector += dbArr[i] + "_condition";
            }
            var obj = $(selector);
            dealSelectStatus(obj, "selector__list-target");
        }
    }
    if(searchPubType != null && searchPubType != ""){
        var typeArr = searchPubType.split(",");
        for(var i=0; i<typeArr.length; i++){
            var selector = "#pub_type_" + typeArr[i];
            var obj = $(selector);
            dealSelectStatus(obj, "selector__list-target");
        }
    }
    if(searchPubYear != null && searchPubYear != ""){
        var yearArr = searchPubYear.split(",");
        var selector = "#pub_year_" + yearArr[yearArr.length - 1];
        var obj = $(selector);
        dealSelectStatus(obj, "selector__list-target");
    }else{
        dealSelectStatus($("#pub_every_year"), "selector__list-target");
    }
/*     if(searchLanguage != null && searchLanguage != ""){
      var obj = $(".type_language[value='"+searchLanguage+"']");
      dealSelectStatus(obj, "selector__list-target");
    }else{
      dealSelectStatus($(".type_language[value='0']"), "selector__list-target");
    } */
    if(orderBy!=null && orderBy != ""){
      var obj = $(".type_orderBy[value='"+orderBy+"']");
      dealSelectStatus(obj, "selector__list-target");
    }else{
      dealSelectStatus($(".type_orderBy[value='readCount']"), "selector__list-target");
    }
}
</script>
</head>
<body>
  <form id="find_pub_search" method="post" action="/pub/find/main">
    <input type="hidden" id="scienceAreaIds" name="des3AreaId" value="${model.des3AreaId}" /> 
    <input type="hidden" id="searchPubYear" name="publishYear" value="${model.publishYear}" /> 
    <input type="hidden" id="searchPubType" name="searchPubType" value="${model.searchPubType}" /> 
    <input type="hidden" id="searchString" name="searchString" value="${model.searchString }" /> 
    <input type="hidden" id="includeType" name="includeType" value="${model.includeType }" />
    <input type="hidden" id="areaName" name="areaName" value="${model.areaName }" />         
<%--     <input type="hidden" id="searchLanguage" name="searchLanguage" value="${model.searchLanguage }" />
 --%>    <input type="hidden" id="orderBy" name="orderBy" value="${model.orderBy }" />
  </form>
  <div class="provision_container-title">
    <!-- <a href="javascript:window.history.back();"> -->
    <i class="material-icons" onclick="window.history.back();">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置发现条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>成果类型</h3></span>
    </div>
    <div class="provision_container-body_item pub_type">
      <div class="provision_container-body_item-list div_item" id="pub_type_4" onclick="selectPubType(this);" value="4">期刊论文</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_3" onclick="selectPubType(this);" value="3">会议论文</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_5" onclick="selectPubType(this);" value="5">专利</div>      
      <div class="provision_container-body_item-list div_item" id="pub_type_7" onclick="selectPubType(this);" value="7">其他</div>
    </div>
  <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>收录类别</h3></span>
    </div>
    <div class="provision_container-body_item pub_db">
      <div class="provision_container-body_item-list div_item type_time" id="14_condition" onclick="selectPubDB(this);"
        value="14">EI</div>
      <div class="provision_container-body_item-list div_item type_time" id="16_condition" onclick="selectPubDB(this);"
        value="16">SCIE</div>
      <div class="provision_container-body_item-list div_item type_time" id="17_condition" onclick="selectPubDB(this);"
        value="17">SSCI</div>
      <div class="provision_container-body_item-list div_item type_time" id="15_condition" onclick="selectPubDB(this);"
        value="15">ISTP</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>出版年份</h3></span>
    </div>
    <div class="provision_container-body_item pub_time">
      <div class="provision_container-body_item-list div_item type_time" onclick="selectPubYear(this);"
        id="pub_year_${model.currentYear }" value="${model.currentYear }">${model.currentYear }年以来</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="selectPubYear(this);"
        id="pub_year_${model.currentYear - 2 }"
        value="${model.currentYear },${model.currentYear - 1},${model.currentYear - 2}">${model.currentYear - 2 }年以来</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="selectPubYear(this);"
        id="pub_year_${model.currentYear - 4 }"
        value="${model.currentYear },${model.currentYear - 1},${model.currentYear - 2},${model.currentYear - 3},${model.currentYear - 4}">${model.currentYear - 4 }年以来</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="selectPubYear(this);"
        id="pub_every_year" value="">不限</div>
    </div>
<!--     <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">语言</span>
    </div>
    <div class="provision_container-body_item pub_db">
      <div class="provision_container-body_item-list div_item type_language"  onclick="selectLanguage(this);"
        value="zh_CN">中文</div>
      <div class="provision_container-body_item-list div_item type_language" onclick="selectLanguage(this);"
        value="en_US">外文</div>
      <div class="provision_container-body_item-list div_item type_language" onclick="selectLanguage(this);"
        value="0">不限</div>
    </div> -->
    
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor"><h3>排序</h3></span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list div_item type_orderBy"  onclick="selectOrderBy(this);"
        value="readCount">阅读数</div>
      <div class="provision_container-body_item-list div_item type_orderBy" onclick="selectOrderBy(this);"
        value="downLoadCount">下载数</div>
      <div class="provision_container-body_item-list div_item type_orderBy" onclick="selectOrderBy(this);"
        value="pubCitations">引用数</div>
      <div class="provision_container-body_item-list div_item type_orderBy" onclick="selectOrderBy(this);"
        value="pubYear">发表年份</div>
    </div>
    
  </div>
  
    
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findPubs();" style="width: 100%">确定</div>
  </div>
</body>
</html>
