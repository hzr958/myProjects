<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>科研之友</title>
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/new-confirmbox/confirm.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/jquery.validate.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/base/psnsetting.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/base/psnsetting.base.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/password/psnsetting.password_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/password/psnsetting.password.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/privacy/psnsetting.privacy.set_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/privacy/psnsetting.privacy.set.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/attention/psnsetting.attention.set_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/attention/psnsetting.attention.set.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/email/psnsetting.email.set_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/email/psnsetting.email.set.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/merge/psnsetting.merge.set_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/psnsetting/merge/psnsetting.merge.set.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />

<script type="text/javascript">
var model="${model}";
var domainscm="${domainscm}";
$(document).ready(function(){
	
	if(model =="account"){
		$("#psnsetting_account_email").click();
	}else if(model=="password" ){
		$("#psnsetting_change_password").click();
	}else if(model=="privacy" ){
		$("#psnsetting_privacy").click();
	}else if(model=="attention" ){
		$("#psnsetting_attention").click();
	}else if(model=="email" ){
		$("#psnsetting_email").click();
	}else if(model=="merge" ){
		$("#psnsetting_merge").click();
	}else{
		$("#psnsetting_account_email").click();
	}
   var emailist = document.getElementsByClassName("confirm-email__body-list__address");
   for(var i = 0; i < emailist.length; i++){
	   emailist[i].onmouseover = function(){
		   this.title = this.innerHTML;
	   }
   }
});

function showMain(url,obj ,paramt ,callback){
	$("#psnsetting_main_box").doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	});
	$(".psn-Setup__item-selctor").removeClass("psn-Setup__item-selctor");
	$(obj).addClass("psn-Setup__item-selctor");
	$.ajax({
		url : url,
		type : 'post',
		dataType:'html',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				$("#psnsetting_main_box").html(data);
				if(typeof callback == "function"){
					callback();
				}
			});
		},
		error: function(){
		}
	});	
	
};


//帐号邮件
function showAccountEmailMain(obj){
	PsnsettingBase.changeUrl("account");
	showMain('/psnweb/psnsetting/ajaxaccountemaillist',obj,{} ,PsnsettingPwd.checkEmailSendDate );		
};
//设置密码
function showChangePasswordMain(obj){
	PsnsettingBase.changeUrl("password");
	showMain('/psnweb/psnsetting/ajaxchangepassword',obj,{} ,PsnsettingPwd.initForm );		
};
//设置隐私
function showPrivacyMain(obj){
	PsnsettingBase.changeUrl("privacy");
	showMain('/psnweb/psnsetting/ajaxprivacyinit',obj,{} ,PsnsettingPrivacy.init );		
};
//设置关注
function showAttentionMain(obj){
	PsnsettingBase.changeUrl("attention");
	showMain('/psnweb/psnsetting/ajaxattentioninit',obj,{}, PsnsettingAttention.init );		
};
//设置邮件
function showEmailMain(obj){
	PsnsettingBase.changeUrl("email");
	showMain('/psnweb/psnsetting/ajaxgetMailTypeList',obj,{},  PsnsettingEmail.init );		
};
//合并账号
<%-- 先隐藏帐号合并功能 tsz 20181010 --%> 
function showMergeMain(obj){
	PsnsettingBase.changeUrl("merge");
	showMain('/psnweb/psnsetting/ajaxmergeCount',obj,{},  PsnsettingMerge.init );		
};

function bindQQ() {
    window.open('/psnweb/psnsetting/qqloginrelation','newwindow',
    'height=560,width=1024,top=220,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no')
}
function bindWeChat(){
    var  link = "/oauth/wechat/bind?wechatFunction=bindWX&des3PsnId="+$("#email_mail_tilte").attr("des3PsnId") ;
    window.open(link,'newwindow',
        'height=560,width=1024,top=220,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no')
}
</script>
</head>
<body>
  <div class="page-box" occupy="${occupy }">
    <div class="set-container" style="margin-top: 32px;">
      <div class="set-container__left">
        <div class="psn-Setup__title">
          <s:text name="psnset.psnset" />
        </div>
        <!--  帐号邮件 start -->
        <div class="psn-Setup__item" id="psnsetting_account_email" onclick="showAccountEmailMain(this);">
          <i class="psn-Setup__item-tip set-passwordemail__main-tip"></i>
          <div class="psn-Setup__item-title">
            <s:text name="psnset.account.email" />
          </div>
        </div>
        <!--  帐号邮件 end -->
        <div class="psn-Setup__item" id="psnsetting_change_password" onclick="showChangePasswordMain(this);">
          <i class="psn-Setup__item-tip set-password__main-tip"></i>
          <div class="psn-Setup__item-title">
            <s:text name="psnset.pwdset" />
          </div>
        </div>
        <!--  先隐藏隐私设置 后面在改  -->
        <div class="psn-Setup__item" id="psnsetting_privacy" onclick="showPrivacyMain(this);">
          <i class="psn-Setup__item-tip set-privacy__main-tip"></i>
          <div class="psn-Setup__item-title">
            <s:text name="psnset.privacyset" />
          </div>
        </div>
        <div class="psn-Setup__item psn-Setup__item-selctor" id="psnsetting_attention"
          onclick="showAttentionMain(this);">
          <i class="psn-Setup__item-tip set-attention__main-tip"></i>
          <div class="psn-Setup__item-title">
            <s:text name="psnset.attentionset" />
          </div>
        </div>
        <div class="psn-Setup__item" id="psnsetting_email" onclick="showEmailMain(this);">
          <i class="psn-Setup__item-tip set-email__main-tip"></i>
          <div class="psn-Setup__item-title">
            <s:text name="psnset.emailset" />
          </div>
        </div>
        <%-- 先隐藏帐号合并功能 tsz 20181010--%>
        <div class="psn-Setup__item" id="psnsetting_merge" onclick="showMergeMain(this);">
          <i class="psn-Setup__item-tip merge-account__main-tip"></i>
          <div class="psn-Setup__item-title">
            <s:text name="psnset.mergeset" />
          </div>
        </div>
        
      <!--   <div class="psn-Setup__item" id="psnsetting_merge" onclick="showMergeMain(this);">
          <i class="psn-Setup__item-tip show-psninfor__main-tip"></i>
          <div class="psn-Setup__item-title">个人信息展示</div>
        </div> -->
        
      </div>
      <div class="set-container__right" id="psnsetting_main_box"></div>
    </div>
  </div>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
