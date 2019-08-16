<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${!empty friendList }">
<c:forEach items="${friendList }" var="psnInfo">
    <div class="new-mobile_totarget-friend_list" des3PsnId="${psnInfo.person.personDes3Id }" select_option = "friend_${psnInfo.person.personDes3Id }" onclick="MobileSmateShare.addChoosed(this,'${psnInfo.person.personDes3Id }','${psnInfo.person.name }','friend')">
       <i class="material-icons new-mobile_totarget-friend_list-icon">add</i>
       <span class="new-mobile_totarget-friend_list-detail friend_${psnInfo.person.personDes3Id }">${psnInfo.person.name }</span>
    </div>
</c:forEach>
</c:if>