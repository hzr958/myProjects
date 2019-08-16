<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<!-- <div class="tab_style" id="pubListTal" list-main="pub_list">
 -->
<!-- <table border="0" cellpadding="0" cellspacing="0" class="tab_style" id="pubListTal"> -->
<c:forEach items="${pubListVO.resultList }" var="pubInfo">
  <input type="hidden" name="articleType" id="articleType" value="1" />
  <input type="hidden" alt="${snsctx}/dynamic/ajaxShareMaint?TB_iframe=true&height=580&width=720"
    title="<spring:message code='dyn.common.label.share'/>" class="thickbox" id="viewSharePubBtn" />
  <input name="des3pubId" id="des3pubId" type="hidden" value="${pubInfo.des3PubId }" />
  <input name="dbId" type="hidden" value="${pubInfo.des3PubId }" />
  <input name="language" type="hidden" value="${pubInfo.isCollected }" />
  <input name="snspubId" type="hidden">
  <div class="cont_r-list" style="padding: 12px 0px;">
    <div style="width: 725px; padding: 0px; margin: 0px;" class="main-list__item">
      <div style="display: flex; align-items: flex-start;">
        <c:if test="${pubInfo.hasFulltext == 1}">
          <div class="file-left file-left__border  file-left__download" style="position: relative; margin-bottom: 12px;"
            onclick="location.href = '${pubInfo.fullTextDownloadUrl}'">
            <img src="${empty pubInfo.fullTextImgUrl ? false :pubInfo.fullTextImgUrl}" name="fullImg"
              des3PubId="${pubInfo.des3PubId }" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            <div class='pub-idx__full-text_newbox-box_load dev_img_title'
              title="<spring:message code='pdwh.pub.search.download'/>"></div>
          </div>
        </c:if>
        <c:if test="${pubInfo.hasFulltext ==0}">
          <div class="file-left file-left__border  file-left__download fileupload__box-border"
            style="position: relative; flex-shrink: 0; margin-bottom: 12px;"
            onclick="Pubdetails.requestPdwhFullText('${pubInfo.des3PubId }')">
            <img src="${resmod }/images_v5/images2016/file_img.jpg">
            <div class="pub-idx__full-text_newbox-box dev_img_title"
              title="<spring:message code='pdwh.pub.search.request.fulltext'/>"></div>
            <div class="pub-idx__full-text_newbox-box_request"
              title="<spring:message code='pdwh.pub.search.request.fulltext'/>"></div>
          </div>
        </c:if>
        <div class="file-rigth file-rigth_container" style="margin-left: 20px;">
          <div class="file-rigth-box">
            <div class="file-rigth-title file-rigth-container dev_pubdetails_title  pub-idx__main_title-multipleline" style="width: 600px!important;height: 40px;overflow: hidden; line-height: 20px!important;">
              <a class="multipleline-ellipsis__content-box" title="<c:out value="${pubInfo.title }"/>" href="${pubInfo.pubIndexUrl}" target="_blank">${pubInfo.title }</a>
            </div>
            <div class="p1 file-rigth-container dev_pubdetails_author" style="width: 600px!important">
              <span style="cursor: default" title="${pubInfo.authorNames}">${pubInfo.authorNames}</span>
            </div>
            <div class="file-rigth-container dev_pubdetails_source" title="${pubInfo.briefDesc}">
              ${pubInfo.briefDesc}
            </div>
          </div>
          <%@ include file="/WEB-INF/jsp/pub/search/operations_list.jsp"%>
        </div>
      </div>
    </div>
    <div class="func_container-list">
      <div class="func_container-item" style="width: 50px;">
        <c:if test="${!empty pubInfo.readCount && pubInfo.readCount!=0}">
          <div>${pubInfo.readCount}</div>
          <div class="">
            <spring:message code="pub.search.read" />
          </div>
        </c:if>
      </div>
      <div class="func_container-item dev_down-count" des3PubId="${pubInfo.des3PubId }" style="width: 50px;">
        <c:if test="${!empty pubInfo.downloadCount && pubInfo.downloadCount!=0}">
          <div>${pubInfo.downloadCount}</div>
          <div class="">
            <spring:message code="pub.search.download" />
          </div>
        </c:if>
      </div>
      <div class="func_container-item" style="width: 50px;">
        <c:if test="${!empty pubInfo.citedTimes && pubInfo.citedTimes!=0}">
          <div>${pubInfo.citedTimes}</div>
          <div class="">
            <spring:message code="pub.search.citations" />
          </div>
        </c:if>
      </div>
    </div>
  </div>
</c:forEach>
<!--  </table> -->
<!-- </div> -->
