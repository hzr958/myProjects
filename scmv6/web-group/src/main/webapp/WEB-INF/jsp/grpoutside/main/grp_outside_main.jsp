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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.fileupload.js"></script>
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
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.discuss.list.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.discussCommon_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.discuss.comment.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.dynamic.list.opendetail.pc.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common_${locale }.js"></script>
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
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scm-pc_filedragbox.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dyreaction.js"></script>
<script type="text/javascript">
var model="${model}";
var ispending = "${ispending}";
/* var grpOutsideMainOpenType = "${GrpBaseinfo.openType}";
var grpOutsideMainFlag = "${flag}";
var grpOutsideMainIsBtnEnable = false;
if (grpOutsideMainOpenType == "O") {
  grpOutsideMainIsBtnEnable = true;
} 
if (grpOutsideMainOpenType == "H" && grpOutsideMainFlag == "1") {
  grpOutsideMainIsBtnEnable = true;
} */
$(document).ready(function(){
	addFormElementsEvents();
	//领域和关键词的加载
	GrpBase.showDisciplinesAndKeywords();
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

	}else if(  ( model=="projectPub" || model=="pub" ) && $("#project_pub_main").length > 0){
		$("#project_pub_main").click();

	}else if(model=="projectRef" && $("#project_ref_main").length > 0){
		$("#project_ref_main").click();

	}else if($("#discuss_main").length > 0){
		$("#discuss_main").click();
	} else{
		$(".nav__list > .nav__item").eq(0).click()
	}
	window.LoadShortUrlEdit({
		"scm_myfunction":GrpBase.doSaveShortUrl,
		"scm_role":2
		});
});


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
}

 /**
  * 群组讨论主页
  */
 function showGrpDiscussMain(obj){
	 GrpBase.changeUrl("discuss");
 	showMain('/groupweb/grpinfo/outside/ajaxdiscussmain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});	
 };
 
/**
 * 群组文献主页
 */
function showGrpPubsMain(obj){
	GrpBase.changeUrl("pub");
	var url='/groupweb/grpinfo/outside/ajaxshowgrppubsmain';
	showMain(url,obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});	
};
/**
 * 群组成果主页  projectPub or pub==项目成果  ，  projectRef== 项目文献
 */
