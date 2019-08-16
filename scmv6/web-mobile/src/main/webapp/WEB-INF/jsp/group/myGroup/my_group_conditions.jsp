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
  $(".searchByRole").click(function(){
    $(".searchByRole").not(this).removeClass("selector__list-target");
    $(this).toggleClass("selector__list-target");   
  });
}
//处理条件上次选中的状态
function dealWithConditionsStatus(){
  var grpCategory = $("#grpCategory").val();
  var searchByRole = $("#searchByRole").val();
  $(".grpCategory").removeClass("selector__list-target");
  $(".grpCategory[value='"+grpCategory+"']").addClass("selector__list-target");
  $(".searchByRole").removeClass("selector__list-target");
  $(".searchByRole[value='"+searchByRole+"']").addClass("selector__list-target");
}
function findGroups(){
  var grpCategory = $(".grpCategory.selector__list-target").attr("value");
  var area = $(".searchByRole.selector__list-target").attr("value");
  $("#grpCategory").val(grpCategory);
  $("#searchByRole").val(area);
  $("#my_grp_search").submit()
}
</script>
</head>
<body>
  <form id="my_grp_search" method="get" action="/grp/mobile/mygroupmain">
    <input type="hidden" id="grpCategory" name="grpCategory" value="${groupVO.grpCategory}"/>
    <input type="hidden" id="searchByRole" name="searchByRole" value="${groupVO.searchByRole}"/>
    <input type="hidden" id="searchKey" name="searchKey" value="${groupVO.searchKey}"/>
  </form>
  <div class="provision_container-title">
    <!-- <a href="javascript:window.history.back();"> -->
    <i class="material-icons" onclick="javascript:SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
    <!-- </a> -->
    <span>设置条件</span> <i></i>
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
      <span class="provision_container-body_title-infor">群组权限</span>
    </div>
    <div class="provision_container-body_item pub_time">
      <div class="provision_container-body_item-list searchByRole" value="2">我管理的群组</div>
      <div class="provision_container-body_item-list searchByRole" value="3">我加入的群组</div>
      <div class="provision_container-body_item-list searchByRole" value="4">待批准的群组</div>
    </div>
  </div>
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findGroups();" style="width: 100%">确定</div>
  </div>
</body>
</html>
