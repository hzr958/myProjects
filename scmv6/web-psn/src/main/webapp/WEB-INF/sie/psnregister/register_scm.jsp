<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>申请加入</title>
<c:set var="o" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ressie}/css/plugin/scmpcframe.css" />
<link rel="stylesheet" href="${ressie}/css/administrator.css" />
<link rel="stylesheet" href="${ressie }/css/plugin/jquery.validate.css" />
<link rel="stylesheet" type="text/css" href="${ressie}/css/jquery.scmtips.css" />
<!-- confirm框 -->
<link rel="stylesheet" href="${ressie}/css/toast/toast.css" />
<!-- confirm框 -->
<script type="text/javascript" src="${ressie}/js/confirm.js"></script>
<script type="text/javascript" src="${ressie}/js/smate.toast.js"></script>
<script type="text/javascript" src="${ressie}/js/lib/common.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/restipboxlist.js"></script>
<script type="text/javascript" src="${ressie }/js/jquery.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/scmpc_form.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/jquery.autocomplete.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.complete.js"></script>
<script type="text/javascript" src="${resins}/person/jquery.region.js"></script>
<script type="text/javascript" src="${resins}/person/reigister_${locale}.js"></script>
<script type="text/javascript" src="${resins}/person/reigister.js"></script>
<script type="text/javascript" src="${resins}/person/rol.unit.autoComplete.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/browsercompatible.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.scmtips.js"></script>
<!-- 表单验证 -->
<script type="text/javascript" src="${ressie }/js/jquery.validate.js"></script>
<script type="text/javascript">
$(document).ready(function() {  
	Register.showMsg("${message}");	
    addFormElementsEvents($("#auto_input_div1")[0]); //输入框输入搜索提示调用
    jQuery.validator.addMethod("remoteEmail",Register.remoteEmail, "");
    $("#regForm").validate({
        errorPlacement: function(error, element) {//修改错误的显示位置.
            error.appendTo(element.closest(".targetlocal"));
        },
          rules : {
        	  email : {
                  required : true,
                  emailCheck : true ,
                  remoteEmail:{
                      url: "/psnweb/register/ajaxemailcheckscm",   
                   type: 'post',  
                  }
              },
        	  pwd : {
                  required : true,
              }              
            /*   validateCode : {
                  required : true,
                  minlength:4,
                  remote:"/psnweb/register/ajaxcheckedcode"
              } */
          },
          messages : {
        	  email : {
                  required : " 邮件不能为空",
                  emailCheck : "请输入正确的邮箱" 
              },
              pwd : {
                  required : "密码不能为空",
              }              
              /* validateCode : {
                  required : "验证码不能为空",
                  minlength: "验证码错误",
                  remote:"验证码错误"
              } */
          }
      });
    //添加自定义校验方法.
       //邮箱验证 
        jQuery.validator.addMethod("emailCheck",function(value, element,params) {
        	 value = $.trim(value.toLowerCase());
             var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
             return this.optional(element)||(checkEmail.test(value)); 
    }, "请输入正确的邮箱");
});
function sieSearch(){//输入框输入搜索提示选中后的回调函数
    
}

function refreshCode(){
    document.getElementById("img_checkcode").src = "/psnweb/validatecode.gif?"+ Math.random();
}
 </script>
</head>
<body>
  <!--banner部分-->
  <div class="sie_role_conter role_conter_bt">
    <input type="hidden" name="ins_name" id="ins_name" value="<c:out value="${userRolData.rolTitle}"></c:out>" />
    <form action="/psnweb/register/submitscmreg" method="POST" id="regForm">
      <div class="message_conter">
        <div id="con_three_1" style="display: block" class="message_conter_left administrator_conter_left fl">
          <div class="seek">
            <p class="headline_1">
              <s:text name="register.join.tip3">
                <s:param>${userRolData.rolTitle}</s:param>
              </s:text>
            </p>
            <div class="clear"></div>
          </div>
          <ul class="scientific_number menu pt10">
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container input-targetlocal_container-tip sie_targetlocal"
                style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.join.email" /></i><b>*</b></span>
                  <div class="matter fl">
                    <input type="text" name="email" id="email" value="${email }" maxlength="50"
                      style="margin-left: 0px; width: 300px !important;">
                  </div>
                </div>
              </div>
              <p class="clear"></p>
            </li>
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container sie_targetlocal" style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.join.password" /></i> <b>*</b></span>
                  <div class="matter fl">
                    <input type="password" name="pwd" id="pwd" value="" maxlength="16">
                  </div>
                </div>
              </div>
              <p class="clear"></p>
            </li>
            <li class="mt10  input_container-list input_container-list_bt "><span class="fl"><s:text
                  name="register.join.unit" /></span>
              <div id="auto_input_div1">
                <input type="text" name="unitName" id="unitName" value="" maxlength="200" class="js_autocompletebox"
                  request-url="/psnweb/register/ajaxinsunitac" code="" manual-input="no" /> <input type="hidden"
                  name="unitId" id="unitId" value="" />
              </div></li>
            <!-- <li><span class="fl">验证码：</span>
                <div class="targetlocal matter fl matter_import sie_targetlocal">
                    <input id="validateCode" name="validateCode" type="text" />
                    <img id="img_checkcode" class="img_checkcode_bt" align="absmiddle" src="/psnweb/validatecode.gif" alt="验证码" />
                    <a style="color:#005eac!important;text-decoration:underline!important;;height: 30px;line-height: 37px;" href="javascript:void(0);" onclick="refreshCode();" style="cursor:pointer" onclick="">看不清楚换一张</a>   
                </div>
                 <p class="clear"></p>
                </li> -->
            <div class="refer">
              <input type="submit" id="regSubmit" value="<s:text name="register.action.sumbit" />"
                class="submit_btn submit-btn_box refer_1"> <a href="#"
                onclick="javascript:location.href='/psnweb/index'" class="refer_2"><s:text name="register.join.back" /></a>
            </div>
            <div class="clear"></div>
          </ul>
        </div>
      </div>
    </form>
  </div>
</body>
</html>
