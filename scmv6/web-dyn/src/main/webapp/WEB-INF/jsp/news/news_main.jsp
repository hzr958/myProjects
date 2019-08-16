<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/js_v5/scm.maint.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news.base.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script type="text/javascript" src="/resmod/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript">
    var shareI18 = '<s:text name="news.list.share"/>';
    var locale = '{locale}';

$(function(){
    NewsBase.newsList();
    document.getElementsByClassName("new-newshow_container-body")[0].style.minHeight = window.innerHeight - 305 + "px";
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

<div class="new-newshow_container">
  <div class="new-newshow_container-header">
    <div class="sort-container js_filtersection" style="margin-right: 20px;">
      <div class="sort-container_header" style="width: 130px;">
        <div class="sort-container_header-tip">
          <i class="sort-container_header-flag sort-container_header-up"></i><i class="sort-container_header-down"></i>
        </div>
        <div class="sort-container_header-title filter-section__title" style="width: 105px;">
          <s:text name="news.list.update.date"/></div>
      </div>
      <div class="sort-container_item" style="width: 128px!important;">
        <div class="filter-list vert-style option_has-stats" list-filter="dynnews">
          <div class="filter-list__section js_filtersection"  style="margin: 0; padding: 0;" filter-section="orderBy" filter-method="compulsory">
            <ul class="filter-value__list">
              <li class="filter-value__item js_filtervalue sort-container_item-list option_selected" style="padding: 0px;"
                  filter-value="update">
                <div class="filter-value__option js_filteroption sort-container_item_name" style="font-size: 14px !important; margin: 0px -10px; padding-left: 34px;">
                  <s:text name="news.list.update.date"/></div>
                <input type="checkbox" style="display: none;"/>
                <div class="filter-value__stats js_filterstats"></div>
              </li>
              <li class="filter-value__item js_filtervalue sort-container_item-list" style="padding: 0px;"
                  filter-value="heat" >
                <div class="filter-value__option js_filteroption sort-container_item_name" style="font-size: 14px !important; margin: 0px -10px; padding-left: 34px;">
                  <s:text name="news.list.heat"/></div>
                <input type="checkbox" style="display: none;"/>
                <div class="filter-value__stats js_filterstats"></div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 新闻列表  begin -->
  <div class="new-newshow_container-body main-list__list" list-main="dynnews">

  </div>
  <div class="main-list__footer">
    <div class="pagination__box" list-pagination="dynnews">
      <!-- 翻页 -->
    </div>
  </div>
  <!-- 新闻列表 end -->
</div>
<jsp:include page="/common/smate.share.jsp" />
<!-- 分享操作 -->
