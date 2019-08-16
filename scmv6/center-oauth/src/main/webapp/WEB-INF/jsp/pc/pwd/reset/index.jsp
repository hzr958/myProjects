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
<title><s:text name="forgetpassword.label.resetpwd" /> | <s:text name="skin.main.title_scm" /></title>
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/scmjscollection.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.tips {
  text-align: center;
  margin-top: 20px;
}
</style>
</head>
<body>
  <%@ include file="/common/header.jsp"%>
  <div class="content-1200 content-special__style">
    <div class="fillin-left">
      <h1>
        <s:text name="forgetpassword.label.resetpwd" />
        &nbsp;&nbsp; <span><s:text name="sns.v5.page.must.label" /></span>
      </h1>
      <p class="tips">
        <span class="f999"><s:text name="forgetPassword.label.resettips" /></span>
      </p>
      <form id="resetpwdForm" action="/oauth/pwd/reset/toreset" method="post" class="cmxform">
        <input id="key" name="key" type="hidden" value="${key}"> <input id="gen" name="gen" type="hidden"
          value="${gen}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="fillin__eidt--tab mt20">
          <tr>
            <td align="right" width="30%"><s:text name="forgetPassword.label.loginEmail" />:&nbsp;</td>
            <td>
              <div class="targetloca" style="display: flex; flex-direction: column;">
                <input id="email" name="email" type="text" class="input w300" value="${email}" readonly
                  style="border: none; background: none;">
              </div>
            </td>
          </tr>
          <tr>
            <td align="right"><span class="red">*</span> <s:text name="forgetPassword.label.newpwd" />:&nbsp;</td>
            <td>
              <div class="targetloca" style="display: flex; flex-direction: column;">
                <input id="newpwd" name="newpwd" type="password" maxlength="40" class="input w300">
              </div>
            </td>
          </tr>
          <tr>
            <td align="right"><span class="red">*</span> <s:text name="forgetPassword.label.renewpwd" />:&nbsp;</td>
            <td>
              <div class="targetloca" style="display: flex; flex-direction: column;">
                <input id="renewpwd" name="renewpwd" type="password" maxlength="40" class="input w300">
              </div>
            </td>
          </tr>
          <tr>
            <td align="right"></td>
            <td><input type="submit" class="blue_btn mw100" value="<s:text name='forgetPassword.btn.submit' />">
              <input type="reset" class="border_btn mw100 ml10" value="<s:text name='forgetPassword.btn.reset' />">
            </td>
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
  <script src="${resmod}/js/plugin/des/des.js"></script>
  <script language="javascript">
		$(document).ready(function() {
			$(":input").blur(function(){
				$(this).val($.trim($(this).val()));
			});
			$("#resetpwdForm").validate({
				onkeyup : false,
				errorPlacement : function(error,element) {//修改错误的显示位置.
					/* error.appendTo(element.parent()); */
					error.appendTo(element.closest(".targetloca"));
				},
				submitHandler: function(form){
				    //2018-12-12 密码不要加密 SCM-21858
					//var encrypt = strEnc($.trim($("#newpwd").val()) ,$("#email").val());
					//$("#newpwd").val(encrypt);
					//$("#renewpwd").val(encrypt);
					form.submit();
				},
				rules : {
					newpwd: {
						required: true,
						maxlength: 40,
						minlength: 6
					},
					renewpwd:{
						required: true,
						maxlength: 40,
						minlength: 6,
						equalTo: "#newpwd"
					}
				},
				messages : {
					newpwd : {
						required : "<s:text name='forgetPassword.tips.notBlank'/>",
						maxlength : "<s:text name='forgetPassword.tips.morethan'/>",
						minlength : "<s:text name='forgetPassword.tips.lessthan'/>",
					},
					renewpwd : {
						required : "<s:text name='forgetPassword.tips.notBlank'/>",
						maxlength : "<s:text name='forgetPassword.tips.morethan'/>",
						minlength : "<s:text name='forgetPassword.tips.lessthan'/>",
						equalTo: "<s:text name='forgetPassword.tips.pwddiff'/>"
					}
				}
	
			});
		});
	</script>
</body>
</html>
