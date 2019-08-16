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
<script type="text/javascript" src="${resmod }/js/weixin/iscroll.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript">
var model="${model}";
var des3ChatPsnId="${des3ChatPsnId}";  //"gdC9pv0cs%2BvMbeuihiyATw%3D%3D" ;
midmsg.loadclienheight=0;
    window.onload = function(){
        //样式加载初始化
        addFormElementsEvents();
        if(des3ChatPsnId!=""){
        	$("div[list-main='mobile_msg_chatbox__list']").attr("code",des3ChatPsnId);
            //$("#id_meun_header").attr("des3PsnId",des3ChatPsnId) ; 自己的psnId
            midmsg.loadMsgChatList(des3ChatPsnId,1,"html");
        }
        if($("#id_meun_header_new").attr("des3PsnId")!=""){         
          $("#id_meun_header").css("display", "flex");
          $("#id_meun_header_new").css("display", "flex");
        }
        $("#mid_msg_content").keyup(function(){
        	var receiverIds=$("div[list-main='mobile_msg_chatbox__list']").attr("code");
            if($.trim(receiverIds) === ""){
                scmpublictoast("请添加联系人",1000,3);
                $("#mid_msg_content").val("");
                return  false;
            }

        });
    }
    
    //添加添加联系人界面
    function addContacts(){
    	window.location.href="/psnweb/mobile/friendlistotselect";   
    }
    
    function search(){
        // 使输入框失去焦点，退出手机键盘
        document.getElementsByTagName("input")[0].blur();
        document.getElementsByTagName("input")[1].blur();
    };
</script>
</head>
<body>
  <div class="message-page">
    <div>
      <div class="message-page__header" id="id_meun_header" des3PsnId=""
        style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
        <span style="width: 100%; display: flex; justify-content: center; align-items: center;"> <span class=""
          style="width: 10%; font-size: 16px;"></span> <span class="message-page__psnheader-title" chat="new"
          style="width: 80%">新建消息</span> <span class="material-icons" style="width: 10%; font-size: 16px;"
          onclick="midmsg.openchatpsnlist()">取消</span>
        </span>
      </div>
      <div class="new_mobile-send_message-neck"
        style="top: 48px; position: fixed; left: 0px; width: 100vw; overflow-x: hidden;">
        <div class="new_mobile-send_message-neck_input">
          <span class="new_mobile-send_message-neck_input-box">联系人:&nbsp;${chatPsnName }</span> <span
            class="new_mobile-send_message-neck_input-contacts"></span>
        </div>
        <div class="new_mobile-send_message-neck_add" onclick="addContacts();">添加</div>
      </div>
      <div class="message-page__header" id="id_meun_header_new" des3PsnId="${des3ChatPsnId }"
        style="position: fixed; top: 0px; z-index: 55; display: none; justify-content: space-between;">
        <i class="material-icons" onclick="midmsg.openchatpsnlist()" style="margin-left: 15px; width: 10vw;">keyboard_arrow_left</i>
        <span style="width: 80vw; display: flex; justify-content: center; align-items: center;"><span
          class="message-page__psnheader-title">${chatPsnName }</span></span> <i style="width: 10vw; margin-right: 15px"></i>
      </div>
    </div>
    <div class="message-page__body">
      <div class="dev_meun_item" id="id_msg_box">
        <!-- 消息BOX -->
        <!-- 站内信 会话聊天框 -->
        <div class="state-content__box">
          <div class="state-content__commit">
            <div code="${des3ChatPsnId}" class="main-list__list item_no-padding" list-main="mobile_msg_chatbox__list"
              chat-ui="new" changepage="false"
              style="overflow: auto; padding-top: 0px; position: fixed; top: 100px; bottom: 102px; background: #fff; width: 100vw; left: 0px;">
              <!-- 消息列表 -->
            </div>
          </div>
        </div>
        <div class="state-footer" id="id_chatbox_footer"
          style="height: auto; position: fixed; bottom: 0px; left: 0px; width: 100%">
          <textarea class="state-footer__input" placeholder="输入你想说的话" maxlength="500" style="width: 100%;"
            id="mid_msg_content"></textarea>
          <div class="state-footer__selector">
            <div>
              <img onclick="midmsg.showfilesUI()" class="state-footer__selector-tip"
                src="/resmod/smate-pc/img/acquiescence-add-file1.png"> <img onclick="midmsg.showpubsUI()"
                class="state-footer__selector-tip1" src="/resmod/smate-pc/img/add-Achievements2.png">
            </div>
            <div class="state-footer__selector-save" onclick="midmsg.sendtextmsg(event)">发送</div>
          </div>
        </div>
      </div>
      <div id="pub-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 0px;">
          <div class="paper__func-tool main-list__searchbox" list-search="mobile_msg_chatpub_list">
            <div class="paper__func-box searchbox__main" style="width: 90%;">
              <i class="paper__func-search" style="background-position: 3px -1px; width: 9%;"></i>
              <form action="javascript:search();" style="width: 100%;">
                <input type="search" style="line-height: 27px; font-size: 100%; border: none;"
                  class="paper__func-search__flag" placeholder="检索成果">
              </form>
            </div>
          </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatpub_list"
            style="padding-top: 108px;">
            <!-- 成果列表 -->
          </div>
        </div>
      </div>
      <div id="file-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 0px;">
          <div class="paper__func-tool main-list__searchbox" list-search="mobile_msg_chatfile_list">
            <div class="paper__func-box searchbox__main" style="width: 90%;">
              <i class="paper__func-search" style="background-position: 3px -1px; width: 9%;"></i>
              <form action="javascript:search();" style="width: 100%;">
                <input type="search" style="line-height: 27px; font-size: 100%; border: none;"
                  class="paper__func-search__flag" placeholder="检索文件">
              </form>
            </div>
          </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatfile_list"
            style="padding-top: 108px;">
            <!-- 文件列表 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>