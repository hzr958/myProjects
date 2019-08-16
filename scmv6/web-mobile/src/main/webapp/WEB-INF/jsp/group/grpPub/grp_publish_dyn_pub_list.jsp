<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:if test="${pubList.size()>0}">
  <c:forEach items="${pubList}" var="pub" varStatus="stat">
    <input type="hidden" name="fulltextImagePermission" id="fulltextImagePermission" value="${pub.fullTextPermission}">
    <div class="paper main-list__item" onclick="Group.selectPubToPublish('<iris:des3 code='${pub.pubId }'/>',this,'${pub.permission}','${pub.isOwn }')"  style="margin: 0px; padding: 12px 16px;">
      <div class="paper_content-container_list-avator" style="float: left;">
        <c:if test="${pub.hasFulltext == 1}">
          <img src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" style="border-radius: 0px; width: 40px;"> 
        </c:if>
        <c:if test='${pub.hasFulltext != 1}'>
          <img src="${resmod }/images_v5/images2016/file_img.jpg" style="border-radius: 0px; width: 40px;">
        </c:if>
      </div>
      <div class="paper_cont" style="margin-left: 52px !important;" href="javascript:void(0);">
        <div class="pubTitle" style="line-height: 20px; text-align: left;"> 
               ${not empty pub.zhTitle ? pub.zhTitle : pub.enTitle }
               <c:if test="${pub.permission eq 4 }">
                  <i class = "selected-func_closed"></i>
               </c:if>
        </div>
        
        <p class="p1" style="text-align:left;">${pub.authors }</p>
        <p class="p3 f999">
          <span>${pub.zhBrif}</span><span class="fccc"><c:if test="${not empty pub.publishYear && pubQueryModel.fromPage != 'dyn'}"> | ${pub.publishYear}</c:if></span>
        </p>
      </div>
    </div>
  </c:forEach>
</c:if>
<c:if test="${pubList.size()<1}">
  <div class="response_no-result">未找到符合条件的记录</div>
</c:if>
