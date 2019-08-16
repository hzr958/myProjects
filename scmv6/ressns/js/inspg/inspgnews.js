var smate = smate ? smate : {};
smate.inspgnews = smate.inspgnews ? smate.inspgnews : {};

/**
 * 机构主页-检索新闻列表.
 */
smate.inspgnews.search = function() {
    $('#leftMenuId').val("");
    var actionUrl = '/inspg/inspgnews/showinspgnews?des3InspgId='
            + $("#des3InspgId").val();
    $('#mainForm').attr("action", actionUrl);
    // 如果为水印文字，查询条件为空
    if ($("#searchName").css("color") == "rgb(153, 153, 153)") {
        $("#searchName").val("");
    }
    $('#mainForm').submit();
};

/**
 * 机构主页-管理资质类别.
 */
smate.inspgnews.managesort = function() {
    // @TODO 进入管理资质类别弹出框.
};

/**
 * 机构主页-保存修改管理资质类别.
 */
smate.inspgnews.savesort = function() {
    // @TODO 调用后台保存修改的管理资质类别.
};

/**
 * 机构主页-进入新增检索新闻页面.
 */
smate.inspgnews.add = function() {
    var actionUrl = '/inspg/inspgnews/addinspgnews';
    $('#mainForm').attr("action", actionUrl);
    $('#mainForm').submit();
};

/**
 * 机构主页-进入编辑检索新闻页面.
 */
smate.inspgnews.edit = function() {
    var newsIds = smate.inspgnews.getCheckedNews();
    if (newsIds.length == 0) {
        $.smate.scmtips.show('warn', "请选择要编辑的新闻");
        return false;
    }
    newsIds = newsIds.substr(0, newsIds.length - 1);
    var indx = newsIds.indexOf(",");
    if (indx >= 0) {
        $.smate.scmtips.show('warn', "一次只能编辑一条新闻");
        return false;
    }
    var actionUrl = '/inspg/inspgnews/editinspgnews?newsId=' + newsIds;
    $('#mainForm').attr("action", actionUrl);
    $('#mainForm').submit();
};

/**
 * 机构主页-新增编辑新闻-请求后台保存新闻记录.
 */
