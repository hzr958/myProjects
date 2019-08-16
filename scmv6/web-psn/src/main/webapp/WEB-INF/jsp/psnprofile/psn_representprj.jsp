<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="representPrjIds" name="representPrjIds" value="${representPrjIds }" />
<input type="hidden" id="representPrjIdsOld" name="representPrjIdsOld" value="${representPrjIds }" />
<c:if test="${not empty representPrjList || isMySelf== 'true'}">
  <div class="container__card representPrjDiv">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name='homepage.profile.title.featured.projects' />
        </div>
        <button class="button_main button_icon button_light-grey operationBtn" onclick="showPsnRepresentPrjBox();">
          <i class="material-icons">edit</i>
        </button>
      </div>
      <div class="main-list__list">
        <c:if test="${!empty representPrjList}">
          <c:forEach items="${representPrjList }" var="representPrj" varStatus="status">
            <input type="hidden" id="representPrjList" value="${representPrj.downUploadFulltext}">
            <div class="main-list__item">
              <div class="main-list__item_content">
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <!-- <div class="pub-idx__full-text_box"> -->
                    <%-- <c:if test="${representPrj.fulltextFileId != null  && representPrj.fulltextFileId !='' }">
                     <div class="pub-idx__full-text_img"  onclick = "project.downloadAuthority('<iris:des3 code="${representPrj.fulltextFileId}"/>','${representPrj.des3Id}');"  style="cursor: pointer;"><img src="${representPrj.fullTextImgPath }"></div>
                    </c:if> 
                    <c:if test="${representPrj.fulltextFileId == null || representPrj.fulltextFileId == '' }">
                       <div class="pub-idx__full-text_img"><img src="${representPrj.fullTextImgPath }"></div>
                    </c:if> --%>
                    <%--  <c:choose>
                  <c:when test="${not empty representPrj.fulltextFileId && representPrj.downUploadFulltext==true}">
                    <div class="pub-idx__full-text_img" id="fulltext_downlink"><a><img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" onclick ="project.download('<iris:des3 code='${representPrj.fulltextFileId}'/>')"></a></div>
                   </c:when>
                    <c:when test="${not empty representPrj.fulltextFileId && isMySelf!=false}">
                     <div class="pub-idx__full-text_img" id="fulltext_downlink"><a><img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" onclick ="project.download('<iris:des3 code='${representPrj.fulltextFileId}'/>')"></a></div>
                   </c:when>
                   <c:otherwise>
                     <div class="pub-idx__full-text_img" id="fulltext_downlink"><img src="/resscmwebsns/images_v5/images2016/file_img.jpg"></div>
                   </c:otherwise>
                   </c:choose>  --%>
                    <!-- <div id="fulltext_downlink"></div> -->
                    <!-- </div> -->
                    <div class="pub-idx__main_box">
                      <div class="pub-idx__main">
                        <div class="pub-idx__main_title">
                          <a href="javascript:viewPsnPrjDetails('${representPrj.des3Id}');">${representPrj.prjInfo.showTitle }</a>
                        </div>
                        <div class="pub-idx__main_author">${representPrj.prjInfo.showAuthorNames }</div>
                        <div class="pub-idx__main_src">${representPrj.prjInfo.showBriefDesc }</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </c:forEach>
        </c:if>
      </div>
    </div>
  </div>
</c:if>
<div class="dialogs__box representPrjDiv" style="width: 720px;" id="representPrjBox" dialog-id="representPrjBox"
  cover-event="hide">
  <%@ include file="psn_representprj_edit.jsp"%>
</div>