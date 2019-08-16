<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${fn:length(fulltextList) > 0 }">
  <c:forEach items="${fulltextList}" var="page">
  <input type="hidden" id="downloadUrl" value="${page.downloadUrl }">
    <div class="new-full_text-container_body-box">
      <div class="new-full_text-container_body-box_title">以下文件是否为对应成果的全文</div>
      <div class="new-full_text-container_detail-container">
       <div class="new-full_text-container_detail-container-infor">
          <div class="new-full_text-container_detail-title">${page.pub.title}</div>
          <div class="new-full_text-container_detail-author">${page.pub.authorNames}</div>
          <c:choose>
             <c:when test="${empty page.pub.briefDesc}">
               <div class="new-full_text-container_detail-time">${page.pub.briefDescEn}</div>
             </c:when>
             <c:otherwise>
               <div class="new-full_text-container_detail-time">${page.pub.briefDesc}</div>
             </c:otherwise>
          </c:choose>
        </div>
        <div class="new-full_text-container_detail-avator">
          <img id="fulltextImage" onclick="Msg.enlarge('${page.fullTextImagePath}','${page.srcFullTextImagePath}','${model.detailPageNo}','${model.toBack}');" src="${page.fullTextImagePath}" onerror="javascript:this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
        </div>
        <div class="new-full_text-container_detail-func">
          <div onclick="Msg.doConfirmPubft(${page.id},'next');" class="new-full_text-container_detail-func_yes dev_yes_botton">确认成果全文</div>
          <div onclick="Msg.doRejectPubft(${page.id},'next');" class="new-full_text-container_detail-func_no dev_not_botton">不是成果全文</div>
        </div>
<%--         <a href="javascript:downloadFulltext('${page.downloadUrl}')">下载</a> --%>
        <i onclick="Msg.enlarge('${page.fullTextImagePath}','${page.srcFullTextImagePath}','${model.detailPageNo}','${model.toBack}');"
          class="material-icons new-full_text-container_enlarge">zoom_in</i>
      </div>
    </div>
  </c:forEach>
</c:if>
<c:if test="${fn:length(fulltextList) == 0}">
<div id="con_one_2" class="noRecord" style="margin-top:-50px">
            <div class="content">
                <div class="no_effort">
                    <h2 class="tc">未找到全文认领记录</h2>
                    <!-- <div class="no_effort_tip pl27">
                        <span>温馨提示： </span>
                        <p>请立即登录查看.</p>
                    </div> -->
                </div>
            </div>
        </div>
  <!-- <div class="no_msg"text-align:center;"></div> -->
</c:if>
