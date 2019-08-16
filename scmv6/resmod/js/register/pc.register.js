/**
 * 
 */

var Register = Register || {};
Register.change_locol_language = function(locale) {
  strUrl = location.href;// 当前url地址.
  // 请求的url地址.
  var hrefString = strUrl.substring(0, strUrl.indexOf("?"));
  var params = "";
  var flag = false;// 验证url请求参数中是否包含页面语言参数locale(true-包含；false-不包含).
  if (strUrl.indexOf("?") >= 0) {
    // url请求参数.
    var paraString = strUrl.substring(strUrl.indexOf("?") + 1, strUrl.length);
    if (paraString != '') {
      var paraArr = paraString.split("&");
      for (var i = 0; j = paraArr[i]; i++) {
        // 参数名.
        var name = j.substring(0, j.indexOf("="));
        // 参数值.
        var value = j.substring(j.indexOf("=") + 1, j.length);
        if (name == 'locale') {// 页面语言的参数设置为locale的值.
          flag = true;
          value = locale;
        } else {// 其他参数进行编码处理,替换掉特殊字符.
          // value=SCMIndex.URLencode(value);
        }
        params = params + name + "=" + value + "&";
      }
    }
  }
  // 如果请求参数中不包含语言参数，则增加该参数.
  if (!flag) {
    params = params + "locale=" + locale;
  }
  // 重定位页面请求地址
  window.location.href = hrefString + "?" + params;

};

// input框的id来源 , 目的地Id
Register.wordParsePinyin = function(sourceId, destId, length) {
  /*
   * var ids = new Array(); ids.push(destId) ; Register.checkInputDefaultValue(ids);
   */
  var sourceValue = $("#" + sourceId).val();
  var destValue = $("#" + destId).val();
  if ($("#" + sourceId)[0].value === $("#" + sourceId)[0].defaultValue || sourceValue.trim() === ""
      || $("#" + destId).attr("auto") == "false") {
    return;
  }
  var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
  if (!pattern.test($("#" + sourceId)[0].value)) {
    return;
  }
  $.ajax({
    url : "/psnweb/pinyin/ajaxparse",
    type : "post",
    dataType : "json",
    data : {
      word : sourceValue,
      wordLength : length,
      wordType : destId
    },
    success : function(data) {
      if (data.result !== "") {
        if (length != undefined && data.result != undefined && data.result.length > length) {
          var pinYin = data.result.substring(0, length - 1);
          $("#" + destId).val(pinYin);
        } else {
          $("#" + destId).val(data.result);
        }
        $("#" + destId).css("color", "#333");
        $("#" + destId).attr("auto", "true");
        $("#" + destId).blur();
      }
    }
  });
}
Register.initCommonData = function() {

  // email.onfocus=function(){if(this.value==this.defaultValue)this.value=''};
  $("#email").blur(function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  });
  $("#email").keyup(function() {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333'
    }
  });

  // newpassword.onfocus=function(){if(this.value==this.defaultValue)this.value=''};
  $("#newpassword").blur(function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  });
  $("#newpassword").keyup(function() {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333'
    }
  });

  $("#zhlastName").keyup(function() {
    Register.wordParsePinyin("zhlastName", "lastName", 20);
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc';
    } else {
      this.style.color = '#333';
    }
  });

  $("#zhfirstName").keyup(function() {
    Register.wordParsePinyin("zhfirstName", "firstName", 40);
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc';
    } else {
      this.style.color = '#333';
    }
  });

  $("#zhlastNameNoPhone").keyup(function() {
    Register.wordParsePinyin("zhlastNameNoPhone", "lastNameNoPhone", 20);
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc';
    } else {
      this.style.color = '#333';
    }
  });

  $("#zhfirstNameNoPhone").keyup(function() {
    Register.wordParsePinyin("zhfirstNameNoPhone", "firstNameNoPhone", 20);
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc';
    } else {
      this.style.color = '#333';
    }
  });

  // lastName.onfocus=function(){if(this.value==this.defaultValue)this.value=''};
  $("#lastName").blur(function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  });
  $("#lastName").keyup(function() {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333';
      $("#lastName").attr("auto", "false");
    }
  });

  // firstName.onfocus=function(){if(this.value==this.defaultValue)this.value=''};
  $("#firstName").blur(function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  });
  $("#firstName").keyup(function() {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333';
      $("#firstName").attr("auto", "false");
    }
  });

  $("#lastNameNoPhone").blur(function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  });
  $("#lastNameNoPhone").keyup(function() {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333';
      $("#lastNameNoPhone").attr("auto", "false");
    }
  });

  $("#firstNameNoPhone").blur(function() {
    if ($.trim(this.value) === "") {
      this.style.color = '#ccc'
    }
  });
  $("#firstNameNoPhone").keyup(function() {
    if ($.trim(this.value) !== "") {
      this.style.color = '#333';
      $("#firstNameNoPhone").attr("auto", "false");
    }
  });
};
/**
 * 检查默认值 ,为空 checkIds数组
 */
