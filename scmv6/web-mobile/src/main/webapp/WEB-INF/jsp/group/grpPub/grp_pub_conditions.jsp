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
<script type="text/javascript" src="${resmod }/js/group/mobile/mobile_group.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript">

$(function(){
    dealWithConditionsStatus();
    bindSelectClick();

});

function bindSelectClick(){//绑定点击事件
  $(".type_year").click(function(){
    $(".type_year").not(this).removeClass("selector__list-target");
    $(this).toggleClass("selector__list-target");   
  });
  $(".type_order").click(function(){
    $(".type_order").not(this).removeClass("selector__list-target");
    $(this).toggleClass("selector__list-target");   
  });
  $(".pub_type").click(function(){
    $(this).toggleClass("selector__list-target");   
  });
  $(".type_include").click(function(){
    $(this).toggleClass("selector__list-target");   
  });
}
//处理条件上次选中的状态
function dealWithConditionsStatus(){
  var publishYear = $("#publishYear").attr("value");
  var orderBy = $("#orderBy").attr("value");
  var pubType = $("#pubType").attr("value");
  var includeType = $("#includeType").attr("value");
  
  $(".type_year[value='"+publishYear+"']").addClass("selector__list-target");
  $(".type_order[value='"+orderBy+"']").addClass("selector__list-target");
  $(".pub_type").each(function(){
    var value = $(this).attr("value");
    var reg = RegExp("(^"+value+"(,|$)|,"+value+"(,|$))","g");
    if(pubType.match(reg)){
      $(this).addClass("selector__list-target");
    }
  });
  $(".type_include").each(function(){
    var value = $(this).attr("value");
    if(includeType.indexOf(value)>-1){
      $(this).addClass("selector__list-target");
    }
  });
}
function findGroups(){
  var publishYear = $(".type_year.selector__list-target").attr("value");
  var orderBy = $(".type_order.selector__list-target").attr("value");
  var pubType = [];
  var includeType = [];
  $(".pub_type.selector__list-target").each(function(){
    pubType.push($(this).attr("value"));
  });
  $(".type_include.selector__list-target").each(function(){
    includeType.push($(this).attr("value"));
  });
  $("#publishYear").val(publishYear);
  $("#orderBy").val(orderBy);
  $("#pubType").val(pubType.join(","));
  $("#includeType").val(includeType.join(","));
  if($("#fromPage").val() == "grpDyn"){
    $("#grp_pub_search").attr("action", "/grp/publish/pub");
  }else{
    var url = $("#isLogin").val()=="false" ? "/grp/outside/mobile/grppubmain" : "/grp/mobile/grppubmain";
    $("#grp_pub_search").attr("action",url);
  }
  $("#grp_pub_search").submit()
}
</script>
</head>
<body>
  <form action="/grp/mobile/grppubmain" method="get" id="grp_pub_search">
    <input type="hidden" id="des3GrpId" name="des3GrpId" value="${pubVO.des3GrpId}"/> 
    <input type="hidden" id="searchKey" name="searchKey" value="${pubVO.searchKey}"/>
    <input type="hidden" id="publishYear" name="publishYear" value="${pubVO.publishYear}"/>
    <input type="hidden" id="pubType" name="pubType" value="${pubVO.pubType}"/>
    <input type="hidden" id="includeType" name="includeType" value="${pubVO.includeType}"/>
    <input type="hidden" id="orderBy" name="orderBy" value="${pubVO.orderBy}"/>  
    <input type="hidden" id="showPrjPub" name="showPrjPub" value="${pubVO.showPrjPub}"/>
    <input type="hidden" id="showRefPub" name="showRefPub" value="${pubVO.showRefPub}"/>
    <input type="hidden" id="grpCategory" name="grpCategory" value="${pubVO.grpCategory}"/>
    <input type="hidden" id="fromPage" name="fromPage" value="${pubVO.fromPage}"/>  
    <input type="hidden" id="dynText" name="dynText" value="${pubVO.dynText}"/>  
  </form>
  <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>
  <div class="provision_container-title">
    <!-- <a href="javascript:window.history.back();"> -->
    <i class="material-icons" onclick="javascript:SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置筛选条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">发表年份</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list type_year" value="${pubVO.currentYear }">${pubVO.currentYear }年</div>
      <div class="provision_container-body_item-list type_year" value="${pubVO.currentYear-1 }">${pubVO.currentYear-1 }年</div>
      <div class="provision_container-body_item-list type_year" value="${pubVO.recentYear5 }">近5年</div>
      <div class="provision_container-body_item-list type_year" value="${pubVO.recentYear10 }">近10年</div>

    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">成果类型</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list pub_type" value="4">期刊论文</div>
      <div class="provision_container-body_item-list pub_type" value="3">会议论文</div>
      <div class="provision_container-body_item-list pub_type" value="5">专利</div>
      <div class="provision_container-body_item-list pub_type" value="1,2,7,8,10,12,13">其他</div>
    </div>   
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">收录类别</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list type_include" value="scie">SCIE</div>
      <div class="provision_container-body_item-list type_include" value="ssci">SSCI</div>
      <div class="provision_container-body_item-list type_include" value="ei">EI</div>
      <div class="provision_container-body_item-list type_include" value="istp">ISTP</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">排序</span>
    </div>
    <div class="provision_container-body_item">
      <div class="provision_container-body_item-list type_order" value="createDate">最新添加</div>
      <div class="provision_container-body_item-list type_order" value="publishYear">最新发表</div>
      <div class="provision_container-body_item-list type_order" value="citedTimes">最多引用</div>

    </div>    
  </div>
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findGroups();" style="width: 100%">确定</div>
  </div>
</body>
</html>
