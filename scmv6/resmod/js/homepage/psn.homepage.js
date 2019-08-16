var Resume = Resume ? Resume : {};
Resume.personInfo = Resume.personInfo ? Resume.personInfo : {};
Resume.personInfo.base = Resume.personInfo.base ? Resume.personInfo.base : {};
var Psn_Info = Psn_Info ? Psn_Info : {};
// 保存短地址--zzx-----
Psn_Info.doSaveShortUrl = function(oldurl, newurl, myfunction1, myfunction2) {
  $.ajax({
    url : '/psnweb/homepage/ajaxsavepsnshorturl',
    type : 'post',
    dataType : 'json',
    async : false,
    data : {
      'des3PsnId' : $("#des3PsnId").val(),
      'oldShortUrl' : oldurl,
      'newShortUrl' : newurl
    },
    success : function(data) {
      if (data.result == "success") {
        myfunction1();
        // scmpublictoast(data.msg,2000);
      } else {
        myfunction2();
        scmpublictoast(data.msg, 2000);
      }
    },
    error : function() {
      myfunction2();
      scmpublictoast(homepage.systemBusy, 2000);
    }
  });
};
Psn_Info.copyUrl = function() {
  const $range = document.createRange();
  $range.selectNodeContents(document.getElementById('span_shorturl'));
  window.getSelection().removeAllRanges();
  window.getSelection().addRange($range);
  document.execCommand('Copy');
};
// 点击编辑短地址------------zzx---------------
Psn_Info.modificationshortUrl = function(event) {
  if (1 != event.which) {
    return;
  }
  var span_shorturl = $("#span_shorturl");
  // 最开始的短地址
  var shorturl = $("#span_shorturl").attr("scm-oldurl");
  if ($("#input_shorturl").length > 0) {
    // 移除编辑框前要还原内容
    span_shorturl.text(span_shorturl.attr("scm-oldurl"));
    $("#input_shorturl").remove();
    return;
  }
  // 清理浏览器默认事件
  event.preventDefault();
  if (Psn_Info.getSelectionText().length > 0) {
    // 取消选中状态
    if (window.getSelection()) {
      window.getSelection().removeAllRanges();
    } else {
      document.selection.empty();
    }
    // 操作短地址可编辑部分
    var strArr = shorturl.split("/P/");
    strArr[0] += "/P/";
    span_shorturl.text(strArr[0]);
    // 添加编辑框
    $("#span_shorturl").after(
        "<input id='input_shorturl'style='min-width:10px;'  type='text' value='" + strArr[1] + "'/>");
    $("#input_shorturl").focus();
    var num = $('#input_shorturl').val().length;
    $('#input_shorturl')[0].setSelectionRange(num, num);
    // 给编辑框绑定事件
    $("#input_shorturl").bind("keydown", function(ev) {
      if (ev.keyCode == 13) {
        // 正则过滤用户输入的短地址
        // var patrn=/^(?!_)(?!.*?_$)[a-zA-Z0-9_]{1,20}$/; //不能已下划线开头或结尾
        var patrn = /^[a-zA-Z0-9_]{1,20}$/;
        if (patrn.exec($('#input_shorturl').val()) == null) {
          scmpublictoast(homepage.shortAddressNote, 3000);
          return;
        }
        // 发请求保存短地址
        Psn_Info.doSaveShortUrl(strArr[1], $('#input_shorturl').val(), function() {
          // 保存成功-移除编辑框前要记录编辑内容
          $("#span_shorturl").attr("scm-oldurl", span_shorturl.text() + $('#input_shorturl').val());
          span_shorturl.text(span_shorturl.text() + $('#input_shorturl').val());
          $("#input_shorturl").remove();
          $("#copyEle").attr("data-clipboard-text", span_shorturl.text());
        }, function() {
          // 保存失败-移除编辑框前要还原内容
          span_shorturl.text(span_shorturl.attr("scm-oldurl"));
          $("#input_shorturl").remove();
        });
      }
    }).bind("blur", function() {
      // 移除编辑框前要还原内容
      span_shorturl.text(span_shorturl.attr("scm-oldurl"));
      $("#input_shorturl").remove();
    });
  } else {
    // 第一次点击设置选中
    Psn_Info.selectText("id_shorturl");
  }
}
// 检查短地址是否合法----------zzx----------
Psn_Info.checkShortUrl = function(shortUrl) {
  if (Regex.IsMatch(shortUrl, "")) {

  } else {

  };
}
// 使元素的文本选中----------zzx----------
Psn_Info.selectText = function(element) {
  var text = $("#span_shorturl")[0];
  if (document.body.createTextRange) {
    var range = document.body.createTextRange();
    range.moveToElementText(text);
    range.select();
  } else if (window.getSelection) {
    var selection = window.getSelection();
    var range = document.createRange();
    range.selectNodeContents(text);
    selection.removeAllRanges();
    selection.addRange(range);
  }
}
// 获取选中的文本- 用于判断是否已经点击一次-----------zzx----------------
Psn_Info.getSelectionText = function() {
  if (window.getSelection) {
    return window.getSelection().toString();
  } else if (document.selection && document.selection.createRange) {
    return document.selection.createRange().text;
  }
  return '';
}
/**
 * 根据权限选项，加载主页各个模块
 */
function loadHomepageModule() {
  if ($("#isMySelf").val() == "true") {
    ajaxFindPsnBriefDesc();
    showPsnScienceArea();
    ajaxShowPsnKeyWords(false);
    showPsnWorkHistory();
    showPsnEduHistory();
    showPsnRepresentPub();
    showPsnRepresentPrj();
    ajaxFindPsnContactInfo();// 打开联系人设置
    // 获取来访人员列表
    Resume.getPsnList();
    // 基金推荐
    /* Resume.showRecommendFund(); */
    // 个人主页推荐人员
    Resume.showRecommendPsn();
    // 个人主页弹框
    if (jumpto != undefined && jumpto === "showSecuritySetting") {
      showSecuritySetting()
    }
    Psn_Info.initPsnConfInfo();
  } else {
    if ($(".conf_input:checked").length == 0) {
      $(".dev_private_msg").show();
    }
    $(".conf_input:checked").each(function() {
      var _this = $(this);
      var moduleName = _this.attr("name");
      switch (moduleName) {
        case "briefConf" :
          ajaxFindPsnBriefDesc();
        break;
        case "keywordsConf" :
          showPsnScienceArea();
          ajaxShowPsnKeyWords(false);
        break;
        case "workHistoryConf" :
          showPsnWorkHistory();
        break;
        case "eduHistoryConf" :
          showPsnEduHistory();
        break;
        case "representPubConf" :
          showPsnRepresentPub();
        break;
        case "representPrjConf" :
          showPsnRepresentPrj();
        break;
        case "contactInfoConf" :
          ajaxFindPsnContactInfo();
        break;
      }
    });
    if ($("#cnfAnyMode").val() != 0) {
      // 合作者
      Resume.cooperator();
    }
  }

}

function toHomePage() {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    if ($("#isMySelf").val() == "true") {
      window.location.href = "/psnweb/homepage/show";
    } else {
      window.location.href = "/psnweb/homepage/show?des3PsnId=" + $("#des3PsnId").val();
    }
  }, 1);
}

/**
 * 显示权限选择框
 */
function showSecuritySetting() {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    loadPsnInfoConf();
    showDialog("homepage_security_setting");
  }, 1);
}

// 隐藏权限选择框
function reloadHomePage(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("homepage_security_setting");
}

// 人员信息----------------------begin
function buildWorkHistorySelector() {
  return {
    "des3PsnId" : $("#des3PsnId").val()
  };
}

/**
 * 工作经历选择后改变所在地区的值
 * 
 * @param insId
 */
function changeInsAddr(workId) {
  $("#psnRegionName").val($("#ins_" + workId).val());
  $("#psnRegionId").val($("#regionId_" + workId).val());
  // $("#psnRegionName").attr("code", $("#regionId_"+workId).val());
  $("#workId").val(workId);
  $("#currentWorkSelectDiv").addClass("input_not-null");
  workArea();
}

// 编辑人员信息
function editPsnInfo() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxedit",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#psnInfoBox").html(data);
        addFormElementsEvents($("#psnInfoBox")[0]);
        showDialog("psnInfoBox");
        if (typeof po_last == "function") {
          po_last($("#psnBriefDescArea")[0]);
        }
      });

    },
    error : function() {
    }
  });
}
/**
 * 编辑个人简介
 * 
 * @returns
 */
function psnBriefDesc() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxedit",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#psnInfoBox").html(data);
        addFormElementsEvents($("#psnInfoBox")[0]);
        showDialog("psnInfoBox");
        showFocus($("#psnBriefDescArea")[0]);
      });

    },
    error : function() {
    }
  });
}
// 定位input、textarea
function showFocus(obj) {
  obj.focus();// 解决ff不获取焦点无法定位问题
  if (window.getSelection) {// ie11 10 9 ff safari
    var max_Len = obj.value.length;// text字符数
    obj.setSelectionRange(max_Len, max_Len);
  } else if (document.selection) {// ie10 9 8 7 6 5
    var range = obj.createTextRange();// 创建range
    range.collapse(false);// 光标移至最后
    range.select();// 避免产生空格
  }
}

function reloadPsnBaseInfo() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxbase",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#psn_info_head").html(data);
      });

    },
    error : function() {
    }
  });
}

// 隐藏人员信息编辑框
function hidePsnInfoBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("psnInfoBox");
}

// 保存人员信息
function savePsnInfo(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var workId = $("#workId").val();
  var firstName = $("#firstName").val();
  var lastName = $("#lastName").val();
  var zhFirstName = $("#zhFirstName").val();
  var zhLastName = $("#zhLastName").val();
  var otherName = Resume.trimLeftAndRightSpace($("#otherName").val());
  var name = "";
  var check = Resume.personInfo.base.checkName(firstName, lastName, zhFirstName, zhLastName);
  if (check) {
    return false;
  }
  if (checkIsNull(name)) {
    if (!checkIsNull(zhLastName + zhFirstName)) {
      name = zhLastName + zhFirstName;
    } else {
      name = firstName + " " + lastName;
    }
  }
  var newBrief = $("#psnBriefDescArea").val();
  $.ajax({
    url : "/psnweb/baseinfo/ajaxsave",
    type : "post",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "insId" : $("#psnInsId").val(),
      "insName" : $("#psnInsName").val(),
      "department" : $("#psnDepartment").val(),
      "titolo" : $("#psnTitolo").val(),
      "regionId" : $("#psnRegionId").val(),
      "reginonName" : $("#psnRegionName").val(),
      "workId" : workId,
      "psnBriefDesc" : newBrief,
      "firstName" : firstName,
      "lastName" : lastName,
      "zhFirstName" : zhFirstName,
      "zhLastName" : zhLastName,
      "name" : name,
      "otherName" : otherName
    },
    dataType : "json",
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          $("#psn_insAndDept").html(data.insAndDept);
          $("#psn_posAndTitolo").html(data.posiAndTitolo);
          $("#psn_regionName").html(data.regionName);
          $(".work_history_isprimary").val("0");
          $("#" + workId + "_isPrimary").val("1");
          $("#psn_name").html(data.psnName);
          $("#psn_avatar").attr("src", data.avatars);
          hideDialog("psnInfoBox");
          // ajaxFindPsnBriefDesc();
          // 刷新当前页面
          window.location.href = window.location.href;
        } else if (data.result == "checkError") {
          scmpublictoast(data.msg, 1000);
        } else {
          // 提示信息
          scmpublictoast(homepage.systemBusy, 1000);
        }
      });

    },
    error : function() {
    }
  });
}

// 人员信息----------------------end

// 配置信息--------------------begin

// 加载人员各模块配置信息
function loadPsnInfoConf() {
  var anyMod = $("#cnfAnyMode").val();
  var openHomepage = false;
  $(".conf_input").each(function() {
    var _this = $(this);
    var _val = _this.val();
    if ((anyMod & _val) == _val) {
      $("#openHomepage").attr("checked", true);
      openHomepage = true;
    }
    _this.attr("checked", (anyMod & _val) == _val);
  });
  if (!openHomepage) {
    $("#closeHomepage").attr("checked", true);
    $(".conf_input").each(function() {
      $(this).attr("disabled", "disabled");
    });
  } else {
    $(".conf_input").removeAttr("disabled");
  }
}

// 保存人员各模块配置信息
function savePsnConfInfo(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var anyModNew = 0;
  $(".conf_input:checked").each(function() {// 设置复选框选择状态
    var _this = $(this);
    anyModNew |= _this.val();
  });
  if ($("input[name='homepageSecurity']:checked").val() == 0) {
    anyModNew = 0;
  }

  $(this).attr("disabled", "disabled");
  // 修改模块查看权限
  $.ajax({
    url : '/psnweb/homepage/ajaxsavemod',
    type : 'post',
    dataType : 'json',
    data : {
      anyMod : anyModNew
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        parent.window.location.href = '/psnweb/homepage/show';
      });

    },
    error : function() {
      parent.window.location.href = '/psnweb/homepage/show';
    }
  });
}

// 各个模块配置权限的公开和隐私选择时间
function chooseHomepageSecurity() {
  document.getElementById("closeHomepage").addEventListener("change", function() {
    if (this.checked) {
      Array.from(document.querySelectorAll(".conf_input")).forEach(function(x) {
        x.disabled = true;
      })
    }
  })

  document.getElementById("openHomepage").addEventListener("change", function() {
    // if (this.checked){
    Array.from(document.querySelectorAll(".conf_input")).forEach(function(x) {
      x.disabled = false;
      x.checked = true;
    })
    // }
  })
}
// 配置信息--------------------end

// 简介-----------begin
// 显示简介信息
function ajaxFindPsnBriefDesc() {
  $.ajax({
    url : "/psnweb/briefdesc/ajaxshow",
    type : "post",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    dataType : "html",
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#briefDescModule").html(data);
        $("#briefDescModule").show();
        hideOperationBtn();
      });

    },
    error : function() {

    }
  });
}

