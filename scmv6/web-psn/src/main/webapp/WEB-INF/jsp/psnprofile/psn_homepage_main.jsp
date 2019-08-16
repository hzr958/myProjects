<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="format-detection" content="telephone=no">

<title>科研之友</title>
<c:if test="${!empty psnBriefDesc }">
  <meta name="description" content="<c:out value='${psnBriefDesc }' escapeXml='true'/>" />
</c:if>
<c:if test="${empty psnBriefDesc }">
  <meta name="description" content="<s:text name='homepage.profile.description'/>" />
</c:if>
<c:if test="${!empty psnKeywordsStr}">
  <meta name="keywords" content="<c:out value='${psnKeywordsStr }' escapeXml='true'/>" />
</c:if>
<c:if test="${!empty psnProfileUrl }">
  <link rel="canonical" href="${psnProfileUrl }" />
</c:if>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/scm-changeboxpic/scm-changebox.css">
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/homepage/homepage_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js/loadding_div.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/no.result.prompt.language.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/cursorPositionPlugins.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.common.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.common_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dialogs.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<link type="text/css" rel="stylesheet" href="${resmod}/css/smate.autoword.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css/plugin/jquery.complete.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/css2016/newfirstpage.css">
<script type="text/javascript" src="${resmod}/js/plugin/jquery.appraisal.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.imgcutupload.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.imgcut.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${resmod }/js/homepage/psn.homepage.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.updown.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/influence/influence_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/influence/influence.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit.js"></script>
<link href="${resmod}/css/resetpwd/resetpassword-temp.css" rel="stylesheet" type="text/css" />
<!--重置密码框css  -->
<script src="${resmod}/js/plugin/des/des.js" type="text/javascript"></script>
<!--重置密码框-密码加密js  -->
<script type="text/javascript" src="${resmod}/js/resetpwd/resetpassword_${locale}.js"></script>
<!--重置密码框js  -->
<script type="text/javascript" src="${resmod}/js/resetpwd/resetpassword.js"></script>
<!--重置密码框js  -->
<script type="text/javascript" src="${resmod}/smate-pc/scm-changeboxpic/scm-changebox_${ locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/scm-changeboxpic/scm-changebox.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc-mobiletip_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/homepage/repeatpub.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc-mobiletip.js"></script>
<script type="text/javascript" src="${resmod}/js/resume/psn.cv_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/resume/psn.resume.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript">
var module = "${module}";
var opttype = "${opttype}";
var jumpto = "${jumpto}";
var showDialogPub = "${showDialogPub}";
var isEditPub = "${isEdit}";
var filterPubType="${filterPubType}" ;//过滤成果类型
var nowTime = "${nowTime}";
var nowYear;
var nowMonth;
$(document).ready(function(){
	chooseHomepageSecurity();
	loadPsnInfoConf();
	hideOperationBtn();
	if(module == "" || module == "homepage"){
		loadHomepageModule();
	}else{
	  //SCM-22505  站外查看个人主页成果、项目、影响力，会有脚本错误提示，无法查看列表。
	  if(typeof selectTab != 'undefined' && selectTab instanceof Function){
	    selectTab(module);
	  }else{
	    window.location.reload();
	  }
	}
	//showCooperation();
	changePsnAvatar();
	var scm_role =2;
	if("true"==$("#isMySelf").val()){
		scm_role=1;
	}
	window.LoadShortUrlEdit({
		"scm_myfunction":Psn_Info.doSaveShortUrl,
		"split_text":"/P/",
		"save_title": "<s:text name='homepage.profile.saveUrl' />",
		"scm_role":scm_role
		});
	if("false"==$("#isMySelf").val()){
		addVistRecord($("#des3PsnId").val(),$("#des3PsnId").val(),6);
	}
	if("false"==$("#isMySelf").val()){
		  document.getElementsByClassName("cancle-focus_btn")[0].onclick = function(event){
		        this.closest(".pro-header__actions-container").querySelector(".cancle-focus_box").style.display="block";
		        event.stopPropagation();
		        if(event.currentTarget){
		            if(event.stopPropagation){
		                event.stopPropagation();
		            }else{
		                event.cancelBubble=true;
		            }
		        }
		    }
		    document.addEventListener("click",function(){
		         document.getElementsByClassName("cancle-focus_box")[0].style.display="none"; 
		    })
	}
	//获取服务器的时间
	var timeArrays=nowTime.split("-");
	$("#nowYear").val(parseInt(timeArrays[0]));
	$("#nowMonth").val(parseInt(timeArrays[1]));
});
function selectTab(tabName,obj){
    if(obj != undefined){
        BaseUtils.doHitMore(obj,2000);
        //console.log("fan"+obj);
    }
	$(".tabClass").removeClass("item_selected");
	$(".container__horiz").replaceWith('<div class="container__horiz">'+$("#preloading").html()+'</div>');
	switch(tabName){
		case 'pub': 
			$("#pubTab").addClass("item_selected");
			Pub.main();
			break;
		case 'homepage':
			$("#homeTab").addClass("item_selected");
			toHomePage();
			break;
		case 'prj':
			$("#prjTab").addClass("item_selected");
			var des3PsnId = $("#des3PsnId").val();
			project.ajaxShow(des3PsnId);
			break;
	    case 'influence':
            $("#infTab").addClass("item_selected");
            Influence.main();
            break;	
		case 'consequence':
			$("#consequenceTab").addClass("item_selected");
			window.location.href="/scmwebsns/spread/pubConsequence";
			break;
		case 'resume':
			$("#resumeTab").addClass("item_selected");
			window.location.href="/scmwebsns/personalResume/load?menuId=1700";
			break;
		default: 
			$("#homeTab").addClass("item_selected");
			break;
	}
}

