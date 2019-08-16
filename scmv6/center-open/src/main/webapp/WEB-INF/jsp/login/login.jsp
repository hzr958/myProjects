<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科研之友第三方登录</title>
<link href="${resmod}/css/smate.scmtips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<style>
body, h1, h2, h3, h4, h5, h6, hr, p, blockquote, dl, dt, dd, ul, ol, li, pre, form, fieldset, legend, button, input,
  textarea, select, th, td, div {
  margin: 0;
  padding: 0
}

ul, li {
  list-style: none;
}

body {
  background: #666;
}

.clear {
  clear: both
}

.fr {
  float: right
}

.mt15 {
  margin-top: 15px;
}

.mr6 {
  margin-right: 6px;
}

.mt20 {
  margin-top: 20px;
}

.sm_login {
  width: 800px;
  margin: 0 auto;
  height: 460px;
  background: #fff;
  font-family: arial, "宋体";
}

.sm_login .title {
  height: 48px;
  background: #f0eff1;
  padding: 0 20px;
}

.sm_login .mid {
  height: 300px;
  padding-bottom: 50px;
  margin: 50px 0 0 17px;
  background: url(/ressns/images/sm_login/login_bg.jpg) no-repeat top right
}

.sm_login .lst {
  width: 445px;
  border-right: 1px #f0eff1 solid;
  font-size: 12px;
  padding: 10px 0 30px 0;
  float: left;
}

.sm_login .lst input, .sm_login .lst span {
  vertical-align: middle;
  font-family: arial, "宋体";
}

.sm_login .lst li {
  width: 100%;
  float: left;
}

.sm_login .lst li span {
  width: 115px;
  font-size: 14px;
  color: #666;
  line-height: 32px;
  display: inline-block;
  padding-bottom: 12px;
}

.sm_login .lst_ipt {
  width: 246px;
  height: 22px;
  line-height: 22px;
  border: 1px #ddd7d9 solid;
  padding: 5px;
  background: #fff;
  color: #666
}

.sm_login .lst_ipt_code {
  width: 80px;
  height: 22px;
  line-height: 22px;
  border: 1px #ddd7d9 solid;
  padding: 5px;
  background: #fff;
  color: #666
}

.sm_login .lst div {
  width: 256px;
  color: #666;
}

.sm_login .lst div a {
  color: #999;
}

.sm_login .lst div a:hover {
  color: #777;
}

* html .sm_login .lst input {
  margin-top: -3px;
}

.sm_login .but {
  padding: 11px 0 0 161px;
}

.sm_login .button {
  width: 256px;
  height: 38px;
  font-size: 14px;
  color: #FFF;
  font-weight: bold;
  line-height: 100%;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
  display: inline-block;
  background: url(/ressns/images/sm_login/sm_icon.png) no-repeat scroll -352px -179px;
  border: 1px solid #285510;
}

.sm_login .hint {
  width: 308px;
  margin-top: 26px;
  padding-top: 20px;
}

.sm_login .hint dt, .sm_login .hint dd {
  float: left;
  vertical-align: middle;
}

.sm_login .hint dt {
  width: 50px;
}

.sm_login .hint dd {
  width: 250px;
  font-size: 12px;
  line-height: 21px;
  color: #666;
}

.sm_ico1, .sm_ico2 {
  background: url(/ressns/images/sm_login/sm_login_ico.png) no-repeat;
  display: inline-block;
}

.sm_ico1 {
  width: 40px;
  height: 36px;
  background-position: 0px 0px;
  margin: 5px 0 0 2px;
}

.sm_ico2 {
  width: 11px;
  height: 11px;
  background-position: -9px -39px;
  margin-top: 18px;
}

.sm_ico2:hover {
  background-position: -9px -62px;
}

.sm_login .txt {
  width: 240px;
  margin: 30px 0 0 70px;
  float: left;
}

.sm_login .txt h2 {
  font: 20px/30px "Microsoft YaHei", "SimHei";
  color: #666;
}

