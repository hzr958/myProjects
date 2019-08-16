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
<%-- <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> --%>
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
<script type="text/javascript" src="${resmod}/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/group/mobile/mobile_grp_pub.js"></script>
<script type="text/javascript" src="${resmod}/js/group/mobile/mobile_grp_file.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/group/mobile.grp.member.js"></script>
<script type="text/javascript">
$(function(){
    //文件初始化搜索框,其他选项卡隐藏该搜索框
    var searchInputOptions = {
        "oninput":"mGrpfileSearch()",//检索框oninput事件
        "placeHolder": "检索文件",
        "searchInputVal" : "${groupFile.searchKey }", //检索的字符串
        "searchFilter": "mGrpFileSearchByFilter()", //过滤条件图标点击事件
        "needFilter": true, //是否需要显示过滤条件图标
    };
    commonMobileSearch.initSearchInput(searchInputOptions);
    //$(".paper__func-tool").css("top","95px");
    MobileGrpFile.queryGrpFileList();
    var workFileType = $("#workFileType").val();
    var courseFileType = $("#courseFileType").val();
    if(workFileType==1){
      changeSelectStyle($("#grpWorkFile"));
      $("#file_title").text("群组作业");
    }else if(courseFileType==2){
      changeSelectStyle($("#grpCourseFile"));
      $("#file_title").text("群组课件");
    }else{
      changeSelectStyle($("#grpFile"));
    }
});
//输入搜索
function mGrpfileSearch(){
  $("#m_grp_file_searchKey").val($.trim($("#searchStringInput").val()));
  MobileGrpFile.queryGrpFileList();
}

//根据群组成员或者文件标签查询
function mGrpFileSearchByFilter(){
  var url = $("#isLogin").val()=="false" ? "/grp/outside/mobile/showconditions" : "/grp/mobile/showconditions";
  $("#m_grp_file_search_form").attr("action",url);
  $("#m_grp_file_search_form").submit();
}

function shareGrpFile(des3ResId,resType){
  window.location.href = "/psn/share/page?des3ResId="+encodeURIComponent(des3ResId) + "&resType=grpfile&currentDes3GrpId="+encodeURIComponent($("#m_grp_file_des3GrpId").val())+"&hideModule=1";
}
</script>
</head>
<body>
  <form action="/grp/mobile/showconditions" method="get" id = "m_grp_file_search_form">
      <input type = "hidden" name = "des3GrpId" id = "m_grp_file_des3GrpId" value="${groupFile.des3GrpId}"/>
      <input type = "hidden" name = "des3GrpLabelId" id = "m_grp_file_des3LableId" value = "${groupFile.des3GrpLabelId }"/>
      <input type = "hidden" name = "searchKey" id = "m_grp_file_searchKey" value = "${groupFile.searchKey }"/>
      <input type = "hidden" name = "des3MemberId" id = "m_grp_file_des3MemberId" value = "${groupFile.des3MemberId }"/>
      <input type = "hidden" name = "workFileType" id = "workFileType" value = "${groupFile.workFileType }"/>
      <input type = "hidden" name = "courseFileType" id = "courseFileType" value = "${groupFile.courseFileType }"/>
  </form>
 <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>
    <div class="provision_container-title" style="z-index: 120;">
    <i class="material-icons" style="width: 8vw;padding-right: 2vw;" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
    <span id="file_title">群组文件</span> <i></i>
    </div>  
    <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>
 
  <div class="effort_list" style="width: 100%;z-index: 99;">
    <div class="paper_content-container main-list" style="margin-bottom: 50px;">
      <div class="main-list__list item_no-padding" list-main="mobile_group_file_list"></div>
    </div>
  </div>
 <c:set var="des3GrpId" value="${groupFile.des3GrpId}"></c:set>
 <c:set var="grpCategory" value="${groupFile.grpCategory}"></c:set>   
 <c:set var="psnRole" value="${groupFile.psnRole}"></c:set>        
 <%@ include file="/WEB-INF/jsp/group/common/mobile_group_nav.jsp" %>   
</body>
</html>