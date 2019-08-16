<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<meta name="format-detection" content="telephone=no">
<title>科研之友</title>
<c:if test="${!empty grpDescription }">
  <meta name="description" content="${grpDescription }" />
</c:if>
<c:if test="${empty grpDescription }">
  <meta name="description" content="<s:text name='groups.base.detail.description'/>" />
</c:if>
<c:if test="${!empty grpKeywords }">
  <meta name="keywords" content="${grpKeywords }" />
</c:if>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod }/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/scm-changeboxpic/scm-changebox.css">
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/css2016/newfirstpage.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="format-detection" content="telephone=no">
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dyreaction.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpFile/grp.file.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpFile/grp.file_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpDiscuss/grp.selectpub.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpDiscuss/grp.discuss.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpDiscuss/grp.discuss_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpMember/grp.member.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grppub/grp.member.pub.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grppub/grppub.common_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grppub/grp.pub.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/new-groupimport-Achive/newgroup-import.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.discuss.list.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.discussCommon_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.discuss.comment.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.dynamic.list.opendetail.pc.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod }/js/sharebutton.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/dialog.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_mainlist.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/browsercompatible.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/scm-changeboxpic/scm-changebox_${ locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/scm-changeboxpic/scm-changebox.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpMember/grp.member_${locale }.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript">
var model="${model}";
var ispending = "${ispending}";
var grpInfoMainControl="${GrpControl.isMemberShow}";
$(document).ready(function(){//群组成果主页  projectPub==项目成果  ， projectRef== 项目文献
	role = '${grpShowInfo.role}';
	var grpPendingConfirmPubs = '${grpShowInfo.grpPendingConfirmPubs}';
	addFormElementsEvents();
	//领域和关键词的加载curware_file_main
	GrpBase.showDisciplinesAndKeywords();
	if((role==1|| role == 2)&&grpPendingConfirmPubs>0){
		getGroupRcmdPub();
	} else{
		loadFunctionMode();
	}
	
	window.LoadShortUrlEdit({
		"scm_myfunction":GrpBase.doSaveShortUrl,
		"save_title": "<s:text name='groups.tips.saveUrl' />",
		"scm_role":<s:if test="grpShowInfo.role==1 || grpShowInfo.role==2">1</s:if><s:else>2</s:else>
	});

});

function loadFunctionMode(){
	if(model=="file" && $("#file_main").length > 0){
		$("#file_main").click();
	}else if(model=="curware" && $("#curware_file_main").length > 0){
		$("#curware_file_main").click();
	}else if(model=="work" && $("#work_file_main").length > 0){
		$("#work_file_main").click();
	}else if(model=="member" && $("#member_main").length > 0){
		$("#member_main").click();
	}else if(model=="pub" && $("#pub_main").length > 0){
		$("#pub_main").click();
	}else if( ( model=="projectPub" ||  model=="pub" ) && $("#project_pub_main").length > 0){
		$("#project_pub_main").click();

	}else if(model=="projectRef" && $("#project_ref_main").length > 0){
		$("#project_ref_main").click();

	}else if($("#discuss_main").length > 0){
		$("#discuss_main").click();		
	} 
}

//成果认领与全文认领查看全部上的全文认领模块
function getGroupRcmdPub() {
	$('.dev_group_pub_confirmList').show();
	showDialog("grp_pub_comfirm_box");
	pubftconfirm = window.Mainlist({
		name : "grouppubconfirm",
		listurl : "/groupweb/grppub/ajaxpubrcmdnew",
		listdata : {
			"isAll" : "1",
			"grpId" : $("#grp_params").attr("smate_grp_id")
		},
		method : "scroll",
		listcallback : function(xhr) {
			//回调函数中加载剩余模块，否则其他的列表请求会在插件中被取消
			loadFunctionMode();
		}
	});
}

function cutImg(){
	var parnode = document.getElementById("cut_img");
    parnode.style.display="block";
    var cutpicinfor = function(imgdata){
    	 var data={"imgData":imgdata,"fileDealType":"groupavatars","fileUniqueKey":$("#grp_params").attr("smate_grp_id")};
         $.ajax({
 			url : '/fileweb/imgupload',
 			type : 'post',
 			dataType:'json',
 			data : data,
 			success : function(data) {
 				if(data.result=='success'){
 					$("#upload_img").find("img").attr("src",data.fileUrl);
 					GrpBase.saveGrpAvartars(data.fileUrl);
 				}
 			},
 			error: function(){
 			}
 		});
    }
    publicPicclipupload({
    	"parelement":parnode,
    	"scanwidth" : 120,
    	"scanheight" : 120,
    	"filecallback":cutpicinfor
    	});
}


//微信分享
function getQrImg(url){
	$(".dui-dialog-shd").css("height" ,"240");
	$(".dui-dialog-content").find(" .bd > img").css("height" ,"175px").css("width","200px");
	if(navigator.userAgent.indexOf("MSIE")>0){
		$("#share-qr-img").qrcode({render: "table",width: 175,height:175,text:url });
	}else{
		$("#share-qr-img").qrcode({render: "canvas",width: 175,height:175,text:url });
	}
}

//初始化 分享 插件
function initSharePlugin(obj){
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
	var obj_lis = $("#share_to_scm_box").find("li");
	// 群组文件不能分享去除分享至动态，但是其他可以，两种初始化需要进行重新作用
	obj_lis.eq(1).click();
	if("GRP_ADDFILE"==dyntype||"GRP_SHAREFILE"==dyntype||"GRP_ADDCOURSE"==dyntype||"GRP_ADDWORK"==dyntype){
		$("#share_site_div_id").find(".inside").click();
		obj_lis.eq(0).hide();
		obj_lis.eq(1).click();
	}
    document.getElementsByClassName("nav__item-selected")[0].classList.remove("nav__item-selected");
    document.getElementsByClassName("nav__item-container")[0].querySelector(".item_selected").classList
    .add("nav__item-selected");
}

 /**
  * 群组讨论主页
  */
 function showGrpDiscussMain(obj){
	 GrpBase.changeUrl("discuss");
 	showMain('/groupweb/grpdiscuss/ajaxmain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});	
 };
 
/**
 * 群组成果主页  projectPub or pub==项目成果  ，  projectRef== 项目文献
 */
function showGrpProjectPubsMain(obj){
	model="pub";
	GrpBase.changeUrl("pub");
	var url='/groupweb/grppub/ajaxshowgrppubsmain';
	if(ispending==2){//群组列表未处理事项跳转
		url+="?ispending=2";
		ispending=0;
	}
	showMain(url,obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id") ,'module':'projectPub'});
}
	GrpMemberPub.showgrpMemberPubListStatus=false;//用来标记是不是第一次点击群组成员成果
/**
 * 群组成果主页  projectPub==项目成果  ， projectRef== 项目文献
 */
function showGrpProjectRefsMain(obj){
	model="projectRef";
	GrpBase.changeUrl("projectRef");
	var url='/groupweb/grppub/ajaxshowgrppubsmain';
	if(ispending==2){//群组列表未处理事项跳转
		url+="?ispending=2";
		ispending=0;
	}
	showMain(url,obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id") ,'module':'projectRef'});	
	GrpMemberPub.showgrpMemberPubListStatus=false;//用来标记是不是第一次点击群组成员成果
};
/**
 * 群组文献主页
 */
function showGrpPubsMain(obj){
	GrpBase.changeUrl("pub");
	var url='/groupweb/grppub/ajaxshowgrppubsmain';
	if(ispending==2){//群组列表未处理事项跳转
		url+="?ispending=2";
		ispending=0;
	}
	showMain(url,obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id"),'module':'projectRef'});	
	GrpMemberPub.showgrpMemberPubListStatus=false;//用来标记是不是第一次点击群组成员成果
};

/**
 * 群组成员主页
 */
function showGrpMemberMain(){
	GrpBase.changeUrl("member");
	var url = '/groupweb/grpmember/ajaxshowgrpmembermain';
	if(ispending==1){//群组列表未处理事项跳转
		url+="?ispending=1";
	}
	showMain(url,"",{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});
};

function showMain(url,obj,paramt){
	$.ajax({
		url : url,
		type : 'post',
		dataType:'html',
		data:paramt,
		success : function(data) {
    		GrpDiscuss.showGrpDesc();
    		GrpDiscuss.showOtherInfo();
    	    if (grpInfoMainControl == 1) {
    	          GrpDiscuss.showGrpFiveMember();
    	    }
			GrpBase.ajaxTimeOut(data,function(){
			    if($("#grp_main_box").length==0){
			      alert(111);
			    }
				$("#grp_main_box").html(data);
			});
		},
		error: function(){
		}
	});	
	
};


//群组文件
function showGrpFileMain(obj){
	GrpBase.changeUrl("file");
	$("#grp_params").attr("module", "file");
	showMain('/groupweb/grpfile/ajaxgrpfilemain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});		
};
//群组课件
function showGrpCurwareFileMain(obj){
	GrpBase.changeUrl("curware");
	$("#grp_params").attr("module" ,"curware");
	showMain('/groupweb/grpfile/ajaxgrpfilemain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id"),'module':'curware'});		
};
//群组作业
function showGrpWorkFileMain(obj){
	GrpBase.changeUrl("work");
	$("#grp_params").attr("module", "work");
	showMain('/groupweb/grpfile/ajaxgrpfilemain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id") ,'module':'work'});		
};
</script>
</head>
<body>
  <div class="pro-header">
    <div class="pro-header__container">
      <div class="pro-header__left-panel">
        <div class="pro-header__base-info">
          <!-- 显示当前浏览者的psnId -->
          <input id="userId" name="userId" type="hidden" value="<iris:des3 code='${psnId }'/>">
          <div class="pro-header__avatar" id="upload_img"
            <s:if test="grpShowInfo.role==1 || grpShowInfo.role==2"> onclick="cutImg();" </s:if>>
            <img src="${grpShowInfo.grpBaseInfo.grpAuatars}"
              onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
            <s:if test="grpShowInfo.role==1 || grpShowInfo.role==2">
              <div class="pro-header__avator-tip">
                <s:text name="groups.pro.header" />
              </div>
            </s:if>
          </div>
          <div class="target" style="display: none;" id="cut_img"></div>
          <div class="pro-header__main">
            <div class="pro-header__main_grp-name">${grpShowInfo.grpBaseInfo.grpName}</div>
            <div class="pro-header__main_grp-kw">
              <div class="kw__box" smate_disciplines='${firstDisciplinetName};${secondDisciplinetName}'
                smate_keywords='<c:out value="${grpShowInfo.grpKwDisc.keywords}"></c:out> '
                id="smate_disciplines_keywords"></div>
            </div>
            <s:if test="grpShowInfo.prjInfo != null">
              <div class="pro-header__main_grp-agency">
                <c:if test="${!empty grpShowInfo.prjInfo.agencyName || !empty grpShowInfo.prjInfo.enAgencyName  }">
                     <iris:lable zhText="${grpShowInfo.prjInfo.agencyName }" enText="${grpShowInfo.prjInfo.enAgencyName }"></iris:lable>
                </c:if>
                <c:if test="${!empty grpShowInfo.prjInfo.schemeName || !empty grpShowInfo.prjInfo.enSchemeName  }">
                                             ，<iris:lable zhText="${grpShowInfo.prjInfo.schemeName }" enText="${grpShowInfo.prjInfo.enSchemeName }"></iris:lable>
                </c:if>
              </div>
            </s:if>
            <div class="pro-header__main_grp-stats">
              <!--  课程群组 成员，文献，课件，作业  -->
              <s:if test="grpShowInfo.grpBaseInfo.grpCategory ==10">
                <span id="grpinfo_sum_member">${grpShowInfo.grpStatistic.sumMember}&nbsp;<s:text
                    name='groups.list.person' /></span>
                <span id="grpinfo_sum_pub">${grpShowInfo.grpStatistic.sumPubs}&nbsp;<s:text
                    name='groups.list.pub' /></span>
                <span id="grpinfo_sum_coursefile">${grpShowInfo.grpCourseFileSum}&nbsp;<s:text
                    name='groups.file.curware' /></span>
                <span id="grpinfo_sum_workfile">${grpShowInfo.grpWorkFileSum}&nbsp;<s:text
                    name='groups.file.work' /></span>
              </s:if>
              <!--  项目群组 成员，成果，文献，文件   -->
              <s:elseif test="grpShowInfo.grpBaseInfo.grpCategory ==11">
                <span id="grpinfo_sum_member">${grpShowInfo.grpStatistic.sumMember}&nbsp;<s:text
                    name='groups.list.person' /></span>
                <span id="grpinfo_sum_pub">${grpShowInfo.grpProjectPubSum}&nbsp;<s:text name='groups.project.pub' /></span>
                <span id="grpinfo_sum_ref">${grpShowInfo.grpProjectRefSum}&nbsp;<s:text name='groups.project.ref' /></span>
                <span id="grpinfo_sum_file">${grpShowInfo.grpStatistic.sumFile}&nbsp;<s:text
                    name='groups.list.file' /></span>
              </s:elseif>
              <s:else>
                <span id="grpinfo_sum_member">${grpShowInfo.grpStatistic.sumMember}&nbsp;<s:text
                    name='groups.list.person' /></span>
                <span id="grpinfo_sum_pub">${grpShowInfo.grpStatistic.sumPubs}&nbsp;<s:text
                    name='groups.list.pub' /></span>
                <span id="grpinfo_sum_file">${grpShowInfo.grpStatistic.sumFile}&nbsp;<s:text
                    name='groups.list.file' /></span>
              </s:else>
            </div>
            <s:if test="grpShowInfo.isNsfcPrj == 1">
              <div class="pro-header__main_grp-nsfc">
                <a href="https://isisn.nsfc.gov.cn/egrantweb/" target="_blank"><s:text name='groups.list.nsfc' /></a>
              </div>
            </s:if>
          </div>
        </div>
      </div>
      <div class="pro-header__right-panel">
        <div class="pro-header__actions">
          <s:if test="grpShowInfo.role==1 || grpShowInfo.role==2">
            <button class="button_main button_primary-reverse" onclick='GrpBase.grpEdit()' id="btn_grp_manage">
              <s:text name='groups.base.edit.manage' />
            </button>
          </s:if>
          <s:elseif test="grpShowInfo.role==4">
            <button class="button_main button_primary-reverse" disabled>
              <s:text name='groups.discover.pending' />
            </button>
          </s:elseif>
          <s:elseif test="grpShowInfo.role==9">
            <button class="button_main button_primary-reverse" onclick="GrpMember.ajaxMemberApplyJoinGrp(null,null,1)"
              id="btn_apply">
              <s:text name='groups.discover.joinGroup' />
            </button>
          </s:elseif>
        </div>
      </div>
    </div>
    <div class="pro-header__nav" id="grp_params" smate_grpcategory='${grpShowInfo.grpBaseInfo.grpCategory}'
      smate_grp_id='${grpShowInfo.grpBaseInfo.grpId}' smate_des3_grp_id="${des3GrpId}" pageValue="${pageValue }"
      smate_des3_first_psn_id="${des3FirstPsnId }">
      <div class="pro-header__nav-box">
        <nav class="nav_horiz">
          <ul class="nav__list">
            <s:if test="grpShowInfo.role==9">
              <c:if test="${grpShowInfo.grpControl.isIndexDiscussOpen==1 }">
                <li class="nav__item" id="discuss_main" onclick='showGrpDiscussMain(this)'><s:text
                    name='groups.list.home' /></li>
              </c:if>
              <c:if test="${grpShowInfo.grpControl.isIndexMemberOpen==1}">
                <li class="nav__item" id="member_main" onclick='showGrpMemberMain(this)'><s:text
                    name='groups.list.person' /></li>
              </c:if>
              <s:if test="grpShowInfo.grpBaseInfo.grpCategory == 11">
                <c:if test="${grpShowInfo.grpControl.isPrjPubShow==1}">
                  <li class="nav__item" id="project_pub_main" onclick='showGrpProjectPubsMain(this)'><s:text
                      name='groups.project.pub' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isPrjRefShow==1}">
                  <li class="nav__item" id="project_ref_main" onclick='showGrpProjectRefsMain(this)'><s:text
                      name='groups.project.ref' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isIndexFileOpen==1}">
                  <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                      name='groups.list.file' /></li>
                </c:if>
              </s:if>
              <s:elseif test="grpShowInfo.grpBaseInfo.grpCategory == 10">
                <c:if test="${grpShowInfo.grpControl.isCurwareFileShow==1}">
                  <li class="nav__item" id="curware_file_main" onclick='showGrpCurwareFileMain(this)'><s:text
                      name='groups.file.curware' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isWorkFileShow==1}">
                  <li class="nav__item" id="work_file_main" onclick='showGrpWorkFileMain(this)'><s:text
                      name='groups.file.work' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isIndexPubOpen==1}">
                  <li class="nav__item" id="pub_main" onclick='showGrpPubsMain(this)'><s:text
                      name='groups.list.pub' /></li>
                </c:if>
              </s:elseif>
              <s:else>
                <c:if test="${grpShowInfo.grpControl.isIndexPubOpen==1}">
                  <li class="nav__item" id="pub_main" onclick='showGrpPubsMain(this)'><s:text
                      name='groups.list.pub' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isIndexFileOpen==1}">
                  <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                      name='groups.list.file' /></li>
                </c:if>
              </s:else>
            </s:if>
            <s:else>
              <li class="nav__item" id="discuss_main" onclick='showGrpDiscussMain(this)'><s:text
                  name='groups.list.home' /></li>
              <li class="nav__item" id="member_main" onclick='showGrpMemberMain(this)'><s:text
                  name='groups.list.person' /></li>
              <s:if test="grpShowInfo.grpBaseInfo.grpCategory == 10">
                <li class="nav__item" id="curware_file_main" onclick='showGrpCurwareFileMain(this)'><s:text
                    name='groups.file.curware' /></li>
                <li class="nav__item" id="work_file_main" onclick='showGrpWorkFileMain(this)'><s:text
                    name='groups.file.work' /></li>
                <li class="nav__item" id="pub_main" onclick='showGrpPubsMain(this)'><s:text name='groups.list.pub' /></li>
              </s:if>
              <s:if test="grpShowInfo.grpBaseInfo.grpCategory == 11">
                <li class="nav__item" id="project_pub_main" onclick='showGrpProjectPubsMain(this)'><s:text
                    name='groups.project.pub' /></li>
                <li class="nav__item" id="project_ref_main" onclick='showGrpProjectRefsMain(this)'><s:text
                    name='groups.project.ref' /></li>
                <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                    name='groups.list.file' /></li>
              </s:if>
              <s:if test="grpShowInfo.grpBaseInfo.grpCategory == 12">
                <li class="nav__item" id="pub_main" onclick='showGrpPubsMain(this)'><s:text name='groups.list.pub' /></li>
                <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                    name='groups.list.file' /></li>
              </s:if>
            </s:else>
          </ul>
          <div class="nav__underline"></div>
        </nav>
        <s:if test="grpShowInfo.grpIndexUrl!=null && grpShowInfo.grpIndexUrl!=''">
          <div class="pro-header__url" id="id_shorturl" scm-paren='span_paren'>
            <s:if test="grpShowInfo.role==1 || grpShowInfo.role==2">
              <a><span scm-id='span_id' id="span_shorturl" scm-oldurl="${grpShowInfo.grpIndexUrl}"
                title="<s:text name='groups.tips.editUrl' />">${grpShowInfo.grpIndexUrl}</span></a>
            </s:if>
            <s:else>
              <a><span scm-id='span_id' id="span_shorturl" scm-oldurl="${grpShowInfo.grpIndexUrl}">${grpShowInfo.grpIndexUrl}</span></a>
            </s:else>
            <i class="material-icons pro-header__url-share" scm-id='span_c_s'
              title="<s:text name='groups.tips.copyUrl' />">content_copy</i>
          </div>
        </s:if>
      </div>
    </div>
  </div>
  <div class="container__horiz" id='grp_main_box'>
    <div class="container__horiz_left width-7"></div>
    <div class="container__horiz_right width-5"></div>
  </div>
  <div class="dialogs__box dialogs__box-limit_size" id="dev_jconfirm" style="width: auto; height: 135px;display: flex; flex-direction: column; justify-content: space-between" dialog-id="dev_jconfirm_ui" flyin-direction="top">
    <div class="dialogs__modal_text" style="width: 100%; padding: 20px 0px;">
      <s:text name='groups.discover.quit' />
    </div>
    <div class="dialogs__modal_actions">
      <button class="button_main button_dense button_primary" onclick="hideDialog('dev_jconfirm_ui')">
        <s:text name='groups.manage.btn.confirm' />
      </button>
      <button class="button_main button_dense" onclick="hideDialog('dev_jconfirm_ui')">
        <s:text name='groups.manage.btn.cancel' />
      </button>
    </div>
  </div>
  <div class="dialogs__box" style="width: 720px;" dialog-id="grp_pub_comfirm_box" flyin-direction="top">
    <div class="dialogs__childbox_fixed"
      style="display: flex; justify-content: space-between; align-items: center; padding: 6px 0px 0px 0px;">
      <nav class="nav_horiz">
        <ul class="nav__list">
          <li class="nav__item item_selected dev_grp_module_reg"><s:text name='groups.pub.recommend' /></li>
        </ul>
        <div class="nav__underline" style="left: 0px !important; min-width: 120px;"></div>
      </nav>
      <i class="list-results_close" onclick="GrpBase.closeGroupRcmdPub();"></i>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 560px;">
      <div class="main-list__list item_no-border dev_group_pub_confirmList" list-main="grouppubconfirm"></div>
    </div>
  </div>
  <jsp:include page="/common/smate.share.jsp" />
  <input type="hidden" name="frompage" id="frompage" value="${frompage }" />
</body>
</html>
