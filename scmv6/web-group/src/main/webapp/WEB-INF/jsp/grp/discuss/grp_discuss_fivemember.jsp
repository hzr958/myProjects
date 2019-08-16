<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	var buttonlist = document.getElementsByClassName("button_primary");
	clickDynamicreact(buttonlist);
})
</script>
<div class="module-card__box">
  <div class="module-card__header">
    <div class="module-card-header__title">
      <s:text name='groups.base.members' />
    </div>
    <button class="button_main button_link" onclick="GrpBase.toGrpMember(this)">
      <s:text name='groups.base.more.members' />
    </button>
  </div>
  <div class="main-list__list">
    <s:iterator value="memberInfoList" var="psn" status="st">
      <div class="main-list__item">
        <div class="main-list__item_content">
          <div class="psn-idx_small">
            <div class="psn-idx__base-info">
              <div class="psn-idx__avatar_box">
                <div class="psn-idx__avatar_img">
                  <a href="/psnweb/homepage/show?des3PsnId=${psn.des3MemberId}" target="_Blank"> <img
                    src="${psn.memberAvatur }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
                </div>
              </div>
              <div class="psn-idx__main_box">
                <div class="psn-idx__main">
                  <div class="psn-idx__main_name">
                    <a href="/psnweb/homepage/show?des3PsnId=${psn.des3MemberId}" title="${psn.memberName }"
                      target="_Blank">${psn.memberName }</a>
                  </div>
                  <div class="psn-idx__main_title psn-idx__main_title-Customized" title="${psn.insName }">${psn.insName }</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <c:if test="${!psn.isFriend }">
          <div class="main-list__item_actions">
            <button class="button_main button_dense button_primary"
              onclick="GrpMember.addFriend('${psn.des3MemberId }')">
              <s:text name='groups.base.members.add' />
            </button>
          </div>
        </c:if>
      </div>
    </s:iterator>
  </div>
</div>
