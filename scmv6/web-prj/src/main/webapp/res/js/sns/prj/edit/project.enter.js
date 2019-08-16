ProjectEnter = function() {
};
ScholarEnter = function() {
};
ProjectEnter.ResappContext = respath;
ProjectEnter.setUpPrjInstance = function(prjId) {

  var output = new DefaultProject(prjId);
  window.output = output;
};

ProjectEnter.getPrjInstance = function() {

  return window.output;
};
ProjectEnter.saveOutput = function(confirm) {
  // 关键词
  var _zh_keys = $.autoword['_project_zh_keywords_div'].words();
  var _zh_keys_str = "";
  $.each(_zh_keys, function() {
    _zh_keys_str += "; " + this["text"];
  });
  $("#_project_zh_keywords").attr("value", $.trim(_zh_keys_str.substr(1)));
  var _en_keys = $.autoword['_project_en_keywords_div'].words();
  var _en_keys_str = "";
  $.each(_en_keys, function() {
    _en_keys_str += "; " + this["text"];
  });
  $("#_project_en_keywords").attr("value", $.trim(_en_keys_str.substr(1)));

  $('.irisRichBox').each(
      function() {
        // $("#"+$(this).attr("id")).attr("value",$.editor($(this).attr("id")).html());
        var id = $(this).attr("id");
        var editor = editors[id];
        // editor.sync();
        var text = $.trim(editor.text());
        var val = "";
        if (text != "") {
          val = editor.html();
        }
        if ("_project_zh_title" == $(this).attr("id") || "_project_en_title" == $(this).attr("id")
            || "_project_zh_abstract" == $(this).attr("id") || "_project_en_abstract" == $(this).attr("id")) {
          val = val.replace(/</g, "&amp;lt;");
          val = val.replace(/>/g, "&amp;gt;");
        }
        $("#" + $(this).attr("id")).attr("value", val);
      });
  ProjectEnter.getPrjInstance().beforeSave();
  // 清空错误消息
  $("div.errorMessage").html("");
  $("div.pub-error").remove();

  var flag = ProjectEnter.getPrjInstance().validate();
  ProjectEnter.getPrjInstance().postSave();
  if (flag) {
    $("div.errorMessage").fadeOut();
    ProjectEnter.DoSubmitPrj();
    return;
  }

  $("div.errorMessage").fadeIn();
  $("label.error").fadeIn();

  if (ProjectEnter.getPrjInstance().is_titleBlank()) {
    ScmMaint.setMenuTab('one', 1, 3);
    // var msg = $("div.errorMessage")[0];
    ProjectEnter.goOn();// projectEdit.msg.enter.required
    var title = i18n_prj["projectEdit.label.ctitle"];
    var msg = $.fn.formateMsg(i18n_prj["projectEdit.msg.enter.required"], title);
    ProjectEnter.goOn();
    $.scmtips.show("warn", msg);
    $("#TB_ajaxContent").css("height", "auto");
  } else if (ProjectEnter.getPrjInstance().is_titleOutBound(false)) {
    var title = i18n_prj["projectEdit.label.ctitle"];
    msg = $.fn.formateMsg(i18n_prj["projectEdit.msg.enter.maxlength"], title, 2000);
    ScmMaint.setMenuTab('one', 1, 3);
    ProjectEnter.goOn();
    // $.scmtips.show("warn",msg);
    $("#TB_ajaxContent").css("height", "auto");
  } else if (ProjectEnter.getPrjInstance().is_abstractOutBound(false)) {
    var title = i18n_prj["projectEdit.tips.cabstract"];
    msg = $.fn.formateMsg(i18n_prj["projectEdit.msg.enter.maxlength"], title, 20000);
    ScmMaint.setMenuTab('one', 1, 3);
    ProjectEnter.goOn();
    $.scmtips.show("warn", msg);
    $("#TB_ajaxContent").css("height", "auto");
  } else if (ProjectEnter.getPrjInstance().hasFieldDataError()) {
    ProjectEnter.goOn();
    $.scmtips.show("warn", i18n_prj["projectEdit.msg.enter.hasFieldDataError"]);
    ProjectEnter.forcusError();
  } else if (ProjectEnter.getPrjInstance().hasNullField()) {
    // ProjectEnter.goOn();
    // $("#show_confirm_alert").trigger("click");

    ProjectEnter.DoSubmitPrj();
    /*
     * jConfirm(i18n_prj["projectEdit.msg.enter.hasNullField"],maint_alart_deleteTipTitle,function(sure){
     * if(sure){ ProjectEnter.goOn(); }else{ ProjectEnter.DoSubmitPrj(); } },'yesOrNo');
     */
  } else {
    ProjectEnter.DoSubmitPrj();
  }
};
ProjectEnter.ShowWarnAlert = function(msg) {
  $("#warn_alert").find(".warn_alert_msg_label").html(msg);
  // $("#show_warn_alert").trigger("click");
  jAlert(msg, maint_alart_deleteTipTitle);
};
ProjectEnter.forcusSetId = function(tagId) {

  var tag = $("#" + tagId + "_focus")[0];
  if (!tag) {
    tag = $("#" + tagId)[0];
  }
  $(".close_div").trigger("click");
  if (tag) {
    tag.focus();
  }
};
ProjectEnter.forcusError = function() {
  var errorLabels = $("label.error");
  if (errorLabels.size() > 0) {
    for (var i = 0; i < errorLabels.size(); i++) {
      var errorLabel = $(errorLabels[i]);
      var tagId = errorLabel.attr("for");
      if (!tagId || tagId == "") {
        continue;
      }
      var tab = $("label.title[for='" + tagId + "']").attr("tab");
      if (!tab) {
        continue;
      }
      ScmMaint.setMenuTab('one', tab, 3);
      if (!$(errorLabel).is(":visible")) {
        continue;
      }
      $("#" + tagId).focus();
      return;
    }
  }
};
ProjectEnter.AuthorDetails = function() {
};
ProjectEnter.AuthorDetails.selectIns = function(tag) {
  // 获取当前单位名称
  var currentInsName = $("#currentInsName").val();
  var currentInsId = $("#currentInsId").val();
  var tr = $(tag).parent().parent().parent().parent();
  var insId = $(tag).val();
  if (insId == currentInsId) {
    tr.find(".wrap_ac_psn_div").show();
    $(tr).find(".member_psn_acname_input").val("").show();
    $(tr).find(".author_ins_id").val(currentInsId);
    $(tr).find(".author_ins_name").val(currentInsName);
  } else {
    tr.find(".wrap_ac_psn_div").hide();
    ProjectEnter.AuthorDetails.removeInsPsn(tr.find(".member_psn_acname_remove"));
    $(tr).find(".author_ins_id").val(insId);
    $(tr).find(".author_ins_name").val("");
  }
};
ProjectEnter.AuthorDetails.removeInsPsn = function(tag) {

  var div = $(tag).parent();
  var psnId = $(div).find(":hidden.member_psn_id_hidden").val();
  var rpsns = $("#_prj_members_remove_psns").val();
  if ($.trim(psnId) != '' && /\d+/g.test(psnId)) {
    if (rpsns == "") {
      rpsns = psnId;
    } else {
      rpsns = rpsns + "," + psnId;
    }
    $("#_prj_members_remove_psns").val(rpsns);
  }

  $(div).find(":hidden.member_psn_id_hidden").val("");
  $(div).find(":hidden.member_psn_acname_hidden").val("");
  $(div).find("label.member_psn_acname_label").hide().html("");
  $(div).find(".member_psn_acname_remove").hide();
  $(div).find(".member_psn_acname_input").val("").show();

};
ScholarEnter.forcusSetId = function(tagId) {
  ProjectEnter.forcusSetId(tagId);
};
/* autoComplete begin */
ProjectEnter.AutoComplete = function() {
};
ProjectEnter.AutoComplete.rolAuthorPsnSetupInit = function() {

  var url = ctxpath + "/ac/rolPnsIns.action";

  $(".member_psn_acname_input").each(function() {

    ProjectEnter.AutoComplete.rolAuthorPsnSetup(this);
  });
};
ProjectEnter.AutoComplete.rolAuthorPsnSetup = function(tag) {

  var url = ctxpath + "/ac/rolPnsIns.action";

  $(tag).autocomplete(url, {
    width : 220,
    highlight : function(value, term) {
      return value;
    },
    max : 10,
    dataType : 'JSON',
    scroll : false,
    extraParams : {
      'excludePsn' : function() {
        var usedPsnId = [];
        $(":hidden.member_psn_id_hidden").each(function() {
          var value = $.trim($(this).val());
          if (value != "") {
            usedPsnId.push(value);
          }
        });
        return usedPsnId.join(",");
      },
      'defaultPsn' : function() {
        // 默认人员
        return $.trim($(tag).parent().parent().parent().find(".prj_member_input_name").val());
      }
    },
    formatItem : function(data, i, n, value, term) {
      return data.psnName;
    },
    parse : function(data) {// 转化数据
      var rows = eval(data);
      var parsed = [];
      for (var i = 0; i < rows.length; i++) {
        var item = rows[i];
        parsed.push({
          data : item,
          value : item.cpPsnId.toString(),
          result : item.psnName
        });
      }
      return parsed;
    }
  });

  $(tag).result(function(event, data, formatted) {// 返回result

    $(tag).fadeOut(function() {
      var li = $(tag).parent().parent();
      li.find(":hidden.member_psn_id_hidden").val(data.cpPsnId);
      li.find(":hidden.member_psn_acname_hidden").val(data.psnName);
      li.find("label.member_psn_acname_label").html(data.psnName).fadeIn();
      li.find(".member_psn_acname_remove").show();

    }).val("");
  });
};
ProjectEnter.AutoComplete.setup = function(tag, acType, acOptions) {
  if (!acType)
    return;
  var url = '';
  var lang = 'zh_CN';
  switch (acType) {
    case 'authorInstitution' :
      url += acType;
    break;
    case 'psnIns' :
      url = '/prjweb/prj/ajax-psnIns';
    break;
    case 'prjScheme' :
      url = '/prjweb/prj/ajax-schemes';
    break;
    case 'prjSchemeEn' :
      url = '/prjweb/prj/ajax-schemes';
      lang = 'en_US';
    break;
    case 'prjSchemeAgency' :
      url = '/prjweb/prj/ajax-scheme-agencies';
    break;
    case 'prjSchemeAgencyEn' :
      url = '/prjweb/prj/ajax-scheme-agencies';
      lang = 'en_US';
    break;
  }

  $(tag).autocomplete(url, {
    width : acOptions['width'] || 320,
    highlight : function(value, term) {
      return value;
    },
    max : 10,
    dataType : 'JSON',
    type : 'POST',
    scroll : false,
    scrollHeight : 300,
    cacheLength : 0,
    extraParams : {
      'excludeParam' : function() {
        if ("authorInstitution" == acType || "psnIns" == acType) {
          if ($(tag).val() != $("#_project_old_ins_name").val()) {
            $("#_project_ins_id").val("");
          }
        }
        return '';
      },
      'agencyId' : function() {
        if ("prjScheme" == acType) {
          if ($(tag).val() != $("#_project_old_scheme_name").val()) {
            $("#_project_scheme_id").val("");
          }
        }
        if ("prjSchemeEn" == acType) {
          if ($(tag).val() != $("#_project_old_scheme_name_en").val()) {
            $("#_project_scheme_en_id").val("");
          }
        }
        if ("prjSchemeAgency" == acType) {
          if ($(tag).val() != $("#_project_old_scheme_agency_name").val()) {
            $("#_project_scheme_agency_id").val("");
            $("#_project_scheme_id").val("");
          }
        }
        if ("prjSchemeAgencyEn" == acType) {
          if ($(tag).val() != $("#_project_old_scheme_agency_name_en").val()) {
            $("#_project_scheme_agency_en_id").val("");
            $("#_project_scheme_en_id").val("");
          }
        }
        return $("#_project_scheme_agency_id").val();
      },
      'lang' : lang
    },
    formatItem : function(data, i, n, value, term) {
      return data.name;
    },
    parse : function(data) {// 转化数据
      var rows = eval(data);
      var parsed = [];
      for (var i = 0; i < rows.length; i++) {
        var item = rows[i];
        parsed.push({
          data : item,
          value : item.code.toString(),
          result : item.name
        });
      }
      return parsed;
    }
  });
  $(tag).result(function(event, data, formatted) {// 返回result
    var id = null;
    if ("authorInstitution" == acType || "psnIns" == acType) {
      id = "#_project_ins_id";
      $("#_project_old_ins_name").val($(tag).val());
    }
    if ("prjSchemeAgency" == acType) {
      id = "#_project_scheme_agency_id";
      $("#_project_old_scheme_agency_name").val($(tag).val());
    }
    if ("prjSchemeAgencyEn" == acType) {
      id = "#_project_scheme_agency_en_id";
      $("#_project_old_scheme_agency_name_en").val($(tag).val());
    }
    if ("prjScheme" == acType) {
      id = "#_project_scheme_id";
      $("#_project_old_scheme_name").val($(tag).val());
    }
    if ("prjSchemeEn" == acType) {
      id = "#_project_scheme_en_id";
      $("#_project_old_scheme_name_en").val($(tag).val());
    }
    if (data && id) {
      $(id).val(data.code);
    } else if (id) {
      $(id).val("");
    }
  });
};

