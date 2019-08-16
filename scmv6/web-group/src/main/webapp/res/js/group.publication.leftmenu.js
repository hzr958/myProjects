var scmLeftMenu;
ScmLeftMenu = function(leftMenuId, ctxpath) {
  this.leftMenuId = leftMenuId;
  this.showMenu = ScmLeftMenu.showMenu;
  this.addMenu = ScmLeftMenu.addMenu;
  this.editMenu = ScmLeftMenu.editMenu;
  this.handlerEditMenu = ScmLeftMenu.handlerEditMenu;
  this.deleteMenu = ScmLeftMenu.deleteMenu;
  this.switchContent = ScmLeftMenu.switchContent;
  this.ctxpath = snsctx;
  this.init = ScmLeftMenu.init;
  this.fundingYearMove = ScmLeftMenu.fundingYearMove;
  this.doFundingYear = ScmLeftMenu.doFundingYear;
  this.initTag = ScmLeftMenu.initTag;
};

/**
 * 初始.
 */
ScmLeftMenu.init = function() {
  $("#" + this.leftMenuId).find("a").attr("class", "leftnav-hover");// 初始选中项
  if (this.leftMenuId != "menu-mine") {
    $("#" + this.leftMenuId).parent().attr("class", "menu-expansion");
    $("#" + this.leftMenuId).parent().parent().find(".Shear-head").attr("class", "Shear-headbottom");
  } else {
    $("#tag-dl").attr("class", "menu-expansion");
    $("#tag-dl").parent().find(".Shear-head").attr("class", "Shear-headbottom");
  }
};

/**
 * 菜单收缩展开.
 * 
 * @param obj
 * @return
 */
ScmLeftMenu.showMenu = function(obj) {
  var currentDl = $(obj).parent().find("dl");
  if ($(currentDl).hasClass("menu-expansion")) {
    currentDl.attr("class", "menu-shrink");
    $(obj).find(".Shear-headbottom").attr("class", "Shear-head");
  } else {
    // 关闭其他
    $(".menu-expansion").each(function() {
      $(this).attr("class", "menu-shrink");
      $(this).parent().find(".Shear-headbottom").attr("class", "Shear-head");
    });
    // 当前
    currentDl.attr("class", "menu-expansion");
    $(obj).find(".Shear-head").attr("class", "Shear-headbottom");
  }

};

/**
 * 菜单添加.
 * 
 * @param obj
 * @param leftMenuParent左侧菜单父元素ID
 * @param url
 *          添加请求url
 * @param fn回调函数
 * @return
 */
