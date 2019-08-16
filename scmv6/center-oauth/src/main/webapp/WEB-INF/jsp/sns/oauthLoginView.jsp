<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="keywords" content='<s:text name="page.seo.index.Keywords"/>' />
<meta name="description" content='<s:text name="page.seo.index.description"/>' />
<meta name="robots" content='<s:text name="page.seo.index.robots"/>' />
<title><s:text name="page.seo.index.title" /></title>
<link href="${resmod}/css/smate.oauth.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/index.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/header.css" rel="stylesheet" type="text/css" />
<script src="${resscmsns}/js_v5/jquery.js"></script>
<script src="${resscmsns}/js_v5/scm.cookie.js"></script>
<script src="${resscmsns}/js_v5/scm.index.maint.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.watermark.js"></script>
<script src="${resscmsns}/js_v5/plugin/md5/md5.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/rsa/security.js"></script>
<script type="text/javascript">
	var locale = '${locale}';
<%--修正记住登录问题_在index.jsp跳到login.jsp时填写帐号不一致问题_tsz_2014-01-02_SCM-4233 --%>
	var username = "${param.username}";
	var password = "";
	var zh_merge_tips1 = '您当前输入的帐号因为帐号重复的原因, 已被合并至帐号';
	var zh_merge_tips2 = ', 请使用邮箱';
	var zh_merge_tips3 = '登陆系统, 如不记得密码, 请使用“忘记密码”功能重置密码';
	var en_merge_tips1 = 'Your account was merged to ';
	var en_merge_tips2 = ' due to duplication. ';
	var en_merge_tips3 = 'Please enter ';
	var en_merge_tips4 = ' to login. If you forgot your password, please use Forgot Password to reset. ';
	String.prototype.trim = function() {
		// 用正则表达式将前后空格  
		// 用空字符串替代。  
		return this.replace(/(^\s*)|(\s*$)/g, "");

	}
	$(document).ready(
			function() {
				//修正密码输入错误后跳转的登录页面，账号输入框中的内容请显示为黑色字体 TSZ-2014-1-11_SCM-4397
				//$('#username').watermark({
				//tipCon : '<spring:message code="cas.login.email.tip"/>',
				//blurClass : 'watermark1'
				//});
				/* $("#submit").bind("click",function(){
					 	SCMIndex.login();
					 	loginForm();
				    	//密码加密传输 tsz_2014.11.05_SCM-5542
				    	//更改加密方式
				}); */
				SCMIndex.initPageInfo();
				/* ajaxSysUserMerge(); */
				/**
				 * (未登录系统的)转换页面语言.
				 */
				change_locol_language = function(locale) {
					strUrl = location.href;//当前url地址.
					if (strUrl.indexOf("cas/login") >= 0) { //加判断解决SCM-6345
						window.location.href = "/scmwebsns/?locale=" + locale
								+ "&submit=true";
					} else {
						//请求的url地址.
						var hrefString = strUrl.substring(0, strUrl
								.indexOf("?"));
						var params = "";
						var flag = false;//验证url请求参数中是否包含页面语言参数locale(true-包含；false-不包含).
						if (strUrl.indexOf("?") >= 0) {
							//url请求参数.
							var paraString = strUrl.substring(strUrl
									.indexOf("?") + 1, strUrl.length);
							if (paraString != '') {
								var paraArr = paraString.split("&");
								for (var i = 0; j = paraArr[i]; i++) {
									//参数名.
									var name = j.substring(0, j.indexOf("="));
									//参数值.
									var value = j.substring(j.indexOf("=") + 1,
											j.length);
									if (name == 'locale') {//页面语言的参数设置为locale的值.
										flag = true;
										value = locale;
									} else {//其他参数进行编码处理,替换掉特殊字符.
										//value=SCMIndex.URLencode(value);
									}
									params = params + name + "=" + value + "&";
								}
							}
						}
						//如果请求参数中不包含语言参数，则增加该参数.
						if (!flag) {
							params = params + "locale=" + locale;
						}
						//重定位页面请求地址
						window.location.href = hrefString + "?" + params;
					}
				};
				// 初始化验证码
				if("${needValidateCode}" == '1'){
				    refreshCode();
				}
			});
	function loginform() {
		SCMIndex.login();
		/* var password= $("#writepassword").val();
		 $.ajax({
				url:"/oauth/rsa",
				async: false,
				type:"post",
				dataType:"json",
				success:function(data){
					var modulus = data.modulus, exponent = data.exponent;//从后台获取公钥
		            var epwd = password;
		            if (epwd.length != 256) {
		                var publicKey = RSAUtils.getKeyPair(exponent, '', modulus);//调用公钥security.js加密
		                $("#password").val(RSAUtils.encryptedString(publicKey, password));//利用加密后的公钥对密码进行加密
		                password =$("#password").val();
		            }
		            
				}
		  })  */
	}
	function refreshCode() {
		document.getElementById("img_checkcode").src ="/oauth/validatecode.gif?date="+new Date().getTime();
    };
