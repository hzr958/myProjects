<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<c:forEach items="${pubListVO.resultList }" var="pubInfo">
  <div class="main-list__item main-list__item-botStyle dev_pub_del_${pubInfo.pubId} dev_pub_list_div"
    id="dev_pub_del_${pubInfo.des3PubId}" resId="${pubInfo.pubId}" resNode="1" style="align-items: flex-start;"
    drawer-id="${pubInfo.des3PubId}">
    <input type="hidden" name="des3PubId" id="des3PubId_${pubInfo.pubId}" value="${pubInfo.des3PubId}">
    <div class="main-list__item_content">
      <div class="pub-idx_medium">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img  fileupload__box-border XXXX" style="position: relative">
              <c:if test="${not empty pubInfo.fullTextDownloadUrl }">
                <c:if test="${pubInfo.fullTextPermission == 0 }">
                  <a href="${pubInfo.fullTextDownloadUrl}"> <img src="${pubInfo.fullTextImgUrl}"
                    class="dev_fulltext_download dev_pub_img" style="cursor: pointer;"
                    title='<spring:message code="pub.download" /> " />'
                    onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" />
                  </a>
                  <a href="${pubInfo.fullTextDownloadUrl}" class="new-tip_container-content"
                    title="<spring:message code="pub.download" />"> <img
                    src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                    src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                  </a>
                </c:if>
                <c:if test="${pubInfo.fullTextPermission != 0 }">
                  <img src="${pubInfo.fullTextImgUrl}" class="dev_pub_img"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg1'">
                  <!-- 请求全文 start -->
                  <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                    des3Id="${pubInfo.des3PubId}">
                    <div class="fileupload__box"
                      onclick="Pub.requestFullText('<iris:des3 code='${pubInfo.ownerPsnId}'/>','${pubInfo.des3PubId}');"
                      title="<spring:message code="pub.details.fulltext.req_fulltext" />">
                      <div class="fileupload__core initial_shown">
                        <div class="fileupload__initial">
                          <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                            src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                          <!-- 这个 input 框要加上,要不上传全文插件scm-pc_filedragbox.js报错-->
                          <input type="file" class="fileupload__input" style='display: none;' />
                        </div>
                      </div>
                    </div>
                  </div>
                  <!-- 请求全文 start -->
                </c:if>
              </c:if>
              <c:if test="${empty pubInfo.fullTextDownloadUrl }">
                <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                <!-- 请求全文 start -->
                <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                  des3Id="${pubInfo.des3PubId}">
                  <div class="fileupload__box"
                    onclick="Pub.requestFullText('<iris:des3 code='${pubInfo.ownerPsnId}'/>','${pubInfo.des3PubId}');"
                    title="<spring:message code="pub.details.fulltext.req_fulltext" />">
                    <div class="fileupload__core initial_shown">
                      <div class="fileupload__initial">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                          src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                        <!-- 这个 input 框要加上,要不上传全文插件scm-pc_filedragbox.js报错-->
                        <input type="file" class="fileupload__input" style='display: none;' />
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 请求全文 start -->
              </c:if>
            </div>
            <div class="pub-idx__full-text_box-content_icon">
              <c:if test="${pubInfo.updateMark==1}">
                <i class="demo-tip_onlineimport-icon" title="<spring:message code='pub.statistics.member.updatemark1'/>"></i>
              </c:if>
              <c:if test="${pubInfo.updateMark==2}">
                <i class="demo-tip_inlineimport-icon" title="<spring:message code='pub.statistics.member.updatemark2'/>"></i>
              </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div
                class="pub-idx__main_title dev_pub_title pub-idx__main_title-multipleline pub-idx__main_title-multipleline-box"
                style="height: 40px; overflow: hidden;">
                <c:if test="${ not empty pubInfo.pubIndexUrl}">
                  <a class="multipleline-ellipsis__content-box" href="${pubInfo.pubIndexUrl}" target="_Blank"
                    title="${pubInfo.title}">
                </c:if>
                <c:if test="${ empty pubInfo.pubIndexUrl}">
                  <a class="multipleline-ellipsis__content-box" onclick="Pub.newPubDetail('${pubInfo.des3PubId}');"
                    title="${pubInfo.title}">
                </c:if>
                ${pubInfo.title} </a>
              </div>
              <div class="pub-idx__main_author dev_pub_author" title="${pubInfo.noneHtmlLableAuthorNames}">${pubInfo.authorNames}</div>
              <div class="pub-idx__main_src dev_pub_src" title="${pubInfo.briefDesc}">${pubInfo.briefDesc}</div>
            </div>
            <ul class="idx-social__list">
              <li class="idx-social__item dev_pub_award" isAward="${pubInfo.isAward}"
                onclick="Pub.pubAward('${pubInfo.des3PubId}',this)"><i class="icon-praise"></i> <span
                class="dev_pub_award_item"><spring:message code="pub.like" /> <c:if
                    test="${pubInfo.awardCount > 0}">(${pubInfo.awardCount})</c:if></span></li>
              <li class="idx-social__item dev_pub_share_${pubInfo.pubId}"
                onclick="SmateShare.getPsnPubListSareParam(this); initSharePlugin(this);" resId="${pubInfo.des3PubId}"
                resType="1" pubId="${pubInfo.pubId}"><i class="icon-share"></i> <spring:message
                  code="publication.recommend.btn.share" /> <c:if test="${pubInfo.shareCount > 0}">(${pubInfo.shareCount})</c:if>
              </li>
              <li class="idx-social__item" onclick="Pub.showSnsQuote('/pub/ajaxpubquote','${pubInfo.des3PubId}',this)">
                <i class="icon-reference"></i> <spring:message code="common.cite" />
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <div class="pro-stats__list pub-list-stats">
          <!-- 阅读 -->
          <div class="pro-stats__item">
            <c:if test="${pubInfo.readCount != null && pubInfo.readCount > 0 }">
              <div class="pro-stats__item_number">${pubInfo.readCount}</div>
              <div class="pro-stats__item_title">
                <spring:message code="pub.reads" />
              </div>
            </c:if>
          </div>
          <!-- 下载 -->
          <div class="pro-stats__item">
            <c:if test="${pubInfo.downloadCount != null && pubInfo.downloadCount > 0 }">
              <div class="pro-stats__item_number">${pubInfo.downloadCount}</div>
              <div class="pro-stats__item_title">
                <spring:message code="psn.pub.statistics.downloadCount" />
              </div>
            </c:if>
          </div>
          <!-- 引用 -->
          <div class="pro-stats__item">
            <c:if test="${pubInfo.citations != null && pubInfo.citations > 0  }">
              <div class="pro-stats__item_number dev_pub_cite_num" id="pubId_${pubInfo.pubId}">${pubInfo.citations}</div>
              <div class="pro-stats__item_title dev_pub_cite_name" id="citeName_${pubInfo.pubId}">
                <spring:message code="pub.citations" />
              </div>
              <div class="preloader active" id="preloader_${pubInfo.pubId}"
                style="height: 28px; width: 28px; display: none;">
                <div class="preloader-ind-cir__box">
                  <div class="preloader-ind-cir__fill">
                    <div class="preloader-ind-cir__arc-box left-half">
                      <div class="preloader-ind-cir__arc"></div>
                    </div>
                    <div class="preloader-ind-cir__gap">
                      <div class="preloader-ind-cir__arc"></div>
                    </div>
                    <div class="preloader-ind-cir__arc-box right-half">
                      <div class="preloader-ind-cir__arc"></div>
                    </div>
                  </div>
                </div>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</c:forEach>
