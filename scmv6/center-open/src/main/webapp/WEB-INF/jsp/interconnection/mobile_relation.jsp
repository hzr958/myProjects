<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
  <title>科研之友</title>
  <link href="/resmod/mobile/css/mobile.css" rel="stylesheet" type="text/css">
    <link href="/resmod/css/jquery.validate.css" rel="stylesheet">
      <script type="text/javascript" src="/resmod/js/jquery.js"></script>
      <script type="text/javascript" src="/resmod/js/plugin/jquery-migrate-1.2.1.min.js"></script>
      <script type="text/javascript" src="/resmod/js/plugin/jquery.validate.min.js"></script>
      <script type="text/javascript">

    $(document).ready(function() {
      var domain = document.domain;
      var httpsUrl = "https://" + domain + "/oauth/pc/register/sava";
      $("#regForm").attr("action",httpsUrl);
    	$("#loginForm").validate({
            errorPlacement: function(error,element) {//修改错误的显示位置. targetloca
                    error.appendTo(element.closest(".account-relation_input"));
            },
            rules: {
            	userName : {
                    required: true,
                    checkEmail : true,
                    maxlength: 50
                },
                password: {
                    required: true,
                    minlength: 6,
                    maxlength: 40
                },
                
            },
            messages: {
               
            	userName: {
                    required: "不能为空",
                    checkEmail: "邮箱格式错误",
                    maxlength: "不能大于{0}个字符",
                },
                password: {
                    required: "不能为空",
                    minlength: "不能小于{0}个字符",
                    maxlength: "不能大于{0}个字符"
                },
                
            }
        });
    	
    	$("#regForm").validate({
            errorPlacement: function(error,element) {//修改错误的显示位置. targetloca
                    error.appendTo(element.closest(".account-relation_input"));
            },
            rules: {
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
                renewpassword: {
                    required: true,
                    equalTo:"#newpassword"
                },
                
            },
            messages: {
               
                email: {
                    required: "不能为空",
                    checkEmail: "邮箱格式错误",
                    maxlength: "不能大于{0}个字符",
                    remote: "邮箱已注册"
                },
                newpassword: {
                    required: "不能为空",
                    minlength: "不能小于{0}个字符",
                    maxlength: "不能大于{0}个字符"
                },
                renewpassword: {
                    required: "不能为空",
                    equalTo: "两次密码不相同"
                },
                
                
            }
        });
    	
    	
    	//登录失败提示用户名密码错误
    	if($("#loginStatus").val() === "0" ){
    		var tip = '<label id="password-error" class="error" for="password">邮箱或密码不正确</label>';
    		$("#password").closest(".account-relation_input").append(tip);
    	}
    	
    });
 
  //邮件验证.
    jQuery.validator.addMethod("checkEmail",function(value, element) {
        value = $.trim(value);
        return (value.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) >= 0);
    }, "邮箱格式错误");

	function switchModule(module){
		if(module == "1"){
			$(".account-relation_neck-onload").addClass("account-check_btn");
			$(".account-relation_neck-create").removeClass("account-check_btn");
			$("#loginForm").css("display","block");
			$("#regForm").css("display","none");
		}else if(module == "2"){
			$(".account-relation_neck-onload").removeClass("account-check_btn");
            $(".account-relation_neck-create").addClass("account-check_btn");
			$("#loginForm").css("display","none");
            $("#regForm").css("display","block");
		}
		
	}


</script>
</head>
<body>
  <body>
    <div class="account-relation" id="accountRelation">
      <input type="hidden" name="loginStatus" id="loginStatus" value="${loginStatus }" />
      <div class="account-relation_neck">
        <div class="account-relation_neck-onload account-check_btn" onclick="switchModule(1);" id="accountLogin">帐号登录</div>
        <div class="account-relation_neck-create" onclick="switchModule(2);" id="createAccount">创建新帐号</div>
      </div>
      <div class="account-relation_title">关联帐号后可获取准确的论文推荐</div>
      <div class="account-relation_body">
        <form action="/open/mobilelogin" id="loginForm" method="post">
          <input type="hidden" name="back" value="${back }" /> <input type="hidden" name="token" value="${token }" />
          <input type="hidden" name="zhlastName" value="${zhlastName }" /> <input type="hidden" name="zhfirstName"
            value="${zhfirstName }" />
          <div class="account-relation_input">
            <div class="account-relation_body-item">
              <i class="account-relation_body-account"></i> <input type="text"
                class="account-relation_body-account_input" id="userName" value="${userName }" name="userName"
                maxlength="50" placeholder="请输入登录帐号">
            </div>
          </div>
          <div class="account-relation_input">
            <div class="account-relation_body-item">
              <i class="account-relation_body-password"></i> <input type="password"
                class="account-relation_body-password_input" value="${password }" id="password" name="password"
                maxlength="40" placeholder="请输入登录密码">
            </div>
          </div>
          <div class="account-relation_body-load_btn">
            关联帐号 <input class="account-relation_body-load_btn-input" type="submit" value="  关联帐号" />
          </div>
        </form>
        <!--  下面是注册 -->
        <form action="/oauth/pc/register/sava" id="regForm" style="display: none;" method="post">
          <input type="hidden" name="back" value="${back }" /> <input type="hidden" name="token" value="${token }" />
          <input type="hidden" name="zhlastName" value="${zhlastName }" /> <input type="hidden" name="zhfirstName"
            value="${zhfirstName }" /> <input type="hidden" name="needDynamicOpenId" value="true" />
          <div class="account-relation_input">
            <div class="account-relation_body-item">
              <i class="account-relation_body-account"></i> <input type="text"
                class="account-relation_body-account_input" id="email" name="email" maxlength="50" placeholder="请输入帐号">
            </div>
          </div>
          <div class="account-relation_input">
            <div class="account-relation_body-item">
              <i class="account-relation_body-password"></i> <input type="password"
                class="account-relation_body-password_input" id="newpassword" name="newpassword" maxlength="40"
                placeholder="请输入密码">
            </div>
          </div>
          <div class="account-relation_input">
            <div class="account-relation_body-item">
              <i class="account-relation_body-password"></i> <input type="password"
                class="account-relation_body-password_input" id="renewpassword" name="renewpassword" maxlength="40"
                placeholder="请确认密码">
            </div>
          </div>
          <div class="account-relation_body-load_btn">
            关联帐号 <input class="account-relation_body-load_btn-input" type="submit" value="创建并关联帐号" />
          </div>
        </form>
        <!-- <div class="account-relation_body-prompt">
                <i class="account-relation_body-prompt_icon"></i>
                <span class="account-relation_body-prompt_content">授权关联后表明您已同意《许可协议》</span>
            </div> -->
        </form>
      </div>
    </div>
  </body>
</html>
