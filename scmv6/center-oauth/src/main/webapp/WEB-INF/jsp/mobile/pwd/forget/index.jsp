<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="/resmod/mobile/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resmod/js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="/resmod/js/link.status.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	
		(function(emailId, retrieve_pwd_btn) {
			$(emailId).blur(function(){
				if($(emailId).val().trim()!=""){
					$("#msg").html("<br>");
				}else{
					$("#msg").text("输入登录帐号或首要邮件");
					$("#msg").css("color","#333");
				}
			});
			$("#retrieve_pwd_btn").click(function() {
				$("#retrieve_pwd_btn").disabled();
				checkedEmail();
			});
			var checkedEmail = function(){
				var email = $(emailId).val().trim();
				var reg = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i;
				if (reg.test(email) && email != "") {
					$.ajax({
						type : "post",
						url : "/oauth/pwd/ajaxcheckedemail",
						data : {
							email : email
						},
						dataType : "json",
						success : function(data) {
							if (data) {
								$("#emailFrom").submit();
								setTimeout(function(){
									$("#retrieve_pwd").val("");
									$("#retrieve_pwd_btn").enabled();
								},1000);
							} else {
								$("#msg").text("帐号或首要邮件不存在");
								$("#msg").css("color","red");
								$("#retrieve_pwd_btn").enabled();
							}
						},
						error : function() {
							$("#msg").text("帐号或首要邮件检验失败");
							$("#msg").css("color","red");
							$("#retrieve_pwd_btn").enabled();
						}
					});
				} else if(email==""){
					$("#msg").text("帐号或首要邮件不能为空");
					$("#msg").css("color","red");
					$("#retrieve_pwd_btn").enabled();
				}else{
					$("#msg").text("帐号或首要邮件格式不正确");
					$("#msg").css("color","red");
					$("#retrieve_pwd_btn").enabled();
				}
			} 
		})("#retrieve_pwd", "#retrieve_pwd_btn");
	});
	function check(){
        var msg = $("#msg").text(); 
        var email = $("#retrieve_pwd").val().trim();
        var reg = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i;
        if (reg.test(email) && email != "" && msg != "输入登录帐号或首要邮件") {
        	$("#msg").css("color","black");
        	$("#msg").html("<br>");    	
        }
	}
</script>
</head>
<body class="white_bg">
  <div class="fpd_bg"></div>
  <div class="con_1">
    <form action="/oauth/mobile/pwd/forget/sendemail" method="post" id="emailFrom">
      <h2>重新获取密码：</h2>
      <ul class="it_box">
        <li><input id="retrieve_pwd" name="email" type="email" placeholder="帐号/邮件"
          style="color: #333; padding-left: 0.3rem;" oninput="check();" maxlength="50"/>
          <p id="msg">输入登录帐号或首要邮件</p></li>
      </ul>
      <!-- <h3>只记得ISIS帐号？</h3>
			<em>请在上面输入isis帐号中使用的邮件地址来<br>获取科研之友的帐号与密码 -->
      </em> <input type="button" id="retrieve_pwd_btn" value="下一步">
    </form>
  </div>
</body>
</html>