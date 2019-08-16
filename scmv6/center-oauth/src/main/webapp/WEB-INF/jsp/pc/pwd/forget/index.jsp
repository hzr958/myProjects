<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta name="keywords"
  content="科研之友，科研社交网络，科研创新生态环境，学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研诚信，同行专家，科研系统，科研项目申请书，ISIS系统。" />
<meta name="description" content="科研之友：科研社交网络，成就科研梦想。学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研系统。" />
<title><s:text name="forgetPassword.label.forgetpassword" /> | <s:text name="skin.main.title_scm" /></title>
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/scmjscollection.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/jquery.validate.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
	window.onload=function(){
		var returnUrl = "${returnUrl}";
		var referrerUrl = window.document.referrer;
		//没有可跳转页面时，隐藏返回按钮
		if(returnUrl.trim()!="" || referrerUrl.trim()!=""){
			document.getElementsByClassName("border_btn")[0].style.display="inline";
		}
	}
</script>
<body>
  <%@ include file="/common/header.jsp"%>
  <div class="content-1200 content-special__style">
    <div class="fillin-left">
      <h1>
        <s:text name="forgetPassword.label.forgetpassword" />
        &nbsp;&nbsp; <span><s:text name="sns.v5.page.must.label" /></span>
      </h1>
      <form id="forgetpwdForm" action="/oauth/pwd/forget/sendemail" method="post" class="cmxform">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="fillin__eidt--tab mt20">
          <tr>
            <td align="right" width="30%"><span class="red">*</span> <s:text name="forgetPassword.label.email" />:&nbsp;
            </td>
            <td><input id="email" name="email" type="text" class="input w300" maxlength="50" value="${email}"
              placeholder="<s:text name='forgetPassword.email.tips' />"></td>
          </tr>
          <tr>
            <td align="right"><span class="red">*</span> <s:text name="forgetPassword.label.validateCode" />:&nbsp;
            </td>
            <td><input id="validateCode" name="validateCode" type="text" class="input w200" maxlength="4" value="">
              <a class="yzm"> <img id="validateCodeImg" onclick="refreshCode();"
                alt="<s:text name='forgetPassword.label.code' />">
            </a></td>
          </tr>
          <tr>
            <td align="right"></td>
            <td><input type="submit" class="blue_btn mw100" value="<s:text name='forgetPassword.btn.submit' />"
              style="float: left; margin-right: 32px;">
              <button type="button" style='display: none' class="border_btn mw100 ml10" onclick="back()">
                <s:text name='forgetPassword.btn.return' />
              </button></td>
          </tr>
        </table>
      </form>
    </div>
    <div class="fillin-right">
      <h2>
        <s:text name="forgetPassword.label.joinus" />
      </h2>
      <ul>
        <li><i class="icon-join-related01"></i>&nbsp;<s:text name="forgetPassword.label.keepInTouch" /></li>
        <li><i class="icon-join-related02"></i>&nbsp;<s:text name="forgetPassword.label.generalizeResearchPub" /></li>
        <li><i class="icon-join-related03"></i>&nbsp;<s:text name="forgetPassword.label.getOpportunities" /></li>
      </ul>
    </div>
  </div>
  <%@include file="/common/footer.jsp"%>
  <script src="${resmod}/js/jquery-1.11.3.js"></script>
  <script src="${resmod}/js/plugin/jquery.validate.min.js"></script>
  <script language="javascript">
		jQuery.validator.addMethod("checkEmail", function(value, element, param){
			value = $.trim(value.toLowerCase());
			var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
		    return this.optional(element)||(checkEmail.test(value)); 
		}, "邮件格式不正确！");
		$(document).ready(function() {
			refreshCode();
			$("#email").focus();
			$(":input").blur(function() {
				$(this).val($.trim($(this).val()));
				$(this).removeData("previousValue");
			});
			$("#forgetpwdForm").validate({
				onkeyup : false,
				errorPlacement : function(error,element) {//修改错误的显示位置.
					error.appendTo(element.parent());
				},
				rules : {
					email : {
						required : true,
						checkEmail : true,
						remote:	"/oauth/pwd/ajaxcheckedemail",
						maxlength : 50
					},
					validateCode : {
						required : true,
						minlength : 4,
						remote : "/oauth/validatecode/check"
					}
				},
				messages : {
					email : {
						required : "<s:text name='forgetPassword.tips.notBlank'/>",
						checkEmail : "<s:text name='forgetPassword.tips.invalidEmail'/>",
						maxlength : "<s:text name='forgetPassword.tips.morethan'/>",
						remote : "<s:text name='forgetPassword.tips.emailNotExist'/>"
					},
					validateCode : {
						required : "<s:text name='forgetPassword.tips.notBlank'/>",
						remote : "<s:text name='forgetPassword.tips.validateCodeError'/>",
						minlength : "<s:text name='forgetPassword.tips.validateCodeError'/>"
					}
				}
	
			});
		});
		function refreshCode() {
			document.getElementById("validateCodeImg").src = "/oauth/validatecode.gif?" + Math.random();
			$("#validateCode").val("");
			$("#validateCode").removeData("previousValue");
			$("#validateCode").focus();
		};
		function back() {
			var returnUrl = "${returnUrl}";
			if(returnUrl != ""){
				window.location.href= returnUrl;
			}else if (window.document.referrer != "" && window.document.referrer != window.location.href) {
				window.location.href = window.document.referrer;
			} else {
				window.history.back();
			}
		}
	</script>
</body>
</html>
