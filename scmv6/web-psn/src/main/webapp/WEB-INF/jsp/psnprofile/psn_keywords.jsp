<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${!empty keywords || isMySelf== 'true' }">
  <div class="container__card" style="display: none;" id="keywordsModule">
    <div class="module-card__box" id="rskeywords">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name='homepage.profile.title.expertise' />
        </div>
        <button class="button_main button_icon button_light-grey operationBtn"
          onclick="javascript:showPsnKeyWordsBox();">
          <i class="material-icons">edit</i>
        </button>
      </div>
      <c:if test="${!empty keywords }">
        <div class="global__padding_16">
          <c:forEach items="${keywords }" var="keyword" varStatus="status">
            <div class="kw__box" style="align-items: center; width: 80%;">
              <div class="kw-stick" style="margin-right: 0px; height: 30px;">
                <div class="kw-stick__stats" style="min-width: 6px;" id="idenSum_${keyword.id }"
                  onclick="showIdentifyPsnBox('${keyword.id}');">${keyword.identificationSum }</div>
                <div class="kw-stick__word kw-stick__word-normal_size" title="<c:out value='${keyword.keyWords }'/>">${keyword.keyWords }</div>
                <div class="kw-stick__endorse " id="area_${keyword.id }"
                  style="${(isMySelf || keyword.hasIdentified) ? 'display:none;' : ''}"
                  onclick="identifyKeyword('${keyword.id}')">
                  <i class="normal-global_icon  normal-global_identification-icon"
                    title='<s:text name="homepage.profile.rentong"/>' style="color: #333;"></i>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </div>
</c:if>
<%@ include file="keyword_identific_psnlist.jsp"%>
