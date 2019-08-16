<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
.chip-panel__manual-input {
  z-index: 9;
}
</style>
<script type="text/javascript">
var role="${role}";
var ispending = "${ispending}";
$(document).ready(function(){
	window.ChipBox({
		name:"chipcodeinvite",
		callbacks: {
			compose:function(){
				GrpMember.enterCallBack();
			}
		}
	});
	addFormElementsEvents($("#grp_invited_friend_ui")[0]);
	$("#grp_member_List").doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	});
	//群组成员列表
	var $grpID = parseInt(document.getElementById("grp_params").getAttribute("smate_grp_id"));
	GrpMember.refreshMember($grpID,GrpMember.loadAddFriend());
	//群组成员列表
	if(role==1 || role==2){
		$("#grp_proposer").doLoadStateIco({
			style:"height:28px; width:28px; margin:auto;margin-top:24px;",
			status:1
		});
	//申请人
	GrpMember.showGrpProposers(1,1,2);
	}
	//推荐人员列表
	var gratarget = document.getElementsByClassName("dialogs__content")[0];
	var targetparent = document.getElementsByClassName("chip-panel__box")[0];
    var target = document.getElementsByClassName("chip-panel__manual-input")[0];
    var targetson = document.getElementsByClassName("chip-panel__Prompt")[0];
    target.onkeyup = function(){
    	targetparent.style.border="2px solid #3faffa";
        var text = this.innerHTML.trim() + "";
        if(text.length===0){
           targetson.style.display="block";
        }else{
           targetson.style.display="none";
        }
    };
    target.onclick = function(){
    	targetparent.style.border="2px solid #3faffa";
    };
    targetson.onclick = function(){
        target.focus();
        targetparent.style.border="2px solid #3faffa";
    };  
   target.onblur = function(){
	    targetparent.style.border="1px solid #eeeeee";
        var text = this.innerHTML.trim() + "";
        if(text.length - 1 != 0){
            targetson.style.display="block";
        }
    };
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
    	targetlist[i].querySelector("input").onfocus = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
    	}
    	targetlist[i].querySelector("input").onblur = function(){
    			this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";		
    	}
    }
    document.onkeydown = function(event){
        if(event.keyCode == 27){
            event.stopPropagation();
            event.preventDefault();
            GrpMember.hideInvItedFriendUI()
        }
    }
});
var $aID;
function inviteBtnInit(){
	if($("#grp_selected_friends").find(".chip__box").length>0){
		document.getElementById("dev_sendinvition").disabled = false;
		$("#grp_selected_friends").find(".chip-panel__Prompt").hide();
	}else{
		document.getElementById("dev_sendinvition").disabled = true;
		$("#grp_selected_friends").find(".chip-panel__Prompt").show();
		if($("#grp_selected_friends").find(".chip-panel__manual-input").text().length>0){
			$("#grp_selected_friends").find(".chip-panel__Prompt").hide();
		}
	}
	$aID = window.requestAnimationFrame(function(){
		inviteBtnInit();
	});
}
//群组成员列表
;function showGrpMembers(){
	$.ajax({
		url : '/groupweb/grpmember/ajaxshowgrpmembers',
		type : 'post',
		dataType:'html',
		data:{
			'des3GrpId':$("#grp_params").attr("smate_des3_grp_id"),
			'pageNo':1,
			'pageSize':10
		},
		success : function(data) {
			$("#grp_member_List").html(data);
		},
		error: function(){
		}
	}); 
}; 
function buildExtralParams(){
  var ids = "";
  $("#grp_selected_friends").find(".chip__box").each(function() {
    var des3PsnId = $(this).attr("code");
    if (des3PsnId != "") {
      ids += "," + des3PsnId;
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
    return {"des3PsnId":ids};
 }
</script>
<!-- <div class="container__horiz"> -->
<div class="container__horiz_left width-8">
  <div class="container__card">
    <div class="main-list">
      <div class="main-list__header">
        <div class="main-list-header__title"></div>
        <div class="main-list-header__searchbox" style="margin-right: 54px;">
          <div class="searchbox__container main-list__searchbox" list-search="grpmember">
            <div class="searchbox__main">
              <input placeholder=" <s:text name='groups.member.search' />">
              <div class="searchbox__icon material-icons"></div>
            </div>
          </div>
        </div>
        <c:if test="${role==1||role==2||role==3}">
          <button class="button_main button_primary-reverse" onclick='GrpMember.showInvItedFriendUI()'>
            <s:text name='groups.member.invite' />
          </button>
        </c:if>
      </div>
      <div class="main-list__list" id="grp_member_List" list-main="grpmember">
        <!-- 群组成员列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="grpmember">
          <!-- 翻页 -->
        </div>
      </div>
    </div>
  </div>
</div>
<div class="container__horiz_right width-4">
  <div class="container__card" id="grp_proposer">
    <!-- 群组申请人员 -->
  </div>
  <div class="container__card" id="grp_referrers_List">
    <!-- 群组推荐成员列表 -->
  </div>
</div>
<!-- </div> -->
<div class="dialogs__box dialogs__childbox_limited-bigger" id="grp_invited_friend_ui" dialog-id="grp_invited_friend_ui"
  flyin-direction="top">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='groups.member.inviteFriends' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content" style="height: 280px;">
      <div class="friend-selection__box" id="grp_invited_friend_list">
        <!-- 联系人列表 -->
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__content global__padding_8">
      <div class="chip-panel__box" id='grp_selected_friends' chipbox-id="chipcodeinvite" style="position: relative;">
        <!-- 已选择联系人列表 -->
        <div class="chip-panel__manual-input js_autocompletebox" manual-input="no" onkeydown="SmateCommon.commonDelSelectedFriend(this,event);"
          request-url="/psnweb/friend/ajaxautofriendnames" request-data="buildExtralParams();" autofilled="true" contenteditable="true" tabindex="2" autotext="true" auto_box="true"></div>
        <div class="chip-panel__Prompt" style="position: absolute; color: #ccc; left: 12px; top: 12px; z-index: 6;">
          <s:text name='groups.member.inviteFriendsTips' />
        </div>
      </div>
      <div class="global__para_caption" style="white-space: normal;">
        <s:text name='groups.member.inviteFriends.desc' />
      </div>
      <div class="dev_template_deal">
        <a class="dev_template_deal-download_file" onclick="GrpMember.downloadEmailExcel(event)"><s:text
            name='groups.member.inviteFriends.dowloadtemplate' /></a> <span><s:text
            name='groups.member.inviteFriends.upfile' /></span> <a class="dev_template_deal-upload_file"
          onclick="GrpMember.uploadEmailExcel(event)"><s:text name='groups.member.inviteFriends.uptemplate' /></a>
        <div style="display: none;">
          <input type="file" id="emailExcelFile" name="emailExcelFile" onchange="GrpMember.uploadFile('emailExcelFile')" />
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse " id="dev_sendinvition"
        onclick='GrpMember.sendFriendInvition(this)' disabled>
        <s:text name='groups.member.inviteFriends.send' />
      </button>
      <button class="button_main button_primary-cancle" onclick='GrpMember.hideInvItedFriendUI()'>
        <s:text name='groups.member.inviteFriends.cancel' />
      </button>
    </div>
  </div>
</div>
<div class="dialogs__box" style="width: 720px;" id='grp_proposes_ui' dialog-id="grp_proposes_ui" flyin-direction="right"
  cover-event="hide">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='groups.member.manage.viewAllTitle' />
      </div>
      <button class="button_main button_primary-reverse"
        onclick='GrpMember.disposegrpAllApplications(1,GrpMember.disposegrpApplicationCallBack3,this)'>
        <s:text name='groups.member.manage.viewAllAccept' />
      </button>
      <button class="button_main button_icon" onclick='GrpMember.hidePropsesList()'>
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id='grp_proposes_list'>
      <!-- 申请人列表 -->
    </div>
  </div>
</div>
