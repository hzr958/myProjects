<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${shareType eq 'prj' ||  shareType eq 'prjDetail'}">项目分享</c:when>
            <c:when test="${shareType eq 'sns' || shareType eq 'pdwh' || shareType eq 'pdwhDetail'}">成果分享</c:when>
            <c:when test="${shareType eq 'fund' || shareType eq 'fundDetail' || shareType eq 'aidInsDetail'}">基金分享</c:when>
            <c:when test="${shareType eq 'aidIns' }">资助机构分享</c:when>
          <c:when test="${shareType eq 'news' }">新闻分享</c:when>
        </c:choose>
    </title>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
    <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod }/js/smate.share.js"></script>
    <script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
    <script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod}/js_v8/agency/scm_pc_agency.js"></script>
    <script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
    <script type="text/javascript">
      window.onload = function(){
          $("#resHistoryPage").val(document.referrer);
          //调整资源位置在最下方,当输入框聚焦时自动上移
      }
    </script>
</head>
<body>  
    <div class="message-page__header">
        <span class="new-mobile_sharedyn-item" onclick="javascript:window.history.go(-1);">取消</span>
        <span class="new-mobile_sharedyn-title">分享至动态</span>
        <span class="new-mobile_sharedyn-item" onclick="SmateCommon.resShareToDyn('${shareType}')">分享</span>
    </div>
    <div class="new-mobile_sharedyn-container" style = "height: 92vh;justify-content: space-between;">
        <div class="new-mobile_sharedyn-input" style="margin-top: 0px;">
            <textarea placeholder="分享留言" class="new-mobile_sharedyn-input_box" id = "id_sharegrp_textarea" oninput="SmateCommon.isSupassMax();"></textarea>
            <input type = "hidden" id = "des3ResId" value = "${des3ResId }"/>
            <input type="hidden" value = "${queryStr }" id = "queryStr"/>
            <input type="hidden" id = "resHistoryPage" value = ""/>
        </div>
        <!-- first==title|second==authorNames|third==briefDesc|fourth==hasFullText|fifth==thumbnailPath|sixth==publishYear -->
        <div class="new-mobile_sharedyn-shareitem" id = "share_to_scm_box"  style="justify-content: flex-start; padding: 12px; min-height: 0px; height: auto; display: flex;align-items: flex-start;">
            <!-- 项目begin -->
            <c:if test="${(shareType eq 'prj' || shareType eq 'prjDetail') && status eq 'success'}">
                <div class="paper_cont" style="margin-left: 5px !important;">
                  <p style="text-align: left;overflow :hidden;text-overflow: ellipsis;white-space: nowrap;width: 92vw;font-weight: bold;font-size: 15px;">${first}</p>
                  <p class="p3" style="text-align: left;overflow:hidden;text-overflow: ellipsis;white-space: nowrap;width: 92vw; line-height: 20px;">${second}</p>
                  <p class="p3 f999" style="text-align: left;overflow:hidden;text-overflow: ellipsis;white-space: nowrap;width: 92vw; line-height:20px;">
                    <span style="max-width: 100%;">${third}</span>
                  </p>
                </div>
             </c:if>
            <!-- 项目end -->
            <!-- 成果begin -->
            <c:if test="${(shareType eq 'sns' || shareType eq 'pdwh' || shareType eq 'snsDetail' || shareType eq 'pdwhDetail') && status eq 'success' }">
                <div class="paper_content-container_list-avator" style="float: left;">
                    <c:choose>
                        <c:when test="${fourth eq true && not empty fifth}">
                        <!--有全文(fourth)并且有缩略图(fifth) -->
                            <img  class="paper_content-list_save-avator" src="${fifth}"
                                  onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" />
                        </c:when>
                        <c:when test="${fourth eq true && empty fifth}">
                        <!--有全文(fourth)但是没有缩略图(fifth) -->
                            <img  class="paper_content-list_save-avator" src="${resmod }/images_v5/images2016/file_img1.jpg"
                                  onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" />
                        </c:when>
                        <c:otherwise>
                        <!--没有全文 -->
                            <img  class="paper_content-list_save-avator" src="${resmod }/images_v5/images2016/file_img.jpg"
                                  onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'" />
                        </c:otherwise>
                    </c:choose>
                  </div>
                  <div class="paper_cont" style="margin-left: 12px;">
                    <p class="paper_cont—title">
                      <a href = "javascript:void(0);" style="text-decoration: none;width: 83vw;display: block;" class="pubTitle" id = "shareTitleSub">${first}</a>
                    </p>
                    <p class="p1" style="overflow:hidden;text-overflow: ellipsis;white-space: nowrap;width: 100%; max-width:100%; line-height: 20px;"><c:out value="${second}" escapeXml="false"/></p>
                    <p class="f999  paper_content-container_list-source paper_content-container_list-box" style="width: 83vw; max-width: 83vw; height: 20px; line-height: 20px;">
                      <span class="paper_content-container_list-source_detail" style="width: auto;max-width: 70%;">${third }</span>
                      <c:if test="${not empty sixth}"><span class="fccc paper_content-container_list-source_time" style="margin-right: 8px;" >${sixth}</span></c:if>
                    </p>
                  </div>
            </c:if>
            <!-- 成果end -->
            <!-- 基金begin -->
            <c:if test="${(shareType eq 'fund' || shareType eq 'fundDetail' || shareType eq 'aidInsDetail') && status eq 'success'}">
                <div class="paper_content-container_list-avator">
                  <input type = "hidden" id = "zhTitle" value = "${first }">
                  <input type = "hidden" id = "enTitle" value = "${fifth }">
                  <input type = "hidden" id = "zhShowDesc" value = "${sixth }">
                  <input type = "hidden" id = "enShowDesc" value = "${seventh }">
                  <div>
                    <c:choose>
                        <c:when test="${not empty fourth }">
                             <!-- 有logo -->
                            <img src="${fourth }" onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'" style="width: 12vw; height: 12vw;border-radius: 50%;"></a>
                        </c:when>
                        <c:otherwise>
                            <img src="${ressns }/images/default/default_fund_logo.jpg" onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'" style="width: 12vw; height: 12vw;border-radius: 50%;"></a>
                        </c:otherwise>
                    </c:choose>
                  </div>
                </div>
                <div style="padding-left: 12px;">
                  <div class="paper_list-box_title webkit-multipleline-ellipsis">
                     <c:out value="${first}" />
                  </div>
                  <div class="paper_list-box_author">
                    <c:out value="${second}" />
                  </div>
                  <div class="paper_list-box_introdu">
                    <c:out value="${third}" />
                  </div>
                </div>
            </c:if>
            <!-- 基金end -->
            <!-- 资助机构begin -->
            <c:if test="${shareType eq 'aidIns' && status eq 'success'}">
                  <div class="new-financial_body-item_avatar">
                    <img src="${third }" onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'"
                      class="new-financial_body-item_avatar-detail" />
                  </div>
                  <div style="display: flex; flex-direction: column; flex-grow: 1;">
                    <div style="display: flex; align-items: center; justify-content: space-between;">
                      <div class="new-financial_body-item_infor">
                        <div class="new-financial_body-item_infor-work">${first }</div>
                        <div class="new-financial_body-item_infor-adddress">${second }</div>
                      </div>
                    </div>
                 </div>
            </c:if>
            <!-- 资助机构end -->
          <!--  新闻begin -->
          <c:if test="${shareType eq 'news' && status eq 'success'}">
            <div class="new-financial_body-item_avatar">
              <img src="${third }" onerror="this.src='/resmod/smate-pc/img/logo_newsdefault.png'"
                   class="paper_content-list_save-avator" />
            </div>
            <div class="paper_cont" style="margin-left: 12px;">
              <p class="paper_cont—title">
                <a href = "javascript:void(0);" style="text-decoration: none;width: 83vw;display: block;" class="pubTitle" id = "">${first}</a>
              </p>
              <p class="p1" style="overflow:hidden;text-overflow: ellipsis;white-space: nowrap;width: 83vw; line-height: 20px;"><c:out value="${second}" escapeXml="false"/></p>
            </div>
          </c:if>
          <!-- 新闻end -->
            <c:if test="${status eq 'error' }">
                <div class="new-mobile_Privacy-tip_container" style="background: rgb(238, 238, 238); height: 60%;">
                    <div class="new-mobile_Privacy-tip_container" style="height: 100vh;">
                        <div class="new-mobile_Privacy-tip_avator"></div>
                    <div class="new-mobile_Privacy-tip_content">
                                                      访问的资源不存在<a href = "javascript:void(0);" style = "color: #568ec8;font-weight: bold;" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">返回</a>
                    </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</body>  
</html>
