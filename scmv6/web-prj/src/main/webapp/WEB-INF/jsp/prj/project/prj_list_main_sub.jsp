<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<c:if test="${not empty page.result}">
  <c:forEach items="${page.result}" var="result">
    <div class="main-list__item main-list__item-botStyle" drawer-id="${result.des3Id}">
      <input type="hidden" id="des3prjId" value="${result.des3Id}">
      <input type="hidden"  name="prjTitle__" value="<c:out value ="${result.title}"></c:out>">
      <input type="hidden"  name="prjAbstracts__" value="<c:out value="${result.abstracts}"></c:out>">
      <input type="hidden"  name="firstCategory" value="${result.disciplineMap.firstCategory}">
      <input type="hidden"  name="firstCategoryId" value="${result.disciplineMap.firstCategoryId}">
      <input type="hidden"  name="secondCategory" value="${result.disciplineMap.secondCategory}">
      <input type="hidden"  name="secondCategoryId" value="${result.disciplineMap.secondCategoryId}">
        <%--<input type="hidden"  id="prjId" value="${result.id}"> --%>
      <input type="hidden" id="des3GroupIds" value="${result.des3GroupId}">
      <c:if test="${othersSee !=true}">
      <div class="main-list__item_checkbox"
        style="height: 120px; display: flex; justify-content: center; align-items: center;">
        <div class="input-custom-style">
          <input type="checkbox" name=""> <i class="material-icons custom-style"></i>
        </div>
      </div>
    </c:if>
      <div class="main-list__item_content"  style="width: 500px;">
        <div class="pub-idx_medium">
          <div class="pub-idx__base-info">
          
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img" style="position: relative">
            <c:if test="${empty result.downloadUrl }">
                  <img src="${resmod}/images_v5/images2016/file_img.jpg" class="dev_pub_img" 
                    onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
                  <!-- 上传全文 start -->
                  <div class="pub_uploadFulltext pub-idx__full-text__tip pub-idx__full-text__tip1" style="cursor: default;"
                    des3Id="${result.des3Id }">
                    <div class="fileupload__box" onclick=""
                      title="">
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
                <c:if test="${not empty result.downloadUrl }">
                    <img src="${resmod}/images_v5/images2016/file_img1.jpg" onclick="window.location.href='${result.downloadUrl }'"
                    class="dev_fulltext_download dev_pub_img" title="下载全文"
                    onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" />
                    <a href="${result.downloadUrl }" class="new-tip_container-content" title="下载全文"> 
                    <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> 
                    <img src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2"> 
                  </a>
               </c:if>
             </div>
            </div>
            <div class="pub-idx__main_box">
              <div class="pub-idx__main">
                <div class="pub-idx__main_title pub-idx__main_title-multipleline pub-idx__main_title-multipleline-box" style="height: 40px; overflow: hidden;">
                  <a class="multipleline-ellipsis__content-box" href="/prjweb/project/detailsshow?des3PrjId=${result.des3Id}" target="_blank" title="${result.title}">${result.title}</a>
                </div>
                <div class="pub-idx__main_author" style="max-width: 849px; min-width: 0px;" title="${result.noneHtmlLableAuthorNames}">${result.authorNames}</div>
                <div class="pub-idx__main_src" title="${result.briefDesc}">${result.briefDesc}</div>
              </div>
              <c:if test="${othersSee !=true}">
                <ul class="idx-social__list">
                  <li class="idx-social__item" onclick="prjEdit('${result.des3Id}')"><i
                    class="new-normal_funcicon-edit"></i> <s:text name="project.edit"></s:text></li>
                  <li class="idx-social__item" onclick="project.delete('${result.des3Id}')"><i
                    class="new-normal_funcicon-delete"></i> <s:text name="project.remove"></s:text></li>
                  <c:if test="${result.shareCount > 0 }">
                    <li class="idx-social__item" onclick="project.itemShare('${result.des3Id}',event)"><i
                      class="new-normal_funcicon-share"></i> <s:text name="project.share" /><span
                      value="${result.des3Id}">(${result.shareCount})</span></li>
                  </c:if>
                  <c:if test="${!(result.shareCount > 0) }">
                    <li class="idx-social__item" onclick="project.itemShare('${result.des3Id}',event)"><i
                      class="new-normal_funcicon-share"></i> <s:text name="project.share" /></li>
                  </c:if>
                  <c:if test="${result.showPrjGroup==false&&othersSee !=true}">
                    <li class="idx-social__item" onclick="showCreateGrp( this,'${result.externalNo}','${result.keywords}')">
                        <i class="new-Standard_Upload-icon"></i><s:text name="project.create.group.button"></s:text></li>
                  </c:if>
                  <c:if test="${result.showPrjGroup==true}">
                    <li class="idx-social__item" onclick="javascript:window.location.href='/groupweb/grpinfo/main?des3GrpId=${result.des3GroupId}'">
                        <i class="new-Standard_Upload-icon"></i><s:text name="project.enter.group"></s:text></li>
                  </c:if>
                </ul>
              </c:if>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <s:if test="isVIP == true && othersSee != true">
            <!-- 成果 -->
          <div class="pro-stats__item">
            <c:if test="${result.pubCount != null && result.pubCount > 0 }">
              <div class="pro-stats__item_number">${result.pubCount}</div>
              <div class="pro-stats__item_title">
                                            成果
              </div>
            </c:if>
          </div>
          <!-- 可用经费-->
          <div class="pro-stats__item">
            <c:if test="${result.availableTotal != null }">
              <div class="pro-stats__item_number">${result.availableTotal}</div>
              <div class="pro-stats__item_title">
                                        可用经费
              </div>
            </c:if>
          </div>
           <!-- 报告-->
          <div class="pro-stats__item">
            <c:if test="${result.reportType != null && result.reportType > 0 }">
              <div class="pro-stats__item_number">
              <c:choose>
                  <c:when test="${result.reportType=='1'}">
                                                        进展报告
                  </c:when>
                  <c:when test="${result.reportType=='2'}">
                                                         中期报告
                  </c:when>
                  <c:when test="${result.reportType=='3'}">
                                                        审计报告
                  </c:when>
                  <c:when test="${result.reportType=='5'}">
                                                         结题报告
                  </c:when>
                  <c:otherwise>
                                                        验收报告
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="pro-stats__item_title">
                                             待提交
              </div>
            </c:if>
          </div>
        </s:if>
      </div>
    </div>
  </c:forEach>
  <!-- 分享插件 -->
</c:if>
