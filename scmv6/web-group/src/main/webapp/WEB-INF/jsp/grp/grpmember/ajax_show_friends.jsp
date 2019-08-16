<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
  var isSetTitilePrompt_isGrpMember = locale == "zh_CN" ? "该用户已是群组成员" : "该用户已是群组成员";
  var isSetTitile_isGrpMember = $("[name = 'isSetTitile_isGrpMember']");
  for (var i = 0;i < isSetTitile_isGrpMember.length;i++) {
    var isGrpMember = $(isSetTitile_isGrpMember[i]).attr("title_isGrpMember");
    if (isGrpMember == 1) {
      $(isSetTitile_isGrpMember[i]).attr("title",isSetTitilePrompt_isGrpMember);
    }
  }
	
});
</script>
<!--列表循环 start  -->
<s:iterator value="psnInfoList" var="pi" status="st">
  <div class="friend-selection__item-3" smate_psn_id='${pi.des3PsnId}' onclick='GrpMember.clickFriend(this)'
    name="isSetTitile_isGrpMember" title_isGrpMember="${pi.isGrpMember }">
    <div class='psn-idx_small <s:if test="#pi.isGrpMember==1">none-selected</s:if>'>
      <div class="psn-idx__base-info">
        <div class="psn-idx__avatar_box">
          <div class="psn-idx__avatar_img">
            <img src="${pi.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          </div>
        </div>
        <div class="psn-idx__main_box">
          <div class="psn-idx__main">
            <div class="psn-idx__main_name" title="${pi.name}">${pi.name}</div>
            <!-- title="${pi.person.insName}" -->
            <div class="psn-idx__main_title">${pi.person.insName}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>