Register.checkInputDefaultValue = function(checkIds) {
  var email = document.getElementById("email");
  var newpassword = document.getElementById("newpassword");
  var zhlastName = document.getElementById("zhlastName");
  var zhfirstName = document.getElementById("zhfirstName");
  var lastName = document.getElementById("lastName");
  var firstName = document.getElementById("firstName");
  var arrInput = {};
  if (checkIds !== undefined && checkIds.length > 0) {
    arrInput = new Array();
    for (var i = 0; i < checkIds.length; i++) {
      arrInput.push(document.getElementById(checkIds[i]))
    }
  } else {
    arrInput = new Array(email, newpassword, zhlastName, zhfirstName, lastName, firstName);
  }
  // 检查input框的默认值
  checkInputDefaultValue = function() {
    for (var i = 0; i < arrInput.length; i++) {
      var obj = arrInput[i];
      if (obj.value == obj.defaultValue) {
        obj.value = '';
      }
    }
  }
  checkInputDefaultValue();
};

/**
 * 点击提交
 */
Register.submit = function(obj) {
  // Register.checkInputDefaultValue();
  // 防重复点击
  BaseUtils.doHitMore(obj, 2000);
  setTimeout(function() {
    $("#newpassword").val("")
  }, 1500);
  var flag = $("#regForm").valid();
  if ($(obj).attr("id") == "regSubmit") {
    setTimeout(Register.validLoginReg, 50)
  } else {
    setTimeout(Register.validRegPage, 50);
  }

  return flag;
};

Register.validLoginReg = function() {
  if (locale == "en_US") {
    var lastName = $("#regForm").validate().element($("#lastName"));
    if (lastName) {
      $("#regForm").validate().element($("#firstName"));
    };
  } else {
    var zhlastName = $("#regForm").validate().element($("#zhlastName"));
    if (zhlastName) {
      $("#regForm").validate().element($("#zhfirstName"));
    };
  }

}

Register.validRegPage = function() {
  var zhlastName = $("#regForm").validate().element($("#zhlastName"));
  if (zhlastName) {
    $("#regForm").validate().element($("#zhfirstName"));
  };
  var lastName = $("#regForm").validate().element($("#lastName"));
  if (lastName) {
    $("#regForm").validate().element($("#firstName"));
  };
}

/**
 * 发送手机验证码
 * 
 * @param obj
 * @returns {boolean}
 */
Register.sendMobileCode = function(obj) {
  var mobileNumber = $("#mobileNumber").val();
  var result = $("#regForm").validate().element($("#mobileNumber"));
  if (!result) {
    return;
  }
  // 防重复点击
  var time = 60;
  var htmlContent = $(obj).html();
  $(obj).html("60s");
  BaseUtils.doHitMore(obj, time * 1000);
  var timeout = setInterval(function() {
    time = time - 1;
    if (time == 0) {
      $(obj).html(confirm_mobile.sendAgain);
      window.clearInterval(timeout)
    } else {
      $(obj).html(time + "s");
    }
  }, 1000);
  $.ajax({
    url : "/oauth/pc/register/ajaxsendmobilecode",
    type : "post",
    dataType : "json",
    data : {
      'mobileNumber' : $.trim(mobileNumber)
    },
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast(confirm_mobile.sendSuccess, 1500);
      } else {
        scmpublictoast(confirm_mobile.sendError, 1500);
      }
    }
  });

};

Register.listenerMobileNum = function() {
  var style = {
    background : "#288aed",
    border : "1px solid #288aed"
  };
  var styleErr = {
    background : "#ddd",
    border : ""
  };
  $("#mobileNumber").bind("input", function() {
    var isMobile = Register.checkMobile($(this).val());
    if (isMobile) {
      $("#mobileNumberBtn").css(style);
    } else {
      $("#mobileNumberBtn").css(styleErr);
    }
  });
  $("#mobileNum").bind("input", function() {
    var isMobile = Register.checkMobile($(this).val());
    if (isMobile) {
      $("#mobileNumBtn").css(style);
    } else {
      $("#mobileNumBtn").css(styleErr);
    }
  });

}
/**
 * 发送手机验证码
 * 
 * @param obj
 * @returns {boolean}
 */
