<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="searchRegionList.size() > 0"></s:if>
<div class="region__list">
  <ul>
    <s:iterator value="searchRegionList">
      <li class="has_selected dev_search"
        onclick="FundRecommend.changeRgb(this); location.href='/prjweb/mobile/savefundarea?areaIds=${id}'"><a
        href="javascript:;">${zhName}</a></li>
    </s:iterator>
  </ul>
</div>
