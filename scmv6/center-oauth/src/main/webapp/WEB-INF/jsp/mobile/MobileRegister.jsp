<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${resmod}/js/link.status.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
  <script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>

<script type="text/javascript">
$(document).ready(function() {
    Register.checkIp();
	var msg = $("#msg1").val();
	if(msg != "" && msg != null && typeof(msg) != "undefined" && msg!="邮箱注册信息验证"){
		$("#emailMsg").html("<span style='color:red'>"+msg+"</span>");
	}else{
		$("#emailMsg").html("<span>此邮箱将会用来登录及与你联系</span>");
	}
	//SCM-8832
	$("#regForm").validate({
		errorPlacement : function(error, element) {//修改错误的显示位置.
			element.parent().find("p.msgTip").html("");
			error.appendTo(element.parent().find("p.msgTip"));
			$(error).css("line-height","5px");
			$("#mobileRegister").enabled();//释放按钮
		},
		rules : {
			newpassword : {
				required : true,
				minlength : 6,
				maxlength : 40,
				success: function(){
				    $("#passwordMsg").html("<span>请输入你的密码</span>");
				}
			},
			zhfirstName : {
				required : true,
				firstnameCheck:true,
				maxlength : 61,
			    success: function(){
			        $("#zhfirstNameMsg").html("<span>请输入你的名</span>");
                }
			},
			zhlastName : {
				required : true,
				lastnameCheck:true,
				maxlength : 61,
			    success: function(){
			        $("#zhlastNameMsg").html("<span>请输入你的姓</span>");
                }
			},
			email : {
                required:true,
                emailCheck:true,
                remote:"/oauth/register/ajaxCheckedUserName",
                maxlength:50,
                success:function(){
                    $("#emailMsg").html("<span>此邮箱将会用来登录及与你联系</span>");
                }
            },
            agreement : {
                required:true,
                agreementCheck:true,
                success:function(){
                    $("#aggreementMsg").html("<span></span>");
                }
                
            },
            mobileNumber:{
              required: true,
              mobileCheck: true,
              remote: "/oauth/pc/register/ajaxcheckmobile",
            },
            mobileVerifyCode:{
              required: true,
              minlength:6,
              maxlength:6,
              remote:{
                  url: "/oauth/pc/register/ajaxcheckmobilecode",
                  type: "post",
                  dataType: "json",
                  data: {
                      mobileVerifyCode: function() {
                      return $("#mobileVerifyCode").val();
                      },
                      mobileNumber: function() {
                          return $("#mobileNumber").val();
                      }
               }
              }
          }
		},
		messages : {
			zhfirstName : {
				required : "名不能为空",
				maxlength : "不能大于61个字符"
			},
			zhlastName : {
				required : "姓不能为空",
				maxlength : "不能大于61个字符"
			},
			email : {
				required : "邮箱不能为空",
				email : "邮箱格式错误",
				maxlength : "不能大于50个字符",
				remote : "邮箱已存在"
			},
			newpassword : {
				required : "密码不能为空",
				minlength : "不能小于6个字符",
				maxlength : "不能大于40个字符"
			},
			agreement : {
				required : "必须同意用户协议和隐私政策"
			},
             mobileNumber: {
                required: "手机号不能为空",
                mobileCheck: "手机号格式不正确",
                remote: "手机号已注册"
            },
            mobileVerifyCode: {
              required: "验证码不能为空",
              minlength:"验证码长度为6",
              maxlength:"验证码长度为6",
              remote:"验证码错误或已过期"
          },
		},
		submitHandler : function(form) {
			formsubmit(form);
		}

	});
	//邮件验证.
	jQuery.validator.addMethod("emailCheck",function(value, element) {
		value = $.trim(value.toLowerCase());
        var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
        return this.optional(element)||(checkEmail.test(value));
    }, "邮箱格式错误");
	//同意协议验证.
	jQuery.validator.addMethod("agreementCheck",function(value, element) {
		return $("#agreement").attr("checked") == "checked";
    }, "");
	//姓名验证
	jQuery.validator.addMethod("firstnameCheck",function(value, element){
        value = $.trim(value);
        var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
        return pattern.test(value);
    }, "名不能包含特殊字符");
	//姓名验证
    jQuery.validator.addMethod("lastnameCheck",function(value, element){
        value = $.trim(value);
        var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
        return pattern.test(value);
    }, "姓不能包含特殊字符");

	});
    //手机号验证.
    jQuery.validator.addMethod("checkMobileNumber",function(value, element) {
        value = $.trim(value.toLowerCase());
        var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
        return this.optional(element)||(checkEmail.test(value));
    }, "请输入正确的手机号码");
	var formsubmit = function(form) {
		$("#mobileRegister").disabled();
		$("#regForm").attr("action", '/oauth/register/save');
		form.submit();
		//重新返回时置空
		return ;
		setTimeout(function() {
			$("#mobileRegister").enabled();
			$("#email,#zhfirstName,#zhlastName,#newpassword,#firstName,#lastName").val("");
		}, 1000);
	}
