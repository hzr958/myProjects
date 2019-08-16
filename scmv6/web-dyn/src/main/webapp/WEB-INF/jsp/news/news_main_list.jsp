<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${list }" var="news">
  <div class="new-newshow_container-item"  des3newsId="${news.des3NewsId }"  seqNo="${news.seqNo}">
    <div class="new-newshow_container-avator">
      <a  href="/dynweb/news/details?des3NewsId=${news.des3NewsId}" target="_blank">
      <img src="${news.image}" onerror="this.src='/resmod/smate-pc/img/logo_newsdefault.png'" >
      </a>
    </div>
    <div class="new-newshow_container-content">
      <div>
          <div class="new-newshow_container-content_title pub-idx__main_title-multipleline" style="height: 40px;overflow: hidden; line-height: 20px!important;">
            <a class="multipleline-ellipsis__content-box" href="/dynweb/news/details?des3NewsId=${news.des3NewsId}" target="_blank"  id="news_title_${news.id }"><span  title="${news.title}">${news.title}</span></a>
          </div>
          <div class="new-newshow_container-content_author">
             ${news.brief}
          </div>
      </div>
      <div class="new-newshow_container-content_foot">
        <div class="new-Standard_Function-bar">
          <a class="manage-one mr20">
          <c:if test="${isLogin }">
            <c:if test="${news.isAward == 1}">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;" isAward="1" onclick="NewsBase.newsAward('${news.des3NewsId}',this)">
                  <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.cancel.award"/>
                     <c:if test="${news.awardCount > 0&&news.awardCount>=1000}" >
                       (1K+)
                    </c:if>
                    <c:if test="${news.awardCount > 0&&news.awardCount<1000}">
                        (${news.awardCount})
                    </c:if>
                </span>
              </div>
         </c:if>
         <c:if test="${news.isAward == 0}">
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0" onclick="NewsBase.newsAward('${news.des3NewsId}',this)">
                  <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.award"/>
                  <c:if test="${news.awardCount > 0&&news.awardCount>=1000}">
                       (1K+)
                    </c:if>
                    <c:if test="${news.awardCount > 0&&news.awardCount<1000}">
                        (${news.awardCount})
                    </c:if>
                </span>
              </div>
         </c:if>
         </c:if>
         <c:if test="${!isLogin }">
             <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0" onclick="NewsBase.outsideTimeOut();">
                  <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.award"/>
                  <c:if test="${news.awardCount > 0&&news.awardCount>=1000}">
                       (1K+)
                    </c:if>
                    <c:if test="${news.awardCount > 0&&news.awardCount<1000}">
                        (${news.awardCount})
                    </c:if>
                </span>
             </div>
         </c:if>
          </a>
          <a class="manage-one mr20 thickbox" style="cursor:default;">
            <div class="new-Standard_Function-bar_item none-selected">
              <i class="new-Standard_Share-checkinfor"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.view"/>
              <c:if test="${news.viewCount > 0&&news.viewCount>=1000}">
                 (1K+)
              </c:if>
              <c:if test="${news.viewCount > 0&&news.viewCount<1000}">
                  (${news.viewCount})
              </c:if>
              </span>
            </div>
          </a>
          <a class="manage-one mr20 dev_pdwhpub_share" publish="${news.publish}" onclick="SmateShare.shareRecommendNews($(this));"
                    resid="${news.des3NewsId}" newsId="${news.id }">
            <div class="new-Standard_Function-bar_item">
              <i class="new-Standard_function-icon new-Standard_Share-icon"></i>
              <span class="new-Standard_Function-bar_item-title dev_news_share_${news.id}"><s:text name="news.list.share"/>
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
        <c:if test="${manager}">
        <div class="new-newshow_container-content_func">
          <div class="new-newshow_container-content_state">
            <c:if test="${news.publish}">
              <s:text name="news.list.published"/>
            </c:if>
            <c:if test="${!news.publish}">
              <span style="color: red"><s:text name="news.list.not.publish"/></span>
            </c:if>
          </div>
          <div class="new-newshow_container-content_item" onclick="NewsBase.edit('${news.des3NewsId}','');"><s:text name="news.list.edit" /></div>
          <div class="new-newshow_container-content_item" onclick="NewsBase.newsDel('${news.des3NewsId}','list')"><s:text name="news.list.delete" /></div>
          <div class="new-newshow_container-content_item selected-item_up" onclick="NewsBase.upMoveNews(this)"><s:text name="news.list.upmove"/> </div>
          <div class="new-newshow_container-content_item selected-item_down" onclick="NewsBase.downMoveNews(this)"><s:text name="news.list.downmove"/></div>
        </div>
          <div class="new-newshow_container-content_time" style="">${news.gmtCreate}</div>
        </c:if>
        <c:if test="${!manager}">
          <div class="new-newshow_container-content_time" style="">${news.gmtUpdate}</div>
        </c:if>
      </div>
    </div>
  </div>
</c:forEach>
