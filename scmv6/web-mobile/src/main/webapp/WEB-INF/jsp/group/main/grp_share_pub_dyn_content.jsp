<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dynamic-main__att">
    <div class="pub-idx_medium">
        <div class="pub-idx__base-info">
            <div class="pub-idx__full-text_box">
                <div class="pub-idx__full-text_img pub-idx__full-text_newbox dev_pub_fulltext" style="cursor: default;pointer-events: auto;"
                permission=""
                des3pubid="${jsonDynInfo.RES_ID}" 
                des3fileid="${jsonDynInfo.FULL_TEXT_FIELD}">
                 <a class="pub-idx__full-text_newbox-avator" onclick='Group.dealWithFulltext("${jsonDynInfo.RES_TYPE}", "${jsonDynInfo.DES3_RES_ID}","<iris:des3 code='${groupDynShowInfo.resFullTextFileId}'/>", "${jsonDynInfo.RES_OWNER_DES3ID }",event, "${jsonDynInfo.RES_NOT_EXISTS }")'>
                    <c:if test="${not empty groupDynShowInfo.resFullTextFileId}">
                        <img src="${jsonDynInfo.FULL_TEXT_IMAGE}" onerror="this.src='/resmod/images_v5/images2016/file_img1.jpg'"/>
                    </c:if>
                    <c:if test="${empty groupDynShowInfo.resFullTextFileId}">
                        <img src="/resmod/images_v5/images2016/file_img.jpg"/>
                    </c:if>
                    <div class="pub-idx__full-text_newbox-box dev_img_title" title=""></div>
                 </a>
                </div>
            </div>
            <div class="pub-idx__main_box">
                <div class="pub-idx__main">
                    <div class="pub-idx__main_title">
                        <a onclick='Group.openResDetails("${jsonDynInfo.DES3_RES_ID}", "${jsonDynInfo.RES_TYPE }", event, "${jsonDynInfo.RES_NOT_EXISTS }", "title")'>
                        ${jsonDynInfo.ZH_RES_NAME}</a>
                    </div>
                    <div class="pub-idx__main_author">${jsonDynInfo.AUTHOR_NAMES}</div>
                    <div class="pub-idx__main_src pub-idx__main_src-line">${jsonDynInfo.ZH_RES_DESC}</div>
                </div>
            </div>
        </div>
    </div>
</div>