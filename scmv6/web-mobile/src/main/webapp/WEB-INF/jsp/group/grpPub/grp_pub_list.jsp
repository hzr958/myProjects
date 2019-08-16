<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var snsctx = "${snsctx}";
$(function(){
    //弹出框
});
</script>
<div class="js_listinfo" smate_count='${totalCount}'></div>
<c:forEach items="${pubList}" var="pub" varStatus="status">
    <div class="paper main-list__item" style="display: flex; justify-content: flex-start; margin: 0px;">
      <div class="paper_content-container_list-avator">
          <c:if test="${pub.hasFulltext == 1}">
              <a href="javaScript:void(0);" 
              <c:if test="${pub.isOwn == 1 || (pub.isOwn == 0 && pub.fullTextPermission == 0)}">
               onclick="GrpPub.downloadFile('${pub.fullTextUrl }')"
              </c:if>
              <c:if test="${pub.isOwn == 0 && pub.fullTextPermission != 0}">
               onclick="mobile.pub.requestFTFile('${pub.des3PubId }','sns','${pub.des3RecvPsnId}','true')"
              </c:if>
              class="new-tip_container-content">
                <img class="paper_content-list_save-avator" src="${pub.fullTextImaUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'"/>
              </a>          
           </c:if>
          <c:if test="${pub.hasFulltext != 1 && pub.isOwn == 0}">
              <img  class="paper_content-list_save-avator" onclick="mobile.pub.requestFTFile('${pub.des3PubId }', 'SNS', '${pub.des3RecvPsnId}');" src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'"/>
          </c:if>
          <c:if test="${pub.hasFulltext != 1 && pub.isOwn == 1}">
              <img  class="paper_content-list_save-avator" onclick="mobile.pub.smatemobile();" src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'"/>
          </c:if>
          <span class="paper_content-container_list-icon">
              <c:if test="${pub.labeld==1 }">
                   <span class="pub-idx__icon_grant-mark"></span>
              </c:if>
              <c:if test="${pub.updateMark==1}">
                   <i class="demo-tip_onlineimport-icon"></i>
              </c:if>
              <c:if test="${pub.updateMark==2}">
                   <i class="demo-tip_inlineimport-icon"></i>
              </c:if>
          </span>
      </div>

      <div class="paper_cont paper_content-listcontainer" >
          <div class="paper_content-secondcontaine">
              <div class="paper_content-inforbox">
                  <div>
                    <div class="pubTitle paper_cont-title-detail_link" onclick="javascript:
                       <c:if test="${not empty pub.pubIndexUrl}">location.href='${pub.pubIndexUrl}'</c:if>
                       <c:if test="${empty pub.pubIndexUrl}">openPubDetail('${pub.des3PubId }')</c:if>">
                        ${not empty pub.zhTitle ? pub.zhTitle : pub.enTitle} 
                    </div>
                    <div class="page-body_program-detail_item-author" style="text-align: left; display: block; width: 68vw;">${pub.authors}</div>
                    
                    <div class="f999  paper_content-container_list-source" style="text-align: left;">
                      <span class="paper_content-container_list-source_detail" style="width: auto;max-width: 70%;">${pub.zhBrif}</span><span class="fccc" style="padding-left: 3px;"><c:if test="${not empty pub.publishYear}"> | </c:if>${pub.publishYear}</span>
                    </div>
                  </div>
                  <div class="main-list__item_actions">
                      <c:if test="${pub.relevance == 1}">
                        <div class="grp-correlation degree_2"></div>
                      </c:if>
                      <c:if test="${pub.relevance == 2}">
                        <div class="grp-correlation degree_3"></div>
                      </c:if>
                      <c:if test="${pub.relevance >= 3}">
                        <div class="grp-correlation degree_4"></div>
                      </c:if>
                      <c:if test="${pub.relevance == null || pub.relevance == 0}">
                        <div class="grp-correlation degree_1"></div>
                      </c:if>
                  </div>
              </div>
               <%--社交操作start --%>
              <c:set var="isAward" value="${pub.isAward}"></c:set>
              <c:set var="resDes3Id" value="${pub.des3PubId }"></c:set>
              <c:set var="awardCount" value="${pub.awardCount}"></c:set>
              <c:set var="shareCount" value="${pub.shareCount}"></c:set>
              <c:set var="showCollection" value="0"></c:set>
              <c:set var="showComment" value="0"></c:set>
              <c:set var="currentPage" value="${pubQueryModel.nextId}"></c:set>  
              <c:set var="currentIndex" value="${stat.index}"></c:set>        
              <%@ include file="/common/standard_function_bar.jsp" %>
             <%--社交操作 end--%>
         </div>
         
      </div>
 </div>    
</c:forEach>
