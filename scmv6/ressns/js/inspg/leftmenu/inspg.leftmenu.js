var ScmLeftMenu;
ScmLeftMenu = function(leftMenuId, editUrl, deleteUrl) {
    this.leftMenuId = leftMenuId;
    this.showMenu = ScmLeftMenu.showMenu;
    this.addMenu = ScmLeftMenu.addMenu;
    this.editMenu = ScmLeftMenu.editMenu;
    this.handlerEditMenu = ScmLeftMenu.handlerEditMenu;
    this.deleteMenu = ScmLeftMenu.deleteMenu;
    this.switchContent = ScmLeftMenu.switchContent;
    this.init = ScmLeftMenu.init;
    this.fundingYearMove = ScmLeftMenu.fundingYearMove;
    this.doFundingYear = ScmLeftMenu.doFundingYear;
    this.initTag = ScmLeftMenu.initTag;
    this.editUrl = editUrl;
    this.deleteUrl = deleteUrl;
};

/**
 * 初始.
 */
ScmLeftMenu.init = function() {
    $("#" + this.leftMenuId).find("a").attr("class", "leftnav-hover");// 初始选中项
    if (this.leftMenuId != "menu-mine") {
        $("#" + this.leftMenuId).parent().attr("class", "menu-expansion");
        $("#" + this.leftMenuId).parent().parent().find(".Shear-head").attr(
                "class", "Shear-headbottom");
    } else {
        $("#tag-dl").attr("class", "menu-expansion");
        $("#tag-dl").parent().find(".Shear-head").attr("class",
                "Shear-headbottom");
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
        $(".menu-expansion").each(
                function() {
                    $(this).attr("class", "menu-shrink");
                    $(this).parent().find(".Shear-headbottom").attr("class",
                            "Shear-head");
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
 *            添加请求url
 * @param fn回调函数
 * @return
 */
ScmLeftMenu.addMenu = function(obj, leftMenuParent, url, fn) {
    var isAddFolder = false;
    var folderName = $.trim($("#tag-input").val());
    var inspgId = $("#des3InspgId").val();
    var folderName2 = $.trim($("#tag-input-new").val());
    if (folderName.length == 0 && folderName2.length > 0) {
        folderName = folderName2;
        isAddFolder = true;
    } else {
        isAddFolder = false;
    }
    if (folderName.length == 0) {
        $.smate.scmtips.show("warn", inspgleftmenuinternational.tagNameReq);
    } else {
        $
                .ajax({
                    method : "post",
                    url : url,
                    data : {
                        "name" : folderName,
                        "des3InspgId" : inspgId
                    },
                    dataType : 'json',
                    success : function(data) {
                        if (data.result == "success") {
                            folderName = ScmMaint.HTMLEnCode(folderName);
                            var newMenuId = data.id;
                            var dd = $("#tagMove").prev("dd");
                            var display = (dd.length > 0 && !dd.is(":visible")) ? "display:none"
                                    : "display:block";
                            // 左侧菜单添加
                            $("#" + leftMenuParent)
                                    .find("#tagMove")
                                    .before(
                                            "<dd class=\"prjTag\" style=\""
                                                    + display
                                                    + "\" id=\"menu-dd-f"
                                                    + newMenuId
                                                    + "\"><a href=\"javascript:void(0)\" title='"
                                                    + folderName
                                                    + "' onclick=\" smate.pubinspg.changeTags('"
                                                    + newMenuId
                                                    + "','tag',this);\"><div class=\"two_nav_name\"><label>"
                                                    + folderName
                                                    + "</label></div><div class=\"Fright\">(0)</div><div class=\"clear\"></div></a></dd>");
                            // 弹出窗口添加
                            var newTagHtml = "";
                            if (!isAddFolder) {// 管理
                                newTagHtml = "<li><span class=\"lable-nr\"   id=\"menu-label"
                                        + newMenuId
                                        + "\" >"
                                        + folderName
                                        + "</span>"
                                        + "<input maxlength=\"20\" type=\"text\" onblur=\"scmLeftMenu.handlerEditMenu('"
                                        + newMenuId
                                        + "','"
                                        + scmLeftMenu.editUrl
                                        + "')\" type=\"text\" style=\"display:none;width: 130px; margin-left: 3px;\" class=\"inp_text Fleft menu-label\" itemId='"
                                        + newMenuId
                                        + "' id=\"menu-input"
                                        + newMenuId
                                        + "\" value=\""
                                        + folderName
                                        + "\"/>"
                                        + "<i class=\"icon_edit\" style=\"float:left;cursor:pointer;\"  onclick=\"scmLeftMenu.editMenu('"
                                        + newMenuId
                                        + "')\"></i>"
                                        + "<a href=\"javascript:void(0)\" class=\"pop-delete\" onclick=\"scmLeftMenu.deleteMenu(this,"
                                        + newMenuId
                                        + ",'"
                                        + scmLeftMenu.deleteUrl
                                        + "')\" ></a></li>";
                                ScmLeftMenu.syncThickBoxFolder(newMenuId,
                                        folderName, 1, scmLeftMenu.editUrl,
                                        scmLeftMenu.deleteUrl);
                            } else {// 加入
                                newTagHtml = "<li id='edittag_title_"
                                        + newMenuId
                                        + "'><span class=\"Fleft\"><input type=\"checkbox\" maxlength=\"20\" name=\"moveToFolders\"   value='"
                                        + newMenuId
                                        + "' ></span><span id='edittag_lable_"
                                        + newMenuId + "' class=\"lable-nr\">"
                                        + folderName + "</span></li>";
                                ScmLeftMenu.syncThickBoxFolder(newMenuId,
                                        folderName, 2, scmLeftMenu.editUrl,
                                        scmLeftMenu.deleteUrl);
                            }
                            $(obj).parent().parent().find(".label-list")
                                    .append(newTagHtml);
                            // 清空当前输入框
                            $("#tag-input").val("");
                            if (fn != null && typeof (fn) != "undefined")
                                fn(newMenuId);
                            $.smate.scmtips.show("success", data.msg);
                        } else if (data.result == "exist") {
                            $.smate.scmtips.show("warn", data.msg);
                            $("#tag-input").val("");
                        } else {
                            if (data.ajaxSessionTimeOut == 'yes') {
                                jConfirm(
                                        inspgleftmenuinternational.sessionTip2,
                                        inspgleftmenuinternational.prompt,
                                        function(r) {
                                            if (r) {
                                                top.location.reload(true);
                                                return;
                                            }
                                        });
                            } else {
                                $.smate.scmtips.show("error", data.msg);
                            }
                        }
                    },
                    error : function(xmlhttp, error, desc) {
                        $.smate.scmtips.show("error", "add folder error");
                    }
                });

    }
};

ScmLeftMenu.syncThickBoxFolder = function(id, name, flag, editUrl, deleteUrl) {
    // flag=1管理时同步到加入，flag=2加入时同步到管理
    if (flag == 1) {
        $(".addFolderUl")
                .append(
                        "<li id='edittag_title_"
                                + id
                                + "' title='"
                                + name
                                + "'><span class=\"Fleft\"><input maxlength=\"20\" type=\"checkbox\" name=\"folderName\" value='"
                                + id + "' ></span><span id='edittag_lable_"
                                + id + "' class=\"lable-nr\">" + name
                                + "</span></li>");
    } else {
        var newTagHtml = "<li>" + "<span class=\"lable-nr\" id=\"menu-label"
                + id
                + "\" >"
                + name
                + "</span>"
                + "<input type=\"text\" onblur=\"scmLeftMenu.handlerEditMenu('"
                + id
                + "','"
                + editUrl
                + "')\" type=\"text\" style=\"display:none;width: 130px; margin-left: 3px;\" class=\"inp_text Fleft\" id=\"menu-input"
                + id
                + "\" value=\""
                + name
                + "\"/>"
                + "<i class=\"icon_edit\" style=\"float:left;cursor:pointer;\"  onclick=\"scmLeftMenu.editMenu("
                + id
                + ")\"></i>"
                + "<a href=\"javascript:void(0)\" title='"
                + name
                + "(0)' class=\"pop-delete\" onclick=\"scmLeftMenu.deleteMenu(this,"
                + id + ",'" + deleteUrl + "')\" ></a></li>";
        $(".manageFolderUl").append(newTagHtml);
    }
};

/**
 * 切换至菜单编辑.
 * 
 * @param menuId
 *            菜单的ID
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
 *            菜单ID
 * @param url
 *            编辑请求url
 * @param fn
 *            回调函数.
 * @return
 */
ScmLeftMenu.handlerEditMenu = function(menuId, url) {
    var spanText = $.trim($("#menu-label" + menuId).text());
    var inputText = $.trim($("#menu-input" + menuId).val());
    var des3InspgId = $("#des3InspgId").val();
    if (inputText.length == 0) {
        $.smate.scmtips.show("warn", inspgleftmenuinternational.tagNameReq);
        return;
    }
    if (spanText != inputText) {
        $.ajax({
            method : "post",
            url : url,
            data : {
                "name" : inputText,
                "id" : menuId,
                "des3InspgId" : des3InspgId
            },
            dataType : "json",
            success : function(data) {
                if (data.result == "exist") {
                    $.smate.scmtips.show("warn", data.msg);
                    $("#menu-input" + menuId).val(spanText);
                    $("#menu-label" + menuId).show();
                    $("#menu-input" + menuId).hide();
                } else if (data.result == "success") {
                    inputText = ScmMaint.HTMLEnCode(inputText);
                    // 更新左侧菜单
                    var old_title = $("#menu-dd-f" + menuId).find("a").attr(
                            "title");
                    var old_title_count = old_title.substr(old_title
                            .indexOf("("), old_title.length);
                    $("#menu-dd-f" + menuId).find("a").attr("title",
                            inputText + old_title_count);
                    $("#menu-dd-f" + menuId).find("label").html(inputText);
                    // 更新编辑标签
                    $("#edittag_title_" + menuId).attr("title", inputText);
                    $("#edittag_lable_" + menuId).html(inputText);
                    // 更新当前弹出窗口
                    $("#menu-label" + menuId).html(inputText);
                    $("#menu-labelr" + menuId).html(inputText);
                    // 切换编辑
                    $("#menu-label" + menuId).show();
                    $("#menu-input" + menuId).hide();
                    $.smate.scmtips.show("success", data.msg);
                } else {
                    if (data.ajaxSessionTimeOut == 'yes') {
                        jConfirm(inspgleftmenuinternational.sessionTip2,
                                inspgleftmenuinternational.prompt, function(r) {
                                    if (r) {
                                        top.location.reload(true);
                                        return;
                                    }
                                });
                    } else {
                        $.smate.scmtips.show("error", data.msg);
                    }
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
 *            菜单ID
 * @param url
 *            菜单删除请求url
 * @param fn
 *            回调函数.
 * @return
 */
ScmLeftMenu.deleteMenu = function(obj, folderId, url, fn) {
    jConfirm(
            inspgleftmenuinternational.delTagCfm,
            inspgleftmenuinternational.prompt,
            function(r) {
                if (r) {
                    var des3InspgId = $("#des3InspgId").val();
                    $
                            .ajax({
                                method : "post",
                                url : url,
                                data : {
                                    "id" : folderId,
                                    "des3InspgId" : des3InspgId
                                },
                                dataType : "json",
                                success : function(data) {
                                    if (data.result == "success") {
                                        // 左侧菜单移除
                                        $("#menu-dd-f" + folderId).remove();
                                        // 当前弹出窗口移除
                                        $(obj).parent().remove();
                                        // 同步管理和加入弹出框
                                        $(".addFolderUl li")
                                                .each(
                                                        function() {
                                                            var val = $(this)
                                                                    .find(
                                                                            "input[type='checkbox']")
                                                                    .val();
                                                            if (folderId == val) {
                                                                $(this)
                                                                        .remove();
                                                            }
                                                        });
                                        $.smate.scmtips.show("success",
                                                data.msg);
                                    } else {
                                        if (data.ajaxSessionTimeOut == 'yes') {
                                            jConfirm(
                                                    inspgleftmenuinternational.sessionTip2,
                                                    inspgleftmenuinternational.prompt,
                                                    function(r) {
                                                        if (r) {
                                                            top.location
                                                                    .reload(true);
                                                            return;
                                                        }
                                                    });
                                        } else {
                                            $.smate.scmtips.show("error",
                                                    data.msg);
                                        }
                                    }

                                },
                                error : function(e) {
                                    // $.smate.scmtips.show('error', 'error');
                                }
                            });
                }
            });
};

/**
 * 菜单选中更新.
 * 
 * @param menuId
 *            选中菜单元素的ID
 * @param url
 * @return
 */
ScmLeftMenu.switchContent = function(menuId, url, searchName, searchId) {
    if (menuId != this.leftMenuId) {
        // $("#searchId").val(searchId);
        $('#leftMenuId').val(menuId);
        // $("#searchName").val(searchName);
        // $("#searchKey").val("");
        $("#pageNo").val(1);
        if (url != null) {
            $("#mainForm").attr("action", url);
        }
        $("#mainForm").submit();
    }
};

ScmLeftMenu.doFundingYear = function() {
    var years = $(".infolink_fundingYear");
    var currentIndex = this.leftMenuId == 'menu-mine' ? 1 : $(
            "#" + this.leftMenuId).hasClass("infolink_fundingYear") ? $(
            "#" + this.leftMenuId).index() + 1 : 1;
    var currentPage = Math.floor(currentIndex / 5)
            + (currentIndex % 5 == 0 ? 0 : 1);
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
    for (var i = (currentFirst - 1) * 5; i < ((currentFirst - 1) * 5 + 5)
            && i < years.length; i++) {
        if ($(years[i]).length > 0) {
            $(years[i]).show();
        }
    }
};

/**
 * 项目标签个数初始化
 * 
 */
ScmLeftMenu.initTag = function(className, count) {
    var Tag = $("." + className);
    var length = Tag.length;
    var currentIndex = this.leftMenuId == 'menu-mine' ? -1 : $(
            "#" + this.leftMenuId).hasClass(className) ? $(
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
        for (var i = nextNum; i < (nextNum + count < length ? nextNum + count
                : length); i++) {
            $(Tag[i]).show();
        }
        var forwarName = $("#forward").attr("canClick", "ture").attr("class",
                "canclick");
        if (nextNum + count >= length) {
            var backName = $("#back").attr("canClick", "false").attr("class",
                    "");
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
 * 项目标签前后移动
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
        }
        ;
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
        }
        ;
        if (i == length - 1) {
            currentIndex = length;
        }
    });
    if (currentIndex > 0) {
        var className = $("#forward").attr("canClick", "ture").attr("class",
                "canclick");
    } else {
        var className = $("#forward").attr("canClick", "false").attr("class",
                "");
    }
    if (currentIndex + count >= length) {
        var className = $("#back").attr("canClick", "false").attr("class", "");
    } else {
        var className = $("#back").attr("canClick", "ture").attr("class",
                "canclick");
    }
};