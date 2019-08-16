<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 循环 -->
<c:forEach items="${repeatPubVO.repeatPubInfoList }" var="pubinfo">
  <div class="Similarresults-body_item dev_repeatpub_item" des3pubSameItemId="${pubinfo.des3pubSameItemId }"
    des3RecordId="${pubinfo.des3RecordId }" dealStatus="${pubinfo.dealStatus }">
    <div class="pub-idx__full-text_img" style="position: relative; border: 1px solid #ddd;">
      <img src="${pubinfo.fullTextImagePath }"
        <c:if test="${!empty pubinfo.downloadUrl}">
            onclick="window.location.href='${pubinfo.downloadUrl}';" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'"
        </c:if>
        <c:if test="${empty pubinfo.downloadUrl}">
            onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'"
        </c:if>
        class="Similarresults-body_avator">
      <c:if test="${!empty pubinfo.downloadUrl}">
        <a onclick="window.location.href='${pubinfo.downloadUrl}';" class="new-tip_container-content" title="下载全文"
          style="width: 100%; height: 100%; justify-content: center; align-items: center;"> <img
          src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
          src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
        </a>
      </c:if>
    </div>
    <div class="Similarresults-body_container">
      <div class="Similarresults-body_container-title"
        <c:if test="${!empty pubinfo.pubIndexUrl}">
                onclick="window.open('${pubinfo.pubIndexUrl}');" style="cursor: pointer;"
            </c:if>
        style="width: 485px;" ;  title="${pubinfo.title }">${pubinfo.title }</div>
      <div class="Similarresults-body_container-introduc" title="${pubinfo.authorNamesNoTag}">${pubinfo.authorNames }</div>
      <div class="Similarresults-body_container-author" title="${pubinfo.briefDesc}">${pubinfo.briefDesc }</div>
    </div>
    <div class="Similarresults-body_uploadtime" style="padding-right: 2px;">
      <div style="color: #999;">
        <fmt:formatDate value="${pubinfo.updateDate }" pattern="yyyy.MM.dd" />
      </div>
      <div style="color: #999;">
        <spring:message code="repeat.pub.last.update" />
      </div>
    </div>
    <c:if test="${pubinfo.dealStatus==0}">
      <div class="confirm-email__body-iconlist dev_del_btn" onclick="RepeatPub.itemdelete(event)">
        <spring:message code="repeat.pub.delete" />
      </div>
    </c:if>
    <c:if test="${pubinfo.dealStatus==1}">
      <div class="confirm-email__body-iconlist dev_del_btn" style="color: #999; border: #999;">
        <spring:message code="repeat.pub.already.save" />
      </div>
    </c:if>
    <c:if test="${pubinfo.dealStatus==2}">
      <div class="confirm-email__body-iconlist dev_del_btn" style="color: #999; border: #999;">
        <spring:message code="repeat.pub.already.delete" />
      </div>
    </c:if>
  </div>
</c:forEach>