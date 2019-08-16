<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <meta charset="utf-8">
    <link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/share/jquery.form.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
    <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
    <script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
    <script type="text/javascript" src="${resmod}/js/weixin/iscroll.js?version=1"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/share/mobile.smate.share.js"></script>
    <script type="text/javascript">
    $(function(){
      loadEditPage();
      MobileSmateShare.initSharePage("${vo.shareTo }");
      window.onresize = function(){
          document.getElementsByClassName("new-mobile_totarget-friend_input").style.height = window.innerHeight - 270 + "px";
          document.getElementsByClassName("new-mobile_totarget-group_input").style.height = window.innerHeight - 220 + "px";
      }
    })
    
    function loadEditPage(){
      $("#mobile_share_form").ajaxSubmit(function(message) {
        // 对于表单提交成功后处理，message为返回内容
        $("body").html(message);
        MobileSmateShare.initSharePage($("#mobile_share_shareTo").val());
        MobileSmateShare.findMayShareToGrp();
        MobileSmateShare.findMayShareToPsn();
        $("textarea").on('focus',function () {
          window.addEventListener('resize', function () {
              if (document.activeElement.tagName === 'TEXTAREA') {
                  document.activeElement.scrollIntoView({behavior: "smooth"})
              }
          })
       });
      });
      return false;
    }
    
    function choosePage(){
      $("#mobile_share_choose_form").ajaxSubmit(function(message) {
        // 对于表单提交成功后处理，message为返回内容
        $("body").html(message);
       });   
      return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转   
    }
    
    //returnType: sysReturn 系统或浏览器自带的返回键， btnReturn 页面的取消按钮
    function backToSharePageFromGrp(opt, returnType){
      if(opt == "cancel"){
        $("#mobile_share_des3GrpId").val($("#old_mobile_share_des3GrpId").val());
      }
      $("#mobile_share_form").ajaxSubmit(function(message) {
        // 对于表单提交成功后处理，message为返回内容
        $("body").html(message);
        MobileSmateShare.initSharePage($("#mobile_share_shareTo").val());
        MobileSmateShare.findMayShareToGrp();
        MobileSmateShare.findMayShareToPsn();
        if(returnType == "btnReturn"){
          window.history.back();//因为在选择联系人和群组页面，为了监听返回上一页操作放了一个#历史记录到history中
        }
       });   
      return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转   
    }
    
    function backToSharePageFromPsn(opt, returnType){
      if(opt == "cancel"){
        $("#mobile_share_des3FriendIds").val($("#old_mobile_share_des3FriendIds").val());
      }
      $("#mobile_share_form").ajaxSubmit(function(message) {
        // 对于表单提交成功后处理，message为提交页面saveReport.htm的返回内容
        $("body").html(message);
        MobileSmateShare.initSharePage($("#mobile_share_shareTo").val());
        MobileSmateShare.findMayShareToGrp();
        MobileSmateShare.findMayShareToPsn();
        if(returnType == "btnReturn"){
          window.history.back();//因为在选择联系人和群组页面，为了监听返回上一页操作放了一个#历史记录到history中
        }
       });   
      return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转   
    }
    function checkmaxlength(){
      var content = $("textarea.smate_share_text").not(":hidden").val();
      var textNum = content.length;
      if(textNum > 500){
        $("textarea.smate_share_text").not(":hidden").val(content.substring(0,500));
        newMobileTip("最大限制输入500个字符", 1000);
      }
    }
    
    
    function contactPageBackEvent(){
      backToSharePageFromPsn("cancel");
    }
    
    function grpPageBackEvent(){
      backToSharePageFromGrp('cancel');
    }
