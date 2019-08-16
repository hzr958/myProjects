smate = smate ? smate : {};
smate.inspgadmin = smate.inspgadmin ? smate.inspgadmin : {};

/**
 * 点击设置管理员.
 */
smate.inspgadmin.adminlist = function(psnId) {
    // TODO psnId填入表单
    var actionUrl = '/inspg/inspgadmin/adminlist';
    $('#mainForm').attr("action", actionUrl);
    $('#mainForm').submit();
};

/**
 * 点击加载选择联系人弹框
 */
smate.inspgadmin.loadAddAdmin = function() {
    // 判断 管理员的个数
    var adminSum = $("#adminSum").val();
    if (adminSum < 10) {
        $('#add_admin').attr("title", "选择联系人");
        $('#add_admin').click();
    } else {
        $.smate.scmtips.show('warn', "管理员的个数最多为10个");
    }
};

/**
 * 添加-管理员.
 */
smate.inspgadmin.addadmin = function(params) {
    // TODO 表单验证,验证成功,填充data,执行下面ajax
    $.ajax({
        url : '/inspg/inspgadmin/ajaxaddadmin',
        type : 'post',
        dataType : 'json',
        data : params,
        success : function(data) {
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
                $.Thickbox.closeWin();
                setTimeout(location.reload(), 1000);
            } else if (data.result == 'worm') {
                $.smate.scmtips.show('warn', data.msg);
            } else {
                if (data.ajaxSessionTimeOut == "yes") {
                    jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                        if (r) {
                            var url = document.location.href;
                            url = url.replace('#', '');
                            document.location.href = url;
                        }
                    });
                }
                $.Thickbox.closeWin();
                setTimeout(location.reload(), 1000);
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('error', '设置管理员失败');
            $.Thickbox.closeWin();
            setTimeout(location.reload(), 1000);
        }
    });
};

/**
 * 删除-管理员.
 */
smate.inspgadmin.deladmin = function() {
    // 获取选中数据
    var delAdminIds = smate.inspgadmin.getcheckinfo();
    if (delAdminIds == "") {
        $.smate.scmtips.show('warn', "请选择要删除的管理员");
        return;
    }
    var params = {
        "delAdminIds" : delAdminIds,
        "inspgIdStr" : $("#inspgIdStr").val(),
        "psnIdStr" : $("#psnIdStr").val()
    }
    $.ajax({
        url : '/inspg/inspgadmin/ajaxdeladmin',
        type : 'post',
        dataType : 'json',
        data : params,
        success : function(data) {
            if (data.result == 'success') {
                $.smate.scmtips.show('success', data.msg);
            } else {
                if (data.ajaxSessionTimeOut == "yes") {
                    jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                        if (r) {
                            var url = document.location.href;
                            url = url.replace('#', '');
                            document.location.href = url;
                        }
                    });
                }
                /* $.smate.scmtips.show('error',data.msg,350,10000); */
            }
            setTimeout(location.reload(), 1000);
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('error', "删除管理员出错");
            setTimeout(location.reload(), 1000);
        }
    });

};

smate.inspgadmin.getcheckinfo = function() {
    var str = document.getElementsByName("newsRecord");
    var objarray = str.length;
    var delAdminIds = "";
    for (i = 0; i < objarray; i++) {
        if (str[i].checked == true) {
            if (i == objarray - 1) {
                delAdminIds += str[i].value;
            } else {
                delAdminIds += (str[i].value + ",");
            }
        }
    }
    return delAdminIds;
};
