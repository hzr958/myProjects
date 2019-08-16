<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/outside.pub.js"></script>
<script type="text/javascript">
    var shareI18 = '<spring:message code="publication.recommend.btn.share"/>';
    $(function() {
        //加载上传全文的展示框
        if ($("#isLogin").val() == "true" && Pub.loadPubFulltext !== undefined ) {
            Pub.loadPubFulltext();
        }
        if (document
                .getElementsByClassName("container__effect-hindex_right-item")) {
            var overlist = document
                    .getElementsByClassName("container__effect-hindex_right-item");
            for (var i = 0; i < overlist.length; i++) {
                overlist[i].onmouseover = function() {
                    if (!this
                            .querySelector(".container__effect-hindex_right-item_detaile").classList
                            .contains("afterbackcolor")) {
                        this
                                .querySelector(".container__effect-hindex_right-item_detaile").classList
                                .add("afterbackcolor");
                    }
                }
                overlist[i].onmouseleave = function() {
                    if (this
                            .querySelector(".container__effect-hindex_right-item_detaile").classList
                            .contains("afterbackcolor")) {
                        this
                                .querySelector(".container__effect-hindex_right-item_detaile").classList
                                .remove("afterbackcolor");
                    }
                }
            }
        }
        $(".dev_inf_pub_title").each(function() {
            $(this).attr("title", $(this).text());
        });
    });
    //初始化 分享 插件
    function initSharePlugin(obj) {
        if (SmateShare.timeOut && SmateShare.timeOut == true)
            return;
        if (locale == "en_US") {
            $(obj).dynSharePullMode({
                'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
                'language' : 'en_US'
            });
        } else {
            $(obj).dynSharePullMode({
                'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
            });
        }
    };
    //==============================
    function sharePsnCallback(dynId, shareContent, resId, pubId, isB2T,
            receiverGrpId) {
        var count = Number($('.dev_pub_share_' + pubId).text().replace(
                /[\D]/ig, "")) + 1;
        $('.dev_pub_share_' + pubId).text(shareI18 + "(" + count + ")");
    }
    function shareGrpCallback(dynId, shareContent, resId, pubId, isB2T,
            receiverGrpId) {
        shareCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId);
    }
    //==============================
    //分享回调
    function shareCallback(dynId, shareContent, resId, pubId, isB2T,
            receiverGrpId) {
        var count = Number($('.dev_pub_share_' + pubId).text().replace(
                /[\D]/ig, "")) + 1;
        if (isB2T) {
            $('.dev_pub_share_' + pubId).text(shareI18 + "(" + count + ")");
        } else {
            $.ajax({
                url : '/pubweb/pubopt/ajaxsharecount',
                type : 'post',
                dataType : 'json',
                data : {
                    'des3ResId' : resId,
                    'des3GrpId' : receiverGrpId
                },
                success : function(data) {
                    if (data.result == "success") {
                        $('.dev_pub_share_' + pubId).text(
                                shareI18 + "(" + count + ")");
                    }
                }
            });
        }
    };
