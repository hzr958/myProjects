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

//绑定点击事件
function bindSelectClick(){
  $(".grpMember").click(function(){
    $(".grpMember").not(this).removeClass("selector__list-target"); 
    $(this).toggleClass("selector__list-target"); 
  });
  $(".grpFileLabel").click(function(){
    $(this).toggleClass("selector__list-target"); 
  });
}


//处理条件上次选中的状态
function dealWithConditionsStatus(){
  var des3memberId = $("#m_grp_file_member").val();
  var des3labelId = $("#m_grp_file_label").val();
  $(".grpMember").removeClass("selector__list-target");
  $(".grpMember[value='"+des3memberId+"']").addClass("selector__list-target");
  $(".grpFileLabel").removeClass("selector__list-target");
  if(des3labelId!=""){
    var label = des3labelId.split(",");
    $.each(label,function(i,data){
      $(".grpFileLabel[value='"+data+"']").addClass("selector__list-target");
    });   
  }

}
function findGrpFiles(){
  var des3memberId = $(".grpMember.selector__list-target").attr("value");  
  var des3labelId = [];
  $(".grpFileLabel.selector__list-target").each(function(){
    des3labelId.push($(this).attr("value"));
  });
  $("#m_grp_file_member").val(des3memberId);
  $("#m_grp_file_label").val(des3labelId.join(","));
  
  var url = $("#isLogin").val()=="false" ? "/grp/outside/main/grpfilemain" : "/grp/main/grpfilemain";
  $("#mobile_grp_file_form").attr("action",url);
  $("#mobile_grp_file_form").submit();
}
</script>
</head>
<body>
  <form id="mobile_grp_file_form" method="get" action="/grp/main/grpfilemain">
    <input type = "hidden" id = "m_grp_file_des3GrpId" name = "des3GrpId" value="${groupFile.des3GrpId}"/>
    <input type="hidden" id="m_grp_file_member" name="des3MemberId" value="${groupFile.des3MemberId}"/>
    <input type="hidden" id="m_grp_file_label" name="des3GrpLabelId" value="${groupFile.des3GrpLabelId}"/>
    <input type="hidden" id="m_grp_file_searchKey" name="searchKey" value="${groupFile.searchKey}"/>
    <input type = "hidden" name = "workFileType" id = "workFileType" value = "${groupFile.workFileType }"/>
    <input type = "hidden" name = "courseFileType" id = "courseFileType" value = "${groupFile.courseFileType }"/>
  </form>
   <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>
  <div class="provision_container-title">
    <i class="material-icons" onclick="javascript:window.history.back()">keyboard_arrow_left</i>
    <span>设置筛选条件</span> <i></i>
  </div>
  <c:choose>
    <c:when test="${fn:length(psnInfos) > 0 or fn:length(grpLabels) > 0 }">
        <div class="provision_container-body">
          <c:if test="${fn:length(psnInfos) > 0 }">
            <div class="provision_container-body_title">
              <span class="provision_container-body_title-infor">组员上传文件</span>
            </div>
            <div class="provision_container-body_item type_span">
              <c:forEach items="${psnInfos }" var="psnInfo">
                  <div class="provision_container-body_item-sublist grpMember" value="${psnInfo.des3MemberId }">
                  <div  class="provision_container-body_item-list_detail">${psnInfo.memberName }</div>
                  <c:if test='${not empty psnInfo.memberFileNum && psnInfo.memberFileNum>0}'>
                     <span  class="provision_container-body_item-list_num">(${psnInfo.memberFileNum})</span>
                  </c:if>
                  </div>
              </c:forEach>
            </div>
          </c:if>
          <c:if test="${fn:length(grpLabels) > 0 }">
            <div class="provision_container-body_title">
              <span class="provision_container-body_title-infor">文件标签</span>
            </div>
            <div class="provision_container-body_item pub_time">
              <c:forEach items="${grpLabels }" var="grpLabel">
                  <div class="provision_container-body_item-sublist grpFileLabel" value="${grpLabel.des3LabelId }">
                    <div class="provision_container-body_item-list_detail">${grpLabel.labelName }</div>
                    <c:if test='${not empty grpLabel.resCount && grpLabel.resCount>0}'>
                       <div class="provision_container-body_item-list_num">(${grpLabel.resCount})</div>
                    </c:if>
                   </div>
              </c:forEach>
            </div>
          </c:if>
        </div>
    </c:when>
    <c:otherwise>
        <div class="new-mobile_Privacy-tip_container" style="background: rgb(238, 238, 238); height: 60%;">
            <div class="new-mobile_Privacy-tip_container" style="height: 100vh;">
                <div class="new-mobile_Privacy-tip_avator"></div>
            <div class="new-mobile_Privacy-tip_content">
                                             暂没有可供筛选的条件,请用电脑端创建
            </div>
            </div>
        </div>
    </c:otherwise>
  </c:choose>
  <div class="filter__footer">
    <div class="filter__footer-confirm" onclick="findGrpFiles();" style="width: 100%">确定</div>
  </div>
</body>
</html>
