<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
.new-Standard_Function-bar .new-Standard_Function-bar_item:hover .new-Standard_Function-bar_item-title {
    color: #666 !important;
}
</style>

<%--
isAward：0未赞，1已赞
resDes3Id：资源id
awardCount：赞统计数
showComment：是否显示评论功能，0不显示
commentCount：评论数量
showShare：是否显示分享功能，0不显示
shareCount：分享数
showCollection：是否显示收藏功能，0不显示
isCollection：0未收藏，1已收藏
resType: 22基准库
pubDb：pubDb ：基准库：PDWH，个人：SNS
collectName：收藏的名称（如收藏，关注）
unCollectName：取消收藏的名称（如取消收藏，取消关注）
currentIndex:当前的第几条
function commonResAward(obj){//赞

}
function commonShowReplyBox(obj){//评论

}
function commonShare(obj){//分享

}
function commonCollectnew(obj){//收藏

}

function callBackAward(obj,awardCount){//赞回调

}
function callBackReply(obj){

}
function callBackShare(obj){

}
function callBackCollect(obj){

}
  --%>
<input type="hidden" id="dddd" value="${pubDetailVO.des3PubId}"/>
<input type="hidden" id="dssddd" value="${awardCount}"/>


<div class="new-Standard_Function-bar" style="margin-bottom: 12px;">
    <a class="manage-one" isAward="${isAward }"  resType="${resType}"  resId="${resDes3Id}"  pubDb="${pubDb }"  onclick="commonResAward(this);"> 
         <c:if test="${isAward != 0}">
             <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;width:100%;">
               <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> 
               <span class="new-Standard_Function-bar_item-title span_award" resId="${resDes3Id}">取消赞
                  <c:if test="${awardCount > 0 && awardCount<=999 }">
                      (${awardCount})
                  </c:if>
                  <c:if test="${awardCount > 999 }">
                      (1k+)
                  </c:if>   
               </span> 
             </div>                     
          </c:if>
          <c:if test="${isAward == 0}">
             <div class="new-Standard_Function-bar_item" style="margin-left: 0px;width:100%">
               <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> 
               <span class="new-Standard_Function-bar_item-title span_award"  pubDb="${pubDb }" resId="${resDes3Id}">赞
                  <c:if test="${awardCount > 0 && awardCount<=999 }">
                      (${awardCount})
                  </c:if>
                  <c:if test="${awardCount > 999 }">
                      (1k+)
                  </c:if>                   
               </span>
             </div>
          </c:if>
    </a>
    <c:if test="${showComment != 0  }">
      <a class="manage-one dev_pub_comment">
        <div class="new-Standard_Function-bar_item" style="width:100%;">
          <i class="new-Standard_function-icon new-Standard_comment-icon"></i> 
          <span class="new-Standard_Function-bar_item-title span_comment" resType="${resType}" resId="${resDes3Id}"  pubDb="${pubDb }" currentPage="${currentPage }" currentIndex="${currentIndex }" onclick="commonShowReplyBox(this)">评论
              <c:if test="${commentCount > 0 && commentCount<=999 }">
                  (${commentCount})
              </c:if>
              <c:if test="${commentCount > 999 }">
                  (1k+)
              </c:if>            
          </span>
        </div>
      </a>
    </c:if>
    <c:if test="${showShare != 0  }">
        <a class="manage-one dev_pub_share">
          <div class="new-Standard_Function-bar_item" style="width:100%;">
            <i class="new-Standard_function-icon new-Standard_Share-icon"></i> 
            <span class="new-Standard_Function-bar_item-title span_share" resType="${resType}"  pubDb="${pubDb }" resId="${resDes3Id}" fundId="${fundId }" resType = "${resType }" onclick="commonShare(this)">分享
              <c:if test="${shareCount > 0 && shareCount<=999 }">
                  (${shareCount})
              </c:if>
              <c:if test="${shareCount > 999 }">
                  (1k+)
              </c:if>
            </span>
          </div>
        </a>
    </c:if>
    <c:if test="${showCollection != 0  }">
        <a class="manage-one" > 
             <c:if test="${isCollection!=0}">
                  <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="width:100%;"  resType="${resType}"  collect="${isCollection}" resId="${resDes3Id}"  pubDb="${pubDb }" onclick="commonCollectnew(this)">
                      <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                     <span class="new-Standard_Function-bar_item-title span_collect">${not empty unCollectName ? unCollectName : "取消收藏" }</span>
                  </div>
             </c:if>
             <c:if test="${isCollection==0}">
                 <div class="new-Standard_Function-bar_item" style="width:100%;" collect="${isCollection}" resId="${resDes3Id}" pubDb="${pubDb }" onclick="commonCollectnew(this)">
                        <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                       <span class="new-Standard_Function-bar_item-title span_collect">${not empty collectName ? collectName : "收藏" }</span>
                   </div>
               </c:if>
          </a>
    </c:if>      
</div>