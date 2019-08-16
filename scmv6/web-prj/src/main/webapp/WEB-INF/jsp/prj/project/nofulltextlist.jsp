<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<c:if test="${not empty page.result}">
  <c:forEach items="${page.result}" var="result">
    <div class="main-list__item" id="${result.des3Id}">
      <input type="hidden" id="des3Ids" value="${result.des3Id}"> <input type="hidden" id="prjTitle"
        value="${result.title}"> <input type="hidden" id="prjId" value="${result.id}"> <input
        type="hidden" id="des3GroupIds" value="${result.des3GroupId}">
      <!-- <div class="main-list__item_checkbox">
              <div class="input-custom-style">
                <input type="checkbox" name="pub-type">
                <i class="material-icons custom-style"></i> </div>
            </div> -->
      <div class="main-list__item_content">
        <div class="pub-idx_medium">
          <div class="pub-idx__base-info">
            <div class="pub-idx__full-text_box">
              <div class="pub-idx__full-text_img" id="fulltext_downlink">
                <img src="/resscmwebsns/images_v5/images2016/file_img.jpg">
              </div>
            </div>
            <div class="pub-idx__main_box">
              <div class="pub-idx__main">
                <div class="pub-idx__main_title">
                  <a href="/prjweb/outside/project/detailsshow?des3PrjId=${result.des3Id}" target="_blank">${result.title}</a>
                </div>
                <div class="pub-idx__main_author">${result.authorNames}</div>
                <div class="pub-idx__main_src">${result.briefDesc}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <div class="fileupload__box"></div>
      </div>
    </div>
  </c:forEach>
</c:if>