<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="keywords" content='<s:text name="page.seo.index.Keywords"/>' />
<meta name="description" content='<s:text name="page.seo.index.description"/>' />
<meta name="robots" content='<s:text name="page.seo.index.robots"/>' />
<title><s:text name="page.seo.index.title" /></title>
<link href="${resmod }/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.validate.min.js"></script>
  <script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmemail_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript">
var locale='${locale}';
$(document).ready(function() {
    var domain = document.domain;
    var httpsUrl = "https://" + domain + "/oauth/pc/register/sava";
    $("#regForm").attr("action",httpsUrl);

	$(":input").blur(function(){
		$(this).val($.trim($(this).val()));
	});
    $("#email").focusout(function(){
        setTimeout(function () {
            var flag = $("#regForm").validate().element($("#email"));
        },500);
    });
 $("#regForm").validate({
		errorPlacement: function(error, element) {//修改错误的显示位置. 
			//checkbox换行显示
			if (element.is(':checkbox')){
				 element.parent().addClass("targetloca-input_container-tipborder");
				 element.closest(".targetloca-input_container-tipborder").find(".error_message-prompt_detail").html(error.html());
			}
			else{
				if(element[0].name ==="email"){
					//element = $("#email");
				}
                element.closest(".dev_message_class").addClass("targetloca-input_container-tipborder");
                element.closest(".dev_message_class").find(".error_message-prompt_detail").html(error.html());
				element.closest(".dev_message_class").find(".error_message-prompt").css("display","block");
			}
        },
       success: function(label ,element) {
           $(element).closest(".dev_message_class").removeClass("targetloca-input_container-tipborder");
           $(element).closest(".dev_message_class").find(".error_message-prompt").css("display","none");
       },
		rules: { 
			name: {
				required: true,
				maxlength:61
			},  
			zhlastName: { 
				required: true,
				nameCheck:true,
				maxlength:20
			}, 
			zhfirstName: { 
				required: true,
				nameCheck:true,
				maxlength:40
			}, 
			lastName: { 
				required: true,
				nameCheck:true,
				maxlength:20
			}, 
			firstName: { 
				required: true,
				nameCheck:true,
				maxlength:40
			}, 
			email:{ 
				required: true, 
				checkEmail: true,
				remote: "/oauth/register/ajaxCheckedUserName",
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
            newpassword: {
				required: true,
				minlength:6,
				maxlength:40
			}, 
			checkedall:{
				required:true
			}
		},
		messages: {
			name: {
			    required: "<s:text name='register.name.zh' /><s:text name='register.required' />",
			    maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			zhlastName: {
				    required: "<s:text name='register.required.not.blank' />",
				    maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
				},
			zhfirstName: {
			    required: "<s:text name='register.required.not.blank' />",
			    maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			lastName: {
			    required: "<s:text name='register.required.not.blank' />",
			    maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			firstName: {
			    required: "<s:text name='register.required.not.blank' />",
			    maxlength: jQuery.validator.format("<s:text name='register.maxlength' />")
			},
			email: {
			    required: "<s:text name='register.email' /><s:text name='register.required' />",
			    checkEmail: "<s:text name='register.isemail' />",
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
 Register.checkIp();
})

//邮件验证.
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

</script>
</head>
<jsp:include page="/common/header.jsp"></jsp:include>
<div class="content-1200 content-special__style">
  <div class="clear_h20"></div>
  <div class="fillin-left">
    <h1>
      <s:text name="register.label.v5.left" />
    </h1>
    <form action="/oauth/pc/register/sava" method="POST" id="regForm">
      <input type="hidden" id="token" name="token" value="${token}" /> <input type="hidden" id="forwardAction"
        name="forwardAction" value="${forward}" /> <input type="hidden" id="key" name="key" value="${key}" /> <input
        type="hidden" id="back" name="back" value="${ back}" /> <input type="hidden" id="targetUrl" name="targetUrl"
        value="${ targetUrl}" /> <input type="hidden" id="thirdSysName" name="thirdSysName" value="${ thirdSysName}" />
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="fillin__eidt--tab mt20">
        <tr>
          <td align="right" width="14%">
             <span>
                <span class="text_login-Required">*</span>
                <span><s:text name='register.email' />：</span>
             </span>      
          </td>
          <!-- 防止浏览器表单自动填充  Start -->
          <input type="text" name="email" disabled style="display: none;" />
          <input type="text" name="newpassword" disabled style="display: none;" />
          <!--/ 防止浏览器表单自动填充 End -->
          <td>
              <div style="min-width: 0px; width: auto; position: relative;" class="dev_message_class">
                <input type="text" class="input w300 input_default blcakcolor" value="" style="width: 370px;"
                 maxlength="50" placeholder="<s:text name='register.email.friend.tip' />" id="email" name="email"
                 onblur="$(this).val($.trim($(this).val()));">
                 <div class="error_message-prompt error_message-rightprompt" style=" right: -9px; top: 0px;">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">邮箱不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
              </div>
           </td>
        </tr>
        <tr>
          <td align="right">
              <span>
                  <span class="text_login-Required">*</span>
                  <span><s:text name='register.password' />：</span>
              </span> 
          </td>
          <td>
              <div style="min-width: 0px; width: auto; position: relative;" class="dev_message_class">
                  <input type="password" class="input w300 input_default blcakcolor" value="" style="width: 370px;"
                    maxlength="40" placeholder="<s:text name='register.password.friend.tip' />" id="newpassword"
                    name="newpassword">
                    <div class="error_message-prompt error_message-rightprompt" style=" right: -9px; top: 0px;">
                        <div class="error_message-prompt_side error_message-prompt_rightside">
                            <div class="error_message-prompt_detail">邮箱不能为空</div>
                            <div class="error_message-prompt_icon-right"></div>
                        </div>
                    </div>
               </div>
            </td>
        </tr>
        <tr id="mobileNumberDiv">
          <td align="right">
              <span>
                  <span class="text_login-Required">*</span>
                  <span><s:text name="register.mobile"/> </span>
               </span>
         </td>
          <td >
             <div style="display: flex; align-items: center; width: auto; position: relative;" class="dev_message_class">
                <input type="text" class="input w300 input_default blcakcolor" value="" style="width: 265px;"
                     maxlength="11" placeholder="" id="mobileNumber"  name="mobileNumber">
                <span class="get-mobile_phonecode"  onclick="Register.sendMobileCode(this);"><a><s:text name="register.get.validate.code"/> </a></span>
                 <div class="error_message-prompt error_message-rightprompt" style=" right: -9px; top: 0px;">
                     <div class="error_message-prompt_side error_message-prompt_rightside">
                          <div class="error_message-prompt_detail">邮箱不能为空</div>
                          <div class="error_message-prompt_icon-right"></div>
                     </div>
                </div>
             </div>
          </td>
        </tr>
       <tr id="mobileVerifyCodeDiv">
          <td align="right">
              <span>
                 <span class="text_login-Required">*</span>
                 <span><s:text name="register.validate.code"/> </span>
              </span>
          </td>
          <td>
              <div style="min-width: 0px; width: auto; position: relative;" class="dev_message_class">
                <input type="text" class="input w300 input_default blcakcolor" value="" style="width: 100px;"
                     maxlength="6" placeholder="" id="mobileVerifyCode"
                     name="mobileVerifyCode">
                <div class="error_message-prompt error_message-rightprompt" style="right: 260px; top: 0px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                          <div class="error_message-prompt_detail">邮箱不能为空</div>
                          <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
             </div>
          </td>
        </tr>
        <tr>
          <td align="right">
             <span>
                <span class="text_login-Required">*</span>
                <span><s:text name='register.name.zh' />：</span>
             </span>
          </td>
          <td>
            <div style="display: flex; width: auto; min-width: 0px; position: relative;" class="dev_message_class">
              <div>
                <input type="text" class="input w300 input_default firstname blcakcolor" value="" style="width: 175px;"
                  maxlength="20" placeholder="<s:text name='register.zhlastName'/>" id="zhlastName" name="zhlastName">
              </div>
              <div>
                <input type="text" class="input w300 input_default lastname blcakcolor" value="" maxlength="40"
                  style="width: 175px;" placeholder="<s:text name='register.zhfirstName'/>" id="zhfirstName"
                  name="zhfirstName">
              </div>
              <div class="error_message-prompt error_message-rightprompt" style="right: -9px; top: 0px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                          <div class="error_message-prompt_detail">邮箱不能为空</div>
                          <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
          </td>
        </tr>
        <tr>
          <td align="right" style="width: 24%;">
            <span> 
               <span class="text_login-Required">*</span>
               <span><s:text name='register.name.en' />：</span>
            </span> 
          </td>
          <td>
            <div style="display: flex; width: auto; min-width: 0px; position: relative;" class="dev_message_class">
              <div>
                <input type="text" class="input w300 input_default firstname blcakcolor" value="" maxlength="20"
                  style="width: 175px;" placeholder="<s:text name='register.lastName'/>" id="lastName" name="lastName">
              </div>
              <div>
                <input type="text" class="input w300 input_default lastname blcakcolor" value="" maxlength="40"
                  style="width: 175px;" placeholder="<s:text name='register.firstName'/>" id="firstName"
                  name="firstName">
              </div>
              <div class="error_message-prompt error_message-rightprompt" style=" right: -9px; top: 0px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                          <div class="error_message-prompt_detail">邮箱不能为空</div>
                          <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
          </td>
        </tr>
        <tr>
          <td align="right">&nbsp;</td>
          <td height="40" align="left"><input type="checkbox" id="checkedall" disabled="disabled" name="checkedall" checked="checked">
            <label for="checkbox">&nbsp; <s:text name='register.iris1' /> <a target="_blank"
              class="Blue checkbox-detaile_btn" href="/resmod/html/condition_${locale }.html"> <s:text
                  name='register.iris2' />
            </a> <s:text name='register.iris3' /> <a target="_blank" class="Blue checkbox-detaile_btn"
              href="/resmod/html/policy_${locale }.html"> <s:text name='register.iris4' />
            </a> <s:text name='register.iris5' />
          </label></td>
        </tr>
        <tr>
          <td align="right"></td>
          <td><input type="submit" onclick=" Register.submit(this);" class="blue_btn mw100"
            value='<s:text name="register.action.submit"/>'></td>
        </tr>
      </table>
    </form>
  </div>
  <div class="fillin-right">
    <h2>
      <s:text name="register.join" />
    </h2>
    <ul>
      <li><i class="icon-join-related01"></i> <s:text name="register.join.help8" /></li>
      <li><i class="icon-join-related02"></i> <s:text name="register.join.help9" /></li>
      <li><i class="icon-join-related03"></i> <s:text name="register.join.help10" /></li>
    </ul>
  </div>
  <div class="clear_h20"></div>
</div>
<jsp:include page="/common/footer.jsp"></jsp:include>
<script language="javascript">
//初始化数据
Register.initCommonData();
</script>
</html>
