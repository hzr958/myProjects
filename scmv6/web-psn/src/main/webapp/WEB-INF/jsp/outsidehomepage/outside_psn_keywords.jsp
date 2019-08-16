<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__card" style="display: none;" id="keywordsModule">
  <c:if test="${!empty keywords }">
    <div class="module-card__box" id="rskeywords">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="keywordsModule.header_title" />
        </div>
      </div>
      <div class="global__padding_16">
        <c:forEach items="${keywords }" var="keyword" varStatus="status">
          <div class="kw__box">
            <div class="kw-stick">
              <div class="kw-stick__stats" style="min-width: 6px;" id="idenSum_${keyword.id }">${keyword.identificationSum }</div>
              <div class="kw-stick__word" title="<c:out value='${keyword.keyWords }'/>">${keyword.keyWords }</div>
            </div>
          </div>
        </c:forEach>
      </div>
      <%--       <div class="global__padding_16">
          <div class="kw__box" id="psnkeywords_list">
          	<c:forEach items="${keywords}" var="keyword" varStatus="status">
            	<div class="kw-chip_medium">${keyword.keyWords }</div>
          	</c:forEach>
          </div>
        </div> --%>
  </c:if>
</div>
</div>
