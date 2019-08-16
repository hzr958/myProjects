<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${prjRepostList.size()}"></div>
<c:forEach items="${prjRepostList }" var="reportInfo">
  <div class="main-list__item main-list__item-botStyle dev_pub_del_${reportInfo.id} dev_pub_list_div"
    id="dev_pub_del_${reportInfo.id}" resId="${reportInfo.id}" resNode="1" style="align-items: flex-start;"
    drawer-id="<iris:des3 code='${reportInfo.id}'/>">
    <div class="main-list__item_content">
      <div class="pub-idx_medium">
        <div class="pub-idx__base-info" style="align-items: center;">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img" style="position: relative">
              <c:if test="${empty reportInfo.downloadUrl }">
                  <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                  <!-- 上传全文 start -->
                  <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1"
                    des3Id="${reportInfo.id }">
                    <div class="fileupload__box" onclick="fileuploadBoxOpenInputClick(event);"
                      title="上传全文">
                      <div class="fileupload__core initial_shown">
                        <div class="fileupload__initial">
                          <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                            src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator"> <input
                            type="file" class="fileupload__input" style='display: none;'>
                        </div>
                        <div class="fileupload__progress" style="margin: 0px -1px 3px 0px;">
                          <canvas width="56" height="56"></canvas>
                          <div class="fileupload__progress_text"></div>
                        </div>
                        <div class="fileupload__saving" style="margin-top: -10px;">
                          <div class="preloader"></div>
                          <div class="fileupload__saving-text"></div>
                        </div>
                        <div class="fileupload__finish"></div>
                        <div class="fileupload__hint-text" style="display: none">添加全文</div>
                      </div>
                    </div>
                  </div>
                </c:if>
                <c:if test="${not empty reportInfo.downloadUrl }">
                  <img src="${resmod}/images_v5/images2016/file_img1.jpg" onclick="window.location.href='${reportInfo.downloadUrl }'"
                    class="dev_fulltext_download dev_pub_img" style="cursor: pointer;" title="下载全文"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" />
                    <a href="${reportInfo.downloadUrl }" class="new-tip_container-content" title="下载全文"> 
                    <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> 
                    <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2"> 
                  </a>
                </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title"
                style="height: 40px; overflow: hidden;">
                  <c:choose>
                  <c:when test="${reportInfo.reportType=='1'}">
                    进展报告
                  </c:when>
                  <c:when test="${reportInfo.reportType=='2'}">
                    中期报告
                  </c:when>
                  <c:when test="${reportInfo.reportType=='3'}">
                    审计报告
                  </c:when>
                  <c:when test="${reportInfo.reportType=='5'}">
                    结题报告
                  </c:when>
                  <c:otherwise>
                    验收报告
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions" style="width: 35%; ">
        <div class="pro-stats__list pub-list-stats" style="width:100%; display: flex; align-items: center; justify-content: space-between;">
          <div class="pro-stats__item"> 
              <div class="pro-stats__item_number" style="font-size: 14px;"><fmt:formatDate value="${reportInfo.warnDate}" pattern="yyyy-MM-dd" /></div>
              <div class="pro-stats__item_title">
                截止日期
              </div>
          </div>
          <div class="pro-stats__item">
              <div class="pro-stats__item_number" style="font-size: 14px;">
              <c:choose>
                  <c:when test="${reportInfo.status == '1'}">
                    已结束
                  </c:when>
                  <c:when test="${reportInfo.status=='2'}">
                    已填写
                  </c:when>
                  <c:when test="${reportInfo.status=='3'}">
                  <c:if test="${not empty des3AgencyId}">
                    <a href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=${des3AgencyId}" target="_Blank" style="color: #288aed;">前往填写</a>
                   </c:if>
                   <c:if test="${empty des3AgencyId}">
                    前往填写
                   </c:if>
                  </c:when>
                  <c:otherwise>
                    未填写
                  </c:otherwise>
                </c:choose>
              </div>
          </div>
          
        </div>
      </div>
    </div>
  </div>
</c:forEach>
