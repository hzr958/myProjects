var categoryThickbox = categoryThickbox ? categoryThickbox : {};

categoryThickbox.putPrvCity = function() {
  var text = "";
  var counText = $("#counId option:selected").text();
  var prvText = $("#prvId option:selected").text();
  var cityText = $("#cityId option:selected").text();
  var quText = $("#quId option:selected").text();
  if (counText != "" && prvText != "" && cityText != "" && quText != "") {// 只显示最后一级地区
    text = quText;
  } else if (counText != "" && prvText != "" && cityText != "" && quText == "") {
    text = cityText;
  } else if (counText != "" && prvText != "" && cityText == "" && quText == "") {
    text = prvText;
  } else if (counText != "" && prvText == "" && cityText == "" && quText == "") {
    text = counText;
  }
  // var text = counText+prvText+cityText+quText;
  var counVal = $("#counId").val();
  var prvVal = $("#prvId").val();
  var cityVal = $("#cityId").val();
  var quVal = $("#quId").val();
  prvVal = prvVal == null ? counVal : prvVal == "" ? counVal : prvVal;
  var val = cityVal == null ? prvVal : cityVal == "" ? prvVal : cityVal;
  if (quVal != null) {
    if (quVal != "") {
      val = quVal;
    }
  }
  if (text != "") {
    prvcityWord.put(val, text);
  }
}

$(document).ready(function() {
  // 检测输入框为空时列出所有
  var key = $("#agencyNameId").val();
  if (key == '') {
    matchDropAgencyName($("#agencyNameId")[0])
  }
})

function matchDropAgencyName(ob) {
  // 是否存在的资助机构
  var drop = $(".pro_fund_main-selector_box")[0];
  $(drop).empty();
  var key = ob.value;
  $.ajax({
    url : ctxpath + "/fund/ajaxMatchAgencyNameBykey",
    data : {
      "key" : key
    },
    dataType : 'json',
    success : function(data) {
      var i = 0;
      $(drop).height(0);
      for (i; i < data.length; i++) {
        var item = document.createElement("div");
        var height = $(drop).height();
        item.className = "pro_fund_main-selector_item";
        $(item).html(data[i].nameZh);
        // 绑定点击事件
        item.setAttribute("onclick", "addIdToInput(this)");
        item.setAttribute("agencyId", data[i].id);
        drop.appendChild(item);
        if (height < 175) {
          $(drop).height(24 * (i + 1));
        }
      }
      drop.style.display = "block";
    }
  });
}
// 添加点击事件把id给input框
function addIdToInput(obj) {
  $("#agencyId").val(obj.getAttribute("agencyId"));
  $("#agencyNameId").val($(obj).html());
  $(".pro_fund_main-selector_box")[0].style.display = "none";
  $("#isExist").val("true");
}

categoryThickbox.delChossenDisc = function(cmpId) {
  var cmpOjb = $('#' + cmpId);
  if ($.trim(cmpOjb.val()) == '')
    return;
  jConfirm("您确定删除此学科吗？", "提示", function(sure) {
    if (sure) {
      cmpOjb.empty();
      cmpOjb.attr("disc_code", "");
      cmpOjb.append("<option value=''>选择学科</option>");
    }
  });
}
var count = 0;
categoryThickbox.addChossenDisc = function(obj, subject) {
  var $obj = $(obj);
  var count1 = ++count;
  var count2 = ++count;
  if (!subject) {
    subject = "";
  }
  var setcontent = '<td align="right" valign="middle">' + subject + '</td>'
      + '<td align="left" valign="top" colspan="3">'
      + '<select class="inp_text Fleft select-subject_btn" id="discipline_' + count1
      + '" name="disList" style="width: 275px" seq="0">' + '<option value="" selected="selected">选择学科</option>'
      + '</select> ' + '<a class="Blue Fleft mleft5"  onclick="categoryThickbox.delChossenDisc(\'discipline_' + count1
      + '\');">' + '<i class="icon_del"></i>删除</a> '
      + '<select class="inp_text Fleft mleft5 select-subject_btn" id="discipline_' + count2
      + '" name="disList" style="width: 275px" seq="1">' + '<option value="" selected="selected" >选择学科</option>'
      + '</select> ' + '<a class="Blue Fleft mleft5" onclick="categoryThickbox.delChossenDisc(\'discipline_' + count2
      + '\');">' + '<i class="icon_del"></i>删除</a>' + '</td>';
  var box = document.createElement("tr");
  box.innerHTML = setcontent;
  $obj.parent().parent().before(box);

  var selectlist = document.getElementsByClassName("select-subject_btn");
  for (var i = 0; i < selectlist.length; i++) {
    selectlist[i].addEventListener("click", function(event) {
      this.blur();
      newstructuresubject("/scmmanagement/const/ajaxDiscipline", this);
    })
  }
}

