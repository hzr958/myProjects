<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/global.css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/scmmobileframe.css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc-mobiletip.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/mobile.pub.js"></script>
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
<script type="text/javascript" src="${resmod}/mobile/js/plugin/mobile.fresh.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript">
var model="${model}";
var whoFirst = "${whoFirst}";
	window.onload = function(){
        selectMsgPages(model);
        //为成果认领和全文认领谁先显示赋值
        $("#whoFirst").val(whoFirst);
        //调整下拉数据的显示
        document.getElementById("show_pub_fulltext_rcmd_list").style.height = window.innerHeight - 154/* 217 */ + "PX";
		//样式加载初始化
   		addFormElementsEvents();
		//更新全文认领和成果认领
		//midmsg.showMsgTip();
		//消息统计数
        midmsg.loadMsgStatus(function(){
            //消息通知、站内信、全文请求url控制
            midmsg.menumodel(model);
        });
		//载入页面切换样式事件
   		midmsg.loadMain();
		//消息通知、站内信、全文请求切换请求事件绑定
   		midmsg.meunItemsEvent();
   		//消息通知、站内信、全文请求检索事件绑定
   		midmsg.ChatPsnListInputEvent();
		
   		//限制发送站内信文体最大500个字符
   		//midmsg.checkMsgInput(500);
   		var clienheight = document.body.clientHeight; 
   	 	var heightlist = document.getElementsByClassName("main-list__list"); 
   		for(var i=0; i < heightlist.length;i++){
   			heightlist[i].style.height = clienheight - 184 + 18 - 30 + "px";
   			document.getElementById("msg-chat_friend_all-list").style.height = clienheight - 180 + "px";
   		}
   		/* 原基础值为-158  20180327改为 -158-25 测试部分机型是否会出现问题，解决I6plus显示不全的问题 */
   	    document.getElementById("main-list_container").style.height = clienheight - 158 - 25 + "px";
   	    var targetlist = document.getElementsByClassName("searchbox__main");
   	    for(var i = 0; i< targetlist.length; i++){
   	    	targetlist[i].querySelector("input").onfocus = function(){
   	    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
   	    	}
   	    	targetlist[i].querySelector("input").onblur = function(){
   	    			this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";
   	    				
   	    	}
   	    }
   	    document.getElementsByClassName("message-page__neck-search")[0].onfocus = function(){
   	    	this.style.border="none";
   	    }
   	    document.getElementsByClassName("message-page__neck-search")[0].onblur = function(){
	    	this.style.border="none";
	    }
   	
   	 document.getElementById("msg-chat_friend_all-list__content").style.height = document.getElementById("msg-chatfriend-list").offsetHeight + document.getElementById("msg-chatallpsn-list").offsetHeight + document.getElementById("msg-chatsearchmsg-list").offsetHeight + "px";
   	Fresh.backFresh();
   	mobile_bottom_setTag("msg");
   	
   	if($("#id_midchatsearch").val()=="" && !$("#id_midchatsearch").is(":focus")){
    	$("#showIcon1").show();
    	$("#showIcon2").hide();
	}else{
    	$("#showIcon1").hide();
    	$("#showIcon2").show();    		
	}
    $("#id_midchatsearch").focus(function(){
    	$("#showIcon1").hide();
    	$("#showIcon2").show();
    });
    $("#id_midchatsearch").blur(function(){
    	if($("#id_midchatsearch").val()==""){
	    	$("#showIcon1").show();
	    	$("#showIcon2").hide();
	    	// 继续消息检索,否则会处于抑制转转转的情况，数据也没有加载出来
	    	midmsgClick();
    	}else{
	    	$("#showIcon1").hide();
	    	$("#showIcon2").show();    		
    	}
    });
    //清除聊天窗口的位置记录缓存
    localStorage.clear();
    //当页面变化后需要重新计算列表显示区域的高度
    window.onresize = function () {
      var $showArea = document.getElementById("msg-chat_friend_all-list");
      var $clientHeight = document.body.clientHeight;
      $showArea.style.height = parseInt($clientHeight) - 167 + "px";
    }
}
	
  
	
  //根据传参确定加载的列表
  function selectMsgPages(selectModel){
    if(selectModel == "reqFullTextMsg"){
      $("#msg_items_fulltext_req").addClass("message-page__fuctool-selector");
    }else if(selectModel == "chatMsg"){
      $("#msg_items_chat").addClass("message-page__fuctool-selector");
    }else{
      $("#msg_items_notice").addClass("message-page__fuctool-selector");
    } 
  }
	
	
	changCheckBox = function() {
    if (document.getElementsByClassName("pub-idx__main_add-tip")) {
        var addlist = document.getElementsByClassName("pub-idx__main_add-tip");
        for (var i = 0; i < addlist.length; i++) {
            addlist[i].onclick = function() {
                if (this.innerHTML != "check_box") {
                    this.innerHTML = "check_box";
                } else {
                    this.innerHTML = "check_box_outline_blank";
                }
            }
        }
    }
};
	/**
	 * mid消息检索
	 */
	function midmsgClick(){
		var $input = $("#id_midchatsearch");
		var $meun_items = $("#meun_items").find("div.message-page__fuctool-item");
		var index = $meun_items.index($("#meun_items").find(".active"));
		if(index==0){
			if($("#id_msg_box").is(":hidden")){
				//检索人员会话
				if($.trim($input.val())==""){
					$meun_items.eq(index).click();
					return;
				}
				midmsg.chatPsnListInputSearch();
			}else{
				//检索消息内容
				if($.trim($input.val())==""){
					midmsg.loadMsgChatList($("div[list-main='mobile_msg_chatbox__list']").attr("code"),1,"html");
					return;
				}
				midmsg.chatMsgListInputSearch();
			}
		}else if (index==1){
			//检索消息通知
			if($.trim($input.val())==""){
				$meun_items.eq(index).click();
				return;
			}
		}else if (index==2){
			//检索全文
			if($.trim($input.val())==""){
				$meun_items.eq(index).click();
				return;
			}
		}
		// 使输入框失去焦点，退出手机键盘
		document.getElementById("id_midchatsearch").blur();
	}
