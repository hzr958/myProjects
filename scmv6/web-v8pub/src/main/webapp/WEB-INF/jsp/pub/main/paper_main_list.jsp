<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<c:forEach items="${pubListVO.resultList }" var="pubInfo">
  <div class="main-list__item cont_r-list dev_pub_list_div" style=" padding: 4px 0px 10px 0px; margin: 0px;">
    <input type="hidden" name="des3PubId" id="des3PubId_${pubInfo.pubId}" value="${pubInfo.des3PubId}">
    <div class="main-list__item_content">
      <div style="display: flex; align-items: flex-start;">
        <c:if test="${pubInfo.hasFulltext == 1}">
          <div class="file-left file-left__border  file-left__download " style="position: relative; flex-shrink: 0; margin-bottom: 12px;"
            onclick="location.href = '${pubInfo.fullTextDownloadUrl}';">
            <img src="${empty pubInfo.fullTextImgUrl ? false : pubInfo.fullTextImgUrl}" name="fullImg"
              des3PubId="${pubInfo.des3PubId}" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            <div class='pub-idx__full-text_newbox-box_load dev_img_title' title="<spring:message code='pub.download'/>"></div>
          </div>
        </c:if>
        <c:if test="${pubInfo.hasFulltext == 0}">
          <c:if test="${pubInfo.pubDb == 'PDWH' }">
            <div class="file-left file-left__border  file-left__download fileupload__box-border"
              style="position: relative; flex-shrink: 0; margin-bottom: 12px;"
              onclick="Pubdetails.requestPdwhFullText('${pubInfo.des3PubId}')">
              <img src="${resmod}/images_v5/images2016/file_img.jpg">
              <div class="pub-idx__full-text_newbox-box dev_img_title" title="<spring:message code='pub.request'/>"></div>
              <div class="pub-idx__full-text_newbox-box_request" title="<spring:message code='pub.request'/>"></div>
            </div>
          </c:if>
          <c:if test="${pubInfo.pubDb == 'SNS' }">
            <c:if test="${ not empty pubInfo.mySelfPub && pubInfo.mySelfPub}">
              <div class="pub-idx__full-text_box dev_pub_del_${pubInfo.pubId} pub-idx__full-text_img"
                style="display: flex; flex-shrink: 0; height: 92px; margin-bottom: 12px;">
                <div class="pub-idx__full-text_img" style="position: relative">
                  <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'" style="width: 72px; height: 92px;">
                  <!-- 上传全文 start -->
                  <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                    des3Id="${pubInfo.pubId}">
                    <div class="fileupload__box" onclick="fileuploadBoxOpenInputClick(event);"
                      title="<spring:message code="pub.upload"/>">
                      <div class="fileupload__core initial_shown">
                        <div class="fileupload__initial">
                          <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                            src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator"> <input
                            type="file" class="fileupload__input" style='display: none;'>
                        </div>
                        <div class="fileupload__progress" style="margin: 0px -1px 3px 0px;">
                          <canvas width="56" height="56"></canvas>
                          <div class="fileupload__progress_text"></div>
                        </div>
                        <div class="fileupload__saving" style="margin-top: -50px;">
                          <div class="preloader"></div>
                          <div class="fileupload__saving-text"></div>
                        </div>
                        <div class="fileupload__finish"></div>
                        <div class="fileupload__hint-text" style="display: none">添加全文</div>
                      </div>
                    </div>
                  </div>
                  <!-- 上传全文 start -->
                </div>
              </div>
            </c:if>
            <c:if test="${empty pubInfo.mySelfPub || !pubInfo.mySelfPub}">
              <div class="file-left file-left__border  file-left__download fileupload__box-border"
                style="position: relative; flex-shrink: 0; margin-bottom: 12px;"
                onclick="Pubdetails.requestFullText(this,'<iris:des3 code='${pubInfo.ownerPsnId}'/>','${pubInfo.des3PubId}')">
                <img src="${resmod}/images_v5/images2016/file_img.jpg">
                <div class="pub-idx__full-text_newbox-box dev_img_title" title="<spring:message code='pub.request'/>"></div>
                <div class="pub-idx__full-text_newbox-box_request" title="<spring:message code='pub.request'/>"></div>
              </div>
            </c:if>
          </c:if>
        </c:if>
        <div class="file-rigth file-rigth_container" style="margin-left: 20px;">
          <div style="align-items: flex-start;">
            <div class="file-rigth-title file-rigth-container dev_pub_title pub-idx__main_title-multipleline" style="height: 40px; overflow: hidden; width: 562px;">
              <c:choose>
                <c:when test="${not empty pubInfo.pubIndexUrl}">
                  <a class="multipleline-ellipsis__content-box" href="${pubInfo.pubIndexUrl}" target="_blank" title='${pubInfo.title}'>${pubInfo.title}</a>
                </c:when>
                <c:otherwise>
                  <a class="multipleline-ellipsis__content-box" onclick="Pub.newPubDetail('${pubInfo.des3PubId}');" title='${pubInfo.title}'>${pubInfo.title}</a>
                </c:otherwise>
              </c:choose>
            </div>
            <div class="p1 file-rigth-container file-rigth-container_author" style="line-height: 24px;" title="${pubInfo.authorNames}">${pubInfo.authorNames}</div>
            <div class="p1 file-rigth-container file-rigth-container_source" style="color: #999; line-height: 20px;" title="${pubInfo.briefDesc}">${pubInfo.briefDesc}</div>
          </div>
          <div class="new-Standard_Function-bar">
            <c:if test="${pubInfo.isAward == 0}">
              <c:if test="${pubInfo.pubDb == 'PDWH' }">
                <a onclick="Pub.pdwhAward('${pubInfo.des3PubId}',this)" isAward="${pubInfo.isAward}"
                  class="manage-one mr20" style="">
              </c:if>
              <c:if test="${pubInfo.pubDb == 'SNS' }">
                <a onclick="Pub.pubAward('${pubInfo.des3PubId}',this)" isAward="${pubInfo.isAward}"
                  class="manage-one mr20" style="">
              </c:if>
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <spring:message code="pub.like" />
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount>=1000}">
                         (1k+)
                      </c:if>
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount<1000}">
                          (${pubInfo.awardCount})
                      </c:if>
                </span>
              </div>
              </a>
            </c:if>
            <c:if test="${pubInfo.isAward == 1}">
              <c:if test="${pubInfo.pubDb == 'PDWH' }">
                <a onclick="Pub.pdwhAward('${pubInfo.des3PubId}',this)" isAward="${pubInfo.isAward}"
                  class="manage-one mr20" style="">
              </c:if>
              <c:if test="${pubInfo.pubDb == 'SNS' }">
                <a onclick="Pub.pubAward('${pubInfo.des3PubId}',this)" isAward="${pubInfo.isAward}"
                  class="manage-one mr20" style="">
              </c:if>
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <spring:message code="pub.unlike" />
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount>=1000}">
                         (1k+)
                      </c:if>
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount<1000}">
                          (${pubInfo.awardCount})
                      </c:if>
                </span>
              </div>
              </a>
            </c:if>
            <c:if test="${pubInfo.pubDb == 'PDWH' }">
              <a href="${pubInfo.pubIndexUrl}" class="tmanage-one mr20" style="">
            </c:if>
            <c:if test="${pubInfo.pubDb == 'SNS' }">
              <c:choose>
                <c:when test="${not empty pubInfo.pubIndexUrl }">
                  <a href="${pubInfo.pubIndexUrl}" class="tmanage-one mr20" style="">
                </c:when>
                <c:otherwise>
                  <a onclick="Pub.newPubDetail('${pubInfo.des3PubId}');" class="tmanage-one mr20" style=""></a>
                </c:otherwise>
              </c:choose>
            </c:if>
            <div class="new-Standard_Function-bar_item">
              <i class="new-Standard_function-icon new-Standard_comment-icon"></i> <span
                class="new-Standard_Function-bar_item-title"> <spring:message code="maint.pubview.label.comment" />
                  <c:if test="${pubInfo.commentCount > 0 && pubInfo.commentCount>=1000}">
                     (1k+)
                  </c:if>
                  <c:if test="${pubInfo.commentCount > 0 && pubInfo.commentCount<1000}">
                      (${pubInfo.commentCount})
                  </c:if>
              </span>
            </div>
            <c:if test="${pubInfo.pubDb == 'PDWH' }">
              <a class="dev_pub_share_${pubInfo.pubId} manage-one mr20"
                onclick="initShare('${pubInfo.des3PubId }',this);"
                resId="<iris:des3 code='${pubInfo.pubId}'/>" resType="22" des3ResId="${pubInfo.des3PubId }"
                databaseType="2" pubId="${pubInfo.pubId}" dbId style="">
            </c:if>
            <c:if test="${pubInfo.pubDb == 'SNS' }">
              <a class="dev_pub_share_${pubInfo.pubId} manage-one mr20"
                onclick="Pub.pubCite(this);" resId="<iris:des3 code='${pubInfo.pubId}'/>" resType="1" pubId="${pubInfo.pubId}" style="">
            </c:if>
            <div class="new-Standard_Function-bar_item">
              <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                class="new-Standard_Function-bar_item-title"> <spring:message
                  code="publication.recommend.btn.share" /> 
                    <c:if test="${pubInfo.shareCount > 0 && pubInfo.shareCount>=1000}">
                       (1k+)
                    </c:if>
                    <c:if test="${pubInfo.shareCount > 0 && pubInfo.shareCount<1000}">
                        (${pubInfo.shareCount})
                    </c:if>
              </span>
            </div>
            </a>
            <c:if test="${pubInfo.pubDb == 'PDWH' }">
              <a class="manage-one mr20" style=""
                onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${pubInfo.des3PubId}',this)">
            </c:if>
            <c:if test="${pubInfo.pubDb == 'SNS' }">
              <a class="manage-one mr20" style=""
                onclick="Pub.showSnsQuote('/pub/ajaxpubquote','${pubInfo.des3PubId}',this)">
            </c:if>
            <div class="new-Standard_Function-bar_item">
              <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
                class="new-Standard_Function-bar_item-title"> <spring:message code='common.cite' />
              </span>
            </div>
            </a> <a class="manage-one mr20" collected="1"
              onclick="Pub.deleteCollectedPub('${pubInfo.des3PubId}','${pubInfo.pubDb }',this)"
              des3PubId="${pubInfo.des3PubId}" pubDb="${pubInfo.pubDb }">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <spring:message code='publication.btn.cancel' />
                </span>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions func_container-list" style="padding-right: 12px; width: 240px;">
      <div class="func_container-item">
        <c:if test="${pubInfo.readCount > 0}">
          <div>${pubInfo.readCount}</div>
          <div class="">
            <spring:message code='psn.pub.statistics.readCount' />
          </div>
        </c:if>
      </div>
      <div class="func_container-item">
        <c:if test="${pubInfo.downloadCount > 0}">
          <div>${pubInfo.downloadCount}</div>
          <div class="">
            <spring:message code='psn.pub.statistics.downloadCount' />
          </div>
        </c:if>
      </div>
      <div class="func_container-item">
        <c:if test="${pubInfo.citedTimes> 0}">
          <div>${pubInfo.citedTimes}</div>
          <div class="">
            <spring:message code='psn.pub.statistics.citedCount' />
          </div>
        </c:if>
      </div>
    </div>
  </div>
</c:forEach>