// 弹出简介编辑框
function showBriefDescBox() {
  $.ajax({
    url : "/psnweb/briefdesc/ajaxedit",
    type : "post",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    dataType : "html",
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#briefDescBox").html(data);
        addFormElementsEvents($("#briefDescBox")[0]);
        showDialog("briefDescBox");
      });

    },
    error : function() {
    }
  });
}

// 隐藏简介编辑框
function hideBriefDescBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  };
  hideDialog("briefDescBox");
}

// 保存简介信息
function savePsnBrief(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var postData = "{}";
  var newBrief = $("#psnBriefDescArea").val();
  $.ajax({
    url : "/psnweb/brief/ajaxsave",
    type : "post",
    dataType : "json",
    data : {
      "psnBriefDesc" : newBrief,
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          // $("#psnBriefVal").html(newBrief)
          ajaxFindPsnBriefDesc();
          hideBriefDescBox();
          // scmpublictoast(homepage.operateSuccess,1000);
        }

      });

    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
    }
  });
}

// 简介--------------end

// 联系信息--------------begin
// 隐藏联系信息编辑框
function hideContactInfoBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  // hideDialog("contactInfoBox");
  // 不使用以前的弹框了，2018-04-23
  $("#contactInfoBox").css("display", "none");
}

// 显示联系信息编辑框
function showContactInfoBox() {
  $.ajax({
    url : "/psnweb/contact/ajaxedit",
    type : "post",
    dataType : "json",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#tel-error").css("display", "none")
        // $("#email_div").removeClass("input_invalid");
        if (data.result == "success") {
          var editObj = document.getElementsByClassName("new_edit-psninfor")[0];
          positionfix({
            screentarget : editObj
          });
          /* positionfix({'screentarget':editObj}); */
          $("#tel").val(data.tel);
          $("#email").val(data.email);
          // addFormElementsEvents($("#contactInfoBox")[0]);
          // showDialog("contactInfoBox");
        }
      });

    },
    error : function() {

    }
  });

}

// 保存人员联系信息
function saveContactInfo(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var tel = $("#tel").val();
  var email = $("#email").val();

  var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
  // var
  // isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
  var isMob = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
  if (isPhone.test(tel) || isMob.test(tel)) {
    $.ajax({
      url : "/psnweb/contact/ajaxsave",
      type : "post",
      dataType : "json",
      data : {
        "des3PsnId" : $("#des3PsnId").val(),
        "tel" : tel,
        "email" : email
      },
      success : function(data) {
        Resume.ajaxTimeOut(data, function() {
          if (data.result == "success") {
            $("#psnTel").val(tel);
            $("#psnEmail").val(email.toLowerCase());
            hideContactInfoBox();
          } else {
            if (data.code == "email_error") {
              $("#email_div").addClass("input_invalid");
              $("#email_error").attr("invalid-message", homepage.trueEmail);
            }
            if (data.code == "tel_toLong") {
              $("#tel-error").css("display", "block")
              $("#tel-error").html(homepage.truePhone);
            }
          }
        });

      },
      error : function() {
      }
    });

  } else {
    $("#tel-error").css("display", "block");
    $("#tel-error").html(homepage.truePhone);
  }

}
function checkPhone() {
  var tel = $("#tel").val();
  var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
  // var
  // isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
  var isMob = /^1[3|4|5|6|7|8|9][0-9]{9}$/;;
  if (isPhone.test(tel) || isMob.test(tel)) {
    $("#tel-error").css("display", "none");
    $("#tel-error").html("");;
  } else {
    $("#tel-error").css("display", "block");
    $("#tel-error").html(homepage.truePhone);;
  }

}
// 显示人员联系信息
function ajaxFindPsnContactInfo() {
  $.ajax({
    url : "/psnweb/contact/ajaxshow",
    type : "post",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    dataType : "html",
    success : function(data) {

      Resume.ajaxTimeOut(data, function() {
        $("#contactInfoModule").html(data);
        $("#contactInfoModule").show();
        hideOperationBtn();
      });

    },
    error : function() {

    }
  });
  $("#contactInfoModule").show();
}

function editPsnEmailAccount(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  window.open("/psnweb/psnsetting/main?model=account");
}

// 联系信息--------------end

// 关键词---------------begin
// 隐藏关键词编辑框
function hidePsnKeyWordsBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("psnKeyWordsBox");
}

// 显示关键词编辑框
function showPsnKeyWordsBox() {
  ajaxShowPsnKeyWords(true);

}

// 保存人员关键词
function savePsnKeyWords(obj) {

  var keysArr = [];
  var keyStrArr = [];
  var keyStr = "";
  $.each($("#oneKeyWords").find(".chip__text"), function(i, o) {
    /* keysArr.push($(o).text()); */
    keysArr.push({
      'keys' : $(o).text()
    });
    keyStrArr.push($(o).text().toUpperCase());
  });
  if (arrayCheck(keyStrArr)) {
    scmpublictoast(homepage.keyWordRepeat, 1000);
    return;
  }
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var str_key = JSON.stringify(keysArr);
  if (str_key == "[]") {// 关键词为空
    str_key = '[{"keys":""}]';// 区分学科领域与关键词
  }
  var postData = {
    'keywordStr' : str_key,
    'des3PsnId' : $("#des3PsnId").val()
  };
  $.ajax({
    url : "/psnweb/keywords/ajaxsave",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          $("div.psnkeyword_show:hidden").remove();
          if (data.keywordsList != null) {
            var keywordsHtml = "";
            for (var i = 0; i < data.keywordsList.length; i++) {
              keywordsHtml += '<div class="kw-chip_medium new_class">' + data.keywordsList[i] + '</div>';
            }
            $("#psnkeywords_list").html(keywordsHtml);
          }
          hidePsnKeyWordsBox();
          $("div#keywordsModule").remove();
          ajaxShowPsnKeyWords(false);
        } else if (data.result == "blank") {
          scmpublictoast("至少选择1个关键词", 1500);
        }
      });

    },
    error : function() {

    }
  });

}
function arrayCheck(arr)// 数组查重
{
  var hash = {};
  for ( var i in arr) {
    if (hash[arr[i]]) {
      return true;
    }
    hash[arr[i]] = true;
  }
  return false;
}
// 人员关键词模块显示
function ajaxShowPsnKeyWords(editKeywords) {
  $.ajax({
    url : "/psnweb/keywords/ajaxshow",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "editKeywords" : editKeywords
    },
    success : function(data) {

      Resume.ajaxTimeOut(data, function() {
        if (editKeywords == false) {
          $(data).insertAfter($("#keywordsModuleDiv"));
          $("#keywordsModule").show();
          hideOperationBtn();
        } else {
          $("#psnKeyWordsBox").remove();
          $(data).insertAfter($("#keywordsModule"));
          showDialog("psnKeyWordsBox");
        }
      })

    },
    error : function() {

    }
  });
}

function removeKeywords(id) {
  if (id != "" && id != null) {
    var boxkeywords = "#" + id;
    $(boxkeywords).hide();
  }
  /* dealKeywordSaveBtnStatus(); */
}

function delThisKeyWords(obj) {
  $(obj).parent("div.psnkeyword_show").remove();
}

function addPsnKeywords(id, keyword, $createGrpChipBox) {
  var chipbox = $("div[chipbox-id='oneKeyWords']");
  var chipNum = $("div[chipbox-id='oneKeyWords']").children("div.chip__box").length;
  if (!(chipNum < 10)) {
    // SCM-16133 关键词超过10个
    chipbox.addClass("count_exceed");
    return;
  }
  if (id != null && id != "") {
    var rcdKeyword = "#" + id;
    $(rcdKeyword).remove();
    var strS = "<div class=\"chip__box\" id=\"disciplinesAndKeywords\"><div class=\"chip__avatar\"></div><div class=\"chip__text\">";
    var strE = "</div><div class=\"chip__icon icon_delete\" onclick=\"javascript:removeKeywords('boxkeywords');\"><i class=\"material-icons\">close</i></div>";
    var str = "";
    str = strS + keyword + strE;
    var boxkeywordsNumber = chipNum + 1;
    str = str.replace("boxkeywords", "boxkeywords_" + boxkeywordsNumber);
    $("#autokeywords").before(str);
    if ($(".recommendKeywordItem").size() <= 0) {
      $('.psn_rcmd_keywords').html("");
    }
  }
  $createGrpChipBox.chipBoxInitialize();
  /* dealKeywordSaveBtnStatus(); */
}

// 处理关键词输入框完成按钮状态
/*
 * dealKeywordSaveBtnStatus = function(){ var keyCount =
 * $("#oneKeyWords").find(".chip__box").length; if(keyCount > 0){
 * $("#homepage_keywords_save_btn").removeAttr("disabled"); }else{
 * $("#homepage_keywords_save_btn").attr("disabled", "true"); } }
 */
// 关键词-------------------end
// 科技领域------------------begin
function showPsnScienceArea() {
  $.ajax({
    url : "/psnweb/sciencearea/ajaxshow",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#scienceAreaDiv").remove();
        $(data).insertAfter($("#scienceAreaModuleDiv"));
        hideOperationBtn();
      });

    },
    error : function() {
    }
  });
}

function showScienceAreaBox() {
  $.ajax({
    url : "/psnweb/sciencearea/ajaxedit",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#scienceAreaBox").html(data);
        showDialog("scienceAreaBox");
      });

    },
    error : function() {
    }
  });
}

function hideScienceAreaBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("scienceAreaBox");
}

function savePsnScienceArea(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $.ajax({
    url : "/psnweb/sciencearea/ajaxsave",
    type : "post",
    dataType : "json",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "scienceAreaIds" : $("#scienceAreaIds").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          hideScienceAreaBox();
          showPsnScienceArea();
        } else if (data.result == "blank") {
          scmpublictoast(homepage.scienceAreaNotNull, 1500);
        }
      });

    },
    error : function() {
    }
  });
}

function addScienceArea(id) {
  var key = $("#" + id + "_category_title").html();
  var sum = parseInt($("#scienceAreaSum").val());
  if (sum < 5) {
    var html = '<div class="main-list__item" style="padding: 0px 16px!important;" id="choosed_' + id + '">'
        + '<div class="main-list__item_content">' + key + '</div>'
        + '<div class="main-list__item_actions"  onclick="javascript:delScienceArea(\'' + id
        + '\');"><i class="material-icons">close</i></div></div>';
    $("#choosed_area_list").append(html);
    $("#unchecked_area_" + id).attr("onclick", "");
    $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
    $("#" + id + "_status").html("check");
    $("#" + id + "_status").css("color", "forestgreen");
    $("#scienceAreaSum").val(sum + 1);
    $("#scienceAreaIds").val($("#scienceAreaIds").val() + "," + id);
  } else {
    // 出提示语
    scmpublictoast(homepage.scienceAreaLimit, 1500);
  }
  /* dealAreaSaveBtnStatus(); */
}

function delScienceArea(id) {
  $("#choosed_" + id).remove();
  $("#checked_area_" + id).attr("onclick", "addScienceArea('" + id + "')");
  $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
  $("#" + id + "_status").html("add");
  $("#" + id + "_status").css("color", "");
  $("#scienceAreaIds").val($("#scienceAreaIds").val().replace("," + id, ""));
  var sum = parseInt($("#scienceAreaSum").val());
  $("#scienceAreaSum").val(sum - 1);
  /* dealAreaSaveBtnStatus(); */
}

/*
 * function identifyScienceArea(scienceAreaId){ var idenSum =
 * parseInt($("#idenSum_"+scienceAreaId).html()); idenSum = idenSum + 1; $.ajax({ url:
 * "/psnweb/sciencearea/ajaxidentify", type: "post", dataType: "json", data: { "des3PsnId":
 * $("#des3PsnId").val(), "oneScienceAreaId" : scienceAreaId, "idenSum" : idenSum }, success:
 * function(data){ Resume.ajaxTimeOut(data ,function(){ if(data.result == "success"){
 * $("#idenSum_"+scienceAreaId).html(data.sum); $("#area_"+scienceAreaId).hide(); //
 * scmpublictoast(homepage.operateSuccess,1000); }else{ scmpublictoast(homepage.systemBusy,1000); }
 * }); }, error: function(){ scmpublictoast(homepage.systemBusy,1000); } }); }
 */
// 认同关键词
function identifyKeyword(keyworId) {
  $.ajax({
    url : "/psnweb/keyword/ajaxIdentificKeyword",
    type : "post",
    dataType : "json",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "oneKeyWordId" : keyworId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          $("#idenSum_" + keyworId).html(data.sum);
          $("#area_" + keyworId).hide();
          // scmpublictoast(homepage.operateSuccess,1000);
        } else {
          scmpublictoast(homepage.systemBusy, 1000);
        }
      });

    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
    }
  });
}
// 弹出科技领域认同人员的弹出框
function showIdentifyPsnBox(kwId) {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    showDialog("keyWordIdentificPsnBox");
    showKwIdentificPsnList(kwId);
  }, 1);
}
/**
 * 查询关键词认同人员列表
 */
function showKwIdentificPsnList(kwId) {
  window.Mainlist({
    name : 'psnInfoList',
    listurl : '/psnweb/psnlist/ajaxshow',
    listdata : {
      "des3PsnId" : $("#des3PsnId").val(),
      "discId" : kwId,
      "serviceType" : "kwIdentific"
    },
    listcallback : function(xhr) {
    },
    method : 'scroll'
  });
}
/**
 * 查询科技领域认同人员列表
 */
function showSaIdentificPsnList(scienceAreaId) {
  window.Mainlist({
    name : 'psnInfoList',
    listurl : '/psnweb/psnlist/ajaxshow',
    listdata : {
      "des3PsnId" : $("#des3PsnId").val(),
      "scienceAreaId" : scienceAreaId,
      "serviceType" : "saIdentific"
    },
    listcallback : function(xhr) {
    },
    method : 'scroll'
  });
}

// 判断当前选中的科技领域数量，禁用或启用下一步按钮
/*
 * dealAreaSaveBtnStatus = function(){ if(checkIsNull($("#scienceAreaIds").val())){
 * $("#homepage_area_save_btn").attr("disabled", "true"); }else{
 * $("#homepage_area_save_btn").removeAttr("disabled"); } }
 */
