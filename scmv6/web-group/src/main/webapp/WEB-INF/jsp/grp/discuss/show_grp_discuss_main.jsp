<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod }/js_v8/agency/scm_pc_agency.js"></script>
<script type="text/javascript">
var shareTips = "<s:text name='groups.base.discuss.share' />";
var showGrpDiscussMainControl="${GrpControl.isMemberShow}";
var showGrpDiscussMainIsGrpMember = "${flag}";
var showGrpDiscussMainRole = "${role}";
$(document).ready(function(){
    GrpDiscuss.showGrpDesc();
    if (showGrpDiscussMainControl == "1" || showGrpDiscussMainIsGrpMember == "2") {
          GrpDiscuss.showGrpFiveMember();
    }
	$("#grpDiscussListId").doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	});
	GrpDiscuss.showGrpDiscussList(showGrpDiscussMainRole);
	GrpDiscuss.showGrpFivePub();
	addFormElementsEvents(document.getElementById("publish_box"));
    document.onkeydown = function(event){
       if(event.keyCode == 27){
           event.stopPropagation();
           event.preventDefault();
           GrpSelectPub.hideSelectPubBox();
       }
    }
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
    	targetlist[i].querySelector("input").onfocus = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
    	}
    	targetlist[i].querySelector("input").onblur = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";		
    	}
    }
});
//==============================
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId,resType){
    if(resType == "pdwhpub"){
    	addPdwhPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId,resType)
    }else{
    	addPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId,resType)
    }
    shareCallback(dynId,shareContent,resId);
}
function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId,resType){
    
  if ("GRP_SHAREPRJ" == resType) {
    var data = {
        "dynContent" :shareContent,
        "des3ResId" : resId,
        "tempType" : resType,
        "resType" : "prj",
        "des3ReceiverGrpId" : receiverGrpId
    };
    $.ajax({
        url : "/dynweb/dynamic/ajaxgroupdyn/dopublish",
        type : 'post',
        dataType : 'json',
        data : data,
        success : function(data) {
        },
        error : function() {
        }
    });
  }
	shareCallback(dynId,shareContent,resId);
}
//==============================
//分享回调
function shareCallback (dynId,shareContent,resId){
	if(dynId!=null&&dynId!=""){
		var dataJson = {
				des3DynId:dynId,
				shareContent:shareContent
		};
		$.ajax({
			url : '/dynweb/dynamic/groupdyn/ajaxshare',
			type : 'post',
			dataType:'json',
			data:dataJson,
			success : function(data) {
				var $obj_share = $("*[id='share_count_"+dynId+"'");
				$obj_share.text(shareTips+"("+(Number($obj_share.text().replace(/[^0-9]/ig,""))+1)+")");
			},
			error: function(){
			}
		});
	}else{
	}
	
	
}; 

function shareAgencyGrpCallback(data, des3ResId, des3GroupId, comments,dynId){
  $.ajax({
      url: "/prjweb/share/ajaxupdate",
      type: "post",
      data:{
          "Des3FundAgencyId": des3ResId,
          "shareToPlatform": 3,
          "des3GroupId": des3GroupId,
          "comments": comments
      },
      dataType: "json",
      success: function(data){
          if(data.result = "success"){
          }
      },
      error: function(){}
  });
  shareCallback(dynId,comments,des3ResId);
}

