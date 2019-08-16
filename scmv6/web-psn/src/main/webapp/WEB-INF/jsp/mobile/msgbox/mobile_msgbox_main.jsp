<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<%-- <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css"/> --%>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/search/mobile.search.scroll.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/moveorclick.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/mobile.fresh.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/mobile.moveitem.activity.js"></script>
<title>科研之友</title>
<script type="text/javascript">
var snsctx = "${snsctx}";
$(function(){
	searchListening();
	mobile_bottom_setTag("msg");
	$(".header_search_input").bind("search", function() {
		window.location.href="/pubweb/mobile/search/pdwhpaper?searchString=" + $.trim($(".header_search_input").val());
	});
	Msg.showMsgTip();
	Msg.showReadyShareMsg();  
	Msg.loadMoreShareMsg();
	//加载圈圈
	$(".body_content_container").doLoadStateIco({
		addWay: "append",
		status:1
	});
	Fresh.backFresh();
});
//清空搜索框
function clean_search() {
	$(".header_search_input").val("");
	$(".header_search_cancel").hide();
};
//检索框监听事件
function searchListening(){
	$(".header_search_input").keyup(function(){
		var searchkey = $.trim($(this).val());
		if(searchkey!=""&&searchkey.length>0){
			$(".header_search_cancel").show();
		} else {
			$(".header_search_cancel").hide();
		} 
	});
};
</script>
</head>
<body>
  <input id="no_msg_style" type="hidden" value="0" />
  <input id="no_fullfile_style" type="hidden" value="0" />
  <input id="pageNo" type="hidden" value="0" />
  <input type="hidden" id="currentDes3PsnId" name="currentDes3PsnId" value="${des3PsnId}" />
  <form action="" id="searchFrom" onsubmit="return false;">
    <div class="header">
      <div class="header_toolbar">
        <div class="header_toolbar_title">
          <div class="header_search_container">
            <input type="search" placeholder="检索论文、专利、人员..." maxlength="50" class="header_search_input">
            <div style="display: none;" onclick="clean_search();" class="header_search_cancel">
              <i class="material-icons fz_14">close</i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
  <div style="height: 56px;"></div>
  <div class="body_content">
    <div class="pub_fullfile_num" style="padding: 16px;">
      <div id="confirm_pub_div" onclick="Msg.confimPubList();"
        style="background-color: rgba(0, 0, 0, 0.12); height: 32px; line-height: 32px; font-size: 16px; text-align: center; display: none;">
        你有<span id="confirmPubNum" style="color: red;"></span>条成果认领未确认
      </div>
      <div style="height: 8px;"></div>
      <div id="confirm_fullText_div" onclick="Msg.confimpubftrcmd();"
        style="background-color: rgba(0, 0, 0, 0.12); height: 32px; line-height: 32px; font-size: 16px; text-align: center; display: none;">
        你有<span id="confirmfullTextNum" style="color: red;"></span>条成果全文未认领
      </div>
    </div>
    <div class="body_content_container">
      <div class="list_container"></div>
    </div>
  </div>
  <div class="no_msg" style="display: none; text-align: center;">你没有更多任务需要完成了</div>
  <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_bottom.jsp"></jsp:include>
</body>
</html>
