<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
/*   var totalCount = "${totalCount}";
  if(!isNaN(totalCount) && parseInt(totalCount) > 0){
    $("#grp_apply_member_count").text(totalCount).show();
  } */
})

</script>
<c:if test="${!empty totalCount && totalCount > 0}">
<div class="new-mobilegroup_body-showitem" id="mobile_grp_member_req_model">
  <div class="new-mobilegroup_body-showitem_header" style=" border: none;">
     <span class="new-mobilegroup_body-showitem_title">待批准成员</span>
     <i></i>
  </div>

  <c:forEach items="${psnInfoList }" var="psn" varStatus="status">
  <div class="new-mobilegroup_body-showitem_body mobile_member_req_item" style="border-top: 1px solid #ddd;" des3PsnId="${psn.des3PsnId}">
    <div style="display: flex; align-items: center; padding:12px 0px 12px 0px;">
       <div class="new-mobilegroup_body-item_avator" onclick="Group.goToPersonalPage('${psn.des3PsnId}');"><img src="${psn.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"/></div>
       <div class="new-mobilegroup_body-item_body" style="height: 48px;">
           <div class="new-mobilegroup_fabulous-header">
            <div class="new-mobilegroup_fabulous-header_left">
                <a onclick="Group.goToPersonalPage('${psn.des3PsnId}');">${psn.name }</a>
                
             </div>
           </div>
           <div class="new-mobilegroup_fabulous-infor">${psn.insName }</div>
       </div>
       <div class="new-mobilegroup_body-item_functool">
         <div class="new-mobilegroup_body-item_functool-add" onclick="Group.dealApply('1', '${psn.des3PsnId}', this, event)">批准</div>
         <div class="new-mobilegroup_body-item_functool-ignore" onclick="Group.dealApply('0', '${psn.des3PsnId}', this, event)">忽略</div>
       </div>
    </div>
  </div>
  </c:forEach>
</div>
</c:if>