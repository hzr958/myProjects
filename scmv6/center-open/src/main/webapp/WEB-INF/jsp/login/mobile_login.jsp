<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="/resmod/mobile/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resmod/js/jquery.js"></script>
<script type="text/javascript" src="/resmod/js/rsa/security.js"></script>
<script type="text/javascript">
//当再次编辑时，把提示去掉 SCM-8876
	$(document).ready(function(){
		;(function(){
			$("#userName,#password").focus(function(){
				$("#msg").html("<br>");
			});
		})();
		
	});
	//验证码
	var refreshCode = function(){
		document.getElementById("img_checkcode").src ="/oauth/validatecode.gif?date="+new Date();
	}
	function loginform(){
		var userName = $.trim($("#userName").val());
		var password =$.trim($("#password").val());
		 $.ajax({
				url:"/open/rsa",
				type:"post",
				dataType:"json",
				success:function(data){
					var modulus = data.modulus, exponent = data.exponent;
                    var epwd = $('#password').val();
                    if (epwd.length != 256) {
                        var publicKey = RSAUtils.getKeyPair(exponent, '', modulus);
                        $('#encodePassword').val(RSAUtils.encryptedString(publicKey, password));
                        $("#encodeUserName").val(RSAUtils.encryptedString(publicKey, userName));
                        $("#userName").val("");
                        $("#password").val("");
                        $("#loginForm").submit();
                    }
				}
  		})
	}
	</script>
</head>
<body class="bdbg" style="margin: -8px !important; padding: 0px !important;">
  <div class="logo logo-big"></div>
  <div class="content">
    <form action="/open/login" id="loginForm" method="post">
      <input type="hidden" id="back" name="back" value="${back}" /> <input type="hidden" id="token" name="token"
        value="${token}" /> <input type="hidden" id="thirdSysName" name="thirdSysName" value="${thirdSysName}" /> <input
        type="hidden" id="needValidateCode" name="needValidateCode" value="${needValidateCode}" />
      <div class="l_input">
        <i class="icon_user"></i><input value="${userName}" id="userName" name="userName" class="ui_input" type="text"
          placeholder="邮箱/手机号/科研号"><input type="hidden" id="encodeUserName" name="encodeUserName" value="" />
      </div>
      <div class="l_input">
        <i class="icon_pw"></i><input id="password" name="password" class="ui_input" type="password" placeholder="密码"><input
          type="hidden" id="encodePassword" name="encodePassword" value="" />
      </div>
      <c:if test="${needValidateCode eq '1'}">
        <a class="yzm_pic" onclick="refreshCode();" href="###"><img src="/oauth/validatecode.gif" id="img_checkcode"></a>
        <div class="l_input yzm_input">
          <input id="validateCode" name="validateCode" class="ui_input" type="text" placeholder="请输入验证码">
        </div>
      </c:if>
      <input type="button" value="登录" class="log_btn" onclick="loginform();"/>
      <c:if test="${loginStatus eq '0'}">
        <p id="msg">${msg }</p>
      </c:if>
      <p></p>
      <div class="sign_up">
        <a href="/oauth/mobile/pwd/forget/index?sysType=${type }" style="color: #39639f;">忘记密码</a>
      </div>
      <div class="forget_password">
        <c:if test="${!empty wxOpenId}">
          <a href="/oauth/mobile/register?wxOpenId=${wxOpenId }&sysType=${type }&back=${back}" style="color: #39639f;">新用户注册</a>
        </c:if>
        <c:if test="${empty wxOpenId}">
          <a href="/oauth/mobile/register?sysType=${type }&back=${back}" style="color: #39639f;">新用户注册</a>
        </c:if>
      </div>
    </form>
  </div>
</body>
</html>