Register.sendMobileLoginCode = function(obj) {
  var mobileNumber = $("#mobileNum").val();
  if (mobileNumber == "") {
    scmpublictoast(confirm_mobile.mobileNotBlank, 1500);
    return;
  }
  var isMobile = Register.checkMobile(mobileNumber);
  if (!isMobile) {
    scmpublictoast(confirm_mobile.mobileFromatError, 1500);
    return;
  }
  // 防重复点击
  BaseUtils.doHitMore(obj, 2000);
  var time = 60;
  var htmlContent = $(obj).html();
  $.ajax({
    url : "/oauth/pc/register/ajaxsendmobilelogincode",
    type : "post",
    dataType : "json",
    data : {
      'mobileNumber' : $.trim(mobileNumber)
    },
    success : function(data) {
      if (data.result == "success") {
        $(obj).html("60s");
        setTimeout(function() {
          BaseUtils.doHitMore(obj, 58 * 1000);
        }, 2000);
        timeout = setInterval(function() {
          time = time - 1;
          if (time == 0) {
            $(obj).html(confirm_mobile.sendAgain);
            window.clearInterval(timeout)
          } else {
            $(obj).html(time + "s");
          }
        }, 1000);
        scmpublictoast(confirm_mobile.sendSuccess, 1500);
      } else if (data.result == "notExist") {
        scmpublictoast(confirm_mobile.mobileNotUsed, 1500);
      } else {
        scmpublictoast(confirm_mobile.sendError, 1500);
      }
    }
  });

};

Register.checkMobile = function(value) {
  value = $.trim(value);
  var pattern = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
  return pattern.test(value);
}

/**
 * 发送手机验证码
 * 
 * @param obj
 * @returns {boolean}
 */
Register.checkLoginCode = function() {

  var mobileNum = $("#mobileNum").val();
  var mobileCode = $("#mobileCode").val();

  if (mobileNum == "") {
    scmpublictoast(confirm_mobile.mobileNotBlank, 1500);
    return false;
  }
  if (mobileCode == "") {
    scmpublictoast(confirm_mobile.codeNotBlank, 1500);
    return false;
  }
  $.ajax({
    url : "/oauth/ajaxcheckmobilecode",
    type : "post",
    dataType : "json",
    async : false,
    data : {
      'mobileNum' : mobileNum,
      'mobileCode' : mobileCode,
    },
    success : function(data) {
      if (data.result == "success") {
        $("#loginForm").submit();
        return true;
      } else {
        scmpublictoast(data.errorMsg, 1500);
        return false;
      }
    },
    error : function() {
      scmpublictoast(confirm_mobile.systemError, 1500);
      return false;
    }
  });

};
/**
 * 通过ip判断 来源 中国大陆ip 就显示手机关联验证，非大陆ip就显示邮箱验证
 */
Register.checkIp = function() {
  $.ajax({
    url : "/oauth/ip",
    type : "post",
    dataType : "json",
    async : false,
    data : {},
    success : function(data) {
      $("#isPhoneCheck").val(data.result);
      if (data.status == "success") {
        if (data.result == "1") {// 手机号验证
          $("#loginByPhone").css("display", "block");
            Register.mobileShowPhone()
        } else {
          $("#loginByPhone").css("display", "none");
          $("#mobileNumberDiv").css("display", "none");
          $("#mobileVerifyCodeDiv").css("display", "none");
          $("#code_login_select").css("display", "none");
        }
      } else {
        $("#loginByPhone").css("display", "none");
        $("#mobileNumberDiv").css("display", "none");
        $("#mobileVerifyCodeDiv").css("display", "none");
        $("#code_login_select").css("display", "none");
      }
    },
    error : function(data) {
      $("#isPhoneCheck").val(data.result);
      $("#loginByPhone").css("display", "none");
      $("#mobileNumberDiv").css("display", "none");
      $("#mobileVerifyCodeDiv").css("display", "none");
      $("#code_login_select").css("display", "none");
    }
  });
}


/**
 * 手机端显示手机号
 */
Register.mobileShowPhone = function () {
    $("#mobile_bottom_height").css("display","none");
    $("#mobile_register_div").css("display","block");
}



