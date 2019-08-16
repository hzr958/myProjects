<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>科研之友</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="keywords"
  content="科研之友，科研社交网络，科研创新生态环境，学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研诚信，同行专家，科研系统，科研项目申请书，ISIS系统。" />
<meta name="description" content="科研之友：科研社交网络，成就科研梦想。学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研系统。" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.scmtips.css" />
<link href="${resmod}/css_v5/agency.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/header.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/index.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.validate.min.js"></script>
  <script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmemail_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript">
var locale='${locale}';
var lgType = "${loginType }";
var passwordError = "${passwordError}";
var accountHasBind = "${accountHasBind}";
var errorMsg = "${msg}";
$(document).ready(function() {
  var domain = document.domain;
  var httpsUrl = "https://" + domain + "/oauth/pc/register/sava";
  $("#regForm_2").attr("action",httpsUrl);
	//绑定出错的错误信息弹窗显示
	if(passwordError == "false"){
		scmpublictoast("<s:text name='register.bind.error' />",5000);
	}
	//绑定出错的错误信息弹窗显示
	if(accountHasBind == "true"){
		scmpublictoast("<s:text name='register.bind.hasbind' />",5000);
	}
	//监听浏览器回退时间，禁止回到二维码页面
	if (window.history && window.history.pushState) {
	    $(window).on('popstate', function() {
	      window.history.pushState('forward', null, '/ouath/index');
          window.history.forward();
	    });
	  }
	
	Register.initCommonData();
$("#regForm_1").validate({
    errorPlacement: function(error,element) {//修改错误的显示位置. targetloca
    	$('<br/>').appendTo(element.parent());
        error.appendTo(element.parent());
    },
    rules:{
    	userName : {
            required: true,
            checkEmail : true,
            maxlength: 50
        },
        passwords: {
            required: true,
            minlength: 6,
            maxlength: 40
        },
    },
    messages:{
    	userName: {
             required: "<s:text name='register.email' /><s:text name='register.required' />",
             checkEmail: "<s:text name='register.isemail' />",
             maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
         },
         passwords:{
        	 required: "<s:text name='register.password' /><s:text name='register.required' />",
        	 minlength: jQuery.validator.format("<s:text name='register.minlength' />"),
             maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
         }
    }
});
//注册校验
$("#regForm_2").validate({
    errorPlacement: function(error,element) {//修改错误的显示位置. targetloca
    	$('<br/>').appendTo(element.parent());
            error.appendTo(element.parent());
    },
    rules: {
        name: {
            required: true,
            maxlength: 61
        },
        <s:text name='page.index.show.lastName' />: {
            required: true,
            nameCheck:true
        },
        <s:text name='page.index.show.firstName' /> : {
            required: true,
            nameCheck:true
        },
        email : {
            required: true,
            checkEmail : true,
            remote: "/oauth/register/ajaxCheckedUserName",
            maxlength: 50
        },
        newpassword: {
            required: true,
            minlength: 6,
            maxlength: 40
        },
        checkedall: {
            required: true
        }
    },
    messages: {
        name: {
            required: "<s:text name='register.name.zh' /><s:text name='register.required' />",
            maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
        },
        
        <s:text name='page.index.show.lastName' />: {
            required: "<s:text name='register.bind.lastname' /><s:text name='register.required.not.blank' />"
        },
        <s:text name='page.index.show.firstName' />: {
            required: "<s:text name='register.bind.firstname' /><s:text name='register.required.not.blank' />"
        },
        /* lastName: {
            required: "<s:text name='register.bind.lastname' /><s:text name='register.required.not.blank' />",
            maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
        },
        firstName: {
            required: "<s:text name='register.bind.firstname' /><s:text name='register.required.not.blank' />",
            maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
        }, */
        email: {
            required: "<s:text name='register.email' /><s:text name='register.required' />",
            checkEmail: "<s:text name='register.isemail' />",
            maxlength: jQuery.validator.format("<s:text name='register.maxlength' />"),
            remote: "<span class='logintitle'><s:text name='register.email.exists' /></span>"
        },
        newpassword: {
            required: "<s:text name='register.password' /><s:text name='register.required' />",
            minlength: jQuery.validator.format("<s:text name='register.minlength' />"),
            maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
        },
        checkedall: {
            required: "<s:text name='register.iris6' />"
        }
    }
});

//获取父页面的url ROL-4006 目前处理办法，先注释这个代码
//$(".parentWindowUrl").val(window.opener.document.location.href);
});

