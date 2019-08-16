<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/global.css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/scmmobileframe.css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc-mobiletip.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="${ressns}/js/dyn/mid_msg/mid_msg_center.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/mid_msg/mid_msg_chat.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/mid_msg/mid_msg_fulltext.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/mid_msg/mid_msg_common_en_US.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/mid_msg/mid_msg_common_zh_CN.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/plugin/mobile.moveitem.activity.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/plugin/moveorclick.js"></script>
<script type="text/javascript" src="/resmod/js/weixin/iscroll.js"></script>
<script type="text/javascript">
var model="${model}";
var des3ChatPsnId="${des3ChatPsnId}";
var des3CurrentPsnId="${des3PsnId}";
midmsg.loadclienheight=0;
	window.onload = function(){
	  var flag = false;
		//样式加载初始化
   		addFormElementsEvents();
		if(des3ChatPsnId!=""){
			flag = midmsg.loadMsgChatList(des3ChatPsnId,1,"html");
		}
		//SCM-24040 在聊天记录中点击内容返回后要回到当前浏览的位置
		var offset = localStorage.getItem("offsetTop");
		var loadPage = localStorage.getItem("loadPage");
		 if(offset != null && loadPage != null){//有缓存的情况
		 //定时器监听聊天数据是否加载完毕
		 var timer = setInterval(function(){
		  if(flag){
            var currentloadPage = $("div[scm_id='mobile_msg_chatbox__list']").attr("scm_pageno");
            if(currentloadPage == undefined)return;//聊天数据未加载出来
            //要先加载完所有的页面，再滚动到对应高度
            if (loadPage != currentloadPage) {
              flag = midmsg.loadMsgChatList(des3ChatPsnId, loadPage, "prepend");
              return;
            }
            //滚动到记录的浏览位置的高度
            $(".main-list__list.item_no-padding").scrollTop(offset);
            recordScrollTop();
            clearInterval(timer);//完成回滚高度，关闭定时器
          }
		},100);
	  }else{
        recordScrollTop();
	  }
	 }
	function search(){
		// 使输入框失去焦点，退出手机键盘
        document.getElementsByTagName("input")[0].blur();
        document.getElementsByTagName("input")[1].blur();
	};
    //监听滚动,保存浏览位置的方法
	function recordScrollTop(){
      $(".main-list__list.item_no-padding").scroll(function(){
       if($(this).scrollTop()!=0){
         //记录当前浏览的高度
         localStorage.setItem("offsetTop", $(this).scrollTop());
         //记录当前已加载的页数
         localStorage.setItem("loadPage", $("div[scm_id='mobile_msg_chatbox__list']").attr("scm_pageno"));
       }
      });
	}
</script>
</head>
<body>
  <div class="message-page">
    <div>
      <div class="message-page__header" id="id_meun_header" des3PsnId="${des3PsnId }"
        style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
        <i class="material-icons" onclick="SmateCommon.goBack('/psnweb/mobile/msgbox?model=chatMsg');" style="margin-left: 15px; width: 10vw;">keyboard_arrow_left</i>
        <span style="width: 80vw; display: flex; justify-content: center; align-items: center;"><span
          class="message-page__psnheader-title">${chatPsnName }</span></span> <i style="width: 10vw; margin-right: 15px"></i>
      </div>
    </div>
    <div class="message-page__body">
      <div class="dev_meun_item" id="id_msg_box">
        <!-- 消息BOX -->
        <jsp:include page="/WEB-INF/jsp/mid_msg/mid2_msgchat_box.jsp"></jsp:include>
      </div>
      <div id="pub-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 105px;">
          <div class="paper__func-tool main-list__searchbox" list-search="mobile_msg_chatpub_list">
            <div class="paper__func-box searchbox__main" style="width: 90%;">
              <i class="paper__func-search" style="background-position: 3px -1px; width: 9%;"></i>
              <form action="javascript:search();" style="width: 100%;">
                <input type="search" style="line-height: 27px; font-size: 100%; border: none;"
                  class="paper__func-search__flag" placeholder="检索成果">
              </form>
            </div>
          </div>
          <div class="item_no-padding main-list__list-sort__box" style="height: 630px;">
            <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatpub_list">
              <!-- 成果列表 -->
            </div>
          </div>
        </div>
      </div>
      <div id="file-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 102px;">
          <div class="paper__func-tool main-list__searchbox" list-search="mobile_msg_chatfile_list">
            <div class="paper__func-box searchbox__main" style="width: 90%;">
              <i class="paper__func-search" style="background-position: 3px -1px; width: 9%;"></i>
              <form action="javascript:search();" style="width: 100%;">
                <input type="search" style="line-height: 27px; font-size: 100%; border: none;"
                  class="paper__func-search__flag" placeholder="检索文件">
              </form>
            </div>
          </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatfile_list">
            <!-- 文件列表 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>