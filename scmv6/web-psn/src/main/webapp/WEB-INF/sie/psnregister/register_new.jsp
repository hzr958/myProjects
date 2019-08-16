<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>申请加入</title>
<link rel="stylesheet" href="${ressie}/css/plugin/scmpcframe.css" />
<link rel="stylesheet" href="${ressie}/css/administrator.css" />
<link rel="stylesheet" href="${ressie }/css/plugin/jquery.validate.css" />
<link rel="stylesheet" type="text/css" href="${ressie}/css/jquery.scmtips.css" />
<!-- 公共js -->
<script type="text/javascript" src="${ressie}/js/lib/common.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/restipboxlist.js"></script>
<script type="text/javascript" src="${ressie }/js/jquery.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/scmpc_form.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/jquery.autocomplete.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.complete.js"></script>
<script type="text/javascript" src="${resins}/person/jquery.region.js"></script>
<script type="text/javascript" src="${resins}/person/reigister.js"></script>
<script type="text/javascript" src="${resins}/person/reigister_${locale}.js"></script>
<script type="text/javascript" src="${resins}/person/rol.unit.autoComplete.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/browsercompatible.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.scmtips.js"></script>
<!-- 表单验证 -->
<script type="text/javascript" src="${ressie }/js/jquery.validate.js"></script>
<script type="text/javascript">
$(document).ready(function() {  
    addFormElementsEvents($("#auto_input_div")[0]);//输入框输入搜索提示调用
    addFormElementsEvents($("#auto_input_div1")[0]); //输入框输入搜索提示调用
	Register.bindPinyin("/psnweb","${locale}");
	jQuery.validator.addMethod("remoteEmail",Register.remoteEmail, "");
	var tokeId = "${tokeId}";
    var regionComponent = null;
    regionComponent = $("#country,#province,#city").region({"locale":"${locale}","ctx":"/psnweb"});
    $("#regForm").validate({
        errorPlacement: function(error, element) {//修改错误的显示位置.
            error.appendTo(element.closest(".targetlocal"));
        },
          rules : {
        	  email : {
                  required : true,
                  emailCheck : true,
                  remoteEmail:{
                      url: "/psnweb/register/ajaxemailcheck",   
                   type: 'post',  
                  }
              },
        	  pwd : {
                  required : true,
                  minlength: 6
              },
              rpwd : {
            	  equalTo: "#pwd"
              },
              zhName : {
                  required : true,
                  minlength: 2,
                  maxlength: 50
              },
              lastName : {
                  required : true,
                  maxlength:20
              },
              firstName : {
                  required : true,
                  maxlength:40,
                  first_last_name:true
              }
          },
          messages : {
        	  email : {
                  required : " 邮件不能为空",
                  emailCheck : "请输入正确的邮箱" 
              },
              pwd : {
            	  required:'<s:text name="register.write.required.password"/>',
                  minlength: $.format('<s:text name="register.write.password.empty"/>')
              },
              rpwd : {
            	  equalTo: '<s:text name="register.write.rpassword.error"/>'
              },
              zhName : {
            	  required: '<s:text name="register.write.zhname.empty"/>',
                  minlength: $.format('<s:text name="register.write.zhname.less"/>'),
                  maxlength: $.format('<s:text name="register.write.zhname.max"/>')
              },
              lastName : {
            	  required: '<s:text name="register.write.lastname.empty"/>',
                  maxlength: $.format('<s:text name="register.write.lastname.max"/>')
              },
              firstName : {
            	  required: '<s:text name="register.write.firstname.empty"/>',
                  maxlength: $.format('<s:text name="register.write.firstname.max"/>'),
                  first_last_name:'<s:text name="register.write.firstlastname.max"/>'
              }
          },submitHandler: function(form) {
              $("#regSubmit").attr("disabled","disabled");            
              var postData = $("#regForm").serialize();  
              $.ajax( {
                  url : '/psnweb/register/ajaxsubmitreg',
                  type : 'post',
                  dataType:'json',
                  data : postData,
                  success : function(data) {
                      if(data.result == 'error'){
                          tokeId = data.tokeId;
                          $.scmtips.show('warn',data.msg);
                      }else{
                          location.href="/psnweb/register/success";
                      }
                  },
                  error:function(){
                	  $.scmtips.show('error','保存出错');
                  }
              });
              return false;
          }
      });
    //添加自定义校验方法.
       //邮箱验证 
        jQuery.validator.addMethod("emailCheck",function(value, element,params) {
        	 value = $.trim(value.toLowerCase());
             var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
             return this.optional(element)||(checkEmail.test(value)); 
    }, "请输入正确的邮箱");        
      //姓验证.
       jQuery.validator.addMethod("first_last_name", function(value, element) {
            var firstName = $("#firstName").val();
            var lastName = $("#lastName").val();
            var fullName = firstName +" "+ lastName;
            if(fullName.length > 61)
                return false;
            return true;    
        }, "");
});
function sieSearch(){//输入框输入搜索提示选中后的回调函数
    
}
 </script>
