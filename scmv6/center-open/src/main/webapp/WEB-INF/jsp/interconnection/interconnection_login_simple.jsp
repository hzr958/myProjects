<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友帐号关联</title>
<link rel="stylesheet" href="/resmod/interconnection/css/style.css" type="text/css" />
<link rel="stylesheet" href="/resmod/css/jquery.validate.css" type="text/css" />
<script type="text/javascript" src="/resmod/interconnection/js/action.js"></script>
<script type="text/javascript" src="/resmod/interconnection/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/resmod/interconnection/js/smate.cross.js"></script>
<script type="text/javascript" src="/resmod/js/xdomainrequest/jquery.xdomainrequest.min.js"></script>
<style type="text/css">
#tab {
  float: inherit !important;
}

.selected-similar_account-number {
  border: 2px solid #218aed;
  border-radius: 3px;
}
</style>
<script type="text/javascript">
  var isHttps = 'https:' == document.location.protocol ? true : false;
  var domainScm =  isHttps ? "${domainOauth}" : "${httpDomainOauth}";
  var domainOpen = isHttps ? "${domainScm}" : "${httpDomain}";
  var cross_url = "${crossUrl}";
  var domain = document.domain.split('.');
  var run_env = domain[0];
  $(document).ready(function() {
    if ("false" == $("#emailIsExist").val()) {
      $("#new_account").attr("disabled", "disabled");
      $("#new_account").addClass("ipt_grey");
    } else {
      $("#changeAccount").hide();
    }

    if ("false" == $("#effectiveUrl").val()) {
      $("#connectionDiv").hide();
      $("#urlMsg").show();
    }
    if ("true" == $("#effectiveUrl").val() && "false" == $("#effectiveToken").val()) {
      $("#connectionDiv").hide();
      $("#tokenMsg").show();
    }
    if (navigator.userAgent.indexOf("MSIE 8.0") > 0) {
      $(".reg_box input").css({
        "padding-top" : "12px",
        "height" : "28px"
      });
      $(".login_input input").css({
        "padding-top" : "5px",
        "height" : "20px"
      });
    }
   

  });

  //提交创建并关联账号
  function createAndRelate() {
    $("#create_submit").attr("disabled", "disabled");
    var name = $.trim($("#userName").val());
    var lastName = $.trim($("#lastName").val());
    var firstName = $.trim($("#firstName").val());
    var password = $.trim($("#new_password").val());
    var email = $.trim($("#new_account").val()).toLowerCase();
    //检查邮箱必填和格式
    var flag = true;
    if (!validateEmail()) {
      $("#create_submit").removeAttr("disabled");
      flag = false;
    } else {
      //检验邮箱是否存在
      checkEmailExist();
    }
    if ("true" == $("#emailExist").val()) {
      $("#create_submit").removeAttr("disabled");
      flag = false;
    }
    //检查密码必填和位数
    if (!validatePassword()) {
      $("#create_submit").removeAttr("disabled");
      flag = false;
    }
    /*//检查确认密码
    if (!validateRepassword()) {
      $("#create_submit").removeAttr("disabled");
      flag = false;
    }*/
    if (flag == false) {
      return;
    }
    var insName = $.trim($("#insName").val());
    var department = $.trim($("#department").val());
    var position = $.trim($("#position").val());
    var fromSys = $.trim($("#token").val());
    var birthday = $("#birthdate").val();
    var degree = $("#degree").val();
    var url = '';
    var postData = {};
    if (navigator.userAgent.indexOf("MSIE 8.0") > 0 || navigator.userAgent.indexOf("MSIE 9.0") > 0) {
      url = domainOpen + "/oauth/connection/ajaxcreateandrelate?jsoncallback=jsoncallback&email=" + email
          + "&password=" + password + "&userName=" + encodeURI(name) + "&lastName=" + encodeURI(lastName)
          + "&firstName=" + encodeURI(firstName) + "&insName=" + encodeURI(insName) + "&department="
          + encodeURI(department) + "&position=" + encodeURI(position) + "&fromSys=" + fromSys;
    } else {
      url = domainOpen + "/oauth/connection/ajaxcreateandrelate?jsoncallback=jsoncallback";
      postData = {
        'email' : email,
        'password' : password,
        'userName' : name,
        'firstName' : firstName,
        'lastName' : lastName,
        'insName' : insName,
        'department' : department,
        'position' : position,
        'fromSys' : fromSys,
        'birthday' : birthday,
        'degree' : degree,
        'isHttps' : isHttps
      }
    }

    $("#shelterBoxGroup").show();
    $("#shelterBgGroup").show();

    $.ajax({
      url : url,
      data : postData,
      type : 'POST',
      dataType : 'json'/*,
      		    xhrFields:{
      		      withCredentials:true
      		    }*/
    }).done(function(data) {
      $("#shelterBoxGroup").hide();
      $("#shelterBgGroup").hide();
      $("#create_submit").removeAttr("disabled");
      if (data.result == "success") {
        //创建并关联账号-同步人员信息
        sync_person_info(data.openId);
        //alert("注册并关联成功， openId="+data.openId);
        //window.parent.window.saveDemoUnion(data.openId,"SNS",data.signature);
        smate.sendMsg(data.openId + ",SNS," + data.signature);
      } else {
        if (data.errorMsg == "wrongToken") {
          $("#wrongToken").show();
        }
        //alert("注册失败");
      }
    });

  }

  //点击修改，可编辑账号
  function changeStatus() {
    $("#new_account").removeAttr("disabled");
    $("#new_account").removeClass("ipt_grey");
  }

  //修改提交按钮状态
  function changeButtonStatus() {
    $("#create_submit").addClass("contact_btn_grey1");
    $("#create_submit").removeClass("contact_btn");
    $("#create_submit").attr("disabled", "disabled");
  }

  //检查邮箱是否存在
  function checkEmailExist() {
    var email = $.trim($("#new_account").val()).toLowerCase();
    $.ajax({
      url : "/open/connection/validateemail",
      type : "post",
      data : {
        email : email
      },
      dataType : "json",
      async : false,
      success : function(data) {
        if (data.result == "exist") {
          $(".email").hide();
          $("#emailExistMsg").show();
          $("#emailExist").val("true");
        } else {
          $("#emailExistMsg").hide();
          $("#emailExist").val("false");
        }
      },
      error : function(data) {

      }

    });
  }

  //检查邮箱格式
  function checkFormatOfEmail() {
    var flag = true;
    var email = $.trim($("#new_account").val());
    //var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    var reg = /^[a-z0-9]+[a-z0-9_\-\.]*\@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/;
    if (email != "" && email.match(reg) == null) {
      $(".email").hide();
      $("#emailWrongMsg").show();
      flag = false;
    } else {
      $("#emailWrongMsg").hide();
    }
    return flag;
  }

  //对邮箱的必填和格式检查
  function validateEmail() {
    var flag = true;
    var email = $.trim($("#new_account").val());
    if (email == "") {
      $(".email").hide();
      $("#emailEmptyMsg").show();
      flag = false;
    } else {
      $("#emailEmptyMsg").hide();
    }
    //^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$
      email = $.trim(email.toLowerCase());
    var reg = /^[a-z0-9]+[a-z0-9_\-\.]*\@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/;
    if (email != "" && email.match(reg) == null) {
      $(".email").hide();
      $("#emailWrongMsg").show();
      flag = false;
    } else {
      $("#emailWrongMsg").hide();
    }
    return flag;
  }

  //对密码输入框的检查
  function validatePassword() {
    var flag = true;
    var password = $.trim($("#new_password").val());
    var repassword = $.trim($("#new_repassword").val());
    if (password == "") {
      $(".password").hide();
      $("#passwordLengthMsg").hide();
      $("#passwordEmptyMsg").show();
      flag = false;
    } else {
      $("#passwordEmptyMsg").hide();
    }
    if (password != "" && password.length < 6) {
      $(".password").hide();
      $("#passwordLengthMsg").addClass("error");
      $("#passwordLengthMsg").show();
      flag = false;
    } else {
      $("#passwordLengthMsg").hide();
    }
    return flag;
  }

  //对确认密码输入框的检查
  function validateRepassword() {
    var flag = true;
    var password = $.trim($("#new_password").val());
    var repassword = $.trim($("#new_repassword").val());
    if (password != repassword) {
      if (repassword == "") {
        $("#passwordNotEqMsg").text("确认密码不能为空！");
      } else {
        $("#passwordNotEqMsg").text("确认密码与密码不一致！");
      }
      $("#passwordNotEqMsg").show();
      flag = false;
    } else {
      $("#passwordNotEqMsg").hide();
    }
    return flag;
  }
  //快速关联登录ltl
  function fastRelateAccount() {
    var token = $("#token").val();
    var des3PsnId = $("#des3PsnId").val();
    $("#shelterBoxGroup").show();
    $("#shelterBgGroup").show();
    $.ajax({
      url : '/open/interconnection/ajaxfastrelate',
      type : 'post',
      datatype : 'json',
      data : {
        token : token,
        des3PsnId : des3PsnId
      },
      success : function(data) {
        if (data.result == 'success') {//关联成功
          $("#shelterBoxGroup").hide();
          $("#shelterBgGroup").hide();
          //快速关联登录-同步人员信息
          sync_person_info(data.openId);
          //	window.parent.window.saveDemoUnion(data.openId,"SNS",data.signature);
          smate.sendMsg(data.openId + ",SNS," + data.signature);
        } else {
          if (data.errorMsg == 'wrongToken') {
            $("#fastTokenMsg").show();
          }
        }
      },
      error : function() {
        //    			alert('快速关联不成功');
      }
    });
  }
  function pwdTrim(pwd) {//去掉前后空格
    var result = pwd.value.replace(/(^\s*)|(\s*$)/g, "");
    pwd.value = result;
  }
  function hiddenMsg(n) {
    if (n == 1) {
      $("#menberExistMsg").hide();
    }
    if (n == 2) {
      $("#emailWrongMsg").hide();
      $("#emailEmptyMsg").hide();
      $("#emailExistMsg").hide();
    }
    if (n == 3) {
      $("#passwordEmptyMsg").hide();
      $("#passwordLengthMsg").hide();
    }
    if (n == 4) {
      $("#passwordNotEqMsg").hide();
    }

  }

  function waitMage() {
    $("#waitMage").css("display", "block");
    $("#email").css("readOnly", true);
    $("#con_two_2").find(".contact_btn").css("disabled", true);

  }
  function cleanWaitMage() {
    $("waitMage").css("display", "none");
  }
  //已有账号关联登录ltl
  function relateExistAccount() {
    $("#menberExistMsg").hide();
    var token = $("#token").val();
    var email = $.trim($("#email").val());
    var password = $.trim($("#password").val());
    if (email == "" || password == "") {
      $("#menberExistMsg").text("登录邮箱/手机号/科研号或密码不能为空"); //关联失败弹出提示
      $("#menberExistMsg").show();
      return;
    }
    $("#shelterBoxGroup").show();
    $("#shelterBgGroup").show();
    $.ajax({
      url : '/open/interconnection/ajaxrelateexist',
      data : {
        token : token,
        email : email,
        password : password
      },
      type : 'post',
      datatype : 'json',
      success : function(data) {
        $("#shelterBoxGroup").hide();
        $("#shelterBgGroup").hide();
        if (data.result == 'success') {//获取openId成功
          //已有账号关联登录-同步人员信息
          sync_person_info(data.openId);
          //window.parent.window.saveDemoUnion(data.openId,"SNS",data.signature);
          smate.sendMsg(data.openId + ",SNS," + data.signature);
        } else if (data.result == "exist") {
          //已有账号关联登录-同步人员信息
          sync_person_info(data.openId);
          //表示已经关联过了
          smate.sendMsg(data.openId + ",SNS," + data.signature);
        } else {
          $("#menberExistMsg").text(data.result); //关联失败弹出提示
          $("#menberExistMsg").show();
        }
      },
      error : function() {
        //    			alert("账号密码关联登录不成功");
      }
    });
  }
  /**
   * 忘记密码.
   */
  function SCMIndex_forgetPassword(obj) {
    var email = $.trim($("#email").val());
    var frmUrl = domainScm + "/oauth/pwd/forget/index?email=" + email + "&from=other";
    window.open(frmUrl);
  };
  /*暂不关联*/
  function cancelAssocicate() {
    smate.sendMsg("cancelAssocicate");
  }

  //打开数据交换许可协议
  function open_policy() {
    window.open(domainScm + "/resscmwebsns/html/policy_zh_CN.html");
  }

  //同步人员信息
  function sync_person_info(openId) {
    var name = $.trim($("#userName").val());
    $.ajax({
      url : "/open/interconnection/ajaxsyncpersoninfo",
      data : {
        openId : openId,
        fromSys : $.trim($("#token").val()),
        userName : name,
        email : $.trim($("#sync_email").val()),
        insName : $.trim($("#insName").val()),
        department : $.trim($("#department").val()),
        position : $.trim($("#position").val()),
        degree : $.trim($("#degree").val()),
        disciplineCode : $.trim($("#disciplineCode").val()),
        researchArea : $.trim($("#researchArea").val()),
        country : $.trim($("#country").val()),
        province : $.trim($("#province").val()),
        city : $.trim($("#city").val()),
        birthdate : $.trim($("#birthdate").val()),
        sysName : $.trim($("#sysName").val()),
        keyWordsZh : $("#keyWordsZh").val(),
        keyWordsEn : $("#keyWordsEn").val(),
        isHttps : isHttps
      },
      type : "post",
      datatype : "json",
      success : function(data) {
        if (data.result == "success") {
          // 					alert("创建并关联账号-同步人员信息成功");
        } else {
          //alert("创建并关联账号-同步人员信息失败");
        }
      },
      error : function() {
        //alert("创建并关联账号-同步人员信息失败");
      }
    });
  };

  function trimPwd(psd) {
    var result = $(this).attr("value").replace(/(^\s*)|(\s*$)/g, "");
    $(this).attr("value", result);
  }

  function forwardPage(url) {
    window.open(url);
  }
  function selectPsnInfo(obj) {
    $("#des3PsnId").val($(obj).attr("des3PsnId"));
    $(".selected-similar_account-number").removeClass("selected-similar_account-number");
    $(obj).addClass("selected-similar_account-number");
  }
  /* //html实体类转义
  function htmlencode(s){  
      var div = document.createElement('div');  
      div.appendChild(document.createTextNode(s));  
      return div.innerHTML;  
  } 
  //html实体类解码
  function htmldecode(s){  
      var div = document.createElement('div');  
      div.innerHTML = s;  
      return div.innerText || div.textContent;  
  }   */
