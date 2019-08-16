<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 登录弹框 -->
<script type="text/javascript">
var locale='${locale}';
</script>
<script type="text/javascript" src="${resmod}/js/index/pc.scm.index.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmemail_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript" src="${resmod }/js/login/scm.pc.login_box.js"></script>
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<style>
#login-error {
  background: #FEEEEB;
  border: 1px solid #F15532;
  color: #F15533;
  line-height: 24px;
  margin-top: 12px;
  box-sizing: border-box;
  font-family: PingFangSC-Light;
  font-size: 14px;
  padding: 5px;
  text-align:center;
  width: 340px;
  word-break: break-word;
}
.searchbox__main{
    /* ww*/
   /* */
}


.sina_login_icon{
    cursor: pointer;
    margin: 0px 4px;
    background: url(/resmod/images_v5/third_login/weibo_login.svg);
    background-repeat: no-repeat;
    background-position: 0px 0px;
    width: 26px;
    height: 26px;
    background-size: 26px 26px;
}
</style>
<!-- 登录弹框 -->
<c:if test="${locale eq 'zh_CN'}">
  <c:set var="scm_watchword" value="连接，让科研更成功" />
  <c:set var="scm_login_title" value="密码登录" />
  <c:set var="scm_code_login_title" value="验证码登录" />
  <c:set var="scm_code" value="验证码" />
  <c:set var="scm_input_mobile" value="输入手机号码" />
  <c:set var="scm_get_code" value="获取验证码" />
  <c:set var="scm_login_name" value="登录邮箱/手机号/科研号" />
  <c:set var="scm_login_password" value="密码" />
  <c:set var="scm_forgetpwd" value="忘记密码" />
  <c:set var="scm_use_others" value="使用其他帐号登录" />
  <c:set var="scm_qq_login" value="QQ登录" />
  <c:set var="scm_wechat_login" value="微信登录" />
  <c:set var="scm_sina_login" value="微博登录" />
  <c:set var="scm_login_btn" value="登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录" />
  <c:set var="scm_login_error_tip" value="帐号或密码不正确" />
  <c:set var="scm_regist" value="免费注册" />
</c:if>
<c:if test="${locale ne 'zh_CN' }">
  <c:set var="scm_watchword" value="Innovation made effective" />
  <c:set var="scm_login_title" value="By Password" />
  <c:set var="scm_code_login_title" value="By Mobile Code" />
  <c:set var="scm_input_mobile" value="Enter mobile no." />
  <c:set var="scm_get_code" value="Mobile code" />
  <c:set var="scm_login_name" value="Email/Mobile/Scholar ID" />
  <c:set var="scm_code" value="Mobile code" />
  <c:set var="scm_login_password" value="Password" />
  <c:set var="scm_forgetpwd" value="Forgot password?" />
  <c:set var="scm_use_others" value="Login with" />
  <c:set var="scm_qq_login" value="QQ Login" />
  <c:set var="scm_wechat_login" value="WeChat Login" />
  <c:set var="scm_sina_login" value="Weibo Login" />
  <c:set var="scm_login_btn" value="Login" />
  <c:set var="scm_login_error_tip" value="Incorrect account or password." />
  <c:set var="scm_regist" value="Register" />
