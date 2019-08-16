<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	if($("#grp_referrers_List").find(".main-list__item").length<=0){
		$("#grp_referrers_List").hide();
	}
});
</script>
<div class="module-card__box">
  <div class="module-card__header">
    <div class="module-card-header__title">推荐成员</div>
  </div>
  <div class="main-list__list">
    <s:iterator value="psnInfoList" var="pi" status="st">
      <div class="main-list__item">
        <div class="main-list__item_content">
          <div class="psn-idx_small">
            <div class="psn-idx__base-info">
              <div class="psn-idx__avatar_box">
                <div class="psn-idx__avatar_img">
                  <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" target="_Blank"><img
                    src="${pi.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
                </div>
              </div>
              <div class="psn-idx__main_box">
                <div class="psn-idx__main">
                  <div class="psn-idx__main_name">
                    <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" target="_Blank">${pi.name}</a>
                  </div>
                  <div class="psn-idx__main_title">${pi.person.insName}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="main-list__item_actions">
          <button class="button_main button_dense button_primary"
            onclick='GrpMember.InvitedReferrer("${pi.des3PsnId}",this)'>邀请加入</button>
        </div>
      </div>
    </s:iterator>
  </div>
</div>