ScmLeftMenu.addMenu = function(obj, leftMenuParent, url, fn) {
  var artType = $("#navAction").val();
  var folderType;
  var newFolderUrl;
  var folderNameTips;
  if ($("#navAction").val() == 'groupPubs') {
    folderType = "P";
    newFolderUrl = snsctx + "/group/pub";
    folderNameTips = groupDetailPubs.tagNameReq;
  } else if ($("#navAction").val() == 'groupRefs') {
    folderType = "R";
    newFolderUrl = ctxpath + "/group/ref";
    folderNameTips = groupDetailPubs.tagNameReq;
  } else if ($("#navAction").val() == 'groupPrjs') {
    folderType = "J";
    newFolderUrl = ctxpath + "/group/prj";
    folderNameTips = groupDetailPrjs.tagNameReq;
  } else if ($("#navAction").val() == 'groupFiles') {
    folderType = "F";
    newFolderUrl = ctxpath + "/group/file";
    folderNameTips = groupDetailFiles.tagNameReq;
  } else if ($("#navAction").val() == 'groupWorks') {
    folderType = "W";
    newFolderUrl = ctxpath + "/group/work";
    folderNameTips = groupDetailFiles.tagNameReq;
  } else if ($("#navAction").val() == 'groupCourses') {
    folderType = "M";
    newFolderUrl = ctxpath + "/group/course";
    folderNameTips = groupDetailFiles.tagNameReq;
  }

  var folderName = $.trim($("#tag-input").val());// 管理文件夹文件夹时进行文件夹新增操作
  var folderName2 = $.trim($("#tag-input-new").val());// 加入文件夹时进行文件夹新增操作
  var isAddFolder = false;
  if (folderName.length == 0 && folderName2.length > 0) {
    folderName = folderName2;
    isAddFolder = true;
  } else {
    isAddFolder = false;
  }

  if (folderName == "") {
    $.scmtips.show("warn", folderNameTips);
  } else {
    var post_data = {
      "groupFolder.folderName" : folderName,
      "groupFolder.folderType" : folderType,
      "groupPsn.groupNodeId" : $("#groupNodeId").val(),
      "groupPsn.des3GroupId" : $("#des3GroupId").val()
    };
    $
        .ajax({
          method : "post",
          url : url,
          data : post_data,
          dataType : 'json',
          success : function(data) {
            if (data.result == "success") {
              folderName = ScmMaint.HTMLEnCode(folderName);
              var newMenuId = data.folderId;
              var des3FolderId = data.des3FolderId;
              var dd = $("#tagMove").prev("dd");
              var display = (dd.length > 0 && !dd.is(":visible")) ? "display:block" : "display:block";
              // 左侧菜单添加
              $("#" + leftMenuParent).find("#tagMove").before(
                  "<dd class=\"groupTag\" style=\"" + display + "\" id=\"menu-dd" + newMenuId
                      + "\"><a href=\"javascript:void(0)\" title='" + folderName
                      + "(0)' onclick=\"scmLeftMenu.switchContent('menu-dd" + newMenuId + "','" + newFolderUrl
                      + "','folder','" + des3FolderId + "')\"><div class=two_nav_name>" + folderName
                      + "</div><div class=Fright>(0)</div><div class=\"clear\"></div></a></dd>");
              // 弹出窗口添加
              var newTagHtml = "";
              if (!isAddFolder) {// 管理
                newTagHtml = "<li><span class=\"lable-nr\" id=\"menu-label"
                    + newMenuId
                    + "\" >"
                    + folderName
                    + "</span>"
                    + "<input maxlength=\"50\" type=\"text\" onblur=\"scmLeftMenu.handlerEditMenu('"
                    + newMenuId
                    + "',snsctx+'/group/ajaxUpdateGroupFolder')\" type=\"text\" style=\"display:none;width: 130px; margin-left: 3px;\" class=\"inp_text Fleft menu-label\" itemId='"
                    + newMenuId + "' id=\"menu-input" + newMenuId + "\" value=\"" + folderName + "\"/>"
                    + "<i class=\"icon_edit\" style=\"float:left;cursor:pointer;\" onclick=\"scmLeftMenu.editMenu("
                    + newMenuId + ")\"></i>"
                    + "<a href=\"javascript:void(0)\" class=\"pop-delete\" onclick=\"scmLeftMenu.deleteMenu(this,"
                    + newMenuId + ",ctxpath+'/group/ajaxDeleteGroupFolder')\" ></a></li>";
                ScmLeftMenu.syncThickBoxFolder(newMenuId, folderName, 1);
              } else {// 加入
                newTagHtml = "<li id='edittag_title_" + newMenuId
                    + "'><span class=\"Fleft\"><input type=\"checkbox\" name=\"moveToFolders\" value='" + newMenuId
                    + "' ></span><span id='edittag_lable_" + newMenuId + "' class=\"lable-nr\">" + folderName
                    + "</span></li>";
                ScmLeftMenu.syncThickBoxFolder(newMenuId, folderName, 2);
              }

              $(obj).parent().parent().find(".label-list").append(newTagHtml);

              // 清空当前输入框
              $("#tag-input").val("");
              $("#tag-input-new").val("");
              if (fn != null && typeof (fn) != "undefined")
                fn(newMenuId);
              // $.scmtips.show("success", data.msg);
            } else if (data.ajaxSessionTimeOut == 'yes') {
              // 超时
              Group.timeout.alert();
            } else if (data.result == "exist") {
              $.scmtips.show("warn", data.msg);
            }
          },
          error : function(xmlhttp, error, desc) {
            $.scmtips.show("error", "add folder error");
          }
        });

  }
};