function shareAgencyFrdCallback(data, agencyId,dynId){
  var $obj_share = $("*[id='share_count_"+dynId+"'");
  $obj_share.text(shareTips+"("+(Number($obj_share.text().replace(/[^0-9]/ig,""))+1)+")");
}
function addPdwhPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId,resType){
	$.ajax({
        url : '/pub/opt/ajaxpdwhshare',
        type : 'post',
        dataType : 'json',
        data : {
                  'des3PdwhPubId':resId,
                  'comment':shareContent,
                  'sharePsnGroupIds':receiverGrpId,
                  'platform':"2"
               },
        success : function(data) {
        }
    }); 
}
function addPubShare(dynId,shareContent,resId,pubId,isB2T ,receiverGrpId,resType){
	$.ajax({
        url : '/pub/opt/ajaxshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PubId':resId,
             'comment':shareContent,
             'sharePsnGroupIds':receiverGrpId,
             'platform':"2"
               },
        success : function(data) {
        }
    });
}
function showDynContentLength(){
	$("#id_grpdyn_content_length").html($("#publish_text").val().length);
}
//初始化 分享 插件
function fileInitSharePlugin(obj,event){
    $("#share_to_scm_box").find(".nav__underline").hide();
    var dyntype = $("#share_to_scm_box").attr("dyntype") ;
    var styleVersion ;
    if("GRP_ADDFILE"==dyntype||"GRP_SHAREFILE"==dyntype||"GRP_ADDCOURSE"==dyntype||"GRP_ADDWORK"==dyntype){
        styleVersion = "10" ;
    }
    $(obj).dynSharePullMode({
        'groupDynAddShareCount':"",
        'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)' ,
        'styleVersion' : styleVersion
    });
    $("#share_site_div_id").find(".inside").click()
    var $li = $("#share_to_scm_box").find("li");
    $li.eq(0).hide();
    $li.eq(1).click();
    $("#share_to_scm_box").find(".nav__underline").show();
}
</script>
<div class="container__horiz_left width-7" flyin-direction="bottom">
  <c:if test="${role==1 || role==2 || role==3 }">
    <div class="container__card" onclick="GrpDiscuss.showPublish();" id="grp_share_card">
      <div class="dynamic__box create-new-post">
        <div class="dynamic-cmt">
          <div class="dynamic-cmt__post">
            <div style="display: flex; height: 145px;">
              <div class="dynamic-content__post"
                style="border-bottom: 1px solid #ccc; position: relative; height: 101%;">
                <div class="dynamic-post__avatar" id="publish_current_psn_id">
                  <a href="/psnweb/homepage/show?des3PsnId=${des3PsnId}" target="_Blank"> <img
                    src="${userData.avatars }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                  </a>
                </div>
                <div class="dynamic-post__main">
                  <div class="dynamic-cmt__new-post_deactive" id="publish_notic">
                    <s:text name="groups.base.publishText" />
                  </div>
                  <div class="form__sxn_row" style="display: none;" id="publish_box" style="margin-right: -7px;">
                    <div class="input__box dynamic-post__linelimit">
                      <div class="input__area ">
                        <textarea oninput="showDynContentLength()" class="global_no-border" maxlength="500"
                          onfocus=" this.placeholder='' " placeholder="<s:text name='groups.base.publishText' />"
                          id="publish_text"></textarea>
                        <div class="textarea-autoresize-div"></div>
                      </div>
                    </div>
                  </div>
                  <div class="dynamic-main__box select_res_box" style="display: none">
                    <div class="dynamic-divider"></div>
                    <div class="dynamic-main__att">
                      <div class="pub-idx_small">
                        <div class="pub-idx__base-info">
                          <div class="pub-idx__main_box">
                            <div class="pub-idx__main aleady_select_pub" pubId="">
                              <div class="pub-idx__main_title">
                                <a></a>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="att-delete-icon clear_selected_pub" onclick="GrpSelectPub.clearSelectedPub(this);">
                        <i class="material-icons">close</i>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="dynamic-content__post-count">
                  <div class="dynamic-content__post-count_Curve" id="id_grpdyn_content_length">0</div>
                  /
                  <div class="dynamic-content__post-count_total">500</div>
                </div>
              </div>
              <div class="dynamic-post__linelimit-icon" onclick="GrpDiscuss.showSelectPubBox(this);"
                title="<s:text name='groups.base.addPub' />" style="height: 100%;  line-height: 100%;"></div>
            </div>
            <div class="dynamic-cmt__post_actions" style="display: none; justify-content: flex-end;" id="publish_option">
              <button class="button_main button_dense button_primary" disabled id="publish_button"
                style="color: rgb(40, 130, 216);" onclick="GrpDiscuss.publishshare(this);">
                <s:text name='groups.base.postPub' />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:if>
  <div class="main-list">
    <c:if test="${openType == 'O'}">
      <div class="main-list__list item_no-border" list-main="grp_discuss_list" id="grpDiscussListId"></div>
    </c:if>
    <c:if test="${openType == 'H' && flag == '2'}">
      <div class="main-list__list item_no-border" list-main="grp_discuss_list" id="grpDiscussListId"></div>
    </c:if>
    <c:if test="${openType == 'P' && flag == '2'}">
      <div class="main-list__list item_no-border" list-main="grp_discuss_list" id="grpDiscussListId"></div>
    </c:if>
    <c:if test="${openType == 'H' && flag == '1'}">
      <div class="main-list__list item_no-border" list-main="grp_discuss_list" id="grpDiscussListId"></div>
    </c:if>
    <c:if test="${openType == 'H' && flag == '0'}">
      <div class="main-list__list item_no-border">
        <div class="response_no-result">
          <s:text name='groups.base.loginOrJoinPrompt' />
        </div>
      </div>
    </c:if>
  </div>