// 科技领域------------------end
// 代表性成果-----------------begin
function showPsnRepresentPub() {
  $.ajax({
    url : "/pub/representpub/ajaxshow",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $(data).insertAfter($("#representPubModuleDiv"));
        hideOperationBtn();
        Pub.loadPubFulltext();
      });

    },
    error : function() {

    }
  });
}

function showPsnRepresentPubBox() {
  $.ajax({
    url : "/pub/representpub/ajaxedit",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "cnfId" : $("#cnfId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#representPubBox").html(data);
        addFormElementsEvents($("#representPubBox")[0]);
        showDialog("representPubBox");
        checkRepresentPubStatus();
        getPsnOpenPub();
      });

    },
    error : function() {

    }
  });

}

function getPsnOpenPub() {
  window.Mainlist({
    name : 'psnOpenPubList',
    listurl : '/pub/ajaxgetopenlist',
    listdata : {
      "des3SearchPsnId" : $("#des3PsnId").val(),
      "cnfId" : $("#cnfId").val()
    },
    listcallback : function(xhr) {
      resetPubCheckStatus();
    },
    method : 'scroll'
  });
}

function hideRepresentPubBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $("#addToRepresentPubIds").val($("#addToRepresentPubIdsOld").val());
  hideDialog("representPubBox");
  $("#pubPageNo").val(1);
}

function addToRepresentPub(obj) {
  // 最多加10个
  var pubSum = $("#addedPubList").find(".main-list__item").length - 1;// 有一个是用来克隆的
  var $item = $(obj).closest(".main-list__item");
  var des3PubId = $item.attr("des3PubId");
  if (pubSum < 10 && $("#addedPubList").find(".main-list__item[des3pubid='" + des3PubId + "']").length == 0) {
    var title = $item.find(".pubTitle").html();
    var author = $item.find(".pubAuthorNames").html();
    var briefDesc = $item.find(".pub_BriefDesc").html();
    var $addNode = $("#addedPubItem").clone(true);
    $addNode.attr("des3pubid", des3PubId);
    $addNode.find(".selected_pub_title").html(title);
    $addNode.find(".selected_pub_author").html(author);
    $addNode.find(".selected_pub_BriefDesc").html(briefDesc);
    $addNode.attr("id", "");
    $addNode.attr("seq_pub", pubSum + 1);
    $("#addedPubItem").before($addNode);
    $($addNode).show();
    var $checkPubDiv = $item.find(".checkPubDiv");
    $checkPubDiv.children("i").html("check");
    $checkPubDiv.children("i").css("color", "forestgreen");
    $checkPubDiv.removeAttr("onclick");
    $item.find(".pub-idx__main").removeAttr("onclick");
    checkRepresentPubStatus();
  } else {
    // 出提示语
    scmpublictoast("最多选择10个代表成果", 1500);
  }
}

function delRepresentPub(obj) {
  var $deleteitem = $(obj).closest(".main-list__item");
  var des3PubId = $deleteitem.attr("des3PubId");
  var $item = $("#psnOpenPubList").find(".main-list__item[des3PubId='" + des3PubId + "']");
  var $selectDiv = $("#psnOpenPubList").find(".main-list__item[des3PubId='" + des3PubId + "']").find(".checkPubDiv");
  $selectDiv.children("i").html("add");
  $selectDiv.children("i").css("color", "");
  $selectDiv.attr("onclick", "addToRepresentPub(this)");
  $item.find(".pub-idx__main").attr("onclick", "addToRepresentPub(this)");
  $deleteitem.remove();
  checkRepresentPubStatus();
}
function downMoveRepresentPub(obj) {
  var $curItem = $(obj).closest(".main-list__item");
  var $clone = cloneRepPub($curItem);
  var seq_pub = $curItem.attr("seq_pub");
  seq_pub = numObj = new Number(seq_pub)
  seq_pub = seq_pub + 1;
  var $moverItem = $("#addedPubList").find("[seq_pub='" + seq_pub + "']");
  $moverItem.after($clone);
  $moverItem.attr("seq_pub", seq_pub - 1);
  $clone.attr("seq_pub", seq_pub);
  $curItem.remove();
  checkRepresentPubStatus();
}
function cloneRepPub($curItem) {
  var title = $curItem.find(".selected_pub_title").html();
  var author = $curItem.find(".selected_pub_author").html();
  var briefDesc = $curItem.find(".selected_pub_BriefDesc").html();
  var des3PubId = $curItem.attr("des3PubId");
  var $addNode = $("#addedPubItem").clone(true);
  $addNode.attr("des3pubid", des3PubId);
  $addNode.find(".selected_pub_title").html(title);
  $addNode.find(".selected_pub_author").html(author);
  $addNode.find(".selected_pub_BriefDesc").html(briefDesc);
  $addNode.css("display", "");
  $addNode.attr("id", "");
  return $addNode;
}
function upMoveRepresentPub(obj) {
  var $curItem = $(obj).closest(".main-list__item");
  var $clone = cloneRepPub($curItem);
  var seq_pub = $curItem.attr("seq_pub");
  seq_pub = numObj = new Number(seq_pub)
  seq_pub = seq_pub - 1;
  var $moverItem = $("#addedPubList").find("[seq_pub='" + seq_pub + "']");
  $moverItem.before($clone);
  $moverItem.attr("seq_pub", seq_pub + 1);
  $clone.attr("seq_pub", seq_pub);
  $curItem.remove();
  checkRepresentPubStatus();
}
function checkRepresentPubStatus() {
  var len = $("#addedPubList").find(".main-list__item").length - 1;
  if (len < 1) {
    return;
  } else if (len == 1) {
    $("#addedPubList").find(".main-list__item").each(function(i) {
      if (i == 0) {
        $(this).find(".main-list__item_actions .arrow_up").remove();
        $(this).find(".main-list__item_actions .arrow_down").remove();
        $(this).attr("seq_pub", i + 1);
      }
    });
  } else {
    $("#addedPubList").find(".main-list__item").each(
        function(i) {
          if (i != len) {
            $(this).attr("seq_pub", i + 1);
          }
          if (i == 0) {
            $(this).find(".main-list__item_actions .arrow_up").remove();
            $(this).find(".main-list__item_actions .arrow_down").remove();
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_down arrow_down" ' + 'onclick="downMoveRepresentPub(this);"></i>')
          } else if (i == len - 1) {
            $(this).find(".main-list__item_actions .arrow_down").remove();
            $(this).find(".main-list__item_actions .arrow_up").remove();
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_up arrow_up" ' + 'onclick="upMoveRepresentPub(this);"></i>')
          } else {
            $(this).find(".main-list__item_actions .arrow_down").remove();
            $(this).find(".main-list__item_actions .arrow_up").remove();
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_up arrow_up" ' + 'onclick="upMoveRepresentPub(this);"></i>')
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_down arrow_down" ' + 'onclick="downMoveRepresentPub(this);"></i>')
          }
        });
  }

}

function savePsnRepresentPub(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var addDes3PubId = "";
  $("#addedPubList").find(".main-list__item").each(function() {
    var des3PubId = $(this).attr("des3pubid");
    if (des3PubId != "") {
      addDes3PubId += des3PubId + ",";
    }
  });
  $(obj).attr("disabled", "disabled");
  $.ajax({
    url : "/pub/representpub/ajaxsave",
    type : "post",
    dataType : "json",
    data : {
      'addToRepresentPubIds' : addDes3PubId.replace(",$", ""),
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          $(obj).removeAttr("disabled");
          hideRepresentPubBox();
        }
        // 刷新模块
        showPsnRepresentPub();
        $(".representPubDiv").remove();
      });

    },
    error : function() {
    }
  });
}

function viewPubDetails(des3Id, Id) {
  var params = "des3Id=" + encodeURIComponent(des3Id);
  var pram = {
    pubId : Id,
    des3Id : des3Id
  };
  newwindow = window.open("about:blank");
  $.ajax({
    url : "/pubweb/publication/ajaxview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
        /* newwiondow.focus(); */
      }
      if (data.result == 1 || data.ajaxSessionTimeOut == 'yes') {
        newwindow.location.href = "/pubweb/outside/details?" + params + "&currentDomain=" + "/pubweb" + "&pubFlag=1";
        /* newwindow.focus(); */
      } else if (data.result == 0) {
        newwindow.location.href = "/pubweb/publication/wait?" + params;
      }
    },
    error : function() {

    }
  });
}

// 重置成果列表中成果选中状态
function resetPubCheckStatus() {
  var $selectPub = $("#addedPubList").find(".main-list__item");
  $selectPub.each(function() {
    var des3PubId = $(this).attr("des3pubid");
    var $item = $("#psnOpenPubList").find(".main-list__item[des3pubid='" + des3PubId + "']");
    $item.find(".checkPubDiv").attr("onclick", "").find("i").html("check").css("color", "forestgreen").removeClass(
        "pubCheckStatus");
    $item.find(".pub-idx__main").removeAttr("onclick");

  });
}

// 代表性成果-------------------end

// 代表性项目-------------------begin
function showPsnRepresentPrj() {
  $.ajax({
    url : "/psnweb/representprj/ajaxshow",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $(data).insertAfter($("#representPrjModuleDiv"));
        hideOperationBtn();
      });

    },
    error : function() {

    }
  });
}

function showPsnRepresentPrjBox() {
  $.ajax({
    url : "/psnweb/representprj/ajaxedit",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "cnfId" : $("#cnfId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#representPrjBox").html(data);
        addFormElementsEvents($("#representPrjBox")[0]);
        showDialog("representPrjBox");
        getPsnOpenPrj();
        checkRepresentPrjStatus();
      });

    },
    error : function() {

    }
  });
}

function hideRepresentPrjBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $("#representPrjIds").val($("#representPrjIdsOld").val());
  hideDialog("representPrjBox");
}

function addToRepresentPrj(id) {
  if ($("#representPrj_" + id).length == 1) {
    return;
  }
  var prjSum = parseInt($("#representPrjSum").val());
  var title = $("#title_" + id).html();
  var author = $("#authorNames_" + id).html();
  if (prjSum < 5) {
    $("#checkDiv_" + id).children("i").html("check");
    $("#checkDiv_" + id).children("i").css("color", "forestgreen");
    $("#checkDiv_" + id).removeAttr("onclick");
    // var addHtml = $("#addedPrjItem").prop("outerHTML");
    // addHtml = addHtml.replace("replacetitle", title).replace("replaceAuthorName",
    // author).replace("addedPrjItem", "representPrj_" + id).replace("onclickmethod",
    // 'onclick="delRepresentPrj(\''+id+'\');"').replace("$prjId$", id);
    var $addNode = cloneRepPrj($("#unadded_" + id));
    $addNode.attr("prj_id", id);
    $addNode.attr("id", "representPrj_" + id);
    $("#addedPrjItem").before($addNode);
    $("#representPrj_" + id).show();
    $("#representPrj_" + id).attr("seq_prj", prjSum + 1);
    $("#representPrj_" + id).attr("prj_id", id);
    var prjIds = $("#representPrjIds").val();
    $("#representPrjIds").val(prjIds + id + ",");
    $("#representPrjSum").val(prjSum + 1);
    checkRepresentPrjStatus();
  } else {
    // 出提示语
    scmpublictoast("最多选择5个代表项目", 1500);
  }
}

function delRepresentPrj(id, title, author) {
  var title = $("#selected_prj_title_" + id).html();
  var author = $("#selected_prj_author_" + id).html();
  var selectDiv = "#checkDiv_" + id;
  $(selectDiv).children("i").html("add");
  $(selectDiv).children("i").css("color", "");
  $(selectDiv).attr("onclick", "addToRepresentPrj('" + id + "')");
  $("#representPrj_" + id).remove();
  var prjIds = $("#representPrjIds").val();
  $("#representPrjIds").val(prjIds.replace(id + ",", ""));
  var prjSum = parseInt($("#representPrjSum").val());
  $("#representPrjSum").val(prjSum - 1);
  checkRepresentPrjStatus();
}
function delRepresentPrjObj(obj) {
  var id = $(obj).closest(".main-list__item").attr("prj_id");
  var title = $("#selected_prj_title_" + id).html();
  var author = $("#selected_prj_author_" + id).html();
  var selectDiv = "#checkDiv_" + id;
  $(selectDiv).children("i").html("add");
  $(selectDiv).children("i").css("color", "");
  $(selectDiv).attr("onclick", "addToRepresentPrj('" + id + "')");
  $("#representPrj_" + id).remove();
  var prjIds = $("#representPrjIds").val();
  $("#representPrjIds").val(prjIds.replace(id + ",", ""));
  var prjSum = parseInt($("#representPrjSum").val());
  $("#representPrjSum").val(prjSum - 1);
  checkRepresentPrjStatus();
}

