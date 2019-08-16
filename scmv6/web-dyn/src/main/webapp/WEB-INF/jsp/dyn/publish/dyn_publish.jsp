<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script type="text/javascript" src="/ressns/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="/ressns/js/dyn/dynamic.common_${locale}.js"></script>
<script type="text/javascript" src="/ressns/js/dyn/dynamic.mobile.detail.js?version=20181013"></script>
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="/resmod/js/link.status.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/smate.materialize.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript">
	$(function(){
	    document.getElementById("dyntextarea").style.maxHeight = window.innerHeight - 270 + "px";
        document.getElementById("dyntextarea").style.overflowY = "auto";
		autosize(document.querySelectorAll('textarea'));
		$('#dyntextarea').focus();
		var ua = navigator.userAgent.toLowerCase();	
		if (/iphone|ipad|ipod/.test(ua)) {
			    $("#dynrealclose").css({"margin-left":12});		
		}
		if("${des3PubId}"){
			dynamic.getpubinfo("${des3PubId}");
			dynamic.allowPublish($("#dynrealtime"));
		} 
		if($("#dynTextHide").val()){
			 dynamic.allowPublish($("#dynrealtime"));
		}
		if("${wxOpenId}"){
			smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
		}
		 $('#dyntextarea').on('valuechange', function (e, previous) {
			 if($(this).val().length>0&&$(this).val().trim()!=""){
				 dynamic.allowPublish($("#dynrealtime"));
			 }else{
				 if($("#des3ResId").val().length==0){
					dynamic.dontAllowPublish($("#dynrealtime"));
				 }
			 }
	    })
	    var value=$("#dyntextarea").val(); //获取光标最后一个文字的后面获得焦点
		$("#dyntextarea").val("").focus().val(value); 

	    document.getElementById("content2").style.height  = window.innerHeight - 120 + "px";

	});

	function select_pub(){
		var text=$("#dyntextarea").val();
		$("#dynText").val(text);		
		$("#pubselectform").submit();
	}
	
	function showPublishCancelTip(){
		$("#publish_cancel_tip").show();
	}
	
	function confirmCancelPublish(){
		$("#to_dyn_list_span").click();
	}
	
	function stayHere(){
		$("#publish_cancel_tip").hide();
	}
	function checkmaxlength(){
	  var content = $("#dyntextarea").val();
      var textNum = content.length;
      if(textNum > 500){
        $("#dyntextarea").val(content.substring(0,500));
          scmpublictoast("最大限制输入500个字符", 1000);
      }
	}
</script>
</head>
<body>
  <input type="hidden" name="des3psnId" id="des3psnId" value="${des3psnId}">
  <input type="hidden" id="dynTextHide" value="${dynText}">
  <div class="black_top" style="display: none" id="publish_cancel_tip">
    <div class="screening_box">
      <div class="screening">
        <h2>删除这份草稿？</h2>
        <div class="screening_tx" id="dynpubtitle"></div>
        <p>
          <input type="button" onclick="stayHere()" value="保&nbsp;&nbsp;留" class="determine_btn" /><input type="button"
            value="删&nbsp;&nbsp;除" onclick="confirmCancelPublish()" class="cancel_btn" />
        </p>
      </div>
    </div>
  </div>
  <div class="m-top-other">
      <div style="display: none;">
           <a href="/dynweb/dynamic/show" id="to_dyn_list"><span id="to_dyn_list_span"></span></a>
      </div>
      <a onclick="showPublishCancelTip();" id="dynrealclose" class="fl"><i class="material-icons close">close</i></a>
      <div id="dynrealtime" class="share_no fr">发&nbsp;布</div>
      <div id="toast-container"></div>
  </div>
  <div class="top_clear"></div>
  <div class="content" style="height:100%; position: fixed; top: 48px;  left: 0px; width: 100%;">
      <div class="pa16" id="content1" style="display: flex;flex-direction: column;justify-content: space-between; height: 100%;">
        <div id="content2" style="display: flex;flex-direction: column;justify-content: space-between;">
        <div>
            <div class="dynamic_one">
                <a href="javascript:;" class="dynamic_head"><img src="${psnAvatars}"
                  onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
                <p>
                  <em>${psnName}</em>
                </p>
                <p class="p1">${psnInsAndPos}</p>
            </div>
            <input type="hidden" name="dynResType" id="dynResType" value="0">
            <!-- 动态资源类型 -->
            <textarea id="dyntextarea" style="word-break:break-all;" cols="30" oninput="checkmaxlength();" maxlength="501" placeholder="编辑动态" class="textarea_box">${dynText }</textarea>
            <input type="hidden" id="des3ResId" value="${des3PubId}">
        </div>
  
        <div id="paper" class="paper" style="display: none;">
          <div class="border_top" style="margin: 0 0 12px 0"></div>
          <a href="javascript:;" onclick="dynamic.delPub();" class="delete_wrap"><i class="material-icons delete">close</i></a>
          <img class="publishPub" src="/resmod/images_v5/images2016/file_img.jpg"
            onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'" />
          <div class="paper_cont" style="margin-left: 65px !important;width: 75%;">
            <p class="paper_cont—title">
              <a target="_blank" href="javascript:;" id="pubtitle"></a>
            </p>
            <p class="p1 paper_cont—author" id=author></p>
            <p class="p3 f999 paper_cont—time">
              <em id="journal"><em>
            </p>
          </div>
        </div>
        </div>
        <form id="pubselectform" action="/pub/querylist/psn" method="post">
          <input id="fromPage" name="fromPage" type="hidden" value="dyn" /> <input id="articleType" name="articleType"
            type="hidden" value="1" /> <input id="dynText" name="dynText" type="hidden" value="" />
        </form>
      </div>
      <div style="min-height: 42px"></div>
  </div>
  <div class="file_mn">
    <div class="file_mn-selfcontainer" onclick="select_pub()">
        <div class="new-mobilepage_footer-item_tip-file"></div>
        <span class="file_mn-share_arrrayitem-title">个人成果</span>
    </div>
  </div>
</body>
</html>
