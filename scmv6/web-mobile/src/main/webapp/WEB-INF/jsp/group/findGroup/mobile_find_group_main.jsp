<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<%response.setHeader("cache-control","public"); %>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/common/pulldown_refresh.js"></script>
<script type="text/javascript" src="${resmod}/js/group/mobile/mobile_group.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/plugin/moveorclick.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/plugin/mobile.moveitem.activity.js"></script>

<script type="text/javascript">
$(function(){
 //初始化检索输入框
  var searchInputOptions = {
      "searchFunc": "groupSearch()", //点击检索图标执行函数
      //"oninput":"groupSearch()",
      "placeHolder": "检索群组",
      "searchInputVal" : "${groupVO.searchKey }", //检索的字符串
      "searchFilter": "select_filter();", //过滤条件图标点击事件
      "needFilter": true, //是否需要显示过滤条件图标
  };
  commonMobileSearch.initSearchInput(searchInputOptions);   
  Group.findGroupList();
  selectRecommendPubMenu('recommend');   
});
function groupSearch(){
  var searchStr = $.trim($("#searchStringInput").val());
  $("#searchKey").val(searchStr);
  Group.findGroupList();
}
function select_filter(){
  $("#select_filter_action").submit();
}
function openGrpDetail(des3GrpId){
  window.location.href = "/grp/main?des3GrpId="+encodeURIComponent(des3GrpId);
}
</script>
</head>
<body>
  <form action="/grp/mobile/findgroupconditions" method="get" id="select_filter_action">
    <input type="hidden" id="grpCategory" name="grpCategory" value="${groupVO.grpCategory}"/>
    <input type="hidden" id="disciplineCategory" name="disciplineCategory" value="${groupVO.disciplineCategory}"/>
    <input type="hidden" id="searchKey" name="searchKey" value="${groupVO.searchKey}"/>
  </form>

  <div>
    <div class="paper__func-header">
      <a href="/psnweb/mobile/myhome"><i class="material-icons paper__func-header__tip">keyboard_arrow_left</i></a> <span>群组发现</span>
      <i class="material-icons paper__func-header__tip"></i>
    </div>
   <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>
  </div>
  <div style="width: 100%;">
    <div class="paper_content-container main-list" style="margin-bottom: 50px;">
      <div class="main-list__list item_no-padding" list-main="grprcmdlist"></div>
    </div>
  </div>
  
  <%@ include file="/WEB-INF/jsp/mobile/bottom/mobile_recommend_group_bottom.jsp"%>
</body>
</html>