<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$("#mobile_grp_members_model").attr("totalCount", "${totalCount}");
});
</script>  
    <div class="js_listinfo" smate_count="${totalCount}"></div>
    <c:if test="${!empty grpMembers && totalCount > 0}">
    <c:forEach items="${grpMembers }" var="member" varStatus="status">
    <div class="new-mobilegroup_body-showitem_body grp_member_item main-list__item">
      <div style="display: flex; align-items: center; padding:12px 0px 12px 0px; justify-content: space-between;">
         <div style="display: flex; align-items: center;justify-content: flex-start;">
         <div class="new-mobilegroup_body-item_avator" onclick="Group.goToPersonalPage('${member.des3PsnId}');"><img src="${member.person.avatars}"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></div>
         <div class="new-mobilegroup_body-item_body" style="height: 48px; width: 42vw;">
             <div class="new-mobilegroup_fabulous-header" >
              <div class="new-mobilegroup_fabulous-header_left">
                  <a onclick="Group.goToPersonalPage('${member.des3PsnId}');">${ member.name}</a>
                  <div></div>
               </div>
             </div>
             <div class="new-mobilegroup_fabulous-infor">${member.insName }</div>
         </div>
         </div>
         <div class="new-mobilegroup_body-item_functool">
          <c:if test="${member.isSelf == 0 && member.isFriend == 1}">
              <div class="new-mobilegroup_body-item_functool-add" onclick="Group.sendMsg('${member.des3PsnId}', event);">发送消息</div>
          </c:if>
          <c:if test="${member.isSelf == 0 && member.isFriend != 1}">
              <div class="new-mobilegroup_body-item_functool-add" onclick="Group.addFriendReq('${member.des3PsnId}', event);">加为联系人</div>
          </c:if>
         </div>
      </div>
    </div>
    </c:forEach>
    </c:if>
