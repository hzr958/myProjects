<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<s:iterator value="grpShowInfoList" var="gsi" status="st">
  <div class="main-list__item item_no-border item_zero-space">
    <a href="${gsi.grpIndexUrl }">
      <div class="home-quickaccess-subitem__group-main">
        <div class="home-quickaccess-subitem__group-logo">
          <img src="${gsi.grpAuatars }">
        </div>
        <div class="home-quickaccess-subitem__group-name">${gsi.grpName }</div>
        <div class="grp-idx__logo_box-tip"
          title="<s:text name='groups.unread.count.tips.front'></s:text> ${gsi.groupUnreadCount}&nbsp;<s:text name='groups.unread.count.tips.back'></s:text>"
          style='display:<s:if test="%{#gsi.groupUnreadCount>0}">block;</s:if>'>
          <s:if test="%{#gsi.groupUnreadCount<=99}">${gsi.groupUnreadCount}</s:if>
          <s:elseif test="%{#gsi.groupUnreadCount>99}">99+</s:elseif>
        </div>
      </div>
    </a>
  </div>
</s:iterator>