</div>
<div class="container__horiz_right width-5">
  <div class="container__card" id="grp_discuss_desc"></div>
  <div class="container__card" id="grp_discuss_other"></div>
  <div class="container__card" id="grp_discuss_member"></div>
  <div class="container__card" id="grp_discuss_pub"></div>
</div>
<div class="dialogs__box setnormalZindex" style="width: 600px;" dialog-id="grp_select_pub" id="id_grp_select_pub"
  flyin-direction="bottom">
  <div class="dialogs__childbox_fixed" id="id_dialogs__header">
    <div class="dialogs__header">
      <div class="dialogs__header_title" style="flex-grow: 0; width: 120px;">
        <div class="sel__box" selector-id="pub_owner_type" sel-value="1">
          <div class="sel__value global_no-border" id="select_search_type" searchType="1"
            style="display: flex; align-items: center;">
            <span class="sel__value_selected"><s:text name='groups.base.grpPub' /></span> <i
              class="material-icons sel__dropdown-icon">arrow_drop_down</i>
          </div>
        </div>
      </div>
      <div class="dialogs__header_searchbox" style="margin-left: 64px;">
        <div class="searchbox__container main-list__searchbox" list-search="selectpub" id="select_pub_search_box">
          <div class="searchbox__main">
            <input placeholder=" <s:text name='groups.base.search' />">
            <div class="searchbox__icon material-icons"></div>
          </div>
        </div>
      </div>
      <i class="list-results_close" onclick="GrpSelectPub.hideSelectPubBox();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id="grp_select_pub_list" list-main="selectpub"></div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="GrpSelectPub.sureSelectOnePub()">
        <s:text name="groups.base.confirm"/></button>
    </div>
  </div>
</div>
<div class="sel-dropdown__box filter-list filter-list_custom-style" selector-data="pub_owner_type"
  list-filter="selectpub" id="id_pubtype_list">
  <div class="sel-dropdown__list js_filtersection filter-list__section" filter-section="des3GrpId"
    filter-method="compulsory">
    <ul>
      <div class="filter-value__item sel-dropdown__item filter-value__option"
        filter-value="<iris:des3 code='${grpId}'/>" sel-itemvalue="1">
        <input type="checkbox" checked> <span class=""><s:text name='groups.base.grpPub' /></span>
      </div>
      <div class="filter-value__item sel-dropdown__item option_selected filter-value__option" filter-value=""
        sel-itemvalue="2">
        <input type="checkbox"> <span class=""><s:text name='groups.base.myPub' /></span>
      </div>
    </ul>
  </div>
</div>
