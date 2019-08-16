<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty prjPubList }">
  <input type="hidden" class="dev_confirm_pub_count" value="0" />
  <div class="dev_nofulltext_count">
    <c:forEach items="${prjPubList}" var="pub" varStatus="stat">
      <div class="container__card dev_nofulltext_div">
        <div class="promo__box" style="display: flex; flex-direction: column;">
          <div class="promo__box-header">
            <div class="promo__box-title">
                                        成果匹配
            </div>
            <div class="promo__box-check_all" onclick="NewProject.toConfirmList('${des3PrjId}');">
                                        查看全部
            </div>
          </div>
          <div class="promo__box-container">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img" style="position: relative; width: 72px; height: 92px;">
                    <c:if test="${not empty pub.fullTextFieId }">
                      <a href="${pub.fullTextDownloadUrl }"> <img src="${pub.fullTextImgUrl}"
                         onerror="this.src='/resmod/images_v5/images2016/file_img1.jpg'"></a>
                      <a href="${pub.fullTextDownloadUrl }" class="new-tip_container-content"
                         title="下载全文"> <img src="/resmod/smate-pc/img/file_ upload1.png"
                         class="new-tip_container-content_tip1"> <img src="/resmod/smate-pc/img/file_ upload.png"
                          class="new-tip_container-content_tip2">
                      </a>
                    </c:if>
                    <c:if test="${empty pub.fullTextFieId }">
                      <img src="${pub.fullTextImgUrl}" style="cursor: default;"
                        onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'" />
                    </c:if>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title dev_pub_title">
                      <a href="${pub.pubIndexUrl}" target="_blank"
                        title='<c:out value="${pub.title}" escapeXml="false"/>'><c:out value="${pub.title}"
                          escapeXml="false" /></a>
                    </div>
                    <div class="pub-idx__main_author dev_pub_author" title="${pub.authorNames}">
                      <c:out value="${pub.authorNames}" escapeXml="false" />
                    </div>
                    <div class="pub-idx__main_src dev_pub_src" title="${pub.briefDesc}">
                      <c:out value="${pub.briefDesc}" escapeXml="false" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="promo__box-check">
              <div class="promo__box-comfirm" onclick="NewProject.pubConfirmOpt(this,'${des3PrjId}','${pub.pubId}','1');">是此项目成果</div>
              <div class="promo__box-refuse" style="background: #fff;border: 1px solid #ccc;line-height: 28px;border-radius: 3px;" onclick="NewProject.pubConfirmOpt(this,'${des3PrjId}','${pub.pubId}','2');">不是</div>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>
<c:if test="${empty prjPubList }">
  <input type="hidden" class="dev_no_pupconfirm" value="no" />
</c:if>