function downMoveRepresentPrj(obj) {
  var $curItem = $(obj).closest(".main-list__item");
  var $clone = cloneRepPrj($curItem);
  var seq_prj = $curItem.attr("seq_prj");
  seq_prj = numObj = new Number(seq_prj)
  seq_prj = seq_prj + 1;
  var $moverItem = $("#addedPrjList").find("[seq_prj='" + seq_prj + "']");
  $moverItem.after($clone);
  $moverItem.attr("seq_prj", seq_prj - 1);
  $clone.attr("seq_prj", seq_prj);
  $curItem.remove();
  checkRepresentPrjStatus();
}
function cloneRepPrj($curItem) {
  var title = $curItem.find(".pub-idx__main_title").html();
  var author = $curItem.find(".pub-idx__main_author").html();
  var briefDesc = $curItem.find(".pub-idx__main_src").html();
  var prj_id = $curItem.attr("prj_id");
  var $addNode = $("#addedPrjItem").clone(true);
  $addNode.attr("prj_id", prj_id);
  $addNode.find(".pub-idx__main_title").html(title);
  $addNode.find(".pub-idx__main_author").html(author);
  $addNode.find(".pub-idx__main_src").html(briefDesc);
  $addNode.css("display", "");
  $addNode.attr("id", "representPrj_" + prj_id);
  return $addNode;
}
function upMoveRepresentPrj(obj) {
  var $curItem = $(obj).closest(".main-list__item");
  var $clone = cloneRepPrj($curItem);
  var seq_prj = $curItem.attr("seq_prj");
  seq_prj = numObj = new Number(seq_prj)
  seq_prj = seq_prj - 1;
  var $moverItem = $("#addedPrjList").find("[seq_prj='" + seq_prj + "']");
  $moverItem.before($clone);
  $moverItem.attr("seq_prj", seq_prj + 1);
  $clone.attr("seq_prj", seq_prj);
  $curItem.remove();
  checkRepresentPrjStatus();
}
function checkRepresentPrjStatus() {
  var len = $("#addedPrjList").find(".main-list__item").length - 1;
  if (len < 1) {
    return;
  } else if (len == 1) {
    $("#addedPrjList").find(".main-list__item").each(function(i) {
      if (i == 0) {
        $(this).find(".main-list__item_actions .arrow_up").remove();
        $(this).find(".main-list__item_actions .arrow_down").remove();
        $(this).attr("seq_prj", i + 1);
      }
    });
  } else {
    $("#addedPrjList").find(".main-list__item").each(
        function(i) {
          if (i != len) {
            $(this).attr("seq_prj", i + 1);
          }
          if (i == 0) {
            $(this).find(".main-list__item_actions .arrow_up").remove();
            $(this).find(".main-list__item_actions .arrow_down").remove();
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_down arrow_down" ' + 'onclick="downMoveRepresentPrj(this);"></i>')
          } else if (i == len - 1) {
            $(this).find(".main-list__item_actions .arrow_down").remove();
            $(this).find(".main-list__item_actions .arrow_up").remove();
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_up arrow_up" ' + 'onclick="upMoveRepresentPrj(this);"></i>')
          } else {
            $(this).find(".main-list__item_actions .arrow_down").remove();
            $(this).find(".main-list__item_actions .arrow_up").remove();
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_up arrow_up" ' + 'onclick="upMoveRepresentPrj(this);"></i>')
            $(this).find(".main-list__item_actions .arrow_close").before(
                '<i class="selected-func_down arrow_down" ' + 'onclick="downMoveRepresentPrj(this);"></i>')
          }
        });
  }

}

function savePsnRepresentPrj(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $(obj).attr("disabled", "disabled");
  var representPrjIds = "";
  $("#addedPrjList").find(".main-list__item").each(function() {
    var prj_id = $(this).attr("prj_id");
    if (prj_id != undefined && prj_id != "") {
      representPrjIds = representPrjIds + prj_id + ",";
    }
  })
  $.ajax({
    url : "/psnweb/representprj/ajaxsave",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      'representPrjIds' : representPrjIds
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $(obj).removeAttr("disabled");
        hideDialog("representPrjBox");
        showPsnRepresentPrj();
        $(".representPrjDiv").remove();
      });

    },
    error : function() {
    }
  });
}

function getPsnOpenPrj() {
  window.Mainlist({
    name : 'psnOpenPrjList',
    listurl : '/psnweb/representprj/ajaxopen',
    listdata : {
      "des3PsnId" : $("#des3PsnId").val(),
      "cnfId" : $("#cnfId").val()
    },
    listcallback : function(xhr) {
      resetPrjCheckStatus();
    },
    method : 'scroll'
  });
}

function resetPrjCheckStatus() {
  var prjIds = $("#representPrjIds").val();
  $(".prjCheckStatus").each(function() {
    var _this = $(this);
    var prjId = _this.attr("checkPrjId");
    if (prjIds.indexOf(prjId) == -1) {
      _this.html("add");
      _this.css("color", "");
      var title = $("#title_" + prjId).html();
      var authorName = $("#authorNames_" + prjId).html();
      $("#checkDiv_" + prjId).attr("onclick", "addToRepresentPrj('" + prjId + "')");
      _this.removeClass("prjCheckStatus");
    } else {
      _this.html("check");
      _this.css("color", "forestgreen");
      var title = $("#title_" + prjId).html();
      var authorName = $("#authorNames_" + prjId).html();
      $("#checkDiv_" + prjId).attr("onclick", "addToRepresentPrj('" + prjId + "')");
      _this.removeClass("prjCheckStatus");
    }
  });
}

function viewPsnPrjDetails(des3Id) {
  var params = "des3PrjId=" + encodeURIComponent(des3Id);
  var url = "/prjweb/project/detailsshow?" + params;
  // newwindow = window.open("about:blank");
  // newwindow.location.href = url;
  window.open(url, "_blank");
  // newwindow.focus();
}
// 代表性项目-------------------end

// 工作经历--------------------begin
function showPsnWorkHistory() {
  $.ajax({
    url : "/psnweb/workhistory/ajaxshow",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#workHistoryItems").remove();
        $(data).insertAfter($("#workHistoryModuleDiv"));
        hideOperationBtn();
      });

    },
    error : function() {
    }
  });
}

/**
 * 工作经历至今的点击事件
 */