</head>
<body>
  <div class="message">
    <div class="message_conter">
      <input type="hidden" name="ins_name" id="ins_name" value="<c:out value="${userRolData.rolTitle}"></c:out>" />
      <form action="" method="POST" id="regForm">
        <div class="message_conter_left fl">
          <div class="seek">
            <p class="headline_1">
              <s:text name="register.join.tip1">
                <s:param>${userRolData.rolTitle}</s:param>
              </s:text>
              <span><s:text name="register.join.tip2" /></span>
            </p>
            <p class="headline_2 fr mb10">
              <s:text name="register.join.already">
                <s:param>
                  <a href="/psnweb/register/scmregister"
                    style="color: #1265cf !important; text-decoration: underline !important;"><s:text
                      name="register.join.apply" /></a>
                </s:param>
              </s:text>
            </p>
            <div class="clear"></div>
          </div>
          <input type="hidden" name="tokeId" value="${tokeId }" />
          <ul class="menu pt10">
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container sie_targetlocal" style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.join.email" /></i><b>*</b></span>
                  <div class="matter fl">
                    <input type="text" name="email" id="email" maxlength="50">
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
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container sie_targetlocal" style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.join.renewpassword" /></i><b>*</b> </span>
                  <div class="matter fl">
                    <input type="password" name="rpwd" id="rpwd" value="" maxlength="16">
                  </div>
                </div>
              </div>
              <p class="clear"></p>
            </li>
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container sie_targetlocal" style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.join.name" /></i><b>*</b></span>
                  <div class="matter fl">
                    <input type="text" name="zhName" id="zhName" maxlength="50">
                  </div>
                </div>
              </div>
              <p class="monicker ">（例：陈小明）</p>
            </li>
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container sie_targetlocal" style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.lastname" /></i><b>*</b> </span>
                  <div class="matter fl">
                    <input type="text" name="lastName" id="lastName" maxlength="20">
                  </div>
                </div>
              </div>
              <p class="monicker ">（例：chen）</p>
            </li>
            <li style="justify-content: flex-start;">
              <div class="targetlocal input-targetlocal_container sie_targetlocal" style="width: 420px;">
                <div>
                  <span class="fl"><i><s:text name="register.join.firstname" /></i><b>*</b> </span>
                  <div class="matter fl">
                    <input type="text" name="firstName" id="firstName" maxlength="40">
                  </div>
                </div>
              </div>
              <p class="monicker ">（例：xiao ming）</p>
            </li>
            <li class="mt10 "><span class="fl"><s:text name="register.join.location" /></span> <select
              name="country" id="country" class="selectVal">
            </select> <select name="province" id="province" class="inp_text" style="display: none;"></select> <select name="city"
              id="city" class="inp_text" style="display: none;"></select> <input type="hidden" id="region_id"
              name="region_id" value="" />
              <p class="clear"></p></li>
            <li class="mt10"><span class="fl"><s:text name="register.join.position" /></span>
              <div id="auto_input_div">
                <input type="text" name="position" id="position" value="" maxlength="200" class="js_autocompletebox"
                  request-url="/psnweb/register/ajaxpositionac" code="" manual-input="no" /> <input type="hidden"
                  name="posId" id="posId" value="" />
              </div></li>
            <li class="mt10"><span class="fl"><s:text name="register.join.unit" /></span>
              <div id="auto_input_div1">
                <input type="text" name="unitName" id="unitName" value="" maxlength="200" class="js_autocompletebox"
                  request-url="/psnweb/register/ajaxinsunitac" code="" manual-input="no" /> <input type="hidden"
                  name="unitId" id="unitId" value="" />
              </div> <!--  <p class="clear"></p> --></li>
          </ul>
          <div class="refer">
            <input type="submit" id="regSubmit" value="<s:text name="register.action.sumbit" />"
              class="submit_btn submit-btn_box refer_1"> <a href="#"
              onclick="javascript:location.href='/psnweb/index'" class="refer_2"><s:text name="register.join.back" /></a>
          </div>
        </div>
      </form>
      <div class="message_conter_right fl">
        <h1 class="seek ft16 pt15">检索与发现项目、论文、专利</h1>
        <div class="seek_1 pd15">
          <p class="f666 ftbold">面向单位管理人员：</p>
          <p>跨库自动收集数据</p>
          <p>管理单位科研数据</p>
        </div>
        <h1 class="seek ft16 pt15">分析与管理科研信息</h1>
        <div class="seek_1 pd15">
          <p class="f666 ftbold">面向单位领导：</p>
          <p>大数据决策分析工具</p>
          <p>年度科研报告</p>
        </div>
        <h1 class="seek ft16 pt15">发布与推广科研成果</h1>
        <div class="seek_1 pd15">
          <p class="f666 ftbold">面向社会、公众用户：</p>
          <p>展示科研项目</p>
          <p>展示科研成果</p>
        </div>
      </div>
    </div>
    <div class="clear"></div>
  </div>
</body>
</html>
