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
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script src="${resmod}/js/plugin/jquery.validate.min.js"></script>
<script src="${resmod}/js/plugin/des/des.js"></script>
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
    window.onload=function(){
        document.getElementsByClassName("resetpassword-container_body")[0].style.height = window.innerheight - 96 + "px"; 
        $("#resetpwdForm").validate({
            onkeyup : false,
            errorPlacement : function(error,element) {//修改错误的显示位置.
                 error.appendTo(element.closest(".dev_input").next(".dev_error_msg"));
/*                 element.closest(".dev_error_msg").after(error);
 */            },
            submitHandler: function(form){
                var encrypt = strEnc($.trim($("#newpwd").val()) ,$("#email").val());
                $("#newpwd").val(encrypt);
                $("#renewpwd").val(encrypt);
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
                    required : "密码不能为空",
                    maxlength : "<s:text name='forgetPassword.tips.morethan'/>",
                    minlength : "<s:text name='forgetPassword.tips.lessthan'/>",
                },
                renewpwd : {
                    required : "确认密码不能为空",
                    maxlength : "<s:text name='forgetPassword.tips.morethan'/>",
                    minlength : "<s:text name='forgetPassword.tips.lessthan'/>",
                    equalTo: "<s:text name='forgetPassword.tips.pwddiff'/>"
                }
            }

        });  
    }
    function submitPwd(){
    	if($("#resetpwdForm").valid()){
    	     $("#resetpwdForm").submit();
    	}
    }
</script>
</head>
<body>
  <div class="resetpassword-container">
    <div class="provision_container-title">
      <i class="material-icons" onclick="goBack();">keyboard_arrow_left</i> <span>重置密码</span> <i></i>
    </div>
    <form id="resetpwdForm" action="/oauth/mobile/pwd/reset/toreset" method="post" class="cmxform">
      <input id="key" name="key" type="hidden" value="${key}"> <input id="gen" name="gen" type="hidden"
        value="${gen}">
      <div class="resetpassword-container_body">
        <div class="resetpassword-container_body-accountnumber">
          <span class="resetpassword-container_body-accountnumber_title">账号:</span> <span
            class="resetpassword-container_body-accountnumber_detaile">${email}</span> <input id="email" name="email"
            type="hidden" class="input w300" value="${email}" readonly style="border: none; background: none;">
        </div>
        <div class="resetpassword-container_body-password dev_input">
          <input id="newpwd" name="newpwd" type="password" maxlength="40" placeholder="输入新密码">
        </div>
        <div class="dev_error_msg" style="width: 90%;"></div>
        <div class="resetpassword-container_body-password dev_input">
          <input id="renewpwd" name="renewpwd" type="password" maxlength="40" placeholder="再次输入新密码">
        </div>
        <div class="dev_error_msg" style="width: 90%;"></div>
        <div class="resetpassword-container_body-password_tip">
          <span class="resetpassword-container_body-password_tip-title">温馨提示:</span> <span
            class="resetpassword-container_body-password_tip-detail">请重新设置科研之友的登录密码, 建议使用尽量复杂的密码, 不少于6个字符,
            不多于40个字符</span>
        </div>
      </div>
      <div class="new_subject-field_checked-container_footer" onclick="submitPwd();">确认提交</div>
    </form>
  </div>
</body>
</html>