</script>
</head>
<body>
<body id="main_body">
  <c:choose>
    <c:when test="${sys eq 'scmsns'}">
      <c:redirect url="${domainscm}/">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'prosns'}">
      <c:redirect url="${domainscm}/scmwebsns/proIndex">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'scmrol'}">
      <c:redirect url="${homeRol}">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="username" value="${param.username}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'nsfcscm'}">
      <c:redirect url="${domainnsfcscm}/scm">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'nsfcnet'}">
      <c:redirect url="${domainnet}/scm">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${from eq 'scmwebbpo'}">
      <c:redirect url="${domainbpo}/scmwebbpo">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'nsfcr'}">
      <c:redirect url="${domainnsfc}/scm">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'gxrol'}">
      <c:redirect url="${domaingxrol}/scmwebrol">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'stdrol'}">
      <c:redirect url="${domainstdrol}/scmstdrol">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'hnstdrol'}">
      <c:redirect url="${domainhnrol}/scmstdrol">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'zsrol'}">
      <c:redirect url="${domainzsrol}/zsrol">
        <c:param name="auth" value="no" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
        <c:param name="service">${service}</c:param>
      </c:redirect>
    </c:when>
    <c:when test="${sys eq 'sync-check'}">
      <c:redirect url="${service}">
      </c:redirect>
    </c:when>
    <c:when test="${target_url ne null and target_url ne ''}">
      <%--  <img style="display:none" src="${domainscm }/scmwebsns/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
         <img style="display:none" src="${domainnsfc }/scm/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
         <img style="display:none" src="${domainnsfcscm }/scm/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
         <img style="display:none" src="${domainnet}/scm/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
         <img style="display:none" src="${domaingxrol}/scmwebrol/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
         <img style="display:none" src="${domainexpert}/egtexpertweb/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
         <c:if test="{domainnsfcsns ne null}">
	     <img style="display:none" src="${domainnsfcsns}/checkcas?casAuth=false&maxAge=-1"  alt="writecookie"/>  
         </c:if> --%>
    </c:when>
    <c:when test="${page eq 'frdInvite' or page eq 'groupInvite' or page eq 'inviteUrlValue'}">
      <c:redirect url="${param.inviteUrl}">
        <c:param name="auth" value="no" />
        <c:param name="username" value="${param.username}" />
        <c:param name="validateCode" value="${nopassvalcode}" />
        <c:param name="needCode" value="${needCode}" />
      </c:redirect>
    </c:when>
    <c:otherwise>
      <!-- TOP START -->
      <div id="home-header">
        <div class="sm-top">
          <div class="logo2" style="height: 22px;">
            <a href="${domainscm}" style="height: 22px;" title="<s:text name="oauth.login.logo.tip" />"
              alt="<s:text name="oauth.login.logo.tip" />"></a>
          </div>
          <div class="h-top-nav">
            <a href="/resscmwebsns/html/helpCenter/index.html"> <s:text name="oauth.login.help" />
            </a> | <a href="/resscmwebsns/html/res_scm_zh_CN.html"> <s:text name="oauth.login.resource" />
            </a> | <a style="cursor: pointer;" onclick="SCMIndex.change_locol_language('zh_CN');">中文版</a> | <a
              style="cursor: pointer;" onclick="SCMIndex.change_locol_language('en_US');">English</a>
          </div>
        </div>
        <div class="menu">
          <div class="menu-bjleft"></div>
          <div class="menu-bjcenter">
            <div class="m-text">
              <a href="${domainscm}"> <s:text name="oauth.login.index" />
              </a> <a href="${domainscm}/resscmwebsns/html/helpCenter/index.html"> <s:text name="oauth.login.smate" />
              </a> <a href="${domainscm}/oauth/pc/register"> <s:text name="oauth.login.join" />
              </a>
            </div>
            <div class="mailbox">
              <span class="fcu14"> <s:text name="oauth.login.dream" />
              </span>
            </div>
          </div>
          <div class="menu-bjright"></div>
        </div>
      </div>
      <div class="clear_h20"></div>
      <div id="invite_login">
        <form method="post" id="loginForm" name="fm1" Class="fm-v clearfix" action="${domainscm}/oauth/login"
          onsubmit="return loginform()">
          <input type="hidden" value="${sys}" id="sys" name="sys" /> <input type="hidden" value="${SYSSID}" id="SYSSID"
            name="SYSSID"> <input type="hidden" value="${service}" id="service" name="service">
          <div class="clear_h20"></div>
          <div class="login_enter">
            <div class="login_top2">
              <i class="icon_login"></i>
              <s:text name="oauth.login.title" />
            </div>
            <div class="land_box">
              <div class="land_box">
                <!-- <form:errors path="*" cssClass="error_box" id="status" element="div" /> -->
                <div id="status" class="error_box">
                  <%-- <s:text name="error.authentication.credentials.bad" /> --%>
                  ${msg }
                </div>
                <table width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
                  <tr>
                    <td width="100" align="right"><s:text name="oauth.login.email" /></td>
                    <td align="left"><input name=userName type="text" class="h-login-btn2" id="username" /></td>
                  </tr>
                  <tr>
                    <td align="right"><s:text name="oauth.login.password" /></td>
                    <td align="left"><input type="password" id="writepassword" name="password" class="h-login-btn2" />
                      &nbsp;&nbsp; <a href="${domainscm}/oauth/pwd/forget/index?returnUrl=${domainscm}&locale=${locale}"
                      class="gray u" title="<s:text name="oauth.login.forgotpassword" />"> <s:text
                          name="oauth.login.forgotpassword" />
                    </a></td>
                  </tr>
                  <c:if test="${needValidateCode=='1'}">
                    <tr>
                      <td height="30" align="right"><s:text name="oauth.login.validatecode" /></td>
                      <td colspan="2" align="left"><input type="text" name="validateCode" id="validateCode"
                        class="code-login" /> <input type="hidden" name="needValidateCode" id="needValidateCode"
                        value="1" /> &nbsp;&nbsp; 
                        <img id="img_checkcode"
                        src="${domainscm}/cas/validatecode/jcaptcha.jpg" width="88" height="23" align="absmiddle" />
                        &nbsp;&nbsp;&nbsp;&nbsp; <a onclick="refreshCode()" style="cursor: pointer" class="Blue u">
                          <s:text name="oauth.login.validatecode.refresh" />
                      </a></td>
                    </tr>
                  </c:if>
                  <%--  <tr>
          <td align="right">&nbsp;</td>
          <td align="left">
          		默认选中复选框_tsz_2013-12-31_SCM-4229
          	<input type="checkbox" name="rememberMe" id="rememberMe" checked="checked"/>
            <label for="rememberMe"><s:text name="oauth.login.rememberLogin" /></label></td>
        </tr> --%>
                  <tr>
                    <td align="right">&nbsp;</td>
                    <td height="40" align="left"><input type="submit" name="submit" id="submit"
                      class="uiButton text14 uiButtonConfirm" title="<s:text name="screen.welcome.button.login"/>"
                      style="padding: 4px 20px;" value="<s:text name="screen.welcome.button.login"/>" /></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="50">&nbsp;</td>
                    <td align="left"><s:text name='oauth.login.no.account' /> <a
                      href="${domainscm}/oauth/pc/register" class="Blue u"> <s:text name="oauth.login.tip.tip2" />
                    </a></td>
                  </tr>
                </table>
                <input type="hidden" name="locale" value="${locale}" /> <input type="hidden" name="_eventId"
                  value="submit" />
                <!-- <input type="hidden" name="service" value="${service }" /> -->
              </div>
            </div>
            <div class="clear_h20"></div>
          </div>
        </form>
        <div id="footer">
          <div class="box_footer">
            <div class="footer-left">
              <s:text name="oauth.footer.label.name" />
              <img src="${resscmsns}/images_v5/beian/beian.png" style="width: 12px;">
              <s:text name="oauth.foot.new.beian" />
            </div>
            <div class="footer-right">
              <a href="/resscmwebsns/html/about_us_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.aboutUs" />
              </a> | <a href="/resscmwebsns/html/policy_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.privacv" />
              </a> | <a href="/resscmwebsns/html/condition_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.terms" />
              </a> | <a href="/resscmwebsns/html/contact_zh_CN.html" target="_blank" class="Blue"> <s:text
                  name="oauth.foot.contactUs" />
              </a>
            </div>
          </div>
        </div>
      </div>
      <%-- <img style="display:none" src="${domainscm }/scmwebsns/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
	         <img style="display:none" src="${domainnsfc }/scm/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
	         <img style="display:none" src="${domainnsfcscm }/scm/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
	         <img style="display:none" src="${domainnet}/scm/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
	         <img style="display:none" src="${domaingxrol}/scmwebrol/checkcas?casAuth=false&maxAge=${rememberMeMaxAge}"  alt="writecookie"/> 
	         <img style="display:none" src="${domainnsfc }/scm/checkcas?casAuth=false&maxAge=-1"  alt="writecookie"/>  
	       <c:if test="{domainnsfcsns ne null}">
	            <img style="display:none" src="${domainnsfcsns}/checkcas?casAuth=false&maxAge=-1"  alt="writecookie"/>  
          </c:if> --%>
    </c:otherwise>
  </c:choose>
</body>
</html>