ScmLeftMenu.syncThickBoxFolder = function(id, name, flag) {
  // flag=1管理时添加到加入窗口，flag=2加入时添加到管理窗口
  if (flag == 1) {
    var span = "<li id='edittag_title_" + id + "' title='" + name
        + "'><span class=\"Fleft\"><input type=\"checkbox\" name=\"moveToFolders\" value='" + id
        + "' ></span><span id='edittag_lable_" + id + "' class=\"lable-nr\">" + name + "</span></li>";
    // 加入标签到加入标签弹出的窗口
    $(".addFolderUl").append(span);
  } else {
    var newTagHtml = "<li>"
        + "<span class=\"lable-nr\" style=\"cursor: pointer;\" id=\"menu-label"
        + id
        + "\" onclick=\"scmLeftMenu.editMenu("
        + id
        + ")\" >"
        + name
        + "</span>"
        + "<input maxlength=\"50\" type=\"text\" onblur=\"scmLeftMenu.handlerEditMenu('"
        + id
        + "',snsctx+'/group/ajaxUpdateGroupFolder')\" type=\"text\" style=\"display:none;width: 130px; margin-left: 3px;\" class=\"inp_text Fleft\" id=\"menu-input"
        + id + "\" value=\"" + name + "\"/>"
        + "<i class=\"icon_edit\" style=\"float:left;cursor:pointer;\" onclick=\"scmLeftMenu.editMenu(" + id
        + ")\"></i>" + "<a href=\"javascript:void(0)\" title='" + name
        + "(0)' class=\"pop-delete\" onclick=\"scmLeftMenu.deleteMenu(this," + id
        + ",ctxpath+'/group/ajaxDeleteGroupFolder')\" ></a></li>";
    $(".manageFolderUl").append(newTagHtml);
  }
};

/**
 * 切换至菜单编辑.
 * 
 * @param menuId
 *          菜单的ID
 * @return
 */
ScmLeftMenu.editMenu = function(menuId) {
  // 其他变为非编辑状态
  $("span[id*=menu-label]").each(function() {
    $(this).show();
  });
  $("input[id*=menu-input]").each(function() {
    $(this).hide();
  });
  $("#menu-label" + menuId).hide();
  var menuInputObj = $("#menu-input" + menuId);
  menuInputObj.show();
  // menuInputObj.focus();
  // 光标移到最后
  var t = menuInputObj.val();
  menuInputObj.val("").focus().val(t);
};

/**
 * 菜单编辑处理
 * 
 * @param menuId
 *          菜单ID
 * @param url
 *          编辑请求url
 * @param fn
 *          回调函数.
 * @return
 */
ScmLeftMenu.handlerEditMenu = function(menuId, url) {
  var spanText = $.trim($("#menu-label" + menuId).text());
  var inputText = $.trim($("#menu-input" + menuId).val());
  var folderType;
  if ($("#navAction").val() == 'groupPubs') {
    folderType = "P";
  } else if ($("#navAction").val() == 'groupRefs') {
    folderType = "R";
  } else if ($("#navAction").val() == 'groupPrjs') {
    folderType = "J";
  } else if ($("#navAction").val() == 'groupFiles') {
    folderType = "F";
  } else if ($("#navAction").val() == 'groupWorks') {
    folderType = "W";
  } else if ($("#navAction").val() == 'groupCourses') {
    folderType = "M";
  }
  if (inputText.length == 0) {
    $.scmtips.show("warn", action_label_please_tagname);
    return;
  }

  if (spanText != inputText) {
    $.ajax({
      method : "post",
      url : url,
      data : {
        "groupFolder.folderName" : inputText,
        "groupFolder.groupFolderId" : menuId,
        "groupFolder.folderType" : folderType,
        "groupPsn.groupNodeId" : $("#groupNodeId").val(),
        "groupPsn.des3GroupId" : $("#des3GroupId").val()
      },
      dataType : "json",
      success : function(data) {
        if (data.ajaxSessionTimeOut == 'yes') {
          alert('系统超时');
          top.location.reload(true);
          return;
        }
        if (data.result == "exist") {
          $.scmtips.show("warn", data.msg);
          $("#menu-input" + menuId).val(spanText);
          $("#menu-label" + menuId).show();
          $("#menu-input" + menuId).hide();

        } else if (data.result == "success") {
          inputText = ScmMaint.HTMLEnCode(inputText);
          // 更新左侧菜单
          $("#menu-dd" + menuId).find(".two_nav_name").html(inputText);
          // 更新当前弹出窗口
          $("#menu-label" + menuId).text(inputText);
          $("#menu-labelr" + menuId).text(inputText);
          // 更新加入标签弹出的窗口
          $(".addFolderUl").find("input[value='" + menuId + "']").parent().next(".lable-nr").text(inputText);
          $(".addFolderUl").find("input[value='" + menuId + "']").parent().parent("li").attr("title", inputText);
          // 切换编辑
          $("#menu-label" + menuId).show();
          $("#menu-input" + menuId).hide();
          $.scmtips.show("success", data.msg);
        } else {
          $.scmtips.show("error", data.msg);
        }
      },
      error : function(e) {
        $("#menu-input" + menuId).val(spanText);
        $("#menu-label" + menuId).show();
        $("#menu-input" + menuId).hide();
      }
    });
  } else {
    $("#menu-input" + menuId).val(spanText);
    $("#menu-label" + menuId).show();
    $("#menu-input" + menuId).hide();
  }
};

