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
<script type="text/javascript" src="${resmod }/js_v8/common/jquery/jquery-3.4.1.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="/resmod/js/link.status.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/smate.materialize.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/group/mobile.grp.main.js"></script>
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
		if("${dto.des3PubId}"){
          Group.getpubinfo("${dto.des3PubId}");
          Group.allowPublish($("#dynrealtime"));
        } 
		if($("#dyntextarea").val()){
		  Group.allowPublish($("#dynrealtime"));
		}
        $('#dyntextarea').on('valuechange', function (e, previous) {
           if($(this).val().length>0&&$(this).val().trim()!=""){
             Group.allowPublish($("#dynrealtime"));
           }else{
              if($("#des3ResId").val() == null || $("#des3ResId").val() == ""){
               Group.dontAllowPublish($("#dynrealtime"));
              }
           }
        })
	    var value=$("#dyntextarea").val(); //获取光标最后一个文字的后面获得焦点
		$("#dyntextarea").val("").focus().val(value); 
	    document.getElementById("content2").style.height  = window.innerHeight - 120 + "px";
	});
	
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
  <input type="hidden" name="des3psnId" id="des3psnId" value="${dto.des3PsnId}">
  <input type="hidden" id="dynTextHide" value="">
  <input type="hidden" name="dynResType" id="dynResType" value="0">
  <input type="hidden" id="des3PubId" value="${dto.des3PubId}">
  <input type="hidden" id="des3GrpId" name="des3GrpId" value="${dto.des3GrpId }"/>
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
      <a onclick="Group.backToGrpHomepage();" id="to_dyn_list"><span id="to_dyn_list_span"></span></a>
    </div>
    <a onclick="showPublishCancelTip();" id="dynrealclose" class="fl"><i class="material-icons close">close</i></a>
    <div id="dynrealtime" class="share_no fr">发&nbsp;布</div>
    <div id="toast-container"></div>
  </div>
  <div class="top_clear"></div>
  <div class="content" style="max-height: auto;">
    <div class="pa16" id="content1" style="display: flex;flex-direction: column;justify-content: space-between;">
      <div id="content2" style="display: flex;flex-direction: column;justify-content: space-between;">
      <div>
        <!-- 人员信息 -->
        <div class="dynamic_one">
          <a href="javascript:;" class="dynamic_head"><img src="${psnInfo.avatars}"
            onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
          <p>
            <em>${psnInfo.psnName}</em>
          </p>
          <p class="p1">${psnInfo.insInfo}</p>
        </div>
        <!-- 文字输入框 -->
        <textarea id="dyntextarea" cols="30" oninput="checkmaxlength();" maxlength="501" placeholder="编辑动态" class="textarea_box" style="overflow-y: auto;">${dto.dynText }</textarea>
      </div>
      <!-- 选中的成果信息  begin-->
      <div id="paper" class="paper" style="display: none;">
        <div class="border_top" style="margin: 0 0 12px 0"></div>
        <a href="javascript:;" onclick="Group.delPub();" class="delete_wrap"><i class="material-icons delete">close</i></a>
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
      <!-- 选中的成果信息  end-->
      </div>
      <form id="pubselectform" action="/grp/publish/pub" method="post">
         <input id="fromPage" name="fromPage" type="hidden" value="grpDyn" />
         <input id="pubListType" name="pubListType" type="hidden" value="grpPub" />
         <input id="showPrjPub" name="showPrjPub" type="hidden" value="1" />
         <input id="dynText" name="dynText" type="hidden" value="" />
         <input id="dynGrpDes3Id" name="des3GrpId" type="hidden" value="${dto.des3GrpId }"/>
      </form>
    </div>
    <div style="min-height: 42px"></div>
  </div>
  <div class="file_mn file_mn-shareitem_arrray">
    <div class="file_mn-share_arrrayitem file_mn-sharegroup_icon" onclick="Group.selectPub('grpPub')">
        <div class="new-mobilepage_footer-item_tip-Achievements"></div>
        <span class="file_mn-share_arrrayitem-title">群组成果</span>
    </div>
    <div class="file_mn-share_arrrayitem file_mn-shareself_icon" onclick="Group.selectPub('myPub')">
        <div class="new-mobilepage_footer-item_tip-file"></div>
        <span class="file_mn-share_arrrayitem-title">个人成果</span>
    </div>
    
    <!-- <a href="javascript:void(0);" class="file_mn-sharegroup_icon" style="width: 18%; height: 24px; display: flex; align-items: center; justify-content: center;"><i class="material-icons file_upload" onclick="Group.selectPub('grpPub')">description</i></a>
    <a href="javascript:void(0);" class="file_mn-shareself_icon" style="width: 18%;"><i class="material-icons file_upload" onclick="Group.selectPub('myPub')">description</i></a> -->
  </div>
</body>
</html>