</c:if>
<script type="text/javascript">
$(function(){
    $("#login_toast_box").click(function(event){
        //SCM-17505
        event.stopPropagation();
    });
    var domain = document.domain;
    var httpsUrl = "https://" + domain + "/oauth/login";
    $("#loginForm").attr("action",httpsUrl);
    Register.checkIp();
    Register.listenerMobileNum();
})
//SCM-8877验证码
var refreshCode = function(){
    // new Date() IE浏览器：获取的时间是Fri May 18 2018 09:58:09 GMT+0800 (中国标准时间)
    document.getElementById("img_checkcode").src ="/oauth/validatecode.gif?date="+new Date().getTime();
}
function codeLogin(obj){
    $("#login-error").css("display","none");
    $("#showValidateCodeDiv").css("display","none");
    $("#account_login_select").removeClass("sign-in__header-title__selected");
    $("#code_login_select").addClass("sign-in__header-title__selected");
    $("#mobileCodeLogin").val("true");
    document.getElementById("loadin-function_container1").style.display="none";
    document.getElementById("loadin-function_container2").style.display="block";
}
function accountLogin(obj){
    $("#login-error").css("display","none");
    $("#code_login_select").removeClass("sign-in__header-title__selected");
    $("#account_login_select").addClass("sign-in__header-title__selected");
    $("#mobileCodeLogin").val("false");
    document.getElementById("loadin-function_container2").style.display="none";
    document.getElementById("loadin-function_container1").style.display="block";
}
</script>
<div class="bckground-cover" style="display: none; z-index: 1111111; left: 0px;" id="login_toast_box">
  <div class="toast_load-container" id="toast_load-container_div">
    <div class="sign-in__container" style="position: static;">
      <div class="toast_load-container_title">${scm_watchword }
        <i class="list-results_close toast_load-container_close positionfix-cancle" onclick="ScmLoginBox.closeBox();"></i>
      </div>
      <div class="sign-in__header" style="background: #fff;">
        <div class="sign-in__header-container" style="justify-content:space-around;">
          <div class="sign-in__header-title sign-in__header-title__selected" id="account_login_select" onclick="accountLogin(this);">
            <span class="ml10" style="margin: 0px;">${scm_login_title }</span>
          </div>

          <div class="sign-in__header-title"  id="code_login_select" onclick="codeLogin(this)">
            <span class="ml10" style="margin: 0px;">${scm_code_login_title}</span>
          </div>
        </div>
      </div>
      <div class="sign-in__body" style="background: #fff;">
        <form action="/oauth/login" id="loginForm" method="post">
          <input type="hidden" value="SNS" id="sys" name="sys"> 
          <input type="hidden" name="needValidateCode" id="needValidateCode" value="${needValidateCode }"> 
          <input type="hidden" value="" id="login_target_url" name="service"> 
          <input type="hidden" value="true" id="login_box_refresh_currentPage">
          <div id="login-error" class="form-error notice notice-error" style="display: none;">
            <span class="icon-notice icon-error"></span> <span class="notice-descript">${scm_login_error_tip }</span>
          </div>
          <div id="loadin-function_container1">
            <div class="sign-in__account sign-in__num-box" id="user_name_inp_div">
              <i class="sign-in__account-tip"> </i> <input type="text" class="sign-in__num-input" name="userName"
                 id="username" value="" maxlength="50" placeholder="${scm_login_name }" style="font-size: 14px;color: rgb(51, 51, 51);">
            </div>
            <div class="sign-in__password sign-in__num-box">
              <i class="sign-in__password-tip"> </i> <input type="password" class="sign-in__num-input" name="password"
                  id="password" maxlength="40" value="" placeholder="${scm_login_password }" style="font-size: 14px;color: rgb(51, 51, 51);">
            </div>
          </div>

          <div style="display: none;" id="loadin-function_container2">
            <div class="sign-in__account sign-in__num-box" style="height:38px; margin: 32px 0px 10px 0px;">
              <i class="sign-in__account-tip"></i> <input type="text" class="sign-in__num-input" name="mobileNum" id="mobileNum" value="" maxlength="11" placeholder="${scm_input_mobile}" style="padding-left: 4px;">
              <input type="hidden" id="mobileCodeLogin" name="mobileCodeLogin" value="false">
            </div>
            <div class="targetloca tip__container-flag" style="display: flex; flex-direction: column; width: 100%; padding-top: 2px; margin: 12px 0px 16px 0px;">
              <div class="targetloca-input_container" style="display: flex; align-items: center; position: relative;">
                <div class="phone-Verification_icon  phone-Verification_icon-tip" style="height: 38px; background-position: 0px -2px;"></div>
                <input type="text" value="" id="mobileCode" name="mobileCode" maxlength="6" placeholder="${scm_code}" class="phone-Verification_icon-input" style="height: 38px;">
                <div class="phone-Verification_code" id="mobileNumBtn" type="2000" onclick="Register.sendMobileLoginCode(this);">${scm_get_code}</div>
              </div>
            </div>
          </div>

            <div class="sign-in__Checkcode" id="showValidateCodeDiv" style="display:none">
              <div class="sign-in__password sign-in__num-box targetloca-input_container" style="width: 180px;margin: 0px 0px 16px 0px;">
                  <i class="phone-Verification_icon  phone-Verification_icon-tips text_login-tip" style="border: none; background-position: -1px -2px; width: 46px;"></i> 
                  <input type="text" class="sign-in__num-input" name="validateCode" id="validateCode" maxlength="40" value="" placeholder="校验码" style="color: rgb(51, 51, 51);width: 125px!important;">
              </div>
              <div class="sign-in__Checkcode-avator" style="width: 100px;" onclick="refreshCode();">
                <img src="" id="img_checkcode"  style="width: 100%; height: 100%;"/>
              </div>
              <i class="material-icons" style="margin-bottom: 16px;" onclick="refreshCode()">cached</i>
          </div>
          
          <div class="sign-in__btn" onclick="ScmLoginBox.beforeSubmit();">${scm_login_btn }</div>
          <div class="sign-in__remember">
            <div class="sign-in__forget">
              <a class="blue1 fr" onclick="window.open('/oauth/pwd/forget/index?returnUrl=/oauth/index');">${scm_forgetpwd }</a>
            </div>
            <div class="sign-in__forget">
              <a class="blue1 fr" onclick="ScmLoginBox.toRegister();">${scm_regist }</a>
            </div>
          </div>
          <div class="sign-in__footer">
            <div class="sign-in__footer-title">${scm_use_others }</div>
            <div class="sign-in__footer-title__container">
              <i class="change-load__tip1" title="${scm_qq_login }" onclick="ScmLoginBox.loginQQ();"></i> <i
                class="change-load__tip2" title="${scm_wechat_login }" onclick="ScmLoginBox.loginWechat();"></i>
              <%-- <i class="sina_login_icon" title="${scm_sina_login }" onclick="ScmLoginBox.weiboLogin()"></i> --%>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
