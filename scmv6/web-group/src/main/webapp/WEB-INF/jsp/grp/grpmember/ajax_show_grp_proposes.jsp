
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>
<s:iterator value="psnInfoList" var="pi" status="st">
  <div class="main-list__item" smate_psn_id='${pi.des3PsnId}'>
    <div class="main-list__item_content">
      <div class="psn-idx_medium">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" target="_Blank"> <img
                src="${pi.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" title="${pi.name}" target="_Blank">${pi.name}</a>
              </div>
              <div class="psn-idx__main_title" style="max-width: 100%; title="${pi.person.insName}">${pi.person.insName}</div>
              <div class="psn-idx__main_area">
                <div class="kw__box">
                  <s:iterator value="#pi.psnDisciplineKeyList" var="psnD" status="st">
                    <div class="kw-chip_small">${psnD.keyWords}</div>
                  </s:iterator>
                </div>
              </div>
              <div class="psn-idx__main_stats">
                <span class="psn-idx__main_stats-item"><s:text name='groups.member.pro' />:
                  ${pi.psnStatistics.prjSum}</span><span class="psn-idx__main_stats-item"><s:text
                    name='groups.member.pub' />: ${pi.psnStatistics.pubSum}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey"
        onclick='GrpMember.disposegrpApplication(0,"${pi.des3PsnId}",GrpMember.disposegrpApplicationCallBack2,this)'>
        <s:text name='groups.member.manage.btnIgnore' />
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick='GrpMember.disposegrpApplication(1,"${pi.des3PsnId}",GrpMember.disposegrpApplicationCallBack2,this)'>
        <s:text name='groups.member.manage.btnConfirm' />
      </button>
    </div>
  </div>
</s:iterator>