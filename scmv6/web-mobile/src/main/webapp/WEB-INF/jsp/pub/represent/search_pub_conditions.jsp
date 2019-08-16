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
    $("#pubType").val(pubTypeArr.join(","));
}

//选中成果年份
function selectPubYear(obj){
    dealSelectStatus(obj, "selector__list-target");
    $(obj).siblings().removeClass("selector__list-target");
    $("#publishYear").val($(".pub_time .selector__list-target:last").attr("value"));
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
        pubType = $("#pubType").val(),
        searchPubYear = $("#publishYear").val(),
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
    if(pubType != null && pubType != ""){
        var typeArr = pubType.split(",");
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
    if(orderBy == "DEFAULT"){
        dealSelectStatus($("#order_by_default"), "selector__list-target");
    }else{
        dealSelectStatus($("#order_by_year"), "selector__list-target");
    }
}
</script>
</head>
<body>
  <form id="find_pub_search" method="get" action="/pub/represent/addenter">
    <input type="hidden" id="orderBy" name="orderBy" value="${model.orderBy}" /> 
    <input type="hidden" id="publishYear" name="publishYear" value="${model.publishYear}" /> 
    <input type="hidden" id="pubType" name="pubType" value="${model.pubType}" /> 
    <input type="hidden" id="searchKey" name="searchKey" value="${model.searchKey }" /> 
    <input type="hidden" id="includeType" name="includeType" value="${model.includeType }" /> 
  </form>
  <div class="provision_container-title">
    <i class="material-icons" onclick="window.history.back();">keyboard_arrow_left</i>
    <span>设置条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">排序</span>
    </div>
    <div class="provision_container-body_item pub_order_by_conditions">
      <div class="provision_container-body_item-list div_item type_time" onclick="selectOrderBy(this);"
        id="order_by_default" value="gmtModified">最新修改</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="selectOrderBy(this);"
        id="order_by_year" value="pubLishDate">最新发表</div>
      <div class="provision_container-body_item-list div_item type_time" onclick="selectOrderBy(this);"
        id="order_by_year" value="citations">最多引用</div>
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
      <span class="provision_container-body_title-infor">收录</span>
    </div>
    <div class="provision_container-body_item pub_db">
      <div class="provision_container-body_item-list div_item type_time" id="ei_condition" onclick="selectPubDB(this);"
        value="ei">EI</div>
      <div class="provision_container-body_item-list div_item type_time" id="scie_condition" onclick="selectPubDB(this);"
        value="scie">SCIE</div>
      <div class="provision_container-body_item-list div_item type_time" id="ssci_condition" onclick="selectPubDB(this);"
        value="ssci">SSCI</div>
      <div class="provision_container-body_item-list div_item type_time" id="istp_condition" onclick="selectPubDB(this);"
        value="istp">ISTP</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">成果类型 </span>
    </div>
    <div class="provision_container-body_item pub_type">
      <div class="provision_container-body_item-list div_item" id="pub_type_4" onclick="selectPubType(this);" value="4">期刊论文</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_3" onclick="selectPubType(this);" value="3">会议论文</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_5" onclick="selectPubType(this);" value="5">专利</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_1" onclick="selectPubType(this);" value="1">奖励</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_2" onclick="selectPubType(this);" value="2">书/著作</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_10" onclick="selectPubType(this);"
        value="10">书籍章节</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_8" onclick="selectPubType(this);" value="8">学位论文</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_12" onclick="selectPubType(this);" value="12">标准</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_13" onclick="selectPubType(this);" value="13">软件著作权</div>
      <div class="provision_container-body_item-list div_item" id="pub_type_7" onclick="selectPubType(this);" value="7">其他</div>
    </div>
  </div>
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findPubs();" style="width: 100%">确定</div>
  </div>
</body>
</html>
