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
  $(".grpCategory").click(function(){
    $(".grpCategory").not(this).removeClass("selector__list-target");
    $(this).toggleClass("selector__list-target");   
  });
  $(".type_area").click(function(){
    $(".type_area").not(this).removeClass("selector__list-target");
    $(this).toggleClass("selector__list-target");   
  });
}
//处理条件上次选中的状态
function dealWithConditionsStatus(){
  var grpCategory = $("#grpCategory").val();
  var area = $("#disciplineCategory").val();
  $(".grpCategory").removeClass("selector__list-target");
  $(".grpCategory[value='"+grpCategory+"']").addClass("selector__list-target");
  $(".type_area").removeClass("selector__list-target");
  $(".type_area[value='"+area+"']").addClass("selector__list-target");
}
function findGroups(){
  var grpCategory = $(".grpCategory.selector__list-target").attr("value");
  var area = $(".type_area.selector__list-target").attr("value");
  $("#grpCategory").val(grpCategory);
  $("#disciplineCategory").val(area);
  $("#find_grp_search").submit()
}
</script>
</head>
<body>
  <form id="find_grp_search" method="get" action="/grp/mobile/findgroupmain">
    <input type="hidden" id="grpCategory" name="grpCategory" value="${groupVO.grpCategory}"/>
    <input type="hidden" id="disciplineCategory" name="disciplineCategory" value="${groupVO.disciplineCategory}"/>
    <input type="hidden" id="searchKey" name="searchKey" value="${groupVO.searchKey}"/>
  </form>
  <div class="provision_container-title">
    <!-- <a href="javascript:window.history.back();"> -->
    <i class="material-icons" onclick="javascript:SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置发现条件</span> <i></i>
  </div>
  <div class="provision_container-body">
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">群组类别</span>
    </div>
    <div class="provision_container-body_item type_span">
      <div class="provision_container-body_item-list grpCategory" value="12">兴趣群组</div>
      <div class="provision_container-body_item-list grpCategory" value="11">项目群组</div>
      <div class="provision_container-body_item-list grpCategory" value="10">课程群组</div>
    </div>
    <div class="provision_container-body_title">
      <span class="provision_container-body_title-infor">科技领域</span>
    </div>
    <div class="provision_container-body_item pub_time">
      <div class="provision_container-body_item-list type_area" value="1">农业</div>
      <div class="provision_container-body_item-list type_area" value="2">科学</div>
      <div class="provision_container-body_item-list type_area" value="3">人文社科</div>
      <div class="provision_container-body_item-list type_area" value="4">经济管理</div>
      <div class="provision_container-body_item-list type_area" value="5">工程</div>
      <div class="provision_container-body_item-list type_area" value="6">信息科技</div>
      <div class="provision_container-body_item-list type_area" value="7">医药卫生</div>

    </div>
  </div>
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findGroups();" style="width: 100%">确定</div>
  </div>
</body>
</html>