ProjectEnter.AutoComplete.authorInsSetupInit = function(acType) {

  if (!acType)
    acType = "authorInstitution";
  $(".author_ins_name_input").each(function() {
    var psnId = $(this).closest('tr').find('td:eq(2)').find('input:eq(0)').val();
    ProjectEnter.AutoComplete.authorInsSetup($(this), acType, psnId);
  });
};
ProjectEnter.AutoComplete.authorInsSetup = function(tag, acType, psnId) {
  psnId = psnId || '';
  var url = '/prjweb/prj/ajax-psnIns';
  $(tag).autocomplete(url, {
    width : 280,
    highlight : function(value, term) {
      return value;
    },
    max : 5,
    cacheLength : 0,
    dataType : 'JSON',
    scroll : false,
    extraParams : {
      'psnId' : psnId,
      'excludeParam' : function() {
        if ($(tag).parent().find(".author_ins_old_input").val() != $(tag).val()) {
          $(tag).parent().find(":hidden.author_ins_id").val("");
        }
        return '';
      }
    },
    formatItem : function(data, i, n, value, term) {
      if (data.country) {
        return data.name + "<br/><label class='msglabel'>" + data.country + "</label>";
      } else {
        return data.name;
      }
    },
    parse : function(data) {// 转化数据
      var rows = eval(data);
      var parsed = [];
      for (var i = 0; i < rows.length; i++) {
        var item = rows[i];
        parsed.push({
          data : item,
          value : item.code.toString(),
          result : item.name
        });
      }
      return parsed;
    }
  });

  $(tag).result(function(event, data, formatted) {// 返回result
    $(tag).parent().find(".author_ins_old_input").val($(tag).val());
    $(tag).parent().find(".author_ins_id_input").val(data.code);
  });
};
ProjectEnter.richBox = [];
ProjectEnter.InitRichTextBox = function() {
};
/* autoComplete end */
// 校验必填字段
// ["field1",["field2","field3"]]
ProjectEnter.checkRequiredField = function(mark) {

  if (!mark)
    return [];

  var blankFields = [];
  if (ProjectEnter.getPrjInstance().is_titleBlank()) {
    blankFields.push('/project/@title');
  }

  var fields = ProjectEnter.getPrjInstance().get_reqFields();
  for (var i = 0; i < fields.length; i++) {
    var item = fields[i];
    if ("string" == typeof (item)) {
      var obj = $g(item)[0];
      if (obj) {
        // 多个只需要放第一个radio id即可
        if ($(obj).attr("type") == "radio") {
          var objs = $(":radio[name='" + $(obj).attr("name") + "']");
          var flag = true;
          for (var j = 0; j < objs.length; j++) {
            if ($(objs[j]).attr("checked") == true) {
              flag = false;
              break;
            }
          }
          if (flag)
            blankFields.push(item);
        } else {
          if ($.trim($g(item).attr('value')) == "")
            blankFields.push(item);
        }

      }
    } else {
      var flag = false;
      var flagitem = null;
      $.each(item, function(subindex, subitem) {
        if ($g(subitem) && $.trim($g(subitem).attr('value')) != "") {
          flag = true;
        } else if (!flagitem) {
          flagitem = subitem;
        }
      });
      if (!flag) {
        blankFields.push(flagitem);
      }
    }
  }
  return blankFields;
};
ProjectEnter.DoSubmitPrj = function() {
  // 弹框登录不刷新页面
  BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
    $("#mainform").attr("action", "save.action");
    $("#mainform").submit();
  }, 0);
};
ProjectEnter.goOn = function() {
  $('.saveOutputButton').attr("disabled", false);
  $('#doing_tip').hide();
};
// 是否有上一页
ProjectEnter.InitPrePage = function() {
  var url = ctxpath + "/ajaxHashPreUrl";
  $.ajax({
    url : url,
    type : 'get',
    dataType : 'json',
    success : function(data) {
      if (data.result) {
        $(".back_prepage_btn").bind("click", function() {
          window.location.href = ctxpath + "/backUrl";
        }).show();
      }
    }
  });
};