</script>
</head>
<body>
  <div class="pop__bg"></div>
  <div class="pop-cover">
    <div class="pop_box">
      <div class="pop" style="width: 910px; margin-left: -455px; margin-top: -240px;">
        <!-- 			<div class="pop_top"> -->
        <!-- 				<a href="javascript: smate.sendMsg('close');" class="close"><i class="close_icon"></i></a> -->
        <!-- 				<span class="ml20">科研之友账号关联</span> -->
        <!-- 			</div> -->
        <input type="hidden" id="effectiveUrl" value="${effectiveUrl }" /> <input type="hidden" id="effectiveToken"
          value="${effectiveToken }" />
        <div class="pop_shelter" style="display: none;" id="shelterBoxGroup">
          <div class="pop_slt_tit">
            <a href="#" class=""></a>
          </div>
          <div class="pop_slt_cont">
            <span id="showMsgGroup">正在关联...</span>
          </div>
        </div>
        <div class="pop_shelter_bg" style="display: none; position: absolute;" id="shelterBgGroup"></div>
        <div class="pop_tx" id="connectionDiv" style="margin-top: 30px;">
          <input type="hidden" id="userName" name="userName" value="${userName }" /> <input type="hidden"
            id="firstName" name="firstName" value="${firstName }" /> <input type="hidden" id="lastName" name="lastName"
            value="${lastName }" /> <input type="hidden" id="position" name="position" value="${position }" /> <input
            type="hidden" id="insName" name="insName" value="${insName }" /> <input type="hidden" id="department"
            name="department" value="${department }" /> <input type="hidden" id="des3PsnId" name="des3PsnId"
            value="${psnInfo.des3PsnId }" /> <input type="hidden" id="token" name="token" value="${fromSys}"> <input
            type="hidden" id="emailIsExist" name="emailIsExist" value="${emailIsExist }" /> <input type="hidden"
            id="degree" name="degree" value="${degree }" /> <input type="hidden" id="disciplineCode"
            name="disciplineCode" value="<c:out value="${disciplineCode }"/>" /> <input type="hidden" id="researchArea"
            name="researchArea" value="<c:out value="${researchArea }"/>" /> <input type="hidden" id="country"
            name="country" value="${country }" /> <input type="hidden" id="province" name="province"
            value="${province }" /> <input type="hidden" id="city" name="city" value="${city }" /> <input
            type="hidden" id="birthdate" name="birthdate" value="${birthdate }" /> <input type="hidden" id="sync_email"
            name="sync_email" value="${email }" /> <input type="hidden" id="sysName" name="sysName" value="${sysName }" />
          <input type="hidden" id="keyWordsZh" name="keyWordsZh" value="<c:out value="${keyWordsZh }"/>" /> <input
            type="hidden" id="keyWordsEn" name="keyWordsEn" value="<c:out value="${keyWordsEn }"/>" />
          <div id="tab" style="margin: 0px; float:left!important; margin-left: 25px;">
            <div class="menubox">
              <ul>
                <li id="two1" onClick="setTab('two',1,3)" class="hover"
                  style="${empty psnInfoList ? 'display:none;' : ''}">快速登录</li>
                <li id="two2" onClick="setTab('two',2,3)" class="${empty psnInfoList ? 'hover' : ''}">帐号登录</li>
                <li id="two3" onClick="setTab('two',3,3)">创建新帐号</li>
              </ul>
            </div>
            <div class="contentbox">
              <div id="con_two_1" class="hover" style="${empty psnInfoList ? 'display:none;' : ''}">
                <div class="login_box">
                  <p>检测到有可能是你的帐号</p>
                  <c:if test='${!empty psnInfoList}'>
                    <c:forEach items="${ psnInfoList}" var="psnInfo" varStatus="status">
                      <c:if test="${ fn:length(psnInfoList)==1}">
                        <a class="quick_login" href="javascript:" target=""
                          onclick="forwardPage('${psnInfo.psnShortUrl}');" des3PsnId="${psnInfo.des3PsnId}"> <img
                          src="${psnInfo.avatorUrl}"
                          onerror="src='https://www.scholarmate.com/resmod/smate-pc/img/logo_psndefault.png'">
                          <p>${psnInfo.psnName}</p>
                          <p
                            title="${insName}<c:if test='${!empty department }'>，</c:if>${department}<c:if test='${!empty position}'>，</c:if>${position}">${insName}
                            <c:if test="${!empty department }">，</c:if>${department}
                            <c:if test="${!empty position}">，</c:if>${position}</p>
                          <p>成果：${!empty psnInfo.pubSum ? psnInfo.pubSum :'0'}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            专利：${!empty psnInfo.patentSum ? psnInfo.patentSum :0}</p>
                        </a>
                      </c:if>
                      <c:if test="${ fn:length(psnInfoList)>1}">
                        <a class="quick_login  ${status.index == 0 ?'selected-similar_account-number':''}"
                          href="javascript:" target="" onclick="selectPsnInfo(this);"
                          des3PsnId="${psnInfo.des3PsnId}"> <img onclick="forwardPage('${psnInfo.psnShortUrl}')"
                          src="${psnInfo.avatorUrl}"
                          onerror="src='https://www.scholarmate.com/resmod/smate-pc/img/logo_psndefault.png'">
                          <p>${psnInfo.psnName}</p>
                          <p
                            title="${insName}<c:if test='${!empty department }'>，</c:if>${department}<c:if test='${!empty position}'>，</c:if>${position}">${insName}
                            <c:if test="${!empty department }">，</c:if>${department}
                            <c:if test="${!empty position}">，</c:if>${position}</p>
                          <p>成果：${!empty psnInfo.pubSum ? psnInfo.pubSum :'0'}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            专利：${!empty psnInfo.patentSum ? psnInfo.patentSum :0}</p>
                        </a>
                      </c:if>
                    </c:forEach>
                  </c:if>
                  <label id="fastTokenMsg" class="error ml70" style="display: none;">该来源系统权限未开放</label> <input
                    type="button" value="关联帐号" class="contact_btn" onclick="fastRelateAccount()">
                </div>
                <div class="agreement" style="border: none;">
                  <i class="tip_icon"></i>授权关联后表明你已同意<a href="javascript:;" onclick="open_policy();">《许可协议》</a>
                </div>
              </div>
              <div id="con_two_2" style="${!empty psnInfoList ? 'display:none;' : ''}">
                <div class="login_box" style="margin-top: 30px;">
                  <p>输入科研之友帐号/科研号密码关联</p>
                  <div class="login_input">
                    <i class="demo-admin_icon"></i> <input type="text" id="email" placeholder="登录邮箱/手机号/科研号 " oninput="hiddenMsg(1)"
                      onpropertychange="hiddenMsg(1)" />
                  </div>
                  <div style="height: 10px;"></div>
                  <div class="login_input">
                    <i class="demo-password_icon"></i> <input type="password" id="password" placeholder="密码" onblur="pwdTrim(this);"
                      oninput="hiddenMsg(1)" onpropertychange="hiddenMsg(1)" /> <label id="menberExistMsg"
                      class="menber error" style="display: none; color: red">帐号已经关联过了！</label>
                  </div>
                  <input type="button" value="关联帐号" class="contact_btn" onclick="relateExistAccount()">
                  <p>
                    <c:if test="${ cancleV eq 'yes'}">
                    <a href="#" id="cancel_associcate" onclick="cancelAssocicate();" class=" demo_float-left blue" style="color: #999;">暂不关联</a>
                    </c:if>
                    <a href="javascript:;" onclick="SCMIndex_forgetPassword(this);" class="fr blue">忘记密码</a>
                  </p>
                </div>
                <div class="agreement" style="border: none;">
                  <i class="tip_icon"></i>授权关联后表明你已同意<a href="javascript:;" onclick="open_policy();">《许可协议》</a>
                </div>
              </div>
              <div id="con_two_3" style="display: none">
                <input type="hidden" id="emailExist" value="${emailIsExist }" />
                <div class="login_box">
                  <div style="height: 10px;"></div>
                    <div class="reg_box">
                      <i class="demo-user_icon"></i> 
                      <input type="text" id="new_name" class="reg_box-inputbox" value="${userName }" maxlength="40" disabled="disabled" readonly="readonly" style="background-color: #ddd; height: 40px; width: 289px; margin: 0px;"/>
                    </div>
                    
                    <div class="reg_box">
                      <i class="demo-email_icon"></i>
                      <input class="reg_box-inputbox" type="text" placeholder="登录邮箱" value="${emailIsExist ? '' : email}" id="new_account"
                        maxlength="50" onchange="validateEmail();" oninput="hiddenMsg(2);" onpropertychange="hiddenMsg(2)" />
                      <a id="changeAccount" href="javascript:changeStatus();">修改</a>
                   </div>
                   <div style="width: 312px; height: 2px;">
                        <label id="emailExistMsg" class="email error" style="color: red; display: none;">登录邮箱已存在！</label>
                        <label id="emailEmptyMsg" class="email error" style="color: red; display: none;">请输入登录邮箱！</label>
                        <label id="emailWrongMsg" class="email  error" style="color: red; display: none;">登录邮箱格式不正确！</label>
                  </div>
                  <div class="reg_box">
                     <i class="demo-password_icon"></i>
                     <input class="reg_box-inputbox" type="password" placeholder="密码" id="new_password" maxlength="40"
                      onchange="validatePassword();" onblur="pwdTrim(this);" oninput="hiddenMsg(3);"
                      onpropertychange="hiddenMsg(3)" />
                  </div>
                  <div style="width: 312px; height: 2px;"> 
                      <label id="passwordEmptyMsg" class="password error" style="color: red; display: none;">请输入密码！</label>
                      <label id="passwordLengthMsg" class="password  error" style="color: red; display: none;">至少需要6个字符！</label>
                      <label id="wrongToken" class="error" style="display: none;">来源系统权限未开放</label>
                    </div>
                  
                  <input id="create_submit" type="button" value="创建并关联帐号" class="contact_btn" style="margin-top: 20px;"
                    onclick="createAndRelate();">
                </div>
                <div class="agreement" style="border: none;">
                  <i class="tip_icon"></i>授权关联后表明你已同意<a href="javascript:;" onclick="open_policy();">《许可协议》</a>
                </div>
              </div>
            </div>
          </div>
          <span class="demo-reg_box-line"></span>
          
          <div class="demo-reg_box-right">
            <div class="demo-reg_box-right_line"></div>
            <div class="demo-reg_box-right_avator">
                <img src="/resmod/interconnection/images/login_bg-left.png"  style="width: 100%; height: 100%;">
            </div>
            <div class="demo-reg_box-right_title">协同创新网络，让创新更有效</div>
          </div>
          
        </div>
        <div id="tokenMsg" style="display: none; height: 465px;">
          <span class="ml20 mt10">该来源系统权限未开放</span>
        </div>
        <div id="urlMsg" style="display: none; height: 465px;">
          <span class="ml20 mt10">该请求URL无效</span>
        </div>
      </div>
    </div>
</body>
</html>