function setTab(id){
    if(id==1){
        $("#registSnsTab").css("display","none");   
        $("#linkSnsTab").css("display",""); 
        $("#tab2").removeClass("hover hover-title-back");
        $("#tab1").addClass("hover hover-title-back");
        if($("#userName_1").val()==""){
            $("#userName_1").val($("#userName_2").val());
        }
    }else{
        $("#linkSnsTab").css("display","none"); 
        $("#registSnsTab").css("display","");
        $("#tab1").removeClass("hover hover-title-back");
        $("#tab2").addClass("hover hover-title-back");
        if($("#userName_2").val()==""){
            $("#userName_2").val($("#userName_1").val());
        }
        //邮箱不为空需要触发校验
        if($("#email").val() != ""){
            $("#email").blur();
        }
    }   
};
jQuery.validator.addMethod("checkEmail", function(value, element, param){
    value = $.trim(value.toLowerCase());
    var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
    return this.optional(element)||(checkEmail.test(value)); 
}, "邮件格式不正确！");
//姓名验证
jQuery.validator.addMethod(
        "nameCheck",
        function(value, element){
            value = $.trim(value);
            var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
            return pattern.test(value);
        }, "<s:text name='register.invalidName' />"
        );
        
/**
 * 点击提交
 */
 function beforeRegister(obj){
    //防重复点击
   BaseUtils.doHitMore(obj , 2000) ;
   setTimeout(function(){
       $("#newpassword").val("")
   }, 1500);
   return true ;
}
</script>
</head>
<body>
<div id="home-header">
        <div class="sm-top">
          <div class="logo2" style="height: 22px;">
            <a href="${domainscm}" style="height: 22px;" title="<s:text name="oauth.login.logo.tip" />"
              alt="<s:text name="oauth.login.logo.tip" />"></a>
          </div>
          <div class="h-top-nav">
            <a href="/resscmwebsns/html/helpCenter/index.html"> <s:text name="oauth.login.help" />
            </a> | <a href="/resscmwebsns/html/res_scm_zh_CN.html"> <s:text name="oauth.login.resource" />
            </a> | <a style="cursor: pointer;" onclick="SCMIndex.change_locol_language('zh_CN');">中文版</a> | <a
              style="cursor: pointer;" onclick="SCMIndex.change_locol_language('en_US');">English</a>
          </div>
        </div>
      </div>
      <div class="clear_h20"></div>
  <div class="menu">
    <input type="hidden" value="" id="passwordError" name="passwordError" />
    <div class="menu-bjleft"></div>
    <div class="menu-bjcenter">
      <div class="m-text">
        <span class="fcu14"><s:text name='register.bind.joinlabel' /> </span>
      </div>
      <div class="mailbox">
        <span class="fcu14"><s:text name='register.bind.intro' /></span>
      </div>
    </div>
    <div class="menu-bjright"></div>
  </div>
  <div id="zhuce">
    <div class="z-left">
      <table width="729" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="23" valign="top"><img src="${resmod}/images_v5/login_nrbj2.gif" width="729" height="23" /></td>
        </tr>
        <tr>
          <td align="left" valign="top" class="login_centerbg pb60">
            <div class="binding">
              <ul>
                <li id="tab1" class="hover hover-title-back" onclick="setTab(1);"><s:text
                    name="register.bind.hasaccount" /> <!-- <span class="">登录完成绑定</span> --></li>
                <li id="tab2" onclick="setTab(2);"><s:text name="register.bind.noaccount" /> <!-- <span class="">创建新账号绑定</span> --></li>
              </ul>
            </div>
            <div id="linkSnsTab">
              <form class="cmxform" id="regForm_1" name="regForm_1" action="/oauth/thirdlogin/scmloginconnect"
                method="post">
                <input type="hidden" name="des3ThirdId" value="${des3ThirdId }" /> 
                <input type="hidden" name="loginType" value="${loginType }" /> 
                <input type="hidden" name="wechatName" value="${wechatName }" /> 
                <input type="hidden" name="qqName" value="${qqName }" /> 
                <input type="hidden" name="parentWindowUrl" class="parentWindowUrl" value="" /> 
                <input type="hidden" name="host" value="${host }" />
                <input type="hidden" name="weiboNickname" value="${weiboNickName}"/>
                <input type="hidden" name="weiboUid" value="${uid }"/>
                <table width="580" border="0" align="center" cellpadding="0" cellspacing="0" class="login-mail bound">
                  <tr>
                    <td width="120" align="right">&nbsp;</td>
                    <td width="460" align="left"><c:if test="${loginType eq '1' }">
                        <s:text name="register.bind.welcomeyou" />
                        <img alt="QQ" src="${resmod }/images_v5/third_login/qq_login.png" />${qqName }</c:if> <c:if
                        test="${loginType eq '2' }">
                        <s:text name="register.bind.welcomeyou" />
                        <img alt="weibo" src="${resmod}/images_v5/third_login/weibo_sina_login.png" />${weiboNickName }</c:if>
                      <c:if test="${loginType eq '3' }">
                        <s:text name="register.bind.welcomeyou" />
                        <img class="wechat_avator" alt="WeChat" src="${resmod}/images_v5/third_login/wx_logo.png" />${wechatName }</c:if>
                    </td>
                  </tr>
                  <tr>
                    <td width="120" align="right">&nbsp;</td>
                    <td width="460" align="left"><span class="hint"><s:text
                          name="register.bind.inputhasaccount" /> <c:if test="${loginType eq '1' }">QQ</c:if> <c:if
                          test="${loginType eq '2' }">
                          <s:text name='register.bind.sinaweibo' />
                        </c:if> <c:if test="${loginType eq '3' }">
                          <s:text name='register.bind.wechat' />
                        </c:if> <s:text name='register.bind.accountbinding' /></span></td>
                  </tr>
                  <tr class="targetloca">
                    <td align="right"><span class="red">*</span> <s:text name="register.email" />:</td>
                    <td align="left"><input type="text" name="userName" id="userName" class="inp_text"
                      style="width: 250px;" onblur="$(this).val($.trim($(this).val()));" /></td>
                  </tr>
                  <tr class="targetloca">
                    <td align="right"><span class="red">*</span> <s:text name="register.password" />:</td>
                    <td align="left"><input type="password" name="passwords" id="passwords" class="inp_text"
                      style="width: 250px;" /></td>
                  </tr>
                  <tr>
                    <td></td>
                    <td>
                      <div class="binding_but">
                        <input type="submit" name="registerButton" class="uiButton-green"
                          style="float: left; padding: 10px 40px;" id="registerButton"
                          value="<s:text name='register.bind.button.connect' />" /> <a href="javascript:void(0);"
                          onclick="setTab(2);" class="ml16"><span class="span"><s:text
                              name="register.bind.newaccountbinding" /></span></a>
                        <div class="errorMessage">
                          <s:actionmessage />
                        </div>
                      </div>
                    </td>
                  </tr>
                </table>
              </form>
            </div> <!--  -------------------- -->
            <div id="registSnsTab" style="display: none">
              <form class="cmxform" id="regForm_2" name="regForm_2" action="/oauth/pc/register/sava" method="post">
                <input type="hidden" name="des3ThirdId" value="${des3ThirdId}" /> 
                <input type="hidden" name="loginType" value="${loginType }" /> 
                <input type="hidden" name="wechatName" value="${wechatName }" /> 
                <input type="hidden" name="qqName" value="${qqName }" /> 
                <input type="hidden" value="/oauth/thirdlogin/scmregist" name="back" /> 
                <input type="hidden" name="host" value="${host }" />
                <input type="hidden" name="parentWindowUrl" class="parentWindowUrl" value="" />
                <input type="hidden" name="weiboNickname" value="${weiboNickName}"/>
                <input type="hidden" name="weiboUid" value="${uid }"/>
                <table width="580" border="0" align="center" cellpadding="0" cellspacing="0" class="login-mail bound">
                  <tr>
                    <td width="120" align="right">&nbsp;</td>
                    <td width="460" align="left"><c:if test="${loginType eq '1' }">
                        <s:text name="register.bind.welcomeyou" />
                        <img alt="QQ" src="${resmod }/images_v5/third_login/qq_login.png" />${qqName }</c:if> <c:if
                        test="${loginType eq '2' }">
                        <s:text name="register.bind.welcomeyou" />
                        <img alt="weibo" src="${resmod}/images_v5/third_login/weibo_sina_login.png" />${weiboNickName }</c:if>
                      <c:if test="${loginType eq '3' }">
                        <s:text name="register.bind.welcomeyou" />
                        <img class="wechat_avator" alt="WeChat" src="${resmod}/images_v5/third_login/wx_logo.png" />${wechatName }</c:if>
                    </td>
                  </tr>
                  <tr>
                    <td width="120" align="right">&nbsp;</td>
                    <td width="460" align="left"><span class="hint"><s:text
                          name="register.bind.createnewaccount" /> <c:if test="${loginType eq '1' }">QQ</c:if> <c:if
                          test="${loginType eq '2' }">
                          <s:text name='register.bind.sinaweibo' />
                        </c:if> <c:if test="${loginType eq '3' }">
                          <s:text name='register.bind.wechat' />
                        </c:if> <s:text name='register.bind.accountbinding' /></span></td>
                  </tr>
                  <tr class="targetloca">
                    <td align="right"><span class="red">*</span> <s:text name="register.email" />:</td>
                    <td align="left"><input type="text" name="email" id="email" maxlength="50" class="inp_text"
                      style="width: 250px;" onblur="$(this).val($.trim($(this).val()));" /></td>
                  </tr>
                  <tr class="targetloca">
                    <td align="right"><span class="red">*</span> <s:text name="register.password" />:</td>
                    <td align="left"><input type="password" name="newpassword" maxlength="40" id="newpassword"
                      class="inp_text" style="width: 250px;" /></td>
                  </tr>
                  <tr class="targetloca">
                    <td align="right"><span class="red">*</span> <s:text name="register.bind.lastname" />:</td>
                    <td align="left"><c:if test='${locale!="en_US" }'>
                        <input type="text" name="zhlastName" id="zhlastName" class="inp_text" style="width: 250px;"
                          maxlength="20" onblur="$(this).val($.trim($(this).val()));" />
                        <input type="hidden" name="lastName" id="lastName" />
                      </c:if> <c:if test='${locale=="en_US" }'>
                        <input type="text" name="lastName" id="lastName" class="inp_text" style="width: 250px;"
                          maxlength="40" onblur="$(this).val($.trim($(this).val()));" />
                      </c:if></td>
                  </tr>
                  <tr class="targetloca">
                    <td align="right"><span class="red">*</span> <s:text name="register.bind.firstname" />:</td>
                    <td align="left"><c:if test='${locale!="en_US" }'>
                        <input type="text" name="zhfirstName" id="zhfirstName" class="inp_text" style="width: 250px;"
                          maxlength="40" onblur="$(this).val($.trim($(this).val()));" />
                        <input type="hidden" name="firstname" id="firstname" />
                      </c:if> <c:if test='${locale=="en_US" }'>
                        <input type="text" name="firstName" id="firstName" class="inp_text" style="width: 250px;"
                          maxlength="40" onblur="$(this).val($.trim($(this).val()));" />
                      </c:if></td>
                  </tr>
                  <tr>
                    <td></td>
                    <td>
                      <div class="binding_but">
                        <input type="submit" name="registerButton" onclick="beforeRegister(this);"
                          class="uiButton-green" style="padding: 10px 40px;" id="registerButton"
                          value="<s:text name='register.bind.button.connect' />" />
                        <div class="errorMessage">
                          <s:actionmessage />
                        </div>
                      </div>
                    </td>
                  </tr>
                </table>
              </form>
            </div> <!-- --------------------------- -->
          </td>
        </tr>
        <tr>
          <td height="6" valign="top"><img src="${resmod}/images_v5/login_nrbj4.gif" width="729" height="6" /></td>
        </tr>
      </table>
    </div>
    <div class="z-right">
      <div class="z-joinbj"></div>
      <div class="join-prompt">
        <div class="join-p-title">
          <s:text name="register.bind.binding" />
          <c:if test="${loginType eq '1' }">QQ</c:if>
          <c:if test="${loginType eq '2' }">
            <s:text name='register.bind.sinaweibo' />
          </c:if>
          <c:if test="${loginType eq '3' }">
            <s:text name='register.bind.wechat' />
          </c:if>
          <s:text name="register.bind.account" />
        </div>
        <div class="binding_rq">
          <c:if test="${loginType eq '1' }">
            <img src="${resmod}/images_v5/qqpic.png" />
          </c:if>
          <c:if test="${loginType eq '2' }">
            <img src="${resmod}/images_v5/third_login/weibo_to_scm.png" />
          </c:if>
          <c:if test="${loginType eq '3' }">
            <img src="${resmod}/images_v5/third_login/wechat_to_scm.png" />
          </c:if>
        </div>
      </div>
    </div>
  </div>
  <div id="footer">
          <div class="box_footer">
            <div class="footer-left" style="width: 63%;">
              <s:text name="oauth.footer.label.name" />
              <img src="${resscmsns}/images_v5/beian/beian.png" style="width: 12px;">
              <s:text name="oauth.foot.new.beian" />
            </div>
            <div class="footer-right">
              <a href="/resscmwebsns/html/about_us_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.aboutUs" />
              </a> | <a href="/resscmwebsns/html/policy_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.privacv" />
              </a> | <a href="/resscmwebsns/html/condition_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.terms" />
              </a> | <a href="/resscmwebsns/html/contact_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.contactUs" />
              </a>
            </div>
          </div>
        </div>
</body>
</html>
<script language="javascript">
//初始化数据
Register.initCommonData();

</script>