function clickUpToNow(obj) {
  var upToNow = $(obj).prop("checked")
  if (upToNow) {
    var $input = $(obj).closest(".form__sxn_row").find(".dev_end_year  input");
    $input.val("")
    $input.attr("date-year", "");
    $input.attr("date-month", "");
    $input.attr("date-date", "");
    $(obj).closest(".form__sxn_row").find(".dev_end_year").removeClass("input_disabled");

    $("#addWorkToYearDiv,#addEduToYearDiv").removeClass("input_not-null input_invalid");
    $("#addWorkToYearHelp,#addEduToYearHelp").attr("invalid-message", "");
    $("#workToYearDiv,#eduToYearDiv").removeClass("input_not-null input_invalid");
    $("#workToYearHelp,#eduToYearHelp").attr("invalid-message", "");
    // SCM-16168 修改endTime 为可点击
    $input.parent().bind("click", function() {
      obj.checked = false;
    });

  } else {
    var newEndYear = $("#newEndTime").attr("date-year");
    var endYear = $("#endTime").attr("date-year");
    if (isNaN(newEndYear) || newEndYear == "") {
      $("#addWorkToYearDiv,#addEduToYearDiv").removeClass("input_not-null").addClass("input_invalid");
      $("#addWorkToYearHelp,#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
    }
    if (isNaN(endYear) || endYear == "") {
      $("#workToYearDiv,#eduToYearDiv").removeClass("input_not-null").addClass("input_invalid");
      $("#workToYearHelp,#eduToYearHelp").attr("invalid-message", homepage.trueEndTime);
    }
    $(obj).closest(".form__sxn_row").find(".dev_end_year").removeClass("input_disabled");
  }
}

function saveWorkHistory(type, obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  if(document.getElementsByClassName("ac__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
  }
  if(document.getElementsByClassName("datepicker__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("datepicker__box")[0]);
  }
  var result = true;
  if ("add" == type && $("#newInsName").val().trim() == "") {
    $("#newInsNameDiv").addClass("input_invalid");
    $("#newInsNameHelper").attr("invalid-message", homepage.inputInsName);
    result = false;
  }
  if ("edit" == type && $("#insName").val().trim() == "") {
    $("#insNameDiv").addClass("input_invalid");
    $("#insNameHelper").attr("invalid-message", homepage.inputInsName);
    result = false;
  }
  var startYear = parseInt($("#newStartTime").attr("date-year"));
  var startMonth = parseInt($("#newStartTime").attr("date-month"));
  var toYear = parseInt($("#newEndTime").attr("date-year"));
  var toMonth = parseInt($("#newEndTime").attr("date-month"));
  var editStartYear = parseInt($("#startTime").attr("date-year"));
  var editStartMonth = parseInt($("#startTime").attr("date-month"));
  var editToYear = parseInt($("#endTime").attr("date-year"));
  var editToMonth = parseInt($("#endTime").attr("date-month"));

  var addIsActive = 0; // 至今
  var editIsActive = 0; // 至今
  var addWorkupToNow = $("#addWorkUpToNow").prop("checked");
  var editWorkupToNow = $("#editWorkUpToNow").prop("checked");
  if (addWorkupToNow) {
    addIsActive = 1;
  }
  if (editWorkupToNow) {
    editIsActive = 1;
  }
  if ("add" == type) {
    if (isNaN(toYear) && addIsActive == 0) {
      $("#addWorkToYearDiv").addClass("input_invalid");
      $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
    if (isNaN(startYear)) {
      $("#addWorkFromYearDiv").addClass("input_invalid");
      $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
      result = false;
    } else {
      // 没勾选至今或结束时间小于开始时间
      if (!((!isNaN(toYear) && compareEduTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)) {
        $("#addWorkToYearDiv").addClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      } else {
        $("#addWorkToYearDiv").removeClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", "");
        $("#addWorkFromYearDiv").removeClass("input_invalid");
        $("#addWorkFromYearHelp").attr("invalid-message", "");
      }
      // 校验开始年份是否大于至今
      if (addIsActive == 1 && !compareEduTime(startYear, startMonth, $("#nowYear").val(), $("#nowMonth").val())) {
        $("#addWorkFromYearDiv").addClass("input_invalid");
        $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
        result = false;
      }
      // 校验开始年份是否大于结束年份
      if (addIsActive == 0 && !compareEduTime(startYear, startMonth, toYear, toMonth)) {
        $("#addWorkToYearDiv").addClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
      // 校验结束年份是否大于10年
      if (addIsActive == 0
          && !compareEduTime(toYear, toMonth, parseInt($("#nowYear").val()) + 10, $("#nowMonth").val())) {
        $("#addWorkToYearDiv").addClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
    }
  }
  if ("edit" == type) {
    if (isNaN(editToYear) && editIsActive == 0) {
      $("#workToYearDiv").addClass("input_invalid");
      $("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
    if (isNaN(editStartYear)) {
      $("#workFromYearDiv").addClass("input_invalid");
      $("#workFromYearHelp").attr("invalid-message", homepage.trueStartTime);
      result = false;
    } else {
      if (!((!isNaN(editToYear) && compareEduTime(editStartYear, editStartMonth, editToYear, editToMonth)) || editIsActive == 1)) {
        $("#workToYearDiv").addClass("input_invalid");
        $("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      } else {
        $("#workToYearDiv").removeClass("input_invalid");
        $("#workToYearHelp").attr("invalid-message", "");
        $("#workFromYearDiv").removeClass("input_invalid");
        $("#workFromYearHelp").attr("invalid-message", "");
      }
      // 校验开始年份是否大于至今
      if (editIsActive == 1
          && !compareEduTime(editStartYear, editStartMonth, $("#nowYear").val(), $("#nowMonth").val())) {
        $("#workFromYearDiv").addClass("input_invalid");
        $("#workFromYearHelp").attr("invalid-message", homepage.trueStartTime);
        result = false;
      }
      // 校验开始年份是否大于结束年份
      if (editIsActive == 0 && !compareEduTime(editStartYear, editStartMonth, editToYear, editToMonth)) {
        $("#workToYearDiv").addClass("input_invalid");
        $("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
      // 校验结束年份是否大于10年
      if (editIsActive == 0
          && !compareEduTime(editToYear, editToMonth, parseInt($("#nowYear").val()) + 10, $("#nowMonth").val())) {
        $("#workToYearDiv").addClass("input_invalid");
        $("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
    }
  }
  if (!result) {
    return false;
  }
  var addData, editData;
  if (type == "edit") {
    editData = {
      'des3Id' : $("#des3WorkId").val(),
      'insId' : insId,
      'insName' : $("#insName").val().trim(),
      'department' : $("#department").val().trim(),
      'position' : $("#position").val().trim(),
      'anyUser' : 7,
      'fromYear' : $("#startTime").attr("date-year"),
      'fromMonth' : $("#startTime").attr("date-month") == "null" ? "" : $("#startTime").attr("date-month"),
      'toYear' : $("#endTime").attr("date-year") == "null" ? "" : $("#endTime").attr("date-year"),
      'toMonth' : $("#endTime").attr("date-month") == "null" ? "" : $("#endTime").attr("date-month"),
      'description' : $("#description").val(),
      'isActive' : editIsActive
    };
  } else {
    addData = {
      'insId' : $("#newInsName").attr("code"),
      'insName' : $("#newInsName").val().trim(),
      'department' : $("#newDepartment").val().trim(),
      'position' : $("#newPosition").val().trim(),
      'anyUser' : 7,
      'fromYear' : $("#newStartTime").attr("date-year"),
      'fromMonth' : $("#newStartTime").attr("date-month") == "null" ? "" : $("#newStartTime").attr("date-month"),
      'toYear' : $("#newEndTime").attr("date-year") == "null" ? "" : $("#newEndTime").attr("date-year"),
      'toMonth' : $("#newEndTime").attr("date-month") == "null" ? "" : $("#newEndTime").attr("date-month"),
      'description' : $("#newDescription").val(),
      'isActive' : addIsActive
    };
  }
  var insId = "";
  var code = $('#insName').attr("code");
  /*
   * if (code != null && code != "" && code != undefined) { insId = code; } else { insId =
   * $('#insId').val(); }
   */
  if (code == null || code == undefined) {
    insId = "";
  }

  $.ajax({
    url : "/psnweb/workhistory/ajaxsave",
    type : "post",
    dataType : "json",
    data : type == "edit" ? editData : addData,
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          // scmpublictoast(homepage.operateSuccess,1000);
          // 关掉弹窗
          if ("edit" == type) {
            hideEditWorkBox();
          } else if ("add" == type) {
            hideAddWorkBox();
          }
          // 更新对应的工作经历信息
          showPsnWorkHistory();
          var workId = $("#workId").val();
          var selectId = "#" + workId + "_isPrimary";
          var isPrimary = $(selectId).val();
          if (("edit" == type && isPrimary == "1") || "add" == type) {
            reloadPsnBaseInfo();
          }
        } else {
          scmpublictoast(homepage.systemBusy, 1000);
        }
      });

    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
      hideEditWorkBox();
      hideAddWorkBox();
    }
  });

}

function editWorkHistory(workId) {
  $.ajax({
    url : "/psnweb/workhistory/ajaxedit",
    type : "post",
    dateType : "html",
    data : {
      "encodeWrokId" : workId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#editPsnWorkBox").html(data);
        addFormElementsEvents($("#editPsnWorkBox")[0]);
        showDialog("editPsnWorkBox");
        // 绑定校验
        $("#insName").bind('blur', checkWorkNameNull);
        $("#startTime").bind('blur', checkWorkDateNull);
        $("#endTime").bind('blur', checkWorkEndDateNull);
        // 重置结束日期选择事件
        clickUpToNow($("#editWorkUpToNow")[0]);
      });

    },
    error : function() {
      hideDialog("editPsnWorkBox");
    }
  });
}

function addWorkHistory() {
  // 先清空输入框中的内容
  $("#newInsId").val("");
  $("#newInsName").val("");
  $("#newDepartment").val("");
  $("#newPosition").val("");
  $("#newStartTime").val("");
  $("#newEndTime").val("");
  $("#newStartTime").attr("date-year", "");
  $("#newStartTime").attr("date-month", "");
  $("#newStartTime").attr("date-date", "");
  $("#newEndTime").attr("date-year", "");
  $("#newEndTime").attr("date-month", "");
  $("#newEndTime").attr("date-date", "");
  $("#newDescription").val("");
  // 清空提示语
  $("#newInsNameDiv").removeClass("input_invalid");
  $("#newInsNameHelper").attr("invalid-message", "");
  $("#addWorkFromYearDiv").removeClass("input_invalid");
  $("#addWorkFromYearHelp").attr("invalid-message", "");
  $("#addWorkToYearDiv").removeClass("input_invalid");
  $("#addWorkToYearHelp").attr("invalid-message", "");
  addFormElementsEvents($("#addPsnWorkBox")[0]);
  // 显示弹出框
  showDialog("addPsnWorkBox");
  // 为必填项添加校验
  $("#newInsName").bind('blur', checkWorkNameNull);
  $("#newStartTime").bind('blur', checkWorkDateNull);
  $("#newEndTime").bind('blur', checkWorkEndDateNull);
}
function checkWorkNameNull() {
  // add
  if (typeof ($("#newInsName").val()) != "undefined") {
    if ($("#newInsName").val().trim() == "") {
      $("#newInsNameDiv").addClass("input_invalid");
      $("#newInsNameHelper").attr("invalid-message", homepage.inputInsName);
    } else {
      $("#newInsNameDiv").removeClass("input_invalid");
      $("#newInsNameHelper").attr("invalid-message", "");
    }
  }
  // edit
  if (typeof ($("#insName").val()) != "undefined") {
    if ($("#insName").val().trim() == "") {
      $("#insNameDiv").addClass("input_invalid");
      $("#insNameHelper").attr("invalid-message", homepage.inputInsName);
    } else {
      $("#insNameDiv").removeClass("input_invalid");
      $("#insNameHelper").attr("invalid-message", "");
    }
  }

}
function checkWorkDateNull() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  // add
  // 开始时间
  var startYear = $("#newStartTime").attr("date-year");
  var startMonth = $("#newStartTime").attr("date-month");
  // 结束时间
  var endYear = $("#newEndTime").attr("date-year");
  var endMonth = $("#newEndTime").attr("date-month");
  var checked = $("#addWorkUpToNow").prop("checked");
  // 开始时间校验错误的情况：
  // 1.开始年份为空
  // 2.开始时间大于当前时间的后10年(SCM-23891)
  // 3.结束时间不为空时开始时间大于结束时间
  // 4.结束时间选为至今时开始时间大于当前服务器的时间
  if (checkIsBlank(startYear) || !compareEduTime(startYear, startMonth, parseInt(nowYear) + 10, nowMonth)
      || (!checkIsBlank(endYear) && !compareEduTime(startYear, startMonth, endYear, endMonth))
      || (checked && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#addWorkFromYearDiv").addClass("input_invalid");
    $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addWorkFromYearDiv").removeClass("input_invalid");
    $("#addWorkFromYearHelp").attr("invalid-message", "");
  }
  // edit
  startYear = $("#startTime").attr("date-year");
  startMonth = $("#startTime").attr("date-month");
  endYear = $("#endTime").attr("date-year");
  endMonth = $("#endTime").attr("date-month")
  checked = $("#editWorkUpToNow").prop("checked");
  if (checkIsBlank(startYear) || !compareEduTime(startYear, startMonth, parseInt(nowYear) + 10, nowMonth)
      || (!checkIsBlank(endYear) && endYear != "" && !compareEduTime(startYear, startMonth, endYear, endMonth))
      || (checked && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#workFromYearDiv").addClass("input_invalid");
    $("#workFromYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#workFromYearDiv").removeClass("input_invalid");
    $("#workFromYearHelp").attr("invalid-message", "");
  }
}

function checkWorkEndDateNull() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  // add
  // 结束时间
  var endYear = $("#newEndTime").attr("date-year");
  var endMonth = $("#newEndTime").attr("date-month");
  // 开始时间
  var startYear = $("#newStartTime").attr("date-year");
  var startMonth = $("#newStartTime").attr("date-month");
  var checked = $("#addWorkUpToNow").prop("checked");
  // 结束时间校验错误的情况：
  // 1.结束年份为空且没勾选至今
  // 2.结束年份有值且大于当前时间的后10年(SCM-23891)
  // 3.结束年份有值且小于开始年份
  $("#addWorkFromYearDiv").removeClass("input_invalid");
  $("#addWorkFromYearHelp").attr("invalid-message", "");
  $("#addWorkToYearDiv").removeClass("input_invalid");
  $("#addWorkToYearHelp").attr("invalid-message", "");
  if ((checkIsBlank(endYear) && !checked)
      || (!checkIsBlank(endYear) && !checked && (!compareEduTime(endYear, endMonth, parseInt(nowYear) + 10, nowMonth) || (!isNaN(startYear) && !compareEduTime(
          startYear, startMonth, endYear, endMonth))))) {
    $("#addWorkToYearDiv").addClass("input_invalid");
    $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
    // 4.结束年份为至今且小于开始年份
  } else if (checkIsBlank(startYear)
      || (checked && !isNaN(startYear) && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#addWorkFromYearDiv").addClass("input_invalid");
    $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else {
    $("#addWorkToYearDiv").removeClass("input_invalid");
    $("#addWorkToYearHelp").attr("invalid-message", "");
  }
  // edit
  startYear = $("#startTime").attr("date-year");
  startMonth = $("#startTime").attr("date-month");
  endYear = $("#endTime").attr("date-year");
  endMonth = $("#endTime").attr("date-month");
  checked = $("#editWorkUpToNow").prop("checked");
  $("#workFromYearDiv").removeClass("input_invalid");
  $("#workFromYearHelp").attr("invalid-message", "");
  $("#workToYearDiv").removeClass("input_invalid");
  $("#workToYearHelp").attr("invalid-message", "");
  if ((checkIsBlank(endYear) && !checked)
      || (!checkIsBlank(endYear) && !checked && (!compareEduTime(endYear, endMonth, parseInt(nowYear) + 10, nowMonth) || (!isNaN(startYear) && !compareEduTime(
          startYear, startMonth, endYear, endMonth))))) {
    $("#workToYearDiv").addClass("input_invalid");
    $("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else if (isNaN(startYear)
      || (checked && !checkIsBlank(startYear) && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#workFromYearDiv").addClass("input_invalid");
    $("#workFromYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else {
    $("#workToYearDiv").removeClass("input_invalid");
    $("#workToYearHelp").attr("invalid-message", "");
  }
}

function hideAddWorkBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("addPsnWorkBox");
  if(document.getElementsByClassName("ac__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
  }
  if(document.getElementsByClassName("datepicker__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("datepicker__box")[0]);
  }
}

function hideEditWorkBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("editPsnWorkBox");
  if(document.getElementsByClassName("ac__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
  }
  if(document.getElementsByClassName("datepicker__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("datepicker__box")[0])
  }
}

function delWorkHistory(workId, obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  if(document.getElementsByClassName("ac__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
  }
  if(document.getElementsByClassName("datepicker__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("datepicker__box")[0])
  }
  $.ajax({
    url : "/psnweb/workhistory/ajaxdel",
    type : "post",
    dateType : "json",
    data : {
      "des3Id" : workId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          // parent.window.location.href = "/psnweb/homepage/show";
          hideDialog("editPsnWorkBox");
          var encodeWorkIdForRemove = $("input[value='" + workId + "']").prev().val();
          $("#" + encodeWorkIdForRemove + "_isPrimary").next("div").remove();
        } else {
          scmpublictoast(homepage.systemBusy, 1000);
        }
      });

    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
    }
  });
}

// 工作经历--------------------end

// 教育经历--------------------begin
function showPsnEduHistory() {
  $.ajax({
    url : "/psnweb/eduhistory/ajaxshow",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#eduHistoryItems").remove();
        $(data).insertAfter($("#eduHistoryModuleDiv"));
        hideOperationBtn();
      });

    },
    error : function() {
    }
  });
}

function compareEduTime(startYear, startMonth, toYear, toMonth) {
  if (startYear > toYear) {
    return false;
  } else if (startYear == toYear && startMonth > toMonth) {
    return false;
  } else {
    return true;
  }

};

function saveEduHistory(type, obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var result = true;
  if ("add" == type && $("#eduInsName").val().trim() == "") {
    $("#eduInsNameDiv").addClass("input_invalid");
    $("#eduInsNameHelper").attr("invalid-message", homepage.inputInsName);
    result = false;
  }
  if ("edit" == type && $("#editEduInsName").val().trim() == "") {
    $("#editEduInsNameDiv").addClass("input_invalid");
    $("#editEduInsNameHelper").attr("invalid-message", homepage.inputInsName);
    result = false;
  }
  var startYear = parseInt($("#eduFromTime").attr("date-year"));
  var startMonth = parseInt($("#eduFromTime").attr("date-month"));
  var toYear = parseInt($("#eduToTime").attr("date-year"));
  var toMonth = parseInt($("#eduToTime").attr("date-month"));
  var editStartYear = parseInt($("#editEduFromTime").attr("date-year"));
  var editStartMonth = parseInt($("#editEduFromTime").attr("date-month"));
  var editToYear = parseInt($("#editEduToTime").attr("date-year"));
  var editToMonth = parseInt($("#editEduToTime").attr("date-month"));

  var addIsActive = 0;
  var editIsActive = 0;
  var addEduUpToNow = $("#addEduUpToNow").prop("checked");
  var editEduUpToNow = $("#editEduUpToNow").prop("checked");
  if (addEduUpToNow) {
    addIsActive = 1;
  }
  if (editEduUpToNow) {
    editIsActive = 1;
  }

  if ("add" == type) {
    if (checkIsBlank(toYear) && addIsActive == 0) {
      $("#addEduToYearDiv").addClass("input_invalid");
      $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
    if (checkIsBlank(startYear)) {
      $("#addEduYearDiv").addClass("input_invalid");
      $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
      result = false;
    } else {
      if (!((!checkIsBlank(toYear) && compareEduTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)) {
        $("#addEduToYearDiv").addClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      } else {
        $("#addEduToYearDiv").removeClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", "");
        $("#addEduYearDiv").removeClass("input_invalid");
        $("#addEduYearHelp").attr("invalid-message", "");
      }
      // 校验开始年份是否大于至今
      if (addIsActive == 1 && !compareEduTime(startYear, startMonth, $("#nowYear").val(), $("#nowMonth").val())) {
        $("#addEduYearDiv").addClass("input_invalid");
        $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
        result = false;
      }
      // 校验开始年份是否大于结束年份
      if (addIsActive == 0 && !compareEduTime(startYear, startMonth, toYear, toMonth)) {
        $("#addEduToYearDiv").addClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
      // 校验结束年份是否大于10年
      if (addIsActive == 0
          && !compareEduTime(toYear, toMonth, parseInt($("#nowYear").val()) + 10, $("#nowMonth").val())) {
        $("#addEduToYearDiv").addClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
    }
  }
  if ("edit" == type) {
    if (checkIsBlank(editToYear) && editIsActive == 0) {
      $("#editEduToYearDiv").addClass("input_invalid");
      $("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
    if (checkIsBlank(editStartYear)) {
      $("#editEduYearDiv").addClass("input_invalid");
      $("#editEduYearHelp").attr("invalid-message", homepage.trueStartTime);
      result = false;

    } else {
      if (!((!checkIsBlank(editToYear) && compareEduTime(editStartYear, editStartMonth, editToYear, editToMonth)) || editIsActive == 1)) {
        $("#editEduToYearDiv").addClass("input_invalid");
        $("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      } else {
        $("#editEduToYearDiv").removeClass("input_invalid");
        $("#editEduToYearHelp").attr("invalid-message", "");
        $("#editEduYearDiv").removeClass("input_invalid");
        $("#editEduYearHelp").attr("invalid-message", "");
      }
      if (editIsActive == 1
          && !compareEduTime(editStartYear, editStartMonth, $("#nowYear").val(), $("#nowMonth").val())) {
        $("#editEduYearDiv").addClass("input_invalid");
        $("#editEduYearHelp").attr("invalid-message", homepage.trueStartTime);
        result = false;
      }
      if (addIsActive == 0 && !compareEduTime(editStartYear, editStartMonth, editToYear, editToMonth)) {
        $("#editEduToYearDiv").addClass("input_invalid");
        $("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
      if (addIsActive == 0
          && !compareEduTime(editToYear, editToMonth, parseInt($("#nowYear").val()) + 10, $("#nowMonth").val())) {
        $("#editEduToYearDiv").addClass("input_invalid");
        $("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      }
    }
  }
  if (!result) {
    return false;
  }
  var addData, editData;
  if ("edit" == type) {
    editData = {
      'des3Id' : $("#des3EduId").val(),
      'insId' : insId,
      'insName' : $("#editEduInsName").val().trim(),
      'study' : $("#editEduStudy").val().trim(),
      'degreeName' : $("#editEduDegree").val().trim(),
      'anyUser' : 7,
      'fromYear' : $("#editEduFromTime").attr("date-year"),
      'fromMonth' : $("#editEduFromTime").attr("date-month") == "null" ? "" : $("#editEduFromTime").attr("date-month"),
      'toYear' : $("#editEduToTime").attr("date-year") == "null" ? "" : $("#editEduToTime").attr("date-year"),
      'toMonth' : $("#editEduToTime").attr("date-month") == "null" ? "" : $("#editEduToTime").attr("date-month"),
      'description' : $("#editEduDescription").val(),
      'isActive' : editIsActive
    };
  } else {
    addData = {
      'insId' : $("#eduInsName").attr("code"),
      'insName' : $("#eduInsName").val().trim(),
      'study' : $("#eduDepartment").val().trim(),
      'degreeName' : $("#eduPosition").val().trim(),
      'anyUser' : 7,
      'fromYear' : $("#eduFromTime").attr("date-year"),
      'fromMonth' : $("#eduFromTime").attr("date-month") == "null" ? "" : $("#eduFromTime").attr("date-month"),
      'toYear' : $("#eduToTime").attr("date-year") == "null" ? "" : $("#eduToTime").attr("date-year"),
      'toMonth' : $("#eduToTime").attr("date-month") == "null" ? "" : $("#eduToTime").attr("date-month"),
      'description' : $("#eduDescription").val(),
      'isActive' : addIsActive
    };
  }
  var insId = "";
  var code = $('#editEduInsName').attr("code");
  if (code != null && code != "" && code != undefined) {
    insId = code;
  } else {
    insId = $("#editEduInsId").val();
  }

  $.ajax({
    url : "/psnweb/eduhistory/ajaxsave",
    type : "post",
    dataType : "json",
    data : type == "edit" ? editData : addData,
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          // 关掉弹窗
          if ("edit" == type) {
            hideEditEduBox();
          } else if ("add" == type) {
            hideAddEduBox();
          }
          // 更新对应的工作经历信息
          showPsnEduHistory();
          // scmpublictoast(homepage.operateSuccess,1000);
        } else {
          scmpublictoast(homepage.systemBusy, 1000);
        }
      });

    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
      hideEditEduBox();
      hideAddEduBox();
    }
  });

}

function editEduHistory(eduId) {
  $.ajax({
    url : "/psnweb/eduhistory/ajaxedit",
    type : "post",
    dateType : "html",
    data : {
      "encodeEduId" : eduId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#editPsnEduBox").html(data);
        addFormElementsEvents($("#editPsnEduBox")[0]);
        showDialog("editPsnEduBox");
        // 绑定校验
        $("#editEduInsName").bind('blur', checkEduNameNull);
        $("#editEduFromTime").bind('blur', checkEduDateNull);
        $("#editEduToTime").bind('blur', checkEduEndDateNull);
        // 重置结束日期选择事件
        clickUpToNow($("#editEduUpToNow")[0]);
      });

    },
    error : function() {
      hideDialog("editPsnEduBox");
    }
  });
}

function addEduHistory() {
  // 先清空输入框中的内容
  $("#eduInsId").val("");
  $("#eduInsName").val("");
  $("#eduDepartment").val("");
  $("#eduPosition").val("");
  $("#eduFromTime").val("");
  $("#eduToTime").val("");
  $("#eduFromTime").attr("date-year", "");
  $("#eduFromTime").attr("date-month", "");
  $("#eduFromTime").attr("date-date", "");
  $("#eduToTime").attr("date-year", "");
  $("#eduToTime").attr("date-month", "");
  $("#eduToTime").attr("date-date", "");
  $("#eduDescription").val("");
  // 情空提示语
  $("#eduInsNameDiv").removeClass("input_invalid");
  $("#eduInsNameHelper").attr("invalid-message", "");
  $("#addEduYearDiv").removeClass("input_invalid");
  $("#addEduYearHelp").attr("invalid-message", "");
  $("#addEduToYearDiv").removeClass("input_invalid");
  $("#addEduToYearHelp").attr("invalid-message", "");
  addFormElementsEvents($("#addPsnEduBox")[0]);
  // 显示弹出框
  showDialog("addPsnEduBox");
  // 增加校验
  $("#eduInsName").bind('blur', checkEduNameNull);
  $("#eduFromTime").bind('blur', checkEduDateNull);
  $("#eduToTime").bind('blur', checkEduEndDateNull);
}
function checkEduNameNull() {
  // add
  if ($("#eduInsName").val().trim() == "") {
    $("#eduInsNameDiv").addClass("input_invalid");
    $("#eduInsNameHelper").attr("invalid-message", homepage.inputInsName);
  } else {
    $("#eduInsNameDiv").removeClass("input_invalid");
    $("#eduInsNameHelper").attr("invalid-message", "");
  }
  // edit
  if ($("#editEduInsName").val() == "") {
    $("#editEduInsNameDiv").addClass("input_invalid");
    $("#editEduInsNameHelper").attr("invalid-message", homepage.inputInsName);
  } else {
    $("#editEduInsNameDiv").removeClass("input_invalid");
    $("#editEduInsNameHelper").attr("invalid-message", "");
  }

}
function checkEduDateNull() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  // add
  // 开始时间
  var startYear = $("#eduFromTime").attr("date-year");
  var startMonth = $("#eduFromTime").attr("date-month");
  // 结束时间
  var endYear = $("#eduToTime").attr("date-year");
  var endMonth = $("#eduToTime").attr("date-month");
  var checked = $("#addEduUpToNow").prop("checked");
  if (checkIsBlank(startYear) || !compareEduTime(startYear, startMonth, parseInt(nowYear) + 10, nowMonth)
      || (!checkIsBlank(endYear) && !compareEduTime(startYear, startMonth, endYear, endMonth))
      || (checked && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#addEduYearDiv").addClass("input_invalid");
    $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addEduYearDiv").removeClass("input_invalid");
    $("#addEduYearHelp").attr("invalid-message", "");
  }

  // edit
  startYear = $("#editEduFromTime").attr("date-year");
  startMonth = $("#editEduFromTime").attr("date-month");
  endYear = $("#editEduToTime").attr("date-year");
  endMonth = $("#editEduToTime").attr("date-month");
  checked = $("#editEduUpToNow").prop("checked");
  if (checkIsBlank(startYear) || !compareEduTime(startYear, startMonth, parseInt(nowYear) + 10, nowMonth)
      || (!checkIsBlank(endYear) && !compareEduTime(startYear, startMonth, endYear, endMonth))
      || (checked && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#editEduYearDiv").addClass("input_invalid");
    $("#editEduYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#editEduYearDiv").removeClass("input_invalid");
    $("#editEduYearHelp").attr("invalid-message", "");
  }
}
function checkEduEndDateNull() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  // add
  // 结束时间
  var endYear = $("#eduToTime").attr("date-year");
  var endMonth = $("#eduToTime").attr("date-month");
  // 开始时间
  var startYear = $("#eduFromTime").attr("date-year");
  var startMonth = $("#eduFromTime").attr("date-month");
  var checked = $("#addEduUpToNow").prop("checked");
  $("#addEduYearDiv").removeClass("input_invalid");
  $("#addEduYearHelp").attr("invalid-message", "");
  $("#addEduToYearDiv").removeClass("input_invalid");
  $("#addEduToYearHelp").attr("invalid-message", "");
  if ((checkIsBlank(endYear) && !checked)
      || (!checkIsBlank(endYear) && !checked && (!compareEduTime(endYear, endMonth, parseInt(nowYear) + 10, nowMonth) || (!isNaN(startYear) && !compareEduTime(
          startYear, startMonth, endYear, endMonth))))) {
    $("#addEduToYearDiv").addClass("input_invalid");
    $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else if (checkIsBlank(startYear)
      || (checked && !isNaN(startYear) && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#addEduYearDiv").addClass("input_invalid");
    $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addEduToYearDiv").removeClass("input_invalid");
    $("#addEduToYearHelp").attr("invalid-message", "");
  }

  // edit
  startYear = $("#editEduFromTime").attr("date-year");
  startMonth = $("#editEduFromTime").attr("date-month");
  endYear = $("#editEduToTime").attr("date-year");
  endMonth = $("#editEduToTime").attr("date-month");
  checked = $("#editEduUpToNow").prop("checked");
  $("#editEduToYearDiv").removeClass("input_invalid");
  $("#editEduToYearHelp").attr("invalid-message", "");
  $("#editEduFromYearDiv").removeClass("input_invalid");
  $("#editEduFromYearHelp").attr("invalid-message", "");
  if ((checkIsBlank(endYear) && !checked)
      || (!checkIsBlank(endYear) && !checked && (!compareEduTime(endYear, endMonth, parseInt(nowYear) + 10, nowMonth) || (!isNaN(startYear) && !compareEduTime(
          startYear, startMonth, endYear, endMonth))))) {
    $("#editEduToYearDiv").addClass("input_invalid");
    $("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else if (checkIsBlank(startYear)
      || (checked && !isNaN(startYear) && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#editEduFromYearDiv").addClass("input_invalid");
    $("#editEduFromYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#editEduToYearDiv").removeClass("input_invalid");
    $("#editEduToYearHelp").attr("invalid-message", "");
  }
}
function hideAddEduBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("addPsnEduBox");
}

function hideEditEduBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  hideDialog("editPsnEduBox");
}

function delEduHistory(eduId, obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $.ajax({
    url : "/psnweb/eduhistory/ajaxdel",
    type : "post",
    dateType : "json",
    data : {
      "des3Id" : eduId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          // 关掉弹窗
          // hideAddEduBox();
          hideEditEduBox();
          // 更新对应的工作经历信息
          showPsnEduHistory();
        }
      });

    },
    error : function() {
      hideEditEduBox();
    }
  });
}

function validateEduIns(type) {
  if ("add" == type && $("#eduInsName").val().trim() != "") {
    $("#eduInsNameDiv").removeClass("input_invalid");
    $("#eduInsNameHelper").attr("invalid-message", "");
  }
  if ("edit" == type && $("#editEduInsName").val().trim() != "") {
    $("#editEduInsNameDiv").removeClass("input_invalid");
    $("#editEduInsNameHelper").attr("invalid-message", "");
  }
}

// 教育经历--------------------end

// 合作者---------------------begin
function showCooperation() {
  $.ajax({
    url : '/psnweb/ajaxprjcooperation',
    type : 'post',
    dataType : 'html',
    data : {
      "psnId" : '1000000723859'
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $(data).insertAfter($("#copurationModuleDiv"));
      });

    },
    error : function() {
    }
  });
}

// 合作者---------------------end

// 其他--------------begin
function hideLabel(name) {
}

// 显示输入框提示
function showLabel(name, obj) {
}

// 非本人，隐藏部分操作按钮
function hideOperationBtn() {
  if ($("#isMySelf").val() == "false") {
    $(".operationBtn").hide();
  }
}

function copyData() {
  var clipBoardContent = $("#psn_head_url").html();
  window.clipboardData.setData("Text", clipBoardContent);
  // alert("复制成功");
}

function copyToClipboard() {
  var Url2 = document.getElementById("psn_head_url");
  Url2.select(); // 选择对象
  document.execCommand("Copy"); // 执行浏览器复制命令
  // alert("已复制好，可贴粘。");
}

function toPubConsequence() {
  $.ajax({
    url : "/scmwebsns/spread/pubConsequence",
    type : "post",
    dateType : "html",
    data : {

    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $(".oldDiv").remove();
        $(data).insertAfter($("#headDiv"));
      });

    },
    error : function() {

    }
  });
}

// 添加联系人
addIdentificFriend = function(reqPsnId, psnId, obj) {
  if (obj) {
    BaseUtils.doHitMore(obj, 10000);
  }
  $.ajax({
    url : '/psnweb/friend/ajaxaddfriend',
    type : 'post',
    data : {
      'des3Id' : reqPsnId
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "true") {
          scmpublictoast(homepage.addFriendRequest, 1000);
        } else {
          scmpublictoast(data.msg, 1000);
        }
      });

    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
    }
  });
}
// 给联系人发送消息
sendFriendMsg = function(reqPsnId) {
  location.href = "/dynweb/showmsg/msgmain?model=chatMsg&des3ChatPsnId=" + reqPsnId;
}
// 关注此人
addAttention = function(attentionId, reqPsnId) {
  $.ajax({
    url : "/psnweb/psnsetting/ajaxSaveAttFrd",
    type : 'post',
    dataType : 'json',
    data : {
      'des3PsnIds' : reqPsnId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.action == "success" || data.action == "doNothing") {
          scmpublictoast(homepage.attentionPsn, 1000);
          $(".attentionId").removeAttr("onclick");
          $(".attentionId").attr("onclick", "cancelAttention('" + data.attPsnId + "','" + reqPsnId + "')");
          $(".attentionId").text(homepage.cancelAttention);
        } else {
          scmpublictoast(homepage.attentionPsnFail, 1000);
        }
      });
    },
    error : function() {
    }
  });
}
// 取消关注
cancelAttention = function(attentionId, reqPsnId) {
  var param = {
    "userSettings.cancelId" : attentionId
  }
  $.ajax({
    url : "/psnweb/psnsetting/ajaxCancelAttPerson",
    type : 'post',
    dataType : 'json',
    data : param,
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(homepage.cancelAttentionPsn, 1000);
          $(".attentionId").removeAttr("onclick");
          $(".attentionId").attr("onclick", "addAttention('" + attentionId + "','" + reqPsnId + "')");
          $(".attentionId").text(homepage.addAttention);
        } else {
          scmpublictoast(homepage.cancelAttentionPsnFail, 1000);
        }
      });

    },
    error : function() {
    }
  });

}
// 取消联系人
function cancelFriend(des3FriendId) {
  smate.showTips._showNewTips(homepage.sureCancelFriend, homepage.tips, "sureDealFriend('"+des3FriendId+"')", "",
      homepage.sure, homepage.cancel);
}


function sureDealFriend(des3FriendId){
  $.ajax({
    url : "/psnweb/friend/ajaxdel",
    type : "post",
    dataType : 'json',
    data : {
      "friendPsnIds" : des3FriendId
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(homepage.cancelFriend, 2000);
        } else {
          scmpublictoast(homepage.cancelFriendFail, 2000);
        }
        var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
        if(pluginclose != null){
            if($(".new-searchplugin_container")){
                $(".background-cover").remove();
                $(".new-searchplugin_container").remove();
             }
        }
      });
    },
    error: function(){
      scmpublictoast(homepage.cancelFriendFail, 2000);
    }
  });
}

