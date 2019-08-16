<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(
			function() {
				var overlist = document.getElementsByClassName("file-left");
				for (var i = 0; i < overlist.length; i++) {
					overlist[i].onmouseover = function() {
						this.querySelector(".pub-idx__full-text_newbox-box").style.display = "block";
					}
				}
			})
</script>
<div class="js_listinfo" smate_count='${pubListVO.totalCount}'></div>
<input type="hidden" class="dev_fulltext-pubs" value='${fullTextPubs}' />
<c:forEach items="${pubListVO.resultList }" var="pubInfo">
  <div class="cont_r-list" style="padding: 12px 0px;">
    <div style="width: 700px; padding: 0px; margin: 0px;" class="main-list__item">
      <div style="display: flex;">
        <c:if test="${pubInfo.hasFulltext == 1}">
          <div class="file-left file-left__border  file-left__download " style="position: relative; margin-bottom: 12px;"
            onclick="location.href = '${pubInfo.fullTextDownloadUrl}'">
            <img src="${empty pubInfo.fullTextImgUrl ? false :pubInfo.fullTextImgUrl}" name="fullImg"
              des3PubId="${pubInfo.des3PubId}" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
            <div class='pub-idx__full-text_newbox-box_load dev_img_title' title="<spring:message code="pub.download" />"></div>
          </div>
        </c:if>
        <c:if test="${pubInfo.hasFulltext == 0}">
          <div class="file-left file-left__border  file-left__download fileupload__box-border"
            style="position: relative; flex-shrink: 0; margin-bottom: 12px;" onclick="Pubdetails.requestPdwhFullText('${pubInfo.des3PubId}')">
            <img src="${resmod}/images_v5/images2016/file_img.jpg">
            <div class="pub-idx__full-text_newbox-box dev_img_title" title="<spring:message code="pub.request"/>"></div>
            <div class="pub-idx__full-text_newbox-box_request" title="<spring:message code="pub.request"/>"></div>
          </div>
        </c:if>
        <div class="file-rigth file-rigth_container" style="margin-left: 20px; margin-top: 12px;">
          <div class="file-rigth-box">
            <div class="file-rigth-title file-rigth-container dev_pubdetails_title pub-idx__main_title-multipleline" style="width: 650px; height: 40px; overflow: hidden;">
              <a class="multipleline-ellipsis__content-box" href="${pubInfo.pubIndexUrl }" target="_blank" title="${pubInfo.title}">${pubInfo.title}</a>
            </div>
            <div class="p1 file-rigth-container dev_pubdetails_author" style="width: 600px">
              <span title="${pubInfo.authorNames}">${pubInfo.authorNames}</span>
            </div>
            <div class="file-rigth-container dev_pubdetails_source"  title="${pubInfo.briefDesc}">
               ${pubInfo.briefDesc}
            </div>
            <div class="new-Standard_Function-bar">
              <a class="manage-one mr20" isAward="${pubInfo.isAward}"
                onclick="Pub.pdwhAward('${pubInfo.des3PubId}',this)"> <c:if test="${pubInfo.isAward == 1}">
                  <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected"
                    style="margin-left: 0px;">
                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> 
                    <span class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.unaward" />
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount>=1000}">
                         (1k+)
                      </c:if>
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount<1000}">
                          (${pubInfo.awardCount})
                      </c:if>
                    </span>
                  </div>
                </c:if> <c:if test="${pubInfo.isAward == 0}">
                  <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> 
                    <span class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.award" />
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount>=1000}">
                         (1k+)
                      </c:if>
                      <c:if test="${pubInfo.awardCount > 0 && pubInfo.awardCount<1000}">
                          (${pubInfo.awardCount})
                      </c:if>
                   </span>
                  </div>
                </c:if>
              </a>
              <a href="${pubInfo.pubIndexUrl }" class="manage-one mr20" target="">
                        <div class="new-Standard_Function-bar_item">
                            <i class="new-Standard_function-icon new-Standard_comment-icon"></i>
                            <span class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.coment" />
                                <c:if test="${pubInfo.commentCount > 0 && pubInfo.commentCount>=1000}">
                                   (1k+)
                                </c:if>
                                <c:if test="${pubInfo.commentCount > 0 && pubInfo.commentCount<1000}">
                                    (${pubInfo.commentCount})
                                </c:if>
                           </span>
                        </div> 
                  </a>
              <input type="hidden" class="share_title_input" value='${pubInfo.title}' /> <a
                class="manage-one mr20 dev_pdwhpub_share"
                onclick="initShare('${pubInfo.des3PubId}',this);" resId="${pubInfo.des3PubId}"
                resType="22" databaseType="2" dbId="${pub.dbid}">
                <div class="new-Standard_Function-bar_item">
                  <i class="new-Standard_function-icon new-Standard_Share-icon"></i> 
                  <span class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.share" />                   
                    <c:if test="${pubInfo.shareCount > 0 && pubInfo.shareCount>=1000}">
                       (1k+)
                    </c:if>
                    <c:if test="${pubInfo.shareCount > 0 && pubInfo.shareCount<1000}">
                        (${pubInfo.shareCount})
                    </c:if>
                  </span>
                </div>
              </a> <a class="manage-one mr20 thickbox"
                onclick="Pub.showPdwhQuote('/pub/ajaxpdwhpubquote','${pubInfo.des3PubId}',this)"
                title="<spring:message code='pub.recommend.citations'/>">
                <div class="new-Standard_Function-bar_item">
                  <i class="new-Standard_function-icon new-Standard_Quote-icon"></i> <span
                    class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.citations" />
                  </span>
                </div>
              </a> <a class="manage-one mr20" collected="${pubInfo.isCollected}"
                onclick="Pub.dealCollectedPub('${pubInfo.des3PubId}','PDWH',this)"> <c:if
                  test="${pubInfo.isCollected=='0'}">
                  <div class="new-Standard_Function-bar_item">
                    <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                      class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.collect" />
                    </span>
                  </div>
                </c:if> <c:if test="${pubInfo.isCollected=='1'}">
                  <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                    <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                      class="new-Standard_Function-bar_item-title"><spring:message code="pub.recommend.unsave" />
                    </span>
                  </div>
                </c:if>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="func_container-list" style="margin-right:">
      <div class="func_container-item">
        <c:if test="${!empty pubInfo.readCount && pubInfo.readCount!=0}">
          <div>${pubInfo.readCount}</div>
          <div class="">
            <spring:message code="pub.recommend.read" />
          </div>
        </c:if>
      </div>
      <div class="func_container-item dev_down-count" des3PubId="${pubInfo.des3PubId}">
        <c:if test="${!empty pubInfo.downloadCount && pubInfo.downloadCount!=0}">
          <div>${pubInfo.downloadCount}</div>
          <div class="">
            <spring:message code="pub.recommend.download" />
          </div>
        </c:if>
      </div>
      <div class="func_container-item">
        <c:if test="${!empty pubInfo.citedTimes && pubInfo.citedTimes!=0}">
          <div>${pubInfo.citedTimes}</div>
          <div class="">
            <spring:message code="pub.recommend.citations" />
          </div>
        </c:if>
      </div>
    </div>
  </div>
</c:forEach>
<input type='hidden' id="curPage" value='${pubListVO.pubQueryDTO.pageNo}' />
<input type="hidden" value="${pubListVO.pubQueryDTO.totalPages}" />
<jsp:include page="/common/smate.share.mvc.jsp" />
<!-- 分享操作 -->