<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="currentMergeStatus" value="${currentStatus}">
<input type="hidden" id="currentLoginCount" value="${loginCount}">
<div class="set-email__main-title">
  <span class="set-email__main-heading"> <s:text name="sns.menu.homepage.settings.mergecount" /> <span
    class="set-merge__tip">(<s:text name="page.merge.main.label" />)
  </span>
  </span>
</div>
<div class="set-merge__container">
  <div>
    <s:text name="page.merge.main.logincount" />
    <s:text name="psnset.colon" />
  </div>
  <div class="set-merge__container-box">
    <div class="set-merge__account-number">
      <a href="/psnweb/homepage/show?des3PsnId=${psnDes3Id}"> <img
        src="${psnAvatars }?temp=<s:property value="@java.lang.Math@random()"/>" class="set-merge__account-avator"
        onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
      </a>
      <div class="set-merge__account-infor">
        <div class="set-merge__account-name">${psnViewName}(<s:text name="page.merge.main.account" />
          <s:text name="psnset.colon" />
          <a href="mailto:${loginCount}" class="Blue">${loginCount }</a>)
        </div>
        <div class="set-merge__account-work">${psnTitolo}</div>
      </div>
    </div>
    <div class="set-merge__footer">
      <div class="set-merge__add-btn" onclick="PsnsettingMerge.showAddEmailBox()">
        <s:text name="page.merge.add.label.top" />
      </div>
      <span class="set-merge__add-tip"><span class="set-merge__add-flag">*</span> <s:text
          name="page.merge.main.tip.content" /> </span>
    </div>
  </div>
</div>
<c:if test="${ !empty  mergeList}">
  <div class="set-merged__title">
    <s:text name="page.merge.main.label.merge" />
    <s:text name="psnset.colon" />
  </div>
  <div class="set-merged__container">
    <c:forEach items="${mergeList }" var="merge">
      <input type="hidden" name="mergedPsnId" value="${merge.psnDes3Id }">
      <div class="set-merged__item">
        <div class="set-merged__item-content" style="display: flex; align-items: center;">
          <a href="/psnweb/homepage/show?des3PsnId=${merge.psnDes3Id}"> <img
            src="${merge.psnAvatars }?temp=<s:property value="@java.lang.Math@random()"/>"
            class="set-merge__account-avator" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          </a>
          <div class="set-merge__account-infor" style="height: auto;">
            <div class="set-merge__account-name" style="width: 320px;">${merge.psnViewName}(<s:text name="page.merge.main.account" />
              <s:text name="psnset.colon" />
              <a href="mailto:${merge.loginCount}" class="Blue">${merge.loginCount }</a>)
            </div>
            <div class="set-merge__account-work">${merge.psnTitolo}</div>
          </div>
        </div>
        <div class="set-merged__item-state">
          <s:text name="page.merge.main.merging" />
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>