</script>
</head>
<body>
<form action="/psn/share/edit" method="post" id = "mobile_share_form" onsubmit="return loadEditPage();">
    <input type="hidden" value = "${vo.des3ResId }" id = "mobile_share_des3ResId" name = "des3ResId">
    <input type="hidden" value = "${vo.resType }" id = "mobile_share_resType" name = "resType">
    <input type="hidden" value = "${vo.shareTo }" id = "mobile_share_shareTo" name = "shareTo">
    <input type="hidden" value = "${vo.des3FriendIds }" id = "mobile_share_des3FriendIds" name = "des3FriendIds">
    <input type="hidden" value = "${vo.currentDes3GrpId }" id = "mobile_share_currentDes3GrpId" name = "currentDes3GrpId">
    <input type="hidden" value = "${vo.des3GrpId }" id = "mobile_share_des3GrpId" name = "des3GrpId">
    <input type="hidden" value = "${vo.shareText }" id = "mobile_share_shareText" name = "shareText">
    <input type="hidden" value = "${vo.fromPage }" id = "mobile_share_fromPage" name = "fromPage">
    <input type="hidden" value = "${vo.hideModule }" id = "hideModule" name = "hideModule">
    <input type="hidden" value = "${vo.des3DynId }" id = "mobile_share_des3DynId" name = "des3DynId">
</form>

<div class="paper__func-header">
     <span class="paper__func-header_function-left" onclick="window.history.back();">取消</span>
     <span></span>
     <span class="paper__func-header_function-right" id="sharebt" shareto='friend' onclick="MobileSmateShare.doShare(this)">分享</span>
</div>
<div class="new-mobilegroup_neck" style="top: 48px; background:#fff!important;">
   <c:if test="${vo.hideModule != 1 }">
     <div class="new-mobilegroup_neck-list" id="share_to_dyn_tab" onclick="MobileSmateShare.shareTypeChange(this,'dyn')"><span>分享到动态</span></div>
   </c:if> 
   <c:if test="${vo.hideModule != 2 }">  
   </c:if>      
     <div class="new-mobilegroup_neck-list new-mobilegroup_neck-list_selected" id="share_to_friend_tab" onclick="MobileSmateShare.shareTypeChange(this,'friend')"><span>分享给联系人</span></div>
   <c:if test="${vo.hideModule != 3 }">     
     <div class="new-mobilegroup_neck-list" id="share_to_grp_tab" onclick="MobileSmateShare.shareTypeChange(this,'group')"><span>分享给群组</span></div>
   </c:if> 
</div>
<div class="new-mobile_totarget-group" id = "mobile_share_to_dyn_div"  style="display: none;">
    <div class="new-mobile_totarget-group_input" style="height: 50%;width: 90%;left: 9px; top: 100px;">
         <textarea oninput="checkmaxlength()" placeholder="分享留言" class="smate_share_text"></textarea>
    </div>
</div>
<div class="new-mobile_totarget-friend" id = "mobile_share_to_friend_div">
    <div class="new-mobile_totarget-friend_header">
         <div class="new-mobile_totarget-friend_header-title">你可能想分享给:</div>
         <div class="new-mobile_totarget-friend_header-box">
            <div class="new-mobile_totarget-friend_item" id="friend_may_choose">
                
            </div>
         </div>
    </div>
    <div class="new-mobile_totarget-friend_choice">
         <div class="new-mobile_totarget-friend_choice-title">分享给:</div>
         <div class="new-mobile_totarget-friend_choice-box" id = "friend_choose">
         </div>
         <div class="new-mobile_totarget-friend_btn" onclick="MobileSmateShare.loadChoosePage('friend')">选择好友</div>
    </div>
    <div class="new-mobile_totarget-friend_input" style="height: 50%;width: 90%;left: 9px; overflow-x: hidden; overflow-y: auto;">
         <textarea oninput="checkmaxlength()" placeholder="分享留言" class="smate_share_text"></textarea>
    </div>

</div>
<div class="new-mobile_totarget-group" id = "mobile_share_to_grp_div"  style="display: none;">
    <div class="new-mobile_totarget-header">
         <div class="new-mobile_totarget-header_title">
                    分享给:
         </div>
         <div class="new-mobile_totarget-group_box" id = "grp_choose" style="height: 28px;">
         </div>
         <div class="new-mobile_totarget-group_btn" onclick="MobileSmateShare.loadChoosePage('grp')">选择群组</div>
    </div>
    <div class="new-mobile_totarget-group_input" style="height: 50%;width: 90%;left: 9px; overflow-x: hidden; overflow-y: auto;">
         <textarea oninput="checkmaxlength()" placeholder="分享留言" class="smate_share_text"></textarea>
    </div>
</div>
</body>
</html>