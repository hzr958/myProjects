var smate = smate ? smate : {};
smate.inspgstatistics = smate.inspgstatistics ? smate.inspgstatistics : {};

/**
 * 发现机构主页-标注关注.
 */
smate.inspgstatistics.follow = function(inspgId, callback) {
    $.proceeding.show();
    $.ajax({
        url : '/inspg/inspgstatistics/ajaxfollow',
        type : 'post',
        dataType : 'json',
        data : {
            "inspgId" : inspgId
        },
        success : function(data) {
            $.proceeding.hide();
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
                if (typeof (callback) != "undefined") {
                    callback(inspgId);
                    var sum = $("#sum_" + inspgId).text();
                    if (sum == "" || sum == null) {
                        sum = 0;
                    }
                    $("#sum_" + inspgId).text(parseInt(sum) + 1);
                    $("#sum_" + inspgId).parent().show();
                    // window.location.reload();//SCM-8798
                } else {
                    setTimeout(function() {
                        window.location.reload();
                    }, 2000);
                }
            } else {
                if (data.ajaxSessionTimeOut == "yes") {
                    jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                        if (r) {
                            document.location.href = "/inspg/login?service="
                                    + window.location.href;
                        }
                    });
                }
                return false;
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('error', data.msg);
            $.proceeding.hide();
        }
    });
};

smate.inspgstatistics.hide = function(inspgId) {
    var obj = $("#follow_" + inspgId);
    if (typeof (obj) != "undefined") {
        $("#follow_" + inspgId).hide();
    }
}

smate.inspgstatistics.cancelfollow = function(inspgId) {
    $.proceeding.show();
    $.ajax({
        url : '/inspg/inspgstatistics/ajaxunfollow',
        type : 'post',
        dataType : 'json',
        data : {
            "inspgId" : inspgId
        },
        success : function(data) {
            $.proceeding.hide();
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
                setTimeout(function() {
                    window.location.reload();
                }, 2000);
            } else {
                if (data.ajaxSessionTimeOut == "yes") {
                    jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                        if (r) {
                            var url = document.location.href;
                            if (url.indexOf("/inspg/page/") > -1) {
                                url = document.domain + "/inspg/inspgmain";
                            } else {
                                url = url.replace('#', '');
                            }
                            document.location.href = url;
                        }
                    });
                }
                return false;
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('error', data.msg);
            $.proceeding.hide();
        }
    });
}