smate.inspgnews.save = function() {

    var detail = CKEDITOR.instances.edit1.getData();// 获取ckeditor编辑面板所有源数据
    // 验证机构主页简介内容是否超过10000个字符.
    if (detail.length > 10002) {
        var a = detail;
        if (CKEDITOR.env.gecko) {
            a = a.replace(
                    /(<\!--\[if[^<]*?\])\>([\S\s]*?)<\!(\[endif\]--\>)/gi, "");
        }
        if (CKEDITOR.env.webkit) {
            a = a
                    .replace(
                            /(class="MsoListParagraph[^>]+><\!--\[if !supportLists\]\>)([^<]+<span[^<]+<\/span>)(<\!\[endif\]--\>)/gi,
                            "");
        }
        a = a.replace(/cke:.*?".*?"/g, "");
        a = a.replace(/style=""/g, "");
        a = a.replace(/<span>/g, "");
        a = a.replace(/&nbsp;/g, "");
        a = a.replace(/<(?:.|\s)*?>/g, "");
        a = a.replace(/[ ]*/g, "");
        a = a.replace(/[\n\f\r\t\v]/g, "");
        a = $.trim(a);
        if (a.length > 10000) {
            $.smate.scmtips.show('warn', "新闻详情文字过长,不能超过10000字符");
            $("#saveButton").enabled();
            return;
        }
    }
    // 栏目不能为空
    var newsTypes = smate.inspgnews.getCheckedtypes();
    if (newsTypes == 0 || newsTypes == "") {
        $.smate.scmtips.show('warn', "请选择栏目");
        $("#saveButton").enabled();
        return;
    }
    // 新闻详情不能为空
    if (detail.length == 0 || detail == "") {
        $.smate.scmtips.show('warn', "请输入新闻详情");
        $("#saveButton").enabled();
        return;
    }
    // 文件列表保存
    var fileListJson = '';
    var fileDoms = $("#mood_File").children();

    fileListJson += "[";
    for (var i = 0; fileDoms.length > i; i++) {
        fileListJson += "{\"url\":\""
                + $(fileDoms[i]).find('.news_file_path').val()
                + "\",\"fileId\":"
                + $(fileDoms[i]).find('.news_file_fileId').val()
                + ",\"name\":\"" + $(fileDoms[i]).find('.news_file_name').val()
                + "\"},";
    }
    if (fileListJson.substring(fileListJson.length - 1, fileListJson.length) == ",") {
        fileListJson = fileListJson.substring(0, fileListJson.length - 1);
    }
    fileListJson += "]";
    var datajson = {
        "inspgNews.id" : $("#newsId").val(),
        "inspgNews.title" : $("#title").val(),
        "inspgNews.fromUrl" : $("#fromUrl").val(),
        "inspgNews.detail.detail" : detail,
        "inspgNews.type" : 0,
        "saveType" : $("#saveType").val(),
        "psnType" : $("#psnType").val(),
        "des3InspgId" : $("#des3InspgId").val(),
        "newsTypes" : newsTypes,// 获取新闻类别.
        "fileListJson" : fileListJson
    };

    $.proceeding.show();
    $
            .ajax({
                url : '/inspg/inspgnews/ajaxsaveinspgnews',
                type : 'post',
                dataType : 'json',
                data : datajson,
                success : function(data) {
                    if (data.ajaxSessionTimeOut == 'yes') {
                        if (jConfirm) {
                            // 超时
                            jConfirm(
                                    "系统超时或未登录，你要登录吗？",
                                    "提示",
                                    function(r) {
                                        if (r) {
                                            document.location.href = "/inspg/login?service="
                                                    + window.location.href
                                                    + "?des3InspgId="
                                                    + $("#des3InspgId").val();
                                        }
                                    });
                        }
                    }
                    if (data.result == 'success') {
                        $.smate.scmtips.show('success', "保存成功");
                        // 重定位页面.
                        setTimeout(
                                function() {
                                    window.location.href = '/inspg/inspgnews/showinspgnews?des3InspgId='
                                            + $("#des3InspgId").val();
                                }, 2000);
                    } else if (data.result == 'warn') {
                        $.smate.scmtips.show('warn', "保存失败");
                        // $("#saveButton").enabled();
                        $("#saveButton").removeAttr("readonly");
                    }
                },
                error : function(data, status, e) {
                    $.smate.scmtips.show('warn', "保存失败");
                    // $("#saveButton").enabled();
                    $("#saveButton").removeAttr("readonly");
                }
            });
    $.proceeding.hide();
};

/**
 * 机构主页-取消保存.
 */
smate.inspgnews.cancel = function() {
    window.history.back();
};

/**
 * 机构主页-取消编辑.
 */
smate.inspgnews.cancelEdit = function() {
    document.location.href = "/inspg/inspgnews/showinspgnews?des3InspgId="
            + $("#des3InspgId").val();
};

/**
 * 机构主页-新闻列表-请求后台删除新闻.
 */
smate.inspgnews.remove = function() {
    var checkedNews = smate.inspgnews.getCheckedNews();
    if (checkedNews.length == 0) {
        $.smate.scmtips.show('warn', "请选择要删除的新闻");
        return false;
    }
    $.proceeding.show();
    $.ajax({
        url : '/inspg/inspgnews/ajaxdeleteinspgnews',
        type : 'post',
        dataType : 'json',
        data : {
            "newsIds" : checkedNews,
            "des3InspgId" : $("#des3InspgId").val()
        },
        success : function(data) {
            $.proceeding.hide();
            if (data.ajaxSessionTimeOut == 'yes') {
                if (jConfirm) {
                    // 超时
                    jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                        if (r) {
                            document.location.href = "/inspg/login?service="
                                    + window.location.href;
                        }
                    });
                    return false;
                }
            }
            if (data.result == 'success') {
                $.smate.scmtips.show('success', "删除成功");
                // 重定位页面.
                setTimeout(function() {
                    smate.inspgnews.search();
                }, 3000);
            } else {
                $.smate.scmtips.show('warn', "删除失败", 350, 10000);
                return false;
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('warn', "删除失败", 350, 10000);
            $.proceeding.hide();
        }
    });
};

/**
 * 机构主页-发布新闻到其他主页-显示弹出框.
 */
smate.inspgnews.showpublish = function() {
    var checkedNews = smate.inspgnews.getCheckedNews();
    if (checkedNews.length == 0) {
        $.smate.scmtips.show('warn', "请选择要发布到其他主页的新闻");
        return false;
    }
    $("#publishNews_but").attr(
            'alt',
            "/inspg/inspgnews/showshareinspgnews?newsIds=" + checkedNews
                    + "&des3InspgId=" + $("#des3InspgId").val()
                    + "TB_iframe=true&height=380");
    $("#publishNews_but").click();
};

/**
 * 超时响应事件.
 */
smate.inspgnews.timeout = function() {
    // 超时
    jConfirm(i18n_timeout, i18n_tipTitle, function(r) {
        if (r) {
            var url = document.location.href;
            url = url.replace('#', '');
            document.location.href = url;
        }
    });
};

