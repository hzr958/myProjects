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
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">

<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/group/mobile/mobile_grp_pub.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/group/mobile.grp.main.js"></script>


<script type="text/javascript">
$(function(){
 //初始化检索输入框
  var searchInputOptions = {
      "searchFunc": "groupPubSearch()", //点击检索图标执行函数
      "oninput":"groupPubSearch()",
      "placeHolder": "检索论文",
      "searchInputVal" : "${pubVO.searchKey }", //检索的字符串
      "searchFilter": "select_filter();", //过滤条件图标点击事件
      "needFilter": false, //是否需要显示过滤条件图标
  };
  commonMobileSearch.initSearchInput(searchInputOptions); 
  GrpPub.grpSharePubList();
});
function groupPubSearch(){
  var searchStr = $.trim($("#searchStringInput").val());
  $("#searchKey").val(searchStr);
  GrpPub.grpSharePubList();
}
function select_filter(){
  var url = $("#isLogin").val()=="false" ? "/grp/outside/mobile/grouppubconditions" : "/grp/mobile/grouppubconditions";
  $("#select_filter_action").attr("action",url);
  $("#select_filter_action").submit();
}
</script>
</head>
<body>
<%--   <form action="/grp/mobile/grouppubconditions" method="get" id="select_filter_action">
    <input type="hidden" id="des3GrpId" name="des3GrpId" value="${pubVO.des3GrpId}"/> 
    <input type="hidden" id="searchKey" name="searchKey" value="${pubVO.searchKey}"/>
    <input type="hidden" id="publishYear" name="publishYear" value="${pubVO.publishYear}"/>
    <input type="hidden" id="pubType" name="pubType" value="${pubVO.pubType}"/>
    <input type="hidden" id="includeType" name="includeType" value="${pubVO.includeType}"/>
    <input type="hidden" id="orderBy" name="orderBy" value="${pubVO.orderBy}"/>    
    <input type="hidden" id="showPrjPub" name="showPrjPub" value="${pubVO.showPrjPub}"/>
    <input type="hidden" id="showRefPub" name="showRefPub" value="${pubVO.showRefPub}"/>
    <input type="hidden" id="grpCategory" name="grpCategory" value="${pubVO.grpCategory}"/>
    <input type="hidden" id="fromPage" name="fromPage" value="grpDyn"/>
    <input type="hidden" id="publishText" name="dynText" value="${pubVO.dynText }"/>
  </form> --%>
    <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>

    <div class="provision_container-title" style="z-index: 120;">
    <i class="material-icons"  onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
    <span id="pageTitle">
    <c:if test="${pubVO.pubListType == 'myPub'}">
            我的成果
    </c:if>
    <c:if test="${pubVO.pubListType != 'myPub'}">
             群组成果
    </c:if>
    </span> <i></i>
    </div>
    
    
    <div class="black_top" style="display: none" id="dynselect">
      <div class="screening_box">
        <div class="screening">
          <h2>分享成果</h2>
          <div class="screening_tx" id="dynpubtitle"></div>
          <p>
            <input type="button" value="确&nbsp;&nbsp;定" onclick="Group.confirm_dyn_select()" class="determine_btn"><input
              type="button" onclick="Group.cancel_dyn_select()" value="取&nbsp;&nbsp;消" class="cancel_btn">
          </p>
        </div>
        <form id="pubselectform" action="/grp/dyn/publish" method="post">
          <input type="hidden" id="des3GrpId" name="des3GrpId" value="${pubVO.des3GrpId}"/>
          <input id="dynText" name="dynText" type="hidden" value="${pubVO.dynText }" /> 
          <input id="dyndes3pubId" name="des3PubId" type="hidden" value="" />
          <input id="publishDes3GrpId" name="des3GrpId" type="hidden" value="${pubVO.des3GrpId}"/>
          <input type="hidden" id="searchKey" name="searchKey" value="${pubVO.searchKey}"/>
        </form>
          <input id="pubListType" name="pubListType" type="hidden" value="${pubVO.pubListType}" />         
      </div>
    </div>
    
    
    <%@ include file="/common/mobile/common_mobile_search_input.jsp" %> 

 
  <div class="effort_list" style="width: 100%; z-index: 99;">
    <div class="paper_content-container main-list" style="min-height: 100%;">
      <div class="main-list__list item_no-padding" list-main="grppublist"></div>
    </div>
  </div>
  
</body>
</html>