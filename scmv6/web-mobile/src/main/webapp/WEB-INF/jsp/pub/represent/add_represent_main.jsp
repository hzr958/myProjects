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
  //初始化检索输入框
  var searchInputOptions = {
      "searchFunc": "doSearch();", //点击检索图标执行函数
      "formAction": "", //form表单action属性
      "inputOnkeydown": "listenSearchInputKeydown(event);", //检索框onkeydown事件
      "inputOnchange": "doChange();", //检索框onchange事件
      "searchFilter": "select_filter();", //过滤条件图标点击事件
      "needFilter": true, //是否需要显示过滤条件图标
      "placeHolder": "检索成果",
      "searchInputVal": "${pubQueryModel.searchKey}" //检索的字符串
  };
  commonMobileSearch.initSearchInput(searchInputOptions);
  RepresentPub.loadOpenPubList();//加载成果
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
function doSearch(){
  var string = $("#searchStringInput").val();
  string = filterHTMLTag(string);
  $("#searchStringInput").val($.trim(string));
  $("#searchKey").val($.trim(string));
  RepresentPub.loadOpenPubList();//加载成果
}
function select_filter(){
  $("#select_filter_action").submit();
}
function doChange(){
  var string = $("#searchStringInput").val();
  $("#searchKey").val($.trim(string));
}
function filterHTMLTag(msg) {
  var msg = msg.replace(/<\/?[^>]*>/g, ''); //去除HTML Tag 
  msg = msg.replace(/&npsp;/ig, ''); //去掉npsp 
  return msg; 
}
//离开网页后执行
window.onunload=function(){
    var hrefs = location.href;
    if(location.href.indexOf('?') ==-1){
      hrefs = location.href+"?searchKey="+ $("#searchKey").val();
    }else if(location.href.indexOf('searchKey') ==-1){
      hrefs = location.href+"&searchKey="+ $("#searchKey").val();
    }else{
      hrefs = hrefs.replace(/searchKey=[^&]*(&|$)/,"");
      hrefs += "&searchKey="+ $("#searchKey").val();
    }
    history.replaceState('','',hrefs);
};


/**
 * 监听移动端软键盘的回车搜索按键
 */
 function listenSearchInputKeydown(e) {
  var searchKey = $(".paper__func-box input").val();
  if ($.trim(searchKey) != "") {
    var event = e.which || e.keyCode;
    if (event == 13) {
      $("#searchKey").val(searchKey);
      $("#searchStringInput").val($.trim(searchKey));
      RepresentPub.loadOpenPubList();
    }
  }
}
</script>
</head>
<body>
 <form action="/pub/represent/enteraddpubcondition" method="get" id="select_filter_action">
    <input type="hidden" id="orderBy" name="orderBy" value="${pubQueryModel.orderBy}" /> 
    <input type="hidden" id="publishYear" name="publishYear" value="${pubQueryModel.publishYear}" /> 
    <input type="hidden" id="pubType" name="pubType" value="${pubQueryModel.pubType}" /> 
    <input type="hidden" id="searchKey" name="searchKey" value="${pubQueryModel.searchKey }" /> 
    <input type="hidden" id="includeType" name="includeType" value="${pubQueryModel.includeType }" /> 
</form>
    <input type="hidden" id="representDes3PubIds" name="representDes3PubIds" value="${pubQueryModel.representDes3PubIds }" /> 

  <div class="m-top new_page-header_backcover new_page-header_func-tool">
    <div style="display: flex; align-items: center; justify-content: space-between; width: 100vw;">
      <a class="fl mypub" onclick="window.location.replace('/pub/represent/editenter');"><i class="material-icons" style="color: #fff;">keyboard_arrow_left</i></a>
      <span style="color: #fff !important;">添加代表性成果</span>  
      <a class="fr"><i class="filter_list" style="color: #fff;font-size: 14px;padding-right: 12px;diplay:none"></i></a>
    </div>
  </div>
    <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>
  <div class="top_clear" style="height: 100px;"></div>
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