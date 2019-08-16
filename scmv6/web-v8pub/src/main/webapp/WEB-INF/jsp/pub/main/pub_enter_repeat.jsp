<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty repeatPubVO.repeatPubInfoList }">
  <div class="repeat_achive-container">
    <div class="repeat_achive-container_header">
      <span class="repeat_achive-container_header-title"><spring:message code="pub.repeat.tips" /></span> 
      <i class="list-results_close" style="margin:18px 0px;" onclick="PubEdit.closeDupBox();"></i>
    </div>
    <div class="repeat_achive-container_body dev_deupub_list">
      <c:forEach items="${repeatPubVO.repeatPubInfoList }" var="pubinfo">
        <div class="repeat_achive-container_body-item">
          <div class="repeat_achive-container_body-item_avator pub-idx__full-text_img" style="position: relative;">
            <c:if test="${not empty pubinfo.downloadUrl }">
              <a href="${pubinfo.downloadUrl}"><img src="${pubinfo.fullTextImagePath}"
                onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" style="width: 72px; height: 92px;"></a>
              <a href="${pubinfo.downloadUrl}" class="new-tip_container-content"
                title="<spring:message code="pub.download"/>" style="width: 72px; height: 92px"> <img
                src="${resmod}/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                src="${resmod}/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
              </a>
            </c:if>
            <c:if test="${empty pubinfo.downloadUrl }">
              <img src="${pubinfo.fullTextImagePath}" onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'"
                style="width: 72px; height: 92px;">
            </c:if>
          </div>
          <div class="repeat_achive-container_body-item_infor">
            <div class="repeat_achive-container_body-item_infor-title" title="${pubinfo.title}">
              <a onclick="PubEdit.showPubDetail(this,'${pubinfo.pubIndexUrl}');">${pubinfo.title }</a>
            </div>
            <div class="repeat_achive-container_body-item_infor-author" title="${pubinfo.authorNamesNoTag}">${pubinfo.authorNames}</div>
            <div class="repeat_achive-container_body-item_infor-time" title="${pubinfo.briefDesc}">${pubinfo.briefDesc}</div>
          </div>
          <div class="repeat_achive-container_body-item_reflash">
            <span class="repeat_achive-container_body-item_reflash-time"><fmt:formatDate
                value="${pubinfo.updateDate}" pattern="yyyy.MM.dd" /></span> <span
              class="repeat_achive-container_body-item_reflash-content"><spring:message
                code="repeat.pub.last.update" /></span>
          </div>
          <div class="repeat_achive-container_body-item_check"
            onclick="PubEdit.showPubDetail(this,'${pubinfo.pubIndexUrl}');">
            <spring:message code="pub.repeat.view" />
          </div>
        </div>
      </c:forEach>
    </div>
    <div class="repeat_achive-container_footer">
      <div onclick="PubEdit.cancelDupPubBox();" class="repeat_achive-container_footer-continue positionfix-cancle">
        <c:if test="${isEditPub == true}">
          <spring:message code="pub.repeat.nosave" />
        </c:if>
        <c:if test="${isEditPub == false}">
          <spring:message code="pub.repeat.noadd" />
        </c:if>
      </div>
      <div onclick="PubEdit.saveDupPubBox(this);" class="repeat_achive-container_footer-cancle positionfix-cancle">
        <c:if test="${isEditPub == true}">
          <spring:message code="pub.repeat.save" />
        </c:if>
        <c:if test="${isEditPub == false}">
          <spring:message code="pub.repeat.add" />
        </c:if>
      </div>
    </div>
  </div>
</c:if>