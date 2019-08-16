<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<div class="sel-dropdown__list">
  <%-- <c:forEach items="${workSelect }" var="work" varStatus="st">
    		<div class="sel-dropdown__item" onclick="changeInsAddr('${work.key}');" sel-itemvalue="${work.key}">${work.value}</div>
      </c:forEach>
      <c:forEach items="${workAddr }" var="addr" varStatus="st">
      	<input type="hidden" id="ins_${addr.key }" value="${addr.value }"/>
      </c:forEach> --%>
  <c:forEach items="${workList }" var="work" varStatus="st">
    <div class="sel-dropdown__item" onclick="changeInsAddr('${work.workId}');" sel-itemvalue="${work.workId}">${work.workInsInfoStr}</div>
    <input type="hidden" id="ins_${work.workId}" value="${work.workInsAddr }" />
    <input type="hidden" id="regionId_${work.workId }" value="${work.regionId }" />
  </c:forEach>
</div>