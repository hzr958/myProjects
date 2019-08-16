<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- <script type="text/javascript" src="${res }/js_v5/plugin/moveorclick/moveorclick.js"></script> -->
<script type="text/javascript">
$(document).ready(function(){
	slideDom("may_know_item", 35, true, leftMoveItemCallBack, rightMoveItemCallBack, clickItemCallBack);
	$("#lastPsnId").val($(".lastPsnIdInput").val());
	$(".lastPsnIdInput").remove();
	//防止多次绑定touch事件
	$(".may_know_item").removeClass("may_know_item");
});
</script>
<input type="hidden" name="pageNo" id="pageNo" value="${pageNo}">
<s:if test="page.totalCount > 0">
  <s:iterator value="page.result" id="knowList">
    <div class="list_item_container may_know_item" id="${knowList.psnId}"
      des3PsnId="<iris:des3 code='${knowList.psnId}'/>"
      onclick="goToPersonalPage('<iris:des3 code="${knowList.psnId}"/>');"   style="border: none;">
      <div class="list_item_section"></div>
      <div class="list_item_section">
        <div class="person_namecard_whole hasBg" style="border-bottom: 1px solid #ddd; padding: 16px 16px 16px 20px;">
          <div class="avatar">
            <img class="avatar" src="${knowList.avatarUrl}"
              onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          </div>
          <div class="person_information">
            <div class="name">${ knowList.name}</div>
            <div class="institution">
              <c:if test="${not empty knowList.posAndTitolo && not empty knowList.insAndDep}">${knowList.insAndDep},&nbsp;${knowList.posAndTitolo}</c:if>
              <c:if test="${empty knowList.posAndTitolo && not empty knowList.insAndDep}">${knowList.insAndDep}</c:if>
              <c:if test="${not empty knowList.posAndTitolo && empty knowList.insAndDep}">${knowList.posAndTitolo}</c:if>
            </div>
          </div>
          <div class="operations"
            onclick="addFriendReq('<iris:des3 code="${knowList.psnId}"/>', '${knowList.psnId}', event);">
            <div class="btn_normal btn_bg_origin fc_blue500 add_friend_btn" style="margin-right: 0px;"
              pId="${knowList.psnId}" des3PsnId="<iris:des3 code='${knowList.psnId}'/>">添加</div>
          </div>
        </div>
      </div>
      <div class="list_item_section">
        <div class="list_item_right_commands ignore">
          <i class="material-icons icon">chevron_left</i>
          <div>忽 略</div>
        </div>
      </div>
    </div>
  </s:iterator>
</s:if>
<s:else>
  <s:if test="pageNo == 1">
  </s:if>
  <s:else>
    <input type="hidden" name="hasNextPage" id="hasNextPage" value="false" />
  </s:else>
  </div>
</s:else>
