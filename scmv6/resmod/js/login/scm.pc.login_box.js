/*
 * 弹框登录
 * */
var ScmLoginBox = ScmLoginBox ? ScmLoginBox : {};
var operationErrorTip = locale == "zh_CN" ? "网络错误，请稍后重试" : "Network error, please try again.";
// 账号密码登录
ScmLoginBox.beforeSubmit = function() {
  ScmLoginBox.login();
  ScmLoginBox.ajaxLoginToScm();
}

// 显示QQ登录窗口
ScmLoginBox.loginQQ = function() {
  window
      .open('/oauth/qq/login', document.location.href,
          'height=560,width=1024,top=220,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
}

// 显示微信登录窗口
ScmLoginBox.loginWechat = function() {
  window
      .open('/oauth/wechat/login', document.location.href,
          'height=560,width=1024,top=220,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
}

// 显示微博登录窗口
ScmLoginBox.weiboLogin = function() {
  ScmLoginBox.setCookie("isWeiboCall", "true", 1);
  window
      .open('/oauth/weibo/login', document.location.href,
          'height=560,width=1024,top=220,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
}

ScmLoginBox.toRegister = function() {
  window.open("/oauth/pc/register?targetUrl=" + encodeURIComponent(document.location.href));
}

// 显示登录框并初始化
ScmLoginBox.showLoginToast = function() {
  $("#login_toast_box").show();
  ScmLoginBox.resize();
  ScmLoginBox.initCommonData();
  // 去掉遮罩层，不然输入不了账号密码
  // $(".background-cover").remove();
}

// 重新计算弹出框大小
ScmLoginBox.resize = function() {
  var coverbox = $("#login_toast_box")[0];
  var parntbox = $("#toast_load-container_div")[0];
  const selfwidth = parntbox.clientWidth;
  const selfheight = parntbox.clientHeight;
  var newwidth = coverbox.offsetWidth;
  var newheight = coverbox.offsetHeight;
  var setwidth = (newwidth - selfwidth) / 2 + "px";
  var setheight = (newheight - selfheight) / 2 + "px";
  parntbox.style.left = setwidth;
  parntbox.style.bottom = setheight;
}

// 关闭弹框
ScmLoginBox.closeBox = function() {
  // 默认登录后刷新当前页面
  if ($("#login_box_refresh_currentPage_always").val() != "true") {
    $("#login_box_refresh_currentPage").val("true");
  }
  var coverbox = $("#login_toast_box")[0];
  var parntbox = $("#toast_load-container_div")[0];
  const selfheight = parntbox.clientHeight;
  parntbox.style.bottom = -selfheight + "px";
  coverbox.style.display = "none";
  $("#login-error").hide();
  // SCM-24580
  /*
   * setTimeout(function() { coverbox.style.display = "none"; $("#username").val("");
   * $("#password").val(""); $("#mobileNum").val(""); $("#mobileCode").val("");
   * $("#user_name_inp_div").css("margin-top", "60px"); $("#login-error").hide(); }, 500);
   * $(".phone-Verification_code").html(confirm_mobile.send); if (typeof (timeout) != "undefined") {
   * window.clearInterval(timeout); }
   */

}

// ajax登录
ScmLoginBox.ajaxLoginToScm = function() {
  /* $("#user_name_inp_div").css("margin-top", "60px"); */
  var codeLogin = $("#mobileCodeLogin").val();
  if (codeLogin == "true") {
    // 手机验证码登录
    ScmLoginBox.ajaxMobileCodeLoginToScm();
    return;
  }
  $("#login-error").hide();
  var userName = $("#username").val();
  var password = $("#password").val();
  var domain = document.domain;
  var httpsUrl = "/oauth/login/ajaxlogin";
  $.ajax({
    url : httpsUrl,
    type : "post",
    data : {
      "userName" : $.trim(userName),
      "password" : $.trim(password),
      "needValidateCode" : $("#needValidateCode").val(),
      "validateCode" : $.trim($("#validateCode").val())
    },
    dataType : "json",
    success : function(data) {
      if (data.needValidateCode == 1) {
        refreshCode();
        $("#showValidateCodeDiv").css("display", "flex");
        $("#needValidateCode").val("1");
      }
      if (data.result == "serviceError") {
        scmpublictoast(operationErrorTip, 1000);
      } else if (data.result != "success") {
        $("#user_name_inp_div").css("margin-top", "12px");
        $("#login-error").text(data.result);
        setTimeout($("#login-error").show(), 1000);
      } else {
        if ($("#login_box_refresh_currentPage").val() === "false") {
          ScmLoginBox.closeBox();
        } else {
          window.location.reload();
        }
      }
    },
    error : function(data) {
      scmpublictoast(operationErrorTip, 1000);
    }
  });
}
ScmLoginBox.ajaxMobileCodeLoginToScm = function() {
  $("#login-error").hide();
  var mobileNum = $("#mobileNum").val();
  var mobileCode = $("#mobileCode").val();
  if (mobileNum == "") {
    // scmpublictoast(confirm_mobile.mobileNotBlank, 1000);
    $("#login-error").html(confirm_mobile.mobileNotBlank);
    $("#login-error").show();
    return;
  }
  if (mobileCode == "") {
    // scmpublictoast(confirm_mobile.codeNotBlank, 1000);
    $("#login-error").html(confirm_mobile.codeNotBlank);
    $("#login-error").show();
    return;
  }
  var domain = document.domain;
  var httpsUrl = "https://" + domain + "/oauth/login/ajaxlogin"
  $.ajax({
    url : httpsUrl,
    type : "post",
    data : {
      "mobileNum" : $.trim(mobileNum),
      "mobileCode" : $.trim(mobileCode),
      "mobileCodeLogin" : true,
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "serviceError") {
        scmpublictoast(operationErrorTip, 1000);
      } else if (data.result != "success") {
        $("#user_name_inp_div").css("margin-top", "12px");
        $("#login-error").text(data.result);
        setTimeout($("#login-error").show(), 1000);
      } else {
        if ($("#login_box_refresh_currentPage").val() === "false") {
          ScmLoginBox.closeBox();
        } else {
          window.location.reload();
        }
      }
    },
    error : function(data) {
      scmpublictoast(operationErrorTip, 1000);
    }
  });
}

ScmLoginBox.initCommonData = function() {
  var username = document.getElementById("username");
  var writepassword = document.getElementById("password");

  username.onblur = function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  }
  username.onkeyup = function(event) {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333'
    }
    if (event.keyCode == 13) {
      if ($.trim($("#username").val()) !== "" && $.trim($("#password").val()) !== "") {
        ScmLoginBox.ajaxLoginToScm();
      }
    };
  }
  writepassword.onblur = function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  }
  writepassword.onkeyup = function(event) {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333'
    }
    if (event.keyCode == 13) {
      if ($.trim($("#username").val()) !== "" && $.trim($("#password").val()) !== "") {
        ScmLoginBox.ajaxLoginToScm();
      }
    };
  };
}

/**
 * 登陆请求. 如果记住登录 setcookie 如果没选 判断所填帐号与目前kooie所存帐号是否相同 相同 则清除
 */
ScmLoginBox.login = function() {
  ScmLoginBox.setCookie("LashLoginUser", $("#username").val(), 7); // 记住帐号
}

ScmLoginBox.setCookie = function(c_name, value, expiredays) {
  var exdate = new Date();
  exdate.setDate(exdate.getDate() + expiredays);
  document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : "; expires=" + exdate.toGMTString());
}