/**
 * 菜单删除.
 * 
 * @param obj
 * @param menuId
 *          菜单ID
 * @param url
 *          菜单删除请求url
 * @param fn
 *          回调函数.
 * @return
 */
ScmLeftMenu.deleteMenu = function(obj, folderId, url, fn) {
  jConfirm(action_label_del_tagname, person_label_resetComfirm_title, function(r) {
    if (r) {
      var post_data = {
        "groupFolder.groupFolderId" : folderId,
        "groupPsn.groupNodeId" : $("#groupNodeId").val()
      };
      $.ajax({
        method : "post",
        url : url,
        data : post_data,
        dataType : "json",
        success : function(data) {
          if (data.ajaxSessionTimeOut == 'yes') {
            alert('系统超时');
            top.location.reload(true);
            return;
          }

          // 左侧菜单移除,重新计算数量
          var count = $("#menu-dd" + folderId).find(".Fright").text();
          if (count.length > 2) {
            var num = parseInt(count.substr(1, count.length - 1), 10);
            var divObj = $("#menu-dd-folder-else").find(".Fright");
            var noFolderStr = divObj.text();
            if (noFolderStr.length > 2) {
              var numNoFolder = parseInt(noFolderStr.substr(1, noFolderStr.length - 1), 10);
              divObj.text("(" + (numNoFolder + num) + ")");
            }
          }

          $("#menu-dd" + folderId).remove();
          // 当前弹出窗口移除
          $(obj).parent().remove();
          // 同步管理和加入弹出框
          $(".addFolderUl li").each(function() {
            var val = $(this).find("input[type='checkbox']").val();
            if (folderId == val) {
              $(this).remove();
            }
          });
        },
        error : function(e) {
          // $.scmtips.show('error', 'error');
        }
      });
    }
  });
};

/**
 * 菜单选中更新.
 * 
 * @param menuId
 *          选中菜单元素的ID
 * @param url
 * @return
 */
ScmLeftMenu.switchContent = function(menuId, url, searchName, searchId) {
  $("#searchId").val(searchId);
  $('#leftMenuId').val(menuId);
  $("#searchName").val(searchName);
  $("#searchKey").val("");
  $("#pageNo").val(1);

  var folderUrl;
  if ($("#navAction").val() == 'groupPubs') {
    folderUrl = snsctx + "/group/pub";
  } else if ($("#navAction").val() == 'groupRefs') {
    folderUrl = ctxpath + "/group/ref";
  } else if ($("#navAction").val() == 'groupPrjs') {
    folderUrl = ctxpath + "/group/prj";
  } else if ($("#navAction").val() == 'groupFiles') {
    folderUrl = ctxpath + "/group/file";
  } else if ($("#navAction").val() == 'groupWorks') {
    folderUrl = ctxpath + "/group/work";
  } else if ($("#navAction").val() == 'groupCourses') {
    folderUrl = ctxpath + "/group/course";
  }

  $("#mainForm").attr("action", folderUrl);
  $("#mainForm").submit();
};

ScmLeftMenu.doFundingYear = function() {
  var years = $(".infolink_fundingYear");
  var currentIndex = this.leftMenuId == 'menu-mine' ? 1 : $("#" + this.leftMenuId).hasClass("infolink_fundingYear")
      ? $("#" + this.leftMenuId).index() + 1
      : 1;
  var currentPage = Math.floor(currentIndex / 5) + (currentIndex % 5 == 0 ? 0 : 1);
  $(".fundingYearIndex").val(currentPage);
};