// 其他--------------end
// 头像处理回调函数
function imgUploadCutCallBack(patch) {
  var post_data = {
    "avatars" : patch
  };
  $.ajax({
    url : "/psnweb/psninfo/ajaxSaveAvatar",
    type : 'post',
    dataType : 'json',
    data : post_data,
    async : false,
    success : function(_data) {
      if (_data.result == 'error') {
        scmpublictoast(homepage.fail, 1000);
      } else {
        Resume.personInfo.base.avatarClose();
        scmpublictoast(homepage.operateSuccess, 1000);
      }
    },
    error : function() {
      scmpublictoast(homepage.fail, 1000);
    }
  });
};

/**
 * 编辑头像关闭
 */
Resume.personInfo.base.avatarClose = function() {
  $("#base_list_text").show();
  $("#base_list_avatar_box").remove();
};

/**
 * 更换头像
 */
function changePsnAvatar() {
  $("#upload_img_cutfile").imgcutupload({
    "ctxpath" : snsctx,
    "respath" : resscmsns,
    "imgType" : 1,
    "locale" : locale,
    "dataDes3Id" : $("#des3PsnId").val(),
    "selectFileFn" : 1,
    "ispsnweb" : 1
  });
}

// 超时处理
Resume.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;

  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(homepage.timeOut, homepage.tips, function(r) {
      if (r) {
        document.location.href = window.location.href;
        return 0;
      }
    });

  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
};