$(document).ready(
    function() {

      var prj_id = $g("/prj_meta/@prj_id").val();

      $(".saveOutputButton").click(function() {
        $('.saveOutputButton').attr("disabled", true);
        $("#doing_tip").show();
        // 判断页面是不是超时
        BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
          setTimeout(ProjectEnter.saveOutput, 200);
        }, 0);
      });

      // 1.按成果类型实例化一个实例
      ProjectEnter.setUpPrjInstance(prj_id);
      // 2.按成果类型初始化页面控件：AutoComplete,
      ProjectEnter.getPrjInstance().initPagesTags();
      // 3.初始化附件上传:SWFUpload
      // initAttachFileUpload();
      // initFullTextFileUpload();
      // 4.设置禁用输入法的输入框
      $.fn
          .disabledImeMode(["/project/@funding_year", "/project/@amount", "/project/@start_year",
              "/project/@start_month", "/project/@start_day", "/project/@end_year", "/project/@end_month",
              "/project/@end_day", "/project/@prj_internal_no", "/project/@prj_external_no",
              "/prj_fulltext/@fulltext_url"]);
      // 5.是否有上一页
      // ProjectEnter.InitPrePage();
      // TODO: 页面初始化时，标识必填项而未填的
      if (!tabIndex) {
        tabIndex = 1;
      }
      // 设置卡片显示
      if (bckpermission && bckpermission == 1) {
        ScmMaint.setMenuTab('one', 3, 3);
      } else {
        ScmMaint.setMenuTab('one', tabIndex, 3);
      }

      window.initPage = false;
    });