<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="dynId" value="${jsonDynInfo.GROUP_DYN_ID}" dynOwnerDes3Id="${jsonDynInfo.DYN_OWNER_DES3_ID}" resType="${jsonDynInfo.RES_TYPE}" des3ResId="${jsonDynInfo.RES_ID}" resOwnerDes3Id="${jsonDynInfo.RES_OWNER_DES3ID}" resId="${jsonDynInfo.RES_ID}" dbId="${jsonDynInfo.DB_ID}" databaseType="${jsonDynInfo.PDWH_URL}" dynType="${jsonDynInfo.DYN_TYPE}" />
<div class="dynamic-content">
    <div class="dynamic-content__post">
        <div class="dynamic-post__avatar" onclick='Group.openPsnDetail("${jsonDynInfo.DES3_PSN_ID}",event)'>
            <a><img src="${jsonDynInfo.AUTHOR_AVATAR}" onerror="this.src='/ressns/images/phone120X120.gif'"></a>
        </div>
        <div class="dynamic-post__main">
            <div class="dynamic-post__author">
                <a class="dynamic-post__author_name" title="${jsonDynInfo.ZH_AUTHOR_NAME}"  onclick='Group.openPsnDetail("${jsonDynInfo.DES3_PSN_ID}",event)'>
                    ${jsonDynInfo.ZH_AUTHOR_NAME}
                </a>
                <span class="dynamic-post__author_action">
                    <%@ include file="grp_dyn_tips_content.jsp" %>
                </span>
            </div>
            <div class="dynamic-post__author_inst">${jsonDynInfo.PSN_WORK_INFO} </div>
        </div>
        <div class="dynamic-post__time show_time">${groupDynShowInfo.dynDateForShow }</div>
    </div>
    <div class="dynamic-content__main">
        <div class="dynamic-main__box">
            <c:if test="${jsonDynInfo.RES_TYPE != 'fund' && jsonDynInfo.RES_TYPE != 'agency'}">
              <div class="dyn_content dyn_content_div" onclick="Group.dynComment('${jsonDynInfo.GROUP_DYN_ID }')">
                  ${jsonDynInfo.GROUP_DYN_CONTENT}
              </div>
            </c:if>
            <c:if test="${jsonDynInfo.RES_TYPE == 'fund' || jsonDynInfo.RES_TYPE == 'agency'}">
              <div class="dyn_content dyn_content_div">
                  ${jsonDynInfo.GROUP_DYN_CONTENT}
              </div>
            </c:if>
            <c:if test="${not empty jsonDynInfo.RES_ID }">
                <c:if test="${not empty jsonDynInfo.GROUP_DYN_CONTENT}">
                    <div class="dynamic-divider"></div>
                </c:if>
                <!-- 基金 -->
                <c:if test="${ jsonDynInfo.RES_TYPE == 'fund'}">
                    <%@ include file="grp_share_fund_dyn_content.jsp" %>
                </c:if>
                <!-- 资助机构 -->
                <c:if test="${ jsonDynInfo.RES_TYPE == 'agency'}">
                    <%@ include file="grp_share_agency_dyn_content.jsp" %>
                </c:if>
                <!-- 成果 -->
                <c:if test="${ jsonDynInfo.RES_TYPE == 'pub' || jsonDynInfo.RES_TYPE == 'pdwhpub'}">
                    <%@ include file="grp_share_pub_dyn_content.jsp" %>
                </c:if>
                <!-- 文件 -->
                <c:if test="${ jsonDynInfo.RES_TYPE == 'file' || jsonDynInfo.RES_TYPE == 'grpfile'}">
                    <%@ include file="grp_share_file_dyn_content.jsp" %>
                </c:if>
                <!-- 项目 -->
                <c:if test="${ jsonDynInfo.RES_TYPE == 'prj'}">
                    <%@ include file="grp_share_prj_dyn_content.jsp" %>
                </c:if>
                
            </c:if>
        </div>
    </div>
</div>