categoryThickbox.removeFile = function(obj) {
  jConfirm("您确定删除该文件吗？", "提示", function(sure) {
    if (sure) {
      fileCount = fileCount - 1;
      var tabltrcount = $("#viwe_files_table tr").length;
      $(obj).parent().parent().remove();
      if (tabltrcount == 2) {
        $("#viwe_files_table").css("display", "none");
      } else {
        var row = 1;
        $("#viwe_files_table tr").each(function() {
          if (row > 1) {
            $(this).find(".file_no").html(row - 1);
          }
          row++;
        });
      }
    }
  });
}

//文件改变事件
categoryThickbox.change=function(){
  var file= $("#filedata").val();
  //如果点击了取消按钮 
  if(file==""){
    //将显示框也显示为空
    $(".filedata").val("");
  }
}
categoryThickbox.ajaxUploadFile = function() {
  $("#upload_btn").disabled();
  var file = $("#filedata").val();
  if ($(".filedata").val() == application_file_upload_maxSize || $(".filedata").val() == "") {
    $.scmtips.show("warn", "请选择上传的文件", null, 2000);
    $("#upload_btn").enabled();
    return;
  }
  jConfirm(
      "在上传文件之前，请确保您上传的文件不违反相关法律法规并且不侵犯版权",
      "提示",
      function(sure) {
        if (sure) {
          var denyExt = ".js|.jsp|.php|.aspx|.asp|.asa|.sh|.cmd|.bat|.exe|.dll|.com|.lnk";
          var extName = file.substring(file.lastIndexOf(".")).toLowerCase();// （把路径中的所有字母全部转换为小写）
          if (denyExt.indexOf(extName + "|") != -1) {
            ErrMsg = "出于安全性考虑，不支持上传此文件类型"
            $.scmtips.show("warn", ErrMsg, null, 2000);
            $("#upload_btn").enabled();
            return false;
          }
          Loadding_div.show_over("dialog_content", "dialog_content");
          $
              .ajaxFileUpload({
                url : ctxpath + "/archiveFiles/ajaxUploadFund.action",
                secureuri : false,
                fileElementId : 'filedata',
                data : {
                  'jsessionId' : jsessionId
                },
                dataType : 'json',
                success : function(data, status) {
                  if (data.ajaxSessionTimeOut == "yes") {
                    jConfirm("系统超时或未登录，您要登录吗？", "提示", function(r) {
                      if (r) {
                        document.location.href = ctxpath + "/fund/category/maint";
                      }
                    });
                  } else {
                    Loadding_div.close("dialog_content");
                    if (data.result == 'error') {
                      $.scmtips.show("warn", "请选择30MB范围内的文件", null, 2000);
                    } else {
                      $.scmtips.show("success", "上传成功", null, 2000);
                      fileCount = fileCount + 1;
                      $("#viwe_files_table")
                          .append(
                              "<tr><td class=\"file_no\" align=\"center\">"
                                  + fileCount
                                  + "</td><td align=\"left\"><a href=\""
                                  + ctxpath
                                  + "/archiveFiles/fileDownload?fdesId="
                                  + data.des3FileId
                                  + "&nodeId="
                                  + data.nodeId
                                  + "\" class=\"Blue file\">"
                                  + data.fileName
                                  + "</a></td><td align=\"center\"><a onclick=\"categoryThickbox.removeFile(this);\" class=\"Blue\">删除</a></td></tr>");
                      $("#viwe_files_table").css("display", "");
                    }
                    $('.file').attr('value', '');
                    $("#filedata,.filedata").change(
                        function() {
                          $('.filedata').css("color", "#000");
                          $('.filedata').val(
                              $(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
                        });
                    $("#upload_btn").enabled();
                  }
                },
                error : function(data, status, e) {
                  Loadding_div.close("dialog_content");
                  $('.file').attr('value', '');
                  $.scmtips.show("error", "系统超时", null, 2000);
                  $("#upload_btn").enabled();
                }
              });
        } else {
          $("#upload_btn").enabled();
        }
      }, 'yesOrNo');
}

categoryThickbox.getTitleDegree = function(name) {
  var str = "";
  $("input[name='" + name + "']").each(function() {
    if ($(this).attr("checked")) {
      str += "," + $(this).val();
    }
  });
  return str.length > 0 ? str.substr(1) : str;
};
categoryThickbox.getInsType = function(name) {
  var str = "";
  $("input[name='" + name + "']").each(function() {
    if ($(this).attr("checked")) {
      str += "," + $(this).val();
    }
  });
  return str.length > 0 ? str.substr(1) : str;
};
categoryThickbox.checkExistByName=function(){
  var key = $("#agencyNameId").val();
  $.ajax({
    url : ctxpath + "/fund/ajaxCheckExistByName?key=" + encodeURI(key),
    type : 'get',
    async:false,
    dataType : 'json',
    success : function(data) {
      if (data && data.id != null && data.id != '') {
        $("#isExist").val("true");
        $("#agencyId").val(data.id);
      }
    }
  });
}
categoryThickbox.saveCategory = function(insCatId) {
  categoryThickbox.checkExistByName();
  var isExist = $("#isExist").val();
  var id = $("#constCategoryId").val();
  var agencyId = $("#agencyId").val();
  var nameZh = $.trim($("#nameZh").val());
  var nameEn = $.trim($("#nameEn").val());
  var code = $.trim($("#code").val());
  var abbr = $.trim($("#abbr").val());
  var language = $.trim($("#language").val());
  var description=document.getElementById("description").value;
  description = description.replace(/\r\n/g, '<br/>'); //IE9、FF、chrome
  description = description.replace(/\n/g, '<br/>'); //IE7-8
  description = description.replace(/\s/g, '&nbsp;'); //空格处理
  var guideUrl = $.trim($("#guideUrl").val());
  var fundId = $("#fundId").val();
  if (isExist != "true") {
    $.scmtips.show('warn', "资助机构不存在，请新增或者从下拉列表中选择", null, 2000);
    return;
  }
  if (guideUrl == "http://") {
    guideUrl = "";
  }
  if (guideUrl != "" && guideUrl.indexOf("http://") < 0 && guideUrl.indexOf("https://") < 0) {
    guideUrl = "http://" + guideUrl;
  }
  var declareUrl = $.trim($("#declareUrl").val());
  if (declareUrl == "http://") {
    declareUrl = "";
  }
  if (declareUrl != "" && declareUrl.indexOf("http://") < 0 && declareUrl.indexOf("https://") < 0) {
    declareUrl = "http://" + declareUrl;
  }
  var disList = "";
  $("select[name='disList']").each(function() {
    if ($(this).val() != "") {
      disList += "," + $(this).val();
    }
  });
  disList = disList.substring(1);
  var keywordList = autoWord.texts().join(",");
  var regionList = JSON.stringify(prvcityWord.words());
  regionList = regionList == "[]" ? "" : regionList;
  var startDate = $.trim($("#startDate").val());
  var endDate = $.trim($("#endDate").val());
  var titleRequire = $.trim($("#titleRequire").val());
  var titleBest = $.trim($("#titleBest").val());
  var degreeRequire = $.trim($("#degreeRequire").val());
  var degreeBest = $.trim($("#degreeBest").val());
  var birthLeast = $.trim($("#birthLeast").val());
  var birthMax = $.trim($("#birthMax").val());
  var ageLeast = $.trim($("#ageLeast").val());
  var ageMax = $.trim($("#ageMax").val());
  var contact = $.trim($("#contact").val());
  var relationship = $("#relationship").val();
  var deadline = $.trim($("#deadline").val());
  var strength = $.trim($("#strength").val());
  var insCategoryId = $.trim($("#insCategoryId").val()); // 类别id
  var auditType = $.trim($("#auditType").val()); // 批准/更新
  var parentCategoryId = $.trim($("#parentCategoryId").val());
  var fileList = new Array;
  if(fundId == ""){
    $("#viwe_files_table tr").each(
        function(r) {
          if (r > 0) {
            var filePath = "";
            var ldoPath = $(this).find("a.file").attr("href");
            if (ldoPath.indexOf("http:") == -1) {
              filePath = "http://" + pageContext_request_serverName + ldoPath;
            } else {// 如果是审核rol则替换域名
              filePath = "http://" + pageContext_request_serverName + ctxpath + "/"
                  + ldoPath.substr(ldoPath.indexOf("archiveFiles"), ldoPath.length);
            }
            var fileName = $(this).find("a.file").text();
            var param = {
              "fileName" : fileName,
              "filePath" : filePath
            };
            fileList.push(param);
          }
        });
  }else{
    $("#viwe_files_table tr").each(
        function(r) {
          if (r > 0) {
            var filePath = "";
            var ldoPath = $(this).find("a.file").attr("href");
            if (ldoPath.indexOf("http:") == -1) {
              filePath = "http://" + pageContext_request_serverName + ldoPath;
            }else{
              filePath = ldoPath;
            }
            var fileName = $(this).find("a.file").text();
            var param = {
              "fileName" : fileName,
              "filePath" : filePath
            };
            fileList.push(param);
          }
        });
  }
  

  if (keywordList.length > 2000) {
    $.scmtips.show('warn', "关键词字数不能大于2000个字符", null, 2000);
    return;
  }
  fileList = JSON.stringify(fileList);
  if (agencyId == "") {
    $.scmtips.show('warn', "资助机构不能都为空", null, 2000);
    return;
  }
  if (nameZh == "" && nameEn == "") {
    $.scmtips.show('warn', "资助类别名称不能都为空", null, 2000);
    return;
  }
  if (startDate == "" || endDate == "") {
    $.scmtips.show('warn', "申请日期的开始日期和结束日期都不能为空", null, 2000);
    return;
  }
  var begin = new Date(startDate.replace(/\-/g, "\/"));
  var end = new Date(endDate.replace(/\-/g, "\/"));
  if (begin - end > 0) {
    $.scmtips.show('warn', "开始日期必须在截止日期之前", null, 2000);
    return;
  }
  var beginBirth = new Date(birthLeast.replace(/\-/g, "\/"));
  var endBirth = new Date(birthMax.replace(/\-/g, "\/"));
  if (beginBirth - endBirth > 0) {
    $.scmtips.show('warn', "出生年月开始日期必须在结束日期之前", null, 2000);
    return;
  }
  if (parseInt(ageLeast) > parseInt(ageMax)) {
    $.scmtips.show('warn', "最佳年龄开始年龄必须在结束年龄之前", null, 2000);
    return;
  }
  var defTtile = $("#defTtile").val();
  var defDegree = $("#defDegree").val();
  var titleRequire1 = categoryThickbox.getTitleDegree("titleRequire1");
  titleRequire1 = titleRequire1 == "" ? defTtile : titleRequire1;
  var insType = categoryThickbox.getInsType("insType");
  if (insType == "") {
    $.scmtips.show('warn', "单位要求不能都为空", null, 2000);
    return;
  }
  var degreeRequire1 = $("#education").val();
  // 200=所有
  if ("200" == degreeRequire1) {
    degreeRequire1 = "210,220,230,240";
    // 选择本科以上
  } else if ("210" == degreeRequire1) {
    degreeRequire1 = "210,220,230";
  }

  var isMatch = $("#isMatch").val();// 是否配套
  var percentage = $.trim($("#percentage").val());// 比例

  $("#btn_submit").disabled();
  var params = {
    "fundCategory.id" : id,
    "fundCategory.agencyId" : agencyId,
    "fundCategory.nameZh" : nameZh,
    "fundCategory.nameEn" : nameEn,
    "fundCategory.regionList" : regionList,
    "fundCategory.code" : code,
    "fundCategory.abbr" : abbr,
    "fundCategory.relationship" : relationship,
    "languageStr" : language,
    "fundCategory.description" : description,
    "fundCategory.guideUrl" : guideUrl,
    "fundCategory.declareUrl" : declareUrl,
    "fundCategory.disList" : disList,
    "fundCategory.keywordList" : keywordList,
    "startDateStr" : startDate,
    "endDateStr" : endDate,
    "fundCategory.titleRequire1" : titleRequire1,
    "fundCategory.degreeRequire1" : degreeRequire1,
    /* "fundCategory.titleRequire2":titleRequire2,"fundCategory.degreeRequire2":degreeRequire2, */
    "fundCategory.titleBest" : titleBest,
    "fundCategory.degreeBest" : degreeBest,
    "birthLeastStr" : birthLeast,
    "birthMaxStr" : birthMax,
    "fundCategory.ageLeast" : ageLeast,
    "fundCategory.ageMax" : ageMax,
    "fundCategory.contact" : contact,
    "fundCategory.fileList" : fileList,
    "fundCategory.deadline" : deadline,
    "fundCategory.strength" : strength,
    "fundCategory.insCategoryId" : insCategoryId,
    "fundCategory.parentCategoryId" : parentCategoryId,
    "auditType" : auditType,
    "fundCategory.isMatch" : isMatch,
    "fundCategory.percentage" : percentage,
    "fundCategory.insType" : insType,
    "fundCategory.fundId":fundId
  };
  $.ajax({
    url : ctxpath + "/fund/ajaxCheckCategory",
    type : "post",
    data : params,
    timeout : 10000,
    success : function(result) {
      if (result.ajaxSessionTimeOut == "yes") {
        jConfirm("系统超时或未登录，您要登录吗？", "提示", function(r) {
          if (r) {
            document.location.href = ctxpath + "/fund/category/maint";
          }
        });
      } else {
        if (result == "false") {
          $.ajax({
            url : ctxpath + "/fund/ajaxCategorySave",
            type : "post",
            data : params,
            timeout : 10000,
            success : function(data) {
              $.scmtips.show("success", "操作成功", null, 2000);
              setTimeout(function() {
                parent.$.Thickbox.closeWin();
                parent.loadOtherPage(parent.$("#pageNo").val());
              }, 1000);
              setTimeout(function() {
                if(fundId != ""){
                  parent.setTabUrl(ctxpath + '/fund/third/main')
                }
              },1200)
              
            },
            error : function() {
              $("#btn_submit").enabled();
              $.scmtips.show('error', "操作失败", null, 2000);
              setTimeout(function() {
                parent.$.Thickbox.closeWin();
              }, 1000);
            }
          });
        } else {
          $("#btn_submit").enabled();
          $.scmtips.show('warn', "该资助类别已经存在", null, 2000);
        }
      }
    },
    error : function() {
      $("#btn_submit").enabled();
      $.scmtips.show('error', "操作失败", null, 2000);
      setTimeout(function() {
        parent.$.Thickbox.closeWin();
      }, 1000);
    }
  });
}

categoryThickbox.approve = function() {
  var id = $("#constCategoryId").val();
  var agencyId = $("#agencyId").val();
  var nameZh = $.trim($("#nameZh").val());
  var nameEn = $.trim($("#nameEn").val());

  var keywordList = autoWord.texts().join(",");
  var startDate = $.trim($("#startDate").val());
  var endDate = $.trim($("#endDate").val());
  var birthLeast = $.trim($("#birthLeast").val());
  var birthMax = $.trim($("#birthMax").val());
  var ageLeast = $.trim($("#ageLeast").val());
  var ageMax = $.trim($("#ageMax").val());
  var parentCategoryId = $.trim($("#parentCategoryId").val());
  var auditType = $.trim($("#auditType").val()); // 批准/更新
  if (agencyId == "") {
    $.scmtips.show('warn', "资助机构不能都为空", null, 2000);
    return;
  }

  if (keywordList.length > 2000) {
    $.scmtips.show('warn', "关键词字数不能大于2000个字符", null, 2000);
    return;
  }
  if (agencyId == "") {
    $.scmtips.show('warn', "资助机构不能都为空", null, 2000);
    return;
  }
  if (nameZh == "" && nameEn == "") {
    $.scmtips.show('warn', "资助类别名称不能都为空", null, 2000);
    return;
  }
  var begin = new Date(startDate.replace(/\-/g, "\/"));
  var end = new Date(endDate.replace(/\-/g, "\/"));
  if (begin - end > 0) {
    $.scmtips.show('warn', "开始日期必须在截止日期之前", null, 2000);
    return;
  }
  var beginBirth = new Date(birthLeast.replace(/\-/g, "\/"));
  var endBirth = new Date(birthMax.replace(/\-/g, "\/"));
  if (beginBirth - endBirth > 0) {
    $.scmtips.show('warn', "出生年月开始日期必须在结束日期之前", null, 2000);
    return;
  }
  if (parseInt(ageLeast) > parseInt(ageMax)) {
    $.scmtips.show('warn', "最佳年龄开始年龄必须在结束年龄之前", null, 2000);
    return;
  }

  $.ajax({
    url : ctxpath + "/fund/ajaxCheckCategory",
    type : "post",
    data : {
      "fundCategory.id" : id,
      "fundCategory.agencyId" : agencyId,
      "fundCategory.nameZh" : nameZh,
      "fundCategory.nameEn" : nameEn,
      "fundCategory.parentCategoryId" : parentCategoryId,
      "auditType" : auditType
    },
    timeout : 10000,
    success : function(result) {
      if (result.ajaxSessionTimeOut == "yes") {
        jConfirm("系统超时或未登录，您要登录吗？", "提示", function(r) {
          if (r) {
            document.location.href = ctxpath + "/fund/category/maint";
          }
        });
      }
      if (result == "true") {
        $.scmtips.show('warn', "该资助类别已经存在", null, 2000);
      } else {
        jConfirm("您确定要批准该资助类别吗？", "提示", function(sure) {
          if (sure) {
            $.ajax({
              url : ctxpath + "/fund/ajaxAuditInsFundCategory",
              type : "post",
              data : {
                "id" : $("#insCategoryId").val(),
                "status" : 1
              },
              timeout : 10000,
              success : function(data) {
                if (data) {
                  categoryThickbox.saveCategory(insCategoryId);
                }
              }
            });
          }
        });
      }
    }
  });
}

categoryThickbox.refuse = function() {
  jConfirm("您确定要拒绝该资助类别吗？", "提示", function(sure) {
    if (sure) {
      $.ajax({
        url : ctxpath + "/fund/ajaxAuditInsFundCategory",
        type : "post",
        data : {
          "id" : $("#insCategoryId").val(),
          "status" : 2
        },
        timeout : 10000,
        success : function(data) {
          if (data.ajaxSessionTimeOut == "yes") {
            jConfirm("系统超时或未登录，您要登录吗？", "提示", function(r) {
              if (r) {
                document.location.href = ctxpath + "/fund/category/maint";
              }
            });
          } else {
            $.scmtips.show('success', "操作成功!", null, 2000);
            setTimeout(function() {
              parent.$.Thickbox.closeWin();
              parent.window.location.reload();
            }, 1000);
          }
        },
        error : function() {
          $.scmtips.show('error', "操作失败", null, 2000);
          setTimeout(function() {
            parent.$.Thickbox.closeWin();
          }, 1000);
        }
      });
    }
  });
}

categoryThickbox.checkDeadline = function(obj, event) {
  if ($.browser.msie && parseInt($.browser.version) < 9 && event.propertyName == 'value') {
    var inputText = $(obj).val();
    if (inputText != '' && inputText.match(/^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/)) {
      var strs = inputText.split(".");
      var str1 = strs[0];
      var str2 = strs[1];
      if (str2 != '' && str2.match(/[^[1-9]\d*$]/gi)) {

      } else {
        $(obj).val(str1 + "." + str2.replace(/[^0-9]/gi, ''));
      }
    } else if (inputText != '' && inputText.match(/^[1-9]\d*$/)) {

    } else {
      $(obj).val(inputText.replace(/[^0-9]/gi, ''));
    }
  } else {
    var inputText = $(obj).val();
    if (inputText != '' && inputText.match(/^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/)) {
      var strs = inputText.split(".");
      var str1 = strs[0];
      var str2 = strs[1];
      if (str2 != '' && str2.match(/[^[1-9]\d*$]/gi)) {

      } else {
        $(obj).val(str1 + "." + str2.replace(/[^0-9]/gi, ''));
      }
    } else if (inputText != '' && inputText.match(/^[1-9]\d*$/)) {

    } else {
      $(obj).val(inputText.replace(/[^0-9]/gi, ''));
    }
  }
}

categoryThickbox.createWordHtml = function(val, flag, show) {
  $("#hotKeyWord").is(":visible") ? '' : $("#hotKeyWord").show();
  var array = new Array();
  array.push('<div  class="hot_01" name="' + flag + '" valText="' + val + '" style="display:' + (show ? '' : 'none')
      + '">');
  array.push('<a href="javascript:void(0);" onclick="categoryThickbox.addSelectKeyword(this);" title="' + val
      + '" flag="' + flag + '"><i class="addfriends"></i>' + val + '</a>');
  array.push('</div>');
  return array.join('');
}

categoryThickbox.addSelectKeyword = function(obj) {
  var val = "auto_" + $(obj).attr("flag");
  var text = $(obj).attr("title");
  try {
    $("#auto_div").find(".auto_word_div").each(function() {
      if ($(this).find(".auto_word").attr("title") == text) {
        $(this).attr("val", val);
      }
    });
    $.autoword["auto_div"].put(val, text);
    $(obj).parent().hide();
    var hotWordCount = 0;
    $("#container").find(".hot_01").each(function() {
      if ($(this).is(":visible")) {
        hotWordCount++;
      }
    });
    if (hotWordCount < 1) {
      $("#hotKeyWord").css("display", "none");
    }
  } catch (e) {
  };
}

categoryThickbox.delSelectKeyword = function(val, text) {
  var keyword;
  if (val == "" || (typeof val != "undefined" && val != "" && val.indexOf("auto_") == -1)) {// 之前已经输入
    $("#container").find(".hot_01").each(function() {
      if ($(this).find("a").attr("title") == text) {
        $("#hotKeyWord").is(":visible") ? '' : $("#hotKeyWord").show();
        $(this).show();
      } else {
        $.autoword["auto_div"].remove(text, val);
      }
    });
  } else if (val != "" && typeof val != "undefined" && val.indexOf("auto_") != -1) {// 选择系统默认的
    var keyId = val.substring(val.indexOf("_") + 1);
    $("#container").find("div[name='" + keyId + "']").each(function() {
      if ($(this).find("a").attr("title") == text) {
        $("#hotKeyWord").is(":visible") ? '' : $("#hotKeyWord").show();
        $(this).show();
      } else {// 已经不存在了，就直接删除
        $.autoword["auto_div"].remove(text, val);
      }
    });
  }
}