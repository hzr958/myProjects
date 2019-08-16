
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
var callbacks = callbacks || {} ;
callbacks.compose = function(){
	var code =$("#grp_friends").find(".chip__box").last().attr("code");
	SmateShare.sureSelectFriendByCode(code);
}
callbacks.remove = function(obj){
	var code = $(obj).attr("code") ;
	SmateShare.cancelSelectFriendByCode(code);
}

$(document).ready(function(){
	var  options = {name:"chipcodeshare" ,"callbacks":callbacks}
	window.ChipBox(options );
	$("*[dialog-id='share_to_scm_box']").find(".nav__list>.nav__item").eq(1).click();
	$("#grp_names").find("input").keyup(function(e){
		if(e.keyCode!=13){
			$("#grp_names").find("input").attr("code","");
		}
	});
	var selfheight =  document.getElementById("share_to_scm_box").offsetHeight;
	var oHeight = $(document).height();
	window.onresize = function(){
	    if($(document).height() < oHeight){
	        document.getElementById("share_to_scm_box").style.height = selfheight + "px"
	        document.getElementById("share_to_scm_box").style.top =  ( $(document).height() - selfheight)/2 + "px";
	        document.getElementById("dialogs__childbox-fixedfooter").hide();
	        document.getElementById("dialogs__childbox-fixedankle").hide();
	    }else{
	        document.getElementById("share_to_scm_box").style.height = selfheight + "px";
	        document.getElementById("share_to_scm_box").style.top =  ($(document).height() - selfheight)/2 + "px";
	        document.getElementById("dialogs__childbox-fixedfooter").show();
            document.getElementById("dialogs__childbox-fixedankle").show();
	    }
	    
	}
});

function checkmaxlength(){
  var content = $("#dyntextarea").val();
  var textNum = content.length;
  if(textNum > 500){
  $("#dyntextarea").val(content.substring(0,500));
  scmpublictoast("最大限制输入500个字符", 1000); 
  }  
}

