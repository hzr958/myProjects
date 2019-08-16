<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${prjPubList }" var="pubInfo">
  <div class="main-list__item cont_r-list dev_pub_list_div" style=" padding: 4px 0px 10px 0px; margin: 0px;border-bottom: 1px dashed #ddd;">
    <input type="hidden" name="des3PubId" id="des3PubId_${pubInfo.pubId}" value="${pubInfo.des3PubId}">
    <div class="main-list__item_content">
      <div style="display: flex; align-items: flex-start;">
        <c:if test="${pubInfo.hasFulltext == 1}">
          <div class="file-left file-left__border  file-left__download " style="position: relative; flex-shrink: 0; margin-bottom: 12px;"
            onclick="location.href = '${pubInfo.fullTextDownloadUrl}';">
            <img src="${empty pubInfo.fullTextImgUrl ? false : pubInfo.fullTextImgUrl}" name="fullImg"
              des3PubId="${pubInfo.des3PubId}" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            <div class='pub-idx__full-text_newbox-box_load dev_img_title' title="下载全文"></div>
          </div>
        </c:if>
        <c:if test="${pubInfo.hasFulltext == 0}">
            <div class="file-left file-left__border  file-left__download fileupload__box-border"
              style="position: relative; flex-shrink: 0; margin-bottom: 12px;"
              onclick="Pubdetails.requestPdwhFullText('${pubInfo.des3PubId}')">
              <img src="${resmod}/images_v5/images2016/file_img.jpg">
              <div class="pub-idx__full-text_newbox-box dev_img_title" title="请求全文"></div>
              <div class="pub-idx__full-text_newbox-box_request" title="请求全文"></div>
            </div>
        </c:if>
        <div class="file-rigth file-rigth_container" style="margin-left: 20px;">
          <div style="align-items: flex-start;">
            <div class="file-rigth-title file-rigth-container dev_pub_title pub-idx__main_title-multipleline" style="height: 40px; overflow: hidden; width: 562px; font-weight: bold;">
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
                <a onclick="Pub.pdwhAward('${pubInfo.des3PubId}',this)" isAward="${pubInfo.isAward}"
                  class="manage-one mr20" style="">
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> 赞
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
                <a onclick="Pub.pdwhAward('${pubInfo.des3PubId}',this)" isAward="${pubInfo.isAward}"
                  class="manage-one mr20" style="">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> 取消赞
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
              <a class="dev_pub_share_${pubInfo.pubId} manage-one mr20"
                onclick="initShare('${pubInfo.des3PubId }',this);"
                resId="<iris:des3 code='${pubInfo.pubId}'/>" resType="22" des3ResId="${pubInfo.des3PubId }"
                databaseType="2" pubId="${pubInfo.pubId}" dbId style="">
            <div class="new-Standard_Function-bar_item">
              <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                class="new-Standard_Function-bar_item-title"> 分享
                    <c:if test="${pubInfo.shareCount > 0 && pubInfo.shareCount>=1000}">
                       (1k+)
                    </c:if>
                    <c:if test="${pubInfo.shareCount > 0 && pubInfo.shareCount<1000}">
                        (${pubInfo.shareCount})
                    </c:if>
              </span>
            </div>
            </a>
            <a class="manage-one mr20" style=""
                onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${pubInfo.des3PubId}',this)">
            <div class="new-Standard_Function-bar_item">
              <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
                class="new-Standard_Function-bar_item-title">引用
              </span>
            </div>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions func_container-list" style="padding-right: 12px; width: 240px;">
    </div>
  </div>
</c:forEach>
