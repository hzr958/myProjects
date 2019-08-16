var smate = smate ? smate : {};
smate.inspgprofile = smate.inspgprofile ? smate.inspgprofile : {};

/**
 * 保存机构主页简介的JS事件.
 */
smate.inspgprofile.saveprofile = function() {
    var psnType = $.trim($("#psnType").val());
    var profile = CKEDITOR.instances.edit1.getData();// 获取ckeditor编辑面板所有源数据
    // 验证机构主页简介内容是否超过10000个字符.
    if (profile.length > 10000) {
        var a = smate.inspgprofile.profileCountCheck(profile);
        if (a.length > 10000) {
            $.smate.scmtips.show('warn', $.format("机构简介不能超过10000字符.", 10000));
            return;
        }
    }
    if ($.trim(profile) == "") {
        CKEDITOR.instances.edit1.setData("");
    }
    var post_data = {
        'des3InspgId' : $.trim($("#des3InspgId").val()),
        'psnType' : psnType,
        'profile' : profile
    };
    $.ajax({
        url : '/inspg/inspgprofile/ajaxsaveprofile',
        type : 'post',
        dataType : 'json',
        data : post_data,
        success : function(data) {
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
                setTimeout(function() {
                    window.location.reload();
                }, 2000);
            } else {
                if (data.ajaxSessionTimeOut == 'yes') {
                    if (jConfirm) {
                        // 超时
                        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                            if (r) {
                                var url = document.location.href;
                                url = url.replace('#', '');
                                document.location.href = url;
                            }
                        });
                    }
                }
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('error', data.msg, 350, 10000);
            return false;
        }
    });
};

/**
 * 
 */
smate.inspgprofile.profileCountCheck = function(profile) {
    var a = profile;
    if (CKEDITOR.env.gecko) {
        a = a
                .replace(/(<\!--\[if[^<]*?\])\>([\S\s]*?)<\!(\[endif\]--\>)/gi,
                        "");
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
    a = a.replace(/&ldquo;/g, "");
    a = a.replace(/	&rdquo;/g, "");

    a = $.trim(a);
    return a;
}

smate.inspgprofile.editInspgUrl = function() {

}

/**
 * 取消保存机构主页简介的JS事件.
 */
smate.inspgprofile.cancel = function() {
    $("#description_edit").hide();
    $("#descriptionView").show();
};

smate.inspgprofile.edit = function() {
    $("#descriptionView").hide();
    $("#description_edit").show();
    smate.inspgprofile.changeProfileCount();
}

/**
 * 主页地址点击编辑
 */
smate.inspgprofile.homepageedit = function() {
    $("#homepageurlshow").hide();
    $("#homepageurledit").show();
}
/**
 * 主页地址编辑取消
 */
smate.inspgprofile.homepagecancel = function() {
    $("#homepageurledit").hide();
    $("#homepageurlshow").show();
}
/**
 * 主页地址编辑保存
 */
smate.inspgprofile.homepagesave = function() {
    var inspgUrl = $("#inspgUrl").val();
    if (inspgUrl.length > 20 || inspgUrl.length < 8) {
        $.smate.scmtips.show('warn', "主页地址不能超过20位,不少于8位");
        return;
    }
    var post_data = {
        'des3InspgId' : $.trim($("#des3InspgId").val()),
        'inspgUrl' : inspgUrl
    };
    $.ajax({
        url : '/inspg/inspgsetting/ajaxsaveinspgurl',
        type : 'post',
        dataType : 'json',
        data : post_data,
        success : function(data) {
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
                setTimeout(function() {
                    window.location.reload();
                }, 2000);
            } else if (data.result == 'error') {
                $.smate.scmtips.show('warn', data.msg);
            } else {
                if (data.ajaxSessionTimeOut == 'yes') {
                    if (jConfirm) {
                        // 超时
                        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                            if (r) {
                                var url = document.location.href;
                                url = url.replace('#', '');
                                document.location.href = url;
                            }
                        });
                    }
                }
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('warn', "保存失败");
        }
    });
}

smate.inspgprofile.changeProfileCount = function() {
    // 更新统计数据
    var profile = CKEDITOR.instances.edit1.getData();
    var a = smate.inspgprofile.profileCountCheck(profile);
    if (a.length > 10000) {
        $("#profileCount").attr("class", "red");
    } else {
        $("#profileCount").attr("class", "");
    }
    $("#profileCount").text(a.length);
}