var getAreaJSON =function () {
	var $valueBox=document.querySelector('.sel__box[selector-id="1st_area"]');	
	return {"superRegionId":$valueBox.getAttribute("sel-value")};
};
function getSubAreaJSON (){
	var $valueBox=document.querySelector('.sel__box[selector-id="2nd_area"]');
	return {"superRegionId":$valueBox.getAttribute("sel-value")};	
}
function getRegionId (){
	var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="3nd_area"]');
	$("#psnRegionId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
}
function getArea(){
	    var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="1st_area"]');
	    $("#psnRegionId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
		var $selectBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
		if ($selectBox.style.visibility === "visible"&&$(".sel__box[selector-id='1st_area']").attr("sel-nextlevel")!="true") {
			$selectBox.style.visibility = "hidden";
			var $selectBox3 = document.querySelector('.sel__box[selector-id="3nd_area"]').style.visibility = "hidden";
		} 
		if ($selectBox.style.visibility === "hidden"&&$(".sel__box[selector-id='1st_area']").attr("sel-nextlevel")=="true") {
			$selectBox.style.visibility = "visible";
		} else {
			$selectValue = $selectBox.querySelector("span");
			$selectValue.textContent = "<s:text name='homepage.profile.note.second.region'/>"; 
			$selectValue.classList.add("sel__value_placeholder");
			$selectBox.setAttribute("sel-value","");
			document.querySelector('.sel__box[selector-id="3nd_area"]').style.visibility = "hidden";
		}
}
function getSubArea(){
	var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="2nd_area"]');
    $("#psnRegionId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
	var $selectBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
	if ($selectBox.style.visibility === "visible"&&$(".sel__box[selector-id='2nd_area']").attr("sel-nextlevel")!="true") {
		$selectBox.style.visibility = "hidden";
	} 
	if ($selectBox.style.visibility === "hidden"&&$(".sel__box[selector-id='2nd_area']").attr("sel-nextlevel")=="true") {
		$selectBox.style.visibility = "visible";
	} else {
		$selectValue = $selectBox.querySelector("span");
		$selectValue.textContent = "<s:text name='homepage.profile.note.third.region'/>"; 
		$selectValue.classList.add("sel__value_placeholder");
		$selectBox.setAttribute("sel-value","");
	}
}