.sm_login .txt {
  font: 16px/30px "Microsoft YaHei", "SimHei";
  color: #999;
}
</style>
<script type="text/javascript" src="/resmod/js/jquery.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/resmod/js/jquery.validate.js"></script>
<script type="text/javascript" src="/resmod/js/rsa/security.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var domain=location.host;
		$("#logo").attr("href","http\://"+domain);
		$("#openForm").validate({
			errorPlacement : function(error, element) {//修改错误的显示位置.
				error.attr("style","display:inline-block;margin-left: 120px;color:red;");
				error.appendTo(element.parent());
			},
			rules : {
				encodeUserName : {
					required : true
				},
				encodePassword : {
					required : true
				}
                <c:if test="${needValidateCode=='1'}">
                ,validateCode : {
					required : true
				}
				</c:if>
			},
			messages : {
				encodeUserName : {
					required : "邮箱不能为空"
				},
				encodePassword : {
					required : "密码不能为空"
				}
				 <c:if test="${needValidateCode=='1'}">
	                ,validateCode : {
						required : "验证码不能为空"
					}
			   </c:if>
			}/* ,
			submitHandler : function(form) {
				document.getElementById("login").attr("disabled",true);
			} */
			
		});
		
		if(window.top!=window.self){
			//存在父页面
			$("#logo").attr("target","_blank");
			$("#register").attr("target","_blank");
			/* $("#repwd").attr("target","_blank"); */
		};
		
		$("#repwd").mousedown(function(){
			 var email = $("#userName").val(); 
			  var frmUrl = "https://"+location.host+"/oauth/pwd/forget/index?email="+email+"&from=sns";  
			 this.href=frmUrl;
		});
		$("#register").mousedown(function (){
			var thirdSysName = $("#thirdSysName").val();
			var frmUrl = "https://"+location.host+"/oauth/pc/register?thirdSysName="+encodeURI(thirdSysName);  
			  this.href=frmUrl;
		});
		
				/* var Bits = 1024; 
				var MattsRSAkey = cryptico.generateRSAKey(userName, Bits);
				var MattsPublicKeyString = cryptico.publicKeyString(MattsRSAkey); */
	    	
	 });
	
	function refreshCode(){
		document.getElementById("img_checkcode").src = "/oauth/validatecode.gif?date="+new Date();
	}
	function loginform(){
		var userName = $("#userName").val();
		var password =$("#password").val();
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
                        $("#openForm").submit();
                    }
				}
            
            
  })
}
</script>
</head>
<body>
  <form name="openForm" id="openForm" method="post" action="/open/login">
    <input type="hidden" id="back" name="back" value="${back}" /> <input type="hidden" id="token" name="token"
      value="${token}" /> <input type="hidden" id="thirdSysName" name="thirdSysName" value="${thirdSysName}" /> <input
      type="hidden" id="needValidateCode" name="needValidateCode" value="${needValidateCode}" /> <input type="hidden"
      id="locale" name="locale" value="${locale}" />
    <div class="sm_login">
      <div class="title">
        <a id="logo" target="_blank" href="https://www.scholarmate.com"> <c:if test="${locale != 'en_US'}">
            <img src="/ressns/images/sm_login/logo_zh_CN.gif" class="mt15" />
          </c:if> <c:if test="${locale == 'en_US'}">
            <img src="/ressns/images/sm_login/logo_en_US.gif" class="mt15" />
          </c:if>
        </a>
      </div>
      <div class="mid">
        <div class="lst">
          <ul>
            <c:if test="${locale != 'en_US'}">
              <li><span style="margin-top:6px;width:162px">登录邮箱/手机号/科研号：</span><input id="userName" name="userName" type="text" class="lst_ipt"
                value="${userName }" /><input type="hidden" id="encodeUserName" name="encodeUserName" value="" /></li>
              <li><span style="margin-left: 46px;margin-top:6px;text-align:right;">密码：</span><input id="password" name="password" type="password" class="lst_ipt" /><input
                type="hidden" id="encodePassword" name="encodePassword" value="" /></li>
            </c:if>
            <c:if test="${locale == 'en_US'}">
              <li><span style="text-align: right;margin-top:6px;width: 37%;">Email/Mobile/Scholar ID：</span><input id="userName" name="userName" type="text"
                class="lst_ipt" value="${userName }" /><input type="hidden" id="encodeUserName" name="encodeUserName"
                value="" /></li>
              <li><span style="margin-top:6px;text-align: right;width: 37%;">Password：</span><input id="password" name="password" type="password" class="lst_ipt" /><input
                type="hidden" id="encodePassword" name="encodePassword" value="" /></li>
            </c:if>
            <c:if test="${needValidateCode=='1'}">
              <c:if test="${locale != 'en_US'}">
                <li><span style="padding-left: 44px;text-align: right;margin-top:6px;">验证码：</span><input id="validateCode" name="validateCode" type="text" class="lst_ipt_code" />
                  <img id="img_checkcode" width="88px;" align="absmiddle" src="/oauth/validatecode.gif" alt="验证码" /> <a
                  style="color: #005eac; text-decoration: underline;" href="javascript:void(0);"
                  onclick="refreshCode();" style="cursor:pointer" onclick="">刷新</a></li>
              </c:if>
              <c:if test="${locale == 'en_US'}">
                <li><span style="margin-top:6px;text-align: right;width: 37.5%;">Verification code：</span><input id="validateCode" name="validateCode" type="text"
                  class="lst_ipt_code" /> <img id="img_checkcode" width="88px;" align="absmiddle"
                  src="/open/validatecode.gif" alt="Verification code" /> <a
                  style="color: #005eac; text-decoration: underline;" href="javascript:void(0);"
                  onclick="refreshCode();" style="cursor:pointer" onclick="">Refresh</a></li>
              </c:if>
            </c:if>
            <div class="clear"></div>
            <label id="status" class="error" 
            <c:if test="${locale != 'en_US'}">
            style="padding-left: 86px;margin-left: 75px;color:red; 
            </c:if>
            <c:if test="${locale == 'en_US'}">
            style="padding-left: 11%;margin-left: 116px;color:red; 
            </c:if>
            <c:if test="${empty msg}">display:none</c:if>">${msg}</label>
            <c:if test="${locale != 'en_US'}">
              <div class="but">
                <input id="login" name="login" onclick="loginform();" value="提交" 
                </c:if>
                <c:if test="${locale == 'en_US'}">
                <div class="but" style="padding: 11px 0 0 37%;">
                    <input id="login" name="login" onclick="loginform();" value="Login" 
                </c:if>
                class="button"  readonly="readonly" unselectable="on"/>
            </div>
            <div class="but">
              <c:if test="${locale != 'en_US'}">
                <a id="register" href="javascript:void(0);" class="fr" style="text-decoration: none">注册帐号</a>
                <a id="repwd" href="javascript:void(0);" class="fr" style="text-decoration: none; margin-right: 7px;">忘记密码？</a>
              </c:if>
              <c:if test="${locale == 'en_US'}">
                <a id="register" href="javascript:void(0);" class="fr" style="text-decoration: none">Register</a>
                <a id="repwd" href="javascript:void(0);" class="fr" style="text-decoration: none; margin-right: 7px;">Forgot
                  password？</a>
              </c:if>
              <!--   <input type="checkbox" class="mr6" /><span>记住登录</span>  -->
            </div>
          </ul>
          <dl class="hint">
            <%--  <dt><span class="sm_ico1"></span></dt>
                <dd>为保障账号安全，请认准本页URL地址必须以www.scholarmate.com开头</dd>       --%>
          </dl>
          <div class="clear"></div>
        </div>
        <div class="txt">
          <c:if test="${locale != 'en_US'}">
            <h2>使用你的科研之友帐号</h2>访问${thirdSysName}
            </c:if>
          <c:if test="${locale == 'en_US'}">
            <h2>Login via ScholarMate</h2>account  ${thirdSysNameUs}
            </c:if>
        </div>
      </div>
    </div>
  </form>
</body>
</html>
