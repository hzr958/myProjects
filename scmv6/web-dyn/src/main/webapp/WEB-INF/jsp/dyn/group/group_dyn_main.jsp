<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="group_l_box">
  <input type="hidden" id="dyn_first_result" value="0" /> <input type="hidden" id="dyn_max_results" value="10" /> <input
    type="hidden" id="locale_language" value="${locale}" />
  <script type="text/javascript">
	$(document).ready(function(){
		//显示动态时间
		groupDynamic.showDynsTime();
		//加载动态附带 的评论
		groupDynamic.getCommentFordyn();
		$(".class_current_avatars").attr("src",$("#currentAvatars").val());
		//groupnames自动填词
		var data_groupnames = {
				url:"/groupweb/groupopt/ajaxautogroupnames",
				myfunction_enter:groupDynamic.listEnter,
				myfunction_show:groupDynamic.listShow ,
				mySelected:"#id_chioce_group_name",  //触发的元素
				myClass_checked:"hover",
				myLiClass_list:"text",
				myUlClass_parent:"ac_item_list",
				data:{
					maxResults:"4",
					q:""
				}
		};
		autocompleteword.getWordList($("#id_fluctuate"),data_groupnames);
		//friendname自动填词
		var data_friendname = {
				myfunction_enter:groupDynamic.FriendlistEnter,
				myfunction_show:groupDynamic.FriendListShow,
				mySelected:"#id_textarea_friend_name",  //触发的元素
				myClass_checked:"hover",
				myLiClass_list:"text",
				myUlClass_parent:"ac_item_list"
		};
		autocompleteword.getWordList($("#id_fluctuate"),data_friendname);
		groupDynamic.cleanParam();
	});
	
	//初始化 分享 插件
	function initSharePlugin(obj){
		$(obj).dynSharePullMode({
			'groupDynAddShareCount':Group.groupDynAddShareCount,
			'shareToSmateMethod' : 'groupDynamic.shareToIRIS(event)'
		});
	}
	
</script>
  <c:if test="${ currentPsnRole == 2 ||currentPsnRole == 3 || currentPsnRole == 4 }">
    <div class="dynamic_container" onclick="groupDynamic.postnewshare(this)">
      <div class="dynamic_comment_action">
        <div>
          <img src="${ currentAvatars}" class="avatar class_current_avatars">
        </div>
        <textarea class="comment_content textarea_autoresize"
          placeholder='<s:text name="group.discuss.shareSomething"></s:text>' readonly="" style="cursor: text;"></textarea>
      </div>
    </div>
  </c:if>
  <s:iterator value="groupDynShowInfoList" var="groupDynShowInfo" status="gds">
    <div class="dynamic_container">
      <input type="hidden" class="dynTime" value="${dynDateForShow }" /> ${groupDynShowInfo.dynContent }
      <div class="dynamic_operations_container">
        <div class="single_action" style="border: none;" id="group_dyn_id_${groupDynShowInfo.dynId }"
          onclick="groupDynamic.award('${groupDynShowInfo.dynId }');">
          <!--  0  表示已经赞了 -->
          <c:if test="${awardstatus ==0 }">
            <s:text name="group.discuss.canclePraise"></s:text>
            <s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
          </c:if>
          <c:if test="${awardstatus !=0 }">
            <s:text name="group.discuss.praise"></s:text>
            <s:if test="#groupDynShowInfo.awardCount!=null&&#groupDynShowInfo.awardCount!=0">(${groupDynShowInfo.awardCount })</s:if>
          </c:if>
        </div>
        <div class="single_action" onclick="groupDynamic.clickComments('${groupDynShowInfo.dynId }',this)">
          <s:text name="group.discuss.comment"></s:text>
          <s:if test="#groupDynShowInfo.commentCount!=null&&#groupDynShowInfo.commentCount!=0">(${groupDynShowInfo.commentCount })</s:if>
        </div>
        <c:if test="${resType =='pub' }">
          <div class="single_action" style="position: relative;">
            <a onclick="groupDynamic.showPubInfoToShareUINew(this); initSharePlugin(this);"
              "
									class="share_sites_show share_tile " resId="" des3ResId="" nodeId="1" resType="1" dbid=""
              style="color: #2196f3;"> <s:text name="group.discuss.share"></s:text> <s:if
                test="#groupDynShowInfo.shareCount!=null&&#groupDynShowInfo.shareCount!=0">(${groupDynShowInfo.shareCount })</s:if>
            </a>
          </div>
        </c:if>
      </div>
      <%----------附带的评论位置------------------------------------------------------------------------- --%>
      <div class="dynamic_comment_list" style="display: none;"></div>
      <div class="dynamic_comment_action" style="display: none;">
        <div>
          <img src='${ currentAvatars}' class="avatar class_current_avatars">
        </div>
        <textarea class="comment_content textarea_autoresize"
          placeholder='<s:text name="group.discuss.postComment"></s:text>'
          id="comment_content_${groupDynShowInfo.dynId }"></textarea>
        <div>
          <div class="btn_normal btn_bg_origin fc_blue500"
            onclick="groupDynamic.publishCommnet(this,'${groupDynShowInfo.dynId }')">
            <s:text name="group.discuss.post"></s:text>
          </div>
        </div>
      </div>
    </div>
  </s:iterator>
