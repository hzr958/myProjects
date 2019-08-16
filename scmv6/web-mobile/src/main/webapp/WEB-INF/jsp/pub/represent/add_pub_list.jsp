<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="js_listinfo" smate_count='${pubListVO.totalCount}'></div>
<c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
     
    <div class="paper main-list__item" style="display: block;" des3pubid="${pub.des3PubId }">
      <div class="paper_content-container_list-avator" style="float: left;">
        <c:if test="${pub.hasFulltext == 1}">
            <img  class="paper_content-list_save-avator" onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl}')" src="${pub.fullTextImgUrl}"
            onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" />
        </c:if>
        <c:if test="${pub.hasFulltext != 1}">
            <img  class="paper_content-list_save-avator" onclick="fullTextUpTips()"  src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'"/>
        </c:if>
      </div>
      <div class="paper_cont" style="margin-left: 52px !important; display: flex; align-items: center; justify-content: space-between;">
        <div style="width: 65vw;" href="javascript:void(0);" onclick="window.open('/pub/details/snsnonext?des3PubId=${pub.des3PubId }','_self')">
          <p>
            <a class="pubTitle">${pub.title}</a>
          </p>
          <p class="p1">${pub.authorNames}</p>
          <p class="f999">
            <span>${pub.briefDesc }</span><span class="fccc"><c:if test="${not empty pub.publishYear && pub.publishYear>0}"> | ${pub.publishYear}</c:if></span>
          </p>
        </div>
        <i class="material-icons" onclick="RepresentPub.addRepresentPub(this)">add</i>
      </div>
    </div>

</c:forEach>
<c:if test="${(pubListVO.page.pageNo-1)*10+pubListVO.resultList.size() == pubListVO.totalCount}">
  <div class="response_no-result">没有更多记录</div>
</c:if>