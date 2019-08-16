smate = smate ? smate : {};
smate.inspgcreate = smate.inspgcreate ? smate.inspgcreate : {};
/**
 * 进入创建群组主页的JS事件.
 */
smate.inspgcreate.create = function(psnId) {
    var actionUrl = '/inspg/inspgcreate/create';
    $('#mainForm').attr("action", actionUrl);
    $('#mainForm').submit();
};

/**
 * 提交保存创建群组主页的JS事件.
 */
smate.inspgcreate.savedata = function() {
    $("#submitButton").disabled();
    var insEmail = $.trim($("#insEmail").val());
    var insName = $.trim($("#insName").val());
    var inspgType = $(':radio[name="inspgType"]:checked').val();

    $.proceeding.show();
    $.ajax({
        url : '/inspg/inspgcreate/ajaxsaveregdata',
        type : 'post',
        dataType : 'json',
        data : {
            "insEmail" : insEmail,
            "insName" : insName,
            "inspgType" : inspgType
        },
        success : function(data) {
            $.proceeding.hide();
            /* $("#submitButton").enabled(); */
            if (data.ajaxSessionTimeOut == "yes") {
                jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                    if (r) {
                        var url = document.location.href;
                        url = url.replace('#', '');
                        document.location.href = url;
                    }
                });
            }
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
                if (data.forwardType == '1') {
                    var actionUrl = '/inspg/inspgsetting/setting?des3InspgId='
                            + data.inspgId;
                    setTimeout(function() {
                        window.location.href = actionUrl;
                    }, 2000);
                }
                if (data.forwardType == '0') {
                    smate.inspgcreate.forward(data);
                }
            }
            if (data.result == 'warn') {
                $.smate.scmtips.show('warn', data.msg);
                if (data.forwardType == '2' || data.forwardType == '0') {
                    smate.inspgcreate.forward(data);
                }
            }
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data.msg);
                $("#submitButton").enabled();
            }
        },
        error : function(data, status, e) {
            $.proceeding.hide();
        }
    });
};

smate.inspgcreate.forward = function(data) {
    // 根据邮件地址验证结果重定位页面_MJG_SCM-7089.
    var insEmail = $.trim($("#insEmail").val());
    var insName = encodeURI($.trim($("#insName").val()));
    var inspgType = $(':radio[name="inspgType"]:checked').val();
    var actionUrl = '/inspg/inspgcreate/createnotice?insEmail=' + insEmail
            + "&insName=" + insName + "&priorPsnName=" + data.priorPsnName
            + "&priorPsnId=" + data.priorPsnId + "&isExitFlag="
            + data.isExitFlag + "&inspgRegId=" + data.inspgRegId
            + "&des3InspgId=" + data.des3InspgId;
    // 重定位页面.
    setTimeout(function() {
        /*
         * $('#mainForm').attr("action",actionUrl); $('#mainForm').submit();
         */
        window.location.href = actionUrl;
    }, 2000);
}

/**
 * 请求后台发送创建机构主页邮件的JS事件.
 */
smate.inspgcreate.sendmail = function(inspgRegId) {
    $.ajax({
        url : '/inspg/inspgcreate/ajaxsendemail',
        type : 'post',
        dataType : 'json',
        data : {
            "inspgRegId" : inspgRegId
        },
        success : function(data) {
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
            } else {
                $.smate.scmtips.show('error', data.msg);
                return false;
            }
        },
        error : function(data, status, e) {
        }
    });
};

smate.inspgcreate.selectInspgType = function() {
    /*
     * var inspgType=$(':radio[name="inspgType"]:checked').val();
     * $(".inspgTypeNotice").hide(); $("#inspgType"+inspgType).show();
     */
    var inspgType = $(':radio[name="inspgType"]:checked').val();
    if (inspgType == 9) {
        $(".mtop5")
                .html(
                        '<i class="arr" style="top:-6px; left:120px;"></i>如果你不知道想要选择的机构类型，我们为你提供了自定义机构主页类型，你可以根据需要来自定义主页模块，主要包括动态、简介、成员等模块。');
    } else if (inspgType == 4) {
        $(".mtop5")
                .html(
                        '<i class="arr" style="top:-6px; left:75px;"></i>为你的联盟创建一个主页，让更多的人了解你的联盟。主要包括动态、简介、成员、新闻、成果等模块。');
    } else if (inspgType == 2) {
        $(".mtop5")
                .html(
                        '<i class="arr" style="top:-6px; left:30px;"></i>为你的公司创建一个主页，让更多的人了解你的公司。主要包括动态、简介、成果、成员等模块。');
    }
}

smate.inspgcreate.cancelreg = function(objid, resid, callback) {
    jConfirm("确认取消注册吗？", "提示", function(r) {
        if (r) {
            $.ajax({
                url : '/inspg/inspgcreate/cancelreg',
                type : 'post',
                dataType : 'json',
                data : {
                    "inspgRegId" : resid
                },
                success : function(data) {
                    if (data.ajaxSessionTimeOut == "yes") {
                        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                            if (r) {
                                var url = document.location.href;
                                url = url.replace('#', '');
                                document.location.href = url;
                            }
                        });
                    }
                    if (data.result == 'success') {
                        $.smate.scmtips.show('success', data.msg);
                        if (callback == 'refresh') {
                            window.location.reload();
                        } else if (typeof (callback) != "undefined") {
                            callback();
                        } else {
                            $("#regtr" + objid).hide();
                        }
                        return;
                    } else {
                        $.smate.scmtips.show('warn', data.msg);
                        return false;
                    }
                },
                error : function(data, status, e) {
                }
            });

        }
    });
}

smate.inspgcreate.cancelbt = function() {
    $(".regtr").each(function() {
        this.onmouseover = function() {
            var resid = $(this).attr("resid");
            $("#cancelreg" + resid).show();
            return;
        }
        this.onmouseout = function() {
            var resid = $(this).attr("resid");
            $("#cancelreg" + resid).hide();
            return;
        }
    });
}