</div>
<!-- 发布新动态 -->
<div class="background_cover_layer" id="publishpage" onclick="hideDialogs(this,event)" style="display: none;">
  <div class="dialogs_whole" style="width: 560px; height: auto; max-height: calc(100% - 8px)">
    <div class="dialogs_container">
      <div class="dynamic_header">
        <div>
          <img src="" class="avatar class_current_avatars">
        </div>
        <div class="author_information">
          <div class="action">
            <div class="name" id="first_name"></div>
          </div>
          <div class="institution" id="first_institution"></div>
        </div>
      </div>
      <div class="dynamic_content_container">
        <textarea class="dynamic_share_text textarea_autoresize"
          placeholder="<s:text name="group.discuss.shareSomething"></s:text>" id="first_dynamicContent"></textarea>
        <input type="hidden" id="select_pub_id" value=""> <input type="hidden" id="select_page_no" value="1">
        <div id="select_pub"></div>
      </div>
    </div>
    <div class="share_plugin_add_attachment">
      <div class="dialogs_title_operations" style="cursor: pointer;" onclick="groupSelectPub.comeSelectPub(event);">
        <i class="material-icons fc_black54">description</i>
      </div>
    </div>
    <div class="dialogs_operations">
      <div class="btn_normal btn_bg_origin" onclick="groupDynamic.deletework(this)">
        <s:text name="group.discuss.cancel"></s:text>
      </div>
      <div class="btn_normal btn_bg_blue" onclick="groupDynamic.publishshare(this)">
        <s:text name="group.discuss.share"></s:text>
      </div>
    </div>
  </div>
</div>
<!-- 发布动态添加成果 -->
<div class="background_cover_layer" id="share_plugin_addpub" onclick="hideDialogs(this,event)" style="display: none;">
  <div class="dialogs_whole" style="width: 560px; height: 536px; max-height: calc(100% - 8px)">
    <div class="dialogs_title">
      <div class="selector_dropdown_single" onclick="groupDynamic.selectPubSource(this);"
        style="width: 120px; cursor: pointer; display: block;">
        <div class="selector_dropdown_icon"></div>
        <div class="selector_dropdown_value">
          <div class="selector_dropdown_option selected" val="1">群组成果</div>
          <div class="selector_dropdown_option" val="2">我的成果</div>
        </div>
        <div class="selector_dropdown_collections">
          <div class="selector_dropdown_option hover" val="1">群组成果</div>
          <div class="selector_dropdown_option" val="2">我的成果</div>
        </div>
      </div>
      <div class="dialogs_title_operations" style="margin-right: -8px; cursor: pointer;"
        onclick="groupSelectPub.showSearchPub(this);">
        <i class="material-icons">search</i>
      </div>
    </div>
    <div class="dialogs_title" style="display: none;">
      <div class="dialogs_title_operations" style="margin-left: -8px; cursor: pointer;"
        onclick="groupSelectPub.hiddenSearchPub(this);">
        <i class="material-icons">arrow_back</i>
      </div>
      <div class="input_field_titled">
        <input id="group_dyn_select_pub_key" name="groupName" type="text" class="input_field normal"
          placeholder="检索成果...">
        <div class="input_field_underline"></div>
        <div class="input_field_active_underline"></div>
      </div>
      <div class="dialogs_title_operations" style="cursor: pointer;" onclick="groupSelectPub.emptySerachPubKey(this)">
        <i class="list-results_close"></i>
      </div>
    </div>
    <div id="share_plugin_publist" class="dialogs_container"></div>
  </div>
