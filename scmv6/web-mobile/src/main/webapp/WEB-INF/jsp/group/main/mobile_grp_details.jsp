<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
    <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
    <script type="text/javascript" src="${resmod }/js_v8/common/jquery/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod }/mobile/js/group/mobile.grp.main.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
    <script type="text/javascript">
       window.onload =function(){
         var groupBody = document.getElementsByClassName("new-mobilegroup_body")[0];
         var groupNeck = document.getElementsByClassName("new-mobilegroup_neck")[0];
         var groupHeader = document.getElementsByClassName("new-mobilegroup_header")[0];
         if(typeof(groupBody) != "undefined"){
           groupBody.style.top =  48 + "px";
           groupBody.style.height = window.innerHeight - 120 + "px";
         }
         if(typeof(groupNeck) != "undefined"){
           groupNeck.style.top =   48 + "px";
         }
         if(typeof(groupHeader) != "undefined"){
           groupHeader.style.top= 48 + "px";
         }
       }
       $(function(){
         Group.loadRelationPrjInfo();
         changeSelectStyle($("#grpDyn"));
         document.getElementsByClassName("provision_container-title_onkey")[0].addEventListener("click",function(){
           if(this.querySelector(".sig__out-box__insign").classList.contains("setting-list_page-item_hidden")){
               this.querySelector(".sig__out-box__insign").classList.remove("setting-list_page-item_hidden");
               this.querySelector(".sig__out-box__insign").classList.add("setting-list_page-item_show");
           }else{
               this.querySelector(".sig__out-box__insign").classList.add("setting-list_page-item_hidden");
               this.querySelector(".sig__out-box__insign").classList.remove("setting-list_page-item_show");
           }
         });
       })
       
    </script>
</head>
<body>
    <input type="hidden" id="des3GrpId" name="des3GrpId" value="${dto.des3GrpId }"/>
    <input type="hidden" id="role" name="role" value="${grpInfo.role }"/>
    <input type="hidden" id="grpType" name="grpType" value="${grpInfo.grpBaseInfo.grpCategory }"/>
    <input type="hidden" id="status" name="status" value="${grpInfo.grpBaseInfo.status }">
        <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>
    
    <div  class="paper__func-header">
        <i class="material-icons" onclick="window.history.back();">keyboard_arrow_left</i>
        <span>群组首页</span>
        <i class="material-icons provision_container-title_onkey"  style="position: relative;">
            <c:if test="${grpInfo.role == 1 || grpInfo.role == 2 || grpInfo.role == 3}">
               more_horiz
               <div class="sig__out-box sig__out-box__insign  setting-list_page-item_hidden" style="z-index: 10001;">
                    <em class="sig__out-header"></em>
                    <div class="sig__out-body sig__out-body-container" style="height: auto; border: 1px solid #fefefe; width: 104px;">
                      <div class="sig__out-body-container_list" onclick="Group.stickyGrp('1', this);" style="${grpInfo.isTop == 1 && grpInfo.grpListIndex == 1 ? 'display:none;' : ''}">
                          <div class="sig__out-body-container_icon">
                              <i class="sig__out-body-container_icon-top"></i>
                          </div>
                          <span class="sig__out-body-container_detail">置顶此群组</span>
                      </div>
                      <div class="sig__out-body-container_list" id="cancel_sticky_grp_btn" onclick="Group.stickyGrp('0', this);" style="${grpInfo.isTop == 1 ? '' : 'display:none;'}">
                          <div class="sig__out-body-container_icon">
                              <i class="sig__out-body-container_icon-cancletop"></i>
                          </div>
                          <span class="sig__out-body-container_detail">取消置顶</span>
                      </div>
                      <div class="sig__out-body-container_line"></div>
                     <div class="sig__out-body-container_list" onclick="Group.quitGrp();">
                          <div class="sig__out-body-container_icon">
                              <i class="sig__out-body-container_icon-Signout"></i>
                          </div>
                           <span class="sig__out-body-container_detail">退出群组</span>
                      </div>
                     </div>
                </div 
            </c:if>
        </i>
    </div>


    <div class="new-mobilegroup_body" style="bottom:60px;">
       
       
     <div class="new-mobilegroup_header new-mobilegroup_header-postion" style=" height: 81px; align-items: center;">
        <div class="new-mobilegroup_header-avator">
           <img src="${grpInfo.grpBaseInfo.grpAuatars }"
              onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
        </div>
        <div class="new-mobilegroup_header-infor">
             <div class="new-mobilegroup_header-title">
                <div class="new-mobilegroup_header-title_detail" style=" width: 100%;">${grpInfo.grpBaseInfo.grpName }</div>
             </div>
        </div>
    </div>
    
    
    
       <div class="new-mobilegroup_body-showitem" id="relation_prj_info">
         
       </div>
       
       
       <c:if test="${!empty grpInfo.firstDisciplinetName }">
       <div class="new-mobilegroup_body-showitem">
         <div class="new-mobilegroup_body-showitem_header">
             <span class="new-mobilegroup_body-showitem_title">科研领域</span>
             <i></i>
         </div>
         <div class="new-mobilegroup_body-showitem_body" style="padding: 8px 0px;letter-spacing: 0.1px;">
            <div class="new-mobilegroup_body-showitem_body-scinenitem">${grpInfo.firstDisciplinetName } </div> 
            <div class="new-mobilegroup_body-showitem_body-scinenitem"><c:if test="${!empty grpInfo.secondDisciplinetName }"> </c:if>${grpInfo.secondDisciplinetName }</div>
         </div>
       </div>
       </c:if>
       
       <c:if test="${ !empty grpInfo.grpKwDisc.keywords }">
        <div class="new-mobilegroup_body-showitem">
         <div class="new-mobilegroup_body-showitem_header">
             <span class="new-mobilegroup_body-showitem_title">关键词</span>
             <i></i>
         </div>
         <div class="new-mobilegroup_body-showitem_body"  style="padding: 4px 0px; letter-spacing: 0.1px;">
            <c:set var="keyWordList" value="${fn:split(grpInfo.grpKwDisc.keywords,';') }"/>
            <c:forEach items="${keyWordList }" var="keyword" >
              <div class="new-mobilegroup_body-showitem_body-keyworditem">${keyword }</div>
            </c:forEach>           
         </div>
       </div>
       </c:if>
       
       <c:if test="${!empty grpInfo.grpBaseInfo.grpDescription }">
        <div class="new-mobilegroup_body-showitem">
         <div class="new-mobilegroup_body-showitem_header">
             <span class="new-mobilegroup_body-showitem_title">${grpInfo.grpBaseInfo.grpCategory=="10" ? "课程简介" : "项目摘要"}</span>
             <i></i>
         </div>
         <div class="new-mobilegroup_body-showitem_body"  style="padding: 4px 0px;letter-spacing: 0.1px;">
            ${grpInfo.grpBaseInfo.grpDescription }
         </div>
       </div>
       </c:if>
    </div>

         <!-- 菜单 -->
     <c:set var="des3GrpId" value="${dto.des3GrpId}"></c:set>
     <c:set var="grpCategory" value="${grpInfo.grpBaseInfo.grpCategory}"></c:set>
     <c:set var="psnRole" value="${grpInfo.role}"></c:set>
     <c:set var="grpControl" value="${grpInfo.grpControl}"></c:set>
     <%@ include file="/WEB-INF/jsp/group/common/mobile_group_nav.jsp" %>
</body>
</html>