Resume.doHitMore = function(obj, time) {
  $(obj).attr("disabled", true);
  var click = $(obj).attr("onclick");
  if (click != null && click != "") {
    $(obj).attr("onclick", "");
  }
  setTimeout(function() {
    $(obj).removeAttr("disabled");
    if (click != null && click != "") {
      $(obj).attr("onclick", click);
    }
  }, time);
};
function saveArea(psnRegionId) {
  $.ajax({
    url : '/psnweb/common/ajaxRegion',
    type : 'post',
    dataType : 'json',
    data : {
      "psnId" : psnRegionId
    },
    success : function(data) {
      if (data == null) {
        $("#psnRegionId").val(psnRegionId);
      }
    },
    error : function() {
    }
  });
}
// 修改工作经历后地区也随着变化
// 个人主页地区填值
function initArea() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxRegion",
    type : 'post',
    dataType : 'json',
    data : {
      "superRegionId" : $("#psnRegionId").val()
    },
    success : function(data) {
      if (data.thirdRegionName != null) {
        var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
        $selectValue = $selectBox.querySelector("span");
        $selectValue.textContent = data.thirdRegionName;
        $selectValue.classList.remove("sel__value_placeholder");
        $selectBox.setAttribute("sel-value", data.secondCode);
        var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
        $selectValue = $selectsecondBox.querySelector("span");
        $selectValue.textContent = data.secondRegionName;
        $selectsecondBox.style.visibility = "visible";
        $selectValue.classList.remove("sel__value_placeholder");
        $selectsecondBox.setAttribute("sel-value", data.firstCode);
        var $selectthirdBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
        $selectValue = $selectthirdBox.querySelector("span");
        $selectValue.textContent = data.firstRegionName;
        $selectthirdBox.style.visibility = "visible";
        $selectValue.classList.remove("sel__value_placeholder");
      } else if (data.secondRegionName != null) {
        var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
        $selectValue = $selectBox.querySelector("span");
        $selectValue.textContent = data.secondRegionName;
        $selectBox.setAttribute("sel-value", data.firstCode);
        $selectValue.classList.remove("sel__value_placeholder");
        var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
        $selectValue = $selectsecondBox.querySelector("span");
        $selectValue.textContent = data.firstRegionName;
        $selectsecondBox.style.visibility = "visible";
        $selectValue.classList.remove("sel__value_placeholder");
        getNextLevelRegion();
      } else {
        /*
         * if(data.firstRegionName=="中国"){ var $selectsecondBox=
         * document.querySelector('.sel__box[selector-id="2nd_area"]'); $selectValue =
         * $selectsecondBox.querySelector("span"); $selectsecondBox.style.visibility = "visible";
         * $selectValue.classList.remove("sel__value_placeholder"); var $selectBox=
         * document.querySelector('.sel__box[selector-id="1st_area"]');
         * $selectBox.setAttribute("sel-value","156"); }
         */
        var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
        $selectValue = $selectBox.querySelector("span");
        $selectValue.textContent = data.firstRegionName;
        $selectValue.classList.remove("sel__value_placeholder");
        getNextLevelRegion();
      }
    },
    error : function() {

    }
  });
}
// 判断是否有下一级地区
function getNextLevelRegion() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxGetNextLevelRegion",
    type : "post",
    dataType : "json",
    data : {
      "superRegionId" : $("#psnRegionId").val()
    },
    success : function(data) {
      if (data.nextLevel == 'true') {
        var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
        if ($selectsecondBox.style.visibility != "visible" ||$selectsecondBox.innerText == "" || $selectsecondBox.innerText.indexOf("选择二级地区") != -1
            || $selectsecondBox.innerText.indexOf("Region/Province") != -1) {
          $selectValue = $selectsecondBox.querySelector("span");
          $selectsecondBox.style.visibility = "visible";
          $selectValue.classList.remove("sel__value_placeholder");
          var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
          if ($("#psnRegionId").val() == null) {
            $selectBox.setAttribute("sel-value", "156");
          } else {
            $selectBox.setAttribute("sel-value", $("#psnRegionId").val());
          }
        } else {
          var $selectsecondBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
          $selectValue = $selectsecondBox.querySelector("span");
          $selectsecondBox.style.visibility = "visible";
          $selectValue.classList.remove("sel__value_placeholder");
          var $selectBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
          $selectBox.setAttribute("sel-value", $("#psnRegionId").val());
        }
      }
    }
  });
}
// 个人主页地区填值
function workArea() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxRegion",
    type : 'post',
    dataType : 'json',
    data : {
      "superRegionId" : $("#psnRegionId").val()
    },
    success : function(data) {
      if (data.thirdRegionName != null) {
        var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
        $selectValue = $selectBox.querySelector("span");
        $selectValue.textContent = data.thirdRegionName;
        $selectValue.classList.remove("sel__value_placeholder");
        $selectBox.setAttribute("sel-value", data.secondCode);
        var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
        $selectValue = $selectsecondBox.querySelector("span");
        $selectValue.textContent = data.secondRegionName;
        $selectsecondBox.style.visibility = "visible";
        $selectValue.classList.remove("sel__value_placeholder");
        $selectsecondBox.setAttribute("sel-value", data.firstCode);
        var $selectthirdBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
        $selectValue = $selectthirdBox.querySelector("span");
        $selectValue.textContent = data.firstRegionName;
        $selectthirdBox.style.visibility = "visible";
        $selectValue.classList.remove("sel__value_placeholder");
      } else if (data.secondRegionName != null) {
        var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
        $selectValue = $selectBox.querySelector("span");
        $selectValue.textContent = data.secondRegionName;
        $selectBox.setAttribute("sel-value", data.firstCode);
        $selectValue.classList.remove("sel__value_placeholder");
        var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
        $selectValue = $selectsecondBox.querySelector("span");
        $selectValue.textContent = data.firstRegionName;
        $selectsecondBox.style.visibility = "visible";
        $selectValue.classList.remove("sel__value_placeholder");

        var $selectthirdBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
        $selectValue = $selectthirdBox.querySelector("span");
        $selectthirdBox.style.visibility = "hidden";
      } else {
        var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
        $selectValue = $selectBox.querySelector("span");
        $selectValue.textContent = data.firstRegionName;
        $selectValue.classList.remove("sel__value_placeholder");

        var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
        $selectValue = $selectsecondBox.querySelector("span");
        $selectsecondBox.style.visibility = "hidden";
        var $selectthirdBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
        $selectValue = $selectthirdBox.querySelector("span");
        $selectthirdBox.style.visibility = "hidden";

      }
    },
    error : function() {

    }
  });
}

// 添加访问记录
function addVistRecord(vistPsnDes3Id, actionDes3Key, actionType) {
  if (vistPsnDes3Id != null && vistPsnDes3Id != "" && actionDes3Key != null && actionDes3Key != ""
      && actionType != null && actionType != "") {
    $.ajax({
      url : "/psnweb/outside/addVistRecord",
      type : "post",
      dataType : "json",
      data : {
        "vistPsnDes3Id" : vistPsnDes3Id,
        "actionDes3Key" : actionDes3Key,
        "actionType" : actionType
      },
      success : function(data) {

      },
      error : function() {
      }
    });
  }
}

// 检查姓名是否为空
Resume.personInfo.base.checkName = function(firstName, lastName, zhFirstName, zhLastName) {
  var result = false;
  if (checkIsNull(firstName)) {
    // TODO 提示英文名为空
    $("#firstName_div").addClass("input_invalid");
    $("#firstName_error").attr("invalid-message", homepage.firstNameEmpty);
    result = true;
  } else {
    firstName = Resume.trimLeftAndRightSpace(firstName);
  }
  if (checkIsNull(lastName)) {
    // TODO 提示英文姓为空
    $("#lastName_div").addClass("input_invalid");
    $("#lastName_error").attr("invalid-message", homepage.lastNameEmpty);
    result = true;
  } else {
    lastName = Resume.trimLeftAndRightSpace(lastName);
  }
  if (!checkIsNull(zhFirstName)) {
    zhFirstName = Resume.trimLeftAndRightSpace(zhFirstName);
  }
  if (!checkIsNull(zhLastName)) {
    zhLastName = Resume.trimLeftAndRightSpace(zhLastName);
  }
  return result;
}

// 检查是否为空
function checkIsNull(param) {
  var check = false;
  if (param == null || param == "" || typeof (param) == "undefined") {
    check = true;
  } else {
    param = Resume.trimLeftAndRightSpace(param);
    if (param == "") {
      check = true;
    }
  }
  return check;
}

// 去掉左右两端空格
Resume.trimLeftAndRightSpace = function(str) { // 删除左右两端的空格
  return str.replace(/(^\s*)|(\s*$)/g, "");
}

// 检查姓名
Resume.personInfo.base.checkNameInvalid = function() {
  $("#firstName").bind("input propertychange", function() {
    var firstName = $(this).val();
    if (!checkIsNull(firstName)) {
      $("#firstName_div").removeClass("input_invalid");
      $("#firstName_error").attr("invalid-message", "");
    }
  });

  $("#lastName").bind("input propertychange", function() {
    var lastName = $(this).val();
    if (!checkIsNull(lastName)) {
      $("#lastName_div").removeClass("input_invalid");
      $("#lastName_error").attr("invalid-message", "");
    }
  });
}

// 中文转换拼音
Resume.parsepinyin = function(obj, input_id, parent_id, error_id) {
  var $this = $(obj);
  if (!checkIsNull($this.val())) {
    var pattern = /^[a-zA-Z\u4e00-\u9fa5-·\' ]+$/;
    if (!pattern.test($this.val())) {
      return;
    }
    $.ajax({
      url : "/psnweb/pinyin/ajaxparse",
      type : "post",
      data : {
        "word" : Resume.trimLeftAndRightSpace($this.val())
      },
      dataType : "json",
      success : function(data) {
        var selectedInput = "#" + input_id;
        if (data.result != "error" && checkIsNull($(selectedInput).val()) && !checkIsNull(data.result)) {
          $(selectedInput).val(data.result);
          $("#" + parent_id).addClass("input_not-null");
          $("#" + parent_id).removeClass("input_invalid");
          $("#" + error_id).attr("invalid-message", "");
        }
      },
      error : function() {
      }
    });
  }
};

// 导出简历
Psn_Info.exportResume = function(obj) {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    BaseUtils.doHitMore($(obj), 3000);
    window.location.href = "/psnweb/resume/ajaxwordexport";
  }, 1);
}

// 获取来访人员列表
Resume.getPsnList = function() {
  $("#psn_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : '/psnweb/friendreq/ajaxvistpsnlist',
    type : 'post',
    data : {
      "page.pageSize" : "6",
      "page.ignoreMin" : true
    },
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#psn_list").html(data);
        if ($("#psn_list").find(".main-list__item").length == 0) {
          $("#psn_list").html("<div class='response_no-result'>" + homepage.noRecord + "</div>");
        }
      });
    }
  });
}

// 获取更多来访人员列表
Resume.getPsnListMore = function() {
  Resume.PsnListMore = window.Mainlist({
    name : "vist_psn_more_list",
    listurl : "/psnweb/friendreq/ajaxvistpsnlist",
    listdata : {
      "isAll" : "1"
    },
    method : "scroll",
    listcallback : function(xhr) {
    },
  });
}

// 显示更多来访人员
Resume.showVistPsnMoreUI = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    showDialog("dev_vist_psn_more");
    Resume.getPsnListMore();
  }, 1);
}

// 隐藏更多来访人员
Resume.hideVistPsnMoreUI = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  hideDialog("dev_vist_psn_more");
}

// 成果合作者
Resume.cooperator = function() {
  $("#psn_cooper_loading").show();
  $.ajax({
    url : '/psnweb/cooperation/ajaxshow',
    type : 'post',
    dataType : 'html',
    data : {
      "des3CurrentId" : $("#des3PsnId").val()
    },
    success : function(data) {
      $('#psn_cooperator_list').html(data);
      if ($("#psn_cooperator_list").find(".main-list__item").length == 0) {
        $("#psn_cooperator_list").html("<div class='response_no-result'>" + homepage.noRecord + "</div>");
      }
    }
  });
};

