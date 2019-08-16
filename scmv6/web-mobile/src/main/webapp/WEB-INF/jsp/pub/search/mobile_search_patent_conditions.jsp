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

//选中排序条件
function selectOrderBy(obj){
    dealSelectStatus(obj, "selector__list-target");
    $(obj).siblings().removeClass("selector__list-target");
    $("#orderBy").val($(".pub_order_by_conditions .selector__list-target:last").attr("value"));
}

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
function findPatents(){
    $("#find_patent_search").submit();
}

//处理条件上次选中的状态
function dealWithConditionsStatus(){
        searchPubType = $("#searchPubType").val(),
        searchPubYear = $("#searchPubYear").val(),
        orderBy = $("#orderBy").val();
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
    if(orderBy == "DEFAULT" || orderBy == "" || orderBy == null){
        dealSelectStatus($("#order_by_default"), "selector__list-target");
    }else{
        dealSelectStatus($("#order_by_year"), "selector__list-target");
    }
}
</script>
</head>
<body>
  <form id="find_patent_search" method="post" action="/pub/patent/search">
    <input type="hidden" id="scienceAreaIds" name="des3AreaId" value="${model.des3AreaId}" /> <input type="hidden"
      id="searchPubYear" name="publishYear" value="${model.publishYear}" /> <input type="hidden" id="searchPubType"
      name="searchPubType" value="${model.searchPubType}" /> <input type="hidden" id="searchString" name="searchString"
      value="${model.searchString }" /> <input type="hidden" id="orderBy" name="orderBy" value="${model.orderBy }" />
  </form>
  <div class="provision_container-title">
    <!-- <a href="javascript:window.history.back();"> -->
    <i class="material-icons" onclick="window.history.back();">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">排序</span>
    </div>
    <div class="provision_container-body_item pub_order_by_conditions">
      <div class="provision_container-body_item-list div_item type_time" onclick="selectOrderBy(this);"
        id="order_by_default" value="DEFAULT">相关性</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="selectOrderBy(this);"
        id="order_by_year" value="YEAR">发表年份</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">出版年份</span>
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
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">专利类型 </span>
    </div>
    <div class="provision_container-body_item pub_type">
      <div class="provision_container-body_item-list div_item" id="pub_type_51" onclick="selectPubType(this);"
        value="51">发明专利</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_52" onclick="selectPubType(this);"
        value="52">实用新型</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_53" onclick="selectPubType(this);"
        value="53">外观设计</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_7" onclick="selectPubType(this);" value="7">其他</div>
    </div>
  </div>
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findPatents();" style="width: 100%">确定</div>
  </div>
</body>
</html>
