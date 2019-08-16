<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__card" id="scienceAreaDiv">
  <c:if test="${!empty scienceAreaList}">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="scienceAreaDiv.header_title" />
        </div>
      </div>
      <div class="global__padding_16">
        <div class="kw__box" id="scienceArea_list">
          <c:forEach items="${scienceAreaList}" var="scienceArea" varStatus="status">
            <div class="kw-chip_medium">${scienceArea.showScienceArea  }</div>
          </c:forEach>
        </div>
      </div>
      <%--         <div class="global__padding_16">
		<c:forEach items="${scienceAreaList }" var = "scienceArea" varStatus="status">
          <div class="kw__box">
            <div class="kw-stick">
               <div class="kw-stick__stats text" id="idenSum_${scienceArea.scienceAreaId }"></div>
               <div class="kw-stick__word">${scienceArea.showScienceArea }</div>
            </div>
          </div>
		</c:forEach>
        </div> --%>
    </div>
  </c:if>
</div>
