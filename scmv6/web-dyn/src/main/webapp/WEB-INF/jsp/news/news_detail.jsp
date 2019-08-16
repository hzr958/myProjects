<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>
  <c:if test="${not empty seoTitle}">
    <c:out value="${seoTitle }"/>
  </c:if>
  <c:if test="${empty seoTitle}"><s:text name="skin.main.title_scm"/>
  </c:if>
</title>
<meta name="description" content="${seoDescription}" charset="utf-8">
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/js_v5/scm.maint.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css">
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news.base.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="/resmod/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript">
var shareI18 = '分享';
$(function(){
  var previous = $(".previous-news");
  var next = $(".next-news");
  if(previous.length == 0 && next.length==1){
    $(".new-newshow_detailcontent-next").css("text-align","right");
    $(".new-newshow_detailcontent-next").css("display","flex");
    $(".new-newshow_detailcontent-next").css("justify-content","flex-end");
  }
});
//初始化 分享 插件
function initSharePlugin(obj){
	if(SmateShare.timeOut && SmateShare.timeOut == true)
		return;
	if (locale == "en_US") {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
			'language' : 'en_US'
		});
	} else {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
		});
	}
	var obj_lis = $("#share_to_scm_box").find("li");
	obj_lis.eq(1).click();
	document.getElementsByClassName("nav__item-selected")[0].classList.remove("nav__item-selected");
    document.getElementsByClassName("nav__item-container")[0].querySelector(".item_selected").classList
      .add("nav__item-selected");
};

//分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
  var shareSpan = $(".dev_news_share_"+pubId);
  var count = Number(shareSpan.text().replace(/[\D]/ig,""))+1;
  if(count>=1000){
      shareSpan.text(shareI18+"(1K+)");
  }else{
      shareSpan.text(shareI18+"("+count+")");
  }
};

function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
  shareCallback(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId);
}
</script>
</head>
<body>
<input type="hidden" name="des3NewsId" value="${newsShowInfo.des3NewsId}" id="des3NewsId" />
<div class="new-newshow_container">
       <c:if test="${isManager}">
         <div class="new-newshow_container-neckfunc">
             <div class="new-newshow_container-neckfunc_btn" onclick="NewsBase.edit('${newsShowInfo.des3NewsId}','edit');"><s:text name="news.list.edit"/></div>
         </div>
       </c:if>
         <div class="new-newshow_detailtitle" id="news_title_${newsShowInfo.id }">
           ${newsShowInfo.title }
         </div>
         <div class="new-newshow_detailcontent">
              <c:if test="${!empty newsShowInfo.brief}">
                  <div class="new-newshow_detailcontent-text new-newshow_detailcontent-textIntroduce">
                  ${newsShowInfo.brief }
                  </div>
              </c:if>
              <div class="new-newshow_detailcontent-text">
              ${newsShowInfo.content }
              </div>
              <div class="new-newshow_detailcontent-func" style="margin-top: 24px;">
                   <div class="new-Standard_Function-bar">
                           <a class="manage-one mr20"> 
                           <c:if test="${isLogin }">
                           <c:if test="${newsShowInfo.isAward == 1}">
                                <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;" isAward="1" onclick="NewsBase.newsAward('${newsShowInfo.des3NewsId}',this)">
                                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.cancel.award"/>
                                    <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount>=1000}">
                                       (1K+)
                                    </c:if>
                                    <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount<1000}">
                                        (${newsShowInfo.awardCount})
                                    </c:if>
                                  </span>
                                </div>
                           </c:if>
                           <c:if test="${newsShowInfo.isAward == 0}">
                                <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0" onclick="NewsBase.newsAward('${newsShowInfo.des3NewsId}',this)">
                                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.award"/>
                                    <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount>=1000}">
                                       (1K+)
                                    </c:if>
                                    <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount<1000}">
                                        (${newsShowInfo.awardCount})
                                    </c:if>
                                  </span>
                                </div>
                           </c:if>
                            </c:if>
                            <c:if test="${!isLogin }">
                                 <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0" onclick="NewsBase.outsideTimeOut();">
                                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.award"/>
                                    <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount>=1000}">
                                       (1K+)
                                    </c:if>
                                    <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount<1000}">
                                        (${newsShowInfo.awardCount})
                                    </c:if>
                                  </span>
                                </div>
                            </c:if>
                            </a>
                            <a class="manage-one mr20" style="cursor:default;">
                                <div class="new-Standard_Function-bar_item none-selected">
                                    <i class="new-Standard_Share-checkinfor"></i> <span class="new-Standard_Function-bar_item-title"><s:text name="news.list.view"/>
                                    <c:if test="${newsShowInfo.viewCount > 0&&newsShowInfo.viewCount>=1000}">
                                       (1K+)
                                    </c:if>
                                    <c:if test="${newsShowInfo.viewCount > 0&&newsShowInfo.viewCount<1000}">
                                        (${newsShowInfo.viewCount})
                                    </c:if>
                                    </span>
                                </div> 
                            </a>
                            <a class="manage-one mr20 dev_pdwhpub_share" onclick="SmateShare.shareRecommendNews($(this));initSharePlugin();"
                    resid="${newsShowInfo.des3NewsId}" newsId="${newsShowInfo.id }">
                                <div class="new-Standard_Function-bar_item">
                                    <i class="new-Standard_function-icon new-Standard_Share-icon"></i> 
                                    <span class="new-Standard_Function-bar_item-title dev_news_share_${newsShowInfo.id}"><s:text name="news.list.share"/>
                                    <c:if test="${newsShowInfo.shareCount > 0&&newsShowInfo.shareCount>=1000}">
                                       (1K+)
                                    </c:if>
                                    <c:if test="${newsShowInfo.shareCount > 0&&newsShowInfo.shareCount<1000}">
                                        (${newsShowInfo.shareCount})
                                    </c:if>
                                    </span>
                                </div>
                            </a> 
                        </div>  
                        <div class="new-newshow_container-content_time">${newsShowInfo.gmtPublish}</div>
              </div>
              <div class="new-newshow_detailcontent-next">
                <c:if test="${priorNews != null }">
                   <div class="new-newshow_detailcontent-next_item previous-news">
                      <span class="new-newshow_detailcontent-next_title" onclick="NewsBase.viewDetail('<iris:des3 code='${priorNews.id}'/>')">上一篇</span>
                      <div class="new-newshow_detailcontent-next_detail">
                        <a title="<c:out value='${priorNews.title }' escapeXml='true'/>" onclick="NewsBase.viewDetail('<iris:des3 code='${priorNews.id}'/>')"><c:out value='${priorNews.title }' escapeXml='true'/></a>
                      </div>
                   </div>
                </c:if>
                <c:if test="${nextNews != null }">
                   <div class="new-newshow_detailcontent-next_item next-news">
                      <div class="new-newshow_detailcontent-next_detail" style="text-align: right;">
                        <a title="<c:out value='${nextNews.title }' escapeXml='true'/>"  onclick="NewsBase.viewDetail('<iris:des3 code='${nextNews.id}'/>')"><c:out value='${nextNews.title }' escapeXml='true'/></a>
                      </div>
                      <span class="new-newshow_detailcontent-next_title"  onclick="NewsBase.viewDetail('<iris:des3 code='${nextNews.id}'/>')">下一篇</span>
                   </div>
                </c:if>
              </div>
         </div>
    </div>
<jsp:include page="/common/smate.share.jsp" />
<!-- 分享操作 -->
</body>
</html>