/**
 * 机构主页-新闻列表-获取选中的新闻记录.
 */
smate.inspgnews.getCheckedNews = function() {
    var checkedNews = "";
    var checkboxs = $(":checkbox[name=newsRecord]");
    checkboxs.each(function() {
        if ($(this).attr("checked")) {// 如果复选框被选中.
            checkedNews += $(this).val() + ",";
        }
    });
    return checkedNews;
};

/**
 * 机构主页-新增编辑-获取选中的新闻类别.
 */
smate.inspgnews.getCheckedtypes = function() {
    var icheckedsorts = '';// 机构主页对应选中的新闻类别.
    var checkboxs = $(".newsType");

    checkboxs.each(function() {
        if ($(this).attr("checked")) {// 如果复选框被选中.
            var id = $(this).val();
            icheckedsorts = icheckedsorts + id + ",";
        }
    });
    // 删除最后一个字符.
    if (icheckedsorts != '' && icheckedsorts.length > 1) {
        icheckedsorts = icheckedsorts.substr(0, icheckedsorts.length - 1);
    }
    return icheckedsorts;
};

/**
 * 机构主页-发布新闻到其他主页-获取选中的新闻类别栏目.
 */
smate.inspgnews.calnewstype = function() {
    var newstypes = [];
    var publishnews = $(".inspg_tr");

    publishnews.each(function() {
        var id = $(this).attr("id");// tr标签的id属性--机构主页ID.
        var icheckedsorts = '';// 机构主页对应选中的新闻类别.

        var isorts = $(".inspg_sort_" + id).find(".newsType");// tr标签内class属性为newsType的check子标签--新闻主页对应的新闻类别栏目.
        isorts.each(function(index, _this) {
            if ($(_this).is(":checked")) {// 如果复选框被选中.
                var sortid = $(_this).val();
                icheckedsorts = icheckedsorts + sortid + ",";
            }
        });
        // 删除最后一个字符.
        if (icheckedsorts != '' && icheckedsorts.length > 1) {
            icheckedsorts = icheckedsorts.substr(0, icheckedsorts.length - 1);
        }
        if (icheckedsorts != '') {
            newstypes.push('{"inspgId":' + id + ',"sortIds":\"' + icheckedsorts
                    + '\"}');
        }

    });

    return newstypes;
};

/**
 * 机构主页-发布新闻到其他主页-请求后台保存发布结果.
 */
smate.inspgnews.savepublish = function(obj) {
    $(obj).disabled();
    var newsIds = $("#newsIds").val();
    var newsTypes = smate.inspgnews.calnewstype();
    $.ajax({
        url : '/inspg/inspgnews/ajaxshareinspgnews',
        type : 'post',
        dataType : 'json',
        data : {
            "newsIds" : newsIds,
            "inspgPublishJson" : newsTypes.join(";"),
            "des3InspgId" : parent.$("#des3InspgId").val()
        },
        success : function(data) {
            if (data.result == 'success') {
                parent.$.smate.scmtips.show('success', "发布成功");
                // 重定位页面.
                setTimeout(function() {
                    parent.$.Thickbox.closeWin();
                    parent.smate.inspgnews.search();
                }, 3000);
            } else {
                $(obj).enabled();
                parent.$.smate.scmtips.show('warn', data.msg, 350, 10000);
                return false;
            }
        },
        error : function(data, status, e) {
            $(obj).enabled();
            parent.$.smate.scmtips.show('warn', data.msg, 350, 10000);
        }
    });
};

/**
 * 文件改变事件绑定
 */
smate.inspgnews.fileChangeFn = function() {
    $("input[id=filedata1]").change(
            function() {
                $('#fileNameShow').val(
                        $(this).val().substr(
                                $(this).val().replace(/\\/g, '/').lastIndexOf(
                                        '/') + 1));
                if ($(this).val().length > 0) {
                    $('.filedata1').val(
                            $(this).val().substr(
                                    $(this).val().replace(/\\/g, '/')
                                            .lastIndexOf('/') + 1));
                }
            });
};

/**
 * 上传事件
 */
