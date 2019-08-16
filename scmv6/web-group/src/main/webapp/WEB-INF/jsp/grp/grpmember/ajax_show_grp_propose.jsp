
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var ispending = "${ispending}";
$(document).ready(function(){
	if($("#grp_propose_list").find(".main-list__item").length<=0){
		$("#grp_proposers_List").hide();
	}
	if(ispending==1){
		GrpMember.showPropsesList();
		ispending=0;
		history.pushState("", "",  window.location.href.replace("ispending=1","ispending=0"));
	}
});
</script>
<s:iterator value="psnInfoList" var="pi" status="st">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name='groups.member.manage.request' />
        <span class="module-card-header__title-stats">(${psnCount})</span>
      </div>
      <button class="button_main button_link" onclick='GrpMember.showPropsesList()'>
        <s:text name='groups.member.manage.viewAll' />
      </button>
    </div>
    <div class="main-list__list item_vert-style item_no-border" id='grp_propose_list'>
      <div class="main-list__item">
        <div class="main-list__item_content">
          <div class="pub-idx_small">
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
                      <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" title="${pi.name}" target="_Blank">${pi.name}</a>
                    </div>
                    <div class="psn-idx__main_title" title="${pi.person.insName}" style="max-width: 280px;">${pi.person.insName}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="main-list__item_actions">
          <button class="button_main button_dense button_grey"
            onclick='GrpMember.disposegrpApplication(0,"${pi.des3PsnId}",GrpMember.disposegrpApplicationCallBack1)'>
            <s:text name='groups.member.manage.btnIgnore' />
          </button>
          <button class="button_main button_dense button_primary-reverse"
            onclick='GrpMember.disposegrpApplication(1,"${pi.des3PsnId}",GrpMember.disposegrpApplicationCallBack1)'>
            <s:text name='groups.member.manage.btnConfirm' />
          </button>
        </div>
      </div>
    </div>
  </div>
</s:iterator>