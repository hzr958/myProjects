<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
  <meta charset="utf-8">
  <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
  <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
  <link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="${resmod }/js_v8/common/jquery/jquery-3.4.1.js"></script>
  <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
  <script type="text/javascript" src="${resmod }/mobile/js/plugin/mobile.moveitem.activity.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
  <script type="text/javascript" src="${resmod }/mobile/js/group/mobile.grp.member.js"></script>
  <script type="text/javascript" src="${resmod }/js/loadStateIco.js"></script>
  <script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
  
  <script>
    window.onload = function(){
      changeSelectStyle($("#grpMenber"));
      document.getElementsByClassName("new-mobilegroup_body")[0].style.height = window.innerHeight - 106 + "px";
    }
    $(function(){
      //加载群组成员
      //Group.initGrpMemberScroll();
      //加载待批准人员
      //Group.showApplyMembers();
      Group.showMembers();
    });
  </script>
</head>
<body>
    <input type="hidden" id="des3GrpId" name="des3GrpId" value="${grpDTO.des3GrpId }"/>
    <input type="hidden" id="role" name="role" value="${grpDTO.psnRole}"/>
    <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>
    <div class="provision_container-title" style="z-index: 120;">
      <i class="material-icons"  onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
      <span id="pageTitle">群组成员</span>  
      <i></i>
    </div>
   

    <div class="new-mobilegroup_body main-list" id="group_member_list" style="background: #fff; top:50px;">
        <!-- 群组成员 -->
        <jsp:include page="mobile_grp_member_model.jsp" />
    </div>
     <c:set var="des3GrpId" value="${grpDTO.des3GrpId}"></c:set>
     <c:set var="grpCategory" value="${grpDTO.grpCategory}"></c:set> 
     <c:set var="psnRole" value="${grpDTO.psnRole}"></c:set>      
     <%@ include file="/WEB-INF/jsp/group/common/mobile_group_nav.jsp" %>   
</body>
</html>