smate.inspgnews.confirmAttachUpload = function() {
    $('#loading').show();
    $.ajaxFileUpload({
        url : snsctx + "/archiveFiles/ajaxSimpleUpload.action",
        secureuri : false,
        fileElementId : 'filedata1',
        data : {
            // allowType :
            // '*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;',
            'fileDesc' : ''
        },
        dataType : 'json',
        success : function(data, status) {
            $('#loading1').hide();
            if (data.result == 'error') {
                $.smate.scmtips.show("warn", data.msg);
                smate.inspgnews.fileChangeFn()
                $("#btnFileUpload").attr("onclick",
                        "smate.inspgnews.showAttachFileName();");
            } else {
                $.smate.scmtips.show("success", "上传成功.");
                smate.inspgnews.fileChangeFn();
                smate.inspgnews.addSuccess(data);
                $("#btnFileUpload").attr("onclick",
                        "smate.inspgnews.showAttachFileName();");
                // 附件上传水印提示
                smate.inspgnews.fileNameWatermark();
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show("warn", "上传失败.");
            smate.inspgnews.fileChangeFn()
            $("#btnFileUpload").attr("onclick",
                    "smate.inspgnews.showAttachFileName();");
        }
    });
};

/**
 * 附件上传水印提示
 */
smate.inspgnews.fileNameWatermark = function() {
    var uploadFileText = "文件大小不能超过30M";
    $("#fileNameShow").watermark({
        tipCon : uploadFileText
    });
}

/**
 * 事件检查
 */
smate.inspgnews.showAttachFileName = function() {
    $("#btnFileUpload").removeAttr("onclick");
    if (!smate.inspgnews.checkFileNum()) {
        $('#fileNameShow').val("");
        $.smate.scmtips.show("warn", "最多可上传5份附件.");
        $("#btnFileUpload").attr("onclick",
                "smate.inspgnews.showAttachFileName();");
        return;
    } else {

    }
    var filedata = $('#filedata1').val();
    if (filedata == '') {
        $("#btnFileUpload").attr("onclick",
                "smate.inspgnews.showAttachFileName();");
        return;
    } else {
        var fileName = filedata.substring(filedata.replace(/\\/g, '/')
                .lastIndexOf('/') + 1);
        $('#file_attach').attr('value', fileName);
        smate.inspgnews.confirmAttachUpload();
    }
};

/**
 * 上传成功后处理显示
 */
smate.inspgnews.addSuccess = function(data) {
    $('#loading').hide();
    $('#filedata1').val('');
    $('#fileNameShow').val('');
    var moodFileTemp = $("#mood_File_temp").clone();
    moodFileTemp.addClass("add_Item");
    moodFileTemp.attr("id", "mood_File_add" + data.fileId);
    moodFileTemp.find("input.news_file_name").val(data.fileName);
    moodFileTemp.find("input.news_file_path").val(data.path);
    moodFileTemp.find("input.news_file_fileId").val(data.fileId);

    var tempName = "";
    if (data.fileName.length > 24) {
        tempName = '<a href="/scmwebsns/archiveFiles/fileDownload.action?fdesId='
                + data.des3FileId
                + '"  >'
                + data.fileName.substring(0, 24)
                + "..." + '</a>';
    } else {
        tempName = "<a href='/scmwebsns/archiveFiles/fileDownload.action?fdesId="
                + data.des3FileId + "'>" + data.fileName + "</a>";

    }
    moodFileTemp.find("p.mood_File_detail").html(tempName);
    $("#mood_File").append(moodFileTemp);
    $("#mood_File").show();
    moodFileTemp.show();
};

/**
 * 检查附件个数
 */
smate.inspgnews.checkFileNum = function() {
    return $('#mood_File').children().length > 4 ? false : true;
};

/**
 * 删除附件
 */
smate.inspgnews.deleteFile = function(obj) {
    jConfirm("确定移除附件?", "提示", function(r) {
        if (r) {
            var addItemParentObj = $(obj).parent().parent();
            var addItemObj = addItemParentObj.find("div.add_Item");
            $(obj).parent().remove();
        }
    });
};
/**
 * 发表评论
 */
smate.inspgnews.savecomment = function(type) {
    var commnetContent = $.trim($("#comment_content").text());
    if (commnetContent == "" || $("#comment_content").hasClass("watermark")) {
        $.smate.scmtips.show("warn", "请输入评论内容");
        return false;
    }
    var parentId = $("#parentId").val();
    common.comment.submit(type, parentId, commnetContent);
    $("#comment_content_countLabel").html(0);
}

/**
 * 新闻单选点击
 * 
 * @param obj
 * @return
 */
smate.inspgnews.checkNews = function(obj) {
    var flag = true;
    if (obj.checked) {
        var cks = $(":checkbox[name='newsRecord']");
        for (var i = 0; i < cks.length; i++) {
            // 没有全部选择
            if (!cks[i].checked) {
                flag = false;
                break;
            }
        }
    } else {
        flag = false;
    }
    $(":checkbox[name='newsAll']").attr("checked", flag);
};