</div>
<div class="background_cover_layer" id="id_fluctuate" onclick="hideDialogs(this,event)" style="display: none;">
  <div class="dialogs_whole" style="width: 560px; height: auto; max-height: calc(100% - 8px)">
    <div class="dialogs_title">
      <div class="selector_dropdown_single" id="firstCategoryShown" onclick="groupDynamic.selectShareWay(this,event)"
        style="width: 144px">
        <div class="selector_dropdown_icon"></div>
        <div class="selector_dropdown_value">
          <div class="selector_dropdown_option selected" val="1">分享至动态</div>
          <div class="selector_dropdown_option" val="2">分享至联系人</div>
          <div class="selector_dropdown_option" val="3">分享至群组</div>
        </div>
        <div class="selector_dropdown_collections">
          <div class="selector_dropdown_option hover" val="1">分享至动态</div>
          <div class="selector_dropdown_option" val="2">分享至联系人</div>
          <div class="selector_dropdown_option" val="3">分享至群组</div>
        </div>
      </div>
    </div>
    <div class="dialogs_title" id="share_plugin_select_group" style="display: none;">
      <input id="id_chioce_group_name" class="share_plugin_select_group" placeholder="输入群组名称">
      <div class="btn_normal btn_bg_origin fc_blue500" onclick="groupDynamic.checkGroupForShare()">选择群组</div>
      <div class="ac_container" id="id_group_ac_container"></div>
    </div>
    <div class="dialogs_title" id="share_plugin_select_friends"
      style="height: auto; min-height: 56px; padding: 8px 16px; display: none;">
      <div class="share_plugin_add_friends">
        <div class="share_plugin_add_friends_input" id="id_textarea_friend_name" placeholder="输入联系人姓名..."
          contenteditable></div>
      </div>
      <div class="btn_normal btn_bg_origin fc_blue500" onclick="groupDynamic.showMyfriends()">选择联系人</div>
      <div class="ac_container" id="id_friend_ac_container"></div>
    </div>
    <div class="dialogs_container">
      <div class="dynamic_content_container" style="padding: 16px;">
        <input type="hidden" value="" shareType="1" id="id_share_ui_param" />
        <textarea class="dynamic_share_text textarea_autoresize" id="id_share_textarea" placeholder="分享内容..."></textarea>
        <div class="dynamic_content_divider"></div>
        <div class="attachment">
          <div class="pub_whole">
            <div>
              <img src="" class="pub_avatar">
            </div>
            <div class="pub_information">
              <div class="title"></div>
              <div class="author"></div>
              <div class="source"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="dialogs_operations">
      <div class="btn_normal btn_bg_origin" onclick="groupDynamic.cancleShare()">取消</div>
      <div class="btn_normal btn_bg_blue" onclick="groupDynamic.shareMain(event,this)">分享</div>
    </div>
  </div>
</div>
<div class="background_cover_layer" id="id_group_add_group_name_list" onclick="hideDialogs(this,event)"
  style="display: none">
  <div class="dialogs_whole" style="width: 560px; height: 440px; max-height: calc(100% - 8px)">
    <div class="dialogs_title">选择一个群组</div>
    <div class="dialogs_container">
      <ul class="item_list_container">
        <li class="hover">群组一二三</li>
        <li>群组一三四</li>
        <li>我是一个群组</li>
        <li>群组一二三</li>
        <li>群组一三四</li>
        <li>我是一个群组</li>
      </ul>
    </div>
  </div>
</div>
<div class="background_cover_layer" id="id_group_add_friend_names_list" onclick="hideDialogs(this,event)"
  style="display: none;">
  <div class="dialogs_whole" style="width: 560px; height: auto; max-height: calc(100% - 8px)">
    <div class="dialogs_title">选择联系人</div>
    <div class="dialogs_container">
      <div class="select_friends_list"></div>
    </div>
    <div class="dialogs_operations">
      <div class="btn_normal btn_bg_origin" onclick="groupDynamic.cancleFriendsInfo()">取消</div>
      <div class="btn_normal btn_bg_blue" onclick="groupDynamic.getFriendsInfo()">确定</div>
    </div>
  </div>
</div>