<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test='${dynRemdResList.size() ge 1 }'>
  <div class="main-list__read-item global__padding_0 dyn_pageready_read">
    <c:forEach items="${dynRemdResList}" var="ft" varStatus="stat">
      <c:if test="${ft.type == 1}">
        <input type="hidden" class="remdPubId" id="remdPubId" value="${ft.des3ResId }">
      </c:if>
      <c:if test="${ft.type == 2}">
        <input type="hidden" class="remdFundId" id="remdFundId" value="${ft.des3ResId }">
      </c:if>
      <div class="container__card">
        <div class="dynamic__box">
          <div class="dynamic-creator__box" style="justify-content: space-between;">
            <div class="dyn-publish_content">
              <span class="dyn-publish_content-author"> <c:if test="${ft.type == 1}">
                  <s:text name="dyn.remd.res.suggestpaper" />
                </c:if> <c:if test="${ft.type == 2}">
                  <s:text name="dyn.remd.res.suggestfund" />
                </c:if>
              </span>
            </div>
            <div class="dynamic-post__operation-box dynamic-post__operation-check">
              <span onclick="dynamic.viewMoreRemd('${ft.type}')"><s:text name="dyn.remd.res.viewmore" /></span>
            </div>
          </div>
          <div class="dynamic-content__main">
            <div class="dynamic-main__setbox" style="flex-direction: column;">
              <div class="dynamic-main__att new-Recommend_container" style="width: 100%;">
                <div class="new-Recommend_infor-avator" style="width: 40px; height:50px; margin-top: 4px;">
                  <c:if test="${ft.type == 1}">
                    <c:if test="${ft.hasFulltext == 1}">
                      <div class="file-left file-left__border  file-left__download " style="position: relative;"
                        onclick="location.href = '${ft.fullTextDownloadUrl}'">
                        <img src="${empty ft.fullTextUrl ? false :ft.fullTextUrl}" name="fullImg"
                          des3ResId="${ft.des3ResId}" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" style="width: 40px; height:50px;">
                        <div class='pub-idx__full-text_newbox-box_load dev_img_title'
                          title="<s:text name='dyn.remd.res.fulltext.download' />"></div>
                      </div>
                    </c:if>
                    <c:if test="${ft.hasFulltext == 0}">
                      <div class="file-left__border  file-left__download fileupload__box-border"
                        style="position: relative;" onclick="dynamic.remdPdwhFullText('${ft.des3ResId}',event)">
                        <img src="${empty ft.fullTextUrl ? false :ft.fullTextUrl}" name="fullImg"
                          des3ResId="${ft.des3ResId}" onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'" style="width: 40px; height:50px;">
                        <div class="pub-idx__full-text_newbox-box dev_img_title"
                          title="<s:text name='dyn.remd.res.fulltext.req_fulltext' />"></div>
                        <div class="pub-idx__full-text_newbox-box_request"
                          title="<s:text name='dyn.remd.res.fulltext.req_fulltext' />"></div>
                      </div>
                    </c:if>
                  </c:if>
                  <c:if test="${ft.type == 2}">
                    <div class="file-left__border  file-left__download fileupload__box-border"
                      style="position: relative;">
                      <a href="/prjweb/outside/showfund?encryptedFundId=${ft.des3ResId }" target="_blank"> <img
                        src="${empty ft.fullTextUrl ? false :ft.fullTextUrl}"
                        onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'" style="width: 40px; height:50px;"></a>
                    </div>
                  </c:if>
                </div>
                <div class="new-Recommend_infor-box" style="width: 72%;">
                  <div class="new-Recommend_infor-box_title">
                    <a onclick="dynamic.viewRemdRes('${ft.des3ResId }','${ft.type}');">${ft.resTitle }</a>
                  </div>
                  <div class="new-Recommend_infor-box_author" onclick="dynamic.viewRemdRes('${ft.des3ResId }','${ft.type}');">${ft.resAuthorNames }</div>
                  <div class="new-Recommend_infor-box_time" onclick="dynamic.viewRemdRes('${ft.des3ResId }','${ft.type}');">
                    <em style="font-style: normal;">${ft.resBriefDesc }</em>
                  </div>
                  
                                <div class="new-Recommend_infor-func_tool"
                style="width: 100%; justify-content: flex-start; margin-bottom: 12px; margin-top: 12px;">
                <div class="new-Recommend_infor-func_cancle"
                  onclick="dynamic.notViewRemdRes(this,'${ft.des3ResId }','${ft.type}');">
                  <s:text name="dyn.remd.res.notview" />
                </div>
                <div class="new-Recommend_infor-func_comfire"
                  onclick="dynamic.viewRemdRes('${ft.des3ResId }','${ft.type}');">
                  <s:text name="dyn.remd.res.view" />
                </div>
              </div>
                  
                </div>
              </div>

            </div>
          </div>
          <div class="dynamic-social__list setting-list_page-item_hidden">
            <div class="dynamic-social__item setting-list_page-item_hidden">
              <a>赞</a>
            </div>
            <div class="dynamic-social__item setting-list_page-item_hidden">
              <a>评论</a>
            </div>
            <div class="dynamic-social__item setting-list_page-item_hidden">
              <a>分享</a>
            </div>
            <div class="dynamic-social__item setting-list_page-item_hidden">
              <a title="引用">引用</a>
            </div>
            <div class="dynamic-social__item setting-list_page-item_hidden">
              <a>收藏</a>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>