</script>
</head>
<body>
  <input type = "hidden" id = "whoFirst"/>
  <div class="message-page">
    <div>
      <div class="message-page__header" id="id_meun_header" des3PsnId="${des3PsnId }"
        style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
        <i class="material-icons" style="width: 15vw; visibility: hidden;">keyboard_arrow_left</i> <span
          style="width: 70vw; display: flex; justify-content: center; align-items: center;"><span
          class="message-page__psnheader-title">消息</span></span> <span id="id_menu_header_new_msg"
          style="width: 15vw; font-size: 16px; visibility: hidden;"
          onclick='javascript:window.location.href="/dynweb/mobile/ajaxshownewcreatemsgui"'>新建</span>
      </div>
    </div>
    <div class="message-page__neck" id="id_meun_main"
      style="flex-direction: column; height: 44px; position: fixed; top: 40px; z-index: 55; background: #fff;">
      
      <div class="message-page__fuctool" id="meun_items" style="width: 100%; border: none;">
        <div class=" message-page__fuctool-item" id="msg_items_chat" item-id="msg-chatpsn-list" onclick="midmsg.loadMain();"  style="text-align: center;  border: none; line-height: 24px;">
               <span class="message-page__fuctool-item_detail">站内信<p class=""></p></span>
        </div>
        <div class="message-page__fuctool-item_line"></div>
        <div class="message-page__fuctool-item message-page__fuctool-item__center" id="msg_items_notice" 
          item-id="msg-center-list" onclick="midmsg.loadMain(); "  style="text-align: center;  border: none; line-height: 24px;">
               <span class="message-page__fuctool-item_detail">消息通知<p class=""></p></span>
        </div>
        <div  class="message-page__fuctool-item_line"></div>
        <div class="message-page__fuctool-item" item-id="msg-fulltext-list"  id="msg_items_fulltext_req"  onclick="midmsg.loadMain();" style="text-align: center; border: none; line-height: 24px;">
                <span class="message-page__fuctool-item_detail">全文请求<p class=""></p></span>
        </div>
        <div class="message-page__selector-line  setting-list_page-item_hidden"></div>
      </div>
    </div>
    <div class="message-page__body">
      <div id="msg-center-list" class="dev_meun_item ">
        <!-- <div class="main-list" style="padding-top: 147px; "> -->
        <div class="main-list" id = "show_pub_fulltext_rcmd_list" style="position: fixed;  top: 96px;overflow-y:scroll; overflow-x: hidden;">
          <div id="mobile_msg_claim_list">
            <%-- <jsp:include page="/WEB-INF/jsp/mobile/msgbox/mid2_msgcenter_list2.jsp"></jsp:include> --%>
          </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_center_list">
            <!-- 消息通知 -->
          </div>
        </div>
      </div>
      <div id="msg-chatpsn-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 82px; background: #fff; margin-top: 12px;">
          <div class="message-page__search-container_box" style="border-bottom: 1px solid #ddd;">
                  <div class="message-page__search-box main-list__searchbox" list-search="" style="margin-top: 0px;">
                    <i class="message-page__search-tip" id="showIcon1"></i>
                    <div class="searchbox__main-box">
                      <form action="javascript:midmsgClick();">
                        <input class="message-page__neck-search" type="search"
                          style="line-height: 30px; font-size: 100%; padding-left: 3px;" id="id_midchatsearch" placeholder="检索消息" />
                      </form>
                    </div>
                    <i class="message-page__search-tip" id="showIcon2" style="margin-left: 20px;" onclick="midmsgClick();"></i>
                  </div>
                </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatpsn_list"  style="background: #fff;">
            <!-- 站内信-会话列表 -->
          </div>
        </div>
      </div>
      <div id="msg-chat_friend_all-list" class="dev_meun_item main-list__list-sort__box"
        style="display: none;padding-bottom: 0px; display: flex; flex-direction: column;">
        <div id="msg-chat_friend_all-list__content">
          <div id="msg-chatfriend-list">
            <div id="title_friend"
              style="display: block; width: 108px; height: 24px; line-height: 24px; margin: 8px 0px 8px 5px; font-weight: bold;">联系人</div>
            <div class="main-list">
              <div class="item_no-padding main-list__set-container" list-main="mobile_msg_friend_psn_list">
                <!-- 站内信-联系人列表 -->
              </div>
            </div>
          </div>
          <div id="msg-chatallpsn-list">
            <div class="main-list">
              <div id="title_more"
                style="display: block; width: 108px; height: 24px; line-height: 24px; margin: 8px 0px 8px 5px; font-weight: bold;">更多人员</div>
              <div class="item_no-padding main-list__set-container" list-main="mobile_msg_all_psn_list">
                <!-- 站内信-全站列表 -->
              </div>
            </div>
          </div>
          <div id="msg-chatsearchmsg-list">
            <div class="main-list">
              <div id="chat_record"
                style="display: block; width: 108px; height: 24px; line-height: 24px; margin: 8px 0px 8px 5px; font-weight: bold;">聊天记录</div>
              <div class="item_no-padding main-list__set-container" list-main="mobile_msg_chat_record_list" style="">
                <!-- 站内信-聊天记录 -->
              </div>
            </div>
          </div>
        </div>
      </div>
      <div id="msg-fulltext-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 83px; height: 100%;">
          <div class="main-list__list item_no-padding main-list__list-sort__box" id="main-list_container" no-more-record = "no_more_record_tips"
            list-main="mobile_msg_fulltext_list" style="margin-top: 12px;">
            <!-- 全文请求 -->
          </div>
        </div>
      </div>
      <div class="dev_meun_item" id="id_msg_box" style="display: none;">
        <!-- 消息BOX -->
        <jsp:include page="/WEB-INF/jsp/mobile/msgbox/mid2_msgchat_box.jsp"></jsp:include>
      </div>
      <div id="pub-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 105px;">
          <div class="paper__func-tool main-list__searchbox" list-search="mobile_msg_chatpub_list">
            <div class="paper__func-box searchbox__main" style="width: 90%;">
              <i class="paper__func-search"></i> <input type="text"
                style="line-height: 27px; font-size: 100%; padding-left: 12px; border: none;"
                class="paper__func-search__flag" placeholder="检索成果">
            </div>
          </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatpub_list">
            <!-- 成果列表 -->
          </div>
        </div>
      </div>
      <div id="file-list" class="dev_meun_item" style="display: none;">
        <div class="main-list" style="padding-top: 102px;">
          <div class="paper__func-tool main-list__searchbox" list-search="mobile_msg_chatfile_list">
            <div class="paper__func-box searchbox__main" style="width: 90%;">
              <i class="paper__func-search"></i> <input type="text"
                style="line-height: 27px; font-size: 100%; padding-left: 12px; border: none;"
                class="paper__func-search__flag" placeholder="检索文件">
            </div>
          </div>
          <div class="main-list__list item_no-padding main-list__list-sort__box" list-main="mobile_msg_chatfile_list">
            <!-- 文件列表 -->
          </div>
        </div>
      </div>
    </div>
  </div>
  <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_bottom.jsp"></jsp:include>
</body>
</html>