<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="set-email__main-title">
  <span class="set-email__main-heading" style="font-size: 20px; font-weight: normal;"><s:text
      name="psnset.attentionset" /> </span>
</div>
<div class="set-email__container">
  <div class="set-attention__subheading">
    <div class="set-attention__subtitle">
      <span><s:text name="attention.has.attention" /> <s:text name="psnset.colon" /></span> <span id="attPsnCountId">(${userSettings.totalCount}<s:text
          name="attention.people.unit" />)
      </span>
    </div>
    <div style="margin-top: 12px; margin-left: -10px;">
      <div class="set-attention__container" id="attention_psn_list_id"></div>
    </div>
  </div>
</div>
<div class="set-attention__footer">
  <!-- start -->
  <div class="dialogs__childbox_fixed" id='id_bt_select_friend'>
    <div class="share-panel__selection share-to-friends">
      <div classs="share-to__friends-heading">
        <s:text name="attention.want" />
        <s:text name="psnset.colon" />
      </div>
      <div class="share-panel__select-friends" style=" width: 904px; max-width: 904px; border-bottom: none;">
        <div class="share-friends__selection_box" style="border: 1px solid #ccc;">
          <span class="share-panel__selection_desc"><s:text name="attention.select.people" /></span>
          <div class="chip-panel__box inline-style" style="flex-grow: 1;" id="grp_friends" chipbox-id="chipcodeshare">
            <!-- 已选择联系人列表 -->
            <div id="grp_addfriend" class="chip-panel__manual-input js_autocompletebox rich" manual-input="no" style="line-height: 25px;" onkeydown="SmateCommon.commonDelSelectedFriend(this,event);"
              request-url="/psnweb/friend/ajaxautofriendnames" request-data="PsnsettingAttention.buildExtralParams();"
              contenteditable="true" max-record=10></div>
          </div>
          <div class="share-panel__selection_action share-panel__addfriend"
            onclick="SmateShare.showShareToScmSelectFriendBox()">
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
      <s:if test="#attr.lang eq  'zh'">
        <div class="">输入联系人姓名，然后选择提示的人名 或 点击"选择联系人"按钮</div>
      </s:if>
      <s:else>
        <div class="">Please enter contact's name then choose the name of the prompt or click the [Select Friends]
          button to search friends to follow.</div>
      </s:else>
    </div>
  </div>
  <div class="set-attention__footer-container">
    <div class="set-attention__addsave" onclick="PsnsettingAttention.addAtttionFriend();">
      <s:text name="attention.add" />
    </div>
  </div>
</div>
<div class="dialogs__box" dialog-id="share_to_scm_select_friend_box" attention="true" flyin-direction="bottom"
  style="width: 560px;" id="share_to_scm_select_friend_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="common.share.select.friend" />
      </div>
      <%-- <select  class="dev_select_order" id="select" style="cursor: pointer; height: 32px; border-radius: 4px;  border: 1px solid #ddd;  color: #999;">
		  <option class="dev_select_order-list" value="date" style="cursor: pointer;"><s:text name="common.share.select.friend"/></option>
		  <option class="dev_select_order-list" value="name" style="cursor: pointer;">联系人</option>
		</select>--%>
      <div class="dialogs__header_serch-box"
        style="display: flex; width: 160px; height: 32px; line-height: 32px; border: 1px solid #ddd; margin-right: 90px; border-radius: 5px;">
        <i class="dialogs__header_serch-tip"></i> <input type="text" class="dialogs__header_serch-tool dev_search_key"
          onkeyup="SmateShare.loadFriendList();" placeholder="<s:text name="common.share.searchByName"/>"
          style="outline: none; padding-left: 2px; margin-left: 2px; border-style: none; width: 90%;">
      </div>
      <div class="dialogs__header_sort"
        style="display: flex; justify-content: space-between; cursor: pointer; margin-right: 36px;">
        <select class="dev_select_order" id="select"
          style="cursor: pointer; height: 32px; border-radius: 4px; border: 1px solid #ddd; color: #999;"
          onchange="SmateShare.loadFriendListByOrder(this.value )">
          <option class="dev_select_order-list" value="date" style="cursor: pointer;"><s:text
              name="common.share.dateOrder" /></option>
          <option class="dev_select_order-list" value="name" style="cursor: pointer;"><s:text
              name="common.share.nameOrder" /></option>
        </select>
        <%--   <i class="material-icons">swap_vert</i>
         <div class="dialogs__header_sort-box"   style="cursor: pointer;" onmouseover="SmateShare.selectFriendModuleToShowOrder(this)"><s:text name="common.share.nameOrder"/></div>
         <div class="dialogs__header_sort-content" style="position: absolute; top: 24px; left:22px; display: none; width: 71px; border: 1px solid #ddd; cursor: pointer; background: #fff;">
             <div class="dialogs__header_sort-item" style="cursor: pointer;"   onclick="SmateShare.loadFriendListByOrder('date' ,this);"><s:text name="common.share.dateOrder"/></div>
             <div class="dialogs__header_sort-item" style="cursor: pointer;"  onclick="SmateShare.loadFriendListByOrder('name' ,this);"><s:text name="common.share.nameOrder"/></div>
         </div> --%>
      </div>
      <!-- 存储分享者的容器，所有的人员的增减，分享人员的显示都是在这个容器中进行操作 -->
      <div id="shareFriendResults" style="display: none;"></div>
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