function cutImg(){
    var cutpicinfor = function(imgdata){
    	 var data={"imgData":imgdata,"fileDealType":"personavatars"}; //后台默认 当前人为 图片唯一标识KEY
         $.ajax({
 			url : '/fileweb/imgupload',
 			type : 'post',
 			dataType:'json',
 			data : data,
 			success : function(data) {
 				if(data.result=='success'){
 					$("#upload_img").find("img").attr("src",data.fileUrl);
 					$("#skin_psn_auatars").find("img").attr("src",data.fileUrl);
 					imgUploadCutCallBack(data.fileUrl);
 				}
 			},
 			error: function(){
 			}
 		});
    }
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        var parnode = document.getElementById("cut_img");
        parnode.style.display="block";
    publicPicclipupload({
    	"parelement":parnode,
    	"scanwidth" : 120,
    	"scanheight" : 120,
    	"filecallback":cutpicinfor
    });
    }, 1);
}

// fileupload__box运行内层input type="file"的click事件
function fileuploadBoxOpenInputClick(ev){
	var $this = $(ev.currentTarget);
	$this.find('input.fileupload__input').click();
}

//初始化 分享 插件
function psnInitSharePlugin(obj){
    if(SmateShare.timeOut && SmateShare.timeOut == true)
        return;
    if (locale == "en_US") {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
            'language' : 'en_US'
        });
    } else {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
        });
    }
    var obj_lis = $("#share_to_scm_box").find("li");
    obj_lis.eq(0).hide();
    obj_lis.eq(1).click();
    obj_lis.eq(2).hide();
};
</script>
</head>
<body>
 <input type="hidden" value="" id="nowYear" />
  <input type="hidden" value="" id="nowMonth" />
  <%@ include file="./psn_cnf_const.jsp"%>
  <input type="hidden" name="briefSecurity" value="" id="briefSecurity" />
  <input type="hidden" name="keywordsSecurity" value="" id="keywordsSecurity" />
  <input type="hidden" name="workSecurity" value="" id="workSecurity" />
  <input type="hidden" name="eduSecurity" value="" id="eduSecurity" />
  <input type="hidden" name="pubSecurity" value="" id="pubSecurity" />
  <input type="hidden" name="prjSecurity" value="" id="prjSecurity" />
  <input type="hidden" id="psnName" value="${name}" />
  <input type="hidden" id="insAndDepInfo" value="${insAndDepInfo}" />
  <input type="hidden" name="cnfAnyMode" id="cnfAnyMode" value="${cnfAnyMode }" />
  <input type="hidden" name="des3PsnId" value='<iris:des3 code="${psnId }"></iris:des3>' id="des3PsnId" />
  <input type="hidden" name="isMySelf" id="isMySelf" value="${isMySelf }" />
  <input type="hidden" name="cnfId" id="cnfId" value="${cnfBuild.cnf.cnfId }" />
  <input type="hidden" name="isFriend" id="isFriend" value="${isFriend }" />
  <input type="hidden" name="searchKey" id="searchKey" value="${searchKey }" />
  <input type="hidden" name="sortBy" id="sortBy" value="${sortBy }" />
  <input type="hidden" name="frompage" id="frompage" value="${frompage }" />
  <input type="hidden" name="isLogin" id="isLogin" value="${isLogin }" />
  <div class="pro-header headDiv" id="head_div">
    <div class="pro-header__container"  style="position:relative;">
      <div class="pro-header__left-panel" id="psn_base_info">
        <div class="pro-header__base-info">
          <div class="pro-header__avatar" id="upload_img" style="position: relative;"
            <c:if test="${isMySelf }">onclick="cutImg();" </c:if>>
            <img id="psn_avatar" src="${psnInfo.avatars}?random=<%=Math.random()%> "
              onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
            <c:if test="${isMySelf }">
              <div class="pro-header__avator-tip">
                <s:text name='homepage.profile.header' />
              </div>
            </c:if>
          </div>
          <div class="target" style="display: none;" id="cut_img"></div>
          <div class="pro-header__main" id="psn_info_head">
            <div class="pro-header__main_psn-name" id="psn_name">
              <span title="${name}">${name}</span>
            </div>
            <div class="pro-header__main_psn-inst" id="psn_insAndDept">
              <span title="${insAndDepInfo }">${insAndDepInfo }</span>
            </div>
            <div class="pro-header__main_psn-inst" id="psn_posAndTitolo">
              <span title="${positionAndTitolo}">${positionAndTitolo}</span>
            </div>
            <div class="pro-header__main_psn-loc" id="psn_regionName">${psnRegionInfo }</div>
          </div>
        </div>
      </div>
      <div class="pro-header__right-panel">
        <div class="pro-header__actions">
          <button class="button_main button_icon button_light-grey operationBtn" onclick="editPsnInfo();">
            <i class="material-icons">edit</i>
          </button>
          <button class="button_main button_icon button_light-grey operationBtn"
            onclick="javascript:showSecuritySetting();">
            <i class="material-icons">settings</i>
          </button>
        </div>
        <c:if test="${isMySelf }">
          <div class="pro-header__right-panel-list" style="position: relative;">
            <div class="pro-header__right-panel-item" style="cursor: pointer" onclick="PsnResume.showResumeList(this);">
              <s:text name="homepage.profile.exportascv" />
            </div>
            <!-- <div class="pro-header__right-panel-resume" onclick="PsnResume.showResumeList(this);">基金委标准简历</div> -->
          </div>
        </c:if>
        <div class="pro-header__actions-container"
          style="display: flex; align-items:flex-end; position: relative; flex-direction: column; margin-bottom: 30px;">
          <c:if test="${!isMySelf }">
            <i class="material-icons cancle-focus_btn" style="transform: rotate(-90deg); margin-bottom: 16px;">more_vert</i>
          </c:if>
          <div class="pro-header__actions">
            <button class="button_main button_primary-reverse" style="${(isMySelf || isFriend) ? 'display:none;' : ''}; margin-left: 0px;"
              onclick="addIdentificFriend('${des3PsnId }','${psnId}',this);">
              <s:text name='homepage.head.addfriend' />
            </button>
          </div>
          <div class="pro-header__actions">
            <button class="button_main button_primary-reverse" style="${(isFriend) ? '' : 'display:none;'}"
              onclick="sendFriendMsg('${des3PsnId }');">
              <s:text name='homepage.send.msg'></s:text>
            </button>
          </div>
          <div class="cancle-focus_box">
            <c:if test="${payAttention}">
              <div class="cancle-focus_box-items attentionId"
                onclick="cancelAttention('${attentionId}','${des3PsnId }');">
                <s:text name='homepage.send.cancelAttentionPsn' />
              </div>
            </c:if>
            <c:if test="${!payAttention}">
              <div class="cancle-focus_box-items attentionId" onclick="addAttention('${attentionId}','${des3PsnId }');">
                <s:text name='homepage.send.attentionPsn' />
              </div>
            </c:if>
            <c:if test="${isFriend}">
              <div class="cancle-focus_box-items" onclick="cancelFriend('${des3PsnId}');">
                <s:text name='homepage.cancel.friend' />
              </div>
            </c:if>
          </div>
        </div>
      </div>
      <div class="new-scholarmate_ID-container">
        <c:if test="${locale == 'zh_CN'}">
          <span class="new-scholarmate_ID-container_title">科研号:</span>
        </c:if>
        <c:if test="${locale != 'zh_CN'}">
          <span class="new-scholarmate_ID-container_title">Scholar ID:</span>
        </c:if>
        <div class="new-scholarmate_ID-container_content">
            <span class="new-scholarmate_ID-container_content-detaile">${psnOpenId }</span>
            <i class="find-nothing_tip" title='<c:if test="${locale == 'zh_CN'}">科研之友人员唯一编号</c:if><c:if test="${locale != 'zh_CN'}">The only identifier of researchers in ScholarMate</c:if>'></i>
        </div>
    </div>
    </div>
    <%@ include file="./psn_security_setting_dialog.jsp"%>
    
    <div class="pro-header__nav">
      <div class="pro-header__nav-box">
        <nav class="nav_horiz">
          <ul class="nav__list">
            <li class="nav__item dev_module_homepage item_selected tabClass" id="homeTab"
              onclick="selectTab('homepage',this)"><s:text name='homepage.head.tab.profile' /></li>
            <c:if test="${isMySelf ||openPrjSum!=0}">
              <li class="nav__item tabClass" onclick="selectTab('prj',this);" id="prjTab"><s:text
                  name='homepage.head.tab.project' /></li>
            </c:if>
            <li class="nav__item dev_module_pub tabClass" onclick="selectTab('pub' ,this);" id="pubTab"><s:text
                name='homepage.head.tab.pub' /></li>
            <c:if test="${isMySelf}">
            <li class="nav__item dev_module_inf tabClass" onclick="selectTab('influence',this);" id="infTab"><s:text
                name='homepage.head.tab.influence' /></li>
            </c:if>
          </ul>
          <div class="nav__underline"></div>
        </nav>
        <div class="pro-header__url" id="clip_container" scm-paren='span_paren'>
          <c:if test="${!empty psnProfileUrl }">
            <a><span id="span_shorturl" scm-oldurl="${psnProfileUrl }" scm-id='span_id'
              <c:if test="${isMySelf }"> title='<s:text name="homepage.profile.editUrl" />' </c:if>>${psnProfileUrl }</span></a>
              
              <i class="material-icons pro-header__url-share" id="shareshorturl" scm-id='span_c_s' style="display: none;"  title='<s:text name="homepage.profile.copyUrl" />'>content_copy</i> 
              
              <a class="manage-one dev_pdwhpub_share" onclick="SmateShare.shareprofile($(this));psnInitSharePlugin();"
                    resid="${des3PsnId}" psnId="${psnId }">
                <div class="new-Standard_Function-bar_item">
                    <!-- <i class="new-Standard_function-icon new-Standard_Share-icon"></i>  -->
                    <span class="new-Standard_Function-bar_item-title dev_psn_share_${psnId}">
                       <i class="material-icons" style="font-size:22px; color: #666;margin-left: 4px;" title='<s:text name="homepage.profile.share" />'>share</i>
                    </span>
                </div>
              </a> 
          </c:if>
        </div>
      </div>
    </div>
  </div>
  <div id="preloading" style="display: none;">
    <div class="preloader active"
      style="display: flex; position: relative; left: 50%; align-self: center; width: 200px; height: 28px; width: 28px;">
      <div class="preloader-ind-cir__box">
        <div class="preloader-ind-cir__fill">
          <div class="preloader-ind-cir__arc-box left-half">
            <div class="preloader-ind-cir__arc"></div>
          </div>
          <div class="preloader-ind-cir__gap">
            <div class="preloader-ind-cir__arc"></div>
          </div>
          <div class="preloader-ind-cir__arc-box right-half">
            <div class="preloader-ind-cir__arc"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="container__horiz oldDiv">
    <div class="container__horiz_left width-8">

      <c:if test="${cnfAnyMode != 0 || (cnfAnyMode == 0 && isMySelf)}">
        <div class="container__card" style="${(empty module || module eq 'homepage') ? '' : 'display:none;'}">
          <div class="pro-stats__panel">
            <div class="pro-stats__list">
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty prjSum || prjSum < 0 ? '0' : prjSum }</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.statistics.project' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty pubSum || pubSum < 0 ? '0' :  pubSum}</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.statistics.pub' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty readSum || readSum < 0 ? '0' : readSum}</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.title.reads' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty downloadCount || downloadCount < 0 ? '0' : downloadCount }</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.title.downloadCount' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty citedSum || citedSum < 0 ? '0' : citedSum }</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.title.citations' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty hIndex || hindex < 0 ? '0' :  hIndex}</div>
                <div class="pro-stats__item_title">
                  <s:text name="homepage.profile.statistics.hindex" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </c:if>
      <!-- 简介 -->
      <div class="container__card" style="display: none;" id="briefDescModule"></div>
      <!--  -->
      <!-- 科技领域 -->
      <div id="scienceAreaModuleDiv"></div>
      <!-- 关键词 -->
      <div id="keywordsModuleDiv"></div>
      <!-- 工作经历 -->
      <div id="workHistoryModuleDiv"></div>
      <!-- 教育经历 -->
      <div id="eduHistoryModuleDiv"></div>
      <!-- SCM-13019暂时隐藏掉 -->
      <div id="representPubModuleDiv"></div>
      <div id="representPrjModuleDiv"></div>
      <!-- 联系信息 -->
      <div id="contactInfoModuleDiv"></div>
      <div class="container__card" style="display: none;" id="contactInfoModule"></div>
      <div class="Privacy-tip_container dev_private_msg" style="display:none;margin: 0 auto;height: 450px;">
          <div class="Privacy-tip_container-avator" style=" width: 120px;"></div>
          <div  class="Privacy-tip_container-tip">
                                        由于隐私设置,内容未完全显示
         </div>
      </div>
      <%@ include file="psn_contactinfo_edit.jsp"%>
    </div>
    <div class="container__horiz_right width-4">
      <c:if test="${!isMySelf && cnfAnyMode != 0}">
        <!-- 合作者 -->
        <div id="psn_cooperator" class="psn_cooperator">
          <%@ include file="./psn_cooperator_model.jsp"%>
        </div>
      </c:if>
      <c:if test="${isMySelf}">
        <!-- 查看过我的人 -->
        <div id="visitorsModuleDiv">
          <%@ include file="./visitors_show.jsp"%>
        </div>
        <!-- 基金推荐 -->
      </c:if>
      <%@ include file="./recommended_psn.jsp"%>
    </div>
  </div>
  <%@ include file="./psn_workhistory_add.jsp"%>
  <div class="dialogs__box oldDiv setnormalZindex" style="width: 480px;" dialog-id="editPsnWorkBox" cover-event="hide"
    id="editPsnWorkBox"></div>
  <%@ include file="./psn_eduhistory_add.jsp"%>
  <div class="dialogs__box oldDiv setnormalZindex" style="width: 480px;" dialog-id="editPsnEduBox" cover-event="hide" id="editPsnEduBox"></div>
  <div class="dialogs__box oldDiv setnormalZindex" style="width: 640px;" dialog-id="psnInfoBox" cover-event="hide" id="psnInfoBox"></div>
  <div class="sel-dropdown__box oldDiv  sel-dropdown__box-normal" selector-data="work_history_select" data-src="request"
    request-url="/psnweb/workhistory/ajaxlist" request-data="buildWorkHistorySelector();"></div>
  <div class="dialogs__box oldDiv setnormalZindex" dialog-id="briefDescBox" cover-event="hide" style="width: 480px; height: 400px;"
    id="briefDescBox"></div>
  <div class="sel-dropdown__box" selector-data="1st_area" data-type="json" item-event="getArea()" data-src="request"
    request-url="/psnweb/common/ajaxRegion"></div>
  <div class="sel-dropdown__box" selector-data="2nd_area" data-type="json" item-event="getSubArea()" data-src="request"
    request-url="/psnweb/common/ajaxRegion" request-data="getAreaJSON()"></div>
  <div class="sel-dropdown__box" selector-data="3nd_area" data-type="json" item-event="getRegionId()" data-src="request"
    request-url="/psnweb/common/ajaxRegion" request-data="getSubAreaJSON()"></div>
  <%@ include file="./visitors_show_more.jsp"%>
  <%@ include file="./psn_cooperator_dialog.jsp"%>
  <%@ include file="/WEB-INF/jsp/pub/main/pub_cited_up.js.jsp"%>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
