<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${list }" var="news">
  <div class="main-list__item" style="display: block; padding: 12px 16px; border-bottom:1px solid #ddd; margin: 0px;">
  <div class="new-mobilenews_body-list">
    <div class="new-mobilenews_body-item_avator">
      <a  href="/dynweb/mobile/news/details?des3NewsId=${news.des3NewsId}" target="_blank">
        <img src="${news.image}" onerror="this.src='/resmod/smate-pc/img/logo_newsdefault.png'" >
      </a>
    </div>
    <div class="new-mobilenews_body-item_content">
      <div class="new-mobilenews_body-item_title" onclick="MobileNews.openNewsDetail('<iris:des3 code='${news.id}'/>')">
          ${news.title}
      </div>
      <div class="new-mobilenews_body-item_introduce">${news.brief}</div>
    </div>
  </div>
  <div class="new-Standard_Function-bar" style="margin-top: 12px;">
    <a class="manage-one mr20">
      <div class="new-Standard_Function-bar_item <c:if test="${news.isAward == 1}">new-Standard_Function-bar_selected</c:if>" style="margin-left: 0px;width:100%"  isaward="${news.isAward}" onclick="NewsBase.newsAward('${news.des3NewsId}',this)">
        <i class="new-Standard_function-icon new-Standard_Praise-icon"></i>
        <span class="new-Standard_Function-bar_item-title span_award" style="color: #666 !important;">
          <c:if test="${news.isAward == 1}">取消赞</c:if>
          <c:if test="${news.isAward == 0}">赞</c:if>
         <c:if test="${news.awardCount > 0&&news.awardCount>=1000}" >
           (1K+)
         </c:if>
          <c:if test="${news.awardCount > 0&&news.awardCount<1000}">
            (${news.awardCount})
          </c:if>
        </span>
      </div>
    </a>
    <a class="manage-one mr20 dev_pub_comment">
      <div class="new-Standard_Function-bar_item none-selected" style="width:100%;">
        <i class="new-Standard_Share-checkinfor"></i>
        <span class="new-Standard_Function-bar_item-title span_comment" >查看
        <c:if test="${news.viewCount > 0&&news.viewCount>=1000}">
          (1K+)
        </c:if>
        <c:if test="${news.viewCount > 0&&news.viewCount<1000}">
          (${news.viewCount})
        </c:if>
        </span>
      </div>
    </a>
    <a class="manage-one mr20 dev_pub_share">
      <div class="new-Standard_Function-bar_item" style="width:100%;"  resid="${news.des3NewsId}" onclick="MobileNews.shareNews(this)">
        <i class="new-Standard_function-icon new-Standard_Share-icon"></i>
        <span class="new-Standard_Function-bar_item-title span_share">分享
         <c:if test="${news.shareCount > 0&&news.shareCount>=1000}">
           (1K+)
         </c:if>
          <c:if test="${news.shareCount > 0&&news.shareCount<1000}">
            (${news.shareCount})
          </c:if>
        </span>
      </div>
    </a>
  </div>
  </div>
</c:forEach>
