<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:if test="${not empty psnInfos && fn:length(psnInfos) > 0}">
    <c:forEach items="${psnInfos }" var="psnInfo">
        <div class="new-mobileContact_item friend main-list__item" onclick="MobileGrpFile.choose(this,'friend')"  des3PsnId = "${psnInfo.person.personDes3Id }">
           <div class="new-mobileContact_item-infor">
             <div class="new-mobileContact_item-avator">
                <img src="${psnInfo.person.avatars }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"/>
             </div>
             <div class="new-mobileContact_item-content">
               <div class="new-mobileContact_item-author">${psnInfo.person.name }</div>
               <div class="new-mobileContact_item-introduce">${psnInfo.person.insName }</div>
             </div>
           </div>
           <div class="new-mobileContact_item-check">
             <i class="material-icons" style="color:#666;">add</i>
           </div>
       </div>
    </c:forEach>
</c:if>
