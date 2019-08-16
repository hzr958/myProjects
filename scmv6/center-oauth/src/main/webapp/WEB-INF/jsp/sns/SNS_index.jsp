<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><s:text name="page.index.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="${resmod }/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/index.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/jquery.validate.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript" src="${resmod}/js/index/pc.scm.index.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript">var locale='${locale}';
var searchTypeTip="输入关键词检索论文、专利、专家、机构... ";
var searchTypeTip_en_US="Enter keywords to search publications, patents, researchers, organization.";
var searchType="<s:text name='page.main.search' />";
var searchType2="<s:text name='page.main.search2'/>";
var searchType3="<s:text name='page.main.search3'/>";
$(document).ready(function() {
	var foculist = document.getElementsByClassName("text_login");
	for(var i = 0; i<foculist.length;i++ ){
		foculist[i].onfocus = function(obj){
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
			//checkbox换行显示
			if (element.is(':checkbox')) {
				$('<br/>').appendTo(element.parent());
				error.appendTo(element.parent());
			} else {
				/* error.appendTo(element.parent()); */
				error.appendTo(element.closest(".targetloca"));
			}
		},
		rules : {
			name : {
				required : true,
				maxlength : 61
			},
			zhlastName : {
				required : true,
				maxlength : 20
			},
			zhfirstName : {
				required : true,
				maxlength : 40
			},
			lastName : {
				required : true,
				maxlength : 20
			}, 
			firstName : {
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
				maxlength : jQuery.format("<s:text name='register.maxlength' />")
			},
			zhlastName : {
				/* required : "<s:text name='register.zhlastName' /><s:text name='register.required' />", */
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.format("<s:text name='register.maxlength' />")
			},
			zhfirstName : {
				/* required : "<s:text name='register.zhfirstName' /><s:text name='register.required' />", */
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.format("<s:text name='register.maxlength' />")
			},
			lastName : {
				/* required : "<s:text name='register.lastName' /><s:text name='register.required' />", */
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery.format("<s:text name='register.maxlength' />")
			},
			firstName : {
				/* required : "<s:text name='register.firstName' /><s:text name='register.required' />", */
				required : "<s:text name='register.required.not.blank' />",
				maxlength : jQuery
						.format("<s:text name='register.maxlength' />")
			},
			email : {
				required : "<s:text name='register.email' /><s:text name='register.required' />",
				emailCheck : "<s:text name='register.isemail' />",
				maxlength : jQuery
						.format("<s:text name='register.maxlength' />"),
				remote : "<s:text name='register.email.isregister' />"
			},
			newpassword : {
				required : "<s:text name='register.password' /><s:text name='register.required' />",
				minlength : jQuery.format("<s:text name='register.minlength' />"),
				maxlength : jQuery.format("<s:text name='register.maxlength' />")
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
  <div class="top-t">
    <div class="header-t">
      <div class="header_wrap-t">
        <a href="/" class="logo fl"></a>
        <div class="rssie_log" style="display: none;">
          <img class="rssie_sub-logo" src="${resmod}/smate-pc/img/newlogo_bt.png"> <a class="rssie_sub-select">
            <!--存放选择的值--> <c:if test="${empty rolTitle }">
              <span class="rssie_sub-selectVal">科研之友</span>
            </c:if> <c:if test="${!empty rolTitle }">
              <span class="rssie_sub-selectVal">${rolTitle}</span>
            </c:if>
            <div class="clear"></div>
          </a>
        </div>
        <div id="divselect1" class="search fl">
          <span class="tp_box" id="show_select"><i id="search_type_img" class="lt_icon"></i></span>
          <ul id="home_search_items" style="display: none">
            <li onclick="ScmMaint.select_search_paper()"><a href="javascript:void(0);" selectid="1"><i
                class="lw_icon"></i> <s:text name='page.index.search.paper' /></a></li>
            <li onclick="ScmMaint.select_search_patent()"><a href="javascript:void(0)" selectid="2"><i
                class="zl_icon"></i> <s:text name='page.index.search.patent' /></a></li>
            <li onclick="ScmMaint.select_search_psn()"><a href="javascript:void(0)" selectid="3"><i
                class="ry_icon"></i> <s:text name='page.index.search.person' /></a></li>
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
        <div class="logining">
          <div class="logining_icon">
            <c:if test="${locale == 'zh_CN'}">
              <a href="/resscmwebsns/html/helpCenter/index.html"> <i class="question_icon" style="cursor: pointer;"></i>
              </a>
            </c:if>
            <c:if test="${locale != 'zh_CN'}">
              <a href="/resscmwebsns/html/helpCenter/index_en_US.html"> <i class="question_icon"
                style="cursor: pointer;"></i>
              </a>
            </c:if>
          </div>
          <div class="version_en">
            <c:if test="${locale == 'zh_CN'}">
              <a onclick="Register.change_locol_language('en_US');">&nbsp;English&nbsp;</a>
            </c:if>
            <c:if test="${locale != 'zh_CN'}">
              <a onclick="Register.change_locol_language('zh_CN');">&nbsp;中文&nbsp;</a>
            </c:if>
          </div>
        </div>
      </div>
    </div>
    <div class="nav-t">
      <div class="nav_wrap-t">
        <ul>
          <li><c:if test="${locale == 'zh_CN'}">
              <a href="/resscmwebsns/html/helpCenter/index.html"> <s:text name="page.index.label.tips2" />
              </a>
            </c:if> <c:if test="${locale   != 'zh_CN'}">
              <a href="/resscmwebsns/html/helpCenter/index_en_US.html"> <s:text name="page.index.label.tips2" />
              </a>
            </c:if></li>
          <li><a href="/oauth/pc/register"> <s:text name="page.index.label.tips3" />
          </a></li>
        </ul>
      </div>
    </div>
  </div>
  <div class="content-1200" style="height: 640px;">
    <div class="login-box">
      <div class="login_wrap">
        <div class="login_slogan">
          <c:choose>
            <c:when test="${locale == 'zh_CN' }">
              <div class="sm_banner sm__background-cover__zh">
                <%-- <img src="${resscmsns}/images_v5/index/banner_zh_CN.jpg" width="540" height="329" border="0" usemap="#Map" /> --%>
                <map name="Map" id="Map">
                  <area shape="rect" coords="278,33,322,43" href="${resscmsns}/html/helpCenter/Research_group.html"
                    target="_blank" title="<s:text name="page.index.tips.img1"/>" />
                  <area shape="rect" coords="201,55,253,69" target="_blank"
                    href="${resscmsns}/html/helpCenter/Friends.html" title="<s:text name="page.index.tips.img2"/>" />
                  <area shape="rect" coords="350,65,417,77" target="_blank"
                    title="<s:text name="page.index.tips.img3"/>" />
                  <area shape="rect" coords="60,170,140,200" target="_blank"
                    href="${resscmsns}/html/helpCenter/MyHomePage.html" title="<s:text name="page.index.tips.img6"/>" />
                  <area shape="rect" coords="278,166,349,182" href="${resscmsns}/html/helpCenter/MyResume.html"
                    target="_blank" title="<s:text name="page.index.tips.img7"/>" />
                </map>
              </div>
            </c:when>
            <c:otherwise>
              <div class="sm_banner sm__background-cover__en">
                <%-- <img src="${resscmsns}/images_v5/index/banner_en_US.jpg" width="540" height="329" border="0" usemap="#Map" /> --%>
                <map name="Map" id="Map">
                  <area shape="rect" coords="277,23,323,46"
                    href="${resscmsns}/html/helpCenter/Research_group_en_US.html" target="_blank"
                    title="<s:text name="page.index.tips.img1"/>" />
                  <area shape="rect" coords="182,54,256,81" target="_blank"
                    href="${resscmsns}/html/helpCenter/Friends_en_US.html" title="<s:text name="page.index.tips.img2"/>" />
                  <area shape="rect" coords="354,58,416,84" target="_blank"
                    title="<s:text name="page.index.tips.img3"/>" />
                  <area shape="rect" coords="60,170,149,200" target="_blank"
                    href="${resscmsns}/html/helpCenter/MyHomePage_en_US.html "
                    title="<s:text name="page.index.tips.img6"/>" />
                  <area shape="rect" coords="278,157,356,191" href="${resscmsns}/html/helpCenter/MyResume_en_US.html"
                    target="_blank" title="<s:text name="page.index.tips.img7"/>" />
                </map>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="login_cont checkbox-con">
          <h2>
            <a href="javascript:void(0);" class="hover" id="select_login_id"
              onclick="ScmIndex.changeModel('login' , this)"> <s:text name="page.index.login" />
            </a> <a href="javascript:void(0);" id="select_register_id" onclick="ScmIndex.changeModel('register' , this)">
              <s:text name="page.index.reg" />
            </a>
          </h2>
          <form action="${domainscm}/oauth/login" id="loginForm" method="post">
            <input type="hidden" value="${sys }" id="sys" name="sys" /> <input type="hidden" value="${service }"
              id="service" name="service" /> <input type="hidden" value="${SYSID }" id="SYSSID" name="SYSSID">
            <ul id="login_form_ul">
              <li class="tip__container-flag" style="display: flex;">
                <div class="admin_bg text_login-tip"
                  style="width: 43px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div> <input
                type="text" name="userName" value="${username}" maxlength="50"
                placeholder="<s:text name="register.email"/>" id="username" class="text_login text__input-container"
                style="border-left-style: none; width: 333px; padding-left: 4px;" />
              </li>
              <li class="tip__container-flag" style="display: flex;">
                <div class="password_bg text_login-tip"
                  style="width: 43px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div> <input
                type="password" name="password" value="${password }" maxlength="40"
                placeholder="<s:text name="register.password"/>" id="password" class="text_login text__input-container"
                style="border-left-style: none; width: 333px; padding-left: 4px;" />
              </li>
              <li><a href="/oauth/pwd/forget/index?returnUrl=${domainscm}/oauth/index" class="blue1 fr"> <s:text
                    name="page.index.forgotpassword" />
              </a></li>
              <li><input type="submit" value='<s:text name="page.index.submit.login"/>' class="login_btn"
                onclick="beforeSubmit();">
                <p class="other_l">
                  <a href="javascript:void(0);"> <s:text name="page.third.login" />
                  </a> <a onclick="loginQQ()"> <img src="${resscmsns}/images_v5/third_login/qq_login.png" alt="QQ" />
                  </a>
                </p></li>
            </ul>
          </form>
          <!--  注册页面 -->
          <form action="${domainscm}/oauth/pc/register/sava" method="POST" id="regForm">
            <input type="hidden" id="token" name="token" value="${token}" /> <input type="hidden" value="${service }"
              name="service" />
            <ul id="register_form_ul" style="display: none;">
              <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                <div style="display: flex; align-items: center;">
                  <div class="email_bg text_login-tip"
                    style="width: 45px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div>
                  <input type="text" value="" maxlength="50" placeholder="<s:text name='register.email.friend.tip' />"
                    title="<s:text name='register.email.friend.tip' />" class="text_login text__input-container"
                    id="email" name="email" style="border-left-style: none; width: 333px; padding-left: 4px;" /> <span
                    class="text_login-Required">*</span>
                </div>
              </li>
              <li class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                <div style="display: flex; align-items: center;">
                  <div class="password_bg text_login-tip"
                    style="width: 45px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div>
                  <input type="password" value="" maxlength="40"
                    placeholder="<s:text name='register.password.friend.tip' />"
                    class="text_login text__input-container" id="newpassword" name="newpassword"
                    style="border-left-style: none; width: 333px; padding-left: 4px;" /> <span
                    class="text_login-Required">*</span>
                </div>
              </li>
              <li>
                <div style="display: flex;">
                  <div class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                    <div style="display: flex;">
                      <div class="chinese_bg text_login-tip"
                        style="width: 45px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div>
                      <input type="text" value="" placeholder="<s:text name='register.zhlastName'/>" id="zhlastName"
                        name="zhlastName" class="text_login text__input-container" maxlength="20"
                        style="border-left-style: none; width: 122px; padding-left: 6px;" /> <span
                        class="text_login-Required">*</span>
                    </div>
                  </div>
                  <div class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                    <div style="display: flex;">
                      <div class="chinese_bg text_login-tip"
                        style="width: 45px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div>
                      <input type="text" value="" maxlength="40" placeholder="<s:text name='register.zhfirstName'/>"
                        id="zhfirstName" name="zhfirstName" class="text_login text__input-container"
                        style="border-left-style: none; width: 122px; padding-left: 6px;" /> <span
                        class="text_login-Required">*</span>
                    </div>
                  </div>
                </div>
              </li>
              <li>
                <div style="display: flex;">
                  <div class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                    <div style="display: flex;">
                      <div class="english_bg text_login-tip"
                        style="width: 45px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div>
                      <input type="text" value="" maxlength="20" placeholder="<s:text name='register.lastName'/>"
                        title="<s:text name='register.lastName'/>" id="lastName" name="lastName"
                        class="text_login text__input-container"
                        style="border-left-style: none; width: 122px; padding-left: 4px;" /> <span
                        class="text_login-Required">*</span>
                    </div>
                  </div>
                  <div class="targetloca tip__container-flag" style="display: flex; flex-direction: column;">
                    <div style="display: flex;">
                      <div class="english_bg text_login-tip"
                        style="width: 45px; height: 44px; border: 1px solid #ddd; border-right-style: none;"></div>
                      <input type="text" value="" maxlength="40" placeholder="<s:text name='register.firstName'/>"
                        title="<s:text name='register.firstName'/>" id="firstName" name="firstName"
                        class="text_login text__input-container"
                        style="border-left-style: none; width: 122px; padding-left: 4px;" />
                      <div class="text_login-Required">*</div>
                    </div>
                  </div>
                </div>
              </li>
              <li><input type="submit" id="regSubmit" value="<s:text name="register.action.submit"/>"
                onclick=" Register.submit(this);" class="login_btn">
                <p class="other_l">
                  <span class="check_fx"> <input type="checkbox" id="checkedall" name="checkedall"
                    checked="checked"> <label for="checkbox"> &nbsp; <s:text name='register.iris1' /> <a
                      target="_blank" class="Blue" href="/resscmwebsns/html/condition_${locale }.html"> <s:text
                          name='register.iris2' />
                    </a> <s:text name='register.iris3' /> <a target="_blank" class="Blue"
                      href="/resscmwebsns/html/policy_${locale }.html"> <s:text name='register.iris4' />
                    </a> <s:text name='register.iris5' />
                  </label>
                </p></li>
            </ul>
          </form>
        </div>
      </div>
    </div>
  </div>
  <!-- <div class="clear_h40"></div> -->
  <jsp:include page="/common/footer.jsp"></jsp:include>
</body>
<script language="javascript">
//初始化数据
ScmIndex.initPageInfo();
Register.initCommonData();
ScmIndex.initCommonData();

</script>
</html>