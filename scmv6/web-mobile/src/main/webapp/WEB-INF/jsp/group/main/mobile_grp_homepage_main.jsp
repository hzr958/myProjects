<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <meta charset="utf-8">
    <%@ include file = "mobile_grp_detail_style.jsp" %>
    <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
    <link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
    <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
    <script type="text/javascript" src="${resmod }/js_v8/common/jquery/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod }/mobile/js/group/mobile.grp.main.js"></script>
    <script type="text/javascript" src="${resmod }/js/loadStateIco.js"></script>
    <script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
    <script>
       window.onload =function(){
           document.getElementsByClassName("new-mobilegroup_body")[0].style.top =  50  + "px";
           document.getElementsByClassName("new-mobilegroup_body")[0].style.height = window.innerHeight - 100 + "px";
           document.getElementsByClassName("provision_container-title_onkey")[0].addEventListener("click",function(){
               if(this.querySelector(".sig__out-box__insign").classList.contains("setting-list_page-item_hidden")){
                   this.querySelector(".sig__out-box__insign").classList.remove("setting-list_page-item_hidden");
                   this.querySelector(".sig__out-box__insign").classList.add("setting-list_page-item_show");
               }else{
                   this.querySelector(".sig__out-box__insign").classList.add("setting-list_page-item_hidden");
                   this.querySelector(".sig__out-box__insign").classList.remove("setting-list_page-item_show");
               }
               
           })
       }
    </script>
</head>
<body>
    <input type="hidden" id="des3GrpId" name="des3GrpId" value="${dto.des3GrpId }"/>
    <input type="hidden" id="role" name="role" value="${grpInfo.role }"/>
    <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>
    <input type="hidden" id="des3CurrentPsnId" value="${dto.des3PsnId}"/>
    
    <div class="provision_container-title" style="z-index: 120;">
      <i class="material-icons" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
      <span id="pageTitle">群组首页</span>
      <i class="material-icons provision_container-title_onkey"  style="position: relative;">
      <c:if test="${grpInfo.role == 1 || grpInfo.role == 2 || grpInfo.role == 3}">
         more_horiz
         <div class="sig__out-box sig__out-box__insign  setting-list_page-item_hidden" style="z-index: 10001;">
              <em class="sig__out-header"></em>
              <div class="sig__out-body sig__out-body-container" style="height: auto; border: 1px solid #fefefe; width: 104px;">
                <div class="sig__out-body-container_list" id="sticky_grp_btn" onclick="Group.stickyGrp('1', this);" style="${grpInfo.isTop == 1 && grpInfo.grpListIndex == 1 ? 'display:none;' : ''}">
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
      

      <!-- 动态列表 -->
      <div class="new-mobilegroup_body grp_item_list main-list" style="top: 50px;">
         <!-- 头部信息 start -->
         <%@ include file="mobile_grp_homepage_headinfo.jsp" %> 
         <!-- 头部信息  end -->
            <c:if test="${grpInfo.showDyn == '1'}">
               <div class="main-list__list" id="group_dyn_list" list-main="mobile_grp_dyn_model"></div>
            </c:if>
            <c:if test="${grpInfo.showDyn != '1'}">
              <div class="main-list__list item_no-border">
                <div class="response_no-result">
                                                             需要加入群组后才能发表、查看讨论
                </div>
              </div>
            </c:if>
          
      </div>
      <c:if test="${grpInfo.role == 1 || grpInfo.role == 2 || grpInfo.role == 3}">
        <div class="new-mobilegroup_Release" id="group_dyn_publish_btn" onclick="Group.publishDyn();"><i class="material-icons">add</i></div>
      </c:if>
      <!-- 菜单 -->
       <c:set var="des3GrpId" value="${dto.des3GrpId}"></c:set>
       <c:set var="grpCategory" value="${grpInfo.grpBaseInfo.grpCategory}"></c:set>
       <c:set var="psnRole" value="${grpInfo.role}"></c:set>
       <c:set var="grpControl" value="${grpInfo.grpControl}"></c:set>
       <%@ include file="/WEB-INF/jsp/group/common/mobile_group_nav.jsp" %>

    <script type="text/javascript">
      $(function(){
        changeSelectStyle($("#grpDyn"));
        Group.loadGrpDynList();
      });
    </script>
</body>
</html>
