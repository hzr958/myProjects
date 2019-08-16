<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> -->
<!-- xmlns="https://www.w3.org/1999/xhtml" -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="/resmod/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="/resmod/mobile/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resmod/js/jquery.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<%-- <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> --%>
<script type="text/javascript" src="/resmod/js/jquery.validate.js"></script>
<script type="text/javascript" src="/resmod/js/link.status.js"></script>
<script type="text/javascript" src="/resmod/js/weixin/wechat.bind.js"></script>
<script type="text/javascript" src="/resmod/js/baseutils/baseutils.js"></script>
  <link rel="stylesheet" type="text/css" href="/resmod/mobile/css/mobile.css">
  <script type="text/javascript" src="/resmod/mobile/js/plugin/new-mobile_Hintframe.js"></script>
  <script type="text/javascript" src="/resmod/js/register/pc.register.js"></script>
<script type="text/javascript">
  $(document).ready(function(){
      Register.checkIp();
      var sUserAgent = window.navigator.userAgent.toLowerCase();
      if(sUserAgent.match(/isapp/i) == "isapp"){
          document.getElementById("openAppBtn").style.display = " none";
          document.getElementsByClassName("account-relation_body-backCover")[0].style.display = "none";
      } 
      document.getElementsByClassName("openpage-inapp_mainPage-goWeb")[0].onclick = function(){
          document.getElementById("openAppBtn").style.display = " none";
          document.getElementsByClassName("account-relation_body-backCover")[0].style.display = "none";
      }
      
      var userAgent = window.navigator.userAgent.toLowerCase();
      if(userAgent.indexOf("smate-android") != -1){
          document.getElementById("openAppBtn").style.display = " none";
          document.getElementsByClassName("account-relation_body-backCover")[0].style.display = "none";
       }

    
  //判断是否为iso系统 
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)){
    //调整app打开按钮的居中
     var oWidth = window.innerWidth;
        document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
        window.onresize = function(){
            var oWidth = window.innerWidth;
            document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
        }
      $("#openAppBtn").show();
    }
  if ("${userName}"!=null) {
  	$("#userName").val("${userName}")
  } 
  closeWeiXinBrower();
  var success ="${success}";
  var msg ="${msg}";
  if(success == "false" && msg != ""){
      newMobileTip(msg);
  };
  if($("#needValidateCode").val() == "1"){
    $("#mobile_login_validateCode").show();
    wechat.bind.refreshcode();
  }
  //手机验证码登录
  var mobileCodeLogin = "${mobileCodeLogin}";
  if( mobileCodeLogin== "true"){
      $("#loginByPhone").click();
  }
});

	function closeWeiXinBrower(){
		if('${success}'==='true'){
			//在生产url会丢失，跳转次数太多，下次调整，可以存放至缓存，
			var url = document.getElementById("url").value;
			
			if(url != null && url != ""){
				setTimeout(window.location.href='${url}',1000);
			}else{
			    setTimeout(window.location.href="/dynweb/mobile/dynshow",1000);
			}
	    }
	};
	function hide(){
		//隐藏提示的信息
		 document.getElementById('status').style.display='none';
		return; 
	};

    function beforLogin(obj){
        if(obj != undefined){//防止重复点击
            BaseUtils.doHitMore(obj , 2000) ;
        }
        if(checkSubmitData()){
          /* if($("#mobileCodeLogin").val() == "true"){
            doMobileCodeLogin();
          }else{
          } */
            doBind();
        }
        
    }


    var switchMethod = function (obj) {
        if($(obj).attr("data-show") == "code"){
          var userName = $("#userName").val();
          var isMobile = checkMobile(userName);   
          if(isMobile){
            $("#mobileNum").val(userName);
          }
          document.getElementsByClassName("phonecode-load_style")[0].style.display="block";
          document.getElementsByClassName("normal-login_style")[0].style.display="none";
          $(obj).html("密码登录");
          $(obj).attr("data-show","password");
          $("#mobileCodeLogin").val("true");
          BaseUtils.changeLocationHref("mobileCodeLogin", "true");
        }else{
          var mobileNum = $("#mobileNum").val();
          if(checkMobile(mobileNum)){
            $("#userName").val(mobileNum);
          }
          document.getElementsByClassName("phonecode-load_style")[0].style.display="none";
          document.getElementsByClassName("normal-login_style")[0].style.display="block";
          $(obj).html("验证码登录");
          $(obj).attr("data-show","code");
          $("#mobileCodeLogin").val("false");
          BaseUtils.changeLocationHref("mobileCodeLogin", "false");
        }
        if($("#needValidateCode").val() == "1"){
          $("#mobile_login_validateCode").show();
          wechat.bind.refreshcode();
        }
        $("#validateCode2").val("");
    }

    function checkMobile (value) {
        value = $.trim(value);
        var pattern=/^[1][3,4,5,7,8][0-9]{9}$/;
        return pattern.test(value);
    }
    
    function checkUserName(userName) {
      if(userName.indexOf("@") != -1){
       var reg = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
       return reg.test(userName);
      }else{
        return checkMobile(userName);
      }
    }
    /**
     * 发送手机验证码
     * @param obj
     * @returns {boolean}
     */
    function  sendMobileLoginCode(obj){
        var mobileNumber = $("#mobileNum").val();
        if($.trim(mobileNumber) == ""){
            newMobileTip("手机号不能为空");
            return ;
        }
        var isMobile = checkMobile(mobileNumber);
        if(!isMobile){
            newMobileTip("手机号格式不正确");
            return ;
        }
        BaseUtils.doHitMore(obj , 2000) ;
        var time = 60 ;
        $.ajax( {
            url :"/oauth/pc/register/ajaxsendmobilelogincode" ,
            type : "post",
            dataType : "json",
            data : {
                'mobileNumber':$.trim(mobileNumber)
            },
            success : function(data) {
                if(data.result == "success"){
                    //防重复点击
                    var htmlContent = $(obj).html();
                    $(obj).html("60s");
                    setTimeout(function () {
                        BaseUtils.doHitMore(obj , 58*1000) ;
                    },2000);
                    var timeout = setInterval(function () {
                        time = time -1;
                        if(time == 0){
                            $(obj).html("重新获取");
                            window.clearInterval(timeout)
                        }else{
                            $(obj).html(time+"s");
                        }
                    },1000);
                    newMobileTip("发送成功");
                }else if(data.result == "notExist"){
                    newMobileTip("手机号未注册，请重新输入");
                }else{
                    newMobileTip("发送失败");
                }
            }
        });

    };
  //打开App的方法  
    function openApp(){
         window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);
    }
  
  
  
    function checkSubmitData(){
      //手机验证码登录
      if($("#mobileCodeLogin").val() == "true"){
        var mobileNumber = $("#mobileNum").val();
        if(BaseUtils.checkIsNull(mobileNumber)){
          newMobileTip("请输入手机号", 2, 2000);
          return false;
        }
        var isMobile = checkMobile(mobileNumber);
        if(!isMobile){
            newMobileTip("请输入正确的手机号", 2, 2000);
            return false;
        }
        if(BaseUtils.checkIsNull($("#mobileCode").val())){
          newMobileTip("请输入短信验证码", 2, 2000);
          return false;
        }
        if($("#needValidateCode").val() == "1" && BaseUtils.checkIsNull($("#validateCode2").val())){
          newMobileTip("请输入验证码", 2, 2000);
          return false;
        }
        
      }else{
        var userName = $("#userName").val();
        //账号密码登录
        if(BaseUtils.checkIsNull(userName)){
          newMobileTip("请输入邮箱/手机号", 2, 2000);
          return false;
        }
        var flag = checkUserName(userName);
        if(!flag){
            newMobileTip("请输入正确的邮箱/手机号", 2, 2000);
            return false;
        }
        if(BaseUtils.checkIsNull($("#password").val())){
          newMobileTip("请输入密码", 2, 2000);
          return false;
        }
        if($("#needValidateCode").val() == "1" && BaseUtils.checkIsNull($("#validateCode2").val())){
          newMobileTip("请输入验证码", 2, 2000);
          return false;
        }
      }
      return true;
    }


    function doBind(){
      $.ajax({
        url :"/open/mobile/ajaxbind" ,
        type : "post",
        dataType : "json",
        async:false ,
        data : {
            'needValidateCode':$.trim($("#needValidateCode").val()),
            'service':$.trim($("#service").val()),
            'userName': $.trim($("#userName").val()),
            'password': encodeHtml($.trim($("#password").val())),
            'validateCode': $.trim($("#validateCode2").val()),
            'url':$.trim($("#url").val()),
            'des3WxOpenId':$.trim($("#des3WxOpenId").val()),
            'isFirst':$.trim($("#isFirst").val()),
            'mobileNum':$.trim($("#mobileNum").val()),
            'mobileCode':$.trim($("#mobileCode").val()),
            'validateCode': $.trim($("#validateCode2").val()),
            'mobileCodeLogin': $.trim($("#mobileCodeLogin").val()),
            'bindType': '0',
            'des3WxUnionId':$.trim($("#des3WxUnionId").val())
        },
        success : function(data) {
            if(data.result == "success"){
              if(BaseUtils.checkIsNull(data.targetUrl)){
                window.location.href = "/dynweb/mobile/dynshow?locale=zh_CN";
              }else{
                window.location.href = data.targetUrl;
              }
            }else if(data.result == "serviceError"){
              newMobileTip("网络异常，请稍后再试", 2, 2000);
              return false ;
            }else{
              //显示验证码
              if(data.needValidateCode == "1"){
                $("#needValidateCode").val("1");
                $("#mobile_login_validateCode").show();
                wechat.bind.refreshcode();
              }
              newMobileTip(data.result, 2, 2000);
              return false ;
            }
        },
        error: function(){
          newMobileTip("网络异常，请稍后再试", 2, 2000);
          return false ;
        }
    });
  }
  function encodeHtml(html){
   var temp = document.createElement ("div");
   (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
   var output = temp.innerHTML;
   temp = null;
   return output.replace(/"/g,"&quot;");
  };
  function callbackKey(event){
    if(event.keyCode == "13"){
      $("#login_btn").click();
    }
  }
</script>
</head>
<body>
<div class="account-relation">
  <div class="account-relation_neck"></div>
  <div class="account-relation_body">
    <form action="/open/wechat/bind" id="loginForm" method="post">
      <input type="hidden" name="mobileCodeLogin" id="mobileCodeLogin" value="false">
      <input type="hidden" id="needValidateCode" name="needValidateCode" value="${needValidateCode}" />
      <input type="hidden" id="isFirst" name="isFirst" value="${isFirst}" />
      <input type="hidden" id="des3WxOpenId" name="des3WxOpenId" value="${des3WxOpenId }" />
      <input type="hidden" id="url" name="url" value="${url }" />
      <input type="hidden" id="des3WxUnionId" name="des3WxUnionId" value="${des3WxUnionId }" />
      <div class="normal-login_style" >
        <div class="account-relation_input" style="margin-bottom: 0px;">
          <div class="account-relation_body-item  account-relation_body-item_bottomNoradius">
            <i class="account-relation_body-account"></i>
            <input type="email"  value="${userName}" id="userName" name="userName" class="account-relation_body-account_input" placeholder="邮箱/手机号">
          </div>
        </div>
        <div  class="account-relation_input"  style="border-top: 1px solid #ddd; margin-bottom: 20px;">
          <div class="account-relation_body-item account-relation_body-item_topNoradius">
            <i class="account-relation_body-password"></i>
            <input type="password"  id="password" onkeydown="callbackKey(event)" name="password" class="account-relation_body-password_input" placeholder="密码">
          </div>
        </div>
      </div>
      <div class="phonecode-load_style" style="display: none;">
        <div class="account-relation_input" style="margin-bottom: 0px;">
          <div class="account-relation_body-item account-relation_body-item_bottomNoradius">
            <i class="account-relation_body-account"></i>
            <input type="text" maxlength="11"  value="${mobileNum}" id="mobileNum" name="mobileNum" class="account-relation_body-account_input" placeholder="手机号">
          </div>
        </div>

        <div  class="account-relation_input" style="border-top: 1px solid #ddd; margin-bottom: 20px;">
          <div class="account-relation_body-item account-relation_body-item_topNoradius">
            <i class="account-relation_body-phonecode"></i>
            <input type="text" maxlength="6" name="mobileCode" id="mobileCode" class="account-relation_body-password_input" placeholder="验证码">
            <div class="account-relation_body-item_getcode" onclick="sendMobileLoginCode(this);">获取验证码</div>
          </div>
        </div>
      </div>

       <div class="account-relation_input account-relation_input-Verification" id="mobile_login_validateCode" style="display:none;">
          <div class="account-relation_body-item" style="width: 38vw;">
            <i class="account-relation_body-phonecode"></i>
            <input type="text" id="validateCode2"  name="validateCode" class="account-relation_body-password_input" placeholder="请输入验证码">
          </div>
          <div class="account-relation_input-Verification_icon">
            <a class="" href="javascript:void(0);" onclick="wechat.bind.refreshcode();"><img src="/open/validatecode.gif" id="img_checkcode"></a>
          </div>
        </div>

        <div class="account-relation_body-prompt" >
          <span class="account-relation_body-prompt_content"  id="loginByPhone" data-show="code" style="margin-left: 10px; display: none;" onclick="switchMethod(this)">验证码登录</span>
          <span class="account-relation_body-prompt_content" style="margin-right: 10px;"><a href="/oauth/mobile/pwd/forget/index" style="color: #ccc;">忘记密码</a></span>
        </div>

        <div class="account-relation_body-load_btn account-relation_body-load-active" id="login_btn" data-show="load" onclick="beforLogin(this);">登录</div>



      <%--     <span class="account-relation_body-prompt_content" style="margin-left: 10px;">
              <c:if test="${!empty wxOpenId}">
                  <a href="/oauth/mobile/register?wxOpenId=" ${wxOpenId } style="color: #ccc;">新用户注册</a>
              </c:if>
              <c:if test="${empty wxOpenId}">
                   <a  href="${domainoauth }/oauth/mobile/register?wxOpenId=${des3WxOpenId }&des3UnionId=${des3WxUnionId}&wxUrl=${url}" style="color: #ccc;">新用户注册</a>
              </c:if>
          </span> --%>


    <div class="account-relation_body-newUser">
      <c:if test="${!empty wxOpenId}">
         <div class="account-relation_body-newUser_btn">
           <a href="/oauth/mobile/register?wxOpenId=" ${wxOpenId } style="color: #ccc;">新用户注册</a>
         </div>
      </c:if>
      <c:if test="${empty wxOpenId}">
        <div class="account-relation_body-newUser_btn" onclick="toRegister()" >新用户注册</div>
       </c:if>
    </div> 
    
    
    
    </form>
  </div>
  <div class="openpage-inapp_mainPage-btn" id="openAppBtn">
       <div class="openpage-inapp_mainPage-btnHeader">
            <div class="openpage-inapp_mainPage-btnHeader_avator"></div>
            <div class="openpage-inapp_mainPage-btnHeader_content">
                <div  class="openpage-inapp_mainPage-btnHeader_contentD1">科研之友APP</div>
                <div  class="openpage-inapp_mainPage-btnHeader_contentD2"> 前往APP获取更快更好地体验</div>
            </div>
        </div>
        <div class="openpage-inapp_mainPage-btnBody">
            <div class="openpage-inapp_mainPage-goAPP" onclick="openApp()">前往APP</div>
            <div class="openpage-inapp_mainPage-goWeb">继续使用网页版</div>
        </div>
  </div>
</div>
<div class="account-relation_body-backCover"></div>
</body>
</html>
