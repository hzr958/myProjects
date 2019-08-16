<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<link href="${resmod }/css/wechat.iscroll.css?version=2" media="all" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/scmmobileframe.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/represent_mobile_pub.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>

<script type="text/javascript" src="${resmod }/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=2"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.search.scroll.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>

<script type="text/javascript">
$(function(){
  RepresentPub.loadPubList();//加载成果
});
//全文请求提示
function fullTextUpTips(){
  if (document.getElementsByClassName("bckground-cover").length == 0) {
    var innerele = '<div class="background-cover__content">请使用电脑端上传全文</div>';
    var newele = document.createElement("div");
    newele.className = "bckground-cover";
    newele.innerHTML = innerele;
    document.getElementsByTagName("body")[0].appendChild(newele);
    var windheight = newele.offsetHeight;
    var windwidth = newele.offsetWidth;
    var windbottom = (windheight - 48) / 2 + "px";
    var windleft = (windwidth - 240) / 2 + "px";
    document.getElementsByClassName("background-cover__content")[0].style.left = windleft;
    document.getElementsByClassName("background-cover__content")[0].style.bottom = windbottom;
    setTimeout(function() {
      document.getElementsByClassName("background-cover__content")[0].style.bottom = "-64px";
      setTimeout(function() {
        document.getElementsByTagName("body")[0].removeChild(newele);
      }, 500);
    }, 1500);
  }
};

</script>
</head>
<body>
  <div class="m-top new_page-header_backcover new_page-header_func-tool">
    <div style="display: flex; align-items: center; justify-content: space-between; width: 100vw;">
      <a class="fl mypub" onclick="window.location.replace('/psnweb/mobile/homepage');"><i class="material-icons" style="color: #fff;">keyboard_arrow_left</i></a>
      <span style="color: #fff !important;">设置代表性成果</span>  
      <a class="fr" href = '/pub/represent/addenter'><i class="filter_list" style="color: #fff;font-size: 14px;padding-right: 12px;">添加</i></a>
    </div>
  </div>
  <div class="top_clear"></div>
  <div class="content">
    <div class="effort_list">
        <div class="wrap_com1" id="listdiv">
          <div id="addload" style="width: 100%; height: 120px;">
              <div class="main-list">
                 <div class="main-list__list" list-main="pub_list"></div>
              </div>
          </div>
        </div>
    </div>
  </div>
</body>
</html>