ScmLeftMenu.fundingYearMove = function(go) {
  var years = $(".infolink_fundingYear");
  var count = Math.floor(years.length / 5);
  if (years.length % 5 > 0) {
    count++;
  }
  var currentFirst = Number($(".fundingYearIndex").val());
  // 由于发表年份为倒序
  if ("left" == go) {
    var leftCanClick = $("#left").attr("canClick");
    if (leftCanClick == "true") {// 前五年可用时
      currentFirst += 1;
    }
    $(".fundingYearIndex").val(currentFirst);
  }
  if ("right" == go) {
    var rightCanClick = $("#right").attr("canClick");
    if (rightCanClick == "true") {// 后五年五年可用时
      currentFirst -= 1;
    }
    $(".fundingYearIndex").val(currentFirst);
  }
  if (currentFirst <= 1) {
    $("#right").attr("class", "");
    $("#right").attr("canClick", "false");
    $("#left").attr("class", "canclick");
    $("#left").attr("canClick", "true");
    currentFirst = 1;
  } else if (currentFirst >= count) {
    $("#right").attr("class", "canclick");
    $("#right").attr("canClick", "true");
    $("#left").attr("class", "");
    $("#left").attr("canClick", "false");
    currentFirst = count;
  } else {
    $("#right").attr("class", "canclick");
    $("#right").attr("canClick", "true");
    $("#left").attr("class", "canclick");
    $("#left").attr("canClick", "true");
  }
  for (var i = 0; i < years.length; i++) {
    $(years[i]).hide();
  }
  for (var i = (currentFirst - 1) * 5; i < ((currentFirst - 1) * 5 + 5) && i < years.length; i++) {
    if ($(years[i]).length > 0) {
      $(years[i]).show();
    }
  }
};

/**
 * 群组标签个数初始化
 */
ScmLeftMenu.initTag = function(className, count) {
  var Tag = $("." + className);
  var length = Tag.length;
  var currentIndex = this.leftMenuId == 'menu-mine' ? -1 : $("#" + this.leftMenuId).hasClass(className) ? $(
      "#" + this.leftMenuId).index() : -1;
  if (currentIndex < count) {
    if (length == 0) {
      $("#tagMove").hide();
      return;
    }
    for (var i = 0; i < count; i++) {
      if (i == length)
        break;
      $(Tag[i]).show();
    }
  } else {
    var nextNum = currentIndex - currentIndex % count;
    for (var i = nextNum; i < (nextNum + count < length ? nextNum + count : length); i++) {
      $(Tag[i]).show();
    }
    var forwarName = $("#forward").attr("canClick", "ture").attr("class", "canclick");
    if (nextNum + count >= length) {
      var backName = $("#back").attr("canClick", "false").attr("class", "");
    }
  }
  if (length <= count) {
    $("#tagMove").hide();
  } else {
    $("#forward,#back").click(function() {
      ScmLeftMenu.forwardOrBack(this, className, count);
    });
  }
};
/**
 * 群组标签前后移动
 */
ScmLeftMenu.forwardOrBack = function(obj, className, count) {
  if (obj == null || count == null || className == null)
    return;
  var direction = $(obj).attr("id");
  var canClick = $(obj).attr("canClick");
  if (canClick == "false") {
    return;
  }
  var Tag = $("." + className);
  var length = Tag.length;
  var currentIndex = 0;
  Tag.each(function(i) {
    if ($(this).is(":visible")) {
      currentIndex = i;
      return false;
    };
  });
  if (direction == "forward") {
    Tag.hide();
    if (currentIndex < count) {
      for (var i = 0; i < count; i++) {
        $(Tag[i]).show();
      }
    } else {
      for (var i = currentIndex - 1; i >= currentIndex - count; i--) {
        $(Tag[i]).show();
      }
    }
  } else if (direction == "back") {
    Tag.hide();
    for (var i = currentIndex + count; i < currentIndex + 2 * count; i++) {
      if (i == length)
        break;
      $(Tag[i]).show();
    }
  }
  Tag.each(function(i) {
    if ($(this).is(":visible")) {
      currentIndex = i;
      return false;
    };
    if (i == length - 1) {
      currentIndex = length;
    }
  });
  if (currentIndex > 0) {
    var className = $("#forward").attr("canClick", "ture").attr("class", "canclick");
  } else {
    var className = $("#forward").attr("canClick", "false").attr("class", "");
  }
  if (currentIndex + count >= length) {
    var className = $("#back").attr("canClick", "false").attr("class", "");
  } else {
    var className = $("#back").attr("canClick", "ture").attr("class", "canclick");
  }
};