</script>
<div class="dialogs__box setnormalZindex" dialog-id="share_to_scm_box" flyin-direction="bottom" style="width: 600px;"
  id="share_to_scm_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="height: 56px;">
      <div class="dialogs__header_title">
        <div selector-id="list_sharetype" sel-value='1'>
          <nav class="nav_horiz">
            <ul class="nav__list">
              <li class="nav__item" onclick="SmateShare.shareToDynUI()"><s:text name="common.share.to.dyn" /></li>
              <li class="nav__item " onclick="SmateShare.shareToFriendUI()"><s:text name="common.share.to.friend" /></li>
              <li class="nav__item" onclick="SmateShare.shareToGrpUI()"><s:text name="common.share.to.group" /></li>
            </ul>
           <!--  <uat></uat> -->
            <div class="nav__underline" style="width: 100%"></div>
          </nav>
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed" id='id_bt_select_grp'>
    <div class="dialogs__header  js_autocompletebox" request-url="/groupweb/mygrp/ajaxautogrpnames" manual-input="no">
      <div class="input__area  dialogs__header_title" id="grp_names">
        <input class="global_no-border dev_grp_input" placeholder='<s:text name="common.share.search.group"/>'>
      </div>
      <div class="button__box button__model_rect" onclick="SmateShare.showShareToScmSelectGrpBox()">
        <a>
          <div
            class="button__main button__style_flat button__size_dense button__color-plain color-display_blue ripple-effect">
            <span><s:text name="common.share.select.group" /></span>
          </div>
        </a>
      </div>
    </div>
  </div>
  <!-- start -->
  <div class="dialogs__childbox_fixed" id='id_bt_select_friend'>
    <div class="share-panel__selection share-to-friends">
      <div class="share-panel__select-friends">
        <div class="share-friends__rcmd_box">
          <span class="share-panel__selection_desc"><s:text name="common.share.to.shareFriends" />: </span>
          <div class="share-friends__rcmd_chips-container"></div>
        </div>
        <div class="share-friends__selection_box">
          <span class="share-panel__selection_desc"><s:text name="common.share.to.shareList" />: </span>
          <div class="chip-panel__box inline-style" style="flex-grow: 1;" id="grp_friends" chipbox-id="chipcodeshare">
            <!-- 已选择联系人列表 -->
            <div id="grp_addfriend" class="chip-panel__manual-input js_autocompletebox rich"
              request-url="/psnweb/friend/ajaxautofriendnames" contenteditable="true"></div>
          </div>
          <div class="share-panel__selection_action" onclick="SmateShare.showShareToScmSelectFriendBox()">
            <div class="button__box button__model_rect">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-plain color-display_blue ripple-effect">
                  <span><s:text name="common.share.select.friend" /></span>
                </div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted ">
    <div class="share-panel__main-content" style="min-height: 200px;">
    <!-- <div class="share-panel__main-content" style="min-height: 100px;"> -->
      <div class="dynamic-main__box no-attachment">
        <div class="form__sxn_row">
          <div class="input__box input_not-null">
            <div class="input__area">
              <textarea maxlength="501" class="global_no-border" placeholder="<s:text name="common.share.to.new"/>"
                style="min-height: 60px;" id="id_sharegrp_textarea" oninput="checkmaxlength();"></textarea>
              <div class="textarea-autoresize-div">
                <br>
              </div>
            </div>
          </div>
        </div>
        <div class="dynamic-divider"></div>
        <div class="dynamic-main__att"></div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed dev_dialogs_share_file_module" id="dialogs__childbox-fixedankle">
    <div class="share-panel__attachments-box">
      <div class="share-attachmemts__list"></div>
    </div>
  </div>
  <!-- end -->
  <div class="dialogs__childbox_fixed" id="dialogs__childbox-fixedfooter">
    <div class="dialogs__footer">
      <div class="multiple-button-container">
        <div class="button__box button__model_rect" onclick="SmateShare.hideShareToScmBox();">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-plain color-display_grey ripple-effect">
              <span><s:text name="dyn.add.mydyn.cancel.label" /></span>
            </div>
          </a>
        </div>
        <div class="button__box button__model_rect" id="sharePrimary" disabled onclick='SmateShare.shareMain()'>
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
              <span><s:text name="dyn.common.label.share" /></span>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__box  setnormalZindex" dialog-id="share_to_scm_select_grp_box" flyin-direction="bottom" style="width: 480px;"
  id="share_to_scm_select_grp_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="height: 56px;">
      <div class="dialogs__header_title">
        <s:text name="common.share.select.group" />
      </div>
      <button class="button_main button_icon" onclick="SmateShare.clickSeachGrp()">
        <i class="material-icons">search</i>
      </button>
      <button class="button_main button_icon" onclick="SmateShare.hideShareToScmSelectGrpBox()">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_fixed" style="display: none;">
    <div class="dialogs__header" id="grp_seach_input">
      <button class="button_main button_icon" onclick="SmateShare.clickSeachGrp()">
        <i class="material-icons">arrow_back</i>
      </button>
      <div class="dialogs__header_title">
        <div class="input__box">
          <div class="input__area">
            <input id="select_grp_searchKey" placeholder='<s:text name="common.share.search.group"/>'
              onkeyup="SmateShare.loadGrpList();" />
          </div>
        </div>
      </div>
      <button class="button_main button_icon" onclick="SmateShare.clickcLeanSeachKey()">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <ul class="main-list__list item_text-style" id="share_to_scm_select_grp_list">
    </ul>
  </div>
</div>
<div class="dialogs__box setnormalZindex" dialog-id="share_to_scm_select_friend_box" flyin-direction="bottom" style="width: 560px;"
  id="share_to_scm_select_friend_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="height: 56px;">
      <div class="dialogs__header_title">
        <s:text name="common.share.select.friend" />
      </div>
      <button class="button_main button_icon" onclick="SmateShare.hideShareToScmSelectFriendBox()">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content" style="height: 480px; overflow-y: auto;">
      <div class="friend-selection__box" id="id_grp_add_friend_names_list"></div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <div class="multiple-button-container">
        <div class="button__box button__model_rect" onclick="SmateShare.hideShareToScmSelectFriendBox()">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-plain color-display_grey ripple-effect">
              <span><s:text name="dyn.add.mydyn.cancel.label" /></span>
            </div>
          </a>
        </div>
        <div class="button__box button__model_rect" onclick="SmateShare.clickFriendName()">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
              <span><s:text name="common.label.confirm1" /></span>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</div>