// 合作者-查看全部
Resume.psnCooperatorAll = function() {
  $('#dev_pubcooperator_isall').val("all");
  showDialog("psncooperator_dialog");
  window.Mainlist({
    name : "psncooperator",
    listurl : "/psnweb/cooperation/ajaxshow",
    listdata : {
      "des3CurrentId" : $("#des3PsnId").val(),
      "isAll" : "1"
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
};

// 合作者-关闭查看全部弹出层
Resume.closePsnCooperatorBack = function() {
  hideDialog("psncooperator_dialog");
};

// 基金推荐
Resume.showRecommendFund = function() {
  $("#recommend_fund_loading").show();
  $.ajax({
    url : '/prjweb/fundrecommend/ajaxshow',
    type : 'post',
    dataType : 'html',
    data : {
      "pageNum" : 1,
      "pageSize" : 3
    },
    success : function(data) {
      $('#recommend_fund_list').html(data);
      if ($("#recommend_fund_list").find(".main-list__item").length == 0) {
        $("#recommend_fund_list").html("<div class='response_no-result'>" + homepage.noRecord + "</div>");
      }
    },
    error : function() {

    }
  });
}

// 跳转基金推荐页面
Resume.toFundRecommend = function() {
  document.location.href = "/prjweb/fund/main?module=recommend";
}

// 跳转基金详情
Resume.showFundDetails = function(desFundId, shortUrl) {
  window.open("/prjweb/outside/showfund?encryptedFundId=" + desFundId);
}
// 个人主页 推荐人员列表
Resume.showRecommendPsn = function() {
  $.ajax({
    url : '/psnweb/homepage/recommendpsn',
    type : 'post',
    dataType : 'html',
    success : function(data) {
      /*
       * $('#recommend_fund_list').html(data);
       * if($("#recommend_fund_list").find(".main-list__item").length==0){
       * $("#recommend_fund_list").html("<div class='response_no-result'>"+homepage.noRecord+"</div>"); }
       */
    },
    error : function() {

    }
  });

}
// 个人主页 推荐人员列表 加载更多
Resume.showRecommendPsnMore = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    showDialog("recommend_psn_more");
    $recommendfriend = window.Mainlist({
      name : "recommendfriend",
      listurl : "/psnweb/homepage/ajaxrecommendpsn",
      listdata : {
        "isAll" : "1",
        "des3PsnId" : $("#des3PsnId").val()
      },
      method : "scroll",
      listcallback : function(xhr) {

      },
    });
  }, 1);
}
Resume.hideRecommendPsnMore = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  hideDialog("recommend_psn_more");
}

// 添加首要工作经历
Resume.editFirstWorkHistory = function() {
  // 没有首要单位，弹出添加工作经历框
  Resume.addPrimaryWorkHistoryOnline();
}

// 保存首要单位
Resume.saveFirstWorkHistory = function(type, obj) {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    Resume.saveFirstWorkHistoryOnline(type, obj);
  }, 1);
}

// 取消编辑首要单位
Resume.cancelFirstWorkHistory = function() {
  // $("#addPsnWorkBox").html($("#psnInfoBox").html());
  // editPsnInfoAfterAddWork();
  Resume.resetPsnInfoEditBox(null, null, null, null);
}

Resume.saveFirstWorkHistoryOnline = function(type, obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 4000);
  }
  var result = true;
  if ("add" == type && $("#newInsNamePrimary").val().trim() == "") {
    $("#newInsNameDivPrimary").addClass("input_invalid");
    $("#newInsNameHelperPrimary").attr("invalid-message", homepage.inputInsName);
    result = false;
  }

  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  var startYear = parseInt($("#newStartTimePrimary").attr("date-year"));
  var startMonth = parseInt($("#newStartTimePrimary").attr("date-month"));
  var toYear = parseInt($("#newEndTimePrimary").attr("date-year"));
  var toMonth = parseInt($("#newEndTimePrimary").attr("date-month"));

  var addIsActive = 0; // 至今
  var addWorkupToNow = $("#addWorkUpToNowPrimary").prop("checked")
  if (addWorkupToNow) {
    addIsActive = 1;
  }
  if (isNaN(toYear) && addIsActive == 0) {
    $("#addWorkToYearDivPrimary").addClass("input_invalid");
    $("#addWorkToYearHelpPrimary").attr("invalid-message", homepage.trueEndTime);
    result = false;
  }
  if (isNaN(startYear)) {
    $("#addWorkFromYearDivPrimary").addClass("input_invalid");
    $("#addWorkFromYearHelpPrimary").attr("invalid-message", homepage.trueStartTime);
    result = false;
  } else {
    if (!((!isNaN(toYear) && compareEduTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)) {
      $("#addWorkToYearDivPrimary").addClass("input_invalid");
      $("#addWorkToYearHelpPrimary").attr("invalid-message", homepage.trueEndTime);
      result = false;
    } else {
      $("#addWorkToYearDivPrimary").removeClass("input_invalid");
      $("#addWorkToYearHelpPrimary").attr("invalid-message", "");
      $("#addWorkFromYearDivPrimary").removeClass("input_invalid");
      $("#addWorkFromYearHelpPrimary").attr("invalid-message", "");
    }
    if (addIsActive == 1 && !compareEduTime(startYear, startMonth, nowYear, nowMonth)) {
      $("#addWorkFromYearDivPrimary").addClass("input_invalid");
      $("#addWorkFromYearHelpPrimary").attr("invalid-message", homepage.trueStartTime);
      result = false;
    }
    if (addIsActive == 0 && !compareEduTime(startYear, startMonth, toYear, toMonth)) {
      $("#addWorkFromYearDivPrimary").addClass("input_invalid");
      $("#addWorkFromYearHelpPrimary").attr("invalid-message", homepage.trueStartTime);
      result = false;
    }
    if (addIsActive == 0 && !compareEduTime(toYear, toMonth, parseInt(nowYear) + 10, nowMonth)) {
      $("#addWorkToYearDivPrimary").addClass("input_invalid");
      $("#addWorkToYearHelpPrimary").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
  }
  if (!result) {
    return false;
  }
  var addData;
  var newInsName = $("#newInsNamePrimary").val().trim();
  var newDeptName = $("#newDepartmentPrimary").val().trim();
  var newPosition = $("#newPositionPrimary").val().trim();
  addData = {
    'insId' : $("#newInsNamePrimary").attr("code"),
    'insName' : newInsName,
    'department' : newDeptName,
    'position' : newPosition,
    'anyUser' : 7,
    'fromYear' : $("#newStartTimePrimary").attr("date-year"),
    'fromMonth' : $("#newStartTimePrimary").attr("date-month") == "null" ? "" : $("#newStartTimePrimary").attr(
        "date-month"),
    'toYear' : $("#newEndTimePrimary").attr("date-year") == "null" ? "" : $("#newEndTimePrimary").attr("date-year"),
    'toMonth' : $("#newEndTimePrimary").attr("date-month") == "null" ? "" : $("#newEndTimePrimary").attr("date-month"),
    'description' : $("#newDescriptionPrimary").val(),
    'isActive' : addIsActive,
    'operateType' : 'addPrimary'
  };
  // $("#editPsnWorkBox").doLoadStateIco({status:1});//加载圈
  $.ajax({
    url : "/psnweb/workhistory/ajaxsave",
    type : "post",
    dataType : "json",
    async : false,
    data : addData,
    success : function(data) {
      if (data.result == "success") {
        // 更新对应的工作经历信息
        Resume.resetPsnInfoEditBox(data.workId, newInsName, newDeptName, newPosition);
        showPsnWorkHistory();
        reloadPsnBaseInfo();
      } else {
        scmpublictoast(homepage.systemBusy, 1000);
        return false;
      }
    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
      return false;
    }
  });

}

Resume.resetPsnInfoEditBox = function(workId, insName, deptName, position) {
  $("#add_primary_wokhis").hide();
  if (workId != null && typeof (workId) != undefined && workId != "") {
    $("#workId").val(workId);
    $("#current_work_box").attr("sel-value", workId);
    $("#currentWorkSelectDiv").addClass("input_not-null");
    var workText = insName;
    if (deptName != null && deptName != "") {
      workText += ", " + deptName;
    }
    if (position != null && position != "") {
      workText += ", " + position;
    }
    $("#current_work_box_text_span").text(workText);
  }
  $("#psn_info_editDiv").show();
  addFormElementsEvents($("#psnInfoBox")[0]);
  $("#psnInfoBox").css("height", reCalculateDialogHeight("psnInfoBox") + "px");
}

function editPsnInfoAfterAddWork() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxedit",
    type : "post",
    dataType : "html",
    async : false,
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#psnInfoBox").html(data);
        addFormElementsEvents($("#psnInfoBox")[0]);
        $("#psnInfoBox").css("height", reCalculateDialogHeight("psnInfoBox") + "px");
      });
    },
    error : function() {
    }
  });
}

function reCalculateDialogHeight(dialogId) {
  var $dialog = document.getElementById(dialogId);
  var $dialogHeight = 0;
  Array.from($dialog.children).forEach(function(x) {
    const $height = x.style.height ? parseInt(x.style.height) : x.scrollHeight;
    $dialogHeight = $dialogHeight + $height;
  });
  return $dialogHeight;
}

Resume.addPrimaryWorkHistoryOnline = function() {
  // 先清空输入框中的内容
  $("#newInsNamePrimary").val("");
  $("#newDepartmentPrimary").val("");
  $("#newPositionPrimary").val("");
  $("#newStartTimePrimary").val("");
  $("#newEndTimePrimary").val("");
  $("#newStartTimePrimary").attr("date-year", "");
  $("#newStartTimePrimary").attr("date-month", "");
  $("#newStartTimePrimary").attr("date-date", "");
  $("#newEndTimePrimary").attr("date-year", "");
  $("#newEndTimePrimary").attr("date-month", "");
  $("#newEndTimePrimary").attr("date-date", "");
  $("#newDescriptionPrimary").val("");
  // 清空提示语
  $("#newInsNameDivPrimary").removeClass("input_invalid");
  $("#newInsNameHelperPrimary").attr("invalid-message", "");
  $("#addWorkFromYearDivPrimary").removeClass("input_invalid");
  $("#addWorkFromYearHelpPrimary").attr("invalid-message", "");
  $("#addWorkToYearDivPrimary").removeClass("input_invalid");
  $("#addWorkToYearHelpPrimary").attr("invalid-message", "");
  $("#psn_info_editDiv").hide();
  $("#add_primary_wokhis").show();
  addFormElementsEvents($("#add_primary_wokhis")[0]);
  $("#psnInfoBox").css("height", reCalculateDialogHeight("psnInfoBox") + "px");
  // 重新绑定事件
  // 为必填项添加校验---TODO
  $("#newInsNamePrimary").bind('blur', checkPrimaryWorkNameNull);
  $("#newStartTimePrimary").bind('blur', checkPrimaryWorkDateNull);
  $("#newEndTimePrimary").bind('blur', checkPrimaryWorkEndDateNull);
}

function checkPrimaryWorkNameNull() {
  // add
  if (typeof ($("#newInsNamePrimary").val()) != "undefined") {
    if ($("#newInsNamePrimary").val().trim() == "") {
      $("#newInsNameDivPrimary").addClass("input_invalid");
      $("#newInsNameHelperPrimary").attr("invalid-message", homepage.inputInsName);
    } else {
      $("#newInsNameDivPrimary").removeClass("input_invalid");
      $("#newInsNameHelperPrimary").attr("invalid-message", "");
    }
  }
}

function checkPrimaryWorkDateNull() {
  // add
  var startYear = $("#newStartTimePrimary").attr("date-year");
  var startMonth = $("#newStartTimePrimary").attr("date-month");
  var endYear = $("#newEndTimePrimary").attr("date-year");
  var endMonth = $("#newEndTimePrimary").attr("date-month");
  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  var checked = $("#addWorkUpToNowPrimary").prop("checked");
  if (checkIsBlank(startYear) || !compareEduTime(startYear, startMonth, parseInt(nowYear) + 10, nowMonth)
      || (!checkIsBlank(endYear) && !compareEduTime(startYear, startMonth, endYear, endMonth))
      || (checked && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#addWorkFromYearDivPrimary").addClass("input_invalid");
    $("#addWorkFromYearHelpPrimary").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addWorkFromYearDivPrimary").removeClass("input_invalid");
    $("#addWorkFromYearHelpPrimary").attr("invalid-message", "");
  }
}

function checkIsBlank(str) {
  if (isNaN(str) || str == "" || str == null || str == "undefined") {
    return true;
  }
  return false;
}

function checkPrimaryWorkEndDateNull() {
  // add
  var startYear = $("#newStartTimePrimary").attr("date-year");
  var startMonth = $("#newStartTimePrimary").attr("date-month");
  var endYear = $("#newEndTimePrimary").attr("date-year");
  var endMonth = $("#newEndTimePrimary").attr("date-month");
  var nowYear = $("#nowYear").val();
  var nowMonth = $("#nowMonth").val();
  var checked = $("#addWorkUpToNowPrimary").prop("checked");
  if ((checkIsBlank(endYear) && !checked)
      || (!checkIsBlank(endYear) && !checked && (!compareEduTime(endYear, endMonth, parseInt(nowYear) + 10, nowMonth) || (!isNaN(startYear) && !compareEduTime(
          startYear, startMonth, endYear, endMonth))))) {
    $("#addWorkToYearDivPrimary").addClass("input_invalid");
    $("#addWorkToYearHelpPrimary").attr("invalid-message", homepage.trueEndTime);
    // 4.结束年份为至今且小于开始年份
  } else if (checkIsBlank(startYear)
      || (checked && !isNaN(startYear) && !compareEduTime(startYear, startMonth, nowYear, nowMonth))) {
    $("#addWorkFromYearDivPrimary").addClass("input_invalid");
    $("#addWorkFromYearHelpPrimary").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addWorkToYearDivPrimary").removeClass("input_invalid");
    $("#addWorkToYearHelpPrimary").attr("invalid-message", "");
  }
}

// 检查人员配置信息
Psn_Info.initPsnConfInfo = function() {
  $.ajax({
    url : "/psnweb/psnconf/ajaxinit",
    type : "post",
    datatype : "json",
    data : {},
    success : function(data) {
    },
    error : function() {
    }
  });
}
