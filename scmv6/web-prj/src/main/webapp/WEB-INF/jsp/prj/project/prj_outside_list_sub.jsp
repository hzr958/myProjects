<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<c:if test="${not empty page.result}">
  <c:forEach items="${page.result}" var="result">
    <div class="main-list__item  main-list__item-botStyle" drawer-id="${result.des3Id}">
      <input type="hidden" id="des3prjId" value="${result.des3Id}"> <input type="hidden"
        id="prjTitle_${result.id}" value="${result.title}"> <input type="hidden" id="prjAbstracts_${result.id}"
        value="${result.abstracts}"> <input type="hidden" id="prjId" value="${result.id}"> <input
        type="hidden" id="des3GroupIds" value="${result.des3GroupId}">
      <!-- <div class="main-list__item_checkbox">
            </div> -->
      <div class="main-list__item_content">
        <div class="pub-idx_medium">
          <div class="pub-idx__base-info">
            <%-- <div class="pub-idx__full-text_box">
                  <c:choose>
                  <c:when test="${not empty result.fulltextField && result.downUploadFulltext==true}">
                    <div class="pub-idx__full-text_img" id="fulltext_downlink"><a><img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" onclick ="project.download('<iris:des3 code='${result.fulltextField}'/>')"></a></div>
                   </c:when>
                   <c:otherwise>
                     <div class="pub-idx__full-text_img" id="fulltext_downlink"><img src="/resscmwebsns/images_v5/images2016/file_img.jpg"></div>
                   </c:otherwise>
                   </c:choose>
                  </div> --%>
            <div class="pub-idx__main_box">
              <div class="pub-idx__main">
                <div class="pub-idx__main_title pub-idx__main_title-multipleline pub-idx__main_title-multipleline-box" style="height: 40px; overflow: hidden;">
                  <a class="multipleline-ellipsis__content-box" href="/prjweb/outside/project/detailsshow?des3PrjId=${result.des3Id}&isfrom=detail" target="_blank" title="${result.title}">${result.title}</a>
                </div>
                <div class="pub-idx__main_author" style="max-width: 849px; min-width: 0px;" title="${result.noneHtmlLableAuthorNames}">${result.authorNames}</div>
                <div class="pub-idx__main_src" title="${result.briefDesc}">${result.briefDesc}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:forEach>
</c:if>