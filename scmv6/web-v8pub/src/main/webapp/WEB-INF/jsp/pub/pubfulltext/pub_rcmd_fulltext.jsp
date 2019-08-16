<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test='${page.result.size() ge 1 }'>
  <input type="hidden" class="dev_rcmd_pub_fulltext_count" value="0" />
  <input type="hidden" id="dev_pub_confirmCount" type="hidden" value="${confirmCount}" />
  <div class="dev_nofulltext_count">
    <c:forEach items="${page.result}" var="ft" varStatus="stat">
      <div class="container__card dev_nofulltext_div">
        <div class="promo__box" style="display: flex; flex-direction: column;">
          <div class="promo__box-header">
            <div class="promo__box-title">
              <spring:message code="pub.fulltext.confirm" />
            </div>
            <div class="promo__box-check_all" onclick="Pub.pubftConfirmAll();">
              <spring:message code="pub.more" />
            </div>
          </div>
          <div class="promo__box-container">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img fileupload__box-borde" style="position: relative;">
                    <c:if test="${not empty ft.downloadUrl}">
                      <a href="${ft.downloadUrl }"> <img src="${ft.fullTextImagePath}" style="cursor: pointer;"
                        onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
                      </a>
                      <a href="${ft.downloadUrl }" class="new-tip_container-content"
                        style="position: absolute; opacity: 0.5;" title='<spring:message code="pub.download"/>'> <img
                        src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                        src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                      </a>
                    </c:if>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title dev_pub_title">
                      <a onclick="Pub.newPubDetail('<iris:des3 code='${ft.pub.pubId}'/>');"> ${ft.pub.title} </a>
                    </div>
                    <div class="pub-idx__main_author dev_pub_author" title="${ft.pub.authorNames}">
                      <c:out value="${ft.pub.authorNames}" escapeXml="false" />
                    </div>
                    <div class="pub-idx__main_src dev_pub_src" title="${ft.pub.briefDesc}">${ft.pub.briefDesc}</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="promo__box-check">
              <div class="promo__box-comfirm" onclick="Pub.doConfirmPubft(${ft.id});">
                <spring:message code="pub.confirm.opt" />
              </div>
              <div class="promo__box-refuse" onclick="Pub.doRejectPubft(${ft.id});">
                <spring:message code="pub.ignore.opt" />
              </div>
            </div>
          </div>
          <!--<i onclick="closeNofulltext();" class="promo__close material-icons">close</i>-->
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>
<c:if test='${page.result.size() le 0 }'>
  <input type="hidden" class="dev_no_pupfulltext" value="no" />
</c:if>
