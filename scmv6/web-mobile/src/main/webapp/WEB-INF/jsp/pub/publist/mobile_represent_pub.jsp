<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function doEditRepresentPub(){
    document.location.href = "/pub/represent/editenter";
}
/* function commonResAward(obj){
  mobile.snspub.awardoptnew(obj);
}
function commonShowReplyBox(obj){
   var des3PubId = $(obj).attr("resId");
   mobile.snspub.details(des3PubId, 'single');
} */
/* function commonShare(obj){
  //需求变更,进入页面分享
   var des3PubId = $(obj).attr("resId");
  mobile.pub.sharePubBox(des3PubId); 
} */


/* function callBackAward(obj,awardCount){
  if(awardCount>999){
    awardCount = "1k+";
  }
  $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
  var isAward = $(obj).attr("isAward");
  if (isAward == 1) {// 取消赞
    $(obj).attr("isAward", 0);           
    if (awardCount == 0) {
      $(obj).find('span').text("赞");
    } else {
      $(obj).find('span').text("赞" + "(" + awardCount + ")");
    }
  } else {// 赞
    $(obj).attr("isAward", 1);
    $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
  }
} */
</script>
<input type="hidden" id="isSelf" value="${pubListVO.self}" />
<input type="hidden" id="hasLogin" value="${pubListVO.hasLogin}" />
<input type="hidden" id="represent_prj_size" value="${fn:length(pubListVO.resultList) }" />
<c:if test="${pubListVO.self == 'yes' || pubListVO.resultList.size()>0}">
  <div class="app_psn-main_page-body_item-title dev_psn_represent_pub">
    <span>代表成果</span> 
    <c:if test="${pubListVO.self == 'yes'}">
        <i class="app_psn-main_page-body_item-icon edit_icon_i" onclick="doEditRepresentPub();" style=""></i>
    </c:if>
  </div>
  <div class="app_psn-main_page-body_program-detail">
    <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
      <div class="app_psn-main_page-body_program-container"  style="width: 97vw; margin-right: 14px;">
        <div class="page-body_program-detail_item represent_pub_item" style="border: none;">
          <img class="page-body_program-detail_item-avator"
            <c:if test="${pubListVO.fromPage=='psn'}"> 
                <c:if test="${pub.fullTextFieId != null && pub.fullTextFieId !='' && not empty pub.fullTextDownloadUrl}">
                        <c:if test="${not empty pub.fullTextImgUrl }">
                             src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" onclick="Msg.downloadFTFile('${pub.fullTextDownloadUrl}')" />
                        </c:if>
            <c:if test="${empty pub.fullTextImgUrl }">
                            src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" onclick="fullTextUpTips()"/>
                        </c:if>
         </c:if>
        <c:if test="${pub.fullTextFieId == null || pub.fullTextFieId =='' || empty pub.fullTextDownloadUrl}">
                            src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'" onclick="fullTextUpTips()"/>
                        </c:if>
        </c:if>
        <c:if test="${pubListVO.fromPage!='psn'}">
                    <c:if test="${pub.fullTextPermission ==0 && not empty pub.fullTextFieId}">
                    src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" onclick="Msg.downloadFTFile('${pub.fullTextDownloadUrl}')" />
                    </c:if>
                    <c:if test='${pub.fullTextPermission !=0}'> 
                        <c:if test = '${empty pub.fullTextFieId}'>
                            src="${pub.fullTextImgUrl}" onclick="mobile.pub.requestFTFile('${pub.des3PubId}','sns','${pubListVO.des3SearchPsnId}','false')" onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'"/>
                        </c:if>
                        <c:if test = '${not empty pub.fullTextFieId}'>
                            src="${pub.fullTextImgUrl}" onclick="mobile.pub.requestFTFile('${pub.des3PubId}','sns','${pubListVO.des3SearchPsnId}','true')" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'"/>
                        </c:if>
                    </c:if>
        </c:if> 
<div class="page-body_program-detail_item-content" onclick="mobile.snspub.details('${pub.des3PubId}', 'single');">
  <div class="page-body_program-detail_item-title">${pub.title }</div>
  <div class="page-body_program-detail_item-author"><c:out value="${pub.authorNames }" escapeXml="false"/></div>
  <div class="page-body_program-detail_item-time">${pub.briefDesc }</div>
</div>
</div>

 <%--社交操作start --%>
        <c:set var="isAward" value="${pub.isAward}"></c:set>
        <c:set var="resDes3Id" ><iris:des3 code='${pub.pubId }'/></c:set>
        <c:set var="awardCount" value="${pub.awardCount}"></c:set>
        <c:set var="commentCount" value="${pub.commentCount}"></c:set>
        <c:set var="shareCount" value="${pub.shareCount}"></c:set>
        <c:set var="showCollection" value="0"></c:set>
        <!-- 代表成果全部是个人库成果 -->
        <c:set var="resType" value="sns"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>
 
</div>
</c:forEach>
</div>
</c:if>
<div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
  onclick="javascript: $('#dynamicShare').hide();">
  <div class="screening_box" style="display: flex; justify-content: center;">
    <div class="screening" id="shareScreen" style="max-width: 400px"
      onclick="mobile.pub.quickShareDyn(1,'dev_publist_share');">
      <h2>
        <a href="javascript:;">立即分享到科研之友</a> <input type="hidden" class="dev_publist_share" value="" name="sharePubId" />
      </h2>
    </div>
  </div>
</div>
