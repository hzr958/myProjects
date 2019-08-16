var MobileNews = MobileNews ? MobileNews : {}  ;


/**
 * 分享新闻
 */
MobileNews.shareNews = function (obj) {
    BaseUtils.mobileCheckTimeoutByUrl("dynweb/ajaxtimeout", function() {
        SmateCommon.mobileShareEntrance($(obj).attr("resid"),"news");
    });
}


MobileNews.openNewsDetail = function (des3NewsId) {
  BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
    window.location.href=("/dynweb/mobile/news/details?des3NewsId=" + encodeURIComponent(des3NewsId));
  }, 0);
}