</script>
<c:if test="${fn:length(pubVo.pubHindexList) > 0 }">
  <div class="container__effect-hindex_right">
    <c:if test="${locale=='en_US'}">
      <div style="width: 76%;">
        <span class="container__effect-hindex_right-title">
          <spring:message code="psnInfluence.visit.h_indexSuggestionsTitle" />
        </span>
        <span class="container__effect-hindex_right-cnt"
          title="<spring:message code="psnInfluence.visit.h_indexSuggestionsCnt"/>" style="width: 400px;">
          <spring:message code="psnInfluence.visit.h_indexSuggestionsCnt" />
        </span>
      </div>
      <span class="container__effect-hindex_right-item_checkall"
        style="width: 24%; margin-left: 13px; margin-bottom: 22px;">
    </c:if>
    <c:if test="${locale=='zh_CN'}">
      <div style="width: 110%; display: flex; align-items: center;">
        <span class="container__effect-hindex_right-title">
          <spring:message code="psnInfluence.visit.h_indexSuggestionsTitle" />
        </span>
        <span class="container__effect-hindex_right-cnt"
          title="<spring:message code="psnInfluence.visit.h_indexSuggestionsCnt"/>">
          <spring:message code="psnInfluence.visit.h_indexSuggestionsCnt" />
        </span>
      </div>
      <span class="container__effect-hindex_right-item_checkall" style="width: 23%;">
    </c:if>
    <c:if test="${pubVo.hasLogin == 'yes'}">
      <a href="/psnweb/homepage/show?module=pub&sortBy=citedTimes&des3PsnId=${pubVo.des3PsnId}" target="_blank">
        <spring:message code="psnInfluence.visit.viewAllPublication" />
      </a>
    </c:if>
    <c:if test="${pubVo.hasLogin == 'no'}">
      <a href="/psnweb/outside/homepage?module=pub&sortBy=citedTimes&des3PsnId=${pubVo.des3PsnId}" target="_blank">
        <spring:message code="psnInfluence.visit.viewAllPublication" />
      </a>
    </c:if>
    </span>
  </div>
  <div class="container__effect-hindex_right-container">
    <c:forEach items="${pubVo.pubHindexList }" var="pub">
      <div class="container__effect-hindex_right-item dev_pub_del_${pub.pubId } dev_pub_list_div">
        <div class="container__effect-hindex_right-item_content">
          <div class="container__effect-hindex_right-item_content-avator pub-idx__full-text_img">
            <c:if test="${pubVo.self == 'yes'}">
              <c:if test="${not empty pub.fullTextDownloadUrl }">
                <a href="${pub.fullTextDownloadUrl}">
                  <img src="${pub.fullTextImgUrl}" class="dev_fulltext_download dev_pub_img" style="cursor: pointer;"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'"
                    title="<spring:message code="pub.download"/>" />
                </a>
                <a href="${pub.fullTextDownloadUrl}" class="new-tip_container-content"
                  title="<spring:message code="pub.download"/>">
                  <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
                  <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                </a>
              </c:if>
              <c:if test="${empty pub.fullTextDownloadUrl }">
                <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                <!-- 上传全文 start -->
                <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1" des3Id="${pub.pubId }"
                  style="width: 63px; height: 77px;">
                  <div class="fileupload__box" onclick="fileuploadBoxOpenInputClick(event);"
                    title="<spring:message code="pub.upload"/>">
                    <div class="fileupload__core initial_shown">
                      <div class="fileupload__initial">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                        <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                        <input type="file" class="fileupload__input" style='display: none;'>
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
              </c:if>
            </c:if>
            <c:if test="${pubVo.self == 'no'}">
              <c:if test="${not empty pub.fullTextDownloadUrl && pub.fullTextPermission == 0}">
                <a href="${pub.fullTextDownloadUrl}">
                  <img src="${pub.fullTextImgUrl}" class="dev_fulltext_download dev_pub_img" style="cursor: pointer;"
                    title='<spring:message code="pub.details.fulltext.download_fulltext" />'
                    onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" />
                </a>
                <a href="${pub.fullTextDownloadUrl}" class="new-tip_container-content"
                  title="<spring:message code="pub.download"/>">
                  <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1">
                  <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                </a>
              </c:if>
              <c:if test="${empty pub.fullTextDownloadUrl || pub.fullTextPermission != 0}">
                 <c:if test="${empty pub.fullTextDownloadUrl}">
                <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                 </c:if> 
                  <c:if test="${not empty pub.fullTextDownloadUrl}">
                   <img src="${pub.fullTextImgUrl}" class="dev_pub_img"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
                  </c:if>
                <!-- 请求全文 start -->
                <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                  des3Id="${pub.des3PubId}" style="width: 63px; height: 77px;">
                  <div class="fileupload__box"
                    onclick="Pubdetails.requestFullText(this,'${pub.des3OwnerPsnId}','${pub.des3PubId}');"
                    title="<spring:message code="pub.details.fulltext.req_fulltext"/>">
                    <div class="fileupload__core initial_shown">
                      <div class="fileupload__initial">
                        <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator">
                        <img src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator">
                        <!-- 这个 input 框要加上,要不上传全文插件scm-pc_filedragbox.js报错-->
                        <input type="file" class="fileupload__input" style='display: none;'>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 请求全文 start -->
              </c:if>
            </c:if>
            <!-- <img src=""> -->
          </div>
          <div class="container__effect-hindex_right-item_inforbox">
            <div
              class="container__effect-hindex_right-item_detaile container__effect-hindex_right-item_detaile-ellipsis dev_pub_title"
              style="height: 40px; overflow: hidden;">
              <div title="<c:out value="${pub.title}"/>" class="multipleline-ellipsis__content-box dev_inf_pub_title" style="white-space: normal;">
                <c:if test="${not empty pub.pubIndexUrl}">
                  <a href="${pub.pubIndexUrl}" target="_Blank">
                </c:if>
                <c:if test="${empty pub.pubIndexUrl}">
                  <a onclick="Pub.pubDetail('${pub.des3PubId}');">
                </c:if>${pub.title}</a>
              </div>
            </div>
            <div class="container__effect-hindex_right-item_author">
              <a title="${pub.authorNamesHtml}">${pub.authorNames}</a>
            </div>
            <div class="container__effect-hindex_right-item_source">
              <a title="${pub.briefDesc}">${pub.briefDesc}</a>
            </div>
            <div class="container__effect-hindex_right-item_footer">
              <c:if test="${pubVo.self == 'no'}">
                <div class="dev_award  container__effect-hindex_right-item_footer-item" isAward="${pub.isAward}"
                  onclick="Pub.pubAward('${pub.des3PubId}',this)">
                  <c:if test="${pub.isAward == 0}">
                    <div style="cursor: pointer;" class="fabulous_icon"></div>
                    <span style="cursor: pointer;"
                      class="container__effect-hindex_right-item_footer-fabulous dev_pub_award dev_psn_influence dev_pub_award_item">
                      <spring:message code="pub.like" />
                      <c:if test="${pub.awardCount > 0}">(${pub.awardCount})</c:if>
                    </span>
                  </c:if>
                  <c:if test="${pub.isAward == 1}">
                    <div style="cursor: pointer;" class="fabulous_icon fabulous_icon-checked"></div>
                    <span style="cursor: pointer;"
                      class="container__effect-hindex_right-item_footer-fabulous dev_cancel_award dev_pub_award dev_psn_influence dev_pub_award_item">
                      <spring:message code="pub.unlike" />
                      <c:if test="${pub.awardCount > 0}">(${pub.awardCount})</c:if>
                    </span>
                  </c:if>
                </div>
                <div class="container__effect-hindex_right-item_footer-item" style="margin-left: 8px;"
                  onclick="SmateShare.getPsnPubListSareParam(this); initSharePlugin(this);" resId="${pub.des3PubId}"
                  resType="1" pubId="${pub.pubId}">
                  <div style="cursor: pointer;" class="share_icon"></div>
                  <span style="cursor: pointer;"
                    class="container__effect-hindex_right-item_footer-share dev_pub_share_${pub.pubId}">
                    <spring:message code="publication.recommend.btn.share" />
                    <c:if test="${pub.shareCount > 0}">(${pub.shareCount})</c:if>
                  </span>
                </div>
              </c:if>
              <c:if test="${pubVo.self == 'yes'}">
                <div class="container__effect-hindex_right-item_footer-item"
                  onclick="SmateShare.getPsnPubListSareParam(this); initSharePlugin(this);" resId="${pub.des3PubId}"
                  resType="1" pubId="${pub.pubId}">
                  <div style="cursor: pointer;" class="share_icon"></div>
                  <span style="cursor: pointer;"
                    class="container__effect-hindex_right-item_footer-share dev_pub_share_${pub.pubId}">
                    <spring:message code="publication.recommend.btn.share" />
                    <c:if test="${pub.shareCount > 0}">(${pub.shareCount})</c:if>
                  </span>
                </div>
              </c:if>
              <c:if test="${pubVo.self == 'no'}">
                <div  class="container__effect-hindex_right-item_footer-item" style=" margin-left: 8px;"
                  onclick="Pub.showSnsQuote('/pub/ajaxpubquote','${pub.des3PubId}',this);">
                  <div style="cursor: pointer;" class="quote_icon"></div>
                  <span style="cursor: pointer;" class="container__effect-hindex_right-item_footer-quote">
                    <spring:message code="common.cite" />
                  </span>
                </div>
              </c:if>
            </div>
          </div>
        </div>
        <div class="container__effect-hindex_right-item_func">
          <span class="container__effect-hindex_right-item_func-cont">${pub.citedTimes}</span>
          <span class="container__effect-hindex_right-item_func-detail">
            <spring:message code="common.cite" />
          </span>
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>