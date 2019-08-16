<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form action="/psn/share/edit" method="post" id = "mobile_share_form" onsubmit="return backToSharePageFromPsn('save', 'btnReturn');">
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
<input type="hidden" value = "${vo.des3FriendIds }" id = "old_mobile_share_des3FriendIds" >
<div class="paper__func-header">
     <span class="paper__func-header_function-left"  onclick="backToSharePageFromPsn('cancel', 'btnReturn');">取消</span>
     <span>选择联系人</span>
     <span class="paper__func-header_function-right" onclick="MobileSmateShare.backShare()">确定</span>
</div>
<div class="paper__func-tool" style="top: 48px;">
    <div class="paper__func-tool-findbox" style = "width: 96%;">
        <i class="paper__func-search"></i>
        <input type="text" placeholder="检索联系人" id = "mobile_share_search_friend_key" oninput="MobileSmateShare.showChooseList(this.value,'friend')">
    </div>
</div>
<div class="new-mobilegroup_body" style="top: 96px;">
    <div class="paper_content-container main-list">
      <div class="main-list__list item_no-padding" list-main="mobile_share_psn_or_grp_list"></div>
    </div>
</div>
<script type="text/javascript">
   $(function(){
     //处理返回上一页
     if (window.history && window.history.pushState) {
       setTimeout(function(){
         window.addEventListener("popstate", contactPageBackEvent, false);
       }, 1000);
     }
     window.history.pushState('forward', null, '#');
     window.history.forward(1);
     MobileSmateShare.showChooseList($.trim($("#mobile_share_search_friend_key").val()),"friend");
     document.getElementsByClassName("new-mobilegroup_body")[0].style.height = window.innerHeight - 104 + "px";
   });

</script>