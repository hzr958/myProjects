<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
  window.removeEventListener("popstate", grpPageBackEvent);
  window.removeEventListener("popstate", contactPageBackEvent);
  $(function(){
      document.getElementsByClassName("new-mobile_totarget-friend_input").style.height = window.innerHeight - 270 + "px";
      document.getElementsByClassName("new-mobile_totarget-group_input").style.height = window.innerHeight - 220 + "px";
      window.onresize = function(){
          document.getElementsByClassName("new-mobile_totarget-friend_input").style.height = window.innerHeight - 270 + "px";
          document.getElementsByClassName("new-mobile_totarget-group_input").style.height = window.innerHeight - 220 + "px";
      }
    })
</script>
<form action="/psn/share/choosefriend" method="post" id = "mobile_share_choose_form" onsubmit="return choosePage();">
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
         <textarea class="smate_share_text" oninput="checkmaxlength()" placeholder="分享留言" id="msg_textarea"></textarea>
    </div>
</div>
<div class="new-mobile_totarget-friend" id = "mobile_share_to_friend_div">
    <div class="new-mobile_totarget-friend_header">
         <div class="new-mobile_totarget-friend_header-title">你可能想分享给:</div>
         <div class="new-mobile_totarget-friend_header-box">
            <div class="new-mobile_totarget-friend_item" id = "friend_may_choose">
                
            </div>
         </div>
    </div>
    <div class="new-mobile_totarget-friend_choice">
         <div class="new-mobile_totarget-friend_choice-title">分享给:</div>
         <div class="new-mobile_totarget-friend_choice-box" id = "friend_choose">
         </div>
         <div class="new-mobile_totarget-friend_btn" onclick="MobileSmateShare.loadChoosePage('friend')">选择好友</div>
    </div>
    <div class="new-mobile_totarget-friend_input" style="height: 50%;width: 90%;left: 9px;">
         <textarea class="smate_share_text" oninput="checkmaxlength()" placeholder="分享留言" id="msg_textarea" ></textarea>
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
    <div class="new-mobile_totarget-group_input" style="height: 50%;width: 90%;left: 9px;">
         <textarea class="smate_share_text"  oninput="checkmaxlength()" placeholder="分享留言" id="msg_textarea"></textarea>
    </div>
</div>
<jsp:include page="show_share_res_info.jsp"></jsp:include>