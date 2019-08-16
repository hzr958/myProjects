<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html style="height: 100%;">
<head>
<meta charset="utf-8">
<meta name="keywords" content='<s:text name="page.seo.index.Keywords"/>' />
<meta name="description" content='<s:text name="page.seo.index.description"/>' />
<meta name="robots" content='<s:text name="page.seo.index.robots"/>' />
<meta name="renderer" content="ie-stand">
<title><s:text name="page.seo.index.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet">
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var locale='${locale}';
</script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.validate.min.js"></script>
  <script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmemail_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript" src="${resmod}/js/index/pc.scm.index.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript" src="${resmod }/js/login/scm.pc.login_box.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<style>
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
<script type="text/javascript">
var searchTypeTip="检索论文、专利、专家、机构...";
var searchTypeTip_en_US="Search publications, patents, researchers, organization...";
var searchType="<s:text name='page.main.search' />";
var searchType2="<s:text name='page.main.search2'/>";
var searchType3="<s:text name='page.main.search3'/>";
var searchType4="<s:text name='page.main.search4'/>";
$(document).ready(function() {
	var constheight = window.screen.height;
    Register.listenerMobileNum();
 	if(constheight<1048){
	    document.getElementById("firstpage_container").style.display="block";
	}else{
	    document.getElementById("firstpage_container").style.display="flex";
	} 
    if(($(window).width() - document.getElementsByClassName("sign-in__container")[0].offsetLeft - document.getElementsByClassName("register_wrap")[0].offsetLeft - 400) < 240){
        var rightshow = document.getElementsByClassName("error_message-prompt");
        for(var i= 0; i < rightshow.length; i++){
            if( rightshow[i].parentNode.querySelector(".setting-list_page-item_hidden")){
                rightshow[i].parentNode.querySelector(".setting-list_page-item_hidden").classList.remove("setting-list_page-item_hidden");
                rightshow[i].parentNode.querySelector(".error_message-rightprompt").classList.add("setting-list_page-item_hidden");
            }
        }
    }

    window.onresize = function(){
	    if(document.body.clientHeight<960){
	        document.getElementById("firstpage_container").style.display="block";
	    }else if(document.body.clientHeight>960){
	        document.getElementById("firstpage_container").style.display="flex";
	    }
	    if((($(window).width() - document.getElementsByClassName("sign-in__container")[0].offsetLeft - document.getElementsByClassName("register_wrap")[0].offsetLeft - 400) < 240)
	             ||(document.getElementsByClassName("register_wrap")[0].offsetLeft < 240)){
	        var rightshow = document.getElementsByClassName("error_message-rightprompt");
	        for(var i= 0; i < rightshow.length; i++){
	            if( rightshow[i].parentNode.querySelector(".setting-list_page-item_hidden")){
	                rightshow[i].parentNode.querySelector(".setting-list_page-item_hidden").classList.remove("setting-list_page-item_hidden");
	                rightshow[i].parentNode.querySelector(".error_message-rightprompt").classList.add("setting-list_page-item_hidden");
	            }
	        }
	    }
	}
	
	$('.checkbox').on('click',function() {
		if ($(this).siblings("input[type='checkbox']").attr('checked')) {
			$(this).removeClass('cur');
			$(this).siblings("input[type='checkbox']").removeAttr('checked')
		} else {
			$(this).addClass('cur');
			$(this).siblings("input[type='checkbox']").attr('checked', 'checked')
		}
	});
	try{
	  $(".sign-in__Checkcode-avator").mouseover(function(){
	    $(this).parent().find(".error_message-prompt").css("display","block");
	  })
	  $(".sign-in__Checkcode-avator").mouseleave(function(){
        $(this).parent().find(".error_message-prompt").css("display","none");
      })
	  
    }catch(err){
      console.log(err)
      console.log(err.message);
    }
	var foculist = document.getElementsByClassName("text_login");
	for (var i = 0; i < foculist.length; i++) {
		foculist[i].onfocus = function(obj) {
			if (this.value == "") {
				this.style.color = "#ccc";
			}
			if($(this).hasClass("text_login-flex")){
			    $(this).addClass("text_login-flexborder");
			}else{
			    if($(this).closest(".tip__container-flag").find(".text_login-tip").length >0){
                    $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.border = "1px solid #288aed";
                    $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.borderRightStyle = "none";
                }
			}
		}
		foculist[i].onblur = function() {
		    if($(this).hasClass("text_login-flex")&&$(this).hasClass("text_login-flexborder")){
		        $(this).removeClass("text_login-flexborder");
            }else{
              if($(this) instanceof jQuery){
		        if($(this).closest(".tip__container-flag").find(".text_login-tip").length >0){
                    $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.border = "1px solid #ddd";
                    $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.borderRightStyle = "none";
                }
              }
			}
		}
	};

	var locale = '${locale}';
	$(":input").blur(function() {
		$(this).val($.trim($(this).val()));
	});
	$("#regForm").validate({
        //debug:true,
		errorPlacement: function(error,element) {//修改错误的显示位置. targetloca
		  $(element).closest(".targetloca-input_container").addClass("targetloca-input_container-tip");
		  $(element).closest(".targetloca").find(".error_message-prompt_detail").html(error.html());
		  $(element).closest(".targetloca").find(".error_message-prompt").css("display","block");
		},
        success: function(label ,element) {
            $(element).closest(".targetloca-input_container").removeClass("targetloca-input_container-tip");
            $(element).closest(".targetloca").find(".error_message-prompt_detail").html("");
            $(element).closest(".targetloca").find(".error_message-prompt").css("display","none");
        },
		rules: {
			name: {
				required: true,
				maxlength: 61
			},
			<s:text name='page.index.show.lastName' />: {
				required: true,
				nameCheck:true,
				maxlength: 20
			},
			<s:text name='page.index.show.firstName' /> : {
				required: true,
				nameCheck:true,
				maxlength: 40
			},
			<s:text name='page.index.show.lastNameNoPhone' />: {
                required: true,
                nameCheck:true,
                maxlength: 20
            },
            <s:text name='page.index.show.firstNameNoPhone' /> : {
                required: true,
                nameCheck:true,
                maxlength: 40
            },
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
            },
			checkedall: {
				required: true
			}
		},
		messages: {
			name: {
				required: "<s:text name='register.name.zh' /><s:text name='register.required' />",
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			zhlastName: {
				required: "<s:text name='zhfirst.register.required.not.blank'/>",
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			zhfirstName: {
				required: "<s:text name='zhlast.register.required.not.blank'/>",
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			lastName: {
				required: "<s:text name='last.register.required.not.blank' />",
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			firstName: {
				required: "<s:text name='first.register.required.not.blank' />",
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			email: {
				required: "<s:text name='register.email' /><s:text name='register.required' />",
				checkEmail: "<s:text name='register.isemail' />",
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />"),
				remote: "<span class='logintitle'><s:text name='register.email.exists' /></span>"
			},
           mobileNumber: {
                required: "<s:text name='mobile.register.required.not.blank' />",
                    mobileCheck: "<s:text name='page.auth.mobile.warn1' />",
                    remote: "<s:text name='page.auth.mobile.warn2' />"
            },
            mobileVerifyCode: {
                required: "<s:text name='mobileVerifyCode.register.required.not.blank' />",
                    minlength:"<s:text name='page.auth.mobileVerifyCode.warn1' />",
                    maxlength:"<s:text name='page.auth.mobileVerifyCode.warn1' />",
                    remote:"<s:text name='page.auth.mobileVerifyCode.warn2' />"
            },
			newpassword: {
				required: "<s:text name='register.password' /><s:text name='register.required' />",
				minlength: jQuery.validator.format("<s:text name='register.minlength' />"),
				maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			checkedall: {
				required: "<s:text name='register.iris6' />"
			}
		}
	});
	ScmMaint.searchSomeOneBind();
	$("#show_select").click(function(e) {
		var _obj = $("#home_search_items");
		if (_obj.is(":hidden")) {
			_obj.show();
		} else {
			_obj.hide();
			return;
		}
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {//IE
			window.event.cancelBubble = true;
		}
		$(document).click(function() {
			_obj.hide();
		});
	});
	if ("en_US" == locale) {
		ScmMaint.searchWater(searchTypeTip_en_US);
	} else {
		ScmMaint.searchWater(searchTypeTip);
	}
	Register.checkIp();
  //初始化验证码
  if("${needValidateCode}" == '1'){
      refreshCode();
  }
});

jQuery.validator.addMethod("checkEmail", function(value, element, param){
	value = $.trim(value.toLowerCase());
	var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
    return this.optional(element)||(checkEmail.test(value)); 
}, "邮件格式不正确！");
//姓名验证
jQuery.validator.addMethod(
        "nameCheck",
        function(value, element){
            value = $.trim(value);
            var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
            return pattern.test(value);
        }, "<s:text name='register.invalidName' />"
        );

//电话验证
jQuery.validator.addMethod(
    "mobileCheck",
    function(value, element){
        value = $.trim(value);
        var pattern=/^[1][3,4,5,6,7,8,9][0-9]{9}$/;
        return pattern.test(value);
    }, "手机号无效"
);
function changeloadbox1(){
    $("#mobileCodeLogin").val("true");
    document.getElementById("loadin-function_container1").style.display="none";
    document.getElementById("loadin-function_container2").style.display="block";
}
function changeloadbox2(){
    $("#mobileCodeLogin").val("false");
    document.getElementById("loadin-function_container2").style.display="none";
    document.getElementById("loadin-function_container1").style.display="block";
}
function loginQQ() {
	window.open('/oauth/qq/login',document.location.href,
	'height=560,width=1024,top=220,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
}
function loginWeChat(){
	document.location.href="/oauth/wechat/login";
}
function beforeSubmit() {
	ScmIndex.login();
    if($("#mobileCodeLogin").val() == "true"){
        var login = Register.checkLoginCode();
    }else{
        var flag =$("#regForm").valid();
        if(flag)
        $("#loginForm").submit();
    }
}

function showAppDownloadQRCode(){
  var runEnv = "${runEnv}";
  if(runEnv == "test"){
    qrcodeSrc = "/resmod/smate-pc/img/app_download/test_app_download.png";
    $("#app_download_qrcode").attr("src", qrcodeSrc);
  }else if(runEnv == "uat"){
    qrcodeSrc = "/resmod/smate-pc/img/app_download/uat_app_download.png";
    $("#app_download_qrcode").attr("src", qrcodeSrc);
  }
}
function refreshCode() {
  document.getElementById("img_checkcode").src ="/oauth/validatecode.gif?date="+new Date().getTime();
  $("#show_checkcode").css("display","flex");
};
function hideTip(idName){
  if($.trim($("#"+idName).val()).length > 0){
    switch(idName){
      case 'username':
        /* $("#username_tip").hide(); */
        $("#username_tip").find(".error_message-prompt").hide();
        break;
      case 'password':
        /* $("#username_tip").hide();
        $("#password_tip").hide(); */
        $("#username_tip").find(".error_message-prompt").hide();
        $("#password_tip").find(".error_message-prompt").hide();
        break;
      case 'needValidateCode':
        /* $("#needValidateCode_tip").hide(); */
        $("#needValidateCode_tip").find(".error_message-prompt").hide();
        break;
    }
  }
}
</script>
</head>
<body id="firstpage_container"
  style="height: 100%; display: flex; flex-direction: column; justify-content: space-between; width: 100%;">
  <div>
    <div class="header">
      <div class="hd_wrap">
        <div style="display: flex; align-items: center;">
          <a href="/" class="logo"><img src="${resmod }/smate-pc/img/logo.png"></a>
          <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get">
            <div class="main-page__search">
              <div class="main-page__searchbox">
                <input id="search_some_one"
                  style="height: 100%; width: 100%; outline: none; border: none; margin-left: 6px; color:#999;" maxlength="100"
                  name="searchString" title="<s:text name="page.index.search.tip"/>"
                  placeholder="<s:text name="page.index.search.tip"/>" autocomplete="off" value="">
                <div class="searchbox__icon" style="cursor: pointer;" onclick="ScmMaint.searchSomeOne()"></div>
              </div>
            </div>
          </form>
          <div class="logining">
            <div class="logining_icon" style="display: flex; width: 24px; align-items: center;"
              onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">
              <i class="new-online_Service" style="cursor: pointer;" title="<s:text name='page.index.online.question'/>"></i>
            </div>
            <div class="version_en" style="margin-left: 0px;">
              <c:if test="${locale == 'zh_CN'}">
                <a onclick="Register.change_locol_language('en_US');">&nbsp;English&nbsp;</a>
              </c:if>
              <c:if test="${locale != 'zh_CN'}">
                <a onclick="Register.change_locol_language('zh_CN');">&nbsp;中文&nbsp;</a>
              </c:if>
            </div>
          </div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
    <div class="content first-page_content">
      <div class="register">
        <div class="register_wrap">
          <div class="register_wrap-index_footer">
            <c:if test="${locale == 'zh_CN'}">
              <span class="register_wrap-index_footer-content">科研社交网络，成就创新梦想</span>
            </c:if>
            <c:if test="${locale != 'zh_CN'}">
              <span class="register_wrap-index_footer-content">Connect people to research and innovate smarter</span>
            </c:if>
          </div>
          <c:if test="${locale == 'zh_CN'}">
            <div class="register_slogan-CN"></div>
          </c:if>
          <c:if test="${locale != 'zh_CN'}">
            <div class="register_slogan-US"></div>
          </c:if>
          <!-- 登录 -->
          <div class="sign-in__container" id="select_login_id">
            <div class="login_cont-top_title">
              <a style="font-size: 18px;" class="login_header__title" href="/oauth/pc/register"> <s:if
                  test="randomNum==1">
                  <s:text name="page.index.newtips.item51" />
                </s:if> <s:if test="randomNum==2">
                  <s:text name="page.index.newtips.item52" />
                </s:if> <s:if test="randomNum==3">
                  <s:text name="page.index.newtips.item53" />
                </s:if>
              </a>
            </div>
            <div class="sign-in__header">
              <div class="sign-in__header-container">
                <div class="sign-in__header-title sign-in__header-title__selected"
                  onclick="ScmIndex.newChangeModel('login');">
                  <span style="margin: 0 auto; width: 100%;"><s:text name="page.index.login" /> </span>
                </div>
                <div class="sign-in__header-title" onclick="ScmIndex.newChangeModel('register');">
                  <span style="margin: 0 auto; width: 100%;"><s:text name="page.index.reg" /> </span>
                </div>
              </div>
            </div>
            <div class="sign-in__body">
              <form action="${sysDomain}/oauth/login" id="loginForm" method="post">
                <input type="hidden" value="${sys }" id="sys" name="sys" /> <input type="hidden" value="${service }"
                  id="service" name="service" /> <input type="hidden" value="${SYSID }" id="SYSSID" name="SYSSID">
                 <div id="loadin-function_container1">
                    <div class="sign-in__account sign-in__num-box targetloca-input_container" style="position: relative;">
                      <i class="sign-in__account-tip"></i> <input type="text" class="sign-in__num-input" name="userName"
                        id="username" oninput="hideTip('username');" value="${username}" maxlength="50" placeholder="<s:text name="page.index.account"/>">
                        <c:if test="${not empty msg && errMsgPosition == '1'}">
                            <div id="username_tip">
                                <div class="error_message-prompt error_message-rightprompt" style="display: block;top: 1px;">
                                    <div class="error_message-prompt_side error_message-prompt_rightside" style="height: 40px; word-break: break-all;">
                                        <div class="error_message-prompt_detail">${msg}</div>
                                        <div class="error_message-prompt_icon-right"></div>
                                    </div>
                                </div>
                                <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden" style="display: block; top: 1px;">
                                    <div class="error_message-prompt_side error_message-prompt_leftside" style="height: 40px; word-break: break-all;">
                                        <div class="error_message-prompt_detail">${msg}</div>
                                        <div class="error_message-prompt_icon-left"></div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                      </div>
                    <div class="sign-in__password sign-in__num-box targetloca-input_container" style="position:relative;">
                      <i class="sign-in__password-tip"></i> <input type="password" class="sign-in__num-input"
                        name="password" id="password" oninput="hideTip('password');" maxlength="40" value="${password }"
                        placeholder="<s:text name="register.password"/>">
                        <c:if test="${not empty msg && errMsgPosition == '2'}">
                            <div  id="password_tip"> 
                              <div class="error_message-prompt error_message-rightprompt" style="display: block;top: 1px;">
                                  <div class="error_message-prompt_side error_message-prompt_rightside">
                                      <div class="error_message-prompt_detail">${msg}</div>
                                      <div class="error_message-prompt_icon-right"></div>
                                  </div>
                              </div>
                              <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden" style="display: block; top: 1px;">
                                  <div class="error_message-prompt_side error_message-prompt_leftside">
                                      <div class="error_message-prompt_detail">${msg}</div>
                                      <div class="error_message-prompt_icon-left"></div>
                                  </div>
                              </div>
                            </div>
                        </c:if>
                    </div>
                    
                    <div class="sign-in__Checkcode" id="show_checkcode" style="display:none;">
                         <div class="sign-in__password sign-in__num-box targetloca-input_container" style="width: 180px;margin: 0px 0px 16px 0px;">
                             <input type="hidden" name="needValidateCode" oninput="hideTip('needValidateCode');" id="needValidateCode" value="${needValidateCode}" />
                             <i class="phone-Verification_icon  phone-Verification_icon-tips text_login-tip" style="border: none; background-position: -1px -2px; width: 46px;"></i> <input type="text" class="sign-in__num-input" name="validateCode" id="validateCode" maxlength="40" value="" placeholder="<s:text name="register.check.code"/>" style="color: rgb(51, 51, 51);width: 125px!important;">
                         </div>
                         <div class="sign-in__Checkcode-avator" onclick="refreshCode()">
                           <img id="img_checkcode" src="${domainscm}/oauth/validatecode.gif" style="width: 100%;height: 100%;" />
                         </div>
                         <c:if test="${not empty msg && errMsgPosition == '3'}">
                            <div id="needValidateCode_tip">
                              <div class="error_message-prompt error_message-rightprompt" style="display: block;top: 1px;">
                                  <div class="error_message-prompt_side error_message-prompt_rightside">
                                      <div class="error_message-prompt_detail">${msg}</div>
                                      <div class="error_message-prompt_icon-right"></div>
                                  </div>
                              </div>
                               <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden" style="display: block; top: 1px;">
                                  <div class="error_message-prompt_side error_message-prompt_leftside">
                                      <div class="error_message-prompt_detail">${msg}</div>
                                      <div class="error_message-prompt_icon-left"></div>
                                  </div>
                              </div>
                            </div>
                        </c:if>
                         <div class="error_message-prompt error_message-rightprompt" style="top: 2px;right: -215px;">
                            <div class="error_message-prompt_side error_message-prompt_rightside" style="width: 180px;">
                               <div class="error_message-prompt_detail">看不清？换一张</div>
                                <div class="error_message-prompt_icon-right"></div>
                            </div>
                         </div>
                    </div>
                    
                         <div class="sign-in__remember">
                        <div class="sign-in__forget">
                          <a href="/oauth/pwd/forget/index?returnUrl=${domainscm}/oauth/index" class="blue1 fr"> <s:text
                              name="page.index.forgotpassword" />
                          </a>
                        </div>
                        <div class="loadin-by_phone" onclick="changeloadbox1()" id="loginByPhone" style="display:block;">
                          <s:text name="page.mobile.code.login"/>
                        </div>
                    </div>
                </div>
                
                <div id="loadin-function_container2" style="display:none;">
                
                    <div class="sign-in__account sign-in__num-box">
                      <i class="sign-in__account-tip"></i> <input type="text" class="sign-in__num-input" name="mobileNum"
                        id="mobileNum" value="${mobileNum}" maxlength="11" placeholder="<s:text name="page.mobile.enter"/> " style="  padding-left: 6px ;">
                      <input type="hidden" id="mobileCodeLogin" name="mobileCodeLogin" value="false">
                    </div>
                    
                    
                    <div class="targetloca tip__container-flag" style="display: flex; flex-direction: column; width: 100%; padding-top: 2px; margin: 12px 0px 16px 0px;">
                        <div  class="targetloca-input_container" style="display: flex; align-items: center; position: relative;">
                          <div class="phone-Verification_icon  phone-Verification_icon-tip"></div>
                          
                          <input type="text" value="" id="mobileCode" name="mobileCode" maxlength="6" placeholder="<s:text name="page.mobile.code"/>" class="phone-Verification_icon-input"/>
                          
                          <div class="phone-Verification_code" type="2000" id="mobileNumBtn" onclick="Register.sendMobileLoginCode(this);"><s:text name="page.mobile.code.get"/> </div>
                        
                        </div>
                    </div>
                    
                 
               
                      <div class="sign-in__remember" style="justify-content: flex-end;">
                      <div class="loadin-by_phone" onclick="changeloadbox2()"><s:text name="page.password.login"/> </div>
                    </div>
              </div>
                
                
                
                <div class="sign-in__btn" onclick="beforeSubmit();">
                  <s:text name="page.index.new.login" />
                </div>
                <div class="sign-in__footer">
                  <div class="sign-in__footer-title">
                    <s:text name="page.other.account.login" />
                  </div>
                  <div class="sign-in__footer-title__container">
                    <i class="change-load__tip1" title="<s:text name='page.index.qq.login'/>" onclick="loginQQ()"></i>
                    <i class="change-load__tip2" title="<s:text name='page.index.wechat.login'/>" onclick="ScmLoginBox.loginWechat();"></i>
                    <%-- <i class="sina_login_icon" title="<s:text name='page.index.sina.login'/>" onclick="ScmLoginBox.weiboLogin()"></i> --%>
                  </div>
                </div>
                <div class="new-mainpage_app-QRcontainer"> 
                    <div class="new-mainpage_app-QRBtn">
                         <a href="${resmod }/smate-pc/new-IntroducePage/scmIntroduce.html" >下载科研之友app</a>
                    </div>
                    <div class="new-mainpage_app-QRline"></div>
                    <div class="new-mainpage_app-QRcode">
                        <div class="new-mainpage_app-QRcode_Box">
                             <img src="/resmod/smate-pc/img/app_download/uat_app_download.png"> 
                        </div>
                    </div>
                </div>
                
              </form>
            </div>
          </div>
          <!-- 注册 -->
          <div class="login_cont checkbox-con" id="select_register_id" style="display: none; background: #fff;">
            <div class="login_cont-top_title">
              <a style="font-size: 18px;" class="login_header__title" href="/oauth/pc/register"> <s:if
                  test="randomNum==1">
                  <s:text name="page.index.newtips.item51" />
                </s:if> <s:if test="randomNum==2">
                  <s:text name="page.index.newtips.item52" />
                </s:if> <s:if test="randomNum==3">
                  <s:text name="page.index.newtips.item53" />
                </s:if>
              </a>
            </div>
            <div class="sign-in__header">
              <div class="sign-in__header-container">
                <div class="sign-in__header-title" onclick="ScmIndex.newChangeModel('login');">
                  <span style="width: 100%; margin: 0 auto;"><s:text name="page.index.login" /> </span>
                </div>
                <div class="sign-in__header-title sign-in__header-title__selected"
                  onclick="ScmIndex.newChangeModel('register');">
                  <span style="width: 100%; margin: 0 auto;"><s:text name="page.index.reg" /> </span>
                </div>
              </div>
            </div>
            <form action="${domainscm}/oauth/pc/register/sava" method="POST" id="regForm">
              <input type="hidden" id="token" name="token" value="${token}" /> <input type="hidden" value="${service }"
                name="service" />
              <input type="hidden" id="isPhoneCheck" name="isPhoneCheck" value=""/>
              <ul style="margin: 10px auto;">
                <div class="login-error_tip" style="display: none;"> </div>
                <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                  <div class="targetloca-input_container" style="display: flex; align-items: center;">
                    <div class="email_bg text_login-tip"
                      style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                    <!-- 防止浏览器表单自动填充  Start -->
                    <input type="text" name="email" disabled style="display: none;" /> <input type="text"
                      name="newpassword" disabled style="display: none;" />
                    <!--/ 防止浏览器表单自动填充 End -->
                    <input type="text" value="" maxlength="50" placeholder="<s:text name='register.email.friend.tip' />"
                      class="text_login text_login-tip_inputbox" id="email" name="email"/>
                  </div>
                  <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">邮件作为登录名及接受系统通知</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">邮件作为登录名及接受系统通知</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                </li>
                <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                  <div class="targetloca-input_container" style="display: flex; align-items: center;">
                    <div class="password_bg text_login-tip"
                      style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                    <input type="password" value="" maxlength="40"  placeholder="<s:text name='register.password.friend.tip' />" id="newpassword" name="newpassword" class="text_login text_login-tip_inputbox"/>
                  </div>
                  <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">密码最少为6个字符</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">密码最少为6个字</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                </li>
                <c:if test="${locale == 'zh_CN'}">
                  <li class="targetloca tip__container-flag" id="phoneZhRegister" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="chinese_bg text_login-tip"
                        style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                      <input type="text" value="" placeholder="<s:text name='register.zhlastName'/>" id="zhlastName" name="zhlastName" class="text_login" maxlength="20"
                        style="border-left-style: none; width: 80px; padding-left: 6px; height: 38px!important;" /> 
                        <input type="hidden"  value="" maxlength="20" id="lastName" name="lastName" style="height: 38px!important;" />
                        
                        <input type="text" value="" maxlength="40" placeholder="<s:text name='register.zhfirstName'/>"  id="zhfirstName" name="zhfirstName" class="text_login text_login-flex"
                        style="border-left-style: none; width: 194px; padding-left: 6px; margin-left: 8PX;border-left: 1px solid #ddd; height: 38px!important;" /> 
                        <input type="hidden" value="" maxlength="40" id="firstName" name="firstName" style="height: 38px!important;" />
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                  <li class="targetloca tip__container-flag" id="onlyEmailZhlastName" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="chinese_bg text_login-tip"
                        style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                      <input type="text" value="" placeholder="<s:text name='register.zhlastName'/>" id="zhlastNameNoPhone" name="zhlastNameNoPhone" class="text_login text_login-tip_inputbox" maxlength="40" /> 
                        <input type="hidden"  value="" maxlength="20" id="lastNameNoPhone" name="lastNameNoPhone" />
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                  <li class="targetloca tip__container-flag" id="onlyEmailZhfirstName" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="chinese_bg text_login-tip"
                        style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                        <input type="text" value="" maxlength="40" placeholder="<s:text name='register.zhfirstName'/>"  id="zhfirstNameNoPhone" name="zhfirstNameNoPhone" class="text_login text_login-tip_inputbox"/> 
                        <input type="hidden" value="" maxlength="20" id="firstNameNoPhone" name="firstNameNoPhone" />
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                </c:if>
                <c:if test="${locale != 'zh_CN'}">
                  <li class="targetloca tip__container-flag" id="phoneEnRegister" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="english_bg text_login-tip"
                        style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                      <input type="text" value="" placeholder="<s:text name='register.lastName'/>" id="lastName"
                        name="lastName" class="text_login" maxlength="20"
                        style="border-left-style: none; width: 80px; padding-left: 4px;height: 38px;" />
                        
                       <input type="text" value="" maxlength="40" placeholder="<s:text name='register.firstName'/>"
                        id="firstName" name="firstName" class="text_login text_login-flex"
                        style="border-left-style: none; width: 194px!important; padding-left: 6px; width: 80px; margin-left: 8PX; border-left: 1px solid #ddd; height: 38px;"/>  
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden" >
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                  <li class="targetloca tip__container-flag" id="onlyEmailEnlastName" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="english_bg text_login-tip"
                        style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                      <input type="text" value="" placeholder="<s:text name='register.lastName'/>" id="lastNameNoPhone"
                        name="lastNameNoPhone" class="text_login text_login-tip_inputbox" maxlength="40"/>
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden" >
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                  <li class="targetloca tip__container-flag" id="onlyEmailEnfirstName" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="english_bg text_login-tip"
                        style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                       <input type="text" value="" maxlength="40" placeholder="<s:text name='register.firstName'/>"
                        id="firstNameNoPhone" name="firstNameNoPhone" class="text_login text_login-tip_inputbox"/>  
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden" >
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">姓名不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                </c:if>
                
                <li class="targetloca tip__container-flag" id="mobileNumberDiv" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center;">
                      <div class="phone-mobile_icon phone-mobile_icon-tips text_login-tip"></div>
                      <input type="text" value="" id="mobileNumber" class="text_login-tip_inputbox text_login" name="mobileNumber" maxlength="11" placeholder="<s:text name="page.mobile.enter"/> " />
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">电话号码不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">电话号码不能为空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>
                  </li>
                  
                  <li class="targetloca tip__container-flag" id="mobileVerifyCodeDiv" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: center; position: relative;">
                      <div class="phone-Verification_icon  phone-Verification_icon-tips text_login-tip"></div>
                      <input type="text" class="text_login-tip_inputbox text_login" value="" id="mobileVerifyCode" name="mobileVerifyCode" maxlength="6" placeholder="<s:text name="page.mobile.code"/> "/>
                      <div class="phone-Verification_code" id="mobileNumberBtn" onclick="Register.sendMobileCode(this);"><s:text name="page.mobile.code.get"/> </div>
                    </div>
                    <div class="error_message-prompt error_message-rightprompt">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">验证码不能空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
                    <div class="error_message-prompt error_message-leftprompt setting-list_page-item_hidden">
                        <div class="error_message-prompt_side error_message-prompt_leftside">
                            <div class="error_message-prompt_detail">验证码不能空</div>
                            <div class="error_message-prompt_icon-left"></div>
                        </div>
                    </div>

                  </li>
                  </li>
                <li><input type="submit" id="regSubmit" value="<s:text name="register.action.submit"/>"
                  onclick=" Register.submit(this);" class="login_btn">
                  <p class="other_l f666" style="display: flex; align-items: center; margin-top: 4px;">
                  <div class="targetloca" style="display: flex; flex-direction: column;">
                    <div class="targetloca-input_container" style="display: flex; align-items: flex-start;">
                      <span class="check_fx" style="margin: 0px;"> <input type="checkbox" id="checkedall"
                        name="checkedall" disabled="disabled" checked="checked">
                      </span>
                      <div style="margin-left: 12px; word-break: break-word;">
                        <s:text name='register.iris1' />
                        <a href="/resmod/html/condition_${locale }.html" target="_blank" rel="nofollow"
                          class="checkbox-detaile_btn"><s:text name='register.iris2' /></a>
                        <s:text name='register.iris3' />
                        <a href="/resmod/html/policy_${locale }.html" target="_blank" rel="nofollow"
                          class="checkbox-detaile_btn"><s:text name='register.iris4' /></a>
                        <s:text name='register.iris5' />
                      </div>
                    </div>
                  </div>
                  </p></li>
              </ul>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="footer__cont">
      
      <div class="footer__cont--code">
        <div class="footer__cont-title"><s:text name="page.index.scan.scm" /></div>
        <div class="footer__cont--code_title">
          <s:text name="page.index.wechat.qrcode" />
          <div class="footer__cont--code_avator">
             <img src="${resmod }/images_v5/images2016/code.jpg">
          </div>
        </div>
       <%--  <div class="footer__cont--code_title">
          <s:text name="page.index.app.download" />
          <div class="footer__cont--code_avator">
             <img src="${resmod }/smate-pc/img/app_download/run_app_download.png" id="app_download_qrcode">
          </div>
        </div> --%>
      </div>
      
     
    
    
    
    
    <div class="footer__cont--search">
      <div style="display: flex; flex-direction: column;">
        <div class="footer-Catalog">
          <div class="footer-Catalog_title">
            <s:text name="page.index.catalog" />
          </div>
          <div style="display: flex; justify-content: space-between; margin-top: 12px; flex-wrap: wrap; width: 150px;">
            <div class="footer__cont-select">
              <a href="${domainrol }/common/inslist?locale=${locale }" target="_blank"><s:text
                  name="page.index.institution" /></a>
            </div>
            <div class="footer__cont-select">
              <a href="/indexhtml/psn_maint_${locale }.html" target="_blank"><s:text name="page.index.member" /></a>
            </div>
            <div class="footer__cont-select">
              <a href="/indexhtml/pub_A_${locale }.html" target="_blank"><s:text name="page.index.paper" /></a>
            </div>
            <div class="footer__cont-select">
              <a href="https://www.innocity.com/" target="_blank"><s:text name="page.index.patent" /></a>
            </div>
            <div class="footer__cont-select">
              <a href="/dynweb/news/main" target="_blank"><s:text name="page.index.news" /></a>
            </div>
            
            
          </div>
        </div>
      </div>
    </div>
    <div class="footer__cont--solution">
      <h3>
        <s:text name="page.index.solution" />
      </h3>
      <ul>
        <li class="footer_fun-selector"><a style="text-decoration: none;"
          href="http://irisaas.smate.com/egrantweb/" target="_blank"><s:text name="page.index.Branch.cloud" /></a></li>
        <li class="footer_fun-selector">
          <!-- /resmod/cotrun/html/sie_index.html --> <a style="text-decoration: none;"
          href="https://sie.scholarmate.com/common/index" target="_blank"><s:text name="page.index.Institutional" /></a>
        </li>
        <li class="footer_fun-selector"><a href="/prjweb/ins/create/main" target="_blank"
          style="display: flex; align-items: center; text-decoration: none;"><i
            class="material-icons create-branch_tip"
            style="color: #999; font-size: 22px; padding: 0px; margin: 0px; background: #fff;">add</i> <s:text
              name="page.index.Branch.creat" /></a></li>
      </ul>
    </div>
  </div>
  <footer class="footer footer-page_fixed" style="min-width: 1200px; width: 100%;">
    <div class="footer__bottom" style="display: flex; flex-direction: column; justify-content: center; align-items: center;  height: 70px;">
      <div style="line-height: 20px;height: 20px;  font-size: 14px;">
        <s:text name="page.foot.copyright" />
        <s:text name="page.foot.new.beian1" />
        <img src="${resscmsns}/images_v5/beian/beian.png" style="width: 12px;">
        <s:text name="page.foot.new.beian2" />
      </div>
      <p style="line-height: 20px;height: 20px;  font-size: 14px;">
         <a class="responsibility_icon" href="https://szcert.ebs.org.cn/aba63455-ed82-47fd-8507-6c63185188d5" target="_blank">
            <%-- <img src="${resmod }/smate-pc/img/govIcon.gif" class="responsibility_icon" style="width: 20px; height: 26px;"> --%>
         </a>
         <script id="ebsgovicon" src="https://szcert.ebs.org.cn/govicon.js?id=0284c163-4b3a-4eaf-9891-bccedc7636b4&width=25&height=34&type=1" type="text/javascript" charset="utf-8"></script>
         <a  class="responsibility_icon" href="${resmod }/smate-pc/img/commitment.jpg" target="_blank">《安全生产主体责任承诺书》</a>
      </p>
    </div>
  </footer>
</body>
<script language="javascript">
//初始化数据
ScmIndex.initPageInfo();
Register.initCommonData();
ScmIndex.initCommonData();
showAppDownloadQRCode();
</script>
</html>
