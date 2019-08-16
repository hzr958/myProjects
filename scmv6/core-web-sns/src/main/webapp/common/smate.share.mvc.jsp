
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
var callbacks = callbacks || {} ;
callbacks.compose = function(){
    var newAddItem = $("#grp_friends").find(".chip__box").last();
    var newAddItemText = $.trim(newAddItem.find(".chip__text").text());
    var code =newAddItem.attr("code");
    var newItemCodeIsNotNull = code != null && code != "" && typeof(code) != "undefined";
    //去掉code不为空且code一样的
    if(newItemCodeIsNotNull){
        var objbox = $("#grp_friends").find(".chip__box[code='"+code+"']");
        if(objbox.length>1){
            objbox.eq(1).remove();
        }
    }
    //去掉text一样但code为空的
    var notLastItems = $("#grp_friends").find(".chip__box:not(:last)");
    if(notLastItems.length >= 1){
        for(var i=0; i<notLastItems.length; i++){
            var currentItem = $(notLastItems[i]);
            var currentItemCode = currentItem.attr("code");
            var currentItemCodeNotNull = currentItemCode != "" && currentItemCode != null && typeof(currentItemCode) != "undefined"; 
            //text相同，
            if(currentItem.find(".chip__text").text().trim() == newAddItemText){
                //新输入项code不为空，但当前被遍历到的项code为空，删除当前遍历项
                if(newItemCodeIsNotNull && !currentItemCodeNotNull){
                    currentItem.remove();
                }else if(!newItemCodeIsNotNull){
                    //新输入项code为空，则删除新输入项
                    newAddItem.remove();
                }
                break;
            }
        }
    }
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
    var seclist =  document.getElementsByClassName("dialogs__header_sort-box_list");
    for(var i = 0; i < seclist.length; i++){
        seclist[i].onclick = function(){
            this.closest(".dialogs__header_sort-box").querySelector(".dialogs__header_sort-box_detaile").innerHTML = this.innerHTML;
            $(this.closest(".dialogs__header_sort-box").querySelector(".dialogs__header_sort-box_detaile")).attr("val",$(this).attr("val"));
            SmateShare.loadFriendListByOrder($(this).attr("val"));
        }
    }
    /* $("#select").bind("focus", function(){ $("#select").find("option").each(function(){$(this).css("cursor", "pointer") });  }); */
});
function buildExtralParams(){
  var ids = "";
  $("#grp_friends").find(".chip__box").each(function() {
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
<div class="dialogs__box setnormalZindex" dialog-id="share_to_scm_box" flyin-direction="bottom" style="width: 600px;"
  id="share_to_scm_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="height: 56px;">
      <div class="dialogs__header_title">
        <div selector-id="list_sharetype" sel-value='1'>
          <nav class="nav_horiz">
            <ul class="nav__list">
              <li class="nav__item" onclick="SmateShare.shareToDynUI()"><spring:message code="common.share.to.dyn" /></li>
              <li class="nav__item " onclick="SmateShare.shareToFriendUI()"><spring:message
                  code="common.share.to.friend" /></li>
              <li class="nav__item" onclick="SmateShare.shareToGrpUI()"><spring:message
                  code="common.share.to.group" /></li>
            </ul>
           <!--  <test></test> -->
            <div class="nav__underline" style=""></div>
          </nav>
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed" id='id_bt_select_grp'>
    <div class="dialogs__header  js_autocompletebox dialogs__header" style="display: inline-flex;"
      request-url="/groupweb/mygrp/ajaxautogrpnames" manual-input="no" max-record=10>
      <div class="input__area  dialogs__header_title" id="grp_names">
        <input class="global_no-border dev_grp_input" placeholder='<spring:message code="common.share.search.group"/>'>
      </div>
      <div class="button__box button__model_rect" onclick="SmateShare.showShareToScmSelectGrpBox()">
        <a>
          <div
            class="button__main button__style_flat button__size_dense button__color-plain color-display_blue ripple-effect ">
            <span class="dev_button_chooise"> <spring:message code="common.share.select.group" />
            </span>
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
          <span class="share-panel__selection_desc"><spring:message code="common.share.to.shareFriends" />: </span>
          <div class="share-friends__rcmd_chips-container"></div>
        </div>
        <div class="share-friends__selection_box">
          <span class="share-panel__selection_desc"><spring:message code="common.share.to.shareList" />: </span>
          <div class="chip-panel__box inline-style" style="flex-grow: 1;" id="grp_friends" chipbox-id="chipcodeshare">
            <!-- 已选择好友列表 -->
            <div id="grp_addfriend" auto_box="true" style="min-width: 30px;line-height: 24px;"
              class="chip-panel__manual-input js_autocompletebox rich" request-url="/psnweb/friend/ajaxautofriendnames" request-data="buildExtralParams();"
              contenteditable="true" placeholder='<spring:message code="common.share.search.friendtip"/>' max-record=10></div>
          </div>
          <div class="share-panel__selection_action" onclick="SmateShare.showShareToScmSelectFriendBox()">
            <div class="button__box button__model_rect">
              <a>
                <div
                  class="button__main button__style_flat button__size_dense button__color-plain color-display_blue ripple-effect">
                  <span class="dev_select_friend"> <spring:message code="common.share.select.friend" />
                  </span>
                </div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted ">
    <div class="share-panel__main-content">
      <div class="dynamic-main__box no-attachment">
        <div class="form__sxn_row">
          <div class="input__box input_not-null">
            <div class="input__area">
              <textarea maxlength="500" class="global_no-border"
                placeholder="<spring:message code="common.share.to.new"/>" style="min-height: 120px;"
                id="id_sharegrp_textarea"></textarea>
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
  <div class="dialogs__childbox_fixed dev_dialogs_share_file_module">
    <div class="share-panel__attachments-box">
      <div class="share-attachmemts__list"></div>
    </div>
  </div>
  <!-- end -->
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <div class="multiple-button-container">
        <div class="button__box button__model_rect" onclick="SmateShare.hideShareToScmBox();">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-plain color-display_grey ripple-effect">
              <span><spring:message code="dyn.add.mydyn.cancel.label" /></span>
            </div>
          </a>
        </div>
        <div class="button__box button__model_rect" id="sharePrimary" disabled onclick='SmateShare.shareMain()'>
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
              <span><spring:message code="dyn.common.label.share" /></span>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__box setnormalZindex" dialog-id="share_to_scm_select_grp_box" flyin-direction="bottom" style="width: 480px;"
  id="share_to_scm_select_grp_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="height: 56px;">
      <div class="dialogs__header_title">
        <spring:message code="common.share.select.group" />
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
            <input id="select_grp_searchKey" placeholder='<spring:message code="common.share.search.group"/>'
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
      <div class="dialogs__header_title" id="memberChooise">
        <spring:message code="common.share.select.friend" />
      </div>
      <select id="memberTypeSelect"
        style="cursor: pointer; height: 32px; border-radius: 4px; border: 1px solid #ddd; color: #999; display: none;"
        onchange="SmateShare.loadFriendListByOrder()">
        <option class="dev_select_order-list" value="0" style="cursor: pointer;"><spring:message
            code="comment.share.select.group.member" /></option>
        <option class="dev_select_order-list" value="1" style="cursor: pointer;"><spring:message
            code="common.share.friend" /></option>
      </select>
      <div class="dialogs__header_serch-box"
        style="display: flex; width: 160px; height: 32px; line-height: 32px; border: 1px solid #ddd; margin-left: 5px; margin-right: 50px; margin-right: 50px; border-radius: 5px;">
        <i class="dialogs__header_serch-tip"></i> <input type="text" class="dialogs__header_serch-tool dev_search_key"
          onkeyup="SmateShare.loadFriendList();" placeholder="<spring:message code="common.share.searchByName"/>"
          style="outline: none; padding-left: 2px; margin-left: 2px; border-style: none; width: 90%;">
      </div>
      <div class="dialogs__header_sort-box">
        <i class="material-icons dialogs__header_sort-box-flag">swap_vert</i> <span
          class="dialogs__header_sort-box_detaile" style="width: 105px;"><spring:message
            code="common.share.dateOrder" /></span>
        <div class="dialogs__header_sort-box_item dev_select_order" style="width: 105px;">
          <span class="dialogs__header_sort-box_list" val="date"><spring:message code="common.share.dateOrder" /></span>
          <span class="dialogs__header_sort-box_list" val="name"><spring:message code="common.share.nameOrder" /></span>
        </div>
      </div>
      <%--  <div class="dialogs__header_sort" style="display:flex; justify-content: space-between; cursor: pointer; margin-right: 36px;">
        <select  class="dev_select_order" id="select" style="cursor: pointer; height: 32px; border-radius: 4px;  border: 1px solid #ddd;  color: #999;" onchange="SmateShare.loadFriendListByOrder(this.value )">
          <option class="dev_select_order-list" value="date" style="cursor: pointer;"><s:text name="common.share.dateOrder"/></option>
          <option class="dev_select_order-list" value="name" style="cursor: pointer;"><s:text name="common.share.nameOrder"/></option>
        </select>
      </div> --%>
      <!-- 存储分享者的容器，所有的人员的增减，分享人员的显示都是在这个容器中进行操作 -->
      <div id="shareFriendResults" style="display: none;"></div>
      <button class="button_main button_icon" onclick="SmateShare.hideShareToScmSelectFriendBox()">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content" style="height: 480px; overflow-y: auto;">
      <div class="friend-selection__box" id="id_grp_add_friend_names_list" totalCount="0" currentCount="0"></div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <div class="multiple-button-container">
        <div class="button__box button__model_rect" onclick="SmateShare.hideShareToScmSelectFriendBox()">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-plain color-display_grey ripple-effect">
              <span><spring:message code="dyn.add.mydyn.cancel.label" /></span>
            </div>
          </a>
        </div>
        <div class="button__box button__model_rect" onclick="SmateShare.clickFriendName()">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
              <span><spring:message code="common.label.confirm1" /></span>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</div>