function showGrpProjectPubsMain(obj){
	GrpBase.changeUrl("pub");
	var url='/groupweb/grpinfo/outside/ajaxshowgrppubsmain';
	showMain(url,obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id") ,'module':'projectPub'});	
}
	
/**
 * 群组成果主页  projectPub==项目成果  ， projectRef== 项目文献
 */
function showGrpProjectRefsMain(obj){
	GrpBase.changeUrl("projectRef");
	var url='/groupweb/grpinfo/outside/ajaxshowgrppubsmain';
	showMain(url,obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id") ,'module':'projectRef'});	
};

/**
 * 群组成员主页
 */
function showGrpMemberMain(){
	GrpBase.changeUrl("member");
	var url = '/groupweb/grpinfo/outside/ajaxshowgrpmembermain';
	showMain(url,"",{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});
};

function showMain(url,obj,paramt){
     /* if (grpOutsideMainIsBtnEnable || (url.indexOf("discuss") != -1)) {
         
     } */
     $.ajax({
       url : url,
       type : 'post',
       dataType:'html',
       data:paramt,
       success : function(data) {
           GrpBase.ajaxTimeOut(data,function(){
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
	showMain('/groupweb/grpinfo/outside/ajaxgrpfilemain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id")});		
}
//群组课件
function showGrpCurwareFileMain(obj){
	GrpBase.changeUrl("curware");
	showMain('/groupweb/grpinfo/outside/ajaxgrpfilemain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id"),'module':'curware'});		
}
//群组作业
function showGrpWorkFileMain(obj){
	GrpBase.changeUrl("work");
	showMain('/groupweb/grpinfo/outside/ajaxgrpfilemain',obj,{'des3GrpId':$("#grp_params").attr("smate_des3_grp_id") ,'module':'work'});		
}
 //站外前往登录,登录后跳转：type discuss=群组讨论 member=群组成员 pub=群组成果 file=群组文件
function loginFromOutside(type){
	BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function(){
		var url="/groupweb/grpinfo/main?des3GrpId="+encodeURIComponent($("#grp_params").attr("smate_des3_grp_id"))+"&model="+type;
		document.location.href=url;
	}, 1);
}
 
 function loginFromReqFulltext(){
	 var type = "pub";//pub=成果模块，默认值  //projectRef=文献模块
	 if($("#project_ref_main").hasClass("item_selected")){
		 type = "projectRef";
	 }
	 loginFromOutside(type);
 }

/**
 * 站外分享成果
 * @param obj
 */
 function outsideSharePub(obj){
     SmateShare.getPsnPubListSareParam(obj);
     initSharePlugin(obj);
 }
 //站外个人主页
 function openPsnDetail(des3ProducerPsnId){
	 window.open("/psnweb/outside/homepage?des3PsnId="+encodeURIComponent(des3ProducerPsnId));
 }
 //站外成果详情
 function openPubDetail(des3PubId){
   var groupId = $("#grp_params").attr("smate_des3_grp_id");
   if (des3PubId != null && des3PubId != "") {
     window
         .open("/pub/outside/details?des3PubId=" + encodeURIComponent(des3PubId) + "&des3GrpId=" + encodeURIComponent(groupId));
 }
 }
 //申请加入群组
 function applyJoinGrp(){
	 loginFromOutside($("#grp_params").find(".item_selected").attr("name"));
 }
</script>
</head>
<body>
  <div class="pro-header">
    <div class="pro-header__container">
      <div class="pro-header__left-panel">
        <div class="pro-header__base-info">
          <div class="pro-header__avatar">
            <img src="${grpShowInfo.grpBaseInfo.grpAuatars}"
              onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
            <s:if test="grpShowInfo.role==1 || grpShowInfo.role==2">
              <div class="pro-header__avator-tip">
                <s:text name='groups.outside.pro.header' />
              </div>
            </s:if>
          </div>
          <div class="pro-header__main">
            <div class="pro-header__main_grp-name">${grpShowInfo.grpBaseInfo.grpName}</div>
            <div class="pro-header__main_grp-kw">
              <div class="kw__box" smate_disciplines='${firstDisciplinetName};${secondDisciplinetName}'
                smate_keywords='${grpShowInfo.grpKwDisc.keywords}' id="smate_disciplines_keywords"></div>
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
                <span>${grpShowInfo.grpStatistic.sumMember}&nbsp;<s:text name='groups.outside.list.person' /></span>
                <span>${grpShowInfo.grpStatistic.sumPubs}&nbsp;<s:text name='groups.outside.list.pub' /></span>
                <span>${grpShowInfo.grpCourseFileSum}&nbsp;<s:text name='groups.outside.file.curware' /></span>
                <span>${grpShowInfo.grpWorkFileSum}&nbsp;<s:text name='groups.outside.file.work' /></span>
              </s:if>
              <!--  项目群组 成员，成果，文献，文件   -->
              <s:elseif test="grpShowInfo.grpBaseInfo.grpCategory ==11">
                <span>${grpShowInfo.grpStatistic.sumMember}&nbsp;<s:text name='groups.outside.list.person' /></span>
                <span>${grpShowInfo.grpProjectPubSum}&nbsp;<s:text name='groups.outside.project.pub' /></span>
                <span>${grpShowInfo.grpProjectRefSum}&nbsp;<s:text name='groups.outside.project.ref' /></span>
                <span>${grpShowInfo.grpStatistic.sumFile}&nbsp;<s:text name='groups.outside.list.file' /></span>
              </s:elseif>
              <s:else>
                <span>${grpShowInfo.grpStatistic.sumMember}&nbsp;<s:text name='groups.outside.list.person' /></span>
                <span>${grpShowInfo.grpStatistic.sumPubs}&nbsp;<s:text name='groups.outside.list.pub' /></span>
                <span>${grpShowInfo.grpStatistic.sumFile}&nbsp;<s:text name='groups.outside.list.file' /></span>
              </s:else>
            </div>
          </div>
        </div>
      </div>
      <div class="pro-header__right-panel">
        <div class="pro-header__actions">
          <button class="button_main button_primary-reverse" onclick="applyJoinGrp()" id="btn_apply">
            <s:text name='groups.outside.discover.joinGroup' />
          </button>
        </div>
      </div>
    </div>
    <div class="pro-header__nav" id="grp_params" smate_grpcategory='${grpShowInfo.grpBaseInfo.grpCategory}'
      smate_grp_id='${grpShowInfo.grpBaseInfo.grpId}' smate_des3_grp_id="${des3GrpId}" outside="true">
      <div class="pro-header__nav-box">
        <nav class="nav_horiz">
          <ul class="nav__list">
            <s:if test="grpShowInfo.role==9">
              <c:if test="${grpShowInfo.grpControl.isIndexDiscussOpen==1 }">
                <li class="nav__item" name='discuss' id="discuss_main" onclick='showGrpDiscussMain(this)'><s:text
                    name='groups.outside.list.discuss' /></li>
              </c:if>
              <c:if test="${grpShowInfo.grpControl.isIndexMemberOpen==1 }">
                <li class="nav__item" name='member' id="member_main" onclick='showGrpMemberMain(this)'><s:text
                    name='groups.outside.list.person' /></li>
              </c:if>
              <s:if test="grpShowInfo.grpBaseInfo.grpCategory == 11">
                <c:if test="${grpShowInfo.grpControl.isPrjPubShow==1 }">
                  <li class="nav__item" id="project_pub_main" onclick='showGrpProjectPubsMain(this)'><s:text
                      name='groups.outside.project.pub' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isPrjRefShow==1 }">
                  <li class="nav__item" id="project_ref_main" onclick='showGrpProjectRefsMain(this)'><s:text
                      name='groups.outside.project.ref' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isIndexFileOpen==1 }">
                  <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                      name='groups.outside.list.file' /></li>
                </c:if>
              </s:if>
              <s:elseif test="grpShowInfo.grpBaseInfo.grpCategory == 10">
                <c:if test="${grpShowInfo.grpControl.isCurwareFileShow==1 }">
                  <li class="nav__item" id="curware_file_main" onclick='showGrpCurwareFileMain(this)'><s:text
                      name='groups.outside.file.curware' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isWorkFileShow==1 }">
                  <li class="nav__item" id="work_file_main" onclick='showGrpWorkFileMain(this)'><s:text
                      name='groups.outside.file.work' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isIndexPubOpen==1 }">
                  <li class="nav__item" name='pub' id="pub_main" onclick='showGrpPubsMain(this)'><s:text
                      name='groups.outside.list.pub' /></li>
                </c:if>
              </s:elseif>
              <s:else>
                <c:if test="${grpShowInfo.grpControl.isIndexPubOpen==1 }">
                  <li class="nav__item" name='pub' id="pub_main" onclick='showGrpPubsMain(this)'><s:text
                      name='groups.outside.list.pub' /></li>
                </c:if>
                <c:if test="${grpShowInfo.grpControl.isIndexFileOpen==1 }">
                  <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                      name='groups.outside.list.file' /></li>
                </c:if>
              </s:else>
            </s:if>
            <s:else>
              <li class="nav__item" id="discuss_main" onclick='showGrpDiscussMain(this)'><s:text
                  name='groups.outside.list.discuss' /></li>
              <li class="nav__item" id="member_main" onclick='showGrpMemberMain(this)'><s:text
                  name='groups.outside.list.person' /></li>
              <li class="nav__item" id="pub_main" onclick='showGrpPubsMain(this)'><s:text
                  name='groups.outside.list.pub' /></li>
              <li class="nav__item" id="file_main" onclick='showGrpFileMain(this)'><s:text
                  name='groups.outside.list.file' /></li>
            </s:else>
          </ul>
          <div class="nav__underline"></div>
        </nav>
        <s:if test="grpShowInfo.grpIndexUrl!=null && grpShowInfo.grpIndexUrl!=''">
          <div class="pro-header__url" id="id_shorturl" scm-paren='span_paren'>
            <a><span id="span_shorturl" scm-oldurl="${grpShowInfo.grpIndexUrl}" scm-id='span_id'
              title="${grpShowInfo.grpIndexUrl}">${grpShowInfo.grpIndexUrl}</span></a> <i
              class="material-icons pro-header__url-share" scm-id='span_c_s'
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
</body>
</html>