//电话验证
	jQuery.validator.addMethod(
	    "mobileCheck",
	    function(value, element){
	        value = $.trim(value);
	        var pattern=/^[1][3,4,5,6,7,8,9][0-9]{9}$/;
	        return pattern.test(value);
	    }, "手机号无效"
	);
	
	/**
	 * 发送手机验证码
	 * @param obj
	 * @returns {boolean}
	 */
	 function sendMobileCode(obj){
	    var mobileNumber = $("#mobileNumber").val();
	    var result = $("#regForm").validate().element($("#mobileNumber"));
	    if(!result) return ;
    	$.ajax( {
            url :"/oauth/pc/register/ajaxsendmobilecode" ,
            type : "post",
            dataType : "json",
            data : {
                'mobileNumber':$.trim(mobileNumber)
            },
            success : function(data) {
                if(data.result == "success"){
                    scmpublictoast("发送成功",1500);
                  //防重复点击
                    var time = 60 ;
                    var htmlContent = $(obj).val();
                    $(obj).val("60s");
                    BaseUtils.doHitMore(obj , time*1000) ;
                    var timeout = setInterval(function () {
                        time = time -1;
                        if(time == 0){
                            $(obj).val("重新获取");
                            window.clearInterval(timeout)
                        }else{
                            $(obj).val(time+"s");
                        }
                    },1000);
                }else{
                    scmpublictoast("发送失败",1500);
                    return;
                }
            }
        });
	};
</script>
</head>
<body class="white_bg" style="margin: 0px !important;">
  <div class="per_inf"></div>
  <div class="con_1">
    <input type="hidden" id="msg1" value="${actionMessages[0]}">
    <form id="regForm" method="post">
      <input type="hidden" id="autoLogin" name="autoLogin" value="true" /> <input type="hidden" id="wxOpenId"
        name="wxOpenId" value="${wxOpenId}" /> <input type="hidden" id="des3UnionId" name="des3UnionId"
        value="${des3UnionId}" /> <input type="hidden" id="wxUrl" name="wxUrl" value="${wxUrl }" /> <input
        type="hidden" id="sysType" name="sysType" value="${sysType}" /> <input type="hidden" id="service"
        name="service" value="${service}" />
      <ul class="it_box">
        <li><input type="text" name="zhlastName" id="zhlastName" style="color: #333; padding-left: 0.3rem;"
          value="${zhlastName}" placeholder="姓" maxlength="20" />
          <p class="msgTip" id="zhlastNameMsg" style="line-height: 0px !important;">
            <span>请输入你的姓</span>
          </p></li>
        <input type="hidden" name="lastName" id="lastName" value="${lastName }" />
        <li><input type="text" name="zhfirstName" id="zhfirstName" style="color: #333; padding-left: 0.3rem;"
          value="${zhfirstName}" placeholder="名" maxlength="40" />
          <p class="msgTip" id="zhfirstNameMsg" style="line-height: 0px !important;">
            <span>请输入你的名</span>
          </p></li>
        <input type="hidden" name="firstName" id="firstName" value='${firstName }' />
        <li><input type="email" name="email" id="email" style="color: #333; padding-left: 0.3rem;"
          value="${email }" placeholder="邮箱" maxlength="50" />
          <p class="msgTip" id="emailMsg" style="line-height: 0px !important;"></p></li>
        <li><input type="password" name="newpassword" style="color: #333; padding-left: 0.3rem;" id="newpassword"
          placeholder="密码" minlength="6" maxlength="40" />
          <p class="msgTip" id="passwordMsg" style="line-height: 0px !important;">
            <span>请输入你的密码</span>
          </p></li>
        <div id="mobile_register_div" style="display: none;">
         <li><input type="text" name="mobileNumber" style="color: #333; padding-left: 0.3rem;" id="mobileNumber"
          placeholder="手机号码"  maxlength="11" />
          <p class="msgTip" id="mobileNumberMsg" style="line-height: 0px !important;">
            <span>请输入你的手机号码</span>
          </p></li>
         <li><input type="text" name="mobileVerifyCode" style="width:70%; color: #333; padding-left: 0.3rem;" id="mobileVerifyCode"
          placeholder="验证码" maxlength="6" />
          <input type="button" value="获取验证码" style="width: 25%; font-size: 11px; margin-top:0px; height: 2.55rem;" onclick="sendMobileCode(this);">
          <p class="msgTip" id="mobileVerifyCodeMsg" style="line-height: 0px !important;">
            <span>请输入手机验证码</span>
          </p></li>
        </div>
        <!-- 同意用户协议和隐私政策 -->
        <li><input type="checkbox" name="agreement" id="agreement" style="-webkit-appearance: checkbox;"  checked="checked"/>
          <span>同意用户协议和隐私政策</span>
          <p class="msgTip" id="aggreementMsg" style="line-height: 0px !important;">
        </li>
        <li style="height:4rem;" id="mobile_bottom_height"></li>
      </ul>
      <a href="javascript:void();"><input type="submit" value="注册" id="mobileRegister"></a>
    </form>
  </div>
</body>
<script type="text/javascript">
window.onload=function(){
    var zhfirstname=document.getElementById("zhfirstName");
	var zhlastname=document.getElementById("zhlastName");
	var firstname=document.getElementById("firstName");
	var lastname=document.getElementById("lastName");
	zhfirstname.onfocus=function(){firstname.value = ""};
	zhlastname.onfocus=function(){lastname.value = ""};
	zhfirstname.onblur=function(){parsePinyin("zhfirstName","firstName")};
	zhlastname.onblur=function(){parsePinyin("zhlastName","lastName")};
	function parsePinyin(source,target){
		var  sourceInput = document.getElementById(source);
		 var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
	     if(!pattern.test(sourceInput.value)){
	    	 return;
	     }
		if(sourceInput == null||sourceInput.value.trim() === ""){
			return ;
		}
		$.ajax( {
			url :"/psnweb/pinyin/ajaxparse" ,
			type : "post",
			dataType : "json",
			data : {
				word:sourceInput.value,
				wordType:target
			},
			success : function(data) {
				if(data.result !== ""){
					document.getElementById(target).value = data.result;
				}else{
					document.getElementById(target).value = $.trim(sourceInput.value);
				}
			},
			error : function(){
				document.getElementById(target).value = $.trim(sourceInput.value);
			}
		});
	}
}

</script>
</html>
