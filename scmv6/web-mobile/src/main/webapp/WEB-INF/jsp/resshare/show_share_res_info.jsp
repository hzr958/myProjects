<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${!empty vo.showInfo }">
<input type="hidden" id="mobile_share_grpres_grpid" value="${vo.showInfo.resDes3GrpId }">
<div class="new-mobilefile_share-box">
     <div class="new-mobilefile_share-item">
        <div class="new-mobilefile_share-item_avator">
            <!-- 图标 -->
            <c:if test="${vo.resType == 'agency' }"><img src="${vo.showInfo.imgSrc}" onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'"/></c:if>
            <c:if test="${vo.resType == 'fund' }"><img src="${vo.showInfo.imgSrc}" onerror="this.src='/ressns/images/default/default_fund_logo.jpg'"/></c:if>
            <c:if test="${vo.resType == 'pdwhpub' }"><img src="${vo.showInfo.imgSrc}" onerror="this.src='/resmod/images_v5/images2016/file_img.jpg'"/></c:if>
            <c:if test="${vo.resType == 'pub' }"><img src="${vo.showInfo.imgSrc}" onerror="this.src='/resmod/images_v5/images2016/file_img.jpg'"/></c:if>
            <c:if test="${vo.resType == 'prj' }"><img src="${vo.showInfo.imgSrc}" onerror="this.src='/resmod/images_v5/images2016/file_img.jpg'"/></c:if>
            <c:if test="${vo.resType == 'psnfile' || vo.resType == 'grpfile'}"><img src="${vo.showInfo.imgSrc}" onerror="this.src='/resmod/images_v5/images2016/file_img.jpg'"/></c:if>
        </div>
        <div class="new-mobilefile_share-item_content">
            <!-- 标题 -->
           <div class="new-mobilefile_share-item_title">
                <a>${vo.showInfo.title }</a>
           </div>
           <!-- 作者 -->
           <div class="new-mobilefile_share-item_source">
                ${vo.showInfo.authorNames }
           </div>
           <!-- 概述 -->
           <div class="new-mobilefile_share-item_source">
                ${vo.showInfo.briefDesc }
                <c:if test="${not empty vo.showInfo.publishYear}">
                    <span class="fccc paper_content-container_list-source_time" style="margin-right: 8px;" >${vo.showInfo.publishYear}</span>
                </c:if>
           </div>
           <!-- 资助机构地址 -->
           <div class="new-mobilefile_share-item_source">
                ${vo.showInfo.address }
           </div>
        </div>
     </div>
</div>
</c:if>




