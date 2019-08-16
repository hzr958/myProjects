<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}">
  <c:forEach items="${page.result}" var="ft" varStatus="stat">
    <div class="main-list__item" id="dev_${ft.pubId}">
      <div class="main-list__item_content">
        <div class="pub-idx_small">
          <div class="pub-idx__base-info">
            <div class="pub-idx__full-text_box">
              <div class="pub-idx__full-text_img" style="position: relative; width: 72px; height: 92px;">
                <c:if test="${not empty ft.fullTextUrl}">
                  <a href="${ft.fullTextUrl }"> <img src="${ft.fullTextImaUrl}"
                    class="dev_fulltext_download dev_pub_img" style="cursor: pointer;"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
                  </a>
                  <a href="${ft.fullTextUrl }" class="new-tip_container-content"
                    title="<s:text name='groups.pub.member.download.fulltext'/>"> <img
                    src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                    src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
                  </a>
                </c:if>
                <c:if test="${empty ft.fullTextUrl}">
                  <img src="${resmod}/images_v5/images2016/file_img.jpg"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                </c:if>
              </div>
            </div>
            <div class="pub-idx__main_box">
              <div class="pub-idx__main" style="height: 100%; justify-content: space-around;">
                <div class="pub-idx__main_title pub-idx__main_title-detail">
                  <a onclick='Pub.newPubPdwhDetail("<iris:des3 code='${ft.pubId}'/>");'
                    onmouseover="javascript:$(this).attr('title',($(this).html().trim()))"> ${ft.showTitle} </a>
                </div>
                <div class="pub-idx__main_author">${ft.authors}</div>
                <div class="pub-idx__main_src">${ft.showBrif}</div>
                <c:if test="${not empty ft.fundInfo}">
                  <div class="pub-idx__main_title" title="${ft.fundInfoComplete}">${ft.fundInfo}</div>
                </c:if>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <button class="button_main button_dense button_primary-cancle" onclick="GrpBase.rejectGrpPubRcmd(${ft.pubId});">
          <s:text name="pub.ignore.opt" />
        </button>
        <button class="button_main button_dense button_primary-changestyle"
          onclick="GrpBase.acceptGrpPubRcmd(${ft.pubId});">
          <s:text name="pub.confirm.opt" />
        </button>
      </div>
    </div>
  </c:forEach>