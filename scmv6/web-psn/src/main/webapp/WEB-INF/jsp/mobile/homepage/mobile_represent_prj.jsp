<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function openMobilePrjDetail(des3PrjId){
    location.href="/prjweb/wechat/findprjxml?des3PrjId="+des3PrjId;
}
/* function sharePrj(obj){
    $('#prjShareScreen').attr("des3ResId",$(obj).attr("des3ResId"));
    $('#prjDynamicShare').show();
}; */
function hideDiv(){
    $("#prjDynamicShare").hide();
};
function opendetail(obj){
    location.href="/prjweb/wechat/findprjxml?des3PrjId="+obj;
};
function doEditRepresentPrj(){
  document.location.href = "/prj/mobile/represent/editenter";
};

/* function commonResAward(obj){
  Project.award(obj);
}
function commonShowReplyBox(obj){
  var des3PrjId = $(obj).attr("resId");
   location.href="/prjweb/wechat/findprjxml?des3PrjId="+des3PrjId;
} */
/* function commonShare(obj){
  var des3PrjId = $(obj).attr("resId");
  $('#prjShareScreen').attr("des3ResId",des3PrjId);
  $('#prjDynamicShare').show();
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
  <input type="hidden" id="represent_prj_size" value="${fn:length(prjList) }" />
    <input type="hidden" id="isMyself" value="${isMyself }" />
  
<c:if test="${isMyself == true || prjList.size()>0}">  
  <div class="app_psn-main_page-body_item-title dev_psn_represent_prj">
    <span>代表项目</span> 
     <c:if test="${isMyself == true}">   
        <i class="app_psn-main_page-body_item-icon edit_icon_i" onclick="doEditRepresentPrj()" style=""></i>
     </c:if>
  </div>
  <div class="app_psn-main_page-body_program-detail">
    <s:iterator value="prjList" var="prj">
      <div class="page-body_program-detail_item represent_prj_item" style="flex-direction: column; padding-bottom: 12px; width: 97vw; margin-right: 14px;">
        <div class="page-body_program-detail_item-content" onclick="openMobilePrjDetail('${prj.des3Id}');"
          style="width: 100%;align-items: flex-start;">
          <div class="page-body_program-detail_item-title" style="width: 90%;">${prj.prjInfo.showTitle }</div>
          <div class="page-body_program-detail_item-author" style="width: 90%;">${prj.prjInfo.showAuthorNames }</div>
          <div class="page-body_program-detail_item-time" style="width: 90%;">${prj.prjInfo.showBriefDesc }</div>
        </div>
 
 <%--社交操作start --%>
        <c:set var="isAward" value="${prj.prjInfo.award}"></c:set>
        <c:set var="resDes3Id" >${prj.des3Id }</c:set>
        <c:set var="awardCount" value="${prj.prjInfo.awardCount}"></c:set>
        <c:set var="commentCount" value="${prj.prjInfo.commentCount}"></c:set>
        <c:set var="shareCount" value="${prj.prjInfo.shareCount}"></c:set>
        <c:set var="showCollection" value="0"></c:set>
        <c:set var="resType" value="prj"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>
       
        <!-- 社交操作 -->
<%--          <div class="paper_footer" style="margin-left: 1px; justify-content: space-around;">
        <c:if test="${hasLogin != 0}" >
          <span class="paper_footer-tool__box" isAward="${prj.prjInfo.award}" des3PrjId='${prj.des3Id}'
            onclick="Project.award(this)"> <c:if test="${prj.prjInfo.award == 1}">
              <i class="paper_footer-tool paper_footer-fabulous paper_footer-award_unlike"></i>
              <span class="dev_pub_award ">取消赞 <c:if test="${prj.prjInfo.awardCount > 0}">
                         (${prj.prjInfo.awardCount})
                        </c:if>
              </span>
            </c:if> <c:if test="${prj.prjInfo.award != 1}">
              <i class="paper_footer-tool paper_footer-fabulous"></i>
              <span class="dev_pub_award">赞 <c:if test="${prj.prjInfo.awardCount > 0}">
                         (${prj.prjInfo.awardCount})
                        </c:if>
              </span>
            </c:if>
          </span> <span class="paper_footer-tool__box paper_footer-tool__pos" onclick="opendetail('${prj.des3Id}');"> <i
            class="paper_footer-tool paper_footer-comment2"></i> <span>评论 <c:if
                test="${prj.prjInfo.commentCount>0}">
                            (${prj.prjInfo.commentCount})
                        </c:if>
          </span>
          </span> <span class="paper_footer-tool__box paper_footer-tool__pos" des3ResId="${prj.des3Id}"
            onclick="sharePrj(this)"> <i class="paper_footer-tool paper_footer-share"></i> <span
            shareprjid="${prj.des3Id}">分享 <c:if test="${prj.prjInfo.shareCount>0 }"> 
                       (${prj.prjInfo.shareCount })
                    </c:if>
          </span>
          </span>
        </c:if>
        <c:if test="${hasLogin == 0}" >
          <span class="paper_footer-tool__box" onclick="loginToScmOutside()"> 
              <i class="paper_footer-tool paper_footer-fabulous"></i>
              <span class="dev_pub_award">赞 </span>
          </span> <span class="paper_footer-tool__box paper_footer-tool__pos" onclick="loginToScmOutside()"> <i
            class="paper_footer-tool paper_footer-comment2"></i> <span>评论</span>
          </span> <span class="paper_footer-tool__box paper_footer-tool__pos" 
            onclick="loginToScmOutside()"> <i class="paper_footer-tool paper_footer-share"></i> <span>分享 </span>
          </span>
        </c:if>
        </div> --%>
        
        
      </div>
    </s:iterator>
  </div>
</c:if>
<div class="black_top" id="prjDynamicShare" style="display: none; margin-top: -1px;"
  onclick="javascript: $('#dynamicShare').hide();">
  <div class="screening_box" style="display: flex; justify-content: center;">
    <div class="screening" style="max-width: 400px" id="prjShareScreen" onclick="Project.quickShareDyn(this);hideDiv();">
      <h2 style="color: #333;">
        <a href="javascript:;">立即分享到科研之友</a>
      </h2>
    </div>
  </div>
</div>