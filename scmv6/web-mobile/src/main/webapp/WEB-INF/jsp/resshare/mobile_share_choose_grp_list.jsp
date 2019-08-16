<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:if test="${not empty grpInfos && fn:length(grpInfos) > 0}">
    <c:forEach items="${grpInfos }" var="grpInfo">
        <div class="new-mobileContact_item grp main-list__item" onclick="MobileSmateShare.choose(this,'grp')"  des3GrpId = "${grpInfo.des3GrpId }">
           <div class="new-mobileContact_item-infor">
             <div class="new-mobileContact_item-content"  style="text-align: left;">
               <div class="new-mobileContact_item-author">${grpInfo.grpName }</div>
             </div>
           </div>
           <div class="new-mobileContact_item-check">
             <i class="material-icons" style="color:#666;">add</i>
           </div>
       </div>
    </c:forEach>
</c:if>
