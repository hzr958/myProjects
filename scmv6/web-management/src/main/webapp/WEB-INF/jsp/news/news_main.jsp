<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />

<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />

<link href="${resmod}/css/smate.scmtips.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/achievement.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/smate.alerts.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/home.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news.base.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<%--<script type="text/javascript" src="${resmod}/js/plugin/judge-browser/judge-browser.js"></script>--%>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script type="text/javascript" src="/resmod/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript">
    var shareI18 = '分享';
    var locale = '${locale}';
    var snsctx='/scmwebsns';
    var resmod ='/resmod';
    var ressns ='/ressns';
    var resscmsns='/resscmwebsns';
    var locale='zh_CN';

    $(function(){
    NewsBase.newsList("management");
    document.getElementsByClassName("new-newshow_container-body")[0].style.minHeight = window.innerHeight - 305 + "px";
});
    //微信分享
    function getQrImg(url){
        if(navigator.userAgent.indexOf("MSIE")>0){
            $("#share-qr-img").qrcode({render: "table",width: 175,height:175,text:url });
        }else{
            $("#share-qr-img").qrcode({render: "canvas",width: 175,height:175,text:url });
        }
    }
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
      <div class="sort-container_item" style="width: 128px!important;">
        <div class="filter-list vert-style option_has-stats" list-filter="dynnews">
          <div class="filter-list__section js_filtersection"  style="margin: 0; padding: 0;" filter-section="orderBy" filter-method="compulsory">
            <ul class="filter-value__list">
              <li class="filter-value__item js_filtervalue sort-container_item-list option_selected" style="padding: 0px;"
                  filter-value="seqNo">
                <div class="filter-value__option js_filteroption sort-container_item_name" style="font-size: 14px !important; margin: 0px -10px; padding-left: 34px;">
                  创建时间</div>
                <input type="checkbox" style="display: none;"/>
                <div class="filter-value__stats js_filterstats"></div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="new-newshow_container-header_add" onclick="NewsBase.add();"><s:text name="news.base.add" />  </div>
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
<!-- 超时弹框 -->
<%@ include file="/skins_v6/login_box.jsp"%>
