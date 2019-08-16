<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><s:text name="page.index.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet">
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.validate.min.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript" src="${resmod}/js/index/pc.scm.index.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript">
var locale='${locale}';
var searchTypeTip="输入关键词检索论文、专利、专家、机构... ";
var searchTypeTip_en_US="Enter keywords to search publications, patents, researchers, organization.";
var searchType="<s:text name='page.main.search' />";
var searchType2="<s:text name='page.main.search2'/>";
var searchType3="<s:text name='page.main.search3'/>";
var searchType4="<s:text name='page.main.search4'/>";
$(document).ready(function() {
	    
	    $('.checkbox').on('click',function(){
	      if($(this).siblings("input[type='checkbox']").attr('checked')){
	        $(this).removeClass('cur');
	        $(this).siblings("input[type='checkbox']").removeAttr('checked')
	      }else{
	        $(this).addClass('cur');
	        $(this).siblings("input[type='checkbox']").attr('checked','checked')
	      }
	    });
	    var foculist = document.getElementsByClassName("text_login");
	    for(var i = 0; i<foculist.length;i++ ){
	      foculist[i].onfocus = function(obj){
	    	  if(this.value==""){
	    		  this.style.color="#ccc";
	    	  }
	          $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.border="1px solid #288aed";
	          $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.borderRightStyle="none";
	      }
	      foculist[i].onblur = function(){
	          $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.border="1px solid #ddd";
	          $(this).closest(".tip__container-flag").find(".text_login-tip")[0].style.borderRightStyle="none";
	      }
	    }
	
	
	
	var locale = '${locale}';
	$(":input").blur(function() {
		$(this).val($.trim($(this).val()));
	});
	$("#regForm").validate({
		errorPlacement : function(error, element) {//修改错误的显示位置. targetloca
			if (element.is(':checkbox')) {
				$('<br/>').appendTo(element.parent());
				error.appendTo(element.parent());
			} else {
				error.appendTo(element.closest(".targetloca"));	
			}
		},
		rules : {
			name : {
				required : true,
				maxlength : 61
			},
			<s:text name='page.index.show.lastName' /> : {
				required : true,
				maxlength : 20
			},
			<s:text name='page.index.show.firstName' /> : {
				required : true,
				maxlength : 40
			},
			email : {
				required : true,
				emailCheck : true,
				remote : "/oauth/register/ajaxCheckedUserName",
				maxlength : 50
			},
			newpassword : {
				required : true,
				minlength : 6,
				maxlength : 40
			},
			checkedall : {
				required : true
			}
		},
		messages : {
			name : {
				required : "<s:text name='register.name.zh' /><s:text name='register.required' />",
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			zhlastName : {
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			zhfirstName : {
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			lastName : {
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			firstName : {
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			email : {
				required : "<s:text name='register.email' /><s:text name='register.required' />",
				emailCheck : "<s:text name='register.isemail' />",
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />"),
				remote : "<span class='logintitle'><s:text name='register.email.exists' /></span>"
			},
			newpassword : {
				required : "<s:text name='register.password' /><s:text name='register.required' />",
				minlength : jQuery.validator.format("<s:text name='register.minlength' />"),
				maxlength : jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			checkedall : {
				required : "<s:text name='register.iris6' />"
			}
		}
	});
		ScmMaint.searchSomeOneBind();
		$("#show_select").click(function(e){
			var _obj = $("#home_search_items");
			if (_obj.is(":hidden")) {
				_obj.show();
			} else {
				_obj.hide();
				return;
			}
		    if (e && e.stopPropagation) {
		        e.stopPropagation();
		    }
		    else {//IE
		        window.event.cancelBubble = true;
		    }
		    $(document).click(function(){_obj.hide();});
		});
		if("en_US" == locale){
			ScmMaint.searchWater(searchTypeTip_en_US);
		}else{
			ScmMaint.searchWater(searchTypeTip);
		}
	//邮件验证.
	jQuery.validator.addMethod("emailCheck",function(value, element) {
		value = $.trim(value);
		return (value.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) >= 0);
	}, "<s:text name='register.isemail' />");
});
function loginQQ() {
	window.open('/scmwebsns/thirdlogin/qqlogin','newwindow','height=400,width=600,top=250,left=600,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no')
}
function  beforeSubmit() {
	ScmIndex.login();
}
</script>
</head>
<body>
  <div class="header">
    <div class="hd_wrap">
      <a href="/" class="logo"><img src="${resmod }/smate-pc/img/logo.png"></a>
      <div class="logining">
        <form action="${domainscm}/oauth/login" id="loginForm" method="post" style="display: flex;">
          <input type="hidden" value="${sys }" id="sys" name="sys" /> <input type="hidden" value="${service }"
            id="service" name="service" /> <input type="hidden" value="${SYSID }" id="SYSSID" name="SYSSID"> <input
            type="text" class="top_login_input input_default" value="${username}" maxlength="50"
            placeholder="<s:text name="page.index.username"/>" name="userName" id="username"> <input
            type="password" value="${password }" class="top_login_input input_default" maxlength="40"
            placeholder="<s:text name="register.password"/>" name="password" id="password"> <input type="submit"
            class="top_login_btn" value='<s:text name="page.index.login"/>' onclick="beforeSubmit();"> <span
            class="forget__password-btn"> <a href="/oauth/pwd/forget/index?returnUrl=${domainscm}/oauth/index"
            class="blue1 fr"> <s:text name="page.index.forgotpassword" />
          </a>
          </span>
          <div class="other_load-method" style="display: flex;">
            <div class="other_load-title" style="font-size: 14px; color: #ccc;">
              <s:text name="page.third.login" />
            </div>
            <div class="other_load-icon" title="<s:text name='page.index.qq.login'/>" onclick="loginQQ()"></div>
          </div>
          <div class="logining_icon"
            onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">
            <i class="question_icon" style="cursor: pointer;" title="<s:text name='page.index.online.question'/>"></i>
          </div>
          <div class="version_en">
            <c:if test="${locale == 'zh_CN'}">
              <a onclick="Register.change_locol_language('en_US');">&nbsp;English&nbsp;</a>
            </c:if>
            <c:if test="${locale != 'zh_CN'}">
              <a onclick="Register.change_locol_language('zh_CN');">&nbsp;中文&nbsp;</a>
            </c:if>
          </div>
        </form>
      </div>
      <div class="clear"></div>
    </div>
  </div>
  <div class="content">
    <div class="register">
      <div class="register_wrap">
        <c:if test="${locale == 'zh_CN'}">
          <div class="register_slogan-CN"></div>
        </c:if>
        <c:if test="${locale != 'zh_CN'}">
          <div class="register_slogan-US"></div>
        </c:if>
        <!-- 登录 -->
        <div class="sign-in__container" id="select_login_id" style="">
          <div class="login_cont-top_title" >连接人与知识</div>
          <div class="sign-in__header">
            <div class="sign-in__header-container">
              <div class="sign-in__header-title sign-in__header-title__selected"
                onclick="ScmIndex.newChangeModel('login');">
                <span>登录</span>
              </div>
              <div class="sign-in__header-title" onclick="ScmIndex.newChangeModel('register');">
                <span>注册</span>
              </div>
            </div>
          </div>
          <div class="sign-in__body">
            <div class="sign-in__account sign-in__num-box">
              <i class="sign-in__account-tip"></i> <input type="text" class="sign-in__num-input" placeholder="帐号">
            </div>
            <div class="sign-in__password sign-in__num-box">
              <i class="sign-in__password-tip"></i> <input type="text" class="sign-in__num-input" placeholder="密码">
            </div>
            <div class="sign-in__remember">
              <div class="sign-in__remember-tip_box">
                <i class="sign-in__remember-tip sign-in__remember-change"></i> <span class="sign-in__remember-content">记住密码</span>
              </div>
              <div class="sign-in__forget">忘记密码</div>
            </div>
            <div class="sign-in__btn">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</div>
            <div class="sign-in__footer">
              <div class="sign-in__footer-title">使用其他账号登录</div>
              <div class="sign-in__footer-title__container">
                <i class="change-load__tip1"></i> <i class="change-load__tip2"></i>
              </div>
            </div>
          </div>
        </div>
        <!--  <p> <a style="font-size: 14px;color: #999;" href="/oauth/pc/register"><s:text name="page.index.join.now" /></a></p>   -->
        <!--  注册 -->
        <div class="login_cont checkbox-con" id="select_register_id" style="display: none;">
          <%-- <h2 class="tc"><s:text name="page.index.connection.people"/>  
        
         </h2> --%>
          <div
            style="height: 48px; background: #fff; font-size: 18px; text-align: center; line-height: 48px; width: 100%;">连接人与知识</div>
          <div class="sign-in__header">
            <div class="sign-in__header-container">
              <div class="sign-in__header-title" onclick="ScmIndex.newChangeModel('login');">
                <span>登录</span>
              </div>
              <div class="sign-in__header-title sign-in__header-title__selected"
                onclick="ScmIndex.newChangeModel('register');">
                <span>注册</span>
              </div>
            </div>
          </div>
          <form action="${domainscm}/oauth/pc/register/sava" method="POST" id="regForm">
            <input type="hidden" id="token" name="token" value="${token}" /> <input type="hidden" value="${service }"
              name="service" />
            <ul style="margin: 10px auto;">
              <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                <div style="display: flex; align-items: center;">
                  <div class="email_bg text_login-tip"
                    style="width: 45px; height: 44px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                  <!-- 防止浏览器表单自动填充  Start -->
                  <input type="text" name="email" disabled style="display: none;" /> <input type="text"
                    name="newpassword" disabled style="display: none;" />
                  <!--/ 防止浏览器表单自动填充 End -->
                  <input type="text" value="" maxlength="50" placeholder="<s:text name='register.email.friend.tip' />"
                    class="text_login" id="email" name="email"
                    style="border-left-style: none; width: 290px; padding-left: 4px;" />
                </div>
              </li>
              <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                <div style="display: flex; align-items: center;">
                  <div class="password_bg text_login-tip"
                    style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                  <input type="password" value="" maxlength="40"
                    placeholder="<s:text name='register.password.friend.tip' />" id="newpassword" name="newpassword"
                    class="text_login" style="border-left-style: none; width: 290px; padding-left: 4px;" />
                </div>
              </li>
              <c:if test="${locale == 'zh_CN'}">
                <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                  <div style="display: flex; align-items: center;">
                    <div class="chinese_bg text_login-tip"
                      style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                    <input type="text" value="" placeholder="<s:text name='register.zhlastName'/>" id="zhlastName"
                      name="zhlastName" class="text_login" maxlength="20"
                      style="border-left-style: none; width: 290px; padding-left: 6px;" /> <input type="hidden"
                      value="" maxlength="20" id="lastName" name="lastName" />
                  </div>
                </li>
                <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                  <div style="display: flex; align-items: center;">
                    <div class="chinese_bg text_login-tip"
                      style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                    <input type="text" value="" maxlength="40" placeholder="<s:text name='register.zhfirstName'/>"
                      id="zhfirstName" name="zhfirstName" class="text_login"
                      style="border-left-style: none; width: 290px; padding-left: 6px;" /> <input type="hidden"
                      value="" maxlength="40" id="firstName" name="firstName" />
                  </div>
                </li>
              </c:if>
              <c:if test="${locale != 'zh_CN'}">
                <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                  <div style="display: flex; align-items: center;">
                    <div class="english_bg text_login-tip"
                      style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                    <input type="text" value="" placeholder="<s:text name='register.lastName'/>" id="lastName"
                      name="lastName" class="text_login" maxlength="20"
                      style="border-left-style: none; width: 290px; padding-left: 4px;" />
                  </div>
                </li>
                <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                  <div style="display: flex; align-items: center;">
                    <div class="english_bg text_login-tip"
                      style="width: 45px; height: 38px; border-width: 1px; border-style: solid none solid solid; border-color: rgb(221, 221, 221); border-image: initial;"></div>
                    <input type="text" value="" maxlength="40" placeholder="<s:text name='register.firstName'/>"
                      id="firstName" name="firstName" class="text_login"
                      style="border-left-style: none; width: 290px; padding-left: 4px;" />
                  </div>
                </li>
              </c:if>
              <li><input type="submit" id="regSubmit" value="<s:text name="register.action.submit"/>"
                onclick=" Register.submit(this);" class="login_btn">
                <p class="other_l f666" style="display: flex; align-items: center; margin-top: 4px;">
                  <span class="check_fx"> <input type="checkbox" id="checkedall" name="checkedall"
                    checked="checked">
                  </span> <span> <s:text name='register.iris1' /> <a
                    href="/resscmwebsns/html/condition_${locale }.html" target="_blank" class="checkbox-detaile_btn"><s:text
                        name='register.iris2' /></a> <s:text name='register.iris3' /><a
                    href="/resscmwebsns/html/policy_${locale }.html" target="_blank" class="checkbox-detaile_btn"><s:text
                        name='register.iris4' /></a> <s:text name='register.iris5' />
                  </span>
                </p></li>
            </ul>
          </form>
        </div>
      </div>
    </div>
  </div>
  <div class="footer">
    <div class="footer__cont">
      
      <div class="footer__cont-title">关注科研之友</div>
      <div class="footer__cont--code">
        <div class="footer__cont--code_title">
          <s:text name="page.index.scan.scm" />
          <div class="footer__cont--code_avator">
             <img src="${resmod }/images_v5/images2016/code.jpg">
          </div>
        </div>
      </div>
      
      <div class="footer__cont--code">
        <div class="footer__cont--code_title">
                         科研之友APP
          <div class="footer__cont--code_avator">
             <img src="${resmod }/images_v5/images2016/code.jpg">
          </div>
        </div>
      </div>
      
      
      <div class="footer__cont--solution">
        <h3>
          <s:text name="page.index.solution" />
        </h3>
        <ul>
          <li class="footer_fun-selector"><a
            href="http://www.irissz.com/<s:text name="page.index.language"/>/government.html" target="_blank"><s:text
                name="page.index.technological.management" /></a></li>
          <li class="footer_fun-selector"><a
            href="http://www.irissz.com/<s:text name="page.index.language"/>/university.html" target="_blank"><s:text
                name="page.index.publication.promotion" /></a></li>
          <li class="footer_fun-selector"><a href="https://www.innocity.com/" target="_blank"><s:text
                name="page.index.technology.transfer" /></a></li>
        </ul>
      </div>
      <div class="footer__cont--search">
        <div id="divselect1" class="search fl" style="position: relative;">
          <span class="tp_box" id="show_select"><i id="search_type_img" class="lt_icon"></i></span>
          <ul id="home_search_items"
            style="display: none; width: 132px; border: 1px solid #dfdfdf; background-color: #fff; position: absolute; z-index: 6; top: 22px; left: -1px;">
            <li onclick="ScmMaint.select_search_paper()" class="select__item-list__container"><a
              class="select__item-list" href="javascript:void(0);" selectid="1"> <i class="lw_icon"></i> <span
                class="select__item-list__container-text"><s:text name='page.index.search.paper' /></span>
            </a></li>
            <li onclick="ScmMaint.select_search_patent()" class="select__item-list__container"><a
              class="select__item-list" a href="javascript:void(0)" selectid="2"> <i class="zl_icon"></i> <span
                class="select__item-list__container-text"><s:text name='page.index.search.patent' /></span>
            </a></li>
            <li onclick="ScmMaint.select_search_psn()" class="select__item-list__container"><a
              class="select__item-list" href="javascript:void(0)" selectid="3"> <i class="ry_icon"></i> <span
                class="select__item-list__container-text"><s:text name='page.index.search.person' /></span>
            </a></li>
            <li onclick="ScmMaint.select_search_ins()" class="select__item-list__container"><a
              class="select__item-list" href="javascript:void(0)" selectid="4"> <i class="mesm_icon"></i> <span
                class="select__item-list__container-text"><s:text name='page.index.search.institution' /></span>
            </a></li>
          </ul>
          <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get">
            <c:if test="${locale=='en_US'}">
              <input id="search_some_one" name="searchString" value=""
                placeholder="Enter keywords to search publications, patents, researchers, organization."
                title="Enter keywords to search publications, patents, researchers, organization." type="text">
            </c:if>
            <c:if test="${locale=='zh_CN'}">
              <input id="search_some_one" name="searchString" value="" placeholder="输入关键词检索论文、专利、专家、机构..."
                title="输入关键词检索论文、专利、专家、机构..." type="text">
            </c:if>
          </form>
          <a href="javascript:void(0)" class="s_btn" onclick="ScmMaint.searchSomeOne()"></a>
        </div>
        <ul style="padding-left: 42px;">
          <li style="text-align: left;"><a href="${domainrol }?locale=${locale }" target="_blank"><s:text
                name="page.index.institution" /></a></li>
          <li style="text-align: left;"><a href="/indexhtml/psn_maint_${locale }.html" target="_blank"><s:text
                name="page.index.psn" /></a></li>
          <li style="text-align: left;"><a href="/indexhtml/pub_A_${locale }.html" target="_blank"><s:text
                name="page.index.paper" /></a></li>
          <li style="text-align: left;"><a href="https://www.innocity.com/" target="_blank"><s:text
                name="page.index.patent" /></a></li>
        </ul>
      </div>
    </div>
    <div class="footer__bottom">
      <s:text name="page.foot.copyright" />
      <s:text name="page.foot.new.beian1" />
      <img src="${resscmsns}/images_v5/beian/beian.png" style="width: 12px;">
      <s:text name="page.foot.new.beian2" />
    </div>
  </div>
</body>
<script language="javascript">
//初始化数据
ScmIndex.initPageInfo();
Register.initCommonData();
ScmIndex.initCommonData();

</script>
</html>