<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="representPrjIds" name="representPrjIds" value="${representPrjIds }" />
<input type="hidden" id="representPrjIdsOld" name="representPrjIdsOld" value="${representPrjIds }" />
<div class="container__card representPrjDiv">
  <c:if test="${!empty representPrjList}">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="representPrjDiv.header_title" />
        </div>
      </div>
      <div class="main-list__list">
        <c:forEach items="${representPrjList }" var="representPrj" varStatus="status">
          <input type="hidden" id="representPrjList" value="${representPrj.downUploadFulltext}">
          <div class="main-list__item">
            <div class="main-list__item_content">
              <div class="pub-idx_medium">
                <div class="pub-idx__base-info">
                  <%--      <div class="pub-idx__full-text_box">
                    <c:choose>
                  <c:when test="${not empty representPrj.fulltextFileId && representPrj.downUploadFulltext==true}">
                    <div class="pub-idx__full-text_img" id="fulltext_downlink"><a><img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" onclick ="project.download('<iris:des3 code='${representPrj.fulltextFileId}'/>')"></a></div>
                   </c:when>
                    <c:when test="${not empty representPrj.fulltextFileId && isMySelf!=false}">
                     <div class="pub-idx__full-text_img" id="fulltext_downlink"><a><img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" onclick ="project.download('<iris:des3 code='${representPrj.fulltextFileId}'/>')"></a></div>
                   </c:when>
                   <c:otherwise>
                     <div class="pub-idx__full-text_img" id="fulltext_downlink"><img src="/resscmwebsns/images_v5/images2016/file_img.jpg"></div>
                   </c:otherwise>
                   </c:choose> 
                  </div> --%>
                  <div class="pub-idx__main_box">
                    <div class="pub-idx__main">
                      <div class="pub-idx__main_title">
                        <a href="/prjweb/outside/project/detailsshow?des3PrjId=${representPrj.des3Id}" target="_blank">${representPrj.prjInfo.showTitle}</a>
                      </div>
                      <div class="pub-idx__main_author">${representPrj.prjInfo.showAuthorNames }</div>
                      <div class="pub-idx__main_src">${representPrj.prjInfo.showBriefDesc }</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
  </c:if>
</div>
</div>
</div>