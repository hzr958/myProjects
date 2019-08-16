<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="psnFileShareBaseInfos" var="pfsbl" status="st">
  <div class="main-list__item">
    <div class="main-list__item_content">
      <div class="file-share-history__box">
        <div class="file-share-history__info">
          <span class="file-share-history__share-time"> <fmt:formatDate
              value="${pfsbl.psnFileShareBase.createDate }" pattern="yyyy-MM-dd" />&nbsp;<s:text
              name='apps.files.showShare.shareTo' />
          </span> <div class="file-share-history__receiver-list" style="display: inline;color:#005cac;"> <c:forEach var="pInfo"
              items="${pfsbl.psnInfoList}" varStatus="stat">
              <c:if test="${pInfo.psnId!=0 }">
                <a class="file-share-history__receiver" href="${pInfo.psnShortUrl }" target="_Blank"> <c:if
                    test="${locale=='zh_CN'}">${pInfo.zhName }</c:if> <c:if test="${locale=='en_US'}">${pInfo.enName }</c:if></a>
              <c:if test="${!stat.last}">,&nbsp;</c:if>
              </c:if>
              <c:if test="${pInfo.psnId==0 }">
                 <font color="#777777">${pInfo.email } <c:if test="${!stat.last}"> ,&nbsp;</c:if></font>
              </c:if>
            </c:forEach>
          </div>
        </div>
        <c:forEach var="fInfo" items="${pfsbl.psnFileInfoList}">
          <div class="file-share-history__file-name">
            <a onclick="BaseUtils.fileDown('${fInfo.downloadUrl }',this,event)">${fInfo.fileName }</a>
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey"
        onclick="VFileMain.cancelFileShare('<iris:des3 code="${pfsbl.psnFileShareBase.id}"/>',this)">
        <s:text name='apps.files.showShare.cancel' />
      </button>
    </div>
  </div>
</s:iterator>